FROM gradle:7.0.0 as builder
WORKDIR /app
COPY . .
RUN ./gradlew build -x test

FROM openjdk:latest
COPY --from=builder /app/build/libs/customer-0.0.1-SNAPSHOT.jar /customer.jar
EXPOSE 8080
CMD ["java", "-jar", "customer.jar"]