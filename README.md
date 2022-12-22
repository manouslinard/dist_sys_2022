### Run a postgres database using docker

```bash
docker run --name spb_db --rm -e  POSTGRES_PASSWORD=pass123 -e POSTGRES_DB=realestate --net=host -v pgdata14:/var/lib/postgresql/data  -d postgres:14
```
### Initialize default users

```bash
docker run --name spb_db --rm -e  POSTGRES_PASSWORD=pass123 -e POSTGRES_DB=realestate --net=host -v "$(pwd)"/assets/db:/docker-entrypoint-initdb.d -v pgdata14:/var/lib/postgresql/data -d postgres:14
```

## remove db data
```bash
docker volume rm pgdata14
```

## connect to database using psql

```bash
sudo apt install postgresl-client
psql -h localhost -U postgres -d realestate -p 5432 -W
```

# Branches:
* [Main](https://github.com/manouslinard/dist_sys_2022). Spring boot application with thymeleaf, spring security and bootstrap 3
Existing Users and Roles in pre-configured initial sql

| USER   | PASSWORD | ROLES       |
|------- |----------|-------------|
| tenant | pass123  | TENANT      |
| lessor | pass123  | LESSOR      |
| admin  | pass123  | ADMIN       |
 
You can create users using /register
* [Security](https://gitlab.com/atsadimas/springbootdemo/-/tree/security). Spring Boot Backend with JWT Authentication

API endpoints provided to register users

## Links:
* [1st Derivable](https://tinyurl.com/2m3bhahn)