-- Run this first
USE training_application

-- For the procedures below; add them induvidually.

-- New procedure
CREATE PROCEDURE GetCustomers
AS
BEGIN
SELECT * FROM customer
END;
-- Procedure test
EXEC GetCustomers;

-- New procedure
CREATE PROCEDURE GetWorkouts
AS
SELECT * FROM [training_application].[dbo].[workout]
GO
-- Procedure test
EXEC GetWorkouts;