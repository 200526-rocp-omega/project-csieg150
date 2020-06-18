#Wishlist
This is a list of things I wanted to get done but haven't had the time to do yet:

1. /accounts/owners/(id)?type=(typeId)
2. Further abstracting the FrontController logic
3. Making it so non-employee users can only create Pending accounts
4. Added more exceptions with more specific explanations what went wrong
5. Adding the 'View' part 
6. Adding the 'Strech Goals' from the README
7. Maybe change the syntax on returning and writing Account info - maybe just add an endpoint to find owners by account. acount/id?ownedBy ?
8. Have 'Pending', 'Closed', and 'Denied' Status types affect the code in some way. Like they shouldn't be able to withdraw or something like that
9. Add some extra checks for Update operations that do a quick Read operation and look to make sure the record we want to update exists