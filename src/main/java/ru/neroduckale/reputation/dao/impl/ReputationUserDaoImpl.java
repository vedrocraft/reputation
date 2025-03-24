package ru.neroduckale.reputation.dao.impl;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.ConnectionSource;
import lombok.NonNull;
import ru.neroduckale.reputation.dao.ReputationUserDao;
import ru.neroduckale.reputation.model.ReputationUser;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;

public class ReputationUserDaoImpl extends BaseDaoImpl<ReputationUser, Long> implements ReputationUserDao {
    public ReputationUserDaoImpl(ConnectionSource connectionSource, Class<ReputationUser> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    @Override
    public ReputationUser save(@NonNull ReputationUser user) throws SQLException {
        createOrUpdate(user);
        return user;
    }

    @Override
    public void saveAll(@NonNull List<ReputationUser> users) throws SQLException {
        callBatchTasks((Callable<Void>) () -> {
            for (ReputationUser user : users) {
                createOrUpdate(user);
            }
            return null;
        });
    }

    @Override
    public Optional<ReputationUser> findById(@NonNull Long id) throws SQLException {
        ReputationUser result = queryForId(id);
        return Optional.ofNullable(result);
    }

    @Override
    public Optional<ReputationUser> findByUsername(@NonNull String username) throws SQLException {
        QueryBuilder<ReputationUser, Long> queryBuilder = queryBuilder();
        Where<ReputationUser, Long> where = queryBuilder.where();
        String columnName = "username";

        SelectArg selectArg = new SelectArg(SqlType.STRING, username.toLowerCase());
        where.raw("LOWER(" + columnName + ")" + " = LOWER(?)", selectArg);
        return Optional.ofNullable(queryBuilder.queryForFirst());
    }

    @Override
    public List<ReputationUser> findAll() throws SQLException {
        return queryForAll();
    }
}
