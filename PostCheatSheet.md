## POST Formating
In order to ensure your post requests are handled properly, follow these formats:

* /users?login : Posting credentials to log in. Cross references with the database to make sure everything matches.
```json
{
	"username":"value",
	"password":"value"
}
```
* /users : Posting here allows an employee or admin to insert a new user.  - Note firstName and lastName MUST have capital N 
```json
{
    "userId":0,
    "username":"orange",
    "password":"juice",
    "firstName":"John",
    "lastName":"orange",
    "email":"oj@juiceguy.com",
    "roleId":2
}
```