package com.github.shynixn.shybossbar.contract

import com.github.shynixn.mcutils.common.language.LanguageItem
import com.github.shynixn.mcutils.common.language.LanguageProvider

interface ShyBossBarLanguage : LanguageProvider {
  var playerNotFoundMessage: LanguageItem

  var noPermissionCommand: LanguageItem

  var reloadCommandHint: LanguageItem

  var reloadMessage: LanguageItem

  var commonErrorMessage: LanguageItem

  var commandSenderHasToBePlayer: LanguageItem

  var bossBarCommandUsage: LanguageItem

  var bossBarCommandDescription: LanguageItem

  var bossBarAddCommandHint: LanguageItem

  var bossBarRemoveCommandHint: LanguageItem

  var bossBarNotFoundMessage: LanguageItem

  var bossBarNoPermissionToBossBarCommand: LanguageItem

  var bossBarAddedMessage: LanguageItem

  var bossBarRemovedMessage: LanguageItem

  var bossBarUpdateCommandHint: LanguageItem

  var bossBarUpdatedMessage: LanguageItem
}
