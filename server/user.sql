-- phpMyAdmin SQL Dump
-- version 4.9.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 12, 2019 at 02:24 AM
-- Server version: 10.4.8-MariaDB
-- PHP Version: 7.1.33

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `uploadimg_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `id` int(11) NOT NULL,
  `uname` varchar(250) NOT NULL,
  `uemail` varchar(250) NOT NULL,
  `upassword` varchar(250) NOT NULL,
  `ugender` varchar(20) NOT NULL,
  `uimage` varchar(250) DEFAULT NULL,
  `udesignation` varchar(250) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`id`, `uname`, `uemail`, `upassword`, `ugender`, `uimage`, `udesignation`) VALUES
(1, 'Jaiveer Gupta', 'jaiveer@gmail.com', '123456', 'male', 'jai_blog_logo.png', 'Android Developer'),
(2, 'Jaiveer Gupta', 'jaiveergufffpta95@gmail.com', 'fddfu', 'Male', NULL, 'gggghhhhh'),
(3, 'Binod Gupta', 'jaiveergupta98@gmail.com', '123456', 'Male', NULL, 'netwoker'),
(4, 'kavita', 'jaiveerguptahlk98@gmail.com', 'nahha', 'Female', NULL, 'hgagav'),
(5, 'yyyyy', 'ggggg@gmail.com', 'yyttt', 'Female', NULL, 'ggty');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uemail` (`uemail`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
