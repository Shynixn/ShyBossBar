package com.github.shynixn.shybossbar.impl.listener

import com.github.shynixn.mccoroutine.folia.launch
import com.github.shynixn.shybossbar.contract.BossBarService
import com.github.shynixn.shybossbar.entity.ShyBossBarSettings
import kotlinx.coroutines.delay
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.Plugin

class ShyBossBarListener(
    private val settings: ShyBossBarSettings,
    private val plugin: Plugin,
    private val bossBarService: BossBarService
) : Listener {
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        plugin.launch {
            delay(settings.joinDelaySeconds * 1000L)
            if (player.isOnline) {
                bossBarService.updatePlayerBossBar(player)
            }
        }
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        bossBarService.clearData(event.player)
    }
}
