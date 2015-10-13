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
Exec GetCustomers;

-- New procedure
CREATE PROCEDURE GetUserByEmail(
	@email varchar(100)
)
AS
BEGIN
SELECT customer_first_name, customer_last_name FROM customer WHERE customer_email = @email
END;
-- Procedure test
Exec GetUserByEmail  N'duke@gmail.com';


-- New procedure
CREATE PROCEDURE GetWorkouts
AS
BEGIN
SELECT * FROM workout
END;
-- Procedure test
Exec GetWorkouts;

