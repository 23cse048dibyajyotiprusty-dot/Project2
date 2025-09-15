<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.ecommerce.model.User" %>
<%@ page import="com.ecommerce.dao.*" %>
<%@ page import="java.text.DecimalFormat" %>
<%
    // Check if admin is logged in
    User admin = (User) session.getAttribute("user");
    if (admin == null || !"admin@ecommerce.com".equals(admin.getEmail())) {
        response.sendRedirect("../login.jsp");
        return;
    }
    
    // Initialize DAOs and get statistics
    UserDAO userDAO = new UserDAO();
    ProductDAO productDAO = new ProductDAO();
    CategoryDAO categoryDAO = new CategoryDAO();
    
    int totalUsers = userDAO.getTotalUserCount();
    int totalProducts = productDAO.getTotalProductCount();
    int totalCategories = categoryDAO.getTotalCategoryCount();
    
    DecimalFormat formatter = new DecimalFormat("#,###");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard - ShopEasy</title>
    <link rel="stylesheet" href="../css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        .admin-header {
            background: linear-gradient(135deg, #2c3e50 0%, #34495e 100%);
            color: white;
            padding: 1rem 0;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        
        .admin-nav {
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        
        .admin-logo {
            font-size: 1.8rem;
            font-weight: bold;
            color: white;
            text-decoration: none;
        }
        
        .admin-links {
            display: flex;
            list-style: none;
            gap: 2rem;
            align-items: center;
        }
        
        .admin-links a {
            color: white;
            text-decoration: none;
            font-weight: 500;
            padding: 0.5rem 1rem;
            border-radius: 25px;
            transition: all 0.3s ease;
        }
        
        .admin-links a:hover {
            background: rgba(255,255,255,0.2);
        }
        
        .dashboard-container {
            max-width: 1400px;
            margin: 2rem auto;
            padding: 0 1rem;
        }
        
        .dashboard-header {
            margin-bottom: 3rem;
        }
        
        .dashboard-header h1 {
            color: #2c3e50;
            font-size: 2.5rem;
            margin-bottom: 0.5rem;
        }
        
        .dashboard-header p {
            color: #6c757d;
            font-size: 1.1rem;
        }
        
        .stats-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 2rem;
            margin-bottom: 3rem;
        }
        
        .stat-card {
            background: white;
            border-radius: 15px;
            padding: 2rem;
            box-shadow: 0 5px 15px rgba(0,0,0,0.08);
            border: 1px solid #e9ecef;
            transition: transform 0.3s ease;
            position: relative;
            overflow: hidden;
        }
        
        .stat-card::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            height: 4px;
            background: var(--card-color);
        }
        
        .stat-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 15px 35px rgba(0,0,0,0.15);
        }
        
        .stat-card.users { --card-color: linear-gradient(45deg, #667eea, #764ba2); }
        .stat-card.products { --card-color: linear-gradient(45deg, #ff6b6b, #ee5a24); }
        .stat-card.categories { --card-color: linear-gradient(45deg, #27ae60, #2ecc71); }
        .stat-card.orders { --card-color: linear-gradient(45deg, #f39c12, #e67e22); }
        
        .stat-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 1rem;
        }
        
        .stat-icon {
            font-size: 2.5rem;
            opacity: 0.8;
        }
        
        .stat-card.users .stat-icon { color: #667eea; }
        .stat-card.products .stat-icon { color: #ff6b6b; }
        .stat-card.categories .stat-icon { color: #27ae60; }
        .stat-card.orders .stat-icon { color: #f39c12; }
        
        .stat-number {
            font-size: 2.5rem;
            font-weight: bold;
            color: #2c3e50;
            margin-bottom: 0.5rem;
        }
        
        .stat-label {
            color: #6c757d;
            font-size: 1.1rem;
            font-weight: 500;
        }
        
        .stat-change {
            font-size: 0.9rem;
            margin-top: 0.5rem;
        }
        
        .stat-change.positive {
            color: #27ae60;
        }
        
        .stat-change.negative {
            color: #e74c3c;
        }
        
        .quick-actions {
            background: white;
            border-radius: 15px;
            padding: 2rem;
            box-shadow: 0 5px 15px rgba(0,0,0,0.08);
            border: 1px solid #e9ecef;
            margin-bottom: 3rem;
        }
        
        .quick-actions h2 {
            color: #2c3e50;
            margin-bottom: 1.5rem;
        }
        
        .actions-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 1.5rem;
        }
        
        .action-btn {
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 0.75rem;
            padding: 1.25rem;
            background: linear-gradient(45deg, var(--btn-color-1), var(--btn-color-2));
            color: white;
            text-decoration: none;
            border-radius: 10px;
            font-weight: 600;
            transition: all 0.3s ease;
            text-align: center;
        }
        
        .action-btn:hover {
            transform: translateY(-3px);
            box-shadow: 0 8px 25px rgba(0,0,0,0.2);
            color: white;
            text-decoration: none;
        }
        
        .action-btn.manage-users { --btn-color-1: #667eea; --btn-color-2: #764ba2; }
        .action-btn.manage-products { --btn-color-1: #ff6b6b; --btn-color-2: #ee5a24; }
        .action-btn.manage-categories { --btn-color-1: #27ae60; --btn-color-2: #2ecc71; }
        .action-btn.view-orders { --btn-color-1: #f39c12; --btn-color-2: #e67e22; }
        .action-btn.reports { --btn-color-1: #9b59b6; --btn-color-2: #8e44ad; }
        .action-btn.settings { --btn-color-1: #34495e; --btn-color-2: #2c3e50; }
        
        .recent-activity {
            background: white;
            border-radius: 15px;
            padding: 2rem;
            box-shadow: 0 5px 15px rgba(0,0,0,0.08);
            border: 1px solid #e9ecef;
        }
        
        .recent-activity h2 {
            color: #2c3e50;
            margin-bottom: 1.5rem;
        }
        
        .activity-list {
            list-style: none;
        }
        
        .activity-item {
            display: flex;
            align-items: center;
            gap: 1rem;
            padding: 1rem 0;
            border-bottom: 1px solid #f8f9fa;
        }
        
        .activity-item:last-child {
            border-bottom: none;
        }
        
        .activity-icon {
            width: 40px;
            height: 40px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 1.2rem;
            color: white;
        }
        
        .activity-icon.user { background: linear-gradient(45deg, #667eea, #764ba2); }
        .activity-icon.product { background: linear-gradient(45deg, #ff6b6b, #ee5a24); }
        .activity-icon.order { background: linear-gradient(45deg, #f39c12, #e67e22); }
        
        .activity-details {
            flex: 1;
        }
        
        .activity-title {
            font-weight: 600;
            color: #2c3e50;
            margin-bottom: 0.25rem;
        }
        
        .activity-time {
            font-size: 0.9rem;
            color: #6c757d;
        }
        
        @media (max-width: 768px) {
            .stats-grid {
                grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            }
            
            .actions-grid {
                grid-template-columns: 1fr;
            }
        }
    </style>
</head>
<body>
    <!-- Admin Header -->
    <header class="admin-header">
        <div class="container">
            <nav class="admin-nav">
                <a href="dashboard.jsp" class="admin-logo">
                    <i class="fas fa-cogs"></i> ShopEasy Admin
                </a>
                
                <ul class="admin-links">
                    <li><a href="dashboard.jsp">Dashboard</a></li>
                    <li><a href="users.jsp">Users</a></li>
                    <li><a href="products.jsp">Products</a></li>
                    <li><a href="categories.jsp">Categories</a></li>
                    <li><a href="orders.jsp">Orders</a></li>
                    <li><a href="../index.html">View Site</a></li>
                    <li><a href="../LogoutServlet">Logout</a></li>
                </ul>
            </nav>
        </div>
    </header>

    <div class="dashboard-container">
        <!-- Dashboard Header -->
        <div class="dashboard-header">
            <h1>Admin Dashboard</h1>
            <p>Welcome back, <%= admin.getDisplayName() %>! Here's what's happening with your store today.</p>
        </div>

        <!-- Statistics Grid -->
        <div class="stats-grid">
            <div class="stat-card users">
                <div class="stat-header">
                    <div class="stat-icon">
                        <i class="fas fa-users"></i>
                    </div>
                </div>
                <div class="stat-number"><%= formatter.format(totalUsers) %></div>
                <div class="stat-label">Total Users</div>
                <div class="stat-change positive">
                    <i class="fas fa-arrow-up"></i> +12% from last month
                </div>
            </div>

            <div class="stat-card products">
                <div class="stat-header">
                    <div class="stat-icon">
                        <i class="fas fa-box"></i>
                    </div>
                </div>
                <div class="stat-number"><%= formatter.format(totalProducts) %></div>
                <div class="stat-label">Total Products</div>
                <div class="stat-change positive">
                    <i class="fas fa-arrow-up"></i> +3 new this week
                </div>
            </div>

            <div class="stat-card categories">
                <div class="stat-header">
                    <div class="stat-icon">
                        <i class="fas fa-tags"></i>
                    </div>
                </div>
                <div class="stat-number"><%= formatter.format(totalCategories) %></div>
                <div class="stat-label">Categories</div>
                <div class="stat-change">
                    <i class="fas fa-minus"></i> No change
                </div>
            </div>

            <div class="stat-card orders">
                <div class="stat-header">
                    <div class="stat-icon">
                        <i class="fas fa-shopping-cart"></i>
                    </div>
                </div>
                <div class="stat-number">0</div>
                <div class="stat-label">Total Orders</div>
                <div class="stat-change">
                    <i class="fas fa-info-circle"></i> Ready for business
                </div>
            </div>
        </div>

        <!-- Quick Actions -->
        <div class="quick-actions">
            <h2><i class="fas fa-bolt"></i> Quick Actions</h2>
            <div class="actions-grid">
                <a href="users.jsp" class="action-btn manage-users">
                    <i class="fas fa-users-cog"></i>
                    Manage Users
                </a>
                <a href="products.jsp" class="action-btn manage-products">
                    <i class="fas fa-boxes"></i>
                    Manage Products
                </a>
                <a href="categories.jsp" class="action-btn manage-categories">
                    <i class="fas fa-list"></i>
                    Manage Categories
                </a>
                <a href="orders.jsp" class="action-btn view-orders">
                    <i class="fas fa-clipboard-list"></i>
                    View Orders
                </a>
                <a href="reports.jsp" class="action-btn reports">
                    <i class="fas fa-chart-bar"></i>
                    View Reports
                </a>
                <a href="settings.jsp" class="action-btn settings">
                    <i class="fas fa-cog"></i>
                    Settings
                </a>
            </div>
        </div>

        <!-- Recent Activity -->
        <div class="recent-activity">
            <h2><i class="fas fa-history"></i> Recent Activity</h2>
            <ul class="activity-list">
                <li class="activity-item">
                    <div class="activity-icon user">
                        <i class="fas fa-user-plus"></i>
                    </div>
                    <div class="activity-details">
                        <div class="activity-title">System initialized successfully</div>
                        <div class="activity-time">Database connected and ready</div>
                    </div>
                </li>
                <li class="activity-item">
                    <div class="activity-icon product">
                        <i class="fas fa-box"></i>
                    </div>
                    <div class="activity-details">
                        <div class="activity-title">Sample products loaded</div>
                        <div class="activity-time"><%= totalProducts %> products available in catalog</div>
                    </div>
                </li>
                <li class="activity-item">
                    <div class="activity-icon order">
                        <i class="fas fa-store"></i>
                    </div>
                    <div class="activity-details">
                        <div class="activity-title">Store is ready for customers</div>
                        <div class="activity-time">All systems operational</div>
                    </div>
                </li>
            </ul>
        </div>
    </div>

    <script>
        // Add some interactive features
        document.addEventListener('DOMContentLoaded', function() {
            // Animate stat numbers on page load
            const statNumbers = document.querySelectorAll('.stat-number');
            statNumbers.forEach(stat => {
                const finalValue = parseInt(stat.textContent.replace(/,/g, ''));
                let currentValue = 0;
                const increment = finalValue / 50;
                const timer = setInterval(() => {
                    currentValue += increment;
                    if (currentValue >= finalValue) {
                        currentValue = finalValue;
                        clearInterval(timer);
                    }
                    stat.textContent = Math.floor(currentValue).toLocaleString();
                }, 30);
            });
            
            // Add hover effects to cards
            const cards = document.querySelectorAll('.stat-card, .quick-actions .action-btn');
            cards.forEach(card => {
                card.addEventListener('mouseenter', function() {
                    this.style.transform = 'translateY(-5px) scale(1.02)';
                });
                
                card.addEventListener('mouseleave', function() {
                    this.style.transform = 'translateY(0) scale(1)';
                });
            });
        });
        
        // Auto-refresh dashboard data every 5 minutes
        setInterval(function() {
            console.log('Dashboard data refresh...');
            // In a real application, you would fetch updated statistics here
        }, 300000);
    </script>
</body>
</html>