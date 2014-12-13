VKConnector
===========

## How to get access token ##
Follow instructions on http://vk.com/dev/auth_sites

## Code example ##
```java
VKAuth auth = new VKAuth(accessToken, userId);
VKMethod getUsers = new VKMethod("users.get", auth);
String users = getUsers
        .addParam("uids", "1")
        .addParam("uids", auth.getUserID())
        .execute();
System.out.println(String.format("Users response in JSON: %s", users));
// Users response in JSON: {"response":[{"uid":1,"first_name":"Павел","last_name":"Дуров"},{"uid":1935036,"first_name":"Максим","last_name":"Логунов"}]}
```