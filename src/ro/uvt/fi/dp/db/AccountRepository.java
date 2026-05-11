package ro.uvt.fi.dp.db;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class AccountRepository {
    private final JdbcTemplate jdbcTemplate;

    public AccountRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public AccountRecord create(long clientId, String iban, String currency, double balance) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO accounts (client_id, iban, currency, balance) VALUES (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, clientId);
            ps.setString(2, iban);
            ps.setString(3, currency);
            ps.setDouble(4, balance);
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        AccountRecord record = new AccountRecord();
        record.id = key != null ? key.longValue() : null;
        record.clientId = clientId;
        record.iban = iban;
        record.currency = currency;
        record.balance = balance;
        return record;
    }

    public Optional<AccountRecord> findByIban(String iban) {
        return jdbcTemplate.query(
                "SELECT id, client_id, iban, currency, balance FROM accounts WHERE iban = ?",
                rs -> rs.next() ? Optional.of(map(rs)) : Optional.empty(),
                iban);
    }

    public int updateBalance(String iban, double balance) {
        return jdbcTemplate.update(
                "UPDATE accounts SET balance = ? WHERE iban = ?",
                balance, iban);
    }

    private AccountRecord map(java.sql.ResultSet rs) throws java.sql.SQLException {
        AccountRecord record = new AccountRecord();
        record.id = rs.getLong("id");
        record.clientId = rs.getLong("client_id");
        record.iban = rs.getString("iban");
        record.currency = rs.getString("currency");
        record.balance = rs.getDouble("balance");
        return record;
    }
}
