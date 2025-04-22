package com.github.shynixn.shybossbar

import com.github.shynixn.mcutils.common.language.LanguageItem
import com.github.shynixn.shybossbar.contract.ShyBossBarLanguage

class ShyBossBarLanguageImpl : ShyBossBarLanguage {
 override val names: List<String>
  get() = listOf("en_us")
 override var shyBossBarPlayerNotFoundMessage = LanguageItem("[&9ShyBossBar&f] &cPlayer %1$1s not found.")

 override var shyBossBarNoPermissionCommand = LanguageItem("[&9ShyBossBar&f] &cYou do not have permission to execute this command.")

 override var shyBossBarReloadCommandHint = LanguageItem("Reloads all bossbars and configuration.")

 override var shyBossBarReloadMessage = LanguageItem("[&9ShyBossBar&f] Reloaded all bossbars and configuration.")

 override var shyBossBarCommonErrorMessage = LanguageItem("[&9ShyBossBar&f]&c A problem occurred. Check the console log for details.")

 override var shyBossBarCommandSenderHasToBePlayer = LanguageItem("[&9ShyBossBar&f] The command sender has to be a player if you do not specify the optional player argument.")

 override var shyBossBarCommandUsage = LanguageItem("[&9ShyBossBar&f] Use /shybossbar help to see more info about the plugin.")

 override var shyBossBarCommandDescription = LanguageItem("[&9ShyBossBar&f] All commands for the ShyBossBar plugin.")

 override var shyBossBarAddCommandHint = LanguageItem("Adds a bossbar to a player.")

 override var shyBossBarSetCommandHint = LanguageItem("Sets a bossbar to a player.")

 override var shyBossBarRemoveCommandHint = LanguageItem("Removes a bossbar from a player.")

 override var shyBossBarNotFoundMessage = LanguageItem("[&9ShyBossBar&f] &cBossBar %1$1s not found.")

 override var shyBossBarNoPermissionToBossBarCommand = LanguageItem("[&9ShyBossBar&f] &cYou do not have permission to this bossbar.")

 override var shyBossBarAddedMessage = LanguageItem("[&9ShyBossBar&f] Added the bossbar %1$1s to the player %2$1s.")

 override var shyBossBarRemovedMessage = LanguageItem("[&9ShyBossBar&f] Removed the bossbar %1$1s from the player %2$1s.")

 override var shyBossBarUpdateCommandHint = LanguageItem("Updates the placeholder of the bossbar.")

 override var shyBossBarUpdatedMessage = LanguageItem("[&9ShyBossBar&f] Updated the bossbar.")

 override var shyBossBarBooleanNotFoundMessage = LanguageItem("[&9ShyBossBar&f]&c Only true and false are allowed as values.")
}
