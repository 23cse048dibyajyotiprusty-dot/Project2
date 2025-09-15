<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.ecommerce.model.User" %>
<%
    // Check if user is logged in
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">
    <title>Dashboard - ShopEasy</title>
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        .dashboard-container {
            max-width: 1200px;
            margin: 2rem auto;
            padding: 0 1rem;
        }
        
        .welcome-section {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 2rem;
            border-radius: 15px;
            margin-bottom: 2rem;
        }
        
        .dashboard-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
            gap: 2rem;
            margin-bottom: 2rem;
        }
        
        .dashboard-card {
            background: white;
            border-radius: 15px;
            padding: 1.5rem;
            box-shadow: 0 5px 15px rgba(0,0,0,0.08);
            border: 1px solid #e9ecef;
            transition: transform 0.3s ease;
        }
        
        .dashboard-card:hover {
            transform: translateY(-5px);
        }
        
        .card-header {
            display: flex;
            align-items: center;
            margin-bottom: 1rem;
        }
        
        .card-icon {
            font-size: 2rem;
            margin-right: 1rem;
            width: 50px;
            height: 50px;
            display: flex;
            align-items: center;
            justify-content: center;
            border-radius: 50%;
        }
        
        .orders-icon { background: linear-gradient(45deg, #667eea, #764ba2); color: white; }
        .cart-icon { background: linear-gradient(45deg, #ff6b6b, #ee5a24); color: white; }
        .profile-icon { background: linear-gradient(45deg, #27ae60, #2ecc71); color: white; }
        .wishlist-icon { background: linear-gradient(45deg, #e74c3c, #c0392b); color: white; }
        
        .quick-actions {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 1rem;
            margin-top: 2rem;
        }
        
        .action-btn {
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 0.5rem;
            padding: 1rem;
            background: white;
            border: 2px solid #e9ecef;
            border-radius: 10px;
            text-decoration: none;
            color: #2c3e50;
            font-weight: 600;
            transition: all 0.3s ease;
        }
        
        .action-btn:hover {
            border-color: #667eea;
            color: #667eea;
            transform: translateY(-2px);
            text-decoration: none;
        }
    </style>
</head>
<body>
    <!-- Header -->
    <header class="header">
        <div class="container">
            <nav class="navbar">
                <a href="index.html" class="logo">
                    <i class="fas fa-shopping-cart"></i>
                    ShopEasy
                </a>
                
                <ul class="nav-links">
                    <li><a href="index.html">Home</a></li>
                    <li><a href="products.jsp">Products</a></li>
                    <li><a href="categories.jsp">Categories</a></li>
                    <li><a href="dashboard.jsp">Dashboard</a></li>
                    <li><a href="cart.jsp">Cart</a></li>
                    <li><a href="LogoutServlet">Logout</a></li>
                </ul>
            </nav>
        </div>
    </header>

    <div class="dashboard-container">
        <!-- Welcome Section -->
        <div class="welcome-section">
            <h1>Welcome back, <%= user.getDisplayName() %>!</h1>
            <p>Here's what's happening with your account today.</p>
        </div>

        <!-- Dashboard Grid -->
        <div class="dashboard-grid">
            <!-- Recent Orders Card -->
            <div class="dashboard-card">
                <div class="card-header">
                    <div class="card-icon orders-icon">
                        <i class="fas fa-shopping-bag"></i>
                    </div>
                    <div>
                        <h3>Recent Orders</h3>
                        <p>View and track your orders</p>
                    </div>
                </div>
                <div class="card-content">
                    <p>You have <strong>0</strong> recent orders</p>
                    <a href="orders.jsp" class="btn btn-primary mt-2">View All Orders</a>
                </div>
            </div>

            <!-- Shopping Cart Card -->
            <div class="dashboard-card">
                <div class="card-header">
                    <div class="card-icon cart-icon">
                        <i class="fas fa-shopping-cart"></i>
                    </div>
                    <div>
                        <h3>Shopping Cart</h3>
                        <p>Items ready for checkout</p>
                    </div>
                </div>
                <div class="card-content">
                    <p>Your cart has <strong><span id="cartItemCount">0</span></strong> items</p>
                    <a href="cart.jsp" class="btn btn-primary mt-2">View Cart</a>
                </div>
            </div>

            <!-- Profile Card -->
            <div class="dashboard-card">
                <div class="card-header">
                    <div class="card-icon profile-icon">
                        <i class="fas fa-user"></i>
                    </div>
                    <div>
                        <h3>Profile Settings</h3>
                        <p>Manage your account</p>
                    </div>
                </div>
                <div class="card-content">
                    <p><strong>Email:</strong> <%= user.getEmail() %></p>
                    <p><strong>Phone:</strong> <%= user.getPhone() != null ? user.getPhone() : "Not provided" %></p>
                    <a href="profile.jsp" class="btn btn-primary mt-2">Edit Profile</a>
                </div>
            </div>

            <!-- Wishlist Card -->
            <div class="dashboard-card">
                <div class="card-header">
                    <div class="card-icon wishlist-icon">
                        <i class="fas fa-heart"></i>
                    </div>
                    <div>
                        <h3>Wishlist</h3>
                        <p>Your saved items</p>
                    </div>
                </div>
                <div class="card-content">
                    <p>You have <strong>0</strong> items in wishlist</p>
                    <a href="wishlist.jsp" class="btn btn-primary mt-2">View Wishlist</a>
                </div>
            </div>
        </div>

        <!-- Quick Actions -->
        <div class="quick-actions">
            <a href="products.jsp" class="action-btn">
                <i class="fas fa-search"></i>
                Browse Products
            </a>
            <a href="orders.jsp" class="action-btn">
                <i class="fas fa-history"></i>
                Order History
            </a>
            <a href="addresses.jsp" class="action-btn">
                <i class="fas fa-map-marker-alt"></i>
                Manage Addresses
            </a>
            <a href="account-settings.jsp" class="action-btn">
                <i class="fas fa-cog"></i>
                Account Settings
            </a>
        </div>
    </div>

    <!-- Footer -->
    <footer class="footer">
        <div class="container">
            <div class="footer-content">
                <div class="footer-section">
                    <h3>ShopEasy</h3>
                    <p>Your trusted online shopping destination.</p>
                </div>
            </div>
            <div class="footer-bottom">
                <p>&copy; 2024 ShopEasy. All rights reserved.</p>
            </div>
        </div>
    </footer>

    <script>
        // Update cart count on page load
        document.addEventListener('DOMContentLoaded', function() {
            const cartCount = localStorage.getItem('cartCount') || '0';
            document.getElementById('cartItemCount').textContent = cartCount;
        });
        
        // Add some interactive features
        document.querySelectorAll('.dashboard-card').forEach(card => {
            card.addEventListener('mouseenter', function() {
                this.style.boxShadow = '0 15px 35px rgba(0,0,0,0.15)';
            });
            
            card.addEventListener('mouseleave', function() {
                this.style.boxShadow = '0 5px 15px rgba(0,0,0,0.08)';
            });
        });
    </script>
</body>
</html>