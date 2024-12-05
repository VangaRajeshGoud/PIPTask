import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';  // useNavigate instead of useHistory
import './Employees.css'; // Make sure to import the CSS file

const Employees = () => {
  const [employees, setEmployees] = useState([]);
  const [error, setError] = useState('');
  const [currentPage, setCurrentPage] = useState(2);  // Track the current page
  const [totalPages, setTotalPages] = useState(0);    // Track total pages
  const navigate = useNavigate();  // Hook to navigate programmatically

  // Fetch employees on page load
  useEffect(() => {
    const token = localStorage.getItem('token');
    if (!token) {
      navigate('/login');  // Use navigate instead of history.push
      return;
    }

    const fetchEmployees = async () => {
      try {
        const response = await fetch(`http://localhost:8080/employees/paginated?page=${currentPage}&size=10&sortBy=firstName`, {
          method: 'GET',
          headers: {
            'Authorization': `Bearer ${token}`,
          },
        });

        if (response.ok) {
          const data = await response.json();
          setEmployees(data.content);  // Assuming 'content' contains the employee array
          setTotalPages(data.totalPages);  // Assuming 'totalPages' contains the total number of pages
        } else {
          setError('Failed to fetch employees');
        }
      } catch (err) {
        setError('An error occurred while fetching employees');
      }
    };

    fetchEmployees();
  }, [currentPage, navigate]);  // Re-fetch when the page changes

  // Function to handle deleting an employee
  const handleDelete = async (employeeId) => {
    const token = localStorage.getItem('token');
    if (!token) {
      navigate('/login');
      return;
    }

    const response = await fetch(`http://localhost:8080/employees/deleteEmployee/${employeeId}`, {
      method: 'DELETE',
      headers: {
        'Authorization': `Bearer ${token}`,
      },
    });

    if (response.ok) {
      // Refresh the employee list after successful delete
      setEmployees((prevEmployees) => prevEmployees.filter(employee => employee.id !== employeeId));
    } else {
      setError('Failed to delete employee');
    }
  };

  // Function to handle navigating to edit page
  const handleEdit = (employeeId) => {
    navigate(`/edit-employee/${employeeId}`);
  };

  // Handle pagination (next and previous)
  const handlePagination = (page) => {
    if (page > 0 && page <= totalPages) {
      setCurrentPage(page);
    }
  };

  return (
    <div className="employees-container">
      <h2 className="employees-heading">Employee List</h2>
      {error && <p className="employees-error">{error}</p>}
      <table className="employees-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>First Name</th>
            <th>Last Name</th>
            <th>Email</th>
            <th>Department</th>
            <th>Actions</th> {/* Column for Edit/Delete */}
          </tr>
        </thead>
        <tbody>
          {employees.length > 0 ? (
            employees.map((employee) => (
              <tr key={employee.id}>
                <td>{employee.id}</td>
                <td>{employee.firstName}</td>
                <td>{employee.lastName}</td>
                <td>{employee.email}</td>
                <td>{employee.department}</td>
                <td className="employees-actions">
                  {/* Edit Button */}
                  <button className="edit-btn" onClick={() => handleEdit(employee.id)}>Edit</button>
                  {/* Delete Button */}
                  <button className="delete-btn" onClick={() => handleDelete(employee.id)}>Delete</button>
                </td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan="6">No employees found</td>
            </tr>
          )}
        </tbody>
      </table>

      {/* Pagination Controls (Previous, Page Number, Next) */}
      <div className="employees-pagination">
        <button onClick={() => handlePagination(currentPage - 1)} disabled={currentPage === 1}>Previous</button>
        <span>Page {currentPage} of {totalPages}</span>
        <button onClick={() => handlePagination(currentPage + 1)} disabled={currentPage === totalPages}>Next</button>
      </div>
    </div>
  );
};

export default Employees;
