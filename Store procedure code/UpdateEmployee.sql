USE [EmployeeManagement]
GO
/****** Object:  StoredProcedure [dbo].[UpdateEmployee]    Script Date: 31-05-2024 21:42:35 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
ALTER PROCEDURE [dbo].[UpdateEmployee] @EID INT, @Name VARCHAR(100), @Position VARCHAR(100), @Salary DECIMAL(10, 2)
AS
BEGIN
    SET NOCOUNT ON;

    BEGIN TRY
        UPDATE EMPLOYEE 
        SET Name = @Name, Position = @Position, Salary = @Salary 
        WHERE ID = @EID;

        SELECT 'Employee updated successfully.' AS Result;
    END TRY
    BEGIN CATCH
        SELECT 'Error: ' + ERROR_MESSAGE() AS Result;
    END CATCH
END;