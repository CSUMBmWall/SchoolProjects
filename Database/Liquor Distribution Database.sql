drop schema if exists groupProject;
create schema groupProject;

use groupProject;

drop table if exists Liquor;
drop table if exists Dist_Ctr;
drop table if exists Retail_Store;
drop table if exists Dist_Inv;
drop table if exists Retail_Inv;

/* create tables */

create table Liquor
(
UPC varchar(13) NOT NULL PRIMARY KEY
, descr text
, size decimal (4, 2)
, liter double
, pack int
, btl_price decimal (7, 2)
, case_price decimal (7, 2)
);

/*import liquor information*/
LOAD DATA LOCAL INFILE "C:/Temp/Liquor Price List.csv" 
 INTO TABLE Liquor 
 FIELDS TERMINATED BY "," 
 OPTIONALLY ENCLOSED BY """" 
			IGNORE 1 LINES;

create table Dist_Ctr
(
dist_id int PRIMARY KEY 
, region varchar(25)
, street varchar(25)
, city varchar(25)
, state varchar(2)
, zip int(5)
);

/*populate distribution center tables*/ 
insert into Dist_Ctr values (100,'North', '1347 Anywhere Drive', 'Dumont', 'IA', 50625);
insert into Dist_Ctr values (101,'South', '6328 Somewhere Court', 'Iowa Falls', 'IA', 50126);

create table Retail_store
(
store_id int PRIMARY KEY
, dist_id int
, s_name varchar(25)
, street varchar(25)
, city varchar(25)
, state varchar(2)
, zip varchar(5)
, foreign key (dist_id) references Dist_Ctr(dist_id)
);

/*retail stores served by North Dist_Ctr*/
insert into retail_store values (1000, 100, 'Hy-Vee Wine & Spirits', '2400 4th Street SW', 'Mason City', 'IA', 50401);
insert into retail_store values (2000, 100, 'Chubbys Liquors', 'Hwy 21', 'Floyd', 'IA', 50435);
insert into retail_store values (3000, 100, 'Last Call Wines', '20 S 4th St.', 'Clear Lake', 'IA', 50428); 

/*retail stores served by South Dist_Ctr*/
insert into retail_store values (4000, 101, 'Shamrock Spirits', '1204 Water Street', 'Alden', 'IA', 50006);
insert into retail_store values (5000, 101, 'Liquor Barn', '721 Central Ave W', 'Hampton', 'IA', 50441);
insert into retail_store values (6000, 101, 'Eagle City Winery', '28536 160th Street', 'Iowa Falls', 'IA', 50126);
 
create table Dist_Inv
(
dist_id int 
, UPC varchar(13)
, QTY varchar(5)
, opt_QTY varchar(5)
, foreign key (UPC) references Liquor (UPC)
, foreign key (dist_id) references Dist_Ctr(dist_id)
, primary key (dist_id, UPC)
);

insert into Dist_Inv (dist_id, UPC, QTY, opt_QTY) values(100, 859824000000, 500, 500), (100, 626990000000, 600, 600), (100, 80686042709, 500, 500), (100, 80686479109, 300, 300), (100, 80686042129, 200, 200)
, (101, 84848220103, 100, 100), (101, 88352129013, 300, 300), (101, 812459000000, 400, 400), (101, 736212000000, 900, 900);

create table Retail_Inv
(
store_id int
, UPC varchar(13)
, QTY varchar(5)
, opt_QTY varchar(5)
, foreign key (UPC) references Liquor (UPC)
, foreign key (store_id) references Retail_Store (store_id)
,primary key (store_id, UPC)
);

insert into Retail_Inv (store_id, UPC, QTY, opt_QTY) values (1000, 859824000000, 12, 23), (1000, 626990000000, 25, 27), (1000, 80686042709, 35, 36), (2000, 859824000000, 12, 20), (2000, 626990000000, 25, 30), (2000, 80686042709, 35, 40), (3000, 859824000000, 12, 20), (3000, 626990000000, 25, 30), (3000, 80686042709, 35, 50);
insert into Retail_Inv (store_id, UPC, QTY, opt_QTY) values (4000, 859824000000, 12, 20), (4000, 626990000000, 25, 30), (4000, 80686042709, 35, 40), (5000, 859824000000, 12, 20), (5000, 626990000000, 25, 30), (5000, 80686042709, 35, 40), (6000, 859824000000, 12, 20), (6000, 626990000000, 25, 30), (6000, 80686042709, 35, 50);

/*Inventory order for Hy_Vee_Wine store_id 1000*/
create view Hy_Vee_Wine as
select S.s_name, R.UPC, R.opt_QTY - R.QTY  
from Retail_Inv R, Retail_Store S
where R.store_id = S.store_id and S.store_id = 1000 and QTY < opt_QTY;

/*Inventory order for Chubby's store_id 2000*/
create view Chubbys as
select S.s_name, R.UPC, R.opt_QTY - R.QTY  
from Retail_Inv R, Retail_Store S
where R.store_id = S.store_id and S.store_id = 2000 and QTY < opt_QTY;

/*Inventory order filled by Northern Distribution Ctr includes IDs between 1000 and 3000*/
create view Inv_Order_North as
select S.s_name as Store, S.store_id as Store_ID, R.UPC, R.opt_QTY - R.QTY as Sold 
from Retail_Inv R, Retail_Store S
where R.store_id = S.store_id and S.store_id >= 1000 and S.store_id <= 3000 and QTY < opt_QTY;

/*Retail Store query for any type of Jack Daniels*/
select descr, UPC, size, btl_price 
from Liquor
where descr like 'Jack Daniels%';

/*Retail store query for any type of vodka that is less than $10 and smaller than 0.5 liter*/
select descr, UPC, liter, btl_price
from Liquor
where descr like 'Vodka%' and btl_price < 10 and liter < .5;

/*retail store query for any type of Crown Royal*/
select descr, UPC, liter, btl_price
from Liquor
where descr like 'Crown Royal%';





