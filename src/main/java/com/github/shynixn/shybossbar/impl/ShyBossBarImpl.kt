package com.github.shynixn.shybossbar.impl

import com.github.shynixn.mccoroutine.folia.globalRegionDispatcher
import com.github.shynixn.mccoroutine.folia.launch
import com.github.shynixn.mcutils.common.placeholder.PlaceHolderService
import com.github.shynixn.mcutils.packet.api.PacketService
import com.github.shynixn.mcutils.packet.api.packet.PacketOutBossBarDestroy
import com.github.shynixn.mcutils.packet.api.packet.PacketOutBossBarSpawn
import com.github.shynixn.mcutils.packet.api.packet.PacketOutBossBarUpdate
import com.github.shynixn.shybossbar.contract.ShyBossBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

class ShyBossBarImpl(
    private val id: String,
    override val name: String,
    /**
     * message
     */
    override var message: String,

    /**
     * Color.
     */
    override var color: String,
    /**
     * Style
     */
    override var style: String,

    /**
     * Flags
     */
    override var flags: List<String>,

    /**
     * Player.
     */
    private var playerParam: Player?,

    /**
     * How often refreshed.
     */
    private var refreshMilliSeconds: Long,

    /**
     * PacketService.
     */
    private var packetService: PacketService,

    /**
     * PlaceHolder service.
     */
    private var placeHolderService: PlaceHolderService,

    /**
     * Plugin
     */
    private var plugin: Plugin
) : ShyBossBar {
    private var lastMessage = ""
    private var lastColor = ""
    private var lastProgress = ""
    private var lastStyle = ""

    /**
     * Gets the player using this bossbar.
     */
    override val player: Player
        get() {
            checkDisposed()
            return playerParam!!
        }

    /**
     * Is this bossBar disposed.
     */
    override var isDisposed: Boolean = false

    init {
        plugin.launch {
            sendSpawnPacket()

            while (!isDisposed) {
                updateAsync()
                delay(refreshMilliSeconds)
            }
        }
    }

    /**
     * Performs an immediate update. If you have set a short update interval when creating this bossBar, you do not need to send update.
     */
    override fun update(respawn: Boolean) {
        checkDisposed()
        plugin.launch {
            if (respawn) {
                sendDestroyPacket()
                sendSpawnPacket()
            } else {
                updateAsync()
            }
        }
    }

    /**
     * Disposes this bossBar permanently.
     */
    override fun close() {
        if (playerParam != null) {
            sendDestroyPacket()
        }
        isDisposed = true
        playerParam = null
    }

    private suspend fun updateAsync() {
        val initialPair = resolveTitleAndLines()

        if (isDisposed) {
            return
        }

        val message = initialPair[0]
        val color = initialPair[1]
        val progressRaw = initialPair[2]
        val progress = if (progressRaw.toFloatOrNull() != null) {
            progressRaw.toFloat()
        } else {
            1.0F
        }
        val style = initialPair[3]

        val packetOutBossBarUpdate = PacketOutBossBarUpdate(id)

        if (message != lastMessage) {
            packetOutBossBarUpdate.message = message
        }
        if (color != lastColor) {
            packetOutBossBarUpdate.color = color
        }
        if (progressRaw != lastProgress) {
            packetOutBossBarUpdate.progress = progress
        }
        if (style != lastStyle) {
            packetOutBossBarUpdate.style = style
        }

        lastMessage = message
        lastColor = color
        lastStyle = style
        lastProgress = progressRaw
        packetService.sendPacketOutBossBarUpdate(player, packetOutBossBarUpdate)
    }

    private fun sendDestroyPacket() {
        if (isDisposed) {
            return
        }

        packetService.sendPacketOutBossBarDestroy(playerParam!!, PacketOutBossBarDestroy(id))
    }

    private suspend fun sendSpawnPacket() {
        val initialPair = resolveTitleAndLines()

        if (isDisposed) {
            return
        }

        val message = initialPair[0]
        val color = initialPair[1]
        val progressRaw = initialPair[2]
        val progress = if (progressRaw.toFloatOrNull() != null) {
            progressRaw.toFloat()
        } else {
            1.0F
        }
        val style = initialPair[3]
        packetService.sendPacketOutBossBarSpawn(
            playerParam!!, PacketOutBossBarSpawn(id, color, message, progress, style, flags)
        )
    }

    private suspend fun resolveTitleAndLines(): Array<String> {
        return withContext(plugin.globalRegionDispatcher) {
            if (isDisposed) {
                return@withContext emptyArray()
            }
            val finalMessage = placeHolderService.resolvePlaceHolder(message, player)
            arrayOf(finalMessage)
        }
    }

    private fun checkDisposed() {
        if (isDisposed) {
            throw IllegalArgumentException("ShyBossBaris already disposed!")
        }
    }
}
