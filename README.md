# MealooApp 

Web app that generates meal recipes automatically basend on filters such as BMI and weight goals.
It also serves as a food diary where users can create their own meals.

## Table of contents
* General info
* Features
* Code Examples
* Technologies 
* Setup
* Contributors

## General info

 The assumption of the application is to create a food diary where every user
 will have possibility to create own diary or generate, based on requirements . <br>
 Thanks to BMI and calories demand calculator and meals algorithm, the user will be able to get interesting meals
 and change weight according to his demands.
 
### Features

 * User registration
 * Three roles and access restrictions : User, Moderator and Admin
 * Adding products with details : Proteins, Carbohydrates and Fats per 100g, Product Type and price per kg 
 * Adding meals based on products and amount with details : image, recipe and difficulty 
 * Adding details about user
 * Calculate BMI and caloric demand based on details
 * Calculate caloric demand according to user goal : losing, gaining or keep weight
 * Create diaries for user : add or remove meals from current day 
 * Generate choosen numbers of meals and total calories for whole day
 * Program calulate total calories and price for whole meal and day automatically
  (The program ensures that meals from 3 days back will not appear)
  To-do list:
 * Addition rejection meal types and macronutrients preferences
 * Addition JWT and Oauth2 system to registration, login and security
 * Addition validation for products proportions 

## Code Examples

## Setup

 In order to add data to database application requires HTTP client, f.e Postman

## Technologies 

 Java 11 <br>
 Spring Boot 2 <br>
 Hibernate <br>
 JUnit 5 <br>
 Mockito 2 <br>
 PostresSQL <br>
 Cloudinary API <br>
 REST <br>
 Heroku <br>
 
## Contributors

* [Damian Naglak](https://github.com/naslakboss) - Backend Developer
* [John Patrick Valera](https://github.com/withoutwax13) - Frontend Developer
     
     Frontend Codebase Repository
     [Mealoo Web Client](https://github.com/withoutwax13/mealoo-web-client) - Built with ReactJS

