package ro.uvt.fi.dp.db;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class OperationRepository {
    private final JdbcTemplate jdbcTemplate;

    public OperationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public OperationRecord create(String type, String sourceIban, String destinationIban, double amount, String status) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO operations (type, source_iban, destination_iban, amount, status) VALUES (?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, type);
            ps.setString(2, sourceIban);
            ps.setString(3, destinationIban);
            ps.setDouble(4, amount);
            ps.setString(5, status);
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        OperationRecord record = new OperationRecord();
        record.id = key != null ? key.longValue() : null;
        record.type = type;
        record.sourceIban = sourceIban;
        record.destinationIban = destinationIban;
        record.amount = amount;
        record.status = status;
        record.createdAt = java.time.LocalDateTime.now();
        return record;
    }

    public Optional<OperationRecord> findById(long id) {
        return jdbcTemplate.query(
                "SELECT id, type, source_iban, destination_iban, amount, created_at, status FROM operations WHERE id = ?",
                rs -> rs.next() ? Optional.of(map(rs)) : Optional.empty(),
                id);
    }

    private OperationRecord map(java.sql.ResultSet rs) throws java.sql.SQLException {
        OperationRecord record = new OperationRecord();
        record.id = rs.getLong("id");
        record.type = rs.getString("type");
        record.sourceIban = rs.getString("source_iban");
        record.destinationIban = rs.getString("destination_iban");
        record.amount = rs.getDouble("amount");
        record.createdAt = rs.getTimestamp("created_at").toLocalDateTime();
        record.status = rs.getString("status");
        return record;
    }
}
