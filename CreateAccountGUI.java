import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Homework/Project X: ClassName
 * <p>
 * Describe what the assignment is about.
 *
 * @author Henry J. Lee, Lab Section L20
 * @version Month XX, 2023
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
            if (buyerChoice.isSelected()) {
                type = "b";
            }
            if (sellerChoice.isSelected()) {
                type = "s";
            }
            if (e.getSource() == createAccountButton) {
                if (!buyerChoice.isSelected() && !sellerChoice.isSelected()) {
                    JOptionPane.showMessageDialog(null, "Error! Please selection an account type!",
                            "Tickets@Purdue Marketplace", JOptionPane.ERROR_MESSAGE);
                } else if (!emailTextField.getText().contains("@")) {
                    JOptionPane.showMessageDialog(null, "Error! Email needs an '@' symbol!",
                            "Tickets@Purdue Marketplace", JOptionPane.ERROR_MESSAGE);
                } else if (passwordTextField.getText().length() < 8 ||
                        !Marketplace.isStrongAlphanumeric(passwordTextField.getText())) {
                    JOptionPane.showMessageDialog(null, "Error! " +
                                    "Please check your password is 8 or more\n" +
                                    "characters and has letters, a number, and special character.",
                            "Tickets@Purdue Marketplace", JOptionPane.ERROR_MESSAGE);
                } else if (!passwordTextField.getText().equals(reEnterPasswordTextField.getText())) {
                    JOptionPane.showMessageDialog(null, "Error! Passwords don't match!",
                            "Tickets@Purdue Marketplace", JOptionPane.ERROR_MESSAGE);
                } else {
                    ArrayList<String> fileInfo = Marketplace.readFile("LoginInfo.txt");
                    ArrayList<String> userInfo = new ArrayList<>();
                    LoginInfo loginInfo = new LoginInfo(emailTextField.getText(), passwordTextField.getText());
                    AccountGUI accountGUI = new AccountGUI(loginInfo);
                    String filename;
                    String role;
                    int id;
                    if (type.equals("b")) {
                        role = "b";
                        filename = "BuyerInfo.txt";
                    } else {
                        role = "s";
                        filename = "SellerInfo.txt";
                    }
                    userInfo = Marketplace.readFile(filename);
                    if (fileInfo.isEmpty()) {
                        fileInfo.add(String.format("1,%s,%s,%s", emailTextField.getText(), passwordTextField.getText(),
                                role));
                        userInfo.add(String.format("1,%s", emailTextField.getText()));
                        id = 1;
                    } else {
                        String[] lastLine = fileInfo.get(fileInfo.size() - 1).split(",");

                        fileInfo.add(String.format("%d,%s,%s,%s", Integer.parseInt(lastLine[0]) + 1,
                                emailTextField.getText(), passwordTextField.getText(), role));
                        id = Integer.parseInt(lastLine[0]) + 1;
                        userInfo.add(String.format("%d,%s", Integer.parseInt(lastLine[0]) + 1, emailTextField.getText()));
                    }
                    Marketplace.writeFile(userInfo, filename);
                    Marketplace.writeFile(fileInfo, "LoginInfo.txt");

                    String filename2 = "";
                    if (role.equals("b")) {
                        filename2 = "BuyerHistory.txt";
                        Marketplace.writeFile(userInfo, filename2);
                    }

                    if (role.equals("b")) {
                        BuyerDashboardGUI buyerDashboardGUI = new BuyerDashboardGUI(loginInfo);
                        buyerDashboardGUI.run();
                        frame.setVisible(false);
                    } else {
                        SellerDashboardGUI sellerDashboardGUI = new SellerDashboardGUI(loginInfo);
                        sellerDashboardGUI.run();
                        frame.setVisible(false);
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
