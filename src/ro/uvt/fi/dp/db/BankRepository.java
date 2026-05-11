package ro.uvt.fi.dp.db;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class BankRepository {
    private final JdbcTemplate jdbcTemplate;

    public BankRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public BankRecord create(String code, String name, String address) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO banks (code, name, address) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, code);
            ps.setString(2, name);
            ps.setString(3, address);
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        BankRecord record = new BankRecord();
        record.id = key != null ? key.longValue() : null;
        record.code = code;
        record.name = name;
        record.address = address;
        return record;
    }

    public Optional<BankRecord> findById(long id) {
        return jdbcTemplate.query(
                "SELECT id, code, name, address FROM banks WHERE id = ?",
                rs -> rs.next() ? Optional.of(map(rs)) : Optional.empty(),
                id);
    }

    public Optional<BankRecord> findByCode(String code) {
        return jdbcTemplate.query(
                "SELECT id, code, name, address FROM banks WHERE code = ?",
                rs -> rs.next() ? Optional.of(map(rs)) : Optional.empty(),
                code);
    }

    private BankRecord map(java.sql.ResultSet rs) throws java.sql.SQLException {
        BankRecord record = new BankRecord();
        record.id = rs.getLong("id");
        record.code = rs.getString("code");
        record.name = rs.getString("name");
        record.address = rs.getString("address");
        return record;
    }
}
