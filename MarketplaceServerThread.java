/**
 * Program Name
 * <p>
 * brief description of the program
 *
 * @author Shrish Mahesh, L20
 * @version date
 */

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class MarketplaceServerThread extends Thread {
  Socket socket;



  MarketplaceServerThread(Socket socket)
  {
    this.socket = socket;
  }


  @Override 
  public void run()
  {
    try{
      String email = null;
      String password = null;
      int id = 0;
      ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
      ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

      int optionInitial = (Integer) ois.readObject();
    
      switch(optionInitial) {
        case 1:
            
            boolean notLoggedIn = true;

            while(notLoggedIn) {
                String message = "";
                email = (String)ois.readObject();
                password = (String)ois.readObject();
                //System.out.println(email2 + password2);
                LoginInfo user = new LoginInfo(email, password);
                String currentAuthentication = user.authenticate(email, password);
                if (!currentAuthentication.isEmpty()) {
                    message = "Successfully logged in!";
                    String[] userInfo = user.authenticate(email, password).split(",");
                    id = Integer.parseInt(userInfo[0]);
                    notLoggedIn = false;

                } else {
                    message = "Error.  Account not found.  Please try again.";
            
                }
            
                oos.writeObject(notLoggedIn);
                oos.writeObject(message);
                oos.flush();
            }
            
            break;
        case 2: 
            email = (String)ois.readObject();
            password = (String)ois.readObject();
            int accountChoice = (int)ois.readObject();
            


            ArrayList<String> fileInfo = MarketplaceServer.readFile("LoginInfo.txt");
            ArrayList<String> userInfo = new ArrayList<>();
            String role;
            String filename;

            if (accountChoice == 1) {
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
                String[] lastLine = fileInfo.get(fileInfo.size() - 1).split(",");

                fileInfo.add(String.format("%d,%s,%s,%s", Integer.parseInt(lastLine[0]) + 1, email,
                        password, role));
                id = Integer.parseInt(lastLine[0]) + 1;
                userInfo.add(String.format("%d,%s", Integer.parseInt(lastLine[0]) + 1, email));
            }
            MarketplaceServer.writeFile(userInfo, filename);
            MarketplaceServer.writeFile(fileInfo, "LoginInfo.txt");
      }

    }
    catch(Exception e)
    {

    }

  }

}