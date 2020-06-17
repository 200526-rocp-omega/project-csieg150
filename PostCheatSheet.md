## POST Formating & PUT Formating
In order to ensure your post requests are handled properly, follow these formats:

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