import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {
    private Connection connection;

    public PaymentDAO(Connection connection) {
        this.connection = connection;
    }

    public void addPayment(Payment payment) throws SQLException {
        String query = "INSERT INTO Payments (booking_id, payment_date, amount) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, payment.getBookingId());
            statement.setDate(2, new java.sql.Date(payment.getPaymentDate().getTime()));
            statement.setBigDecimal(3, payment.getAmount());
            statement.executeUpdate();
        }
    }

    public void updatePayment(Payment payment) throws SQLException {
        String query = "UPDATE Payments SET booking_id = ?, payment_date = ?, amount = ? WHERE payment_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, payment.getBookingId());
            statement.setDate(2, new java.sql.Date(payment.getPaymentDate().getTime()));
            statement.setBigDecimal(3, payment.getAmount());
            statement.setInt(4, payment.getPaymentId());
            statement.executeUpdate();
        }
    }

    public void deletePayment(int paymentId) throws SQLException {
        String query = "DELETE FROM Payments WHERE payment_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, paymentId);
            statement.executeUpdate();
        }
    }

    public List<Payment> getAllPayments() throws SQLException {
        List<Payment> payments = new ArrayList<>();
        String query = "SELECT * FROM Payments";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Payment payment = new Payment();
                payment.setPaymentId(resultSet.getInt("payment_id"));
                payment.setBookingId(resultSet.getInt("booking_id"));
                payment.setPaymentDate(resultSet.getDate("payment_date"));
                payment.setAmount(resultSet.getBigDecimal("amount"));
                payments.add(payment);
            }
        }
        return payments;
    }
    public Payment getPaymentById(int paymentId) throws SQLException {
        String query = "SELECT * FROM Payments WHERE payment_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, paymentId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return extractPaymentFromResultSet(resultSet);
                }
            }
        }
        return null;
    }
    private Payment extractPaymentFromResultSet(ResultSet resultSet) throws SQLException {
        Payment payment = new Payment();
        payment.setPaymentId(resultSet.getInt("payment_id"));
        payment.setBookingId(resultSet.getInt("booking_id"));
        payment.setPaymentDate(resultSet.getDate("payment_date"));
        payment.setAmount(resultSet.getBigDecimal("amount"));
        return payment;
    }

}
