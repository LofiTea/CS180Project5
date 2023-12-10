/**
 * Project 5: PrintableStat
 * 
 * Created to help display seller statistics.
 *
 * @author Shrish Mahesh, Lab Section L20
 * @version November 13, 2023
 */

public class PrintableStat {
    private Integer buyerID;
    private String buyerEmail;
    private Integer totalItems;
    private Integer uniqueItems;

    public PrintableStat(int ID, String buyerEmail, int totalItems, int uniqueItems) {
        this.buyerID = ID;
        this.buyerEmail = buyerEmail;
        this.totalItems = totalItems;
        this.uniqueItems = uniqueItems;
    }

    public Integer getBuyerID() {
        return this.buyerID;
    }

    public void setBuyerID(int buyerID) {
        this.buyerID = buyerID;
    }

    public String getBuyerEmail() {
        return this.buyerEmail;
    }

    public void setBuyerEmail(String buyerEmail) {
        this.buyerEmail = buyerEmail;
    }

    public Integer getTotalItems() {
        return this.totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public Integer getUniqueItems() {
        return this.uniqueItems;
    }

    public void setUniqueItems(int uniqueItems) {
        this.uniqueItems = uniqueItems;
    }

    public String toString() {
        return "Customer Info - \nID: " + this.buyerID + "\nEmail: " + this.buyerEmail +
                "\nTotal items purchased: " + this.totalItems + "\nUnique items purchased: " + this.uniqueItems;
    }
}
