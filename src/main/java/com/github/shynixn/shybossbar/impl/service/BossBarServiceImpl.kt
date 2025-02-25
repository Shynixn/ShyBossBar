package com.github.shynixn.shybossbar.impl.service

import com.github.shynixn.mccoroutine.folia.globalRegionDispatcher
import com.github.shynixn.mccoroutine.folia.launch
import com.github.shynixn.mcutils.common.repository.CacheRepository
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
    private val bossBarFactory: BossBarFactory
) :
    BossBarService {
    private val bossBarCache = HashMap<Player, ShyBossBar>()
    private val priorityBossBars = HashMap<Player, HashSet<String>>()
    private var isDisposed = false

    init {
        plugin.launch {
            while (!isDisposed) {
                reloadActiveBossBar()
                delay(settings.checkForPermissionChangeSeconds * 1000L)
            }
        }
    }

    /**
     * Adds a new bossBar.
     */
    override fun addPriorityBossBar(player: Player, name: String) {
        if (!priorityBossBars.containsKey(player)) {
            priorityBossBars[player] = HashSet()
        }

        if (!priorityBossBars[player]!!.contains(name)) {
            priorityBossBars[player]!!.add(name)
            plugin.launch {
                updatePlayerBossBar(player)
            }
        }
    }

    /**
     * Removes a new bossBar.
     */
    override fun removePriorityBossBar(player: Player, name: String) {
        if (priorityBossBars.containsKey(player)) {
            priorityBossBars[player]!!.remove(name)
            plugin.launch {
                updatePlayerBossBar(player)
            }
        }
    }

    /**
     * Reloads all bossBars and configuration.
     */
    override suspend fun reload() {
        repository.clearCache()
        priorityBossBars.clear()
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
        val bossBar = bossBarCache.remove(player)
        bossBar?.close()
        priorityBossBars.remove(player)
    }

    /**
     * Checks registered bossBars for a player and may apply one according to settings.
     */
    override suspend fun updatePlayerBossBar(player: Player) {
        val allBossBarMetas = repository.getAll()
        val possibleBossBarMetas = HashSet<ShyBossBarMeta>()

        // Check first if there are commandBossBars
        val priorityBossBars = priorityBossBars[player]
        if (priorityBossBars != null) {
            for (priorityBossBar in priorityBossBars) {
                val matchingBossBar = allBossBarMetas.firstOrNull { e -> e.name.equals(priorityBossBar, true) }
                if (matchingBossBar != null) {
                    possibleBossBarMetas.add(matchingBossBar)
                }
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
                // The player has no longer a bossbar.
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

    /**
     * Gets the bossbar of a player.
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
        priorityBossBars.clear()
        isDisposed = true
    }

    private suspend fun reloadActiveBossBar() {
        val players = withContext(plugin.globalRegionDispatcher) {
            ArrayList(Bukkit.getOnlinePlayers())
        }

        for (player in players) {
            updatePlayerBossBar(player)
        }
    }
}
