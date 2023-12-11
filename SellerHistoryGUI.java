import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Project 5: SellerHistoryGUI
 * <p>
 * Utilizes GUI to allow a seller to view a store's shopping history.
 *
 * @author Henry J. Lee, Lab Section L20
 * @version November 13, 2023
 */

public class SellerHistoryGUI extends JComponent implements Runnable {
    JFrame frame;
    JLabel label;
    JComboBox<String> list;
    JButton confirmButton;
    JButton returnToMenuButton;
    //LoginInfo loginInfo;
    MarketplaceClient client;
    ArrayList<String> stores;
    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == confirmButton) {
                client.sendInt(1);
                String selectedStores = (String) list.getSelectedItem();
                int selectedStoreNumber = Integer.parseInt(selectedStores.split("\\.")[0]);
                client.sendInt(selectedStoreNumber);


                ArrayList<String> salesList = client.recieveStringArrayList();

                if (salesList.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "This Store has no Sales!",
                            "No Stores!", JOptionPane.ERROR_MESSAGE);
                } else {
                    StringBuilder salesInfo = new StringBuilder();
                    for (int i = 0; i < salesList.size() - 1; i++) {
                        salesInfo.append(i + 1).append(".\n").append(salesList.get(i)).append("\n\n");
                    }
                    salesInfo.append(salesList.get(salesList.size() - 1));

                    JTextArea textArea = new JTextArea(salesInfo.toString());
                    textArea.setEditable(false);
                    textArea.setLineWrap(true);
                    textArea.setWrapStyleWord(true);

                    JScrollPane scrollPane = new JScrollPane(textArea);


                    JFrame salesFrame = new JFrame("Transaction Information");

                    salesFrame.setContentPane(scrollPane);

                    // Set frame properties
                    int frameWidth = Math.min(800, textArea.getPreferredSize().width + 50);
                    int frameHeight = Math.min(600, textArea.getPreferredSize().height + 50);
                    salesFrame.setSize(frameWidth, frameHeight);
                    salesFrame.setLocationRelativeTo(null);
                    salesFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                    // Make the frame visible
                    salesFrame.setVisible(true);
                }
            }
            if (e.getSource() == returnToMenuButton) {
                client.sendInt(2);
                SellerDashboardGUI sellerDashboardGUI = new SellerDashboardGUI();
                sellerDashboardGUI.setClient(client);
                sellerDashboardGUI.run();
                frame.dispose();
            }
        }
    };


    public SellerHistoryGUI() {


    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new SellerHistoryGUI());
    }

    public void run() {
        frame = new JFrame("Seller History");
        frame.setLayout(new GridBagLayout());


        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.add(panel);


        GridBagConstraints gbc1 = new GridBagConstraints();
        gbc1.gridx = 0;
        gbc1.gridy = 0;
        gbc1.insets = new Insets(5, 10, 5, 10);
        gbc1.anchor = GridBagConstraints.NORTH;


        stores = client.recieveStringArrayList();


        if (stores == null || stores.isEmpty()) {
            client.sendBoolean(true);
            frame.dispose();
            SellerDashboardGUI sellerDashboardGUI = new SellerDashboardGUI();
            sellerDashboardGUI.setClient(client);
            sellerDashboardGUI.run();
            return;

        } else {
            client.sendBoolean(false);
            label = new JLabel("Which store would you like to view the sales for?");
            panel.add(label);
            gbc1.gridy++;


            String[] options = new String[stores.size()];
            for (int i = 0; i < stores.size(); i++) {
                options[i] = i + 1 + ". " + stores.get(i);
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
        frame.add(panel);
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void setClient(MarketplaceClient client) {
        this.client = client;
    }
}
