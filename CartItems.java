/**
 * Project 4: CartItems
 *
 * Allows a buyer to buy more than 1 ticket and allows sellers to sell more than 1 ticket.
 *
 * @author Rahul Siddharth, Lab Section L20
 * @version October 30, 2023
 */

public class CartItems {
    private Ticket ticket;
    private int qty;

    public CartItems(Ticket ticket, int qty) {
        this.ticket = ticket;
        this.qty = qty;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public int getQTY() {
        return this.qty;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public void setQTY(int qty) {
        this.qty = qty;
    }

    public String toString() {

        return String.format("%s|%d", ticket.toString(), qty);
    }
}
