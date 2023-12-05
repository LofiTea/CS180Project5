import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Homework/Project X: ClassName
 * <p>
 * Describe what the assignment is about.
 *
 * @author Henry J. Lee, Lab Section L20
 * @version Month XX, 2023
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
    Buyers currentBuyer;
    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == confirmButton) {
                ArrayList<CartItems> shoppingCart2 = currentBuyer.retrieveShoppingCart();
                int item = list.getSelectedIndex();
                StoreNameIDCombo combo =
                        Buyers.getSellerID(shoppingCart2.get(item).getTicket().toString());
                Buyers.updateTicketQuantity("SellerInfo.txt", combo.getSellerID(),
                        shoppingCart2.get(item).getTicket().toString(),
                        -1 * shoppingCart2.get(item).getQTY());
                shoppingCart2.remove(item);
                currentBuyer.setShoppingCart(shoppingCart2);
                try {
                    currentBuyer.updateShoppingCart();
                    JOptionPane.showMessageDialog(null, "Item successfully deleted!",
                                    "Deleted Item", JOptionPane.INFORMATION_MESSAGE);
                    BuyTicketMenuGUI buyTicketMenuGUI = new BuyTicketMenuGUI();
                    buyTicketMenuGUI.setLoginInfo(loginInfo);
                    buyTicketMenuGUI.setCurrentBuyer(currentBuyer);
                    buyTicketMenuGUI.setShoppingCart(shoppingCart);
                    buyTicketMenuGUI.setPreviousShoppingCart(previousShoppingCart);
                    buyTicketMenuGUI.run();
                    frame.dispose();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            if (e.getSource() == returnToMenuButton) {
                BuyTicketMenuGUI buyTicketMenuGUI = new BuyTicketMenuGUI();
                buyTicketMenuGUI.setLoginInfo(loginInfo);
                buyTicketMenuGUI.setCurrentBuyer(currentBuyer);
                buyTicketMenuGUI.setShoppingCart(shoppingCart);
                buyTicketMenuGUI.setPreviousShoppingCart(previousShoppingCart);
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

        ArrayList<CartItems> shoppingCart2 = currentBuyer.retrieveShoppingCart();

        String[] arr = new String[shoppingCart2.size()];
        for (int i = 0; i < shoppingCart2.size(); i++) {
            Ticket ticket = shoppingCart2.get(i).getTicket();
            int qty = shoppingCart2.get(i).getQTY();
            arr[i] = i + 1 + ":\n" + "Sport: " + ticket.getSportType() + "\nLocation: " +
                    ticket.getLocation() + "\nSection: " + ticket.getSection() + "\nPrice: " +
                    String.format("%.2f", ticket.getPrice()) + "\nQuantity: " + qty;
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new RemoveTicketGUI());
    }
}