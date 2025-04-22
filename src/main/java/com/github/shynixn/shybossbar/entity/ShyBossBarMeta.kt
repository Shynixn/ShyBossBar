package com.github.shynixn.shybossbar.entity

import com.github.shynixn.mcutils.common.repository.Element
import com.github.shynixn.shybossbar.enumeration.ShyBossBarType

class ShyBossBarMeta : Element {
    /**
     * Unique Identifier of the element.
     */
    override var name: String = ""

    /**
     * Global type.
     */
    var type: ShyBossBarType = ShyBossBarType.GLOBAL

    /**
     * Priority.
     */
    var priority: Int = 1

    /**
     * How often this bossBar is updated.
     */
    var refreshTicks: Int = 20 * 10

    /**
     * Gets or sets the message.
     */
    var message: String = ""

    /**
     * Color.
     */
    var color: String = "WHITE"

    /**
     * Progress
     */
    var progress: String = "1.0F"

    /**
     * Style
     */
    var style: String = "SOLID"

    /**
     * Flags
     */
    var flags: List<String> = emptyList()
}
