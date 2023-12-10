import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Project 5: BuyerDashboardGUI
 *
 * Utilizes GUI to make a menu page for buyers to buy tickets, view history, and more.
 *
 * @author Henry J. Lee, Lab Section L20
 * @version November 13, 2023
 */

public class BuyerDashboardGUI extends JComponent implements Runnable {
    JFrame frame;
    JLabel welcomeMessage;
    JButton buyTicketButton;
    JButton viewHistoryButton;
    JButton viewStatisticsButton;
    JButton accountButton;
    JButton logOutButton;
    ArrayList<String> shoppingCart;
    ArrayList<String> previousShoppingCart;
    MarketplaceClient client;



    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

            if (e.getSource() == buyTicketButton) {
                client.sendInt(1);
                 
                // shoppingCart = currentBuyer.retrieveShoppingCart();
                BuyTicketMenuGUI buyTicketMenuGUI = new BuyTicketMenuGUI();
                buyTicketMenuGUI.setClient(client);
                buyTicketMenuGUI.run();
                frame.dispose();
            }
            if (e.getSource() == viewHistoryButton) {
                client.sendInt(2);
                previousShoppingCart = client.recieveStringArrayList();
                if (previousShoppingCart == null || previousShoppingCart.isEmpty()) {
                    client.sendBoolean(false);
                    JOptionPane.showMessageDialog(null, "Error! Cannot view the previous cart" +
                            " because it is empty!", "No Items!", JOptionPane.ERROR_MESSAGE);
                } else {
                    client.sendBoolean(true);
                    BuyerHistoryGUI buyerHistoryGUI = new BuyerHistoryGUI();
                    buyerHistoryGUI.setClient(client);
                    buyerHistoryGUI.run();
                    frame.dispose();
                }
            }
            if (e.getSource() == viewStatisticsButton) {
                client.sendInt(3);
                BuyerStatisticsGUI buyerStatisticsGUI = new BuyerStatisticsGUI();
                buyerStatisticsGUI.setClient(client);
                buyerStatisticsGUI.run();
                frame.dispose();
            }
            if (e.getSource() == accountButton) {
                client.sendInt(4);
                AccountGUI accountGUI = new AccountGUI();
                accountGUI.setClient(client);
                accountGUI.run();
                frame.dispose();
            }
            if (e.getSource() == logOutButton) {
                client.sendInt(5);
                JOptionPane.showMessageDialog(null, "Thank you for using Tickets@Purdue! " +
                                "We hope to see you soon!", "Tickets@Purdue Marketplace",
                        JOptionPane.INFORMATION_MESSAGE);
                SwingUtilities.getWindowAncestor((Component) e.getSource()).dispose();
            }
        }
    };

    public BuyerDashboardGUI() {

    }

   

    @Override
    public void run() {
        frame = new JFrame("Tickets@Purdue Buyer Dashboard");
        frame.setLayout(new GridBagLayout());

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.CENTER;

        welcomeMessage = new JLabel("Welcome! What would you like to do?");
        panel.add(welcomeMessage, gbc);

        JButton sampleButton = new JButton();
        sampleButton.setPreferredSize(new Dimension(200, 50));

        gbc.gridy++;
        buyTicketButton = new JButton("Buy A Ticket");
        buyTicketButton.addActionListener(actionListener);
        buyTicketButton.setPreferredSize(sampleButton.getPreferredSize());
        panel.add(buyTicketButton, gbc);

        gbc.gridy++;
        viewHistoryButton = new JButton("View Buying History");
        viewHistoryButton.addActionListener(actionListener);
        viewHistoryButton.setPreferredSize(sampleButton.getPreferredSize());
        panel.add(viewHistoryButton, gbc);

        gbc.gridy++;
        viewStatisticsButton = new JButton("View Statistics");
        viewStatisticsButton.addActionListener(actionListener);
        viewStatisticsButton.setPreferredSize(sampleButton.getPreferredSize());
        panel.add(viewStatisticsButton, gbc);

        gbc.gridy++;
        accountButton = new JButton("View Account Settings");
        accountButton.addActionListener(actionListener);
        accountButton.setPreferredSize(sampleButton.getPreferredSize());
        panel.add(accountButton, gbc);

        gbc.gridy++;
        logOutButton = new JButton("Logout");
        logOutButton.addActionListener(actionListener);
        logOutButton.setPreferredSize(sampleButton.getPreferredSize());
        panel.add(logOutButton, gbc);

        frame.add(panel);
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

 

    public static int determineID(String email, String password) {
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

    public void setClient(MarketplaceClient client)
    {
        this.client = client;
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new BuyerDashboardGUI());
    }
}
