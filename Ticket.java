/**
 * Project 5: Ticket
 * <p>
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

    // Setters are set up so that sellers can manage what type of ticket they are selling
    public void setSportType(String sportType) {
        this.sportType = sportType;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSection() {
        return this.section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    // The toString method is set up like this so that it is easier to implement data preservation via files
    public String toString() {
        return getSportType() + ";" + getLocation() + ";" + getSection() + ";" + String.format(
                "%.2f", getPrice());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else {
            if (obj instanceof Ticket) {
                return sportType.equals(((Ticket) obj).getSportType()) && location.equals(((Ticket) obj).getLocation()) && section.equals(((Ticket) obj).getSection()) && price == ((Ticket) obj).getPrice();
            } else {
                return false;
            }
        }

    }
}
