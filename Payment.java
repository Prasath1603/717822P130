import java.math.BigDecimal;
import java.util.Date;

class Payment {
    private int paymentId;
    private int bookingId;
    private Date paymentDate;
    private BigDecimal amount;

    public Payment() {}

    public Payment(int bookingId, Date paymentDate, BigDecimal amount) {
        this.bookingId = bookingId;
        this.paymentDate = paymentDate;
        this.amount = amount;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

}