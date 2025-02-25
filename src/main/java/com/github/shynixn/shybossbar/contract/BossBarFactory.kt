package com.github.shynixn.shybossbar.contract

import com.github.shynixn.shybossbar.entity.ShyBossBarMeta
import org.bukkit.entity.Player

interface BossBarFactory {
    /**
     * Creates a new bossBar from the given metadata.
     */
    fun createBossBar(player: Player, meta: ShyBossBarMeta): ShyBossBar
}
