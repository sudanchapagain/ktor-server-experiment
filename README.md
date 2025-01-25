ktor server
===========

Example usage
-------------

```shell
curl --request POST --data "username=newuser&password=securepassword" http://localhost:4321/register
curl --request POST --data "username=newuser&password=securepassword" http://localhost:4321/auth -c cookies.txt
curl --request GET http://localhost:4321/balance --cookie cookies.txt
curl --request POST --data "amount=100&action=add" http://localhost:4321/balance/update --cookie cookies.txt
```