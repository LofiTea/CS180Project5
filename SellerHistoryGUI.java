import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Homework/Project X: ClassName
 * <p>
 * Describe what the assignment is about.
 *
 * @author Henry J. Lee, Lab Section L20
 * @version Month XX, 2023
 */

public class SellerHistoryGUI extends JComponent implements Runnable {
    JFrame frame;
    JLabel label;
    JComboBox<String> list;
    JButton confirmButton;
    JButton returnToMenuButton;
    LoginInfo loginInfo;

    public SellerHistoryGUI() {

    }

    public SellerHistoryGUI(LoginInfo loginInfo) {
        this.loginInfo = loginInfo;
    }

    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == confirmButton) {
                Sellers currentSeller = new Sellers(new LoginInfo(loginInfo.getEmail(), loginInfo.getPassword()),
                        determineID(loginInfo.getEmail(), loginInfo.getPassword()));
                ArrayList<String> allTransactions = Marketplace.readFile("TransactionInfo.txt");
                ArrayList<String> stores = currentSeller.retrieveStores();


                ArrayList<String> sales = new ArrayList<>();
                double totalRevenue = 0;
                int totalStores = stores.size();

                for (int store = 1; store <= totalStores; store++) {
                    for (String allTransaction : allTransactions) {
                        String[] transactionInfo = allTransaction.split(",");
                        if (transactionInfo[2].equals(String.valueOf(currentSeller.getSellerID()))) {
                            if (transactionInfo[3].equals(stores.get(store - 1))) {
                                String buyerEmail = null;
                                ArrayList<String> buyers = Marketplace.readFile("BuyerHistory.txt");
                                for (String buyer : buyers) {
                                    String[] buyerInfo = buyer.split(",");
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
                                        Double.parseDouble(transactionInfo[4]) * Integer.parseInt(transactionInfo[5])));

                                totalRevenue += Double.parseDouble(transactionInfo[4]) * Integer.parseInt(transactionInfo[5]);

                            }
                        }
                    }

                    if (sales.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Error! There are no transactions "
                                + "for this store!", "No Transactions!", JOptionPane.ERROR_MESSAGE);
                    } else {
                        for (int i = 0; i < sales.size(); i++) {
                            JOptionPane.showMessageDialog(null, i + 1 + ".\n" + sales.get(i),
                                    "Transaction Information", JOptionPane.INFORMATION_MESSAGE);
                        }
                        JOptionPane.showMessageDialog(null, "Total Revenue from store: " +
                                        String.format("%.2f", totalRevenue), "Transaction Information",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
            if (e.getSource() == returnToMenuButton) {
                SellerDashboardGUI sellerDashboardGUI = new SellerDashboardGUI();
                sellerDashboardGUI.setLoginInfo(loginInfo);
                sellerDashboardGUI.run();
                frame.dispose();
            }
        }
    };

    public void run() {
        frame = new JFrame("Seller History");
        frame.setLayout(new GridBagLayout());

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.add(panel);

        GridBagConstraints gbc1 = new GridBagConstraints();
        gbc1.gridx = 0;
        gbc1.gridy = 0;
        gbc1.insets = new Insets(5, 10, 5, 10);
        gbc1.anchor = GridBagConstraints.NORTH;

        Sellers currentSeller = new Sellers(new LoginInfo(loginInfo.getEmail(), loginInfo.getPassword()),
                determineID(loginInfo.getEmail(), loginInfo.getPassword()));
        ArrayList<String> stores = currentSeller.retrieveStores();

        if (stores == null) {
            JOptionPane.showMessageDialog(null, "Error! There are no stores to view!",
                    "No Stores!", JOptionPane.ERROR_MESSAGE);
        } else {
            label = new JLabel("Which store would you like to view the sales for?");
            panel.add(label);
            gbc1.gridy++;

            String[] options = new String[stores.size()];
            for (int i = 0; i < stores.size(); i++) {
                options[i] = i + 1 + ". " + stores.get(i);
            }
            list = new JComboBox<>(options);
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
            gbc2.anchor = GridBagConstraints.SOUTH;

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

    public int determineID(String email, String password) {
        ArrayList<String> arrayList = Marketplace.readFile("LoginInfo.txt");
        int id = 0;
        for (int i = 0; i < arrayList.size(); i++) {
            String[] userInfo = arrayList.get(i).split(",");
            if (email.equals(userInfo[1]) && password.equals(userInfo[2])) {
                id = Integer.parseInt(userInfo[0]);
            }
        }
        return id;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new SellerHistoryGUI());
    }
}