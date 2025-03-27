package ru.neroduckale.reputation.listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import ru.neroduckale.reputation.service.LocalizationService;
import ru.neroduckale.reputation.service.ReputationUserService;
import ru.sema1ary.vedrocraftapi.player.PlayerUtil;

@RequiredArgsConstructor
public class MuteListener implements Listener {
    private final LocalizationService localizationService;
    private final ReputationUserService userService;

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        Player player = event.getPlayer();
        if (userService.getUser(player.getName()).getReputation() <= 3000) {
            PlayerUtil.sendMessage(player, localizationService.get("muted-chat-message", localizationService.getLang(player)));
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPickup(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player player)
                || userService.getUser(player.getName()).getReputation() > 1500) {
            return;
        }

        Material itemType = event.getItem().getItemStack().getType();
        if (itemType.equals(Material.WRITTEN_BOOK) || itemType.equals(Material.WRITABLE_BOOK)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();

        if (userService.getUser(player.getName()).getReputation() > 1500) {
            return;
        }

        Material itemType = event.getItemDrop().getItemStack().getType();
        if (itemType.equals(Material.WRITTEN_BOOK) || itemType.equals(Material.WRITABLE_BOOK)) {
            PlayerUtil.sendMessage(player, localizationService.get("muted-chat-message", localizationService.getLang(player)));
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        Player player = event.getPlayer();

        if (userService.getUser(player.getName()).getReputation() > 1000) {
            return;
        }

        PlayerUtil.sendMessage(player, localizationService.get("muted-chat-message", localizationService.getLang(player)));
        event.setCancelled(true);
    }
}
