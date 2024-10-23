create database if not exists turistguidedb;
USE turistguidedb;

drop table if exists City;
create table City(
	CityID int auto_increment primary key,
    Name varchar(100) not null
);

drop table if exists Tags;
create table Tags(
	TagID int auto_increment primary key,
    name varchar(100) not null
);

drop table if exists Attraction;
create table Attraction(
	AttractionID int auto_increment primary key,
    Name varchar(100) not null,
    CityID INT,
    Description text,
    constraint fk_city foreign key (CityID) references City(CityID) on delete set null
    );
    
drop table if exists Attraction_Tag;
create table Attraction_Tag(
AttractionID int,
TagID int,
primary key (AttractionID, TagID),
constraint fk_attraction foreign key (AttractionID) references Attraction(AttractionID) on delete cascade,
constraint fk_tag foreign key (TagID) references Tags(TagID) on delete cascade
);



 


