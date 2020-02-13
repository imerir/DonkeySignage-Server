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
OPEN_WEATHER_API_KEY=xxxxxx
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
