import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Project 5: BuyerStatisticsGUI
 *
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
    LoginInfo loginInfo;
    ArrayList<CartItems> shoppingCart;
    ArrayList<CartItems> previousShoppingCart;
    Buyers currentBuyer;

    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == confirmButton) {
                int choice = list.getSelectedIndex();
                String[] options = {"1. Raw Data", "2. Sorted Dashboard"};
                switch (choice) {
                    case 0:
                        String decision1 = (String) JOptionPane.showInputDialog(frame,
                                "How would you like to view the dashboard?", "View Buyer Statistics",
                                JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
                        int num1 = Arrays.asList(options).indexOf(decision1);
                        switch (num1) {
                            case 0:
                                String s = "";
                                ArrayList<String> list = printGeneralDash(
                                        currentBuyer.generalDashbaord(false));
                                for (int i = 0; i < list.size(); i++) {
                                    s += list.get(i);
                                }
                                JOptionPane.showMessageDialog(frame, s,
                                        "Raw Data Buyer Statistics", JOptionPane.INFORMATION_MESSAGE);
                                break;
                            case 1:
                                String s1 = "";
                                ArrayList<String> list2 = printGeneralDash(
                                        currentBuyer.generalDashbaord(true));
                                for (int i = 0; i < list2.size(); i++) {
                                    s1 += list2.get(i);
                                }
                                JOptionPane.showMessageDialog(frame, s1,
                                        "Raw Data Buyer Statistics", JOptionPane.INFORMATION_MESSAGE);
                                break;
                        }
                    case 1:
                        String decision2 = (String) JOptionPane.showInputDialog(frame,
                                "How would you like to view the dashboard?", "View Buyer Statistics",
                                JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
                        int num2 = Arrays.asList(options).indexOf(decision2);
                        switch (num2) {
                            case 0:
                                String s = "";
                                ArrayList<String> list = printBuyerDash(
                                        currentBuyer.listCustomerSpecificDashboard(false));
                                for (int i = 0; i < list.size(); i++) {
                                    s += list.get(i);
                                }
                                JOptionPane.showMessageDialog(frame, s,
                                        "Raw Data Buyer Statistics", JOptionPane.INFORMATION_MESSAGE);
                                break;
                            case 1:
                                String s1 = "";
                                ArrayList<String> list2 = printBuyerDash(
                                        currentBuyer.listCustomerSpecificDashboard(true));
                                for (int i = 0; i < list2.size(); i++) {
                                    s1 += list2.get(i);
                                }
                                JOptionPane.showMessageDialog(frame, s1,
                                        "Raw Data Buyer Statistics", JOptionPane.INFORMATION_MESSAGE);
                                break;

                        }
                }
            }
            if (e.getSource() == returnToMenuButton) {
                BuyerDashboardGUI buyerDashboardGUI = new BuyerDashboardGUI();
                buyerDashboardGUI.setLoginInfo(loginInfo);
                buyerDashboardGUI.setCurrentBuyer(currentBuyer);
                buyerDashboardGUI.setShoppingCart(shoppingCart);
                buyerDashboardGUI.run();
                frame.dispose();
            }
        }
    };

    public BuyerStatisticsGUI() {

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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new BuyerStatisticsGUI());
    }
}