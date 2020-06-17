# Banking API Project for ROCP
### by Christopher Siegfried

For POST formating, check PostCheatSheet.md!

Below is the laid out version of how the program works, the various calls, and basically functions as a collection of all the comments in the program files.

The general hierarchy is laid out as such:
* FrontController: The hub of the program. All HTTP requests are handled through this, and are delivered to the appropriate controller.	
  * LoginController: Responsible for handling Login and Logout methods to sign our users in and out.
  * UserController: Responsible for inserting, updating, and fetching user data.
    * UserService: Handles any business logic for the UserController before accessing the DAO
      * AbstractUserDAO: The DAO that interacts with our USERS database and has access to our CRUD operations
  * AccountController: Responsible for inserting, updating, and fetching account data, as well as the join table USERS-ACCOUNTS
    * AccountService: Handles any business logic for account data, as well as the relationship to the USERS-ACCOUNTS table
      * AccountDAO: The DAO that interacts with our USERS database  and has access to our CRUD operations
      * UserAccountDAO: The DAO that interacts with our USERS-ACCOUNTS database and has access to our CRUD operations
      
      
