-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Sep 30, 2024 at 02:26 AM
-- Server version: 9.0.1
-- PHP Version: 8.3.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `allo_bank`
--

-- --------------------------------------------------------

--
-- Table structure for table `caleg`
--

CREATE TABLE `caleg` (
  `id` binary(16) NOT NULL,
  `jenis_kelamin` enum('LAKILAKI','PEREMPUAN') DEFAULT NULL,
  `nama` varchar(255) DEFAULT NULL,
  `nomor_urut` int DEFAULT NULL,
  `id_dapil` binary(16) NOT NULL,
  `id_partai` binary(16) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `caleg`
--

INSERT INTO `caleg` (`id`, `jenis_kelamin`, `nama`, `nomor_urut`, `id_dapil`, `id_partai`) VALUES
(0x1ff6bd927d9e11efbf7dbfcab632d597, 'LAKILAKI', 'Caleg 1', 1, 0x1277d9da7d9e11efbf7dbfcab632d597, 0x1817c7c47d9e11efbf7dbfcab632d597),
(0x1ff6d78c7d9e11efbf7dbfcab632d597, 'PEREMPUAN', 'Caleg 2', 2, 0x12780f4a7d9e11efbf7dbfcab632d597, 0x1817d2147d9e11efbf7dbfcab632d597),
(0x1ff6da3e7d9e11efbf7dbfcab632d597, 'LAKILAKI', 'Caleg 3', 3, 0x127810ee7d9e11efbf7dbfcab632d597, 0x1817d3b87d9e11efbf7dbfcab632d597),
(0x1ff6dbf67d9e11efbf7dbfcab632d597, 'PEREMPUAN', 'Caleg 4', 4, 0x1278115c7d9e11efbf7dbfcab632d597, 0x1817d4947d9e11efbf7dbfcab632d597),
(0x1ff6ddae7d9e11efbf7dbfcab632d597, 'LAKILAKI', 'Caleg 5', 5, 0x1277d9da7d9e11efbf7dbfcab632d597, 0x1817c7c47d9e11efbf7dbfcab632d597),
(0x1ff6df987d9e11efbf7dbfcab632d597, 'PEREMPUAN', 'Caleg 6', 6, 0x12780f4a7d9e11efbf7dbfcab632d597, 0x1817d2147d9e11efbf7dbfcab632d597),
(0x1ff6e1327d9e11efbf7dbfcab632d597, 'LAKILAKI', 'Caleg 7', 7, 0x127810ee7d9e11efbf7dbfcab632d597, 0x1817d3b87d9e11efbf7dbfcab632d597),
(0x1ff6e2cc7d9e11efbf7dbfcab632d597, 'PEREMPUAN', 'Caleg 8', 8, 0x1278115c7d9e11efbf7dbfcab632d597, 0x1817d4947d9e11efbf7dbfcab632d597),
(0x1ff6e4847d9e11efbf7dbfcab632d597, 'LAKILAKI', 'Caleg 9', 9, 0x1277d9da7d9e11efbf7dbfcab632d597, 0x1817c7c47d9e11efbf7dbfcab632d597),
(0x1ff6e5f67d9e11efbf7dbfcab632d597, 'PEREMPUAN', 'Caleg 10', 10, 0x12780f4a7d9e11efbf7dbfcab632d597, 0x1817d2147d9e11efbf7dbfcab632d597);

-- --------------------------------------------------------

--
-- Table structure for table `dapil`
--

CREATE TABLE `dapil` (
  `id` binary(16) NOT NULL,
  `jumlah_kursi` int NOT NULL,
  `nama_dapil` varchar(255) DEFAULT NULL,
  `provinsi` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `dapil`
--

INSERT INTO `dapil` (`id`, `jumlah_kursi`, `nama_dapil`, `provinsi`) VALUES
(0x1277d9da7d9e11efbf7dbfcab632d597, 5, 'Dapil 1', 'Provinsi A'),
(0x12780f4a7d9e11efbf7dbfcab632d597, 4, 'Dapil 2', 'Provinsi A'),
(0x127810ee7d9e11efbf7dbfcab632d597, 6, 'Dapil 3', 'Provinsi B'),
(0x1278115c7d9e11efbf7dbfcab632d597, 3, 'Dapil 4', 'Provinsi B');

-- --------------------------------------------------------

--
-- Table structure for table `dapil_wilayah_dapil_list`
--

CREATE TABLE `dapil_wilayah_dapil_list` (
  `dapil_id` binary(16) NOT NULL,
  `wilayah_dapil_list` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `dapil_wilayah_dapil_list`
--

INSERT INTO `dapil_wilayah_dapil_list` (`dapil_id`, `wilayah_dapil_list`) VALUES
(0x1277d9da7d9e11efbf7dbfcab632d597, 'Wilayah A1'),
(0x1277d9da7d9e11efbf7dbfcab632d597, 'Wilayah A2'),
(0x12780f4a7d9e11efbf7dbfcab632d597, 'Wilayah B1'),
(0x12780f4a7d9e11efbf7dbfcab632d597, 'Wilayah B2'),
(0x127810ee7d9e11efbf7dbfcab632d597, 'Wilayah C1'),
(0x127810ee7d9e11efbf7dbfcab632d597, 'Wilayah C2'),
(0x1278115c7d9e11efbf7dbfcab632d597, 'Wilayah D1'),
(0x1278115c7d9e11efbf7dbfcab632d597, 'Wilayah D2');

-- --------------------------------------------------------

--
-- Table structure for table `partai`
--

CREATE TABLE `partai` (
  `id` binary(16) NOT NULL,
  `nama_partai` varchar(255) DEFAULT NULL,
  `nomor_urut` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `partai`
--

INSERT INTO `partai` (`id`, `nama_partai`, `nomor_urut`) VALUES
(0x1817c7c47d9e11efbf7dbfcab632d597, 'Partai A', 1),
(0x1817d2147d9e11efbf7dbfcab632d597, 'Partai B', 2),
(0x1817d3b87d9e11efbf7dbfcab632d597, 'Partai C', 3),
(0x1817d4947d9e11efbf7dbfcab632d597, 'Partai D', 4);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `caleg`
--
ALTER TABLE `caleg`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKaujnksfcp45xgs6il9pwknp9h` (`id_dapil`),
  ADD KEY `FKac8lve4bt6ydvsyg8b67unsm8` (`id_partai`);

--
-- Indexes for table `dapil`
--
ALTER TABLE `dapil`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `dapil_wilayah_dapil_list`
--
ALTER TABLE `dapil_wilayah_dapil_list`
  ADD KEY `FK6ehl8rmwahgy7ei2acxhbnfkj` (`dapil_id`);

--
-- Indexes for table `partai`
--
ALTER TABLE `partai`
  ADD PRIMARY KEY (`id`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `caleg`
--
ALTER TABLE `caleg`
  ADD CONSTRAINT `FKac8lve4bt6ydvsyg8b67unsm8` FOREIGN KEY (`id_partai`) REFERENCES `partai` (`id`),
  ADD CONSTRAINT `FKaujnksfcp45xgs6il9pwknp9h` FOREIGN KEY (`id_dapil`) REFERENCES `dapil` (`id`);

--
-- Constraints for table `dapil_wilayah_dapil_list`
--
ALTER TABLE `dapil_wilayah_dapil_list`
  ADD CONSTRAINT `FK6ehl8rmwahgy7ei2acxhbnfkj` FOREIGN KEY (`dapil_id`) REFERENCES `dapil` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
