package ru.neroduckale.reputation.service.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ru.neroduckale.reputation.dao.ReputationUserDao;
import ru.neroduckale.reputation.model.ReputationUser;
import ru.neroduckale.reputation.service.ReputationUserService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class ReputationUserServiceImpl implements ReputationUserService {
    private final ReputationUserDao userDao;

    @Override
    public ReputationUser save(@NonNull ReputationUser user) {
        try {
            return userDao.save(user);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveAll(@NonNull List<ReputationUser> users) {
        try {
            userDao.saveAll(users);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<ReputationUser> findById(@NonNull Long id) {
        try {
            return userDao.findById(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<ReputationUser> findByUsername(@NonNull String username) {
        try {
            return userDao.findByUsername(username);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ReputationUser> findAll() {
        try {
            return userDao.findAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ReputationUser getUser(@NonNull String username) {
        return findByUsername(username).orElseGet(() -> save(ReputationUser.builder()
                .username(username)
                .reputation(5000)
                .build()));
    }


    @Override
    public void addReputation(@NonNull String username, int amount) {
        ReputationUser user = getUser(username);
        user.setReputation(user.getReputation() + amount);
        save(user);
    }

    @Override
    public void removeReputation(@NonNull String username, int amount) {
        ReputationUser user = getUser(username);
        user.setReputation(user.getReputation() - amount);
        save(user);
    }


}