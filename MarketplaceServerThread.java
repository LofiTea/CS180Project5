/**
 * Program Name
 * <p>
 * brief description of the program
 *
 * @author Shrish Mahesh, L20
 * @version date
 */

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MarketplaceServerThread extends Thread {
    public static Object obj = new Object();
    private static final ReadWriteLock lock = new ReentrantReadWriteLock();

    Socket socket;


    MarketplaceServerThread(Socket socket) {
        this.socket = socket;
    }


    @Override
    public void run() {
        try {
            String email = null;
            String password = null;
            int id = 0;
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            boolean notLoggedIn = true;
            String role = "";


            while (notLoggedIn) {
                int optionInitial = (Integer) ois.readObject();
                switch (optionInitial) {
                    case 1:
                        boolean successful = false;
                        //while (notLoggedIn) {
                        String message = "";
                        email = (String) ois.readObject();
                        password = (String) ois.readObject();

                        //System.out.println(email2 + password2);
                        LoginInfo user = new LoginInfo(email, password);
                        String currentAuthentication = user.authenticate(email, password);
                        if (!currentAuthentication.isEmpty()) {
                            successful = true;
                            message = "Successfully logged in!";
                            String[] userInfo = user.authenticate(email, password).split(",");
                            id = Integer.parseInt(userInfo[0]);
                            notLoggedIn = false;

                        } else {
                            message = "Error.  Account not found.  Please try again.";

                        }

                        //oos.writeObject(notLoggedIn);
                        oos.writeObject(successful);
                        if (successful) {
                            role = MarketplaceServer.determineRole(email, password);
                            oos.writeObject(role);
                            //System.out.println(notLoggedIn);
                        }
                        oos.flush();
                        // }

                        break;
                    case 2:
                        email = (String) ois.readObject();
                        password = (String) ois.readObject();
                        System.out.println(email + password);
                        String accountChoice = (String) ois.readObject();

                        // System.out.println(email + password + accountChoice);


                        ArrayList<String> fileInfo = MarketplaceServer.readFile("LoginInfo.txt");

                        ArrayList<String> userInfo = new ArrayList<>();
                        String filename;

                        if (accountChoice.equals("b")) {
                            role = "b";
                            filename = "BuyerInfo.txt";
                        } else {
                            role = "s";
                            filename = "SellerInfo.txt";
                        }


                        userInfo = MarketplaceServer.readFile(filename);

                        if (fileInfo.isEmpty()) {
                            fileInfo.add(String.format("1,%s,%s,%s", email, password, role));
                            userInfo.add(String.format("1,%s", email));
                            id = 1;
                        } else {
                            System.out.println("hello");
                            String[] lastLine = fileInfo.get(fileInfo.size() - 1).split(",");


                            fileInfo.add(String.format("%d,%s,%s,%s", Integer.parseInt(lastLine[0]) + 1, email,
                                    password, role));

                            id = Integer.parseInt(lastLine[0]) + 1;
                            userInfo.add(String.format("%d,%s", Integer.parseInt(lastLine[0]) + 1, email));
                        }
                        MarketplaceServer.writeFile(userInfo, filename);
                        MarketplaceServer.writeFile(fileInfo, "LoginInfo.txt");


                        if (role.equals("b")) {
                            String filename2 = "BuyerHistory.txt";

                            ArrayList<String> history = MarketplaceServer.readFile(filename2);
                            history.add(String.format("%d,%s", id, email));

                            MarketplaceServer.writeFile(userInfo, filename2);
                        }

                        notLoggedIn = false;

                }

            }

            boolean notLoggedOut = true;
            while (notLoggedOut) {
                switch (role) {
                    case "b":
                        //wip
                        break;
                    case "s":
                        System.out.println("Before waiting for object");
                        int whatDash = (int) ois.readObject();
                        System.out.println(whatDash);
                        switch (whatDash) {
                            case 1:
                                break;
                            case 2:
                              Sellers curSellers = new Sellers(id);
                              boolean exitCurrentMenu = false;

                              while(!exitCurrentMenu)
                              {
                                ArrayList<String> curStores =  curSellers.retrieveStores();
                                oos.writeObject(curStores);

                              }       
                                break;
                            case 3:
                                break;
                            case 4:
                                boolean notReturnedToMenu = true;
                                while (notReturnedToMenu) {
                                    email = MarketplaceServer.getEmailPassword(id).get(0);
                                    password = MarketplaceServer.getEmailPassword(id).get(1);
                                    System.out.println(email+password);
                                    int whatEditOption = (int) ois.readObject();
                                    System.out.println(whatEditOption);
                                    switch (whatEditOption) {
                                        case 1:
                                            ArrayList<String> details = MarketplaceServer.readFile("LoginInfo.txt");
                                            for (int i = 0; i < details.size(); i++) {
                                                String[] arr = details.get(i).split(",");
                                                if (email.equals(arr[1]) && password.equals(arr[2])) {
                                                    oos.writeObject(arr[0]);
                                                    oos.writeObject(arr[1]);


                                                    if (arr[3].equals("b")) {
                                                        oos.writeObject("Buyer");
                                                    } else {
                                                        oos.writeObject("Seller");
                                                    }
                                                }
                                            }

                                            break;
                                        case 2:
                                            String newEmail = (String) ois.readObject();
                                            if (!newEmail.equals("")) {
                                                MarketplaceServer.editEmail(id, newEmail);
                                                //Thread.sleep(1000);
//                                                email = MarketplaceServer.getEmailPassword(id).get(0);
//
//
//
//                                                System.out.println(email);
                                            }
                                            //editing the email
                                            break;
                                        case 3:
                                            String newPassword = (String) ois.readObject();
                                            System.out.println(newPassword);
                                            if (newPassword != null) {
                                                synchronized (obj) {
                                                    MarketplaceServer.editPassword(email, password, newPassword);
                                                    password = newPassword;
                                                }
                                            }

                                            break;
                                        case 4:
                                            String confirmation = (String) ois.readObject();
                                            if (confirmation.equals("yes")) {
                                                MarketplaceServer.deleteAccount(email, password);
                                                notLoggedOut = false;
                                                notReturnedToMenu = false;
                                            }
                                            break;
                                        case 5:
                                            oos.writeObject(role);
                                            notReturnedToMenu = false;
                                            break;

                                    }

                                }
                                break;
                            case 5:
                                System.out.println("Thread closing");
                                notLoggedOut = false;
                        }
                }
            }

            // System.out.println("Out");
        } catch (Exception e) {

        }

    }


}