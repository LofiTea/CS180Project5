import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Project 4: Buyers
 *
 * This class allows a user to buy a ticket.
 *
 * @author Rahul Siddharth, Shrish Mahesh, Lab Section L20
 * @version October 30, 2023
 */

public class Buyers {
    private int buyerID;
    private LoginInfo loginInfo;
    private ArrayList<CartItems> shoppingCart;
    private ArrayList<CartItems> previousShopped;


    public Buyers(int buyerID, LoginInfo loginInfo) {
        //call the user login function do not want constructor directly handling the username and password data
        this.buyerID = buyerID;
        this.loginInfo = loginInfo;
        this.shoppingCart = new ArrayList<>();
        this.previousShopped = new ArrayList<>();
    }


    public int getBuyerID() {
        return buyerID;
    }


    public ArrayList<CartItems> getShoppingCart() {
        return shoppingCart;
    }


    public ArrayList<CartItems> getPreviousShopped() {
        return previousShopped;
    }

    //for data restoration purposes
    public void setBuyerID(int buyerID) {
        this.buyerID = buyerID;
    }


    public void setShoppingCart(ArrayList<CartItems> shoppingCart) {
        this.shoppingCart = shoppingCart;
    }


    public void setPreviousShopped(ArrayList<CartItems> previousShopped) {
        this.previousShopped = previousShopped;
    }


    public void updateShoppingCart() throws IOException {
        ArrayList<String> buyerInfo = Marketplace.readFile("BuyerInfo.txt");
        int indexToChange = 0;
        for (int i = 0; i < buyerInfo.size(); i++) {
            String[] line = buyerInfo.get(i).split(",");
            if (line[0].equals(String.valueOf(this.buyerID))) {
                indexToChange = i;
                break;
            }
        }
        buyerInfo.set(indexToChange, this.buyerID + "," + this.loginInfo.getEmail() + "," + formatShoppingCart());
        Marketplace.writeFile(buyerInfo, "BuyerInfo.txt");
    }

    public void updateShoppingCart2() throws IOException {
        ArrayList<String> buyerInfo = Marketplace.readFile("BuyerHistory.txt");
        int indexToChange = 0;
        for (int i = 0; i < buyerInfo.size(); i++) {
            String[] line = buyerInfo.get(i).split(",");
            if (line[0].equals(String.valueOf(this.buyerID))) {
                indexToChange = i;
                break;
            }
        }
        buyerInfo.set(indexToChange, this.buyerID + "," + this.loginInfo.getEmail() + "," + formatShoppingCart2());
        Marketplace.writeFile(buyerInfo, "BuyerHistory.txt");
    }


    public void clearShoppingCart() throws IOException {
        ArrayList<String> buyerInfo = Marketplace.readFile("BuyerInfo.txt");
        int indexToChange = 0;
        for (int i = 0; i < buyerInfo.size(); i++) {
            String[] line = buyerInfo.get(i).split(",");
            if (line[0].equals(String.valueOf(this.buyerID))) {
                indexToChange = i;
                break;
            }
        }
        buyerInfo.set(indexToChange, this.buyerID + "," + this.loginInfo.getEmail() + ",");
        Marketplace.writeFile(buyerInfo, "BuyerInfo.txt");
    }


    public String formatShoppingCart() {
        String returnStr = "";
        for (int i = 0; i < shoppingCart.size(); i++) {
            CartItems item = shoppingCart.get(i);
            if (i == 0) {
                returnStr += item.toString();
            } else {
                returnStr += ":" + item.toString();
            }
        }
        return returnStr;
    }

    public String formatShoppingCart2() {
        String returnStr = "";
        for (int i = 0; i < previousShopped.size(); i++) {
            CartItems item = previousShopped.get(i);
            if (i == 0) {
                returnStr += item.toString();
            } else {
                returnStr += ":" + item.toString();
            }
        }
        return returnStr;
    }

    public void addToShoppingCart(Ticket b, int qty) {
        shoppingCart.add(new CartItems(b, qty));
    }


    public void removeToShoppingCart(Ticket b, int qty) {
        shoppingCart.remove(new CartItems(b, qty));
    }

    public void checkoutCart() {
        try {
            int transID;
            ArrayList<String> fileInfo = Marketplace.readFile("TransactionInfo.txt");
            ArrayList<CartItems> shoppingCartClear = (ArrayList<CartItems>) shoppingCart.clone();
            for (CartItems b : shoppingCart) {
                if (fileInfo.isEmpty()) transID = 1;
                else {
                    String[] lastLine = fileInfo.get(fileInfo.size() - 1).split(",");
                    transID = Integer.parseInt(lastLine[0]) + 1;
                }
                double price = b.getTicket().getPrice();
                int qty = b.getQTY();
                String productDescribe = b.getTicket().toString();
                StoreNameIDCombo returnCombo = getSellerID(b.getTicket().toString());
                fileInfo.add(String.format("%d,%d,%d,%s,%.2f,%d,%s", transID, buyerID, returnCombo.getSellerID(),
                        returnCombo.getStoreName(), price, qty,
                        productDescribe));


                previousShopped.add(b);
                shoppingCartClear.remove(b);
                //updateTicketQuantity("SellerInfo.txt", returnCombo.getSellerID(), productDescribe, qty);
           /*  addPreviousShoppedItems(b);
            if (checkIfEmpty()) {
                updateBuyerHistory();
            } else {
                updateBuyerHistoryAgain(this.buyerID, this.loginInfo);
            }*/


                //shoppingCart.remove(b);
            }
            shoppingCart = shoppingCartClear;
            updateShoppingCart();
            Marketplace.writeFile(fileInfo, "TransactionInfo.txt");
            createBuyerHistory();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void createBuyerHistory() {


        if (checkIfEmpty(this.buyerID)) {
            updateBuyerHistory();
        } else {
            //updateBuyerHistoryAgain(this.buyerID, this.loginInfo);
            try {
                updateBuyerHistoryAgain(buyerID, loginInfo);
            } catch (Exception e) {

                e.printStackTrace();
            }
        }

    }

    public ArrayList<CartItems> retrieveShoppingCart() {
        ArrayList<String> buyerInfo = Marketplace.readFile("BuyerInfo.txt");
        int indexToSearch = 0;
        for (int i = 0; i < buyerInfo.size(); i++) {
            String[] line = buyerInfo.get(i).split(",");
            if (line[0].equals(String.valueOf(this.buyerID))) {
                indexToSearch = i;
                break;
            }
        }
        ArrayList<CartItems> returnSC = new ArrayList<>();
        String currentLine = buyerInfo.get(indexToSearch);
        String[] elements = currentLine.split(",");


        if (elements.length <= 2) {
            return null;
        } else {
            if (!elements[2].contains(":")) {
                String[] cartItem = elements[2].split("\\|");
                String[] ticket = cartItem[0].split(";");
                Ticket t = new Ticket(ticket[0], ticket[1], ticket[2],
                        Double.parseDouble(ticket[3]));
                returnSC.add(new CartItems(t, Integer.parseInt(cartItem[1])));
            } else {
                String[] shoppingCart = elements[2].split(":");
                for (int i = 0; i < shoppingCart.length; i++) {
                    String[] cartItem = shoppingCart[i].split("\\|");
                    String[] ticket = cartItem[0].split(";");
                    Ticket t = new Ticket(ticket[0], ticket[1], ticket[2],
                            Double.parseDouble(ticket[3]));
                    returnSC.add(new CartItems(t, Integer.parseInt(cartItem[1])));
                }
            }
            return returnSC;
        }
    }

    public ArrayList<CartItems> retrieveShoppingCart2() {
        ArrayList<String> buyerInfo = Marketplace.readFile("BuyerHistory.txt");
        int indexToSearch = 0;
        for (int i = 0; i < buyerInfo.size(); i++) {
            String[] line = buyerInfo.get(i).split(",");
            if (line[0].equals(String.valueOf(this.buyerID))) {
                indexToSearch = i;
                break;
            }
        }
        ArrayList<CartItems> returnSC = new ArrayList<>();
        String currentLine = buyerInfo.get(indexToSearch);
        String[] elements = currentLine.split(",");


        if (elements.length <= 2) {
            return null;
        } else {
            if (!elements[2].contains(":")) {
                String[] cartItem = elements[2].split("\\|");
                String[] ticket = cartItem[0].split(";");
                Ticket t = new Ticket(ticket[0], ticket[1], ticket[2],
                        Double.parseDouble(ticket[3]));
                returnSC.add(new CartItems(t, Integer.parseInt(cartItem[1])));
            } else {
                String[] shoppingCart = elements[2].split(":");
                for (int i = 0; i < shoppingCart.length; i++) {
                    String[] cartItem = shoppingCart[i].split("\\|");
                    String[] ticket = cartItem[0].split(";");
                    Ticket t = new Ticket(ticket[0], ticket[1], ticket[2],
                            Double.parseDouble(ticket[3]));
                    returnSC.add(new CartItems(t, Integer.parseInt(cartItem[1])));
                }
            }
            return returnSC;
        }
    }

        public void dashboardInteraction(String marketFile)  {
        Scanner scan = new Scanner(System.in);

        ArrayList<String> product = Marketplace.readFile(marketFile);
        if (product.isEmpty()) {
            System.out.println("No data found in file.");
        }

        System.out.println("Dashboard: ");
        System.out.println("1. Show Items");
        System.out.println("2. Show IDs");
        System.out.println("3. Show Prices");

        System.out.println("Enter which one (pick the number only): ");
        int temp = scan.nextInt();

    

    }

    public void showItems (ArrayList<String> product) {
        System.out.println("Show Items: ");
        for (String i : product) {
            System.out.println(i);
        }
    }

    public void showIDs (ArrayList<String> product) {
        Collections.sort(product, Comparator.comparingDouble(this::getIDs));
        System.out.println("Show Items: ");
        for (String i : product) {
            System.out.println(i);
        }
    }

    public void showPrices (ArrayList<String> product) {
        Collections.sort(product, Comparator.comparingDouble(this::getPrices));
        System.out.println("Show Items: ");
        for (String i : product) {
            System.out.println(i);
        }
    }

    public double getPrices (String prod) {
        String[] parts = prod.split(",");
        if (parts.length >= 3) {
            try {
                return Double.parseDouble(parts[2]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return 0.0;
    }

    public double getIDs (String prod) {
        String[] parts = prod.split(",");
        if (parts.length >= 2) {
            try {
                return Integer.parseInt(parts[1]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }


    public static void updateTicketQuantity(String filePath, int userId, String ticketDescription,
                                            int purchasedQuantity) {
        try {
            File inputFile = new File(filePath);
            File tempFile = new File(filePath + ".temp");

            try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                 BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

                String currentLine;

                while ((currentLine = reader.readLine()) != null) {
                    String[] parts = currentLine.split(",");
                    if (parts.length > 0 && Integer.parseInt(parts[0]) == userId) {
                        // Update the quantity for the specified ticket
                        for (int i = 2; i < parts.length; i++) {
                            if (parts[i].contains(ticketDescription)) {
                                String[] storeParts = parts[i].split(":");
                                boolean didGetDeleted = false;
                                int typeOfDeletion = -1;
                                for (int j = 0; j < storeParts.length; j++) {
                                    boolean havetoClose = false;
                                    if (storeParts[j].contains(ticketDescription)) {
                                        // Extract the product description and quantity
                                        String[] descriptionAndQty = storeParts[j].split("\\|");
                                        // Update the quantity
                                        if (descriptionAndQty[1].length() >= 2 && descriptionAndQty[1].contains("}")) {

                                            descriptionAndQty[1] = descriptionAndQty[1].substring(0,
                                                    descriptionAndQty[1].indexOf("}"));
                                            if ((j + 1 == storeParts.length)) havetoClose = true;
                                        }
                                        int currentQuantity = Integer.parseInt(descriptionAndQty[1]);
                                        int updatedQuantity = currentQuantity - purchasedQuantity;
                                        if (updatedQuantity <= 0) {
                                            if (storeParts[j].contains("{")) {
                                                int openBraceIndex = storeParts[j].indexOf("{");
                                                String updatedWithout = storeParts[j].substring(0, openBraceIndex + 1);
                                                storeParts[j] = updatedWithout;
                                                didGetDeleted = true;
                                                typeOfDeletion = 1;
                                            } else if (storeParts[j].contains("}")) {
                                                storeParts[j] = "}";
                                                didGetDeleted = true;
                                                typeOfDeletion = 2;
                                            } else {
                                                storeParts[j] = "";
                                                didGetDeleted = true;
                                                typeOfDeletion = 3;
                                            }
                                        } else {
                                            if (havetoClose) {
                                                descriptionAndQty[1] = Integer.toString(updatedQuantity) + "}";
                                            } else {
                                                descriptionAndQty[1] = Integer.toString(updatedQuantity);
                                            }

                                            storeParts[j] = String.join("|", descriptionAndQty);
                                        }

                                    }
                                }

                                parts[i] = String.join(":", storeParts);
                                if (didGetDeleted) {
                                    switch (typeOfDeletion) {
                                        case 1:
                                            parts[i] = parts[i].replaceFirst(":", "");
                                            break;
                                        case 2:
                                            String curEnd = parts[i];
                                            int indexOfCloseBrace = curEnd.indexOf("}");
                                            String newEdit = "";
                                            for (int k = 0; k < curEnd.length(); k++) {
                                                if (k == indexOfCloseBrace - 1) {
                                                    int x3232 = 3; //newEdit += "";
                                                } else {
                                                    newEdit += curEnd.charAt(k);
                                                }
                                            }
                                            parts[i] = newEdit;
                                            break;
                                        case 3:
                                            parts[i] = parts[i].replaceAll("::", ":");
                                            break;
                                    }
                                    if (parts[i].indexOf('}') == -1) {
                                        parts[i] = parts[i] + "}";
                                    }

                                }
                            }
                        }
                    }
                    writer.write(String.join(",", parts) + System.lineSeparator());
                }
            }

            ArrayList<String> tempContents = Marketplace.readFile(String.valueOf(tempFile));
            Marketplace.writeFile(tempContents, String.valueOf(inputFile));
            tempFile.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static StoreNameIDCombo getSellerID(String searchFor) {
        try {
            ArrayList<String> fileInfo = Marketplace.readFile("SellerInfo.txt");
            if (fileInfo.isEmpty()) new StoreNameIDCombo("n/a", -1);
            for (String eachLine : fileInfo) {
                DoubleArrayList storeThing = splitStoreInfo(eachLine);
                String mostRecentStoreSeen = "";
                for (String b : storeThing.getStuffInStores()) {

                    if (storeThing.getStoreList().contains(b)) mostRecentStoreSeen = b;

                    if (b.equals(searchFor)) {
                        String foundStore = mostRecentStoreSeen;
                        String[] curLine = eachLine.split(",");
                        return new StoreNameIDCombo(foundStore, Integer.parseInt(curLine[0]));
                    }
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return new StoreNameIDCombo("n/a", -1);


    }

    public static DoubleArrayList splitStoreInfo(String s) {
        ArrayList<String> stufInStores = new ArrayList<>();
        String[] splitStore = s.split(",");
        ArrayList<String> storeInfo = new ArrayList<>();
        ArrayList<String> allStores = new ArrayList<>();

        for (int i = 2; i < splitStore.length; i++) {
            storeInfo.add(splitStore[i]);
        }

        for (String b : storeInfo) {
            b = b.replace('{', ',');
            b = b.replace('}', ',');
            b = b.replace('|', ',');
            b = b.replace(":", ",");
            String[] curStringSplit = b.split(",");
            allStores.add(curStringSplit[0]);
            for (String thing : curStringSplit) {
                stufInStores.add(thing);
            }

        }


        return (new DoubleArrayList(stufInStores, allStores));

    }

    public void addPreviousShoppedItems(CartItems cartItems) {
        previousShopped.add(cartItems);
    }


    public void updateBuyerHistory() {
        try (PrintWriter pw = new PrintWriter(new FileOutputStream("BuyerHistory.txt", false))) {
            pw.print(buyerID + "," + loginInfo.getEmail() + ",");
            for (int i = 0; i < previousShopped.size(); i++) {
                pw.println(previousShopped.get(i));
            }
            pw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void updateBuyerHistoryAgain(int id, LoginInfo loginInfo) {
        String idNumber = Integer.toString(id);
        String email = loginInfo.getEmail();
        ArrayList<String> list = Marketplace.readFile("BuyerHistory.txt");
        boolean hadToMakeNewEntry = true;
        for (int i = 0; i < list.size(); i++) {
            String[] arr = list.get(i).split(",");
            String check1 = arr[0];
            String check2 = arr[1];
            if (idNumber.equals(check1) && email.equals(check2)) {
                hadToMakeNewEntry = false;
                try {

                    updateShoppingCart2();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

        if (hadToMakeNewEntry) {
            updateBuyerHistory();
        }
    }

    public boolean checkIfEmpty(int buyerId) {
        ArrayList<String> list = Marketplace.readFile("BuyerHistory.txt");
        if (list.size() == 0) return true;
        for (int i = 0; i < list.size(); i++) {
            String[] arr = list.get(i).split(",");
            String check1 = arr[0];
            if (Integer.parseInt(check1) == buyerId) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            // It's not a number
            return false;
        }
    }

    public static String[] listTicket(String s) {
        String[] thing = s.split(";");
        return thing;
    }

    public static TicketInfoCombo viewAllListingsGeneral() {
        ArrayList<String> fileInfo = Marketplace.readFile("SellerInfo.txt");
        ArrayList<String[]> allListedTicks = new ArrayList<>();
        int tickNumber = 1;
        for (String eachLine : fileInfo) {

            DoubleArrayList b = splitStoreInfo(eachLine);
            String curSeller = (eachLine.split(","))[1];
            String curStoreListing = "";
            ArrayList<String> stuffInStores = b.getStuffInStores();
            ArrayList<String> storelist = b.getStoreList();

            for (int i = 0; i < stuffInStores.size(); i++) {
                String curItem = stuffInStores.get(i);
                if (storelist.contains(curItem)) {
                    curStoreListing = curItem;
                } else {
                    if (!isNumeric(curItem)) {
                        System.out.println(tickNumber + ": ");
                        tickNumber++;
                        System.out.println("Seller: " + curSeller);
                        System.out.println("Store: " + curStoreListing);
                        String[] thing = listTicket(curItem);
                        String lmao = "Sport: " + thing[0] + "\nLocation: " + thing[1] + "\nRow/Section Area: "
                                + thing[2] + "\nPrice: " + thing[3] + "\n";
                        System.out.print(lmao);
                        String qtyNumber = stuffInStores.get(i + 1);
                        System.out.print("Quantity: " + qtyNumber);

                        String[] tickInformation = new String[5];
                        for (int j = 0; j < 4; j++) {
                            tickInformation[j] = thing[j];
                        }
                        tickInformation[4] = qtyNumber;
                        allListedTicks.add(tickInformation);
                    }
                }
                System.out.println();
            }


        }
        return new TicketInfoCombo(allListedTicks, tickNumber - 1);

    }

    public static TicketInfoCombo viewListingsWithConstraint(String constraint) {
        ArrayList<String> fileInfo = Marketplace.readFile("SellerInfo.txt");
        ArrayList<String[]> allListedTicks = new ArrayList<>();
        int tickNumber = 1;
        for (String eachLine : fileInfo) {

            DoubleArrayList b = splitStoreInfo(eachLine);
            String curSeller = (eachLine.split(","))[1];
            String curStoreListing = "";
            ArrayList<String> stuffInStores = b.getStuffInStores();
            ArrayList<String> storelist = b.getStoreList();

            for (int i = 0; i < stuffInStores.size(); i++) {
                String curItem = stuffInStores.get(i);
                if (storelist.contains(curItem)) {
                    curStoreListing = curItem;
                } else {
                    if (!isNumeric(curItem)) {
                        boolean shouldList = false;
                        String[] thing = listTicket(curItem);
                        if (curStoreListing.toLowerCase().equals(constraint.toLowerCase()) || thing[0].toLowerCase().
                                equals(constraint.toLowerCase())) {
                            shouldList = true;
                        }

                        if (shouldList) {
                            System.out.println(tickNumber + ": ");
                            tickNumber++;
                            System.out.println("Seller: " + curSeller);
                            System.out.println("Store: " + curStoreListing);

                            String lmao = "Sport: " + thing[0] + "\nLocation: " + thing[1] + "\nRow/Section Area: " +
                                    thing[2] + "\nPrice: " + thing[3] + "\n";
                            System.out.print(lmao);
                            String qtyNumber = stuffInStores.get(i + 1);
                            System.out.print("Quantity: " + qtyNumber);

                            String[] tickInformation = new String[5];
                            for (int j = 0; j < 4; j++) {
                                tickInformation[j] = thing[j];
                            }
                            tickInformation[4] = qtyNumber;
                            allListedTicks.add(tickInformation);
                        }
                        System.out.println();
                    }
                }
                System.out.println();

            }


        }
        return new TicketInfoCombo(allListedTicks, tickNumber - 1);

    }

    public static TicketInfoCombo viewAllListingsSortedByTicketQuantity() {
        ArrayList<String> fileInfo = Marketplace.readFile("SellerInfo.txt");
        ArrayList<String[]> allListedTicks = new ArrayList<>();
        int tickNumber = 1;
        for (String eachLine : fileInfo) {

            DoubleArrayList b = splitStoreInfo(eachLine);
            String curSeller = (eachLine.split(","))[1];
            String curStoreListing = "";
            ArrayList<String> stuffInStores = b.getStuffInStores();
            ArrayList<String> storelist = b.getStoreList();

            for (int i = 0; i < stuffInStores.size(); i++) {
                String curItem = stuffInStores.get(i);
                if (storelist.contains(curItem)) {
                    curStoreListing = curItem;
                } else {
                    if (!isNumeric(curItem)) {
                        String[] thing = listTicket(curItem);
                        String qtyNumber = stuffInStores.get(i + 1);
                        String[] tickInformation = new String[7];
                        for (int j = 2; j < 6; j++) {
                            tickInformation[j] = thing[j - 2];
                        }
                        tickInformation[6] = qtyNumber;
                        tickInformation[1] = curStoreListing;
                        tickInformation[0] = curSeller;
                        allListedTicks.add(tickInformation);
                    }

                }
            }

        }


        allListedTicks.sort((arr1, arr2) -> Integer.compare(Integer.parseInt(arr2[6]), Integer.parseInt(arr1[6])));


        for (String[] things : allListedTicks) {
            System.out.println(tickNumber + ": ");
            tickNumber++;
            System.out.println("Seller: " + things[0]);
            System.out.println("Store: " + things[1]);

            String lmao = "Sport: " + things[2] + "\nLocation: " + things[3] + "\nRow/Section Area: " + things[4] + "\nPrice: " +
                    things[5] + "\n";
            System.out.print(lmao);
            System.out.println("Quantity: " + things[6]);
            System.out.println();
        }

        return new TicketInfoCombo(allListedTicks, tickNumber - 1);

    }


    public static Ticket buyTicket(ArrayList<String[]> tickets, int ticketToBuy, int ticketAmount, int howManyThereAre) {
        if (ticketToBuy > howManyThereAre) {
            System.out.println("Error enter a valid ticket number");
            return null;
        } else {

            String[] curTicketInfo = tickets.get(ticketToBuy - 1);
            if (ticketAmount > Integer.parseInt(curTicketInfo[4])) {
                System.out.println("Error: buy a quantity that is avaliable!");
                return null;
            } else {
                Ticket yay = new Ticket(curTicketInfo[0], curTicketInfo[1], curTicketInfo[2],
                        Double.parseDouble(curTicketInfo[3]));
                updateTicketQuantity("SellerInfo.txt", getSellerID(yay.toString()).getSellerID(),
                        yay.toString(), ticketAmount);
                return yay;
            }
        }
    }

    public static Ticket buyTicket2(ArrayList<String[]> tickets, int ticketToBuy, int ticketAmount, int howManyThereAre) {
        if (ticketToBuy > howManyThereAre) {
            System.out.println("Error enter a valid ticket number");
            return null;
        } else {

            String[] curTicketInfo = tickets.get(ticketToBuy - 1);
            if (ticketAmount > Integer.parseInt(curTicketInfo[6])) {
                System.out.println("Error: buy a quantity that is avaliable!");
                return null;
            } else {
                Ticket yay = new Ticket(curTicketInfo[2], curTicketInfo[3], curTicketInfo[4],
                        Double.parseDouble(curTicketInfo[5]));
                updateTicketQuantity("SellerInfo.txt", getSellerID(yay.toString()).getSellerID(),
                        yay.toString(), ticketAmount);
                return yay;
            }
        }
    }
//^^^^^ this is the buy ticket you should use when the user wants to buy when the listings are sorted buy ticket qty


  public ArrayList<ArrayList<String>> listCustomerSpecificDashboard(boolean shouldSort)
   {
        ArrayList<ArrayList<String>> dashInfo = new ArrayList<>();
        ArrayList<String> transFileInfo = Marketplace.readFile("TransactionInfo.txt");

        for(String b: transFileInfo)
        {
            String[] splits = b.split(",");
            String curSport = splits[6].split(";")[0];
            String curStore = splits[3];
            int curQty = Integer.parseInt(splits[5]);

            if(Integer.parseInt(splits[1]) == buyerID) {
                boolean isSportThere = false;
                for(ArrayList<String> cd : dashInfo)
                {
                   if(cd.get(0).equals(curSport+":"))
                   {
                      isSportThere = true;
                      boolean isStoreThere = false;
                      for(int i = 1;i<cd.size();i++)
                      {
                        String[] curSplits = cd.get(i).split(",");
                        if(curStore.equals(curSplits[0]))
                        {
                            isStoreThere = true;
                            curSplits[1] = String.valueOf(Integer.parseInt(curSplits[1])+ curQty);
                            String updatedEntry  = String.join(",",curSplits);
                            cd.set(i,updatedEntry);
                        }

                      }

                       if(!isStoreThere)
                        {
                            String newENtryString = curStore +","+curQty;
                            cd.add(newENtryString);
                        }
                    
                   } 
                 
                }
              
                if(!isSportThere)
                {
                    ArrayList<String> newEntry = new ArrayList<>();
                    newEntry.add(curSport+":");
                    String newEntryString = curStore+","+curQty;
                    newEntry.add(newEntryString);
                    dashInfo.add(newEntry);
                }
                
            }
          
        }  
        if(shouldSort){
             class TicketComparator implements Comparator<String> {
                @Override
                public int compare(String ticket1, String ticket2) {
                    int tickets1 = Integer.parseInt(ticket1.split(",")[1]);
                    int tickets2 = Integer.parseInt(ticket2.split(",")[1]);
                    return Integer.compare(tickets2, tickets1);
                }
            }
         
         for(int i = 0; i<dashInfo.size();i++)
         {
            ArrayList<String> thing = dashInfo.get(i);
            ArrayList<String> subsetList = new ArrayList<>(thing.subList(1, thing.size()));
            Collections.sort(subsetList, new TicketComparator());
            ArrayList<String> newEntryList = new ArrayList<>();
            newEntryList.add(thing.get(0));
            newEntryList.addAll(subsetList);
            dashInfo.set(i,newEntryList);
            

         }
         
        }
        
        for(ArrayList<String> thing : dashInfo)
        {
            for(int i = 1; i<thing.size();i++)
            {
                String curEntry = thing.get(i);
                curEntry = curEntry.replace(",",": ");
                thing.set(i,curEntry);
            }
        }
        return dashInfo;
   }

}
