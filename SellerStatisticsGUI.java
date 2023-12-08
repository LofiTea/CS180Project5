import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

/**
 * Project 5: SellerStatisticsGUI
 *
 * Utilizes GUI to allow a seller to view their stores' statistics.
 *
 * @author Henry J. Lee, Lab Section L20
 * @version November 13, 2023
 */

public class SellerStatisticsGUI extends JComponent implements Runnable {
    JFrame frame;
    JLabel label;
    JComboBox<String> list;
    JButton confirmButton;
    JButton returnToMenuButton;
    LoginInfo loginInfo;
    Sellers currentSeller;
    ArrayList<String> stores;
    int num;
    int choice;
    int number;
    int anotherOne;

    MarketplaceClient client;
    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == confirmButton) {
                ArrayList<String> allTransactions2 = Marketplace.readFile("TransactionInfo.txt");
                num = list.getSelectedIndex();
                if (num < 0 || num > stores.size()) {
                    JOptionPane.showMessageDialog(frame, "Please enter a valid store!",
                            "Invalid Store", JOptionPane.ERROR_MESSAGE);
                } else {
                    String[] options2 = {"1. List of customers and items purchased",
                            "2. List of products and number of sales"};
                    String decision2 = (String) JOptionPane.showInputDialog(frame,
                            "How would you like to view the statistics?", "View Seller Statistics",
                            JOptionPane.PLAIN_MESSAGE, null, options2, options2[0]);
                    choice = Arrays.asList(options2).indexOf(decision2);
                    if (choice < 0) {
                        JOptionPane.showMessageDialog(frame, "Please enter a valid choice!",
                                "Invalid Choice", JOptionPane.ERROR_MESSAGE);
                    } else {
                        ArrayList<String> buyerIDList = new ArrayList<>();
                        ArrayList<PrintableStat> toPrint = new ArrayList<>();
                        switch (choice) {
                            case 0:
                                for (int i = 0; i < allTransactions2.size(); i++) {
                                    String[] transactionLine = allTransactions2.get(i).split(",");
                                    if (Integer.parseInt(transactionLine[2]) == currentSeller.getSellerID() &&
                                            transactionLine[3].equals(stores.get(num))) {
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
                                        if (Integer.parseInt(transactionLine[2]) == currentSeller.getSellerID() &&
                                                transactionLine[1].equals(buyerIDList.get(i)) &&
                                                transactionLine[3].equals(stores.get(num))) {
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
                                String[] options3 = {"1. Default (Buyer ID)", "2. Customer Email (Alphabetical Order",
                                        "3. Number of Items Purchased"};
                                String decision3 = (String) JOptionPane.showInputDialog(frame,
                                        "How would you to sort the data?", "View Seller Statistics",
                                        JOptionPane.PLAIN_MESSAGE, null, options3, options3[0]);
                                number = Arrays.asList(options3).indexOf(decision3);
                                if (number < 0) {
                                    JOptionPane.showMessageDialog(frame, "Please enter a valid choice!",
                                            "Invalid Choice", JOptionPane.ERROR_MESSAGE);
                                } else {

                                    switch (number) {
                                        case 0:
                                            Collections.sort(toPrint,
                                                    Comparator.comparing(PrintableStat::getBuyerID));
                                            break;
                                        case 1:
                                            Collections.sort(toPrint,
                                                    Comparator.comparing(PrintableStat::getBuyerEmail));
                                            break;
                                        case 2:
                                            Collections.sort(toPrint,
                                                    Comparator.comparing(PrintableStat::getTotalItems));
                                            break;
                                    }

                                    for (int i = 0; i < toPrint.size(); i++) {
                                        String s = "Customer " + (i + 1) + "\n";
                                        JOptionPane.showMessageDialog(frame, s + "\n" +
                                                        toPrint.get(i).toString() + "\n", "View Seller Statistics",
                                                JOptionPane.INFORMATION_MESSAGE);
                                    }
                                    break;
                                }
                            case 1:
                                String[] options4 = {"1. Location", "2. Sport", "3. Number of Sales"};
                                String decision4 = (String) JOptionPane.showInputDialog(frame,
                                        "How would you to sort the data?", "View Seller Statistics",
                                        JOptionPane.PLAIN_MESSAGE, null, options4, options4[0]);
                                anotherOne = Arrays.asList(options4).indexOf(decision4);
                                switch (anotherOne) {
                                    case 0:
                                        ArrayList<String> list1 = Marketplace.uniqueProductsByLocation(stores.get(num),
                                                currentSeller.getSellerID());
                                        for (int i = 0; i < list1.size(); i++) {
                                            String[] listItem = list1.get(i).split(";");
                                            String s = "Product " + (i + 1) + "\n";
                                            JOptionPane.showMessageDialog(frame, s + "\n" +
                                                            "Product Info - \nSport: " + listItem[1] + "\nLocation: " +
                                                            listItem[2] + "\nSection: " + listItem[3] + "\nSales: " +
                                                            listItem[0], "View Seller Statistics",
                                                    JOptionPane.INFORMATION_MESSAGE);
                                        }
                                        break;
                                    case 1:
                                        ArrayList<String> list2 = Marketplace.uniqueProductsBySport(stores.get(num),
                                                currentSeller.getSellerID());
                                        for (int i = 0; i < list2.size(); i++) {
                                            String[] listItem = list2.get(i).split(";");
                                            String s = "Product " + (i + 1) + "\n";
                                            JOptionPane.showMessageDialog(frame, s + "\n" +
                                                            "Product Info - \nSport: " + listItem[1] + "\nLocation: " +
                                                            listItem[2] + "\nSection: " + listItem[3] + "\nSales: " +
                                                            listItem[0], "View Seller Statistics",
                                                    JOptionPane.INFORMATION_MESSAGE);
                                        }
                                        break;
                                    case 2:
                                        ArrayList<String> list3 = Marketplace.uniqueProductsBySales(stores.get(num),
                                                currentSeller.getSellerID());
                                        for (int i = 0; i < list3.size(); i++) {
                                            String[] listItem = list3.get(i).split(";");
                                            String s = "Product " + (i + 1) + "\n";
                                            JOptionPane.showMessageDialog(frame, s + "\n" +
                                                            "Product Info - \nSport: " + listItem[1] + "\nLocation: " +
                                                            listItem[2] + "\nSection: " + listItem[3] + "\nSales: " +
                                                            listItem[0], "View Seller Statistics",
                                                    JOptionPane.INFORMATION_MESSAGE);
                                        }
                                }
                        }
                    }
                }
            }
            if (e.getSource() == returnToMenuButton) {
                SellerDashboardGUI sellerDashboardGUI = new SellerDashboardGUI();
//                sellerDashboardGUI.setLoginInfo(loginInfo);
//                sellerDashboardGUI.setCurrentSeller(currentSeller);
//                sellerDashboardGUI.setStores(stores);
                sellerDashboardGUI.setClient(client);
                sellerDashboardGUI.run();
                frame.dispose();
            }
        }
    };

    public SellerStatisticsGUI() {

    }

    public void run() {
        frame = new JFrame("Seller Statistics");
        JPanel panel = new JPanel(new GridBagLayout());

        GridBagConstraints gbc1 = new GridBagConstraints();
        gbc1.gridx = 0;
        gbc1.gridy = 0;
        gbc1.insets = new Insets(5, 10, 5, 10);
        gbc1.anchor = GridBagConstraints.CENTER;

        stores = currentSeller.retrieveStores();

        if (stores == null) {
            JOptionPane.showMessageDialog(null, "Error! There are no stores!",
                    "No Stores", JOptionPane.ERROR_MESSAGE);
        } else {
            label = new JLabel("Which store would you like to view the sales for?");
            panel.add(label, gbc1);
            gbc1.gridy++;

            String[] options1 = new String[stores.size()];
            for (int i = 0; i < stores.size(); i++) {
                options1[i] = i + 1 + ". " + stores.get(i);
            }
            list = new JComboBox<>(options1);
            panel.add(list, gbc1);

            gbc1.gridy++;
            confirmButton = new JButton("See Selected Item");
            confirmButton.addActionListener(actionListener);
            confirmButton.setPreferredSize(new Dimension(150, 30));
            panel.add(confirmButton, gbc1);

            GridBagConstraints gbc2 = new GridBagConstraints();
            gbc2.gridx = 0;
            gbc2.gridy = gbc1.gridy + 1;
            gbc2.insets = new Insets(50, 10, 10, 10);
            gbc2.anchor = GridBagConstraints.CENTER;

            returnToMenuButton = new JButton("Return to Menu");
            returnToMenuButton.addActionListener(actionListener);
            returnToMenuButton.setPreferredSize(new Dimension(200, 50));
            panel.add(returnToMenuButton, gbc2);

            int verticalCenter = (Toolkit.getDefaultToolkit().getScreenSize().height - frame.getHeight()) / 2;
            frame.setLocationRelativeTo(null);
            frame.setLocation(frame.getLocation().x, verticalCenter);
        }
        frame.add(panel);
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }


    public void setLoginInfo(LoginInfo loginInfo) {
            this.loginInfo = loginInfo;
        }

        public void setCurrentSeller(Sellers currentSeller) {
            this.currentSeller = currentSeller;
        }

        public void setStores(ArrayList<String> stores) {
            this.stores = stores;
        }

        public static void main(String[] args) {
            SwingUtilities.invokeLater(new SellerStatisticsGUI());
        }
    }
