import java.util.ArrayList;

/**
 * Project 5: LoginInfo
 * <p>
 * This class authenticates a user to enter the marketplace.  They need a valid username and password.
 *
 * @author Henry J. Lee, Lab Section L20
 * @version November 13, 2023
 */

public class LoginInfo {
    private String email;
    private String password;

    public LoginInfo(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getters are set up so that a user can see what their username and password is
    public String getEmail() {
        return email;
    }

    // Setters are made so that a user can change either their username or password if they desired
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    // This method checks to see whether a user is valid or not
    public String authenticate(String email, String password) {
        ArrayList<String> arrayList = MarketplaceServer.readFile("LoginInfo.txt");
        for (int i = 0; i < arrayList.size(); i++) {
            String[] userInfo = arrayList.get(i).split(",");
            if (userInfo.length != 4) {
                return "";
            }
            String check1 = userInfo[1];
            String check2 = userInfo[2];
            if (check1.equals(email)) {
                if (check2.equals(password)) {
                    return arrayList.get(i);
                }
            }
        }
        return "";
    }
}
