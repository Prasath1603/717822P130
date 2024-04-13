import java.sql.*;
import java.util.ArrayList;
import java.util.List;

class GuestDAO {
    private Connection connection;

    public GuestDAO(Connection connection) {
        this.connection = connection;
    }

    public void addGuest(Guest guest) throws SQLException {
        String query = "INSERT INTO Guests (first_name, last_name, email, phone_number) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, guest.getFirstName());
            statement.setString(2, guest.getLastName());
            statement.setString(3, guest.getEmail());
            statement.setString(4, guest.getPhoneNumber());
            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                guest.setGuestId(generatedKeys.getInt(1));
            }
        }
    }

    public void updateGuest(Guest guest) throws SQLException {
        String query = "UPDATE Guests SET first_name = ?, last_name = ?, email = ?, phone_number = ? WHERE guest_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, guest.getFirstName());
            statement.setString(2, guest.getLastName());
            statement.setString(3, guest.getEmail());
            statement.setString(4, guest.getPhoneNumber());
            statement.setInt(5, guest.getGuestId());
            statement.executeUpdate();
        }
    }

    public void deleteGuest(int guestId) throws SQLException {
        String query = "DELETE FROM Guests WHERE guest_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, guestId);
            statement.executeUpdate();
        }
    }

    public Guest getGuestById(int guestId) throws SQLException {
        String query = "SELECT * FROM Guests WHERE guest_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, guestId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return extractGuestFromResultSet(resultSet);
                }
            }
        }
        return null;
    }

    public List<Guest> getAllGuests() throws SQLException {
        List<Guest> guests = new ArrayList<>();
        String query = "SELECT * FROM Guests";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                guests.add(extractGuestFromResultSet(resultSet));
            }
        }
        return guests;
    }

    private Guest extractGuestFromResultSet(ResultSet resultSet) throws SQLException {
        Guest guest = new Guest();
        guest.setGuestId(resultSet.getInt("guest_id"));
        guest.setFirstName(resultSet.getString("first_name"));
        guest.setLastName(resultSet.getString("last_name"));
        guest.setEmail(resultSet.getString("email"));
        guest.setPhoneNumber(resultSet.getString("phone_number"));
        return guest;
    }
}
