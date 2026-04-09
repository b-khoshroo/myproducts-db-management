create table worker(
    name varchar(50),
    salary real,
    address varchar(100),
    products_per_day int,
    primary key (name)
)
-- creating clustered index on salary, table worker:
CREATE CLUSTERED INDEX ix_sal2 ON worker(salary)

create table quality_controller(
    name varchar(50),
    salary real,
    address varchar(100),
    product_type varchar(8),
    primary key (name),
    check (product_type in ('product1', 'product2', 'product3') )
)

create table technical_staff(
    name varchar(50),
    salary real,
    address varchar(100),
    education varchar(3),
    position varchar(40),
    primary key (name),
    check (education in ('BS', 'MS', 'PhD'))
)
-- creating clustered index on salary, table technical_staff:
CREATE CLUSTERED INDEX ix_sal ON technical_staff(salary)


create table product(
    id varchar(20),
    date_produced date,
    time_spent real,
    worker varchar(50),
    tester varchar(50),
    repair_person varchar(50),
    primary key (id),
    foreign key (worker) references worker(name),
    foreign key (tester) references quality_controller(name),
    foreign key (repair_person) references technical_staff(name)
);
-- * the tabel product was altered (adding the column 'size'):
alter table product 
add size varchar(6);

ALTER TABLE product
add constraint size_constraint check (size in ('small', 'medium', 'large'));
-- *
--indexing on Product:
CREATE INDEX idx_prd ON product(worker)

create table product1(
    id varchar(20),
    size varchar(6),
    software_name varchar(50),
    primary key (id),
    foreign key (id) references product(id),
    check (size in ('small', 'medium', 'large'))
);
-- * table product1 was altered (removing the column 'size'):
alter table product1
drop constraint CK__product1__size__5006DFF2;

alter table product1
drop column size;
-- *

create table product2(
    id varchar(20),
    size varchar(6),
    color varchar(30),
    primary key (id),
    foreign key (id) references product(id),
    check (size in ('small', 'medium', 'large'))
)
-- * table product2 was altered (removing the column 'size'):
alter table product2
drop constraint CK__product2__size__53D770D6;

alter table product2
drop column size
-- *

create table product3(
    id varchar(20),
    size varchar(6),
    weight real,
    primary key (id),
    foreign key (id) references product(id),
    check (size in ('small', 'medium', 'large'))
);
-- * table product3 was altered (removing the column 'size'):
alter table product3
drop constraint check_size;

alter table product3
drop column size;
-- * -------------------

create table product1_account(
    account_number varchar(20),
    date date,
    product_cost real,
    product_id varchar(20),
    primary key (account_number),
    foreign key (product_id) references product(id)
)
--indexing:
CREATE INDEX idx_prd1_acc ON product1_account(product_id)


create table product2_account(
    account_number varchar(20),
    date date,
    product_cost real,
    product_id varchar(20),
    primary key (account_number),
    foreign key (product_id) references product(id)
)
--indexing:
CREATE INDEX idx_prd2_acc ON product2_account(product_id)


create table product3_account(
    account_number varchar(20),
    date date,
    product_cost real,
    product_id varchar(20),
    primary key (account_number),
    foreign key (product_id) references product(id)
)
--indexing:
CREATE INDEX idx_prd3_acc ON product3_account(product_id)


create table customer(
    name varchar(50),
    address varchar(100),
    primary key (name)
)

create table purchase(
    customer_name varchar(50),
    product_id varchar(20),
    primary key (customer_name, product_id),
    foreign key (customer_name) references customer(name),
    foreign key (product_id) references product(id)
)

create table accident(
    number varchar(20),
    date date,
    days_lost int,
    product_id varchar(20),
    worker varchar(50),
    repair_person varchar(50),
    primary key (number),
    foreign key (product_id) references product(id),
    foreign key (worker) references worker(name),
    foreign key (repair_person) references technical_staff(name),
    -- the following contraint checks if the accident is related either to a production or a repair process:
    constraint CHK_test_or_production check((worker is null and repair_person is not null) or
                                             (worker is not null and repair_person is null)) 
)
-- creating clustered index on date, table accident:
CREATE CLUSTERED INDEX ix_date ON accident(date)



create table repair(
    product_id varchar(20),
    repair_person varchar(50),
    date date,
    primary key (product_id),
    foreign key (product_id) references product(id),
    foreign key (repair_person) references technical_staff(name)
)

create table repair_request(
    product_id varchar(20),
    tester varchar(50),
    primary key (product_id),
    foreign key (product_id) references product(id),
    foreign key (tester) references quality_controller(name) 
)
--indexing:
CREATE INDEX idx_rp_req ON repair_request(tester)


-- Considering that in the requirement description we have two expectations for treatment, two values have been set for the related attribute 
create table complaint(
    id varchar(20),
    customer_name varchar(50),
    product_id varchar(20),
    date date,
    description varchar(200),
    treatment varchar(8),
    primary key (id),
    foreign key (customer_name) references customer(name),
    foreign key (product_id) references product(id),
    check (treatment in ('refund', 'exchange'))
)
--indexing:
CREATE INDEX idx_cmpt ON complaint(product_id)

create table certify(
    product_id varchar(20),
    tester varchar(50),
    primary key (product_id),
    foreign key (product_id) references product(id),
    foreign key (tester) references quality_controller(name)
)
--indexing:
CREATE INDEX idx_certify ON certify(tester); 