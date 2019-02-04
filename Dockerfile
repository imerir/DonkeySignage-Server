FROM openjdk:11.0.1-jre-stretch
WORKDIR /donkey_src
ARG BUILD_NBR
ARG BRANCH_NAME
ADD build/libs/DonkeyServ-*.jar /donkey_src/DonkeyServ.jar
ENV PORT=8080
CMD java -jar DonkeyServ.jar -t ${TOKEN}
