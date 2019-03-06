# DonkeySignage-Server

Jenkins Build:<br/>
[![Build Status](https://jenkins.donkeysignage.imerir.org/buildStatus/icon?job=DonkeySignage-Server%2Fmaster)](https://jenkins.donkeysignage.imerir.org/job/DonkeySignage-Server/job/master/)

## Environment Variable

1. PORT : 8080
2. DB_URL : jdbc:mysql://localhost:3306/test
3. DB_USER : root
4. DB_PWS : temp
5. DB_MODE : update
6. LOG_LEVEL : trace

## Install Database:
 
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