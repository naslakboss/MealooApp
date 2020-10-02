FROM openjdk:11
ADD target/MealooAp-0.0.1-SNAPSHOT.jar .
EXPOSE 8000
CMD java -jar MealooAp-0.0.1-SNAPSHOT.jar
