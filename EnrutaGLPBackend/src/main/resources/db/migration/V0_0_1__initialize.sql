CREATE TABLE configuracion (
	llave VARCHAR(100) NOT NULL,
    valor VARCHAR(200),
    PRIMARY KEY (llave)
);

CREATE TABLE indicador (
    nombre VARCHAR(100) NOT NULL,
    valor DOUBLE,
    PRIMARY KEY (nombre)
);

CREATE TABLE perfil (
	id TINYINT NOT NULL,
	nombre VARCHAR(20) NOT NULL,
	PRIMARY KEY (id)
);

CREATE TABLE ejecucion (
	id INT NOT NULL AUTO_INCREMENT,
	modo_ejecucion INT NOT NULL,
    fecha_inicio TIMESTAMP NOT NULL,
    fecha_fin TIMESTAMP NOT NULL,
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
    color CHAR(7) NOT NULL,
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
    DECLARE cantidad_por_plan double;
    
	UPDATE pedido set 
    cantidad_glp_por_planificar = cantidad_glp_por_planificar - NEW.cantidad_entregada
    where id = NEW.id_pedido;
    
    SELECT cantidad_glp_por_planificar into cantidad_por_plan from pedido where id = NEW.id_pedido; 
    
    IF cantidad_por_plan = 0.0 then 
		UPDATE pedido
        SET fecha_completado = (SELECT hora_llegada FROM ruta where id = NEW.id_ruta)
        WHERE id = NEW.id_pedido;
    END IF; 
    
END$$

CREATE TRIGGER before_entrega_pedido_delete BEFORE DELETE ON entrega_pedido 
FOR EACH ROW
BEGIN
	UPDATE pedido set 
    cantidad_glp_por_planificar = cantidad_glp_por_planificar + OLD.cantidad_entregada,
    fecha_completado = null
    where id = OLD.id_pedido;
END$$    

DELIMITER ;

-- Inserts:

-- Modo de ejecucion : 1(dia a dia), 2(sim 3 dias), 3(sim hasta colapso)
INSERT INTO configuracion VALUES 
('CTE_VOLUMEN_CONSUMO','1'), 
('ULTIMO_CHECKPOINT_PEDIDOS',NULL),
('MODO_EJECUCION','0'),
('FECHA_INICIO_SIMULACION',NULL),
('FECHA_FIN_SIMULACION',NULL);

-- Indicadores

INSERT INTO indicador VALUES 
('CANTIDAD_PEDIDOS_PROCESADOS',0),
('PORCENTAJE_PLAZO_OCUPADO_PROMEDIO',0);

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

INSERT INTO 
camion 
(id, codigo, placa, ubicacion_actual_x, ubicacion_actual_y, carga_actual_glp, carga_actual_petroleo,estado,tipo,siguiente_movimiento,id_punto_actual,color)
VALUES 
(1,'TA01','ABC-100',12,8,25,25,1,1,null,null,'#ff4400'),
(2,'TA02','ABC-101',12,8,25,25,1,1,null,null,'#ffa600'),
(3,'TB01','ABC-102',12,8,15,25,1,2,null,null,'#c4c57a'),
(4,'TB02','ABC-103',12,8,15,25,1,2,null,null,'#7d5555'),
(5,'TB03','ABC-104',12,8,15,25,1,2,null,null,'#a17317'),
(6,'TB04','ABC-105',12,8,15,25,1,2,null,null,'#a4e40f'),
(7,'TC01','ABC-106',12,8,10,25,1,3,null,null,'#1dc452'),
(8,'TC02','ABC-107',12,8,10,25,1,3,null,null,'#48d777'),
(9,'TC03','ABC-108',12,8,10,25,1,3,null,null,'#032015'),
(10,'TC04','ABC-109',12,8,10,25,1,3,null,null,'#68ded0'),
(11,'TD01','ABC-110',12,8,5,25,1,4,null,null,'#0020a2'),
(12,'TD02','ABC-111',12,8,5,25,1,4,null,null,'#2d0a57'),
(13,'TD03','ABC-112',12,8,5,25,1,4,null,null,'#0e93ff'),
(14,'TD04','ABC-113',12,8,5,25,1,4,null,null,'#e52fb1'),
(15,'TD05','ABC-114',12,8,5,25,1,4,null,null,'#bb96ad'),
(16,'TD06','ABC-115',12,8,5,25,1,4,null,null,'#494461'),
(17,'TD07','ABC-116',12,8,5,25,1,4,null,null,'#3c2005'),
(18,'TD08','ABC-117',12,8,5,25,1,4,null,null,'#eb99d6'),
(19,'TD09','ABC-118',12,8,5,25,1,4,null,null,'#7a3e3e'),
(20,'TD10','ABC-119',12,8,5,25,1,4,null,null,'#94808e');

INSERT INTO planta VALUES
(1,12,8,null,null,null,null,true),
(2,42,42,null,null,null,null,false),
(3,63,3,null,null,null,null,false);

-- Mantenimiento preventivos
INSERT INTO mantenimiento VALUES
(1,1,'2021-08-01 00:00:00','2021-08-01 23:59:00',1),
(2,11,'2021-08-03 00:00:00','2021-08-03 23:59:00',1),
(3,7,'2021-08-05 00:00:00','2021-08-05 23:59:00',1),
(4,3,'2021-08-07 00:00:00','2021-08-07 23:59:00',1),
(5,12,'2021-08-10 00:00:00','2021-08-10 23:59:00',1),
(6,13,'2021-08-13 00:00:00','2021-08-13 23:59:00',1),
(7,4,'2021-08-16 00:00:00','2021-08-16 23:59:00',1),
(8,14,'2021-08-19 00:00:00','2021-08-19 23:59:00',1),
(9,8,'2021-08-22 00:00:00','2021-08-22 23:59:00',1),
(10,15,'2021-08-25 00:00:00','2021-08-25 23:59:00',1),
(11,2,'2021-09-01 00:00:00','2021-09-01 23:59:00',1),
(12,16,'2021-09-03 00:00:00','2021-09-03 23:59:00',1),
(13,9,'2021-09-05 00:00:00','2021-09-05 23:59:00',1),
(14,5,'2021-09-07 00:00:00','2021-09-07 23:59:00',1),
(15,17,'2021-09-10 00:00:00','2021-09-10 23:59:00',1),
(16,18,'2021-09-13 00:00:00','2021-09-13 23:59:00',1),
(17,6,'2021-09-16 00:00:00','2021-09-16 23:59:00',1),
(18,19,'2021-07-19 00:00:00','2021-07-19 23:59:00',1),
(19,10,'2021-07-22 00:00:00','2021-07-22 23:59:00',1),
(20,20,'2021-07-25 00:00:00','2021-07-25 23:59:00',1),

(21,1,'2021-10-01 00:00:00','2021-10-01 23:59:00',1),
(22,11,'2021-10-03 00:00:00','2021-10-03 23:59:00',1),
(23,7,'2021-10-05 00:00:00','2021-10-05 23:59:00',1),
(24,3,'2021-10-07 00:00:00','2021-10-07 23:59:00',1),
(25,12,'2021-10-10 00:00:00','2021-10-10 23:59:00',1),
(26,13,'2021-10-13 00:00:00','2021-10-13 23:59:00',1),
(27,4,'2021-10-16 00:00:00','2021-10-16 23:59:00',1),
(28,14,'2021-10-19 00:00:00','2021-10-19 23:59:00',1),
(29,8,'2021-10-22 00:00:00','2021-10-22 23:59:00',1),
(30,15,'2021-10-25 00:00:00','2021-10-25 23:59:00',1),
(31,2,'2021-11-01 00:00:00','2021-11-01 23:59:00',1),
(32,16,'2021-11-03 00:00:00','2021-11-03 23:59:00',1),
(33,9,'2021-11-05 00:00:00','2021-11-05 23:59:00',1),
(34,5,'2021-11-07 00:00:00','2021-11-07 23:59:00',1),
(35,17,'2021-11-10 00:00:00','2021-11-10 23:59:00',1),
(36,18,'2021-11-13 00:00:00','2021-11-13 23:59:00',1),
(37,6,'2021-11-16 00:00:00','2021-11-16 23:59:00',1),
(38,19,'2021-09-19 00:00:00','2021-09-19 23:59:00',1),
(39,10,'2021-09-22 00:00:00','2021-09-22 23:59:00',1),
(40,20,'2021-09-25 00:00:00','2021-09-25 23:59:00',1),

(41,1,'2021-12-01 00:00:00','2021-12-01 23:59:00',1),
(42,11,'2021-12-03 00:00:00','2021-12-03 23:59:00',1),
(43,7,'2021-12-05 00:00:00','2021-12-05 23:59:00',1),
(44,3,'2021-12-07 00:00:00','2021-12-07 23:59:00',1),
(45,12,'2021-12-10 00:00:00','2021-12-10 23:59:00',1),
(46,13,'2021-12-13 00:00:00','2021-12-13 23:59:00',1),
(47,4,'2021-12-16 00:00:00','2021-12-16 23:59:00',1),
(48,14,'2021-12-19 00:00:00','2021-12-19 23:59:00',1),
(49,8,'2021-12-22 00:00:00','2021-12-22 23:59:00',1),
(50,15,'2021-12-25 00:00:00','2021-12-25 23:59:00',1),
(51,2,'2022-01-01 00:00:00','2022-01-01 23:59:00',1),
(52,16,'2022-01-03 00:00:00','2022-01-03 23:59:00',1),
(53,9,'2022-01-05 00:00:00','2022-01-05 23:59:00',1),
(54,5,'2022-01-07 00:00:00','2022-01-07 23:59:00',1),
(55,17,'2022-01-10 00:00:00','2022-01-10 23:59:00',1),
(56,18,'2022-01-13 00:00:00','2022-01-13 23:59:00',1),
(57,6,'2022-01-16 00:00:00','2022-01-16 23:59:00',1),
(58,19,'2021-11-19 00:00:00','2021-11-19 23:59:00',1),
(59,10,'2021-11-22 00:00:00','2021-11-22 23:59:00',1),
(60,20,'2021-11-25 00:00:00','2021-11-25 23:59:00',1),

(61,1,'2022-02-01 00:00:00','2022-02-01 23:59:00',1),
(62,11,'2022-02-03 00:00:00','2022-02-03 23:59:00',1),
(63,7,'2022-02-05 00:00:00','2022-02-05 23:59:00',1),
(64,3,'2022-02-07 00:00:00','2022-02-07 23:59:00',1),
(65,12,'2022-02-10 00:00:00','2022-02-10 23:59:00',1),
(66,13,'2022-02-13 00:00:00','2022-02-13 23:59:00',1),
(67,4,'2022-02-16 00:00:00','2022-02-16 23:59:00',1),
(68,14,'2022-02-19 00:00:00','2022-02-19 23:59:00',1),
(69,8,'2022-02-22 00:00:00','2022-02-22 23:59:00',1),
(70,15,'2022-02-25 00:00:00','2022-02-25 23:59:00',1),
(71,2,'2022-03-01 00:00:00','2022-03-01 23:59:00',1),
(72,16,'2022-03-03 00:00:00','2022-03-03 23:59:00',1),
(73,9,'2022-03-05 00:00:00','2022-03-05 23:59:00',1),
(74,5,'2022-03-07 00:00:00','2022-03-07 23:59:00',1),
(75,17,'2022-03-10 00:00:00','2022-03-10 23:59:00',1),
(76,18,'2022-03-13 00:00:00','2022-03-13 23:59:00',1),
(77,6,'2022-03-16 00:00:00','2022-03-16 23:59:00',1),
(78,19,'2022-01-19 00:00:00','2022-01-19 23:59:00',1),
(79,10,'2022-01-22 00:00:00','2022-01-22 23:59:00',1),
(80,20,'2022-01-25 00:00:00','2022-01-25 23:59:00',1),

(81,1,'2022-04-01 00:00:00','2022-04-01 23:59:00',1),
(82,11,'2022-04-03 00:00:00','2022-04-03 23:59:00',1),
(83,7,'2022-04-05 00:00:00','2022-04-05 23:59:00',1),
(84,3,'2022-04-07 00:00:00','2022-04-07 23:59:00',1),
(85,12,'2022-04-10 00:00:00','2022-04-10 23:59:00',1),
(86,13,'2022-04-13 00:00:00','2022-04-13 23:59:00',1),
(87,4,'2022-04-16 00:00:00','2022-04-16 23:59:00',1),
(88,14,'2022-04-19 00:00:00','2022-04-19 23:59:00',1),
(89,8,'2022-04-22 00:00:00','2022-04-22 23:59:00',1),
(90,15,'2022-04-25 00:00:00','2022-04-25 23:59:00',1),
(91,2,'2022-05-01 00:00:00','2022-05-01 23:59:00',1),
(92,16,'2022-05-03 00:00:00','2022-05-03 23:59:00',1),
(93,9,'2022-05-05 00:00:00','2022-05-05 23:59:00',1),
(94,5,'2022-05-07 00:00:00','2022-05-07 23:59:00',1),
(95,17,'2022-05-10 00:00:00','2022-05-10 23:59:00',1),
(96,18,'2022-05-13 00:00:00','2022-05-13 23:59:00',1),
(97,6,'2022-05-16 00:00:00','2022-05-16 23:59:00',1),
(98,19,'2022-03-19 00:00:00','2022-03-19 23:59:00',1),
(99,10,'2022-03-22 00:00:00','2022-03-22 23:59:00',1),
(100,20,'2022-03-25 00:00:00','2022-03-25 23:59:00',1),

(101,1,'2022-06-01 00:00:00','2022-06-01 23:59:00',1),
(102,11,'2022-06-03 00:00:00','2022-06-03 23:59:00',1),
(103,7,'2022-06-05 00:00:00','2022-06-05 23:59:00',1),
(104,3,'2022-06-07 00:00:00','2022-06-07 23:59:00',1),
(105,12,'2022-06-10 00:00:00','2022-06-10 23:59:00',1),
(106,13,'2022-06-13 00:00:00','2022-06-13 23:59:00',1),
(107,4,'2022-06-16 00:00:00','2022-06-16 23:59:00',1),
(108,14,'2022-06-19 00:00:00','2022-06-19 23:59:00',1),
(109,8,'2022-06-22 00:00:00','2022-06-22 23:59:00',1),
(110,15,'2022-06-25 00:00:00','2022-06-25 23:59:00',1),
(111,2,'2022-07-01 00:00:00','2022-07-01 23:59:00',1),
(112,16,'2022-07-03 00:00:00','2022-07-03 23:59:00',1),
(113,9,'2022-07-05 00:00:00','2022-07-05 23:59:00',1),
(114,5,'2022-07-07 00:00:00','2022-07-07 23:59:00',1),
(115,17,'2022-07-10 00:00:00','2022-07-10 23:59:00',1),
(116,18,'2022-07-13 00:00:00','2022-07-13 23:59:00',1),
(117,6,'2022-07-16 00:00:00','2022-07-16 23:59:00',1),
(118,19,'2022-05-19 00:00:00','2022-05-19 23:59:00',1),
(119,10,'2022-05-22 00:00:00','2022-05-22 23:59:00',1),
(120,20,'2022-05-25 00:00:00','2022-05-25 23:59:00',1),

(121,1,'2022-08-01 00:00:00','2022-08-01 23:59:00',1),
(122,11,'2022-08-03 00:00:00','2022-08-03 23:59:00',1),
(123,7,'2022-08-05 00:00:00','2022-08-05 23:59:00',1),
(124,3,'2022-08-07 00:00:00','2022-08-07 23:59:00',1),
(125,12,'2022-08-10 00:00:00','2022-08-10 23:59:00',1),
(126,13,'2022-08-13 00:00:00','2022-08-13 23:59:00',1),
(127,4,'2022-08-16 00:00:00','2022-08-16 23:59:00',1),
(128,14,'2022-08-19 00:00:00','2022-08-19 23:59:00',1),
(129,8,'2022-08-22 00:00:00','2022-08-22 23:59:00',1),
(130,15,'2022-08-25 00:00:00','2022-08-25 23:59:00',1),
(131,2,'2022-09-01 00:00:00','2022-09-01 23:59:00',1),
(132,16,'2022-09-03 00:00:00','2022-09-03 23:59:00',1),
(133,9,'2022-09-05 00:00:00','2022-09-05 23:59:00',1),
(134,5,'2022-09-07 00:00:00','2022-09-07 23:59:00',1),
(135,17,'2022-09-10 00:00:00','2022-09-10 23:59:00',1),
(136,18,'2022-09-13 00:00:00','2022-09-13 23:59:00',1),
(137,6,'2022-09-16 00:00:00','2022-09-16 23:59:00',1),
(138,19,'2022-07-19 00:00:00','2022-07-19 23:59:00',1),
(139,10,'2022-07-22 00:00:00','2022-07-22 23:59:00',1),
(140,20,'2022-07-25 00:00:00','2022-07-25 23:59:00',1)
;









