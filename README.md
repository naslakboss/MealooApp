# MealooApp 

Web app that generates meal recipes automatically based on filters such as BMI and weight goals.
It also serves as a food diary where users can create their own meals.

## Table of contents
* General info
* Features
* Technologies 
* Setup
* Contributors

## General info

 The assumption of the application is to create a food diary where every user
 will have possibility to create own diary or generate them, based on requirements . <br>
     Thanks to BMI calculator, caloric demand calculator and meals algorithm, the user will be able to get tasty meals
 and change weight according to his demands.
 
### Features

 * User registration and login
 * Three roles and access restrictions : User, Moderator and Admin
 * Adding products with details : Proteins, Carbohydrates and Fats per 100g, Product Type and price per kg 
 * Adding meals based on products and amount with details : image, recipe and difficulty 
 * Adding details about user
 * Calculate BMI and caloric demand based on details
 * Calculate caloric demand according to user goal : losing, gaining or keep weight
 * Create diaries for user : add or remove meals from current day 
 * Generate choosen numbers of meals and total calories for whole day
 * Program calculate total calories and price for whole meal and day automatically
  (The program ensures that meals from 3 days back will not appear) <br>
 * JWT security and limited acces only for user resources with user role <br>
  To-do list:
 * Addition rejection meal types and macronutrients preferences
 * Addition validation for products proportions 

## Setup
Go to : 
- > https://mealoodietapp.herokuapp.com/swagger-ui.html#/
- > authController : /home/sign-in
- > There are three account with corresponding roles : mealoouser, mealoomoderator and mealooadmin with password : password123
- > Select a token for the appropriate account : </br>

<details> 
  <summary> User token </summary>
Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtZWFsb291c2VyIiwiaWF0IjoxNjAyMjIwNTg1LCJleHAiOjE2MDIzMDY5ODV9.hZEUe8R4hm4STH4Sn_nTjZ1ipPY_GH3xzSyFq_ReWm-708MAk3dJHcluFdHNb_jSrhjTQaFL-AWj_WUB3ZBrlg
</details>
   MODERATOR :
<details> 
  <summary> Moderator token </summary>
Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtZWFsb29tb2RlcmF0b3IiLCJpYXQiOjE2MDIyMjE1NzksImV4cCI6MTYwMjMwNzk3OX0.peiU5BdDjJrEoT7qWG_fpMFVqGnXmbweLNERim7P-mH_mVdNbUCMp_2Xp4Sm7NlG0fE-_b8lq1hoyYt4HG_V-A
</details>
   ADMIN :
<details> 
  <summary> Admin token </summary>
Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtZWFsb29hZG1pbiIsImlhdCI6MTYwMjIyMTYwOCwiZXhwIjoxNjAyMzA4MDA4fQ.a3q6fD1PFJ_RDafDYEuqT_HXOVwYzoHVM4ZrFOMRw1OLqcj_QNo80lnoENiWmKnkw2GT_1LYsTj3JpSGxe7jWw
</details>
- > Now you have access to some methods according to roles, if you want to have full access log as a mealooadmin


## Technologies and Tools

 Java 11 <br>
 Spring Boot 2 <br>
 Hibernate <br>
 JUnit 5 <br>
 Mockito 2 <br>
 PostgresSQL <br>
 Cloudinary API <br>
 REST <br>
 Heroku <br>
 SonarQube <br>
 Swagger <br>
 
 
## Contributors

* [Damian Naglak](https://github.com/naslakboss) - Backend Developer
* [John Patrick Valera](https://github.com/withoutwax13) - Frontend Developer
     
     Frontend Codebase Repository : <br>
     [Mealoo Web Client](https://github.com/withoutwax13/mealoo-web-client) - Built with ReactJS

