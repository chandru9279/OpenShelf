
Drop database openshelf;
create database openshelf;
use openshelf;
CREATE TABLE Books(
   isbn varchar(30) NOT NULL,
   copyid varchar(5) NOT NULL,
   title VARCHAR(200) NOT NULL,
   primary key(isbn,copyid)
);


CREATE TABLE Employees(
   employeeid varchar(10) NOT NULL primary key,
   name varchar(100) NOT NULL
);



CREATE TABLE Borrow(
   isbn varchar(30) NOT NULL,
   copyid varchar(5) NOT NULL,
   employeeid varchar(10) NOT NULL,
   status varchar(10) NOT NULL,
   foreign key(isbn,copyid) references Books(isbn,copyid),
   foreign key(employeeid) references Employees(employeeid),
    primary key(isbn,copyid,employeeid)
);


