package ru.neroduckale.reputation.listener;

import dev.rollczi.litecommands.annotations.async.Async;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import ru.neroduckale.reputation.model.ReputationUser;
import ru.neroduckale.reputation.service.Language;
import ru.neroduckale.reputation.service.ReputationUserService;

import java.util.logging.Level;

@RequiredArgsConstructor
public class PreJoinListener implements Listener {
    private final ReputationUserService userService;

    @EventHandler
    public void onJoin(AsyncPlayerPreLoginEvent event) {
        String username = event.getName();
        if (username.isEmpty()) {
            return;
        }

        if (userService.findByUsername(username).isEmpty()) {
            userService.save(ReputationUser.builder()
                    .username(username)
                    .reputation(5000)
                    .lang(Language.EN)
                    .build());
        }
    }
}
