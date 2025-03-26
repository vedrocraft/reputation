package ru.neroduckale.reputation.placeholder;

import lombok.RequiredArgsConstructor;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import ru.neroduckale.reputation.service.ReputationUserService;

@RequiredArgsConstructor
public class ReputationPlaceholder extends PlaceholderExpansion {
    private final ReputationUserService userService;

    @Override
    public @NotNull String getIdentifier() {
        return "reputation";
    }

    @Override
    public @NotNull String getAuthor() {
        return "sema1ary";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (player.getName() == null || player.getName().isEmpty()) {
            return null;
        }

        return String.valueOf(userService.getUser(player.getName()).getReputation());
    }
}
