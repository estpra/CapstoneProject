
-- ---------------------------------------------------------------------------------------------------------------------------
-- ***********CREATING TABLES****************
-- ---------------------------------------------------------------------------------------------------------------------------
-- This is the way that mysql workbench generates creating a table
CREATE TABLE `capstone`.`categories` 
(
  `categoryID` INT NOT NULL AUTO_INCREMENT,
  `categoryName` VARCHAR(45) NULL,
  PRIMARY KEY (`categoryID`),
  UNIQUE INDEX `categoryName_UNIQUE` (`categoryName` ASC) VISIBLE
  );
  
  -- This seems to be the more general way to create a table in sql
  CREATE TABLE capstone.expenses
  (
  expenseID INT NOT NULL AUTO_INCREMENT primary key,
  expenseName VARCHAR(45) NOT NULL,
  categoryID int not null,
  amount decimal(10,2) not null,
  dateOfEntry char(45) not null,
  billDueDate char(45) not null,
  expenseDescription char(255) null,
  recurringBill tinyint not null,
  imagePath char(255) null,
  foreign key (categoryID) references categories (categoryID)
  );
  
  
  create table capstone.expensesPeople
  (
  expenseID int not null,
  personID int not null,
  numberOfPeople int not null,
  totalMoneyOwedInTotal decimal(10,2) not null,
  totalMoneyOwedByEach decimal(10,2) not null,
  dateAgreedToPay char(45) null,
  paidOrNot tinyint default (0),
  constraint pk_ordersItems primary key (expenseID, personID)
  );
    -- this is saying that when you are looking up data in the expensesPeople table, you look up the data in that table by using a combo of expenseID, personID
  -- added this here cuz when I originally created this file in the pc, I forgot to make the pk a combo of the pks of the 2 tables(expenses and  I was joining 
  alter table capstone.expensesPeople
  add constraint pk_ordersItems primary key (expenseID, personID);
   -- Kept this query here as a lesson cuz originally I thought that I had to add a column referencing the combo table(expensesPeople) in the expenses table but in reality all I need to do is get the data from the combo table itself
  -- and not the individual tables themselves as that is what joining the tables is for, like in this commented out queries case, I thought I had to add a column in expenses to reference the combo table to be able to 
  -- link it to the people table but thats what the combo table itself is for and why its pk is a combo of the 2 tables pk's, no need to add an extra column in either table referncing the combo table, 
  -- thats the combo tables job
  /* alter table capstone.expenses
  add expensesPeople char(45); */
  
  
  -- Leaving this first attempt to create the people table with the way I thought totalNumberOfPeople woudl be created, however, using chatGPT
  -- I found the reasons why I could not create the table this original way that I thought would work, see chatGPT's responsone and do research
  -- and take notes on chatGPT's response to understand the best you can
  -- create table capstone.people
--   (
--   personID int not null primary key auto_increment,
--   personFullName char(45) not null unique,
--   -- left this here to show that you can't create a column using a select query, at least, thats what I think for now(as of 1/15/23
--   -- totalBillsSplit as count(select expensesPeople.personID where personID = expensesPeople.personID)
--   -- so dead ass for the column totalNumberOfPeople, if I did not enclose "count(personFullName)" in parenthesis, it would mark it as wrong
--   -- note that for future refernce
--   totalNumberOfPeople int as (count(personFullName))
--   );

create table capstone.people
  (
  personID int not null primary key auto_increment,
  personFullName char(45) not null unique  
  );
  
  
  -- I had created this query as originally I had created the people table without the totalNumberOfPeople column
--   alter table capstone.people add totalNumberOfPeople int;
  
-- This part can work to automatically update the total number of people in the people table but I found out that to do this, I have to use a 
-- trigger that runs after an insert statement and updates the totalNumberOfPeople column rather than running the query manually as I had tested 
-- to verify if the query did in fact work to update the totalNumberOfPeople column accuratley, which it did 
-- SET @count = (SELECT COUNT(*) FROM capstone.people);
-- UPDATE capstone.people SET totalNumberOfPeople = @count;

--  ------------------------------------------------------------------------------------------------------------------------
-- ***********Creating Triggers****************
--  ------------------------------------------------------------------------------------------------------------------------
-- Left this attempt of how I thought creating the trigger for automatically updating totalNumberOfPeople would work, see chatGPT's response
-- to see explanation on how to cerate the trigger how I wanted to originally
-- create trigger sumPeople after insert on capstone.people for each row 
-- SET @count = (SELECT COUNT(*) FROM capstone.people);
-- UPDATE capstone.people SET people.totalNumberOfPeople = @count;

-- delimiter $$
-- create trigger calculateTotalPeople
-- after insert on capstone.people
-- for each row
-- begin 
-- 	SET @count = (SELECT COUNT(*) FROM capstone.people);
-- 	update capstone.people set totalNumberOfPeople = @count;
-- end $$

-- DELIMITER $$
-- CREATE TRIGGER update_totalNumberOfPeople
-- AFTER INSERT ON capstone.people
-- FOR EACH ROW
-- BEGIN
--     SET @count = (SELECT COUNT(*) FROM capstone.people);
--     CREATE TEMPORARY TABLE temp_people AS (SELECT @count AS totalNumberOfPeople);
--     UPDATE capstone.people JOIN temp_people
--     ON capstone.people.personID = new.personID
--     SET capstone.people.totalNumberOfPeople = temp_people.totalNumberOfPeople;
--     DROP TEMPORARY TABLE temp_people;
-- END $$
-- DELIMITER ;

--  ------------------------------------------------------------------------------------------------------------------------
-- ***********Creating Functions****************
--  ------------------------------------------------------------------------------------------------------------------------
-- going to attempt to create the trigger from above as a stored procedure and see if that works, which actually seems to be the best way to do 
-- operations on table(s) as stored procedures are like functions that can be reused over and over again just by calling the stored procedure.
-- See differnces between triggers vs stored procedures vs functions to see which best to use for your needs; ended up creating a function for the trigger above
-- A lesson we learned for functions is that it seems like when you are returning a value, you dont need to give it a variable name, you just 
-- need to retun the value itself as seen below as this way worked 
create function calcTotalPeople ()
returns int deterministic
return (SELECT COUNT(*) FROM capstone.people);
-- This was original way I was trying to create the function and it was giving me an error
-- create function calcTotalPeople ()
-- returns int deterministic
-- return totalPeople = (SELECT COUNT(*) FROM capstone.people);

-- this worked
create function totalNumOfPeopleThatOwe ()
returns int deterministic
return (select count(*) from capstone.expensesPeople where expensesPeople.paidOrNot = 0);

-- the following 2 commented out functions are beta versions of the totalMonthlyAmount function before getting to a working version 
-- delimiter //
-- create function totalAmountForCategory(monthName varchar(25), categoryName varchar(100))
-- returns int
-- begin 
-- declare total int;
-- set total = count(expenses.amount + expensesPeople.totalMoneyOwedByEach where substring(expenses.dateOfEntry, 5) = str_to_date(monthName) 
-- and categories.categoryName = categoryName);
-- return total;
-- end; 



-- this function finally works!! works exactly how I want it to work, take notes on discoveried made about mysql up to this point!
-- note: i updated this funciton 3/26 which patched bug that returned null when function would return null if the inputted month 
-- was not in the table
delimiter //
create function totalMonthlyAmountForCategory(monthAndYear varchar(25), categoryName varchar(100))
returns decimal(10,2)
deterministic
begin 
declare total decimal;
-- this line calcualtes the total amount that you have wasted for the given category in the expeneses table only
set total = ifnull((select sum(e.amount) from capstone.expenses e where substr(e.dateOfEntry, 1, 2) = substr(monthAndYear, 1, 2) and 
substr(e.dateOfEntry, 7) = substr(monthAndYear, 4) and e.categoryID = (select c.categoryID from capstone.categories c where c.categoryName = categoryName)), 0);
-- this line adds the previous total calcuatled from the previous line with the total from the money you're owed from the totalMoneyOwedByEach column
-- from the expensesPeople table
set total = total + ifnull((select sum(ep.totalMoneyOwedByEach) from capstone.expensesPeople ep, capstone.expenses e where e.expenseID = ep.expenseID 
and substr(ep.dateAgreedToPay, 1, 2) = substr(monthAndYear, 1, 2) and
substr(ep.dateAgreedToPay, 7) = substr(monthAndYear, 4) and e.categoryID = (select c.categoryID from capstone.categories c where c.categoryName = categoryName)), 0);
return total;
end;



-- this is a test function to see what happens if I dont relate the combo table with expenses table when comparing the inputted category
-- with the tables
delimiter //
create function totalMonthlyAmountForCategory2(monthAndYear varchar(25), categoryName varchar(100))
returns decimal(10,2)
deterministic
begin 
declare total decimal;
-- this line calcualtes the total amount that you have wasted for the given category in the expeneses table only
set total = ifnull((select sum(e.amount) from capstone.expenses e where substr(e.dateOfEntry, 1, 2) = substr(monthAndYear, 1, 2) and 
substr(e.dateOfEntry, 7) = substr(monthAndYear, 4) and e.categoryID = (select c.categoryID from capstone.categories c where c.categoryName = categoryName)), 0);
-- this line adds the previous total calcuatled from the previous line with the total from the money you wanted from the totalMoneyOwedByEach column
-- from the expensesPeople table
set total = total + ifnull((select sum(ep.totalMoneyOwedByEach) from capstone.expensesPeople ep, capstone.expenses e where substr(ep.dateAgreedToPay, 1, 2) = substr(monthAndYear, 1, 2) and
substr(ep.dateAgreedToPay, 7) = substr(monthAndYear, 4) and e.categoryID = (select c.categoryID from capstone.categories c where c.categoryName = categoryName)), 0);
return total;
end;







-- this function calculates the increase between the inputted month and the previous month, if you spent less money on inputted month than previous
-- month than the entered month, than the result will be negative and the negtive amount is the amount you saved in the inputted month
delimiter //
create function totalIncreaseForCategoryMonthly(monthAndYear varchar(25), categoryName varchar(100))
returns decimal(10,2) 
deterministic
begin
declare total decimal(10,2);
declare previousMonthTotal decimal(10,2);
declare currentMonthTotal decimal(10,2);
declare previousMonthNum decimal(10,2);
declare previousMonthNumStr varchar(25);
set previousMonthNum = select cast(substr(monthAndYear, 1, 2) as decimal) - 1;
-- made the if statement check if the previousMonthNum is less than 10 cuz if it is, then we know its 9 or less and we need to add a 0 infront
-- of those numbers so it can work properly with the way I programmed the totalMonthlyAmountForCategory function
set previousMonthNumStr = select if(previousMonthNum < 10, concat("0", substr(previousMonthNum, 1, 1)), substr(previousMonthNum, 1, 2));
set previousMonthTotal = totalMonthlyAmountForCategory(concat(previousMonthNumStr, substr(monthAndYear, 4)), categoryName);
set currentMonthTotal = totalMonthlyAmountForCategory(monthAndYear, categoryName);
set total = currentMonthTotal - previousMonthTotal;
return total;
end;


delimiter //
create function totalIncreaseForCategoryMonthly(monthAndYear varchar(25), categoryName varchar(100))
returns decimal(10,2) 
deterministic
begin
declare total decimal(10,2);
declare previousMonthTotal decimal(10,2);
declare currentMonthTotal decimal(10,2);
declare previousMonthNum decimal(10,2);
declare previousMonthNumStr varchar(25);
SELECT cast(substr(monthAndYear, 1, 2) as decimal) - 1 INTO previousMonthNum;
-- made the if statement check if the previousMonthNum is less than 10 cuz if it is, then we know its 9 or less and we need to add a 0 infront
-- of those numbers so it can work properly with the way I programmed the totalMonthlyAmountForCategory function
SELECT if(previousMonthNum < 10, concat("0", substr(previousMonthNum, 1, 1)), substr(previousMonthNum, 1, 2)) INTO previousMonthNumStr;
set previousMonthTotal = totalMonthlyAmountForCategory(concat(previousMonthNumStr, substr(monthAndYear, 4)), categoryName);
set currentMonthTotal = totalMonthlyAmountForCategory(monthAndYear, categoryName);
set total = currentMonthTotal - previousMonthTotal;
return total;
end;



-- revised version of function created on 3/11/23
delimiter //
create function totalIncreaseForCategoryMonthly(monthAndYear varchar(25), categoryName varchar(100))
returns decimal(10,2) 
deterministic
begin
    declare total decimal(10,2);
    declare previousMonthTotal decimal(10,2);
    declare currentMonthTotal decimal(10,2);
    declare previousMonthNum decimal(10,2);
    declare previousMonthNumStr varchar(25);

    select cast(substr(monthAndYear, 1, 2) as decimal(10,2)) - 1.0 into previousMonthNum;
    
    set previousMonthNumStr = if(previousMonthNum < 10, concat("0", substr(previousMonthNum, 1, 1)), substr(previousMonthNum, 1, 2));
    
    select totalMonthlyAmountForCategory(concat(previousMonthNumStr, substr(monthAndYear, 4)), categoryName) into previousMonthTotal;
    
    select totalMonthlyAmountForCategory(monthAndYear, categoryName) into currentMonthTotal;
    
    set total = currentMonthTotal - previousMonthTotal;
    
    return total;
end;
//
delimiter ; 


delimiter //
create function totalIncreaseForCategoryMonthly(monthAndYear varchar(25), categoryName varchar(100))
returns decimal(10,2) 
deterministic
begin
    declare total decimal(10,2);
    
    set total =  totalMonthlyAmountForCategory(monthAndYear, categoryName) - totalMonthlyAmountForCategory(concat(if(cast(substr(monthAndYear, 1, 1) as decimal(10,2)) - 1 < 10, 
    concat("0", substr(monthAndYear, 1, 1)), cast((cast(substr(monthAndYear, 1, 2) as decimal(10,2)) - 1.0) as char)), substr(monthAndYear, 3)), categoryName);
    
    return total;
end;
//
delimiter ; 

-- New version 3/26/23
-- So far, this version is 50% working, the first call of the totalMonthlyAmountForCategory function executes properly but seemingly the
-- second totalMonthlyAmountForCategory function call doesnt seem to work, leaving off debugging the if statement first
-- solved the issue described above 3/27/23 by making month number a whole number rather than a decimal by specifying it as decimal(10,0) which means no decimal points, solving issue I was having before where since the month number was a decimal(10,2)
-- that meant it had 2 trailing zeros(on the right of the decimal point) so the if statement would return a decimal like 02.00, which is why the function call wasnt working but once I implemented the described decimal(10,0) trick, the if 
-- statement returned the intended behavior i.e. 02 
delimiter //
create function totalIncreaseForCategoryMonthly(monthAndYear varchar(25), categoryName varchar(100))
returns decimal(10,2) 
deterministic
begin
    declare total decimal(10,2);
    
    -- Discovered that when we use concat, even if you put non string data types in the parameters, the concat function will still concat them 
    -- and convert them to strings automatically. this is why I dont cast my results into strings(varchars) when doing arithmetic opertaions(+-*/)
    set total =  totalMonthlyAmountForCategory(monthAndYear, categoryName) - totalMonthlyAmountForCategory((concat((if(((cast(substr(monthAndYear, 1, 2) as decimal(10,2)) - 1) < 10), 
    (concat("0", ((cast(substr(monthAndYear, 1, 2) as decimal(10,0))) - 1))), ((cast(substr(monthAndYear, 1, 2) as decimal(10,0))) - 1.0))), substr(monthAndYear, 3))), categoryName);
    
    return total;
end;
//
delimiter ; 


-- squashed bug that was holding me back!!! Accidentally impelemented logic of min max algorithm incorrctly and when the if statment was false, 
-- instead of making it equal to itself(max vairable), i made it equal to the current iteration of category ID rather than itself(max variable)
delimiter //
create function cateogryMostMoneySpentOnMonth(monthAndYear varchar(25))
returns varchar(100) 
deterministic
begin
    declare mostExpensiveCategory varchar(100);
    declare loopCount int;
    set loopCount = 0;
    set mostExpensiveCategory = '';
    label: LOOP
		set loopCount = loopCount + 1;
		set mostExpensiveCategory = (if(totalMonthlyAmountForCategory(monthAndYear, (select c.categoryName from capstone.categories c where c.categoryID = loopCount)) < 
        totalMonthlyAmountForCategory(monthAndYear, (select c.categoryName from capstone.categories c where c.categoryID = loopCount + 1)), (select c.categoryName from capstone.categories c where c.categoryID = (loopCount + 1)),
         mostExpensiveCategory));
         -- leaving this if statement here to show that this type of implementation doesnt work 
        -- if totalMonthlyAmountForCategory(monthAndYear, (select c.categoryName from capstone.categories c where c.categoryID = loopCount)) < 
--         totalMonthlyAmountForCategory(monthAndYear, (select c.categoryName from capstone.categories c where c.categoryID = (loopCount + 1))) then 
			-- set mostExpensiveCategory = (select c.categoryName from capstone.categories c where c.categoryID = (loopCount + 1));
		if loopCount = (select count(*) from capstone.categories) then 
			leave label;
		end if;
	end LOOP label;
	return mostExpensiveCategory;
end;
//
delimiter ;


-- this is the function that makes the totalIncreaseForCategoryMonthly function a general purpose total increase between date1 and date2
-- for the inputted category 
-- this works! as of 5/12/23, I dont see any obvous bugs
delimiter //
create function totalIncreaseBetween(monthAndYear1 varchar(25), monthAndYear2 varchar(25), categoryName varchar(100))
returns decimal(10,2) 
deterministic
begin
declare totalDifference decimal(10,2);

set totalDifference = totalMonthlyAmountForCategory(monthAndYear1, categoryName) - totalMonthlyAmountForCategory(monthAndYear2, categoryName);

return totalDifference;

end;
//
delimiter ; 


#this function calculates which month you spent the most money overall between the two inputted dates
delimiter //
create function mostMoneySpentBetweenMonths(monthAndYear1 varchar(25), monthAndYear2 varchar(25))
returns decimal(10,2) 
deterministic
begin
declare total1 decimal(10,2);
declare total2 decimal(10,2);
declare mostExpensive decimal(10,2);
set total1 = ifnull((select sum(e.amount) from capstone.expenses e where substr(e.dateOfEntry, 1, 2) = substr(monthAndYear1, 1, 2) and 
substr(e.dateOfEntry, 7) = substr(monthAndYear1, 4)), 0);
set total1 = total1 + ifnull((select sum(ep.totalMoneyOwedByEach) from capstone.expensesPeople ep, capstone.expenses e where e.expenseID = ep.expenseID 
and substr(ep.dateAgreedToPay, 1, 2) = substr(monthAndYear1, 1, 2) and
substr(ep.dateAgreedToPay, 7) = substr(monthAndYear1, 4)), 0);
set total2 = ifnull((select sum(e.amount) from capstone.expenses e where substr(e.dateOfEntry, 1, 2) = substr(monthAndYear2, 1, 2) and 
substr(e.dateOfEntry, 7) = substr(monthAndYear2, 4)), 0);
set total2 = total2 + ifnull((select sum(ep.totalMoneyOwedByEach) from capstone.expensesPeople ep, capstone.expenses e where e.expenseID = ep.expenseID 
and substr(ep.dateAgreedToPay, 1, 2) = substr(monthAndYear2, 1, 2) and
substr(ep.dateAgreedToPay, 7) = substr(monthAndYear2, 4)), 0);
set mostExpensive = if(total1 > total2, total1, total2);
return mostExpensive;
end;
//
delimiter ;

-- delimiter //
-- create function totalIncreaseForCategoryMonthly(monthAndYear varchar(25), categoryName varchar(100))
-- return decimal(10,2) 
-- deterministic
-- begin
-- declare total decimal(10,2);
-- declare previousMonthTotal decimal(10,2);
-- declare currentMonthTotal decimal(10,2);
-- declare previousMonthNum int;
-- set previousMonthNum = select cast(substr(monthAndYear, 1, 2) as decimal);
-- set previousMonthTotal = select totalMonthlyAmountForCategory(concat(previousMonthNum, substr(monthAndYear, 4)), categoryName);
-- set currentMonthTotal = select totalMonthlyAmountForCategory(substr(monthAndYear, 1, 2), categoryName);
-- set total = currentMonthTotal - previousMonthTotal;
-- return total;
-- end;

-- delimiter //
-- create function totalIncreaseForCategoryMonthly(monthAndYear varchar(25), categoryName varchar(100))
-- returns decimal(10,2) 
-- deterministic
-- begin
-- declare total decimal(10,2);
-- set total = select totalMonthlyAmountForCategory(concat(select cast(substr(monthAndYear, 1, 2) as decimal), substr(monthAndYear, 4)), categoryName) - 
-- select totalMonthlyAmountForCategory(substr(monthAndYear, 1, 2), categoryName);
-- return total;
-- end;


--  ------------------------------------------------------------------------------------------------------------------------
-- ***********Creating Procedures****************
--  ------------------------------------------------------------------------------------------------------------------------
-- this worked beautifully, from using the subquery to convert the inputted string into its corresponding personID which was used to select the 
-- correct amount that the inputted person owed
delimiter //
create procedure howMuchDoesThisPersonOwe(in personName varchar(100))
begin
select ep.totalMoneyOwedByEach as totalMoneyOwed
from capstone.expensesPeople ep where ep.personID = (select p.personID from capstone.people p where p.personFullName = personName)
and ep.paidOrNot = 0;
end //

-- this works beatifully lfgi, this one was the first iteration for the listOfPeopleThatOwe() procedure, basically does the same thing just doesnt
-- list how much the people owe 
delimiter // 
create procedure getAllPeopleWhoOwe()
begin
select p.personFullName 
from capstone.people p 
inner join capstone.expensesPeople ep on p.personID = ep.personID 
where ep.paidOrNot = 0;
end //

-- this procedure lists all the people that owe money along with the amount that they owe, note that if I want to give an alias to the column(s) I'm
-- selecting, I must do that in the select part of the query 
delimiter //
create procedure listOfPeopleThatOwe()
begin
select p.personFullName as person, ep.totalMoneyOwedByEach as totalMoneyOwed
from capstone.people p 
inner join capstone.expensesPeople ep on p.personID = ep.personID
where ep.paidOrNot = 0;
end //

-- since I know that the precedure from above works, I'm going to manually test what the difference between the different joins are 
-- left and right join for this query does the same thing(seemingly for now, will double check once table is populated). Not sure why full outer
-- join query does not work, seems to be something with the syntax and version of mysql according to error message. The reason why full outer join
-- seemingly wasnt working is becuase mysql seems to call full outer join as cross join, need to verify this tho
select p.personFullName from capstone.people p left join capstone.expensesPeople ep on p.personID = ep.personID where ep.paidOrNot = 0;

select p.personFullName from capstone.people p right join capstone.expensesPeople ep on p.personID = ep.personID where ep.paidOrNot = 0;

select p.personFullName from capstone.people p cross join capstone.expensesPeople ep on p.personID = ep.personID where ep.paidOrNot = 0;

--  ------------------------------------------------------------------------------------------------------------------------
-- ***********Testing functions I create****************
--  ------------------------------------------------------------------------------------------------------------------------
select calcTotalPeople ();

select totalNumOfPeopleThatOwe () as numOfPeopleThatOweMoney;

select totalMonthlyAmountForCategory('01/23', 'Utility Bills') as totalAmount;
select totalMonthlyAmountForCategory('03/23', 'Utility Bills') as totalAmount;
select totalMonthlyAmountForCategory('02/23', 'Utility Bills') as totalAmount;

select totalIncreaseForCategoryMonthly('03/23', 'Utility Bills') as diff_between_current_month_and_last_month;
select totalIncreaseForCategoryMonthly('02/23', 'Utility Bills') as diff_between_current_month_and_last_month;

select cateogryMostMoneySpentOnMonth('03/23');
select cateogryMostMoneySpentOnMonth('01/23');

select totalIncreaseBetween('03/23', '01/23', 'Utility Bills') as totalIncrease;
select mostMoneySpentBetweenMonths('03/23', '01/23');

-- -----------------------------------------------------------------------------------------------------------------------------------
-- **********Testing parts of functions to make sure they are doing what they are supposed to correctly
-- -----------------------------------------------------------------------------------------------------------------------------------
-- Testing the ifnull function to see if I can implement it to solve the bug that I discovered in the totalMonthlyAmountForCategory function which was that when the 
-- expenesesPeople table wouldnt have an entry for the month that the user is searching for, than the result ould be null and as I discovered, if you add null to any 
-- number you get null, so successfully solved this bug by using ifnull as ifnull will return the second option, 69 in the ex below, if the first 
-- expression equals null but if it doesnt equal null, then it reutrns the first expression
-- also side discovery, but seems like there are a lot of functions that do what I need like this ifnull funciton that I stumped into, so try to 
-- find functions that do the specific thing you are trying to do to save you work and make sure it works
select ifnull((select sum(ep.totalMoneyOwedByEach) from capstone.expensesPeople ep where substr(ep.dateAgreedToPay, 1, 2) = substr('03/23', 1, 2) and
substr(ep.dateAgreedToPay, 7) = substr('03/23', 4)), 69);

-- debugging for 3/27/23, in these 2 lines of code I was testing the if statement to make sure that it was working properly and it seems like it was working properly
-- however, since the numbers are decimals, we get a string that looks like 02.00, to fix this bug, I simply just changed the format of the decimal to not have any trailing zeros
-- and that seems to have done the trick! Another bug fixed!
select if(((cast(substr('03/23', 1, 2) as decimal(10,2)) - 1) < 10), 
    (concat("0", ((cast(substr('03/23', 1, 2) as decimal(10,2))) - 1))), ((cast(substr('03/23', 1, 2) as decimal(10,2))) - 1.0));

select if(((cast(substr('03/23', 1, 2) as decimal(10,2)) - 1) < 10), 
    (concat("0", ((cast(substr('03/23', 1, 2) as decimal(10,0))) - 1))), ((cast(substr('03/23', 1, 2) as decimal(10,0))) - 1.0));
    
-- a small but important difference between this line of code and the line right above it is that if you notice the second option for the if statement, on the version on top, it is being suntracted by 1.0, which is a double(decimal), so 
-- even tho I implement the method of making my decimal a whole number by doing decimal(10,0), when we subtract by the 1.0, we get back a decimal(double). 
-- this version fixes that issue by subracting it by the integer 1 rather than the decimal 1.0  
select if(((cast(substr('10/23', 1, 2) as decimal(10,2)) - 1) < 10), 
    (concat("0", ((cast(substr('10/23', 1, 2) as decimal(10,0))) - 1))), ((cast(substr('10/23', 1, 2) as decimal(10,0))) - 1)) as previous_month_num;


select totalMonthlyAmountForCategory('03/23', 'Utility Bills') - totalMonthlyAmountForCategory(concat(if(cast(substr('03/23', 1, 2) as decimal(10,2)) - 1.0 < 10, 
    concat("0", substr(cast(substr('03/23', 1, 2) as decimal(10,2)) - 1.0, 1, 1)), substr(cast(substr('03/23', 1, 2) as decimal(10,2)) - 1.0, 1, 2)), substr('03/23', 4)), 'Utility Bills');
    
-- this code is the code for the first value of the "total" variable from the totalMonthlyAmountForCategory function and I put it here to see if 
-- it was working as inteded cuz when I used the totalMonthlyAmountForCategory with the 03/23 date entry it was returning null. thanks to this
-- I was able to find bug that when there is no entry that matches the entered date in expensesPeople, it will return null so when we add this total 
-- with the follwing line of code(which looks for entries in expensesPeople) it returns null
(select sum(e.amount) from capstone.expenses e where substr(e.dateOfEntry, 1, 2) = substr('03/23', 1, 2) and 
substr(e.dateOfEntry, 7) = substr('03/23', 4) and e.categoryID = (select c.categoryID from capstone.categories c where c.categoryName = 'Utility Bills'));

-- dont forget to add "select" in front of pieces of code that you are testing, if you forget to add select, you will get an error saying
-- You have an error in your SQL syntax
select (if(totalMonthlyAmountForCategory('03/23', (select c.categoryName from capstone.categories c where c.categoryID = 1)) < 
totalMonthlyAmountForCategory('03/23', (select c.categoryName from capstone.categories c where c.categoryID = (1 + 1))), 
(select c.categoryName from capstone.categories c where c.categoryID = (1 + 1)),
(select c.categoryName from capstone.categories c where c.categoryID = 1)));
		
-- this if statement and the one above it are both debugging for the cateogryMostMoneySpentOnMonth function to ensure that the logic
-- of the if statment was correct; i found that the logic was correct but accidentally was setting the max variable to the current
-- category ID iteration rather than itself when the if statement was false as seen below and above(see cateogryMostMoneySpentOnMonth for
-- corrected version)
select (if(totalMonthlyAmountForCategory('03/23', (select c.categoryName from capstone.categories c where c.categoryID = 2)) < 
totalMonthlyAmountForCategory('03/23', (select c.categoryName from capstone.categories c where c.categoryID = (2 + 1))), 
(select c.categoryName from capstone.categories c where c.categoryID = (2 + 1)),
(select c.categoryName from capstone.categories c where c.categoryID = 2)));

--  ------------------------------------------------------------------------------------------------------------------------
-- ***********Testing stored procedures I create****************
--  ------------------------------------------------------------------------------------------------------------------------
call getAllPeopleWhoOwe();
call howMuchDoesThisPersonOwe('Jr Prado');
call listOfPeopleThatOwe();

--  ------------------------------------------------------------------------------------------------------------------------
-- ***********Testing random quries for functionality purposes****************
--  ------------------------------------------------------------------------------------------------------------------------
-- put this query here to test if regular subtraction will work wirh 
select 05 - 03;
select 5 - 03;
select 3.0 - 1;
select if(10<1, "test1", "test2");
select 10.1 < 15;
select 0 = 0.0;
-- testing to see what happens when you add null to a number, this helped debugging the totalMonthlyAmountForCategory function 
select null + 10.5;

--  ------------------------------------------------------------------------------------------------------------------------
-- ***********FILLING TABLES W/DATA****************
--  ------------------------------------------------------------------------------------------------------------------------
 
INSERT INTO `capstone`.`categories` (`categoryName`) VALUES ('Groceries');
INSERT INTO `capstone`.`categories` (`categoryName`) VALUES ('Eating Out');
INSERT INTO `capstone`.`categories` (`categoryName`) VALUES ('Utility Bills');
INSERT INTO `capstone`.`categories` (`categoryName`) VALUES ('Subscriptions ');

insert into capstone.people (personFullName) values ("Alondra Prado");
insert into capstone.people (personFullName) values ("Jr Prado");
insert into capstone.people (personFullName) values ("George Cervantes");
insert into capstone.people (personFullName) values ("Jazmin Aguilar");
insert into capstone.people (personFullName) values ("Mom");
 