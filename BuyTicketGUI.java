import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Project 5: BuyTicketGUI
 * <p>
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
    String[] optionChoices = {"1. View All Listings", "2. View Listings By Store", "3. View Listings By Sport",
            "4. View Listings By Quantity Available"};
    ArrayList<String> realShoppingCart;
    MarketplaceClient client;
    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == confirmButton) {
                int option = list.getSelectedIndex();
                list.removeAllItems();
                client.sendInt(option);
                realShoppingCart = client.recieveStringArrayList();

                switch (option) {
                    case 0 -> {
                        ArrayList<String> allTicks = client.recieveStringArrayList();

                        if (!allTicks.get(0).equals("Lebron James")) {
                            String[] options = new String[allTicks.size()];
                            for (int i = 0; i < options.length; i++) {
                                String curTicket = allTicks.get(i);
                                curTicket = curTicket.replace("&", "<br>");
                                options[i] = "<html>" + curTicket + "</html>";
                            }
                            String ticket = (String) JOptionPane.showInputDialog(frame,
                                    "What type of ticket would you like to buy?", "View All Listings",
                                    JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

                            boolean wantToPurchase = true;
                            int whatToBuy = -123;
                            int howMany = -1;
                            if (ticket == null) {
                                wantToPurchase = false;
                                //client.sendInt(1);

                            } else {
                                whatToBuy = Arrays.asList(options).indexOf(ticket);
                                boolean isValidNum = false;

                                do {
                                    String num = JOptionPane.showInputDialog(frame, "How many would " +
                                            "you like to buy?", "View All Listings", JOptionPane.QUESTION_MESSAGE);
                                    if (num == null) {
                                        wantToPurchase = false;
                                        isValidNum = true;
                                    } else {
                                        try {
                                            howMany = Integer.parseInt(num);
                                            if (howMany <= 0) {
                                                JOptionPane.showMessageDialog(null, "Error! Please enter a valid ticket quantity",
                                                        "Invalid Quantity", JOptionPane.ERROR_MESSAGE);

                                            } else {
                                                isValidNum = true;
                                            }
                                        } catch (NumberFormatException nf) {
                                            JOptionPane.showMessageDialog(null, "Error! Please enter a valid ticket quantity",
                                                    "Invalid Quantity", JOptionPane.ERROR_MESSAGE);
                                        }
                                    }

                                }
                                while (!isValidNum);//endofcopy
                            }


                            if (wantToPurchase) {
                                client.sendBoolean(true);
                                client.sendInt(whatToBuy + 1);
                                client.sendInt(howMany);
                                boolean wasTicketPurchaseSuccesful = client.receiveBoolean();
                                if (wasTicketPurchaseSuccesful) {
                                    JOptionPane.showMessageDialog(frame, "Ticket purchase successful!", "Success",
                                            JOptionPane.INFORMATION_MESSAGE);
                                    client.sendInt(1);
                                } else {
                                    JOptionPane.showMessageDialog(frame, "Ticket purchase failed!", "Failed Purchase",
                                            JOptionPane.ERROR_MESSAGE);
                                    client.sendInt(1);
                                }
                            } else {
                                client.sendBoolean(false);
                                client.sendInt(1);
                            }


                        } else {
                            JOptionPane.showMessageDialog(frame, "There are no listings", "No Listings",
                                    JOptionPane.ERROR_MESSAGE);
                            client.sendInt(1);
                        }
                    }
                    case 1 -> {
                        String curStore = JOptionPane.showInputDialog(frame, "What store would "
                                + "you like to buy from?", "View Listings By Store", JOptionPane.QUESTION_MESSAGE);
                        if (curStore == null) {
                            client.sendBoolean(true);
                            client.sendInt(1);
                        } else {
                            client.sendBoolean(false);
                            client.sendString(curStore);
                            ArrayList<String> allTicks = client.recieveStringArrayList();
                            if (!allTicks.get(0).equals("Lebron James")) {
                                String[] options = new String[allTicks.size()];
                                for (int i = 0; i < options.length; i++) {
                                    String curTicket = allTicks.get(i);
                                    curTicket = curTicket.replace("&", "<br>");
                                    options[i] = "<html>" + curTicket + "</html>";
                                }
                                String ticket = (String) JOptionPane.showInputDialog(frame,
                                        "What type of ticket would you like to buy?", "View All Listings",
                                        JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

                                boolean wantToPurchase = true;
                                int whatToBuy = -123;
                                int howMany = -1;
                                if (ticket == null) {
                                    wantToPurchase = false;
                                    //client.sendInt(1);

                                } else {
                                    whatToBuy = Arrays.asList(options).indexOf(ticket);
                                    boolean isValidNum = false;

                                    do {
                                        String num = JOptionPane.showInputDialog(frame, "How many would " +
                                                "you like to buy?", "View All Listings", JOptionPane.QUESTION_MESSAGE);
                                        if (num == null) {
                                            wantToPurchase = false;
                                            isValidNum = true;
                                        } else {
                                            try {
                                                howMany = Integer.parseInt(num);
                                                if (howMany <= 0) {
                                                    JOptionPane.showMessageDialog(null, "Error! Please enter a valid ticket quantity",
                                                            "Invalid Quantity", JOptionPane.ERROR_MESSAGE);

                                                } else {
                                                    isValidNum = true;
                                                }
                                            } catch (NumberFormatException nf) {
                                                JOptionPane.showMessageDialog(null, "Error! Please enter a valid ticket quantity",
                                                        "Invalid Quantity", JOptionPane.ERROR_MESSAGE);
                                            }
                                        }

                                    }
                                    while (!isValidNum);//endofcopy
                                }


                                if (wantToPurchase) {
                                    client.sendBoolean(true);
                                    client.sendInt(whatToBuy + 1);
                                    client.sendInt(howMany);
                                    boolean wasTicketPurchaseSuccesful = client.receiveBoolean();
                                    if (wasTicketPurchaseSuccesful) {
                                        JOptionPane.showMessageDialog(frame, "Ticket purchase successful!", "Success",
                                                JOptionPane.INFORMATION_MESSAGE);
                                        client.sendInt(1);
                                    } else {
                                        JOptionPane.showMessageDialog(frame, "Ticket purchase failed!", "Failed Purchase",
                                                JOptionPane.ERROR_MESSAGE);
                                        client.sendInt(1);
                                    }
                                } else {
                                    client.sendBoolean(false);
                                    client.sendInt(1);
                                }


                            } else {
                                JOptionPane.showMessageDialog(frame, "There are no listings", "No Listings",
                                        JOptionPane.ERROR_MESSAGE);
                                client.sendInt(1);
                            }
                        }
                    }
                    case 2 -> {
                        String curStore = JOptionPane.showInputDialog(frame, "What Sport would "
                                + "you like to buy tickets for?", "View Listings By Store", JOptionPane.QUESTION_MESSAGE);
                        if (curStore == null) {
                            client.sendBoolean(true);
                            client.sendInt(1);
                        } else {
                            client.sendBoolean(false);
                            client.sendString(curStore);
                            ArrayList<String> allTicks = client.recieveStringArrayList();
                            if (!allTicks.get(0).equals("Lebron James")) {
                                String[] options = new String[allTicks.size()];
                                for (int i = 0; i < options.length; i++) {
                                    String curTicket = allTicks.get(i);
                                    curTicket = curTicket.replace("&", "<br>");
                                    options[i] = "<html>" + curTicket + "</html>";
                                }
                                String ticket = (String) JOptionPane.showInputDialog(frame,
                                        "What type of ticket would you like to buy?", "View All Listings",
                                        JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

                                boolean wantToPurchase = true;
                                int whatToBuy = -123;
                                int howMany = -1;
                                if (ticket == null) {
                                    wantToPurchase = false;
                                    //client.sendInt(1);

                                } else {
                                    whatToBuy = Arrays.asList(options).indexOf(ticket);
                                    boolean isValidNum = false;

                                    do {
                                        String num = JOptionPane.showInputDialog(frame, "How many would " +
                                                "you like to buy?", "View All Listings", JOptionPane.QUESTION_MESSAGE);
                                        if (num == null) {
                                            wantToPurchase = false;
                                            isValidNum = true;
                                        } else {
                                            try {
                                                howMany = Integer.parseInt(num);
                                                if (howMany <= 0) {
                                                    JOptionPane.showMessageDialog(null, "Error! Please enter a valid ticket quantity",
                                                            "Invalid Quantity", JOptionPane.ERROR_MESSAGE);

                                                } else {
                                                    isValidNum = true;
                                                }
                                            } catch (NumberFormatException nf) {
                                                JOptionPane.showMessageDialog(null, "Error! Please enter a valid ticket quantity",
                                                        "Invalid Quantity", JOptionPane.ERROR_MESSAGE);
                                            }
                                        }

                                    }
                                    while (!isValidNum);//endofcopy
                                }


                                if (wantToPurchase) {
                                    client.sendBoolean(true);
                                    client.sendInt(whatToBuy + 1);
                                    client.sendInt(howMany);
                                    boolean wasTicketPurchaseSuccesful = client.receiveBoolean();
                                    if (wasTicketPurchaseSuccesful) {
                                        JOptionPane.showMessageDialog(frame, "Ticket purchase successful!", "Success",
                                                JOptionPane.INFORMATION_MESSAGE);
                                        client.sendInt(1);
                                    } else {
                                        JOptionPane.showMessageDialog(frame, "Ticket purchase failed!", "Failed Purchase",
                                                JOptionPane.ERROR_MESSAGE);
                                        client.sendInt(1);
                                    }
                                } else {
                                    client.sendBoolean(false);
                                    client.sendInt(1);
                                }


                            } else {
                                JOptionPane.showMessageDialog(frame, "There are no listings", "No Listings",
                                        JOptionPane.ERROR_MESSAGE);
                                client.sendInt(1);
                            }
                        }
                    }
                    case 3 -> {
                        ArrayList<String> allTicks = client.recieveStringArrayList();

                        if (!allTicks.get(0).equals("Lebron James")) {
                            String[] options = new String[allTicks.size()];
                            for (int i = 0; i < options.length; i++) {
                                String curTicket = allTicks.get(i);
                                curTicket = curTicket.replace("&", "<br>");
                                options[i] = "<html>" + curTicket + "</html>";
                            }
                            String ticket = (String) JOptionPane.showInputDialog(frame,
                                    "What type of ticket would you like to buy?", "View All Listings",
                                    JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

                            boolean wantToPurchase = true;
                            int whatToBuy = -123;
                            int howMany = -1;
                            if (ticket == null) {
                                wantToPurchase = false;
                                //client.sendInt(1);

                            } else {
                                whatToBuy = Arrays.asList(options).indexOf(ticket);
                                boolean isValidNum = false;

                                do {
                                    String num = JOptionPane.showInputDialog(frame, "How many would " +
                                            "you like to buy?", "View All Listings", JOptionPane.QUESTION_MESSAGE);
                                    if (num == null) {
                                        wantToPurchase = false;
                                        isValidNum = true;
                                    } else {
                                        try {
                                            howMany = Integer.parseInt(num);
                                            if (howMany <= 0) {
                                                JOptionPane.showMessageDialog(null, "Error! Please enter a valid ticket quantity",
                                                        "Invalid Quantity", JOptionPane.ERROR_MESSAGE);

                                            } else {
                                                isValidNum = true;
                                            }
                                        } catch (NumberFormatException nf) {
                                            JOptionPane.showMessageDialog(null, "Error! Please enter a valid ticket quantity",
                                                    "Invalid Quantity", JOptionPane.ERROR_MESSAGE);
                                        }
                                    }

                                }
                                while (!isValidNum);//endofcopy
                            }


                            if (wantToPurchase) {
                                client.sendBoolean(true);
                                client.sendInt(whatToBuy + 1);
                                client.sendInt(howMany);
                                boolean wasTicketPurchaseSuccesful = client.receiveBoolean();
                                if (wasTicketPurchaseSuccesful) {
                                    JOptionPane.showMessageDialog(frame, "Ticket purchase successful!", "Success",
                                            JOptionPane.INFORMATION_MESSAGE);
                                    client.sendInt(1);
                                } else {
                                    JOptionPane.showMessageDialog(frame, "Ticket purchase failed!", "Failed Purchase",
                                            JOptionPane.ERROR_MESSAGE);
                                    client.sendInt(1);
                                }
                            } else {
                                client.sendBoolean(false);
                                client.sendInt(1);
                            }


                        } else {
                            JOptionPane.showMessageDialog(frame, "There are no listings", "No Listings",
                                    JOptionPane.ERROR_MESSAGE);
                            client.sendInt(1);
                        }
                    }
                }
                for (String optionChoice : optionChoices) {
                    list.addItem(optionChoice);
                }
                list.setSelectedIndex(0);
            }
            if (e.getSource() == returnToMenuButton) {
                client.sendInt(-3);
                BuyTicketMenuGUI buyTicketMenuGUI = new BuyTicketMenuGUI();
                buyTicketMenuGUI.setClient(client);
                buyTicketMenuGUI.run();
                frame.dispose();
            }
        }
    };

    public BuyTicketGUI() {

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new BuyTicketGUI());
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


    public void setClient(MarketplaceClient client) {
        this.client = client;
    }
}
