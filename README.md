# CS180 Project 5

For this project, we decided to implement a ticket-sharing market where sellers can sell any tickets to a sports game that they have to willing buyers.  Sellers can decide at what price they decide to sell their tickets while buyers can choose what type of ticket they want to buy.  Server-client interactions, concurrency, and graphical user interfaces were implemented.

Instructions: To get into the marketplace, an email and password are needed to gain access to the marketplace.  Start MarketplaceServer.java and MarketplaceClient.java to start the client and server.  

Important note: To run on different machines, follow the instructions below
Run the MarketplaceServer on a device. All clients and server must be connected to the same Wifi network. If the device is a Windows machine, type ipconfig in the command prompt and get the Ipv4 address. If it is a Mac, get the address by going into network in system preferences and details of the current network, get the IP address from there. For all clients, (unless running on the same device as the server), replace Socket socket = new Socket("localhost", 8080); with Socket socket = new Socket("[serverIP]", 8080); (note that the square brackets represent a placeholder and the actual IP address of the server must be entered. 

The program starts once MarketplaceClient.java starts. Once the program starts, a login page will show up.  On the bottom is a button to create a new account.  Either a buyer or a seller account can be created.  When creating a new account, please make a valid email structure with the '@' character (it does not have to be a real email.  For example, it can be purduecsstudent@gmail.com) and a password that has at least 8 characters that have at least 1 letter, number, and special character (this measure is taken to ensure a user has a strong password).  Once logged in, a menu dashboard will appear (a buyer menu will appear for a buyer account and a seller menu will appear for a seller account) with different options. The buyer dashboard will have options to buy a ticket while the seller dashboard will have options to create a store and sell a ticket. 

Submissions: Henry turned in the report, the video presentation, and the Vocareum workspace.  All were turned in on Brightspace. 

List of Classes:
- AccountGUI.java: This class allows a user to view their account settings, edit their corresponding email or password, or delete their accounts.
- BuyerDashboardGUI.java: This class is the buyer dashboard.  It allows a buyer to buy a ticket, view their previous buying history, or view their relevant statistics.  It is typically the first thing a buyer will see when they log in.
- BuyerHistoryGUI.java: This class shows a buyer's shopping history.  It is the second option on the buyer dashboard menu.
- Buyers.java: This class showcases how buyers can buy tickets and manage their shopping cart of tickets they want to buy. Marketplace.java utilizes this class to ensure that a user can buy a ticket.
- BuyerStatisticsGUI.java: This class shows a buyer's statistics.  It is the third option on the buyer dashboard menu.
- BuyTicketGUI.java: This class allows a buyer to buy a ticket.  They can sort the different listings available on the application.
- BuyTicketMenuGUI.java: This class allows a buyer to buy a ticket, view their shopping cart, remove a ticket from their shopping cart, or checkout.
- CartItems.java: This class organizes items into a shopping cart where buyers can see what tickets are bought from sellers in the marketplace.  Both Buyers.java and Sellers.java utilize this class to organize which tickets are being bought and which tickets are being sold.
- CreateAccountGUI.java: This class allows a new user to create an account on the marketplace application.
- DoubleArrayList.java: This class returns a list of available tickets to buy from a seller and a list of the names of all stores.  Buyers.java and the main class utilize this class to show what sellers are available in the marketplace.
- LoginInfo.java: This class ensures that the user logging into the marketplace is valid and has an account in the system. This prevents any unauthorized users from logging into the marketplace.
- LoginInGUI.java: This class allows a user to log in to the application or create a new account.
- MarketplaceClient.java: This class holds the client for the marketplace.
- MarketplaceServer.java: This class holds the server for the marketplace.
- MarketplaceServerThread.java: This class utilizes threads for the marketplace server.
- PrintableStat.java: This class was created to help display seller statistics.
- RemoveTicketGUI.java: This class allows a buyer to remove a ticket from their shopping cart.
- SellerDashboardGUI.java: This class is the seller dashboard.  It allows a seller to sell a ticket, view a store's shopping history, or view their relevant statistics.  It is typically the first thing a seller will see when they log in.
- SellerEditTicketGUI: This class allows a seller to edit a ticket or store.
- SellerHistoryGUI.java: This class shows a seller's history.  It involves the buying history of the different stores of each seller.  It is the second option of the seller dashboard menu.
- SellerListing.java: This class returns a list of tickets and their available quantities from a store.  Buyers.java and the main class utilize this class to show the description of a store for a buyer wanting to buy a ticket.
- Sellers.java: This class showcases how sellers can sell tickets and manage their store inventory.  Marketplace.java utilizes this class to ensure that a user can sell a ticket. For testing, TestCases.java is run to confirm that everything in the class works.
- SellerStatisticsGUI.java: This class returns a seller's statistics.  It is the third option of the seller dashboard menu.
- SellTicketGUI.java: This class allows a seller to sell a ticket.  They can either create a store or create a ticket and then put that ticket into a store.
- SellTicketMenuGUI.java: This class allows a seller to sell a ticket, edit a store or ticket, or remove a ticket or store.
- StoreNameIDCombo.java: This class returns the name and ID number of a store.  Buyers.java and the main class utilize this class to show the details of a store for buyers.
- Ticket.java: This class showcases the information regarding a particular ticket.  This class is utilized in both Buyers.java and Sellers.java, where tickets to different sports games on campus are available to buy or sell on the marketplace.
- TicketInfoCombo.java: This class returns the type of ticket a buyer can buy and how many of them are available for purchase.  Buyers.java and the main class utilize this class to show buyers the list of tickets that are available for purchase in the marketplace. 
