SELECT * FROM Capstone.expenses;

select sum(e.amount) from capstone.expenses e where strcmp((e.dateOfEntry, 1, 2), substr('01/23', 1, 2)) = 0 and 
strcmp((e.dateOfEntry, 7), substr('01/23', 4)) = 0 and e.categoryID = (select c.categoryID from capstone.categories c where c.categoryName = 'Eating Out');


-- this works exactly how I want it to work, lets go!!!!(this is the total for category/ year entered function)
select sum(e.amount) from capstone.expenses e
where strcmp(substr(e.dateOfEntry, 1, 2), substr('01/23', 1, 2)) = 0
and strcmp(substr(e.dateOfEntry, 7), substr('01/23', 4)) = 0
and e.categoryID = (select c.categoryID from capstone.categories c where c.categoryName = 'Utility Bills');


-- this original query i had created also works
select sum(e.amount) from capstone.expenses e 
where substr(e.dateOfEntry, 1, 2) = substr('01/23', 1, 2) and 
substr(e.dateOfEntry, 7) = substr('01/23', 4) and 
e.categoryID = (select c.categoryID from capstone.categories c where c.categoryName = 'Utility Bills');


select sum(e.amount) from capstone.expenses e where substr(e.dateOfEntry, 1, 2) = substr('01/23', 1, 2) and 
substr(e.dateOfEntry, 7) = substr('01/23', 4) and 
e.categoryID = (select c.categoryID from capstone.categories c where c.categoryName = 'Eating Out');


-- put this here to test to make sure that the cast function would work how I want it to work for deriving months/years based on inputted 
-- month/year
select cast(substr(e.dateOfEntry, 4, 5) as decimal(10,2) ) from capstone.expenses e;

select cast(substr(e.dateOfEntry, 1, 2) as decimal) from capstone.expenses e;

select cast(substr(e.dateOfEntry, 1, 2) as char) from capstone.expenses e;

select cast(substr(e.dateOfEntry, 1, 1) as decimal) from capstone.expenses e;

-- this line of code was to test if I could derive the previous month number of a given date entry using the dates and casting them from strings
-- to decimals 
select cast(cast(substr(e.dateOfEntry, 1, 2) as decimal) - cast(substr(e.dateOfEntry, 1, 2) as decimal) as char) from capstone.expenses e;

select cast(substr(e.dateOfEntry, 1, 2) as decimal) - cast(substr(e.dateOfEntry, 1, 2) as decimal) from capstone.expenses e;

select concat(cast(substr(e.dateOfEntry, 1, 2) as decimal) - cast(substr(e.dateOfEntry, 1, 2) as decimal), 'test') from capstone.expenses e;

select concat(6.9, cast(9 as char));

select concat(4, '20')

