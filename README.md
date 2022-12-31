## Prerequisites:

* Install postgres.

* Install [docker](https://tinyurl.com/2m3bhahn).<br />
##
* This project runs on http://localhost:8080.
To see all the methods available, please checkout the user manuals in [1st Derivable](https://docs.google.com/document/d/1bqAPtyo7gYjIALpuRU-l6SihqhNbUe778N-UDNJwpnI/edit?usp=sharing) and [2nd Derivable](https://docs.google.com/document/d/1zQ9ZFB1zW_16LlmPExFsPG2_oqWwOZkO9oOKpkt20Oo/edit?usp=sharing).
--------------------------------
## Database Setup with Terminal
### Setup postgres database using terminal

* First, create realestate database (if not already created): 

```bash
createdb -h localhost -p 5432 -U postgres realestate
```

* It should ask for user's (postgres) password.
* The password of user postgres in tested systems is pass123 (if yours differs, change it in application.properties too).

* Then run script from project directory to input test users (if you want):

```bash
psql -h localhost -U postgres -d realestate -p 5432 -f assets/db/users.sql -W
```

* Input user's (postgres) password (if requested).

* Database setup finished. Run main app to create other tables automatically.

### Delete posgres database using terminal

```bash
dropdb -h localhost -p 5432 -U postgres realestate
```
* Input user's (postgres) password (if requested).

* Database is deleted.<br />

--------------------------------------
## Database Setup with docker
### Option 1: Run postgres database

```bash
docker run --name spb_db --rm -e  POSTGRES_PASSWORD=pass123 -e POSTGRES_DB=realestate --net=host -v pgdata14:/var/lib/postgresql/data  -d postgres:14
```
### Option 2: Run postgres database & initialize default users

```bash
docker run --name spb_db --rm -e  POSTGRES_PASSWORD=pass123 -e POSTGRES_DB=realestate --net=host -v "$(pwd)"/assets/db:/docker-entrypoint-initdb.d -v pgdata14:/var/lib/postgresql/data -d postgres:14
```

### Remove db data
```bash
docker volume rm pgdata14
```

### Connect to database using psql

```bash
sudo apt install postgresl-client
psql -h localhost -U postgres -d realestate -p 5432 -W
```

----------------------------------------------------------
## Default Users in Database:

Existing Users and Roles in pre-configured initial sql

| USER   | PASSWORD | ROLES       |
|------- |----------|-------------|
| tenant | pass123  | TENANT      |
| lessor | pass123  | LESSOR      |
| admin  | pass123  | ADMIN       |

You can create users using /registerTenant or /registerLessor in frontend (and thus in backend-basic).<br />

------------------------------------------------------------------
## Branches:
* [Backend Token Auth](https://github.com/manouslinard/dist_sys_2022/tree/backend-token-auth) &rarr; Spring Boot Backend with JWT Authentication, API endpoints provided to register users.
* [Frontend](https://github.com/manouslinard/dist_sys_2022/tree/frontend) &rarr; Thymeleaf Frontend with Basic Authentication.
* [Backend Basic Auth](https://github.com/manouslinard/dist_sys_2022/tree/backend-basic) &rarr; Spring Boot Backend with Basic Auth (to register new users, please use [Frontend](https://github.com/manouslinard/dist_sys_2022/tree/frontend) or [Backend Token Auth](https://github.com/manouslinard/dist_sys_2022/tree/backend-token-auth) and its corresponding API endpoints).

## Other Links:
* [1st Derivable](https://docs.google.com/document/d/1bqAPtyo7gYjIALpuRU-l6SihqhNbUe778N-UDNJwpnI/edit?usp=sharing)
* [2nd Derivable](https://docs.google.com/document/d/1zQ9ZFB1zW_16LlmPExFsPG2_oqWwOZkO9oOKpkt20Oo/edit?usp=sharing)
