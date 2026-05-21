package roomescape.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.EntityId;
import roomescape.domain.Shop;

@Repository
public class ShopRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ShopRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("shop");
    }

    public List<Shop> findAll() {
        String findSql = "SELECT id, name FROM shop ORDER BY name";
        return jdbcTemplate.query(findSql, shopRowMapper());
    }

    public Optional<Shop> findById(EntityId id) {
        try {
            String findSql = "SELECT id, name"
                    + " FROM shop"
                    + " WHERE id = ?";

            Shop shop = jdbcTemplate.queryForObject(
                    findSql,
                    shopRowMapper(),
                    id.getValueAsUuid()
            );
            return Optional.ofNullable(shop);
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    private RowMapper<Shop> shopRowMapper() {
        return (resultSet, rowNum) -> {
            EntityId id = readEntityId(resultSet, "id");
            String name = resultSet.getString("name");

            return new Shop(id, name);
        };
    }

    private EntityId readEntityId(ResultSet resultSet, String column) throws SQLException {
        UUID uuid = resultSet.getObject(column, UUID.class);

        return EntityId.fromUuid(uuid);
    }
}
