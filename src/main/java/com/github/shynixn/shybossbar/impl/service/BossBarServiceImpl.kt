package com.github.shynixn.shybossbar.impl.service

import com.github.shynixn.mccoroutine.folia.globalRegionDispatcher
import com.github.shynixn.mccoroutine.folia.launch
import com.github.shynixn.mccoroutine.folia.ticks
import com.github.shynixn.mcutils.common.repository.CacheRepository
import com.github.shynixn.mcutils.worldguard.WorldGuardService
import com.github.shynixn.shybossbar.contract.BossBarFactory
import com.github.shynixn.shybossbar.contract.BossBarService
import com.github.shynixn.shybossbar.contract.ShyBossBar
import com.github.shynixn.shybossbar.entity.ShyBossBarMeta
import com.github.shynixn.shybossbar.entity.ShyBossBarSettings
import com.github.shynixn.shybossbar.enumeration.ShyBossBarType
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

class BossBarServiceImpl(
    private val repository: CacheRepository<ShyBossBarMeta>,
    private val plugin: Plugin,
    private var settings: ShyBossBarSettings,
    private val bossBarFactory: BossBarFactory,
    private val worldGuardService: WorldGuardService
) : BossBarService {
    private val bossBarCache = HashMap<Player, ShyBossBar>()
    private val commandBossBars = HashMap<Player, HashSet<String>>()
    private var isDisposed = false

    init {
        plugin.launch {
            while (!isDisposed) {
                reloadActiveBossBar()
                delay(settings.checkForChangeChangeSeconds * 1000L)
            }
        }
    }

    /**
     * Adds a new bossbar.
     */
    override fun addCommandBossBar(player: Player, name: String) {
        if (!commandBossBars.containsKey(player)) {
            commandBossBars[player] = HashSet()
        }

        if (!commandBossBars[player]!!.contains(name)) {
            commandBossBars[player]!!.add(name)
            plugin.launch {
                updatePlayerBossBar(player)
            }
        }
    }

    /**
     * Removes a new bossbar.
     */
    override fun removeCommandBossBar(player: Player, name: String) {
        if (commandBossBars.containsKey(player)) {
            commandBossBars[player]!!.remove(name)
            plugin.launch {
                updatePlayerBossBar(player)
            }
        }
    }

    /**
     * Gets all command bossbar.
     */
    override fun getCommandBossBars(player: Player): List<String> {
        val data = commandBossBars[player]

        if (data != null) {
            return data.toList()
        }

        return emptyList()
    }

    /**
     * Reloads all bossbars and configuration.
     */
    override suspend fun reload() {
        repository.clearCache()
        commandBossBars.clear()
        repository.getAll()
        val players = bossBarCache.keys.toTypedArray()

        for (player in players) {
            clearData(player)
        }
    }

    /**
     * Clears all allocated data from this player.
     */
    override fun clearData(player: Player) {
        val bossbar = bossBarCache.remove(player)
        bossbar?.close()
        commandBossBars.remove(player)
    }

    /**
     * Checks registered bossBars for a player and may apply one according to settings.
     */
    override suspend fun updatePlayerBossBar(player: Player) {
        val flags = withContext(plugin.globalRegionDispatcher) {
            val flagValue =
                worldGuardService.getFlagValue<String>(player, settings.worldGuardFlag, player.location)
            val flags = ArrayList<String>()
            if (flagValue != null) {
                flags.add(flagValue)
            }
            flags
        }
        val allBossBarMetas = repository.getAll()
        updatePlayerBossBar(player, allBossBarMetas, flags)
    }

    /**
     * Gets the bossBar of a player.
     */
    override fun getBossBarFromPlayer(player: Player): ShyBossBar? {
        return bossBarCache[player]
    }

    /**
     * Closes this resource, relinquishing any underlying resources.
     * This method is invoked automatically on objects managed by the
     * `try`-with-resources statement.
     *
     */
    override fun close() {
        val bossBars = bossBarCache.values.toTypedArray()
        for (bossBar in bossBars) {
            bossBars.clone()
        }
        bossBarCache.clear()
        commandBossBars.clear()
        isDisposed = true
    }

    private fun updatePlayerBossBar(
        player: Player, allBossBarMetas: List<ShyBossBarMeta>, regionFlags: List<String>
    ) {
        val possibleBossBarMetas = HashSet<ShyBossBarMeta>()

        // Check first if there are commandBossBars
        val priorityBossBars = commandBossBars[player]
        if (priorityBossBars != null) {
            for (priorityBossBar in priorityBossBars) {
                val matchingBossBar = allBossBarMetas.firstOrNull { e -> e.name.equals(priorityBossBar, true) }
                if (matchingBossBar != null) {
                    possibleBossBarMetas.add(matchingBossBar)
                }
            }
        }

        // Then check WorldGuard region flags.
        if (possibleBossBarMetas.isEmpty()) {
            for (bossBar in allBossBarMetas.asSequence()
                .filter { e -> e.type == ShyBossBarType.WORLDGUARD && regionFlags.contains(e.name) }) {
                possibleBossBarMetas.add(bossBar)
            }
        }

        // Only take a look at global bossBars if empty.
        if (possibleBossBarMetas.isEmpty()) {
            for (bossBar in allBossBarMetas.asSequence().filter { e -> e.type == ShyBossBarType.GLOBAL }) {
                val permission = "${settings.dynBossBarPermission}${bossBar.name}"

                if (player.hasPermission(permission)) {
                    possibleBossBarMetas.add(bossBar)
                }
            }
        }

        // Select bossBar with the highest priority.
        val selectedBossBarMeta = possibleBossBarMetas.minByOrNull { e -> e.priority }
        val previousBossBar = bossBarCache[player]

        if (previousBossBar != null) {
            if (selectedBossBarMeta == null) {
                // The player has no longer a bossBar.
                previousBossBar.close()
                bossBarCache.remove(player)
                return
            }

            if (previousBossBar.name == selectedBossBarMeta.name) {
                // Ignore, bossBar has not changed.
                return
            }

            // The bossBar is different
            previousBossBar.close()
            bossBarCache.remove(player)
        }

        if (selectedBossBarMeta != null) {
            val bossBar = bossBarFactory.createBossBar(player, selectedBossBarMeta)
            bossBarCache[player] = bossBar
        }
    }

    private suspend fun reloadActiveBossBar() {
        val playerContainers = withContext(plugin.globalRegionDispatcher) {
            val chunked = ArrayList(Bukkit.getOnlinePlayers()).chunked(30)
            var shouldWait = false
            val result = ArrayList<Pair<Player, List<String>>>()

            for (chunk in chunked) {
                if (shouldWait) {
                    delay(1.ticks)
                }

                for (player in chunk) {
                    val flagValue =
                        worldGuardService.getFlagValue<String>(player, settings.worldGuardFlag, player.location)
                    val flags = ArrayList<String>()
                    if (flagValue != null) {
                        flags.add(flagValue)
                    }
                    result.add(player to flags)
                }

                shouldWait = true
            }
            result
        }

        val allBossBarMetas = repository.getAll()
        for (playerContainer in playerContainers) {
            updatePlayerBossBar(playerContainer.first, allBossBarMetas, playerContainer.second)
        }
    }
}
