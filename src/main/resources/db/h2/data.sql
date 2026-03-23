INSERT INTO vets VALUES (default, 'James', 'Carter');
INSERT INTO vets VALUES (default, 'Helen', 'Leary');
INSERT INTO vets VALUES (default, 'Linda', 'Douglas');
INSERT INTO vets VALUES (default, 'Rafael', 'Ortega');
INSERT INTO vets VALUES (default, 'Henry', 'Stevens');
INSERT INTO vets VALUES (default, 'Sharon', 'Jenkins');

INSERT INTO specialties VALUES (default, 'radiology');
INSERT INTO specialties VALUES (default, 'surgery');
INSERT INTO specialties VALUES (default, 'dentistry');

INSERT INTO vet_specialties VALUES (2, 1);
INSERT INTO vet_specialties VALUES (3, 2);
INSERT INTO vet_specialties VALUES (3, 3);
INSERT INTO vet_specialties VALUES (4, 2);
INSERT INTO vet_specialties VALUES (5, 1);

INSERT INTO types VALUES (default, 'cat');
INSERT INTO types VALUES (default, 'dog');
INSERT INTO types VALUES (default, 'lizard');
INSERT INTO types VALUES (default, 'snake');
INSERT INTO types VALUES (default, 'bird');
INSERT INTO types VALUES (default, 'hamster');

INSERT INTO owners VALUES (default, 'George', 'Franklin', '110 W. Liberty St.', 'Madison', '6085551023');
INSERT INTO owners VALUES (default, 'Betty', 'Davis', '638 Cardinal Ave.', 'Sun Prairie', '6085551749');
INSERT INTO owners VALUES (default, 'Eduardo', 'Rodriquez', '2693 Commerce St.', 'McFarland', '6085558763');
INSERT INTO owners VALUES (default, 'Harold', 'Davis', '563 Friendly St.', 'Windsor', '6085553198');
INSERT INTO owners VALUES (default, 'Peter', 'McTavish', '2387 S. Fair Way', 'Madison', '6085552765');
INSERT INTO owners VALUES (default, 'Jean', 'Coleman', '105 N. Lake St.', 'Monona', '6085552654');
INSERT INTO owners VALUES (default, 'Jeff', 'Black', '1450 Oak Blvd.', 'Monona', '6085555387');
INSERT INTO owners VALUES (default, 'Maria', 'Escobito', '345 Maple St.', 'Madison', '6085557683');
INSERT INTO owners VALUES (default, 'David', 'Schroeder', '2749 Blackhawk Trail', 'Madison', '6085559435');
INSERT INTO owners VALUES (default, 'Carlos', 'Estaban', '2335 Independence La.', 'Waunakee', '6085555487');

INSERT INTO pets (name, birth_date, type_id, owner_id) VALUES ('Leo', '2010-09-07', 1, 1);
INSERT INTO pets (name, birth_date, type_id, owner_id) VALUES ('Basil', '2012-08-06', 6, 2);
INSERT INTO pets (name, birth_date, type_id, owner_id) VALUES ('Rosy', '2011-04-17', 2, 3);
INSERT INTO pets (name, birth_date, type_id, owner_id) VALUES ('Jewel', '2010-03-07', 2, 3);
INSERT INTO pets (name, birth_date, type_id, owner_id) VALUES ('Iggy', '2010-11-30', 3, 4);
INSERT INTO pets (name, birth_date, type_id, owner_id) VALUES ('George', '2010-01-20', 4, 5);
INSERT INTO pets (name, birth_date, type_id, owner_id) VALUES ('Samantha', '2012-09-04', 1, 6);
INSERT INTO pets (name, birth_date, type_id, owner_id) VALUES ('Max', '2012-09-04', 1, 6);
INSERT INTO pets (name, birth_date, type_id, owner_id) VALUES ('Lucky', '2011-08-06', 5, 7);
INSERT INTO pets (name, birth_date, type_id, owner_id) VALUES ('Mulligan', '2007-02-24', 2, 8);
INSERT INTO pets (name, birth_date, type_id, owner_id) VALUES ('Freddy', '2010-03-09', 5, 9);
INSERT INTO pets (name, birth_date, type_id, owner_id) VALUES ('Lucky', '2010-06-24', 2, 10);
INSERT INTO pets (name, birth_date, type_id, owner_id) VALUES ('Sly', '2012-06-08', 1, 10);
INSERT INTO pets (name, birth_date, type_id, adoptable) VALUES ('Buddy', '2021-03-15', 2, TRUE);
INSERT INTO pets (name, birth_date, type_id, adoptable) VALUES ('Cleo', '2020-07-22', 1, TRUE);
INSERT INTO pets (name, birth_date, type_id, adoptable) VALUES ('Finn', '2022-01-10', 5, TRUE);
INSERT INTO pets (name, birth_date, type_id, adoptable) VALUES ('Ginger', '2019-11-05', 1, TRUE);
INSERT INTO pets (name, birth_date, type_id, adoptable) VALUES ('Milo', '2021-06-30', 2, TRUE);
INSERT INTO pets (name, birth_date, type_id, adoptable) VALUES ('Nibbles', '2022-04-18', 6, TRUE);
INSERT INTO pets (name, birth_date, type_id, adoptable) VALUES ('Oliver', '2020-09-14', 1, TRUE);
INSERT INTO pets (name, birth_date, type_id, adoptable) VALUES ('Peanut', '2021-12-03', 6, TRUE);
INSERT INTO pets (name, birth_date, type_id, adoptable) VALUES ('Rocky', '2022-02-25', 2, TRUE);
INSERT INTO pets (name, birth_date, type_id, adoptable) VALUES ('Ziggy', '2020-05-11', 3, TRUE);

INSERT INTO visits VALUES (default, 7, '2013-01-01', 'rabies shot');
INSERT INTO visits VALUES (default, 8, '2013-01-02', 'rabies shot');
INSERT INTO visits VALUES (default, 8, '2013-01-03', 'neutered');
INSERT INTO visits VALUES (default, 7, '2013-01-04', 'spayed');
