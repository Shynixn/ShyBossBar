package com.github.shynixn.shybossbar

import com.github.shynixn.mcutils.common.language.LanguageItem
import com.github.shynixn.shybossbar.contract.ShyBossBarLanguage

class ShyBossBarLanguageImpl : ShyBossBarLanguage {
 override val names: List<String>
  get() = listOf("en_us")
 override var playerNotFoundMessage = LanguageItem("[&9ShyBossBar&f] &cPlayer %1$1s not found.")

 override var noPermissionCommand = LanguageItem("[&9ShyBossBar&f] &cYou do not have permission to execute this command.")

 override var reloadCommandHint = LanguageItem("Reloads all bossBars and configuration.")

 override var reloadMessage = LanguageItem("[&9ShyBossBar&f] Reloaded all bossBars and configuration.")

 override var commonErrorMessage = LanguageItem("[&9ShyBossBar&f]&c A problem occurred. Check the console log for details.")

 override var commandSenderHasToBePlayer = LanguageItem("[&9ShyBossBar&f] The command sender has to be a player if you do not specify the optional player argument.")

 override var bossBarCommandUsage = LanguageItem("[&9ShyBossBar&f] Use /shybossbar help to see more info about the plugin.")

 override var bossBarCommandDescription = LanguageItem("[&9ShyBossBar&f]All commands for the ShyBossBar plugin.")

 override var bossBarAddCommandHint = LanguageItem("Adds a bossBar to a player.")

 override var bossBarRemoveCommandHint = LanguageItem("Removes a bossBar from a player.")

 override var bossBarNotFoundMessage = LanguageItem("[&9ShyBossBar&f] &cBossBar %1$1s not found.")

 override var bossBarNoPermissionToBossBarCommand = LanguageItem("[&9ShyBossBar&f] &cYou do not have permission to this bossBar.")

 override var bossBarAddedMessage = LanguageItem("[&9ShyBossBar&f] Added the bossBar %1$1s to the player %2$1s.")

 override var bossBarRemovedMessage = LanguageItem("[&9ShyBossBar&f] Removed the bossBar %1$1s from the player %2$1s.")

 override var bossBarUpdateCommandHint = LanguageItem("Updates the placeholder of the bossBar.")

 override var bossBarUpdatedMessage = LanguageItem("[&9ShyBossBar&f] Updated the bossBar.")
}
