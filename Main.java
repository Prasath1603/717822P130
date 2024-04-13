import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_database", "malli", "malli")) {
            // Initialize DAO objects
            RoomDAO roomDAO = new RoomDAO(connection);
            GuestDAO guestDAO = new GuestDAO(connection);
            BookingDAO bookingDAO = new BookingDAO(connection);
            PaymentDAO paymentDAO = new PaymentDAO(connection);

            Scanner scanner = new Scanner(System.in);
            int choice;

            while (true) {
                // Display menu options
                System.out.println("Welcome to MALLIPRASATH'S Hotel Management System");
                System.out.println("1. Manage Rooms");
                System.out.println("2. Manage Guests");
                System.out.println("3. Manage Bookings");
                System.out.println("4. Manage Payments");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline character

                switch (choice) {
                    case 1:
                        manageRooms(scanner, roomDAO);
                        break;
                    case 2:
                        manageGuests(scanner, guestDAO);
                        break;
                    case 3:
                        manageBookings(scanner, bookingDAO);
                        break;
                    case 4:
                        managePayments(scanner, paymentDAO);
                        break;
                    case 5:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void manageRooms(Scanner scanner, RoomDAO roomDAO) {
        int choice;

        while (true) {
            // Display room management menu options
            System.out.println("\nRoom Management");
            System.out.println("1. Add Room");
            System.out.println("2. Update Room");
            System.out.println("3. Delete Room");
            System.out.println("4. View All Rooms");
            System.out.println("5. Back to Main Menu");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            switch (choice) {
                case 1:
                    addRoom(scanner, roomDAO);
                    break;
                case 2:
                    updateRoom(scanner, roomDAO);
                    break;
                case 3:
                    deleteRoom(scanner, roomDAO);
                    break;
                case 4:
                    viewAllRooms(roomDAO);
                    break;
                case 5:
                    System.out.println("\n");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void addRoom(Scanner scanner, RoomDAO roomDAO) {
        // Get room details from user input
        System.out.println("Enter room number:");
        String roomNumber = scanner.nextLine();
        System.out.println("Enter room type:");
        String roomType = scanner.nextLine();
        System.out.println("Is the room available? (true/false):");
        boolean isAvailable = scanner.nextBoolean();

        // Create a new Room object
        Room room = new Room(roomNumber, roomType, isAvailable);

        try {
            // Add the room to the database
            roomDAO.addRoom(room);
            System.out.println("Room added successfully.");
        } catch (SQLException e) {
            System.out.println("Error adding room: " + e.getMessage());
        }
    }

    private static void updateRoom(Scanner scanner, RoomDAO roomDAO) {
        // Get room number to update
        System.out.println("Enter room number to update:");
        String roomNumber = scanner.nextLine();

        try {
            // Check if the room exists
            Room existingRoom = roomDAO.getRoomByNumber(roomNumber);
            if (existingRoom == null) {
                System.out.println("Room not found.");
                return;
            }

            // Get updated room details from user input
            System.out.println("Enter new room type:");
            String newRoomType = scanner.nextLine();
            System.out.println("Is the room available? (true/false):");
            boolean newIsAvailable = scanner.nextBoolean();

            // Update the room details
            existingRoom.setRoomType(newRoomType);
            existingRoom.setAvailable(newIsAvailable);

            // Update the room in the database
            roomDAO.updateRoom(existingRoom);
            System.out.println("Room updated successfully.");
        } catch (SQLException e) {
            System.out.println("Error updating room: " + e.getMessage());
        }
    }

    private static void deleteRoom(Scanner scanner, RoomDAO roomDAO) {
        // Get room number to delete
        System.out.println("Enter room number to delete:");
        String roomNumber = scanner.nextLine();

        try {
            // Delete the room from the database
            roomDAO.deleteRoom(roomNumber);
            System.out.println("Room deleted successfully.");
        } catch (SQLException e) {
            System.out.println("Error deleting room: " + e.getMessage());
        }
    }

    private static void viewAllRooms(RoomDAO roomDAO) {
        try {
            // Retrieve all rooms from the database
            List<Room> rooms = roomDAO.getAllRooms();

            // Display room details
            System.out.println("All Rooms:");
            for (Room room : rooms) {
                System.out.println("Room Number: " + room.getRoomNumber());
                System.out.println("Room Type: " + room.getRoomType());
                System.out.println("Is Available: " + room.isAvailable());
                System.out.println();
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving rooms: " + e.getMessage());
        }
    }


    private static void manageGuests(Scanner scanner, GuestDAO guestDAO) {
        int choice;

        while (true) {
            // Display guest management menu options
            System.out.println("\nGuest Management");
            System.out.println("1. Add Guest");
            System.out.println("2. Update Guest");
            System.out.println("3. Delete Guest");
            System.out.println("4. View All Guests");
            System.out.println("5. Back to Main Menu");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            switch (choice) {
                case 1:
                    addGuest(scanner, guestDAO);
                    break;
                case 2:
                    updateGuest(scanner, guestDAO);
                    break;
                case 3:
                    deleteGuest(scanner, guestDAO);
                    break;
                case 4:
                    viewAllGuests(guestDAO);
                    break;
                case 5:
                    System.out.println("\n");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void addGuest(Scanner scanner, GuestDAO guestDAO) {
        // Get guest details from user input
        System.out.println("Enter guest's first name:");
        String firstName = scanner.nextLine();
        System.out.println("Enter guest's last name:");
        String lastName = scanner.nextLine();
        System.out.println("Enter guest's email:");
        String email = scanner.nextLine();
        System.out.println("Enter guest's phone number:");
        String phoneNumber = scanner.nextLine();

        // Create a new Guest object
        Guest guest = new Guest(firstName, lastName, email, phoneNumber);

        try {
            // Add the guest to the database
            guestDAO.addGuest(guest);
            System.out.println("Guest added successfully.");
        } catch (SQLException e) {
            System.out.println("Error adding guest: " + e.getMessage());
        }
    }

    private static void updateGuest(Scanner scanner, GuestDAO guestDAO) {
        // Get guest ID to update
        System.out.println("Enter guest ID to update:");
        int guestId = scanner.nextInt();
        scanner.nextLine(); // Consume newline character

        try {
            // Check if the guest exists
            Guest existingGuest = guestDAO.getGuestById(guestId);
            if (existingGuest == null) {
                System.out.println("Guest not found.");
                return;
            }

            // Get updated guest details from user input
            System.out.println("Enter guest's first name:");
            String newFirstName = scanner.nextLine();
            System.out.println("Enter guest's last name:");
            String newLastName = scanner.nextLine();
            System.out.println("Enter guest's email:");
            String newEmail = scanner.nextLine();
            System.out.println("Enter guest's phone number:");
            String newPhoneNumber = scanner.nextLine();

            // Update the guest details
            existingGuest.setFirstName(newFirstName);
            existingGuest.setLastName(newLastName);
            existingGuest.setEmail(newEmail);
            existingGuest.setPhoneNumber(newPhoneNumber);

            // Update the guest in the database
            guestDAO.updateGuest(existingGuest);
            System.out.println("Guest updated successfully.");
        } catch (SQLException e) {
            System.out.println("Error updating guest: " + e.getMessage());
        }
    }

    private static void deleteGuest(Scanner scanner, GuestDAO guestDAO) {
        // Get guest ID to delete
        System.out.println("Enter guest ID to delete:");
        int guestId = scanner.nextInt();
        scanner.nextLine(); // Consume newline character

        try {
            // Delete the guest from the database
            guestDAO.deleteGuest(guestId);
            System.out.println("Guest deleted successfully.");
        } catch (SQLException e) {
            System.out.println("Error deleting guest: " + e.getMessage());
        }
    }

    private static void viewAllGuests(GuestDAO guestDAO) {
        try {
            // Retrieve all guests from the database
            List<Guest> guests = guestDAO.getAllGuests();

            // Display guest details
            System.out.println("All Guests:");
            for (Guest guest : guests) {
                System.out.println("Guest ID: " + guest.getGuestId());
                System.out.println("Name: " + guest.getFirstName() + " " + guest.getLastName());
                System.out.println("Email: " + guest.getEmail());
                System.out.println("Phone Number: " + guest.getPhoneNumber());
                System.out.println();
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving guests: " + e.getMessage());
        }
    }

    private static void manageBookings(Scanner scanner, BookingDAO bookingDAO) {
        int choice;

        while (true) {
            // Display booking management menu options
            System.out.println("\nBooking Management");
            System.out.println("1. Add Booking");
            System.out.println("2. Update Booking");
            System.out.println("3. Delete Booking");
            System.out.println("4. View All Bookings");
            System.out.println("5. Back to Main Menu");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            switch (choice) {
                case 1:
                    addBooking(scanner, bookingDAO);
                    break;
                case 2:
                    updateBooking(scanner, bookingDAO);
                    break;
                case 3:
                    deleteBooking(scanner, bookingDAO);
                    break;
                case 4:
                    viewAllBookings(bookingDAO);
                    break;
                case 5:
                    System.out.println("\n");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void addBooking(Scanner scanner, BookingDAO bookingDAO) {
        // Get booking details from user input
        System.out.println("Enter guest ID:");
        int guestId = scanner.nextInt();
        scanner.nextLine(); // Consume newline character
        System.out.println("Enter room ID:");
        int roomId = scanner.nextInt();
        scanner.nextLine(); // Consume newline character
        System.out.println("Enter check-in date (YYYY-MM-DD):");
        String checkInDateStr = scanner.nextLine();
        System.out.println("Enter check-out date (YYYY-MM-DD):");
        String checkOutDateStr = scanner.nextLine();

        try {
            // Parse check-in and check-out dates
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date checkInDate = dateFormat.parse(checkInDateStr);
            Date checkOutDate = dateFormat.parse(checkOutDateStr);

            // Create a new Booking object
            Booking booking = new Booking(guestId, roomId, checkInDate, checkOutDate);

            // Add the booking to the database
            bookingDAO.addBooking(booking);
            System.out.println("Booking added successfully.");
        } catch (ParseException | SQLException e) {
            System.out.println("Error adding booking: " + e.getMessage());
        }
    }

    private static void updateBooking(Scanner scanner, BookingDAO bookingDAO) {
        // Get booking ID to update
        System.out.println("Enter booking ID to update:");
        int bookingId = scanner.nextInt();
        scanner.nextLine(); // Consume newline character

        try {
            // Check if the booking exists
            Booking existingBooking = bookingDAO.getBookingById(bookingId);
            if (existingBooking == null) {
                System.out.println("Booking not found.");
                return;
            }

            // Get updated booking details from user input
            System.out.println("Enter guest ID:");
            int newGuestId = scanner.nextInt();
            scanner.nextLine(); // Consume newline character
            System.out.println("Enter room ID:");
            int newRoomId = scanner.nextInt();
            scanner.nextLine(); // Consume newline character
            System.out.println("Enter check-in date (YYYY-MM-DD):");
            String newCheckInDateStr = scanner.nextLine();
            System.out.println("Enter check-out date (YYYY-MM-DD):");
            String newCheckOutDateStr = scanner.nextLine();

            // Parse updated check-in and check-out dates
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date newCheckInDate = dateFormat.parse(newCheckInDateStr);
            Date newCheckOutDate = dateFormat.parse(newCheckOutDateStr);

            // Update the booking details
            existingBooking.setGuestId(newGuestId);
            existingBooking.setRoomId(newRoomId);
            existingBooking.setCheckInDate(newCheckInDate);
            existingBooking.setCheckOutDate(newCheckOutDate);

            // Update the booking in the database
            bookingDAO.updateBooking(existingBooking);
            System.out.println("Booking updated successfully.");
        } catch (ParseException | SQLException e) {
            System.out.println("Error updating booking: " + e.getMessage());
        }
    }

    private static void deleteBooking(Scanner scanner, BookingDAO bookingDAO) {
        // Get booking ID to delete
        System.out.println("Enter booking ID to delete:");
        int bookingId = scanner.nextInt();
        scanner.nextLine(); // Consume newline character

        try {
            // Delete the booking from the database
            bookingDAO.deleteBooking(bookingId);
            System.out.println("Booking deleted successfully.");
        } catch (SQLException e) {
            System.out.println("Error deleting booking: " + e.getMessage());
        }
    }

    private static void viewAllBookings(BookingDAO bookingDAO) {
        try {
            // Retrieve all bookings from the database
            List<Booking> bookings = bookingDAO.getAllBookings();

            // Display booking details
            System.out.println("All Bookings:");
            for (Booking booking : bookings) {
                System.out.println("Booking ID: " + booking.getBookingId());
                System.out.println("Guest ID: " + booking.getGuestId());
                System.out.println("Room ID: " + booking.getRoomId());
                System.out.println("Check-In Date: " + booking.getCheckInDate());
                System.out.println("Check-Out Date: " + booking.getCheckOutDate());
                System.out.println();
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving bookings: " + e.getMessage());
        }
    }


    private static void managePayments(Scanner scanner, PaymentDAO paymentDAO) {
        int choice;

        while (true) {
            // Display payment management menu options
            System.out.println("\nPayment Management");
            System.out.println("1. Add Payment");
            System.out.println("2. Update Payment");
            System.out.println("3. Delete Payment");
            System.out.println("4. View All Payments");
            System.out.println("5. Back to Main Menu");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            switch (choice) {
                case 1:
                    addPayment(scanner, paymentDAO);
                    break;
                case 2:
                    updatePayment(scanner, paymentDAO);
                    break;
                case 3:
                    deletePayment(scanner, paymentDAO);
                    break;
                case 4:
                    viewAllPayments(paymentDAO);
                    break;
                case 5:
                    System.out.println("\n");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void addPayment(Scanner scanner, PaymentDAO paymentDAO) {
        // Get payment details from user input
        System.out.println("Enter booking ID:");
        int bookingId = scanner.nextInt();
        scanner.nextLine(); // Consume newline character
        System.out.println("Enter payment date (YYYY-MM-DD):");
        String paymentDateStr = scanner.nextLine();
        System.out.println("Enter payment amount:");
        BigDecimal amount = scanner.nextBigDecimal();
        scanner.nextLine(); // Consume newline character

        try {
            // Parse payment date
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date paymentDate = dateFormat.parse(paymentDateStr);

            // Create a new Payment object
            Payment payment = new Payment(bookingId, paymentDate, amount);

            // Add the payment to the database
            paymentDAO.addPayment(payment);
            System.out.println("Payment added successfully.");
        } catch (ParseException | SQLException e) {
            System.out.println("Error adding payment: " + e.getMessage());
        }
    }

    private static void updatePayment(Scanner scanner, PaymentDAO paymentDAO) {
        // Get payment ID to update
        System.out.println("Enter payment ID to update:");
        int paymentId = scanner.nextInt();
        scanner.nextLine(); // Consume newline character

        try {
            // Check if the payment exists
            Payment existingPayment = paymentDAO.getPaymentById(paymentId);
            if (existingPayment == null) {
                System.out.println("Payment not found.");
                return;
            }

            // Get updated payment details from user input
            System.out.println("Enter booking ID:");
            int newBookingId = scanner.nextInt();
            scanner.nextLine(); // Consume newline character
            System.out.println("Enter payment date (YYYY-MM-DD):");
            String newPaymentDateStr = scanner.nextLine();
            System.out.println("Enter payment amount:");
            BigDecimal newAmount = scanner.nextBigDecimal();
            scanner.nextLine(); // Consume newline character

            // Parse updated payment date
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date newPaymentDate = dateFormat.parse(newPaymentDateStr);

            // Update the payment details
            existingPayment.setBookingId(newBookingId);
            existingPayment.setPaymentDate(newPaymentDate);
            existingPayment.setAmount(newAmount);

            // Update the payment in the database
            paymentDAO.updatePayment(existingPayment);
            System.out.println("Payment updated successfully.");
        } catch (ParseException | SQLException e) {
            System.out.println("Error updating payment: " + e.getMessage());
        }
    }

    private static void deletePayment(Scanner scanner, PaymentDAO paymentDAO) {
        // Get payment ID to delete
        System.out.println("Enter payment ID to delete:");
        int paymentId = scanner.nextInt();
        scanner.nextLine(); // Consume newline character

        try {
            // Delete the payment from the database
            paymentDAO.deletePayment(paymentId);
            System.out.println("Payment deleted successfully.");
        } catch (SQLException e) {
            System.out.println("Error deleting payment: " + e.getMessage());
        }
    }

    private static void viewAllPayments(PaymentDAO paymentDAO) {
        try {
            // Retrieve all payments from the database
            List<Payment> payments = paymentDAO.getAllPayments();

            // Display payment details
            System.out.println("All Payments:");
            for (Payment payment : payments) {
                System.out.println("Payment ID: " + payment.getPaymentId());
                System.out.println("Booking ID: " + payment.getBookingId());
                System.out.println("Payment Date: " + payment.getPaymentDate());
                System.out.println("Amount: " + payment.getAmount());
                System.out.println();
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving payments: " + e.getMessage());
        }
    }

}
