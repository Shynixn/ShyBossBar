package com.github.shynixn.shybossbar.contract

import org.bukkit.entity.Player

interface BossBarService : AutoCloseable {
    /**
     * Reloads all bossBars and configuration.
     */
    suspend fun reload()

    /**
     * Clears all allocated data from this player.
     */
    fun clearData(player: Player)

    /**
     * Checks registered bossBars for a player and may apply one according to settings.
     */
    suspend fun updatePlayerBossBar(player: Player)

    /**
     * Gets the bossBar of a player.
     */
    fun getBossBarFromPlayer(player: Player): ShyBossBar?

    /**
     * Adds a new bossBar
     */
    fun addCommandBossBar(player: Player, name: String)

    /**
     * Removes a new bossBar.
     */
    fun removeCommandBossBar(player: Player, name: String)

    /**
     * Gets all command bossBar.
     */
    fun getCommandBossBars(player: Player): List<String>
}
