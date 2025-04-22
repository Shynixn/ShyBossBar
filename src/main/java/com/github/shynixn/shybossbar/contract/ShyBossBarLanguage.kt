package com.github.shynixn.shybossbar.contract

import com.github.shynixn.mcutils.common.language.LanguageItem
import com.github.shynixn.mcutils.common.language.LanguageProvider

interface ShyBossBarLanguage : LanguageProvider {
  var shyBossBarPlayerNotFoundMessage: LanguageItem

  var shyBossBarNoPermissionCommand: LanguageItem

  var shyBossBarReloadCommandHint: LanguageItem

  var shyBossBarReloadMessage: LanguageItem

  var shyBossBarCommonErrorMessage: LanguageItem

  var shyBossBarCommandSenderHasToBePlayer: LanguageItem

  var shyBossBarCommandUsage: LanguageItem

  var shyBossBarCommandDescription: LanguageItem

  var shyBossBarAddCommandHint: LanguageItem

  var shyBossBarSetCommandHint: LanguageItem

  var shyBossBarRemoveCommandHint: LanguageItem

  var shyBossBarNotFoundMessage: LanguageItem

  var shyBossBarNoPermissionToBossBarCommand: LanguageItem

  var shyBossBarAddedMessage: LanguageItem

  var shyBossBarRemovedMessage: LanguageItem

  var shyBossBarUpdateCommandHint: LanguageItem

  var shyBossBarUpdatedMessage: LanguageItem

  var shyBossBarBooleanNotFoundMessage: LanguageItem
}
