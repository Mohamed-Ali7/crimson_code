
# Crimson Code – REST APIs

## 📌 Introduction

This is the **RESTful API backend** for **Crimson Code** — a modern web platform that powers content creation, discussion, and exploration in the tech community.

The API is built with **Spring Boot** and documented using **OpenAPI 3.0 / Swagger**, enabling seamless integration, testing, and client-side consumption.

---

## 👤 Authors

- **Mohamed Ali** 
[![LinkedIn](https://img.shields.io/badge/LinkedIn-blue?logo=linkedin&style=flat-square)](https://www.linkedin.com/in/mohamed-ali7/)  

---

## ⚙️ Installation

Follow these steps to set up and run the Crimson Code REST APIs locally.

### 1. Clone the Repository

```bash
git clone https://github.com/Mohamed-Ali7/crimson_code.git
cd crimson_code
```

### 2.  **Set up the database:**
    
    - Use the provided SQL file `crimson_code_setup.sql` to set up your MySQL database schema, tables, and user.
    - Ensure your MySQL server is running and accessible.

### 3.  **Configure the email service:**

This project uses **MailHog** as a local SMTP server for development and testing.

📌 **MailHog Overview**:  
MailHog simulates an SMTP server for catching outgoing emails locally — no actual email is sent. More info: [MailHog Explained](https://mailtrap.io/blog/mailhog-explained/)
    
✅ **Configuration**

 - Open the `application.properties` file located at: `crimson_code_blog_rest_apis/src/main/resources/application.properties`
 

 - Current mail properties:

	```
	spring.mail.host=localhost
	spring.mail.port=1025
	spring.mail.username=
	spring.mail.password=
	spring.mail.properties.mail.smtp.auth=
	spring.mail.properties.mail.smtp.starttls.enable=
	```
- You can also configure it for other SMTP providers like **Gmail**:
	```
	spring.mail.host=smtp.gmail.com
	spring.mail.port=587
	spring.mail.username=your_email@gmail.com
	spring.mail.password=your_app_password
	spring.mail.properties.mail.smtp.auth=true
	spring.mail.properties.mail.smtp.starttls.enable=true
	```
        
4.  **Run the Spring boot REST-APIs application:**
    -   Locate the `CrimsonCodeApplication.java` file.
	-   Run you spring boot application as **Java application**
    
	This will launch the REST-APIs application on your **localhost** using the port **8080** (`http://localhost:8080/`), making it accessible for testing and development purposes. Ensure that the database is configured correctly before running the application.

<br>

## 🧪 Usage

Once the app is running, you can interact with the API using:
-   [Postman](https://www.postman.com/)
-   [Insomnia](https://insomnia.rest/)
-   cURL or any other API client
    

### 🔗 Base URL
	`http://localhost:8080/`

<br>

## 📚 API Documentation

The **Crimson Code REST APIs** are documented using **OpenAPI 3.0** and served via **Swagger UI**.

You can access the full API documentation here:

🔗 **[Swagger UI – API Explorer](https://p01--crimson-code-apis--jn622yqytdjv.code.run/swagger-ui/index.html)**

This interactive UI lets you explore all available endpoints, view request/response schemas, and test APIs directly in the browser.

### 🛡️ Authentication
To test secured endpoints (e.g., those requiring JWT), click the **"Authorize"** button in the Swagger UI and provide your access token.

<br>

## 📦 Technologies Used

-   Java 17
    
-   Spring Boot 3
    
-   Spring Security (JWT-based Authentication)
    
-   MySQL (PostgreSQL in production)
    
-   JPA / Hibernate
    
-   MailHog (SMTP Testing) - Gmail in production
    
-   Maven

