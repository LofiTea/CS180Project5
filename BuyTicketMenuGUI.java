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
    LoginInfo loginInfo;
    ArrayList<CartItems> shoppingCart;
    ArrayList<CartItems> previousShoppingCart;
    Buyers currentBuyer;
    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == buyTicketButton) {
                shoppingCart = currentBuyer.retrieveShoppingCart();
                BuyTicketGUI buyTicketGUI = new BuyTicketGUI();
                buyTicketGUI.setLoginInfo(loginInfo);
                buyTicketGUI.setCurrentBuyer(currentBuyer);
                buyTicketGUI.setShoppingCart(shoppingCart);
                buyTicketGUI.setPreviousShoppingCart(previousShoppingCart);
                buyTicketGUI.run();
                frame.dispose();
            }
            if (e.getSource() == viewShoppingCartButton) {
                shoppingCart = currentBuyer.retrieveShoppingCart();
                if (shoppingCart == null) {
                    JOptionPane.showMessageDialog(null, "Error! Cannot view shopping cart" +
                            " because it is empty!", "No Items!", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Your Shopping Cart:",
                            "Shopping Cart", JOptionPane.INFORMATION_MESSAGE);
                    for (int i = 0; i < shoppingCart.size(); i++) {
                        Ticket ticket = shoppingCart.get(i).getTicket();
                        int qty = shoppingCart.get(i).getQTY();
                        String s = i + 1 + ":\n" + "Sport: " + ticket.getSportType() + "\nLocation: " +
                                ticket.getLocation() + "\nSection: " + ticket.getSection() + "\nPrice: " +
                                String.format("%.2f", ticket.getPrice()) + "\nQuantity: " + qty;
                        JOptionPane.showMessageDialog(null, s,
                                "Shopping Cart", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
            if (e.getSource() == removeFromShoppingCartButton) {
                ArrayList<CartItems> shoppingCart2 = currentBuyer.retrieveShoppingCart();
                if (shoppingCart == null || shoppingCart2 == null) {
                    JOptionPane.showMessageDialog(null, "Error! Shopping cart is empty!",
                            "Empty Shopping Cart", JOptionPane.ERROR_MESSAGE);
                } else {
                    RemoveTicketGUI removeTicketGUI = new RemoveTicketGUI();
                    removeTicketGUI.setLoginInfo(loginInfo);
                    removeTicketGUI.setCurrentBuyer(currentBuyer);
                    removeTicketGUI.setShoppingCart(shoppingCart);
                    removeTicketGUI.setPreviousShoppingCart(previousShoppingCart);
                    removeTicketGUI.run();
                    frame.dispose();
                }
            }
            if (e.getSource() == checkOutButton) {
                shoppingCart = currentBuyer.retrieveShoppingCart();
                if (shoppingCart == null) {
                    JOptionPane.showMessageDialog(null, "Error! Shopping cart is empty!",
                            "Empty Shopping Cart", JOptionPane.ERROR_MESSAGE);
                } else {
                    int num = JOptionPane.showConfirmDialog(null, "Are you sure you want " +
                            "to checkout?", "Checkout", JOptionPane.YES_NO_OPTION);
                    if (num == JOptionPane.YES_OPTION) {
                        String s = "Here is a receipt of your checked out items:\n\n";

                        currentBuyer.checkoutCart();
                        for (int i = 0; i < shoppingCart.size(); i++) {
                            Ticket ticket = shoppingCart.get(i).getTicket();
                            int qty = shoppingCart.get(i).getQTY();
                            s += i + 1 + ":\n" + "Sport: " + ticket.getSportType() + "\nLocation: " +
                                    ticket.getLocation() + "\nSection: " + ticket.getSection() + "\nPrice: " +
                                    String.format("%.2f", ticket.getPrice()) + "\nQuantity: " + qty + "\n\n";
                        }
                        JOptionPane.showMessageDialog(null, s,
                                "Shopping Cart", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
            if (e.getSource() == returnToMenuButton) {
                BuyerDashboardGUI buyerDashboardGUI = new BuyerDashboardGUI();
//                buyerDashboardGUI.setLoginInfo(loginInfo);
//                buyerDashboardGUI.setCurrentBuyer(currentBuyer);
//                buyerDashboardGUI.setShoppingCart(shoppingCart);
//                buyerDashboardGUI.setPreviousShoppingCart(previousShoppingCart);
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

    public void setLoginInfo(LoginInfo loginInfo) {
        this.loginInfo = loginInfo;
    }

    public void setShoppingCart(ArrayList<CartItems> shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public void setPreviousShoppingCart(ArrayList<CartItems> previousShoppingCart) {
        this.previousShoppingCart = previousShoppingCart;
    }

    public Buyers getCurrentBuyer() {
        return this.currentBuyer;
    }

    public void setCurrentBuyer(Buyers currentBuyer) {
        this.currentBuyer = currentBuyer;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new BuyTicketMenuGUI());
    }
}
