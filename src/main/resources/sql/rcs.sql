/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 80019
Source Host           : localhost:9688
Source Database       : gdpt_russia_server_test

Target Server Type    : MYSQL
Target Server Version : 80019
File Encoding         : 65001

Date: 2021-11-11 10:21:02
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for rcs_balance
-- ----------------------------
DROP TABLE IF EXISTS `rcs_balance`;
CREATE TABLE `rcs_balance` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'balance主键',
  `contract_code` varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '合同id',
  `contract_current` double(10,2) NOT NULL DEFAULT '0.00' COMMENT '【待定】当前可用资金',
  `consolidate` double(10,2) NOT NULL DEFAULT '0.00' COMMENT '当前余额',
  `operation_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '【待定】',
  `from_type` int DEFAULT NULL COMMENT '1 服务器发送给俄罗斯，俄罗斯返回的信息  2 俄罗斯发给服务器的数据',
  `created_time` datetime NOT NULL COMMENT '创建时间',
  `russia_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `contract_id` (`contract_code`)
) ENGINE=InnoDB AUTO_INCREMENT=3512 DEFAULT CHARSET=utf8 COMMENT='数据同步记录表';

-- ----------------------------
-- Records of rcs_balance
-- ----------------------------
INSERT INTO `rcs_balance` VALUES ('3481', '00000177', '10250.00', '10250.00', 'f2f6e49c-5d1c-4b12-9af2-11bd952d0d70', '2', '2021-10-19 14:55:55', '2021-10-19 11:56:04');
INSERT INTO `rcs_balance` VALUES ('3482', '00000177', '10250.00', '10250.00', '42c45f8c-1b79-47cd-a4fb-697a27f5b15e', '2', '2021-10-19 14:59:00', '2021-10-19 11:56:04');
INSERT INTO `rcs_balance` VALUES ('3483', '00000177', '10500.00', '10500.00', 'bac7999b-0c4c-4eb5-8854-b9f84d4cd91d', '2', '2021-10-19 14:59:35', '2021-10-19 11:57:37');
INSERT INTO `rcs_balance` VALUES ('3484', '00000177', '10000.00', '10500.00', null, '1', '2021-10-19 16:24:51', '2021-10-19 13:25:03');
INSERT INTO `rcs_balance` VALUES ('3485', '00000177', '10000.00', '10500.00', '8b6ee09e-ace3-489d-8811-714b690f4fe4', '2', '2021-10-19 16:24:54', '2021-10-19 13:25:03');
INSERT INTO `rcs_balance` VALUES ('3486', '00000177', '10500.00', '10500.00', '2b5f5b71-af4c-4d08-af7d-79ede97e3350', '2', '2021-10-19 16:26:39', '2021-10-19 13:26:48');
INSERT INTO `rcs_balance` VALUES ('3487', '00000177', '10500.00', '10500.00', null, '1', '2021-10-19 16:26:39', '2021-10-19 13:26:48');
INSERT INTO `rcs_balance` VALUES ('3488', '00000177', '10000.00', '10500.00', null, '1', '2021-10-19 16:30:45', '2021-10-19 13:30:58');
INSERT INTO `rcs_balance` VALUES ('3489', '00000177', '10000.00', '10500.00', '2dbb3746-89d2-48b5-8fb4-217ed037f576', '2', '2021-10-19 16:30:48', '2021-10-19 13:30:58');
INSERT INTO `rcs_balance` VALUES ('3490', '00000177', '10460.00', '10460.00', null, '1', '2021-10-19 16:31:39', '2021-10-19 13:31:52');
INSERT INTO `rcs_balance` VALUES ('3491', '00000177', '10500.00', '10500.00', '6e2c48eb-8035-460e-bb65-f7163975193a', '2', '2021-10-19 16:31:43', '2021-10-19 13:31:52');
INSERT INTO `rcs_balance` VALUES ('3492', '00000177', '9960.00', '10460.00', '7a5832ec-87e5-49d1-8f30-a5e6a3c3e2c3', '2', '2021-10-26 12:41:18', '2021-10-26 09:41:27');
INSERT INTO `rcs_balance` VALUES ('3493', '00000177', '9960.00', '10460.00', null, '1', '2021-10-26 12:41:19', '2021-10-26 09:41:27');
INSERT INTO `rcs_balance` VALUES ('3494', '00000177', '10460.00', '10460.00', '3282b158-ee31-4ff8-87b6-68bc21767b10', '2', '2021-10-26 12:43:55', '2021-10-26 09:44:04');
INSERT INTO `rcs_balance` VALUES ('3495', '00000177', '10430.00', '10430.00', null, '1', '2021-10-26 12:43:55', '2021-10-26 09:44:04');
INSERT INTO `rcs_balance` VALUES ('3496', '00000177', '10409.30', '10430.00', '1f9ec6d6-a9fe-40e5-9050-ead9962fe0fb', '2', '2021-10-28 17:44:16', '2021-10-28 14:44:13');
INSERT INTO `rcs_balance` VALUES ('3497', '00000177', '10430.00', '10430.00', '03011f9a-75c0-4713-b4ad-a0b39ca4b8fa', '2', '2021-10-28 17:44:56', '2021-10-28 14:44:54');
INSERT INTO `rcs_balance` VALUES ('3498', '00000177', '100000.00', '100000.00', null, '1', '2021-10-30 12:41:11', '2021-10-30 12:41:11');
INSERT INTO `rcs_balance` VALUES ('3499', '00000177', '100000.00', '100000.00', null, '1', '2021-10-30 12:41:40', '2021-10-30 12:41:40');
INSERT INTO `rcs_balance` VALUES ('3500', '00000177', '100000.00', '100000.00', null, '1', '2021-10-30 12:42:54', '2021-10-30 12:42:54');
INSERT INTO `rcs_balance` VALUES ('3501', '00000177', '100000.00', '100000.00', null, '1', '2021-10-30 12:44:38', '2021-10-30 12:44:38');
INSERT INTO `rcs_balance` VALUES ('3502', '00000177', '100000.00', '100000.00', null, '1', '2021-10-30 12:56:04', '2021-10-30 12:56:04');
INSERT INTO `rcs_balance` VALUES ('3503', '00000177', '100000.00', '100000.00', null, '1', '2021-10-30 13:20:25', '2021-10-30 13:20:25');
INSERT INTO `rcs_balance` VALUES ('3504', '00000177', '100000.00', '100000.00', null, '1', '2021-10-30 13:20:53', '2021-10-30 13:20:53');
INSERT INTO `rcs_balance` VALUES ('3505', '00000177', '100000.00', '100000.00', null, '1', '2021-10-30 13:58:50', '2021-10-30 13:58:50');
INSERT INTO `rcs_balance` VALUES ('3506', '00000177', '100000.00', '100000.00', null, '1', '2021-10-30 14:04:53', '2021-10-30 14:04:53');
INSERT INTO `rcs_balance` VALUES ('3507', '00000177', '100000.00', '100000.00', null, '1', '2021-11-01 15:35:12', '2021-11-01 15:35:12');
INSERT INTO `rcs_balance` VALUES ('3508', '00000177', '100000.00', '100000.00', null, '1', '2021-11-01 15:35:31', '2021-11-01 15:35:31');
INSERT INTO `rcs_balance` VALUES ('3509', '00000177', '100000.00', '100000.00', null, '1', '2021-11-01 15:36:15', '2021-11-01 15:36:15');
INSERT INTO `rcs_balance` VALUES ('3510', '00000177', '100000.00', '100000.00', null, '1', '2021-11-01 15:38:49', '2021-11-01 15:38:49');
INSERT INTO `rcs_balance` VALUES ('3511', '00000177', '100000.00', '100000.00', null, '1', '2021-11-01 15:39:31', '2021-11-01 15:39:31');

-- ----------------------------
-- Table structure for rcs_contract
-- ----------------------------
DROP TABLE IF EXISTS `rcs_contract`;
CREATE TABLE `rcs_contract` (
  `code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `name` varchar(150) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '合同号的数字，废弃',
  `enable` int NOT NULL COMMENT '【待定】0 不可用 1 可用',
  `current` double(10,2) DEFAULT '0.00' COMMENT '当前可用资金',
  `consolidate` double(10,2) DEFAULT '0.00',
  `created_time` datetime NOT NULL COMMENT '创建时间',
  `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
  `modified` datetime DEFAULT NULL COMMENT 'contract的更新时间',
  `balance_modified` datetime DEFAULT NULL COMMENT ' balance的更新时间 我们的balance被调用成功后，才更新这个字段  访问foeseen/transaction后返回的balance不影响这个字段',
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='合同表';

-- ----------------------------
-- Records of rcs_contract
-- ----------------------------
INSERT INTO `rcs_contract` VALUES ('00000001', '642(оу)', '1', '0.00', '0.00', '2021-10-19 12:22:48', '2021-10-19 12:22:48', '2021-10-19 12:22:25', null);
INSERT INTO `rcs_contract` VALUES ('00000177', '173(оу)', '1', '100000.00', '100000.00', '2021-10-19 16:20:09', '2021-11-01 15:39:31', '2021-10-19 16:20:19', '2021-10-28 14:44:54');
INSERT INTO `rcs_contract` VALUES ('00001010', 'КР/376', '1', '10336.56', '12836.56', '2021-10-14 13:35:15', '2021-10-19 12:46:29', '2021-10-14 13:35:12', '2021-10-19 09:46:37');
INSERT INTO `rcs_contract` VALUES ('00001227', 'КРС/2019/КР/5', '1', '0.00', '0.00', '2021-10-28 18:06:46', '2021-10-28 18:06:46', '2021-10-28 18:06:44', null);
INSERT INTO `rcs_contract` VALUES ('00003879', '327(оу)', '1', '0.00', '0.00', '2021-10-28 18:02:22', '2021-10-28 18:02:22', '2021-10-28 18:02:20', null);

-- ----------------------------
-- Table structure for rcs_contract_address
-- ----------------------------
DROP TABLE IF EXISTS `rcs_contract_address`;
CREATE TABLE `rcs_contract_address` (
  `id` int NOT NULL AUTO_INCREMENT,
  `contract_code` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `contractId` (`contract_code`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of rcs_contract_address
-- ----------------------------
INSERT INTO `rcs_contract_address` VALUES ('1', '00001010', 'Иркутская обл, Иркутск г', '2021-10-16 09:24:56');
INSERT INTO `rcs_contract_address` VALUES ('2', '00001010', 'Иркутская обл, Иркутск г 2222', '2021-10-16 09:27:39');
INSERT INTO `rcs_contract_address` VALUES ('16', '00000177', 'Иркутская обл, Иркутск г 2222', '2021-10-30 12:52:49');
INSERT INTO `rcs_contract_address` VALUES ('17', '00000177', 'Иркутская обл, Иркутск г 666', '2021-10-30 12:53:05');

-- ----------------------------
-- Table structure for rcs_customer
-- ----------------------------
DROP TABLE IF EXISTS `rcs_customer`;
CREATE TABLE `rcs_customer` (
  `id` varchar(64) NOT NULL,
  `contract_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '合同id',
  `name` varchar(50) NOT NULL COMMENT '【待定】',
  `inn_ru` varchar(50) NOT NULL COMMENT '【待定】',
  `kpp_ru` varchar(50) NOT NULL COMMENT '【待定】',
  `legal_address` varchar(255) DEFAULT NULL,
  `office_address` varchar(255) DEFAULT NULL,
  `created_time` datetime NOT NULL COMMENT '创建时间',
  `updated_time` datetime NOT NULL COMMENT '更新时间',
  `modified` datetime DEFAULT NULL COMMENT '俄罗斯时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';

-- ----------------------------
-- Records of rcs_customer
-- ----------------------------
INSERT INTO `rcs_customer` VALUES ('0008d2cd-549e-11ea-80e8-00155d027901', '00000177', 'РОО \"Защита прав потребителей\" Пензенской области', '5836688269', '583601001', '440000, г. Пенза, ул. Кирова, 42 офис 15', '440000, г. Пенза, ул. Кирова, 42 офис 15', '2021-10-19 16:20:09', '2021-10-19 16:20:09', '2021-10-19 16:20:19');
INSERT INTO `rcs_customer` VALUES ('002229fc-e8b8-11e9-80ed-00155dfa0f02', '00001227', 'ПАО \"ВымпелКом\"', '7713076301', '771301001', 'РФ, 127083, г. Москва, ул. Восьмого Марта, д. 10, стр. 14', '660135, г. Красноярск, ул. Взлетная, д. 3', '2021-10-28 18:06:46', '2021-10-28 18:06:46', '2021-10-28 18:06:44');
INSERT INTO `rcs_customer` VALUES ('03f29219-158d-11e5-80c9-b4b52f64dcf2', '00003879', 'ООО \"Пензенская управляющая организация\"', '5835109663', '583501001', 'г.Пенза,5-ый Виноградный проезд ,д.8', 'г.Пенза,5-ый Виноградный проезд ,д.8', '2021-10-28 18:02:22', '2021-10-28 18:02:22', '2021-10-28 18:02:20');
INSERT INTO `rcs_customer` VALUES ('98458965-e306-11e3-a36e-2c59e5453bd0', '00001010', 'Общество с ограниченной ответственностью \"КИНК\"', '2466108959', '502401001', '143404, Московская область, г. Красногорск, ул. Дачная, д. 11а, помещение 14/8. комната 9', '660011, г.Красноярск, ул.Сосновый бор, д.40', '2021-10-14 13:35:15', '2021-10-14 13:35:15', '2021-10-14 13:35:12');
INSERT INTO `rcs_customer` VALUES ('ee49eff4-c447-11e2-88db-b4b52f64ecfe', '00000001', 'ПАО \"Ростелеком\"', '7707049388', '583643001', '191002, Россия, г.Санкт-Петербург, ул.Достоевского, д.15', '440000, Россия, г.Пенза, ул.Кирова, строение 54', '2021-10-19 12:22:48', '2021-10-19 12:22:48', '2021-10-19 12:22:25');

-- ----------------------------
-- Table structure for rcs_fm_status_log
-- ----------------------------
DROP TABLE IF EXISTS `rcs_fm_status_log`;
CREATE TABLE `rcs_fm_status_log` (
  `id` int NOT NULL AUTO_INCREMENT,
  `frank_machine_id` varchar(64) NOT NULL COMMENT '机器id',
  `change_from` int DEFAULT NULL COMMENT '提交改变方：1 机器 2 俄罗斯',
  `interface_name` int DEFAULT NULL COMMENT '接口名',
  `cur_fm_status` int DEFAULT NULL COMMENT '机器当前状态',
  `future_fm_status` int DEFAULT NULL COMMENT '机器想要达到的状态',
  `flow` int DEFAULT NULL COMMENT '流程 0 未闭环   1 闭环',
  `flow_detail` int DEFAULT NULL COMMENT '各种FlowXXXEnum的状态',
  `post_office` varchar(6) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '邮局信息',
  `tax_version` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'tax版本',
  `fm_event` int DEFAULT NULL COMMENT '1 STATUS \r\n									 2 RATE_TABLE_UPDATE',
  `error_code` varchar(6) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '错误代码',
  `error_message` varchar(64) DEFAULT NULL COMMENT '错误信息',
  `created_time` datetime NOT NULL COMMENT '创建时间',
  `updated_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `frank_machine_id` (`frank_machine_id`)
) ENGINE=InnoDB AUTO_INCREMENT=140 DEFAULT CHARSET=utf8 COMMENT='机器状态变更表';

-- ----------------------------
-- Records of rcs_fm_status_log
-- ----------------------------
INSERT INTO `rcs_fm_status_log` VALUES ('24', 'PM200500', '1', '4', '0', '3', '0', '0', '131000', '11.1.2', '1', null, null, '2021-05-13 09:36:26', '2021-10-12 14:19:34');
INSERT INTO `rcs_fm_status_log` VALUES ('25', 'PM200500', '2', '1', '1', '1', '1', '21', '131000', '11.1.2', '1', null, null, '2021-05-13 09:36:26', '2021-10-12 14:19:34');
INSERT INTO `rcs_fm_status_log` VALUES ('26', 'PM200500', '1', '4', '1', '3', '0', '21', '131000', '11.1.2', '1', null, null, '2021-05-13 09:36:26', '2021-10-12 14:19:38');
INSERT INTO `rcs_fm_status_log` VALUES ('27', 'PM200500', '1', '4', '0', '3', '0', '0', '131000', '11.1.2', '1', null, null, '2021-05-13 09:36:26', '2021-10-12 14:36:19');
INSERT INTO `rcs_fm_status_log` VALUES ('28', 'PM200500', '2', '1', '1', '1', '1', '21', '131000', '11.1.2', '1', null, null, '2021-05-13 09:36:26', '2021-10-12 14:36:19');
INSERT INTO `rcs_fm_status_log` VALUES ('29', 'PM200500', '1', '4', '1', '3', '0', '21', '131000', '11.1.2', '1', null, null, '2021-05-13 09:36:26', '2021-10-12 14:36:23');
INSERT INTO `rcs_fm_status_log` VALUES ('30', 'PM200500', '0', '1', '1', '1', '1', '21', '131000', '11.1.2', '1', null, null, '2021-05-13 09:36:26', '2021-10-12 16:54:42');
INSERT INTO `rcs_fm_status_log` VALUES ('31', 'PM200500', '1', '4', '1', '3', '0', '14', '131000', '11.1.2', '1', null, null, '2021-05-13 09:36:26', '2021-10-12 16:54:51');
INSERT INTO `rcs_fm_status_log` VALUES ('32', 'PM200500', '1', '4', '3', '3', '1', '11', null, null, null, null, null, '2021-10-13 09:13:51', '2021-10-13 09:13:51');
INSERT INTO `rcs_fm_status_log` VALUES ('33', 'PM200500', '0', '1', '1', '1', '1', '21', '131000', '11.1.2', '1', null, null, '2021-05-13 09:36:26', '2021-10-13 09:21:24');
INSERT INTO `rcs_fm_status_log` VALUES ('34', 'PM200500', '1', '4', '1', '2', '0', '14', '131000', '11.1.2', '1', null, null, '2021-05-13 09:36:26', '2021-10-13 09:21:38');
INSERT INTO `rcs_fm_status_log` VALUES ('35', 'PM200500', '0', '1', '1', '1', '1', '21', '131000', '11.1.2', '1', null, null, '2021-05-13 09:36:26', '2021-10-13 09:27:32');
INSERT INTO `rcs_fm_status_log` VALUES ('36', 'PM200500', '1', '4', '1', '2', '0', '14', '131000', '11.1.2', '1', null, null, '2021-05-13 09:36:26', '2021-10-13 09:28:00');
INSERT INTO `rcs_fm_status_log` VALUES ('37', 'PM200500', '0', '1', '1', '1', '1', '21', '131000', '11.1.2', '1', null, null, '2021-05-13 09:36:26', '2021-10-13 09:41:28');
INSERT INTO `rcs_fm_status_log` VALUES ('38', 'PM200500', '1', '4', '1', '2', '0', '14', '131000', '11.1.2', '1', null, null, '2021-05-13 09:36:26', '2021-10-13 09:41:40');
INSERT INTO `rcs_fm_status_log` VALUES ('39', 'PM200500', '0', '1', '1', '1', '1', '21', '131000', '11.1.2', '1', null, null, '2021-05-13 09:36:26', '2021-10-13 09:58:31');
INSERT INTO `rcs_fm_status_log` VALUES ('40', 'PM200500', '1', '4', '1', '2', '0', '14', '131000', '11.1.2', '1', null, null, '2021-05-13 09:36:26', '2021-10-13 09:58:51');
INSERT INTO `rcs_fm_status_log` VALUES ('41', 'PM200500', '1', '4', '2', '2', '1', '11', null, null, null, null, null, '2021-10-13 09:58:52', '2021-10-13 09:58:52');
INSERT INTO `rcs_fm_status_log` VALUES ('42', 'PM200500', '0', '1', '1', '1', '1', '21', '131000', '11.1.2', '1', null, null, '2021-05-13 09:36:26', '2021-10-13 09:59:40');
INSERT INTO `rcs_fm_status_log` VALUES ('43', 'PM200500', '1', '4', '1', '1', '0', '14', '131000', '11.1.2', '1', null, null, '2021-05-13 09:36:26', '2021-10-13 10:00:07');
INSERT INTO `rcs_fm_status_log` VALUES ('44', 'PM200500', '1', '4', '1', '1', '1', '11', null, null, null, null, null, '2021-10-13 10:00:10', '2021-10-13 10:00:10');
INSERT INTO `rcs_fm_status_log` VALUES ('45', 'PM200500', '1', '4', '0', '3', '0', '14', '131000', '11.1.2', '1', null, null, '2021-05-13 09:36:26', '2021-10-13 12:07:49');
INSERT INTO `rcs_fm_status_log` VALUES ('46', 'PM200500', '0', '1', '1', '1', '1', '21', '131000', '11.1.2', '1', null, null, '2021-05-13 09:36:26', '2021-10-13 12:07:50');
INSERT INTO `rcs_fm_status_log` VALUES ('47', 'PM200500', '1', '4', '3', '3', '1', '11', null, null, null, null, null, '2021-10-13 12:07:51', '2021-10-13 12:07:51');
INSERT INTO `rcs_fm_status_log` VALUES ('48', 'PM200500', '0', '1', '3', '1', '1', '22', '131000', '11.1.2', '1', null, null, '2021-05-13 09:36:26', '2021-10-13 12:29:18');
INSERT INTO `rcs_fm_status_log` VALUES ('49', 'PM200500', '1', '4', '3', '3', '0', '14', '131000', '11.1.2', '1', null, null, '2021-05-13 09:36:26', '2021-10-13 12:32:52');
INSERT INTO `rcs_fm_status_log` VALUES ('50', 'PM200500', '0', '1', '1', '1', '1', '21', '131000', '11.1.2', '1', null, null, '2021-05-13 09:36:26', '2021-10-13 12:32:52');
INSERT INTO `rcs_fm_status_log` VALUES ('51', 'PM200500', '1', '4', '3', '3', '1', '11', null, null, null, null, null, '2021-10-13 12:32:54', '2021-10-13 12:32:54');
INSERT INTO `rcs_fm_status_log` VALUES ('52', 'PM200500', '1', '4', '3', '2', '0', '14', '131000', '11.1.2', '1', null, null, '2021-05-13 09:36:26', '2021-10-13 13:31:50');
INSERT INTO `rcs_fm_status_log` VALUES ('53', 'PM200500', '1', '4', '2', '2', '1', '11', null, null, null, null, null, '2021-10-13 13:31:52', '2021-10-13 13:31:52');
INSERT INTO `rcs_fm_status_log` VALUES ('54', 'PM200500', '1', '4', '2', '1', '0', '14', '131000', '11.1.2', '1', null, null, '2021-05-13 09:36:26', '2021-10-13 13:35:53');
INSERT INTO `rcs_fm_status_log` VALUES ('55', 'PM200500', '1', '4', '1', '1', '1', '11', null, null, null, null, null, '2021-10-13 13:35:55', '2021-10-13 13:35:55');
INSERT INTO `rcs_fm_status_log` VALUES ('56', 'PM200500', '1', '4', '1', '3', '0', '14', '131000', '11.1.2', '1', null, null, '2021-05-13 09:36:26', '2021-10-13 13:39:12');
INSERT INTO `rcs_fm_status_log` VALUES ('57', 'PM200500', '1', '4', '3', '3', '1', '11', null, null, null, null, null, '2021-10-13 13:39:13', '2021-10-13 13:39:13');
INSERT INTO `rcs_fm_status_log` VALUES ('58', 'PM200500', '1', '4', '3', '1', '0', '14', '131000', '11.1.2', '1', null, null, '2021-05-13 09:36:26', '2021-10-14 12:15:04');
INSERT INTO `rcs_fm_status_log` VALUES ('59', 'PM200500', '1', '4', '1', '1', '1', '11', null, null, null, null, null, '2021-10-14 12:15:06', '2021-10-14 12:15:06');
INSERT INTO `rcs_fm_status_log` VALUES ('60', 'PM200500', '1', '4', '1', '1', '0', '14', '131000', '11.1.2', '1', null, null, '2021-05-13 09:36:26', '2021-10-14 13:14:34');
INSERT INTO `rcs_fm_status_log` VALUES ('61', 'PM200500', '1', '4', '1', '1', '1', '11', null, null, null, null, null, '2021-10-14 13:14:37', '2021-10-14 13:14:37');
INSERT INTO `rcs_fm_status_log` VALUES ('62', 'PM200500', '0', '2', '4', '4', '1', '31', '131000', '11.1.2', '1', null, null, '2021-05-13 09:36:26', '2021-10-15 17:40:41');
INSERT INTO `rcs_fm_status_log` VALUES ('63', 'PM200500', '0', '3', '5', '5', '1', '41', '131000', '11.1.2', '1', null, null, '2021-05-13 09:36:26', '2021-10-15 17:42:35');
INSERT INTO `rcs_fm_status_log` VALUES ('64', 'PM200500', '0', '4', null, '5', '1', '12', null, null, null, null, null, '2021-10-15 17:42:35', '2021-10-15 17:42:35');
INSERT INTO `rcs_fm_status_log` VALUES ('65', 'PM200500', '0', '2', '5', '4', '1', '32', '131000', '11.1.2', '1', null, null, '2021-05-13 09:36:26', '2021-10-15 17:42:44');
INSERT INTO `rcs_fm_status_log` VALUES ('66', 'PM200500', '0', '2', '5', '4', '1', '32', '131000', '11.1.2', '1', null, null, '2021-05-13 09:36:26', '2021-10-15 17:42:53');
INSERT INTO `rcs_fm_status_log` VALUES ('67', 'PM200500', '0', '2', '5', '4', '1', '32', '131000', '11.1.2', '1', null, null, '2021-05-13 09:36:26', '2021-10-15 17:44:20');
INSERT INTO `rcs_fm_status_log` VALUES ('68', 'PM200500', '0', '2', '4', '4', '1', '31', '131000', '11.1.2', '1', null, null, '2021-05-13 09:36:26', '2021-10-15 17:48:05');
INSERT INTO `rcs_fm_status_log` VALUES ('69', 'PM200500', '0', '2', '4', '4', '1', '32', '131000', '11.1.2', '1', null, null, '2021-05-13 09:36:26', '2021-10-15 17:49:29');
INSERT INTO `rcs_fm_status_log` VALUES ('70', 'PM200500', '0', '3', '5', '5', '1', '41', '131000', '11.1.2', '1', null, null, '2021-05-13 09:36:26', '2021-10-15 17:50:35');
INSERT INTO `rcs_fm_status_log` VALUES ('71', 'PM200500', '0', '4', null, '5', '1', '12', null, null, null, null, null, '2021-10-15 17:50:35', '2021-10-15 17:50:35');
INSERT INTO `rcs_fm_status_log` VALUES ('72', 'PM200500', '0', '3', '5', '5', '1', '42', '131000', '11.1.2', '1', null, null, '2021-05-13 09:36:26', '2021-10-15 17:58:38');
INSERT INTO `rcs_fm_status_log` VALUES ('73', 'PM200500', '0', '3', '5', '5', '1', '41', '131000', '11.1.2', '1', null, null, '2021-05-13 09:36:26', '2021-10-15 18:00:11');
INSERT INTO `rcs_fm_status_log` VALUES ('74', 'PM200500', '0', '4', null, '5', '1', '12', null, null, null, null, null, '2021-10-15 18:00:12', '2021-10-15 18:00:12');
INSERT INTO `rcs_fm_status_log` VALUES ('75', 'PM200500', '0', '3', '5', '5', '1', '41', '131000', '11.1.2', '1', null, null, '2021-05-13 09:36:26', '2021-10-15 18:02:56');
INSERT INTO `rcs_fm_status_log` VALUES ('76', 'PM200500', '0', '4', '5', '5', '1', '11', null, null, null, null, null, '2021-10-15 18:02:56', '2021-10-15 18:02:56');
INSERT INTO `rcs_fm_status_log` VALUES ('77', 'PM200500', '0', '3', '5', '5', '1', '41', '131000', '11.1.2', '1', null, null, '2021-05-13 09:36:26', '2021-10-15 18:03:14');
INSERT INTO `rcs_fm_status_log` VALUES ('78', 'PM200500', '0', '4', '5', '5', '1', '11', null, null, null, null, null, '2021-10-15 18:03:14', '2021-10-15 18:03:14');
INSERT INTO `rcs_fm_status_log` VALUES ('79', 'PM200500', '0', '3', '5', '5', '1', '41', '131000', '11.1.2', '1', null, null, '2021-05-13 09:36:26', '2021-10-15 18:03:32');
INSERT INTO `rcs_fm_status_log` VALUES ('80', 'PM200500', '0', '4', '5', '5', '1', '11', null, null, null, null, null, '2021-10-15 18:03:32', '2021-10-15 18:03:32');
INSERT INTO `rcs_fm_status_log` VALUES ('81', 'PM200500', '0', '2', '4', '4', '1', '31', '131000', '11.1.2', '1', null, null, '2021-05-13 09:36:26', '2021-10-15 18:03:41');
INSERT INTO `rcs_fm_status_log` VALUES ('82', 'PM200500', '0', '3', '5', '5', '1', '41', '131000', '11.1.2', '1', null, null, '2021-05-13 09:36:26', '2021-10-15 18:03:53');
INSERT INTO `rcs_fm_status_log` VALUES ('83', 'PM200500', '0', '4', '5', '5', '1', '11', null, null, null, null, null, '2021-10-15 18:03:53', '2021-10-15 18:03:53');
INSERT INTO `rcs_fm_status_log` VALUES ('84', 'PM200500', '0', '3', '5', '5', '1', '41', '131000', '11.1.2', '1', null, null, '2021-05-13 09:36:26', '2021-10-15 18:04:02');
INSERT INTO `rcs_fm_status_log` VALUES ('85', 'PM200500', '0', '4', '5', '5', '1', '11', null, null, null, null, null, '2021-10-15 18:04:02', '2021-10-15 18:04:02');
INSERT INTO `rcs_fm_status_log` VALUES ('86', 'PM200500', '0', '3', '5', '5', '1', '41', '131000', '11.1.2', '1', null, null, '2021-05-13 09:36:26', '2021-10-15 18:04:11');
INSERT INTO `rcs_fm_status_log` VALUES ('87', 'PM200500', '0', '4', null, '5', '1', '12', null, null, null, null, null, '2021-10-15 18:04:12', '2021-10-15 18:04:12');
INSERT INTO `rcs_fm_status_log` VALUES ('88', 'PM200500', '0', '3', '5', '5', '1', '41', '131000', '11.1.2', '1', null, null, '2021-05-13 09:36:26', '2021-10-15 18:07:38');
INSERT INTO `rcs_fm_status_log` VALUES ('89', 'PM200500', '0', '3', '5', '5', '1', '42', '131000', '11.1.2', '1', null, null, '2021-05-13 09:36:26', '2021-10-15 18:08:44');
INSERT INTO `rcs_fm_status_log` VALUES ('90', 'PM200500', '0', '3', '5', '5', '1', '42', '131000', '11.1.2', '1', null, null, '2021-05-13 09:36:26', '2021-10-15 18:09:20');
INSERT INTO `rcs_fm_status_log` VALUES ('91', 'PM200500', '0', '2', '5', '4', '1', '32', '131000', '11.1.2', '1', null, null, '2021-05-13 09:36:26', '2021-10-15 18:10:50');
INSERT INTO `rcs_fm_status_log` VALUES ('92', 'PM200500', '0', '1', '5', '1', '1', '22', '131000', '11.1.2', '1', null, null, '2021-05-13 09:36:26', '2021-10-15 18:21:30');
INSERT INTO `rcs_fm_status_log` VALUES ('93', 'PM200500', '0', '1', '5', '1', '1', '22', '131000', '11.1.2', '1', null, null, '2021-05-13 09:36:26', '2021-10-16 09:21:42');
INSERT INTO `rcs_fm_status_log` VALUES ('94', 'PM200500', '0', '1', '1', '1', '1', '21', '131000', '11.1.2', '1', null, null, '2021-05-13 09:36:26', '2021-10-16 09:22:19');
INSERT INTO `rcs_fm_status_log` VALUES ('95', 'PM200500', '0', '1', '1', '1', '1', '21', '131000', '11.1.2', '1', null, null, '2021-05-13 09:36:26', '2021-10-16 11:02:15');
INSERT INTO `rcs_fm_status_log` VALUES ('96', 'PM200500', '1', '4', '1', '3', '0', '14', '131000', '11.1.2', '1', null, null, '2021-05-13 09:36:26', '2021-10-18 14:21:16');
INSERT INTO `rcs_fm_status_log` VALUES ('97', 'PM200500', '1', '4', '3', '3', '1', '11', null, null, null, null, null, '2021-10-18 14:30:25', '2021-10-18 14:30:25');
INSERT INTO `rcs_fm_status_log` VALUES ('98', 'PM200500', '1', '4', '3', '1', '0', '14', '131000', '11.1.2', '1', null, null, '2021-05-13 09:36:26', '2021-10-18 14:32:01');
INSERT INTO `rcs_fm_status_log` VALUES ('99', 'PM200500', '1', '4', '1', '1', '1', '11', null, null, null, null, null, '2021-10-18 14:32:03', '2021-10-18 14:32:03');
INSERT INTO `rcs_fm_status_log` VALUES ('100', 'PM100200', '0', '1', '1', '1', '1', '21', '131000', '11.1.2', '1', null, null, '2021-05-13 09:36:26', '2021-10-19 13:55:59');
INSERT INTO `rcs_fm_status_log` VALUES ('101', 'PM100200', '1', '4', '1', '3', '0', '14', '131000', '11.1.2', '1', null, null, '2021-05-13 09:36:26', '2021-10-19 13:56:03');
INSERT INTO `rcs_fm_status_log` VALUES ('102', 'PM100200', '1', '4', '3', '3', '1', '11', null, null, null, null, null, '2021-10-19 13:56:11', '2021-10-19 13:56:11');
INSERT INTO `rcs_fm_status_log` VALUES ('103', 'PM100200', '1', '4', '3', '1', '0', '14', '131000', '11.1.5', '1', null, null, '2021-05-13 09:36:26', '2021-10-19 14:40:46');
INSERT INTO `rcs_fm_status_log` VALUES ('104', 'PM100200', '1', '4', '1', '1', '1', '11', null, null, null, null, null, '2021-10-19 14:40:52', '2021-10-19 14:40:52');
INSERT INTO `rcs_fm_status_log` VALUES ('105', 'PM100200', '1', '4', '1', '1', '0', '14', '131000', '11.1.5', '1', null, null, '2021-05-13 09:36:26', '2021-10-19 14:44:50');
INSERT INTO `rcs_fm_status_log` VALUES ('106', 'PM100200', '1', '4', null, '1', '1', '13', null, null, null, null, null, '2021-10-19 14:44:52', '2021-10-19 14:44:52');
INSERT INTO `rcs_fm_status_log` VALUES ('107', 'PM100200', '1', '4', '1', '2', '0', '14', '131000', '11.1.5', '1', null, null, '2021-05-13 09:36:26', '2021-10-19 14:45:25');
INSERT INTO `rcs_fm_status_log` VALUES ('108', 'PM100200', '1', '4', null, '2', '1', '13', null, null, null, null, null, '2021-10-19 14:45:37', '2021-10-19 14:45:37');
INSERT INTO `rcs_fm_status_log` VALUES ('109', 'PM100200', '1', '4', '1', '2', '0', '14', '131000', '11.1.5', '1', null, null, '2021-05-13 09:36:26', '2021-10-19 14:47:46');
INSERT INTO `rcs_fm_status_log` VALUES ('110', 'PM100200', '1', '4', '2', '2', '1', '11', null, null, null, null, null, '2021-10-19 14:48:01', '2021-10-19 14:48:01');
INSERT INTO `rcs_fm_status_log` VALUES ('111', 'PM100200', '1', '4', '2', '1', '0', '14', '131000', '11.1.5', '1', null, null, '2021-05-13 09:36:26', '2021-10-19 16:22:06');
INSERT INTO `rcs_fm_status_log` VALUES ('112', 'PM100200', '1', '4', '1', '1', '1', '11', null, null, null, null, null, '2021-10-19 16:22:08', '2021-10-19 16:22:08');
INSERT INTO `rcs_fm_status_log` VALUES ('113', 'PM100200', '0', '1', '1', '1', '1', '22', '131000', '11.1.5', '1', null, null, '2021-05-13 09:36:26', '2021-10-19 17:13:08');
INSERT INTO `rcs_fm_status_log` VALUES ('114', 'PM100200', '0', '2', '4', '4', '1', '31', '131000', '11.1.5', '1', null, null, '2021-05-13 09:36:26', '2021-10-19 17:13:20');
INSERT INTO `rcs_fm_status_log` VALUES ('115', 'PM100200', '0', '2', '4', '4', '1', '32', '131000', '11.1.5', '1', null, null, '2021-05-13 09:36:26', '2021-10-19 17:13:29');
INSERT INTO `rcs_fm_status_log` VALUES ('116', 'PM100200', '0', '3', '5', '5', '1', '41', '131000', '11.1.5', '1', null, null, '2021-05-13 09:36:26', '2021-10-19 17:14:59');
INSERT INTO `rcs_fm_status_log` VALUES ('117', 'PM100200', '0', '2', '4', '4', '1', '31', '131000', '11.1.5', '1', null, null, '2021-05-13 09:36:26', '2021-10-19 17:19:38');
INSERT INTO `rcs_fm_status_log` VALUES ('118', 'PM100200', '0', '3', '5', '5', '1', '41', '131000', '11.1.5', '1', null, null, '2021-05-13 09:36:26', '2021-10-19 17:20:50');
INSERT INTO `rcs_fm_status_log` VALUES ('119', 'PM100200', '0', '1', '5', '1', '1', '22', '131000', '11.1.5', '1', null, null, '2021-05-13 09:36:26', '2021-10-26 12:32:02');
INSERT INTO `rcs_fm_status_log` VALUES ('120', 'PM100200', '0', '1', '1', '1', '1', '21', '131000', '11.1.5', '1', null, null, '2021-05-13 09:36:26', '2021-10-26 12:36:23');
INSERT INTO `rcs_fm_status_log` VALUES ('121', 'PM100200', '1', '4', '1', '3', '0', '14', '131000', '11.1.5', '1', null, null, '2021-05-13 09:36:26', '2021-10-26 12:36:58');
INSERT INTO `rcs_fm_status_log` VALUES ('122', 'PM100200', '1', '4', '3', '3', '1', '11', null, null, null, null, null, '2021-10-26 12:37:00', '2021-10-26 12:37:00');
INSERT INTO `rcs_fm_status_log` VALUES ('123', 'PM100200', '1', '4', '3', '1', '0', '14', '131000', '11.1.5', '1', null, null, '2021-05-13 09:36:26', '2021-10-26 12:40:56');
INSERT INTO `rcs_fm_status_log` VALUES ('124', 'PM100200', '1', '4', '1', '1', '1', '11', null, null, null, null, null, '2021-10-26 12:41:00', '2021-10-26 12:41:00');
INSERT INTO `rcs_fm_status_log` VALUES ('125', 'PM100200', '0', '1', '1', '1', '1', '21', '131000', '1.1.1', '1', null, null, '2021-05-13 09:36:26', '2021-10-30 11:06:56');
INSERT INTO `rcs_fm_status_log` VALUES ('126', 'PM100200', '0', '2', '4', '4', '1', '31', '131000', '1.1.1', '1', null, null, '2021-05-13 09:36:26', '2021-10-30 11:07:05');
INSERT INTO `rcs_fm_status_log` VALUES ('127', 'PM100200', '0', '3', '5', '5', '1', '41', '131000', '1.1.1', '1', null, null, '2021-05-13 09:36:26', '2021-10-30 11:10:29');
INSERT INTO `rcs_fm_status_log` VALUES ('128', 'PM100200', '0', '1', '1', '1', '1', '21', '131000', '1.1.1', '1', null, null, '2021-05-13 09:36:26', '2021-10-30 11:14:34');
INSERT INTO `rcs_fm_status_log` VALUES ('129', 'PM100200', '1', '4', '1', '3', '0', '14', '131000', '1.1.1', '1', null, null, '2021-05-13 09:36:26', '2021-10-30 11:17:49');
INSERT INTO `rcs_fm_status_log` VALUES ('130', 'PM100200', '1', '4', '3', '3', '1', '11', null, null, null, null, null, '2021-10-30 11:17:52', '2021-10-30 11:17:52');
INSERT INTO `rcs_fm_status_log` VALUES ('131', 'PM100200', '1', '4', '3', '1', '0', '14', '131000', '1.1.1', '1', null, null, '2021-05-13 09:36:26', '2021-10-30 11:18:25');
INSERT INTO `rcs_fm_status_log` VALUES ('132', 'PM100200', '1', '4', '1', '1', '1', '11', null, null, null, null, null, '2021-10-30 11:18:25', '2021-10-30 11:18:25');
INSERT INTO `rcs_fm_status_log` VALUES ('133', 'PM100200', '0', '1', '1', '1', '1', '21', '131000', '1.1.1', '1', null, null, '2021-05-13 09:36:26', '2021-11-01 15:33:23');
INSERT INTO `rcs_fm_status_log` VALUES ('134', 'PM100200', '0', '2', '4', '4', '1', '31', '131000', '1.1.1', '1', null, null, '2021-05-13 09:36:26', '2021-11-01 15:33:50');
INSERT INTO `rcs_fm_status_log` VALUES ('135', 'PM100200', '0', '3', '5', '5', '1', '41', '131000', '1.1.1', '1', null, null, '2021-05-13 09:36:26', '2021-11-01 15:34:02');
INSERT INTO `rcs_fm_status_log` VALUES ('136', 'PM100200', '0', '1', '1', '1', '1', '21', '131000', '1.1.1', '1', null, null, '2021-05-13 09:36:26', '2021-11-01 15:34:14');
INSERT INTO `rcs_fm_status_log` VALUES ('137', 'PM100200', '1', '4', '1', '1', '0', '14', '131000', '1.1.1', '1', null, null, '2021-05-13 09:36:26', '2021-11-01 15:40:12');
INSERT INTO `rcs_fm_status_log` VALUES ('138', 'PM100200', '1', '4', '1', '1', '1', '11', null, null, null, null, null, '2021-11-01 15:40:13', '2021-11-01 15:40:13');
INSERT INTO `rcs_fm_status_log` VALUES ('139', 'PM100200', '1', '4', '1', '1', '0', '14', '131000', '1.1.1', '1', null, null, '2021-05-13 09:36:26', '2021-11-04 17:56:14');

-- ----------------------------
-- Table structure for rcs_foreseen
-- ----------------------------
DROP TABLE IF EXISTS `rcs_foreseen`;
CREATE TABLE `rcs_foreseen` (
  `id` varchar(64) NOT NULL COMMENT '主键uuid',
  `post_office` varchar(6) NOT NULL COMMENT '邮局信息',
  `contract_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '合同id',
  `frank_machine_id` varchar(64) NOT NULL COMMENT '机器id',
  `user_id` varchar(64) DEFAULT NULL,
  `total_count` int DEFAULT NULL COMMENT '总数量',
  `tax_version` varchar(10) NOT NULL COMMENT 'tax版本',
  `total_ammount` double(10,2) NOT NULL COMMENT '【待定】',
  `created_time` datetime NOT NULL COMMENT '创建时间',
  `updated_time` datetime NOT NULL COMMENT '更新时间',
  `foreseen_status` int NOT NULL COMMENT '0 流程中 1 闭环',
  PRIMARY KEY (`id`),
  KEY `contract_id` (`contract_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='预算订单';

-- ----------------------------
-- Records of rcs_foreseen
-- ----------------------------
INSERT INTO `rcs_foreseen` VALUES ('026ecd5a-cffd-4fa5-8c69-30264f3eeeb9', '131000', '00000177', 'PM100200', 'MXX001_0001', '10', '1.1.1', '500.00', '2021-10-30 12:41:11', '2021-10-30 12:41:11', '1');
INSERT INTO `rcs_foreseen` VALUES ('092681c0-5354-437f-aa5b-86ecada127f4', '131000', '00000177', 'PM100200', 'MXX001_0001', '10', '11.1.5', '500.00', '2021-10-19 16:30:45', '2021-10-19 16:30:45', '1');
INSERT INTO `rcs_foreseen` VALUES ('1959fd58-4701-40bd-8fb0-d4917957cddc', '131000', '00000177', 'PM100200', 'MXX001_0001', '10', '1.1.1', '500.00', '2021-10-30 12:42:54', '2021-10-30 12:42:54', '1');
INSERT INTO `rcs_foreseen` VALUES ('1a126f1f-c64c-4741-a193-a943abcb6569', '131000', '00000177', 'PM100200', 'MXX001_0001', '10', '1.1.1', '500.00', '2021-11-01 15:35:31', '2021-11-01 15:35:31', '1');
INSERT INTO `rcs_foreseen` VALUES ('37431eec-194f-4706-bf56-e8e36c9aca2e', '131000', '00000177', 'PM100200', 'MXX001_0001', '10', '1.1.1', '500.00', '2021-11-01 15:38:49', '2021-11-01 15:38:49', '1');
INSERT INTO `rcs_foreseen` VALUES ('760bd64a-73e1-4292-b813-66052ebf0f43', '131000', '00000177', 'PM100200', 'MXX001_0001', '10', '1.1.1', '500.00', '2021-10-30 12:56:04', '2021-10-30 12:56:04', '1');
INSERT INTO `rcs_foreseen` VALUES ('853d8b5a-5c31-47d2-97c4-3b7e587d0712', '131000', '00000177', 'PM100200', 'MXX001_0001', '10', '11.1.5', '500.00', '2021-10-19 16:22:53', '2021-10-19 16:22:53', '1');
INSERT INTO `rcs_foreseen` VALUES ('d5ac9d85-3831-4e02-9ac6-48486408cea1', '131000', '00000177', 'PM100200', 'MXX001_0001', '10', '11.1.5', '500.00', '2021-10-26 12:41:19', '2021-10-26 12:41:19', '1');
INSERT INTO `rcs_foreseen` VALUES ('d886265f-af3a-406a-8d80-3a66ced83aee', '131000', '00000177', 'PM100200', 'MXX001_0001', '10', '11.1.5', '500.00', '2021-10-19 16:24:50', '2021-10-19 16:24:50', '1');
INSERT INTO `rcs_foreseen` VALUES ('e3b5d974-06f8-49b2-9b3b-d4ff7d96cfd4', '131000', '00000177', 'PM100200', 'MXX001_0001', '10', '1.1.1', '500.00', '2021-10-30 13:20:53', '2021-10-30 13:20:53', '1');
INSERT INTO `rcs_foreseen` VALUES ('e8e173f1-8f5e-4ca8-8a43-df818ed8cd60', '131000', '00000177', 'PM100200', 'MXX001_0001', '10', '11.1.5', '500.00', '2021-10-19 16:20:39', '2021-10-19 16:20:39', '1');
INSERT INTO `rcs_foreseen` VALUES ('efb6f492-4a99-4011-a29b-381ad6f95e17', '131000', '00000177', 'PM100200', 'MXX001_0001', '10', '1.1.1', '500.00', '2021-10-30 14:04:53', '2021-10-30 14:04:53', '1');

-- ----------------------------
-- Table structure for rcs_foreseen_product
-- ----------------------------
DROP TABLE IF EXISTS `rcs_foreseen_product`;
CREATE TABLE `rcs_foreseen_product` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `foreseen_id` varchar(64) NOT NULL COMMENT '预算订单id',
  `product_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '【待定】',
  `p_count` int NOT NULL COMMENT '数量',
  `weight` double(10,2) NOT NULL COMMENT '重量',
  `amount` double(10,2) NOT NULL COMMENT '金额(单位是分)',
  `created_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `f_p` (`foreseen_id`),
  CONSTRAINT `f_p` FOREIGN KEY (`foreseen_id`) REFERENCES `rcs_foreseen` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4373 DEFAULT CHARSET=utf8 COMMENT='预算订单产品';

-- ----------------------------
-- Records of rcs_foreseen_product
-- ----------------------------

-- ----------------------------
-- Table structure for rcs_post_office
-- ----------------------------
DROP TABLE IF EXISTS `rcs_post_office`;
CREATE TABLE `rcs_post_office` (
  `id` varchar(64) NOT NULL COMMENT '邮局索引（id）',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '邮局名',
  `city` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '城市名',
  `time_zone` int DEFAULT NULL COMMENT '【待定】',
  `tariff_zone` int DEFAULT NULL COMMENT '【待定】关税区',
  `modified` datetime DEFAULT NULL COMMENT '俄罗斯修改时间',
  `created_time` datetime NOT NULL COMMENT '创建时间',
  `updated_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='合同表';

-- ----------------------------
-- Records of rcs_post_office
-- ----------------------------
INSERT INTO `rcs_post_office` VALUES ('131000', 'Москва', 'Москва', '3', '2', '2021-01-01 14:00:00', '2021-10-19 13:31:40', '2021-10-19 13:31:40');
INSERT INTO `rcs_post_office` VALUES ('914410', 'УФА-ПОЧТОМАТ (АПС)', 'Уфа г', '5', '2', '2021-10-20 07:36:45', '2021-10-20 07:36:34', '2021-10-20 07:36:34');
INSERT INTO `rcs_post_office` VALUES ('914411', 'УФА-ПОЧТОМАТ (АПС)', 'Уфа г', '5', '2', '2021-10-20 07:36:41', '2021-10-20 07:36:30', '2021-10-20 07:36:30');
INSERT INTO `rcs_post_office` VALUES ('914412', 'УФА-ПОЧТОМАТ (АПС)', 'Уфа г', '5', '2', '2021-10-20 07:36:22', '2021-10-20 07:36:11', '2021-10-20 07:36:11');
INSERT INTO `rcs_post_office` VALUES ('914413', 'УФА-ПОЧТОМАТ (АПС)', 'Уфа г', '5', '2', '2021-10-20 07:36:18', '2021-10-20 07:36:07', '2021-10-20 07:36:07');
INSERT INTO `rcs_post_office` VALUES ('914414', 'УФА-ПОЧТОМАТ (АПС)', 'Уфа г', '5', '2', '2021-10-20 07:36:30', '2021-10-20 07:36:18', '2021-10-20 07:36:18');
INSERT INTO `rcs_post_office` VALUES ('914415', 'УФА-ПОЧТОМАТ (АПС)', 'Уфа г', '5', '2', '2021-10-20 07:36:26', '2021-10-20 07:36:15', '2021-10-20 07:36:15');
INSERT INTO `rcs_post_office` VALUES ('914418', 'САРАТОВ-ПОЧТОМАТ (АПС)', 'Саратов г', '4', '1', '2021-10-20 07:37:47', '2021-10-20 07:37:42', '2021-10-20 07:37:42');
INSERT INTO `rcs_post_office` VALUES ('914419', 'УФА-ПОЧТОМАТ (АПС)', 'Уфа г', '5', '2', '2021-10-20 07:37:43', '2021-10-20 07:37:32', '2021-10-20 07:37:32');
INSERT INTO `rcs_post_office` VALUES ('914420', 'УФА-ПОЧТОМАТ (АПС)', 'Уфа г', '5', '2', '2021-10-20 07:37:24', '2021-10-20 07:37:13', '2021-10-20 07:37:13');
INSERT INTO `rcs_post_office` VALUES ('914421', 'НАБЕРЕЖНЫЕ ЧЕЛНЫ-ПОЧТОМАТ (АПС)', 'Набережные Челны г', '3', '1', '2021-10-20 07:37:20', '2021-10-20 07:37:09', '2021-10-20 07:37:09');
INSERT INTO `rcs_post_office` VALUES ('914422', 'НАБЕРЕЖНЫЕ ЧЕЛНЫ-ПОЧТОМАТ (АПС)', 'Набережные Челны г', '3', '1', '2021-10-20 07:37:32', '2021-10-20 07:37:20', '2021-10-20 07:37:20');
INSERT INTO `rcs_post_office` VALUES ('914423', 'НАБЕРЕЖНЫЕ ЧЕЛНЫ-ПОЧТОМАТ (АПС)', 'Набережные Челны г', '3', '1', '2021-10-20 07:37:28', '2021-10-20 07:37:17', '2021-10-20 07:37:17');
INSERT INTO `rcs_post_office` VALUES ('914426', 'ТОЛЬЯТТИ-ПОЧТОМАТ (АПС)', 'Тольятти г', '4', '1', '2021-10-20 07:37:16', '2021-10-20 07:37:05', '2021-10-20 07:37:05');
INSERT INTO `rcs_post_office` VALUES ('914427', 'САМАРА-ПОЧТОМАТ (АПС)', 'Самара г', '4', '1', '2021-10-20 07:37:12', '2021-10-20 07:37:01', '2021-10-20 07:37:01');
INSERT INTO `rcs_post_office` VALUES ('914428', 'УФА-ПОЧТОМАТ (АПС)', 'Уфа г', '5', '2', '2021-10-20 07:36:53', '2021-10-20 07:36:42', '2021-10-20 07:36:42');
INSERT INTO `rcs_post_office` VALUES ('914429', 'УФА-ПОЧТОМАТ (АПС)', 'Уфа г', '5', '2', '2021-10-20 07:36:49', '2021-10-20 07:36:38', '2021-10-20 07:36:38');
INSERT INTO `rcs_post_office` VALUES ('914430', 'УФА-ПОЧТОМАТ (АПС)', 'Уфа г', '5', '2', '2021-10-20 07:37:00', '2021-10-20 07:36:49', '2021-10-20 07:36:49');
INSERT INTO `rcs_post_office` VALUES ('914431', 'ОРЕНБУРГ-ПОЧТОМАТ (АПС)', 'Оренбург г', '5', '2', '2021-10-20 07:36:57', '2021-10-20 07:36:45', '2021-10-20 07:36:45');
INSERT INTO `rcs_post_office` VALUES ('914432', 'НАБЕРЕЖНЫЕ ЧЕЛНЫ-ПОЧТОМАТ (АПС)', 'Набережные Челны г', '3', '1', '2021-10-20 07:38:49', '2021-10-20 07:38:38', '2021-10-20 07:38:38');
INSERT INTO `rcs_post_office` VALUES ('914433', 'НОГИНСК-ПОЧТОМАТ (АПС)', 'Электроугли г', '3', '1', '2021-10-20 07:38:45', '2021-10-20 07:38:34', '2021-10-20 07:38:34');
INSERT INTO `rcs_post_office` VALUES ('914434', 'САНКТ-ПЕТЕРБУРГ-ПОЧТОМАТ (АПС)', 'Санкт-Петербург г', '3', '1', '2021-10-20 07:38:57', '2021-10-20 07:38:46', '2021-10-20 07:38:46');
INSERT INTO `rcs_post_office` VALUES ('914435', 'САРАТОВ-ПОЧТОМАТ (АПС)', 'Саратов г', '4', '1', '2021-10-20 07:38:53', '2021-10-20 07:38:42', '2021-10-20 07:38:42');
INSERT INTO `rcs_post_office` VALUES ('914436', 'ОРЕНБУРГ-ПОЧТОМАТ (АПС)', 'Оренбург г', '5', '2', '2021-10-20 07:38:33', '2021-10-20 07:38:22', '2021-10-20 07:38:22');
INSERT INTO `rcs_post_office` VALUES ('914437', 'ОРЕНБУРГ-ПОЧТОМАТ (АПС)', 'Оренбург г', '5', '2', '2021-10-20 07:38:30', '2021-10-20 07:38:18', '2021-10-20 07:38:18');
INSERT INTO `rcs_post_office` VALUES ('914440', 'УФА-ПОЧТОМАТ (АПС)', 'Уфа г', '5', '2', '2021-10-20 07:38:18', '2021-10-20 07:38:07', '2021-10-20 07:38:07');
INSERT INTO `rcs_post_office` VALUES ('914441', 'УФА-ПОЧТОМАТ (АПС)', 'Уфа г', '5', '2', '2021-10-20 07:38:14', '2021-10-20 07:38:03', '2021-10-20 07:38:03');
INSERT INTO `rcs_post_office` VALUES ('914442', 'САРАТОВ-ПОЧТОМАТ (АПС)', 'Саратов г', '4', '1', '2021-10-20 07:38:26', '2021-10-20 07:38:15', '2021-10-20 07:38:15');
INSERT INTO `rcs_post_office` VALUES ('914443', 'УФА-ПОЧТОМАТ (АПС)', 'Уфа г', '5', '2', '2021-10-20 07:38:22', '2021-10-20 07:38:11', '2021-10-20 07:38:11');
INSERT INTO `rcs_post_office` VALUES ('914444', 'САРАТОВ-ПОЧТОМАТ (АПС)', 'Саратов г', '4', '1', '2021-10-20 07:38:01', '2021-10-20 07:37:50', '2021-10-20 07:37:50');
INSERT INTO `rcs_post_office` VALUES ('914445', 'САРАТОВ-ПОЧТОМАТ (АПС)', 'Саратов г', '4', '1', '2021-10-20 07:37:57', '2021-10-20 07:37:46', '2021-10-20 07:37:46');
INSERT INTO `rcs_post_office` VALUES ('914446', 'УФА-ПОЧТОМАТ (АПС)', 'Уфа г', '5', '2', '2021-10-20 07:38:10', '2021-10-20 07:37:59', '2021-10-20 07:37:59');
INSERT INTO `rcs_post_office` VALUES ('914447', 'НАБЕРЕЖНЫЕ ЧЕЛНЫ-ПОЧТОМАТ (АПС)', 'Набережные Челны г', '3', '1', '2021-10-20 07:38:06', '2021-10-20 07:37:55', '2021-10-20 07:37:55');
INSERT INTO `rcs_post_office` VALUES ('914460', 'САМАРА-ПОЧТОМАТ (АПС)', 'Самара г', '4', '1', '2021-10-20 07:39:04', '2021-10-20 07:38:59', '2021-10-20 07:38:59');
INSERT INTO `rcs_post_office` VALUES ('914461', 'ВОЛГОГРАД-ПОЧТОМАТ (АПС)', 'Волгоград г', '3', '1', '2021-10-20 07:39:01', '2021-10-20 07:38:49', '2021-10-20 07:38:49');
INSERT INTO `rcs_post_office` VALUES ('914524', 'РЯЗАНЬ-ПОЧТОМАТ (АПС)', 'Рязань г', '3', '1', '2021-10-20 07:39:21', '2021-10-20 07:39:10', '2021-10-20 07:39:10');
INSERT INTO `rcs_post_office` VALUES ('915462', 'КРАСНОДАР-ПОЧТОМАТ (АПС)', 'Краснодар г', '3', '1', '2021-10-20 07:34:12', '2021-10-20 07:34:01', '2021-10-20 07:34:01');
INSERT INTO `rcs_post_office` VALUES ('915463', 'ОРЕНБУРГ-ПОЧТОМАТ (АПС)', 'Оренбург г', '5', '2', '2021-10-20 07:34:08', '2021-10-20 07:33:57', '2021-10-20 07:33:57');
INSERT INTO `rcs_post_office` VALUES ('915464', 'ОРЕНБУРГ-ПОЧТОМАТ (АПС)', 'Оренбург г', '5', '2', '2021-10-20 07:00:32', '2021-10-20 07:00:21', '2021-10-20 07:00:21');
INSERT INTO `rcs_post_office` VALUES ('915465', 'ОРЕНБУРГ-ПОЧТОМАТ (АПС)', 'Оренбург г', '5', '2', '2021-10-20 07:00:28', '2021-10-20 07:00:17', '2021-10-20 07:00:17');
INSERT INTO `rcs_post_office` VALUES ('915468', 'НОВОСИБИРСК-ПОЧТОМАТ (АПС)', 'Новосибирск г', '7', '2', '2021-10-20 07:00:17', '2021-10-20 07:00:06', '2021-10-20 07:00:06');
INSERT INTO `rcs_post_office` VALUES ('915469', 'НОВОСИБИРСК-ПОЧТОМАТ (АПС)', 'Новосибирск г', '7', '2', '2021-10-20 07:00:12', '2021-10-20 07:00:02', '2021-10-20 07:00:02');
INSERT INTO `rcs_post_office` VALUES ('915470', 'НОВОСИБИРСК-ПОЧТОМАТ (АПС)', 'Новосибирск г', '7', '2', '2021-10-20 07:00:25', '2021-10-20 07:00:14', '2021-10-20 07:00:14');
INSERT INTO `rcs_post_office` VALUES ('915471', 'НОВОСИБИРСК-ПОЧТОМАТ (АПС)', 'Новосибирск г', '7', '2', '2021-10-20 07:00:21', '2021-10-20 07:00:10', '2021-10-20 07:00:10');
INSERT INTO `rcs_post_office` VALUES ('915586', 'КРАСНОЯРСК-ПОЧТОМАТ (АПС)', 'Красноярск г', '7', '2', '2021-10-20 07:35:16', '2021-10-20 07:35:05', '2021-10-20 07:35:05');
INSERT INTO `rcs_post_office` VALUES ('915587', 'КРАСНОЯРСК-ПОЧТОМАТ (АПС)', 'Красноярск г', '7', '2', '2021-10-20 07:35:12', '2021-10-20 07:35:01', '2021-10-20 07:35:01');
INSERT INTO `rcs_post_office` VALUES ('915588', 'КРАСНОЯРСК-ПОЧТОМАТ (АПС)', 'Красноярск г', '7', '2', '2021-10-20 07:34:52', '2021-10-20 07:34:41', '2021-10-20 07:34:41');
INSERT INTO `rcs_post_office` VALUES ('915589', 'КРАСНОЯРСК-ПОЧТОМАТ (АПС)', 'Красноярск г', '7', '2', '2021-10-20 07:34:49', '2021-10-20 07:34:37', '2021-10-20 07:34:37');
INSERT INTO `rcs_post_office` VALUES ('915590', 'КРАСНОЯРСК-ПОЧТОМАТ (АПС)', 'Красноярск г', '7', '2', '2021-10-20 07:35:00', '2021-10-20 07:34:49', '2021-10-20 07:34:49');
INSERT INTO `rcs_post_office` VALUES ('915591', 'КРАСНОЯРСК-ПОЧТОМАТ (АПС)', 'Красноярск г', '7', '2', '2021-10-20 07:34:56', '2021-10-20 07:34:45', '2021-10-20 07:34:45');
INSERT INTO `rcs_post_office` VALUES ('915594', 'КРАСНОЯРСК-ПОЧТОМАТ (АПС)', 'Красноярск г', '7', '2', '2021-10-20 07:34:45', '2021-10-20 07:34:34', '2021-10-20 07:34:34');
INSERT INTO `rcs_post_office` VALUES ('915595', 'КРАСНОЯРСК-ПОЧТОМАТ (АПС)', 'Красноярск г', '7', '2', '2021-10-20 07:34:41', '2021-10-20 07:34:30', '2021-10-20 07:34:30');
INSERT INTO `rcs_post_office` VALUES ('915596', 'КРАСНОЯРСК-ПОЧТОМАТ (АПС)', 'Красноярск г', '7', '2', '2021-10-20 07:34:21', '2021-10-20 07:34:10', '2021-10-20 07:34:10');
INSERT INTO `rcs_post_office` VALUES ('915597', 'КРАСНОЯРСК-ПОЧТОМАТ (АПС)', 'Красноярск г', '7', '2', '2021-10-20 07:34:18', '2021-10-20 07:34:06', '2021-10-20 07:34:06');
INSERT INTO `rcs_post_office` VALUES ('915598', 'КРАСНОЯРСК-ПОЧТОМАТ (АПС)', 'Красноярск г', '7', '2', '2021-10-20 07:34:29', '2021-10-20 07:34:18', '2021-10-20 07:34:18');
INSERT INTO `rcs_post_office` VALUES ('915599', 'КРАСНОЯРСК-ПОЧТОМАТ (АПС)', 'Красноярск г', '7', '2', '2021-10-20 07:34:25', '2021-10-20 07:34:14', '2021-10-20 07:34:14');
INSERT INTO `rcs_post_office` VALUES ('915600', 'КРАСНОЯРСК-ПОЧТОМАТ (АПС)', 'Красноярск г', '7', '2', '2021-10-20 07:35:27', '2021-10-20 07:35:16', '2021-10-20 07:35:16');
INSERT INTO `rcs_post_office` VALUES ('915601', 'НОВОКУЗНЕЦК-ПОЧТОМАТ (АПС)', 'Новокузнецк г', '7', '3', '2021-10-20 07:35:24', '2021-10-20 07:35:12', '2021-10-20 07:35:12');
INSERT INTO `rcs_post_office` VALUES ('915603', 'ОМСК-ПОЧТОМАТ (АПС)', 'Омск г', '6', '2', '2021-10-20 07:35:31', '2021-10-20 07:35:20', '2021-10-20 07:35:20');
INSERT INTO `rcs_post_office` VALUES ('915605', 'ОРЕНБУРГ-ПОЧТОМАТ (АПС)', 'Оренбург г', '5', '2', '2021-10-29 07:01:09', '2021-10-29 07:01:12', '2021-10-29 07:01:12');
INSERT INTO `rcs_post_office` VALUES ('915608', 'РОСТОВ-НА-ДОНУ-ПОЧТОМАТ (АПС)', 'Ростов-на-Дону г', '3', '1', '2021-10-29 07:00:58', '2021-10-29 07:01:01', '2021-10-29 07:01:01');
INSERT INTO `rcs_post_office` VALUES ('915609', 'САРАТОВ-ПОЧТОМАТ (АПС)', 'Саратов г', '4', '1', '2021-10-29 07:00:54', '2021-10-29 07:00:57', '2021-10-29 07:00:57');
INSERT INTO `rcs_post_office` VALUES ('915610', 'КАЗАНЬ-ПОЧТОМАТ (АПС)', 'Казань г', '3', '1', '2021-10-29 07:01:05', '2021-10-29 07:01:08', '2021-10-29 07:01:08');
INSERT INTO `rcs_post_office` VALUES ('915611', 'НАБЕРЕЖНЫЕ ЧЕЛНЫ-ПОЧТОМАТ (АПС)', 'Набережные Челны г', '3', '1', '2021-10-29 07:01:01', '2021-10-29 07:01:05', '2021-10-29 07:01:05');
INSERT INTO `rcs_post_office` VALUES ('915612', 'НАБЕРЕЖНЫЕ ЧЕЛНЫ-ПОЧТОМАТ (АПС)', 'Набережные Челны г', '3', '1', '2021-10-29 07:00:12', '2021-10-29 07:00:15', '2021-10-29 07:00:15');
INSERT INTO `rcs_post_office` VALUES ('915613', 'УФА-ПОЧТОМАТ (АПС)', 'Уфа г', '5', '2', '2021-10-29 07:00:07', '2021-10-29 07:00:12', '2021-10-29 07:00:12');
INSERT INTO `rcs_post_office` VALUES ('915614', 'АСТРАХАНЬ-ПОЧТОМАТ (АПС)', 'Астрахань г', '4', '1', '2021-10-29 07:00:19', '2021-10-29 07:00:54', '2021-10-29 07:00:54');
INSERT INTO `rcs_post_office` VALUES ('915615', 'АСТРАХАНЬ-ПОЧТОМАТ (АПС)', 'Астрахань г', '4', '1', '2021-10-29 07:00:16', '2021-10-29 07:00:19', '2021-10-29 07:00:19');
INSERT INTO `rcs_post_office` VALUES ('915616', 'АСТРАХАНЬ-ПОЧТОМАТ (АПС)', 'Астрахань г', '4', '1', '2021-10-29 07:02:10', '2021-10-29 07:02:14', '2021-10-29 07:02:14');
INSERT INTO `rcs_post_office` VALUES ('915617', 'АСТРАХАНЬ-ПОЧТОМАТ (АПС)', 'Астрахань г', '4', '1', '2021-10-29 07:02:07', '2021-10-29 07:02:10', '2021-10-29 07:02:10');
INSERT INTO `rcs_post_office` VALUES ('915619', 'АСТРАХАНЬ-ПОЧТОМАТ (АПС)', 'Астрахань г', '4', '1', '2021-10-29 07:02:14', '2021-10-29 07:02:18', '2021-10-29 07:02:18');
INSERT INTO `rcs_post_office` VALUES ('915620', 'АСТРАХАНЬ-ПОЧТОМАТ (АПС)', 'Астрахань г', '4', '1', '2021-10-29 07:01:56', '2021-10-29 07:01:59', '2021-10-29 07:01:59');
INSERT INTO `rcs_post_office` VALUES ('915622', 'АСТРАХАНЬ-ПОЧТОМАТ (АПС)', 'Астрахань г', '4', '1', '2021-10-29 07:02:03', '2021-10-29 07:02:07', '2021-10-29 07:02:07');
INSERT INTO `rcs_post_office` VALUES ('915623', 'АСТРАХАНЬ-ПОЧТОМАТ (АПС)', 'Астрахань г', '4', '1', '2021-10-29 07:01:59', '2021-10-29 07:02:03', '2021-10-29 07:02:03');
INSERT INTO `rcs_post_office` VALUES ('915624', 'АСТРАХАНЬ-ПОЧТОМАТ (АПС)', 'Астрахань г', '4', '1', '2021-10-29 07:01:41', '2021-10-29 07:01:45', '2021-10-29 07:01:45');
INSERT INTO `rcs_post_office` VALUES ('915625', 'АСТРАХАНЬ-ПОЧТОМАТ (АПС)', 'Астрахань г', '4', '1', '2021-10-29 07:01:38', '2021-10-29 07:01:41', '2021-10-29 07:01:41');
INSERT INTO `rcs_post_office` VALUES ('915628', 'САРАТОВ-ПОЧТОМАТ (АПС)', 'Саратов г', '4', '1', '2021-10-29 07:01:27', '2021-10-29 07:01:30', '2021-10-29 07:01:30');
INSERT INTO `rcs_post_office` VALUES ('915629', 'РЯЗАНЬ-ПОЧТОМАТ (АПС)', 'Рязань г', '3', '1', '2021-10-29 07:01:23', '2021-10-29 07:01:27', '2021-10-29 07:01:27');
INSERT INTO `rcs_post_office` VALUES ('915630', 'БАРНАУЛ-ПОЧТОМАТ (АПС)', 'Барнаул г', '7', '2', '2021-10-29 07:01:34', '2021-10-29 07:01:38', '2021-10-29 07:01:38');
INSERT INTO `rcs_post_office` VALUES ('915631', 'БАРНАУЛ-ПОЧТОМАТ (АПС)', 'Барнаул г', '7', '2', '2021-10-29 07:01:30', '2021-10-29 07:01:34', '2021-10-29 07:01:34');
INSERT INTO `rcs_post_office` VALUES ('915632', 'БАРНАУЛ-ПОЧТОМАТ (АПС)', 'Барнаул г', '7', '2', '2021-10-29 07:03:09', '2021-10-29 07:03:13', '2021-10-29 07:03:13');
INSERT INTO `rcs_post_office` VALUES ('915633', 'БАРНАУЛ-ПОЧТОМАТ (АПС)', 'Барнаул г', '7', '2', '2021-10-29 07:03:06', '2021-10-29 07:03:09', '2021-10-29 07:03:09');
INSERT INTO `rcs_post_office` VALUES ('915634', 'БАРНАУЛ-ПОЧТОМАТ (АПС)', 'Барнаул г', '7', '2', '2021-10-29 07:03:17', '2021-10-29 07:03:20', '2021-10-29 07:03:20');
INSERT INTO `rcs_post_office` VALUES ('915635', 'БАРНАУЛ-ПОЧТОМАТ (АПС)', 'Барнаул г', '7', '2', '2021-10-29 07:03:13', '2021-10-29 07:03:17', '2021-10-29 07:03:17');
INSERT INTO `rcs_post_office` VALUES ('915638', 'БАРНАУЛ-ПОЧТОМАТ (АПС)', 'Барнаул г', '7', '2', '2021-10-29 07:03:02', '2021-10-29 07:03:06', '2021-10-29 07:03:06');
INSERT INTO `rcs_post_office` VALUES ('915640', 'БАРНАУЛ-ПОЧТОМАТ (АПС)', 'Барнаул г', '7', '2', '2021-10-29 07:02:40', '2021-10-29 07:02:44', '2021-10-29 07:02:44');
INSERT INTO `rcs_post_office` VALUES ('915641', 'БАРНАУЛ-ПОЧТОМАТ (АПС)', 'Барнаул г', '7', '2', '2021-10-29 07:02:36', '2021-10-29 07:02:40', '2021-10-29 07:02:40');
INSERT INTO `rcs_post_office` VALUES ('915642', 'БАРНАУЛ-ПОЧТОМАТ (АПС)', 'Барнаул г', '7', '2', '2021-10-29 07:02:47', '2021-10-29 07:02:51', '2021-10-29 07:02:51');
INSERT INTO `rcs_post_office` VALUES ('915643', 'БАРНАУЛ-ПОЧТОМАТ (АПС)', 'Барнаул г', '7', '2', '2021-10-29 07:02:44', '2021-10-29 07:02:47', '2021-10-29 07:02:47');
INSERT INTO `rcs_post_office` VALUES ('915646', 'ОМСК-ПОЧТОМАТ (АПС)', 'Омск г', '6', '2', '2021-10-29 07:02:33', '2021-10-29 07:02:36', '2021-10-29 07:02:36');
INSERT INTO `rcs_post_office` VALUES ('915647', 'ОМСК-ПОЧТОМАТ (АПС)', 'Омск г', '6', '2', '2021-10-29 07:02:29', '2021-10-29 07:02:33', '2021-10-29 07:02:33');
INSERT INTO `rcs_post_office` VALUES ('915648', 'ОМСК-ПОЧТОМАТ (АПС)', 'Омск г', '6', '2', '2021-10-29 07:04:08', '2021-10-29 07:04:11', '2021-10-29 07:04:11');
INSERT INTO `rcs_post_office` VALUES ('915650', 'ОМСК-ПОЧТОМАТ (АПС)', 'Омск г', '6', '2', '2021-10-29 07:04:15', '2021-10-29 07:04:19', '2021-10-29 07:04:19');
INSERT INTO `rcs_post_office` VALUES ('915651', 'ОМСК-ПОЧТОМАТ (АПС)', 'Омск г', '6', '2', '2021-10-29 07:04:11', '2021-10-29 07:04:15', '2021-10-29 07:04:15');
INSERT INTO `rcs_post_office` VALUES ('915652', 'ОМСК-ПОЧТОМАТ (АПС)', 'Омск г', '6', '2', '2021-10-29 07:03:53', '2021-10-29 07:03:57', '2021-10-29 07:03:57');
INSERT INTO `rcs_post_office` VALUES ('915653', 'ОМСК-ПОЧТОМАТ (АПС)', 'Омск г', '6', '2', '2021-10-29 07:03:49', '2021-10-29 07:03:53', '2021-10-29 07:03:53');
INSERT INTO `rcs_post_office` VALUES ('915656', 'ОМСК-ПОЧТОМАТ (АПС)', 'Омск г', '6', '2', '2021-10-29 07:03:39', '2021-10-29 07:03:42', '2021-10-29 07:03:42');
INSERT INTO `rcs_post_office` VALUES ('915657', 'ОМСК-ПОЧТОМАТ (АПС)', 'Омск г', '6', '2', '2021-10-29 07:03:35', '2021-10-29 07:03:38', '2021-10-29 07:03:38');
INSERT INTO `rcs_post_office` VALUES ('915658', 'ОМСК-ПОЧТОМАТ (АПС)', 'Омск г', '6', '2', '2021-10-29 07:03:46', '2021-10-29 07:03:49', '2021-10-29 07:03:49');
INSERT INTO `rcs_post_office` VALUES ('915659', 'АСТРАХАНЬ-ПОЧТОМАТ (АПС)', 'Астрахань г', '4', '1', '2021-10-29 07:03:42', '2021-10-29 07:03:46', '2021-10-29 07:03:46');
INSERT INTO `rcs_post_office` VALUES ('915661', 'КАЗАНЬ-ПОЧТОМАТ (АПС)', 'Казань г', '3', '1', '2021-10-29 07:03:20', '2021-10-29 07:03:24', '2021-10-29 07:03:24');
INSERT INTO `rcs_post_office` VALUES ('915664', 'ОМСК-ПОЧТОМАТ (АПС)', 'Омск г', '6', '2', '2021-10-29 07:05:12', '2021-10-29 07:05:15', '2021-10-29 07:05:15');
INSERT INTO `rcs_post_office` VALUES ('915666', 'БАРНАУЛ-ПОЧТОМАТ (АПС)', 'Барнаул г', '7', '2', '2021-10-29 07:05:19', '2021-10-29 07:05:23', '2021-10-29 07:05:23');
INSERT INTO `rcs_post_office` VALUES ('915667', 'БАРНАУЛ-ПОЧТОМАТ (АПС)', 'Барнаул г', '7', '2', '2021-10-29 07:05:16', '2021-10-29 07:05:19', '2021-10-29 07:05:19');
INSERT INTO `rcs_post_office` VALUES ('915668', 'БАРНАУЛ-ПОЧТОМАТ (АПС)', 'Барнаул г', '7', '2', '2021-10-29 07:04:57', '2021-10-29 07:05:01', '2021-10-29 07:05:01');
INSERT INTO `rcs_post_office` VALUES ('915669', 'БАРНАУЛ-ПОЧТОМАТ (АПС)', 'Барнаул г', '7', '2', '2021-10-29 07:04:54', '2021-10-29 07:04:57', '2021-10-29 07:04:57');
INSERT INTO `rcs_post_office` VALUES ('915672', 'БАРНАУЛ-ПОЧТОМАТ (АПС)', 'Барнаул г', '7', '2', '2021-10-29 07:04:43', '2021-10-29 07:04:46', '2021-10-29 07:04:46');
INSERT INTO `rcs_post_office` VALUES ('915673', 'БАРНАУЛ-ПОЧТОМАТ (АПС)', 'Барнаул г', '7', '2', '2021-10-29 07:04:39', '2021-10-29 07:04:43', '2021-10-29 07:04:43');
INSERT INTO `rcs_post_office` VALUES ('915674', 'БАРНАУЛ-ПОЧТОМАТ (АПС)', 'Барнаул г', '7', '2', '2021-10-29 07:04:50', '2021-10-29 07:04:54', '2021-10-29 07:04:54');
INSERT INTO `rcs_post_office` VALUES ('915675', 'КАЗЕННАЯ ЗАИМКА-ПОЧТОМАТ (АПС)', 'Барнаул г', '7', '2', '2021-10-29 07:04:47', '2021-10-29 07:04:50', '2021-10-29 07:04:50');
INSERT INTO `rcs_post_office` VALUES ('915676', 'АСТРАХАНЬ-ПОЧТОМАТ (АПС)', 'Астрахань г', '4', '1', '2021-10-29 07:04:28', '2021-10-29 07:04:32', '2021-10-29 07:04:32');
INSERT INTO `rcs_post_office` VALUES ('915677', 'АСТРАХАНЬ-ПОЧТОМАТ (АПС)', 'Астрахань г', '4', '1', '2021-10-29 07:04:19', '2021-10-29 07:04:28', '2021-10-29 07:04:28');
INSERT INTO `rcs_post_office` VALUES ('915679', 'ТУЛА-ПОЧТОМАТ (АПС)', 'Тула г', '3', '1', '2021-10-29 07:04:32', '2021-10-29 07:04:36', '2021-10-29 07:04:36');
INSERT INTO `rcs_post_office` VALUES ('915682', 'ТУЛА-ПОЧТОМАТ (АПС)', 'Тула г', '3', '1', '2021-10-29 07:06:17', '2021-10-29 07:06:21', '2021-10-29 07:06:21');
INSERT INTO `rcs_post_office` VALUES ('915684', 'ТУЛА-ПОЧТОМАТ (АПС)', 'Тула г', '3', '1', '2021-10-29 07:05:56', '2021-10-29 07:05:59', '2021-10-29 07:05:59');
INSERT INTO `rcs_post_office` VALUES ('915685', 'ТУЛА-ПОЧТОМАТ (АПС)', 'Тула г', '3', '1', '2021-10-29 07:05:52', '2021-10-29 07:05:56', '2021-10-29 07:05:56');
INSERT INTO `rcs_post_office` VALUES ('915686', 'ТУЛА-ПОЧТОМАТ (АПС)', 'Тула г', '3', '1', '2021-10-29 07:06:03', '2021-10-29 07:06:06', '2021-10-29 07:06:06');
INSERT INTO `rcs_post_office` VALUES ('915687', 'ТУЛА-ПОЧТОМАТ (АПС)', 'Тула г', '3', '1', '2021-10-29 07:05:59', '2021-10-29 07:06:03', '2021-10-29 07:06:03');
INSERT INTO `rcs_post_office` VALUES ('915690', 'ТУЛА-ПОЧТОМАТ (АПС)', 'Тула г', '3', '1', '2021-10-29 07:05:48', '2021-10-29 07:05:52', '2021-10-29 07:05:52');
INSERT INTO `rcs_post_office` VALUES ('915691', 'ТУЛА-ПОЧТОМАТ (АПС)', 'Тула г', '3', '1', '2021-10-29 07:05:45', '2021-10-29 07:05:48', '2021-10-29 07:05:48');
INSERT INTO `rcs_post_office` VALUES ('915692', 'ТУЛА-ПОЧТОМАТ (АПС)', 'Тула г', '3', '1', '2021-10-29 07:05:27', '2021-10-29 07:05:30', '2021-10-29 07:05:30');
INSERT INTO `rcs_post_office` VALUES ('915693', 'ТУЛА-ПОЧТОМАТ (АПС)', 'Тула г', '3', '1', '2021-10-29 07:05:23', '2021-10-29 07:05:26', '2021-10-29 07:05:26');
INSERT INTO `rcs_post_office` VALUES ('915695', 'ТУЛА-ПОЧТОМАТ (АПС)', 'Тула г', '3', '1', '2021-10-29 07:05:30', '2021-10-29 07:05:34', '2021-10-29 07:05:34');
INSERT INTO `rcs_post_office` VALUES ('915696', 'ТУЛА-ПОЧТОМАТ (АПС)', 'Тула г', '3', '1', '2021-10-29 07:06:28', '2021-10-29 07:06:32', '2021-10-29 07:06:32');
INSERT INTO `rcs_post_office` VALUES ('915697', 'ТУЛА-ПОЧТОМАТ (АПС)', 'Тула г', '3', '1', '2021-10-29 07:06:25', '2021-10-29 07:06:28', '2021-10-29 07:06:28');
INSERT INTO `rcs_post_office` VALUES ('915698', 'ТУЛА-ПОЧТОМАТ (АПС)', 'Тула г', '3', '1', '2021-10-29 07:06:36', '2021-10-29 07:06:39', '2021-10-29 07:06:39');
INSERT INTO `rcs_post_office` VALUES ('915700', 'УЛЬЯНОВСК-ПОЧТОМАТ (АПС)', 'Ульяновск г', '4', '1', '2021-10-29 07:06:21', '2021-10-29 07:06:25', '2021-10-29 07:06:25');
INSERT INTO `rcs_post_office` VALUES ('927900', 'ЕКАТЕРИНБУРГ ПВЗ', 'Екатеринбург г', '5', '2', '2021-10-20 07:35:20', '2021-10-20 07:35:09', '2021-10-20 07:35:09');
INSERT INTO `rcs_post_office` VALUES ('940901', 'НИЖНИЙ НОВГОРОД ПВЗ', 'Нижний Новгород г', '3', '1', '2021-10-20 07:36:14', '2021-10-20 07:36:03', '2021-10-20 07:36:03');
INSERT INTO `rcs_post_office` VALUES ('940904', 'НИЖНИЙ НОВГОРОД ПВЗ', 'Нижний Новгород г', '3', '1', '2021-10-20 07:35:39', '2021-10-20 07:35:59', '2021-10-20 07:35:59');

-- ----------------------------
-- Table structure for rcs_post_office_contract
-- ----------------------------
DROP TABLE IF EXISTS `rcs_post_office_contract`;
CREATE TABLE `rcs_post_office_contract` (
  `id` int NOT NULL AUTO_INCREMENT,
  `contract_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '合同id',
  `post_office_id` varchar(64) NOT NULL COMMENT '邮局id',
  PRIMARY KEY (`id`),
  KEY `contract_id` (`contract_code`),
  KEY `post_office_id` (`post_office_id`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8 COMMENT='邮局-合同关系表';

-- ----------------------------
-- Records of rcs_post_office_contract
-- ----------------------------
INSERT INTO `rcs_post_office_contract` VALUES ('10', '00001010', '660001');
INSERT INTO `rcs_post_office_contract` VALUES ('11', '00001010', '131000');
INSERT INTO `rcs_post_office_contract` VALUES ('12', '00001010', '131000');
INSERT INTO `rcs_post_office_contract` VALUES ('13', '00001010', '131000');
INSERT INTO `rcs_post_office_contract` VALUES ('14', '00001010', '131000');
INSERT INTO `rcs_post_office_contract` VALUES ('15', '00001010', '131000');
INSERT INTO `rcs_post_office_contract` VALUES ('16', '00001010', '131000');
INSERT INTO `rcs_post_office_contract` VALUES ('17', '00001010', '131000');
INSERT INTO `rcs_post_office_contract` VALUES ('18', '00001010', '131000');
INSERT INTO `rcs_post_office_contract` VALUES ('19', '00001010', '131000');
INSERT INTO `rcs_post_office_contract` VALUES ('20', '00001010', '131000');
INSERT INTO `rcs_post_office_contract` VALUES ('21', '00000001', '440022');
INSERT INTO `rcs_post_office_contract` VALUES ('22', '00000001', '131000');
INSERT INTO `rcs_post_office_contract` VALUES ('23', '00000177', '131000');
INSERT INTO `rcs_post_office_contract` VALUES ('24', '00000177', '131000');
INSERT INTO `rcs_post_office_contract` VALUES ('25', '00000177', '131000');
INSERT INTO `rcs_post_office_contract` VALUES ('26', '00000177', '131000');
INSERT INTO `rcs_post_office_contract` VALUES ('27', '00000177', '131000');
INSERT INTO `rcs_post_office_contract` VALUES ('28', '00003879', '440022');
INSERT INTO `rcs_post_office_contract` VALUES ('29', '00003879', '196601');
INSERT INTO `rcs_post_office_contract` VALUES ('30', '00003879', '440022');
INSERT INTO `rcs_post_office_contract` VALUES ('31', '00003879', '196601');
INSERT INTO `rcs_post_office_contract` VALUES ('32', '00003879', '170024');
INSERT INTO `rcs_post_office_contract` VALUES ('33', '00001227', '660037');
INSERT INTO `rcs_post_office_contract` VALUES ('34', '00001227', '170024');
INSERT INTO `rcs_post_office_contract` VALUES ('35', '00001227', '660037');
INSERT INTO `rcs_post_office_contract` VALUES ('36', '00001227', '660037');
INSERT INTO `rcs_post_office_contract` VALUES ('37', '00001227', '196601');

-- ----------------------------
-- Table structure for rcs_postal_product
-- ----------------------------
DROP TABLE IF EXISTS `rcs_postal_product`;
CREATE TABLE `rcs_postal_product` (
  `id` int NOT NULL AUTO_INCREMENT,
  `code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '【待定】',
  `tax_id` int NOT NULL,
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '【待定】',
  `mail_type` int DEFAULT NULL COMMENT '【待定】',
  `mail_ctg` int DEFAULT NULL COMMENT '转发区域； 1-内部，2-外部',
  `max_weight` int DEFAULT NULL COMMENT '关税区（值1-5）',
  `trans_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '偏远地区； 1-长达2000公里，2-超过2000公里',
  `region_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '邮政物品的最大重量',
  `region_zone` int DEFAULT NULL COMMENT '邮件类别的名称\r\n									 #*简单-0\r\n									 #*定制-1\r\n									 #*申报价值-2',
  `distance_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '邮件类型代码  2-字母  3-包裹邮寄',
  `contract_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '运输方向  1-内部（RTM-2）;  2-外向国际（RTM-2）;',
  `numdiff` int DEFAULT NULL,
  `label_ru` varchar(255) DEFAULT NULL,
  `is_postal_market_only` varchar(1) DEFAULT NULL,
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
  `modified` datetime DEFAULT NULL COMMENT '俄罗斯修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=414 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='邮政产品表';

-- ----------------------------
-- Records of rcs_postal_product
-- ----------------------------
INSERT INTO `rcs_postal_product` VALUES ('1', '2011', '21', 'Мультиконверт', '20', '1', '150', 'ANY', 'DOMESTIC', '3', 'UP_2000', '10-1200', '10', '20 1 DOMESTIC ANY 10', '0', '2021-10-19 13:42:42', '2021-10-19 13:42:42', '2021-07-15 22:25:17');
INSERT INTO `rcs_postal_product` VALUES ('2', '2100', '21', 'Письмо простое', '2', '0', '150', 'ANY', 'DOMESTIC', '3', 'AFTER_2000', '', '10', '2 0 DOMESTIC ANY 10', '0', '2021-10-19 13:42:42', '2021-10-19 13:42:42', '2021-07-20 16:06:53');
INSERT INTO `rcs_postal_product` VALUES ('3', '2211', '18', 'Письмо', '2', '1', '2000', 'ANY', 'INTERNATIONAL', '1', 'AFTER_2000', null, null, 'Письмо заказное международное (исходящее)', '0', '2021-10-12 12:55:15', '2021-10-12 12:55:15', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('4', '5106', '21', 'Письмо 1 кл.', '15', '1', '500', 'ANY', 'DOMESTIC', '2', 'UP_2000', '', '15', '15 1 DOMESTIC ANY 15', '0', '2021-10-19 13:42:42', '2021-10-19 13:42:42', '2021-07-20 16:24:49');
INSERT INTO `rcs_postal_product` VALUES ('5', '6100', '18', 'Почтовая карточка', '6', '0', '20', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Карточка простая внутренняя', '0', '2021-10-12 12:55:15', '2021-10-12 12:55:15', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('6', '6100', '22', 'Почтовая карточка', '6', '0', '20', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Карточка простая внутренняя', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('7', '4100', '22', 'Трек-открытка', '6', '0', '20', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Карточка простая внутренняя', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('8', '6101', '22', 'Почтовая карточка', '6', '1', '20', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Карточка заказная внутренняя', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('9', '2100', '22', 'Письмо', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Письмо простое внутреннее', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('10', '2101', '22', 'Письмо', '2', '1', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Письмо заказное внутреннее', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('11', '2210', '22', 'Письмо', '2', '0', '2000', 'ANY', 'INTERNATIONAL', '1', 'AFTER_2000', null, null, 'Письмо простое международное (исходящее)', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('12', '2211', '22', 'Письмо', '2', '1', '2000', 'ANY', 'INTERNATIONAL', '1', 'AFTER_2000', null, null, 'Письмо заказное международное (исходящее)', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('13', '1701', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('14', '1703', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('15', '1705', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('16', '1707', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('17', '1709', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('18', '1711', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('19', '1713', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('20', '1715', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('21', '1717', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('22', '1719', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('23', '1721', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('24', '1723', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('25', '1725', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('26', '1727', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('27', '1729', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('28', '1731', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('29', '1733', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('30', '1735', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('31', '1737', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('32', '1739', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('33', '1741', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('34', '1743', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('35', '1745', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('36', '1747', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('37', '1749', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('38', '1751', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('39', '1753', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('40', '1755', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('41', '1757', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('42', '1759', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('43', '1761', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('44', '1763', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('45', '1765', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('46', '1767', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('47', '1769', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('48', '1771', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('49', '1773', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('50', '1775', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('51', '1777', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('52', '1779', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('53', '1702', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('54', '1704', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('55', '1706', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('56', '1708', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('57', '1710', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('58', '1712', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('59', '1714', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('60', '1716', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('61', '1718', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('62', '1720', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('63', '1722', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('64', '1724', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('65', '1726', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('66', '1728', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('67', '1730', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('68', '1732', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('69', '1734', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('70', '1736', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('71', '1738', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('72', '1740', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('73', '1742', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('74', '1744', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('75', '1746', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('76', '1748', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('77', '1750', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('78', '1752', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('79', '1754', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('80', '1756', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('81', '1758', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('82', '1760', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('83', '1762', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('84', '1764', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('85', '1766', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('86', '1768', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('87', '1770', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('88', '1772', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('89', '1774', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('90', '1776', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('91', '1778', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('92', '1780', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('93', '1782', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('94', '1784', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('95', '1786', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('96', '1788', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('97', '1790', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('98', '1792', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('99', '1794', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('100', '1796', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('101', '1798', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('102', '1800', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('103', '1802', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('104', '1804', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('105', '1806', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('106', '1808', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('107', '1810', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('108', '1812', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('109', '1814', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('110', '1816', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('111', '1818', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('112', '1820', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('113', '1822', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('114', '1824', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('115', '1826', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('116', '1828', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('117', '1830', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('118', '1832', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('119', '1834', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('120', '1836', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('121', '1838', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('122', '1840', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('123', '1842', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('124', '1844', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('125', '1846', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('126', '1848', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('127', '1850', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('128', '1852', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('129', '1854', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('130', '1856', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('131', '1858', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('132', '1860', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('133', '1862', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('134', '1864', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('135', '1866', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('136', '1868', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('137', '1870', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('138', '1872', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('139', '1874', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('140', '1876', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('141', '1878', '22', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:30:51', '2021-10-29 16:30:51', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('142', '6100', '23', 'Почтовая карточка', '6', '0', '20', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Карточка простая внутренняя', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('143', '4100', '23', 'Трек-открытка', '6', '0', '20', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Карточка простая внутренняя', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('144', '6101', '23', 'Почтовая карточка', '6', '1', '20', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Карточка заказная внутренняя', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('145', '2100', '23', 'Письмо', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Письмо простое внутреннее', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('146', '2101', '23', 'Письмо', '2', '1', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Письмо заказное внутреннее', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('147', '2210', '23', 'Письмо', '2', '0', '2000', 'ANY', 'INTERNATIONAL', '1', 'AFTER_2000', null, null, 'Письмо простое международное (исходящее)', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('148', '2211', '23', 'Письмо', '2', '1', '2000', 'ANY', 'INTERNATIONAL', '1', 'AFTER_2000', null, null, 'Письмо заказное международное (исходящее)', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('149', '1701', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('150', '1703', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('151', '1705', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('152', '1707', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('153', '1709', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('154', '1711', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('155', '1713', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('156', '1715', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('157', '1717', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('158', '1719', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('159', '1721', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('160', '1723', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('161', '1725', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('162', '1727', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('163', '1729', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('164', '1731', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('165', '1733', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('166', '1735', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('167', '1737', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('168', '1739', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('169', '1741', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('170', '1743', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('171', '1745', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('172', '1747', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('173', '1749', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('174', '1751', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('175', '1753', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('176', '1755', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('177', '1757', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('178', '1759', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('179', '1761', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('180', '1763', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('181', '1765', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('182', '1767', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('183', '1769', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('184', '1771', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('185', '1773', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('186', '1775', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('187', '1777', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('188', '1779', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('189', '1702', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('190', '1704', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('191', '1706', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('192', '1708', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('193', '1710', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('194', '1712', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('195', '1714', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('196', '1716', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('197', '1718', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('198', '1720', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('199', '1722', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('200', '1724', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('201', '1726', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('202', '1728', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('203', '1730', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('204', '1732', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('205', '1734', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('206', '1736', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('207', '1738', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('208', '1740', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('209', '1742', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('210', '1744', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('211', '1746', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('212', '1748', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('213', '1750', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('214', '1752', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('215', '1754', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('216', '1756', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('217', '1758', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('218', '1760', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('219', '1762', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('220', '1764', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('221', '1766', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('222', '1768', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('223', '1770', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('224', '1772', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('225', '1774', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('226', '1776', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('227', '1778', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('228', '1780', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('229', '1782', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('230', '1784', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('231', '1786', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('232', '1788', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('233', '1790', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('234', '1792', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('235', '1794', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('236', '1796', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('237', '1798', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('238', '1800', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('239', '1802', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('240', '1804', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('241', '1806', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('242', '1808', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('243', '1810', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('244', '1812', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('245', '1814', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('246', '1816', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('247', '1818', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('248', '1820', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('249', '1822', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('250', '1824', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('251', '1826', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('252', '1828', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('253', '1830', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('254', '1832', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('255', '1834', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('256', '1836', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('257', '1838', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('258', '1840', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('259', '1842', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('260', '1844', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('261', '1846', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('262', '1848', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('263', '1850', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('264', '1852', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('265', '1854', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('266', '1856', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('267', '1858', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('268', '1860', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('269', '1862', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('270', '1864', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('271', '1866', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('272', '1868', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('273', '1870', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('274', '1872', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('275', '1874', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('276', '1876', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('277', '1878', '23', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:32:13', '2021-10-29 16:32:13', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('278', '6100', '24', 'Почтовая карточка', '6', '0', '20', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Карточка простая внутренняя', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('279', '4100', '24', 'Трек-открытка', '6', '0', '20', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Карточка простая внутренняя', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('280', '6101', '24', 'Почтовая карточка', '6', '1', '20', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Карточка заказная внутренняя', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('281', '2100', '24', 'Письмо', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Письмо простое внутреннее', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('282', '2101', '24', 'Письмо', '2', '1', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Письмо заказное внутреннее', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('283', '2210', '24', 'Письмо', '2', '0', '2000', 'ANY', 'INTERNATIONAL', '1', 'AFTER_2000', null, null, 'Письмо простое международное (исходящее)', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('284', '2211', '24', 'Письмо', '2', '1', '2000', 'ANY', 'INTERNATIONAL', '1', 'AFTER_2000', null, null, 'Письмо заказное международное (исходящее)', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('285', '1701', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('286', '1703', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('287', '1705', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('288', '1707', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('289', '1709', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('290', '1711', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('291', '1713', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('292', '1715', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('293', '1717', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('294', '1719', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('295', '1721', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('296', '1723', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('297', '1725', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('298', '1727', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('299', '1729', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('300', '1731', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('301', '1733', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('302', '1735', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('303', '1737', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('304', '1739', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('305', '1741', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('306', '1743', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('307', '1745', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('308', '1747', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('309', '1749', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('310', '1751', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('311', '1753', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('312', '1755', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('313', '1757', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('314', '1759', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('315', '1761', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('316', '1763', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('317', '1765', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('318', '1767', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('319', '1769', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('320', '1771', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('321', '1773', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('322', '1775', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('323', '1777', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('324', '1779', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса простое', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('325', '1702', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('326', '1704', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('327', '1706', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('328', '1708', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('329', '1710', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('330', '1712', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('331', '1714', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('332', '1716', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('333', '1718', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('334', '1720', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('335', '1722', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('336', '1724', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('337', '1726', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('338', '1728', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('339', '1730', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('340', '1732', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('341', '1734', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('342', '1736', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('343', '1738', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('344', '1740', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('345', '1742', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('346', '1744', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('347', '1746', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('348', '1748', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('349', '1750', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('350', '1752', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('351', '1754', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('352', '1756', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('353', '1758', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('354', '1760', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('355', '1762', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('356', '1764', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('357', '1766', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('358', '1768', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('359', '1770', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('360', '1772', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('361', '1774', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('362', '1776', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('363', '1778', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('364', '1780', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('365', '1782', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('366', '1784', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('367', '1786', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('368', '1788', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('369', '1790', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('370', '1792', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('371', '1794', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('372', '1796', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('373', '1798', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('374', '1800', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('375', '1802', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('376', '1804', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('377', '1806', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('378', '1808', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('379', '1810', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('380', '1812', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('381', '1814', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('382', '1816', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('383', '1818', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('384', '1820', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('385', '1822', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('386', '1824', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('387', '1826', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('388', '1828', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('389', '1830', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('390', '1832', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('391', '1834', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('392', '1836', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('393', '1838', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('394', '1840', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('395', '1842', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('396', '1844', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('397', '1846', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('398', '1848', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('399', '1850', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('400', '1852', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('401', '1854', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('402', '1856', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('403', '1858', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('404', '1860', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('405', '1862', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('406', '1864', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('407', '1866', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('408', '1868', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('409', '1870', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('410', '1872', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('411', '1874', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('412', '1876', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('413', '1878', '24', 'Почтовое отправление 1-го класса', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Внутригородское почтовое отправление 1-го класса заказное', '0', '2021-10-29 16:33:18', '2021-10-29 16:33:18', '2021-01-01 14:00:00');

-- ----------------------------
-- Table structure for rcs_print_job
-- ----------------------------
DROP TABLE IF EXISTS `rcs_print_job`;
CREATE TABLE `rcs_print_job` (
  `id` int NOT NULL AUTO_INCREMENT,
  `contract_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '合同id',
  `frank_machine_id` varchar(64) NOT NULL COMMENT '机器id',
  `foreseen_id` varchar(64) NOT NULL COMMENT 'foreseen id',
  `transaction_id` varchar(64) NOT NULL,
  `user_id` varchar(64) DEFAULT NULL,
  `flow` int NOT NULL COMMENT '流程 0 未闭环   1 闭环',
  `flow_detail` int DEFAULT NULL,
  `cancel_msg_code` int DEFAULT '0' COMMENT '取消原因代码',
  `updated_time` datetime DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fm_id` (`frank_machine_id`),
  KEY `flow` (`flow`),
  KEY `foreseen_id` (`foreseen_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4445 DEFAULT CHARSET=utf8 COMMENT='打印任务表';

-- ----------------------------
-- Records of rcs_print_job
-- ----------------------------
INSERT INTO `rcs_print_job` VALUES ('4432', '00000177', 'PM100200', 'e8e173f1-8f5e-4ca8-8a43-df818ed8cd60', '', 'MXX001_0001', '1', '63', '0', '2021-10-19 16:20:39', '2021-10-19 16:20:39');
INSERT INTO `rcs_print_job` VALUES ('4433', '00000177', 'PM100200', '853d8b5a-5c31-47d2-97c4-3b7e587d0712', '', 'MXX001_0001', '1', '63', '0', '2021-10-19 16:22:53', '2021-10-19 16:22:53');
INSERT INTO `rcs_print_job` VALUES ('4434', '00000177', 'PM100200', 'd886265f-af3a-406a-8d80-3a66ced83aee', '', 'MXX001_0001', '1', '64', '1', '2021-10-19 16:26:39', '2021-10-19 16:24:50');
INSERT INTO `rcs_print_job` VALUES ('4435', '00000177', 'PM100200', '092681c0-5354-437f-aa5b-86ecada127f4', '3143b9db-f0a3-41bb-909a-95f8a88e97f7', 'MXX001_0001', '1', '61', '0', '2021-10-19 16:31:39', '2021-10-19 16:30:45');
INSERT INTO `rcs_print_job` VALUES ('4436', '00000177', 'PM100200', 'd5ac9d85-3831-4e02-9ac6-48486408cea1', '4ecac815-d634-4e37-9ef6-f87747f6caa9', 'MXX001_0001', '1', '61', '0', '2021-10-26 12:43:55', '2021-10-26 12:41:19');
INSERT INTO `rcs_print_job` VALUES ('4438', '00000177', 'PM100200', '026ecd5a-cffd-4fa5-8c69-30264f3eeeb9', '', 'MXX001_0001', '1', '64', '1', '2021-10-30 12:41:40', '2021-10-30 12:41:11');
INSERT INTO `rcs_print_job` VALUES ('4439', '00000177', 'PM100200', '1959fd58-4701-40bd-8fb0-d4917957cddc', '03ae2860-96ad-41e9-9d14-697f31b885e3', 'MXX001_0001', '1', '61', '0', '2021-10-30 12:44:38', '2021-10-30 12:42:54');
INSERT INTO `rcs_print_job` VALUES ('4440', '00000177', 'PM100200', '760bd64a-73e1-4292-b813-66052ebf0f43', '', 'MXX001_0001', '1', '64', '1', '2021-10-30 13:20:25', '2021-10-30 12:56:04');
INSERT INTO `rcs_print_job` VALUES ('4441', '00000177', 'PM100200', 'e3b5d974-06f8-49b2-9b3b-d4ff7d96cfd4', '', 'MXX001_0001', '1', '64', '1', '2021-10-30 13:58:50', '2021-10-30 13:20:53');
INSERT INTO `rcs_print_job` VALUES ('4442', '00000177', 'PM100200', 'efb6f492-4a99-4011-a29b-381ad6f95e17', '', 'MXX001_0001', '1', '64', '1', '2021-11-01 15:35:12', '2021-10-30 14:04:53');
INSERT INTO `rcs_print_job` VALUES ('4443', '00000177', 'PM100200', '1a126f1f-c64c-4741-a193-a943abcb6569', '7bdebc6f-b0c2-4940-89c5-acfae99da86e', 'MXX001_0001', '1', '64', '1', '2021-11-01 15:36:15', '2021-11-01 15:35:31');
INSERT INTO `rcs_print_job` VALUES ('4444', '00000177', 'PM100200', '37431eec-194f-4706-bf56-e8e36c9aca2e', 'c05a9a95-44a8-455f-bd5e-27962826b527', 'MXX001_0001', '1', '61', '0', '2021-11-01 15:39:31', '2021-11-01 15:38:49');

-- ----------------------------
-- Table structure for rcs_public_key
-- ----------------------------
DROP TABLE IF EXISTS `rcs_public_key`;
CREATE TABLE `rcs_public_key` (
  `frank_machine_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `public_key` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '公钥',
  `private_key` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `flow` int NOT NULL COMMENT '0 未闭环 1 闭环',
  `flow_detail` int DEFAULT NULL,
  `revision` int NOT NULL COMMENT '公钥的版本（序列号）',
  `created_time` datetime NOT NULL COMMENT '创建时间',
  `expire_time` datetime NOT NULL COMMENT '到期时间',
  PRIMARY KEY (`frank_machine_id`),
  KEY `fm_id` (`frank_machine_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='公钥表';

-- ----------------------------
-- Records of rcs_public_key
-- ----------------------------
INSERT INTO `rcs_public_key` VALUES ('PM100200', 'MDIwEAYHKoZIzj0CAQYFK4EEAAYDHgAEyPdguaYv3FFD0AnEokKo/nfkkWmkoEy1\nOVnACw==', 'MD4CAQEEDqVST2CykbIB4qiEdDn/oAcGBSuBBAAGoSADHgAEyPdguaYv3FFD0AnE\nokKo/nfkkWmkoEy1OVnACw==', '1', '81', '6', '2021-11-01 15:34:14', '2024-10-31 00:00:00');
INSERT INTO `rcs_public_key` VALUES ('PM200500', 'MDIwEAYHKoZIzj0CAQYFK4EEAAYDHgAEyPdguaYv3FFD0AnEokKo/nfkkWmkoEy1\nOVnACw==', 'MD4CAQEEDqVST2CykbIB4qiEdDn/oAcGBSuBBAAGoSADHgAEyPdguaYv3FFD0AnE\nokKo/nfkkWmkoEy1OVnACw==', '1', '81', '4', '2021-10-16 11:02:15', '2024-10-15 00:00:00');

-- ----------------------------
-- Table structure for rcs_registers
-- ----------------------------
DROP TABLE IF EXISTS `rcs_registers`;
CREATE TABLE `rcs_registers` (
  `id` varchar(64) NOT NULL,
  `contract_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '合同id',
  `contract_num` int DEFAULT NULL COMMENT '合同号',
  `postofficeIndex` varchar(10) NOT NULL COMMENT '【待定】',
  `frank_machine_id` varchar(64) NOT NULL COMMENT '机器id',
  `asc_register` varchar(50) NOT NULL COMMENT '【待定】',
  `dec_register` varchar(50) NOT NULL COMMENT '【待定】',
  `amount` varchar(50) NOT NULL COMMENT '金额（单位是分）【待定：什么金额】',
  `r_type` int DEFAULT NULL COMMENT '【待定】1 REFILL  2 REFUND',
  `created_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `contract_id` (`contract_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='【待定】';

-- ----------------------------
-- Records of rcs_registers
-- ----------------------------

-- ----------------------------
-- Table structure for rcs_statistics
-- ----------------------------
DROP TABLE IF EXISTS `rcs_statistics`;
CREATE TABLE `rcs_statistics` (
  `id` varchar(64) NOT NULL,
  `contract_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '合同id',
  `contract_num` int DEFAULT NULL COMMENT '合同号',
  `frank_machine_id` varchar(64) NOT NULL COMMENT '机器id',
  `post_office` varchar(6) NOT NULL COMMENT '邮局信息',
  `total_amount` varchar(50) NOT NULL COMMENT '总金额',
  `initial_piece_counter` bigint NOT NULL COMMENT '【待定】',
  `final_piece_counter` bigint NOT NULL COMMENT '【待定】',
  `created_time` datetime NOT NULL COMMENT '创建时间',
  `franking_period_start` datetime NOT NULL COMMENT '【待定】',
  `franking_period_end` datetime NOT NULL COMMENT '【待定】',
  PRIMARY KEY (`id`),
  KEY `contract_id` (`contract_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='【待定】';

-- ----------------------------
-- Records of rcs_statistics
-- ----------------------------

-- ----------------------------
-- Table structure for rcs_tax
-- ----------------------------
DROP TABLE IF EXISTS `rcs_tax`;
CREATE TABLE `rcs_tax` (
  `id` int NOT NULL AUTO_INCREMENT,
  `version` varchar(30) NOT NULL COMMENT '版本信息',
  `created_date` datetime NOT NULL COMMENT '创建时间',
  `save_path` varchar(255) DEFAULT NULL COMMENT 'tax的json文件保存地址',
  `apply_date` datetime DEFAULT NULL COMMENT '【待定】',
  `publish_date` datetime DEFAULT NULL COMMENT '【待定】',
  `modified` datetime DEFAULT NULL COMMENT '【待定】',
  `description` varchar(200) DEFAULT NULL COMMENT '描述',
  `source` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '【待定：长度，含义，来源】',
  `inform_russia` int NOT NULL DEFAULT '0' COMMENT '是否通知俄罗斯 0 未通知 1 已通知',
  PRIMARY KEY (`id`),
  KEY `version` (`version`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8 COMMENT='税率表';

-- ----------------------------
-- Records of rcs_tax
-- ----------------------------
INSERT INTO `rcs_tax` VALUES ('18', '11.1.2', '2021-10-12 12:55:15', 'D:\\PostmartOfficeServiceFile\\tax\\2021_10_12_12_55_14.json', '2021-10-01 08:00:00', '2021-06-30 15:13:38', '2021-01-01 14:00:00', 'Тарифы на 4 квартал 2021', 'ИС Тарификатор - АТиКС', '1');
INSERT INTO `rcs_tax` VALUES ('21', '11.1.5', '2021-10-19 13:42:42', 'D:\\PostmartOfficeServiceFile\\tax\\2021_10_19_13_42_42.json', '2021-10-20 02:00:00', '2021-10-19 10:42:48', '2021-10-19 09:17:58', 'PM UAT 5 Test Tax', 'Manual Input', '1');
INSERT INTO `rcs_tax` VALUES ('24', '22.1.1', '2021-10-29 16:33:18', 'D:\\workspace\\PostmartOfficeServiceFile\\tax\\2021_10_29_16_33_17.json', '2021-10-01 08:00:00', '2021-06-30 15:13:38', '2021-01-01 14:00:00', 'Тарифы на 4 квартал 2021', 'ИС Тарификатор - АТиКС', '1');

-- ----------------------------
-- Table structure for rcs_tax_device_unreceived
-- ----------------------------
DROP TABLE IF EXISTS `rcs_tax_device_unreceived`;
CREATE TABLE `rcs_tax_device_unreceived` (
  `id` int NOT NULL AUTO_INCREMENT,
  `fm_id` char(8) DEFAULT NULL,
  `tax_version` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_index` (`fm_id`,`tax_version`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of rcs_tax_device_unreceived
-- ----------------------------

-- ----------------------------
-- Table structure for rcs_transaction
-- ----------------------------
DROP TABLE IF EXISTS `rcs_transaction`;
CREATE TABLE `rcs_transaction` (
  `id` varchar(64) NOT NULL,
  `foreseen_id` varchar(64) NOT NULL COMMENT 'foreseen id',
  `post_office` varchar(6) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '邮局信息',
  `contract_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '合同id',
  `user_id` varchar(64) DEFAULT NULL,
  `graph_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '【待定】',
  `frank_machine_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '机器id',
  `contract_num` int DEFAULT NULL COMMENT '合同号',
  `credit_val` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '【待定】',
  `amount` double(10,2) DEFAULT NULL COMMENT '金额（单位是分）',
  `count` int DEFAULT NULL COMMENT '总数量',
  `tax_version` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'tax版本',
  `start_date_time` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '开始时间',
  `stop_date_time` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '停止时间',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
  `transaction_status` int DEFAULT NULL COMMENT '0 流程中 1 闭环',
  PRIMARY KEY (`id`),
  KEY `contract_id` (`contract_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='交易表';

-- ----------------------------
-- Records of rcs_transaction
-- ----------------------------
INSERT INTO `rcs_transaction` VALUES ('03ae2860-96ad-41e9-9d14-697f31b885e3', '1959fd58-4701-40bd-8fb0-d4917957cddc', '131000', '00000177', 'MXX001_0001', '', 'PM100200', null, '0.0', '20.00', '2', '1.1.1', '2021-10-30T12:43:52.000+08:00', '2021-10-30T12:44:38.000+08:00', '2021-10-30 12:43:53', '2021-10-30 12:44:38', '1');
INSERT INTO `rcs_transaction` VALUES ('3143b9db-f0a3-41bb-909a-95f8a88e97f7', '092681c0-5354-437f-aa5b-86ecada127f4', '131000', '00000177', 'MXX001_0001', '', 'PM100200', null, '0.0', '40.00', '4', '11.1.5', '2021-10-19T16:31:05.000+08:00', '2021-10-19T16:31:39.000+08:00', '2021-10-19 16:31:05', '2021-10-19 16:31:39', '1');
INSERT INTO `rcs_transaction` VALUES ('4ecac815-d634-4e37-9ef6-f87747f6caa9', 'd5ac9d85-3831-4e02-9ac6-48486408cea1', '131000', '00000177', 'MXX001_0001', '', 'PM100200', null, '0.0', '30.00', '3', '11.1.5', '2021-10-26T12:42:02.000+08:00', '2021-10-26T12:43:55.000+08:00', '2021-10-26 12:42:03', '2021-10-26 12:43:55', '1');
INSERT INTO `rcs_transaction` VALUES ('7bdebc6f-b0c2-4940-89c5-acfae99da86e', '1a126f1f-c64c-4741-a193-a943abcb6569', null, null, '', '', null, null, '0.0', '0.00', '0', '', '2021-11-01T15:35:56.000+08:00', '', '2021-11-01 15:35:57', null, '0');
INSERT INTO `rcs_transaction` VALUES ('c05a9a95-44a8-455f-bd5e-27962826b527', '37431eec-194f-4706-bf56-e8e36c9aca2e', '131000', '00000177', 'MXX001_0001', '', 'PM100200', null, '0.0', '10.00', '1', '1.1.1', '2021-11-01T15:39:12.000+08:00', '2021-11-01T15:39:30.000+08:00', '2021-11-01 15:39:12', '2021-11-01 15:39:31', '1');

-- ----------------------------
-- Table structure for rcs_transaction_data
-- ----------------------------
DROP TABLE IF EXISTS `rcs_transaction_data`;
CREATE TABLE `rcs_transaction_data` (
  `id` int NOT NULL AUTO_INCREMENT,
  `statistics_id` varchar(64) NOT NULL,
  `product_code` varchar(30) NOT NULL COMMENT '【待定】',
  `t_weight` int DEFAULT NULL COMMENT '重量',
  `t_count` int DEFAULT NULL COMMENT '数量',
  `t_amount` varchar(50) NOT NULL COMMENT '【待定】',
  `tax_version` varchar(20) NOT NULL COMMENT 'tax版本',
  `created_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `t_s` (`statistics_id`),
  CONSTRAINT `t_s` FOREIGN KEY (`statistics_id`) REFERENCES `rcs_statistics` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='交易数据表【待定】';

-- ----------------------------
-- Records of rcs_transaction_data
-- ----------------------------

-- ----------------------------
-- Table structure for rcs_transaction_msg
-- ----------------------------
DROP TABLE IF EXISTS `rcs_transaction_msg`;
CREATE TABLE `rcs_transaction_msg` (
  `id` int NOT NULL AUTO_INCREMENT,
  `transaction_id` varchar(64) DEFAULT NULL,
  `frank_machine_id` varchar(64) DEFAULT NULL,
  `count` bigint DEFAULT NULL COMMENT '一批邮票的数量',
  `amount` bigint DEFAULT NULL,
  `dm_msg` varchar(255) DEFAULT NULL,
  `status` varchar(1) DEFAULT NULL COMMENT '1开始 2结束 0开机：需要服务器自己判断：上一个为2，不存入数据库，上一个为1，服务器储存这条信息，status改成2',
  `created_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `s_id` (`transaction_id`,`count`,`amount`,`created_time`,`dm_msg`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=125 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of rcs_transaction_msg
-- ----------------------------
INSERT INTO `rcs_transaction_msg` VALUES ('111', '3143b9db-f0a3-41bb-909a-95f8a88e97f7', 'PM100200', '137', '115587', '!45!01PM6431310001910211002000000013800010002100000100000177', '1', '2021-10-19 16:31:05');
INSERT INTO `rcs_transaction_msg` VALUES ('112', '3143b9db-f0a3-41bb-909a-95f8a88e97f7', 'PM100200', '141', '119587', '!45!01PM6431310001910211002000000014200010002100000100000177', '2', '2021-10-19 16:31:35');
INSERT INTO `rcs_transaction_msg` VALUES ('113', '4ecac815-d634-4e37-9ef6-f87747f6caa9', 'PM100200', '141', '119587', '!45!01PM6431310002610211002000000014200010002100000100000177', '1', '2021-10-26 12:42:03');
INSERT INTO `rcs_transaction_msg` VALUES ('114', '4ecac815-d634-4e37-9ef6-f87747f6caa9', 'PM100200', '142', '120587', '!45!01PM6431310002610211002000000014200010002100000100000177', '2', '2021-10-26 12:42:17');
INSERT INTO `rcs_transaction_msg` VALUES ('115', '4ecac815-d634-4e37-9ef6-f87747f6caa9', 'PM100200', '142', '120587', '!45!01PM6431310002610211002000000014300010002100000100000177', '1', '2021-10-26 12:42:23');
INSERT INTO `rcs_transaction_msg` VALUES ('116', '4ecac815-d634-4e37-9ef6-f87747f6caa9', 'PM100200', '144', '122587', '!45!01PM6431310002610211002000000014300010002100000100000177', '2', '2021-10-26 12:42:44');
INSERT INTO `rcs_transaction_msg` VALUES ('117', '03ae2860-96ad-41e9-9d14-697f31b885e3', 'PM100200', '144', '122587', '!45!01PM6431310003010211002000000014500010000000000100000177', '1', '2021-10-30 12:43:53');
INSERT INTO `rcs_transaction_msg` VALUES ('118', '03ae2860-96ad-41e9-9d14-697f31b885e3', 'PM100200', '145', '123587', '!45!01PM6431310003010211002000000014500010000000000100000177', '2', '2021-10-30 12:44:11');
INSERT INTO `rcs_transaction_msg` VALUES ('119', '03ae2860-96ad-41e9-9d14-697f31b885e3', 'PM100200', '145', '123587', '!45!01PM6431310003010211002000000014600010000000000100000177', '1', '2021-10-30 12:44:17');
INSERT INTO `rcs_transaction_msg` VALUES ('120', '03ae2860-96ad-41e9-9d14-697f31b885e3', 'PM100200', '146', '124587', '!45!01PM6431310003010211002000000014600010000000000100000177', '2', '2021-10-30 12:44:32');
INSERT INTO `rcs_transaction_msg` VALUES ('121', '7bdebc6f-b0c2-4940-89c5-acfae99da86e', 'PM100200', '146', '124587', '!45!01PM6431310000111211002000000014700000000000000100000177', '1', '2021-11-01 15:35:57');
INSERT INTO `rcs_transaction_msg` VALUES ('122', '7bdebc6f-b0c2-4940-89c5-acfae99da86e', 'PM100200', '146', '124587', '!45!01PM6431310000111211002000000014700000000000000100000177', '2', '2021-11-01 15:36:12');
INSERT INTO `rcs_transaction_msg` VALUES ('123', 'c05a9a95-44a8-455f-bd5e-27962826b527', 'PM100200', '146', '124587', '!45!01PM6431310000111211002000000014700010000000000100000177', '1', '2021-11-01 15:39:12');
INSERT INTO `rcs_transaction_msg` VALUES ('124', 'c05a9a95-44a8-455f-bd5e-27962826b527', 'PM100200', '147', '125587', '!45!01PM6431310000111211002000000014700010000000000100000177', '2', '2021-11-01 15:39:27');

-- ----------------------------
-- Table structure for t_device
-- ----------------------------
DROP TABLE IF EXISTS `t_device`;
CREATE TABLE `t_device` (
  `device_id` bigint NOT NULL AUTO_INCREMENT COMMENT '设备id',
  `acnum` varchar(50) NOT NULL COMMENT '表头号',
  `nickname` varchar(50) NOT NULL COMMENT '设备昵称',
  `warn_amount` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '默认警告金额（分为单位）',
  `max_amount` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '最大金额（分为单位）',
  `secret_key` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '加密秘钥',
  `valid_days` char(3) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '有效天数',
  `use_lock` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '0' COMMENT '是否使用加密锁 0 不使用 1 使用',
  `lock_info` varchar(50) DEFAULT NULL COMMENT '加密锁信息',
  `frank_machine_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '机器主键uuid',
  `user_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '【待定】',
  `cur_fm_status` int DEFAULT NULL COMMENT '【待定】机器当前状态\r\n												1 ENABLED\r\n												2 DEMO\r\n												3 BLOCKED\r\n												4 UNAUTHORIZED\r\n												5 LOST',
  `future_fm_status` int DEFAULT NULL COMMENT '机器想要达到的状态',
  `flow` int DEFAULT NULL COMMENT '流程 0 未开始  1进行中   2 闭环(成流程 0 未闭环   1 闭环(成功后闭环) -1 闭环(失败后闭环)功后闭环) -2 闭环(失败后闭环)',
  `flow_detail` int DEFAULT NULL COMMENT '各种FlowXXXEnum的状态',
  `is_russia` int DEFAULT '0' COMMENT '谁在改变状态 1 俄罗斯改变状态  0 机器改变状态',
  `post_office` varchar(6) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '邮局信息',
  `tax_version` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'tax版本 device当前的版本 不一定是最新的版本',
  `tax_is_update` int DEFAULT '1' COMMENT 'tax是否更新 默认为1 最新状态  0 没有更新到最新状态',
  `fm_event` int DEFAULT NULL COMMENT '1 STATUS \r\n									 2 RATE_TABLE_UPDATE',
  `error_code` varchar(6) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '错误代码',
  `error_message` varchar(64) DEFAULT NULL COMMENT '错误信息',
  `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
  `created_time` datetime DEFAULT NULL COMMENT '添加时间',
  PRIMARY KEY (`device_id`) USING BTREE,
  UNIQUE KEY `p_device_acnum` (`acnum`) USING BTREE,
  KEY `p_device_device_id` (`device_id`),
  KEY `cur_fm_status` (`cur_fm_status`),
  KEY `frank_machine_id` (`frank_machine_id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='设备表';

-- ----------------------------
-- Records of t_device
-- ----------------------------
INSERT INTO `t_device` VALUES ('38', 'MXX001', 'MXX001', '10000.00', '100000.00', 'b4b9d97816a46b25', '7', '0', null, 'PM100200', null, '1', '1', '0', '14', '1', '131000', '1.1.1', '1', '1', null, null, '2021-11-04 17:56:14', '2021-05-13 09:36:26');
