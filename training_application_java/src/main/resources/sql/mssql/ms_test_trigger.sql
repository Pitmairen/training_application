 
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

IF OBJECTPROPERTY(OBJECT_ID('customer_program_trigger'), 'IsTrigger') = 1
BEGIN
    DROP TRIGGER customer_program_trigger;
END
GO

CREATE TRIGGER customer_program_trigger
   ON customer 
   INSTEAD OF INSERT
AS 
BEGIN
    DECLARE @program_id int;

    SET NOCOUNT ON;

    INSERT INTO program (program_name, program_description)
        SELECT 
            ('Program for ' + customer_first_name), 'Program description'
        FROM INSERTED;

    SELECT @program_id=SCOPE_IDENTITY();

    INSERT INTO customer (
        customer_program_id,
        customer_first_name, customer_last_name, customer_pw,
        customer_email, customer_weight, customer_height,
        customer_date_of_birth, customer_sex)
        SELECT @program_id, 
            customer_first_name, customer_last_name, customer_pw,
            customer_email, customer_weight, customer_height,
            customer_date_of_birth, customer_sex
        FROM INSERTED;

END
GO
