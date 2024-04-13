import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO {
    private Connection connection;

    public RoomDAO(Connection connection) {
        this.connection = connection;
    }

    public void addRoom(Room room) throws SQLException {
        String query = "INSERT INTO Rooms (room_number, room_type, is_available) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, room.getRoomNumber());
            statement.setString(2, room.getRoomType());
            statement.setBoolean(3, room.isAvailable());
            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                room.setRoomId(generatedKeys.getInt(1));
            }
        }
    }

    public void updateRoom(Room room) throws SQLException {
        String query = "UPDATE Rooms SET room_type = ?, is_available = ? WHERE room_number = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, room.getRoomType());
            statement.setBoolean(2, room.isAvailable());
            statement.setString(3, room.getRoomNumber());
            statement.executeUpdate();
        }
    }

    public void deleteRoom(String roomNumber) throws SQLException {
        String query = "DELETE FROM Rooms WHERE room_number = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, roomNumber);
            statement.executeUpdate();
        }
    }

    public Room getRoomByNumber(String roomNumber) throws SQLException {
        String query = "SELECT * FROM Rooms WHERE room_number = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, roomNumber);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return extractRoomFromResultSet(resultSet);
                }
            }
        }
        return null;
    }

    public List<Room> getAllRooms() throws SQLException {
        List<Room> rooms = new ArrayList<>();
        String query = "SELECT * FROM Rooms";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                rooms.add(extractRoomFromResultSet(resultSet));
            }
        }
        return rooms;
    }

    private Room extractRoomFromResultSet(ResultSet resultSet) throws SQLException {
        Room room = new Room();
        room.setRoomId(resultSet.getInt("room_id"));
        room.setRoomNumber(resultSet.getString("room_number"));
        room.setRoomType(resultSet.getString("room_type"));
        room.setAvailable(resultSet.getBoolean("is_available"));
        return room;
    }
}