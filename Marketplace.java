import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Project 4: Marketplace
 *
 * The main class that holds the marketplace for tickets.
 *
 * @author Shrish Mahesh, Henry Lee, Lab Section L20
 * @version October 30, 2023
 */

public class Marketplace {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Welcome to Tickets@Purdue. Please enter a value to continue.");
        String email = null;
        String password = null;
        int id = 0;
        int optionInitial = 0;
        System.out.println("1. Login\n" + "2. Create a new account");
        do {
            try {
                optionInitial = Integer.parseInt(scan.nextLine());
                if (optionInitial < 1 || optionInitial > 2) {
                    System.out.println("Please enter a valid number.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer.");
            }
        } while (optionInitial < 1 || optionInitial > 2);


        switch (optionInitial) {
            case 1:
                System.out.println("Please login.");
                boolean notLoggedIn = true;
                while (notLoggedIn) {
                    System.out.println("Email: ");
                    email = scan.nextLine();
                    System.out.println("Password: ");
                    password = scan.nextLine();
                    LoginInfo user = new LoginInfo(email, password);
                    if (!user.authenticate(email, password).isEmpty()) {
                        System.out.println("Successfully logged in!");
                        String[] userInfo = user.authenticate(email, password).split(",");
                        id = Integer.parseInt(userInfo[0]);
                        notLoggedIn = false;

                    } else {
                        System.out.println("Error.  Account not found.  Please try again.");
                    }
                }

                break;
            case 2:
                int option2;
                boolean invalidChoice = false;
                int accountChoice = 0;
                boolean invalidEmail = false;
                boolean invalidPassword = false;
                String password2;

                System.out.println("What type of account would you like to create? Press 1 for buyer or 2 for " +
                        "seller");
                do {
                    try {
                        accountChoice = Integer.parseInt(scan.nextLine());
                        if (accountChoice < 1 || accountChoice > 2) {
                            System.out.println("Please enter a valid number");
                            invalidChoice = true;
                        } else {
                            invalidChoice = false;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Please enter a valid integer");
                        invalidChoice = true;
                    }
                } while (invalidChoice);


                System.out.println("Please enter an email: ");
                do {
                    email = scan.nextLine();
                    if (!email.contains("@")) {
                        invalidEmail = true;
                        System.out.println("Invalid email. Email must contain an @symbol.");
                        System.out.println("Please enter the email again: ");
                    } else {
                        invalidEmail = false;
                    }
                } while (invalidEmail);

                System.out.println("Please enter a password: ");
                System.out.println("Passwords must be at least 8 characters and at least 1 letter, number and special" +
                        " character");

                do {
                    password = scan.nextLine();
                    if (password.length() < 8 || !isStrongAlphanumeric(password)) {
                        invalidPassword = true;
                        System.out.println("Invalid password. Please check your password is 8 or more " +
                                "characters and has a letters, number and special character.");
                        System.out.println("Please enter the password again: ");
                    } else {
                        invalidPassword = false;
                        if (!isStrongAlphanumeric(password)) {
                            System.out.println("Invalid password, must have letters, numbers and special characters.");
                            System.out.println("Please enter the password again: ");
                            invalidPassword = true;
                        }
                    }
                } while (invalidPassword);

                System.out.println("Please re-enter the password for confirmation.");
                do {
                    password2 = scan.nextLine();

                    if (password.equals(password2)) {
                        invalidPassword = false;
                    } else {
                        System.out.println("Password does not match, please re-enter the password.");
                        invalidPassword = true;
                    }
                } while (invalidPassword);

                ArrayList<String> fileInfo = readFile("LoginInfo.txt");
                ArrayList<String> userInfo = new ArrayList<>();
                String role;
                String filename;
                if (accountChoice == 1) {
                    role = "b";
                    filename = "BuyerInfo.txt";
                } else {
                    role = "s";
                    filename = "SellerInfo.txt";
                }

                userInfo = readFile(filename);

                if (fileInfo.isEmpty()) {
                    fileInfo.add(String.format("1,%s,%s,%s", email, password, role));
                    userInfo.add(String.format("1,%s", email));
                    id = 1;
                } else {
                    String[] lastLine = fileInfo.get(fileInfo.size() - 1).split(",");

                    fileInfo.add(String.format("%d,%s,%s,%s", Integer.parseInt(lastLine[0]) + 1, email,
                            password, role));
                    id = Integer.parseInt(lastLine[0]) + 1;
                    userInfo.add(String.format("%d,%s", Integer.parseInt(lastLine[0]) + 1, email));
                }
                writeFile(userInfo, filename);
                writeFile(fileInfo, "LoginInfo.txt");


                String filename2 = "";
                if (role.equals("b")) {
                    filename2 = "BuyerHistory.txt";
                    writeFile(userInfo, filename2);
                }
        }

        String role = determineRole(email, password);
        if (role.equals("b")) {

            while (true) {
                buyerDashboard();
                boolean shouldSkipLine = false;
                Buyers currentBuyer = new Buyers(id, new LoginInfo(email, password));
                ArrayList<CartItems> shoppingCart = currentBuyer.retrieveShoppingCart();
                ArrayList<CartItems> previousCart = currentBuyer.retrieveShoppingCart2();

                if (shoppingCart == null) shoppingCart = new ArrayList<>();
                if (previousCart == null) previousCart = new ArrayList<>();

                currentBuyer.setShoppingCart(shoppingCart);
                currentBuyer.setPreviousShopped(previousCart);
                String choice = scan.nextLine();
                switch (choice) {
                    case "1":
                        shouldSkipLine = true;

                        System.out.println("How do you want do view the listings:\nEnter 1 to view all listings.\nEnter 2 to view listings by store.\nEnter 3 to view listings by sport.\nEnter 4 to view listings by qty available");
                        int thischoice = scan.nextInt();
                        scan.nextLine();

                        switch (thischoice) {
                            case 1:
                                TicketInfoCombo b = Buyers.viewAllListingsGeneral();
                                if (b.getListedTicks().size() > 0) {
                                    System.out.println("What ticket number would you like to buy: ");
                                    int whatToBuy = scan.nextInt();
                                    System.out.println("How many do you want to buy?: ");
                                    int howManybuy = scan.nextInt();
                                    Ticket curTickey = Buyers.buyTicket(b.getListedTicks(), whatToBuy, howManybuy,
                                            b.getHowManyTicks());
                                    if (curTickey != null) {
                                        shoppingCart.add(new CartItems(curTickey, howManybuy));
                                        currentBuyer.setShoppingCart(shoppingCart);
                                        try {
                                            currentBuyer.updateShoppingCart();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                                break;
                            case 2:
                                scan.nextLine();
                                System.out.println("What store do you want to buy from: ");
                                String curStore = scan.nextLine();
                                TicketInfoCombo c = Buyers.viewListingsWithConstraint(curStore);
                                if (c.getListedTicks().size() > 0) {
                                    System.out.println("What ticket do you want: ");
                                    int whatToBuy2 = scan.nextInt();
                                    scan.nextLine();
                                    System.out.println("How many do you want to buy: ");
                                    int howManybuy2 = scan.nextInt();
                                    scan.nextLine();
                                    Ticket currentTicket = Buyers.buyTicket(c.getListedTicks(), whatToBuy2, howManybuy2, c.getHowManyTicks());
                                    if (currentTicket != null) {
                                        shoppingCart.add(new CartItems(currentTicket, howManybuy2));
                                        currentBuyer.setShoppingCart(shoppingCart);
                                        try {
                                            currentBuyer.updateShoppingCart();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                                break;
                            case 3:
                                scan.nextLine();
                                System.out.println("What sport do you want to buy tickets for: ");
                                String curSport = scan.nextLine();
                                TicketInfoCombo d = Buyers.viewListingsWithConstraint(curSport);
                                if (d.getListedTicks().size() > 0) {
                                    System.out.println("What ticket do you want: ");
                                    int whatToBuy2 = scan.nextInt();
                                    System.out.println("How many do you want to buy: ");
                                    int howManybuy2 = scan.nextInt();
                                    Ticket currentTicket = Buyers.buyTicket(d.getListedTicks(), whatToBuy2, howManybuy2, d.getHowManyTicks());
                                    if (currentTicket != null) {
                                        shoppingCart.add(new CartItems(currentTicket, howManybuy2));
                                        currentBuyer.setShoppingCart(shoppingCart);
                                        try {
                                            currentBuyer.updateShoppingCart();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                                break;
                            case 4:
                                TicketInfoCombo e = Buyers.viewAllListingsSortedByTicketQuantity();
                                if (e.getListedTicks().size() > 0) {
                                    System.out.println("What ticket do you want to buy: ");
                                    int whatToBuy = scan.nextInt();
                                    System.out.println("How many tickets do you want to buy: ");
                                    int howManybuy = scan.nextInt();
                                    Ticket curTicket = Buyers.buyTicket2(e.getListedTicks(), whatToBuy, howManybuy, e.getHowManyTicks());
                                    if (curTicket != null) {
                                        shoppingCart.add(new CartItems(curTicket, howManybuy));
                                        currentBuyer.setShoppingCart(shoppingCart);
                                        try {
                                            currentBuyer.updateShoppingCart();
                                        } catch (IOException g) {
                                            g.printStackTrace();
                                        }
                                    }
                                }
                                break;
                        }


                        break;
                    //this is where you can buy a ticket
                    case "2":
                        shoppingCart = currentBuyer.retrieveShoppingCart();
                        if (shoppingCart == null) {
                            System.out.println("Cannot view shopping cart because it is empty!");

                        } else {
                            for (int i = 0; i < shoppingCart.size(); i++) {
                                Ticket ticket = shoppingCart.get(i).getTicket();
                                int qty = shoppingCart.get(i).getQTY();

                                System.out.println(i + 1 + ":");
                                System.out.println("Sport: " + ticket.getSportType());
                                System.out.println("Location: " + ticket.getLocation());
                                System.out.println("Section: " + ticket.getSection());
                                System.out.println("Price: " + String.format("%.2f", ticket.getPrice()));
                                System.out.println("Quantity: " + qty);
                            }
                        }
                        break;
                    case "3":
                        ArrayList<CartItems> shoppingCart2 = currentBuyer.retrieveShoppingCart();
                        if (shoppingCart2 == null) {
                            System.out.println("Can't remove from shopping cart because it is empty!");
                        } else {
                            boolean invalidItem = false;
                            System.out.println("Pick an item to remove:");
                            int item = 0;
                            for (int i = 0; i < shoppingCart2.size(); i++) {
                                Ticket ticket = shoppingCart2.get(i).getTicket();
                                int qty = shoppingCart2.get(i).getQTY();

                                System.out.println(i + 1 + ":");
                                System.out.println("Sport: " + ticket.getSportType());
                                System.out.println("Location: " + ticket.getLocation());
                                System.out.println("Section: " + ticket.getSection());
                                System.out.println("Price: " + String.format("%.2f", ticket.getPrice()));
                                System.out.println("Quantity: " + qty);


                            }

                            do {
                                try {
                                    item = Integer.parseInt(scan.nextLine());
                                    if (item < 1 || item > shoppingCart2.size()) {
                                        System.out.println("Please enter a valid number");
                                        invalidItem = true;
                                    } else {
                                        invalidItem = false;
                                    }
                                } catch (Exception e) {
                                    System.out.println("Please enter a valid integer");
                                    invalidItem = true;
                                }

                            } while (invalidItem);

                            StoreNameIDCombo combo =
                                    Buyers.getSellerID(shoppingCart2.get(item - 1).getTicket().toString());
                            Buyers.updateTicketQuantity("SellerInfo.txt", combo.getSellerID(),
                                    shoppingCart2.get(item - 1).getTicket().toString(),
                                    -1 * shoppingCart2.get(item - 1).getQTY());
                            shoppingCart2.remove(item - 1);
                            currentBuyer.setShoppingCart(shoppingCart2);
                            try {
                                currentBuyer.updateShoppingCart();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    case "4":
                        currentBuyer.checkoutCart();
                        break;
                    case "5":
                        previousCart = currentBuyer.retrieveShoppingCart2();
                        if (previousCart == null) {
                            System.out.println("Cannot view previous cart because it is empty!");

                        } else {
                            for (int i = 0; i < previousCart.size(); i++) {
                                Ticket ticket = previousCart.get(i).getTicket();
                                int qty = previousCart.get(i).getQTY();

                                System.out.println(i + 1 + ":");
                                System.out.println("Sport: " + ticket.getSportType());
                                System.out.println("Location: " + ticket.getLocation());
                                System.out.println("Section: " + ticket.getSection());
                                System.out.println("Price: " + String.format("%.2f", ticket.getPrice()));
                                System.out.println("Quantity: " + qty);
                            }
                        }

                        break;
                    case "6":
                        boolean shouldRepeat = true;
                        while (shouldRepeat) {
                            System.out.println("What would you like to change?\n" +
                                    "1. Email\n" +
                                    "2. Password\n" +
                                    "3. Nothing. Go back to menu");
                            String anotherChoice = scan.nextLine();
                            switch (anotherChoice) {
                                case "1":
                                    System.out.println("What would you like to change your email to?");
                                    String newEmail = scan.nextLine();
                                    if (newEmail.contains("@")) {
                                        //editEmail(email, newEmail, password);
                                        editEmail2(email, newEmail, password);
                                        shouldRepeat = false;
                                        break;
                                    } else {
                                        System.out.println("Error!  Invalid email!");
                                        break;
                                    }
                                case "2":
                                    boolean invalidPassword = false;
                                    System.out.println("What would you like to change your password to?");
                                    System.out.println("Passwords must be at least 8 characters and at least 1 letter, number and special" +
                                            " character");
                                    String newPassword;

                                    do {
                                        newPassword = scan.nextLine();
                                        if (newPassword.length() < 8) {
                                            invalidPassword = true;
                                            System.out.println("Invalid password. Must be 8 or more characters.");
                                            System.out.println("Please enter the password again: ");
                                        } else {
                                            invalidPassword = false;
                                            if (!isStrongAlphanumeric(newPassword)) {
                                                System.out.println("Invalid password, must have letters, numbers and special characters.");
                                                System.out.println("Please enter the password again: ");
                                                invalidPassword = true;
                                            }
                                        }
                                    } while (invalidPassword);

                                    editPassword(email, password, newPassword);
                                    shouldRepeat = false;
                                    break;
                                case "3":
                                    System.out.println("Returning back to the main menu.");
                                    break;
                                default:
                                    System.out.println("Error.  Please enter a valid choice.");
                                    break;
                            }
                            break;
                        }
                        break;
                    case "7":
                        System.out.println("Are you sure you want to delete your account?");
                        String response = scan.nextLine();
                        if (response.toLowerCase().equals("yes")) {
                            deleteAccount(email, password);
                        } else {
                            break;
                        }
                        System.out.println("We are sad to see you go. Goodbye!");
                        return;
                    case "8":
                        System.out.println("Thank you for using Tickets@Purdue! Goodbye!");
                        return;
                    default:
                        System.out.println("Please enter a valid choice.");
                        break;
                }

                if (shouldSkipLine) {
                    scan.nextLine();
                }

            }

        } else {
            while (true) {
                sellerDashboard();
                Sellers currentSeller = new Sellers(new LoginInfo(email, password), id);
                String choice = scan.nextLine();
                switch (choice) {
                    case "1":
                        ArrayList<String> stores = currentSeller.retrieveStores();
                        if (stores == null) {
                            System.out.println("You have no stores currently. Would you like to add a store?");
                            System.out.println("1. Yes\n" +
                                    "2. No");
                            String anotherChoice = scan.nextLine();
                            switch (anotherChoice) {
                                case "1":
                                    String storeName;
                                    boolean invalidName = false;
                                    System.out.println("Enter the name of your store");
                                    do {
                                        storeName = scan.nextLine();
                                        if (storeName.isEmpty() || storeName.equals("\n")) {
                                            System.out.println("Invalid store name. Please re-enter");
                                            invalidName = true;
                                        } else {
                                            invalidName = false;
                                        }
                                    } while (invalidName);

                                    currentSeller.addStore(storeName);
                                    break;
                                case "2":
                                    break;
                                default:
                                    System.out.println("Invalid choice!");
                                    break;
                            }
                        }

                        stores = currentSeller.retrieveStores();

                        if (stores != null) {
                            System.out.println("What would you like to do?");
                            System.out.println("1. Add store\n" +
                                    "2. Add ticket\n" +
                                    "3. Neither");
                            String anotherChoice = scan.nextLine();
                            switch (anotherChoice) {
                                case "1":
                                    String storeName;
                                    boolean invalidName = false;
                                    System.out.println("Enter the name of your store");
                                    do {
                                        storeName = scan.nextLine();
                                        if (storeName.isEmpty() || storeName.equals("\n")) {
                                            System.out.println("Invalid store name. Please re-enter");
                                            invalidName = true;
                                        } else {
                                            invalidName = false;
                                        }
                                    } while (invalidName);

                                    currentSeller.addStore(storeName);
                                    break;
                                case "2":
                                    boolean invalidStore = false;
                                    System.out.println("Which store would you like to add your ticket to?");
                                    for (int i = 0; i < stores.size(); i++) {
                                        System.out.println(i + 1 + ". " + stores.get(i));
                                    }

                                    int store = 0;
                                    do {

                                        try {
                                            store = Integer.parseInt(scan.nextLine());
                                            if (store < 1 || store > stores.size()) {
                                                System.out.println("Please enter a number listed above");
                                                invalidStore = true;
                                            } else {
                                                invalidStore = false;
                                            }
                                        } catch (NumberFormatException e) {
                                            invalidStore = true;
                                            System.out.println("Please enter a valid integer");
                                        }
                                    } while (invalidStore);

                                    int qty = 0;
                                    boolean invalidString = false;
                                    boolean invalidPrice = false;
                                    boolean invalidQty = false;
                                    String sport;
                                    String location;
                                    String section;
                                    double price = 0;
                                    System.out.println("Please enter the sport:");
                                    do {
                                        sport = scan.nextLine();
                                        if (sport.isEmpty() || sport.equals("\n")) {
                                            invalidString = true;
                                            System.out.println("Please enter a valid sport:");
                                        } else {
                                            invalidString = false;
                                        }
                                    } while (invalidString);

                                    System.out.println("Please enter the location:");
                                    do {
                                        location = scan.nextLine();
                                        if (location.isEmpty() || location.equals("\n")) {
                                            invalidString = true;
                                            System.out.println("Please enter a valid location:");
                                        } else {
                                            invalidString = false;
                                        }
                                    } while (invalidString);

                                    System.out.println("Please enter the section:");
                                    do {
                                        section = scan.nextLine();
                                        if (section.isEmpty() || section.equals("\n")) {
                                            invalidString = true;
                                            System.out.println("Please enter a valid section:");
                                        } else {
                                            invalidString = false;
                                        }
                                    } while (invalidString);

                                    System.out.println("Please enter the price:");
                                    do {
                                        try {
                                            price = scan.nextDouble();
                                            if (price <= 0) {
                                                System.out.println("Please set a valid price");
                                                invalidPrice = true;
                                            } else {
                                                invalidPrice = false;
                                            }
                                        } catch (Exception e) {
                                            System.out.println("Please enter a valid price");
                                            invalidPrice = true;
                                        }
                                    } while (invalidPrice);

                                    System.out.println("How many tickets would you like to sell?");
                                    do {
                                        try {
                                            qty = scan.nextInt();
                                            if (qty <= 0) {
                                                System.out.println("Please enter a valid quantity");
                                                invalidQty = true;
                                            } else {
                                                invalidQty = false;
                                            }
                                        } catch (Exception e) {
                                            System.out.println("Please enter a valid integer");
                                            invalidQty = true;
                                        }
                                    } while (invalidQty);

                                    currentSeller.addTickets(new Ticket(sport, location, section, price), qty,
                                            stores.get(store - 1));
                                    scan.nextLine();
                            }
                        }
                        break;
                    case "2":
                        ArrayList<String> stores2 = currentSeller.retrieveStores();
                        if (stores2 == null) {
                            System.out.println("There are no stores, first create stores and add tickets before " +
                                    "editing.");
                        } else {
                            System.out.println("Which store would you like to edit a ticket in?");
                            for (int i = 0; i < stores2.size(); i++) {
                                System.out.println(i + 1 + ". " + stores2.get(i));
                            }

                            int store = 0;
                            int listingNum = 0;
                            boolean invalidStore = false;
                            boolean invalidChoice = false;
                            do {
                                try {
                                    store = Integer.parseInt(scan.nextLine());
                                    if (store < 1 || store > stores2.size()) {
                                        System.out.println("Please enter a number listed above");
                                        invalidStore = true;
                                    } else {
                                        invalidStore = false;
                                    }
                                } catch (NumberFormatException e) {
                                    invalidStore = true;
                                    System.out.println("Please enter a valid integer");
                                }
                            } while (invalidStore);

                            ArrayList<String> listings = currentSeller.retrieveListings(stores2.get(store - 1));
                            SellerListing listing = currentSeller.retrieveProducts(stores2.get(store - 1));
                            if (!(listings == null || listing == null)) {
                                System.out.println("Enter a number corresponding to the ticket you would like to edit");
                                for (int i = 0; i < listings.size(); i++) {
                                    System.out.println(listings.get(i));
                                }
                                do {
                                    try {
                                        listingNum = Integer.parseInt(scan.nextLine());
                                        if (listingNum < 1 || listingNum > listings.size()) {
                                            System.out.println("Please enter a number listed above");
                                            invalidChoice = true;
                                        } else {
                                            invalidChoice = false;
                                        }
                                    } catch (NumberFormatException e) {
                                        invalidChoice = true;
                                        System.out.println("Please enter a valid integer");
                                    }
                                } while (invalidChoice);

                                System.out.println("What would you like to change?");
                                System.out.println("1. Ticket info\n" +
                                        "2. Quantity\n" +
                                        "3. Both");

                                int changeChoice = 0;
                                do {
                                    try {
                                        changeChoice = Integer.parseInt(scan.nextLine());
                                        if (changeChoice < 1 || changeChoice > 3) {
                                            System.out.println("Please enter a number listed above");
                                            invalidChoice = true;
                                        } else {
                                            invalidChoice = false;
                                        }
                                    } catch (NumberFormatException e) {
                                        invalidChoice = true;
                                        System.out.println("Please enter a valid integer");
                                    }
                                } while (invalidChoice);

                                switch (changeChoice) {
                                    case 1:
                                        boolean invalidString = false;
                                        boolean invalidPrice = false;
                                        String sport;
                                        String location;
                                        String section;
                                        double price = 0;
                                        System.out.println("Please enter the sport:");
                                        do {
                                            sport = scan.nextLine();
                                            if (sport.isEmpty() || sport.equals("\n")) {
                                                invalidString = true;
                                                System.out.println("Please enter a valid sport:");
                                            } else {
                                                invalidString = false;
                                            }
                                        } while (invalidString);

                                        System.out.println("Please enter the location:");
                                        do {
                                            location = scan.nextLine();
                                            if (location.isEmpty() || location.equals("\n")) {
                                                invalidString = true;
                                                System.out.println("Please enter a valid location:");
                                            } else {
                                                invalidString = false;
                                            }
                                        } while (invalidString);

                                        System.out.println("Please enter the section:");
                                        do {
                                            section = scan.nextLine();
                                            if (section.isEmpty() || section.equals("\n")) {
                                                invalidString = true;
                                                System.out.println("Please enter a valid section:");
                                            } else {
                                                invalidString = false;
                                            }
                                        } while (invalidString);

                                        System.out.println("Please enter the price:");
                                        do {
                                            try {
                                                price = Double.parseDouble(scan.nextLine());
                                                if (price <= 0) {
                                                    System.out.println("Please set a valid price");
                                                    invalidPrice = true;
                                                } else {
                                                    invalidPrice = false;
                                                }
                                            } catch (Exception e) {
                                                System.out.println("Please enter a valid price");
                                                invalidPrice = true;
                                            }
                                        } while (invalidPrice);

                                        currentSeller.modTicket(listing.getTickets().get(listingNum - 1), new Ticket(sport,
                                                        location, section, price), stores2.get(store - 1),
                                                Integer.parseInt(listing.getQuantities().get(listingNum - 1)));
                                        break;
                                    case 2:
                                        int qty = 0;
                                        boolean invalidQty = false;
                                        System.out.println("How many tickets would you like to sell?");
                                        do {
                                            try {
                                                qty = Integer.parseInt(scan.nextLine());
                                                if (qty <= 0) {
                                                    System.out.println("Please enter a valid quantity");
                                                    invalidQty = true;
                                                } else {
                                                    invalidQty = false;
                                                }
                                            } catch (Exception e) {
                                                System.out.println("Please enter a valid integer");
                                                invalidQty = true;
                                            }
                                        } while (invalidQty);

                                        currentSeller.modTicket(listing.getTickets().get(listingNum - 1),
                                                listing.getTickets().get(listingNum - 1),
                                                stores2.get(store - 1), qty);

                                        break;
                                    case 3:
                                        qty = 0;
                                        invalidQty = false;
                                        invalidString = false;
                                        invalidPrice = false;
                                        price = 0;
                                        System.out.println("Please enter the sport:");
                                        do {
                                            sport = scan.nextLine();
                                            if (sport.isEmpty() || sport.equals("\n")) {
                                                invalidString = true;
                                                System.out.println("Please enter a valid sport:");
                                            } else {
                                                invalidString = false;
                                            }
                                        } while (invalidString);

                                        System.out.println("Please enter the location:");
                                        do {
                                            location = scan.nextLine();
                                            if (location.isEmpty() || location.equals("\n")) {
                                                invalidString = true;
                                                System.out.println("Please enter a valid location:");
                                            } else {
                                                invalidString = false;
                                            }
                                        } while (invalidString);

                                        System.out.println("Please enter the section:");
                                        do {
                                            section = scan.nextLine();
                                            if (section.isEmpty() || section.equals("\n")) {
                                                invalidString = true;
                                                System.out.println("Please enter a valid section:");
                                            } else {
                                                invalidString = false;
                                            }
                                        } while (invalidString);

                                        System.out.println("Please enter the price:");
                                        do {
                                            try {
                                                price = Double.parseDouble(scan.nextLine());
                                                if (price <= 0) {
                                                    System.out.println("Please set a valid price");
                                                    invalidPrice = true;
                                                } else {
                                                    invalidPrice = false;
                                                }
                                            } catch (Exception e) {
                                                System.out.println("Please enter a valid price");
                                                invalidPrice = true;
                                            }
                                        } while (invalidPrice);

                                        System.out.println("How many tickets would you like to sell?");
                                        do {
                                            try {
                                                qty = Integer.parseInt(scan.nextLine());
                                                if (qty <= 0) {
                                                    System.out.println("Please enter a valid quantity");
                                                    invalidQty = true;
                                                } else {
                                                    invalidQty = false;
                                                }
                                            } catch (Exception e) {
                                                System.out.println("Please enter a valid integer");
                                                invalidQty = true;
                                            }
                                        } while (invalidQty);

                                        currentSeller.modTicket(listing.getTickets().get(listingNum - 1), new Ticket(sport,
                                                location, section, price), stores2.get(store - 1), qty);

                                        break;
                                    default:
                                        System.out.println("Invalid choice");
                                        break;
                                }
                            } else {
                                System.out.println("Store you have selected is empty!");
                            }
                        }

                        break;
                    case "3":
                        ArrayList<String> stores3 = currentSeller.retrieveStores();
                        if (stores3 == null) {
                            System.out.println("There are no stores, first create stores and add tickets before " +
                                    "removing.");
                        } else {
                            System.out.println("Which store would you like to remove a ticket in?");
                            for (int i = 0; i < stores3.size(); i++) {
                                System.out.println(i + 1 + ". " + stores3.get(i));
                            }

                            int store = 0;
                            int listingNum = 0;
                            boolean invalidStore = false;
                            boolean invalidChoice = false;
                            do {
                                try {
                                    store = Integer.parseInt(scan.nextLine());
                                    if (store < 1 || store > stores3.size()) {
                                        System.out.println("Please enter a number listed above");
                                        invalidStore = true;
                                    } else {
                                        invalidStore = false;
                                    }
                                } catch (NumberFormatException e) {
                                    invalidStore = true;
                                    System.out.println("Please enter a valid integer");
                                }
                            } while (invalidStore);

                            ArrayList<String> listings = currentSeller.retrieveListings(stores3.get(store - 1));
                            SellerListing listing = currentSeller.retrieveProducts(stores3.get(store - 1));

                            System.out.println("Enter a number corresponding to the ticket you would like to edit");
                            for (int i = 0; i < listings.size(); i++) {
                                System.out.println(listings.get(i));
                            }

                            do {
                                try {
                                    listingNum = Integer.parseInt(scan.nextLine());
                                    if (listingNum < 1 || listingNum > listings.size()) {
                                        System.out.println("Please enter a number listed above");
                                        invalidChoice = true;
                                    } else {
                                        invalidChoice = false;
                                    }
                                } catch (NumberFormatException e) {
                                    invalidChoice = true;
                                    System.out.println("Please enter a valid integer");
                                }
                            } while (invalidChoice);

                            currentSeller.deleteTicket(listing.getTickets().get(listingNum - 1), stores3.get(store - 1));

                        }
                        break;

                    case "4":
                        ArrayList<String> allTransactions = Marketplace.readFile("TransactionInfo.txt");
                        System.out.println("Viewing sales by store");
                        stores = currentSeller.retrieveStores();


                        if (stores == null) {
                            System.out.println("There are no stores, so you cannot view sales by store");
                        } else {
                            ArrayList<String> sales = new ArrayList<>();
                            int store = 0;
                            boolean invalidStore = false;
                            double totalRevenue = 0;


                            System.out.println("Which store would you like to view the sales for?");
                            for (int i = 0; i < stores.size(); i++) {
                                System.out.println(i + 1 + ". " + stores.get(i));
                            }

                            do {
                                try {
                                    store = Integer.parseInt(scan.nextLine());
                                    if (store < 1 || store > stores.size()) {
                                        System.out.println("Please enter a number listed above");
                                        invalidStore = true;
                                    } else {
                                        invalidStore = false;
                                    }
                                } catch (NumberFormatException e) {
                                    invalidStore = true;
                                    System.out.println("Please enter a valid integer");
                                }
                            } while (invalidStore);

                            for (int i = 0; i < allTransactions.size(); i++) {
                                String[] transactionInfo = allTransactions.get(i).split(",");
                                if (transactionInfo[2].equals(String.valueOf(currentSeller.getSellerID()))) {
                                    if (transactionInfo[3].equals(stores.get(store - 1))) {
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

                            if (sales.isEmpty()) {
                                System.out.println("No sales found for the store");
                            } else {
                                for (int i = 0; i < sales.size(); i++) {
                                    System.out.println(i + 1 + ".");
                                    System.out.println(sales.get(i));
                                    System.out.println();
                                }
                                System.out.println("Total Revenue from store: " + String.format("%.2f", totalRevenue));
                            }
                        }
                        break;
                    case "5":
                        ArrayList<String> allTransactions2 = Marketplace.readFile("TransactionInfo.txt");
                        System.out.println("Viewing seller-side statistics");
                        stores = currentSeller.retrieveStores();

                        if (stores == null) {
                            System.out.println("There are no stores, so you cannot view statistics by store");
                        } else {
                            ArrayList<String> sales = new ArrayList<>();
                            int store = 0;
                            boolean invalidStore = false;
                            double totalRevenue = 0;


                            System.out.println("Which store would you like to view the sales for?");
                            for (int i = 0; i < stores.size(); i++) {
                                System.out.println(i + 1 + ". " + stores.get(i));
                            }

                            do {
                                try {
                                    store = Integer.parseInt(scan.nextLine());
                                    if (store < 1 || store > stores.size()) {
                                        System.out.println("Please enter a number listed above");
                                        invalidStore = true;
                                    } else {
                                        invalidStore = false;
                                    }
                                } catch (NumberFormatException e) {
                                    invalidStore = true;
                                    System.out.println("Please enter a valid integer");
                                }
                            } while (invalidStore);

                            int statChoice = 0;
                            boolean invalidStatChoice = false;
                            System.out.println("How would you like to view the statistics?");
                            System.out.println("1. List of customers and items purchased");
                            System.out.println("2. List of products and number of sales");

                            do {
                                try {
                                    statChoice = Integer.parseInt(scan.nextLine());
                                    if (statChoice < 1 || statChoice > 2) {
                                        System.out.println("Please enter a number listed above");
                                        invalidStatChoice = true;
                                    } else {
                                        invalidStatChoice = false;
                                    }
                                } catch (NumberFormatException e) {
                                    invalidStatChoice = true;
                                    System.out.println("Please enter a valid integer");
                                }
                            } while (invalidStatChoice);

                            if(statChoice == 1) {

                            } else {

                            }

                        }
                        break;
                    case "6":
                        boolean shouldRepeat = true;
                        while (shouldRepeat) {

                            System.out.println("What would you like to change?\n" +
                                    "1. Email\n" +
                                    "2. Password\n" +
                                    "3. Nothing. Go back to menu");
                            String anotherChoice = scan.nextLine();
                            switch (anotherChoice) {
                                case "1":
                                    System.out.println("What would you like to change your email to?");
                                    String newEmail = scan.nextLine();
                                    if (newEmail.contains("@")) {
                                        editEmail2(email, newEmail, password);
                                        shouldRepeat = false;
                                        break;
                                    } else {
                                        System.out.println("Error!  Invalid email!");
                                        break;
                                    }
                                case "2":
                                    boolean invalidPassword = false;
                                    System.out.println("What would you like to change your password to?");
                                    System.out.println("Passwords must be at least 8 characters and at least 1 letter, number and special" +
                                            " character");
                                    String newPassword;
                                    String password2;

                                    do {
                                        newPassword = scan.nextLine();
                                        if (newPassword.length() < 8) {
                                            invalidPassword = true;
                                            System.out.println("Invalid password. Must be 8 or more characters.");
                                            System.out.println("Please enter the password again: ");
                                        } else {
                                            invalidPassword = false;
                                            if (!isStrongAlphanumeric(newPassword)) {
                                                System.out.println("Invalid password, must have letters, numbers and special characters.");
                                                System.out.println("Please enter the password again: ");
                                                invalidPassword = true;
                                            }
                                        }
                                    } while (invalidPassword);

                                    editPassword(email, password, newPassword);
                                    shouldRepeat = false;
                                    break;
                                case "3":
                                    System.out.println("Returning back to the main menu.");
                                    break;
                                default:
                                    System.out.println("Error.  Please enter a valid choice.");
                                    break;
                            }
                            break;
                        }
                        break;
                    case "7":
                        System.out.println("Are you sure you want to delete your account?");
                        String response = scan.nextLine();
                        if (response.toLowerCase().equals("yes")) {
                            deleteAccount(email, password);
                        } else {
                            break;
                        }
                        System.out.println("We are sad to see you go. Goodbye!");
                        return;
                    case "8":
                        System.out.println("Thank you for using Tickets@Purdue! Goodbye!");
                        return;
                    default:
                        System.out.println("Please enter a valid choice.");
                        break;
                }
            }
        }
    }


    public static ArrayList<String> readFile(String fileName) {
        ArrayList<String> list = new ArrayList<>();
        try (BufferedReader bfr = new BufferedReader(new FileReader(fileName))) {
            String line = bfr.readLine();
            while (line != null) {
                list.add(line);
                line = bfr.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void writeFile(ArrayList<String> lines, String filename) {
        try (PrintWriter pw = new PrintWriter(new FileOutputStream(filename, false))) {
            for (int i = 0; i < lines.size(); i++) {
                pw.println(lines.get(i));
            }
            pw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isStrongAlphanumeric(String password) {
        int letterCount = 0;
        int numCount = 0;
        int specialCount = 0;
        for (int i = 0; i < password.length(); i++) {
            char currentLetter = password.charAt(i);
            if (Character.isLetter(currentLetter)) {
                letterCount++;
            } else if (Character.isDigit(currentLetter)) {
                numCount++;
            } else {
                specialCount++;
            }
        }
        if (letterCount == 0 || numCount == 0 || specialCount == 0) {
            return false;
        }
        return true;
    }

    public static String determineRole(String email, String password) {
        ArrayList<String> arrayList = readFile("LoginInfo.txt");
        String role = "";
        for (int i = 0; i < arrayList.size(); i++) {
            String[] userInfo = arrayList.get(i).split(",");
            if (email.equals(userInfo[1]) && password.equals(userInfo[2])) {
                role = userInfo[3];
            }
        }
        return role;
    }

    public static void buyerDashboard() {
        System.out.println("Welcome!  What would you like to do?\n" +
                "1. Buy a ticket\n" +
                "2. View shopping cart\n" +
                "3. Remove from shopping cart" + "\n" +
                "4. Checkout cart\n" +
                "5. View History (Statistics)\n" +
                "6. Edit my account\n" +
                "7. Delete my account\n" +
                "8. Logout");
    }

    public static void sellerDashboard() {
        System.out.println("Welcome!  What would you like to do?\n" +
                "1. Sell a ticket\n" +
                "2. Edit a ticket\n" +
                "3. Remove a ticket\n" +
                "4. View History\n" +
                "5. View Statistics\n" +
                "6. Edit my account\n" +
                "7. Delete my account\n" +
                "8. Logout");
    }


    public static void deleteAccount(String email, String password) {
        ArrayList<String> loginInfo = readFile("LoginInfo.txt");
        String role = "";
        int id = 0;
        int indexToChange = 0;
        for (int i = 0; i < loginInfo.size(); i++) {
            String[] line = loginInfo.get(i).split(",");
            id = Integer.parseInt(line[0]);
            String check1 = line[1];
            String check2 = line[2];
            role = line[3];
            if (check1.equals(email) && check2.equals(password)) {
                indexToChange = i;
                break;
            }
        }

        ArrayList<String> newLoginInfo = new ArrayList<>();


        for (int i = 0; i < loginInfo.size(); i++) {
            if (i != indexToChange) {
                newLoginInfo.add(loginInfo.get(i));
            }
        }
        writeFile(newLoginInfo, "LoginInfo.txt");

        if (role.equals("b")) {
            int curIndextoChange = -1;
            ArrayList<String> buyerInfo = readFile("BuyerInfo.txt");
            for (int j = 0; j < buyerInfo.size(); j++) {
                String[] newLine = buyerInfo.get(j).split(",");
                if (id == Integer.parseInt(newLine[0])) {
                    curIndextoChange = j;
                    break;
                }
            }


            ArrayList<String> buyerInfo2 = new ArrayList<>();
            for (int j = 0; j < buyerInfo.size(); j++) {
                if (j != curIndextoChange) {
                    buyerInfo2.add(buyerInfo.get(j));
                }
            }
            writeFile(buyerInfo2, "BuyerInfo.txt");
        } else if (role.equals("s")) {
            int curIndextoChange = -1;
            ArrayList<String> buyerInfo = readFile("SellerInfo.txt");
            for (int j = 0; j < buyerInfo.size(); j++) {
                String[] newLine = buyerInfo.get(j).split(",");
                if (id == Integer.parseInt(newLine[0])) {
                    curIndextoChange = j;
                    break;
                }
            }


            ArrayList<String> buyerInfo2 = new ArrayList<>();
            for (int j = 0; j < buyerInfo.size(); j++) {
                if (j != curIndextoChange) {
                    buyerInfo2.add(buyerInfo.get(j));
                }
            }
            writeFile(buyerInfo2, "SellerInfo.txt");
        }
    }


    public static void editEmail(String oldEmail, String newEmail, String password) {
        ArrayList<String> loginInfo = readFile("LoginInfo.txt");
        int indexToChange = 0;
        String id = "";
        String role = "";
        for (int i = 0; i < loginInfo.size(); i++) {
            String[] line = loginInfo.get(i).split(",");
            id = line[0];
            String check1 = line[1];
            String check2 = line[2];
            role = line[3];
            if (check1.equals(oldEmail) && check2.equals(password)) {
                indexToChange = i;
                break;
            }
        }
        loginInfo.set(indexToChange, id + "," + newEmail + "," + password + "," + role);
        if (role.equals("b")) {
            ArrayList<String> buyerInfo = readFile("BuyerInfo.txt");
            for (int j = 0; j < buyerInfo.size(); j++) {
                String newLine = "";
                String[] anotherLine = buyerInfo.get(j).split(",");
                String check3 = anotherLine[0];
                String check4 = anotherLine[1];
                if (check3.equals(id) && check4.equals(oldEmail)) {
                    anotherLine[1] = newEmail;
                    for (int k = 0; k < anotherLine.length; k++) {
                        if (k == anotherLine.length - 1) {
                            newLine += anotherLine[k];
                        } else {
                            newLine += anotherLine[k] + ",";
                        }
                    }
                    buyerInfo.set(j, newLine);
                    break;
                }
            }
        } else if (role.equals("s")) {
            ArrayList<String> sellerInfo = readFile("SellerInfo.txt");
            for (int j = 0; j < sellerInfo.size(); j++) {
                String newLine = "";
                String[] anotherLine = sellerInfo.get(j).split(",");
                String check5 = anotherLine[0];
                String check6 = anotherLine[1];
                if (check5.equals(id) && check6.equals(oldEmail)) {
                    anotherLine[1] = newEmail;
                    for (int k = 0; k < anotherLine.length; k++) {
                        if (k == anotherLine.length - 1) {
                            newLine += anotherLine[k];
                        } else {
                            newLine += anotherLine[k] + ",";
                        }
                    }
                    sellerInfo.set(j, newLine);
                    writeFile(sellerInfo, "SellerInfo.txt");
                    break;
                }
            }
        }
        writeFile(loginInfo, "LoginInfo.txt");
    }

    public static void editEmail2(String oldEmail, String newEmail, String password) {
        ArrayList<String> loginInfo = readFile("LoginInfo.txt");
        int indexToChange = 0;
        String id = "";
        String role = "";
        for (int i = 0; i < loginInfo.size(); i++) {
            String[] line = loginInfo.get(i).split(",");
            id = line[0];
            String check1 = line[1];
            String check2 = line[2];
            role = line[3];
            if (check1.equals(oldEmail) && check2.equals(password)) {
                indexToChange = i;
                break;
            }
        }
        loginInfo.set(indexToChange, id + "," + newEmail + "," + password + "," + role);
        if (role.equals("b")) {
            ArrayList<String> buyerInfo = readFile("BuyerHistory.txt");
            for (int j = 0; j < buyerInfo.size(); j++) {
                String newLine = "";
                String[] anotherLine = buyerInfo.get(j).split(",");
                String check3 = anotherLine[0];
                String check4 = anotherLine[1];
                if (check3.equals(id) && check4.equals(oldEmail)) {
                    anotherLine[1] = newEmail;
                    for (int k = 0; k < anotherLine.length; k++) {
                        if (k == anotherLine.length - 1) {
                            newLine += anotherLine[k];
                        } else {
                            newLine += anotherLine[k] + ",";
                        }
                    }
                    buyerInfo.set(j, newLine);
                    writeFile(buyerInfo, "BuyerHistory.txt");
                    break;
                }
            }

            ArrayList<String> buyerInfo2 = readFile("BuyerInfo.txt");
            for (int j = 0; j < buyerInfo2.size(); j++) {
                String newLine = "";
                String[] anotherLine = buyerInfo2.get(j).split(",");
                String check3 = anotherLine[0];
                String check4 = anotherLine[1];
                if (check3.equals(id) && check4.equals(oldEmail)) {
                    anotherLine[1] = newEmail;
                    for (int k = 0; k < anotherLine.length; k++) {
                        if (k == anotherLine.length - 1) {
                            newLine += anotherLine[k];
                        } else {
                            newLine += anotherLine[k] + ",";
                        }
                    }
                    buyerInfo2.set(j, newLine);
                    writeFile(buyerInfo, "BuyerInfo.txt");
                    break;
                }
            }

        } else if (role.equals("s")) {
            ArrayList<String> sellerInfo = readFile("SellerInfo.txt");
            for (int j = 0; j < sellerInfo.size(); j++) {
                String newLine = "";
                String[] anotherLine = sellerInfo.get(j).split(",");
                String check5 = anotherLine[0];
                String check6 = anotherLine[1];
                if (check5.equals(id) && check6.equals(oldEmail)) {
                    anotherLine[1] = newEmail;
                    for (int k = 0; k < anotherLine.length; k++) {
                        if (k == anotherLine.length - 1) {
                            newLine += anotherLine[k];
                        } else {
                            newLine += anotherLine[k] + ",";
                        }
                    }
                    sellerInfo.set(j, newLine);
                    writeFile(sellerInfo, "SellerInfo.txt");
                    break;
                }
            }
        }
        writeFile(loginInfo, "LoginInfo.txt");
    }


    public static void editPassword(String email, String oldPassword, String newPassword) {
        ArrayList<String> loginInfo = readFile("LoginInfo.txt");
        int indexToChange = 0;
        String id = "";
        String role = "";
        for (int i = 0; i < loginInfo.size(); i++) {
            String[] line = loginInfo.get(i).split(",");
            id = line[0];
            String check1 = line[1];
            String check2 = line[2];
            role = line[3];
            if (check1.equals(email) && check2.equals(oldPassword)) {
                indexToChange = i;
                break;
            }
        }
        loginInfo.set(indexToChange, id + "," + email + "," + newPassword + "," + role);
        writeFile(loginInfo, "LoginInfo.txt");
    }
}
