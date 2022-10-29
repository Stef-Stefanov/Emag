# Emag_clone

by: <br />
**Sotir Donkov**    : [sdonkov](https://github.com/sdonkov) <br />
**Stefan Stefanov** : [Stef-Stefanov](https://github.com/Stef-Stefanov) <br />

For a final project in [ITTalents_14](https://ittalents.bg/home) <br />
A special credit to: **Krasimir Stoev** : [krasimir100ev](https://github.com/krasimir100ev) - our lecturer at ItTalents!

## **What is this about**?

This is our final assignment given out in October 2022 in season 14 of the ItTalents training camp for the Java backend. The assignment is to create a fully functional backend RESTfull API of the popular bulgarian retailer [eMAG](https://www.emag.bg/), following the MVC architectural pattern, without the front end. Below you may find our brief in bulgarian.

### Assignment brief in bulgarian:
Категории артикули. Подкатегории. Преглед на артикул. Възможност за покупка в количката. Възможност за администраторски
достъп - смяна на
количества, добавяне и премахване на артикули. Задаване на отстъпки. Търсене на артикули по име. Подреждане по цени.
Абониране за известия по мейл. Известия при нанасяне на
корекция от страна на администратора към някоя отстъпка към всички абонирани. BONUS - минимална и максимална цена при
филтриране на търсенето на артикули. Добавяне на
ревюта към артикул

### Features:
All basic and bonus functionalities required by the brief. <br />
Full user and admin functionality - CRUD, make reviews of products, put products in cart, make order. <br />
Products - CRUD, images, videos, discounts, search by name, price. Search product can search with user adjustable min and max prices. Can also search for a word or part of word.<br />
Products can have additional features added to them, through which a client can further customize his search -> ex. Screen size: 27 inc. <br />
Emails are sent with java mail sender. Discounts can be set up for individual products or for whole categories.
Many, many others.

### What we used:
Intellij Idea as IDE, Written on Java EE, Spring (Boot, Security, Data), MySQL as DB, Maven, Postman as Client, BCrypt, Hybernate, Lombok, Apache Tomcat, Git, Maven Central.

### How to install and use:
Install mySQL server and Workbench.<br />
Run the create schema query found in the DB folder of this project. <br />
Run the API. <br />
Provide your own mail to send out mail notifications and set it up in application.properties.
Use postman with the provided  requests. (Found in Postman directory) <br />

### What we learned, what challenges we faced and how we overcame them:

One of the key lessons we learned about development is that ultimately it is about solving challenges/problems. So with that in mind, we found it prudent to add this section so as to showcase that our outlook on the project was driven by thinking of our assignment in terms of a heap of small problems to solve. In the following lines, we will highlight a few of them. <br />

How we added pagination. Ask us during our pitch to know more. <br />
How to secure the admin functions from a regular user. -> It's more complicated than that, but in short a user becomes an admin with the help of another admin.
How to defend from session highjacking -> By logging and comparing the session IP with the request IP.
How to securely hash and keep our user's password in the DB -> using BCrypt and more importantly adding SALT to the hashing to defend from rainbow attacks.

