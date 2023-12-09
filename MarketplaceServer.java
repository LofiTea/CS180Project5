import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Project 5: MarketplaceServer
 *
 * Holds the server for the marketplace.
 *
 * @author Shrish Mahesh, Rahul Siddharth, Lab Section L20
 * @version November 13, 2023
 */

public class MarketplaceServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);
        while (true) {

            Socket socket = serverSocket.accept();
            MarketplaceServerThread st = new MarketplaceServerThread(socket);
            st.start();
        }
    }


    synchronized public static ArrayList<String> readFile(String fileName) {
        ArrayList<String> list = new ArrayList<>();
        try (BufferedReader bfr = new BufferedReader(new FileReader(fileName))) {
            String line = bfr.readLine();
            while (line != null) {
                list.add(line);
                line = bfr.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    synchronized public static void writeFile(ArrayList<String> lines, String filename) {
        try (PrintWriter pw = new PrintWriter(new FileOutputStream(filename, false))) {
            for (int i = 0; i < lines.size(); i++) {
                pw.println(lines.get(i));
            }
            pw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    synchronized public static String determineRole(String email, String password) {
        ArrayList<String> arrayList = readFile("LoginInfo.txt");
        String role = "";
        for (int i = 0; i < arrayList.size(); i++) {
            String[] userInfo = arrayList.get(i).split(",");
            if (email.equals(userInfo[1]) && password.equals(userInfo[2])) {
                role = userInfo[3];
            }
        }
        return role;
    }

   synchronized public static void editEmail(int id, String newEmail) {
        ArrayList<String> loginInfo = readFile("LoginInfo.txt");
        int indexToChange = 0;
        String password = "";
        String role = "";
        for (int i = 0; i < loginInfo.size(); i++) {
            String[] line = loginInfo.get(i).split(",");
            String check = line[0];
            String check1 = line[1];
            password = line[2];
            role = line[3];
            if (check.equals(String.valueOf(id))) {
                indexToChange = i;
                break;
            }
        }
        loginInfo.set(indexToChange, id + "," + newEmail + "," + password + "," + role);
        if (role.equals("b")) {
            ArrayList<String> buyerInfo = readFile("BuyerHistory.txt");
            for (int j = 0; j < buyerInfo.size(); j++) {
                String newLine = "";
                String[] anotherLine = buyerInfo.get(j).split(",");
                String check3 = anotherLine[0];
                String check4 = anotherLine[1];
                if (check3.equals(String.valueOf(id))) {
                    anotherLine[1] = newEmail;
                    for (int k = 0; k < anotherLine.length; k++) {
                        if (k == anotherLine.length - 1) {
                            newLine += anotherLine[k];
                        } else {
                            newLine += anotherLine[k] + ",";
                        }
                    }
                    buyerInfo.set(j, newLine);
                    writeFile(buyerInfo, "BuyerHistory.txt");
                    break;
                }
            }

            ArrayList<String> buyerInfo2 = readFile("BuyerInfo.txt");
            for (int j = 0; j < buyerInfo2.size(); j++) {
                String newLine = "";
                String[] anotherLine = buyerInfo2.get(j).split(",");
                String check3 = anotherLine[0];
                if (check3.equals(String.valueOf(id))) {
                    anotherLine[1] = newEmail;
                    for (int k = 0; k < anotherLine.length; k++) {
                        if (k == anotherLine.length - 1) {
                            newLine += anotherLine[k];
                        } else {
                            newLine += anotherLine[k] + ",";
                        }
                    }
                    buyerInfo2.set(j, newLine);
                    writeFile(buyerInfo2, "BuyerInfo.txt");
                    break;
                }
            }

        } else if (role.equals("s")) {
            ArrayList<String> sellerInfo = readFile("SellerInfo.txt");
            for (int j = 0; j < sellerInfo.size(); j++) {
                String newLine = "";
                String[] anotherLine = sellerInfo.get(j).split(",");
                String check5 = anotherLine[0];

                if (check5.equals(String.valueOf(id))) {
                    anotherLine[1] = newEmail;
                    for (int k = 0; k < anotherLine.length; k++) {
                        if (k == anotherLine.length - 1) {
                            newLine += anotherLine[k];
                        } else {
                            newLine += anotherLine[k] + ",";
                        }
                    }
                    sellerInfo.set(j, newLine);
                    writeFile(sellerInfo, "SellerInfo.txt");
                    break;
                }
            }
        }
        writeFile(loginInfo, "LoginInfo.txt");
    }

    synchronized public static void editPassword(int id, String newPassword) {
        ArrayList<String> loginInfo = readFile("LoginInfo.txt");
        int indexToChange = 0;
        String email = "";
        String role = "";
        for (int i = 0; i < loginInfo.size(); i++) {
            String[] line = loginInfo.get(i).split(",");
            String check = line[0];
            email = line[1];
            String check2 = line[2];
            role = line[3];
            if (check.equals(String.valueOf(id))) {
                indexToChange = i;
                break;
            }
        }
        loginInfo.set(indexToChange, id + "," + email + "," + newPassword + "," + role);
        writeFile(loginInfo, "LoginInfo.txt");
    }


   synchronized public static void deleteAccount(String email, String password) {
        ArrayList<String> loginInfo = readFile("LoginInfo.txt");
        String role = "";
        int id = 0;
        int indexToChange = 0;
        for (int i = 0; i < loginInfo.size(); i++) {
            String[] line = loginInfo.get(i).split(",");
            id = Integer.parseInt(line[0]);
            String check1 = line[1];
            String check2 = line[2];
            role = line[3];
            if (check1.equals(email) && check2.equals(password)) {
                indexToChange = i;
                break;
            }
        }

        ArrayList<String> newLoginInfo = new ArrayList<>();


        for (int i = 0; i < loginInfo.size(); i++) {
            if (i != indexToChange) {
                newLoginInfo.add(loginInfo.get(i));
            }
        }
        writeFile(newLoginInfo, "LoginInfo.txt");

        if (role.equals("b")) {
            int curIndextoChange = -1;
            ArrayList<String> buyerInfo = readFile("BuyerInfo.txt");
            for (int j = 0; j < buyerInfo.size(); j++) {
                String[] newLine = buyerInfo.get(j).split(",");
                if (id == Integer.parseInt(newLine[0])) {
                    curIndextoChange = j;
                    break;
                }
            }


            ArrayList<String> buyerInfo2 = new ArrayList<>();
            for (int j = 0; j < buyerInfo.size(); j++) {
                if (j != curIndextoChange) {
                    buyerInfo2.add(buyerInfo.get(j));
                }
            }
            writeFile(buyerInfo2, "BuyerInfo.txt");
        } else if (role.equals("s")) {
            int curIndextoChange = -1;
            ArrayList<String> buyerInfo = readFile("SellerInfo.txt");
            for (int j = 0; j < buyerInfo.size(); j++) {
                String[] newLine = buyerInfo.get(j).split(",");
                if (id == Integer.parseInt(newLine[0])) {
                    curIndextoChange = j;
                    break;
                }
            }


            ArrayList<String> buyerInfo2 = new ArrayList<>();
            for (int j = 0; j < buyerInfo.size(); j++) {
                if (j != curIndextoChange) {
                    buyerInfo2.add(buyerInfo.get(j));
                }
            }
            writeFile(buyerInfo2, "SellerInfo.txt");
        }
    }

    synchronized public static ArrayList<String> getEmailPassword(int id) {
        ArrayList<String> loginInfo = readFile("LoginInfo.txt");
        String email = "";
        String password = "";
        for (int i = 0; i < loginInfo.size(); i++) {
            String[] line = loginInfo.get(i).split(",");
            String check = line[0];
            email = line[1];
            password = line[2];
            if (check.equals(String.valueOf(id))) {
                break;
            }
        }

        return new ArrayList<>(Arrays.asList(email, password));

    }

    synchronized public static ArrayList<String> buildBuyerPreviousShoppingCartPackage(Buyers b)
    {
        ArrayList<CartItems> previousShoppingCart = b.retrieveShoppingCart2();
                             
                             if(previousShoppingCart == null) previousShoppingCart = new ArrayList<>();
                             ArrayList<String> shoppingCartPackage = new ArrayList<>();

                             for(CartItems e: previousShoppingCart)
                             {
                                String curItem = e.getTicket() + "&"+e.getQTY();
                                shoppingCartPackage.add(curItem);
                             }

             return shoppingCartPackage;
    }

    synchronized public static ArrayList<String> buildBuyerCurrentShoppingCartPackage(Buyers b)
    {
             ArrayList<CartItems> currentShoppingCart = b.retrieveShoppingCart();
                             
                             if(currentShoppingCart == null) currentShoppingCart= new ArrayList<>();
                             ArrayList<String> shoppingCartPackage = new ArrayList<>();

                             for(CartItems e: currentShoppingCart)
                             {
                                String curItem = e.getTicket() + "&"+e.getQTY();
                                shoppingCartPackage.add(curItem);
                             }

             return shoppingCartPackage;
    }

 
  synchronized public static ArrayList<String> buildListedTicketsPackage3(ArrayList<String> b)
  {

   for(int i = 0; i<b.size();i++)
   {
      String lol = b.get(i);
       lol = lol.replace("\n","&");
      b.set(i,lol);
   }

    return b;

  }
}
