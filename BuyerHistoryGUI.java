import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Project 5: BuyerHistoryGUI
 * 
 * Utilizes GUI to allow a buyer to see their shopping history.
 *
 * @author Henry J. Lee, Lab Section L20
 * @version November 13, 2023
 */

public class BuyerHistoryGUI extends JComponent implements Runnable {
    JFrame frame;
    JLabel label;
    JComboBox<String> list;
    JButton confirmButton;
    JButton returnToMenuButton;
    MarketplaceClient client;
    ArrayList<String> previousCart;

    public BuyerHistoryGUI() {

    }

    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == confirmButton) {
                client.sendInt(1);
                String selectedOption = (String) list.getSelectedItem();

                int selectedIndex = Integer.parseInt(selectedOption) - 1;
                
                String currentTicketInfo = previousCart.get(selectedIndex);

                String[] curTicketSplit = currentTicketInfo.split("&");
                String[] tickInfoSplit = curTicketSplit[0].split(";");
                


            double transactionAction = Double.parseDouble(curTicketSplit[1]) * Double.parseDouble(tickInfoSplit[3]);

        String transactionDetails = (selectedIndex + 1) + ":\nSport: " +
        tickInfoSplit[0] + "\nLocation: " + tickInfoSplit[1] +
        "\nSection: " + tickInfoSplit[2] + "\nPrice: $" + String.format("%.2f", Double.parseDouble(tickInfoSplit[3])) +
        "\nQuantity: " + curTicketSplit[1] + "\nPrice of Transaction: $" + String.format("%.2f", transactionAction);

                    JOptionPane.showMessageDialog(null, transactionDetails, "Ticket "+selectedOption,
                            JOptionPane.INFORMATION_MESSAGE);

                client.sendInt(13);
                
            }

            if (e.getSource() == returnToMenuButton) {
                client.sendInt(2);
                BuyerDashboardGUI buyerDashboardGUI = new BuyerDashboardGUI();
                buyerDashboardGUI.setClient(client);
                buyerDashboardGUI.run();
                frame.dispose();
            }
        }
    };

    public void run() {
        frame = new JFrame("Buyer History");
        frame.setLayout(new GridBagLayout());

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.add(panel);

        GridBagConstraints gbc1 = new GridBagConstraints();
        gbc1.gridx = 0;
        gbc1.gridy = 0;
        gbc1.insets = new Insets(5, 10, 5, 10);
        gbc1.anchor = GridBagConstraints.NORTH;

        label = new JLabel("Which previous bought item would you like to view?");
        panel.add(label, gbc1);

        gbc1.gridy++;
        previousCart = client.recieveStringArrayList();

        if (previousCart == null) {
            JOptionPane.showMessageDialog(null, "Error! Cannot view the previous cart" +
                            " because it is empty!", "No Items!", JOptionPane.ERROR_MESSAGE);
        } else {
            String[] options = new String[previousCart.size()];
            for (int i = 0; i < previousCart.size(); i++) {
                options[i] = "" + (i + 1);
            }
            list = new JComboBox<>(options);
            panel.add(list, gbc1);

            gbc1.gridy++;
            confirmButton = new JButton("See Selected Item");
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
        }
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
        SwingUtilities.invokeLater(new BuyerHistoryGUI());
    }
}
