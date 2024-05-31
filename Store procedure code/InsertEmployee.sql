USE [EmployeeManagement]
GO
/****** Object:  StoredProcedure [dbo].[InsertEmployee]    Script Date: 31-05-2024 21:41:27 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
ALTER PROCEDURE [dbo].[InsertEmployee]
    @Name NVARCHAR(50), 
    @Position NVARCHAR(50),
    @Salary FLOAT
AS
BEGIN
    DECLARE @CurrentYear CHAR(4)
    DECLARE @MaxEmpId INT
    DECLARE @NewEmpId VARCHAR(10)

    SET @CurrentYear = CONVERT(char(4), GETDATE(), 112) -- 2024

    SELECT @MaxEmpId = MAX(CAST(SUBSTRING(EmpID, 5, 3) AS INT)) FROM Employee  WHERE SUBSTRING(EmpID, 1, 4) = @CurrentYear -- if 2024001 then MaxEmpId =1

    IF @MaxEmpId IS NULL
        SET @MaxEmpId = 0

    SET @NewEmpId = @CurrentYear + RIGHT('000' + CAST(@MaxEmpId + 1 AS VARCHAR(3)), 3)

    -- Insert the new employee record
    INSERT INTO Employee ([EmpID], [Name], [Position], [Salary])
    VALUES (@NewEmpId, @Name, @Position, @Salary);

    -- Set Payed column to 0 (boolean) in the PAYROLL table for the new employee
  --  INSERT INTO PAYROLL (EID, Payed) VALUES ((SELECT ID FROM Employee WHERE EmpID = @NewEmpId), 0);
  
END;