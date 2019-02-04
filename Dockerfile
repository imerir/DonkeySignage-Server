FROM openjdk:11.0.1-jre-stretch
WORKDIR /donkey_src
ADD build/libs/DonkeyServ-*.jar /donkey_src/DonkeyServ.jar
ENV PORT=8080
ENV DB_URL=jdbc:mysql://localhost/donkey
ENV DB_USER=donkey
ENV DB_PWD=donkey
CMD java -jar DonkeyServ.jar -t ${TOKEN}
