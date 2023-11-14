import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

// Please do not buy all the tickets from a single seller

/**
 * Project 4: TestCases
 *
 * Utilizes custom test cases to ensure that the program runs as expected.
 *
 * @author Henry Lee, Shrish Mahesh, Rahul Siddharth, Lab Section L20
 * @version October 30, 2023
 */

public class TestCases {
    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(TestCase.class);
        if (result.wasSuccessful()) {
            System.out.println("Excellent - Test ran successfully");
        } else {
            for (Failure failure : result.getFailures()) {
                System.out.println(failure.toString());
            }
        }
    }

    public static class TestCase {
        private final PrintStream originalOutput = System.out;
        private final InputStream originalInput = System.in;

        @SuppressWarnings("FieldCanBeLocal")
        private ByteArrayInputStream testIn;

        @SuppressWarnings("FieldCanBeLocal")
        private ByteArrayOutputStream testOut;

        @Before
        public void outputStart() {
            testOut = new ByteArrayOutputStream();
            System.setOut(new PrintStream(testOut));
        }

        @After
        public void restoreInputAndOutput() {
            System.setIn(originalInput);
            System.setOut(originalOutput);
        }

        private String getOutput() {
            return testOut.toString();
        }

        @SuppressWarnings("SameParameterValue")
        private void receiveInput(String str) {
            testIn = new ByteArrayInputStream(str.getBytes());
            System.setIn(testIn);
        }

        @Test(timeout = 1000)
        public void invalidResponseTest() {
            String input = "d^3jw&uwh\n1\nshrishmahesh11@gmail.com\nCS180Project4!\n7";
            String expected = """
                    Welcome to Tickets@Purdue. Please enter a value to continue.
                    1. Login
                    2. Create a new account
                    Please enter a valid integer.
                    Please login.
                    Email:\s
                    Password:\s
                    Successfully logged in!
                    Welcome!  What would you like to do?
                    1. Sell a ticket
                    2. Edit a ticket
                    3. Remove a ticket
                    4. View History (Statistics)
                    5. Edit my account
                    6. Delete my account
                    7. Logout
                    Thank you for using Tickets@Purdue! Goodbye!
                    """;

            receiveInput(input);
            Marketplace.main(new String[0]);

            String stuOut = getOutput();
            stuOut = stuOut.replace("\r\n", "\n");

            Assert.assertEquals("Verify that the output matches!", expected, stuOut);
        }

        @Test(timeout = 1000)
        public void invalidAccountTest() {
            String input = """
                    1
                    invalidUsername
                    invalidPassword
                    shrishmahesh11@gmail.com
                    CS180Project4!
                    7""";
            String expected = """
                    Welcome to Tickets@Purdue. Please enter a value to continue.
                    1. Login
                    2. Create a new account
                    Please login.
                    Email:\s
                    Password:\s
                    Error.  Account not found.  Please try again.
                    Email:\s
                    Password:\s
                    Successfully logged in!
                    Welcome!  What would you like to do?
                    1. Sell a ticket
                    2. Edit a ticket
                    3. Remove a ticket
                    4. View History (Statistics)
                    5. Edit my account
                    6. Delete my account
                    7. Logout
                    Thank you for using Tickets@Purdue! Goodbye!
                    """;

            receiveInput(input);
            Marketplace.main(new String[0]);

            String stuOut = getOutput();
            stuOut = stuOut.replace("\r\n", "\n");

            Assert.assertEquals("Verify that the output matches!", expected, stuOut);
        }

        @Test(timeout = 1000)
        public void loggingOutAsBuyerTest() {
            String input = "1\nrasidd2005@gmail.com\nbasketball3!\n8";
            String expected = """
                    Welcome to Tickets@Purdue. Please enter a value to continue.
                    1. Login
                    2. Create a new account
                    Please login.
                    Email:\s
                    Password:\s
                    Successfully logged in!
                    Welcome!  What would you like to do?
                    1. Buy a ticket
                    2. View shopping cart
                    3. Remove from shopping cart
                    4. Checkout cart
                    5. View History (Statistics)
                    6. Edit my account
                    7. Delete my account
                    8. Logout
                    Thank you for using Tickets@Purdue! Goodbye!
                    """;
            receiveInput(input);
            Marketplace.main(new String[0]);

            String stuOut = getOutput();
            stuOut = stuOut.replace("\r\n", "\n");

            Assert.assertEquals("Verify that the output matches!", expected.trim(), stuOut.trim());
        }

        @Test(timeout = 1000)
        public void loggingOutAsSellerTest() {
            String input = "1\nshrishmahesh11@gmail.com\nCS180Project4!\n7";
            String expected = """
                    Welcome to Tickets@Purdue. Please enter a value to continue.
                    1. Login
                    2. Create a new account
                    Please login.
                    Email:\s
                    Password:\s
                    Successfully logged in!
                    Welcome!  What would you like to do?
                    1. Sell a ticket
                    2. Edit a ticket
                    3. Remove a ticket
                    4. View History (Statistics)
                    5. Edit my account
                    6. Delete my account
                    7. Logout
                    Thank you for using Tickets@Purdue! Goodbye!
                    """;

            receiveInput(input);
            Marketplace.main(new String[0]);

            String stuOut = getOutput();
            stuOut = stuOut.replace("\r\n", "\n");

            Assert.assertEquals("Verify that the output matches!", expected, stuOut);
        }


        @Test(timeout = 1000)
        public void createBuyerAccountTest() {
            String input = "2\n1\nhenryleebuyer@gmail.com\nHelloWorld7!\nHelloWorld7!\n8";
            String expected = """
                    Welcome to Tickets@Purdue. Please enter a value to continue.
                    1. Login
                    2. Create a new account
                    What type of account would you like to create? Press 1 for buyer or 2 for seller
                    Please enter an email:\s
                    Please enter a password:\s
                    Passwords must be at least 8 characters and at least 1 letter, number and special character
                    Please re-enter the password for confirmation.
                    Welcome!  What would you like to do?
                    1. Buy a ticket
                    2. View shopping cart
                    3. Remove from shopping cart
                    4. Checkout cart
                    5. View History (Statistics)
                    6. Edit my account
                    7. Delete my account
                    8. Logout
                    Thank you for using Tickets@Purdue! Goodbye!""";
            receiveInput(input);
            Marketplace.main(new String[0]);

            String stuOut = getOutput();
            stuOut = stuOut.replace("\r\n", "\n");

            Assert.assertEquals("Verify that the output matches!", expected.trim(), stuOut.trim());
        }

        @Test(timeout = 1000)
        public void createSellerAccountTest() {
            String input = "2\n2\nhenryleeseller@gmail.com\nHelloWorld7!\nHelloWorld7!\n7";
            String expected = """
                    Welcome to Tickets@Purdue. Please enter a value to continue.
                    1. Login
                    2. Create a new account
                    What type of account would you like to create? Press 1 for buyer or 2 for seller
                    Please enter an email:\s
                    Please enter a password:\s
                    Passwords must be at least 8 characters and at least 1 letter, number and special character
                    Please re-enter the password for confirmation.
                    Welcome!  What would you like to do?
                    1. Sell a ticket
                    2. Edit a ticket
                    3. Remove a ticket
                    4. View History (Statistics)
                    5. Edit my account
                    6. Delete my account
                    7. Logout
                    Thank you for using Tickets@Purdue! Goodbye!""";
            receiveInput(input);
            Marketplace.main(new String[0]);

            String stuOut = getOutput();
            stuOut = stuOut.replace("\r\n", "\n");

            Assert.assertEquals("Verify that the output matches!", expected.trim(), stuOut.trim());
        }

        @Test(timeout = 1000)
        public void buyTicketFromSellerAndShoppingCartTest() {
            String input = "1\nrasidd2005@gmail.com\nbasketball3!\n1\n1\n4\n1\n2\n4\n8";
            String expected = """
                    Welcome to Tickets@Purdue. Please enter a value to continue.
                    1. Login
                    2. Create a new account
                    Please login.
                    Email:\s
                    Password:\s
                    Successfully logged in!
                    Welcome!  What would you like to do?
                    1. Buy a ticket
                    2. View shopping cart
                    3. Remove from shopping cart
                    4. Checkout cart
                    5. View History (Statistics)
                    6. Edit my account
                    7. Delete my account
                    8. Logout
                    How do you want do view the listings:
                    Enter 1 to view all listings.
                    Enter 2 to view listings by store.
                    Enter 3 to view listings by sport.
                    Enter 4 to view listings by qty available
                                        
                    1:\s
                    Seller: mahesh49@purdue
                    Store: Mackay
                    Sport: Basketball
                    Location: Mackay Arena
                    Row/Section Area: Back row
                    Price: 6.00
                    Quantity: 6
                                       
                    2:\s
                    Seller: mahesh49@purdue
                    Store: Mackay
                    Sport: Basketball
                    Location: Mackay Arena
                    Row/Section Area: Middle
                    Price: 21.00
                    Quantity: 12
                                       
                                       
                    3:\s
                    Seller: shrishmahesh11@gmail.com
                    Store: RossAde
                    Sport: Basketball
                    Location: Mackay Arena
                    Row/Section Area:  Back row
                    Price: 10.00
                    Quantity: 2
                                       
                                       
                    4:\s
                    Seller: shrishmahesh11@gmail.com
                    Store: Mackay
                    Sport: Basketball
                    Location: Mackey Arena
                    Row/Section Area: Nosebleeds
                    Price: 4.00
                    Quantity: 5
                                   
                               
                    5:\s
                    Seller: henryleeseller@gmail.com
                    Store: Random Store
                    Sport: Basketball
                    Location: Mackay Arena
                    Row/Section Area: Nosebleeds
                    Price: 49.99
                    Quantity: 2    
                                                                            
                    What ticket number would you like to buy:\s
                    How many do you want to buy?:\s
                    Welcome!  What would you like to do?
                    1. Buy a ticket
                    2. View shopping cart
                    3. Remove from shopping cart
                    4. Checkout cart
                    5. View History (Statistics)
                    6. Edit my account
                    7. Delete my account
                    8. Logout
                    1:
                    Sport: Basketball
                    Location: Mackey Arena
                    Section: Nosebleeds
                    Price: 4.00
                    Quantity: 1
                    Welcome!  What would you like to do?
                    1. Buy a ticket
                    2. View shopping cart
                    3. Remove from shopping cart
                    4. Checkout cart
                    5. View History (Statistics)
                    6. Edit my account
                    7. Delete my account
                    8. Logout
                    Welcome!  What would you like to do?
                    1. Buy a ticket
                    2. View shopping cart
                    3. Remove from shopping cart
                    4. Checkout cart
                    5. View History (Statistics)
                    6. Edit my account
                    7. Delete my account
                    8. Logout
                    Thank you for using Tickets@Purdue! Goodbye!""";
            receiveInput(input);
            Marketplace.main(new String[0]);

            String stuOut = getOutput();
            stuOut = stuOut.replace("\r\n", "\n");

            Assert.assertEquals("Verify that the output matches!", expected.trim(), stuOut.trim());
        }

        @Test(timeout = 1000)
        public void createStoreAndTicketTest() {
            String input = "1\nhenryleeseller@gmail.com\nHelloWorld7!\n1\n1\nRandom Store\n2\n1\nBasketball\n"
                    + "Mackay Arena\nNosebleeds\n49.99\n2\n7";
            String expected = """
                    Welcome to Tickets@Purdue. Please enter a value to continue.
                    1. Login
                    2. Create a new account
                    Please login.
                    Email:\s
                    Password:\s
                    Successfully logged in!
                    Welcome!  What would you like to do?
                    1. Sell a ticket
                    2. Edit a ticket
                    3. Remove a ticket
                    4. View History (Statistics)
                    5. Edit my account
                    6. Delete my account
                    7. Logout
                    You have no stores currently. Would you like to add a store?
                    1. Yes
                    2. No
                    Enter the name of your store
                    What would you like to do?
                    1. Add store
                    2. Add ticket
                    3. Neither
                    Which store would you like to add your ticket to?
                    1. Random Store
                    Please enter the sport:
                    Please enter the location:
                    Please enter the section:
                    Please enter the price:
                    How many tickets would you like to sell?
                    Welcome!  What would you like to do?
                    1. Sell a ticket
                    2. Edit a ticket
                    3. Remove a ticket
                    4. View History (Statistics)
                    5. Edit my account
                    6. Delete my account
                    7. Logout
                    Thank you for using Tickets@Purdue! Goodbye!""";
            receiveInput(input);
            Marketplace.main(new String[0]);

            String stuOut = getOutput();
            stuOut = stuOut.replace("\r\n", "\n");

            Assert.assertEquals("Verify that the output matches!", expected.trim(), stuOut.trim());
        }

        @Test(timeout = 1000)
        public void buyTicketAndDeleteTicketTest() {
            String input = "1\nrasidd2005@gmail.com\nbasketball3!\n1\n1\n4\n1\n3\n1\n2\n8";
            String expected = """
                    Welcome to Tickets@Purdue. Please enter a value to continue.
                    1. Login
                    2. Create a new account
                    Please login.
                    Email:\s
                    Password:\s
                    Successfully logged in!
                    Welcome!  What would you like to do?
                    1. Buy a ticket
                    2. View shopping cart
                    3. Remove from shopping cart
                    4. Checkout cart
                    5. View History (Statistics)
                    6. Edit my account
                    7. Delete my account
                    8. Logout
                    How do you want do view the listings:
                    Enter 1 to view all listings.
                    Enter 2 to view listings by store.
                    Enter 3 to view listings by sport.
                    Enter 4 to view listings by qty available      
                              
                    1:\s
                    Seller: mahesh49@purdue
                    Store: Mackay
                    Sport: Basketball
                    Location: Mackay Arena
                    Row/Section Area: Back row
                    Price: 6.00
                    Quantity: 6
                                       
                    2:\s
                    Seller: mahesh49@purdue
                    Store: Mackay
                    Sport: Basketball
                    Location: Mackay Arena
                    Row/Section Area: Middle
                    Price: 21.00
                    Quantity: 12
                                       
                                       
                    3:\s
                    Seller: shrishmahesh11@gmail.com
                    Store: RossAde
                    Sport: Basketball
                    Location: Mackay Arena
                    Row/Section Area:  Back row
                    Price: 10.00
                    Quantity: 2
                                       
                                       
                    4:\s
                    Seller: shrishmahesh11@gmail.com
                    Store: Mackay
                    Sport: Basketball
                    Location: Mackey Arena
                    Row/Section Area: Nosebleeds
                    Price: 4.00
                    Quantity: 4           
                                        
                                        
                    5:\s
                    Seller: henryleeseller@gmail.com
                    Store: Random Store
                    Sport: Basketball
                    Location: Mackay Arena
                    Row/Section Area: Nosebleeds
                    Price: 49.99
                    Quantity: 2
                                        
                    What ticket number would you like to buy:\s
                    How many do you want to buy?:\s
                    Welcome!  What would you like to do?
                    1. Buy a ticket
                    2. View shopping cart
                    3. Remove from shopping cart
                    4. Checkout cart
                    5. View History (Statistics)
                    6. Edit my account
                    7. Delete my account
                    8. Logout
                    Pick an item to remove:
                    1: 
                    Sport: Basketball
                    Location: Mackey Arena
                    Section: Nosebleeds
                    Price: 4.00
                    Quantity: 1
                    Welcome!  What would you like to do?
                    1. Buy a ticket
                    2. View shopping cart
                    3. Remove from shopping cart
                    4. Checkout cart
                    5. View History (Statistics)
                    6. Edit my account
                    7. Delete my account
                    8. Logout             
                    Cannot view shopping cart because it is empty!
                    Welcome!  What would you like to do?
                    1. Buy a ticket
                    2. View shopping cart
                    3. Remove from shopping cart
                    4. Checkout cart
                    5. View History (Statistics)
                    6. Edit my account
                    7. Delete my account
                    8. Logout             
                    Thank you for using Tickets@Purdue! Goodbye!""";
            receiveInput(input);
            Marketplace.main(new String[0]);

            String stuOut = getOutput();
            stuOut = stuOut.replace("\r\n", "\n");

            Assert.assertEquals("Verify that the output matches!", expected.trim(), stuOut.trim());
        }

        @Test(timeout = 1000)
        public void viewBuyerDashboardTest() {
            String input = "1\nrasidd2005@gmail.com\nbasketball3!\n5\n8";
            String expected = """
                    Welcome to Tickets@Purdue. Please enter a value to continue.
                    1. Login
                    2. Create a new account
                    Please login.
                    Email:\s
                    Password:\s
                    Successfully logged in!
                    Welcome!  What would you like to do?
                    1. Buy a ticket
                    2. View shopping cart
                    3. Remove from shopping cart
                    4. Checkout cart
                    5. View History (Statistics)
                    6. Edit my account
                    7. Delete my account
                    8. Logout
                    1:
                    Sport: Basketball
                    Location: Mackey Arena
                    Section: Nosebleeds
                    Price: 4.00
                    Quantity: 1
                    Welcome!  What would you like to do?
                    1. Buy a ticket
                    2. View shopping cart
                    3. Remove from shopping cart
                    4. Checkout cart
                    5. View History (Statistics)
                    6. Edit my account
                    7. Delete my account
                    8. Logout
                    Thank you for using Tickets@Purdue! Goodbye!""";
            receiveInput(input);
            Marketplace.main(new String[0]);

            String stuOut = getOutput();
            stuOut = stuOut.replace("\r\n", "\n");

            Assert.assertEquals("Verify that the output matches!", expected.trim(), stuOut.trim());
        }
    }
}
