package ru.neroduckale.reputation.service.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import org.jetbrains.annotations.NotNull;
import ru.neroduckale.reputation.service.Language;
import ru.neroduckale.reputation.service.LocalizationService;
import ru.neroduckale.reputation.service.ReputationUserService;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;

@Slf4j
public class LocalizationServiceImpl implements LocalizationService {

    private final Plugin plugin;
    private final HashMap<Language, HashMap<String, String>> localizationMap = new HashMap<>();
    private final HashMap<Language, String> fileNameMap = new HashMap<>();
    private final HashMap<Language, File> fileMap = new HashMap<>();
    private final ReputationUserService userService;

    public LocalizationServiceImpl(Plugin plugin, ReputationUserService userService) {
        this.plugin = plugin;
        this.userService = userService;
        for (Language language : Language.values()) {
            localizationMap.put(language, new HashMap<>());
            fileNameMap.put(language, language.toString().toLowerCase() + ".yml");
            fileMap.put(language, new File(plugin.getDataFolder(), fileNameMap.get(language)));
        }
    }

    @Override
    public void reload() {
        localizationMap.forEach((language, localizationMap) -> {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(fileMap.get(language));
            config.getKeys(false).forEach((string) -> localizationMap.put(string, (String) config.get(string)));
        });
    }

    @Override
    public @NonNull String get(@NonNull String key, @NotNull Language language) {
        if (key == null) {
            throw new NullPointerException("index is marked non-null but is null");
        } else {
            String object = localizationMap.get(language).get(key);
            if (object == null) {
                log.error("[LocalizationService] {} dont exists!", key);
                return "[Localization error]";
            }

            return object;
        }
    }

    @Override
    public void set(@NonNull String key, String value, @NotNull Language language) {
        acceptSet(key, value, language);

        YamlConfiguration config = YamlConfiguration.loadConfiguration(fileMap.get(language));
        config.set(key, value);
        saveLocalization(config, language);
    }

    private void acceptSet(@NotNull String key, String value, @NotNull Language language) {
        boolean isContains = localizationMap.get(language).containsKey(key);
        if (value == null && isContains) {
            localizationMap.get(language).remove(key);
            return;
        }
        if (isContains) {
            localizationMap.get(language).replace(key, value);
        } else {
            localizationMap.get(language).put(key, value);
        }
    }

    @Override
    public boolean isSettingExists(@NonNull String key, @NotNull Language language) {
        if (key == null) {
            throw new NullPointerException("index is marked non-null but is null");
        } else {
            return localizationMap.get(language).containsKey(key);
        }
    }

    @Override
    public void saveDefaultLocalization() {
        fileMap.forEach((language, file) -> {
            if (!file.exists()) plugin.saveResource(fileNameMap.get(language), false);
        });
    }


    @Override
    public void enable() {
        log.info("[LocalizationService] Starting...");
        saveDefaultLocalization();
        reload();
    }

    public void saveLocalization(YamlConfiguration localization, Language language) {
        try {
            localization.save(fileMap.get(language));
        } catch (IOException ex) {
            plugin.getLogger().log(Level.SEVERE, "Could not save config to " + fileMap.get(language), ex);
        }
    }

    @Override
    public Language getLang(@NotNull CommandSender sender) {
        if (sender instanceof Player player) {
            return userService.getUser(player.getName()).getLang();
        }
        return Language.EN;
    }

    @Override
    public Language getLang(@NotNull Player sender) {
        return userService.getUser(sender.getName()).getLang();
    }

    @Override
    public Language getLang(@NotNull String sender) {
        return userService.getUser(sender).getLang();
    }
}
