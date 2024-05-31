USE [EmployeeManagement]
GO
/****** Object:  StoredProcedure [dbo].[AddAttendance]    Script Date: 31-05-2024 21:34:47 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
ALTER PROCEDURE [dbo].[AddAttendance] @EID INT, @NoOfHours INT
AS
BEGIN
    SET NOCOUNT ON;

    BEGIN TRY
        DECLARE @CurrentDate DATE = GETDATE();

        INSERT INTO ATTENDANCE (EID, DATE, HOURSWORKED) VALUES (@EID, @CurrentDate, @NoOfHours);

        SELECT 'Attendance added successfully.' AS Result;
    END TRY
    BEGIN CATCH
        SELECT 'Error: ' + ERROR_MESSAGE() AS Result;
    END CATCH
END;