-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Oct 31, 2023 at 08:27 PM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `paqueteturistico`
--

-- --------------------------------------------------------

--
-- Table structure for table `ciudad`
--

CREATE TABLE `ciudad` (
  `idCiudad` int(11) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `pais` int(11) NOT NULL,
  `provincia` int(11) NOT NULL,
  `fechaDeTemporadaAlta` date NOT NULL,
  `estado` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `establecimiento`
--

CREATE TABLE `establecimiento` (
  `idEstablecimiento` int(11) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `direccion` varchar(50) NOT NULL,
  `telefono` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `estadia`
--

CREATE TABLE `estadia` (
  `idEstadia` int(11) NOT NULL,
  `establecimiento` int(11) NOT NULL,
  `checkIn` date NOT NULL,
  `checkOut` date NOT NULL,
  `servicio` varchar(25) NOT NULL,
  `importeDiario` double NOT NULL,
  `ciudadDestino` int(11) NOT NULL,
  `temporada` varchar(25) NOT NULL,
  `estado` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `pais`
--

CREATE TABLE `pais` (
  `idPais` int(11) NOT NULL,
  `nombre` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `pais`
--

INSERT INTO `pais` (`idPais`, `nombre`) VALUES
(50, 'Argentina');

-- --------------------------------------------------------

--
-- Table structure for table `paquete`
--

CREATE TABLE `paquete` (
  `idPaquete` int(11) NOT NULL,
  `origen` int(11) NOT NULL,
  `destino` int(11) NOT NULL,
  `estadia` int(11) NOT NULL,
  `pasaje` int(11) NOT NULL,
  `valorTotal` decimal(10,2) NOT NULL,
  `estado` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `pasaje`
--

CREATE TABLE `pasaje` (
  `idPasaje` int(11) NOT NULL,
  `tipoTransporte` varchar(25) NOT NULL,
  `importe` decimal(10,2) NOT NULL,
  `nombreCiudadOrigen` varchar(100) NOT NULL,
  `nombreCiudadDestino` varchar(100) NOT NULL,
  `estado` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `provincia`
--

CREATE TABLE `provincia` (
  `idProvincia` int(11) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `pais` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `provincia`
--

INSERT INTO `provincia` (`idProvincia`, `nombre`, `pais`) VALUES
(74, 'Desconocida', 50);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `ciudad`
--
ALTER TABLE `ciudad`
  ADD PRIMARY KEY (`idCiudad`),
  ADD UNIQUE KEY `nombre` (`nombre`),
  ADD KEY `pais` (`pais`),
  ADD KEY `provincia` (`provincia`);

--
-- Indexes for table `establecimiento`
--
ALTER TABLE `establecimiento`
  ADD PRIMARY KEY (`idEstablecimiento`);

--
-- Indexes for table `estadia`
--
ALTER TABLE `estadia`
  ADD PRIMARY KEY (`idEstadia`),
  ADD KEY `establecimiento` (`establecimiento`),
  ADD KEY `ciudadDestino` (`ciudadDestino`);

--
-- Indexes for table `pais`
--
ALTER TABLE `pais`
  ADD PRIMARY KEY (`idPais`),
  ADD UNIQUE KEY `nombre` (`nombre`);

--
-- Indexes for table `paquete`
--
ALTER TABLE `paquete`
  ADD PRIMARY KEY (`idPaquete`),
  ADD KEY `origen` (`origen`),
  ADD KEY `paquete_ibfk_1` (`destino`),
  ADD KEY `pasaje` (`pasaje`),
  ADD KEY `estadia` (`estadia`);

--
-- Indexes for table `pasaje`
--
ALTER TABLE `pasaje`
  ADD PRIMARY KEY (`idPasaje`);

--
-- Indexes for table `provincia`
--
ALTER TABLE `provincia`
  ADD PRIMARY KEY (`idProvincia`),
  ADD UNIQUE KEY `nombre` (`nombre`),
  ADD KEY `pais` (`pais`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `ciudad`
--
ALTER TABLE `ciudad`
  MODIFY `idCiudad` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=152;

--
-- AUTO_INCREMENT for table `establecimiento`
--
ALTER TABLE `establecimiento`
  MODIFY `idEstablecimiento` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=67;

--
-- AUTO_INCREMENT for table `estadia`
--
ALTER TABLE `estadia`
  MODIFY `idEstadia` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `pais`
--
ALTER TABLE `pais`
  MODIFY `idPais` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=56;

--
-- AUTO_INCREMENT for table `paquete`
--
ALTER TABLE `paquete`
  MODIFY `idPaquete` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=75;

--
-- AUTO_INCREMENT for table `pasaje`
--
ALTER TABLE `pasaje`
  MODIFY `idPasaje` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=68;

--
-- AUTO_INCREMENT for table `provincia`
--
ALTER TABLE `provincia`
  MODIFY `idProvincia` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=80;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `ciudad`
--
ALTER TABLE `ciudad`
  ADD CONSTRAINT `ciudad_ibfk_1` FOREIGN KEY (`pais`) REFERENCES `pais` (`idPais`),
  ADD CONSTRAINT `ciudad_ibfk_2` FOREIGN KEY (`provincia`) REFERENCES `provincia` (`idProvincia`);

--
-- Constraints for table `estadia`
--
ALTER TABLE `estadia`
  ADD CONSTRAINT `estadia_ibfk_1` FOREIGN KEY (`establecimiento`) REFERENCES `establecimiento` (`idEstablecimiento`),
  ADD CONSTRAINT `estadia_ibfk_2` FOREIGN KEY (`ciudadDestino`) REFERENCES `ciudad` (`idCiudad`);

--
-- Constraints for table `paquete`
--
ALTER TABLE `paquete`
  ADD CONSTRAINT `paquete_ibfk_1` FOREIGN KEY (`estadia`) REFERENCES `estadia` (`idEstadia`);

--
-- Constraints for table `provincia`
--
ALTER TABLE `provincia`
  ADD CONSTRAINT `provincia_ibfk_1` FOREIGN KEY (`pais`) REFERENCES `pais` (`idPais`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
