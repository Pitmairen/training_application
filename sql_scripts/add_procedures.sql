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
CREATE PROCEDURE GetUserByEmail(
	@email varchar(100)
)
AS
BEGIN
SELECT customer_first_name, customer_last_name FROM customer WHERE customer_email = @email
END;
-- Procedure test
EXEC GetUserByEmail  N'duke@gmail.com';


-- New procedure
CREATE PROCEDURE GetWorkouts
AS
BEGIN
SELECT * FROM workout
END;
-- Procedure test
EXEC GetWorkouts;

-- New procedure
CREATE PROCEDURE GetExercises
AS
BEGIN
SELECT * FROM exercise
END;

EXEC GetExercises

CREATE PROCEDURE GetExercises @program nvarchar(30)
AS
SELECT * FROM 
WHERE City = @City
GO