import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Project 5: SellTicketMenuGUI
 * 
 * Utilizes GUI to allow a seller to sell a ticket, edit a ticket, and more.
 *
 * @author Henry J. Lee, Lab Section L20
 * @version November 13, 2023
 */

public class SellTicketMenuGUI extends JComponent implements Runnable {
    JFrame frame;
    JLabel welcomeMessage;
    JButton sellTicketButton;
    JButton editTicketButton;
    JButton removeTicketButton;
    JButton returnToMenuButton;
    ArrayList<String> stores;
    MarketplaceClient client;
    int num;
    int number;
    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
//            currentSeller = new Sellers(determineID(loginInfo.getEmail(), loginInfo.getPassword()));
//            stores = currentSeller.retrieveStores();
            if (e.getSource() == sellTicketButton) {
                client.sendInt(1);
                SellTicketGUI sellTicketGUI = new SellTicketGUI();
//                sellTicketGUI.setLoginInfo(loginInfo);
//                sellTicketGUI.setCurrentSeller(currentSeller);
//                sellTicketGUI.setStores(stores);
                sellTicketGUI.setClient(client);
                sellTicketGUI.run();
                frame.dispose();
            }
            if (e.getSource() == editTicketButton) {
                client.sendInt(2);
                SellerEditTicketGUI sellerEditTicketGUI = new SellerEditTicketGUI();
                sellerEditTicketGUI.setClient(client);
                sellerEditTicketGUI.run();
                frame.dispose();
            }
            if (e.getSource() == removeTicketButton) {
                client.sendInt(3);
                ArrayList<String> stores3 = client.recieveStringArrayList();
                if (stores3 == null) {
                    JOptionPane.showMessageDialog(null, "Error! There are no stores!",
                            "No Stores", JOptionPane.ERROR_MESSAGE);
                } else {
                    String[] options1 = new String[stores3.size()];
                    for (int i = 0; i < stores3.size(); i++) {
                        options1[i] = i + 1 + ". " + stores3.get(i);
                    }
                    String decision = (String) JOptionPane.showInputDialog(frame,
                            "Which store would you like to remove a ticket in?", "Remove Ticket",
                            JOptionPane.PLAIN_MESSAGE, null, options1, options1[0]);
                    client.sendString(decision);

                    if(decision != null) {
                        num = Arrays.asList(options1).indexOf(decision);
                        client.sendString(stores3.get(num));

                        ArrayList<String> listings = client.recieveStringArrayList();

                        if(listings != null) {
                            String[] options2 = new String[listings.size()];
                            for (int i = 0; i < listings.size(); i++) {
                                options2[i] = "<html>" + listings.get(i).replace("\n", "<br>") + "</html>";
                            }
                            String ticket = (String) JOptionPane.showInputDialog(frame,
                                    "Which ticket would you like to delete?", "Delete Ticket",
                                    JOptionPane.PLAIN_MESSAGE, null, options2, options2[0]);
                            client.sendString(ticket);

                            if(ticket != null) {
                                number = Arrays.asList(options2).indexOf(ticket);
                                client.sendInt(number);
                                boolean success = client.receiveBoolean();

                                if(success) {
                                    JOptionPane.showMessageDialog(frame, "Ticket deleted successfully! "
                                            , "Edit Successful", JOptionPane.INFORMATION_MESSAGE);
                                } else {
                                    JOptionPane.showMessageDialog(frame, "Ticket deletion unsuccessful! "
                                            , "Edit Unsuccessful", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Error! There are no tickets!",
                                    "No Tickets", JOptionPane.ERROR_MESSAGE);
                        }
                        //SellerListing listing = currentSeller.retrieveProducts(stores3.get(num));


                    }

                }
            }
            if (e.getSource() == returnToMenuButton) {
                client.sendInt(4);
                SellerDashboardGUI sellerDashboardGUI = new SellerDashboardGUI();
                //sellerDashboardGUI.setLoginInfo(loginInfo);
                sellerDashboardGUI.setClient(client);
                sellerDashboardGUI.run();
                frame.dispose();
            }
        }
    };

    public SellTicketMenuGUI() {

    }

    public void run() {
        frame = new JFrame("Sell A Ticket");
        frame.setLayout(new GridBagLayout());

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.CENTER;

        welcomeMessage = new JLabel("What would you like to do?");
        panel.add(welcomeMessage, gbc);

        JButton sampleButton = new JButton();
        sampleButton.setPreferredSize(new Dimension(200, 50));

        gbc.gridy++;
        sellTicketButton = new JButton("Sell A Ticket");
        sellTicketButton.addActionListener(actionListener);
        sellTicketButton.setPreferredSize(sampleButton.getPreferredSize());
        panel.add(sellTicketButton, gbc);

        gbc.gridy++;
        editTicketButton = new JButton("Edit A Ticket");
        editTicketButton.addActionListener(actionListener);
        editTicketButton.setPreferredSize(sampleButton.getPreferredSize());
        panel.add(editTicketButton, gbc);

        gbc.gridy++;
        removeTicketButton = new JButton("Remove A Ticket");
        removeTicketButton.addActionListener(actionListener);
        removeTicketButton.setPreferredSize(sampleButton.getPreferredSize());
        panel.add(removeTicketButton, gbc);

        gbc.gridy++;
        returnToMenuButton = new JButton("Return to Menu");
        returnToMenuButton.addActionListener(actionListener);
        returnToMenuButton.setPreferredSize(sampleButton.getPreferredSize());
        panel.add(returnToMenuButton, gbc);

        frame.add(panel);
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
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

    synchronized public void setClient(MarketplaceClient client) {
        this.client = client;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new SellTicketMenuGUI());
    }
}
