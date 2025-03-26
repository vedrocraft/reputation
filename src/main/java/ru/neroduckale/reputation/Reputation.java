package ru.neroduckale.reputation;

import litebans.api.Events;
import org.bukkit.plugin.java.JavaPlugin;
import ru.neroduckale.reputation.command.ReputationCommand;
import ru.neroduckale.reputation.listener.MuteListener;
import ru.neroduckale.reputation.listener.PreJoinListener;
import ru.neroduckale.reputation.listener.PunishmentListener;
import ru.neroduckale.reputation.model.ReputationUser;
import ru.neroduckale.reputation.service.ReputationUserService;
import ru.neroduckale.reputation.service.impl.ReputationUserServiceImpl;
import ru.sema1ary.vedrocraftapi.BaseCommons;
import ru.sema1ary.vedrocraftapi.command.LiteCommandBuilder;
import ru.sema1ary.vedrocraftapi.ormlite.ConnectionSourceUtil;
import ru.sema1ary.vedrocraftapi.ormlite.DatabaseUtil;
import ru.sema1ary.vedrocraftapi.service.ConfigService;
import ru.sema1ary.vedrocraftapi.service.ServiceManager;
import ru.sema1ary.vedrocraftapi.service.impl.ConfigServiceImpl;


public final class Reputation extends JavaPlugin implements BaseCommons {

    @Override
    public void onEnable() {
        ServiceManager.registerService(ConfigService.class, new ConfigServiceImpl(this));

        DatabaseUtil.initConnectionSource(
                this,
                getService(ConfigService.class),
                ReputationUser.class
        );

        ServiceManager.registerService(ReputationUserService.class, new ReputationUserServiceImpl(
                getDao(ReputationUser.class)
        ));

        getServer().getPluginManager().registerEvents(new PreJoinListener(
                getService(ReputationUserService.class)
        ), this);

        getServer().getPluginManager().registerEvents(new MuteListener(
                getService(ConfigService.class),
                getService(ReputationUserService.class)
        ), this);

        LiteCommandBuilder.builder()
                .commands(new ReputationCommand(
                        getService(ConfigService.class),
                        getService(ReputationUserService.class))
                )
                .build();

        Events.get().register(new PunishmentListener(
                getService(ConfigService.class),
                getService(ReputationUserService.class)
        ));
    }

    @Override
    public void onDisable() {
        ConnectionSourceUtil.closeConnection(true);
    }
}
