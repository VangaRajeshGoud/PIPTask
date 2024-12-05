
// src/components/HomePage.js
import React from 'react';
import { Link } from 'react-router-dom';
import './HomePage.css';  

const HomePage = () => {
    return (
        <div className="home-page">
            <header className="header">
                <h1>Welcome to the Employee Management System</h1>
            </header>
            
            <nav className="navbar">
                <ul>
                    <li><Link to="/login">Login</Link></li>
                    <li><Link to="/register">Register</Link></li>
                   
                </ul>
            </nav>

            <main className="main-content">
                <h2>This Portal to Manage Employees Efficiently</h2>
                <p>Use the navigation menu above to get started with the Employee Management System.</p>
                <p>Login to access employee data, or register if you're a new user!</p>
            </main>

            <footer className="footer">
                <p>&copy;2024 Employee Management System</p>
            </footer>
        </div>
    );
};

export default HomePage;
