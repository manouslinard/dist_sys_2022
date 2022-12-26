## Prerequisites:

* Install postgres.

* Install [docker](https://tinyurl.com/2m3bhahn).<br />

--------------------------------
## Database Setup with Terminal
### Setup postgres database using terminal

* First, create students database (if not already created): 

```bash
createdb -h localhost -p 5432 -U postgres students
```

* It should ask for user's (postgres) password.
* The password of user postgres in tested systems is pass123 (if yours differs, change it in application.properties too).

* Then run script from project directory to input test users (if you want):

```bash
psql -h localhost -U postgres -d students -p 5432 -f assets/db/users.sql -W
```

* Input user's (postgres) password (if requested).

* Database setup finished. Run main app to create other tables automatically.

### Delete posgres database using terminal

```bash
dropdb -h localhost -p 5432 -U postgres students
```
* Input user's (postgres) password (if requested).

* Database is deleted.<br />

--------------------------------------
## Database Setup with docker
### Option 1: Run postgres database

```bash
docker run --name spb_db --rm -e  POSTGRES_PASSWORD=pass123 -e POSTGRES_DB=students --net=host -v pgdata14:/var/lib/postgresql/data  -d postgres:14
```
### Option 2: Run postgres database & initialize default users

```bash
docker run --name spb_db --rm -e  POSTGRES_PASSWORD=pass123 -e POSTGRES_DB=students --net=host -v "$(pwd)"/assets/db:/docker-entrypoint-initdb.d -v pgdata14:/var/lib/postgresql/data -d postgres:14
```

### Remove db data
```bash
docker volume rm pgdata14
```

### Connect to database using psql

```bash
sudo apt install postgresl-client
psql -h localhost -U postgres -d students -p 5432 -W
```
----------------------------------------
# Branches:
* [Main](https://gitlab.com/atsadimas/springbootdemo). Spring boot application with thymeleaf, spring security and bootstrap 5
Existing Users and Roles in pre-configured initial sql

| USER    | PASSWORD | ROLES       |
|-------  |----------|-------------|
| tenant  | pass123  | TENANT      |
| lessor  | pass123  | LESSOR      |
| admin   | pass123  | ADMIN       |
 
You can create users using /register
* [Security](https://gitlab.com/atsadimas/springbootdemo/-/tree/security). Spring Boot Backend with JWT Authentication

API endpoints provided to register users

## Links:
* [install docker](https://tinyurl.com/2m3bhahn)
