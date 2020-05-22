drop schema if exists testNauka cascade;
create schema testNauka;
set search_path To testNauka;

create TYPE days_type as ENUM (
    'Рабочий',
    'Выходной',
    'Предпраздничный',
    'Праздничный'
    );

create TYPE markAtWork as ENUM (
    'Я',
    'Н',
    'В',
    'Рв',
    'Б',
    'К',
    'ОТ',
    'До',
    'Хд',
    'У',
    'Ож'
    );

create TYPE positions as ENUM (
    'ОбычныйРабочий',
    'Табельщик',
    'АдминистраторОтделов',
    'АдминистраторСотрудников'
    );

create table Department (
    ID serial primary key,
    Name varchar(256) not null
    );

create table Employee (
    ID serial primary key,
    Name varchar(256) not null ,
    Birth date not null ,
    Position positions not null
);

create table ProductionCalendar (
    ID serial primary key,
    Date date not null,
    DaysType days_type not null
);

create table EmployeeDepart (
    ID serial primary key,
    id_employee integer references Employee(ID) not null,
    id_depart integer references  Department(ID) not null
);

create table EmployeeDepartDay (
    ID serial primary key,
    id_employee integer references Employee(ID) not null,
    id_depart integer references  Department(ID) not null,
    id_day integer references  ProductionCalendar(ID) not null,
    Mark markAtWork not null
);

create table UsersApp(
    ID serial primary key,
    Login varchar(256) unique not null ,
    Position positions not null
);


SELECT * FROM employeedepart
    join Department D on EmployeeDepart.id_depart = D.ID
    join Employee E on EmployeeDepart.id_employee = E.ID;
