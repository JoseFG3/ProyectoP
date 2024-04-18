-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 18-04-2024 a las 14:08:17
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `getbacktowork`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `combate`
--

CREATE TABLE `combate` (
  `ID_COMBATE` int(11) NOT NULL,
  `FECHA_HORA` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `ID_ENTRENADOR` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish2_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `entrenador`
--

CREATE TABLE `entrenador` (
  `ID_ENTRENADOR` int(11) NOT NULL,
  `NOM_ENTRENADOR` varchar(20) NOT NULL,
  `PASS` varchar(20) DEFAULT NULL,
  `POKEDOLLARS` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish2_ci;

--
-- Volcado de datos para la tabla `entrenador`
--

INSERT INTO `entrenador` (`ID_ENTRENADOR`, `NOM_ENTRENADOR`, `PASS`, `POKEDOLLARS`) VALUES
(1, 'Lebron James', NULL, 1500),
(2, 'Barack Obama', NULL, 2000),
(3, 'Samuel Umtiti', NULL, 1750),
(4, 'Usain Bolt', NULL, 1500),
(5, 'Rosa Parks', NULL, 1800),
(6, 'Bob Marley', NULL, 1600),
(7, 'Martin L.K.', NULL, 2200),
(8, 'Pele', NULL, 1900),
(9, 'George Washington', NULL, 1850),
(10, 'Will Smith', NULL, 1500);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `mochila`
--

CREATE TABLE `mochila` (
  `ID_ENTRENADOR` int(11) NOT NULL,
  `ID_OBJETO` int(11) NOT NULL,
  `NUM_OBJETO` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish2_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `movimientos`
--

CREATE TABLE `movimientos` (
  `ID_MOVIMIENTO` int(11) NOT NULL,
  `NOM_MOVIMIENTO` varchar(20) NOT NULL,
  `POTENCIA` int(11) DEFAULT NULL,
  `TIPO` varchar(20) NOT NULL,
  `ESTADO` varchar(20) DEFAULT NULL,
  `QUITA` int(11) DEFAULT NULL,
  `TURNOS` int(11) DEFAULT NULL,
  `MEJORA` varchar(20) DEFAULT NULL,
  `CANT_MEJORA` int(11) DEFAULT NULL,
  `NIVEL_APRENDIZAJE` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish2_ci;

--

-- Volcado de datos para la tabla `movimientos`
--

INSERT INTO `movimientos` (`ID_MOVIMIENTO`, `NOM_MOVIMIENTO`, `POTENCIA`, `TIPO`, `ESTADO`, `QUITA`, `TURNOS`, `MEJORA`, `CANT_MEJORA`, `NIVEL_APRENDIZAJE`) VALUES
(1, 'Burbuja', 20, 'Agua', NULL, NULL, NULL, NULL, NULL, 1),
(2, 'Cascada', 80, 'Agua', NULL, NULL, NULL, NULL, NULL, 2),
(3, 'Hidrobomba', 120, 'Agua', NULL, NULL, NULL, NULL, NULL, 3),
(4, 'Refugio', NULL, 'Agua', NULL, NULL, 5, 'Defensa', 1, 4),
(5, 'Chupavida', 50, 'Bicho', NULL, NULL, NULL, NULL, NULL, 1),
(6, 'Disparo demora', NULL, 'Bicho', NULL, 1, 1, NULL, NULL, 2),
(7, 'Doble ataque', 25, 'Bicho', NULL, NULL, NULL, NULL, NULL, 3),
(8, 'Pin misil', 25, 'Bicho', NULL, NULL, NULL, NULL, NULL, 4),
(9, 'Furia dragón', NULL, 'Dragon', NULL, 40, 1, NULL, NULL, 1),
(10, 'Impactrueno', 40, 'Eléctrico', NULL, NULL, NULL, NULL, NULL, 1),
(11, 'Puño Hielo', NULL, 'Hielo', 'Helado', 6, NULL, NULL, NULL, 1),
(12, 'Golpe Cuerpo', NULL, 'Normal', 'Paralizado', 25, 0, NULL, NULL, 6),
(13, 'Atadura', NULL, 'Normal', 'Atrapado', NULL, 0, NULL, NULL, 6),
(14, 'Picotazo Venenoso', NULL, 'Veneno', 'Envenenado', 13, 0, NULL, NULL, 6),
(15, 'Gruñido', NULL, 'Normal', 'Centro de atención', NULL, 0, NULL, NULL, 6),
(16, 'Canto', NULL, 'Normal', 'Dormido', NULL, 3, NULL, NULL, 6),
(17, 'Supersónico', NULL, 'Normal', 'Confuso', NULL, 0, NULL, NULL, 6),
(18, 'Rayo Confuso', NULL, 'Fantasma', 'Confuso', NULL, 0, NULL, NULL, 6),
(19, 'Ascuas', NULL, 'Fuego', 'Quemado', 6, 0, NULL, NULL, 3),
(20, 'Rayo Hielo', NULL, 'Hielo', 'Congelado', NULL, 0, NULL, NULL, 6),
(21, 'Ventisca', NULL, 'Hielo', 'Helado', NULL, 0, NULL, NULL, 6),
(22, 'Contador', NULL, 'Lucha', 'Cuenta atrás', NULL, 3, NULL, NULL, 6),
(23, 'Drenadoras', NULL, 'Planta', 'Drenadoras', 13, 0, NULL, NULL, 6),
(24, 'Atracción', NULL, 'Normal', 'Enamorado', NULL, 0, NULL, NULL, 6),
(25, 'Somnífero', NULL, 'Planta', 'Dormido', NULL, 0, NULL, NULL, 6),
(26, 'Lanzallamas', 90, 'Fuego', NULL, NULL, NULL, NULL, NULL, 2),
(27, 'Onda Trueno', NULL, 'Eléctrico', 'Paralizado', 25, 0, NULL, NULL, 6),
(28, 'Rayo', NULL, 'Eléctrico', 'Paralizado', 25, 0, NULL, NULL, 6),
(29, 'Tóxico', NULL, 'Veneno', 'Gravemente envenenad', NULL, 0, NULL, NULL, 6),
(30, 'Confusión', NULL, 'Psíquico', 'Confuso', NULL, 0, NULL, NULL, 6),
(31, 'Manto Espejo', NULL, 'Psíquico', 'Cuenta atrás', NULL, 3, NULL, NULL, 6),
(32, 'Hipnosis', NULL, 'Psíquico', 'Dormido', NULL, 0, NULL, NULL, 6),
(33, 'Polución', NULL, 'Veneno', 'Envenenado', 13, 0, NULL, NULL, 6),
(34, 'Llamarada', 110, 'Fuego', NULL, NULL, NULL, NULL, NULL, 7),
(35, 'Deslumbrar', NULL, 'Normal', 'Centro de atención', NULL, 0, NULL, NULL, 6),
(36, 'Gas Venenoso', NULL, 'Veneno', 'Envenenado', 13, 0, NULL, NULL, 6),
(37, 'Chupavidas', NULL, 'Bicho', 'Drenadoras', 13, 0, NULL, NULL, 6),
(38, 'Beso Amoroso', NULL, 'Normal', 'Dormido', NULL, 0, NULL, NULL, 6),
(39, 'Descanso', NULL, 'Psíquico', 'Somnoliento', NULL, 0, NULL, NULL, 6),
(40, 'Maldición', NULL, 'Fantasma', 'Maldito', 25, 0, NULL, NULL, 6),
(41, 'Cara Susto', NULL, 'Normal', 'Amedrentado', NULL, 0, NULL, NULL, 6),
(42, 'Contoneo', NULL, 'Normal', 'Confuso', NULL, 0, NULL, NULL, 6),
(43, 'Pistola Agua', 40, 'Agua', NULL, NULL, NULL, NULL, NULL, 3),
(44, 'Trueno', 120, 'Electrico', NULL, NULL, NULL, NULL, NULL, 5),
(45, 'Puño Fuego', 75, 'Fuego', NULL, NULL, NULL, NULL, NULL, 5),
(46, 'Giro Fuego', NULL, 'Fuego', 'Quemado', 6, 0, NULL, NULL, 5),
(47, 'Lengüetazo', 30, 'Fantasma', NULL, NULL, NULL, NULL, NULL, 4),
(48, 'Rayo Aurora', 65, 'Hielo', NULL, NULL, NULL, NULL, NULL, 4),
(49, 'Patada Salto', 70, 'Lucha', NULL, NULL, NULL, NULL, NULL, 3),
(50, 'Patada Salto Alta', 85, 'Lucha', NULL, NULL, NULL, NULL, NULL, 5),
(51, 'Patada Baja', NULL, 'Lucha', 'Amedrentado', NULL, 0, NULL, NULL, 6),
(52, 'Arañazo', 40, 'Normal', NULL, NULL, NULL, NULL, NULL, 1),
(53, 'Latigo Cepa', 35, 'Planta', NULL, NULL, NULL, NULL, NULL, 2),
(54, 'Hoja Afilada', 55, 'Planta', NULL, NULL, NULL, NULL, NULL, 4),
(55, 'Rayo Solar', 120, 'Planta', NULL, NULL, NULL, NULL, NULL, 8),
(56, 'Psiquico', 90, 'Psiquico', NULL, NULL, NULL, NULL, NULL, 5),
(57, 'Acido', 40, 'Veneno', NULL, NULL, NULL, NULL, NULL, 4);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `movimientos_pokemon`
--

CREATE TABLE `movimientos_pokemon` (
  `ID_MOVIMIENTO` int(11) NOT NULL,
  `ID_POKEMON` int(11) NOT NULL,
  `ACTIVO` char(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish2_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `objeto`
--

CREATE TABLE `objeto` (
  `ID_OBJETO` int(11) NOT NULL,
  `NOMBRE` varchar(20) NOT NULL,
  `ATAQUE` int(11) DEFAULT NULL,
  `AT_ESPECIAL` int(11) DEFAULT NULL,
  `DEFENSA` int(11) DEFAULT NULL,
  `DEF_ESPECIAL` int(11) DEFAULT NULL,
  `VELOCIDAD` int(11) DEFAULT NULL,
  `PRECIO` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish2_ci;

--
-- Volcado de datos para la tabla `objeto`
--

INSERT INTO `objeto` (`ID_OBJETO`, `NOMBRE`, `ATAQUE`, `AT_ESPECIAL`, `DEFENSA`, `DEF_ESPECIAL`, `VELOCIDAD`, `PRECIO`) VALUES
(1, 'Pesa', 20, NULL, 20, NULL, -20, 500),
(2, 'Pluma', NULL, NULL, -20, -20, 30, 300),
(3, 'Chaleco', -15, NULL, 20, 20, -15, 400),
(4, 'Baston', 20, 20, NULL, NULL, -15, 450),
(5, 'Pilas', 20, NULL, NULL, -30, 15, 350),
(6, 'Anillo Unico', 100, 100, NULL, NULL, NULL, 2000),
(7, 'Pokeball', NULL, NULL, NULL, NULL, NULL, 50);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `pokedex`
--

CREATE TABLE `pokedex` (
  `NUM_POKEDEX` int(11) NOT NULL,
  `NOM_POKEMON` varchar(30) NOT NULL,
  `TIPO1` varchar(20) NOT NULL,
  `TIPO2` varchar(20) DEFAULT NULL,
  `IMAGEN` varchar(100) NOT NULL,
  `SONIDO` varchar(100) NOT NULL,
  `NIVEL_EVOLUCION` int(11) DEFAULT NULL,
  `NUM_POKEDEX_EVO` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish2_ci;

--
-- Volcado de datos para la tabla `pokedex`
--

INSERT INTO `pokedex` (`NUM_POKEDEX`, `NOM_POKEMON`, `TIPO1`, `TIPO2`, `IMAGEN`, `SONIDO`, `NIVEL_EVOLUCION`, `NUM_POKEDEX_EVO`) VALUES
(1, 'Bulbasaur', 'planta', 'veneno', '', '', 16, 2),
(2, 'Ivysaur', 'planta', 'veneno', '', '', 32, 3),
(3, 'Venasaur', 'planta', 'veneno', '', '', NULL, NULL),
(4, 'Charmander', 'fuego', '', '', '', 16, 5),
(5, 'Charmaleon', 'fuego', '', '', '', 36, 6),
(6, 'Charizard', 'fuego', 'volador', '', '', NULL, NULL),
(7, 'Squirtle', 'Agua', '', '', '', 16, 8),
(8, 'Wartortle', 'agua', '', '', '', 36, 9),
(9, 'Blastoise', 'agua', '', '', '', NULL, NULL),
(10, 'Caterpie', 'bicho', '', '', '', 7, 11),
(11, 'Metapod', 'bicho', '', '', '', 10, 12),
(12, 'Butterfree', 'bicho', 'volador', '', '', NULL, NULL),
(13, 'Weedle', 'bicho', 'veneno', '', '', 7, 14),
(14, 'Kakuna', 'bicho', 'veneno', '', '', 10, 15),
(15, 'Beedrill', 'bicho', 'veneno', '', '', NULL, NULL),
(16, 'Pidgey', 'normal', 'volador', '', '', 18, 17),
(17, 'Pidgeotto', 'normal', 'volador', '', '', 36, 18),
(18, 'Pidgeot', 'normal', 'volador', '', '', NULL, NULL),
(19, 'Rattata', 'normal', '', '', '', 20, 20),
(20, 'Raticate', 'normal', '', '', '', NULL, NULL),
(21, 'Spearow', 'normal', 'volador', '', '', 20, 22),
(22, 'Fearow', 'normal', 'volador', '', '', NULL, NULL),
(23, 'Ekans', 'veneno', '', '', '', 22, 24),
(24, 'Arbok', 'veneno', '', '', '', NULL, NULL),
(25, 'Pikachu', 'electrico', '', '', '', 28, 26),
(26, 'Raichu', 'electrico', '', '', '', NULL, NULL),
(27, 'Sandshrew', 'tierra', '', '', '', 22, 28),
(28, 'Sandslash', 'tierra', '', '', '', NULL, NULL),
(29, 'Nidoran M', 'veneno', '', '', '', 16, 30),
(30, 'Nidorina', 'veneno', '', '', '', 26, 31),
(31, 'Nidoqueen', 'veneno', 'tierra', '', '', NULL, NULL),
(32, 'Nidoran F', 'veneno', '', '', '', 16, 30),
(33, 'Nidorino', 'veneno', 'tierra', '', '', 26, 34),
(34, 'Nidoking', 'veneno', 'tierra', '', '', NULL, NULL),
(50, 'Diglett', 'tierra', '', '', '', 26, 51),
(51, 'Dugtrio', 'tierra', '', '', '', NULL, NULL),
(56, 'Mankey', 'lucha', '', '', '', 28, 57),
(57, 'Primeape', 'lucha', '', '', '', NULL, NULL),
(60, 'Poliwag', 'agua', '', '', '', 25, 61),
(61, 'Poliwhirl', 'agua', '', '', '', 32, 62),
(62, 'Poliwrath', 'agua', 'lucha', '', '', NULL, NULL),
(63, 'Abra', 'psiquico', '', '', '', 16, 64),
(64, 'Kadabra', 'psiquico', '', '', '', 25, 65),
(65, 'Alakazam', 'psiquico', '', '', '', NULL, NULL),
(66, 'Machop', 'lucha', '', '', '', 28, 67),
(67, 'Machoke', 'lucha', '', '', '', 32, 68),
(68, 'Machamp', 'lucha', '', '', '', NULL, NULL),
(74, 'Geodude', 'roca', 'tierra', '', '', 25, 75),
(75, 'Graveler', 'roca', 'tierra', '', '', 30, 76),
(76, 'Golem', 'roca', 'tierra', '', '', NULL, NULL),
(79, 'Slowpoke', 'agua', 'psiquico', '', '', 37, 80),
(80, 'Slowbro', 'agua', 'psiquico', '', '', NULL, NULL),
(88, 'Grimer', 'veneno', '', '', '', 38, 89),
(89, 'Muk', 'veneno', '', '', '', NULL, NULL),
(92, 'Gastly', 'fantasma', 'veneno', '', '', 25, 93),
(93, 'Haunter', 'fantasma', 'veneno', '', '', 30, 94),
(94, 'Gengar', 'fantasma', 'veneno', '', '', NULL, NULL),
(95, 'Onix', 'roca', 'tierra', '', '', NULL, NULL),
(129, 'Magikarp', 'agua', '', '', '', 20, 130),
(130, 'Gyarados', 'agua', 'volador', '', '', NULL, NULL),
(144, 'Articuno', 'hielo', 'volador', '', '', NULL, NULL),
(145, 'Zapdos', 'electrico', 'volador', '', '', NULL, NULL),
(146, 'Moltres', 'fuego', 'volador', '', '', NULL, NULL),
(147, 'Dratini', 'dragon', 'volador', '', '', 30, 148),
(148, 'Dragonair', 'dragon', '', '', '', 55, 149),
(149, 'Dragonite', 'dragon', 'volador', '', '', NULL, NULL),
(150, 'Mewtwo', 'psiquico', '', '', '', NULL, NULL),
(151, 'Mew', 'psiquico', '', '', '', NULL, NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `pokemon`
--

CREATE TABLE `pokemon` (
  `ID_POKEMON` int(11) NOT NULL,
  `MOTE` varchar(30) DEFAULT NULL,
  `CAJA` int(11) NOT NULL,
  `ATAQUE` int(11) NOT NULL,
  `AT_ESPECIAL` int(11) NOT NULL,
  `DEFENSA` int(11) NOT NULL,
  `DEF_ESPECIAL` int(11) NOT NULL,
  `VELOCIDAD` int(11) NOT NULL,
  `NIVEL` int(11) NOT NULL,
  `FERTILIDAD` int(11) NOT NULL,
  `SEXO` char(1) NOT NULL,
  `ESTADO` varchar(20) NOT NULL,
  `EXPERIENCIA` int(11) NOT NULL,
  `VITALIDAD` int(11) NOT NULL,
  `NUM_POKEDEX` int(11) DEFAULT NULL,
  `ID_ENTRENADOR` int(11) DEFAULT NULL,
  `ID_OBJETO` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish2_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `turno`
--

CREATE TABLE `turno` (
  `ID_TURNO` int(11) NOT NULL,
  `ACCION_ENTRENADOR` varchar(150) NOT NULL,
  `ACCION_RIVAL` varchar(150) NOT NULL,
  `ID_COMBATE` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish2_ci;

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `combate`
--
ALTER TABLE `combate`
  ADD PRIMARY KEY (`ID_COMBATE`),
  ADD KEY `ID_ENTRENADOR` (`ID_ENTRENADOR`);

--
-- Indices de la tabla `entrenador`
--
ALTER TABLE `entrenador`
  ADD PRIMARY KEY (`ID_ENTRENADOR`);

--
-- Indices de la tabla `mochila`
--
ALTER TABLE `mochila`
  ADD PRIMARY KEY (`ID_ENTRENADOR`,`ID_OBJETO`),
  ADD KEY `ID_OBJETO` (`ID_OBJETO`);

--
-- Indices de la tabla `movimientos`
--
ALTER TABLE `movimientos`
  ADD PRIMARY KEY (`ID_MOVIMIENTO`);

--
-- Indices de la tabla `movimientos_pokemon`
--
ALTER TABLE `movimientos_pokemon`
  ADD PRIMARY KEY (`ID_MOVIMIENTO`,`ID_POKEMON`),
  ADD KEY `ID_POKEMON` (`ID_POKEMON`);

--
-- Indices de la tabla `objeto`
--
ALTER TABLE `objeto`
  ADD PRIMARY KEY (`ID_OBJETO`);

--
-- Indices de la tabla `pokedex`
--
ALTER TABLE `pokedex`
  ADD PRIMARY KEY (`NUM_POKEDEX`);

--
-- Indices de la tabla `pokemon`
--
ALTER TABLE `pokemon`
  ADD PRIMARY KEY (`ID_POKEMON`),
  ADD KEY `NUM_POKEDEX` (`NUM_POKEDEX`),
  ADD KEY `ID_ENTRENADOR` (`ID_ENTRENADOR`),
  ADD KEY `ID_OBJETO` (`ID_OBJETO`);

--
-- Indices de la tabla `turno`
--
ALTER TABLE `turno`
  ADD PRIMARY KEY (`ID_TURNO`),
  ADD KEY `ID_COMBATE` (`ID_COMBATE`);

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `combate`
--
ALTER TABLE `combate`
  ADD CONSTRAINT `combate_ibfk_1` FOREIGN KEY (`ID_ENTRENADOR`) REFERENCES `entrenador` (`ID_ENTRENADOR`);

--
-- Filtros para la tabla `mochila`
--
ALTER TABLE `mochila`
  ADD CONSTRAINT `mochila_ibfk_1` FOREIGN KEY (`ID_ENTRENADOR`) REFERENCES `entrenador` (`ID_ENTRENADOR`),
  ADD CONSTRAINT `mochila_ibfk_2` FOREIGN KEY (`ID_OBJETO`) REFERENCES `objeto` (`ID_OBJETO`);

--
-- Filtros para la tabla `movimientos_pokemon`
--
ALTER TABLE `movimientos_pokemon`
  ADD CONSTRAINT `movimientos_pokemon_ibfk_1` FOREIGN KEY (`ID_MOVIMIENTO`) REFERENCES `movimientos` (`ID_MOVIMIENTO`),
  ADD CONSTRAINT `movimientos_pokemon_ibfk_2` FOREIGN KEY (`ID_POKEMON`) REFERENCES `pokemon` (`ID_POKEMON`);

--
-- Filtros para la tabla `pokemon`
--
ALTER TABLE `pokemon`
  ADD CONSTRAINT `pokemon_ibfk_1` FOREIGN KEY (`NUM_POKEDEX`) REFERENCES `pokedex` (`NUM_POKEDEX`),
  ADD CONSTRAINT `pokemon_ibfk_2` FOREIGN KEY (`ID_ENTRENADOR`) REFERENCES `entrenador` (`ID_ENTRENADOR`),
  ADD CONSTRAINT `pokemon_ibfk_3` FOREIGN KEY (`ID_OBJETO`) REFERENCES `objeto` (`ID_OBJETO`);

--
-- Filtros para la tabla `turno`
--
ALTER TABLE `turno`
  ADD CONSTRAINT `turno_ibfk_1` FOREIGN KEY (`ID_COMBATE`) REFERENCES `combate` (`ID_COMBATE`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
