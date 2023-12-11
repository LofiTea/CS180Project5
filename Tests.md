# Test Case 1: Create Buyer Account

Steps:
1. The user launches the application
2. The user hits the create a new account button
3. The user selects the account type: Buyers
4. The user enters their email: ticketbuyer@gmail.com
5. The user enters a password: HelloWorld7!
6. The user re-enters their password
7. The user creates a new account

Expected Result: The application creates a new buyer account with the entered email and password and loads the buyer dashboard.

Test Status: Passed

# Test Case 2: Create Seller Account

Steps:
1. The user launches the application
2. The user hits the create a new account button
3. The user selects the account type: Sellers
4. The user enters their email: shrishm2023@gmail.com
5. The user enters a password: Class2027!
6. The user re-enters their password
7. The user creates a new account

Expected Result: The application creates a new seller account with the entered email and password and loads the seller dashboard.

Test Status: Passed

# Test Case 3: Buyer User Login

Steps: 
1. The user launches the application
2. The user enters their email: rasidd2005@gmail.com
3. The user enters their password: basketball!
4. The user logins in

Expected Result: The application logs in with the login information and loads the buyer dashboard.

Test Status: Passed

# Test Case 4: Seller User Login

Steps: 
1. The user launches the application
2. The user enters their email: shrishmahesh11@gmail.com
3. The user enters their password: CS180Project4!
4. The user logins in

Expected Result: The application logs in with the login information and loads the seller dashboard.

Test Status: Passed

# Test Case 5: Buy a ticket (view all listings)

Steps: 
1. The user launches the application
2. The user enters their email: rasidd2005@gmail.com
3. The user enters their password: basketball3!
4. The user logs in
5. The user clicks buy ticket in the buyer dashboard
6. The user clicks buy ticket in the buy ticket menu
7. The user chooses view all listings
8. The user clicks the third option in the dropdown of tickets
9. The user buys 2

Expected Result: The application displays an option pane saying "Ticket purchase successful". When clicking return to Buy Ticket Menu and then View Shopping Cart, the ticket is added to the dropdown (added to current shopping cart)

Test Status: Passed

# Test Case 6: Remove a ticket from shopping cart

Steps: 
1. The user launches the application
2. The user enters their email: rasidd2005@gmail.com
3. The user enters their password: basketball3!
4. The user logs in
5. The user clicks buy ticket in the buyer dashboard
6. The user clicks remove ticket in the buy ticket menu
7. The user chooses a ticket in the dropdown
8. The user clicks delete ticket
9. The user clicks yes when the option pane asks to confirm if they're sure

Expected Result: The application displays an option pane saying "Item successfully deleted". If there are no tickets in the shopping cart, after option 6 it should display an option pane saying "Error! Shopping cart is empty!"

Test Status: Passed

# Test Case 7: Checkout cart

Steps: 
1. The user launches the application
2. The user enters their email: rasidd2005@gmail.com
3. The user enters their password: basketball3!
4. The user logs in
5. The user clicks buy ticket in the buyer dashboard
6. The user clicks checkout cart in the buy ticket menu
7. The user clicks yes when asked to confirm

Expected Result: The application displays an option pane saying "Checkout successful!". If there are no tickets in the shopping cart, after option 6 it should display an option pane saying "Error! Shopping cart is empty!"

Test Status: Passed

# Test Case 8: View buyer statistics (customer specific)

Steps: 
1. The user launches the application
2. The user enters their email: rasidd2005@gmail.com
3. The user enters their password: basketball3!
4. The user logs in
5. The user selects "Personal Store Transaction Data in the dropdown"
6. The user clicks "See selected item"

Expected Result: The application displays a scroll pane with a sports, the stores and respective number of tickets bought from them

Test Status: Passed


# Test Case 9: Sell a ticket

Steps: 
1. The user launches the application
2. The user enters their email: shrishmahesh11@gmail.com
3. The user enters their password: CS180Project4! 
4. The user logs in
5. The user clicks sell ticket in the seller dashboard
6. The user clicks sell ticket in the sell ticket menu
7. The user selects add ticket and presses ok
8. The user selects Mackay and presses ok
9. The user enters the ticket information (store, location, section, price, quantity)
10. The user clicks create ticket

Expected Result: The application clears the text fields. If the user goes back to the sell ticket menu, the added ticket is visible for editing and removal.

Test Status: Passed

# Test Case 10: Edit a ticket

Steps: 
1. The user launches the application
2. The user enters their email: shrishmahesh11@gmail.com
3. The user enters their password: CS180Project4! 
4. The user logs in
5. The user clicks sell ticket in the seller dashboard
6. The user clicks edit ticket in the sell ticket menu
8. The user selects Mackay and presses ok
9. The user selects the ticket added above and presses ok
10. The user selects Quantity and presses ok
11. The user enters a new quantity
12. The user clicks edit ticket

Expected Result: The application displays an option pane saying "Ticket edited successfully!"

Test Status: Passed

# Test Case 11: Remove a ticket

Steps: 
1. The user launches the application
2. The user enters their email: shrishmahesh11@gmail.com
3. The user enters their password: CS180Project4! 
4. The user logs in
5. The user clicks sell ticket in the seller dashboard
6. The user clicks remove ticket in the sell ticket menu
8. The user selects Mackay and presses ok
9. The user selects the ticket edited above and presses ok

Expected Result: The application displays an option pane saying "Ticket deleted successfully!"

Test Status: Passed

# Test Case 12: View seller history

Steps: 
1. The user launches the application
2. The user enters their email: shrishmahesh11@gmail.com
3. The user enters their password: CS180Project4! 
4. The user logs in
5. The user clicks view seller history in the seller dashboard
6. The user selects Mackay and presses see selected item

Expected Result: The application displays a scroll pane containing the seller history for that store

Test Status: Passed

# Test Case 13: View account details

Steps: 
1. The user launches the application
2. The user enters their email: shrishmahesh11@gmail.com
3. The user enters their password: CS180Project4! 
4. The user logs in
5. The user clicks view account settings in the seller dashboard
6. The user clicks view account details

Expected Result: The application displays an option pane containing the ID, email and account type of the user

Test Status: Passed

# Test Case 14: Edit password

Steps: 
1. The user launches the application
2. The user enters their email: henryleeseller@gmail.com
3. The user enters their password: basketball3!
4. The user logs in
5. The user clicks view account settings in the seller dashboard
6. The user clicks edit password
7. The user enters basketball4! and presses ok

Expected Result: The application changes the user's password, this is updated in the LoginInfo.txt file

Test Status: Passed

# Test Case 15: Logout

Steps: 
1. The user launches the application
2. The user enters their email: henryleeseller@gmail.com
3. The user enters their password: basketball3!
4. The user logs in
5. The user clicks Logout in the seller dashboard

Expected Result: The application displays and option pane saying "Thank you for using Tickets@Purdue! We hope to see you soon!"

Test Status: Passed

