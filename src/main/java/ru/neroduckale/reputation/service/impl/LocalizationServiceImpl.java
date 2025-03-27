package ru.neroduckale.reputation.service.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
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


public class LocalizationServiceImpl implements LocalizationService {

    private final Plugin plugin;
    private final HashMap<String, String> ruLocalizationMap = new HashMap<>();
    private final HashMap<String, String> enLocalizationMap = new HashMap<>();
    private final String ruFileName = "ru.yml";
    private final String enFileName = "en.yml";
    private final File ruFile;
    private final File enFile;
    private final ReputationUserService userService;

    public LocalizationServiceImpl(Plugin plugin, ReputationUserService userService) {
        this.plugin = plugin;
        this.userService = userService;
        ruFile = new File(plugin.getDataFolder(), ruFileName);
        enFile = new File(plugin.getDataFolder(), enFileName);
    }

    @Override
    public void reload() {
        YamlConfiguration ruConfig = YamlConfiguration.loadConfiguration(ruFile);
        YamlConfiguration enConfig = YamlConfiguration.loadConfiguration(enFile);
        ruConfig.getKeys(false).forEach((string) -> ruLocalizationMap.put(string, (String) ruConfig.get(string)));
        enConfig.getKeys(false).forEach((string) -> enLocalizationMap.put(string, (String) enConfig.get(string)));

    }

    @Override
    public @NonNull String get(@NonNull String key, @NotNull Language language) {
        if (language == Language.RU) {
            return ruLocalizationMap.get(key);
        } else if (language == Language.EN) {
            return enLocalizationMap.get(key);
        }
        return "[Ошибка локализации]";
    }

    @Override
    public void set(@NonNull String key, String value, @NotNull Language language) {
        acceptSet(key, value, language);
        if (language == Language.RU) {
            YamlConfiguration ruConfig = YamlConfiguration.loadConfiguration(ruFile);
            ruConfig.set(key, value);
            saveRuLocalization(ruConfig);
        } else if (language == Language.EN) {
            YamlConfiguration enConfig = YamlConfiguration.loadConfiguration(enFile);
            enConfig.set(key, value);
            saveEnLocalization(enConfig);
        }
    }

    private void acceptSet(@NotNull String key, String value, @NotNull Language language) {
        if (language == Language.RU) {
            boolean isContains = ruLocalizationMap.containsKey(key);
            if (value == null && isContains) {
                ruLocalizationMap.remove(key);
                return;
            }

            if (isContains) {
                ruLocalizationMap.replace(key, value);
            } else {
                ruLocalizationMap.put(key, value);
            }
        } else if (language == Language.EN) {
            boolean isContains = enLocalizationMap.containsKey(key);
            if (value == null && isContains) {
                enLocalizationMap.remove(key);
                return;
            }

            if (isContains) {
                enLocalizationMap.replace(key, value);
            } else {
                enLocalizationMap.put(key, value);
            }
        }
    }

    @Override
    public boolean isSettingExists(@NonNull String key, @NotNull Language language) {
        return false;
    }

    @Override
    public void saveDefaultLocalization() {
        if (!ruFile.exists()) {
            plugin.saveResource(ruFileName, false);
        }
        if (!enFile.exists()) {
            plugin.saveResource(enFileName, false);
        }
    }


    @Override
    public void enable() {
        plugin.getLogger().log(Level.ALL, "enable LocalizationService");
        saveDefaultLocalization();
        reload();
    }

    public void saveRuLocalization(YamlConfiguration localization) {
        try {
            localization.save(ruFile);
        } catch (IOException ex) {
            plugin.getLogger().log(Level.SEVERE, "Could not save config to " + ruFile, ex);
        }
    }

    public void saveEnLocalization(YamlConfiguration localization) {
        try {
            localization.save(enFile);
        } catch (IOException ex) {
            plugin.getLogger().log(Level.SEVERE, "Could not save config to " + enFile, ex);
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
