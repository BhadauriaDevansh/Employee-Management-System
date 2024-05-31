USE [EmployeeManagement]
GO
/****** Object:  StoredProcedure [dbo].[DeleteEmployee]    Script Date: 31-05-2024 21:36:19 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
ALTER PROCEDURE [dbo].[DeleteEmployee] 
    @EID INT
AS
BEGIN
    SET NOCOUNT ON;

    BEGIN TRY
        BEGIN TRANSACTION;
        
        DELETE FROM PAYROLL WHERE EID = @EID;
        DELETE FROM EMPLOYEE WHERE ID = @EID;
        
        COMMIT TRANSACTION;

        SELECT 'Employee deleted successfully.' AS Result;
    END TRY
    BEGIN CATCH
        IF @@TRANCOUNT > 0
            ROLLBACK TRANSACTION;

        SELECT 'Error: ' + ERROR_MESSAGE() AS Result;
    END CATCH
END;
