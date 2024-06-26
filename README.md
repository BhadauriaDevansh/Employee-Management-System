# Employee Management System

## Overview

The Employee Management System (EMS) is a Java-based application designed to streamline the process of managing employee data, attendance, and payroll securely. Built on MSSQL, the system employs stored procedures to prevent SQL injection attacks, ensuring robust data security.

## Features

### 1. Employee Management

- Add, edit, and delete employee records.
- View employee details including name, position, and salary.
- Update employee information dynamically using a graphical user interface (GUI).

### 2. Attendance Tracking

- Record employee attendance with date and hours worked.
- View and manage attendance records.
- Automatically update attendance data in real-time.

### 3. Payroll Management

- Track employee payroll details such as hourly rates and total earnings.
- Generate payroll reports based on attendance and salary data.

### 4. User Interface

- Utilizes Java Swing for a user-friendly interface.
- Includes form validation and error handling for data input.

### 5. Database Integration

- Connects to an MSSQL database for storing and retrieving employee data securely.
- Implements stored procedures to prevent SQL injection attacks and ensure data integrity.

## Project Structure

The project is structured into the following main components:

1. **EMSManager**: Java class responsible for managing database connections and executing SQL queries using stored procedures for employee data manipulation.
2. **EMSUI**: Java Swing-based user interface for interacting with the EMS system, including forms for adding/editing employees and managing attendance.
3. **Stored Procedures**: Folder containing SQL stored procedures used for database operations, ensuring data integrity and security.

## Getting Started

To run the Employee Management System on your local machine, follow these steps:

1. Clone the repository to your local environment.
2. Import the project into your preferred Java IDE (e.g., IntelliJ IDEA, Eclipse).
3. Set up an MSSQL database and configure the connection details.
4. Execute the SQL scripts in the `Store procedure code` folder to create the necessary stored procedures in your database.
5. Build and run the application from your IDE.

