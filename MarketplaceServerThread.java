import javax.swing.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Project 5: MarketplaceServerThread
 * 
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
                        int whatDash = (int)ois.readObject();
                        System.out.println(whatDash);
                        switch(whatDash)
                        {
                            case 1:
                             break;
                            case 2:
                             LoginInfo curLoginInfo = new LoginInfo(email, password);
                             Buyers curBuyer  = new Buyers(id, curLoginInfo);
                             ArrayList<String> shoppingCartPackage = MarketplaceServer.buildBuyerPreviousShoppingCartPackage(curBuyer);
                             oos.writeObject(shoppingCartPackage);
                             boolean willGoIntoMenu = (boolean)ois.readObject();
                             
                             for(int i = 0;willGoIntoMenu;i++)
                             {  
                             if(i == 0)
                             {
                                ArrayList<String> shoppingCartPackage2 = MarketplaceServer.buildBuyerPreviousShoppingCartPackage(curBuyer);
                                oos.writeObject(shoppingCartPackage2);
                             }
                            
                               int whatTodoNow = (int)ois.readObject();
                                switch(whatTodoNow)
                                {
                                    case 1:
                                    int currentWait = (int)ois.readObject();
                                     break;
                                    case 2:
                                     willGoIntoMenu = false;
                                     break;
                                }
                             }
                             break;
                            case 3:
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
                                Sellers seller = new Sellers(id);
                                ArrayList<String> stores1 = seller.retrieveStores();
                                oos.writeObject(stores1);
                                oos.flush();
                                if(stores1 == null) {
                                    int option = (int) ois.readObject();
                                    if(option == JOptionPane.YES_OPTION) {
                                        String storeName = (String) ois.readObject();
                                        if(storeName != null) {
                                            seller.addStore(storeName);
                                        }
                                    }
                                } else {
                                    String ticket = (String) ois.readObject();
                                    if(ticket != null) {
                                        int option = (int) ois.readObject();
                                        if(option == 0) {
                                            boolean notDone = true;
                                            while(notDone) {
                                                System.out.println();
                                                notDone = (boolean) ois.readObject();
                                                if(notDone) {
                                                    String store = (String) ois.readObject();
                                                    if(store != null) {
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
                                            if(storeChoice != null) {
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
