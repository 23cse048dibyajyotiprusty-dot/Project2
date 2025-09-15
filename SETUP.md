# 🚀 ShopEasy E-Commerce Application - Quick Setup Guide

## 📋 Prerequisites

1. **Java Development Kit (JDK) 8+**
2. **Apache Tomcat 9.0+**
3. **MySQL 8.0+**
4. **NetBeans IDE** (recommended)

## ⚡ Quick Setup (5 Minutes)

### Step 1: Database Setup
1. Start MySQL server
2. Open MySQL Command Line or MySQL Workbench
3. Run the database schema:
   ```sql
   SOURCE database_schema.sql;
   ```

### Step 2: Configure Database Connection
1. Open `src/java/com/ecommerce/util/DatabaseConnection.java`
2. Update database credentials:
   ```java
   private static final String DB_USERNAME = "your_mysql_username";
   private static final String DB_PASSWORD = "your_mysql_password";
   ```

### Step 3: Add Required Libraries
Download and add to project libraries:
- **MySQL Connector/J** - [Download](https://dev.mysql.com/downloads/connector/j/)
- **Apache Commons DBCP2** - [Download](https://commons.apache.org/proper/commons-dbcp/download_dbcp.cgi)

### Step 4: Deploy and Run
1. Open project in NetBeans
2. Right-click project → **Run**
3. Access application at: `http://localhost:8080/WebApplication5/`

## 🔐 Default Login Credentials

### Admin Access
- **Email:** admin@ecommerce.com
- **Password:** admin123
- **Admin Dashboard:** `http://localhost:8080/WebApplication5/admin/dashboard.jsp`

### User Access
- Register new user account or
- Use the registration form to create test accounts

## ✅ Test the Application

1. **Homepage:** Browse featured products
2. **Registration:** Create a new user account
3. **Login:** Sign in with created account
4. **Products:** Browse and search products
5. **Admin:** Login as admin to access dashboard

## 🛠️ Features Included

### ✅ User Module
- User registration with validation
- Secure login with password hashing
- Product browsing with search/filter
- User dashboard
- Session management

### ✅ Admin Module
- Admin dashboard with KPIs
- User management
- Product catalog management
- Category management
- Security and audit logging

### ✅ Security Features
- SQL injection prevention
- XSS protection
- Password hashing (SHA-256 with salt)
- Session security
- Security headers
- Rate limiting

### ✅ Technical Features
- MVC architecture
- Connection pooling
- Responsive design
- Error handling
- Logging system

## 📊 Sample Data Included

The application comes with:
- **5 Categories:** Electronics, Clothing, Books, Home & Garden, Sports
- **6 Sample Products** with different categories
- **1 Admin User** pre-configured
- **Sample Coupons** for testing

## 🔧 Troubleshooting

### Database Connection Issues
```bash
# Check MySQL is running
mysql -u root -p

# Verify database exists
SHOW DATABASES;
USE ecommerce_db;
SHOW TABLES;
```

### Build Issues
1. Ensure MySQL Connector/J is in classpath
2. Check Java version compatibility
3. Clean and rebuild project
4. Verify Tomcat configuration

### Port Conflicts
- Default port: 8080
- Change in Tomcat configuration if needed
- Ensure no other services using port 8080

## 📁 Project Structure

```
WebApplication5/
├── database_schema.sql          # Database setup
├── src/java/com/ecommerce/
│   ├── dao/                    # Data Access Objects
│   ├── model/                  # Entity classes
│   ├── servlet/                # Servlet controllers
│   └── util/                   # Utility classes
├── web/
│   ├── css/style.css          # Main stylesheet
│   ├── admin/                 # Admin pages
│   ├── *.jsp                  # JSP pages
│   └── index.html            # Homepage
└── README.md                  # Detailed documentation
```

## 🚀 Ready to Go!

Your e-commerce application is now ready with:
- **Modern UI** with responsive design
- **Secure backend** with best practices
- **Complete functionality** for both users and admins
- **Professional code structure** following MVC pattern

Visit `http://localhost:8080/WebApplication5/` to start using your application!

## 📞 Need Help?

Check the detailed `README.md` for:
- Complete feature list
- Detailed setup instructions
- Troubleshooting guide
- Code documentation

---
**ShopEasy v1.0** - Built with ❤️ using JSP, Servlet, JDBC & MySQL