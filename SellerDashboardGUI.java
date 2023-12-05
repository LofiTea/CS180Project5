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

public class SellerDashboardGUI extends JComponent implements Runnable {
    JFrame frame;
    JLabel welcomeMessage;
    JButton sellTicketButton;
    JButton viewHistoryButton;
    JButton viewStatisticsButton;
    JButton accountButton;
    JButton logOutButton;
    LoginInfo loginInfo;
    Sellers currentSeller;
    ArrayList<String> stores;
    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            currentSeller = new Sellers(determineID(loginInfo.getEmail(), loginInfo.getPassword()));
            if (e.getSource() == sellTicketButton) {
                SellTicketMenuGUI sellTicketMenuGUI = new SellTicketMenuGUI();
                sellTicketMenuGUI.setLoginInfo(loginInfo);
                sellTicketMenuGUI.setCurrentSeller(currentSeller);
                sellTicketMenuGUI.run();
                frame.dispose();
            }
            if (e.getSource() == viewHistoryButton) {
                SellerHistoryGUI sellerHistoryGUI = new SellerHistoryGUI();
                sellerHistoryGUI.setLoginInfo(loginInfo);
                sellerHistoryGUI.run();
                frame.dispose();
            }
            if (e.getSource() == viewStatisticsButton) {
                // put logic here
            }
            if (e.getSource() == accountButton) {
                AccountGUI accountGUI = new AccountGUI();
                accountGUI.setLoginInfo(loginInfo);
                accountGUI.run();
                frame.dispose();
            }
            if (e.getSource() == logOutButton) {
                JOptionPane.showMessageDialog(null, "Thank you for using Tickets@Purdue! " +
                                "We hope to see you soon!", "Tickets@Purdue Marketplace",
                        JOptionPane.INFORMATION_MESSAGE);
                SwingUtilities.getWindowAncestor((Component) e.getSource()).dispose();
            }
        }
    };

    public SellerDashboardGUI() {

    }

    public SellerDashboardGUI(LoginInfo loginInfo) {
        this.loginInfo = loginInfo;
    }

    public void run() {
        frame = new JFrame("Tickets@Purdue Seller Dashboard");
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
        sellTicketButton = new JButton("Sell A Ticket");
        sellTicketButton.addActionListener(actionListener);
        sellTicketButton.setPreferredSize(sampleButton.getPreferredSize());
        panel.add(sellTicketButton, gbc);

        gbc.gridy++;
        viewHistoryButton = new JButton("View Selling History");
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

    public void setLoginInfo(LoginInfo loginInfo) {
        this.loginInfo = loginInfo;
    }

    public void setCurrentSeller(Sellers currentSeller) {
        this.currentSeller = currentSeller;
    }

    public void setStores(ArrayList<String> stores) {
        this.stores = stores;
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
        SwingUtilities.invokeLater(new SellerDashboardGUI());
    }
}