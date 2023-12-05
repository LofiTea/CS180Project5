import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Homework/Project X: ClassName
 * <p>
 * Describe what the assignment is about.
 *
 * @author Henry J. Lee, Lab Section L20
 * @version Month XX, 2023
 */

public class AccountGUI extends JComponent implements Runnable {
    JFrame frame;
    JLabel welcomeMessage;
    JButton viewAccountDetailsButton;
    JButton editEmailButton;
    JButton editPasswordButton;
    JButton deleteAccountButton;
    JButton returnToMenuButton;
    LoginInfo loginInfo;
    MarketplaceClient client;
    ActionListener actionListener = new ActionListener() {
        int count = 0;
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == viewAccountDetailsButton) {

                client.sendInt(1);

//                if (count == 0) {
//                    JOptionPane.showMessageDialog(null, viewDetails(loginInfo.getEmail(),
//                            loginInfo.getPassword()), "Account Details", JOptionPane.INFORMATION_MESSAGE);
//                } else {
//                    JOptionPane.showMessageDialog(null, "Please logout to see the " +
//                            "new account details.", "Account Details", JOptionPane.INFORMATION_MESSAGE);
//                    count = 0;
//                }

                String details = client.receiveAccountDetails();

                JOptionPane.showMessageDialog(null, details, "Account Details", JOptionPane.INFORMATION_MESSAGE);

            }
            if (e.getSource() == editEmailButton) {
                client.sendInt(2);
                boolean isValidEmail = false;
                String newEmail = "";
                do {
                    newEmail = JOptionPane.showInputDialog(null, "What would you like to " +
                                "change your email to?", "Change Email", JOptionPane.QUESTION_MESSAGE);
                    if (newEmail == null || newEmail.isEmpty()) {
                         newEmail = "";
                         isValidEmail = true;
                    } else if (!newEmail.contains("@")) {
                        JOptionPane.showMessageDialog(null, "Error! Email needs an '@' symbol!",
                                "Change Email", JOptionPane.ERROR_MESSAGE);
                    }
                    else{
                        isValidEmail = true;
                    }
                } while(!isValidEmail);


                try {
                    if(newEmail == null)
                    {
                        JOptionPane.showMessageDialog(null, "String null!",
                                "Change Email", JOptionPane.ERROR_MESSAGE);
                    }
                    client.sendString(newEmail);
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
            if (e.getSource() == editPasswordButton) {
                client.sendInt(3);

                String newPassword = JOptionPane.showInputDialog(null, "What would you like to "
                        + "change your password to?", "Change Password", JOptionPane.QUESTION_MESSAGE);
                if(newPassword != null) {
                    if (newPassword.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Error! Password needs to be at least "
                                + "8 characters long!", "Change Password", JOptionPane.ERROR_MESSAGE);
                    } else if (newPassword.length() < 8) {
                        JOptionPane.showMessageDialog(null, "Error! Password needs to be at least "
                                + "8 characters long!", "Change Password", JOptionPane.ERROR_MESSAGE);
                    } else if (!MarketplaceClient.isStrongAlphanumeric(newPassword)) {
                        JOptionPane.showMessageDialog(null, "Error! Password must have letters, " +
                                "numbers and special characters.", "Change Password", JOptionPane.ERROR_MESSAGE);
                    }
                }

                try {
                    client.sendString(newPassword);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
            if (e.getSource() == deleteAccountButton) {
                client.sendInt(4);
                int num = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete"
                        + " your account?", "Delete Account", JOptionPane.YES_NO_OPTION);
                if (num == JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog(null, "We are sorry to see you go! Goodbye!",
                            "Account Deleted", JOptionPane.INFORMATION_MESSAGE);
                    try {
                        client.sendString("yes");
                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    Window[] windows = Window.getWindows();
                    for (Window window : windows) {
                        if (window instanceof JFrame) {
                            window.dispose();
                        }
                    }
                } else {
                    try {
                        client.sendString("no");
                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }
            }
            if (e.getSource() == returnToMenuButton) {
                client.sendInt(5);
                String role = "";
                try {
                    role = client.getString();
                } 
                catch(Exception ef){
                    ef.printStackTrace();
                }
                if (role.equals("b")) {
//                    BuyerDashboardGUI buyerDashboardGUI = new BuyerDashboardGUI();
//                    buyerDashboardGUI.setLoginInfo(loginInfo);
//                    buyerDashboardGUI.run();
//                    frame.dispose();
                } else {
                    SellerDashboardGUI sellerDashboardGUI = new SellerDashboardGUI();
                    sellerDashboardGUI.setClient(client);
                    sellerDashboardGUI.run();
                    frame.dispose();
                }
            }
        }
    };

    public AccountGUI() {

    }



    public String viewDetails(String email, String password) {
        ArrayList<String> details = Marketplace.readFile("LoginInfo.txt");
        for (int i = 0; i < details.size(); i++) {
            String[] arr = details.get(i).split(",");
            if (email.equals(arr[1]) && password.equals(arr[2])) {
                if (arr[3].equals("b")) {
                    return "ID: " + arr[0] + "\nEmail: " + arr[1] + "\nAccount Type: Buyer";
                } else {
                    return "ID: " + arr[0] + "\nEmail: " + arr[1] + "\nAccount Type: Seller";
                }
            }
        }
        return "";
    }

    public void run() {
        frame = new JFrame("My Account");
        frame.setLayout(new GridBagLayout());

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.CENTER;

        welcomeMessage = new JLabel("Account Settings");
        panel.add(welcomeMessage, gbc);

        JButton sampleButton = new JButton();
        sampleButton.setPreferredSize(new Dimension(200, 50));

        gbc.gridy++;
        viewAccountDetailsButton = new JButton("View Account Details");
        viewAccountDetailsButton.addActionListener(actionListener);
        viewAccountDetailsButton.setPreferredSize(sampleButton.getPreferredSize());
        panel.add(viewAccountDetailsButton, gbc);

        gbc.gridy++;
        editEmailButton = new JButton("Change Email");
        editEmailButton.addActionListener(actionListener);
        editEmailButton.setPreferredSize(sampleButton.getPreferredSize());
        panel.add(editEmailButton, gbc);

        gbc.gridy++;
        editPasswordButton = new JButton("Change Password");
        editPasswordButton.addActionListener(actionListener);
        editPasswordButton.setPreferredSize(sampleButton.getPreferredSize());
        panel.add(editPasswordButton, gbc);

        gbc.gridy++;
        deleteAccountButton = new JButton("Delete Account");
        deleteAccountButton.addActionListener(actionListener);
        deleteAccountButton.setPreferredSize(sampleButton.getPreferredSize());
        panel.add(deleteAccountButton, gbc);

        gbc.gridy++;
        returnToMenuButton = new JButton("Return to Main Menu");
        returnToMenuButton.addActionListener(actionListener);
        returnToMenuButton.setPreferredSize(sampleButton.getPreferredSize());
        panel.add(returnToMenuButton, gbc);

        frame.add(panel);
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    synchronized public void setClient(MarketplaceClient client) {
        this.client = client;
    }

    public void setLoginInfo(LoginInfo loginInfo) {
        this.loginInfo = loginInfo;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new AccountGUI());
    }
}
