# SistemasCarros
Sistema de Troca de Carros 

CREATE TABLE `Carros` (
  `IdDono` int(11) NOT NULL,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `modelo` varchar(45) NOT NULL,
  `fabricante` varchar(45) NOT NULL,
  `cor` varchar(45) NOT NULL,
  `ano` date NOT NULL,
  `NomeDono` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `fk_Carros_1_idx` (`IdDono`),
  CONSTRAINT `fk_Carros_1` FOREIGN KEY (`IdDono`) REFERENCES `Usuarios` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1

CREATE TABLE `Usuarios` (
  `Nome` varchar(45) NOT NULL,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `login` varchar(45) NOT NULL,
  `senha` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `login_UNIQUE` (`login`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=latin1
