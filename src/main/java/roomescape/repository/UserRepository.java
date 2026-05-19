package roomescape.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.EntityId;
import roomescape.domain.User;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public Optional<User> findByLoginIdAndPassword(
            String loginId,
            String password
    ) {
        try {
            String findSql = "SELECT id, login_id, password, name"
                    + " FROM users"
                    + " WHERE login_id = ? AND password = ?";

            User user = jdbcTemplate.queryForObject(
                    findSql,
                    userRowMapper(),
                    loginId,
                    password
            );

            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    private RowMapper<User> userRowMapper() {
        return (resultSet, rowNum) -> new User(
                readEntityId(resultSet, "id"),
                resultSet.getString("login_id"),
                resultSet.getString("password"),
                resultSet.getString("name")
        );
    }

    private EntityId readEntityId(ResultSet resultSet, String column) throws SQLException {
        UUID uuid = resultSet.getObject(column, UUID.class);
        return EntityId.fromUuid(uuid);
    }
}
