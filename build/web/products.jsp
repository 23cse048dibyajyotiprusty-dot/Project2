<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.ecommerce.model.User" %>
<%@ page import="com.ecommerce.dao.ProductDAO" %>
<%@ page import="com.ecommerce.dao.CategoryDAO" %>
<%@ page import="com.ecommerce.model.Product" %>
<%@ page import="com.ecommerce.model.Category" %>
<%@ page import="java.util.List" %>
<%@ page import="java.math.BigDecimal" %>

<%
    // Get search parameters
    String search = request.getParameter("search");
    String categoryParam = request.getParameter("category");
    String sortParam = request.getParameter("sort");
    String minPriceParam = request.getParameter("minPrice");
    String maxPriceParam = request.getParameter("maxPrice");
    
    // Set defaults
    int categoryId = 0;
    if (categoryParam != null && !categoryParam.isEmpty()) {
        try {
            categoryId = Integer.parseInt(categoryParam);
        } catch (NumberFormatException e) {
            categoryId = 0;
        }
    }
    
    BigDecimal minPrice = null;
    BigDecimal maxPrice = null;
    if (minPriceParam != null && !minPriceParam.isEmpty()) {
        try {
            minPrice = new BigDecimal(minPriceParam);
        } catch (NumberFormatException e) {
            minPrice = null;
        }
    }
    if (maxPriceParam != null && !maxPriceParam.isEmpty()) {
        try {
            maxPrice = new BigDecimal(maxPriceParam);
        } catch (NumberFormatException e) {
            maxPrice = null;
        }
    }
    
    // Get current user
    User currentUser = (User) session.getAttribute("user");
    
    // Initialize DAOs
    ProductDAO productDAO = new ProductDAO();
    CategoryDAO categoryDAO = new CategoryDAO();
    
    // Get products and categories
    List<Product> products = productDAO.searchProducts(search, categoryId, sortParam, minPrice, maxPrice, 1, 20);
    List<Category> categories = categoryDAO.getAllActiveCategories();
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Products - ShopEasy</title>
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        .products-page {
            padding: 2rem 0;
            min-height: 100vh;
        }
        
        .page-header {
            text-align: center;
            margin-bottom: 3rem;
        }
        
        .page-header h1 {
            color: #2c3e50;
            font-size: 2.5rem;
            margin-bottom: 1rem;
        }
        
        .filters-section {
            background: white;
            padding: 2rem;
            border-radius: 15px;
            box-shadow: 0 5px 15px rgba(0,0,0,0.08);
            margin-bottom: 2rem;
        }
        
        .filters-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 1.5rem;
            align-items: end;
        }
        
        .filter-group {
            display: flex;
            flex-direction: column;
        }
        
        .filter-group label {
            font-weight: 600;
            color: #2c3e50;
            margin-bottom: 0.5rem;
        }
        
        .filter-control {
            padding: 0.75rem;
            border: 1px solid #ddd;
            border-radius: 8px;
            font-size: 1rem;
        }
        
        .filter-control:focus {
            outline: none;
            border-color: #667eea;
            box-shadow: 0 0 0 0.2rem rgba(102, 126, 234, 0.25);
        }
        
        .search-results {
            margin-bottom: 2rem;
        }
        
        .results-info {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 1.5rem;
            padding: 1rem;
            background: #f8f9fa;
            border-radius: 8px;
        }
        
        .results-count {
            font-weight: 600;
            color: #2c3e50;
        }
        
        .sort-options {
            display: flex;
            align-items: center;
            gap: 0.5rem;
        }
        
        .no-products {
            text-align: center;
            padding: 4rem 2rem;
            background: white;
            border-radius: 15px;
            box-shadow: 0 5px 15px rgba(0,0,0,0.08);
        }
        
        .no-products i {
            font-size: 4rem;
            color: #6c757d;
            margin-bottom: 1rem;
        }
        
        .no-products h3 {
            color: #2c3e50;
            margin-bottom: 0.5rem;
        }
        
        .no-products p {
            color: #6c757d;
        }
        
        @media (max-width: 768px) {
            .results-info {
                flex-direction: column;
                gap: 1rem;
                text-align: center;
            }
            
            .filters-grid {
                grid-template-columns: 1fr;
            }
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
                
                <div class="search-box">
                    <input type="text" placeholder="Search products..." id="searchInput" 
                           value="<%= search != null ? search : "" %>">
                    <button type="button" onclick="searchProducts()">
                        <i class="fas fa-search"></i>
                    </button>
                </div>
                
                <ul class="nav-links">
                    <li><a href="index.html">Home</a></li>
                    <li><a href="products.jsp">Products</a></li>
                    <li><a href="categories.jsp">Categories</a></li>
                    <% if (currentUser != null) { %>
                        <li><a href="dashboard.jsp">Dashboard</a></li>
                        <li><a href="cart.jsp">Cart</a></li>
                        <li><a href="LogoutServlet">Logout</a></li>
                    <% } else { %>
                        <li><a href="login.jsp">Login</a></li>
                        <li><a href="register.jsp">Register</a></li>
                    <% } %>
                    <li>
                        <a href="cart.jsp" class="cart-icon">
                            <i class="fas fa-shopping-cart"></i>
                            <span class="cart-count" id="cartCount">0</span>
                        </a>
                    </li>
                </ul>
            </nav>
        </div>
    </header>

    <div class="products-page">
        <div class="container">
            <!-- Page Header -->
            <div class="page-header">
                <h1>
                    <% if (search != null && !search.isEmpty()) { %>
                        Search Results for "<%= search %>"
                    <% } else if (categoryId > 0) { %>
                        <% 
                            for (Category cat : categories) {
                                if (cat.getCategoryId() == categoryId) {
                                    out.print(cat.getName());
                                    break;
                                }
                            }
                        %>
                    <% } else { %>
                        All Products
                    <% } %>
                </h1>
                <p>Discover amazing products at great prices</p>
            </div>

            <!-- Filters Section -->
            <div class="filters-section">
                <form method="get" action="products.jsp" id="filtersForm">
                    <div class="filters-grid">
                        <div class="filter-group">
                            <label for="searchFilter">Search</label>
                            <input type="text" id="searchFilter" name="search" class="filter-control"
                                   placeholder="Search products..." value="<%= search != null ? search : "" %>">
                        </div>
                        
                        <div class="filter-group">
                            <label for="categoryFilter">Category</label>
                            <select id="categoryFilter" name="category" class="filter-control">
                                <option value="">All Categories</option>
                                <% for (Category category : categories) { %>
                                    <option value="<%= category.getCategoryId() %>" 
                                            <%= categoryId == category.getCategoryId() ? "selected" : "" %>>
                                        <%= category.getName() %>
                                    </option>
                                <% } %>
                            </select>
                        </div>
                        
                        <div class="filter-group">
                            <label for="minPrice">Min Price</label>
                            <input type="number" id="minPrice" name="minPrice" class="filter-control"
                                   placeholder="0" min="0" step="0.01" 
                                   value="<%= minPrice != null ? minPrice.toString() : "" %>">
                        </div>
                        
                        <div class="filter-group">
                            <label for="maxPrice">Max Price</label>
                            <input type="number" id="maxPrice" name="maxPrice" class="filter-control"
                                   placeholder="10000" min="0" step="0.01"
                                   value="<%= maxPrice != null ? maxPrice.toString() : "" %>">
                        </div>
                        
                        <div class="filter-group">
                            <label for="sortBy">Sort By</label>
                            <select id="sortBy" name="sort" class="filter-control">
                                <option value="">Default</option>
                                <option value="name_asc" <%= "name_asc".equals(sortParam) ? "selected" : "" %>>Name A-Z</option>
                                <option value="name_desc" <%= "name_desc".equals(sortParam) ? "selected" : "" %>>Name Z-A</option>
                                <option value="price_asc" <%= "price_asc".equals(sortParam) ? "selected" : "" %>>Price Low to High</option>
                                <option value="price_desc" <%= "price_desc".equals(sortParam) ? "selected" : "" %>>Price High to Low</option>
                                <option value="newest" <%= "newest".equals(sortParam) ? "selected" : "" %>>Newest First</option>
                            </select>
                        </div>
                        
                        <div class="filter-group">
                            <button type="submit" class="btn btn-primary">
                                <i class="fas fa-search"></i> Apply Filters
                            </button>
                        </div>
                    </div>
                </form>
            </div>

            <!-- Results Info -->
            <div class="results-info">
                <div class="results-count">
                    Found <%= products.size() %> product<%= products.size() != 1 ? "s" : "" %>
                </div>
                <div class="sort-options">
                    <a href="products.jsp" class="btn btn-secondary">
                        <i class="fas fa-times"></i> Clear Filters
                    </a>
                </div>
            </div>

            <!-- Products Grid -->
            <% if (products.isEmpty()) { %>
                <div class="no-products">
                    <i class="fas fa-search"></i>
                    <h3>No products found</h3>
                    <p>Try adjusting your search criteria or browse all categories.</p>
                    <a href="products.jsp" class="btn btn-primary mt-3">View All Products</a>
                </div>
            <% } else { %>
                <div class="products-grid">
                    <% for (Product product : products) { %>
                        <div class="product-card fade-in">
                            <div class="product-image">
                                <% 
                                    String emoji = "📱";
                                    if (product.getCategoryId() == 2) emoji = "👕";
                                    else if (product.getCategoryId() == 3) emoji = "📖";
                                    else if (product.getCategoryId() == 4) emoji = "🏠";
                                    else if (product.getCategoryId() == 5) emoji = "⚽";
                                %>
                                <%= emoji %>
                            </div>
                            <div class="product-info">
                                <h3 class="product-title"><%= product.getName() %></h3>
                                <p class="product-description"><%= product.getShortDescription() %></p>
                                <div class="product-price"><%= product.getFormattedPrice() %></div>
                                <div class="product-rating">
                                    <span class="stars">★★★★☆</span>
                                    <span>(<%= (int)(Math.random() * 200) + 10 %> reviews)</span>
                                </div>
                                <div class="product-stock">
                                    <% if (product.isInStock()) { %>
                                        <span class="stock-available">✓ In Stock</span>
                                    <% } else { %>
                                        <span class="stock-unavailable">✗ Out of Stock</span>
                                    <% } %>
                                </div>
                                <% if (product.isInStock()) { %>
                                    <button class="btn-add-to-cart" onclick="addToCart(<%= product.getProductId() %>)">
                                        <i class="fas fa-shopping-cart"></i> Add to Cart
                                    </button>
                                <% } else { %>
                                    <button class="btn-add-to-cart" disabled style="opacity: 0.5; cursor: not-allowed;">
                                        <i class="fas fa-times"></i> Out of Stock
                                    </button>
                                <% } %>
                            </div>
                        </div>
                    <% } %>
                </div>
            <% } %>
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
        // Initialize cart count on page load
        document.addEventListener('DOMContentLoaded', function() {
            updateCartCount();
        });
        
        // Search functionality
        function searchProducts() {
            const searchTerm = document.getElementById('searchInput').value;
            window.location.href = `products.jsp?search=${encodeURIComponent(searchTerm)}`;
        }
        
        // Allow search on Enter key
        document.getElementById('searchInput').addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                searchProducts();
            }
        });
        
        // Add to cart functionality
        function addToCart(productId) {
            <% if (currentUser != null) { %>
                // User is logged in, make AJAX call to add to cart
                fetch('CartServlet', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    body: `action=add&productId=${productId}&quantity=1`
                })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        updateCartCount();
                        showMessage('Product added to cart!', 'success');
                    } else {
                        showMessage(data.message || 'Failed to add product to cart', 'error');
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    showMessage('Error adding product to cart', 'error');
                });
            <% } else { %>
                // User not logged in, use localStorage
                let cartCount = parseInt(localStorage.getItem('cartCount') || '0');
                cartCount++;
                localStorage.setItem('cartCount', cartCount.toString());
                updateCartCount();
                showMessage('Product added to cart! Please login to checkout.', 'success');
            <% } %>
        }
        
        // Update cart count display
        function updateCartCount() {
            <% if (currentUser != null) { %>
                // Fetch actual cart count from server
                fetch('CartServlet?action=count')
                    .then(response => response.json())
                    .then(data => {
                        document.getElementById('cartCount').textContent = data.count || '0';
                    })
                    .catch(() => {
                        document.getElementById('cartCount').textContent = '0';
                    });
            <% } else { %>
                // Use localStorage count
                const cartCount = localStorage.getItem('cartCount') || '0';
                document.getElementById('cartCount').textContent = cartCount;
            <% } %>
        }
        
        // Show message to user
        function showMessage(message, type) {
            const messageDiv = document.createElement('div');
            messageDiv.className = `alert alert-${type}`;
            messageDiv.textContent = message;
            messageDiv.style.cssText = `
                position: fixed;
                top: 20px;
                right: 20px;
                padding: 1rem 2rem;
                background: ${type === 'success' ? '#27ae60' : '#e74c3c'};
                color: white;
                border-radius: 8px;
                z-index: 10000;
                box-shadow: 0 4px 15px rgba(0,0,0,0.2);
            `;
            
            document.body.appendChild(messageDiv);
            
            setTimeout(() => {
                messageDiv.remove();
            }, 3000);
        }
        
        // Auto-submit filters on change (optional)
        document.querySelectorAll('.filter-control').forEach(control => {
            if (control.type === 'select-one') {
                control.addEventListener('change', function() {
                    // Auto-submit form when dropdown changes
                    // document.getElementById('filtersForm').submit();
                });
            }
        });
    </script>
</body>
</html>