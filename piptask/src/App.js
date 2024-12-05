// src/App.js
import React from 'react';
import { BrowserRouter as Router, Route, Routes, Link } from 'react-router-dom';
import Login from './components/Login';
import Register from './components/Register';
import Employees from './components/Employees';
import EditEmployee from './components/EditEmployee'; 
import HomePage from './components/HomePage';

const App = () => {
  return (
    <Router>
      <div>
        {/* Navigation Links */}
        

        {/* Routing */}
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="/employees" element={<Employees />} /> 
          <Route path="/edit-employee/:id" element={<EditEmployee />} />
          <Route path="/" element={<HomePage />} />

          {/* Catch-all Route */}
          <Route path="/" element={<h2></h2>} />
        </Routes>
      </div>
    </Router>
  );
};

export default App;
