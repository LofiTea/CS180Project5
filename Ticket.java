/**
 * Project 5: Ticket
 *
 * The ticket class describes what type of ticket is available in the marketplace.
 *
 * @author Henry J. Lee, Lab Section L20
 * @version November 13, 2023
 */

public class Ticket {
    private String sportType;
    private String location;
    private String section;
    private double price;

    public Ticket(String sportType, String location, String section, double price) {
        this.sportType = sportType;
        this.location = location;
        this.section = section;
        this.price = price;
    }

    // Getters are set up so that buyers can know what type of ticket they are buying
    public String getSportType() {
        return this.sportType;
    }

    public String getLocation() {
        return this.location;
    }

    public String getSection() {
        return this.section;
    }

    public double getPrice() {
        return this.price;
    }

    // Setters are set up so that sellers can manage what type of ticket they are selling
    public void setSportType(String sportType) {
        this.sportType = sportType;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    // The toString method is set up like this so that it is easier to implement data preservation via files
    public String toString() {
        return getSportType() + ";" + getLocation() + ";" + getSection() + ";" + String.format(
                "%.2f", getPrice());
    }
}
