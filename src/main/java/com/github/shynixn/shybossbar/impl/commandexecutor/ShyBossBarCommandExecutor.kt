package com.github.shynixn.shybossbar.impl.commandexecutor

import com.github.shynixn.mccoroutine.folia.globalRegionDispatcher
import com.github.shynixn.mccoroutine.folia.launch
import com.github.shynixn.mcutils.common.CoroutineExecutor
import com.github.shynixn.mcutils.common.chat.ChatMessageService
import com.github.shynixn.mcutils.common.command.CommandBuilder
import com.github.shynixn.mcutils.common.command.Validator
import com.github.shynixn.mcutils.common.language.reloadTranslation
import com.github.shynixn.mcutils.common.language.sendPluginMessage
import com.github.shynixn.mcutils.common.repository.CacheRepository
import com.github.shynixn.shybossbar.contract.BossBarService
import com.github.shynixn.shybossbar.contract.ShyBossBarLanguage
import com.github.shynixn.shybossbar.entity.ShyBossBarMeta
import com.github.shynixn.shybossbar.entity.ShyBossBarSettings
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import java.util.*

class ShyBossBarCommandExecutor(
    private val settings: ShyBossBarSettings,
    private val plugin: Plugin,
    private val bossBarService: BossBarService,
    private val language: ShyBossBarLanguage,
    chatMessageService: ChatMessageService,
    private val repository: CacheRepository<ShyBossBarMeta>,
) {

    private val coroutineExecutor = object : CoroutineExecutor {
        override fun execute(f: suspend () -> Unit) {
            plugin.launch(plugin.globalRegionDispatcher) {
                f.invoke()
            }
        }
    }

    private val senderHasToBePlayer: () -> String = {
        language.commandSenderHasToBePlayer.text
    }

    private val playerMustExist = object : Validator<Player> {
        override suspend fun transform(
            sender: CommandSender, prevArgs: List<Any>, openArgs: List<String>
        ): Player? {
            try {
                val playerId = openArgs[0]
                val player = Bukkit.getPlayer(playerId)

                if (player != null) {
                    return player
                }
                return Bukkit.getPlayer(UUID.fromString(playerId))
            } catch (e: Exception) {
                return null
            }
        }

        override suspend fun message(sender: CommandSender, prevArgs: List<Any>, openArgs: List<String>): String {
            return language.playerNotFoundMessage.text.format(openArgs[0])
        }
    }

    private val bossBarTabs: (suspend (CommandSender) -> List<String>) = {
        repository.getAll().map { e -> e.name }
    }

    private val bossBarMustExist = object : Validator<ShyBossBarMeta> {
        override suspend fun transform(
            sender: CommandSender, prevArgs: List<Any>, openArgs: List<String>
        ): ShyBossBarMeta? {
            return repository.getAll().firstOrNull { e -> e.name.equals(openArgs[0], true) }
        }

        override suspend fun message(sender: CommandSender, prevArgs: List<Any>, openArgs: List<String>): String {
            return language.bossBarNotFoundMessage.text.format(openArgs[0])
        }
    }

    private val onlinePlayerTabs: (suspend (CommandSender) -> List<String>) = {
        Bukkit.getOnlinePlayers().map { e -> e.name }
    }

    init {
        CommandBuilder(plugin, coroutineExecutor, settings.baseCommand, chatMessageService) {
            usage(language.bossBarCommandUsage.text)
            description(language.bossBarCommandDescription.text)
            aliases(settings.commandAliases)
            permission(settings.commandPermission)
            permissionMessage(language.noPermissionCommand.text)
            subCommand("add") {
                permission(settings.addPermission)
                toolTip { language.bossBarAddCommandHint.text }
                builder().argument("bossbar").validator(bossBarMustExist)
                    .tabs(bossBarTabs).executePlayer(senderHasToBePlayer) { player, bossBarMeta ->
                        plugin.launch {
                            addBossBarToPlayer(player, bossBarMeta, player)
                        }
                    }.argument("player").validator(playerMustExist).tabs(onlinePlayerTabs)
                    .execute { commandSender, bossBarMeta, player ->
                        plugin.launch {
                            addBossBarToPlayer(commandSender, bossBarMeta, player)
                        }
                    }
            }
            subCommand("remove") {
                permission(settings.removePermission)
                toolTip { language.bossBarRemoveCommandHint.text }
                builder().argument("bossbar").validator(bossBarMustExist)
                    .tabs(bossBarTabs).executePlayer(senderHasToBePlayer) { player, bossBarMeta ->
                        plugin.launch {
                            remoteBossBarFromPlayer(player, bossBarMeta, player)
                        }
                    }.argument("player").validator(playerMustExist).tabs(onlinePlayerTabs)
                    .execute { commandSender, bossBarMeta, player ->
                        plugin.launch {
                            remoteBossBarFromPlayer(commandSender, bossBarMeta, player)
                        }
                    }
            }
            subCommand("update") {
                permission(settings.updatePermission)
                toolTip { language.bossBarUpdateCommandHint.text }
                builder().executePlayer(senderHasToBePlayer) { player ->
                    plugin.launch {
                        updatePlayerBossBar(player, player)
                    }
                }.argument("player").validator(playerMustExist).tabs(onlinePlayerTabs)
                    .execute { commandSender, player ->
                        plugin.launch {
                            updatePlayerBossBar(commandSender, player)
                        }
                    }
            }
            subCommand("reload") {
                permission(settings.reloadPermission)
                toolTip {
                    language.reloadCommandHint.text
                }
                builder().execute { sender ->
                    plugin.saveDefaultConfig()
                    plugin.reloadConfig()
                    plugin.reloadTranslation(language)
                    bossBarService.reload()
                    sender.sendPluginMessage(language.reloadMessage)
                }
            }.helpCommand()
        }.build()
    }

    private fun updatePlayerBossBar(sender: CommandSender, player: Player) {
        bossBarService.getBossBarFromPlayer(player)?.update(true)
        sender.sendPluginMessage(language.bossBarUpdatedMessage)
    }

    private fun addBossBarToPlayer(
        sender: CommandSender,
        bossBarMeta: ShyBossBarMeta,
        player: Player
    ) {
        if (!player.hasPermission("${settings.dynBossBarPermission}${bossBarMeta.name}")) {
            sender.sendPluginMessage(language.bossBarNoPermissionToBossBarCommand)
            return
        }

        bossBarService.addPriorityBossBar(player, bossBarMeta.name)
        sender.sendPluginMessage(language.bossBarAddedMessage, bossBarMeta.name, player.name)
    }

    private fun remoteBossBarFromPlayer(
        sender: CommandSender,
        bossBarMeta: ShyBossBarMeta,
        player: Player
    ) {
        bossBarService.removePriorityBossBar(player, bossBarMeta.name)
        sender.sendPluginMessage(language.bossBarRemovedMessage, bossBarMeta.name, player.name)
    }
}
