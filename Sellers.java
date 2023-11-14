import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Project 4: Sellers
 *
 * This class allows a user to sell a ticket.
 *
 * @author Rahul Siddharth, Shrish Mahesh, Lab Section L20
 * @version October 30, 2023
 */

public class Sellers {
    private LoginInfo loginInfo;
    private double grossSales;
    private ArrayList<Ticket> products;
    private ArrayList<String> stores;
    private int sellerID;


    public Sellers(LoginInfo loginInfo,int sellerID)
    {
        this.loginInfo = loginInfo;
        this.sellerID= sellerID;
        products = new ArrayList<>();
        stores = new ArrayList<>();
    }


    public double getGrossSales(){return grossSales;}
    public ArrayList<Ticket> getTickets(){return products;}
    public int getSellerID(){return sellerID;}
    //for restoration purposes when object gets deleted
    public void setGrossSales(double grossSales){this.grossSales = grossSales;}
    public void setProducts(ArrayList<Ticket> products){this.products = products;}
    public void setSellerID(int buyerID){this.sellerID= buyerID;}



    public void deleteTicket(Ticket oldTicket, String store)
    {

        ArrayList<String> sellers = Marketplace.readFile("SellerInfo.txt");
        int indexToChange = 0;
        for (int i = 0; i < sellers.size(); i++) {
            String[] line = sellers.get(i).split(",");
            if (line[0].equals(String.valueOf(this.sellerID))) {
                indexToChange = i;
                break;
            }
        }

        String[] elements = sellers.get(indexToChange).split(",");
        String contents = "";
        int indexOfBracket;
        int indexOfBracket2;

        for(int i = 2; i< elements.length;i++) {
            indexOfBracket = elements[i].indexOf("{");
            String tempStore = elements[i].substring(0, indexOfBracket);
            if(tempStore.equals(store)) {
                indexOfBracket2 = elements[i].indexOf("}");
                contents = elements[i].substring(indexOfBracket+1, indexOfBracket2);
                break;
            }
        }

        if(contents.isEmpty()) {
            System.out.println("Nothing in store to remove");
        } else {
            int indexOfTicket = contents.indexOf(oldTicket.toString());

            int indexOfQty = contents.indexOf("|", indexOfTicket) + 1;
            int lastIndex = contents.indexOf(":", indexOfQty);

            if(lastIndex != -1) {
                String currentQty = contents.substring(indexOfQty, lastIndex);
                contents = contents.replace(oldTicket.toString()+"|"+currentQty+":", "");
            } else {
                String currentQty = contents.substring(indexOfQty);
                if(contents.contains(":"+oldTicket.toString()+"|"+currentQty)) {
                    contents = contents.replace(":"+oldTicket.toString()+"|"+currentQty, "");
                } else {
                    contents = contents.replace(oldTicket.toString()+"|"+currentQty, "");
                }
            }
            int storeIndex = sellers.get(indexToChange).indexOf(store);
            int closingBracket = sellers.get(indexToChange).indexOf("}", storeIndex);
            String toReplace = sellers.get(indexToChange).substring(storeIndex, closingBracket);
            String replacement = store+"{"+contents;
            sellers.set(indexToChange, sellers.get(indexToChange).replace(toReplace, replacement));

            Marketplace.writeFile(sellers, "SellerInfo.txt");
        }

    }

    public boolean addStore(String store) {
        try {
            stores.add(store);
            ArrayList<String> sellers = Marketplace.readFile("SellerInfo.txt");
            int indexToChange = 0;
            for (int i = 0; i < sellers.size(); i++) {
                String[] line = sellers.get(i).split(",");
                if (line[0].equals(String.valueOf(this.sellerID))) {
                    indexToChange = i;
                    break;
                }
            }

            String currentLine = sellers.get(indexToChange)+","+store+"{}";
            sellers.set(indexToChange, currentLine);
            Marketplace.writeFile(sellers, "SellerInfo.txt");
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public ArrayList<String> retrieveStores() {
        ArrayList<String> storesList = new ArrayList<>();
        ArrayList<String> sellers = Marketplace.readFile("SellerInfo.txt");
        int indexToSearch = 0;
        for (int i = 0; i < sellers.size(); i++) {
            String[] line = sellers.get(i).split(",");
            if (line[0].equals(String.valueOf(this.sellerID))) {
                if(line.length < 3) {
                    return null;
                }
                indexToSearch = i;
                break;
            }
        }

        String[] sellerInfo = sellers.get(indexToSearch).split(",");
        for (int i = 2; i < sellerInfo.length; i++) {
            int bracketIdx = sellerInfo[i].indexOf("{");
            String store = sellerInfo[i].substring(0, bracketIdx);
            storesList.add(store);
        }

        return storesList;
    }

    public ArrayList<String> retrieveListings(String store) {
        ArrayList<String> listings = new ArrayList<>();
        ArrayList<String> sellers = Marketplace.readFile("SellerInfo.txt");
        int indexToSearch = 0;
        for (int i = 0; i < sellers.size(); i++) {
            String[] line = sellers.get(i).split(",");
            if (line[0].equals(String.valueOf(this.sellerID))) {
                indexToSearch = i;
                break;
            }
        }

        String[] elements = sellers.get(indexToSearch).split(",");
        String contents = "";
        int indexOfBracket;
        int indexOfBracket2;

        for(int i = 2; i< elements.length;i++) {
            indexOfBracket = elements[i].indexOf("{");
            String tempStore = elements[i].substring(0, indexOfBracket);
            if(tempStore.equals(store)) {
                indexOfBracket2 = elements[i].indexOf("}");
                contents = elements[i].substring(indexOfBracket+1, indexOfBracket2);
                break;
            }
        }

        if(contents.isEmpty()) {
            return null;
        } else {
            if(contents.contains(":")) {
                String[] items = contents.split(":");
                for (int i = 0; i < items.length; i++) {
                    String[] itemParts = items[i].split("\\|");
                    String[] ticketParts = itemParts[0].split(";");
                    listings.add(i+1+":\n" +
                            "Sport: " + ticketParts[0]+"\n" +
                            "Location: " + ticketParts[1]+"\n" +
                            "Section: " + ticketParts[2] + "\n" +
                            "Price: " + ticketParts[3] + "\n" +
                            "Quantity: " + itemParts[1]
                    );
                }
            } else {
                String[] itemParts = contents.split("\\|");
                String[] ticketParts = itemParts[0].split(";");
                listings.add(1+":\n" +
                        "Sport: " + ticketParts[0]+"\n" +
                        "Location: " + ticketParts[1]+"\n" +
                        "Section: " + ticketParts[2] + "\n" +
                        "Price: " + ticketParts[3] + "\n" +
                        "Quantity: " + itemParts[1]
                );
            }
        }

        return listings;
    }

    public SellerListing retrieveProducts(String store) {
        ArrayList<Ticket> tickets = new ArrayList<>();
        ArrayList<String> quantities = new ArrayList<>();
        ArrayList<String> sellers = Marketplace.readFile("SellerInfo.txt");
        int indexToSearch = 0;
        for (int i = 0; i < sellers.size(); i++) {
            String[] line = sellers.get(i).split(",");
            if (line[0].equals(String.valueOf(this.sellerID))) {
                indexToSearch = i;
                break;
            }
        }

        String[] elements = sellers.get(indexToSearch).split(",");
        String contents = "";
        int indexOfBracket;
        int indexOfBracket2;

        for(int i = 2; i< elements.length;i++) {
            indexOfBracket = elements[i].indexOf("{");
            String tempStore = elements[i].substring(0, indexOfBracket);
            if(tempStore.equals(store)) {
                indexOfBracket2 = elements[i].indexOf("}");
                contents = elements[i].substring(indexOfBracket+1, indexOfBracket2);
                break;
            }
        }

        if(contents.isEmpty()) {
            return null;
        } else {
            if(contents.contains(":")) {
                String[] items = contents.split(":");
                for (int i = 0; i < items.length; i++) {
                    String[] itemParts = items[i].split("\\|");
                    String[] ticketParts = itemParts[0].split(";");
                    tickets.add(new Ticket(ticketParts[0], ticketParts[1], ticketParts[2],
                            Double.parseDouble(ticketParts[3])));
                    quantities.add(itemParts[1]);
                }
            } else {
                String[] itemParts = contents.split("\\|");
                String[] ticketParts = itemParts[0].split(";");
                tickets.add(new Ticket(ticketParts[0], ticketParts[1], ticketParts[2],
                        Double.parseDouble(ticketParts[3])));
                quantities.add(itemParts[1]);
            }
        }

        return new SellerListing(tickets, quantities);
    }

    public boolean modTicket(Ticket oldTicket, Ticket newTicket, String store, int qty)
    {
        if(qty == 0) {
            try {
                deleteTicket(oldTicket, store);
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        ArrayList<String> sellers = Marketplace.readFile("SellerInfo.txt");
        int indexToChange = 0;
        for (int i = 0; i < sellers.size(); i++) {
            String[] line = sellers.get(i).split(",");
            if (line[0].equals(String.valueOf(this.sellerID))) {
                indexToChange = i;
                break;
            }
        }

        String[] elements = sellers.get(indexToChange).split(",");
        String contents = "";
        int indexOfBracket;
        int indexOfBracket2;

        for(int i = 2; i< elements.length;i++) {
            indexOfBracket = elements[i].indexOf("{");
            String tempStore = elements[i].substring(0, indexOfBracket);
            if(tempStore.equals(store)) {
                indexOfBracket2 = elements[i].indexOf("}");
                contents = elements[i].substring(indexOfBracket+1, indexOfBracket2);
                break;
            }
        }

        if(!contents.isEmpty()) {
            int indexOfTicket = contents.indexOf(oldTicket.toString());
            int indexOfQty = contents.indexOf("|", indexOfTicket) + 1;
            int lastIndex = contents.indexOf(":", indexOfQty);

            if(lastIndex != -1) {
                String currentQty = contents.substring(indexOfQty, lastIndex);
                contents = contents.replace(oldTicket.toString()+"|"+currentQty, newTicket.toString()+"|"+qty);
            } else {
                String currentQty = contents.substring(indexOfQty);
                contents = contents.replace(oldTicket.toString()+"|"+currentQty, newTicket.toString()+"|"+qty);
            }

            int storeIndex = sellers.get(indexToChange).indexOf(store+"{");
            int closingBracket = sellers.get(indexToChange).indexOf("}", storeIndex);
            String toReplace = sellers.get(indexToChange).substring(storeIndex, closingBracket);
            String replacement = store+"{"+contents;
            sellers.set(indexToChange, sellers.get(indexToChange).replace(toReplace, replacement));
            Marketplace.writeFile(sellers, "SellerInfo.txt");
        } else {
            System.out.println("Nothing in store to edit");
            return false;
        }


        return true;

    }

    public boolean addTickets(Ticket b, int qty, String store)
    {
        try {
            ArrayList<String> sellers = Marketplace.readFile("SellerInfo.txt");
            int indexToChange = 0;
            for (int i = 0; i < sellers.size(); i++) {
                String[] line = sellers.get(i).split(",");
                if (line[0].equals(String.valueOf(this.sellerID))) {
                    indexToChange = i;
                    break;
                }
            }

            String[] elements = sellers.get(indexToChange).split(",");
            String contents = "";
            int secondIndex = 0;
            int indexOfBracket;
            int indexOfBracket2;

            for(int i = 2; i< elements.length;i++) {
                indexOfBracket = elements[i].indexOf("{");
                String tempStore = elements[i].substring(0, indexOfBracket);
                if(tempStore.equals(store)) {
                    indexOfBracket2 = elements[i].indexOf("}");
                    contents = elements[i].substring(indexOfBracket+1, indexOfBracket2);
                    break;
                }
            }

            if(!contents.isEmpty()) {
                contents = contents + ":" + b.toString() + "|"+qty;
            } else {
                contents = b.toString() + "|"+qty;
            }

            int storeIndex = sellers.get(indexToChange).indexOf(store);
            int closingBracket = sellers.get(indexToChange).indexOf("}", storeIndex);
            String toReplace = sellers.get(indexToChange).substring(storeIndex, closingBracket);
            String replacement = store+"{"+contents;
            sellers.set(indexToChange, sellers.get(indexToChange).replace(toReplace, replacement));
            Marketplace.writeFile(sellers, "SellerInfo.txt");
        } catch (Exception e) {
            return false;
        }

        return true;
    }



    public void viewDash(String dashFile)
    {


    }


}
