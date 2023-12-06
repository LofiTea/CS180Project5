import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Homework/Project X: ClassName
 * <p>
 * Describe what the assignment is about.
 *
 * @author Henry J. Lee, Lab Section L20
 * @version Month XX, 2023
 */

public class SellerEditTicketGUI extends JComponent implements Runnable {
    JFrame frame;
    Sellers currentSeller;
    ArrayList<String> stores;
    JLabel storeLabel;
    JLabel sportLabel;
    JLabel locationLabel;
    JLabel sectionLabel;
    JLabel priceLabel;
    JLabel quantityLabel;
    JTextField storeTextField;
    JTextField sportTextField;
    JTextField locationTextField;
    JTextField sectionTextField;
    JTextField priceTextField;
    JTextField quantityTextField;
    JButton confirmButton;
    LoginInfo loginInfo;
    JButton returnToMenuButton;
    int num;
    int choice;
    int number;
    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            ArrayList<String> stores2 = currentSeller.retrieveStores();
            ArrayList<String> listings = currentSeller.retrieveListings(stores2.get(num));
            SellerListing listing = currentSeller.retrieveProducts(stores2.get(num));
            if (e.getSource() == confirmButton) {
                if (choice == 1) {
                    String sport = sportTextField.getText();
                    String location = locationTextField.getText();
                    String section = sectionTextField.getText();
                    double price = Double.parseDouble(priceTextField.getText());

                    if (sport == null || sport.isEmpty()) {
                        JOptionPane.showMessageDialog(frame, "Please enter a valid sport!",
                                "Invalid Sport", JOptionPane.ERROR_MESSAGE);
                    }
                    if (location == null || location.isEmpty()) {
                        JOptionPane.showMessageDialog(frame, "Please enter a valid " +
                                "location!", "Invalid Location", JOptionPane.ERROR_MESSAGE);
                    }
                    if (section == null || section.isEmpty()) {
                        JOptionPane.showMessageDialog(frame, "Please enter a valid " +
                                "section!", "Invalid Section", JOptionPane.ERROR_MESSAGE);
                    }
                    if (priceTextField.getText() == null || priceTextField.getText().isEmpty() || price <= 0) {
                        JOptionPane.showMessageDialog(frame, "Please enter a " +
                                "valid price!", "Invalid Price", JOptionPane.ERROR_MESSAGE);
                    }
                    currentSeller.modTicket(listing.getTickets().get(number - 1), new Ticket(sport,
                                    location, section, price), stores2.get(num - 1),
                            Integer.parseInt(listing.getQuantities().get(number - 1)));
                } else if (choice == 2) {
                    int quantity = Integer.parseInt(quantityTextField.getText());
                    if (quantityTextField.getText() == null || quantityTextField.getText().isEmpty() || quantity <= 0) {
                        JOptionPane.showMessageDialog(frame, "Please enter a " +
                                "valid quantity!", "Invalid Price", JOptionPane.ERROR_MESSAGE);
                    }
                    currentSeller.modTicket(listing.getTickets().get(number - 1),
                            listing.getTickets().get(number - 1),
                            stores2.get(num - 1), quantity);
                } else {
                    String sport = sportTextField.getText();
                    String location = locationTextField.getText();
                    String section = sectionTextField.getText();
                    double price = Double.parseDouble(priceTextField.getText());
                    int quantity = Integer.parseInt(quantityTextField.getText());
                    if (sport == null || sport.isEmpty()) {
                        JOptionPane.showMessageDialog(frame, "Please enter a valid sport!",
                                "Invalid Sport", JOptionPane.ERROR_MESSAGE);
                    }
                    if (location == null || location.isEmpty()) {
                        JOptionPane.showMessageDialog(frame, "Please enter a valid " +
                                "location!", "Invalid Location", JOptionPane.ERROR_MESSAGE);
                    }
                    if (section == null || section.isEmpty()) {
                        JOptionPane.showMessageDialog(frame, "Please enter a valid " +
                                "section!", "Invalid Section", JOptionPane.ERROR_MESSAGE);
                    }
                    if (priceTextField.getText() == null || priceTextField.getText().isEmpty() || price <= 0) {
                        JOptionPane.showMessageDialog(frame, "Please enter a " +
                                "valid price!", "Invalid Price", JOptionPane.ERROR_MESSAGE);
                    }
                    if (quantityTextField.getText() == null || quantityTextField.getText().isEmpty() || quantity <= 0) {
                        JOptionPane.showMessageDialog(frame, "Please enter a " +
                                "valid quantity!", "Invalid Price", JOptionPane.ERROR_MESSAGE);
                    }

                    currentSeller.modTicket(listing.getTickets().get(number - 1), new Ticket(sport,
                            location, section, price), stores2.get(num - 1), quantity);
                }
            }

            if (e.getSource() == returnToMenuButton) {
                if (e.getSource() == returnToMenuButton) {
                    SellTicketMenuGUI sellTicketMenuGUI = new SellTicketMenuGUI();
                    sellTicketMenuGUI.setLoginInfo(loginInfo);
                    //sellTicketMenuGUI.setCurrentSeller(currentSeller);
                    //sellTicketMenuGUI.setStores(stores);
                    sellTicketMenuGUI.run();
                    frame.dispose();
                }
            }
        }
    };

    public SellerEditTicketGUI() {

    }

    public void run() {
        ArrayList<String> stores2 = currentSeller.retrieveStores();
        if (stores2 == null) {
            JOptionPane.showMessageDialog(null, "Error! There are no stores!",
                    "No Stores", JOptionPane.ERROR_MESSAGE);
        } else {
            String[] options1 = new String[stores2.size()];
            for (int i = 0; i < stores2.size(); i++) {
                options1[i] = i + 1 + ". " + stores2.get(i);
            }
            String decision = (String) JOptionPane.showInputDialog(frame,
                    "Which store would you like to edit a ticket in?", "Edit Ticket",
                    JOptionPane.PLAIN_MESSAGE, null, options1, options1[0]);
            num = Arrays.asList(options1).indexOf(decision);
            if (num < 0|| num > stores2.size()) {
                JOptionPane.showMessageDialog(frame, "Please enter a valid store!",
                        "Invalid Store", JOptionPane.ERROR_MESSAGE);
            } else {
                ArrayList<String> listings = currentSeller.retrieveListings(stores2.get(num));
                SellerListing listing = currentSeller.retrieveProducts(stores2.get(num));
                if (!(listings == null || listing == null)) {
                    String[] options2 = new String[listings.size()];
                    for (int i = 0; i < listings.size(); i++) {
                        options2[i] = listings.get(i);
                    }
                    String ticket = (String) JOptionPane.showInputDialog(frame,
                            "Which ticket would you like to edit?", "Edit Ticket",
                            JOptionPane.PLAIN_MESSAGE, null, options2, options2[0]);
                    number = Arrays.asList(options2).indexOf(ticket);
                    if (number < 0 || number > listings.size()) {
                        JOptionPane.showMessageDialog(frame, "Please enter a valid ticket!",
                                "Invalid Ticket", JOptionPane.ERROR_MESSAGE);
                    } else {
                        String[] options3 = {"1. Ticket Info\n", "2. Quantity\n", "3. Both"};
                        String str = (String) JOptionPane.showInputDialog(frame,
                                "What would you like to change?", "Edit Ticket",
                                JOptionPane.PLAIN_MESSAGE, null, options3, options3[0]);
                        if (str == null) {
                            return;
                        }
                        int hahaha = Arrays.asList(options3).indexOf(str);
                        switch (hahaha) {
                            case 0:
                                choice = 1;
                                frame = new JFrame("Edit A Ticket");
                                frame.setLayout(new GridBagLayout());

                                JPanel panel = new JPanel(new GridBagLayout());
                                panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                                frame.add(panel);

                                GridBagConstraints gbc = new GridBagConstraints();
                                gbc.gridx = 0;
                                gbc.gridy = 0;
                                gbc.insets = new Insets(5, 10, 5, 10);
                                gbc.anchor = GridBagConstraints.CENTER;

                                sportLabel = new JLabel("Sport: ");
                                panel.add(sportLabel, gbc);

                                gbc.gridx++;
                                sportTextField = new JTextField(15);
                                panel.add(sportTextField, gbc);

                                gbc.gridx = 0;
                                gbc.gridy++;
                                locationLabel = new JLabel("Location: ");
                                panel.add(locationLabel, gbc);

                                gbc.gridx++;
                                locationTextField = new JTextField(15);
                                panel.add(locationTextField, gbc);

                                gbc.gridx = 0;
                                gbc.gridy++;
                                sectionLabel = new JLabel("Section: ");
                                panel.add(sectionLabel, gbc);

                                gbc.gridx++;
                                sectionTextField = new JTextField(15);
                                panel.add(sectionTextField, gbc);

                                gbc.gridx = 0;
                                gbc.gridy++;
                                priceLabel = new JLabel("Price: ");
                                panel.add(priceLabel, gbc);

                                gbc.gridx++;
                                priceTextField = new JTextField(15);
                                panel.add(priceTextField, gbc);

                                gbc.gridx = 0;
                                gbc.gridy++;

                                confirmButton = new JButton("Edit Ticket");
                                confirmButton.addActionListener(actionListener);
                                gbc.anchor = GridBagConstraints.CENTER;
                                gbc.gridwidth = 2;
                                panel.add(confirmButton, gbc);

                                GridBagConstraints gbc2 = new GridBagConstraints();
                                gbc2.gridx = 0;
                                gbc2.gridy = gbc.gridy + 1;
                                gbc2.insets = new Insets(50, 10, 10, 10);
                                gbc2.anchor = GridBagConstraints.SOUTH;

                                returnToMenuButton = new JButton("Return to Sell Ticket Menu");
                                returnToMenuButton.addActionListener(actionListener);
                                returnToMenuButton.setPreferredSize(new Dimension(200, 50));
                                gbc2.anchor = GridBagConstraints.CENTER;
                                gbc2.gridwidth = 2;
                                panel.add(returnToMenuButton, gbc2);

                                int verticalCenter = (Toolkit.getDefaultToolkit().getScreenSize().height -
                                        frame.getHeight()) / 2;
                                frame.setLocationRelativeTo(null);
                                frame.setLocation(frame.getLocation().x, verticalCenter);

                                frame.setSize(500, 500);
                                frame.setLocationRelativeTo(null);
                                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                                frame.setVisible(true);
                                break;
                            case 1:
                                choice = 2;
                                frame = new JFrame("Edit A Ticket");
                                frame.setLayout(new GridBagLayout());

                                panel = new JPanel(new GridBagLayout());
                                panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                                frame.add(panel);

                                GridBagConstraints gbc1 = new GridBagConstraints();
                                gbc1.gridx = 0;
                                gbc1.gridy = 0;
                                gbc1.insets = new Insets(5, 10, 5, 10);
                                gbc1.anchor = GridBagConstraints.CENTER;

                                gbc1.gridx = 0;
                                gbc1.gridy++;
                                quantityLabel = new JLabel("Quantity: ");
                                panel.add(quantityLabel, gbc1);

                                gbc1.gridx++;
                                quantityTextField = new JTextField(15);
                                panel.add(quantityTextField, gbc1);

                                confirmButton = new JButton("Edit Ticket");
                                confirmButton.addActionListener(actionListener);
                                gbc1.anchor = GridBagConstraints.CENTER;
                                gbc1.gridwidth = 2;
                                panel.add(confirmButton, gbc1);

                                GridBagConstraints gbc3 = new GridBagConstraints();
                                gbc3.gridx = 0;
                                gbc3.gridy = gbc1.gridy + 1;
                                gbc3.insets = new Insets(50, 10, 10, 10);
                                gbc3.anchor = GridBagConstraints.SOUTH;

                                returnToMenuButton = new JButton("Return to Sell Ticket Menu");
                                returnToMenuButton.addActionListener(actionListener);
                                returnToMenuButton.setPreferredSize(new Dimension(200, 50));
                                gbc3.anchor = GridBagConstraints.CENTER;
                                gbc3.gridwidth = 2;
                                panel.add(returnToMenuButton, gbc3);

                                int verticalCenter1 = (Toolkit.getDefaultToolkit().getScreenSize().height -
                                        frame.getHeight()) / 2;
                                frame.setLocationRelativeTo(null);
                                frame.setLocation(frame.getLocation().x, verticalCenter1);

                                frame.setSize(500, 500);
                                frame.setLocationRelativeTo(null);
                                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                                frame.setVisible(true);
                                break;
                            case 2:
                                choice = 3;
                                frame = new JFrame("Edit A Ticket");
                                frame.setLayout(new GridBagLayout());

                                panel = new JPanel(new GridBagLayout());
                                panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                                frame.add(panel);

                                GridBagConstraints gbc4 = new GridBagConstraints();
                                gbc4.gridx = 0;
                                gbc4.gridy = 0;
                                gbc4.insets = new Insets(5, 10, 5, 10);
                                gbc4.anchor = GridBagConstraints.CENTER;

                                sportLabel = new JLabel("Sport: ");
                                panel.add(sportLabel, gbc4);

                                gbc4.gridx++;
                                sportTextField = new JTextField(15);
                                panel.add(sportTextField, gbc4);

                                gbc4.gridx = 0;
                                gbc4.gridy++;
                                locationLabel = new JLabel("Location: ");
                                panel.add(locationLabel, gbc4);

                                gbc4.gridx++;
                                locationTextField = new JTextField(15);
                                panel.add(locationTextField, gbc4);

                                gbc4.gridx = 0;
                                gbc4.gridy++;
                                sectionLabel = new JLabel("Section: ");
                                panel.add(sectionLabel, gbc4);

                                gbc4.gridx++;
                                sectionTextField = new JTextField(15);
                                panel.add(sectionTextField, gbc4);

                                gbc4.gridx = 0;
                                gbc4.gridy++;
                                priceLabel = new JLabel("Price: ");
                                panel.add(priceLabel, gbc4);

                                gbc4.gridx++;
                                priceTextField = new JTextField(15);
                                panel.add(priceTextField, gbc4);

                                gbc4.gridx = 0;
                                gbc4.gridy++;
                                quantityLabel = new JLabel("Quantity: ");
                                panel.add(quantityLabel, gbc4);

                                gbc4.gridx++;
                                quantityTextField = new JTextField(15);
                                panel.add(quantityTextField, gbc4);

                                gbc4.gridx = 0;
                                gbc4.gridy++;
                                confirmButton = new JButton("Edit Ticket");
                                confirmButton.addActionListener(actionListener);
                                gbc4.anchor = GridBagConstraints.CENTER;
                                gbc4.gridwidth = 2;
                                panel.add(confirmButton, gbc4);

                                GridBagConstraints gbc5 = new GridBagConstraints();
                                gbc5.gridx = 0;
                                gbc5.gridy = gbc4.gridy + 1;
                                gbc5.insets = new Insets(50, 10, 10, 10);
                                gbc5.anchor = GridBagConstraints.CENTER;

                                returnToMenuButton = new JButton("Return to Sell Ticket Menu");
                                returnToMenuButton.addActionListener(actionListener);
                                returnToMenuButton.setPreferredSize(new Dimension(200, 50));
                                gbc5.anchor = GridBagConstraints.CENTER;
                                gbc5.gridwidth = 2;
                                panel.add(returnToMenuButton, gbc5);

                                frame.setSize(500, 500);
                                frame.setLocationRelativeTo(null);
                                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                                frame.setVisible(true);
                                break;
                        }
                    }
                }
            }
        }
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
        SwingUtilities.invokeLater(new SellerEditTicketGUI());
    }
}
