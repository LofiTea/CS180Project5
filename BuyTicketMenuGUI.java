import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Project 5: BuyTicketMenuGUI
 * 
 * Utilizes GUI to allow a buyer to buy a ticket, view their shopping cart, and more.
 *
 * @author Henry J. Lee, Lab Section L20
 * @version November 13, 2023
 */

public class BuyTicketMenuGUI extends JComponent implements Runnable {
    JFrame frame;
    JLabel welcomeMessage;
    JButton buyTicketButton;
    JButton viewShoppingCartButton;
    JButton removeFromShoppingCartButton;
    JButton checkOutButton;
    JButton returnToMenuButton;
    MarketplaceClient client;
    ArrayList<String> realShoppingCart;
    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == buyTicketButton) {
                client.sendInt(1);
               // shoppingCart = currentBuyer.retrieveShoppingCart();
                BuyTicketGUI buyTicketGUI = new BuyTicketGUI();
                buyTicketGUI.setClient(client);
                //buyTicketGUI.setLoginInfo(loginInfo);
                //buyTicketGUI.setCurrentBuyer(currentBuyer);
               // buyTicketGUI.setShoppingCart(shoppingCart);
               // buyTicketGUI.setPreviousShoppingCart(previousShoppingCart);
                buyTicketGUI.run();
                frame.dispose();
            }
            if (e.getSource() == viewShoppingCartButton) {
                client.sendInt(2);
                realShoppingCart = client.recieveStringArrayList();
                if (realShoppingCart == null || realShoppingCart.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Error! Cannot view shopping cart" +
                            " because it is empty!", "No Items!", JOptionPane.ERROR_MESSAGE);
                } else {
                    String[] options = new String[realShoppingCart.size()];
                            for (int i = 0; i < options.length; i++) {
                                String curTicket = realShoppingCart.get(i);
                                curTicket = curTicket.replace(";","<br>");
                                curTicket = curTicket.replace("&","<br>");
                                options[i] = "<html>" + curTicket + "</html>";
                        }
                    JOptionPane.showInputDialog(frame,
                        "Current Shopping Cart", "Current Shopping Cart",
                        JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
                        
                    
                }
            }
            if (e.getSource() == removeFromShoppingCartButton) {
                client.sendInt(3);
                realShoppingCart = client.recieveStringArrayList();
                if (realShoppingCart == null || realShoppingCart.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Error! Shopping cart is empty!",
                            "Empty Shopping Cart", JOptionPane.ERROR_MESSAGE);
                } else {
                    RemoveTicketGUI removeTicketGUI = new RemoveTicketGUI();
                    removeTicketGUI.setClient(client);
                    removeTicketGUI.setRealShoppingCart(realShoppingCart);
                    removeTicketGUI.run();
                    frame.dispose();
                }
            }

            if (e.getSource() == checkOutButton) {
                client.sendInt(4);
                realShoppingCart = client.recieveStringArrayList();
                if (realShoppingCart == null || realShoppingCart.isEmpty()) {
                    client.sendBoolean(false);
                    JOptionPane.showMessageDialog(null, "Error! Shopping cart is empty!",
                            "Empty Shopping Cart", JOptionPane.ERROR_MESSAGE);
                } else {
                    int num = JOptionPane.showConfirmDialog(null, "Are you sure you want " +
                            "to checkout?", "Checkout", JOptionPane.YES_NO_OPTION);
                    if (num == JOptionPane.YES_OPTION) {
                       client.sendBoolean(true);
                       boolean succesfulCheckout = client.receiveBoolean();
                       if(succesfulCheckout)
                       {
                         JOptionPane.showMessageDialog(null, "Checkout Succesful!",
                                "Checkout Status", JOptionPane.INFORMATION_MESSAGE);
                       }
                       else{
                         JOptionPane.showMessageDialog(null, "Checkout Failed!",
                            "Checkout Status", JOptionPane.ERROR_MESSAGE);
                       }
                    }
                    else{
                        client.sendBoolean(false);
                    }
                }
            }
            if (e.getSource() == returnToMenuButton) {
                client.sendInt(5);
                BuyerDashboardGUI buyerDashboardGUI = new BuyerDashboardGUI();
//                buyerDashboardGUI.setLoginInfo(loginInfo);
//                buyerDashboardGUI.setCurrentBuyer(currentBuyer);
//                buyerDashboardGUI.setShoppingCart(shoppingCart);
//                buyerDashboardGUI.setPreviousShoppingCart(previousShoppingCart);
               buyerDashboardGUI.setClient(client);
                buyerDashboardGUI.run();
                frame.dispose();
            }
        }
    };

    public BuyTicketMenuGUI() {

    }

    public void run() {
        frame = new JFrame("Buy A Ticket");
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
        buyTicketButton = new JButton("Buy A Ticket");
        buyTicketButton.addActionListener(actionListener);
        buyTicketButton.setPreferredSize(sampleButton.getPreferredSize());
        panel.add(buyTicketButton, gbc);

        gbc.gridy++;
        viewShoppingCartButton = new JButton("View Shopping Cart");
        viewShoppingCartButton.addActionListener(actionListener);
        viewShoppingCartButton.setPreferredSize(sampleButton.getPreferredSize());
        panel.add(viewShoppingCartButton, gbc);

        gbc.gridy++;
        removeFromShoppingCartButton = new JButton("Remove From Shopping Cart");
        removeFromShoppingCartButton.addActionListener(actionListener);
        removeFromShoppingCartButton.setPreferredSize(sampleButton.getPreferredSize());
        panel.add(removeFromShoppingCartButton, gbc);

        gbc.gridy++;
        checkOutButton = new JButton("Checkout");
        checkOutButton.addActionListener(actionListener);
        checkOutButton.setPreferredSize(sampleButton.getPreferredSize());
        panel.add(checkOutButton, gbc);

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

  
    public void setClient(MarketplaceClient client)
    {
        this.client = client;
    }

    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new BuyTicketMenuGUI());
    }
}
