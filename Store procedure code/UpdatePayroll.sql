USE [EmployeeManagement]
GO
/****** Object:  StoredProcedure [dbo].[UpdatePayroll]    Script Date: 31-05-2024 21:43:06 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

ALTER PROCEDURE [dbo].[UpdatePayroll] (
    @EID INT,
    @HoursWorked INT,
    @PayRate float,
    @Month VARCHAR(20),
    @Year INT
  
)
AS
BEGIN
    SET NOCOUNT ON;

    -- Update the existing record if it exists, otherwise insert a new one
    IF EXISTS (SELECT * FROM PAYROLL WHERE EID = @EID AND [Month] = @Month AND [Year] = @Year AND [Payed]=0) 
    BEGIN
        UPDATE PAYROLL 
        SET HoursWorked = @HoursWorked,
            PayRate = @PayRate
        WHERE EID = @EID AND [Month] = @Month AND [Year] = @Year  AND [Payed]=0;
    END
    ELSE
    BEGIN
        INSERT INTO PAYROLL (EID, HoursWorked, PayRate, [Month], [Year])
        VALUES (@EID, @HoursWorked, @PayRate, @Month, @Year);
    END;
END;