USE [EmployeeManagement]
GO
/****** Object:  StoredProcedure [dbo].[DisplayAttendance]    Script Date: 31-05-2024 21:37:07 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
ALTER PROCEDURE [dbo].[DisplayAttendance]
AS
BEGIN
   
SELECT 
    e.ID, 
    e.EMPID, 
    e.[NAME],
    e.Position, 
    CONVERT(VARCHAR(10), GETDATE(), 103) AS [Date],
    NULL AS [HoursWorked]
FROM 
    EMPLOYEE e
LEFT JOIN 
    ATTENDANCE a ON e.ID = a.EID
               AND CONVERT(VARCHAR(10), a.[Date], 103) = CONVERT(VARCHAR(10), GETDATE(), 103)
WHERE 
    a.EID IS NULL 
    OR (a.HoursWorked <= 0 OR a.HoursWorked IS NULL);
END;
