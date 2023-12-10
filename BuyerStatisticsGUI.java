import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Project 5: BuyerStatisticsGUI
 * <p>
 * Utilizes GUI to allow a buyer to see their statistics.
 *
 * @author Henry J. Lee, Lab Section L20
 * @version November 13, 2023
 */

public class BuyerStatisticsGUI extends JComponent implements Runnable {
    JFrame frame;
    JLabel label;
    JComboBox<String> list;
    JButton confirmButton;
    JButton returnToMenuButton;
    MarketplaceClient client;
    Buyers currentBuyer;

    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == confirmButton) {
                client.sendBoolean(true);
                int choice = list.getSelectedIndex();
                client.sendInt(choice);
                String[] options = {"1. Raw Data", "2. Sorted Dashboard"};
                switch (choice) {
                    case 0:
                        String decision1 = (String) JOptionPane.showInputDialog(frame,
                                "How would you like to view the dashboard?", "View Buyer Statistics",
                                JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
                        client.sendString(decision1);
                        if (decision1 != null) {
                            int num1 = Arrays.asList(options).indexOf(decision1);
                            client.sendInt(num1);
                            switch (num1) {
                                case 0:
                                    String s = client.getString();

                                    if(s.isEmpty()) {
                                        JOptionPane.showMessageDialog(frame, "No statistics available",
                                                "Raw Data Buyer Statistics", JOptionPane.ERROR_MESSAGE);
                                    } else  {
                                        JTextArea textArea = new JTextArea(s);
                                        textArea.setEditable(false);
                                        textArea.setLineWrap(true);
                                        textArea.setWrapStyleWord(true);

                                        JScrollPane scrollPane = new JScrollPane(textArea);


                                        JFrame statsFrame = new JFrame("Buyer Statistics");

                                        statsFrame.setContentPane(scrollPane);

                                        // Set frame properties
                                        int frameWidth = Math.min(800, textArea.getPreferredSize().width*2);
                                        int frameHeight = (int) Math.min(600, textArea.getPreferredSize().height*1.5);
                                        statsFrame.setSize(frameWidth, frameHeight);
                                        statsFrame.setLocationRelativeTo(null);
                                        statsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                                        // Make the frame visible
                                        statsFrame.setVisible(true);
                                    }

                                    break;
                                case 1:
                                    String s1 = client.getString();

                                    if(!s1.isEmpty()) {
                                        JTextArea textArea = new JTextArea(s1);
                                        textArea.setEditable(false);
                                        textArea.setLineWrap(true);
                                        textArea.setWrapStyleWord(true);

                                        JScrollPane scrollPane = new JScrollPane(textArea);


                                        JFrame statsFrame = new JFrame("Buyer Statistics");

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
                                        JOptionPane.showMessageDialog(frame, "No statistics available",
                                                "Raw Data Buyer Statistics", JOptionPane.ERROR_MESSAGE);
                                    }
                                    break;
                            }
                        }
                        break;
                    case 1:
                        String decision2 = (String) JOptionPane.showInputDialog(frame,
                                "How would you like to view the dashboard?", "View Buyer Statistics",
                                JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
                        client.sendString(decision2);
                        if(decision2 != null) {
                            int num2 = Arrays.asList(options).indexOf(decision2);
                            client.sendInt(num2);
                            switch (num2) {
                                case 0:
                                    String s = client.getString();

                                    if(s.isEmpty()) {
                                        JOptionPane.showMessageDialog(frame, "No statistics available",
                                                "Raw Data Buyer Statistics", JOptionPane.ERROR_MESSAGE);
                                    } else  {
                                        JTextArea textArea = new JTextArea(s);
                                        textArea.setEditable(false);
                                        textArea.setLineWrap(true);
                                        textArea.setWrapStyleWord(true);

                                        JScrollPane scrollPane = new JScrollPane(textArea);


                                        JFrame statsFrame = new JFrame("Buyer Statistics");

                                        statsFrame.setContentPane(scrollPane);

                                        // Set frame properties
                                        int frameWidth = Math.min(800, textArea.getPreferredSize().width*2);
                                        int frameHeight = (int) Math.min(600, textArea.getPreferredSize().height*1.5);
                                        statsFrame.setSize(frameWidth, frameHeight);
                                        statsFrame.setLocationRelativeTo(null);
                                        statsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                                        // Make the frame visible
                                        statsFrame.setVisible(true);
                                    }
                                    break;
                                case 1:
                                    String s1 = client.getString();

                                    if(!s1.isEmpty()) {
                                        JTextArea textArea = new JTextArea(s1);
                                        textArea.setEditable(false);
                                        textArea.setLineWrap(true);
                                        textArea.setWrapStyleWord(true);

                                        JScrollPane scrollPane = new JScrollPane(textArea);


                                        JFrame statsFrame = new JFrame("Buyer Statistics");

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
                                        JOptionPane.showMessageDialog(frame, "No statistics available",
                                                "Raw Data Buyer Statistics", JOptionPane.ERROR_MESSAGE);
                                    }
                                    break;

                            }
                        }
                        break;
                }
            }
            if (e.getSource() == returnToMenuButton) {
                client.sendBoolean(false);
                BuyerDashboardGUI buyerDashboardGUI = new BuyerDashboardGUI();
//                buyerDashboardGUI.setLoginInfo(loginInfo);
//                buyerDashboardGUI.setCurrentBuyer(currentBuyer);
//                buyerDashboardGUI.setShoppingCart(shoppingCart);
                buyerDashboardGUI.setClient(client);
                buyerDashboardGUI.run();
                frame.dispose();
            }
        }
    };

    public BuyerStatisticsGUI() {

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new BuyerStatisticsGUI());
    }

    public void run() {
        frame = new JFrame("Buyer Statistics");
        JPanel panel = new JPanel(new GridBagLayout());

        GridBagConstraints gbc1 = new GridBagConstraints();
        gbc1.gridx = 0;
        gbc1.gridy = 0;
        gbc1.insets = new Insets(5, 10, 5, 10);
        gbc1.anchor = GridBagConstraints.CENTER;

        label = new JLabel("What dashboard would you like to view?");
        panel.add(label, gbc1);
        gbc1.gridy++;

        String[] options = {"1. Entire Store Dashboard", "2. Personal Store Transaction Data"};
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
        gbc2.anchor = GridBagConstraints.CENTER;

        returnToMenuButton = new JButton("Return to Menu");
        returnToMenuButton.addActionListener(actionListener);
        returnToMenuButton.setPreferredSize(new Dimension(200, 50));
        panel.add(returnToMenuButton, gbc2);

        int verticalCenter = (Toolkit.getDefaultToolkit().getScreenSize().height - frame.getHeight()) / 2;
        frame.setLocationRelativeTo(null);
        frame.setLocation(frame.getLocation().x, verticalCenter);

        frame.add(panel);
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    synchronized public void setClient(MarketplaceClient client) {
        this.client = client;
    }

    public void setCurrentBuyer(Buyers currentBuyer) {
        this.currentBuyer = currentBuyer;
    }

    public ArrayList<String> printBuyerDash(ArrayList<ArrayList<String>> dashInfo) {
        ArrayList<String> list = new ArrayList<String>();
        for (ArrayList<String> thing : dashInfo) {
            for (String b : thing) {
                list.add(b + " ");
            }
        }
        return list;
    }

    public ArrayList<String> printGeneralDash(ArrayList<String> dashInfo) {
        ArrayList<String> list = new ArrayList<String>();
        for (String thing : dashInfo) {
            list.add(thing + " ");
        }
        return list;
    }
}