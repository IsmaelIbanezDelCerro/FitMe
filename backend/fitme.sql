DROP TABLE IF EXISTS `dia_menu`;
CREATE TABLE `dia_menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `menu_id` int(11) NOT NULL,
  `dia_semana` tinyint(4) NOT NULL,
  `desayuno` varchar(255) DEFAULT NULL,
  `almuerzo` varchar(255) DEFAULT NULL,
  `cena` varchar(255) DEFAULT NULL,
  `kcal_totales` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `menu_id` (`menu_id`),
  CONSTRAINT `dia_menu_ibfk_1` FOREIGN KEY (`menu_id`) REFERENCES `menu_semanal` (`id`) ON DELETE CASCADE,
  CONSTRAINT `chk_dia` CHECK (`dia_semana` between 1 and 7)
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

LOCK TABLES `dia_menu` WRITE;
INSERT INTO `dia_menu` VALUES (1,1,1,'Avena con leche, plátano y miel','Arroz con pechuga de pollo y brócoli','Salmón a la plancha con patata asada',2850),(2,1,2,'Tortilla de 3 huevos con pavo y tostadas','Pasta integral con ternera y tomate natural','Pechuga de pollo con batata y espinacas',2920),(3,1,3,'Batido de proteína con avena y frutos rojos','Arroz con atún y guisantes','Ternera a la plancha con arroz basmati',2780),(4,1,4,'Yogur griego con granola y almendras','Lentejas con arroz integral','Salmón al horno con brócoli y quinoa',2900),(5,1,5,'Avena con requesón y nueces','Pollo con pasta y pimiento','Huevos revueltos con jamón y tostadas',2860),(6,1,6,'Tostadas con aguacate, huevos y pavo','Arroz con gambas y champiñones','Ternera con verduras al wok',3100),(7,1,7,'Batido de proteína con plátano y mantequilla','Pasta con pollo y pesto','Salmón con batata y ensalada verde',2950),(8,2,1,'Avena con leche y plátano','Arroz con pollo y brócoli','Atún con patata asada',2800),(9,2,2,'Huevos con tostadas integrales y aguacate','Pasta con ternera y tomate','Pollo a la plancha con arroz',2880),(10,2,3,'Batido proteínico con avena','Lentejas con pollo','Salmón con brócoli',2750),(11,2,4,'Yogur con granola','Arroz con atún y pimiento','Ternera con quinoa',2820),(12,2,5,'Avena con requesón y miel','Pasta con pollo y champiñones','Gambas con arroz',2900),(13,2,6,'Tostadas con huevos y pavo','Arroz con gambas y brócoli','Ternera con ensalada',3050),(14,2,7,'Batido de proteína con nueces','Pasta con pollo y pesto','Salmón con patata',2920),(15,3,1,'Yogur natural con frutos rojos y semillas de chía','Ensalada de atún con huevo duro y rúcula','Merluza al vapor con espinacas salteadas',1850),(16,3,2,'Kéfir con avena y plátano en rodajas','Salmón a la plancha con brócoli al vapor','Pechuga de pollo con espinacas y limón',1780),(17,3,3,'Yogur griego natural con almendras','Ensalada de pollo con tomate y pepino','Merluza al horno con calabacín',1820),(18,3,4,'Tortilla de claras con tomate','Atún con arroz integral y judías verdes','Salmón a la plancha con ensalada verde',1900),(19,3,5,'Kéfir con plátano y nueces','Ensalada de gambas con aguacate','Pechuga con brócoli al vapor',1750),(20,3,6,'Yogur con muesli sin azúcar','Salmón con batata al horno','Merluza con espárragos trigueros',1950),(21,3,7,'Tortilla de claras con tostada integral','Ensalada de lentejas con pimiento rojo','Pollo al horno con champiñones',1880),(22,4,1,'Yogur con frutos rojos','Atún con ensalada','Merluza con espinacas',1800),(23,4,2,'Kéfir con plátano','Salmón con brócoli','Pollo con ensalada verde',1750),(24,4,3,'Yogur griego con almendras','Ensalada de pollo','Bacalao con calabacín',1790),(25,4,4,'Tortilla de claras','Atún con arroz integral','Salmón con lechuga',1870),(26,4,5,'Kéfir con nueces','Gambas con ensalada','Pollo con brócoli',1720),(27,4,6,'Yogur con muesli','Salmón con patata','Merluza al vapor',1920),(28,4,7,'Claras con tostada','Lentejas con pimiento','Pollo al horno',1860),(29,5,1,'Avena con nueces, plátano y miel','Pasta integral con ternera y tomate','Pollo a la plancha con arroz basmati',2650),(30,5,2,'Huevos revueltos con pavo y tostadas integrales','Arroz con pollo y pimiento','Ternera a la plancha con batata',2700),(31,5,3,'Batido proteínico con plátano','Pasta con pollo y champiñones','Salmón con patata al horno',2580),(32,5,4,'Avena con almendras y arándanos','Arroz con atún y guisantes','Ternera con brócoli al vapor',2620),(33,5,5,'Huevos revueltos con pavo','Pasta con gambas y verduras','Pollo con arroz integral',2680),(34,5,6,'Tostadas con aguacate y huevo poché','Arroz con ternera y champiñones','Salmón con patata asada',2900),(35,5,7,'Yogur griego con granola y frutas del bosque','Pasta con pollo y pesto','Huevos al horno con verduras',2720),(36,6,1,'Avena con leche entera, miel y plátano','Lentejas guisadas con arroz integral','Pechuga de pollo con patata al horno',2600),(37,6,2,'Yogur griego con granola y nueces','Garbanzos salteados con arroz','Salmón con batata y brócoli',2550),(38,6,3,'Batido de leche con avena fitness y cacao','Lentejas con verduras y arroz','Atún con arroz basmati y espinacas',2620),(39,6,4,'Huevos con tostadas de centeno y aguacate','Soja texturizada con arroz y pimiento','Pechuga de pollo con brócoli al vapor',2580),(40,6,5,'Avena con plátano y mantequilla de cacahuete','Garbanzos con pasta integral','Atún con patata asada',2640),(41,6,6,'Yogur griego con nueces y miel','Arroz con lentejas y zanahoria','Pollo asado con batata',2700),(42,6,7,'Batido de proteína vegetal con plátano','Pasta con garbanzos y tomate natural','Salmón con arroz integral',2660),(43,7,1,'Claras de huevo revueltas con tostada de centeno','Merluza a la plancha con ensalada verde','Bacalao al horno con brócoli al vapor',1720),(44,7,2,'Kéfir natural desnatado','Atún con arroz integral y judías verdes','Merluza con espinacas salteadas',1680),(45,7,3,'Leche desnatada con copos de avena','Bacalao al vapor con pimiento asado','Tofu salteado con espinacas',1740),(46,7,4,'Tortilla de claras con tomate','Atún con ensalada de lechuga y pepino','Merluza al horno con limón',1700),(47,7,5,'Kéfir con almendras troceadas','Bacalao con brócoli y zanahoria','Tofu con arroz integral y soja',1760),(48,7,6,'Leche desnatada con cereales sin azúcar','Merluza con patata hervida y espárragos','Atún con ensalada de rúcula',1820),(49,7,7,'Claras de huevo con rodajas de naranja','Bacalao al vapor con espinacas','Merluza al papillote con limón',1750);
UNLOCK TABLES;

DROP TABLE IF EXISTS `ejercicio`;
CREATE TABLE `ejercicio` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `rutina_id` int(11) NOT NULL,
  `nombre` varchar(150) NOT NULL,
  `grupo_muscular` varchar(100) DEFAULT NULL,
  `series` int(11) DEFAULT NULL,
  `repeticiones` int(11) DEFAULT NULL,
  `descanso_seg` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `rutina_id` (`rutina_id`),
  CONSTRAINT `ejercicio_ibfk_1` FOREIGN KEY (`rutina_id`) REFERENCES `rutina` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=70 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

LOCK TABLES `ejercicio` WRITE;
INSERT INTO `ejercicio` VALUES (1,1,'Press de banca','Pecho',4,10,90),(2,1,'Press inclinado','Pecho',4,10,90),(3,1,'Aperturas con mancuernas','Pecho',3,10,90),(4,1,'Remo con barra','Espalda',4,10,90),(5,1,'Remo con mancuerna','Espalda',4,10,90),(6,1,'Dominadas asistidas','Espalda',3,10,90),(7,1,'Laterales','Hombro',3,12,90),(8,1,'Curl con mancuernas','Biceps',3,10,90),(9,1,'Extensión en polea','Triceps',3,10,90),(10,1,'Sentadilla','Cuadriceps',4,10,90),(11,1,'Prensa','Cuadriceps',4,10,90),(12,1,'Peso muerto rumano','Isquiotibiales',4,10,90),(13,1,'Hip thrust','Gluteo',4,10,90),(14,1,'Gemelos de pie','Gemelos',4,12,60),(15,2,'Press máquina','Pecho',3,12,60),(16,2,'Aperturas máquina','Pecho',3,12,60),(17,2,'Jalón al pecho','Espalda',3,12,60),(18,2,'Remo en máquina','Espalda',3,12,60),(19,2,'Press militar','Hombro',4,10,60),(20,2,'Curl con barra','Biceps',3,12,60),(21,2,'Fondos en banco','Triceps',3,12,60),(22,2,'Extensiones en máquina','Cuadriceps',3,12,60),(23,2,'Sentadilla guiada','Cuadriceps',3,12,60),(24,2,'Curl femoral','Isquiotibiales',3,12,60),(25,2,'Puente de glúteo','Gluteo',3,12,60),(26,2,'Abducciones con banda','Gluteo',3,15,60),(27,2,'Gemelos sentado','Gemelos',3,15,60),(28,3,'Fondos en paralelas','Pecho',3,NULL,120),(29,3,'Press de banca pesado','Pecho',5,5,120),(30,3,'Flexiones explosivas','Pecho',3,8,120),(31,3,'Dominadas estrictas','Espalda',3,NULL,120),(32,3,'Peso muerto','Espalda',5,5,120),(33,3,'Remo Pendlay','Espalda',4,8,120),(34,3,'Arnold press','Hombro',4,10,120),(35,3,'Curl concentrado','Biceps',3,8,120),(36,3,'Press francés','Triceps',3,8,120),(37,3,'Sentadilla pesada','Cuadriceps',5,5,120),(38,3,'Hack squat','Cuadriceps',4,8,120),(39,3,'Bulgarian split squat','Cuadriceps',3,8,120),(40,3,'Nordic curl','Isquiotibiales',3,6,120),(41,3,'Hip thrust pesado','Gluteo',5,5,120),(42,3,'Peso muerto sumo','Gluteo',5,5,120),(43,3,'Saltos de gemelo','Gemelos',3,20,90),(44,4,'Flexiones inclinadas','Pecho',3,12,60),(45,4,'Press máquina','Pecho',3,12,60),(46,4,'Pullover en polea','Espalda',3,12,60),(47,4,'Jalón al pecho','Espalda',3,12,60),(48,4,'Press militar','Hombro',4,10,60),(49,4,'Curl con barra','Biceps',3,12,60),(50,4,'Fondos en banco','Triceps',3,12,60),(51,4,'Sentadilla guiada','Cuadriceps',3,12,60),(52,4,'Prensa ligera','Cuadriceps',3,12,60),(53,4,'Curl femoral','Isquiotibiales',3,12,60),(54,4,'Puente de glúteo','Gluteo',3,12,60),(55,4,'Patada en polea','Gluteo',3,12,60),(56,4,'Gemelos sentado','Gemelos',3,15,60),(57,5,'Press máquina','Pecho',3,12,60),(58,5,'Aperturas máquina','Pecho',3,12,60),(59,5,'Jalón al pecho','Espalda',3,12,60),(60,5,'Pullover en polea','Espalda',3,12,60),(61,5,'Press militar','Hombro',4,10,60),(62,5,'Curl con barra','Biceps',3,12,60),(63,5,'Fondos en banco','Triceps',3,12,60),(64,5,'Prensa ligera','Cuadriceps',3,12,60),(65,5,'Extensiones en máquina','Cuadriceps',3,12,60),(66,5,'Curl femoral','Isquiotibiales',3,12,60),(67,5,'Abducciones con banda','Gluteo',3,15,60),(68,5,'Patada en polea','Gluteo',3,12,60),(69,5,'Gemelos sentado','Gemelos',3,15,60);
UNLOCK TABLES;

DROP TABLE IF EXISTS `gimnasio_cercano`;
CREATE TABLE `gimnasio_cercano` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `usuario_id` int(11) NOT NULL,
  `nombre` varchar(150) NOT NULL,
  `direccion` varchar(255) DEFAULT NULL,
  `distancia_km` float DEFAULT NULL,
  `valoracion` float DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `usuario_id` (`usuario_id`),
  CONSTRAINT `gimnasio_cercano_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id`) ON DELETE CASCADE,
  CONSTRAINT `chk_valoracion` CHECK (`valoracion` between 0 and 5)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

LOCK TABLES `gimnasio_cercano` WRITE;
INSERT INTO `gimnasio_cercano` VALUES (1,1,'FitGym Madrid Centro','Calle Gran Vía, 45, Madrid',1.2,4.5),(2,1,'SportZone Sol','Calle del Arenal, 10, Madrid',2.1,4.2),(3,1,'Basic-Fit Alcalá','Calle de Alcalá, 88, Madrid',3.4,3.8),(4,2,'Holmes Place Gràcia','Passeig de Gràcia, 72, Barcelona',0.8,4.7),(5,2,'DIR Diagonal','Avinguda Diagonal, 555, Barcelona',1.5,4.3),(6,2,'Synergym Eixample','Carrer de Entença, 24, Barcelona',2.8,4),(7,3,'Supera Valencia Puerto','Avenida del Puerto, 15, Valencia',0.5,4.8),(8,3,'FitGym Colón','Calle de Colón, 30, Valencia',1.8,4.1),(9,3,'FitnessFirst Gran Via','Gran Via Marqués del Turia, 52, Valencia',2.3,3.9),(10,4,'McFIT Buhaira','Avenida de la Buhaira, 20, Sevilla',1,4.4),(11,4,'Synergym Nervión','Calle Luis Montoto, 55, Sevilla',2.2,4.2),(12,4,'Sport Zone Eduardo Dato','Avenida Eduardo Dato, 45, Sevilla',3.1,3.7),(13,5,'FitGym Larios','Calle Marqués de Larios, 8, Málaga',0.7,4.6),(14,5,'McFIT Andalucía','Avenida de Andalucía, 20, Málaga',1.9,4),(15,5,'Supera Málaga Centro','Calle Armengual de la Mota, 18, Málaga',2.7,3.8);
UNLOCK TABLES;

DROP TABLE IF EXISTS `menu_semanal`;
CREATE TABLE `menu_semanal` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `usuario_id` int(11) NOT NULL,
  `semana_inicio` date NOT NULL,
  `semana_fin` date NOT NULL,
  `generado_en` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `usuario_id` (`usuario_id`),
  CONSTRAINT `menu_semanal_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

LOCK TABLES `menu_semanal` WRITE;
INSERT INTO `menu_semanal` VALUES (1,1,'2026-04-21','2026-04-27','2026-04-20 20:00:00'),(2,1,'2026-04-14','2026-04-20','2026-04-13 19:30:00'),(3,2,'2026-04-21','2026-04-27','2026-04-20 21:00:00'),(4,2,'2026-04-14','2026-04-20','2026-04-13 20:00:00'),(5,3,'2026-04-21','2026-04-27','2026-04-20 19:00:00'),(6,4,'2026-04-21','2026-04-27','2026-04-21 08:00:00'),(7,5,'2026-04-21','2026-04-27','2026-04-21 09:00:00'),(8,6,'2026-05-04','2026-05-10','2026-05-11 00:28:39');
UNLOCK TABLES;

DROP TABLE IF EXISTS `preferencia_alimenticia`;
CREATE TABLE `preferencia_alimenticia` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `usuario_id` int(11) NOT NULL,
  `tipo` varchar(100) NOT NULL,
  `descripcion` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `usuario_id` (`usuario_id`),
  CONSTRAINT `preferencia_alimenticia_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

LOCK TABLES `preferencia_alimenticia` WRITE;
INSERT INTO `preferencia_alimenticia` VALUES (1,1,'Carnes','Pollo'),(2,1,'Carnes','Ternera'),(3,1,'Carnes','Pavo'),(4,1,'Pescado y Marisco','Atún'),(5,1,'Pescado y Marisco','Salmón'),(6,1,'Huevos','Huevos'),(7,1,'Lácteos','Proteína'),(8,1,'Lácteos','Requesón'),(9,1,'Fitness Extra','Arroz'),(10,1,'Fitness Extra','Avena fitness'),(11,1,'Fitness Extra','Creatina'),(12,1,'Fitness Extra','Batido'),(13,2,'Pescado y Marisco','Salmón'),(14,2,'Pescado y Marisco','Merluza'),(15,2,'Lácteos','Yogur'),(16,2,'Lácteos','Kéfir'),(17,2,'Vegetal / Vegano','Espinacas'),(18,2,'Vegetal / Vegano','Brócoli'),(19,2,'Vegetal / Vegano','Lentejas'),(20,2,'Fitness Extra','Batata'),(21,2,'Fitness Extra','Plátano'),(22,2,'Fitness Extra','Claras de huevo'),(23,3,'Carnes','Ternera'),(24,3,'Carnes','Pavo'),(25,3,'Huevos','Huevos'),(26,3,'Gluten','Pasta'),(27,3,'Gluten','Avena'),(28,3,'Frutos Secos','Almendras'),(29,3,'Frutos Secos','Nueces'),(30,3,'Fitness Extra','Arroz'),(31,3,'Fitness Extra','Plátano'),(32,3,'Fitness Extra','Batido'),(33,4,'Carnes','Pechuga de pollo'),(34,4,'Huevos','Huevos'),(35,4,'Lácteos','Queso'),(36,4,'Lácteos','Yogur'),(37,4,'Lácteos','Leche'),(38,4,'Vegetal / Vegano','Lentejas'),(39,4,'Vegetal / Vegano','Garbanzos'),(40,4,'Vegetal / Vegano','Soja'),(41,4,'Gluten','Avena'),(42,4,'Fitness Extra','Avena fitness'),(43,4,'Fitness Extra','Creatina'),(44,4,'Fitness Extra','Plátano'),(45,5,'Pescado y Marisco','Merluza'),(46,5,'Pescado y Marisco','Bacalao'),(47,5,'Pescado y Marisco','Atún'),(48,5,'Lácteos','Leche desnatada'),(49,5,'Lácteos','Kéfir'),(50,5,'Vegetal / Vegano','Tofu'),(51,5,'Vegetal / Vegano','Espinacas'),(52,5,'Frutos Secos','Almendras'),(53,5,'Fitness Extra','Claras de huevo');
UNLOCK TABLES;

DROP TABLE IF EXISTS `racha`;
CREATE TABLE `racha` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `usuario_id` int(11) NOT NULL,
  `dias_consecutivos` int(11) NOT NULL DEFAULT 0,
  `ultima_actividad` date DEFAULT NULL,
  `racha_maxima` int(11) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `usuario_id` (`usuario_id`),
  CONSTRAINT `racha_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

LOCK TABLES `racha` WRITE;
INSERT INTO `racha` VALUES (1,1,15,'2026-04-28',20),(2,2,7,'2026-04-29',12),(3,3,3,'2026-04-29',30),(4,4,22,'2026-04-28',22),(5,5,1,'2026-04-29',5);
UNLOCK TABLES;

DROP TABLE IF EXISTS `registro_peso`;
CREATE TABLE `registro_peso` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `usuario_id` int(11) NOT NULL,
  `peso_kg` float NOT NULL,
  `fecha` date NOT NULL,
  `nota` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `usuario_id` (`usuario_id`),
  CONSTRAINT `registro_peso_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

LOCK TABLES `registro_peso` WRITE;
INSERT INTO `registro_peso` VALUES (1,1,78,'2026-01-10','Peso inicial al registrarse'),(2,1,78.5,'2026-01-25',NULL),(3,1,79,'2026-02-08','Buena progresión en el gym'),(4,1,79.5,'2026-02-22',NULL),(5,1,80,'2026-03-07','Objetivo del primer mes logrado'),(6,1,80,'2026-04-01',NULL),(7,2,70,'2026-01-15','Peso inicial al registrarse'),(8,2,69,'2026-01-29','Buen ritmo de pérdida'),(9,2,68,'2026-02-12',NULL),(10,2,67,'2026-02-26','Semana difícil, pero en racha'),(11,2,66,'2026-03-11',NULL),(12,2,65,'2026-03-25','Objetivo de 5 kg logrado en 10 semanas'),(13,3,74.5,'2026-02-01','Peso inicial al registrarse'),(14,3,74.8,'2026-02-15',NULL),(15,3,75,'2026-03-01',NULL),(16,3,75.2,'2026-03-15','Ganando algo de masa muscular'),(17,3,75,'2026-04-01',NULL),(18,4,56,'2026-02-10','Peso inicial al registrarse'),(19,4,56.5,'2026-02-24',NULL),(20,4,57,'2026-03-10','Progresando bien con la dieta'),(21,4,57.5,'2026-03-24',NULL),(22,4,58,'2026-04-07',NULL),(23,5,100,'2026-03-05','Peso inicial al registrarse'),(24,5,98.5,'2026-03-19',NULL),(25,5,97,'2026-04-02','Costando más de lo esperado'),(26,5,95.5,'2026-04-16',NULL),(27,5,95,'2026-04-25','Manteniéndose en la línea');
UNLOCK TABLES;

DROP TABLE IF EXISTS `rutina`;
CREATE TABLE `rutina` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `usuario_id` int(11) NOT NULL,
  `nombre` varchar(150) NOT NULL,
  `nivel` varchar(50) DEFAULT NULL,
  `objetivo` varchar(100) DEFAULT NULL,
  `activa` tinyint(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`),
  KEY `usuario_id` (`usuario_id`),
  CONSTRAINT `rutina_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

LOCK TABLES `rutina` WRITE;
INSERT INTO `rutina` VALUES (1,1,'Hipertrofia Upper / Lower','Intermedio','Ganancia Muscular',1),(2,2,'Full Body Quema Grasa','Fácil','Reducción de Grasa',1),(3,3,'Fuerza + Cardio Avanzado','Difícil','Entrenamiento Deportivo',1),(4,4,'Iniciación a la Fuerza','Fácil','Ganancia Muscular',1),(5,5,'Cardio y Tonificación','Fácil','Reducción de Grasa',1),(6,6,'Mi Rutina Personal',NULL,NULL,1),(7,6,'Registro Diario',NULL,NULL,1);
UNLOCK TABLES;

DROP TABLE IF EXISTS `usuario`;
CREATE TABLE `usuario` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `email` varchar(150) NOT NULL,
  `password` varchar(255) NOT NULL,
  `fecha_registro` date NOT NULL,
  `peso_actual` float DEFAULT NULL,
  `altura_cm` float DEFAULT NULL,
  `imc_actual` float DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

LOCK TABLES `usuario` WRITE;
INSERT INTO `usuario` VALUES (1,'Carlos López','carlos.lopez@fitme.com','hashed_password_1','2026-01-10',80,180,24.69),(2,'Ana García','ana.garcia@fitme.com','hashed_password_2','2026-01-15',65,165,23.88),(3,'Miguel Torres','miguel.torres@fitme.com','hashed_password_3','2026-02-01',75,178,23.67),(4,'Laura Martínez','laura.martinez@fitme.com','hashed_password_4','2026-02-10',58,162,22.1),(5,'Pedro Sánchez','pedro.sanchez@fitme.com','hashed_password_5','2026-03-05',95,175,31.02),(6,'isma','ismael@gmail.com','123456','2026-05-11',64,174,21.1389),(7,'pepe','pepe45@gmail.com','123456','2026-05-11',80,170,27.6817);
UNLOCK TABLES;
