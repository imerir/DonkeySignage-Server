FROM openjdk:11.0.1-jre-stretch
WORKDIR /donkey_src
ARG BUILD_NBR
ARG BRANCH_NAME
ADD DownloadLast.sh /donkey_src/
RUN chmod +x DownloadLast.sh
RUN ./DownloadLast.sh ${BRANCH_NAME} ${BUILD_NBR}
ENV PORT=8080
CMD java -jar app.jar -t ${TOKEN}
