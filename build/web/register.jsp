<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register - ShopEasy</title>
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        .register-container {
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            padding: 2rem 0;
        }
        
        .register-card {
            background: white;
            padding: 3rem;
            border-radius: 20px;
            box-shadow: 0 20px 40px rgba(0,0,0,0.1);
            width: 100%;
            max-width: 500px;
            text-align: center;
        }
        
        .register-header {
            margin-bottom: 2rem;
        }
        
        .register-header h1 {
            color: #2c3e50;
            font-size: 2rem;
            margin-bottom: 0.5rem;
        }
        
        .register-header p {
            color: #6c757d;
        }
        
        .form-row {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 1rem;
        }
        
        .form-group {
            text-align: left;
            margin-bottom: 1.5rem;
        }
        
        .form-group.full-width {
            grid-column: 1 / -1;
        }
        
        .form-group label {
            display: block;
            margin-bottom: 0.5rem;
            font-weight: 600;
            color: #2c3e50;
        }
        
        .form-control {
            width: 100%;
            padding: 1rem;
            border: 1px solid #ddd;
            border-radius: 10px;
            font-size: 1rem;
            transition: all 0.3s ease;
        }
        
        .form-control:focus {
            outline: none;
            border-color: #667eea;
            box-shadow: 0 0 0 0.2rem rgba(102, 126, 234, 0.25);
        }
        
        .password-strength {
            margin-top: 0.5rem;
            font-size: 0.9rem;
        }
        
        .strength-weak { color: #e74c3c; }
        .strength-fair { color: #f39c12; }
        .strength-good { color: #2ecc71; }
        .strength-strong { color: #27ae60; }
        
        .btn-register {
            width: 100%;
            padding: 1rem;
            background: linear-gradient(45deg, #667eea, #764ba2);
            color: white;
            border: none;
            border-radius: 10px;
            font-size: 1.1rem;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s ease;
            margin-bottom: 1rem;
        }
        
        .btn-register:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
        }
        
        .btn-register:disabled {
            opacity: 0.6;
            cursor: not-allowed;
        }
        
        .terms-checkbox {
            display: flex;
            align-items: center;
            text-align: left;
            margin-bottom: 1.5rem;
        }
        
        .terms-checkbox input {
            margin-right: 0.5rem;
        }
        
        .alert {
            padding: 1rem;
            border-radius: 8px;
            margin-bottom: 1rem;
        }
        
        .alert-error {
            background: #fee;
            color: #c33;
            border: 1px solid #fcc;
        }
        
        .alert-success {
            background: #efe;
            color: #3c3;
            border: 1px solid #cfc;
        }
        
        @media (max-width: 768px) {
            .form-row {
                grid-template-columns: 1fr;
            }
            
            .register-card {
                margin: 1rem;
                padding: 2rem;
            }
        }
    </style>
</head>
<body>
    <div class="register-container">
        <div class="register-card fade-in">
            <div class="register-header">
                <h1><i class="fas fa-user-plus"></i> Join ShopEasy</h1>
                <p>Create your account to start shopping</p>
            </div>
            
            <% 
                String error = request.getParameter("error");
                String success = request.getParameter("success");
                if (error != null) {
            %>
                <div class="alert alert-error">
                    <i class="fas fa-exclamation-triangle"></i>
                    <%= error.equals("1") ? "Please fill in all required fields" : 
                        error.equals("2") ? "Email already exists" :
                        error.equals("3") ? "Passwords do not match" :
                        error.equals("4") ? "Invalid email format" :
                        error.equals("5") ? "Password too weak" : "Registration failed" %>
                </div>
            <% } %>
            
            <% if (success != null) { %>
                <div class="alert alert-success">
                    <i class="fas fa-check-circle"></i>
                    Registration successful! You can now log in.
                </div>
            <% } %>
            
            <form action="RegisterServlet" method="post" id="registerForm">
                <div class="form-row">
                    <div class="form-group">
                        <label for="firstName">First Name *</label>
                        <input type="text" id="firstName" name="firstName" class="form-control" 
                               placeholder="Enter first name" required>
                    </div>
                    
                    <div class="form-group">
                        <label for="lastName">Last Name *</label>
                        <input type="text" id="lastName" name="lastName" class="form-control" 
                               placeholder="Enter last name" required>
                    </div>
                </div>
                
                <div class="form-group full-width">
                    <label for="email">Email Address *</label>
                    <input type="email" id="email" name="email" class="form-control" 
                           placeholder="Enter your email" required>
                </div>
                
                <div class="form-group full-width">
                    <label for="phone">Phone Number</label>
                    <input type="tel" id="phone" name="phone" class="form-control" 
                           placeholder="Enter phone number">
                </div>
                
                <div class="form-row">
                    <div class="form-group">
                        <label for="password">Password *</label>
                        <input type="password" id="password" name="password" class="form-control" 
                               placeholder="Enter password" required>
                        <div class="password-strength" id="passwordStrength"></div>
                    </div>
                    
                    <div class="form-group">
                        <label for="confirmPassword">Confirm Password *</label>
                        <input type="password" id="confirmPassword" name="confirmPassword" class="form-control" 
                               placeholder="Confirm password" required>
                    </div>
                </div>
                
                <div class="terms-checkbox">
                    <input type="checkbox" id="terms" name="terms" required>
                    <label for="terms">I agree to the <a href="#" target="_blank">Terms of Service</a> and <a href="#" target="_blank">Privacy Policy</a></label>
                </div>
                
                <button type="submit" class="btn-register" id="submitBtn">
                    <i class="fas fa-user-plus"></i> Create Account
                </button>
            </form>
            
            <div class="login-links">
                <p>Already have an account? <a href="login.jsp">Sign in here</a></p>
            </div>
            
            <div class="login-links">
                <a href="index.html">← Back to Home</a>
            </div>
        </div>
    </div>
    
    <script>
        // Form validation and password strength checking
        document.addEventListener('DOMContentLoaded', function() {
            const form = document.getElementById('registerForm');
            const password = document.getElementById('password');
            const confirmPassword = document.getElementById('confirmPassword');
            const strengthIndicator = document.getElementById('passwordStrength');
            const submitBtn = document.getElementById('submitBtn');
            
            // Password strength checker
            password.addEventListener('input', function() {
                const strength = checkPasswordStrength(this.value);
                updatePasswordStrength(strength);
            });
            
            // Confirm password validation
            confirmPassword.addEventListener('input', function() {
                validatePasswordMatch();
            });
            
            // Form submission
            form.addEventListener('submit', function(e) {
                if (!validateForm()) {
                    e.preventDefault();
                    return false;
                }
                
                // Show loading state
                submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Creating Account...';
                submitBtn.disabled = true;
            });
            
            function checkPasswordStrength(password) {
                let strength = 0;
                const checks = {
                    length: password.length >= 8,
                    lowercase: /[a-z]/.test(password),
                    uppercase: /[A-Z]/.test(password),
                    numbers: /[0-9]/.test(password),
                    special: /[@#$%^&+=]/.test(password)
                };
                
                strength = Object.values(checks).filter(Boolean).length;
                
                return {
                    score: strength,
                    checks: checks,
                    text: strength === 0 ? '' :
                          strength <= 2 ? 'Weak' :
                          strength === 3 ? 'Fair' :
                          strength === 4 ? 'Good' : 'Strong'
                };
            }
            
            function updatePasswordStrength(strength) {
                strengthIndicator.textContent = strength.text;
                strengthIndicator.className = 'password-strength';
                
                if (strength.text) {
                    strengthIndicator.classList.add('strength-' + strength.text.toLowerCase());
                }
            }
            
            function validatePasswordMatch() {
                const match = password.value === confirmPassword.value;
                confirmPassword.setCustomValidity(match ? '' : 'Passwords do not match');
            }
            
            function validateForm() {
                const firstName = document.getElementById('firstName').value.trim();
                const lastName = document.getElementById('lastName').value.trim();
                const email = document.getElementById('email').value.trim();
                const pass = password.value;
                const confirmPass = confirmPassword.value;
                const terms = document.getElementById('terms').checked;
                
                if (!firstName || !lastName || !email || !pass || !confirmPass) {
                    alert('Please fill in all required fields.');
                    return false;
                }
                
                if (!isValidEmail(email)) {
                    alert('Please enter a valid email address.');
                    return false;
                }
                
                if (pass !== confirmPass) {
                    alert('Passwords do not match.');
                    return false;
                }
                
                const strength = checkPasswordStrength(pass);
                if (strength.score < 3) {
                    alert('Password is too weak. Please use at least 8 characters with uppercase, lowercase, numbers, and special characters.');
                    return false;
                }
                
                if (!terms) {
                    alert('Please agree to the Terms of Service and Privacy Policy.');
                    return false;
                }
                
                return true;
            }
            
            function isValidEmail(email) {
                const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
                return emailRegex.test(email);
            }
        });
    </script>
</body>
</html>