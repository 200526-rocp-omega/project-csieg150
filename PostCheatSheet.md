## POST Formating & PUT Formating
In order to ensure your post requests are handled properly, follow these formats:

### POST
* /users?login : Posting credentials to log in. Cross references with the database to make sure everything matches.
```json
{
	"username":"value",
	"password":"value"
}
```
* /users : Posting here allows an employee or admin to insert a new user.  - Note that:
  * firstName and lastName MUST have capital N 
  * email must follow the "_*@_*._*" regex pattern 
  * roleId is an int that ranges from 1-4 (Standard, Premium, Employee, Admin), and any other numbers will result in 1 (Standard) being submitted
```json
{
    "userId":0,
    "username":"username",
    "password":"pass",
    "firstName":"John",
    "lastName":"Doe",
    "email":"jdoe@generic.com",
    "roleId":1
}
```
* /accounts/(accountId)?deposit or /accounts/(accountId)?withdraw both take a single double 
```json
{
    "amount":00.00
}
```
* /accounts/(accountId)?transfer - the AccountId in the URI is our 'source' to withdraw, the posted data is our 'target' to deposit the amount to
```json
{
	"accountId":0,
    "amount":00.00
}
```
* /accounts -  used for inserting new records into the USERS-ACCOUNTS table and ACCOUNTS. 
  * Standard and Premium users can only input their ID to work with.
```json
{
    "userId":0,
    "accountId":0,
    "balance":0.0,
    "statusId":1,
    "typeId":2
}
```
* /accounts?passtime  - used for accruing compound interest on saving accounts for X months, admin only
```json
{
    "numOfMonths":0
}
```

### PUT
* /users - used to update a user account. Standard/Premium users can update their own accounts, Emp and Admin can adjust others
  * Important to note that getting the User ID correct, and that unless you're an Admin the roleId field won't update
```json
{
    "userId":0,
    "username":"username",
    "password":"pass",
    "firstName":"John",
    "lastName":"Doe",
    "email":"jdoe@generic.com",
    "roleId":1
}
```
* /users?upgrade - checks if standard user is upgrading self or if admin is upgrading them. userId is the user to go from standard -> premium
  * note there's a 100$ transfer from the accountId given to the Admin's account to simulate a fee.
```json
{
	"userId":0,
	"accountId":0
}
```
* /accounts?addjointuser - checks if premium owner of given account ID (or Emp/Admin), if true then add the new user as joint owner of that account
```json
{
	"userId":0,
	"accountId":0
}
```