package ru.neroduckale.reputation.command;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.async.Async;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.neroduckale.reputation.service.ReputationUserService;
import ru.sema1ary.vedrocraftapi.player.PlayerUtil;
import ru.sema1ary.vedrocraftapi.service.ConfigService;

@RequiredArgsConstructor
@Command(name = "reputation", aliases = {"rep"})
public class ReputationCommand {

    private final ConfigService configService;
    private final ReputationUserService userService;

    @Async
    @Execute(name = "reload")
    @Permission("reputation.reload")
    void reload(@Context CommandSender sender) {
        configService.reload();

        PlayerUtil.sendMessage(sender, (String) configService.get("reload-message"));
    }

    @Async
    @Execute(name = "add")
    @Permission("reputation.add")
    void addReputation(@Context CommandSender sender, @Arg("игрок") Player target, @Arg("количество") int amount) {
        userService.addReputation(target.getName(), amount);
        PlayerUtil.sendMessage(sender, ((String) configService.get("successful-added-reputation-message"))
                .replace("{user}", target.getName())
                .replace("{amount}", String.valueOf(amount)));
    }

    @Async
    @Execute(name = "remove")
    @Permission("reputation.remove")
    void removeReputation(@Context CommandSender sender, @Arg("игрок") Player target, @Arg("количество") int amount) {
        if (userService.getUser(target.getName()).getReputation() < amount) {
            PlayerUtil.sendMessage(sender, ((String) configService.get("failed-removed-reputation-message"))
                    .replace("{user}", target.getName()));
            return;
        }

        userService.removeReputation(target.getName(), amount);
        PlayerUtil.sendMessage(sender, ((String) configService.get("successful-removed-reputation-message"))
                .replace("{user}", target.getName())
                .replace("{amount}", String.valueOf(amount)));
    }

    @Async
    @Execute
    void getReputation(@Context Player sender) {
        PlayerUtil.sendMessage(sender, ((String) configService.get("reputation-get-message"))
                .replace("{user}", sender.getName())
                .replace("{amount}", String.valueOf(userService.getUser(sender.getName()).getReputation())));
    }

    @Async
    @Execute
    @Permission("reputation.get")
    void getReputation(@Context CommandSender sender, @Arg("игрок") Player target) {
        PlayerUtil.sendMessage(sender, ((String) configService.get("reputation-get-message"))
                .replace("{user}", target.getName())
                .replace("{amount}", String.valueOf(userService.getUser(target.getName()).getReputation())));
    }
}
