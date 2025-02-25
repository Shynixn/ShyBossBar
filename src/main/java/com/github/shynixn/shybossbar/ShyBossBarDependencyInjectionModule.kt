package com.github.shynixn.shybossbar

import com.fasterxml.jackson.core.type.TypeReference
import com.github.shynixn.mcutils.common.ConfigurationService
import com.github.shynixn.mcutils.common.ConfigurationServiceImpl
import com.github.shynixn.mcutils.common.chat.ChatMessageService
import com.github.shynixn.mcutils.common.di.DependencyInjectionModule
import com.github.shynixn.mcutils.common.language.globalChatMessageService
import com.github.shynixn.mcutils.common.language.globalPlaceHolderService
import com.github.shynixn.mcutils.common.placeholder.PlaceHolderService
import com.github.shynixn.mcutils.common.placeholder.PlaceHolderServiceImpl
import com.github.shynixn.mcutils.common.repository.CacheRepository
import com.github.shynixn.mcutils.common.repository.CachedRepositoryImpl
import com.github.shynixn.mcutils.common.repository.Repository
import com.github.shynixn.mcutils.common.repository.YamlFileRepositoryImpl
import com.github.shynixn.mcutils.packet.api.PacketService
import com.github.shynixn.mcutils.packet.impl.service.ChatMessageServiceImpl
import com.github.shynixn.mcutils.packet.impl.service.PacketServiceImpl
import com.github.shynixn.shybossbar.contract.BossBarFactory
import com.github.shynixn.shybossbar.contract.BossBarService
import com.github.shynixn.shybossbar.contract.ShyBossBarLanguage
import com.github.shynixn.shybossbar.entity.ShyBossBarMeta
import com.github.shynixn.shybossbar.entity.ShyBossBarSettings
import com.github.shynixn.shybossbar.impl.commandexecutor.ShyBossBarCommandExecutor
import com.github.shynixn.shybossbar.impl.listener.ShyBossBarListener
import com.github.shynixn.shybossbar.impl.service.BossBarFactoryImpl
import com.github.shynixn.shybossbar.impl.service.BossBarServiceImpl
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.ServicePriority

class ShyBossBarDependencyInjectionModule(
    private val plugin: Plugin, private val settings: ShyBossBarSettings, private val language: ShyBossBarLanguage
) {
    fun build(): DependencyInjectionModule {
        val module = DependencyInjectionModule()

        // Params
        module.addService<Plugin>(plugin)
        module.addService<ShyBossBarLanguage>(language)
        module.addService<ShyBossBarSettings>(settings)

        // Repositories
        val templateRepositoryImpl = YamlFileRepositoryImpl<ShyBossBarMeta>(
            plugin,
            "bossbar",
            settings.defaultBossBars,
            emptyList(),
            object : TypeReference<ShyBossBarMeta>() {})
        val cacheTemplateRepository = CachedRepositoryImpl(templateRepositoryImpl)
        module.addService<Repository<ShyBossBarMeta>>(cacheTemplateRepository)
        module.addService<CacheRepository<ShyBossBarMeta>>(cacheTemplateRepository)

        // Services
        module.addService<ShyBossBarCommandExecutor> {
            ShyBossBarCommandExecutor(
                module.getService(),
                module.getService(),
                module.getService(),
                module.getService(),
                module.getService(),
                module.getService()
            )
        }
        module.addService<ShyBossBarListener> {
            ShyBossBarListener(module.getService(), module.getService(), module.getService())
        }
        module.addService<BossBarFactory> {
            BossBarFactoryImpl(module.getService(), module.getService(), module.getService())
        }
        module.addService<BossBarService> {
            BossBarServiceImpl(module.getService(), module.getService(), module.getService(), module.getService())
        }

        // Library Services
        module.addService<ConfigurationService>(ConfigurationServiceImpl(plugin))
        module.addService<PacketService>(PacketServiceImpl(plugin))
        val placeHolderService = PlaceHolderServiceImpl(plugin)
        module.addService<PlaceHolderService>(placeHolderService)
        val chatMessageService = ChatMessageServiceImpl(plugin)
        module.addService<ChatMessageService>(chatMessageService)
        plugin.globalChatMessageService = chatMessageService
        plugin.globalPlaceHolderService = placeHolderService

        // Developer Api.
        Bukkit.getServicesManager().register(
            BossBarService::class.java, module.getService<BossBarService>(), plugin, ServicePriority.Normal
        )
        Bukkit.getServicesManager().register(
            BossBarFactory::class.java, module.getService<BossBarFactory>(), plugin, ServicePriority.Normal
        )

        return module
    }
}
