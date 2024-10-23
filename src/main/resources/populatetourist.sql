use turistguidedb;

insert into City (Name)
values ('København'), ('Aarhus'), ('Odense');

insert into Tags (Name)
values ('Gratis'), ('Børnevenlig'), ('Kunst'), ('Museum'), ('Natur');

insert into attraction(Name, CityID, Description)
values ('Rundetårn', 1, 'Det er et rundt tårn');
insert into attraction(Name, CityID, Description)
values ('Den Lille havfrue', 1, 'H.C. Andersen værk');
insert into attraction(Name, CityID, Description)
values ('Rosenborg', 2, 'Et flot slot');


insert into attraction_tag(AttractionID, TagID)
values 	(1,1), (1,2), -- Rundetårn
		(2, 1), (2,2), (2,3), (2,5), -- Den lille havfrue
		(3,2), (3, 3); -- Rosenborg