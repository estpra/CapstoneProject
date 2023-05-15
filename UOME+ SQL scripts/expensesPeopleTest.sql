SELECT * FROM Capstone.expensesPeople;

select sum(ep.totalMoneyOwedByEach) from capstone.expensesPeople ep where substr(ep.dateAgreedToPay, 1, 2) = substr('01/23', 1, 2) and
substr(ep.dateAgreedToPay, 7) = substr('01/23', 4);

-- what this line of code is basically doing is giving you the categoryID for a given entry in the expensesPeople table as an entry
-- in the expensesPeople table also represents an entry in the expense table, hence why the expensesPeople table has an expenseID column
select e.categoryID from capstone.expenses e, expensesPeople ep where e.expenseID = ep.expenseID;

-- this works beatifully, basically, I am using the two tables expenses and expensesPeople to get the categoryID for a given expense that a
-- person joined you in, instead of making another category column in the expensesPeople table(category column exists in expenses table already)
-- we use the power of relational databases to use the expenseID from the expensesPeople table and the expenseID from the expenses table to 
-- relate(connect) those two tables together and to get the specific categoryID for a specififc person, I added the part where you also compare 
-- the personID of the specific person you are looking for to only give you the categoryID of the specific expense and for the specific person
select e.categoryID from capstone.expenses e, expensesPeople ep where e.expenseID = ep.expenseID and 
ep.personID = (select p.personID from capstone.people p where p.personFullName = 'George Cervantes');

-- also, just proved that you could get data from a table that doesnt make up the combo table but by using the combo table, you can reference
-- one of(depending your need) the tables that makes up the combo table and use one of those tables to reference that 3rd table that isnt part of the
-- combo table, an example of this is illulstrated below
select c.categoryName from capstone.categories c where c.categoryID = (select e.categoryID from capstone.expenses e, expensesPeople ep where e.expenseID = ep.expenseID and 
ep.personID = (select p.personID from capstone.people p where p.personFullName = 'George Cervantes'));

