import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Project 5: BuyTicketGUI
 * 
 * Utilizes GUI to allow a buyer to buy tickets from a seller.
 *
 * @author Henry J. Lee, Lab Section L20
 * @version November 13, 2023
 */

public class BuyTicketGUI extends JComponent implements Runnable {
    JFrame frame;
    JLabel messageLabel;
    JComboBox<String> list;
    JButton confirmButton;
    JButton returnToMenuButton;
    LoginInfo loginInfo;
    String[] optionChoices = {"1. View All Listings", "2. View Listings By Store", "3. View Listings By Sport",
            "4. View Listings By Quantity Available"};
    ArrayList<CartItems> shoppingCart;
    ArrayList<CartItems> previousShoppingCart;
    Buyers currentBuyer;
    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == confirmButton) {
                int option = list.getSelectedIndex();
                shoppingCart = currentBuyer.retrieveShoppingCart();
                switch (option) {
                    case 0 -> {
                        TicketInfoCombo b = Buyers.viewAllListingsGeneral();
                        if (!b.getListedTicks().isEmpty()) {
                            String[] options = new String[viewAllListingsGeneral().size()];
                            for (int i = 0; i < options.length; i++) {
                                options[i] = viewAllListingsGeneral().get(i);
                            }
                            String ticket = (String) JOptionPane.showInputDialog(frame,
                                    "What type of ticket would you like to buy?", "View All Listings",
                                    JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
                            if (ticket == null) {
                                for (String optionChoice : optionChoices) {
                                    list.addItem(optionChoice);
                                }
                                list.setSelectedIndex(0);
                                return;
                            }
                            int whatToBuy = Arrays.asList(options).indexOf(ticket);

                            String num = JOptionPane.showInputDialog(frame, "How many would " +
                                    "you like to buy?", "View All Listings", JOptionPane.QUESTION_MESSAGE);
                            if (num == null) {
                                for (String optionChoice : optionChoices) {
                                    list.addItem(optionChoice);
                                }
                                list.setSelectedIndex(0);
                                return;
                            }
                            int howMany = Integer.parseInt(num);
                            Ticket currentTicket = Buyers.buyTicket(b.getListedTicks(), whatToBuy, howMany,
                                    b.getHowManyTicks());
                            if (currentTicket != null) {
                                shoppingCart.add(new CartItems(currentTicket, howMany));
                                currentBuyer.setShoppingCart(shoppingCart);
                                try {
                                    currentBuyer.updateShoppingCart();
                                    JOptionPane.showMessageDialog(frame, "Ticket purchase successful!", "Success",
                                            JOptionPane.INFORMATION_MESSAGE);
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }
                    }
                    case 1 -> {
                        String curStore = JOptionPane.showInputDialog(frame, "What store would "
                                + "you like to buy from?", "View Listings By Store", JOptionPane.QUESTION_MESSAGE);
                        if (curStore == null) {
                            for (String optionChoice : optionChoices) {
                                list.addItem(optionChoice);
                            }
                            list.setSelectedIndex(0);
                            return;
                        }
                        String[] options = new String[viewListingsWithConstraint(curStore).size()];
                        for (int i = 0; i < options.length; i++) {
                            options[i] = viewListingsWithConstraint(curStore).get(i);
                        }
                        TicketInfoCombo c = Buyers.viewListingsWithConstraint(curStore);
                        if (!c.getListedTicks().isEmpty()) {
                            String ticket = (String) JOptionPane.showInputDialog(frame,
                                    "What type of ticket would you like to buy?", "View Listings By " +
                                            "Store", JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
                            if (ticket == null) {
                                for (String optionChoice : optionChoices) {
                                    list.addItem(optionChoice);
                                }
                                list.setSelectedIndex(0);
                                return;
                            }
                            int whatToBuy2 = Arrays.asList(options).indexOf(ticket);
                            String num = JOptionPane.showInputDialog(frame, "How many would " +
                                    "you like to buy?", "View Listings By Store", JOptionPane.QUESTION_MESSAGE);
                            if (num == null) {
                                for (String optionChoice : optionChoices) {
                                    list.addItem(optionChoice);
                                }
                                list.setSelectedIndex(0);
                                return;
                            }
                            int howMany2 = Integer.parseInt(num);
                            Ticket currentTicket = Buyers.buyTicket(c.getListedTicks(), whatToBuy2, howMany2,
                                    c.getHowManyTicks());
                            if (currentTicket != null) {
                                shoppingCart.add(new CartItems(currentTicket, howMany2));
                                currentBuyer.setShoppingCart(shoppingCart);
                                try {
                                    currentBuyer.updateShoppingCart();
                                    JOptionPane.showMessageDialog(frame, "Ticket purchase successful!", "Success",
                                            JOptionPane.INFORMATION_MESSAGE);
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }
                    }
                    case 2 -> {
                        String curSport = JOptionPane.showInputDialog(frame, "What sport " +
                                        "would you want to buy tickets for?", " View Listings By Sport",
                                JOptionPane.QUESTION_MESSAGE);
                        if (curSport == null) {
                            for (String optionChoice : optionChoices) {
                                list.addItem(optionChoice);
                            }
                            list.setSelectedIndex(0);
                            return;
                        }
                        String[] options1 = new String[viewListingsWithConstraint(curSport).size()];
                        for (int i = 0; i < options1.length; i++) {
                            options1[i] = viewListingsWithConstraint(curSport).get(i);
                        }
                        TicketInfoCombo d = Buyers.viewListingsWithConstraint(curSport);
                        if (!d.getListedTicks().isEmpty()) {
                            String ticket = (String) JOptionPane.showInputDialog(frame,
                                    "What type of ticket would you like to buy?", "View Listings By " +
                                            "Store", JOptionPane.PLAIN_MESSAGE, null, options1, options1[0]);
                            if (ticket == null) {
                                for (String optionChoice : optionChoices) {
                                    list.addItem(optionChoice);
                                }
                                list.setSelectedIndex(0);
                                return;
                            }
                            int whatToBuy3 = Arrays.asList(options1).indexOf(ticket);
                            String num = JOptionPane.showInputDialog(frame, "How many would " +
                                    "you like to buy?", "View Listings By Store", JOptionPane.QUESTION_MESSAGE);
                            if (num == null) {
                                for (String optionChoice : optionChoices) {
                                    list.addItem(optionChoice);
                                }
                                list.setSelectedIndex(0);
                                return;
                            }
                            int howMany3 = Integer.parseInt(num);
                            Ticket currentTicket = Buyers.buyTicket(d.getListedTicks(), whatToBuy3, howMany3,
                                    d.getHowManyTicks());
                            if (currentTicket != null) {
                                shoppingCart.add(new CartItems(currentTicket, howMany3));
                                currentBuyer.setShoppingCart(shoppingCart);
                                try {
                                    currentBuyer.updateShoppingCart();
                                    JOptionPane.showMessageDialog(frame, "Ticket purchase successful!", "Success",
                                            JOptionPane.INFORMATION_MESSAGE);
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }
                    }
                    case 3 -> {
                        String[] options2 = new String[viewAllListingsSortedByTicketQuantity().size()];
                        for (int i = 0; i < options2.length; i++) {
                            options2[i] = viewAllListingsSortedByTicketQuantity().get(i);
                        }
                        TicketInfoCombo ee = Buyers.viewAllListingsSortedByTicketQuantity();
                        if (!ee.getListedTicks().isEmpty()) {
                            String ticket = (String) JOptionPane.showInputDialog(frame,
                                    "What type of ticket would you like to buy?", "View Listings By " +
                                            "Quantity", JOptionPane.PLAIN_MESSAGE, null, options2, options2[0]);
                            if (ticket == null) {
                                for (String optionChoice : optionChoices) {
                                    list.addItem(optionChoice);
                                }
                                list.setSelectedIndex(0);
                                return;
                            }
                            int whatToBuy4 = Arrays.asList(options2).indexOf(ticket);
                            String num = JOptionPane.showInputDialog(frame, "How many would " +
                                    "you like to buy?", "View Listings By Quantity", JOptionPane.QUESTION_MESSAGE);
                            if (num == null) {
                                for (String optionChoice : optionChoices) {
                                    list.addItem(optionChoice);
                                }
                                list.setSelectedIndex(0);
                                return;
                            }
                            int howMany4 = Integer.parseInt(num);
                            Ticket currentTicket = Buyers.buyTicket2(ee.getListedTicks(), whatToBuy4, howMany4,
                                    ee.getHowManyTicks());
                            if (currentTicket != null) {
                                shoppingCart.add(new CartItems(currentTicket, howMany4));
                                currentBuyer.setShoppingCart(shoppingCart);
                                try {
                                    currentBuyer.updateShoppingCart();
                                    JOptionPane.showMessageDialog(frame, "Ticket purchase successful!", "Success",
                                            JOptionPane.INFORMATION_MESSAGE);
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }
                    }
                }
                for (String optionChoice : optionChoices) {
                    list.addItem(optionChoice);
                }
                list.setSelectedIndex(0);
            }
            if (e.getSource() == returnToMenuButton) {
                BuyTicketMenuGUI buyTicketMenuGUI = new BuyTicketMenuGUI();
                buyTicketMenuGUI.setLoginInfo(loginInfo);
                buyTicketMenuGUI.setShoppingCart(shoppingCart);
                buyTicketMenuGUI.setCurrentBuyer(currentBuyer);
                buyTicketMenuGUI.setPreviousShoppingCart(previousShoppingCart);
                buyTicketMenuGUI.run();
                frame.dispose();
            }
        }
    };

    public BuyTicketGUI() {

    }

    public void run() {
        frame = new JFrame("Buying Tickets");
        frame.setLayout(new GridBagLayout());

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.add(panel);

        GridBagConstraints gbc1 = new GridBagConstraints();
        gbc1.gridx = 0;
        gbc1.gridy = 0;
        gbc1.insets = new Insets(5, 10, 5, 10);
        gbc1.anchor = GridBagConstraints.NORTH;

        messageLabel = new JLabel("How do you want to view the listings?");
        panel.add(messageLabel, gbc1);

        gbc1.gridy++;

        list = new JComboBox<>(optionChoices);
        panel.add(list, gbc1);

        gbc1.gridy++;
        confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(actionListener);
        confirmButton.setPreferredSize(new Dimension(150, 30));
        panel.add(confirmButton, gbc1);

        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.gridx = 0;
        gbc2.gridy = gbc1.gridy + 1;
        gbc2.insets = new Insets(50, 10, 10, 10);
        gbc2.anchor = GridBagConstraints.SOUTH;

        returnToMenuButton = new JButton("Return to Buy Ticket Menu");
        returnToMenuButton.addActionListener(actionListener);
        returnToMenuButton.setPreferredSize(new Dimension(200, 50));
        panel.add(returnToMenuButton, gbc2);

        int verticalCenter = (Toolkit.getDefaultToolkit().getScreenSize().height - frame.getHeight()) / 2;
        frame.setLocationRelativeTo(null);
        frame.setLocation(frame.getLocation().x, verticalCenter);

        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public ArrayList<String> viewAllListingsGeneral() {
        ArrayList<String> fileInfo = Marketplace.readFile("SellerInfo.txt");
        ArrayList<String[]> allListedTicks = new ArrayList<>();
        ArrayList<String> list = new ArrayList<>();
        String s = "";
        int tickNumber = 1;
        for (String eachLine : fileInfo) {
            DoubleArrayList b = Buyers.splitStoreInfo(eachLine);
            String curSeller = (eachLine.split(","))[1];
            String curStoreListing = "";
            ArrayList<String> stuffInStores = b.getStuffInStores();
            ArrayList<String> storelist = b.getStoreList();

            for (int i = 0; i < stuffInStores.size(); i++) {
                String curItem = stuffInStores.get(i);
                if (storelist.contains(curItem)) {
                    curStoreListing = curItem;
                } else {
                    if (!Buyers.isNumeric(curItem)) {
                        s += tickNumber + ": \n";
                        tickNumber++;
                        s += "Seller: " + curSeller + "\n";
                        s += "Store: " + curSeller + "\n";
                        String[] thing = Buyers.listTicket(curItem);
                        s += "Sport: " + thing[0] + "\nLocation: " + thing[1] + "\nRow/Section Area: "
                                + thing[2] + "\nPrice: " + thing[3] + "\n";
                        String qtyNumber = stuffInStores.get(i + 1);
                        s += "Quantity: " + qtyNumber;
                        list.add(s);
                        s = "";

                        String[] tickInformation = new String[5];
                        for (int j = 0; j < 4; j++) {
                            tickInformation[j] = thing[j];
                        }
                        tickInformation[4] = qtyNumber;
                        allListedTicks.add(tickInformation);
                    }
                }
            }
        }
        return list;
    }

    public ArrayList<String> viewListingsWithConstraint(String constraint) {
        ArrayList<String> fileInfo = Marketplace.readFile("SellerInfo.txt");
        ArrayList<String[]> allListedTicks = new ArrayList<>();
        ArrayList<String> list = new ArrayList<>();
        String s = "";
        int tickNumber = 1;
        for (String eachLine : fileInfo) {

            DoubleArrayList b = Buyers.splitStoreInfo(eachLine);
            String curSeller = (eachLine.split(","))[1];
            String curStoreListing = "";
            ArrayList<String> stuffInStores = b.getStuffInStores();
            ArrayList<String> storelist = b.getStoreList();

            for (int i = 0; i < stuffInStores.size(); i++) {
                String curItem = stuffInStores.get(i);
                if (storelist.contains(curItem)) {
                    curStoreListing = curItem;
                } else {
                    if (!Buyers.isNumeric(curItem)) {
                        boolean shouldList = false;
                        String[] thing = Buyers.listTicket(curItem);
                        if (curStoreListing.toLowerCase().equals(constraint.toLowerCase()) || thing[0].toLowerCase().
                                equals(constraint.toLowerCase())) {
                            shouldList = true;
                        }
                        if (shouldList) {
                            s += tickNumber + ": \n";
                            tickNumber++;
                            s += "Seller: " + curSeller + "\n";
                            s += "Store: " + curStoreListing + "\n";
                            s += "Sport: " + thing[0] + "\nLocation: " + thing[1] + "\nRow/Section Area: " +
                                    thing[2] + "\nPrice: " + thing[3] + "\n";
                            String qtyNumber = stuffInStores.get(i + 1);
                            s += "Quantity: " + qtyNumber;
                            list.add(s);
                            s = "";

                            String[] tickInformation = new String[5];
                            for (int j = 0; j < 4; j++) {
                                tickInformation[j] = thing[j];
                            }
                            tickInformation[4] = qtyNumber;
                            allListedTicks.add(tickInformation);
                        }
                    }
                }
            }
        }
        return list;
    }

    public ArrayList<String> viewAllListingsSortedByTicketQuantity() {
        ArrayList<String> fileInfo = Marketplace.readFile("SellerInfo.txt");
        ArrayList<String[]> allListedTicks = new ArrayList<>();
        ArrayList<String> list = new ArrayList<>();
        String s = "";
        int tickNumber = 1;
        for (String eachLine : fileInfo) {
            DoubleArrayList b = Buyers.splitStoreInfo(eachLine);
            String curSeller = (eachLine.split(","))[1];
            String curStoreListing = "";
            ArrayList<String> stuffInStores = b.getStuffInStores();
            ArrayList<String> storelist = b.getStoreList();

            for (int i = 0; i < stuffInStores.size(); i++) {
                String curItem = stuffInStores.get(i);
                if (storelist.contains(curItem)) {
                    curStoreListing = curItem;
                } else {
                    if (!Buyers.isNumeric(curItem)) {
                        String[] thing = Buyers.listTicket(curItem);
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
            s += tickNumber + ": \n";
            tickNumber++;
            s += "Seller: " + things[0] + "\n";
            s += "Store: " + things[1] + "\n";
            s += "Sport: " + things[2] + "\nLocation: " + things[3] + "\nRow/Section Area: " + things[4] + "\nPrice: " +
                    things[5] + "\n";
            s += "Quantity: " + things[6] + "\n";
            list.add(s);
            s = "";
        }

        return list;
    }

    public void setLoginInfo(LoginInfo loginInfo) {
        this.loginInfo = loginInfo;
    }

    public void setShoppingCart(ArrayList<CartItems> shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public void setPreviousShoppingCart(ArrayList<CartItems> previousShoppingCart) {
        this.previousShoppingCart = previousShoppingCart;
    }
    public void setCurrentBuyer(Buyers currentBuyer) {
        this.currentBuyer = currentBuyer;
    }

    private void processTicketPurchaseResult(Ticket ticket, int quantity) {
        if (ticket != null) {
            shoppingCart.add(new CartItems(ticket, quantity));
            currentBuyer.setShoppingCart(shoppingCart);
            try {
                currentBuyer.updateShoppingCart();
                JOptionPane.showMessageDialog(frame, "Ticket purchase successful!", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Ticket purchase failed!", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new BuyTicketGUI());
    }
}
