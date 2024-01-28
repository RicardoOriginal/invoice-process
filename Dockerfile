FROM amazoncorretto:21 AS jre
WORKDIR /invoice-process
COPY build/libs/invoice-process.jar invoice.jar
CMD ["java","-jar","-Dspring.profiles.active=docker","-Duser.timezone=America/Sao_Paulo","invoice.jar"]