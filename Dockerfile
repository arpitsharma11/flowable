FROM openjdk:8

ADD ./flowable.jar flowable.jar

ADD ./src/main/resources /home/zadmin/Desktop/flowable/src/main/resources

RUN apt-get update && apt-get install bash

COPY wait-for-it.sh /wait-for-it.sh

RUN chmod +x /wait-for-it.sh

EXPOSE 8091

#ENTRYPOINT ["java","-jar","ze-talent.jar"]
