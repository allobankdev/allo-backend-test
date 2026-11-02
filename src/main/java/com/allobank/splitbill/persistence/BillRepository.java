package com.allobank.splitbill.persistence;

import com.allobank.splitbill.domain.PaymentType;
import com.allobank.splitbill.domain.SplitType;
import com.allobank.splitbill.domain.TaxMode;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class BillRepository {
    private final JdbcTemplate jdbc;

    public BillRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public record UserRow(UUID id, String username, String displayName, Instant createdAt) {}
    public record GroupRow(UUID id, String name, String currency, Instant createdAt) {}
    public record ParticipantRow(UUID id, UUID groupId, UUID userId, String username,
                                 String displayName, int position) {}
    public record ExpenseRow(UUID id, UUID groupId, String description, BigDecimal inputAmount,
                             TaxMode taxMode, BigDecimal taxRatePct, BigDecimal subtotal,
                             BigDecimal taxAmount, BigDecimal amount, UUID paidBy,
                             SplitType splitType, Instant createdAt) {}
    public record ShareRow(UUID expenseId, UUID participantId, BigDecimal subtotal,
                           BigDecimal taxAmount, BigDecimal amount) {}
    public record PaymentRow(UUID id, UUID groupId, UUID payerId, UUID receiverId,
                             BigDecimal amount, PaymentType type, UUID reversalOf,
                             Instant createdAt) {}

    public void insertUser(UserRow user) {
        jdbc.update("INSERT INTO users (id, username, display_name, created_at) VALUES (?, ?, ?, ?)",
                user.id(), user.username(), user.displayName(), Timestamp.from(user.createdAt()));
    }

    public Optional<UserRow> findUserByUsername(String username) {
        return jdbc.query("SELECT id, username, display_name, created_at FROM users WHERE username = ?",
                (rs, row) -> new UserRow(rs.getObject("id", UUID.class), rs.getString("username"),
                        rs.getString("display_name"), rs.getTimestamp("created_at").toInstant()),
                username).stream().findFirst();
    }

    public Optional<UserRow> findUser(UUID userId) {
        return jdbc.query("SELECT id, username, display_name, created_at FROM users WHERE id = ?",
                (rs, row) -> new UserRow(rs.getObject("id", UUID.class), rs.getString("username"),
                        rs.getString("display_name"), rs.getTimestamp("created_at").toInstant()),
                userId).stream().findFirst();
    }

    public void insertGroup(GroupRow group) {
        jdbc.update("INSERT INTO bill_groups (id, name, currency, created_at) VALUES (?, ?, ?, ?)",
                group.id(), group.name(), group.currency(), Timestamp.from(group.createdAt()));
    }

    public void insertParticipant(ParticipantRow participant) {
        jdbc.update("INSERT INTO participants (id, group_id, user_id, name, position) VALUES (?, ?, ?, ?, ?)",
                participant.id(), participant.groupId(), participant.userId(),
                participant.displayName(), participant.position());
    }

    public Optional<GroupRow> findGroup(UUID groupId) {
        return jdbc.query("SELECT id, name, currency, created_at FROM bill_groups WHERE id = ?",
                (rs, row) -> new GroupRow(rs.getObject("id", UUID.class), rs.getString("name"),
                        rs.getString("currency"), rs.getTimestamp("created_at").toInstant()),
                groupId).stream().findFirst();
    }

    public List<GroupRow> findGroupsForUser(UUID userId) {
        return jdbc.query("SELECT g.id, g.name, g.currency, g.created_at "
                        + "FROM bill_groups g JOIN participants p ON p.group_id = g.id "
                        + "WHERE p.user_id = ? ORDER BY g.created_at, g.id",
                (rs, row) -> new GroupRow(rs.getObject("id", UUID.class), rs.getString("name"),
                        rs.getString("currency"), rs.getTimestamp("created_at").toInstant()),
                userId);
    }

    public List<ParticipantRow> findParticipants(UUID groupId) {
        return jdbc.query("SELECT p.id, p.group_id, p.user_id, u.username, u.display_name, p.position "
                        + "FROM participants p JOIN users u ON u.id = p.user_id "
                        + "WHERE p.group_id = ? ORDER BY p.position",
                (rs, row) -> new ParticipantRow(rs.getObject("id", UUID.class),
                        rs.getObject("group_id", UUID.class), rs.getObject("user_id", UUID.class),
                        rs.getString("username"), rs.getString("display_name"), rs.getInt("position")),
                groupId);
    }

    public Optional<ParticipantRow> findParticipantForUser(UUID groupId, UUID userId) {
        return jdbc.query("SELECT p.id, p.group_id, p.user_id, u.username, u.display_name, p.position "
                        + "FROM participants p JOIN users u ON u.id = p.user_id "
                        + "WHERE p.group_id = ? AND p.user_id = ?",
                (rs, row) -> new ParticipantRow(rs.getObject("id", UUID.class),
                        rs.getObject("group_id", UUID.class), rs.getObject("user_id", UUID.class),
                        rs.getString("username"), rs.getString("display_name"), rs.getInt("position")),
                groupId, userId).stream().findFirst();
    }

    public void insertExpense(ExpenseRow expense, List<ShareRow> shares) {
        jdbc.update("INSERT INTO expenses (id, group_id, description, input_amount, tax_mode, tax_rate_pct, "
                        + "subtotal, tax_amount, amount, paid_by, split_type, created_at) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                expense.id(), expense.groupId(), expense.description(), expense.inputAmount(),
                expense.taxMode().name(), expense.taxRatePct(), expense.subtotal(), expense.taxAmount(),
                expense.amount(), expense.paidBy(), expense.splitType().name(),
                Timestamp.from(expense.createdAt()));
        shares.forEach(share -> jdbc.update(
                "INSERT INTO expense_shares (expense_id, participant_id, subtotal, tax_amount, amount) "
                        + "VALUES (?, ?, ?, ?, ?)",
                share.expenseId(), share.participantId(), share.subtotal(), share.taxAmount(), share.amount()));
    }

    public List<ExpenseRow> findExpenses(UUID groupId) {
        return jdbc.query("SELECT id, group_id, description, input_amount, tax_mode, tax_rate_pct, "
                        + "subtotal, tax_amount, amount, paid_by, split_type, created_at "
                        + "FROM expenses WHERE group_id = ? ORDER BY created_at, id",
                (rs, row) -> mapExpense(rs), groupId);
    }

    public Optional<ExpenseRow> findExpense(UUID groupId, UUID expenseId) {
        return jdbc.query("SELECT id, group_id, description, input_amount, tax_mode, tax_rate_pct, "
                        + "subtotal, tax_amount, amount, paid_by, split_type, created_at "
                        + "FROM expenses WHERE group_id = ? AND id = ?",
                (rs, row) -> mapExpense(rs), groupId, expenseId).stream().findFirst();
    }

    public List<ShareRow> findShares(UUID groupId) {
        return jdbc.query("SELECT s.expense_id, s.participant_id, s.subtotal, s.tax_amount, s.amount "
                        + "FROM expense_shares s "
                        + "JOIN expenses e ON e.id = s.expense_id "
                        + "JOIN participants p ON p.id = s.participant_id "
                        + "WHERE e.group_id = ? ORDER BY e.created_at, e.id, p.position",
                (rs, row) -> new ShareRow(rs.getObject("expense_id", UUID.class),
                        rs.getObject("participant_id", UUID.class), rs.getBigDecimal("subtotal"),
                        rs.getBigDecimal("tax_amount"), rs.getBigDecimal("amount")), groupId);
    }

    public void insertPayment(PaymentRow payment) {
        jdbc.update("INSERT INTO payments (id, group_id, payer_id, receiver_id, amount, payment_type, "
                        + "reversal_of, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                payment.id(), payment.groupId(), payment.payerId(), payment.receiverId(), payment.amount(),
                payment.type().name(), payment.reversalOf(), Timestamp.from(payment.createdAt()));
    }

    public List<PaymentRow> findPayments(UUID groupId) {
        return jdbc.query("SELECT id, group_id, payer_id, receiver_id, amount, payment_type, reversal_of, created_at "
                        + "FROM payments WHERE group_id = ? ORDER BY created_at, id",
                (rs, row) -> mapPayment(rs), groupId);
    }

    public Optional<PaymentRow> findPayment(UUID groupId, UUID paymentId) {
        return jdbc.query("SELECT id, group_id, payer_id, receiver_id, amount, payment_type, reversal_of, created_at "
                        + "FROM payments WHERE group_id = ? AND id = ?",
                (rs, row) -> mapPayment(rs), groupId, paymentId).stream().findFirst();
    }

    public Optional<PaymentRow> findReversalFor(UUID paymentId) {
        return jdbc.query("SELECT id, group_id, payer_id, receiver_id, amount, payment_type, reversal_of, created_at "
                        + "FROM payments WHERE reversal_of = ?",
                (rs, row) -> mapPayment(rs), paymentId).stream().findFirst();
    }

    private ExpenseRow mapExpense(java.sql.ResultSet rs) throws java.sql.SQLException {
        return new ExpenseRow(rs.getObject("id", UUID.class), rs.getObject("group_id", UUID.class),
                rs.getString("description"), rs.getBigDecimal("input_amount"),
                TaxMode.valueOf(rs.getString("tax_mode")), rs.getBigDecimal("tax_rate_pct"),
                rs.getBigDecimal("subtotal"), rs.getBigDecimal("tax_amount"), rs.getBigDecimal("amount"),
                rs.getObject("paid_by", UUID.class), SplitType.valueOf(rs.getString("split_type")),
                rs.getTimestamp("created_at").toInstant());
    }

    private PaymentRow mapPayment(java.sql.ResultSet rs) throws java.sql.SQLException {
        return new PaymentRow(rs.getObject("id", UUID.class), rs.getObject("group_id", UUID.class),
                rs.getObject("payer_id", UUID.class), rs.getObject("receiver_id", UUID.class),
                rs.getBigDecimal("amount"), PaymentType.valueOf(rs.getString("payment_type")),
                rs.getObject("reversal_of", UUID.class), rs.getTimestamp("created_at").toInstant());
    }
}
