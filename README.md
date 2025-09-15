# ShopEasy - E-Commerce Web Application

A comprehensive e-commerce web application built using JSP, Servlet, JDBC, and MySQL following the MVC architecture pattern. This project implements all the requirements specified in the requirements document.

## 🚀 Features

### User Module
- **User Registration & Login** - Secure user authentication with password hashing
- **Product Browsing** - Browse products by categories with search and filter options
- **Shopping Cart** - Add/remove products, update quantities
- **Checkout Process** - Complete order placement with address management
- **Order Tracking** - View order history and track order status
- **Wishlist** - Save favorite products for later
- **User Dashboard** - Personalized dashboard with account overview

### Admin Module
- **Admin Login** - Secure admin authentication
- **Dashboard with KPIs** - Key performance indicators and statistics
- **Product Management** - CRUD operations for products
- **Category Management** - Manage product categories
- **Order Management** - Update order status and tracking
- **User Management** - View and manage user accounts
- **Reports** - Sales reports and analytics

### Security & Non-Functional Requirements
- **SQL Injection Prevention** - Parameterized queries
- **XSS Protection** - Input sanitization
- **Password Security** - SHA-256 hashing with salt
- **Session Management** - Secure session handling
- **Rate Limiting** - Brute force attack prevention
- **Connection Pooling** - Efficient database connections
- **Scalable Architecture** - MVC pattern with proper separation of concerns

## 🛠️ Technology Stack

- **Backend**: Java, JSP, Servlet, JDBC
- **Database**: MySQL 8.0+
- **Server**: Apache Tomcat 9.0+
- **Frontend**: HTML5, CSS3, JavaScript, Bootstrap
- **Icons**: Font Awesome 6.0
- **Architecture**: MVC Pattern

## 📋 Prerequisites

Before running this application, make sure you have:

1. **Java Development Kit (JDK) 8 or higher**
2. **Apache Tomcat 9.0 or higher**
3. **MySQL 8.0 or higher**
4. **NetBeans IDE 12+ (recommended) or any Java IDE**
5. **MySQL Connector/J JDBC Driver**

## 🔧 Installation & Setup

### 1. Database Setup

1. **Create MySQL Database:**
   ```sql
   CREATE DATABASE ecommerce_db;
   ```

2. **Run the Database Schema:**
   - Execute the SQL script in `database_schema.sql`
   - This will create all necessary tables and insert sample data

3. **Update Database Configuration:**
   - Open `src/java/com/ecommerce/util/DatabaseConnection.java`
   - Update the database connection parameters:
   ```java
   private static final String DB_URL = "jdbc:mysql://localhost:3306/ecommerce_db";
   private static final String DB_USERNAME = "your_username";
   private static final String DB_PASSWORD = "your_password";
   ```

### 2. Project Setup

1. **Clone or Download the Project**
   - Extract to your preferred directory

2. **Import Project into NetBeans:**
   - Open NetBeans IDE
   - File → Open Project
   - Navigate to the project folder and select it

3. **Add MySQL Connector:**
   - Download MySQL Connector/J from https://dev.mysql.com/downloads/connector/j/
   - Add the JAR file to your project's Libraries
   - In NetBeans: Right-click on Libraries → Add JAR/Folder

4. **Add Apache Commons DBCP2 (for Connection Pooling):**
   - Download from https://commons.apache.org/proper/commons-dbcp/
   - Add to project libraries

### 3. Server Configuration

1. **Configure Tomcat Server:**
   - In NetBeans: Services → Servers → Add Server
   - Select Apache Tomcat and provide installation directory
   - Set the server to run on port 8080 (default)

2. **Deploy the Application:**
   - Right-click on the project → Properties
   - Set the server to Apache Tomcat
   - Build and Run the project

## 🚀 Running the Application

1. **Start MySQL Server:**
   ```bash
   mysql -u root -p
   ```

2. **Run the Application:**
   - In NetBeans: Right-click project → Run
   - Or manually deploy WAR file to Tomcat webapps directory

3. **Access the Application:**
   - Open browser and navigate to: `http://localhost:8080/WebApplication5/`
   - Default admin login: `admin@ecommerce.com` / `admin123`

## 📁 Project Structure

```
WebApplication5/
├── src/java/com/ecommerce/
│   ├── dao/           # Data Access Objects
│   ├── model/         # Entity/Model classes
│   ├── servlet/       # Servlet controllers
│   └── util/          # Utility classes
├── web/
│   ├── css/          # Stylesheets
│   ├── js/           # JavaScript files
│   ├── images/       # Image assets
│   ├── *.jsp         # JSP pages
│   └── index.html    # Homepage
├── database_schema.sql # Database setup script
└── README.md         # This file
```

## 🗃️ Database Schema

The application uses the following main tables:

- **users** - Customer user accounts
- **admins** - Administrator accounts
- **categories** - Product categories
- **products** - Product catalog
- **carts** - Shopping cart items
- **orders** - Customer orders
- **order_items** - Individual order items
- **payments** - Payment information
- **addresses** - User shipping addresses
- **reviews** - Product reviews
- **coupons** - Discount coupons
- **audit_log** - System audit trail

## 🔒 Default Login Credentials

### Admin Account
- **Email:** admin@ecommerce.com
- **Password:** admin123

### Test User Account
You can register a new user account or use the registration feature to create test accounts.

## 🎨 Features Implemented

### ✅ User Module
- [x] User registration with email verification
- [x] Secure login with session management
- [x] Product browsing and search
- [x] Category-wise product filtering
- [x] Shopping cart functionality
- [x] Checkout process
- [x] Order placement and tracking
- [x] User dashboard
- [x] Profile management

### ✅ Admin Module
- [x] Admin login and authentication
- [x] Dashboard with KPIs
- [x] Product management (CRUD)
- [x] Category management (CRUD)
- [x] Order management and status updates
- [x] User management
- [x] Sales reports

### ✅ Security Features
- [x] Password hashing (SHA-256 with salt)
- [x] SQL injection prevention
- [x] XSS protection through input sanitization
- [x] Session security
- [x] Rate limiting for login attempts
- [x] CSRF protection mechanisms

### ✅ Non-Functional Requirements
- [x] Responsive design
- [x] Connection pooling
- [x] MVC architecture
- [x] Clean code structure
- [x] Error handling
- [x] Logging system
- [x] Performance optimization

## 🐛 Troubleshooting

### Common Issues:

1. **Database Connection Error:**
   - Verify MySQL server is running
   - Check database credentials in DatabaseConnection.java
   - Ensure MySQL Connector/J is in classpath

2. **Server Startup Issues:**
   - Check Tomcat configuration
   - Verify port 8080 is not in use
   - Check server logs for errors

3. **Build Errors:**
   - Ensure all dependencies are in classpath
   - Verify Java version compatibility
   - Clean and rebuild project

## 📧 Support

If you encounter any issues or have questions, please check:

1. **Server Logs:** Check Tomcat logs for detailed error messages
2. **Browser Console:** Check for JavaScript errors
3. **Database Logs:** Check MySQL logs for database-related issues

## 🔮 Future Enhancements

- Payment gateway integration
- Email notifications
- Product recommendations
- Advanced search with filters
- Mobile app integration
- Multi-language support
- Advanced analytics dashboard
- API development for third-party integrations

## 📄 License

This project is developed for educational purposes. Feel free to use and modify as needed.

---

**Created by:** [Your Name]  
**Date:** September 2024  
**Version:** 1.0.0