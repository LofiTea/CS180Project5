import java.util.ArrayList;

/**
 * Project 5: SellerListing
 * <p>
 * This class returns a list of tickets and how many quantities for all items in the store.
 *
 * @author Shrish Mahesh, Lab Section L20
 * @version November 13, 2023
 */

public class SellerListing {
    private ArrayList<Ticket> tickets;
    private ArrayList<String> quantities;

    public SellerListing(ArrayList<Ticket> tickets, ArrayList<String> quantities) {
        this.tickets = tickets;
        this.quantities = quantities;
    }

    public ArrayList<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(ArrayList<Ticket> tickets) {
        this.tickets = tickets;
    }

    public ArrayList<String> getQuantities() {
        return quantities;
    }

    public void setQuantities(ArrayList<String> quantities) {
        this.quantities = quantities;
    }
}
