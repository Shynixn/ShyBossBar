package com.github.shynixn.shybossbar.contract

import org.bukkit.entity.Player

interface ShyBossBar {
    /**
     * Name of the meta.
     */
    val name: String

    /**
     * Gets or sets the message.
     */
    var message: String

    /**
     * Color.
     */
    var color: String

    /**
     * Style
     */
    var style: String

    /**
     * Flags
     */
    var flags: List<String>

    /**
     * Gets the player using this bossBar.
     */
    val player: Player

    /**
     * Is this bossBar disposed.
     */
    val isDisposed: Boolean

    /**
     * Performs an immediate update. If you have set a short update interval when creating this bossBar, you do not need to send update.
     */
    fun update(respawn: Boolean = false)

    /**
     * Disposes this bossBar permanently.
     */
    fun close()
}
