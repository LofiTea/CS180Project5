import javax.swing.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Project 5: MarketplaceServerThread
 * <p>
 * Utilizes threads in the server for the marketplace.
 *
 * @author Shrish Mahesh, Lab Section L20
 * @version November 13, 2023
 */

public class MarketplaceServerThread extends Thread {
    public static Object obj = new Object();
    Socket socket;


    MarketplaceServerThread(Socket socket) {
        this.socket = socket;
    }


    @Override
    public void run() {
        try {
            String email = null;
            String password = null;
            int id = 0;
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            boolean notLoggedIn = true;
            String role = "";


            while (notLoggedIn) {
                int optionInitial = (Integer) ois.readObject();
                switch (optionInitial) {
                    case 1:
                        boolean successful = false;
                        //while (notLoggedIn) {
                        String message = "";
                        email = (String) ois.readObject();
                        password = (String) ois.readObject();

                        //System.out.println(email2 + password2);
                        LoginInfo user = new LoginInfo(email, password);
                        String currentAuthentication = user.authenticate(email, password);
                        if (!currentAuthentication.isEmpty()) {
                            successful = true;
                            message = "Successfully logged in!";
                            String[] userInfo = user.authenticate(email, password).split(",");
                            id = Integer.parseInt(userInfo[0]);
                            notLoggedIn = false;

                        } else {
                            message = "Error.  Account not found.  Please try again.";

                        }

                        //oos.writeObject(notLoggedIn);
                        oos.writeObject(successful);
                        if (successful) {
                            role = MarketplaceServer.determineRole(email, password);
                            oos.writeObject(role);
                            //System.out.println(notLoggedIn);
                        }
                        oos.flush();
                        // }

                        break;
                    case 2:
                        email = (String) ois.readObject();
                        password = (String) ois.readObject();
                        System.out.println(email + password);
                        String accountChoice = (String) ois.readObject();

                        // System.out.println(email + password + accountChoice);


                        ArrayList<String> fileInfo = MarketplaceServer.readFile("LoginInfo.txt");

                        ArrayList<String> userInfo = new ArrayList<>();
                        String filename;

                        if (accountChoice.equals("b")) {
                            role = "b";
                            filename = "BuyerInfo.txt";
                        } else {
                            role = "s";
                            filename = "SellerInfo.txt";
                        }


                        userInfo = MarketplaceServer.readFile(filename);

                        if (fileInfo.isEmpty()) {
                            fileInfo.add(String.format("1,%s,%s,%s", email, password, role));
                            userInfo.add(String.format("1,%s", email));
                            id = 1;
                        } else {
                            System.out.println("hello");
                            String[] lastLine = fileInfo.get(fileInfo.size() - 1).split(",");


                            fileInfo.add(String.format("%d,%s,%s,%s", Integer.parseInt(lastLine[0]) + 1, email,
                                    password, role));

                            id = Integer.parseInt(lastLine[0]) + 1;
                            userInfo.add(String.format("%d,%s", Integer.parseInt(lastLine[0]) + 1, email));
                        }
                        MarketplaceServer.writeFile(userInfo, filename);
                        MarketplaceServer.writeFile(fileInfo, "LoginInfo.txt");


                        if (role.equals("b")) {
                            String filename2 = "BuyerHistory.txt";

                            ArrayList<String> history = MarketplaceServer.readFile(filename2);
                            history.add(String.format("%d,%s", id, email));

                            MarketplaceServer.writeFile(userInfo, filename2);
                        }

                        notLoggedIn = false;

                }

            }

            boolean notLoggedOut = true;
            while (notLoggedOut) {
                switch (role) {
                    case "b":
                        System.out.println("Before waiting for object");
                        int whatDash = (int) ois.readObject();
                        System.out.println(whatDash);
                        switch (whatDash) {
                            case 1:
                                LoginInfo curLoginInfo = new LoginInfo(email, password);
                                Buyers curBuyer = new Buyers(id, curLoginInfo);
                                boolean whileInMenu = true;
                                while (whileInMenu) {
                                    boolean whileInListMenu = true;
                                    //boolean shouldRecieveListItem = true;
                                    while (whileInListMenu) {
                                        System.out.println("Waiting for buying selection");

                                        int whatSelect = (int) ois.readObject();

                                        System.out.println("Current listMenuSelection: " + whatSelect);
                                        switch (whatSelect) {
                                            case 1:
                                                ArrayList<CartItems> currentShoppingCart = curBuyer.retrieveShoppingCart();
                                                ArrayList<CartItems> previousShoppingCart = curBuyer.retrieveShoppingCart2();
                                                if (currentShoppingCart == null)
                                                    currentShoppingCart = new ArrayList<>();
                                                if (previousShoppingCart == null)
                                                    previousShoppingCart = new ArrayList<>();

                                                curBuyer.setShoppingCart(currentShoppingCart);
                                                curBuyer.setPreviousShopped(previousShoppingCart);
                                                ArrayList<String> currentShoppingPackage = MarketplaceServer.buildBuyerCurrentShoppingCartPackage(curBuyer);
                                                int howList = (int) ois.readObject();
                                                if (howList != -3) {
                                                    oos.writeObject(currentShoppingPackage);
                                                }
                                                switch (howList) {
                                                    case 0:
                                                        ArrayList<String> b = Buyers.viewAllListingsGeneral2();
                                                        if (!(b.isEmpty() || b == null)) {
                                                            ArrayList<String> ticketPackage = MarketplaceServer.buildListedTicketsPackage3(b);
                                                            oos.writeObject(ticketPackage);
                                                            boolean wantToBuy = (boolean) ois.readObject();
                                                            if (wantToBuy) {
                                                                int whatTicket = (int) ois.readObject();
                                                                int howMany = (int) ois.readObject();
                                                                synchronized (obj) {
                                                                    TicketInfoCombo bAgain = Buyers.viewAllListingsGeneral();
                                                                    Ticket boughtTicket = Buyers.buyTicket(bAgain.getListedTicks(), whatTicket, howMany, bAgain.getHowManyTicks());
                                                                    if (boughtTicket != null) {
                                                                        currentShoppingCart.add(new CartItems(boughtTicket, howMany));
                                                                        curBuyer.setShoppingCart(currentShoppingCart);
                                                                        curBuyer.updateShoppingCart();
                                                                        oos.writeObject(true);
                                                                    } else {
                                                                        oos.writeObject(false);
                                                                    }
                                                                }
                                                            }
                                                        } else {
                                                            ArrayList<String> errorList = new ArrayList<>();
                                                            errorList.add("Lebron James");
                                                            oos.writeObject(errorList);
                                                        }
                                                        break;
                                                    case 1:
                                                        boolean userCancelled = (boolean) ois.readObject();
                                                        if (!userCancelled) {
                                                            String constraint = (String) ois.readObject();
                                                            b = Buyers.viewListingsWithConstraint2(constraint);
                                                            if (!(b.isEmpty() || b == null)) {
                                                                ArrayList<String> ticketPackage = MarketplaceServer.buildListedTicketsPackage3(b);
                                                                oos.writeObject(ticketPackage);
                                                                boolean wantToBuy = (boolean) ois.readObject();
                                                                if (wantToBuy) {
                                                                    int whatTicket = (int) ois.readObject();
                                                                    int howMany = (int) ois.readObject();
                                                                    synchronized (obj) {
                                                                        TicketInfoCombo bAgain = Buyers.viewListingsWithConstraint(constraint);
                                                                        Ticket boughtTicket = Buyers.buyTicket(bAgain.getListedTicks(), whatTicket, howMany, bAgain.getHowManyTicks());
                                                                        if (boughtTicket != null) {
                                                                            currentShoppingCart.add(new CartItems(boughtTicket, howMany));
                                                                            curBuyer.setShoppingCart(currentShoppingCart);
                                                                            curBuyer.updateShoppingCart();
                                                                            oos.writeObject(true);
                                                                        } else {
                                                                            oos.writeObject(false);
                                                                        }
                                                                    }
                                                                }
                                                            } else {
                                                                ArrayList<String> errorList = new ArrayList<>();
                                                                errorList.add("Lebron James");
                                                                oos.writeObject(errorList);
                                                            }
                                                        }
                                                        break;
                                                    case 2:
                                                        userCancelled = (boolean) ois.readObject();
                                                        if (!userCancelled) {
                                                            String constraint = (String) ois.readObject();
                                                            b = Buyers.viewListingsWithConstraint2(constraint);
                                                            if (!(b.isEmpty() || b == null)) {
                                                                ArrayList<String> ticketPackage = MarketplaceServer.buildListedTicketsPackage3(b);
                                                                oos.writeObject(ticketPackage);
                                                                boolean wantToBuy = (boolean) ois.readObject();
                                                                if (wantToBuy) {
                                                                    int whatTicket = (int) ois.readObject();
                                                                    int howMany = (int) ois.readObject();
                                                                    synchronized (obj) {
                                                                        TicketInfoCombo bAgain = Buyers.viewListingsWithConstraint(constraint);
                                                                        Ticket boughtTicket = Buyers.buyTicket(bAgain.getListedTicks(), whatTicket, howMany, bAgain.getHowManyTicks());
                                                                        if (boughtTicket != null) {
                                                                            currentShoppingCart.add(new CartItems(boughtTicket, howMany));
                                                                            curBuyer.setShoppingCart(currentShoppingCart);
                                                                            curBuyer.updateShoppingCart();
                                                                            oos.writeObject(true);
                                                                        } else {
                                                                            oos.writeObject(false);
                                                                        }
                                                                    }
                                                                }
                                                            } else {
                                                                ArrayList<String> errorList = new ArrayList<>();
                                                                errorList.add("Lebron James");
                                                                oos.writeObject(errorList);
                                                            }
                                                        }
                                                        break;
                                                    case 3:
                                                        b = Buyers.viewAllListingsSortedByTicketQuantity2();
                                                        if (!(b.isEmpty() || b == null)) {
                                                            ArrayList<String> ticketPackage = MarketplaceServer.buildListedTicketsPackage3(b);
                                                            oos.writeObject(ticketPackage);
                                                            boolean wantToBuy = (boolean) ois.readObject();
                                                            if (wantToBuy) {
                                                                int whatTicket = (int) ois.readObject();
                                                                int howMany = (int) ois.readObject();
                                                                synchronized (obj) {
                                                                    TicketInfoCombo bAgain = Buyers.viewAllListingsSortedByTicketQuantity();
                                                                    Ticket boughtTicket = Buyers.buyTicket2(bAgain.getListedTicks(), whatTicket, howMany, bAgain.getHowManyTicks());
                                                                    if (boughtTicket != null) {
                                                                        currentShoppingCart.add(new CartItems(boughtTicket, howMany));
                                                                        curBuyer.setShoppingCart(currentShoppingCart);
                                                                        curBuyer.updateShoppingCart();
                                                                        oos.writeObject(true);
                                                                    } else {
                                                                        oos.writeObject(false);
                                                                    }
                                                                }
                                                            }
                                                        } else {
                                                            ArrayList<String> errorList = new ArrayList<>();
                                                            errorList.add("Lebron James");
                                                            oos.writeObject(errorList);
                                                        }
                                                        break;
                                                    case -3:
                                                        whileInListMenu = false;
                                                        break;
                                                }
                                                break;
                                            case 2:
                                                currentShoppingCart = curBuyer.retrieveShoppingCart();
                                                previousShoppingCart = curBuyer.retrieveShoppingCart2();
                                                if (currentShoppingCart == null)
                                                    currentShoppingCart = new ArrayList<>();
                                                if (previousShoppingCart == null)
                                                    previousShoppingCart = new ArrayList<>();
                                                synchronized (obj) {
                                                    curBuyer.setShoppingCart(currentShoppingCart);
                                                    curBuyer.setPreviousShopped(previousShoppingCart);
                                                    currentShoppingPackage = MarketplaceServer.buildBuyerCurrentShoppingCartPackage(curBuyer);
                                                    for (int i = 0; i < currentShoppingPackage.size(); i++) {
                                                        String originalString = currentShoppingPackage.get(i);
                                                        String[] parts = originalString.split(";");
                                                        String sport = parts[0];
                                                        String location = parts[1];
                                                        String section = parts[2];
                                                        String[] priceAndQuantity = parts[3].split("&");
                                                        String price = priceAndQuantity[0];
                                                        String quantity = priceAndQuantity[1];

                                                        // Format the string without spaces after semicolons
                                                        String formattedString = String.format("Sport:%s;Location:%s;Section:%s;Price:%s;Quantity:%s",
                                                                sport, location, section, price, quantity);

                                                        // Replace the original string in the ArrayList
                                                        currentShoppingPackage.set(i, formattedString);
                                                    }
                                                    oos.writeObject(currentShoppingPackage);
                                                }
                                                break;
                                            case 3:
                                                currentShoppingCart = curBuyer.retrieveShoppingCart();
                                                previousShoppingCart = curBuyer.retrieveShoppingCart2();
                                                if (currentShoppingCart == null)
                                                    currentShoppingCart = new ArrayList<>();
                                                if (previousShoppingCart == null)
                                                    previousShoppingCart = new ArrayList<>();

                                                synchronized (obj) {
                                                    curBuyer.setShoppingCart(currentShoppingCart);
                                                    curBuyer.setPreviousShopped(previousShoppingCart);
                                                    currentShoppingPackage = MarketplaceServer.buildBuyerCurrentShoppingCartPackage(curBuyer);
                                                    oos.writeObject(currentShoppingPackage);
                                                    if(!currentShoppingPackage.isEmpty()) {
                                                        boolean notDone = (boolean) ois.readObject();
                                                        if(notDone) {
                                                            int idx = (int) ois.readObject();
                                                            ArrayList<CartItems> currentShoppingCart2 =
                                                                    curBuyer.retrieveShoppingCart();
                                                            try {
                                                                if(currentShoppingCart.get(idx).equals(currentShoppingCart2.get(idx))) {

                                                                    StoreNameIDCombo combo =
                                                                            Buyers.getSellerID(currentShoppingCart.get(idx).getTicket().toString());
                                                                    Buyers.updateTicketQuantity("SellerInfo.txt", combo.getSellerID(),
                                                                            currentShoppingCart.get(idx).getTicket().toString(),
                                                                            -1 * currentShoppingCart.get(idx).getQTY());
                                                                    currentShoppingCart.remove(idx);
                                                                    curBuyer.setShoppingCart(currentShoppingCart);
                                                                    curBuyer.updateShoppingCart();
                                                                    oos.writeObject(true);
                                                                } else {
                                                                    try {
                                                                        if(currentShoppingCart.get(idx).equals(currentShoppingCart2.get(idx-1))) {
                                                                            StoreNameIDCombo combo =
                                                                                    Buyers.getSellerID(currentShoppingCart2.get(idx-1).getTicket().toString());
                                                                            Buyers.updateTicketQuantity("SellerInfo.txt", combo.getSellerID(),
                                                                                    currentShoppingCart2.get(idx-1).getTicket().toString(),
                                                                                    -1 * currentShoppingCart2.get(idx-1).getQTY());
                                                                            currentShoppingCart2.remove(idx-1);
                                                                            curBuyer.setShoppingCart(currentShoppingCart2);
                                                                            curBuyer.updateShoppingCart();
                                                                            oos.writeObject(true);
                                                                        } else {
                                                                            oos.writeObject(false);
                                                                        }
                                                                    } catch (Exception e) {
                                                                        oos.writeObject(false);
                                                                    }
                                                                }
                                                            } catch (Exception e) {
                                                                try {
                                                                    if(currentShoppingCart.get(idx).equals(currentShoppingCart2.get(idx-1))) {
                                                                        StoreNameIDCombo combo =
                                                                                Buyers.getSellerID(currentShoppingCart2.get(idx-1).getTicket().toString());
                                                                        Buyers.updateTicketQuantity("SellerInfo.txt", combo.getSellerID(),
                                                                                currentShoppingCart2.get(idx-1).getTicket().toString(),
                                                                                -1 * currentShoppingCart2.get(idx-1).getQTY());
                                                                        currentShoppingCart2.remove(idx-1);
                                                                        curBuyer.setShoppingCart(currentShoppingCart2);
                                                                        curBuyer.updateShoppingCart();
                                                                        oos.writeObject(true);
                                                                    } else {
                                                                        oos.writeObject(false);
                                                                    }
                                                                } catch (Exception ex) {
                                                                    oos.writeObject(false);
                                                                }
                                                            }
                                                        }

                                                    }
                                                }

                                                break;
                                            case 4:
                                                currentShoppingCart = curBuyer.retrieveShoppingCart();
                                                previousShoppingCart = curBuyer.retrieveShoppingCart2();
                                                if (currentShoppingCart == null)
                                                    currentShoppingCart = new ArrayList<>();
                                                if (previousShoppingCart == null)
                                                    previousShoppingCart = new ArrayList<>();
                                                synchronized (obj) {
                                                    curBuyer.setShoppingCart(currentShoppingCart);
                                                    curBuyer.setPreviousShopped(previousShoppingCart);
                                                    currentShoppingPackage = MarketplaceServer.buildBuyerCurrentShoppingCartPackage(curBuyer);
                                                    oos.writeObject(currentShoppingPackage);
                                                    boolean wantToCheckout = (boolean) ois.readObject();
                                                    if (wantToCheckout) {
                                                        boolean succesfulCheckout = curBuyer.checkoutCart();
                                                        oos.writeObject(succesfulCheckout);
                                                    } else {

                                                    }
                                                }
                                                break;
                                            case 5:
                                                whileInMenu = false;
                                                whileInListMenu = false;
                                                break;
                                        }

                                    }


                                }

                                break;
                            case 2:
                                curLoginInfo = new LoginInfo(email, password);
                                curBuyer = new Buyers(id, curLoginInfo);
                                ArrayList<String> shoppingCartPackage = MarketplaceServer.buildBuyerPreviousShoppingCartPackage(curBuyer);
                                oos.writeObject(shoppingCartPackage);
                                boolean willGoIntoMenu = (boolean) ois.readObject();

                                for (int i = 0; willGoIntoMenu; i++) {
                                    if (i == 0) {
                                        ArrayList<String> shoppingCartPackage2 = MarketplaceServer.buildBuyerPreviousShoppingCartPackage(curBuyer);
                                        oos.writeObject(shoppingCartPackage2);
                                    }

                                    int whatTodoNow = (int) ois.readObject();
                                    switch (whatTodoNow) {
                                        case 1:
                                            int currentWait = (int) ois.readObject();
                                            break;
                                        case 2:
                                            willGoIntoMenu = false;
                                            break;
                                    }
                                }
                                break;
                            case 3:
                                curLoginInfo = new LoginInfo(email, password);
                                curBuyer = new Buyers(id, curLoginInfo);
                                boolean notDone = true;
                                while(notDone) {
                                    notDone = (boolean) ois.readObject();
                                    if(notDone) {
                                        int choice = (int) ois.readObject();
                                        if(choice == 0) {
                                            String sortStr = (String) ois.readObject();
                                            if(sortStr != null) {
                                                int sortChoice = (int) ois.readObject();
                                                if(sortChoice == 0) {
                                                    String s = "";
                                                    ArrayList<String> list = MarketplaceServer.printGeneralDash(
                                                            curBuyer.generalDashbaord(false));

                                                    for (int i = 0; i < list.size()-1; i++) {
                                                        s += list.get(i)+"\n";
                                                    }

                                                    s += list.get(list.size()-1);

                                                    oos.writeObject(s);
                                                } else {
                                                    String s = "";
                                                    ArrayList<String> list = MarketplaceServer.printGeneralDash(
                                                            curBuyer.generalDashbaord(true));

                                                    for (int i = 0; i < list.size()-1; i++) {
                                                        s += list.get(i)+"\n";
                                                    }

                                                    s += list.get(list.size()-1);

                                                    oos.writeObject(s);
                                                }
                                            }
                                        } else {
                                            String sortStr = (String) ois.readObject();
                                            if(sortStr != null) {
                                                int sortChoice = (int) ois.readObject();
                                                if(sortChoice == 0) {
                                                    String s = "";
                                                    ArrayList<String> list = MarketplaceServer.printBuyerDash(
                                                            curBuyer.listCustomerSpecificDashboard(false));
                                                    for (int i = 0; i < list.size(); i++) {
                                                        s += list.get(i);
                                                    }
                                                    oos.writeObject(s);
                                                } else {
                                                    String s = "";
                                                    ArrayList<String> list = MarketplaceServer.printBuyerDash(
                                                            curBuyer.listCustomerSpecificDashboard(true));
                                                    for (int i = 0; i < list.size(); i++) {
                                                        s += list.get(i);
                                                    }
                                                    oos.writeObject(s);

                                                }
                                            }
                                        }
                                    }
                                }
                                break;
                            case 4:
                                boolean notReturnedToMenu = true;
                                while (notReturnedToMenu) {
                                    email = MarketplaceServer.getEmailPassword(id).get(0);
                                    password = MarketplaceServer.getEmailPassword(id).get(1);
                                    System.out.println(email + password);
                                    int whatEditOption = (int) ois.readObject();
                                    System.out.println(whatEditOption);
                                    switch (whatEditOption) {
                                        case 1:
                                            ArrayList<String> details = MarketplaceServer.readFile("LoginInfo.txt");
                                            for (int i = 0; i < details.size(); i++) {
                                                String[] arr = details.get(i).split(",");
                                                if (email.equals(arr[1]) && password.equals(arr[2])) {
                                                    oos.writeObject(arr[0]);
                                                    oos.writeObject(arr[1]);


                                                    if (arr[3].equals("b")) {
                                                        oos.writeObject("Buyer");
                                                    } else {
                                                        oos.writeObject("Seller");
                                                    }
                                                }
                                            }

                                            break;
                                        case 2:
                                            String newEmail = (String) ois.readObject();
                                            if (!newEmail.equals("")) {
                                                MarketplaceServer.editEmail(id, newEmail);
                                                //Thread.sleep(1000);
//                                                email = MarketplaceServer.getEmailPassword(id).get(0);
//                                                System.out.println(email);
                                            }
                                            //editing the email
                                            break;
                                        case 3:
                                            String newPassword = (String) ois.readObject();
                                            if (newPassword != null) {
                                                MarketplaceServer.editPassword(id, newPassword);
                                            }

                                            break;
                                        case 4:
                                            String confirmation = (String) ois.readObject();
                                            if (confirmation.equals("yes")) {
                                                MarketplaceServer.deleteAccount(email, password);
                                                notLoggedOut = false;
                                                notReturnedToMenu = false;
                                            }
                                            break;
                                        case 5:
                                            oos.writeObject(role);
                                            notReturnedToMenu = false;
                                            break;

                                    }

                                }
                                break;
                            case 5:
                                System.out.println("Thread closing");
                                notLoggedOut = false;
                                break;
                        }
                        break;
                    case "s":
                        System.out.println("Before waiting for object");
                        whatDash = (int) ois.readObject();
                        System.out.println(whatDash);
                        switch (whatDash) {
                            case 1:
                                int sellOption = 0;
                                while (sellOption != 4) {
                                    System.out.println("In loop");
                                    sellOption = (int) ois.readObject();
                                    System.out.println(sellOption);
                                    Sellers seller = new Sellers(id);
                                    ArrayList<String> stores1 = seller.retrieveStores();
                                    if (sellOption != 4) {
                                        if (sellOption == 1) {
                                            oos.writeObject(stores1);
                                            oos.flush();
                                            if (stores1 == null) {
                                                int option = (int) ois.readObject();
                                                if (option == JOptionPane.YES_OPTION) {
                                                    String storeName = (String) ois.readObject();
                                                    if (storeName != null) {
                                                        seller.addStore(storeName);
                                                    }
                                                }
                                            } else {
                                                String ticket = (String) ois.readObject();
                                                if (ticket != null) {
                                                    int option = (int) ois.readObject();
                                                    if (option == 0) {
                                                        boolean notDone = true;
                                                        while (notDone) {
                                                            notDone = (boolean) ois.readObject();
                                                            if (notDone) {
                                                                String store = (String) ois.readObject();
                                                                System.out.println(store);
                                                                if (store != null) {
                                                                    if (store.isEmpty() || store.equals("\n")) {
                                                                    } else {
                                                                        seller.addStore(store);
                                                                    }
                                                                } else {
                                                                    notDone = false;
                                                                }
                                                            }
                                                        }

                                                    } else {
                                                        String storeChoice = (String) ois.readObject();
                                                        if (storeChoice != null) {
                                                            boolean notDone = true;
                                                            while (notDone) {
                                                                notDone = (boolean) ois.readObject();
                                                                if (notDone) {
                                                                    boolean notNull = (boolean) ois.readObject();
                                                                    if (notNull) {
                                                                        boolean invalid = (boolean) ois.readObject();
                                                                        if (!invalid) {
                                                                            String sport = (String) ois.readObject();
                                                                            String location = (String) ois.readObject();
                                                                            String section = (String) ois.readObject();
                                                                            double price = (double) ois.readObject();
                                                                            int quantity = (int) ois.readObject();
                                                                            String store = (String) ois.readObject();
                                                                            seller.addTickets(new Ticket(sport, location, section,
                                                                                    price), quantity, store);
                                                                        }
                                                                    } else {
                                                                        notDone = false;
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }

                                                }
                                            }
                                        } else if (sellOption == 2) {
                                            oos.writeObject(stores1);
                                            oos.flush();
                                            if (stores1 != null) {
                                                String storeChoice = (String) ois.readObject();
                                                if (storeChoice != null) {
                                                    String store = (String) ois.readObject();
                                                    ArrayList<String> listings = seller.retrieveListings(store);
                                                    SellerListing listing = seller.retrieveProducts(store);
                                                    oos.writeObject(listings);
                                                    if (listings != null) {
                                                        String oldTicket = (String) ois.readObject();
                                                        if (oldTicket != null) {
                                                            int ticketIdx = (int) ois.readObject();
                                                            String editStr = (String) ois.readObject();
                                                            if (editStr != null) {
                                                                int editChoice = (int) ois.readObject();
                                                                boolean notDone = true;
                                                                while (notDone) {
                                                                    notDone = (boolean) ois.readObject();
                                                                    if (notDone) {
                                                                        switch (editChoice) {
                                                                            case 0:
                                                                                boolean notNull =
                                                                                        (boolean) ois.readObject();
                                                                                if (notNull) {
                                                                                    boolean invalid =
                                                                                            (boolean) ois.readObject();
                                                                                    if (!invalid) {
                                                                                        synchronized (obj) {
                                                                                            listing =
                                                                                                    seller.retrieveProducts(store);
                                                                                            String sport =
                                                                                                    (String) ois.readObject();
                                                                                            String location =
                                                                                                    (String) ois.readObject();
                                                                                            String section =
                                                                                                    (String) ois.readObject();
                                                                                            double price =
                                                                                                    (double) ois.readObject();
                                                                                            try {
                                                                                                seller.modTicket(listing.getTickets().get(ticketIdx), new Ticket(sport,
                                                                                                                location, section, price), store,
                                                                                                        Integer.parseInt(listing.getQuantities().get(ticketIdx)));
                                                                                                oos.writeObject(true);
                                                                                                notDone = false;
                                                                                            } catch (Exception e) {
                                                                                                oos.writeObject(false);
                                                                                                notDone = false;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                } else {
                                                                                    notDone = false;
                                                                                }
                                                                                break;
                                                                            case 1:
                                                                                boolean notNull2 =
                                                                                        (boolean) ois.readObject();
                                                                                if (notNull2) {
                                                                                    boolean invalid =
                                                                                            (boolean) ois.readObject();
                                                                                    if (!invalid) {
                                                                                        synchronized (obj) {
                                                                                            listing =
                                                                                                    seller.retrieveProducts(store);
                                                                                            int quantity =
                                                                                                    (int) ois.readObject();
                                                                                            try {
                                                                                                seller.modTicket(listing.getTickets().get(ticketIdx),
                                                                                                        listing.getTickets().get(ticketIdx),
                                                                                                        store, quantity);
                                                                                                oos.writeObject(true);
                                                                                                notDone = false;
                                                                                            } catch (Exception e) {
                                                                                                oos.writeObject(false);
                                                                                                notDone = false;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                } else {
                                                                                    notDone = false;
                                                                                }
                                                                                break;
                                                                            case 2:
                                                                                boolean notNull3 =
                                                                                        (boolean) ois.readObject();
                                                                                if (notNull3) {
                                                                                    boolean invalid =
                                                                                            (boolean) ois.readObject();
                                                                                    if (!invalid) {
                                                                                        synchronized (obj) {
                                                                                            listing =
                                                                                                    seller.retrieveProducts(store);
                                                                                            String sport =
                                                                                                    (String) ois.readObject();
                                                                                            String location =
                                                                                                    (String) ois.readObject();
                                                                                            String section =
                                                                                                    (String) ois.readObject();
                                                                                            double price =
                                                                                                    (double) ois.readObject();
                                                                                            int quantity =
                                                                                                    (int) ois.readObject();
                                                                                            try {
                                                                                                seller.modTicket(listing.getTickets().get(ticketIdx),
                                                                                                        new Ticket(sport,
                                                                                                                location, section, price),
                                                                                                        store, quantity);
                                                                                                oos.writeObject(true);
                                                                                                notDone = false;
                                                                                            } catch (Exception e) {
                                                                                                oos.writeObject(false);
                                                                                                notDone = false;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                } else {
                                                                                    notDone = false;
                                                                                }
                                                                                break;
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }

                                                    }

                                                }
                                                //if(storeChoice)
                                            }
                                        } else if (sellOption == 3) {
                                            oos.writeObject(stores1);
                                            oos.flush();
                                            if (stores1 != null) {
                                                String storeChoice = (String) ois.readObject();
                                                if (storeChoice != null) {
                                                    String store = (String) ois.readObject();
                                                    ArrayList<String> listings = seller.retrieveListings(store);
                                                    SellerListing listing = seller.retrieveProducts(store);
                                                    oos.writeObject(listings);
                                                    if (listings != null) {
                                                        String ticket = (String) ois.readObject();
                                                        if (ticket != null) {
                                                            int ticketIdx = (int) ois.readObject();
                                                            System.out.println("Ticket index:" + ticketIdx);
                                                            synchronized (obj) {
                                                                SellerListing listing1 = seller.retrieveProducts(store);
                                                                try {
                                                                    if (listing.getTickets().get(ticketIdx).equals(listing1.getTickets().get(ticketIdx))) {
                                                                        seller.deleteTicket(listing1.getTickets().get(ticketIdx), store);
                                                                        oos.writeObject(true);
                                                                    } else {
                                                                        try {
                                                                            int ticketIdx1 = ticketIdx - 1;
                                                                            if (listing.getTickets().get(ticketIdx).equals(listing1.getTickets().get(ticketIdx1))) {
                                                                                seller.deleteTicket(listing1.getTickets().get(ticketIdx1), store);
                                                                                oos.writeObject(true);
                                                                            } else {
                                                                                oos.writeObject(false);
                                                                            }
                                                                        } catch (Exception e) {
                                                                            oos.writeObject(false);
                                                                        }
                                                                    }
                                                                } catch (Exception e) {
                                                                    try {
                                                                        int ticketIdx1 = ticketIdx - 1;
                                                                        System.out.println(listing1.getTickets().get(ticketIdx1));
                                                                        if (listing.getTickets().get(ticketIdx).equals(listing1.getTickets().get(ticketIdx1))) {
                                                                            seller.deleteTicket(listing1.getTickets().get(ticketIdx1), store);
                                                                            oos.writeObject(true);
                                                                        } else {
                                                                            oos.writeObject(false);
                                                                        }
                                                                    } catch (Exception ex) {
                                                                        oos.writeObject(false);
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                break;
                            case 2:
                                Sellers curSeller = new Sellers(id);
                                ArrayList<String> stores = curSeller.retrieveStores();
                                oos.writeObject(stores);
                                boolean exitCurrentMenu = (boolean) ois.readObject();
                                if (!exitCurrentMenu) {
                                    while (!exitCurrentMenu) {
                                        int curSeleciton = (int) ois.readObject();
                                        if (curSeleciton == 1) {
                                            int selectedStore = (int) ois.readObject();
                                            ArrayList<String> sales = new ArrayList<>();
                                            ArrayList<String> allTransactions = MarketplaceServer.readFile("TransactionInfo.txt");
                                            double totalRevenue = 0.0;
                                            for (int i = 0; i < allTransactions.size(); i++) {
                                                String[] transactionInfo = allTransactions.get(i).split(",");
                                                if (transactionInfo[2].equals(String.valueOf(id))) {
                                                    if (transactionInfo[3].equals(stores.get(selectedStore - 1))) {
                                                        String buyerEmail = null;
                                                        ArrayList<String> buyers = Marketplace.readFile("BuyerHistory.txt");
                                                        for (int j = 0; j < buyers.size(); j++) {
                                                            String[] buyerInfo = buyers.get(j).split(",");
                                                            if (buyerInfo[0].equals(transactionInfo[1])) {
                                                                buyerEmail = buyerInfo[1];
                                                                break;
                                                            }
                                                        }
                                                        String[] ticketInfo = transactionInfo[6].split(";");
                                                        sales.add("Ticket Info -\n" +
                                                                "Sport: " + ticketInfo[0] + "\n" +
                                                                "Location: " + ticketInfo[1] + "\n" +
                                                                "Section: " + ticketInfo[2] + "\n" +
                                                                "Price: " + ticketInfo[3] + "\n" +
                                                                "Quantity purchased: " + transactionInfo[5] + "\n" +
                                                                "Customer Info -" + "\n" +
                                                                "ID: " + transactionInfo[1] + "\n" +
                                                                "Email: " + buyerEmail + "\n" +
                                                                "Revenue from sale: " + String.format("%.2f",
                                                                Double.parseDouble(transactionInfo[4]) * Integer.parseInt(transactionInfo[5]))
                                                        );

                                                        totalRevenue += Double.parseDouble(transactionInfo[4]) * Integer.parseInt(transactionInfo[5]);

                                                    }
                                                }

                                            }

                                            if (!sales.isEmpty()) {


                                                sales.add("Total Revenue from store: " + String.format("%.2f", totalRevenue));
                                            }
                                            oos.writeObject(sales);
                                        } else if (curSeleciton == 2) {
                                            exitCurrentMenu = true;
                                        }
                                    }
                                }
                                break;
                            case 3:
                                Sellers seller = new Sellers(id);
                                ArrayList<String> stores1 = seller.retrieveStores();
                                oos.writeObject(stores1);
                                boolean notDone = true;
                                while (notDone) {
                                    notDone = (boolean) ois.readObject();
                                    if (notDone) {
                                        ArrayList<String> allTransactions2 = MarketplaceServer.readFile("TransactionInfo" +
                                                ".txt");
                                        String choice = (String) ois.readObject();
                                        if (choice != null) {
                                            int storeIdx = (int) ois.readObject();
                                            int statChoice = (int) ois.readObject();
                                            switch (statChoice) {
                                                case 0:
                                                    String sortChoice = (String) ois.readObject();
                                                    if (sortChoice != null) {
                                                        int sort = (int) ois.readObject();
                                                        ArrayList<String> buyerIDList = new ArrayList<>();
                                                        ArrayList<PrintableStat> toPrint = new ArrayList<>();

                                                        for (int i = 0; i < allTransactions2.size(); i++) {
                                                            String[] transactionLine = allTransactions2.get(i).split(",");
                                                            if (Integer.parseInt(transactionLine[2]) == seller.getSellerID() &&
                                                                    transactionLine[3].equals(stores1.get(storeIdx))) {
                                                                if (buyerIDList.isEmpty()) {
                                                                    buyerIDList.add(transactionLine[1]);
                                                                } else {
                                                                    if (!buyerIDList.contains(transactionLine[1])) {
                                                                        buyerIDList.add(transactionLine[1]);
                                                                    }
                                                                }
                                                            }
                                                        }

                                                        ArrayList<Integer> quantities = new ArrayList<>();

                                                        for (int i = 0; i < buyerIDList.size(); i++) {
                                                            ArrayList<String> uniqueItems = new ArrayList<>();
                                                            int qty = 0;
                                                            for (int j = 0; j < allTransactions2.size(); j++) {
                                                                String[] transactionLine = allTransactions2.get(j).split(",");
                                                                if (Integer.parseInt(transactionLine[2]) == seller.getSellerID() &&
                                                                        transactionLine[1].equals(buyerIDList.get(i)) &&
                                                                        transactionLine[3].equals(stores1.get(storeIdx))) {
                                                                    String[] ticketInfo = transactionLine[6].split(";");
                                                                    qty += Integer.parseInt(transactionLine[5]);
                                                                    if (uniqueItems.isEmpty()) {
                                                                        uniqueItems.add(ticketInfo[0] + ";" + ticketInfo[1] + ";" +
                                                                                ticketInfo[3]);
                                                                    } else {
                                                                        if (!uniqueItems.contains(ticketInfo[0] + ";" + ticketInfo[1] + ";"
                                                                                + ticketInfo[3])) {
                                                                            uniqueItems.add(ticketInfo[0] + ";" + ticketInfo[1] + ";"
                                                                                    + ticketInfo[3]);
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                            quantities.add(qty);

                                                            String buyerEmail = "";

                                                            ArrayList<String> buyers = Marketplace.readFile("BuyerHistory.txt");
                                                            for (int j = 0; j < buyers.size(); j++) {
                                                                String[] buyerInfo = buyers.get(j).split(",");
                                                                if (buyerInfo[0].equals(buyerIDList.get(i))) {
                                                                    buyerEmail = buyerInfo[1];
                                                                    break;
                                                                }
                                                            }

                                                            toPrint.add(new PrintableStat(Integer.parseInt(buyerIDList.get(i)), buyerEmail,
                                                                    quantities.get(i),
                                                                    uniqueItems.size()));
                                                        }

                                                        if (sort == 0) {
                                                            Collections.sort(toPrint,
                                                                    Comparator.comparing(PrintableStat::getBuyerID));
                                                        } else if (sort == 1) {
                                                            Collections.sort(toPrint,
                                                                    Comparator.comparing(PrintableStat::getBuyerEmail));
                                                        } else if (sort == 2) {
                                                            Collections.sort(toPrint,
                                                                    Comparator.comparing(PrintableStat::getTotalItems));
                                                        }

                                                        ArrayList<String> listToSend = new ArrayList<>();


                                                        for (int i = 0; i < toPrint.size(); i++) {
                                                            listToSend.add(toPrint.get(i).toString());
                                                        }

                                                        oos.writeObject(listToSend);


                                                    }
                                                    break;
                                                case 1:
                                                    String sortChoice1 = (String) ois.readObject();
                                                    if (sortChoice1 != null) {
                                                        int sort = (int) ois.readObject();
                                                        ArrayList<String> listToSend = new ArrayList<>();
                                                        switch (sort) {
                                                            case 0:
                                                                listToSend =
                                                                        MarketplaceServer.uniqueProductsByLocation(stores1.get(storeIdx),
                                                                                seller.getSellerID());
                                                                break;
                                                            case 1:
                                                                listToSend =
                                                                        MarketplaceServer.uniqueProductsBySport(stores1.get(storeIdx),
                                                                                seller.getSellerID());
                                                                break;
                                                            case 2:
                                                                listToSend =
                                                                        Marketplace.uniqueProductsBySales(stores1.get(storeIdx),
                                                                                seller.getSellerID());
                                                                break;
                                                        }

                                                        oos.writeObject(listToSend);
                                                    }
                                                    break;
                                            }
                                        }

                                    }
                                }
                                break;
                            case 4:
                                boolean notReturnedToMenu = true;
                                while (notReturnedToMenu) {
                                    email = MarketplaceServer.getEmailPassword(id).get(0);
                                    password = MarketplaceServer.getEmailPassword(id).get(1);
                                    System.out.println(email + password);
                                    int whatEditOption = (int) ois.readObject();
                                    System.out.println(whatEditOption);
                                    switch (whatEditOption) {
                                        case 1:
                                            ArrayList<String> details = MarketplaceServer.readFile("LoginInfo.txt");
                                            for (int i = 0; i < details.size(); i++) {
                                                String[] arr = details.get(i).split(",");
                                                if (email.equals(arr[1]) && password.equals(arr[2])) {
                                                    oos.writeObject(arr[0]);
                                                    oos.writeObject(arr[1]);


                                                    if (arr[3].equals("b")) {
                                                        oos.writeObject("Buyer");
                                                    } else {
                                                        oos.writeObject("Seller");
                                                    }
                                                }
                                            }

                                            break;
                                        case 2:
                                            String newEmail = (String) ois.readObject();
                                            if (!newEmail.equals("")) {
                                                MarketplaceServer.editEmail(id, newEmail);
                                                //Thread.sleep(1000);
//                                                email = MarketplaceServer.getEmailPassword(id).get(0);
//                                                System.out.println(email);
                                            }
                                            //editing the email
                                            break;
                                        case 3:
                                            String newPassword = (String) ois.readObject();
                                            if (newPassword != null) {
                                                MarketplaceServer.editPassword(id, newPassword);
                                            }

                                            break;
                                        case 4:
                                            String confirmation = (String) ois.readObject();
                                            if (confirmation.equals("yes")) {
                                                MarketplaceServer.deleteAccount(email, password);
                                                notLoggedOut = false;
                                                notReturnedToMenu = false;
                                            }
                                            break;
                                        case 5:
                                            oos.writeObject(role);
                                            notReturnedToMenu = false;
                                            break;

                                    }

                                }
                                break;
                            case 5:
                                System.out.println("Thread closing");
                                notLoggedOut = false;
                        }
                }
            }

            // System.out.println("Out");
        } catch (Exception e) {

        }

    }


}
