import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Project 5: SellerStatisticsGUI
 *
 * Utilizes GUI to allow a seller to view their stores' statistics.
 *
 * @author Henry J. Lee, Lab Section L20
 * @version November 13, 2023
 */

public class SellerStatisticsGUI extends JComponent implements Runnable {
    JFrame frame;
    JLabel label;
    JComboBox<String> list;
    JButton confirmButton;
    JButton returnToMenuButton;
    ArrayList<String> stores;
    int num;
    int choice;
    int number;
    int anotherOne;

    MarketplaceClient client;
    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == confirmButton) {
                client.sendBoolean(true);
                ArrayList<String> allTransactions2 = Marketplace.readFile("TransactionInfo.txt");
                num = list.getSelectedIndex();

                String[] options2 = {"1. List of customers and items purchased",
                        "2. List of products and number of sales"};
                String decision2 = (String) JOptionPane.showInputDialog(frame,
                        "How would you like to view the statistics?", "View Seller Statistics",
                        JOptionPane.PLAIN_MESSAGE, null, options2, options2[0]);
                client.sendString(decision2);

                if (decision2 != null) {
                    client.sendInt(num);

                    choice = Arrays.asList(options2).indexOf(decision2);
                    client.sendInt(choice);


                    switch (choice) {
                        case 0:
                            String[] options3 = {"1. Default (Buyer ID)", "2. Customer Email (Alphabetical Order)",
                                    "3. Number of Items Purchased"};
                            String decision3 = (String) JOptionPane.showInputDialog(frame,
                                    "How would you to sort the data?", "View Seller Statistics",
                                    JOptionPane.PLAIN_MESSAGE, null, options3, options3[0]);
                            client.sendString(decision3);
                            if (decision3 != null) {
                                number = Arrays.asList(options3).indexOf(decision3);
                                client.sendInt(number);


                                ArrayList<String> listToPrint = client.recieveStringArrayList();
                                StringBuilder stats = new StringBuilder();

                                if(!listToPrint.isEmpty()) {
                                    for (int i = 0; i < listToPrint.size() - 1; i++) {
                                        stats.append("Customer ").append(i + 1).append("\n").append(listToPrint.get(i)).append("\n\n");
                                    }

                                    stats.append("Customer ").append(listToPrint.size()).append("\n").append(listToPrint.get(listToPrint.size() - 1));

                                    JTextArea textArea = new JTextArea(stats.toString());
                                    textArea.setEditable(false);
                                    textArea.setLineWrap(true);
                                    textArea.setWrapStyleWord(true);

                                    JScrollPane scrollPane = new JScrollPane(textArea);


                                    JFrame statsFrame = new JFrame("Seller Statistics");

                                    statsFrame.setContentPane(scrollPane);

                                    // Set frame properties
                                    int frameWidth = Math.min(800, textArea.getPreferredSize().width*2);
                                    int frameHeight = (int) Math.min(600, textArea.getPreferredSize().height*1.5);
                                    statsFrame.setSize(frameWidth, frameHeight);
                                    statsFrame.setLocationRelativeTo(null);
                                    statsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                                    // Make the frame visible
                                    statsFrame.setVisible(true);
                                } else {
                                    JOptionPane.showMessageDialog(null, "No transactions for this store", "No " +
                                            "transactions", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                            break;
                        case 1:
                            String[] options4 = {"1. Location", "2. Sport", "3. Number of Sales"};
                            String decision4 = (String) JOptionPane.showInputDialog(frame,
                                    "How would you to sort the data?", "View Seller Statistics",
                                    JOptionPane.PLAIN_MESSAGE, null, options4, options4[0]);
                            client.sendString(decision4);

                            if(decision4 != null) {
                                anotherOne = Arrays.asList(options4).indexOf(decision4);
                                client.sendInt(anotherOne);

                                ArrayList<String> listToPrint = client.recieveStringArrayList();

                                if(!listToPrint.isEmpty()) {
                                    StringBuilder stats = new StringBuilder();

                                    for (int i = 0; i < listToPrint.size()-1; i++) {
                                        String[] listItem = listToPrint.get(i).split(";");

                                        stats.append("Product ").append(i + 1).append("\n").append("Product Info - \nSport: ").append(listItem[1]).append("\nLocation: ").append(listItem[2]).append("\nSection: ").append(listItem[3]).append("\nSales: ").append(listItem[0]).append("\n\n");

                                    }

                                    String[] listItem = listToPrint.get(listToPrint.size()-1).split(";");
                                    stats.append("Product ").append(listToPrint.size()).append("\n").append("Product Info" +
                                            " - " +
                                            "\nSport: ").append(listItem[1]).append("\nLocation: ").append(listItem[2]).append("\nSection: ").append(listItem[3]).append("\nSales: ").append(listItem[0]);

                                    JTextArea textArea = new JTextArea(stats.toString());
                                    textArea.setEditable(false);
                                    textArea.setLineWrap(true);
                                    textArea.setWrapStyleWord(true);

                                    JScrollPane scrollPane = new JScrollPane(textArea);


                                    JFrame statsFrame = new JFrame("Seller Statistics");

                                    statsFrame.setContentPane(scrollPane);

                                    // Set frame properties
                                    int frameWidth = Math.min(800, textArea.getPreferredSize().width*2);
                                    int frameHeight = (int) Math.min(600, textArea.getPreferredSize().height*1.5);
                                    statsFrame.setSize(frameWidth, frameHeight);
                                    statsFrame.setLocationRelativeTo(null);
                                    statsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                                    // Make the frame visible
                                    statsFrame.setVisible(true);
                                } else {
                                    JOptionPane.showMessageDialog(null, "No transactions for this store", "No " +
                                            "transactions", JOptionPane.ERROR_MESSAGE);
                                }


                            }
                    }
                }


            }


            if (e.getSource() == returnToMenuButton) {
                client.sendBoolean(false);
                SellerDashboardGUI sellerDashboardGUI = new SellerDashboardGUI();
                sellerDashboardGUI.setClient(client);
                sellerDashboardGUI.run();
                frame.dispose();
            }
        }
    };

    public SellerStatisticsGUI() {

    }

    public void run() {
        frame = new JFrame("Seller Statistics");
        JPanel panel = new JPanel(new GridBagLayout());

        GridBagConstraints gbc1 = new GridBagConstraints();
        gbc1.gridx = 0;
        gbc1.gridy = 0;
        gbc1.insets = new Insets(5, 10, 5, 10);
        gbc1.anchor = GridBagConstraints.CENTER;

        stores = client.recieveStringArrayList();

        if (stores == null) {
            JOptionPane.showMessageDialog(null, "Error! There are no stores!",
                    "No Stores", JOptionPane.ERROR_MESSAGE);
        } else {
            label = new JLabel("Which store would you like to view the sales for?");
            panel.add(label, gbc1);
            gbc1.gridy++;

            String[] options1 = new String[stores.size()];
            for (int i = 0; i < stores.size(); i++) {
                options1[i] = i + 1 + ". " + stores.get(i);
            }
            list = new JComboBox<>(options1);
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
            gbc2.anchor = GridBagConstraints.CENTER;

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

        synchronized public void setClient(MarketplaceClient client) {
        this.client = client;
    }
        
        public void setStores(ArrayList<String> stores) {
            this.stores = stores;
        }

        public static void main(String[] args) {
            SwingUtilities.invokeLater(new SellerStatisticsGUI());
        }
    }
