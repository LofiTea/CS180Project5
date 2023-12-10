import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

/**
 * Project 5: CreateAccountGUI
 * <p>
 * Utilizes GUI to allow a user to create an account.
 *
 * @author Henry J. Lee, Lab Section L20
 * @version November 13, 2023
 */

public class CreateAccountGUI extends JComponent implements Runnable {
    JFrame frame;
    JLabel welcomeMessage;
    JLabel typeOfAccountMessage;
    JRadioButton buyerChoice;
    JRadioButton sellerChoice;
    JLabel emailLabel;
    JTextField emailTextField;
    JLabel passwordLabel;
    JTextField passwordTextField;
    JLabel reEnterPasswordLabel;
    JTextField reEnterPasswordTextField;
    JButton createAccountButton;
    String type;
    ActionListener actionListenerBottom = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            MarketplaceClient client;

            try {
                client = new MarketplaceClient();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            if (buyerChoice.isSelected()) {
                type = "b";
            }
            if (sellerChoice.isSelected()) {
                type = "s";
            }
            if (e.getSource() == createAccountButton) {
                if (!buyerChoice.isSelected() && !sellerChoice.isSelected()) {
                    JOptionPane.showMessageDialog(null, "Error! Please select an account type!",
                            "Tickets@Purdue Marketplace", JOptionPane.ERROR_MESSAGE);
                } else if (!emailTextField.getText().contains("@")) {
                    JOptionPane.showMessageDialog(null, "Error! Email needs an '@' symbol!",
                            "Tickets@Purdue Marketplace", JOptionPane.ERROR_MESSAGE);
                } else if (passwordTextField.getText().length() < 8 ||
                        !MarketplaceClient.isStrongAlphanumeric(passwordTextField.getText())) {
                    JOptionPane.showMessageDialog(null, "Error! " +
                                    "Please check your password is 8 or more\n" +
                                    "characters and has letters, a number, and special character.",
                            "Tickets@Purdue Marketplace", JOptionPane.ERROR_MESSAGE);
                } else if (!passwordTextField.getText().equals(reEnterPasswordTextField.getText())) {
                    JOptionPane.showMessageDialog(null, "Error! Passwords don't match!",
                            "Tickets@Purdue Marketplace", JOptionPane.ERROR_MESSAGE);
                } else {
                    String email = emailTextField.getText();
                    String password = passwordTextField.getText();

                    try {
                        client.createAccount(email, password, 2, type);

                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    } catch (ClassNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                    if (type.equals("b")) {
                        BuyerDashboardGUI buyerDashboardGUI = new BuyerDashboardGUI();
                        buyerDashboardGUI.setClient(client);
                        buyerDashboardGUI.run();
                        frame.setVisible(false);
                    } else {
                        SellerDashboardGUI sellerDashboardGUI = new SellerDashboardGUI();
                        sellerDashboardGUI.setClient(client);
                        sellerDashboardGUI.run();
                        frame.dispose();
                    }
                }
            }
        }
    };

    public CreateAccountGUI() {

    }

    public void run() {
        frame = new JFrame("Create New Tickets@Purdue Account");
        frame.setLayout(new GridBagLayout());

        JPanel panel1 = topPanel();
        JPanel panel2 = middlePanel();
        JPanel panel3 = bottomPanel();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 0.5;
        frame.add(panel1, gbc);

        gbc.gridy = 1;
        frame.add(panel2, gbc);

        gbc.gridy = 2;
        frame.add(panel3, gbc);

        frame.pack();
        frame.setSize(500, 300);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private JPanel topPanel() {
        JPanel topPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 10, 12, 10);
        gbc.anchor = GridBagConstraints.CENTER;

        welcomeMessage = new JLabel("Please enter the relevant information below.");
        topPanel.add(welcomeMessage, gbc);

        gbc.gridy++;
        gbc.gridwidth = 2;

        typeOfAccountMessage = new JLabel("What type of account do you want to create?");
        topPanel.add(typeOfAccountMessage, gbc);

        buyerChoice = new JRadioButton("Buyer");
        sellerChoice = new JRadioButton("Seller");

        ButtonGroup group = new ButtonGroup();
        group.add(buyerChoice);
        group.add(sellerChoice);

        gbc.gridy++;
        gbc.gridwidth = 1;
        topPanel.add(buyerChoice, gbc);

        gbc.gridx++;
        topPanel.add(sellerChoice, gbc);

        return topPanel;
    }

    private JPanel middlePanel() {
        JPanel middlePanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 10, 5, 10);

        gbc.gridy++;
        gbc.gridwidth = 1;
        emailLabel = new JLabel("Email:");
        middlePanel.add(emailLabel, gbc);

        gbc.gridx++;
        emailTextField = new JTextField(15);
        middlePanel.add(emailTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        passwordLabel = new JLabel("Password:");
        middlePanel.add(passwordLabel, gbc);

        gbc.gridx++;
        passwordTextField = new JTextField(15);
        middlePanel.add(passwordTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        reEnterPasswordLabel = new JLabel("Re-Enter Password:");
        middlePanel.add(reEnterPasswordLabel, gbc);

        gbc.gridx++;
        reEnterPasswordTextField = new JTextField(15);
        middlePanel.add(reEnterPasswordTextField, gbc);

        return middlePanel;
    }

    private JPanel bottomPanel() {
        JPanel bottomPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 10, 5, 10);

        createAccountButton = new JButton("Create New Account");
        createAccountButton.addActionListener(actionListenerBottom);
        bottomPanel.add(createAccountButton, gbc);

        return bottomPanel;
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new CreateAccountGUI());
    }
}
