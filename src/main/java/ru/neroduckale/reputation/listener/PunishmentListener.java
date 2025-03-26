package ru.neroduckale.reputation.listener;

import litebans.api.*;
import lombok.RequiredArgsConstructor;
import ru.neroduckale.reputation.service.ReputationUserService;
import ru.sema1ary.vedrocraftapi.player.PlayerUtil;
import ru.sema1ary.vedrocraftapi.service.ConfigService;

@RequiredArgsConstructor
public class PunishmentListener extends Events.Listener {
    private final ConfigService configService;
    private final ReputationUserService userService;

    @Override
    public void entryAdded(Entry entry) {
        String username = entry.getExecutorName();

        if(username == null || username.isEmpty()) {
            return;
        }

        int amount = configService.get("reputation-remove-amount");

        if (userService.getUser(username).getReputation() < amount) {
            return;
        }

        userService.removeReputation(username, amount);
        PlayerUtil.sendMessage(username,
                ((String) configService.get("get-punishment-message")).replace("{amount}", String.valueOf(amount))
        );
    }
}
