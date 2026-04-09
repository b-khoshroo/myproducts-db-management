-- Query (1)
-- Inserting a worker:
GO
create procedure q1_enter_worker
    @name varchar(50),
    @salary real,
    @address varchar(100),
    @products_per_day INT
AS
BEGIN
        -- checking if the information already exists in the DB: 
        IF (@name in (select name
                from worker))
        BEGIN
            PRINT 'The information of ' +  @name + ' has already been entered.';
            return -1;
        END

        insert into worker(name, salary, address, products_per_day)
        values(@name, @salary, @address, @products_per_day)
END
GO

-- Inserting a quality controller:
GO
create procedure q1_enter_qcontroller
    @name varchar(50),
    @salary real,
    @address varchar(100),
    @product_type varchar(8)
AS
BEGIN
    -- checking if the information already exists in the DB: 
    IF (@name in (select name
                from quality_controller))
        BEGIN
            PRINT 'The information of ' +  @name + ' has already been entered.';
            return -1;
        END

    insert into quality_controller(name, salary, address, product_type)
    values(@name, @salary, @address, @product_type)
END
GO

-- Inserting a technical staff:
GO
create procedure q1_enter_tstaff
    @name varchar(50),
    @salary real,
    @address varchar(100),
    @education varchar(3),
    @position varchar(40)
AS
BEGIN
    IF (@name in (select name
                from technical_staff))
        BEGIN
            PRINT 'The information of ' +  @name + ' has already been entered.';
            return -1;
        END
    
    insert into technical_staff(name, salary, address, education, position)
    values(@name, @salary, @address, @education, @position)
END
GO

-----------------------------------------------------
-- Query (2)
GO
CREATE PROCEDURE q2_enter_product
    @product_type varchar(8),
    @id varchar(20),
    @date_produced date,
    @time_spent real,
    @worker varchar(50),
    @tester varchar(50),
    @repair_person varchar(50),
    @size varchar(6),
    @software_name varchar(50), --for product1 
    @color varchar(30), --for product2
    @weight real --for product3
AS 
BEGIN
--checking if a product with the same id already exists:
IF (@id in (select product.id
                from product)) 
    BEGIN 
        PRINT 'A product with the same ID exists.';
        return -1;
    END 

-- if the name of a repair person has been given, checking if the given product has been repaired at all:
IF(@repair_person IS NOT NULL)
    BEGIN
        IF(@id not in (select repair.product_id
                                from repair))
            BEGIN
                PRINT 'Error in inserting the given information: the intended product has not been repairded by anyone. Please check the information you want to insert.';
                RETURN -1;
            END
    END

--checking if the given tester (quality controller) checks the quality of the given product type:
DECLARE @tester_type varchar(10);
set @tester_type = (select quality_controller.product_type
                    from quality_controller
                    where quality_controller.name = @tester);
IF (@tester_type <> @product_type)
    BEGIN
        PRINT 'Error in inserting the given information: The entered quality controler does not check this product type. Please check your information.'
        RETURN -1;
    END

-- inserting the information into the table 'product':
INSERT INTO product(id, date_produced, time_spent, worker, tester, repair_person, size)
VALUES(@id, @date_produced,@time_spent,@worker,@tester,@repair_person, @size);

-- Based on the product's type, inserting the relevant information in to the table related to that product type:
IF(@product_type = 'product1')
    BEGIN
        INSERT INTO product1(id, software_name)
        VALUES (@id, @software_name);
    END

IF(@product_type = 'product2')
    BEGIN
        INSERT INTO product2(id, color)
        VALUES (@id, @color);
    END

IF(@product_type = 'product3')
    BEGIN
        INSERT INTO product3(id, weight)
        VALUES (@id, @weight);
    END
END 
GO 
-----------------------------------------------------------
-- Query (3)

GO 
CREATE PROCEDURE q3_insert_customer
    @cus_name varchar(50),
    @cus_add varchar(100),
    @product_id varchar(20)
AS 
BEGIN

     IF (@product_id not in (select id
                from product))
        BEGIN
            PRINT 'The given product is not among the existing products.';
            return -1;
        END
    
    ---- Inserting the information into the customer table if it is a new customer:
    IF (@cus_name not in (select name from customer))
    BEGIN
        INSERT INTO customer(name, address)
        VALUES(@cus_name, @cus_add);
    END

    --Insert the product purchased into the purchase table:
    INSERT INTO purchase(customer_name, product_id)
    VALUES(@cus_name, @product_id);

END 
GO
------------------------------------------------------------

-- Query (4)
GO
CREATE PROCEDURE q4_insert_account
    @acc_num varchar(20), -- account number
    @date date,
    @cost real, -- the cost of the product
    @p_id varchar(20) -- product id 
AS
BEGIN
IF (@p_id in (select product1.id
                from product1)) 
    BEGIN 
        INSERT INTO product1_account(account_number, date, product_cost, product_id)
        VALUES(@acc_num, @date, @cost, @p_id);
    END 
ELSE IF (@p_id in (select product2.id
                from product2)) 
    BEGIN 
        INSERT INTO product2_account(account_number, date, product_cost, product_id)
        VALUES(@acc_num, @date, @cost, @p_id);
    END
ELSE IF (@p_id in (select product3.id
                from product3)) 
    BEGIN 
        INSERT INTO product3_account(account_number, date, product_cost, product_id)
        VALUES(@acc_num, @date, @cost, @p_id);
    END 
ELSE
    PRINT 'Error in inserting the given information: The given product ID could not be found.'
END 
GO
--------------------------------------------------------------------

--Query (5)
CREATE PROCEDURE q5_insert_complaint
    @id varchar(20), -- complaint id
    @cus_name varchar(50), -- customer name
    @p_id varchar(20), -- product id
    @date date,
    @description varchar(200),
    @treatment varchar(8)
AS
BEGIN
    IF ((@p_id not in (select product1.id
                from product1)) AND
        (@p_id not in (select product2.id
                from product2)) AND
        (@p_id not in (select product3.id
                from product3)))
        BEGIN
            PRINT '!!! Error in inserting the information: The given product could not be found (has not been purchased)';
            return -1;
        END
    IF (@cus_name not in (select customer.name
                from customer))
        BEGIN
            PRINT '!!! Error in inserting the information: The given customer could not be found.';
            return -1;
        END     

    INSERT INTO complaint(id, customer_name, product_id, date, description, treatment)
    VALUES(@id, @cus_name, @p_id, @date, @description, @treatment);
END
GO
-------------------------------------------------------------------------

-- Query (6)
CREATE PROCEDURE q6_insert_accident
    @number varchar(20),
    @date date,
    @d_lost int, -- days lost due to the accident 
    @p_id varchar(20), -- product id 
    @worker varchar(50),
    @repair_person varchar(50)
AS
BEGIN
    -- If the accident has been related to both a worker or repair_person, or it has not been related to any worker or repair_person, an error is returned:
    IF ((@worker is null and @repair_person is null) or (@worker is not null and @repair_person is not null))
        BEGIN
            PRINT 'An accident must be either related to a production or a repair process. Please enter the name of one related worker or one repair person.';
            RETURN -1;
        END
    IF ((@p_id not in (select product1.id
                from product1)) AND
        (@p_id not in (select product2.id
                from product2)) AND
        (@p_id not in (select product3.id
                from product3)))
        BEGIN
            PRINT 'Error in inserting the information: The given product could not be found (has not been purchased)';
            return -1;
        END
    IF ((@worker is not null) AND (@worker not in (select worker.name from worker)))
        BEGIN
            PRINT 'Error in inserting the information: The given worker could not be found in the employee names.';
            return -1;
        END
    IF ((@repair_person is not null) AND (@repair_person not in (select technical_staff.name from technical_staff)))
        BEGIN
            PRINT 'Error in inserting the information: The given name for the repair person could not be found in the employee names.';
            return -1;
        END   

    -- When the above errore do not happen, inserting the values into the accident table:
    INSERT INTO accident(number, date, days_lost, product_id, worker, repair_person)
    VALUES(@number, @date, @d_lost, @p_id, @worker, @repair_person);
END
GO
---------------------------------------------------------------------------

 -- Query (7)
 CREATE PROCEDURE q7_date_time
    @product_id varchar(20)
AS
BEGIN
    SELECT date_produced, time_spent 
    from product
    where id = @product_id;
END
GO
-------------------------------------------------------------------------

-- Query (8)
CREATE PROCEDURE q8_products_worker
    @worker varchar(50)
AS
BEGIN
    SELECT id
    FROM product
    WHERE worker = @worker;
END
GO
---------------------------------------------------------------------------

-- Query (9)
CREATE PROCEDURE q9_errors
    @tester varchar(50)
AS
BEGIN
    IF (@tester not in (select name
                from quality_controller))
        BEGIN
            PRINT 'The given name is not among the employee names.';
            return -1;
        END
    SELECT COUNT(certify.product_id) as errors
    FROM certify,complaint
    WHERE certify.product_id = complaint.product_id and certify.tester = @tester;
END
GO
-----------------------------------------------------------------------------

-- Query (10)
CREATE PROCEDURE q10_total_cost
    @tester varchar(50)
AS
BEGIN
    --checking if the tester's name in amoong the employee names:
    IF (@tester not in (select name
                from quality_controller))
        BEGIN
            PRINT 'The given name is not among the employee names.';
            return -1;
        END
    --If the tester name exists in the DB then:
    SELECT SUM(product3_account.product_cost)
    FROM product3_account, repair_request, repair
    WHERE product3_account.product_id=repair_request.product_id and repair_request.product_id=repair.product_id AND
            repair_request.tester=@tester
END
GO
----------------------------------------------------------------------------------

-- Query (11)
CREATE PROCEDURE q11_color
    @color varchar(30)
AS
BEGIN
    -- retreiveing all the customers all the products they have purchased have the color passed to the procedure:
    (SELECT purchase.customer_name
    FROM purchase, product, product2
    WHERE purchase.product_id=product.id AND product.id=product2.id AND product2.color=@color
    EXCEPT 
    SELECT purchase.customer_name
    FROM purchase, product, product2
    WHERE purchase.product_id=product.id AND product.id=product2.id AND product2.color<>@color)
    ORDER BY purchase.customer_name ASC;
END
GO
------------------------------------------------------------------------------------

-- Query (12)
CREATE PROCEDURE q12_salary
    @salary REAL
AS
BEGIN
    SELECT worker.name 
    FROM worker
    WHERE worker.salary > @salary
    UNION 
    SELECT quality_controller.name 
    FROM quality_controller 
    WHERE quality_controller.salary > @salary
    UNION 
    SELECT technical_staff.name 
    FROM technical_staff 
    WHERE technical_staff.salary > @salary;
END 
GO 
-------------------------------------------------------------------------------------

-- Query (13)
CREATE PROCEDURE q13_days_lost
AS
BEGIN
    SELECT sum(A.days_lost)
    FROM accident as A, repair as R, complaint as C 
    WHERE (A.repair_person is not null) AND (R.product_id=A.product_id) AND (R.product_id=C.product_id)
END
GO
------------------------------------------------------------------------------------

-- Query (14)

CREATE PROCEDURE q14_avg_cost
    @year INT
AS
BEGIN
    select avg(T.product_cost)
    from
    (select product1_account.product_cost 
    from product1_account, product 
    where product1_account.product_id=product.id AND YEAR(product.date_produced)=@year
    UNION ALL
    select product2_account.product_cost 
    from product2_account, product 
    where product2_account.product_id=product.id AND YEAR(product.date_produced)=@year
    UNION ALL
    select product3_account.product_cost 
    from product3_account, product 
    where product3_account.product_id=product.id AND YEAR(product.date_produced)=@year) AS T
END
GO
----------------------------------------------------------------------------------

-- Query (15)
CREATE PROCEDURE q15_accident_date
    @beginning DATE,
    @end DATE
AS
BEGIN
    IF (not exists (select number
            from accident
            where date between @beginning and @end))
        BEGIN
            print 'There is no accident in this range to be deleted.';
            return -1;
        END
    DELETE FROM accident 
    WHERE date BETWEEN @beginning AND @end;
END
GO
-----------------------------------------------------------------------------------

-- A procedure for inserting the information of a repair request by a quality controller (tester).
    -- Requirement: It should be checked that the tester to be inserted checks the type associated with the given product.
CREATE PROCEDURE insert_repair_req
    @product_id varchar(20),
    @tester varchar(50)
AS
BEGIN
    DECLARE @tester_type varchar(10);
    DECLARE @product_type varchar(10);

    -- determining which type of product the given tester checks:
    set @tester_type = (select quality_controller.product_type
                        from quality_controller
                        where quality_controller.name = @tester);
    

    -- determining what type the given product is of (product1, product2, or product3):
    IF (@product_id in (select product1.id
                        from product1))
        BEGIN
            set @product_type = 'product1';
        END
    ELSE IF (@product_id in (select product2.id
                                from product2))
        BEGIN
            set @product_type = 'product2';
        END
    ELSE IF (@product_id in (select product3.id
                                from product3))
        BEGIN
            set @product_type = 'product3';
        END
    
    -- Now, checking whether the given tester checks this type of product or not:
    IF (@tester_type <> @product_type)
        BEGIN
            PRINT 'Error in inserting the given information: The entered quality controler does not check this product type. Please double check your information.'
            RETURN -1;
        END
    
    -- If the requirement gets satisfied:
    INSERT INTO repair_request(product_id, tester)
    VALUES(@product_id, @tester);
END
GO
-----------------------------------------------------------------------------------------


-- A procedure for inserting into the repair table that cheks if the information to be inserted satisfies the following requirement:
    -- "A product can be repaired by a technical staff either because it got a complaint or because the repair was requested by a quality controller."
CREATE PROCEDURE insert_repair
    @product_id varchar(20),
    @repair_person varchar(50),
    @date DATE
AS
BEGIN
    IF ((@product_id in (select R.product_id from repair_request as R)) OR
        (@product_id in (select C.product_id from complaint as C)))
        BEGIN
            INSERT INTO repair(product_id, repair_person, date)
            VALUES(@product_id, @repair_person, @date);
        END
    ELSE
        BEGIN
            PRINT 'The given product has neither been requested (by a quality controller) for repair nor has been associated with any customer complaint.';
        END
END
GO
----------------------------------------------------------------------------------------------


