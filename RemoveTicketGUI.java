import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Project 5: RemoveTicketGUI
 * 
 * Utilizes GUi to allow a seller to remove a ticket from a store.
 *
 * @author Henry J. Lee, Lab Section L20
 * @version November 13, 2023
 */

public class RemoveTicketGUI extends JComponent implements Runnable {
    JFrame frame;
    JLabel label;
    JComboBox<String> list;
    JButton confirmButton;
    JButton returnToMenuButton;
    LoginInfo loginInfo;
    ArrayList<CartItems> shoppingCart;
    ArrayList<CartItems> previousShoppingCart;
    ArrayList<String> realShoppingCart;
    Buyers currentBuyer;
    MarketplaceClient client;
    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == confirmButton) {
                client.sendBoolean(true);
                //ArrayList<CartItems> shoppingCart2 = currentBuyer.retrieveShoppingCart();
                int item = list.getSelectedIndex();
                client.sendInt(item);

//                StoreNameIDCombo combo =
//                        Buyers.getSellerID(shoppingCart2.get(item).getTicket().toString());
//                Buyers.updateTicketQuantity("SellerInfo.txt", combo.getSellerID(),
//                        shoppingCart2.get(item).getTicket().toString(),
//                        -1 * shoppingCart2.get(item).getQTY());
//                shoppingCart2.remove(item);
//                currentBuyer.setShoppingCart(shoppingCart2);

                boolean success = client.receiveBoolean();

                if(success) {
                    JOptionPane.showMessageDialog(null, "Item successfully deleted!",
                            "Deleted Item", JOptionPane.INFORMATION_MESSAGE);

                    BuyTicketMenuGUI buyTicketMenuGUI = new BuyTicketMenuGUI();
                    buyTicketMenuGUI.setClient(client);
                    buyTicketMenuGUI.run();
                    frame.dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Deletion unsuccessful!",
                            "Not Deleted Item", JOptionPane.ERROR_MESSAGE);
                    BuyTicketMenuGUI buyTicketMenuGUI = new BuyTicketMenuGUI();
                    buyTicketMenuGUI.setClient(client);
                    buyTicketMenuGUI.run();
                    frame.dispose();
                }

            }
            if (e.getSource() == returnToMenuButton) {
                client.sendBoolean(false);
                BuyTicketMenuGUI buyTicketMenuGUI = new BuyTicketMenuGUI();
                buyTicketMenuGUI.setClient(client);
                buyTicketMenuGUI.run();
                frame.dispose();
            }
        }
    };

    public RemoveTicketGUI() {

    }

    public void run() {
        frame = new JFrame("Remove A Ticket");
        frame.setLayout(new GridBagLayout());

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.add(panel);

        GridBagConstraints gbc1 = new GridBagConstraints();
        gbc1.gridx = 0;
        gbc1.gridy = 0;
        gbc1.insets = new Insets(5, 10, 5, 10);
        gbc1.anchor = GridBagConstraints.NORTH;

        label = new JLabel("Which item would you like to remove?");
        panel.add(label, gbc1);

        gbc1.gridy++;

        //ArrayList<CartItems> shoppingCart2 = currentBuyer.retrieveShoppingCart();

        String[] arr = new String[realShoppingCart.size()];
        for (int i = 0; i < realShoppingCart.size(); i++) {
            String cartItem = realShoppingCart.get(i);
            String[] parts = cartItem.split(";");
            String sport = parts[0];
            String location = parts[1];
            String section = parts[2];
            String[] priceAndQuantity = parts[3].split("&");
            double price = Double.parseDouble(priceAndQuantity[0]);
            String quantity = priceAndQuantity[1];

            arr[i] = "<html>" + i + 1 + ":<br>" + "Sport: " + sport + "<br>Location: " +
                    location + "<br>Section: " + section + "<br>Price: " +
                    String.format("%.2f", price) + "<br>Quantity: " + quantity + "</html>";
        }



        list = new JComboBox<>(arr);
        panel.add(list, gbc1);

        gbc1.gridy++;
        confirmButton = new JButton("Delete Item");
        confirmButton.addActionListener(actionListener);
        confirmButton.setPreferredSize(new Dimension(150, 30));
        panel.add(confirmButton, gbc1);

        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.gridx = 0;
        gbc2.gridy = gbc1.gridy + 1;
        gbc2.insets = new Insets(50, 10, 10, 10);
        gbc2.anchor = GridBagConstraints.SOUTH;

        returnToMenuButton = new JButton("Return to Menu");
        returnToMenuButton.addActionListener(actionListener);
        returnToMenuButton.setPreferredSize(new Dimension(200, 50));
        panel.add(returnToMenuButton, gbc2);

        int verticalCenter = (Toolkit.getDefaultToolkit().getScreenSize().height - frame.getHeight()) / 2;
        frame.setLocationRelativeTo(null);
        frame.setLocation(frame.getLocation().x, verticalCenter);

        frame.setSize(700, 500);
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

    public void setShoppingCart(ArrayList<CartItems> shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public void setPreviousShoppingCart(ArrayList<CartItems> previousShoppingCart) {
        this.previousShoppingCart = previousShoppingCart;
    }

    public void setCurrentBuyer(Buyers currentBuyer) {
        this.currentBuyer = currentBuyer;
    }

    public void setRealShoppingCart(ArrayList<String> realShoppingCart) {
        this.realShoppingCart = realShoppingCart;
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new RemoveTicketGUI());
    }
}
