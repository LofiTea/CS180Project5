import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Project 5: MarketplaceClient
 *
 * Holds the client for the marketplace.
 *
 * @author Shrish Mahesh, Rahul Siddharth, Lab Section L20
 * @version November 13, 2023
 */

public class MarketplaceClient {
    public static MarketplaceClient client;

    Socket socket = new Socket("localhost", 8080);

    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

    public MarketplaceClient() throws IOException {
    }

    public static MarketplaceClient getClient() {
        return client;
    }

    public static void main(String[] args) throws Exception {
        LoginInGUI loginInGUI = new LoginInGUI();
        loginInGUI.run();
    }

    public static boolean isStrongAlphanumeric(String password) {
        int letterCount = 0;
        int numCount = 0;
        int specialCount = 0;
        for (int i = 0; i < password.length(); i++) {
            char currentLetter = password.charAt(i);
            if (Character.isLetter(currentLetter)) {
                letterCount++;
            } else if (Character.isDigit(currentLetter)) {
                numCount++;
            } else {
                specialCount++;
            }
        }
        return letterCount != 0 && numCount != 0 && specialCount != 0;
    }
    synchronized public boolean sendLogin(String email, String password, int optionInitial) throws IOException,
            ClassNotFoundException {
        oos.writeObject(optionInitial);
        oos.writeObject(email);
        oos.writeObject(password);
        oos.flush();

        boolean success = (boolean) ois.readObject();


        return success;

    }

    synchronized public void createAccount(String email, String password, int optionInitial, String type) throws IOException,
            ClassNotFoundException {
        oos.writeObject(optionInitial);
        oos.writeObject(email);
        oos.writeObject(password);
        oos.writeObject(type);
        oos.flush();

    }

    synchronized public String receiveAccountDetails() {
        String ID = null;
        String email = null;
        String type = null;
        try {
            ID = (String) ois.readObject();
            email = (String) ois.readObject();
            type = (String) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "ID: " + ID + "\nEmail: " + email + "\nAccount Type: " + type;
    }

    synchronized public String getString() {
        try {
            return (String) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    synchronized public void sendString(String str) {
        try {
            oos.writeObject(str);
            oos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    synchronized public void sendInt(int x) {
        try {
            oos.writeObject(x);
            oos.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    synchronized public void sendBoolean(boolean bool) {
        try {
            oos.writeObject(bool);
            oos.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    synchronized ArrayList<String> recieveStringArrayList() {
        try {
            return (ArrayList<String>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    synchronized public void sendTicketInfo(String sport, String location, String section, double price, int quantity
            , String store) {
        try {
            oos.writeObject(sport);
            oos.writeObject(location);
            oos.writeObject(section);
            oos.writeObject(price);
            oos.writeObject(quantity);
            oos.writeObject(store);
            oos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    synchronized public void sendModTicket(String sport, String location, String section, double price) {
        try {
            oos.writeObject(sport);
            oos.writeObject(location);
            oos.writeObject(section);
            oos.writeObject(price);
            oos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    synchronized public boolean receiveBoolean() {
        boolean bool = false;
        try {
            bool = (boolean) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bool;
    }


}
