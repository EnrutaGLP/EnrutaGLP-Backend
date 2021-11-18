CREATE TABLE configuracion (
	llave VARCHAR(100) NOT NULL,
    valor VARCHAR(200),
    PRIMARY KEY (llave)
);

CREATE TABLE perfil (
	id TINYINT NOT NULL,
	nombre VARCHAR(20) NOT NULL,
	PRIMARY KEY (id)
);

CREATE TABLE estado_camion (
	id TINYINT NOT NULL,
	nombre VARCHAR(20) NOT NULL,
	PRIMARY KEY (id)
);

CREATE TABLE estado_pedido (
	id TINYINT NOT NULL,
	nombre VARCHAR(20) NOT NULL,
	PRIMARY KEY (id)
);

CREATE TABLE tipo_mantenimiento (
	id TINYINT NOT NULL,
	nombre VARCHAR(20) NOT NULL,
	PRIMARY KEY (id)
);

CREATE TABLE tipo_ruta (
	id TINYINT NOT NULL,
	nombre VARCHAR(20) NOT NULL,
	PRIMARY KEY (id)
);

CREATE TABLE usuario (
	id INT NOT NULL AUTO_INCREMENT,
	nombre VARCHAR(100) NULL,
	correo VARCHAR(100) NOT NULL,
	id_perfil TINYINT NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (id_perfil) REFERENCES perfil(id)
);

CREATE TABLE planta (
	id INT NOT NULL AUTO_INCREMENT,
	ubicacion_x INT NOT NULL,
	ubicacion_y INT NOT NULL,
	capacidad_petroleo DOUBLE,
    capacidad_glp DOUBLE,
    carga_actual_glp DOUBLE,
    carga_actual_petroleo DOUBLE,
    es_principal TINYINT NOT NULL,
	PRIMARY KEY (id)
);

CREATE TABLE pedido (
	id INT NOT NULL AUTO_INCREMENT,
	codigo CHAR(8),
	cliente VARCHAR(100),
    cantidad_glp DOUBLE NOT NULL,
    cantidad_glp_atendida DOUBLE NOT NULL,
    cantidad_glp_por_planificar DOUBLE NOT NULL,
	ubicacion_x INT NOT NULL,
	ubicacion_y INT NOT NULL,
    fecha_pedido TIMESTAMP NOT NULL, 
    fecha_limite TIMESTAMP NOT NULL, 
    fecha_completado TIMESTAMP, 
    estado TINYINT,
	PRIMARY KEY (id),
	FOREIGN KEY (estado) REFERENCES estado_pedido(id)
);

CREATE TABLE tipo_camion (
	id INT NOT NULL,
	tara CHAR(2) NOT NULL,
    peso_bruto DOUBLE NOT NULL,
    capacidad_glp DOUBLE NOT NULL,
	peso_glp DOUBLE NOT NULL,
	peso_combinado DOUBLE NOT NULL,
    capacidad_tanque DOUBLE NOT NULL,
    velocidad_promedio DOUBLE NOT NULL,
    unidades INT NOT NULL,
	PRIMARY KEY (id)
);

CREATE TABLE bloqueo (
	id INT NOT NULL AUTO_INCREMENT,
	fecha_inicio TIMESTAMP,
    fecha_fin TIMESTAMP,
	PRIMARY KEY (id)
);

CREATE TABLE camion (
	id INT NOT NULL,
	codigo CHAR(4) NOT NULL,
    placa CHAR(7),
    ubicacion_actual_x INT,
	ubicacion_actual_y INT,
    carga_actual_glp DOUBLE,
    carga_actual_petroleo DOUBLE,
    estado TINYINT,
    tipo INT NOT NULL,
    siguiente_movimiento TIMESTAMP,
    id_punto_actual INT,
	PRIMARY KEY (id),
	FOREIGN KEY (estado) REFERENCES estado_camion(id),
	FOREIGN KEY (tipo) REFERENCES tipo_camion(id)
);


CREATE TABLE averia (
	id INT NOT NULL AUTO_INCREMENT,
	id_camion INT NOT NULL,
    fecha TIMESTAMP NOT NULL,
	PRIMARY KEY (id),
    FOREIGN KEY (id_camion) REFERENCES camion(id)
);

CREATE TABLE mantenimiento (
	id INT NOT NULL AUTO_INCREMENT,
	id_camion INT NOT NULL,
    fecha_inicio TIMESTAMP,
    fecha_fin TIMESTAMP,
    tipo TINYINT NOT NULL,
	PRIMARY KEY (id),
    FOREIGN KEY (id_camion) REFERENCES camion(id),
	FOREIGN KEY (tipo) REFERENCES tipo_mantenimiento(id)
);

CREATE TABLE ruta (
	id INT NOT NULL AUTO_INCREMENT,
	consumo_petroleo DOUBLE,
    hora_llegada TIMESTAMP,
    hora_salida TIMESTAMP,
    id_camion INT NOT NULL,
    orden INT,
    tipo TINYINT NOT NULL,
	PRIMARY KEY (id),
    FOREIGN KEY (id_camion) REFERENCES camion(id),
	FOREIGN KEY (tipo) REFERENCES tipo_ruta(id)
);

CREATE TABLE entrega_pedido (
	id_ruta INT NOT NULL,
	cantidad_entregada DOUBLE,
    id_pedido INT NOT NULL,
	PRIMARY KEY (id_ruta),
    FOREIGN KEY (id_pedido) REFERENCES pedido(id),
    FOREIGN KEY (id_ruta) REFERENCES ruta(id) ON DELETE CASCADE 
);

CREATE TABLE recarga (
	id_ruta INT NOT NULL,
	cantidad_recargada DOUBLE,
    id_planta INT,
	PRIMARY KEY (id_ruta),
    FOREIGN KEY (id_planta) REFERENCES planta(id),
    FOREIGN KEY (id_ruta) REFERENCES ruta(id) ON DELETE CASCADE  
);

CREATE TABLE punto (
	id INT NOT NULL AUTO_INCREMENT,
	ubicacion_x INT NOT NULL,
	ubicacion_y INT NOT NULL,
    orden INT,
    id_bloqueo INT,
    id_ruta INT,
	PRIMARY KEY (id),
    FOREIGN KEY (id_bloqueo) REFERENCES bloqueo(id),
    FOREIGN KEY (id_ruta) REFERENCES ruta(id) ON DELETE CASCADE 
);

ALTER TABLE camion ADD FOREIGN KEY (id_punto_actual) REFERENCES punto(id) ON DELETE SET NULL; 
-- Triggers:

DELIMITER $$

CREATE TRIGGER after_insert_entrega_pedido AFTER INSERT ON entrega_pedido
FOR EACH ROW 
BEGIN
	UPDATE pedido set 
    cantidad_glp_por_planificar = cantidad_glp_por_planificar - NEW.cantidad_entregada
    where id = NEW.id_pedido;
END$$

CREATE TRIGGER before_entrega_pedido_delete BEFORE DELETE ON entrega_pedido 
FOR EACH ROW
BEGIN
	UPDATE pedido set 
    cantidad_glp_por_planificar = cantidad_glp_por_planificar + OLD.cantidad_entregada
    where id = OLD.id_pedido;
END$$    

DELIMITER ;

-- Inserts:

INSERT INTO configuracion VALUES ('CTE_VOLUMEN_CONSUMO','1') , ('ULTIMO_CHECKPOINT_PEDIDOS',NULL);

INSERT INTO estado_pedido VALUES (1,'En cola'),(2,'En proceso'),(3,'Completado'); 

INSERT INTO perfil VALUES(1,'Administrador'),(2,'Gestor de pedidos'),(3,'Gestor de rutas');

INSERT INTO tipo_mantenimiento VALUES(1,'Preventivo'),(2,'Correctivo');

INSERT INTO tipo_ruta VALUES (1,'Entrega'),(2,'Recarga'); 

INSERT INTO estado_camion VALUES(1,'En reposo'),(2,'En ruta'),(3,'Averiado'),(4,'En mantenimiento');

INSERT INTO tipo_camion VALUES
(1,'TA',2.5,25,12.5,15,25,50,2),
(2,'TB',2.0,15,7.5,9.5,25,50,4),
(3,'TC',1.5,10,5,6.5,25,50,4),
(4,'TD',1.0,5,2.5,3.5,25,50,10);

INSERT INTO camion VALUES 
(1,'TA01','ABC-100',12,8,25,25,1,1,null,null),
(2,'TA02','ABC-101',12,8,25,25,1,1,null,null),
(3,'TB01','ABC-102',12,8,15,25,1,2,null,null),
(4,'TB02','ABC-103',12,8,15,25,1,2,null,null),
(5,'TB03','ABC-104',12,8,15,25,1,2,null,null),
(6,'TB04','ABC-105',12,8,15,25,1,2,null,null),
(7,'TC01','ABC-106',12,8,10,25,1,3,null,null),
(8,'TC02','ABC-107',12,8,10,25,1,3,null,null),
(9,'TC03','ABC-108',12,8,10,25,1,3,null,null),
(10,'TC04','ABC-109',12,8,10,25,1,3,null,null),
(11,'TD01','ABC-110',12,8,5,25,1,4,null,null),
(12,'TD02','ABC-111',12,8,5,25,1,4,null,null),
(13,'TD03','ABC-112',12,8,5,25,1,4,null,null),
(14,'TD04','ABC-113',12,8,5,25,1,4,null,null),
(15,'TD05','ABC-114',12,8,5,25,1,4,null,null),
(16,'TD06','ABC-115',12,8,5,25,1,4,null,null),
(17,'TD07','ABC-116',12,8,5,25,1,4,null,null),
(18,'TD08','ABC-117',12,8,5,25,1,4,null,null),
(19,'TD09','ABC-118',12,8,5,25,1,4,null,null),
(20,'TD10','ABC-119',12,8,5,25,1,4,null,null);

INSERT INTO planta VALUES
(1,12,8,null,null,null,null,true),
(2,42,42,null,null,null,null,false),
(3,63,3,null,null,null,null,false);


INSERT INTO mantenimiento VALUES
(1,1,'2021-12-01 00:00:00','2021-12-01 23:59:00',1),
(2,11,'2021-12-03 00:00:00','2021-12-03 23:59:00',1),
(3,7,'2021-12-05 00:00:00','2021-12-05 23:59:00',1),
(4,3,'2021-12-07 00:00:00','2021-12-07 23:59:00',1),
(5,12,'2021-12-10 00:00:00','2021-12-10 23:59:00',1),
(6,13,'2021-12-13 00:00:00','2021-12-13 23:59:00',1),
(7,4,'2021-12-16 00:00:00','2021-12-16 23:59:00',1),
(8,14,'2021-12-19 00:00:00','2021-12-19 23:59:00',1),
(9,8,'2021-12-22 00:00:00','2021-12-22 23:59:00',1),
(10,15,'2021-12-25 00:00:00','2021-12-25 23:59:00',1),
(11,2,'2022-01-01 00:00:00','2022-01-01 23:59:00',1),
(12,16,'2022-01-03 00:00:00','2022-01-03 23:59:00',1),
(13,9,'2022-01-05 00:00:00','2022-01-05 23:59:00',1),
(14,5,'2022-01-07 00:00:00','2022-01-07 23:59:00',1),
(15,17,'2022-01-10 00:00:00','2022-01-10 23:59:00',1),
(16,18,'2022-01-13 00:00:00','2022-01-13 23:59:00',1),
(17,6,'2022-01-16 00:00:00','2022-01-16 23:59:00',1),
(18,19,'2021-11-19 00:00:00','2021-11-19 23:59:00',1),
(19,10,'2021-11-22 00:00:00','2021-11-22 23:59:00',1),
(20,20,'2021-11-25 00:00:00','2021-11-25 23:59:00',1);









