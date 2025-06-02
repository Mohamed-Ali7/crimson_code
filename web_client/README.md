
# Crimson Code – Frontend

## 📌 Overview

This is the **frontend** interface of **Crimson Code**, a modern web platform designed to provide high-quality, trustworthy content for tech enthusiasts. The frontend communicates with the Crimson Code REST API to enable features such as:

- User authentication and profile management  
- Post creation, editing, and exploration  
- Commenting and interaction  
- Responsive navigation and a modern user interface
    

Built with **vanilla HTML, CSS, and JavaScript**, and enhanced using **jQuery**, this frontend aims to deliver a lightweight, responsive experience across devices.

<hr>
<br>

## 🛠️ Technologies Used

-   **HTML5**
    
-   **CSS3** (Responsive design and custom styling)
    
-   **JavaScript**
    
-   **jQuery** (AJAX, DOM manipulation, event handling)
    
-   **AOS.js** (for scroll animations — optional)
    
-   **Font Awesome** (icons)

-   **SweetAlert** – for elegant and customizable alert modals
    
-   **VS Code Live Server** – for running the frontend locally during development

<hr>
<br>

## 🚀 Getting Started

### 1. Clone the Repository

```
git clone https://github.com/Mohamed-Ali7/crimson_code.git
cd crimson_code/web_client
```

## ▶️ Running the Frontend Locally

The simplest and recommended way to run the Crimson Code frontend is by using the **Live Server** extension in **Visual Studio Code**.

### ✅ Prerequisites

-   [Visual Studio Code](https://code.visualstudio.com/)
-   [Live Server Extension](https://marketplace.visualstudio.com/items?itemName=ritwickdey.LiveServer)
    

### 🛠 Steps

1.  Open the project in **VS Code**.
    
2.  Navigate to the frontend directory:
    
    `crimson_code/web_client` 
    
    > 🔔 Make sure that `web_client` is the **working directory** in VS Code before starting the server.
    
3.  Right-click on `index.html` (or any page you wish to open), and choose: 

	`Open  with Live Server` 
    
5.  The application will be served on:
    
    `http://localhost:5500/` 
    
    This URL is consistent with the backend’s API CORS setup (typically running on `http://localhost:8080`), allowing proper integration during development.
    

	> 🧩 **Note:** The backend API base URL is declared in the `main.js` file. Make sure it is set to `http://localhost:8080` when running locally so all API requests are routed correctly.

<hr>
<br>

## 📚 Related Projects

-   🔗 [Crimson Code – REST API (Spring Boot)](https://github.com/Mohamed-Ali7/crimson_code/tree/main/crimson_code_blog_rest_apis)
