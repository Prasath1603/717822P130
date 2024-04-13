import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {
    private Connection connection;

    public BookingDAO(Connection connection) {
        this.connection = connection;
    }

    public void addBooking(Booking booking) throws SQLException {
        String query = "INSERT INTO Bookings (guest_id, room_id, check_in_date, check_out_date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, booking.getGuestId());
            statement.setInt(2, booking.getRoomId());
            statement.setDate(3, new java.sql.Date(booking.getCheckInDate().getTime())); // Convert java.util.Date to java.sql.Date
            statement.setDate(4, new java.sql.Date(booking.getCheckOutDate().getTime())); // Convert java.util.Date to java.sql.Date
            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                booking.setBookingId(generatedKeys.getInt(1));
            }
        }
    }

    public void updateBooking(Booking booking) throws SQLException {
        String query = "UPDATE Bookings SET guest_id = ?, room_id = ?, check_in_date = ?, check_out_date = ? WHERE booking_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, booking.getGuestId());
            statement.setInt(2, booking.getRoomId());
            statement.setDate(3, new java.sql.Date(booking.getCheckInDate().getTime())); // Convert java.util.Date to java.sql.Date
            statement.setDate(4, new java.sql.Date(booking.getCheckOutDate().getTime())); // Convert java.util.Date to java.sql.Date
            statement.setInt(5, booking.getBookingId());
            statement.executeUpdate();
        }
    }

    public void deleteBooking(int bookingId) throws SQLException {
        String query = "DELETE FROM Bookings WHERE booking_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, bookingId);
            statement.executeUpdate();
        }
    }

    public Booking getBookingById(int bookingId) throws SQLException {
        String query = "SELECT * FROM Bookings WHERE booking_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, bookingId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return extractBookingFromResultSet(resultSet);
                }
            }
        }
        return null;
    }

    public List<Booking> getAllBookings() throws SQLException {
        List<Booking> bookings = new ArrayList<>();
        String query = "SELECT * FROM Bookings";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                bookings.add(extractBookingFromResultSet(resultSet));
            }
        }
        return bookings;
    }

    private Booking extractBookingFromResultSet(ResultSet resultSet) throws SQLException {
        Booking booking = new Booking();
        booking.setBookingId(resultSet.getInt("booking_id"));
        booking.setGuestId(resultSet.getInt("guest_id"));
        booking.setRoomId(resultSet.getInt("room_id"));
        booking.setCheckInDate(resultSet.getDate("check_in_date"));
        booking.setCheckOutDate(resultSet.getDate("check_out_date"));
        return booking;
    }
}
