package com.github.shynixn.shybossbar.impl.commandexecutor

import com.github.shynixn.mccoroutine.folia.globalRegionDispatcher
import com.github.shynixn.mccoroutine.folia.launch
import com.github.shynixn.mcutils.common.CoroutinePlugin
import com.github.shynixn.mcutils.common.chat.ChatMessageService
import com.github.shynixn.mcutils.common.command.CommandBuilder
import com.github.shynixn.mcutils.common.command.Validator
import com.github.shynixn.mcutils.common.language.LanguageItem
import com.github.shynixn.mcutils.common.language.reloadTranslation
import com.github.shynixn.mcutils.common.placeholder.PlaceHolderService
import com.github.shynixn.mcutils.common.repository.CacheRepository
import com.github.shynixn.shybossbar.contract.BossBarService
import com.github.shynixn.shybossbar.contract.ShyBossBarLanguage
import com.github.shynixn.shybossbar.entity.ShyBossBarMeta
import com.github.shynixn.shybossbar.entity.ShyBossBarSettings
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

class ShyBossBarCommandExecutor(
    private val settings: ShyBossBarSettings,
    private val plugin: CoroutinePlugin,
    private val bossBarService: BossBarService,
    private val language: ShyBossBarLanguage,
    private val chatMessageService: ChatMessageService,
    private val repository: CacheRepository<ShyBossBarMeta>,
    private val placeHolderService: PlaceHolderService
) {
    private val senderHasToBePlayer: () -> String = {
        language.shyBossBarCommandSenderHasToBePlayer.text
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
            return placeHolderService.resolvePlaceHolder(
                language.shyBossBarPlayerNotFoundMessage.text, null, mapOf("0" to openArgs[0])
            )
        }
    }

    private val bossBarTabs: (CommandSender) -> List<String> = {
        repository.getCache()?.map { e -> e.name } ?: emptyList()
    }

    private val booleanTabs: (CommandSender) -> List<String> = {
        listOf("true", "false")
    }

    private val booleanValidator = object : Validator<Boolean> {
        override suspend fun transform(
            sender: CommandSender, prevArgs: List<Any>, openArgs: List<String>
        ): Boolean? {
            return openArgs[0].toBooleanStrictOrNull()
        }

        override suspend fun message(sender: CommandSender, prevArgs: List<Any>, openArgs: List<String>): String {
            return placeHolderService.resolvePlaceHolder(
                language.shyBossBarBooleanNotFoundMessage.text, null, mapOf("0" to openArgs[0])
            )
        }
    }

    private val bossBarMustExist = object : Validator<ShyBossBarMeta> {
        override suspend fun transform(
            sender: CommandSender, prevArgs: List<Any>, openArgs: List<String>
        ): ShyBossBarMeta? {
            return repository.getAll().firstOrNull { e -> e.name.equals(openArgs[0], true) }
        }

        override suspend fun message(sender: CommandSender, prevArgs: List<Any>, openArgs: List<String>): String {
            return placeHolderService.resolvePlaceHolder(
                language.shyBossBarNotFoundMessage.text, null, mapOf("0" to openArgs[0])
            )
        }
    }

    private val onlinePlayerTabs: (CommandSender) -> List<String> = {
        Bukkit.getOnlinePlayers().map { e -> e.name }
    }

    init {
        CommandBuilder(plugin, settings.baseCommand, chatMessageService) {
            usage(language.shyBossBarCommandUsage.text)
            description(language.shyBossBarCommandDescription.text)
            aliases(settings.commandAliases)
            permission(settings.commandPermission)
            permissionMessage(language.shyBossBarNoPermissionCommand.text)
            subCommand("add") {
                permission(settings.addPermission)
                toolTip { language.shyBossBarAddCommandHint.text }
                builder().argument("bossbar").validator(bossBarMustExist).tabs(bossBarTabs)
                    .executePlayer(senderHasToBePlayer) { player, bossBarMeta ->
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
            subCommand("set") {
                permission(settings.setPermission)
                toolTip { language.shyBossBarSetCommandHint.text }
                builder().argument("bossbar").validator(bossBarMustExist).tabs(bossBarTabs)
                    .executePlayer(senderHasToBePlayer) { player, bossBarMeta ->
                        plugin.launch {
                            setBossBarToPlayer(player, bossBarMeta, player)
                        }
                    }.argument("player").validator(playerMustExist).tabs(onlinePlayerTabs)
                    .execute { commandSender, bossBarMeta, player ->
                        plugin.launch {
                            setBossBarToPlayer(commandSender, bossBarMeta, player)
                        }
                    }
            }
            subCommand("remove") {
                permission(settings.removePermission)
                toolTip { language.shyBossBarRemoveCommandHint.text }
                builder().argument("bossbar").validator(bossBarMustExist).tabs(bossBarTabs)
                    .executePlayer(senderHasToBePlayer) { player, bossBarMeta ->
                        plugin.launch {
                            removeBossBarFromPlayer(player, bossBarMeta, player)
                        }
                    }.argument("player").validator(playerMustExist).tabs(onlinePlayerTabs)
                    .execute { commandSender, bossBarMeta, player ->
                        plugin.launch {
                            removeBossBarFromPlayer(commandSender, bossBarMeta, player)
                        }
                    }
            }
            subCommand("update") {
                permission(settings.updatePermission)
                toolTip { language.shyBossBarUpdateCommandHint.text }
                builder().executePlayer(senderHasToBePlayer) { player ->
                    plugin.launch {
                        updatePlayerBossBar(player, true, player)
                    }
                }.argument("respawn").validator(booleanValidator).tabs(booleanTabs)
                    .executePlayer(senderHasToBePlayer) { player, flag ->
                        plugin.launch {
                            updatePlayerBossBar(player, flag, player)
                        }
                    }.argument("player").validator(playerMustExist).tabs(onlinePlayerTabs)
                    .execute { commandSender, flag, player ->
                        plugin.launch {
                            updatePlayerBossBar(commandSender, flag, player)
                        }
                    }
            }
            subCommand("reload") {
                permission(settings.reloadPermission)
                toolTip {
                    language.shyBossBarReloadCommandHint.text
                }
                builder().execute { sender ->
                    plugin.saveDefaultConfig()
                    plugin.reloadConfig()
                    plugin.reloadTranslation(language)
                    bossBarService.reload()
                    sender.sendLanguageMessage(language.shyBossBarReloadMessage)
                }
            }.helpCommand()
        }.build()
    }

    private fun updatePlayerBossBar(sender: CommandSender, respawn: Boolean, player: Player) {
        bossBarService.getBossBarFromPlayer(player)?.update(respawn)
        sender.sendLanguageMessage(language.shyBossBarUpdatedMessage)
    }

    private fun addBossBarToPlayer(
        sender: CommandSender, bossBarMeta: ShyBossBarMeta, player: Player
    ) {
        if (!player.hasPermission("${settings.dynBossBarPermission}${bossBarMeta.name}")) {
            sender.sendLanguageMessage(language.shyBossBarNoPermissionToBossBarCommand)
            return
        }

        bossBarService.addCommandBossBar(player, bossBarMeta.name)
        sender.sendLanguageMessage(language.shyBossBarAddedMessage, bossBarMeta.name, player.name)
    }

    private fun setBossBarToPlayer(
        sender: CommandSender, bossBarMeta: ShyBossBarMeta, player: Player
    ) {
        if (!player.hasPermission("${settings.dynBossBarPermission}${bossBarMeta.name}")) {
            sender.sendLanguageMessage(language.shyBossBarNoPermissionToBossBarCommand)
            return
        }

        val bossBars = bossBarService.getCommandBossBars(player)
        for (bossBar in bossBars) {
            bossBarService.removeCommandBossBar(player, bossBar)
        }
        bossBarService.addCommandBossBar(player, bossBarMeta.name)
        sender.sendLanguageMessage(language.shyBossBarAddedMessage, bossBarMeta.name, player.name)
    }

    private fun removeBossBarFromPlayer(
        sender: CommandSender, bossBarMeta: ShyBossBarMeta, player: Player
    ) {
        bossBarService.removeCommandBossBar(player, bossBarMeta.name)
        sender.sendLanguageMessage(language.shyBossBarRemovedMessage, bossBarMeta.name, player.name)
    }

    private fun CommandSender.sendLanguageMessage(languageItem: LanguageItem, vararg args: String) {
        val sender = this
        plugin.launch(plugin.globalRegionDispatcher) {
            chatMessageService.sendLanguageMessage(sender, languageItem, *args)
        }
    }
}
