package com.github.shynixn.shybossbar.entity

import com.github.shynixn.shybossbar.enumeration.Permission

class ShyBossBarSettings(private val reloadFun: (ShyBossBarSettings) -> Unit) {
    /**
     * Delay when joining the server.
     */
    var joinDelaySeconds = 3

    /**
     * Permission change seconds.
     */
    var checkForChangeChangeSeconds = 5

    /**
     * Base Command.
     */
    var baseCommand: String = "shybossbar"

    /**
     * Worldguard flag.
     */
    var worldGuardFlag: String = "shybossbar"

    /**
     * Command aliases.
     */
    var commandAliases: List<String> = ArrayList()


    var commandPermission: String = Permission.COMMAND.text


    var reloadPermission: String = Permission.RELOAD.text

    var dynBossBarPermission: String = Permission.DYN_BOSSBAR.text

    var addPermission: String = Permission.ADD.text

    var setPermission: String = Permission.SET.text

    var removePermission: String = Permission.REMOVE.text

    var updatePermission: String = Permission.UPDATE.text

    var defaultBossBars: List<Pair<String, String>> = listOf(
        "bossbar/sample_bossbar.yml" to "sample_bossbar.yml"
    )

    /**
     * Reloads the config.
     */
    fun reload() {
        reloadFun.invoke(this)
    }
}
