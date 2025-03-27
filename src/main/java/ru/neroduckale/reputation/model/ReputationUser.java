package ru.neroduckale.reputation.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.neroduckale.reputation.dao.impl.ReputationUserDaoImpl;
import ru.neroduckale.reputation.service.Language;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DatabaseTable(tableName = "players", daoClass = ReputationUserDaoImpl.class)
public class ReputationUser {
    @DatabaseField(unique = true, generatedId = true)
    private Long id;

    @DatabaseField(canBeNull = false)
    private String username;

    @DatabaseField(canBeNull = false)
    private int reputation;

    @DatabaseField
    private Language lang;
}
