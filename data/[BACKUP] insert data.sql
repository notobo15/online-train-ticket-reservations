-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               8.0.30 - MySQL Community Server - GPL
-- Server OS:                    Win64
-- HeidiSQL Version:             12.1.0.6537
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

-- Dumping data for table booking_train.carriages: ~6 rows (approximately)
INSERT INTO `carriages` (`set_count`, `total_floors`, `booking_train_id`, `carriage_id`, `class_id`, `car_number`) VALUES
	(64, 1, 8, 1, 1, '1'),
	(64, 1, 8, 2, 1, '2'),
	(48, 3, 8, 3, 2, '3'),
	(48, 3, 8, 4, 2, '4'),
	(32, 2, 8, 5, 3, '5'),
	(32, 2, 8, 6, 3, '6');

-- Dumping data for table booking_train.carriage_class: ~2 rows (approximately)
INSERT INTO `carriage_class` (`id`, `name`) VALUES
	(1, 'Ghế cứng'),
	(2, 'Gường nằm khoang 6'),
	(3, 'Gường nằm khoang 4');

-- Dumping data for table booking_train.compartment: ~6 rows (approximately)
INSERT INTO `compartment` (`capacity`, `floor`, `carriage_id`, `compartment_id`, `seat_type_id`, `compartment_number`) VALUES
	(6, 1, 3, 1, 5, '1'),
	(6, 2, 3, 2, 6, '1'),
	(6, 3, 3, 3, 7, '1'),
	(6, 1, 3, 4, 5, '2'),
	(6, 2, 3, 5, 6, '2'),
	(6, 3, 3, 6, 7, '2');

-- Dumping data for table booking_train.passengers: ~0 rows (approximately)
INSERT INTO `passengers` (`passenger_id`, `passenger_type_id`, `full_name`) VALUES
	(1, 1, 'Nguyễn Văn A');

-- Dumping data for table booking_train.passenger_types: ~4 rows (approximately)
INSERT INTO `passenger_types` (`discount_percentage`, `passenger_type_id`, `passenger_type`) VALUES
	(0, 1, 'Người lớn'),
	(25, 2, 'Trẻ em'),
	(15, 3, 'Sinh viên'),
	(20, 4, 'Người cao tuổi');

-- Dumping data for table booking_train.prices: ~0 rows (approximately)

-- Dumping data for table booking_train.provinces: ~63 rows (approximately)
INSERT INTO `provinces` (`code`, `code_name`, `full_name`, `full_name_en`, `name`, `name_en`) VALUES
	('01', 'ha_noi', 'Thành phố Hà Nội', 'Ha Noi City', 'Hà Nội', 'Ha Noi'),
	('02', 'ha_giang', 'Tỉnh Hà Giang', 'Ha Giang Province', 'Hà Giang', 'Ha Giang'),
	('04', 'cao_bang', 'Tỉnh Cao Bằng', 'Cao Bang Province', 'Cao Bằng', 'Cao Bang'),
	('06', 'bac_kan', 'Tỉnh Bắc Kạn', 'Bac Kan Province', 'Bắc Kạn', 'Bac Kan'),
	('08', 'tuyen_quang', 'Tỉnh Tuyên Quang', 'Tuyen Quang Province', 'Tuyên Quang', 'Tuyen Quang'),
	('10', 'lao_cai', 'Tỉnh Lào Cai', 'Lao Cai Province', 'Lào Cai', 'Lao Cai'),
	('11', 'dien_bien', 'Tỉnh Điện Biên', 'Dien Bien Province', 'Điện Biên', 'Dien Bien'),
	('12', 'lai_chau', 'Tỉnh Lai Châu', 'Lai Chau Province', 'Lai Châu', 'Lai Chau'),
	('14', 'son_la', 'Tỉnh Sơn La', 'Son La Province', 'Sơn La', 'Son La'),
	('15', 'yen_bai', 'Tỉnh Yên Bái', 'Yen Bai Province', 'Yên Bái', 'Yen Bai'),
	('17', 'hoa_binh', 'Tỉnh Hoà Bình', 'Hoa Binh Province', 'Hoà Bình', 'Hoa Binh'),
	('19', 'thai_nguyen', 'Tỉnh Thái Nguyên', 'Thai Nguyen Province', 'Thái Nguyên', 'Thai Nguyen'),
	('20', 'lang_son', 'Tỉnh Lạng Sơn', 'Lang Son Province', 'Lạng Sơn', 'Lang Son'),
	('22', 'quang_ninh', 'Tỉnh Quảng Ninh', 'Quang Ninh Province', 'Quảng Ninh', 'Quang Ninh'),
	('24', 'bac_giang', 'Tỉnh Bắc Giang', 'Bac Giang Province', 'Bắc Giang', 'Bac Giang'),
	('25', 'phu_tho', 'Tỉnh Phú Thọ', 'Phu Tho Province', 'Phú Thọ', 'Phu Tho'),
	('26', 'vinh_phuc', 'Tỉnh Vĩnh Phúc', 'Vinh Phuc Province', 'Vĩnh Phúc', 'Vinh Phuc'),
	('27', 'bac_ninh', 'Tỉnh Bắc Ninh', 'Bac Ninh Province', 'Bắc Ninh', 'Bac Ninh'),
	('30', 'hai_duong', 'Tỉnh Hải Dương', 'Hai Duong Province', 'Hải Dương', 'Hai Duong'),
	('31', 'hai_phong', 'Thành phố Hải Phòng', 'Hai Phong City', 'Hải Phòng', 'Hai Phong'),
	('33', 'hung_yen', 'Tỉnh Hưng Yên', 'Hung Yen Province', 'Hưng Yên', 'Hung Yen'),
	('34', 'thai_binh', 'Tỉnh Thái Bình', 'Thai Binh Province', 'Thái Bình', 'Thai Binh'),
	('35', 'ha_nam', 'Tỉnh Hà Nam', 'Ha Nam Province', 'Hà Nam', 'Ha Nam'),
	('36', 'nam_dinh', 'Tỉnh Nam Định', 'Nam Dinh Province', 'Nam Định', 'Nam Dinh'),
	('37', 'ninh_binh', 'Tỉnh Ninh Bình', 'Ninh Binh Province', 'Ninh Bình', 'Ninh Binh'),
	('38', 'thanh_hoa', 'Tỉnh Thanh Hóa', 'Thanh Hoa Province', 'Thanh Hóa', 'Thanh Hoa'),
	('40', 'nghe_an', 'Tỉnh Nghệ An', 'Nghe An Province', 'Nghệ An', 'Nghe An'),
	('42', 'ha_tinh', 'Tỉnh Hà Tĩnh', 'Ha Tinh Province', 'Hà Tĩnh', 'Ha Tinh'),
	('44', 'quang_binh', 'Tỉnh Quảng Bình', 'Quang Binh Province', 'Quảng Bình', 'Quang Binh'),
	('45', 'quang_tri', 'Tỉnh Quảng Trị', 'Quang Tri Province', 'Quảng Trị', 'Quang Tri'),
	('46', 'thua_thien_hue', 'Tỉnh Thừa Thiên Huế', 'Thua Thien Hue Province', 'Thừa Thiên Huế', 'Thua Thien Hue'),
	('48', 'da_nang', 'Thành phố Đà Nẵng', 'Da Nang City', 'Đà Nẵng', 'Da Nang'),
	('49', 'quang_nam', 'Tỉnh Quảng Nam', 'Quang Nam Province', 'Quảng Nam', 'Quang Nam'),
	('51', 'quang_ngai', 'Tỉnh Quảng Ngãi', 'Quang Ngai Province', 'Quảng Ngãi', 'Quang Ngai'),
	('52', 'binh_dinh', 'Tỉnh Bình Định', 'Binh Dinh Province', 'Bình Định', 'Binh Dinh'),
	('54', 'phu_yen', 'Tỉnh Phú Yên', 'Phu Yen Province', 'Phú Yên', 'Phu Yen'),
	('56', 'khanh_hoa', 'Tỉnh Khánh Hòa', 'Khanh Hoa Province', 'Khánh Hòa', 'Khanh Hoa'),
	('58', 'ninh_thuan', 'Tỉnh Ninh Thuận', 'Ninh Thuan Province', 'Ninh Thuận', 'Ninh Thuan'),
	('60', 'binh_thuan', 'Tỉnh Bình Thuận', 'Binh Thuan Province', 'Bình Thuận', 'Binh Thuan'),
	('62', 'kon_tum', 'Tỉnh Kon Tum', 'Kon Tum Province', 'Kon Tum', 'Kon Tum'),
	('64', 'gia_lai', 'Tỉnh Gia Lai', 'Gia Lai Province', 'Gia Lai', 'Gia Lai'),
	('66', 'dak_lak', 'Tỉnh Đắk Lắk', 'Dak Lak Province', 'Đắk Lắk', 'Dak Lak'),
	('67', 'dak_nong', 'Tỉnh Đắk Nông', 'Dak Nong Province', 'Đắk Nông', 'Dak Nong'),
	('68', 'lam_dong', 'Tỉnh Lâm Đồng', 'Lam Dong Province', 'Lâm Đồng', 'Lam Dong'),
	('70', 'binh_phuoc', 'Tỉnh Bình Phước', 'Binh Phuoc Province', 'Bình Phước', 'Binh Phuoc'),
	('72', 'tay_ninh', 'Tỉnh Tây Ninh', 'Tay Ninh Province', 'Tây Ninh', 'Tay Ninh'),
	('74', 'binh_duong', 'Tỉnh Bình Dương', 'Binh Duong Province', 'Bình Dương', 'Binh Duong'),
	('75', 'dong_nai', 'Tỉnh Đồng Nai', 'Dong Nai Province', 'Đồng Nai', 'Dong Nai'),
	('77', 'ba_ria_vung_tau', 'Tỉnh Bà Rịa - Vũng Tàu', 'Ba Ria - Vung Tau Province', 'Bà Rịa - Vũng Tàu', 'Ba Ria - Vung Tau'),
	('79', 'ho_chi_minh', 'Thành phố Hồ Chí Minh', 'Ho Chi Minh City', 'Hồ Chí Minh', 'Ho Chi Minh'),
	('80', 'long_an', 'Tỉnh Long An', 'Long An Province', 'Long An', 'Long An'),
	('82', 'tien_giang', 'Tỉnh Tiền Giang', 'Tien Giang Province', 'Tiền Giang', 'Tien Giang'),
	('83', 'ben_tre', 'Tỉnh Bến Tre', 'Ben Tre Province', 'Bến Tre', 'Ben Tre'),
	('84', 'tra_vinh', 'Tỉnh Trà Vinh', 'Tra Vinh Province', 'Trà Vinh', 'Tra Vinh'),
	('86', 'vinh_long', 'Tỉnh Vĩnh Long', 'Vinh Long Province', 'Vĩnh Long', 'Vinh Long'),
	('87', 'dong_thap', 'Tỉnh Đồng Tháp', 'Dong Thap Province', 'Đồng Tháp', 'Dong Thap'),
	('89', 'an_giang', 'Tỉnh An Giang', 'An Giang Province', 'An Giang', 'An Giang'),
	('91', 'kien_giang', 'Tỉnh Kiên Giang', 'Kien Giang Province', 'Kiên Giang', 'Kien Giang'),
	('92', 'can_tho', 'Thành phố Cần Thơ', 'Can Tho City', 'Cần Thơ', 'Can Tho'),
	('93', 'hau_giang', 'Tỉnh Hậu Giang', 'Hau Giang Province', 'Hậu Giang', 'Hau Giang'),
	('94', 'soc_trang', 'Tỉnh Sóc Trăng', 'Soc Trang Province', 'Sóc Trăng', 'Soc Trang'),
	('95', 'bac_lieu', 'Tỉnh Bạc Liêu', 'Bac Lieu Province', 'Bạc Liêu', 'Bac Lieu'),
	('96', 'ca_mau', 'Tỉnh Cà Mau', 'Ca Mau Province', 'Cà Mau', 'Ca Mau');

-- Dumping data for table booking_train.railway_networks: ~1 rows (approximately)
INSERT INTO `railway_networks` (`railway_id`, `name`, `departure_station`, `destination_station`) VALUES
	(1, 'Sài Gòn - Hà Nội', 173, 1);

-- Dumping data for table booking_train.railway_routes: ~4 rows (approximately)
INSERT INTO `railway_routes` (`railway_route_id`, `departure_date`, `is_open`, `railway_network`, `booking_train_id`) VALUES
	(1, '2024-09-16', b'1', 1, 8),
	(2, '2024-09-16', b'1', 1, 6),
	(3, '2024-09-16', b'1', 1, 4),
	(4, '2024-09-16', b'1', 1, 2);

-- Dumping data for table booking_train.routes: ~55 rows (approximately)
INSERT INTO `routes` (`route_id`, `arrival_time`, `date_number`, `departure_time`, `distance`, `station_number`, `end_station_id`, `start_station_id`, `railway_route_id`) VALUES
	(1, '06:00:00.000000', 0, '06:00:00.000000', 0, 1, 173, 173, 1),
	(2, '06:29:00.000000', 0, '06:32:00.000000', 19, 2, 168, 173, 1),
	(3, '06:45:00.000000', 0, '06:48:00.000000', 10, 3, 172, 168, 1),
	(4, '07:48:00.000000', 0, '07:51:00.000000', 48, 4, 171, 172, 1),
	(5, '08:39:00.000000', 0, '08:43:00.000000', 46, 5, 164, 171, 1),
	(6, '09:41:00.000000', 0, '09:46:00.000000', 52, 6, 163, 164, 1),
	(7, '12:01:00.000000', 0, '12:04:00.000000', 143, 7, 158, 163, 1),
	(8, '13:39:00.000000', 0, '13:59:00.000000', 93, 8, 156, 158, 1),
	(9, '14:41:00.000000', 0, '14:44:00.000000', 34, 9, 155, 156, 1),
	(10, '15:12:00.000000', 0, '15:15:00.000000', 27, 10, 154, 155, 1),
	(11, '16:17:00.000000', 0, '16:21:00.000000', 56, 11, 148, 154, 1),
	(12, '17:16:00.000000', 0, '17:20:00.000000', 44, 12, 147, 148, 1),
	(13, '18:29:00.000000', 0, '18:41:00.000000', 58, 13, 143, 147, 1),
	(14, '20:03:00.000000', 0, '20:06:00.000000', 79, 14, 140, 143, 1),
	(15, '21:48:00.000000', 0, '21:53:00.000000', 89, 15, 139, 140, 1),
	(16, '23:02:00.000000', 0, '23:05:00.000000', 63, 16, 138, 139, 1),
	(17, '00:36:00.000000', 1, '00:56:00.000000', 74, 17, 135, 138, 1),
	(18, '03:28:00.000000', 1, '03:34:00.000000', 103, 18, 129, 135, 1),
	(19, '04:46:00.000000', 1, '04:49:00.000000', 66, 19, 122, 129, 1),
	(20, '06:06:00.000000', 1, '06:15:00.000000', 71, 20, 113, 122, 1),
	(21, '06:50:00.000000', 1, '07:02:00.000000', 29, 21, 110, 113, 1),
	(22, '07:48:00.000000', 1, '07:51:00.000000', 40, 22, 106, 110, 1),
	(23, '08:46:00.000000', 1, '08:49:00.000000', 46, 23, 100, 106, 1),
	(24, '09:54:00.000000', 1, '09:57:00.000000', 49, 24, 94, 100, 1),
	(25, '10:53:00.000000', 1, '10:56:00.000000', 47, 25, 88, 94, 1),
	(26, '11:22:00.000000', 1, '11:29:00.000000', 21, 26, 85, 88, 1),
	(27, '12:32:00.000000', 1, '12:35:00.000000', 40, 27, 84, 85, 1),
	(28, '14:12:00.000000', 1, '14:15:00.000000', 82, 28, 82, 84, 1),
	(29, '14:38:00.000000', 1, '14:41:00.000000', 22, 29, 81, 82, 1),
	(30, '15:16:00.000000', 1, '15:19:00.000000', 34, 30, 80, 81, 1),
	(31, '16:09:00.000000', 1, '16:12:00.000000', 26, 31, 79, 80, 1),
	(32, '16:48:00.000000', 1, '17:08:00.000000', 28, 32, 78, 79, 1),
	(33, '17:49:00.000000', 1, '17:52:00.000000', 31, 33, 77, 78, 1),
	(34, '19:12:00.000000', 1, '19:12:00.000000', 56, 34, 1, 77, 1),
	(35, '21:02:00.000000', 0, '21:05:00.000000', 19, 1, 168, 173, 2),
	(36, '21:18:00.000000', 0, '21:21:00.000000', 10, 2, 172, 168, 2),
	(37, '23:59:00.000000', 0, '00:04:00.000000', 146, 3, 163, 172, 2),
	(38, '03:48:00.000000', 0, '03:56:00.000000', 236, 4, 156, 163, 2),
	(39, '06:03:00.000000', 0, '06:06:00.000000', 117, 5, 148, 156, 2),
	(40, '08:05:00.000000', 0, '08:17:00.000000', 102, 6, 143, 148, 2),
	(41, '11:02:00.000000', 0, '11:07:00.000000', 168, 7, 139, 143, 2),
	(42, '12:12:00.000000', 0, '12:15:00.000000', 63, 8, 138, 139, 2),
	(43, '13:40:00.000000', 0, '13:55:00.000000', 74, 9, 135, 138, 2),
	(44, '16:19:00.000000', 0, '16:24:00.000000', 103, 10, 129, 135, 2),
	(45, '17:32:00.000000', 0, '17:35:00.000000', 66, 11, 122, 129, 2),
	(46, '19:18:00.000000', 0, '19:30:00.000000', 100, 12, 110, 122, 2),
	(47, '21:07:00.000000', 0, '21:10:00.000000', 86, 13, 100, 110, 2),
	(48, '22:13:00.000000', 0, '22:16:00.000000', 49, 14, 94, 100, 2),
	(49, '23:11:00.000000', 0, '23:14:00.000000', 47, 15, 88, 94, 2),
	(50, '23:40:00.000000', 0, '23:47:00.000000', 21, 16, 85, 88, 2),
	(51, '02:33:00.000000', 1, '02:40:00.000000', 144, 17, 81, 85, 2),
	(52, '03:43:00.000000', 1, '03:46:00.000000', 60, 18, 79, 81, 2),
	(53, '04:18:00.000000', 1, '04:21:00.000000', 28, 19, 78, 79, 2),
	(54, '04:53:00.000000', 1, '04:56:00.000000', 31, 20, 77, 78, 2),
	(55, '06:00:00.000000', 1, '06:00:00.000000', 56, 21, 1, 77, 2);

-- Dumping data for table booking_train.seats: ~176 rows (approximately)
INSERT INTO `seats` (`floor`, `carriage_id`, `compartment_id`, `seat_id`, `seat_number`, `seat_type_id`, `compartment_number`, `status`) VALUES
	(NULL, 1, NULL, 1, '1', 8, NULL, b'0'),
	(NULL, 1, NULL, 2, '2', 8, NULL, b'0'),
	(NULL, 1, NULL, 3, '3', 8, NULL, b'0'),
	(NULL, 1, NULL, 4, '4', 8, NULL, b'0'),
	(NULL, 1, NULL, 5, '5', 8, NULL, b'0'),
	(NULL, 1, NULL, 6, '6', 8, NULL, b'0'),
	(NULL, 1, NULL, 7, '7', 8, NULL, b'0'),
	(NULL, 1, NULL, 8, '8', 8, NULL, b'0'),
	(NULL, 1, NULL, 9, '9', 8, NULL, b'0'),
	(NULL, 1, NULL, 10, '10', 8, NULL, b'0'),
	(NULL, 1, NULL, 11, '11', 8, NULL, b'0'),
	(NULL, 1, NULL, 12, '12', 8, NULL, b'0'),
	(NULL, 1, NULL, 13, '13', 8, NULL, b'0'),
	(NULL, 1, NULL, 14, '14', 8, NULL, b'0'),
	(NULL, 1, NULL, 15, '15', 8, NULL, b'0'),
	(NULL, 1, NULL, 16, '16', 8, NULL, b'0'),
	(NULL, 1, NULL, 17, '17', 8, NULL, b'0'),
	(NULL, 1, NULL, 18, '18', 8, NULL, b'0'),
	(NULL, 1, NULL, 19, '19', 8, NULL, b'0'),
	(NULL, 1, NULL, 20, '20', 8, NULL, b'0'),
	(NULL, 1, NULL, 21, '21', 8, NULL, b'0'),
	(NULL, 1, NULL, 22, '22', 8, NULL, b'0'),
	(NULL, 1, NULL, 23, '23', 8, NULL, b'0'),
	(NULL, 1, NULL, 24, '24', 8, NULL, b'0'),
	(NULL, 1, NULL, 25, '25', 8, NULL, b'0'),
	(NULL, 1, NULL, 26, '26', 8, NULL, b'0'),
	(NULL, 1, NULL, 27, '27', 8, NULL, b'0'),
	(NULL, 1, NULL, 28, '28', 8, NULL, b'0'),
	(NULL, 1, NULL, 29, '29', 8, NULL, b'0'),
	(NULL, 1, NULL, 30, '30', 8, NULL, b'0'),
	(NULL, 1, NULL, 31, '31', 8, NULL, b'0'),
	(NULL, 1, NULL, 32, '32', 8, NULL, b'0'),
	(NULL, 1, NULL, 33, '33', 8, NULL, b'0'),
	(NULL, 1, NULL, 34, '34', 8, NULL, b'0'),
	(NULL, 1, NULL, 35, '35', 8, NULL, b'0'),
	(NULL, 1, NULL, 36, '36', 8, NULL, b'0'),
	(NULL, 1, NULL, 37, '37', 8, NULL, b'0'),
	(NULL, 1, NULL, 38, '38', 8, NULL, b'0'),
	(NULL, 1, NULL, 39, '39', 8, NULL, b'0'),
	(NULL, 1, NULL, 40, '40', 8, NULL, b'0'),
	(NULL, 1, NULL, 41, '41', 8, NULL, b'0'),
	(NULL, 1, NULL, 42, '42', 8, NULL, b'0'),
	(NULL, 1, NULL, 43, '43', 8, NULL, b'0'),
	(NULL, 1, NULL, 44, '44', 8, NULL, b'0'),
	(NULL, 1, NULL, 45, '45', 8, NULL, b'0'),
	(NULL, 1, NULL, 46, '46', 8, NULL, b'0'),
	(NULL, 1, NULL, 47, '47', 8, NULL, b'0'),
	(NULL, 1, NULL, 48, '48', 8, NULL, b'0'),
	(NULL, 1, NULL, 49, '49', 8, NULL, b'0'),
	(NULL, 1, NULL, 50, '50', 8, NULL, b'0'),
	(NULL, 1, NULL, 51, '51', 8, NULL, b'0'),
	(NULL, 1, NULL, 52, '52', 8, NULL, b'0'),
	(NULL, 1, NULL, 53, '53', 8, NULL, b'0'),
	(NULL, 1, NULL, 54, '54', 8, NULL, b'0'),
	(NULL, 1, NULL, 55, '55', 8, NULL, b'0'),
	(NULL, 1, NULL, 56, '56', 8, NULL, b'0'),
	(NULL, 1, NULL, 57, '57', 8, NULL, b'0'),
	(NULL, 1, NULL, 58, '58', 8, NULL, b'0'),
	(NULL, 1, NULL, 59, '59', 8, NULL, b'0'),
	(NULL, 1, NULL, 60, '60', 8, NULL, b'0'),
	(NULL, 1, NULL, 61, '61', 8, NULL, b'0'),
	(NULL, 1, NULL, 62, '62', 8, NULL, b'0'),
	(NULL, 1, NULL, 63, '63', 8, NULL, b'0'),
	(NULL, 1, NULL, 64, '64', 8, NULL, b'0'),
	(NULL, 2, NULL, 65, '1', 8, NULL, b'0'),
	(NULL, 2, NULL, 66, '2', 8, NULL, b'0'),
	(NULL, 2, NULL, 67, '3', 8, NULL, b'0'),
	(NULL, 2, NULL, 68, '4', 8, NULL, b'0'),
	(NULL, 2, NULL, 69, '5', 8, NULL, b'0'),
	(NULL, 2, NULL, 70, '6', 8, NULL, b'0'),
	(NULL, 2, NULL, 71, '7', 8, NULL, b'0'),
	(NULL, 2, NULL, 72, '8', 8, NULL, b'0'),
	(NULL, 2, NULL, 73, '9', 8, NULL, b'0'),
	(NULL, 2, NULL, 74, '10', 8, NULL, b'0'),
	(NULL, 2, NULL, 75, '11', 8, NULL, b'0'),
	(NULL, 2, NULL, 76, '12', 8, NULL, b'0'),
	(NULL, 2, NULL, 77, '13', 8, NULL, b'0'),
	(NULL, 2, NULL, 78, '14', 8, NULL, b'0'),
	(NULL, 2, NULL, 79, '15', 8, NULL, b'0'),
	(NULL, 2, NULL, 80, '16', 8, NULL, b'0'),
	(NULL, 2, NULL, 81, '17', 8, NULL, b'0'),
	(NULL, 2, NULL, 82, '18', 8, NULL, b'0'),
	(NULL, 2, NULL, 83, '19', 8, NULL, b'0'),
	(NULL, 2, NULL, 84, '20', 8, NULL, b'0'),
	(NULL, 2, NULL, 85, '21', 8, NULL, b'0'),
	(NULL, 2, NULL, 86, '22', 8, NULL, b'0'),
	(NULL, 2, NULL, 87, '23', 8, NULL, b'0'),
	(NULL, 2, NULL, 88, '24', 8, NULL, b'0'),
	(NULL, 2, NULL, 89, '25', 8, NULL, b'0'),
	(NULL, 2, NULL, 90, '26', 8, NULL, b'0'),
	(NULL, 2, NULL, 91, '27', 8, NULL, b'0'),
	(NULL, 2, NULL, 92, '28', 8, NULL, b'0'),
	(NULL, 2, NULL, 93, '29', 8, NULL, b'0'),
	(NULL, 2, NULL, 94, '30', 8, NULL, b'0'),
	(NULL, 2, NULL, 95, '31', 8, NULL, b'0'),
	(NULL, 2, NULL, 96, '32', 8, NULL, b'0'),
	(NULL, 2, NULL, 97, '33', 8, NULL, b'0'),
	(NULL, 2, NULL, 98, '34', 8, NULL, b'0'),
	(NULL, 2, NULL, 99, '35', 8, NULL, b'0'),
	(NULL, 2, NULL, 100, '36', 8, NULL, b'0'),
	(NULL, 2, NULL, 101, '37', 8, NULL, b'0'),
	(NULL, 2, NULL, 102, '38', 8, NULL, b'0'),
	(NULL, 2, NULL, 103, '39', 8, NULL, b'0'),
	(NULL, 2, NULL, 104, '40', 8, NULL, b'0'),
	(NULL, 2, NULL, 105, '41', 8, NULL, b'0'),
	(NULL, 2, NULL, 106, '42', 8, NULL, b'0'),
	(NULL, 2, NULL, 107, '43', 8, NULL, b'0'),
	(NULL, 2, NULL, 108, '44', 8, NULL, b'0'),
	(NULL, 2, NULL, 109, '45', 8, NULL, b'0'),
	(NULL, 2, NULL, 110, '46', 8, NULL, b'0'),
	(NULL, 2, NULL, 111, '47', 8, NULL, b'0'),
	(NULL, 2, NULL, 112, '48', 8, NULL, b'0'),
	(NULL, 2, NULL, 113, '49', 8, NULL, b'0'),
	(NULL, 2, NULL, 114, '50', 8, NULL, b'0'),
	(NULL, 2, NULL, 115, '51', 8, NULL, b'0'),
	(NULL, 2, NULL, 116, '52', 8, NULL, b'0'),
	(NULL, 2, NULL, 117, '53', 8, NULL, b'0'),
	(NULL, 2, NULL, 118, '54', 8, NULL, b'0'),
	(NULL, 2, NULL, 119, '55', 8, NULL, b'0'),
	(NULL, 2, NULL, 120, '56', 8, NULL, b'0'),
	(NULL, 2, NULL, 121, '57', 8, NULL, b'0'),
	(NULL, 2, NULL, 122, '58', 8, NULL, b'0'),
	(NULL, 2, NULL, 123, '59', 8, NULL, b'0'),
	(NULL, 2, NULL, 124, '60', 8, NULL, b'0'),
	(NULL, 2, NULL, 125, '61', 8, NULL, b'0'),
	(NULL, 2, NULL, 126, '62', 8, NULL, b'0'),
	(NULL, 2, NULL, 127, '63', 8, NULL, b'0'),
	(NULL, 2, NULL, 128, '64', 8, NULL, b'0'),
	(1, 3, NULL, 129, '1', 5, 1, b'0'),
	(1, 3, NULL, 130, '2', 5, 1, b'0'),
	(2, 3, NULL, 131, '3', 6, 1, b'0'),
	(2, 3, NULL, 132, '4', 6, 1, b'0'),
	(3, 3, NULL, 133, '5', 7, 1, b'0'),
	(3, 3, NULL, 134, '6', 7, 1, b'0'),
	(1, 3, NULL, 135, '7', 5, 2, b'0'),
	(1, 3, NULL, 136, '8', 5, 2, b'0'),
	(2, 3, NULL, 137, '9', 6, 2, b'0'),
	(2, 3, NULL, 138, '10', 6, 2, b'0'),
	(3, 3, NULL, 139, '11', 7, 2, b'0'),
	(3, 3, NULL, 140, '12', 7, 2, b'0'),
	(1, 3, NULL, 141, '13', 5, 3, b'0'),
	(1, 3, NULL, 142, '14', 5, 3, b'0'),
	(2, 3, NULL, 143, '15', 6, 3, b'0'),
	(2, 3, NULL, 144, '16', 6, 3, b'0'),
	(3, 3, NULL, 145, '17', 7, 3, b'0'),
	(3, 3, NULL, 146, '18', 7, 3, b'0'),
	(1, 3, NULL, 147, '19', 5, 4, b'0'),
	(1, 3, NULL, 148, '20', 5, 4, b'0'),
	(2, 3, NULL, 149, '21', 6, 4, b'0'),
	(2, 3, NULL, 150, '22', 6, 4, b'0'),
	(3, 3, NULL, 151, '23', 7, 4, b'0'),
	(3, 3, NULL, 152, '24', 7, 4, b'0'),
	(1, 3, NULL, 153, '25', 5, 5, b'0'),
	(1, 3, NULL, 154, '26', 5, 5, b'0'),
	(2, 3, NULL, 155, '27', 6, 5, b'0'),
	(2, 3, NULL, 156, '28', 6, 5, b'0'),
	(3, 3, NULL, 157, '29', 7, 5, b'0'),
	(3, 3, NULL, 158, '30', 7, 5, b'0'),
	(1, 3, NULL, 159, '31', 5, 6, b'0'),
	(1, 3, NULL, 160, '32', 5, 6, b'0'),
	(2, 3, NULL, 161, '33', 6, 6, b'0'),
	(2, 3, NULL, 162, '34', 6, 6, b'0'),
	(3, 3, NULL, 163, '35', 7, 6, b'0'),
	(3, 3, NULL, 164, '36', 7, 6, b'0'),
	(1, 3, NULL, 165, '37', 5, 7, b'0'),
	(1, 3, NULL, 166, '38', 5, 7, b'0'),
	(2, 3, NULL, 167, '39', 6, 7, b'0'),
	(2, 3, NULL, 168, '40', 6, 7, b'0'),
	(3, 3, NULL, 169, '41', 7, 7, b'0'),
	(3, 3, NULL, 170, '42', 7, 7, b'0'),
	(1, 3, NULL, 171, '43', 5, 8, b'0'),
	(1, 3, NULL, 172, '44', 5, 8, b'0'),
	(2, 3, NULL, 173, '45', 6, 8, b'0'),
	(2, 3, NULL, 174, '46', 6, 8, b'0'),
	(3, 3, NULL, 175, '47', 7, 8, b'0'),
	(3, 3, NULL, 176, '48', 7, 8, b'0'),
	(1, 4, NULL, 177, '1', 5, 1, b'0'),
	(1, 4, NULL, 178, '2', 5, 1, b'0'),
	(2, 4, NULL, 179, '3', 6, 1, b'0'),
	(2, 4, NULL, 180, '4', 6, 1, b'0'),
	(3, 4, NULL, 181, '5', 7, 1, b'0'),
	(3, 4, NULL, 182, '6', 7, 1, b'0'),
	(1, 4, NULL, 183, '7', 5, 2, b'0'),
	(1, 4, NULL, 184, '8', 5, 2, b'0'),
	(2, 4, NULL, 185, '9', 6, 2, b'0'),
	(2, 4, NULL, 186, '10', 6, 2, b'0'),
	(3, 4, NULL, 187, '11', 7, 2, b'0'),
	(3, 4, NULL, 188, '12', 7, 2, b'0'),
	(1, 4, NULL, 189, '13', 5, 3, b'0'),
	(1, 4, NULL, 190, '14', 5, 3, b'0'),
	(2, 4, NULL, 191, '15', 6, 3, b'0'),
	(2, 4, NULL, 192, '16', 6, 3, b'0'),
	(3, 4, NULL, 193, '17', 7, 3, b'0'),
	(3, 4, NULL, 194, '18', 7, 3, b'0'),
	(1, 4, NULL, 195, '19', 5, 4, b'0'),
	(1, 4, NULL, 196, '20', 5, 4, b'0'),
	(2, 4, NULL, 197, '21', 6, 4, b'0'),
	(2, 4, NULL, 198, '22', 6, 4, b'0'),
	(3, 4, NULL, 199, '23', 7, 4, b'0'),
	(3, 4, NULL, 200, '24', 7, 4, b'0'),
	(1, 4, NULL, 201, '25', 5, 5, b'0'),
	(1, 4, NULL, 202, '26', 5, 5, b'0'),
	(2, 4, NULL, 203, '27', 6, 5, b'0'),
	(2, 4, NULL, 204, '28', 6, 5, b'0'),
	(3, 4, NULL, 205, '29', 7, 5, b'0'),
	(3, 4, NULL, 206, '30', 7, 5, b'0'),
	(1, 4, NULL, 207, '31', 5, 6, b'0'),
	(1, 4, NULL, 208, '32', 5, 6, b'0'),
	(2, 4, NULL, 209, '33', 6, 6, b'0'),
	(2, 4, NULL, 210, '34', 6, 6, b'0'),
	(3, 4, NULL, 211, '35', 7, 6, b'0'),
	(3, 4, NULL, 212, '36', 7, 6, b'0'),
	(1, 4, NULL, 213, '37', 5, 7, b'0'),
	(1, 4, NULL, 214, '38', 5, 7, b'0'),
	(2, 4, NULL, 215, '39', 6, 7, b'0'),
	(2, 4, NULL, 216, '40', 6, 7, b'0'),
	(3, 4, NULL, 217, '41', 7, 7, b'0'),
	(3, 4, NULL, 218, '42', 7, 7, b'0'),
	(1, 4, NULL, 219, '43', 5, 8, b'0'),
	(1, 4, NULL, 220, '44', 5, 8, b'0'),
	(2, 4, NULL, 221, '45', 6, 8, b'0'),
	(2, 4, NULL, 222, '46', 6, 8, b'0'),
	(3, 4, NULL, 223, '47', 7, 8, b'0'),
	(3, 4, NULL, 224, '48', 7, 8, b'0'),
	(1, 5, NULL, 225, '1', 1, 1, b'0'),
	(1, 5, NULL, 226, '2', 1, 1, b'0'),
	(2, 5, NULL, 227, '3', 3, 1, b'0'),
	(2, 5, NULL, 228, '4', 3, 1, b'0'),
	(1, 5, NULL, 229, '5', 1, 2, b'0'),
	(1, 5, NULL, 230, '6', 1, 2, b'0'),
	(2, 5, NULL, 231, '7', 3, 2, b'0'),
	(2, 5, NULL, 232, '8', 3, 2, b'0'),
	(1, 5, NULL, 233, '9', 1, 3, b'0'),
	(1, 5, NULL, 234, '10', 1, 3, b'0'),
	(2, 5, NULL, 235, '11', 3, 3, b'0'),
	(2, 5, NULL, 236, '12', 3, 3, b'0'),
	(1, 5, NULL, 237, '13', 1, 4, b'0'),
	(1, 5, NULL, 238, '14', 1, 4, b'0'),
	(2, 5, NULL, 239, '15', 3, 4, b'0'),
	(2, 5, NULL, 240, '16', 3, 4, b'0'),
	(1, 5, NULL, 241, '17', 1, 5, b'0'),
	(1, 5, NULL, 242, '18', 1, 5, b'0'),
	(2, 5, NULL, 243, '19', 3, 5, b'0'),
	(2, 5, NULL, 244, '20', 3, 5, b'0'),
	(1, 5, NULL, 245, '21', 1, 6, b'0'),
	(1, 5, NULL, 246, '22', 1, 6, b'0'),
	(2, 5, NULL, 247, '23', 3, 6, b'0'),
	(2, 5, NULL, 248, '24', 3, 6, b'0'),
	(1, 5, NULL, 249, '25', 1, 7, b'0'),
	(1, 5, NULL, 250, '26', 1, 7, b'0'),
	(2, 5, NULL, 251, '27', 3, 7, b'0'),
	(2, 5, NULL, 252, '28', 3, 7, b'0'),
	(1, 5, NULL, 253, '29', 1, 8, b'0'),
	(1, 5, NULL, 254, '30', 1, 8, b'0'),
	(2, 5, NULL, 255, '31', 3, 8, b'0'),
	(2, 5, NULL, 256, '32', 3, 8, b'0'),
	(1, 6, NULL, 257, '1', 1, 1, b'0'),
	(1, 6, NULL, 258, '2', 1, 1, b'0'),
	(2, 6, NULL, 259, '3', 3, 1, b'0'),
	(2, 6, NULL, 260, '4', 3, 1, b'0'),
	(1, 6, NULL, 261, '5', 1, 2, b'0'),
	(1, 6, NULL, 262, '6', 1, 2, b'0'),
	(2, 6, NULL, 263, '7', 3, 2, b'0'),
	(2, 6, NULL, 264, '8', 3, 2, b'0'),
	(1, 6, NULL, 265, '9', 1, 3, b'0'),
	(1, 6, NULL, 266, '10', 1, 3, b'0'),
	(2, 6, NULL, 267, '11', 3, 3, b'0'),
	(2, 6, NULL, 268, '12', 3, 3, b'0'),
	(1, 6, NULL, 269, '13', 1, 4, b'0'),
	(1, 6, NULL, 270, '14', 1, 4, b'0'),
	(2, 6, NULL, 271, '15', 3, 4, b'0'),
	(2, 6, NULL, 272, '16', 3, 4, b'0'),
	(1, 6, NULL, 273, '17', 1, 5, b'0'),
	(1, 6, NULL, 274, '18', 1, 5, b'0'),
	(2, 6, NULL, 275, '19', 3, 5, b'0'),
	(2, 6, NULL, 276, '20', 3, 5, b'0'),
	(1, 6, NULL, 277, '21', 1, 6, b'0'),
	(1, 6, NULL, 278, '22', 1, 6, b'0'),
	(2, 6, NULL, 279, '23', 3, 6, b'0'),
	(2, 6, NULL, 280, '24', 3, 6, b'0'),
	(1, 6, NULL, 281, '25', 1, 7, b'0'),
	(1, 6, NULL, 282, '26', 1, 7, b'0'),
	(2, 6, NULL, 283, '27', 3, 7, b'0'),
	(2, 6, NULL, 284, '28', 3, 7, b'0'),
	(1, 6, NULL, 285, '29', 1, 8, b'0'),
	(1, 6, NULL, 286, '30', 1, 8, b'0'),
	(2, 6, NULL, 287, '31', 3, 8, b'0'),
	(2, 6, NULL, 288, '32', 3, 8, b'0');

-- Dumping data for table booking_train.seat_route: ~0 rows (approximately)

-- Dumping data for table booking_train.seat_types: ~8 rows (approximately)
INSERT INTO `seat_types` (`seat_type_id`, `code`, `description`, `seat_type`, `price`) VALUES
	(1, 'AnLT1', NULL, 'Tầng 1, khoang có 4 giường', 1612000),
	(2, 'AnLT1v', NULL, 'Tầng 1, khoang có 4 giường vip', 1647000),
	(3, 'AnLT1AnLT2v', NULL, 'Tầng 2, khoang có 4 giường', 1496000),
	(4, 'AnLT2v', NULL, 'Tầng 2, khoang có 4 giường vip', 1531000),
	(5, 'BnLT1', NULL, 'Tầng 1, khoang có 6 giường', 1461000),
	(6, 'BnLT2', NULL, 'Tầng 2, khoang có 6 giường', 1317000),
	(7, 'BnLT3', NULL, '	\r\nTầng 3, khoang có 6 giường', 1144000),
	(8, 'NML', NULL, 'Ngồi mềm', 922000),
	(9, 'NMLV', NULL, '	\r\nNgồi mềm', 942000);

-- Dumping data for table booking_train.stations: ~173 rows (approximately)
INSERT INTO `stations` (`latitude`, `longitude`, `station_id`, `province_code`, `station_name`) VALUES
	(NULL, NULL, 1, '01', 'Ga Hà Nội'),
	(NULL, NULL, 2, '01', 'Ga Giáp Bát'),
	(NULL, NULL, 3, '01', 'Ga Văn Điển'),
	(NULL, NULL, 4, '01', 'Ga Phú Diễn'),
	(NULL, NULL, 5, '01', 'Ga Phường Mỗ'),
	(NULL, NULL, 6, '01', 'Ga Gia Lâm'),
	(NULL, NULL, 7, '01', 'Ga Long Biên'),
	(NULL, NULL, 8, '01', 'Ga Yên Viên'),
	(NULL, NULL, 9, '01', 'Ga Cổ Loa'),
	(NULL, NULL, 10, '01', 'Ga Đông Anh'),
	(NULL, NULL, 11, '01', 'Ga Thạch Lỗi'),
	(NULL, NULL, 12, '01', 'Ga Đa Phúc'),
	(NULL, NULL, 13, '01', 'Ga Trung Giã'),
	(NULL, NULL, 14, '01', 'Ga Thị Cầu'),
	(NULL, NULL, 15, '10', 'Ga Bảo Hà'),
	(NULL, NULL, 16, '10', 'Ga Thái Băn'),
	(NULL, NULL, 17, '10', 'Ga Phố Lu'),
	(NULL, NULL, 18, '10', 'Ga Thái Niên'),
	(NULL, NULL, 19, '10', 'Ga Lào Cai'),
	(NULL, NULL, 20, '15', 'Ga Yên Bái'),
	(NULL, NULL, 21, '15', 'Ga Cổ Phúc'),
	(NULL, NULL, 22, '15', 'Ga Ngòi Hóp'),
	(NULL, NULL, 23, '15', 'Ga Mậu A'),
	(NULL, NULL, 24, '15', 'Ga Trái Hút'),
	(NULL, NULL, 25, '15', 'Ga Lâm Giang '),
	(NULL, NULL, 26, '15', 'Ga Lang Khay'),
	(NULL, NULL, 27, '15', 'Ga Lang Thíp'),
	(NULL, NULL, 28, '19', 'Ga Lương Sơn'),
	(NULL, NULL, 29, '19', 'Ga Lưu Xá Phổ Yên'),
	(NULL, NULL, 30, '19', 'Ga Quán Triều'),
	(NULL, NULL, 31, '19', 'Ga Thái Nguyên'),
	(NULL, NULL, 32, '20', 'Ga Voi Xô'),
	(NULL, NULL, 33, '20', 'Ga Phố Vị'),
	(NULL, NULL, 34, '20', 'Ga Bắc Lệ'),
	(NULL, NULL, 35, '20', 'Ga Sông Hóa'),
	(NULL, NULL, 36, '20', 'Ga Chi Lăng'),
	(NULL, NULL, 37, '20', 'Ga Đồng Mỏ'),
	(NULL, NULL, 38, '20', 'Ga Bắc Thủy'),
	(NULL, NULL, 39, '20', 'Ga Bản Thí'),
	(NULL, NULL, 40, '20', 'Ga Yên Trạch'),
	(NULL, NULL, 41, '20', 'Ga Lạng Sơn'),
	(NULL, NULL, 42, '20', 'Ga Đồng Đăng'),
	(NULL, NULL, 43, '22', 'Ga Hạ Long'),
	(NULL, NULL, 44, '22', 'Ga Đông Triều'),
	(NULL, NULL, 45, '22', 'Ga Mạo Khê'),
	(NULL, NULL, 46, '22', 'Ga Nam Khê'),
	(NULL, NULL, 47, '22', 'Ga Uông Bí'),
	(NULL, NULL, 48, '22', 'Ga Yên Cư'),
	(NULL, NULL, 49, '22', 'Ga Yên Dưỡng'),
	(NULL, NULL, 50, '22', 'Ga Bàn Cờ'),
	(NULL, NULL, 51, '24', 'Ga Sen Hồ'),
	(NULL, NULL, 52, '24', 'Ga Bắc Giang'),
	(NULL, NULL, 53, '24', 'Ga Phố Tráng'),
	(NULL, NULL, 54, '24', 'Ga Lan Mẫu'),
	(NULL, NULL, 55, '24', 'Ga Bảo Sơn'),
	(NULL, NULL, 56, '24', 'Ga Cảm Lý'),
	(NULL, NULL, 57, '24', 'Ga Lép'),
	(NULL, NULL, 58, '25', 'Ga việt Trì'),
	(NULL, NULL, 59, '25', 'Ga Phủ Đức'),
	(NULL, NULL, 60, '25', 'Ga Tiên Kiên'),
	(NULL, NULL, 61, '25', 'Ga Phú Thọ'),
	(NULL, NULL, 62, '25', 'Ga Chí Chủ'),
	(NULL, NULL, 63, '25', 'Ga Vũ Ẻn'),
	(NULL, NULL, 64, '25', 'Ga Ấm Thượng'),
	(NULL, NULL, 65, '25', 'Ga Đoan Thượng'),
	(NULL, NULL, 66, '26', 'Ga Phúc Yên'),
	(NULL, NULL, 67, '26', 'Ga Vĩnh Yên'),
	(NULL, NULL, 68, '27', 'Ga Từ Sơn'),
	(NULL, NULL, 69, '27', 'Ga Lim'),
	(NULL, NULL, 70, '27', 'Ga Bắc Ninh'),
	(NULL, NULL, 71, '30', 'Ga Cẩm Giàng'),
	(NULL, NULL, 72, '30', 'Ga Chí Linh'),
	(NULL, NULL, 73, '30', 'Ga Hải Dương'),
	(NULL, NULL, 74, '30', 'Ga Phú Thái'),
	(NULL, NULL, 75, '31', 'Ga Thượng Lý'),
	(NULL, NULL, 76, '31', 'Ga Hải Phòng'),
	(NULL, NULL, 77, '35', 'Ga Phủ Lý'),
	(NULL, NULL, 78, '36', 'Ga Nam Định'),
	(NULL, NULL, 79, '37', 'Ga Ninh Bình'),
	(NULL, NULL, 80, '38', 'Ga Bỉm Sơn'),
	(NULL, NULL, 81, '38', 'Ga Thanh Hóa'),
	(NULL, NULL, 82, '38', 'Ga Minh Khôi'),
	(NULL, NULL, 83, '40', 'Ga Cầu Giát'),
	(NULL, NULL, 84, '40', 'Ga Chợ Sy'),
	(NULL, NULL, 85, '40', 'Ga Vinh'),
	(NULL, NULL, 86, '40', 'Ga Yên Xuân'),
	(NULL, NULL, 87, '40', 'Hà Tĩnh'),
	(NULL, NULL, 88, '40', 'Ga Yên Trung'),
	(NULL, NULL, 89, '40', 'Ga Đức Lạc'),
	(NULL, NULL, 90, '40', 'Ga Yên Duệ'),
	(NULL, NULL, 91, '40', 'Ga Hòa Duyệt'),
	(NULL, NULL, 92, '40', 'Ga Thanh Luyệt'),
	(NULL, NULL, 93, '40', 'Ga Chu Lễ'),
	(NULL, NULL, 94, '40', 'Ga Hương Phố'),
	(NULL, NULL, 95, '40', 'Ga Phúc Trạch'),
	(NULL, NULL, 96, '44', 'Ga La Khê'),
	(NULL, NULL, 97, '44', 'Ga Tân Ấp'),
	(NULL, NULL, 98, '44', 'Ga Đồng Chuối'),
	(NULL, NULL, 99, '44', 'Ga Kim Lũ'),
	(NULL, NULL, 100, '44', 'Ga Đồng Lê'),
	(NULL, NULL, 101, '44', 'Ga Ngọc Lâm'),
	(NULL, NULL, 102, '44', 'Ga Lạc Sơn'),
	(NULL, NULL, 103, '44', 'Ga Lê Sơn'),
	(NULL, NULL, 104, '44', 'Ga Ngân Sơn'),
	(NULL, NULL, 105, '44', 'Ga Minh Lệ'),
	(NULL, NULL, 106, '44', 'Ga Thọ Lộc'),
	(NULL, NULL, 107, '44', 'Ga Hoàn Lão'),
	(NULL, NULL, 108, '44', 'Ga Lạc Giao'),
	(NULL, NULL, 109, '44', 'Ga Tự Phúc'),
	(NULL, NULL, 110, '44', 'Ga Đồng Hới'),
	(NULL, NULL, 111, '44', 'Ga Lệ Kỳ'),
	(NULL, NULL, 112, '44', 'Ga Long Đại'),
	(NULL, NULL, 113, '44', 'Ga Mỹ Đức'),
	(NULL, NULL, 114, '44', 'Ga Phú Hòa'),
	(NULL, NULL, 115, '44', 'Ga Mỹ Trạch'),
	(NULL, NULL, 116, '44', 'Ga Thượng Lâm'),
	(NULL, NULL, 117, '44', 'Ga Minh Cầm'),
	(NULL, NULL, 118, '45', 'Ga Sa Lung'),
	(NULL, NULL, 119, '45', 'Ga Tiên An'),
	(NULL, NULL, 120, '45', 'Ga Hà Thanh'),
	(NULL, NULL, 121, '45', 'Ga Đông Hà'),
	(NULL, NULL, 122, '45', 'Ga Quảng Trị'),
	(NULL, NULL, 123, '45', 'Ga Vĩnh Thủy'),
	(NULL, NULL, 124, '45', 'Ga Diên Sanh'),
	(NULL, NULL, 125, '45', 'Ga Mỹ Chánh'),
	(NULL, NULL, 126, '46', 'Ga Phò Trạch'),
	(NULL, NULL, 127, '46', 'Ga Hiền Sỹ'),
	(NULL, NULL, 128, '46', 'Ga Văn Xá'),
	(NULL, NULL, 129, '46', 'Ga Huế'),
	(NULL, NULL, 130, '46', 'Ga Cầu Hai'),
	(NULL, NULL, 131, '46', 'Ga Lăng Cô'),
	(NULL, NULL, 132, '46', 'Ga An Hòa'),
	(NULL, NULL, 133, '46', 'Đà nẵng'),
	(NULL, NULL, 134, '46', 'Ga Kim Liên'),
	(NULL, NULL, 135, '46', 'Ga Đà nẵng'),
	(NULL, NULL, 136, '49', 'Ga Trà Kiệu'),
	(NULL, NULL, 137, '49', 'Ga Phú Cang'),
	(NULL, NULL, 138, '49', 'Ga Tam Kỳ'),
	(NULL, NULL, 139, '49', 'Ga Núi Thành'),
	(NULL, NULL, 140, '52', 'Ga Bồng Sơn'),
	(NULL, NULL, 141, '52', 'Ga Vạn Phú'),
	(NULL, NULL, 142, '52', 'Ga Quy Nhơn'),
	(NULL, NULL, 143, '52', 'Ga Diêu Trì'),
	(NULL, NULL, 144, '52', 'Ga Tân Vinh'),
	(NULL, NULL, 145, '52', 'Ga Vân Canh'),
	(NULL, NULL, 146, '54', 'Ga Phước Lãnh'),
	(NULL, NULL, 147, '54', 'Ga La Hai'),
	(NULL, NULL, 148, '54', 'Ga Tuy Hòa'),
	(NULL, NULL, 149, '54', 'Ga Đông Tác'),
	(NULL, NULL, 150, '54', 'Ga Phú Hiệp'),
	(NULL, NULL, 151, '54', 'Khánh Hòa'),
	(NULL, NULL, 152, '54', 'Ga Đại Lãnh '),
	(NULL, NULL, 153, '54', 'Ga Tu Bông'),
	(NULL, NULL, 154, '54', 'Ga Giã'),
	(NULL, NULL, 155, '54', 'Ga Ninh Hòa'),
	(NULL, NULL, 156, '54', 'Ga Nha Trang'),
	(NULL, NULL, 157, '54', 'Ga Ngã Ba'),
	(NULL, NULL, 158, '58', 'Ga Tháp Chàm'),
	(NULL, NULL, 159, '58', 'Ga Cà Ná'),
	(NULL, NULL, 160, '60', 'Ga Sông Mao'),
	(NULL, NULL, 161, '60', 'Ga Ma Lâm'),
	(NULL, NULL, 162, '60', 'Ga Phan Thiết'),
	(NULL, NULL, 163, '60', 'Ga Bình Thuận'),
	(NULL, NULL, 164, '60', 'Ga Suối Kiến'),
	(NULL, NULL, 165, '60', 'Ga Gia Huynh'),
	(NULL, NULL, 166, '68', 'Ga Đà Lạt'),
	(NULL, NULL, 167, '68', 'Ga Trại Mát'),
	(NULL, NULL, 168, '74', 'Ga Dĩ An'),
	(NULL, NULL, 169, '74', 'Ga Sóng Thần'),
	(NULL, NULL, 170, '75', 'Ga Gia Ray'),
	(NULL, NULL, 171, '75', 'Ga Long Khánh'),
	(NULL, NULL, 172, '75', 'Ga Biên Hòa'),
	(NULL, NULL, 173, '79', 'Ga Sài Gòn');

-- Dumping data for table booking_train.tickets: ~0 rows (approximately)

-- Dumping data for table booking_train.booking_trains: ~26 rows (approximately)
INSERT INTO `booking_trains` (`booking_train_id`, `booking_train_number`, `booking_train_type`) VALUES
	(1, 'SE1', 'Tàu tốc hành Hà Nội - Sài Gòn'),
	(2, 'SE2', 'Tàu tốc hành Sài Gòn - Hà Nội'),
	(3, 'SE3', 'Tàu tốc hành Hà Nội - Sài Gòn'),
	(4, 'SE4', 'Tàu tốc hành Sài Gòn - Hà Nội'),
	(5, 'SE5', 'Tàu tốc hành Hà Nội - Sài Gòn'),
	(6, 'SE6', 'Tàu tốc hành Sài Gòn - Hà Nội'),
	(7, 'SE7', 'Tàu tốc hành Hà Nội - Sài Gòn'),
	(8, 'SE8', 'Tàu tốc hành Sài Gòn - Hà Nội'),
	(9, 'SE9', 'Tàu thường Hà Nội - Sài Gòn'),
	(10, 'SE10', 'Tàu thường Sài Gòn - Hà Nội'),
	(11, 'SE11', 'Tàu thường Hà Nội - Sài Gòn'),
	(12, 'SE12', 'Tàu thường Sài Gòn - Hà Nội'),
	(13, 'TN1', 'Tàu Thống Nhất chậm Hà Nội - Sài Gòn'),
	(14, 'TN2', 'Tàu Thống Nhất chậm Sài Gòn - Hà Nội'),
	(15, 'SNT1', 'Tàu Sài Gòn - Nha Trang'),
	(16, 'SNT2', 'Tàu Nha Trang - Sài Gòn'),
	(17, 'SQN1', 'Tàu Sài Gòn - Quy Nhơn'),
	(18, 'SQN2', 'Tàu Quy Nhơn - Sài Gòn'),
	(19, 'SPT1', 'Tàu Sài Gòn - Phan Thiết'),
	(20, 'SPT2', 'Tàu Phan Thiết - Sài Gòn'),
	(21, 'SP1', 'Tàu Hà Nội - Lào Cai'),
	(22, 'SP2', 'Tàu Lào Cai - Hà Nội'),
	(23, 'NA1', 'Tàu Hà Nội - Vinh'),
	(24, 'NA2', 'Tàu Vinh - Hà Nội'),
	(25, 'HP1', 'Tàu Hà Nội - Hải Phòng'),
	(26, 'HP2', 'Tàu Hải Phòng - Hà Nội');

-- Dumping data for table booking_train.booking_train_schedule: ~34 rows (approximately)
INSERT INTO `booking_train_schedule` (`arrival_date`, `departure_date`, `route_id`, `schedule_id`) VALUES
	('2024-09-19', '2024-09-19', 1, 1),
	('2024-09-19', '2024-09-19', 2, 2),
	('2024-09-19', '2024-09-19', 3, 3),
	('2024-09-19', '2024-09-19', 4, 4),
	('2024-09-19', '2024-09-19', 5, 5),
	('2024-09-19', '2024-09-19', 6, 6),
	('2024-09-19', '2024-09-19', 7, 7),
	('2024-09-19', '2024-09-19', 8, 8),
	('2024-09-19', '2024-09-19', 9, 9),
	('2024-09-19', '2024-09-19', 10, 10),
	('2024-09-19', '2024-09-19', 11, 11),
	('2024-09-19', '2024-09-19', 12, 12),
	('2024-09-19', '2024-09-19', 13, 13),
	('2024-09-19', '2024-09-19', 14, 14),
	('2024-09-19', '2024-09-19', 15, 15),
	('2024-09-19', '2024-09-19', 16, 16),
	('2024-09-20', '2024-09-20', 17, 17),
	('2024-09-20', '2024-09-20', 18, 18),
	('2024-09-20', '2024-09-20', 19, 19),
	('2024-09-20', '2024-09-20', 20, 20),
	('2024-09-20', '2024-09-20', 21, 21),
	('2024-09-20', '2024-09-20', 22, 22),
	('2024-09-20', '2024-09-20', 23, 23),
	('2024-09-20', '2024-09-20', 24, 24),
	('2024-09-20', '2024-09-20', 25, 25),
	('2024-09-20', '2024-09-20', 26, 26),
	('2024-09-20', '2024-09-20', 27, 27),
	('2024-09-20', '2024-09-20', 28, 28),
	('2024-09-20', '2024-09-20', 29, 29),
	('2024-09-20', '2024-09-20', 30, 30),
	('2024-09-20', '2024-09-20', 31, 31),
	('2024-09-20', '2024-09-20', 32, 32),
	('2024-09-20', '2024-09-20', 33, 33),
	('2024-09-20', '2024-09-20', 34, 34);

-- Dumping data for table booking_train.users: ~0 rows (approximately)

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
