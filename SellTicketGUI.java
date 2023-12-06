import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
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

public class SellTicketGUI extends JComponent implements Runnable {
    JFrame frame;
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
    JButton returnToMenuButton;
    LoginInfo loginInfo;
    Sellers currentSeller;
    ArrayList<String> stores;

    MarketplaceClient client;
    int num;

    int choiceIdx;

    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == confirmButton) {
                if(choiceIdx == 0) {
                    client.sendBoolean(true);
                    String store = storeTextField.getText();
                    client.sendString(store);
                    if (store == null) {
                        SellerDashboardGUI sellerDashboardGUI = new SellerDashboardGUI();
                        sellerDashboardGUI.setClient(client);
                        sellerDashboardGUI.run();
                        return;
                    }

                    if (store.isEmpty() || store.equals("\n")) {
                        JOptionPane.showMessageDialog(null, "Please enter a valid store name!",
                                "Invalid Store Name", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    client.sendBoolean(true);
                    boolean invalidSport = false;
                    boolean invalidLocation = false;
                    boolean invalidSection = false;
                    boolean invalidPrice = false;
                    boolean invalidQty = false;
                    String sport = sportTextField.getText();
                    String location = locationTextField.getText();
                    String section = sectionTextField.getText();
                    String price = priceTextField.getText();
                    String quantity = quantityTextField.getText();

                    if(sport == null || location == null || section == null || price == null || quantity == null) {
                        client.sendBoolean(false);
                        SellerDashboardGUI sellerDashboardGUI = new SellerDashboardGUI();
                        sellerDashboardGUI.setClient(client);
                        sellerDashboardGUI.run();
                        return;
                    }

                    client.sendBoolean(true);

                    if (sport.isEmpty()) {
                        JOptionPane.showMessageDialog(frame, "Please enter a valid sport!",
                                "Invalid Sport", JOptionPane.ERROR_MESSAGE);
                        invalidSport = true;
                    }
                    if (location.isEmpty()) {
                        JOptionPane.showMessageDialog(frame, "Please enter a valid " +
                                "location!", "Invalid Location", JOptionPane.ERROR_MESSAGE);
                        invalidLocation = true;
                    }
                    if (section.isEmpty()) {
                        JOptionPane.showMessageDialog(frame, "Please enter a valid " +
                                "section!", "Invalid Section", JOptionPane.ERROR_MESSAGE);
                        invalidSection = true;
                    }

                    try {
                        double priceNum = Double.parseDouble(price);
                        if(price.isEmpty() || priceNum <= 0) {
                            JOptionPane.showMessageDialog(frame, "Please enter a " +
                                    "valid price!", "Invalid Price", JOptionPane.ERROR_MESSAGE);
                            invalidPrice = true;
                        } else {
                            invalidPrice = false;
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(frame, "Please enter a " +
                                "valid price!", "Invalid Price", JOptionPane.ERROR_MESSAGE);
                        invalidPrice = true;
                    }


                    try {
                        int quantityNum = Integer.parseInt(quantity);
                        if(quantity.isEmpty() || quantityNum <= 0) {
                            JOptionPane.showMessageDialog(frame, "Please enter a " +
                                    "valid quantity!", "Invalid Price", JOptionPane.ERROR_MESSAGE);
                            invalidQty = true;
                        } else {
                            invalidQty = false;
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(frame, "Please enter a " +
                                "valid quantity!", "Invalid Price", JOptionPane.ERROR_MESSAGE);
                        invalidQty = true;
                    }

                    boolean invalid = false;

                    if(!invalidSport && !invalidLocation && !invalidSection && !invalidPrice && !invalidQty) {
                        client.sendBoolean(invalid);
                        client.sendTicketInfo(sport, location, section, Double.parseDouble(price),
                                Integer.parseInt(quantity), stores.get(num));
                        sportTextField.setText("");
                        locationTextField.setText("");
                        sectionTextField.setText("");
                        priceTextField.setText("");
                        quantityTextField.setText("");
                    } else {
                        invalid = true;
                        client.sendBoolean(invalid);
                    }



//                    currentSeller.addTickets(new Ticket(sport, location, section, price), quantity,
//                            stores.get(num));
                }
            }

            if (e.getSource() == returnToMenuButton) {
                client.sendBoolean(false);
            
                SellTicketMenuGUI sellTicketMenuGUI = new SellTicketMenuGUI();
                //sellTicketMenuGUI.setLoginInfo(loginInfo);
                sellTicketMenuGUI.setCurrentSeller(currentSeller);
                sellTicketMenuGUI.setStores(stores);
                sellTicketMenuGUI.run();
                frame.dispose();
            
            }
        }
    };
    public SellTicketGUI() {

    }

    public void run() {
        stores = client.recieveStringArrayList();
        if (stores == null) {
            int num = JOptionPane.showConfirmDialog(null, "You have no stores currently. " +
                    "Would you like to add a store?"
                    + " your account?", "Add Store?", JOptionPane.YES_NO_OPTION);
            client.sendInt(num);
            if (num == JOptionPane.YES_OPTION) {
                boolean invalidName = false;
                String name = "";
                do {
                    name = JOptionPane.showInputDialog(null, "Enter the name of your " +
                            "store.", "New Store", JOptionPane.QUESTION_MESSAGE);
                    if (name != null) {
                        if (name.isEmpty() || name.equals("\n")) {
                            JOptionPane.showMessageDialog(null, "Please enter a valid store name!",
                                    "Invalid Store Name", JOptionPane.ERROR_MESSAGE);
                            invalidName = true;
                        } else {
                            invalidName = false;
                        }
                    } else {
                        invalidName = false;
                    }
                } while (invalidName);


                client.sendString(name);

                SellerDashboardGUI sellerDashboardGUI = new SellerDashboardGUI();
                sellerDashboardGUI.setClient(client);
                sellerDashboardGUI.run();
            } else {
                SellerDashboardGUI sellerDashboardGUI = new SellerDashboardGUI();
                sellerDashboardGUI.setClient(client);
                sellerDashboardGUI.run();
            }
        } else {
            String[] options1 = {"1. Add Store\n", "2. Add Ticket"};
            String ticket = (String) JOptionPane.showInputDialog(frame,
                    "What would you like to do?", "Create Store",
                    JOptionPane.PLAIN_MESSAGE, null, options1, options1[0]);
            client.sendString(ticket);
            if (ticket == null) {
                SellerDashboardGUI sellerDashboardGUI = new SellerDashboardGUI();
                sellerDashboardGUI.setClient(client);
                sellerDashboardGUI.run();
                return;
            }
            choiceIdx = Arrays.asList(options1).indexOf(ticket);
            client.sendInt(choiceIdx);

            if (choiceIdx == 0) {
                frame = new JFrame("Sell A Ticket");
                frame.setLayout(new GridBagLayout());

                JPanel panel = new JPanel(new GridBagLayout());
                panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

                GridBagConstraints gbc1 = new GridBagConstraints();
                gbc1.gridx = 0;
                gbc1.gridy = 0;
                gbc1.insets = new Insets(5, 10, 5, 10);
                gbc1.anchor = GridBagConstraints.CENTER;

                storeLabel = new JLabel("Store:");
                panel.add(storeLabel, gbc1);

                gbc1.gridx++;
                storeTextField = new JTextField(15);
                panel.add(storeTextField, gbc1);

                gbc1.gridx = 0;
                gbc1.gridy++;
                confirmButton = new JButton("Create Store");
                confirmButton.addActionListener(actionListener);
                gbc1.anchor = GridBagConstraints.CENTER;
                gbc1.gridwidth = 2;
                panel.add(confirmButton, gbc1);

                panel.add(confirmButton, gbc1);

                GridBagConstraints gbc2 = new GridBagConstraints();
                gbc2.gridx = 0;
                gbc2.gridy = gbc1.gridy + 1;
                gbc2.insets = new Insets(50, 10, 10, 10);
                gbc2.anchor = GridBagConstraints.CENTER;

                returnToMenuButton = new JButton("Return to Sell Ticket Menu");
                returnToMenuButton.addActionListener(actionListener);
                returnToMenuButton.setPreferredSize(new Dimension(200, 50));
                gbc2.anchor = GridBagConstraints.CENTER;
                gbc2.gridwidth = 2;
                panel.add(returnToMenuButton, gbc2);
                panel.add(returnToMenuButton, gbc2);

                frame.add(panel);
                frame.setSize(500, 500);
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            } else {
                String[] options2 = new String[stores.size()];
                for (int i = 0; i < stores.size(); i++) {
                    options2[i] = i + 1 + ". " + stores.get(i);
                }
                String decision = (String) JOptionPane.showInputDialog(frame,
                        "Which store would you like to add your ticket to?", "Create Store",
                        JOptionPane.PLAIN_MESSAGE, null, options2, options2[0]);

                client.sendString(decision);

                if (decision == null) {
                    SellerDashboardGUI sellerDashboardGUI = new SellerDashboardGUI();
                    sellerDashboardGUI.setClient(client);
                    sellerDashboardGUI.run();
                    return;
                }

                num = Arrays.asList(options2).indexOf(decision);

//                if (num < 0 || num > stores.size()-1) {
//                    JOptionPane.showMessageDialog(frame, "Please enter a valid store!",
//                            "Invalid Store", JOptionPane.ERROR_MESSAGE);
//                } else {
                    frame = new JFrame("Sell A Ticket");
                    frame.setLayout(new GridBagLayout());

                    JPanel panel = new JPanel(new GridBagLayout());
                    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

                    GridBagConstraints gbc = new GridBagConstraints();
                    gbc.gridx = 0;
                    gbc.gridy = 0;
                    gbc.insets = new Insets(5, 10, 5, 10);
                    gbc.anchor = GridBagConstraints.CENTER;
                    gbc.gridy++;
                    gbc.gridwidth = 1;

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
                    quantityLabel = new JLabel("Quantity: ");
                    panel.add(quantityLabel, gbc);

                    gbc.gridx++;
                    quantityTextField = new JTextField(15);
                    panel.add(quantityTextField, gbc);

                    gbc.gridx = 0;
                    gbc.gridy++;
                    confirmButton = new JButton("Create Ticket");
                    confirmButton.addActionListener(actionListener);
                    gbc.anchor = GridBagConstraints.CENTER;
                    gbc.gridwidth = 2;
                    panel.add(confirmButton, gbc);

                    GridBagConstraints gbc2 = new GridBagConstraints();
                    gbc2.gridx = 0;
                    gbc2.gridy = gbc.gridy + 1;
                    gbc2.insets = new Insets(50, 10, 10, 10);
                    gbc2.anchor = GridBagConstraints.CENTER;

                    returnToMenuButton = new JButton("Return to Sell Ticket Menu");
                    returnToMenuButton.addActionListener(actionListener);
                    returnToMenuButton.setPreferredSize(new Dimension(200, 50));
                    gbc2.anchor = GridBagConstraints.CENTER;
                    gbc2.gridwidth = 2;
                    panel.add(returnToMenuButton, gbc2);

                    frame.add(panel);
                    frame.setSize(500, 500);
                    frame.setLocationRelativeTo(null);
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.setVisible(true);

                //}
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

    synchronized public void setClient(MarketplaceClient client) {
        this.client = client;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new SellTicketGUI());
    }
}
