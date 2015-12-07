

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

IF OBJECTPROPERTY(OBJECT_ID('create_new_customer'), 'IsProcedure') = 1
BEGIN
    DROP PROCEDURE create_new_customer;
END

GO
CREATE PROCEDURE create_new_customer
    @first_name VARCHAR(50),
    @last_name VARCHAR(50),
    @password VARCHAR(255),
    @email VARCHAR(100),
    @body_weight INTEGER,
    @body_height INTEGER,
    @date_of_birth DATE,
    @sex CHAR(1),
    @program_name VARCHAR(100) = '',
    @program_desc TEXT = 'Program description'
AS
BEGIN
    DECLARE @program_id int;

    SET NOCOUNT ON;

    IF @program_name = '' BEGIN
        SELECT @program_name = 'Program for ' + @first_name;
    END

    INSERT INTO program (program_name, program_description)
        VALUES(@program_name, @program_desc);

    SELECT @program_id=SCOPE_IDENTITY();

    INSERT INTO customer (
        customer_program_id,
        customer_first_name, customer_last_name, customer_pw,
        customer_email, customer_weight, customer_height,
        customer_date_of_birth, customer_sex)
        VALUES (
        @program_id, @first_name, @last_name, @password,
        @email, @body_weight, @body_height, @date_of_birth,
        @sex);

    END
GO


-- Example call:

EXEC create_new_customer
	@first_name='Test', 
	@last_name='Test',
	@password='123',
	@email='test@gmail.com',
	@body_weight=332,
	@body_height=32,
	@date_of_birth='2010-01-01',
	@sex='m'; 


