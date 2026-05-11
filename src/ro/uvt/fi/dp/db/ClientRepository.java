package ro.uvt.fi.dp.db;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class ClientRepository {
    private final JdbcTemplate jdbcTemplate;

    public ClientRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public ClientRecord create(long bankId, String name, String address, java.time.LocalDate birthDay, boolean premium) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO clients (bank_id, name, address, birth_day, premium) VALUES (?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, bankId);
            ps.setString(2, name);
            ps.setString(3, address);
            ps.setDate(4, birthDay != null ? Date.valueOf(birthDay) : null);
            ps.setBoolean(5, premium);
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        ClientRecord record = new ClientRecord();
        record.id = key != null ? key.longValue() : null;
        record.bankId = bankId;
        record.name = name;
        record.address = address;
        record.birthDay = birthDay;
        record.premium = premium;
        return record;
    }

    public Optional<ClientRecord> findById(long id) {
        return jdbcTemplate.query(
                "SELECT id, bank_id, name, address, birth_day, premium FROM clients WHERE id = ?",
                rs -> rs.next() ? Optional.of(map(rs)) : Optional.empty(),
                id);
    }

    private ClientRecord map(java.sql.ResultSet rs) throws java.sql.SQLException {
        ClientRecord record = new ClientRecord();
        record.id = rs.getLong("id");
        record.bankId = rs.getLong("bank_id");
        record.name = rs.getString("name");
        record.address = rs.getString("address");
        Date birth = rs.getDate("birth_day");
        record.birthDay = birth != null ? birth.toLocalDate() : null;
        record.premium = rs.getBoolean("premium");
        return record;
    }
}
