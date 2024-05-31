USE [EmployeeManagement]
GO
/****** Object:  StoredProcedure [dbo].[InsertPayroll]    Script Date: 31-05-2024 21:42:07 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
ALTER PROCEDURE [dbo].[InsertPayroll] (
    @EID INT,
    @HoursWorked INT,
    @PayRate DECIMAL(10, 2),
    @Month VARCHAR(20),
    @Year INT
)
AS
BEGIN
    SET NOCOUNT ON;

    INSERT INTO PAYROLL (EID, HoursWorked, PayRate, [Month], [Year])
    VALUES (@EID, @HoursWorked, @PayRate, @Month, @Year);
END;