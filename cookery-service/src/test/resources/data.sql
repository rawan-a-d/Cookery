-- INSERT INTO user (id, name, email, password, role) values(1, 'Rawan', 'rawan@gmail.com', '1234', 'admin');
-- INSERT INTO user (id, name, email, password, role) values(2, 'Omar', 'omar@gmail.com', '1234', 'admin');
-- INSERT INTO user (id, name, email, password, role) values(3, 'Ranim', 'ranim@gmail.com', '1234', 'user');
--
--
-- INSERT INTO recipe (id, name, description, image, user_id) VALUES(1, 'First recipe', 'First recipe description', 'First recipe image', 1);
-- INSERT INTO recipe (id, name, description, image, user_id) VALUES(2, 'Second recipe', 'Second recipe description', 'Second recipe image', 3);
-- INSERT INTO recipe (id, name, description, image, user_id) VALUES(3, 'Third recipe', 'Third recipe description', 'Third recipe image', 3);
-- CREATE TABLE user
-- (id INTEGER not NULL AUTO_INCREMENT,
-- name VARCHAR(255),
-- email VARCHAR(255),
-- password VARCHAR(255),
-- role VARCHAR(255),
-- PRIMARY KEY ( id ));
--
-- CREATE TABLE recipe
-- (id INTEGER not NULL AUTO_INCREMENT,
-- name VARCHAR(255),
-- description VARCHAR(255),
-- image VARCHAR(255),
-- user_id INTEGER,
-- PRIMARY KEY ( id ),
-- CONSTRAINT USER_ID_FK FOREIGN KEY (user_id)
-- REFERENCES  user( id ));
--
-- CREATE TABLE ingredient
-- (id INTEGER not NULL AUTO_INCREMENT,
-- ingredient VARCHAR(255),
-- amount INTEGER,
-- recipe_id INTEGER,
-- PRIMARY KEY ( id ),
-- FOREIGN KEY (recipe_id) REFERENCES recipe( id ));
--
-- CREATE TABLE user_favourite_recipe
-- (id INTEGER not NULL AUTO_INCREMENT,
-- user_id INTEGER,
-- recipe_id INTEGER,
-- PRIMARY KEY ( id ),
-- FOREIGN KEY (user_id) REFERENCES user( id ),
-- FOREIGN KEY (recipe_id) REFERENCES recipe( id ));
--
--
-- INSERT INTO user (name, email, password, role) VALUES ('Rawan', 'rawan@gmail.com', '1234', 'admin');
-- INSERT INTO user (name, email, password, role) VALUES ('Anas', 'anas@gmail.com', '1234', 'user');
-- INSERT INTO user (name, email, password, role) VALUES ('Omar', 'omar@gmail.com', '1234', 'admin');
-- INSERT INTO user (name, email, password, role) VALUES ('Raneem', 'raneem@gmail.com', '1234', 'user');
--
--
-- INSERT INTO recipe (name, description, image, user_id) VALUES ('recipe 1', 'recipe 1 desc', 'recipe 1 image', 1);
-- INSERT INTO recipe (name, description, image, user_id) VALUES ('recipe 2', 'recipe 2 desc', 'recipe 2 image', 1);
-- INSERT INTO recipe (name, description, image, user_id) VALUES ('recipe 3', 'recipe 3 desc', 'recipe 3 image', 2);
-- INSERT INTO recipe (name, description, image, user_id) VALUES ('recipe 4', 'recipe 4 desc', 'recipe 4 image', 3);
--
--
-- INSERT INTO ingredient (ingredient, amount, recipe_id) VALUES ('onion', 2, 1);
-- INSERT INTO ingredient (ingredient, amount, recipe_id) VALUES ('garlic', 1, 1);
-- INSERT INTO ingredient (ingredient, amount, recipe_id) VALUES ('tomato', 2, 2);
-- INSERT INTO ingredient (ingredient, amount, recipe_id) VALUES ('onion', 5, 4);
--
-- INSERT INTO user_favourite_recipe (user_id, recipe_id) VALUES (1, 2);
-- INSERT INTO user_favourite_recipe (user_id, recipe_id) VALUES (1, 3);
-- INSERT INTO user_favourite_recipe (user_id, recipe_id) VALUES (2, 1);


-- DROP ALL OBJECTS;


CREATE TABLE user
(id INTEGER not NULL AUTO_INCREMENT,
 name VARCHAR(255),
 email VARCHAR(255),
 password VARCHAR(255),
 role VARCHAR(255),
 PRIMARY KEY ( id ));

CREATE TABLE recipe
(id INTEGER not NULL AUTO_INCREMENT,
 name VARCHAR(255),
 description VARCHAR(255),
 image VARCHAR(255),
 user_id INTEGER,
 PRIMARY KEY ( id ),
 CONSTRAINT USER_ID_FK FOREIGN KEY (user_id)
REFERENCES  user( id ));

CREATE TABLE ingredient
(id INTEGER not NULL AUTO_INCREMENT,
 ingredient VARCHAR(255),
 amount INTEGER,
 recipe_id INTEGER,
 PRIMARY KEY ( id ),
CONSTRAINT RECIPE_ID_FK FOREIGN KEY (recipe_id)
REFERENCES  recipe( id ));

CREATE TABLE user_favourite_recipe
(id INTEGER not NULL AUTO_INCREMENT,
 user_id INTEGER,
 recipe_id INTEGER,
 PRIMARY KEY ( id ),
CONSTRAINT RECIPE_ID_FOREIGNKEY FOREIGN KEY (recipe_id)
REFERENCES  recipe( id ),
CONSTRAINT USER_ID_FOREIGNKEY FOREIGN KEY (user_id)
REFERENCES  user( id ));


INSERT INTO user (name, email, password, role) VALUES ('Rawan', 'rawan@gmail.com', '1234', 'admin');
INSERT INTO user (name, email, password, role) VALUES ('Anas', 'anas@gmail.com', '1234', 'user');
INSERT INTO user (name, email, password, role) VALUES ('Omar', 'omar@gmail.com', '1234', 'admin');
INSERT INTO user (name, email, password, role) VALUES ('Raneem', 'raneem@gmail.com', '1234', 'user');


INSERT INTO recipe (name, description, image, user_id) VALUES ('recipe 1', 'recipe 1 desc', 'recipe 1 image', 1);
INSERT INTO recipe (name, description, image, user_id) VALUES ('recipe 2', 'recipe 2 desc', 'recipe 2 image', 1);
INSERT INTO recipe (name, description, image, user_id) VALUES ('recipe 3', 'recipe 3 desc', 'recipe 3 image', 2);
INSERT INTO recipe (name, description, image, user_id) VALUES ('recipe 4', 'recipe 4 desc', 'recipe 4 image', 3);


INSERT INTO ingredient (ingredient, amount, recipe_id) VALUES ('onion', 2, 1);
INSERT INTO ingredient (ingredient, amount, recipe_id) VALUES ('garlic', 1, 1);
INSERT INTO ingredient (ingredient, amount, recipe_id) VALUES ('tomato', 2, 2);
INSERT INTO ingredient (ingredient, amount, recipe_id) VALUES ('onion', 5, 4);

INSERT INTO user_favourite_recipe (user_id, recipe_id) VALUES (1, 2);
INSERT INTO user_favourite_recipe (user_id, recipe_id) VALUES (1, 3);
INSERT INTO user_favourite_recipe (user_id, recipe_id) VALUES (2, 1);