import java.util.ArrayList;

/**
 * Project 5: TicketInfoCombo
 * <p>
 * This class returns the type and number of tickets that buyers can from sellers in the marketplace.
 *
 * @author Rahul Siddharth, Lab Section L20
 * @version November 13, 2023
 */

public class TicketInfoCombo {
    private ArrayList<String[]> listedTicks;
    private int howManyTicks;

    public TicketInfoCombo(ArrayList<String[]> listedTicks, int howManyTicks) {
        this.listedTicks = listedTicks;
        this.howManyTicks = howManyTicks;
    }

    public ArrayList<String[]> getListedTicks() {
        return listedTicks;
    }

    public void setListedTicks(ArrayList<String[]> listedTicks) {
        this.listedTicks = listedTicks;
    }

    public int getHowManyTicks() {
        return howManyTicks;
    }

    public void setHowManyTicks(int howManyTicks) {
        this.howManyTicks = howManyTicks;
    }
}

