package com.github.shynixn.shybossbar.impl.service

import com.github.shynixn.mccoroutine.folia.ticks
import com.github.shynixn.mcutils.common.placeholder.PlaceHolderService
import com.github.shynixn.mcutils.packet.api.PacketService
import com.github.shynixn.shybossbar.contract.BossBarFactory
import com.github.shynixn.shybossbar.contract.ShyBossBar
import com.github.shynixn.shybossbar.entity.ShyBossBarMeta
import com.github.shynixn.shybossbar.impl.ShyBossBarImpl
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import java.util.*

class BossBarFactoryImpl(
    private val plugin: Plugin,
    private val packetService: PacketService,
    private val placeHolderService: PlaceHolderService
) :
    BossBarFactory {
    /**
     * Creates a new bossBar from the given metadata.
     */
    override fun createBossBar(player: Player, meta: ShyBossBarMeta): ShyBossBar {
        val id = UUID.randomUUID().toString()
        val refreshMilliSeconds = meta.refreshTicks.ticks
        val bossBar = ShyBossBarImpl(
            id,
            meta.name,
            meta.message,
            meta.color,
            meta.style,
            meta.progress,
            meta.flags,
            player,
            refreshMilliSeconds,
            packetService,
            placeHolderService,
            plugin
        )
        return bossBar
    }
}
