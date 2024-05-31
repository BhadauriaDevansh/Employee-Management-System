USE [EmployeeManagement]
GO
/****** Object:  StoredProcedure [dbo].[DisplayEmployee]    Script Date: 31-05-2024 21:38:23 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
ALTER PROCEDURE [dbo].[DisplayEmployee]
AS
BEGIN
    SET NOCOUNT ON; -- To prevent the row count from being displayed

    -- Drop the temporary table if it already exists
    IF OBJECT_ID('tempdb..#TempEmployee') IS NOT NULL
        DROP TABLE #TempEmployee;

    -- Create a temporary table to hold the employee data
    CREATE TABLE #TempEmployee (
        ID INT,
        EmpID VARCHAR(50),
        Name VARCHAR(100),
        Position VARCHAR(100),
        Salary DECIMAL(10, 2)
    );

    -- Insert data into the temporary table
    INSERT INTO #TempEmployee (ID, EmpID, Name, Position, Salary)
    SELECT ID, EmpID, Name, Position, Salary FROM EMPLOYEE;

    -- Select all data from the temporary table
    SELECT * FROM #TempEmployee;

    -- Drop the temporary table
    DROP TABLE #TempEmployee;
END;