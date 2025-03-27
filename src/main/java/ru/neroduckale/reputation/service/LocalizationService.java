package ru.neroduckale.reputation.service;

import lombok.NonNull;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.sema1ary.vedrocraftapi.service.Service;

public interface LocalizationService extends Service {
    void reload();

    @NonNull String get(@NonNull String key, @NotNull Language language);

    void set(@NonNull String key, String var2, @NotNull Language language);

    boolean isSettingExists(@NonNull String key, @NotNull Language language);

    void saveDefaultLocalization();

    Language getLang(@NotNull CommandSender sender);

    Language getLang(@NotNull Player sender);

    Language getLang(@NotNull String sender);

}
