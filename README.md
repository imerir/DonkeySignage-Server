# DonkeySignage-Server

Username : admin
password : donkey

Jenkins Build:<br/>
[![Build Status](https://jenkins.donkeysignage.imerir.org/buildStatus/icon?job=DonkeySignage-Server%2Fmaster)](https://jenkins.donkeysignage.imerir.org/job/DonkeySignage-Server/job/master/)

## Requirements

### Java
We use Java 11 for this project.
- [Java 11](https://jdk.java.net/11/)

### Gradle

We use Gradle for this project.
- [Gradle](https://gradle.org/install/)

### Environment Variable
```
PORT=8080
DB_URL=jdbc:mysql://localhost:3306/test
DB_USER=root
DB_PWD=temp
DB_MODE=update
LOG_LEVEL=trace
OPEN_WEATHER_API_KEY=65fdeb35f3716ee3e4740f3fcfaa24a3
```


### Install Database:
 
> The easiest method it's to use docker-compose:
> 
> docker-compose.yml:
> ```YAML
> version: '3'
> 
> services:
>   database:
>       image: mariadb:latest
>       volumes:
>         - ./db:/var/lib/mysql
>       labels:
>        - "traefik.enable=false"
>       environment:
>         - MYSQL_ROOT_PASSWORD=temp
>         - MYSQL_DATABASE=test
>         - MYSQL_USER=bot
>         - MYSQL_PASSWORD=Ho0duiWo3noo3Ahrahx0rohz #CHANGE ME!
>       ports:
>         - 3306:3306
>```

## Doc
- [Javadoc](https://jenkins.donkeysignage.imerir.org/job/DonkeySignage-Server/job/master/Javadoc/)
- [Postman](https://documenter.getpostman.com/view/4264362/S11NMH17)

## Jenkins
- [Jenkins](https://jenkins.donkeysignage.imerir.org)


### Install Donkey Project on a new PC
- Requirement:
IntelliJ IDEA Ultimate
Docker-compose

- Open the project with IntelliJ IDEA Ultimate, select Gradle and Finish
- Go to Run>Edit Configurations, open Environment in Configuration Tab, click on the $ in Environment variables, and then enter all environment variable (see above) and apply
- Now to install the Database : create a file called docker-compose.yml, and copy-paste inside the file what's written in the "Install Database"
- Open a terminal inside the folder where the file docker-compose.yml is, use the command ```sudo docker-compose up``` to start the database
- On the main window of IntelliJ, open Database Tab on the right side and create a new Data Souce with mysql or MariaDB
- On User write the user username (MYSQL_USER) from the docker-compose file, in our case it's "bot", do the same for the password (MYSQL_PASSWORD) and apply

You can now run the project !


