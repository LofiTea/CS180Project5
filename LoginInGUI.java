import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

/**
 * Project 5: LoginInGUI
 *
 * Utilizes GUI to allow a user to login to the application.
 *
 * @author Henry J. Lee, Lab Section L20
 * @version November 13, 2023
 */

public class LoginInGUI extends JComponent implements Runnable {

    //MarketplaceClient client;
    int optionInitial;
    String email;
    String password;
    int id;
    JFrame frame;
    JLabel welcomeMessage;
    JLabel emailLabel;
    JLabel passwordLabel;
    JTextField emailTextField;
    JTextField passwordTextField;
    JButton loginButton;
    JLabel newUserMessage;
    JButton createAccountButton;
    ActionListener actionListenerTop = new ActionListener() {
        boolean loginSuccess = false;

        @Override
        public void actionPerformed(ActionEvent e) {


            if (e.getSource() == loginButton && !loginSuccess) {
                MarketplaceClient client = null;
                try {
                    client = new MarketplaceClient();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                loginSuccess = false;
                email = emailTextField.getText();
                password = passwordTextField.getText();

                try {
                    //client.sendOptionInitial(1);
                    loginSuccess = client.sendLogin(email, password, 1);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }

                if(loginSuccess) {
                    String role;
                    try {
                        role = client.getString();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    } catch (ClassNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }

                    if (role.equals("b")) {
                         //System.out.println(role);
                       BuyerDashboardGUI buyerDashboardGUI = new BuyerDashboardGUI();
                      buyerDashboardGUI.run();
                      buyerDashboardGUI.setClient(client);
                      frame.dispose();
                    } else {
                        //System.out.println(role)
                     SellerDashboardGUI sellerDashboardGUI = new SellerDashboardGUI();
                     sellerDashboardGUI.setClient(client);
                     sellerDashboardGUI.run();
                     frame.dispose();
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "Error! Account not found!",
                            "Tickets@Purdue Marketplace", JOptionPane.ERROR_MESSAGE);
                }

            }
        }
    };

    ActionListener actionListenerBottom = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == createAccountButton) {

                CreateAccountGUI createAccountGUI = new CreateAccountGUI();
                createAccountGUI.run();
                frame.dispose();
            }
        }
    };

    public LoginInGUI() {

    }

    public void run() {
        frame = new JFrame("Tickets@Purdue Marketplace");
        frame.setLayout(new GridBagLayout());

        JPanel loginPanel = createLoginPanel();
        JPanel createAccountPanel = createCreateAccountPanel();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        frame.add(loginPanel, gbc);

        gbc.gridy = 1;
        frame.add(createAccountPanel, gbc);

        frame.setSize(500, 300);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private JPanel createLoginPanel() {
        JPanel loginPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 10, 12, 10);

        welcomeMessage = new JLabel("Welcome to Tickets@Purdue!\nPlease login.");
        loginPanel.add(welcomeMessage, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        emailLabel = new JLabel("Email:");
        loginPanel.add(emailLabel, gbc);

        gbc.gridx++;
        emailTextField = new JTextField(15);
        loginPanel.add(emailTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        passwordLabel = new JLabel("Password:");
        loginPanel.add(passwordLabel, gbc);

        gbc.gridx++;
        passwordTextField = new JTextField(15);
        loginPanel.add(passwordTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        loginButton = new JButton("Login");
        loginButton.addActionListener(actionListenerTop);
        loginPanel.add(loginButton, gbc);

        return loginPanel;
    }

    private JPanel createCreateAccountPanel() {
        JPanel createAccountPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(12, 10, 5, 10);

        newUserMessage = new JLabel("To create a new account, hit the button below!");
        createAccountPanel.add(newUserMessage, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        createAccountButton = new JButton("Create New Account");
        createAccountButton.addActionListener(actionListenerBottom);
        createAccountPanel.add(createAccountButton, gbc);

        return createAccountPanel;
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(new LoginInGUI());
    }
}
