drop database if exists studentdb;

create database if not exists studentdb;

use studentdb;

SELECT 1 FROM Information_schema.tables 
WHERE table_schema = 'studentdb'
AND table_name = 'student';

drop table if exists student;
create table if not exists student(
    no char(6) not null primary key,
    name char(5) not null,
    gender char(2) not null,
    kor tinyint not null,
    eng tinyint not null,
    math tinyint not null,
    total int null default 0,
    avg decimal(5,2) null default 0.0,
    grade char(2) null default 'F',
    rate int null default 0
);

-- 1) insert
insert into student values('010101','이한용','남자',100,100,100,0,0,'A',null); 
insert into student values('020202','김한용','남자',10,10,10,30,10,'F',null); 
insert into student values('030303','김하늘','여자',50,50,50,150,50,'F',null); 

select * from student;

-- 2) delete
delete from student where name like '%용%';
delete from student;
-- 3) update
update student set kor = 60, eng = 60, math = 60, total = 180, avr = 60.00 , grade = 'C' where no = '010101';

-- 3) select
select * from student where name like '%용%';

-- 4) order by
select * from student order by no asc;
select * from student order by no desc;
select * from student order by total asc;
select * from student order by total desc;

-- 5) MAX
select MAX(total) from student;
select * from student order by total desc limit 5;

-- 6) total == 300
select * from student where total = (select MAX(total) from student);

-- 7) create index
create index idx_student_name on student(name);

-- 8) select index
select * from student where name = '이한용';

-- 9) group by
select name from student group by name;

drop table if exists deletestudent;
create table if not exists deletestudent(
    no char(6) not null,
    name char(5) not null,
    gender char(2) not null,
    kor tinyint not null,
    eng tinyint not null,
    math tinyint not null,
    total int not null,
    avg decimal(5,2) not null,
    grade char(2) not null,
    rate int null default 0,
    date datetime
);

select * from deletestudent;

drop table if exists updatestudent;
create table if not exists updatestudent(
    no char(6) not null,
    name char(5) not null,
    gender char(2) not null,
    kor tinyint not null,
    eng tinyint not null,
    math tinyint not null,
    total int not null,
    avg decimal(5,2) not null,
    grade char(2) not null,
    rate int null default 0,
    date datetime
);

select * from updatestudent;

-- trigger
drop trigger if exists trg_deletestudent;
DELIMITER $$
create trigger trg_deletestudent
	after delete
	on student
	for each row
begin
	insert into deletestudent values(OLD.no , OLD.name , OLD.gender, OLD.kor, OLD.eng, OLD.math, OLD.total, OLD.avg, OLD.grade, OLD.rate, now());
end $$
DELIMITER ;

drop trigger if exists trg_updatestudent;
DELIMITER $$
create trigger trg_updatestudent
	after update
    on student
    for each row
begin
	insert into updatestudent values(OLD.no , OLD.name , OLD.gender, OLD.kor, OLD.eng, OLD.math, OLD.total, OLD.avg, OLD.grade, OLD.rate, now());
end $$
DELIMITER ;

-- procedure

drop procedure if exists procedure_insert_student;
delimiter $$
create procedure procedure_insert_student (
	In in_no char(6),
    In in_name char(5),
    In in_gender char(2),
	In in_kor int,
	In in_eng int,
	In in_math int
)
begin
-- declare
	declare in_total int;
    declare in_avg double;
    declare in_grade varchar(2);
-- set
	set in_total = in_kor + in_eng + in_math;
    set in_avg = in_total / 3.0;
    set in_grade =
		case
			when in_avg	>= 90.0 then 'A'
			when in_avg	>= 80.0 then 'B'
			when in_avg	>= 70.0 then 'C'
			when in_avg	>= 60.0 then 'D'
			else 'F'
		end;
-- insert into student() values();
	insert into student(no, name, gender, kor, eng, math)
    values(in_no, in_name, in_gender, in_kor, in_eng, in_math);
-- update student set total , avg , grade where id = in_no;
    update student set total = in_total , avg = in_avg , grade = in_grade where no = in_no;
end $$
delimiter ;

call procedure_insert_student('010101', '이한용',  '남자', 90, 90, 90);
select * from student where id = '010101';


drop procedure if exists procedure_update_student;
delimiter $$
create procedure procedure_update_student (
	In in_no char(6),
	In in_kor int,
	In in_eng int,
	In in_math int
)
begin
-- declare
	declare in_total int default 0;
    declare in_avg double default 0.0;
    declare in_grade varchar(2) default 'F';
-- set
	set in_total = in_kor + in_eng + in_math;
    set in_avg = in_total / 3.0;
    set in_grade =
		case
			when in_avg	>= 90.0 then 'A'
			when in_avg	>= 80.0 then 'B'
			when in_avg	>= 70.0 then 'C'
			when in_avg	>= 60.0 then 'D'
			else 'F'
		end;

-- update
	update student set kor = in_kor, eng = in_eng, math = in_math, total = in_total , avg = in_avg , grade = in_grade where no = in_no;
end $$
delimiter ;

call procedure_update_student('010101', 80, 80, 80);
select * from student where id = '010101';
select * from updatestudent;
