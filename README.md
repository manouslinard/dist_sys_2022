## Prerequisites:

* Install postgres.

* Install [docker](https://tinyurl.com/2m3bhahn).<br />
##
* This project runs on http://localhost:8080.
To see all the methods available, please checkout the user manuals in the [Final Derivable](/files/Final_Derivable_ENG.pdf) (translated automatically in English, original version is written in Greek and can be found [here](/files/Final_Derivable_GR.pdf)).

* Also, this project (and specifically the [frontend branch](https://github.com/manouslinard/dist_sys_2022/tree/frontend)) was deployed in microsoft azure. Here is a [video](https://youtu.be/MKoX_YZdJ6A) showcasing it.
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

You can create users using /lessorform or /tenantform in frontend.<br />

You can create users using /api/auth/signup in backend branches.

------------------------------------------------------------------
## Branches:
* [Backend Token Auth](https://github.com/manouslinard/dist_sys_2022/tree/backend-token-auth) &rarr; Spring Boot Backend with JWT Authentication, API endpoints provided to register users.

* [Frontend](https://github.com/manouslinard/dist_sys_2022/tree/frontend) &rarr; Thymeleaf Frontend with Basic Authentication (this branch is used in cloud showcase [video](https://youtu.be/MKoX_YZdJ6A)).

* [Backend Basic Auth](https://github.com/manouslinard/dist_sys_2022/tree/backend-basic) &rarr; Spring Boot Backend with Basic Auth, API endpoints provided to register users.

## Other Links:
* [Final Derivable GREEK](/files/Final_Derivable_GR.pdf)

* [Slides Presentation GREEK](/files/DisSys_Presentation_GR.pdf)

* [Final Derivable ENGLISH (auto-translation)](/files/Final_Derivable_ENG.pdf)

* [Slides Presentation ENGLISH (auto-translation)](/files/DisSys_Presentation_ENG.pdf)

* [Cloud Showcase Video](https://youtu.be/MKoX_YZdJ6A)
