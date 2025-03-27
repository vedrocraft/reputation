package ru.neroduckale.reputation.service;

import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import ru.neroduckale.reputation.model.ReputationUser;
import ru.sema1ary.vedrocraftapi.service.UserService;

public interface ReputationUserService extends UserService<ReputationUser> {
    void addReputation(@NonNull String username, int amount);

    void removeReputation(@NonNull String username, int amount);

    void setLang(@NotNull String username, Language lang);
}
