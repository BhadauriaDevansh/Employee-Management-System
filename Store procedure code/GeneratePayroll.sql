USE [EmployeeManagement]
GO
/****** Object:  StoredProcedure [dbo].[GeneratePayroll]    Script Date: 31-05-2024 21:40:39 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
ALTER PROCEDURE [dbo].[GeneratePayroll]
AS
BEGIN
   UPDATE Employee
		SET Salary = ISNULL(P.HoursWorked, 0) * ISNULL(P.PayRate, 0)
		FROM Employee E
		INNER JOIN PAYROLL P ON E.ID = P.EID
		WHERE [MONTH] = DATENAME(MONTH, GETDATE()) 
    AND [YEAR] = DATENAME(YEAR, GETDATE()) 
    AND P.Payed = 0;

UPDATE PAYROLL
	SET Payed = 1
	FROM PAYROLL P
	INNER JOIN Employee E ON E.ID = P.EID
	WHERE [MONTH] = DATENAME(MONTH, GETDATE()) 
    AND [YEAR] = DATENAME(YEAR, GETDATE()) 
    AND P.Payed = 0;

END;