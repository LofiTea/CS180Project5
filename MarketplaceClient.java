import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Program Name
 * <p>
 * brief description of the program
 *
 * @author Shrish Mahesh, L20
 * @version date
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

//        Socket socket = new Socket("localhost", 8080);
//        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
//        Scanner scan = new Scanner(System.in);
//
        LoginInGUI loginInGUI = new LoginInGUI();
        //System.out.println("Starting new gui stack");
        loginInGUI.run();

//
//
//        System.out.println("Welcome to Tickets@Purdue. Please enter a value to continue.");
//        String email = null;
//        String password = null;
//        int id = 0;
//        int optionInitial = 0;
//        System.out.println("1. Login\n" + "2. Create a new account");
//        do {
//            try {
//                optionInitial = Integer.parseInt(scan.nextLine());
//                if (optionInitial < 1 || optionInitial > 2) {
//                    System.out.println("Please enter a valid number.");
//                }
//            } catch (NumberFormatException e) {
//                System.out.println("Please enter a valid integer.");
//            }
//        } while (optionInitial < 1 || optionInitial > 2);
//
//        oos.writeObject(optionInitial);
//        oos.flush();
//
//
//        switch (optionInitial) {
//            case 1:
//                System.out.println("Please login.");
//                boolean notLoggedIn = true;
//                while (notLoggedIn) {
//                    System.out.println("Email: ");
//                    email = scan.nextLine();
//                    System.out.println("Password: ");
//                    password = scan.nextLine();
//                    oos.writeObject(email);
//                    oos.writeObject(password);
//                    oos.flush();
//
//                    notLoggedIn = (boolean) ois.readObject();
//                    System.out.println((String) ois.readObject());
//
//                }
//
//                break;
//            case 2:
//                int option2;
//                boolean invalidChoice = false;
//                int accountChoice = 0;
//                boolean invalidEmail = false;
//                boolean invalidPassword = false;
//                String password2;
//
//                System.out.println("What type of account would you like to create? Press 1 for buyer or 2 for " +
//                        "seller");
//
//                do {
//                    try {
//                        accountChoice = Integer.parseInt(scan.nextLine());
//                        if (accountChoice < 1 || accountChoice > 2) {
//                            System.out.println("Please enter a valid number");
//                            invalidChoice = true;
//                        } else {
//                            invalidChoice = false;
//                        }
//                    } catch (NumberFormatException e) {
//                        System.out.println("Please enter a valid integer");
//                        invalidChoice = true;
//                    }
//                } while (invalidChoice);
//
//
//                System.out.println("Please enter an email: ");
//                do {
//                    email = scan.nextLine();
//                    if (!email.contains("@")) {
//                        invalidEmail = true;
//                        System.out.println("Invalid email. Email must contain an @symbol.");
//                        System.out.println("Please enter the email again: ");
//                    } else {
//                        invalidEmail = false;
//                    }
//                } while (invalidEmail);
//
//                System.out.println("Please enter a password: ");
//                System.out.println("Passwords must be at least 8 characters and at least 1 letter, number and special" +
//                        " character");
//
//                do {
//                    password = scan.nextLine();
//                    if (password.length() < 8 || !isStrongAlphanumeric(password)) {
//                        invalidPassword = true;
//                        System.out.println("Invalid password. Please check your password is 8 or more " +
//                                "characters and has a letters, number and special character.");
//                        System.out.println("Please enter the password again: ");
//                    } else {
//                        invalidPassword = false;
//                        if (!isStrongAlphanumeric(password)) {
//                            System.out.println("Invalid password, must have letters, numbers and special characters.");
//                            System.out.println("Please enter the password again: ");
//                            invalidPassword = true;
//                        }
//                    }
//                } while (invalidPassword);
//
//                System.out.println("Please re-enter the password for confirmation.");
//                do {
//                    password2 = scan.nextLine();
//
//                    if (password.equals(password2)) {
//                        invalidPassword = false;
//                    } else {
//                        System.out.println("Password does not match, please re-enter the password.");
//                        invalidPassword = true;
//                    }
//                } while (invalidPassword);
//
//                oos.writeObject(email);
//                oos.writeObject(password);
//                oos.writeObject(accountChoice);
//                oos.flush();
//
//
//                // String filename2 = "";
//                // if (role.equals("b")) {
//                //     filename2 = "BuyerHistory.txt";
//                //     writeFile(userInfo, filename2);
//                // }
//        }
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

    public void sendOptionInitial(int optionInitial) throws IOException {
//        oos.writeObject(optionInitial);
//        oos.flush();
    }

    synchronized public boolean sendLogin(String email, String password, int optionInitial) throws IOException,
            ClassNotFoundException {
        //System.out.println("I am in here");
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
            ID = (String)ois.readObject();
            email = (String) ois.readObject();
            type = (String) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "ID: " + ID + "\nEmail: " + email + "\nAccount Type: " + type;
    }

    synchronized public String getString() throws IOException, ClassNotFoundException {
        return (String) ois.readObject();
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


}
