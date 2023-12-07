# CS180 Project 5

For this project, we decided to implement a ticket-sharing market where sellers can sell any tickets to a sports game that they have to willing buyers.  Sellers can decide at what price they decide to sell their tickets while buyers can choose what type of ticket they want to buy.  Server-client interactions, concurrency, and graphical user interfaces were implemented.

Instructions: To get into the marketplace, an email and password are needed to gain access to the marketplace.  Once the program starts, a login page will show up.  On the bottom is a button to create a new account.  Either a buyer or a seller account can be created.  When creating a new account, please make a valid email structure with the '@' character (it does not have to be a real email.  For example, it can be purduecsstudent@gmail.com) and a password that has at least 8 characters that have at least 1 letter, number, and special character (this measure is taken to ensure a user has a strong password).  Once logged in, a menu dashboard will appear (a buyer menu will appear for a buyer account and a seller menu will appear for a seller account) with different options. The buyer dashboard will have options to buy a ticket while the seller dashboard will have options to create a store and sell a ticket. (Maybe add more?)

Submissions: Henry turned in the report and the Vocareum workspace.  Both were turned in on Brightspace. (Someone else turns in the video presentation maybe?)

List of Classes:
- AccountGUI.java:
- BuyerDashboardGUI.java:
- BuyerHistoryGUI.java:
- Buyers.java: This class showcases how buyers can buy tickets and manage their shopping cart of tickets they want to buy. Marketplace.java utilizes this class to ensure that a user can buy a ticket.
- BuyerStatisticsGUI.java:
- BuyTicketGUI.java:
- BuyTicketMenuGUI.java:
- CartItems.java: This class organizes items into a shopping cart where buyers can see what tickets are bought from sellers in the marketplace.  Both Buyers.java and Sellers.java utilize this class to organize which tickets are being bought and which tickets are being sold.
- CreateAccountGUI.java:
- DoubleArrayList.java: This class returns a list of available tickets to buy from a seller and a list of the names of all stores.  Buyers.java and the main class utilize this class to show what sellers are available in the marketplace.
- LoginInfo.java: This class ensures that the user logging into the marketplace is valid and has an account in the system. This prevents any unauthorized users from logging into the marketplace.
- LoginInGUI.java:
- Marketplace.java: This is the main class of our project.  This utilizes the other five classes to allow the user to log in to the marketplace and proceed to buy or sell tickets. It takes in methods from the Buyer and Seller classes to buy or sell tickets and any other relevant actions like viewing statistics.
- MarketplaceClient.java:
- MarketplaceServer.java:
- MarketplaceServerThread.java:
- PrintableStat.java:
- RemoveTicketGUI.java:
- SellerDashboardGUI.java:
- SellerEditTicketGUI:
- SellerHistoryGUI.java:
- SellerListing.java: This class returns a list of tickets and their available quantities from a store.  Buyers.java and the main class utilize this class to show the description of a store for a buyer wanting to buy a ticket.
- Sellers.java: This class showcases how sellers can sell tickets and manage their store inventory.  Marketplace.java utilizes this class to ensure that a user can sell a ticket. For testing, TestCases.java is run to confirm that everything in the class works.
- SellerStatisticsGUI.java:
- SellTicketGUI.java:
- SellTicketMenuGUI.java:
- StoreNameIDCombo.java: This class returns the  name and ID number of a store.  Buyers.java and the main class utilize this class to show the details of a store for buyers.
- Ticket.java: This class showcases the information regarding a particular ticket.  This class is utilized in both Buyers.java and Sellers.java, where tickets to different sports games on campus are available to buy or sell on the marketplace.
- TicketInfoCombo.java: This class returns the type of ticket a buyer can buy and how many of them are available for purchase.  Buyers.java and the main class utilize this class to show buyers the list of tickets that are available for purchase in the marketplace. 
