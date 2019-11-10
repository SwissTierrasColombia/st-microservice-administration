INSERT INTO administration.users (id, first_name, last_name, username, email, password, enabled, created_at) VALUES (1, 'Jhon','Rondon', 'jrondon', 'jhonfr29@gmail.com', '$2a$10$C9Dz6U721ss4HsClLNS7EuWfla6nTMfO8gB9XlZbeNXzi6xNivvnC', true, '2019-11-07 07:51:00');

INSERT INTO administration.roles (id, name) VALUES (1, 'ADMINISTRADOR');
INSERT INTO administration.roles (id, name) VALUES (2, 'GESTOR');
INSERT INTO administration.roles (id, name) VALUES (3, 'INTEGRADOR');
INSERT INTO administration.roles (id, name) VALUES (4, 'OPERADOR');
INSERT INTO administration.roles (id, name) VALUES (5, 'PROVEEDOR INSUMO');

INSERT INTO administration.users_x_roles (user_id, role_id) VALUES (1, 1);