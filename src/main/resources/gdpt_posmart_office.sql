/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 80019
Source Host           : localhost:9688
Source Database       : gdpt_posmart_office

Target Server Type    : MYSQL
Target Server Version : 80019
File Encoding         : 65001

Date: 2021-07-10 14:11:25
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for rcs_balance
-- ----------------------------
DROP TABLE IF EXISTS `rcs_balance`;
CREATE TABLE `rcs_balance` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'balance主键',
  `contract_code` varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '合同id',
  `contract_current` double(10,2) NOT NULL COMMENT '【待定】当前可用资金',
  `consolidate` double(10,2) NOT NULL COMMENT '当前余额',
  `operation_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '【待定】',
  `from_type` int DEFAULT NULL COMMENT '1 服务器发送给俄罗斯，俄罗斯返回的信息  2 俄罗斯发给服务器的数据',
  `created_time` datetime NOT NULL COMMENT '创建时间',
  `russia_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `contract_id` (`contract_code`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8 COMMENT='数据同步记录表';

-- ----------------------------
-- Records of rcs_balance
-- ----------------------------
INSERT INTO `rcs_balance` VALUES ('32', '00001032', '20795.00', '27512.00', null, '1', '2021-07-10 10:48:41', '2021-07-10 10:48:41');
INSERT INTO `rcs_balance` VALUES ('33', '00001032', '20795.00', '27507.00', null, '1', '2021-07-10 10:53:30', '2021-07-10 10:53:30');
INSERT INTO `rcs_balance` VALUES ('34', '00001032', '20790.00', '27507.00', null, '1', '2021-07-10 11:01:42', '2021-07-10 11:01:42');
INSERT INTO `rcs_balance` VALUES ('35', '00001032', '20793.00', '27505.00', null, '1', '2021-07-10 11:02:39', '2021-07-10 11:02:39');

-- ----------------------------
-- Table structure for rcs_contract
-- ----------------------------
DROP TABLE IF EXISTS `rcs_contract`;
CREATE TABLE `rcs_contract` (
  `code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '合同号的数字，废弃',
  `enable` int NOT NULL COMMENT '【待定】0 不可用 1 可用',
  `current` double(10,2) DEFAULT NULL COMMENT '当前可用资金',
  `consolidate` double(10,2) DEFAULT NULL,
  `created_time` datetime NOT NULL COMMENT '创建时间',
  `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
  `modified` datetime DEFAULT NULL COMMENT '俄罗斯更新时间',
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='合同表';

-- ----------------------------
-- Records of rcs_contract
-- ----------------------------
INSERT INTO `rcs_contract` VALUES ('00001032', null, '1', '20793.00', '27505.00', '2021-07-06 14:12:10', '2021-07-10 11:02:39', '2021-01-01 14:00:00');

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
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of rcs_contract_address
-- ----------------------------
INSERT INTO `rcs_contract_address` VALUES ('11', '00001032', 'Иркутская обл, Иркутск г', '2021-06-15 15:18:02');
INSERT INTO `rcs_contract_address` VALUES ('15', '00001032', 'Иркутская обл', '2021-07-06 14:15:43');

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
INSERT INTO `rcs_customer` VALUES ('11a8005e-6d6a-499d-9fca-82aa69103f90', '00001032', 'Заказчик', '680320076000', '346313002', '664033, Иркутская обл, Иркутск г, Лермонтова ул, дом № 257', '664033, Иркутская обл, Иркутск г, Лермонтова ул, дом № 257, оф. 802', '2021-07-06 14:12:10', '2021-07-06 14:12:10', '2021-01-01 14:00:00');

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
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='机器状态变更表';

-- ----------------------------
-- Records of rcs_fm_status_log
-- ----------------------------
INSERT INTO `rcs_fm_status_log` VALUES ('1', 'FM100002', '1', '4', '1', '1', '0', '13', '994700', '2.3.3', '1', null, null, '2021-05-13 09:36:26', '2021-07-06 13:16:33');
INSERT INTO `rcs_fm_status_log` VALUES ('2', '', '1', '4', null, '1', '1', '13', null, null, null, null, null, '2021-07-07 10:20:50', '2021-07-07 10:20:50');
INSERT INTO `rcs_fm_status_log` VALUES ('3', '', '1', '4', null, '1', '1', '13', null, null, null, null, null, '2021-07-07 12:36:47', '2021-07-07 12:36:47');
INSERT INTO `rcs_fm_status_log` VALUES ('4', 'FM100002', '1', '4', null, '1', '1', '13', null, null, null, null, null, '2021-07-07 12:47:32', '2021-07-07 12:47:32');
INSERT INTO `rcs_fm_status_log` VALUES ('5', 'FM100002', '1', '4', '1', '1', '1', '11', null, null, null, null, null, '2021-07-07 12:51:36', '2021-07-07 12:51:36');
INSERT INTO `rcs_fm_status_log` VALUES ('6', 'FM100002', '1', '4', '1', '1', '1', '11', null, null, null, null, null, '2021-07-07 13:17:30', '2021-07-07 13:17:30');
INSERT INTO `rcs_fm_status_log` VALUES ('7', 'FM100002', '1', '4', '1', '1', '1', '11', null, null, null, null, null, '2021-07-07 13:45:51', '2021-07-07 13:45:51');
INSERT INTO `rcs_fm_status_log` VALUES ('8', 'FM100002', '1', '4', '1', '1', '1', '11', null, null, null, null, null, '2021-07-07 13:59:27', '2021-07-07 13:59:27');
INSERT INTO `rcs_fm_status_log` VALUES ('9', 'FM100002', '1', '4', '1', '1', '1', '11', null, null, null, null, null, '2021-07-07 14:01:42', '2021-07-07 14:01:42');

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
INSERT INTO `rcs_foreseen` VALUES ('36fcf82a-f078-470e-a2cc-cbe5fc9320be', '994700', '00001032', 'FM100002', '11a8005e-6d6a-499d-9fca-82aa69103f90', '2', '22.1.1', '5.00', '2021-07-10 10:48:41', '2021-07-10 10:48:41', '1');
INSERT INTO `rcs_foreseen` VALUES ('9d8386f7-5fc2-493b-885a-2ae76160a155', '994700', '00001032', 'FM100002', '11a8005e-6d6a-499d-9fca-82aa69103f90', '2', '22.1.1', '5.00', '2021-07-10 11:01:42', '2021-07-10 11:01:42', '1');

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
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8 COMMENT='预算订单产品';

-- ----------------------------
-- Records of rcs_foreseen_product
-- ----------------------------

-- ----------------------------
-- Table structure for rcs_postal_product
-- ----------------------------
DROP TABLE IF EXISTS `rcs_postal_product`;
CREATE TABLE `rcs_postal_product` (
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
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='邮政产品表';

-- ----------------------------
-- Records of rcs_postal_product
-- ----------------------------
INSERT INTO `rcs_postal_product` VALUES ('2100', '15', 'Письмо', '2', '0', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Письмо простое внутреннее', '0', '2021-07-06 16:13:48', '2021-07-06 16:13:48', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('2101', '15', 'Письмо', '2', '1', '100', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Письмо заказное внутреннее', '0', '2021-07-06 16:13:48', '2021-07-06 16:13:48', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('2210', '15', 'Письмо', '2', '0', '2000', 'ANY', 'INTERNATIONAL', '1', 'AFTER_2000', null, null, 'Письмо простое международное (исходящее)', '0', '2021-07-06 16:13:48', '2021-07-06 16:13:48', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('2211', '15', 'Письмо', '2', '1', '2000', 'ANY', 'INTERNATIONAL', '1', 'AFTER_2000', null, null, 'Письмо заказное международное (исходящее)', '0', '2021-07-06 16:13:48', '2021-07-06 16:13:48', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('6100', '15', 'Почтовая карточка', '6', '0', '20', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Карточка простая внутренняя', '0', '2021-07-06 16:13:48', '2021-07-06 16:13:48', '2021-01-01 14:00:00');
INSERT INTO `rcs_postal_product` VALUES ('6101', '15', 'Почтовая карточка', '6', '1', '20', 'ANY', 'DOMESTIC', '1', 'AFTER_2000', null, null, 'Карточка заказная внутренняя', '0', '2021-07-06 16:13:48', '2021-07-06 16:13:48', '2021-01-01 14:00:00');

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
INSERT INTO `rcs_post_office` VALUES ('994700', 'УФПС Абстрактной области', 'Абстрактск', '0', '1', '2021-01-01 14:00:00', '2021-07-06 14:05:11', '2021-07-06 14:05:11');

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
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='邮局-合同关系表';

-- ----------------------------
-- Records of rcs_post_office_contract
-- ----------------------------
INSERT INTO `rcs_post_office_contract` VALUES ('5', '00001032', '994700');

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
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8 COMMENT='打印任务表';

-- ----------------------------
-- Records of rcs_print_job
-- ----------------------------
INSERT INTO `rcs_print_job` VALUES ('37', '00001032', 'FM100002', '36fcf82a-f078-470e-a2cc-cbe5fc9320be', '08b41d57-190f-4020-935a-cf6876816304', '11a8005e-6d6a-499d-9fca-82aa69103f90', '1', '61', '0', '2021-07-10 10:53:30', '2021-07-10 10:48:41');
INSERT INTO `rcs_print_job` VALUES ('38', '00001032', 'FM100002', '9d8386f7-5fc2-493b-885a-2ae76160a155', '42dfe079-ac66-4aff-9367-4271b9035c1f', '11a8005e-6d6a-499d-9fca-82aa69103f90', '1', '61', '0', '2021-07-10 11:02:39', '2021-07-10 11:01:42');

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
INSERT INTO `rcs_public_key` VALUES ('FM100002', 'MDIwEAYHKoZIzj0CAQYFK4EEAAYDHgAEyPdguaYv3FFD0AnEokKo/nfkkWmkoEy1\nOVnACw==', 'MD4CAQEEDqVST2CykbIB4qiEdDn/oAcGBSuBBAAGoSADHgAEyPdguaYv3FFD0AnE\nokKo/nfkkWmkoEy1OVnACw==', '1', '81', '3', '2021-07-06 13:28:30', '2024-07-05 00:00:00');

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
  `apply_date` datetime NOT NULL COMMENT '【待定】',
  `publish_date` datetime NOT NULL COMMENT '【待定】',
  `modified` datetime NOT NULL COMMENT '【待定】',
  `description` varchar(200) DEFAULT NULL COMMENT '描述',
  `source` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '【待定：长度，含义，来源】',
  PRIMARY KEY (`id`),
  KEY `version` (`version`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8 COMMENT='税率表';

-- ----------------------------
-- Records of rcs_tax
-- ----------------------------
INSERT INTO `rcs_tax` VALUES ('15', '22.1.1', '2021-07-06 16:13:48', 'D:\\PostmartOfficeServiceFile\\tax\\2021_07_06_16_13_47.json', '2021-10-01 08:00:00', '2021-06-30 15:13:38', '2021-01-01 14:00:00', 'Тарифы на 4 квартал 2021', 'ИС Тарификатор - АТиКС');

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
INSERT INTO `rcs_transaction` VALUES ('08b41d57-190f-4020-935a-cf6876816304', '36fcf82a-f078-470e-a2cc-cbe5fc9320be', '994700', '00001032', '11a8005e-6d6a-499d-9fca-82aa69103f90', '', 'FM100002', null, '2.0', '5.00', '2', '22.1.1', '2021-07-10T10:50:19.000+08:00', '2021-07-10T10:53:29.000+08:00', '2021-07-10 10:50:20', '2021-07-10 10:53:30', '1');
INSERT INTO `rcs_transaction` VALUES ('42dfe079-ac66-4aff-9367-4271b9035c1f', '9d8386f7-5fc2-493b-885a-2ae76160a155', '994700', '00001032', '11a8005e-6d6a-499d-9fca-82aa69103f90', '', 'FM100002', null, '5.0', '2.00', '2', '22.1.1', '2021-07-10T11:02:08.000+08:00', '2021-07-10T11:02:39.000+08:00', '2021-07-10 11:02:08', '2021-07-10 11:02:39', '1');

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
  `count` int DEFAULT NULL COMMENT '一批邮票的数量',
  `amount` double(10,2) DEFAULT NULL,
  `dm_msg` varchar(255) DEFAULT NULL,
  `status` varchar(1) DEFAULT NULL COMMENT '1开始 2结束 0开机：需要服务器自己判断：上一个为2，不存入数据库，上一个为1，服务器储存这条信息，status改成2',
  `created_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `s_id` (`transaction_id`,`count`,`amount`,`created_time`,`dm_msg`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of rcs_transaction_msg
-- ----------------------------
INSERT INTO `rcs_transaction_msg` VALUES ('38', '08b41d57-190f-4020-935a-cf6876816304', 'FM100002', '16', '15.00', '!45!01NE6439947000101001000020000000500000012101000100001032', '1', '2021-07-10 10:50:20');
INSERT INTO `rcs_transaction_msg` VALUES ('39', '08b41d57-190f-4020-935a-cf6876816304', 'FM100002', '17', '16.00', '!45!01NE6439947000101001000020000000500000012101000100001032', '2', '2021-07-10 10:50:32');
INSERT INTO `rcs_transaction_msg` VALUES ('40', '08b41d57-190f-4020-935a-cf6876816304', 'FM100002', '17', '16.00', '!45!01NE6439947000101001000020000000500000012101000100001032', '1', '2021-07-10 10:50:38');
INSERT INTO `rcs_transaction_msg` VALUES ('41', '08b41d57-190f-4020-935a-cf6876816304', 'FM100002', '18', '17.00', '!45!01NE6439947000101001000020000000500000012101000100001032', '2', '2021-07-10 10:50:47');
INSERT INTO `rcs_transaction_msg` VALUES ('42', '42dfe079-ac66-4aff-9367-4271b9035c1f', 'FM100002', '18', '17.00', '!45!01NE6439947000101001000020000000500000012101000100001032', '1', '2021-07-10 11:02:08');
INSERT INTO `rcs_transaction_msg` VALUES ('43', '42dfe079-ac66-4aff-9367-4271b9035c1f', 'FM100002', '20', '19.00', '!45!01NE6439947000101001000020000000500000012101000100001032', '2', '2021-07-10 11:02:20');

-- ----------------------------
-- Table structure for t_audit
-- ----------------------------
DROP TABLE IF EXISTS `t_audit`;
CREATE TABLE `t_audit` (
  `audit_id` bigint NOT NULL AUTO_INCREMENT COMMENT '审核id',
  `order_id` bigint NOT NULL COMMENT '订单id',
  `user_id` bigint DEFAULT NULL COMMENT '审核员的user_id',
  `device_id` bigint DEFAULT NULL COMMENT '设备id',
  `order_number` varchar(80) NOT NULL COMMENT '订单号',
  `amount` varchar(50) NOT NULL COMMENT '订单金额（单位为分）',
  `audit_type` char(1) NOT NULL COMMENT '审核类型 1 请求注资  2 请求闭环 ',
  `f_user_id` bigint NOT NULL COMMENT '提交人id',
  `submit_info` varchar(200) DEFAULT NULL COMMENT '提交原因',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '审核状态 0 未开始 1 成功 2 驳回  3 冻结中  4 已注销',
  `old_status` char(1) DEFAULT NULL COMMENT '冻结前的状态',
  `check_remark` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '审核备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  PRIMARY KEY (`audit_id`) USING BTREE,
  KEY `p_audit_user_id` (`audit_id`)
) ENGINE=InnoDB AUTO_INCREMENT=98 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='审核表';

-- ----------------------------
-- Records of t_audit
-- ----------------------------
INSERT INTO `t_audit` VALUES ('44', '63', '32', '29', '51359631238369280', '11111.00', '1', '33', 'wowowowo', '1', null, null, '2020-08-20 10:10:12');
INSERT INTO `t_audit` VALUES ('45', '64', '32', '29', '51418515399380992', '666.00', '1', '33', '', '1', null, null, '2020-08-20 14:03:39');
INSERT INTO `t_audit` VALUES ('46', '65', '32', '29', '51424189638381568', '1111.00', '1', '33', '', '1', null, null, '2020-08-20 14:25:56');
INSERT INTO `t_audit` VALUES ('47', '66', '32', '29', '51437164461953024', '1111.00', '1', '33', '', '1', null, null, '2020-08-20 15:17:28');
INSERT INTO `t_audit` VALUES ('48', '67', '32', '29', '51437677333057536', '1111.00', '1', '33', '', '1', null, null, '2020-08-20 15:19:29');
INSERT INTO `t_audit` VALUES ('49', '68', '32', '29', '51437883076251648', '1.00', '1', '33', '', '1', null, null, '2020-08-20 15:20:19');
INSERT INTO `t_audit` VALUES ('50', '69', '32', '29', '51453435421265920', '11.00', '1', '33', '', '1', null, null, '2020-08-20 16:22:07');
INSERT INTO `t_audit` VALUES ('51', '70', '32', '29', '51460704515002368', '111.00', '1', '33', '', '1', null, null, '2020-08-20 16:51:01');
INSERT INTO `t_audit` VALUES ('52', '71', '32', '29', '51464029000044544', '111.00', '1', '33', '', '1', null, null, '2020-08-20 17:04:42');
INSERT INTO `t_audit` VALUES ('53', '72', '32', '29', '53241043377852416', '0.02', '1', '33', '', '4', '1', null, '2020-08-25 14:58:47');
INSERT INTO `t_audit` VALUES ('54', '73', '32', '29', '53529362091347968', '666.00', '1', '33', '', '2', null, '', '2020-08-26 09:51:36');
INSERT INTO `t_audit` VALUES ('55', '73', '32', '29', '53529362091347968', '888.00', '1', '33', '', '1', null, null, '2020-08-26 09:52:03');
INSERT INTO `t_audit` VALUES ('56', '73', '32', '29', '53529362091347968', '888.00', '2', '33', '', '1', null, null, '2020-08-27 08:53:35');
INSERT INTO `t_audit` VALUES ('57', '74', '32', '29', '53877569812041728', '777.00', '1', '33', '', '1', null, null, '2020-08-27 08:54:46');
INSERT INTO `t_audit` VALUES ('58', '74', '32', '29', '53877569812041728', '777.00', '2', '33', '', '1', null, null, '2020-09-05 09:30:29');
INSERT INTO `t_audit` VALUES ('59', '75', '32', '29', '57148163009679360', '366.00', '1', '33', '', '4', '1', null, '2020-09-05 09:30:57');
INSERT INTO `t_audit` VALUES ('60', '76', '32', '28', '58654046633988096', '11112.00', '1', '33', '', '2', null, '', '2020-09-09 13:14:47');
INSERT INTO `t_audit` VALUES ('61', '76', '32', '28', '58654046633988096', '11112.00', '1', '33', '再来一次', '1', null, null, '2020-09-09 13:15:19');
INSERT INTO `t_audit` VALUES ('62', '76', '32', '28', '58654046633988096', '11112.00', '2', '33', '', '1', null, null, '2020-09-09 13:17:06');
INSERT INTO `t_audit` VALUES ('63', '77', '36', '31', '81460778405859328', '50000.00', '1', '37', '', '1', null, null, '2020-11-11 11:40:38');
INSERT INTO `t_audit` VALUES ('64', '77', '36', '31', '81460778405859328', '50000.00', '2', '37', '', '1', null, null, '2020-11-11 11:55:04');
INSERT INTO `t_audit` VALUES ('65', '78', '36', '31', '81464534069547008', '5000.00', '1', '37', '', '1', null, null, '2020-11-11 11:55:30');
INSERT INTO `t_audit` VALUES ('66', '78', '36', '31', '81464534069547008', '5000.00', '2', '37', '', '1', null, null, '2020-11-11 11:58:14');
INSERT INTO `t_audit` VALUES ('67', '79', '36', '31', '81466213263347712', '5000.00', '1', '37', '', '1', null, null, '2020-11-11 12:02:11');
INSERT INTO `t_audit` VALUES ('68', '79', '36', '31', '81466213263347712', '5000.00', '2', '37', '', '1', null, null, '2020-11-11 12:05:36');
INSERT INTO `t_audit` VALUES ('69', '80', '36', '31', '81468464501493760', '5000.00', '1', '37', '', '1', null, null, '2020-11-11 12:11:09');
INSERT INTO `t_audit` VALUES ('70', '80', '36', '31', '81468464501493760', '5000.00', '2', '37', '', '1', null, null, '2020-11-11 12:15:02');
INSERT INTO `t_audit` VALUES ('71', '81', '36', '31', '81471239155224576', '5000.00', '1', '37', '', '1', null, null, '2020-11-11 12:22:09');
INSERT INTO `t_audit` VALUES ('72', '81', '36', '31', '81471239155224576', '5000.00', '2', '37', '', '1', null, null, '2020-11-11 12:24:05');
INSERT INTO `t_audit` VALUES ('73', '82', '36', '31', '81472010403844096', '5000.00', '1', '37', '', '1', null, null, '2020-11-11 12:25:16');
INSERT INTO `t_audit` VALUES ('74', '83', '36', '31', '81472967472713728', '5000.00', '1', '37', '', '1', null, null, '2020-11-11 12:29:01');
INSERT INTO `t_audit` VALUES ('75', '84', '36', '31', '81479281972219904', '5000.00', '1', '37', '', '1', null, null, '2020-11-11 12:54:16');
INSERT INTO `t_audit` VALUES ('76', '85', '36', '31', '81479980944592896', '5000.00', '1', '37', '', '1', null, null, '2020-11-11 12:56:54');
INSERT INTO `t_audit` VALUES ('77', '86', '36', '31', '81480714691940352', '5000.00', '1', '37', '', '1', null, null, '2020-11-11 12:59:48');
INSERT INTO `t_audit` VALUES ('78', '87', '36', '31', '81481232596209664', '5000.00', '1', '37', '', '1', null, null, '2020-11-11 13:01:56');
INSERT INTO `t_audit` VALUES ('79', '88', '32', '27', '89477015564062720', '666.00', '1', '33', '', '1', null, null, '2020-12-03 14:34:23');
INSERT INTO `t_audit` VALUES ('80', '88', '32', '27', '89477015564062720', '666.00', '2', '33', '', '0', null, null, '2020-12-03 14:36:00');
INSERT INTO `t_audit` VALUES ('81', '89', '36', '31', '90874369739460608', '1212.00', '1', '37', '', '4', '1', null, '2020-12-07 11:06:49');
INSERT INTO `t_audit` VALUES ('82', '90', '36', '31', '90898340031631360', '777.00', '1', '37', '', '4', '2', '', '2020-12-07 12:42:06');
INSERT INTO `t_audit` VALUES ('83', '91', '36', '31', '90912091044712448', '122.00', '1', '37', '666', '2', null, '777', '2020-12-07 14:25:04');
INSERT INTO `t_audit` VALUES ('84', '91', '36', '31', '90912091044712448', '122.00', '1', '37', '888', '4', '1', null, '2020-12-07 14:25:37');
INSERT INTO `t_audit` VALUES ('85', '96', '36', '31', '95266200627580928', '666.00', '1', '37', '', '1', null, null, '2020-12-19 13:58:27');
INSERT INTO `t_audit` VALUES ('86', '96', '36', '31', '95266200627580928', '666.00', '2', '37', '', '1', null, null, '2020-12-19 13:59:52');
INSERT INTO `t_audit` VALUES ('87', '97', '36', '31', '95272789073858560', '66666.00', '1', '37', '', '1', null, null, '2020-12-19 14:24:41');
INSERT INTO `t_audit` VALUES ('88', '97', '36', '31', '95272789073858560', '66666.00', '2', '37', '', '1', null, null, '2020-12-23 15:26:02');
INSERT INTO `t_audit` VALUES ('89', '98', '36', '31', '96759879297011712', '678.00', '1', '37', '', '1', null, null, '2020-12-23 16:53:45');
INSERT INTO `t_audit` VALUES ('90', '98', '36', '31', '96759879297011712', '678.00', '2', '37', '', '2', null, '', '2020-12-23 16:54:12');
INSERT INTO `t_audit` VALUES ('91', '98', '36', '31', '96759879297011712', '678.00', '1', '37', '', '1', null, null, '2020-12-23 16:54:42');
INSERT INTO `t_audit` VALUES ('92', '98', '36', '31', '96759879297011712', '678.00', '2', '37', '', '1', null, null, '2020-12-23 16:55:10');
INSERT INTO `t_audit` VALUES ('93', '99', '36', '31', '96760990569467904', '66.00', '1', '37', '', '4', '1', null, '2020-12-23 16:58:27');
INSERT INTO `t_audit` VALUES ('94', '100', '36', '31', '96765387852615680', '6666.00', '1', '37', '', '1', null, null, '2020-12-23 17:15:37');
INSERT INTO `t_audit` VALUES ('95', '100', '36', '31', '96765387852615680', '6666.00', '2', '37', '', '1', null, null, '2020-12-23 17:15:49');
INSERT INTO `t_audit` VALUES ('96', '101', '44', '36', '101786243234402304', '12345.00', '1', '45', '', '1', null, null, '2021-01-06 13:46:44');
INSERT INTO `t_audit` VALUES ('97', '101', '44', '36', '101786243234402304', '12345.00', '2', '45', '', '1', null, null, '2021-01-06 13:47:04');

-- ----------------------------
-- Table structure for t_backup_device
-- ----------------------------
DROP TABLE IF EXISTS `t_backup_device`;
CREATE TABLE `t_backup_device` (
  `device_id` bigint NOT NULL AUTO_INCREMENT COMMENT '设备id',
  `acnum` varchar(50) NOT NULL COMMENT '表头号',
  `nickname` varchar(50) NOT NULL COMMENT '设备昵称',
  `warn_amount` varchar(50) NOT NULL COMMENT '默认警告金额（分为单位）',
  `max_amount` varchar(50) NOT NULL COMMENT '最大金额（分为单位）',
  `secret_key` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '加密秘钥',
  `valid_days` char(3) NOT NULL COMMENT '有效天数',
  `device_status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '状态：1正常 0冻结',
  `use_lock` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '0' COMMENT '是否使用加密锁 0 不使用 1 使用',
  `lock_info` varchar(50) DEFAULT NULL COMMENT '加密锁信息',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  PRIMARY KEY (`device_id`) USING BTREE,
  UNIQUE KEY `p_device_acnum` (`acnum`) USING BTREE,
  KEY `p_device_device_id` (`device_id`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='设备表';

-- ----------------------------
-- Records of t_backup_device
-- ----------------------------
INSERT INTO `t_backup_device` VALUES ('27', 'CPU223', 'CPU223', '10000.00', '100000.00', '90b1cfeee895c54a', '7', '1', '0', null, '2020-08-20 10:01:15');
INSERT INTO `t_backup_device` VALUES ('28', 'CPU111', 'CPU111', '10000.00', '100000.00', '6a244383aa6a83e4', '7', '1', '0', null, '2020-08-20 10:02:53');
INSERT INTO `t_backup_device` VALUES ('29', 'CPU222', 'CPU222', '10000.00', '100000.00', '8b588ca58b1becc8', '7', '1', '0', null, '2020-08-20 10:02:53');
INSERT INTO `t_backup_device` VALUES ('30', 'CPU333', 'CPU333', '10000.00', '100000.00', '2dc1f4d99e7fcadc', '7', '1', '0', null, '2020-08-20 10:02:53');
INSERT INTO `t_backup_device` VALUES ('31', 'S20008', 'S20008', '10000.00', '100000.00', 'de6065699a75d260', '7', '1', '0', null, '2020-11-11 10:50:40');
INSERT INTO `t_backup_device` VALUES ('32', 'CPU444', 'CPU444', '10000.00', '100000.00', '44bed2cd689055aa', '7', '1', '0', null, '2020-12-07 15:40:28');
INSERT INTO `t_backup_device` VALUES ('33', 'CPU555', 'CPU555', '80000.00', '900000.00', 'b59dafb2f91b74af', '10', '1', '0', null, '2020-12-07 15:40:28');
INSERT INTO `t_backup_device` VALUES ('34', 'CPU666', '8CPU666', '80000.00', '900000.00', 'acef44ecb441f668', '8', '1', '0', null, '2020-12-07 15:40:46');
INSERT INTO `t_backup_device` VALUES ('35', 'CPU777', 'CPU777', '70000.00', '700000.00', '16c91b1bbcab12b1', '7', '1', '0', null, '2020-12-07 16:09:17');
INSERT INTO `t_backup_device` VALUES ('36', 'AAA123', 'AAA123', '10000.00', '100000.00', '2e5858c0215a0e85', '7', '1', '0', null, '2021-01-06 09:43:25');
INSERT INTO `t_backup_device` VALUES ('37', 'CPU123', 'CPU123', '10000.00', '100000.00', '2c52b82ced2e2ec2', '7', '1', '0', null, '2021-03-23 13:13:51');

-- ----------------------------
-- Table structure for t_data_permission_test
-- ----------------------------
DROP TABLE IF EXISTS `t_data_permission_test`;
CREATE TABLE `t_data_permission_test` (
  `field1` varchar(20) NOT NULL,
  `field2` varchar(20) NOT NULL,
  `field3` varchar(20) NOT NULL,
  `field4` varchar(20) NOT NULL,
  `dept_id` int NOT NULL,
  `create_time` datetime NOT NULL,
  `id` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户权限测试';

-- ----------------------------
-- Records of t_data_permission_test
-- ----------------------------
INSERT INTO `t_data_permission_test` VALUES ('小米', '小米10pro', '4999', '珍珠白', '1', '2020-04-14 15:00:38', '1');
INSERT INTO `t_data_permission_test` VALUES ('腾讯', '黑鲨游戏手机3', '3799', '铠甲灰', '2', '2020-04-14 15:01:36', '2');
INSERT INTO `t_data_permission_test` VALUES ('华为', '华为p30', '3299', '天空之境', '1', '2020-04-14 15:03:11', '3');
INSERT INTO `t_data_permission_test` VALUES ('华为', '华为p40pro', '6488', '亮黑色', '3', '2020-04-14 15:04:31', '4');
INSERT INTO `t_data_permission_test` VALUES ('vivo', 'vivo iqoo 3', '3998', '拉力橙', '4', '2020-04-14 15:05:55', '5');
INSERT INTO `t_data_permission_test` VALUES ('一加', '一加7t', '3199', '冰际蓝', '5', '2020-04-14 15:06:53', '6');
INSERT INTO `t_data_permission_test` VALUES ('三星', '三星galaxy s10', '4098', '浩玉白', '6', '2020-04-14 15:08:25', '7');
INSERT INTO `t_data_permission_test` VALUES ('苹果', 'iphone 11 pro max', '9198', '暗夜绿', '4', '2020-04-14 15:09:20', '8');

-- ----------------------------
-- Table structure for t_dept
-- ----------------------------
DROP TABLE IF EXISTS `t_dept`;
CREATE TABLE `t_dept` (
  `dept_id` bigint NOT NULL AUTO_INCREMENT COMMENT '机构id',
  `parent_id` bigint NOT NULL COMMENT '上级机构id',
  `dept_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '机构名称',
  `order_num` bigint DEFAULT NULL COMMENT '排序',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`dept_id`) USING BTREE,
  KEY `t_dept_parent_id` (`parent_id`),
  KEY `t_dept_dept_name` (`dept_name`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='机构表';

-- ----------------------------
-- Records of t_dept
-- ----------------------------
INSERT INTO `t_dept` VALUES ('11', '0', '中国', '1', '2020-05-27 14:23:33', '2020-05-27 14:49:05');
INSERT INTO `t_dept` VALUES ('16', '0', '俄罗斯', '2', '2020-08-20 09:56:22', '2021-05-12 14:17:24');

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
INSERT INTO `t_device` VALUES ('27', 'CPU223', 'CPU223', '10000.00', '100000.00', '90b1cfeee895c54a', '7', '0', null, null, null, '1', '1', '0', null, '2', '2', '0', '1', null, null, null, null);
INSERT INTO `t_device` VALUES ('28', 'CPU111', 'CPU111', '10000.00', '100000.00', '6a244383aa6a83e4', '7', '0', null, null, null, '1', '1', '0', null, '3', '3', '0', '1', null, null, null, '2020-08-20 10:02:53');
INSERT INTO `t_device` VALUES ('29', 'CPU222', 'CPU222', '10000.00', '100000.00', '8b588ca58b1becc8', '7', '0', null, null, null, '1', '1', '0', null, '4', '4', '0', '1', null, null, null, '2020-08-20 10:02:53');
INSERT INTO `t_device` VALUES ('30', 'CPU333', 'CPU333', '10000.00', '100000.00', '2dc1f4d99e7fcadc', '7', '0', null, null, null, '1', '1', '0', null, '5', '5', '0', '1', null, null, null, '2020-08-20 10:02:53');
INSERT INTO `t_device` VALUES ('31', 'S20008', 'S20008', '10000.00', '100000.00', 'de6065699a75d260', '7', '0', null, null, null, '1', '1', '0', null, '6', '6', '0', '1', null, null, null, '2020-11-11 10:50:40');
INSERT INTO `t_device` VALUES ('32', 'CPU444', 'CPU444', '10000.00', '100000.00', '44bed2cd689055aa', '7', '0', null, null, null, '1', '1', '0', null, '7', '7', '0', '1', null, null, null, '2020-12-07 15:40:28');
INSERT INTO `t_device` VALUES ('33', 'CPU555', 'CPU555', '80000.00', '900000.00', 'b59dafb2f91b74af', '10', '0', null, null, null, '1', '1', '0', null, '8', '8', '0', '1', null, null, null, '2020-12-07 15:40:28');
INSERT INTO `t_device` VALUES ('34', 'CPU666', '8CPU666', '80000.00', '900000.00', 'acef44ecb441f668', '8', '0', null, null, null, '1', '1', '0', null, '9', '9', '0', '1', null, null, null, '2020-12-07 15:40:46');
INSERT INTO `t_device` VALUES ('35', 'CPU777', 'CPU777', '70000.00', '700000.00', '16c91b1bbcab12b1', '7', '0', null, null, null, '1', '1', '0', null, '10', '10', '0', '1', null, null, null, '2020-12-07 16:09:17');
INSERT INTO `t_device` VALUES ('36', 'AAA123', 'AAA123', '10000.00', '100000.00', '2e5858c0215a0e85', '7', '0', null, null, null, '1', '1', '0', null, '11', '11', '0', '1', null, null, null, '2021-01-06 09:43:25');
INSERT INTO `t_device` VALUES ('37', 'CPU123', 'CPU1234', '0.00', '0.00', '', '0', '', '', '', null, '0', '1', '1', '13', '12号邮局', '1.0', '0', '0', '', '', '2021-07-07 12:36:47', '2021-04-22 16:41:55');
INSERT INTO `t_device` VALUES ('38', 'MXX001', 'MXX001', '10000.00', '100000.00', 'b4b9d97816a46b25', '7', '0', null, 'FM100002', null, '1', '1', '1', '11', '994700', '22.1.1', '1', '1', null, null, '2021-07-07 14:01:42', '2021-05-13 09:36:26');
INSERT INTO `t_device` VALUES ('39', 'CPU567', 'CPU567', '10000.00', '100000.00', '5cdb4b82335f2bd6', '7', '0', null, null, null, '1', null, null, null, null, null, '0', null, null, null, null, '2021-05-22 11:06:13');

-- ----------------------------
-- Table structure for t_eximport
-- ----------------------------
DROP TABLE IF EXISTS `t_eximport`;
CREATE TABLE `t_eximport` (
  `field1` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字段1',
  `field2` int NOT NULL COMMENT '字段2',
  `field3` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字段3',
  `create_time` datetime NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='excel导入导出测试';

-- ----------------------------
-- Records of t_eximport
-- ----------------------------
INSERT INTO `t_eximport` VALUES ('字段1', '1', 'test@gmail.com', '2020-08-20 03:14:06');

-- ----------------------------
-- Table structure for t_generator_config
-- ----------------------------
DROP TABLE IF EXISTS `t_generator_config`;
CREATE TABLE `t_generator_config` (
  `id` int NOT NULL COMMENT '主键',
  `author` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '作者',
  `base_package` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '基础包名',
  `entity_package` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'entity文件存放路径',
  `mapper_package` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'mapper文件存放路径',
  `mapper_xml_package` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'mapper xml文件存放路径',
  `service_package` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'servcie文件存放路径',
  `service_impl_package` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'serviceimpl文件存放路径',
  `controller_package` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'controller文件存放路径',
  `is_trim` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '是否去除前缀 1是 0否',
  `trim_value` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '前缀内容',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='代码生成配置表';

-- ----------------------------
-- Records of t_generator_config
-- ----------------------------
INSERT INTO `t_generator_config` VALUES ('1', 'mrbird', 'cc.mrbird.febs.gen', 'entity', 'mapper', 'mapper', 'service', 'service.impl', 'controller', '1', 't_');

-- ----------------------------
-- Table structure for t_job
-- ----------------------------
DROP TABLE IF EXISTS `t_job`;
CREATE TABLE `t_job` (
  `job_id` bigint NOT NULL AUTO_INCREMENT COMMENT '任务id',
  `bean_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'spring bean名称',
  `method_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '方法名',
  `params` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '参数',
  `cron_expression` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'cron表达式',
  `status` char(2) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '任务状态  0：正常  1：暂停',
  `remark` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`job_id`) USING BTREE,
  KEY `t_job_create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='定时任务表';

-- ----------------------------
-- Records of t_job
-- ----------------------------
INSERT INTO `t_job` VALUES ('1', 'testtask', 'test', 'mrbird', '0/1 * * * * ?', '1', '有参任务调度测试~~', '2018-02-24 16:26:14');
INSERT INTO `t_job` VALUES ('2', 'testtask', 'test1', null, '0/10 * * * * ?', '1', '无参任务调度测试', '2018-02-24 17:06:23');
INSERT INTO `t_job` VALUES ('3', 'testtask', 'test', 'hello world', '0/1 * * * * ?', '1', '有参任务调度测试,每隔一秒触发', '2018-02-26 09:28:26');
INSERT INTO `t_job` VALUES ('11', 'testtask', 'test2', null, '0/5 * * * * ?', '1', '测试异常', '2018-02-26 11:15:30');

-- ----------------------------
-- Table structure for t_job_log
-- ----------------------------
DROP TABLE IF EXISTS `t_job_log`;
CREATE TABLE `t_job_log` (
  `log_id` bigint NOT NULL AUTO_INCREMENT COMMENT '任务日志id',
  `job_id` bigint NOT NULL COMMENT '任务id',
  `bean_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'spring bean名称',
  `method_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '方法名',
  `params` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '参数',
  `status` char(2) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '任务状态    0：成功    1：失败',
  `error` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '失败信息',
  `times` decimal(11,0) DEFAULT NULL COMMENT '耗时(单位：毫秒)',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`log_id`) USING BTREE,
  KEY `t_job_log_create_time` (`create_time`)
) ENGINE=MyISAM AUTO_INCREMENT=2562 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='调度日志表';

-- ----------------------------
-- Records of t_job_log
-- ----------------------------

-- ----------------------------
-- Table structure for t_log
-- ----------------------------
DROP TABLE IF EXISTS `t_log`;
CREATE TABLE `t_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志id',
  `username` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '操作用户',
  `operation` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '操作内容',
  `time` decimal(11,0) DEFAULT NULL COMMENT '耗时',
  `method` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '操作方法',
  `params` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '方法参数',
  `ip` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '操作者ip',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `location` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '操作地点',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `t_log_create_time` (`create_time`)
) ENGINE=MyISAM AUTO_INCREMENT=173 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='操作日志表';

-- ----------------------------
-- Records of t_log
-- ----------------------------
INSERT INTO `t_log` VALUES ('1', 'admin', '用户列表', '24', 'cc.mrbird.febs.system.controller.UserController.userList()', ' user: \"User(userId=8, username=null, password=null, realname=null, parentId=null, deptId=null, email=null, mobile=null, status=null, createTime=null, modifyTime=null, lastLoginTime=null, sex=null, avatar=null, theme=null, isTab=null, description=null, deptName=null, createTimeFrom=null, createTimeTo=null, roleId=1, roleName=null, deptIds=null)\" request: \"QueryRequest(pageSize=10, pageNum=1, field=null, order=null)\"', '119.37.199.25', '2020-08-20 09:29:55', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('2', 'admin', '用户列表', '19', 'cc.mrbird.febs.system.controller.UserController.userList()', ' user: \"User(userId=8, username=null, password=null, realname=null, parentId=null, deptId=null, email=null, mobile=null, status=null, createTime=null, modifyTime=null, lastLoginTime=null, sex=null, avatar=null, theme=null, isTab=null, description=null, deptName=null, createTimeFrom=null, createTimeTo=null, roleId=1, roleName=null, deptIds=null)\" request: \"QueryRequest(pageSize=10, pageNum=1, field=null, order=null)\"', '119.37.199.25', '2020-08-20 09:46:09', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('3', 'admin', '新增用户', '56', 'cc.mrbird.febs.system.controller.UserController.addUser()', ' user: \"User(userId=30, username=rp123, password=7e57a95d3c935590b01c2fd25706ad1c, realname=么的感情的大虾, parentId=0, deptId=11, email=, mobile=18788897466, status=1, createTime=Thu Aug 20 09:48:55 CST 2020, modifyTime=null, lastLoginTime=null, sex=0, avatar=default.jpg, theme=black, isTab=1, description=, deptName=null, createTimeFrom=null, createTimeTo=null, roleId=1, roleName=null, deptIds=null)\"', '119.37.199.25', '2020-08-20 09:48:56', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('4', 'admin', '用户列表', '16', 'cc.mrbird.febs.system.controller.UserController.userList()', ' user: \"User(userId=8, username=, password=null, realname=, parentId=null, deptId=null, email=null, mobile=, status=, createTime=null, modifyTime=null, lastLoginTime=null, sex=, avatar=null, theme=null, isTab=null, description=null, deptName=null, createTimeFrom=null, createTimeTo=null, roleId=1, roleName=null, deptIds=null)\" request: \"QueryRequest(pageSize=10, pageNum=1, field=createTime, order=)\"', '119.37.199.25', '2020-08-20 09:48:56', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('5', 'admin', '修改用户', '74', 'cc.mrbird.febs.system.controller.UserController.updateUser()', ' user: \"User(userId=30, username=null, password=null, realname=么的感情的大虾, parentId=null, deptId=11, email=, mobile=18788897466, status=1, createTime=null, modifyTime=Thu Aug 20 09:49:26 CST 2020, lastLoginTime=null, sex=0, avatar=null, theme=null, isTab=null, description=, deptName=null, createTimeFrom=null, createTimeTo=null, roleId=2, roleName=null, deptIds=null)\"', '119.37.199.25', '2020-08-20 09:49:26', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('6', 'admin', '用户列表', '15', 'cc.mrbird.febs.system.controller.UserController.userList()', ' user: \"User(userId=8, username=, password=null, realname=, parentId=null, deptId=null, email=null, mobile=, status=, createTime=null, modifyTime=null, lastLoginTime=null, sex=, avatar=null, theme=null, isTab=null, description=null, deptName=null, createTimeFrom=null, createTimeTo=null, roleId=1, roleName=null, deptIds=null)\" request: \"QueryRequest(pageSize=10, pageNum=1, field=createTime, order=)\"', '119.37.199.25', '2020-08-20 09:49:27', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('7', 'admin', '删除用户', '29', 'cc.mrbird.febs.system.controller.UserController.deleteUsers()', ' userIds: \"30\"', '119.37.199.25', '2020-08-20 09:49:44', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('8', 'admin', '用户列表', '18', 'cc.mrbird.febs.system.controller.UserController.userList()', ' user: \"User(userId=8, username=, password=null, realname=, parentId=null, deptId=null, email=null, mobile=, status=, createTime=null, modifyTime=null, lastLoginTime=null, sex=, avatar=null, theme=null, isTab=null, description=null, deptName=null, createTimeFrom=null, createTimeTo=null, roleId=1, roleName=null, deptIds=null)\" request: \"QueryRequest(pageSize=10, pageNum=1, field=createTime, order=)\"', '119.37.199.25', '2020-08-20 09:49:44', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('9', 'admin', '获取页面列表', '12', 'cc.mrbird.febs.device.controller.DeviceController.devicePageList()', ' request: \"QueryRequest(pageSize=10, pageNum=1, field=null, order=null)\" device: \"Device(deviceId=null, acnum=null, nickname=null, warnAmount=null, maxAmount=null, secretKey=null, validDays=null, deviceStatus=null, useLock=null, lockInfo=null, createTime=null)\"', '119.37.199.25', '2020-08-20 09:49:48', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('10', 'admin', '获取注资状态列表', '13', 'cc.mrbird.febs.order.controller.OrderController.selectStatus()', '', '119.37.199.25', '2020-08-20 09:49:49', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('11', 'admin', '获取注资分页列表', '17', 'cc.mrbird.febs.order.controller.OrderController.orderList()', ' request: \"QueryRequest(pageSize=10, pageNum=1, field=null, order=null)\" order: OrderVo{acnum=\'null\', nickname=\'null\', auditType=\'null\', applyUserName=\'null\', auditUserName=null, closeUserName=null, orderId=null, orderNumber=\'null\', amount=\'null\', orderStatus=\'null\', applyUserId=null, auditUserId=null, closeUserId=null, expireDays=null, endTime=null, createTime=null, btnList=[], submitInfo=null}', '119.37.199.25', '2020-08-20 09:49:49', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('12', 'admin', '获取审核状态列表', '8', 'cc.mrbird.febs.audit.controller.AuditController.selectStatus()', '', '119.37.199.25', '2020-08-20 09:49:56', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('13', 'admin', '获取注资状态列表', '1', 'cc.mrbird.febs.order.controller.OrderController.selectStatus()', '', '119.37.199.25', '2020-08-20 09:52:08', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('14', 'admin', '获取注资分页列表', '10', 'cc.mrbird.febs.order.controller.OrderController.orderList()', ' request: \"QueryRequest(pageSize=10, pageNum=1, field=null, order=null)\" order: OrderVo{acnum=\'null\', nickname=\'null\', auditType=\'null\', applyUserName=\'null\', auditUserName=null, closeUserName=null, orderId=null, orderNumber=\'null\', amount=\'null\', orderStatus=\'null\', applyUserId=null, auditUserId=null, closeUserId=null, expireDays=null, endTime=null, createTime=null, btnList=[], submitInfo=null}', '119.37.199.25', '2020-08-20 09:52:09', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('15', 'admin', '用户列表', '17', 'cc.mrbird.febs.system.controller.UserController.userList()', ' user: \"User(userId=8, username=null, password=null, realname=null, parentId=null, deptId=null, email=null, mobile=null, status=null, createTime=null, modifyTime=null, lastLoginTime=null, sex=null, avatar=null, theme=null, isTab=null, description=null, deptName=null, createTimeFrom=null, createTimeTo=null, roleId=1, roleName=null, deptIds=null)\" request: \"QueryRequest(pageSize=10, pageNum=1, field=null, order=null)\"', '119.37.199.25', '2020-08-20 09:53:31', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('16', 'admin', '新增用户', '15', 'cc.mrbird.febs.system.controller.UserController.addUser()', ' user: \"User(userId=31, username=23333a, password=e69be170ce9373441fe61944c0597e0d, realname=aaaaaa, parentId=0, deptId=11, email=, mobile=, status=1, createTime=Thu Aug 20 09:54:42 CST 2020, modifyTime=null, lastLoginTime=null, sex=0, avatar=default.jpg, theme=black, isTab=1, description=, deptName=null, createTimeFrom=null, createTimeTo=null, roleId=2, roleName=null, deptIds=null)\"', '119.37.199.25', '2020-08-20 09:54:43', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('17', 'admin', '用户列表', '13', 'cc.mrbird.febs.system.controller.UserController.userList()', ' user: \"User(userId=8, username=, password=null, realname=, parentId=null, deptId=null, email=null, mobile=, status=, createTime=null, modifyTime=null, lastLoginTime=null, sex=, avatar=null, theme=null, isTab=null, description=null, deptName=null, createTimeFrom=null, createTimeTo=null, roleId=1, roleName=null, deptIds=null)\" request: \"QueryRequest(pageSize=10, pageNum=1, field=createTime, order=)\"', '119.37.199.25', '2020-08-20 09:54:43', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('18', 'admin', '新增部门', '8', 'cc.mrbird.febs.system.controller.DeptController.addDept()', ' dept: \"Dept(deptId=16, parentId=0, deptName=yyy, orderNum=null, createTime=Thu Aug 20 09:56:21 CST 2020, modifyTime=null)\"', '119.37.199.25', '2020-08-20 09:56:22', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('19', 'admin', '修改部门', '26', 'cc.mrbird.febs.system.controller.DeptController.updateDept()', ' dept: \"Dept(deptId=16, parentId=0, deptName=yyy, orderNum=2, createTime=null, modifyTime=Thu Aug 20 09:56:35 CST 2020)\"', '119.37.199.25', '2020-08-20 09:56:35', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('20', 'admin', '用户列表', '31', 'cc.mrbird.febs.system.controller.UserController.userList()', ' user: \"User(userId=8, username=null, password=null, realname=null, parentId=null, deptId=null, email=null, mobile=null, status=null, createTime=null, modifyTime=null, lastLoginTime=null, sex=null, avatar=null, theme=null, isTab=null, description=null, deptName=null, createTimeFrom=null, createTimeTo=null, roleId=1, roleName=null, deptIds=null)\" request: \"QueryRequest(pageSize=10, pageNum=1, field=null, order=null)\"', '119.37.199.25', '2020-08-20 09:59:01', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('21', 'admin', '获取页面列表', '7', 'cc.mrbird.febs.device.controller.DeviceController.devicePageList()', ' request: \"QueryRequest(pageSize=10, pageNum=1, field=null, order=null)\" device: \"Device(deviceId=null, acnum=null, nickname=null, warnAmount=null, maxAmount=null, secretKey=null, validDays=null, deviceStatus=null, useLock=null, lockInfo=null, createTime=null)\"', '119.37.199.25', '2020-08-20 10:01:01', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('22', 'admin', '获取机构用户列表', '9', 'cc.mrbird.febs.system.controller.UserController.deptUserList()', '', '119.37.199.25', '2020-08-20 10:01:04', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('23', 'admin', '检查表头号是否存在', '16', 'cc.mrbird.febs.device.controller.DeviceController.checkIsExist()', ' acnumList: \"CPU223\"', '119.37.199.25', '2020-08-20 10:01:15', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('24', 'admin', '新增Device', '36', 'cc.mrbird.febs.device.controller.DeviceController.addDevice()', ' device: \"Device(deviceId=27, acnum=CPU223, nickname=CPU223, warnAmount=10000.00, maxAmount=100000.00, secretKey=90b1cfeee895c54a, validDays=7, deviceStatus=1, useLock=null, lockInfo=null, createTime=Thu Aug 20 10:01:15 CST 2020)\" bindUserId: \"31\" acnumList: \"CPU223\"', '119.37.199.25', '2020-08-20 10:01:15', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('25', 'admin', '获取页面列表', '9', 'cc.mrbird.febs.device.controller.DeviceController.devicePageList()', ' request: \"QueryRequest(pageSize=10, pageNum=1, field=createTime, order=)\" device: \"Device(deviceId=null, acnum=, nickname=, warnAmount=null, maxAmount=null, secretKey=null, validDays=null, deviceStatus=, useLock=null, lockInfo=null, createTime=null)\"', '119.37.199.25', '2020-08-20 10:01:15', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('26', 'admin', '获取机构用户列表', '7', 'cc.mrbird.febs.system.controller.UserController.deptUserList()', '', '119.37.199.25', '2020-08-20 10:01:32', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('27', 'admin', '获取机构用户列表', '6', 'cc.mrbird.febs.system.controller.UserController.deptUserList()', '', '119.37.199.25', '2020-08-20 10:01:41', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('28', 'admin', '获取机构用户列表', '9', 'cc.mrbird.febs.system.controller.UserController.deptUserList()', '', '119.37.199.25', '2020-08-20 10:02:30', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('29', 'admin', '检查表头号是否存在', '6', 'cc.mrbird.febs.device.controller.DeviceController.checkIsExist()', ' acnumList: \"CPU111,CPU222,CPU333\"', '119.37.199.25', '2020-08-20 10:02:53', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('30', 'admin', '新增Device', '51', 'cc.mrbird.febs.device.controller.DeviceController.addDevice()', ' device: \"Device(deviceId=30, acnum=CPU333, nickname=CPU333, warnAmount=10000.00, maxAmount=100000.00, secretKey=2dc1f4d99e7fcadc, validDays=7, deviceStatus=1, useLock=null, lockInfo=null, createTime=Thu Aug 20 10:02:53 CST 2020)\" bindUserId: \"31\" acnumList: \"CPU111,CPU222,CPU333\"', '119.37.199.25', '2020-08-20 10:02:53', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('31', 'admin', '获取页面列表', '10', 'cc.mrbird.febs.device.controller.DeviceController.devicePageList()', ' request: \"QueryRequest(pageSize=10, pageNum=1, field=createTime, order=)\" device: \"Device(deviceId=null, acnum=, nickname=, warnAmount=null, maxAmount=null, secretKey=null, validDays=null, deviceStatus=, useLock=null, lockInfo=null, createTime=null)\"', '119.37.199.25', '2020-08-20 10:02:53', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('32', 'admin', '导出Excel', '37', 'cc.mrbird.febs.device.controller.DeviceController.export()', ' queryRequest: \"QueryRequest(pageSize=10, pageNum=1, field=createTime, order=null)\" device: \"Device(deviceId=null, acnum=, nickname=, warnAmount=null, maxAmount=null, secretKey=null, validDays=null, deviceStatus=, useLock=null, lockInfo=null, createTime=null)\" response: org.apache.shiro.web.servlet.ShiroHttpServletResponse@5ba85148', '119.37.199.25', '2020-08-20 10:03:01', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('33', 'admin', '获取审核状态列表', '1', 'cc.mrbird.febs.audit.controller.AuditController.selectStatus()', '', '119.37.199.25', '2020-08-20 10:03:20', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('34', '23333a', '获取注资状态列表', '1', 'cc.mrbird.febs.order.controller.OrderController.selectStatus()', '', '119.37.199.25', '2020-08-20 10:04:28', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('35', '23333a', '获取注资分页列表', '9', 'cc.mrbird.febs.order.controller.OrderController.orderList()', ' request: \"QueryRequest(pageSize=10, pageNum=1, field=null, order=null)\" order: OrderVo{acnum=\'null\', nickname=\'null\', auditType=\'null\', applyUserName=\'null\', auditUserName=null, closeUserName=null, orderId=null, orderNumber=\'null\', amount=\'null\', orderStatus=\'null\', applyUserId=null, auditUserId=null, closeUserId=null, expireDays=null, endTime=null, createTime=null, btnList=[], submitInfo=null}', '119.37.199.25', '2020-08-20 10:04:28', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('36', '23333a', '获取页面列表', '10', 'cc.mrbird.febs.device.controller.DeviceController.devicePageList()', ' request: \"QueryRequest(pageSize=10, pageNum=1, field=null, order=null)\" device: \"Device(deviceId=null, acnum=null, nickname=null, warnAmount=null, maxAmount=null, secretKey=null, validDays=null, deviceStatus=null, useLock=null, lockInfo=null, createTime=null)\"', '119.37.199.25', '2020-08-20 10:04:29', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('37', '23333a', '用户列表', '14', 'cc.mrbird.febs.system.controller.UserController.userList()', ' user: \"User(userId=31, username=null, password=null, realname=null, parentId=null, deptId=null, email=null, mobile=null, status=null, createTime=null, modifyTime=null, lastLoginTime=null, sex=null, avatar=null, theme=null, isTab=null, description=null, deptName=null, createTimeFrom=null, createTimeTo=null, roleId=2, roleName=null, deptIds=null)\" request: \"QueryRequest(pageSize=10, pageNum=1, field=null, order=null)\"', '119.37.199.25', '2020-08-20 10:04:38', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('38', '23333a', '新增用户', '42', 'cc.mrbird.febs.system.controller.UserController.addUser()', ' user: \"User(userId=32, username=rp123456, password=b60a884d39718b8730c780d74a936e00, realname=rp123456, parentId=31, deptId=11, email=, mobile=, status=1, createTime=Thu Aug 20 10:04:58 CST 2020, modifyTime=null, lastLoginTime=null, sex=0, avatar=default.jpg, theme=black, isTab=1, description=, deptName=null, createTimeFrom=null, createTimeTo=null, roleId=3, roleName=null, deptIds=null)\"', '119.37.199.25', '2020-08-20 10:04:59', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('39', '23333a', '用户列表', '16', 'cc.mrbird.febs.system.controller.UserController.userList()', ' user: \"User(userId=31, username=, password=null, realname=, parentId=null, deptId=null, email=null, mobile=, status=, createTime=null, modifyTime=null, lastLoginTime=null, sex=, avatar=null, theme=null, isTab=null, description=null, deptName=null, createTimeFrom=null, createTimeTo=null, roleId=2, roleName=null, deptIds=null)\" request: \"QueryRequest(pageSize=10, pageNum=1, field=createTime, order=)\"', '119.37.199.25', '2020-08-20 10:04:59', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('40', '23333a', '新增用户', '16', 'cc.mrbird.febs.system.controller.UserController.addUser()', ' user: \"User(userId=33, username=123456rp, password=215651f1bb450748489623b61f49ebf4, realname=123456rp, parentId=31, deptId=11, email=, mobile=, status=1, createTime=Thu Aug 20 10:05:19 CST 2020, modifyTime=null, lastLoginTime=null, sex=0, avatar=default.jpg, theme=black, isTab=1, description=, deptName=null, createTimeFrom=null, createTimeTo=null, roleId=4, roleName=null, deptIds=null)\"', '119.37.199.25', '2020-08-20 10:05:20', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('41', '23333a', '用户列表', '13', 'cc.mrbird.febs.system.controller.UserController.userList()', ' user: \"User(userId=31, username=, password=null, realname=, parentId=null, deptId=null, email=null, mobile=, status=, createTime=null, modifyTime=null, lastLoginTime=null, sex=, avatar=null, theme=null, isTab=null, description=null, deptName=null, createTimeFrom=null, createTimeTo=null, roleId=2, roleName=null, deptIds=null)\" request: \"QueryRequest(pageSize=10, pageNum=1, field=createTime, order=)\"', '119.37.199.25', '2020-08-20 10:05:20', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('42', '23333a', '获取列表', '21', 'cc.mrbird.febs.device.controller.DeviceController.allList()', ' bindUserId: \"32\"', '119.37.199.25', '2020-08-20 10:05:29', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('43', '23333a', '分配设备', '22', 'cc.mrbird.febs.device.controller.DeviceController.sendDevice()', ' deviceIds: \"28\" bindUserId: \"32\"', '119.37.199.25', '2020-08-20 10:05:36', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('44', '23333a', '用户列表', '15', 'cc.mrbird.febs.system.controller.UserController.userList()', ' user: \"User(userId=31, username=, password=null, realname=, parentId=null, deptId=null, email=null, mobile=, status=, createTime=null, modifyTime=null, lastLoginTime=null, sex=, avatar=null, theme=null, isTab=null, description=null, deptName=null, createTimeFrom=null, createTimeTo=null, roleId=2, roleName=null, deptIds=null)\" request: \"QueryRequest(pageSize=10, pageNum=1, field=createTime, order=)\"', '119.37.199.25', '2020-08-20 10:05:36', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('45', '23333a', '获取列表', '20', 'cc.mrbird.febs.device.controller.DeviceController.allList()', ' bindUserId: \"33\"', '119.37.199.25', '2020-08-20 10:05:38', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('46', '23333a', '分配设备', '37', 'cc.mrbird.febs.device.controller.DeviceController.sendDevice()', ' deviceIds: \"29\" bindUserId: \"33\"', '119.37.199.25', '2020-08-20 10:05:43', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('47', '23333a', '用户列表', '13', 'cc.mrbird.febs.system.controller.UserController.userList()', ' user: \"User(userId=31, username=, password=null, realname=, parentId=null, deptId=null, email=null, mobile=, status=, createTime=null, modifyTime=null, lastLoginTime=null, sex=, avatar=null, theme=null, isTab=null, description=null, deptName=null, createTimeFrom=null, createTimeTo=null, roleId=2, roleName=null, deptIds=null)\" request: \"QueryRequest(pageSize=10, pageNum=1, field=createTime, order=)\"', '119.37.199.25', '2020-08-20 10:05:43', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('48', '23333a', '获取列表', '23', 'cc.mrbird.febs.device.controller.DeviceController.allList()', ' bindUserId: \"33\"', '119.37.199.25', '2020-08-20 10:05:54', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('49', '23333a', '获取列表', '20', 'cc.mrbird.febs.device.controller.DeviceController.allList()', ' bindUserId: \"32\"', '119.37.199.25', '2020-08-20 10:05:58', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('50', '23333a', '分配设备', '29', 'cc.mrbird.febs.device.controller.DeviceController.sendDevice()', ' deviceIds: \"28,29,30,27\" bindUserId: \"32\"', '119.37.199.25', '2020-08-20 10:06:12', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('51', '23333a', '用户列表', '12', 'cc.mrbird.febs.system.controller.UserController.userList()', ' user: \"User(userId=31, username=, password=null, realname=, parentId=null, deptId=null, email=null, mobile=, status=, createTime=null, modifyTime=null, lastLoginTime=null, sex=, avatar=null, theme=null, isTab=null, description=null, deptName=null, createTimeFrom=null, createTimeTo=null, roleId=2, roleName=null, deptIds=null)\" request: \"QueryRequest(pageSize=10, pageNum=1, field=createTime, order=)\"', '119.37.199.25', '2020-08-20 10:06:12', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('52', '123456rp', '获取注资状态列表', '1', 'cc.mrbird.febs.order.controller.OrderController.selectStatus()', '', '119.37.199.25', '2020-08-20 10:08:50', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('53', '123456rp', '获取注资分页列表', '10', 'cc.mrbird.febs.order.controller.OrderController.orderList()', ' request: \"QueryRequest(pageSize=10, pageNum=1, field=null, order=null)\" order: OrderVo{acnum=\'null\', nickname=\'null\', auditType=\'null\', applyUserName=\'null\', auditUserName=null, closeUserName=null, orderId=null, orderNumber=\'null\', amount=\'null\', orderStatus=\'null\', applyUserId=null, auditUserId=null, closeUserId=null, expireDays=null, endTime=null, createTime=null, btnList=[], submitInfo=null}', '119.37.199.25', '2020-08-20 10:08:50', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('54', '123456rp', '获取页面列表', '10', 'cc.mrbird.febs.device.controller.DeviceController.devicePageList()', ' request: \"QueryRequest(pageSize=10, pageNum=1, field=null, order=null)\" device: \"Device(deviceId=null, acnum=null, nickname=null, warnAmount=null, maxAmount=null, secretKey=null, validDays=null, deviceStatus=null, useLock=null, lockInfo=null, createTime=null)\"', '119.37.199.25', '2020-08-20 10:08:51', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('55', '123456rp', '获取表头号列表', '8', 'cc.mrbird.febs.order.controller.OrderController.getAcnumList()', '', '119.37.199.25', '2020-08-20 10:09:04', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('56', '123456rp', '获取审核员列表', '9', 'cc.mrbird.febs.order.controller.OrderController.getAuditUserNameList()', ' deviceId: \"29\"', '119.37.199.25', '2020-08-20 10:09:06', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('57', '123456rp', '新增', '29', 'cc.mrbird.febs.order.controller.OrderController.addOrder()', ' order: OrderVo{acnum=\'CPU222\', nickname=\'null\', auditType=\'null\', applyUserName=\'null\', auditUserName=null, closeUserName=null, orderId=63, orderNumber=\'51359631238369280\', amount=\'11111.00\', orderStatus=\'1\', applyUserId=33, auditUserId=32, closeUserId=null, expireDays=7, endTime=Thu Aug 27 10:09:19 CST 2020, createTime=Thu Aug 20 10:09:19 CST 2020, btnList=[], submitInfo=null}', '119.37.199.25', '2020-08-20 10:09:19', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('58', '123456rp', '获取注资分页列表', '10', 'cc.mrbird.febs.order.controller.OrderController.orderList()', ' request: \"QueryRequest(pageSize=10, pageNum=1, field=createTime, order=)\" order: OrderVo{acnum=\'\', nickname=\'null\', auditType=\'null\', applyUserName=\'null\', auditUserName=null, closeUserName=null, orderId=null, orderNumber=\'\', amount=\'\', orderStatus=\'\', applyUserId=null, auditUserId=null, closeUserId=null, expireDays=null, endTime=null, createTime=null, btnList=[], submitInfo=null}', '119.37.199.25', '2020-08-20 10:09:19', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('59', '123456rp', '获取注资分页列表', '9', 'cc.mrbird.febs.order.controller.OrderController.orderList()', ' request: \"QueryRequest(pageSize=10, pageNum=1, field=null, order=null)\" order: OrderVo{acnum=\'\', nickname=\'null\', auditType=\'null\', applyUserName=\'null\', auditUserName=null, closeUserName=null, orderId=null, orderNumber=\'\', amount=\'\', orderStatus=\'\', applyUserId=null, auditUserId=null, closeUserId=null, expireDays=null, endTime=null, createTime=null, btnList=[], submitInfo=null}', '119.37.199.25', '2020-08-20 10:09:54', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('60', '123456rp', '提交注资审核', '1', 'cc.mrbird.febs.order.controller.ViewController.submitApply()', ' orderId: \"63\" audityType: \"1\" orderId: \"63\" audityType: \"1\"', '119.37.199.25', '2020-08-20 10:10:03', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('61', '123456rp', '提交审核', '33', 'cc.mrbird.febs.order.controller.OrderController.submitApply()', ' orderVo: OrderVo{acnum=\'CPU222\', nickname=\'null\', auditType=\'1\', applyUserName=\'null\', auditUserName=null, closeUserName=null, orderId=63, orderNumber=\'51359631238369280\', amount=\'11111.00\', orderStatus=\'2\', applyUserId=33, auditUserId=32, closeUserId=null, expireDays=7, endTime=Thu Aug 27 10:09:19 CST 2020, createTime=Thu Aug 20 10:09:19 CST 2020, btnList=[], submitInfo=wowowowo} auditType: \"1\"', '119.37.199.25', '2020-08-20 10:10:12', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('62', '123456rp', '获取注资分页列表', '11', 'cc.mrbird.febs.order.controller.OrderController.orderList()', ' request: \"QueryRequest(pageSize=10, pageNum=1, field=createTime, order=null)\" order: OrderVo{acnum=\'\', nickname=\'null\', auditType=\'null\', applyUserName=\'null\', auditUserName=null, closeUserName=null, orderId=null, orderNumber=\'\', amount=\'\', orderStatus=\'\', applyUserId=null, auditUserId=null, closeUserId=null, expireDays=null, endTime=null, createTime=null, btnList=[], submitInfo=null}', '119.37.199.25', '2020-08-20 10:10:12', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('63', 'rp123456', '获取页面列表', '12', 'cc.mrbird.febs.device.controller.DeviceController.devicePageList()', ' request: \"QueryRequest(pageSize=10, pageNum=1, field=null, order=null)\" device: \"Device(deviceId=null, acnum=null, nickname=null, warnAmount=null, maxAmount=null, secretKey=null, validDays=null, deviceStatus=null, useLock=null, lockInfo=null, createTime=null)\"', '119.37.199.25', '2020-08-20 10:12:22', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('64', 'rp123456', '获取审核状态列表', '1', 'cc.mrbird.febs.audit.controller.AuditController.selectStatus()', '', '119.37.199.25', '2020-08-20 10:12:25', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('65', 'rp123456', '审核通过', '26', 'cc.mrbird.febs.audit.controller.AuditController.passAudit()', ' auditId: \"44\"', '119.37.199.25', '2020-08-20 10:12:41', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('66', '123456rp', '获取注资状态列表', '1', 'cc.mrbird.febs.order.controller.OrderController.selectStatus()', '', '119.37.199.25', '2020-08-20 10:12:57', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('67', '123456rp', '获取注资分页列表', '14', 'cc.mrbird.febs.order.controller.OrderController.orderList()', ' request: \"QueryRequest(pageSize=10, pageNum=1, field=null, order=null)\" order: OrderVo{acnum=\'null\', nickname=\'null\', auditType=\'null\', applyUserName=\'null\', auditUserName=null, closeUserName=null, orderId=null, orderNumber=\'null\', amount=\'null\', orderStatus=\'null\', applyUserId=null, auditUserId=null, closeUserId=null, expireDays=null, endTime=null, createTime=null, btnList=[], submitInfo=null}', '119.37.199.25', '2020-08-20 10:12:57', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('68', '123456rp', '获取页面列表', '8', 'cc.mrbird.febs.device.controller.DeviceController.devicePageList()', ' request: \"QueryRequest(pageSize=10, pageNum=1, field=null, order=null)\" device: \"Device(deviceId=null, acnum=null, nickname=null, warnAmount=null, maxAmount=null, secretKey=null, validDays=null, deviceStatus=null, useLock=null, lockInfo=null, createTime=null)\"', '119.37.199.25', '2020-08-20 10:13:05', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('69', 'rp123456', '获取审核状态列表', '1', 'cc.mrbird.febs.audit.controller.AuditController.selectStatus()', '', '119.37.199.25', '2020-08-20 10:18:25', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('70', '123456rp', '获取注资状态列表', '1', 'cc.mrbird.febs.order.controller.OrderController.selectStatus()', '', '119.37.199.25', '2020-08-20 10:18:33', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('71', '123456rp', '获取注资分页列表', '14', 'cc.mrbird.febs.order.controller.OrderController.orderList()', ' request: \"QueryRequest(pageSize=10, pageNum=1, field=null, order=null)\" order: OrderVo{acnum=\'null\', nickname=\'null\', auditType=\'null\', applyUserName=\'null\', auditUserName=null, closeUserName=null, orderId=null, orderNumber=\'null\', amount=\'null\', orderStatus=\'null\', applyUserId=null, auditUserId=null, closeUserId=null, expireDays=null, endTime=null, createTime=null, btnList=[], submitInfo=null}', '119.37.199.25', '2020-08-20 10:18:33', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('72', 'admin', '获取审核状态列表', '7', 'cc.mrbird.febs.audit.controller.AuditController.selectStatus()', '', '119.37.199.25', '2020-08-20 10:36:43', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('73', 'admin', '获取页面列表', '13', 'cc.mrbird.febs.device.controller.DeviceController.devicePageList()', ' request: \"QueryRequest(pageSize=10, pageNum=1, field=null, order=null)\" device: \"Device(deviceId=null, acnum=null, nickname=null, warnAmount=null, maxAmount=null, secretKey=null, validDays=null, deviceStatus=null, useLock=null, lockInfo=null, createTime=null)\"', '119.37.199.25', '2020-08-20 10:36:44', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('74', 'admin', '获取注资状态列表', '1', 'cc.mrbird.febs.order.controller.OrderController.selectStatus()', '', '119.37.199.25', '2020-08-20 10:36:45', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('75', 'admin', '获取注资分页列表', '11', 'cc.mrbird.febs.order.controller.OrderController.orderList()', ' request: \"QueryRequest(pageSize=10, pageNum=1, field=null, order=null)\" order: OrderVo{acnum=\'null\', nickname=\'null\', auditType=\'null\', applyUserName=\'null\', auditUserName=null, closeUserName=null, orderId=null, orderNumber=\'null\', amount=\'null\', orderStatus=\'null\', applyUserId=null, auditUserId=null, closeUserId=null, expireDays=null, endTime=null, createTime=null, btnList=[], submitInfo=null}', '119.37.199.25', '2020-08-20 10:36:45', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('76', '123456rp', '获取注资状态列表', '1', 'cc.mrbird.febs.order.controller.OrderController.selectStatus()', '', '119.37.199.25', '2020-08-20 10:46:50', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('77', '123456rp', '获取注资分页列表', '14', 'cc.mrbird.febs.order.controller.OrderController.orderList()', ' request: \"QueryRequest(pageSize=10, pageNum=1, field=null, order=null)\" order: OrderVo{acnum=\'null\', nickname=\'null\', auditType=\'null\', applyUserName=\'null\', auditUserName=null, closeUserName=null, orderId=null, orderNumber=\'null\', amount=\'null\', orderStatus=\'null\', applyUserId=null, auditUserId=null, closeUserId=null, expireDays=null, endTime=null, createTime=null, btnList=[], submitInfo=null}', '119.37.199.25', '2020-08-20 10:46:50', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('78', 'admin', '获取注资分页列表', '12', 'cc.mrbird.febs.order.controller.OrderController.orderList()', ' request: \"QueryRequest(pageSize=10, pageNum=1, field=null, order=null)\" order: OrderVo{acnum=\'\', nickname=\'null\', auditType=\'null\', applyUserName=\'null\', auditUserName=null, closeUserName=null, orderId=null, orderNumber=\'\', amount=\'\', orderStatus=\'\', applyUserId=null, auditUserId=null, closeUserId=null, expireDays=null, endTime=null, createTime=null, btnList=[], submitInfo=null}', '119.37.199.25', '2020-08-20 11:05:00', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('79', 'admin', '获取注资分页列表', '12', 'cc.mrbird.febs.order.controller.OrderController.orderList()', ' request: \"QueryRequest(pageSize=10, pageNum=1, field=null, order=null)\" order: OrderVo{acnum=\'\', nickname=\'null\', auditType=\'null\', applyUserName=\'null\', auditUserName=null, closeUserName=null, orderId=null, orderNumber=\'\', amount=\'\', orderStatus=\'\', applyUserId=null, auditUserId=null, closeUserId=null, expireDays=null, endTime=null, createTime=null, btnList=[], submitInfo=null}', '119.37.199.25', '2020-08-20 11:26:14', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('80', 'admin', '用户列表', '27', 'cc.mrbird.febs.system.controller.UserController.userList()', ' user: \"User(userId=8, username=null, password=null, realname=null, parentId=null, deptId=null, email=null, mobile=null, status=null, createTime=null, modifyTime=null, lastLoginTime=null, sex=null, avatar=null, theme=null, isTab=null, description=null, deptName=null, createTimeFrom=null, createTimeTo=null, roleId=1, roleName=null, deptIds=null)\" request: \"QueryRequest(pageSize=10, pageNum=1, field=null, order=null)\"', '119.37.199.25', '2020-08-20 12:44:26', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('81', 'admin', '获取注资状态列表', '27', 'cc.mrbird.febs.order.controller.OrderController.selectStatus()', '', '119.37.199.25', '2020-08-20 12:44:33', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('82', 'admin', '获取注资分页列表', '44', 'cc.mrbird.febs.order.controller.OrderController.orderList()', ' request: \"QueryRequest(pageSize=10, pageNum=1, field=null, order=null)\" order: OrderVo{acnum=\'null\', nickname=\'null\', auditType=\'null\', applyUserName=\'null\', auditUserName=null, closeUserName=null, orderId=null, orderNumber=\'null\', amount=\'null\', orderStatus=\'null\', applyUserId=null, auditUserId=null, closeUserId=null, expireDays=null, endTime=null, createTime=null, btnList=[], submitInfo=null}', '119.37.199.25', '2020-08-20 12:44:33', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('83', 'admin', '获取审核状态列表', '2', 'cc.mrbird.febs.audit.controller.AuditController.selectStatus()', '', '119.37.199.25', '2020-08-20 12:44:35', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('84', 'admin', '获取页面列表', '20', 'cc.mrbird.febs.device.controller.DeviceController.devicePageList()', ' request: \"QueryRequest(pageSize=10, pageNum=1, field=null, order=null)\" device: \"Device(deviceId=null, acnum=null, nickname=null, warnAmount=null, maxAmount=null, secretKey=null, validDays=null, deviceStatus=null, useLock=null, lockInfo=null, createTime=null)\"', '119.37.199.25', '2020-08-20 12:44:36', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('85', 'admin', '获取注资状态列表', '25', 'cc.mrbird.febs.order.controller.OrderController.selectStatus()', '', '119.37.199.25', '2020-08-20 13:29:10', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('86', 'admin', '获取注资分页列表', '38', 'cc.mrbird.febs.order.controller.OrderController.orderList()', ' request: \"QueryRequest(pageSize=10, pageNum=1, field=null, order=null)\" order: OrderVo{acnum=\'null\', nickname=\'null\', auditType=\'null\', applyUserName=\'null\', auditUserName=null, closeUserName=null, orderId=null, orderNumber=\'null\', amount=\'null\', orderStatus=\'null\', applyUserId=null, auditUserId=null, closeUserId=null, expireDays=null, endTime=null, createTime=null, btnList=[], submitInfo=null}', '119.37.199.25', '2020-08-20 13:29:10', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('87', 'admin', '获取审核状态列表', '11', 'cc.mrbird.febs.audit.controller.AuditController.selectStatus()', '', '119.37.199.25', '2020-08-20 13:29:11', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('88', 'admin', '获取页面列表', '24', 'cc.mrbird.febs.device.controller.DeviceController.devicePageList()', ' request: \"QueryRequest(pageSize=10, pageNum=1, field=null, order=null)\" device: \"Device(deviceId=null, acnum=null, nickname=null, warnAmount=null, maxAmount=null, secretKey=null, validDays=null, deviceStatus=null, useLock=null, lockInfo=null, createTime=null)\"', '119.37.199.25', '2020-08-20 13:29:13', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('89', 'admin', '获取注资状态列表', '16', 'cc.mrbird.febs.order.controller.OrderController.selectStatus()', '', '119.37.199.25', '2020-08-20 14:01:00', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('90', 'admin', '获取注资分页列表', '24', 'cc.mrbird.febs.order.controller.OrderController.orderList()', ' request: \"QueryRequest(pageSize=10, pageNum=1, field=null, order=null)\" order: OrderVo{acnum=\'null\', nickname=\'null\', auditType=\'null\', applyUserName=\'null\', auditUserName=null, closeUserName=null, orderId=null, orderNumber=\'null\', amount=\'null\', orderStatus=\'null\', applyUserId=null, auditUserId=null, closeUserId=null, expireDays=null, endTime=null, createTime=null, btnList=[], submitInfo=null}', '119.37.199.25', '2020-08-20 14:01:00', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('91', 'admin', '获取审核状态列表', '11', 'cc.mrbird.febs.audit.controller.AuditController.selectStatus()', '', '119.37.199.25', '2020-08-20 14:01:51', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('92', 'admin', '获取页面列表', '16', 'cc.mrbird.febs.device.controller.DeviceController.devicePageList()', ' request: \"QueryRequest(pageSize=10, pageNum=1, field=null, order=null)\" device: \"Device(deviceId=null, acnum=null, nickname=null, warnAmount=null, maxAmount=null, secretKey=null, validDays=null, deviceStatus=null, useLock=null, lockInfo=null, createTime=null)\"', '119.37.199.25', '2020-08-20 14:01:51', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('93', 'admin', '用户列表', '22', 'cc.mrbird.febs.system.controller.UserController.userList()', ' user: \"User(userId=8, username=null, password=null, realname=null, parentId=null, deptId=null, email=null, mobile=null, status=null, createTime=null, modifyTime=null, lastLoginTime=null, sex=null, avatar=null, theme=null, isTab=null, description=null, deptName=null, createTimeFrom=null, createTimeTo=null, roleId=1, roleName=null, deptIds=null)\" request: \"QueryRequest(pageSize=10, pageNum=1, field=null, order=null)\"', '119.37.199.25', '2020-08-20 14:01:52', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('94', 'rp123456', '获取审核状态列表', '1', 'cc.mrbird.febs.audit.controller.AuditController.selectStatus()', '', '119.37.199.25', '2020-08-20 14:02:53', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('95', '123456rp', '获取注资状态列表', '1', 'cc.mrbird.febs.order.controller.OrderController.selectStatus()', '', '119.37.199.25', '2020-08-20 14:03:02', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('96', '123456rp', '获取注资分页列表', '16', 'cc.mrbird.febs.order.controller.OrderController.orderList()', ' request: \"QueryRequest(pageSize=10, pageNum=1, field=null, order=null)\" order: OrderVo{acnum=\'null\', nickname=\'null\', auditType=\'null\', applyUserName=\'null\', auditUserName=null, closeUserName=null, orderId=null, orderNumber=\'null\', amount=\'null\', orderStatus=\'null\', applyUserId=null, auditUserId=null, closeUserId=null, expireDays=null, endTime=null, createTime=null, btnList=[], submitInfo=null}', '119.37.199.25', '2020-08-20 14:03:02', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('97', '123456rp', '获取表头号列表', '11', 'cc.mrbird.febs.order.controller.OrderController.getAcnumList()', '', '119.37.199.25', '2020-08-20 14:03:04', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('98', '123456rp', '获取审核员列表', '10', 'cc.mrbird.febs.order.controller.OrderController.getAuditUserNameList()', ' deviceId: \"29\"', '119.37.199.25', '2020-08-20 14:03:06', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('99', '123456rp', '新增', '29', 'cc.mrbird.febs.order.controller.OrderController.addOrder()', ' order: OrderVo{acnum=\'CPU222\', nickname=\'null\', auditType=\'null\', applyUserName=\'null\', auditUserName=null, closeUserName=null, orderId=64, orderNumber=\'51418515399380992\', amount=\'666.00\', orderStatus=\'1\', applyUserId=33, auditUserId=32, closeUserId=null, expireDays=7, endTime=Thu Aug 27 14:03:18 CST 2020, createTime=Thu Aug 20 14:03:18 CST 2020, btnList=[], submitInfo=null}', '119.37.199.25', '2020-08-20 14:03:18', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('100', '123456rp', '获取注资分页列表', '10', 'cc.mrbird.febs.order.controller.OrderController.orderList()', ' request: \"QueryRequest(pageSize=10, pageNum=1, field=createTime, order=)\" order: OrderVo{acnum=\'\', nickname=\'null\', auditType=\'null\', applyUserName=\'null\', auditUserName=null, closeUserName=null, orderId=null, orderNumber=\'\', amount=\'\', orderStatus=\'\', applyUserId=null, auditUserId=null, closeUserId=null, expireDays=null, endTime=null, createTime=null, btnList=[], submitInfo=null}', '119.37.199.25', '2020-08-20 14:03:18', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('101', '123456rp', '提交注资审核', '0', 'cc.mrbird.febs.order.controller.ViewController.submitApply()', ' orderId: \"64\" audityType: \"1\" orderId: \"64\" audityType: \"1\"', '119.37.199.25', '2020-08-20 14:03:38', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('102', '123456rp', '提交审核', '38', 'cc.mrbird.febs.order.controller.OrderController.submitApply()', ' orderVo: OrderVo{acnum=\'CPU222\', nickname=\'null\', auditType=\'1\', applyUserName=\'null\', auditUserName=null, closeUserName=null, orderId=64, orderNumber=\'51418515399380992\', amount=\'666.00\', orderStatus=\'2\', applyUserId=33, auditUserId=32, closeUserId=null, expireDays=7, endTime=Thu Aug 27 14:03:18 CST 2020, createTime=Thu Aug 20 14:03:18 CST 2020, btnList=[], submitInfo=} auditType: \"1\"', '119.37.199.25', '2020-08-20 14:03:39', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('103', '123456rp', '获取注资分页列表', '11', 'cc.mrbird.febs.order.controller.OrderController.orderList()', ' request: \"QueryRequest(pageSize=10, pageNum=1, field=createTime, order=)\" order: OrderVo{acnum=\'\', nickname=\'null\', auditType=\'null\', applyUserName=\'null\', auditUserName=null, closeUserName=null, orderId=null, orderNumber=\'\', amount=\'\', orderStatus=\'\', applyUserId=null, auditUserId=null, closeUserId=null, expireDays=null, endTime=null, createTime=null, btnList=[], submitInfo=null}', '119.37.199.25', '2020-08-20 14:03:39', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('104', 'rp123456', '审核通过', '39', 'cc.mrbird.febs.audit.controller.AuditController.passAudit()', ' auditId: \"45\"', '119.37.199.25', '2020-08-20 14:03:46', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('105', '123456rp', '获取注资分页列表', '13', 'cc.mrbird.febs.order.controller.OrderController.orderList()', ' request: \"QueryRequest(pageSize=10, pageNum=1, field=null, order=null)\" order: OrderVo{acnum=\'\', nickname=\'null\', auditType=\'null\', applyUserName=\'null\', auditUserName=null, closeUserName=null, orderId=null, orderNumber=\'\', amount=\'\', orderStatus=\'\', applyUserId=null, auditUserId=null, closeUserId=null, expireDays=null, endTime=null, createTime=null, btnList=[], submitInfo=null}', '119.37.199.25', '2020-08-20 14:03:48', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('106', '123456rp', '获取页面列表', '13', 'cc.mrbird.febs.device.controller.DeviceController.devicePageList()', ' request: \"QueryRequest(pageSize=10, pageNum=1, field=null, order=null)\" device: \"Device(deviceId=null, acnum=null, nickname=null, warnAmount=null, maxAmount=null, secretKey=null, validDays=null, deviceStatus=null, useLock=null, lockInfo=null, createTime=null)\"', '119.37.199.25', '2020-08-20 14:19:41', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('107', '123456rp', '获取注资状态列表', '1', 'cc.mrbird.febs.order.controller.OrderController.selectStatus()', '', '119.37.199.25', '2020-08-20 14:19:46', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('108', '123456rp', '获取注资分页列表', '16', 'cc.mrbird.febs.order.controller.OrderController.orderList()', ' request: \"QueryRequest(pageSize=10, pageNum=1, field=null, order=null)\" order: OrderVo{acnum=\'null\', nickname=\'null\', auditType=\'null\', applyUserName=\'null\', auditUserName=null, closeUserName=null, orderId=null, orderNumber=\'null\', amount=\'null\', orderStatus=\'null\', applyUserId=null, auditUserId=null, closeUserId=null, expireDays=null, endTime=null, createTime=null, btnList=[], submitInfo=null}', '119.37.199.25', '2020-08-20 14:19:46', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('109', '123456rp', '获取注资分页列表', '10', 'cc.mrbird.febs.order.controller.OrderController.orderList()', ' request: \"QueryRequest(pageSize=10, pageNum=1, field=null, order=null)\" order: OrderVo{acnum=\'\', nickname=\'null\', auditType=\'null\', applyUserName=\'null\', auditUserName=null, closeUserName=null, orderId=null, orderNumber=\'\', amount=\'\', orderStatus=\'\', applyUserId=null, auditUserId=null, closeUserId=null, expireDays=null, endTime=null, createTime=null, btnList=[], submitInfo=null}', '119.37.199.25', '2020-08-20 14:20:10', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('110', '123456rp', '获取注资分页列表', '12', 'cc.mrbird.febs.order.controller.OrderController.orderList()', ' request: \"QueryRequest(pageSize=10, pageNum=1, field=null, order=null)\" order: OrderVo{acnum=\'\', nickname=\'null\', auditType=\'null\', applyUserName=\'null\', auditUserName=null, closeUserName=null, orderId=null, orderNumber=\'\', amount=\'\', orderStatus=\'\', applyUserId=null, auditUserId=null, closeUserId=null, expireDays=null, endTime=null, createTime=null, btnList=[], submitInfo=null}', '119.37.199.25', '2020-08-20 14:20:11', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('111', '123456rp', '获取注资分页列表', '13', 'cc.mrbird.febs.order.controller.OrderController.orderList()', ' request: \"QueryRequest(pageSize=10, pageNum=1, field=null, order=null)\" order: OrderVo{acnum=\'\', nickname=\'null\', auditType=\'null\', applyUserName=\'null\', auditUserName=null, closeUserName=null, orderId=null, orderNumber=\'\', amount=\'\', orderStatus=\'\', applyUserId=null, auditUserId=null, closeUserId=null, expireDays=null, endTime=null, createTime=null, btnList=[], submitInfo=null}', '119.37.199.25', '2020-08-20 14:20:12', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('112', '123456rp', '获取注资分页列表', '12', 'cc.mrbird.febs.order.controller.OrderController.orderList()', ' request: \"QueryRequest(pageSize=10, pageNum=1, field=null, order=null)\" order: OrderVo{acnum=\'\', nickname=\'null\', auditType=\'null\', applyUserName=\'null\', auditUserName=null, closeUserName=null, orderId=null, orderNumber=\'\', amount=\'\', orderStatus=\'\', applyUserId=null, auditUserId=null, closeUserId=null, expireDays=null, endTime=null, createTime=null, btnList=[], submitInfo=null}', '119.37.199.25', '2020-08-20 14:20:12', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('113', '123456rp', '获取注资分页列表', '13', 'cc.mrbird.febs.order.controller.OrderController.orderList()', ' request: \"QueryRequest(pageSize=10, pageNum=1, field=null, order=null)\" order: OrderVo{acnum=\'\', nickname=\'null\', auditType=\'null\', applyUserName=\'null\', auditUserName=null, closeUserName=null, orderId=null, orderNumber=\'\', amount=\'\', orderStatus=\'\', applyUserId=null, auditUserId=null, closeUserId=null, expireDays=null, endTime=null, createTime=null, btnList=[], submitInfo=null}', '119.37.199.25', '2020-08-20 14:20:13', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('114', '123456rp', '获取注资分页列表', '16', 'cc.mrbird.febs.order.controller.OrderController.orderList()', ' request: \"QueryRequest(pageSize=10, pageNum=1, field=null, order=null)\" order: OrderVo{acnum=\'\', nickname=\'null\', auditType=\'null\', applyUserName=\'null\', auditUserName=null, closeUserName=null, orderId=null, orderNumber=\'\', amount=\'\', orderStatus=\'\', applyUserId=null, auditUserId=null, closeUserId=null, expireDays=null, endTime=null, createTime=null, btnList=[], submitInfo=null}', '119.37.199.25', '2020-08-20 14:20:14', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('115', '123456rp', '获取注资分页列表', '13', 'cc.mrbird.febs.order.controller.OrderController.orderList()', ' request: \"QueryRequest(pageSize=10, pageNum=1, field=null, order=null)\" order: OrderVo{acnum=\'\', nickname=\'null\', auditType=\'null\', applyUserName=\'null\', auditUserName=null, closeUserName=null, orderId=null, orderNumber=\'\', amount=\'\', orderStatus=\'\', applyUserId=null, auditUserId=null, closeUserId=null, expireDays=null, endTime=null, createTime=null, btnList=[], submitInfo=null}', '119.37.199.25', '2020-08-20 14:20:14', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('116', '123456rp', '获取注资分页列表', '13', 'cc.mrbird.febs.order.controller.OrderController.orderList()', ' request: \"QueryRequest(pageSize=10, pageNum=1, field=null, order=null)\" order: OrderVo{acnum=\'\', nickname=\'null\', auditType=\'null\', applyUserName=\'null\', auditUserName=null, closeUserName=null, orderId=null, orderNumber=\'\', amount=\'\', orderStatus=\'\', applyUserId=null, auditUserId=null, closeUserId=null, expireDays=null, endTime=null, createTime=null, btnList=[], submitInfo=null}', '119.37.199.25', '2020-08-20 14:20:15', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('117', '123456rp', '获取注资分页列表', '10', 'cc.mrbird.febs.order.controller.OrderController.orderList()', ' request: \"QueryRequest(pageSize=10, pageNum=1, field=null, order=null)\" order: OrderVo{acnum=\'\', nickname=\'null\', auditType=\'null\', applyUserName=\'null\', auditUserName=null, closeUserName=null, orderId=null, orderNumber=\'\', amount=\'\', orderStatus=\'\', applyUserId=null, auditUserId=null, closeUserId=null, expireDays=null, endTime=null, createTime=null, btnList=[], submitInfo=null}', '119.37.199.25', '2020-08-20 14:20:30', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('118', '123456rp', '获取注资分页列表', '11', 'cc.mrbird.febs.order.controller.OrderController.orderList()', ' request: \"QueryRequest(pageSize=10, pageNum=1, field=null, order=null)\" order: OrderVo{acnum=\'\', nickname=\'null\', auditType=\'null\', applyUserName=\'null\', auditUserName=null, closeUserName=null, orderId=null, orderNumber=\'\', amount=\'\', orderStatus=\'\', applyUserId=null, auditUserId=null, closeUserId=null, expireDays=null, endTime=null, createTime=null, btnList=[], submitInfo=null}', '119.37.199.25', '2020-08-20 14:20:31', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('119', '123456rp', '获取注资分页列表', '12', 'cc.mrbird.febs.order.controller.OrderController.orderList()', ' request: \"QueryRequest(pageSize=10, pageNum=1, field=null, order=null)\" order: OrderVo{acnum=\'\', nickname=\'null\', auditType=\'null\', applyUserName=\'null\', auditUserName=null, closeUserName=null, orderId=null, orderNumber=\'\', amount=\'\', orderStatus=\'\', applyUserId=null, auditUserId=null, closeUserId=null, expireDays=null, endTime=null, createTime=null, btnList=[], submitInfo=null}', '119.37.199.25', '2020-08-20 14:20:31', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('120', '123456rp', '获取注资分页列表', '14', 'cc.mrbird.febs.order.controller.OrderController.orderList()', ' request: \"QueryRequest(pageSize=10, pageNum=1, field=null, order=null)\" order: OrderVo{acnum=\'\', nickname=\'null\', auditType=\'null\', applyUserName=\'null\', auditUserName=null, closeUserName=null, orderId=null, orderNumber=\'\', amount=\'\', orderStatus=\'\', applyUserId=null, auditUserId=null, closeUserId=null, expireDays=null, endTime=null, createTime=null, btnList=[], submitInfo=null}', '119.37.199.25', '2020-08-20 14:20:32', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('121', '123456rp', '获取注资分页列表', '13', 'cc.mrbird.febs.order.controller.OrderController.orderList()', ' request: \"QueryRequest(pageSize=10, pageNum=1, field=null, order=null)\" order: OrderVo{acnum=\'\', nickname=\'null\', auditType=\'null\', applyUserName=\'null\', auditUserName=null, closeUserName=null, orderId=null, orderNumber=\'\', amount=\'\', orderStatus=\'\', applyUserId=null, auditUserId=null, closeUserId=null, expireDays=null, endTime=null, createTime=null, btnList=[], submitInfo=null}', '119.37.199.25', '2020-08-20 14:20:33', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('122', '123456rp', '获取注资分页列表', '12', 'cc.mrbird.febs.order.controller.OrderController.orderList()', ' request: \"QueryRequest(pageSize=10, pageNum=1, field=null, order=null)\" order: OrderVo{acnum=\'\', nickname=\'null\', auditType=\'null\', applyUserName=\'null\', auditUserName=null, closeUserName=null, orderId=null, orderNumber=\'\', amount=\'\', orderStatus=\'\', applyUserId=null, auditUserId=null, closeUserId=null, expireDays=null, endTime=null, createTime=null, btnList=[], submitInfo=null}', '119.37.199.25', '2020-08-20 14:20:33', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('123', '123456rp', '获取注资分页列表', '14', 'cc.mrbird.febs.order.controller.OrderController.orderList()', ' request: \"QueryRequest(pageSize=10, pageNum=1, field=null, order=null)\" order: OrderVo{acnum=\'\', nickname=\'null\', auditType=\'null\', applyUserName=\'null\', auditUserName=null, closeUserName=null, orderId=null, orderNumber=\'\', amount=\'\', orderStatus=\'\', applyUserId=null, auditUserId=null, closeUserId=null, expireDays=null, endTime=null, createTime=null, btnList=[], submitInfo=null}', '119.37.199.25', '2020-08-20 14:20:33', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('124', '123456rp', '获取注资分页列表', '12', 'cc.mrbird.febs.order.controller.OrderController.orderList()', ' request: \"QueryRequest(pageSize=10, pageNum=1, field=null, order=null)\" order: OrderVo{acnum=\'\', nickname=\'null\', auditType=\'null\', applyUserName=\'null\', auditUserName=null, closeUserName=null, orderId=null, orderNumber=\'\', amount=\'\', orderStatus=\'\', applyUserId=null, auditUserId=null, closeUserId=null, expireDays=null, endTime=null, createTime=null, btnList=[], submitInfo=null}', '119.37.199.25', '2020-08-20 14:20:34', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('125', '123456rp', '获取注资分页列表', '13', 'cc.mrbird.febs.order.controller.OrderController.orderList()', ' request: \"QueryRequest(pageSize=10, pageNum=1, field=null, order=null)\" order: OrderVo{acnum=\'\', nickname=\'null\', auditType=\'null\', applyUserName=\'null\', auditUserName=null, closeUserName=null, orderId=null, orderNumber=\'\', amount=\'\', orderStatus=\'\', applyUserId=null, auditUserId=null, closeUserId=null, expireDays=null, endTime=null, createTime=null, btnList=[], submitInfo=null}', '119.37.199.25', '2020-08-20 14:20:34', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('126', '123456rp', '获取注资分页列表', '13', 'cc.mrbird.febs.order.controller.OrderController.orderList()', ' request: \"QueryRequest(pageSize=10, pageNum=1, field=null, order=null)\" order: OrderVo{acnum=\'\', nickname=\'null\', auditType=\'null\', applyUserName=\'null\', auditUserName=null, closeUserName=null, orderId=null, orderNumber=\'\', amount=\'\', orderStatus=\'\', applyUserId=null, auditUserId=null, closeUserId=null, expireDays=null, endTime=null, createTime=null, btnList=[], submitInfo=null}', '119.37.199.25', '2020-08-20 14:20:34', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('127', '123456rp', '获取注资分页列表', '20', 'cc.mrbird.febs.order.controller.OrderController.orderList()', ' request: \"QueryRequest(pageSize=10, pageNum=1, field=null, order=null)\" order: OrderVo{acnum=\'\', nickname=\'null\', auditType=\'null\', applyUserName=\'null\', auditUserName=null, closeUserName=null, orderId=null, orderNumber=\'\', amount=\'\', orderStatus=\'\', applyUserId=null, auditUserId=null, closeUserId=null, expireDays=null, endTime=null, createTime=null, btnList=[], submitInfo=null}', '119.37.199.25', '2020-08-20 14:20:35', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('128', '123456rp', '获取注资分页列表', '14', 'cc.mrbird.febs.order.controller.OrderController.orderList()', ' request: \"QueryRequest(pageSize=10, pageNum=1, field=null, order=null)\" order: OrderVo{acnum=\'\', nickname=\'null\', auditType=\'null\', applyUserName=\'null\', auditUserName=null, closeUserName=null, orderId=null, orderNumber=\'\', amount=\'\', orderStatus=\'\', applyUserId=null, auditUserId=null, closeUserId=null, expireDays=null, endTime=null, createTime=null, btnList=[], submitInfo=null}', '119.37.199.25', '2020-08-20 14:20:35', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('129', '123456rp', '获取注资分页列表', '12', 'cc.mrbird.febs.order.controller.OrderController.orderList()', ' request: \"QueryRequest(pageSize=10, pageNum=1, field=null, order=null)\" order: OrderVo{acnum=\'\', nickname=\'null\', auditType=\'null\', applyUserName=\'null\', auditUserName=null, closeUserName=null, orderId=null, orderNumber=\'\', amount=\'\', orderStatus=\'\', applyUserId=null, auditUserId=null, closeUserId=null, expireDays=null, endTime=null, createTime=null, btnList=[], submitInfo=null}', '119.37.199.25', '2020-08-20 14:20:35', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('130', '123456rp', '获取注资分页列表', '11', 'cc.mrbird.febs.order.controller.OrderController.orderList()', ' request: \"QueryRequest(pageSize=10, pageNum=1, field=null, order=null)\" order: OrderVo{acnum=\'\', nickname=\'null\', auditType=\'null\', applyUserName=\'null\', auditUserName=null, closeUserName=null, orderId=null, orderNumber=\'\', amount=\'\', orderStatus=\'\', applyUserId=null, auditUserId=null, closeUserId=null, expireDays=null, endTime=null, createTime=null, btnList=[], submitInfo=null}', '119.37.199.25', '2020-08-20 14:21:00', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('131', '123456rp', '获取注资分页列表', '12', 'cc.mrbird.febs.order.controller.OrderController.orderList()', ' request: \"QueryRequest(pageSize=10, pageNum=1, field=null, order=null)\" order: OrderVo{acnum=\'\', nickname=\'null\', auditType=\'null\', applyUserName=\'null\', auditUserName=null, closeUserName=null, orderId=null, orderNumber=\'\', amount=\'\', orderStatus=\'\', applyUserId=null, auditUserId=null, closeUserId=null, expireDays=null, endTime=null, createTime=null, btnList=[], submitInfo=null}', '119.37.199.25', '2020-08-20 14:21:03', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('132', '123456rp', '获取注资分页列表', '12', 'cc.mrbird.febs.order.controller.OrderController.orderList()', ' request: \"QueryRequest(pageSize=10, pageNum=1, field=null, order=null)\" order: OrderVo{acnum=\'\', nickname=\'null\', auditType=\'null\', applyUserName=\'null\', auditUserName=null, closeUserName=null, orderId=null, orderNumber=\'\', amount=\'\', orderStatus=\'\', applyUserId=null, auditUserId=null, closeUserId=null, expireDays=null, endTime=null, createTime=null, btnList=[], submitInfo=null}', '119.37.199.25', '2020-08-20 14:25:39', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('133', '123456rp', '获取表头号列表', '10', 'cc.mrbird.febs.order.controller.OrderController.getAcnumList()', '', '119.37.199.25', '2020-08-20 14:25:44', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('134', '123456rp', '获取审核员列表', '12', 'cc.mrbird.febs.order.controller.OrderController.getAuditUserNameList()', ' deviceId: \"29\"', '119.37.199.25', '2020-08-20 14:25:46', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('135', '123456rp', '新增', '49', 'cc.mrbird.febs.order.controller.OrderController.addOrder()', ' order: OrderVo{acnum=\'CPU222\', nickname=\'null\', auditType=\'null\', applyUserName=\'null\', auditUserName=null, closeUserName=null, orderId=65, orderNumber=\'51424189638381568\', amount=\'1111.00\', orderStatus=\'1\', applyUserId=33, auditUserId=32, closeUserId=null, expireDays=7, endTime=Thu Aug 27 14:25:51 CST 2020, createTime=Thu Aug 20 14:25:51 CST 2020, btnList=[], submitInfo=null}', '119.37.199.25', '2020-08-20 14:25:51', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('136', '123456rp', '获取注资分页列表', '12', 'cc.mrbird.febs.order.controller.OrderController.orderList()', ' request: \"QueryRequest(pageSize=10, pageNum=1, field=createTime, order=null)\" order: OrderVo{acnum=\'\', nickname=\'null\', auditType=\'null\', applyUserName=\'null\', auditUserName=null, closeUserName=null, orderId=null, orderNumber=\'\', amount=\'\', orderStatus=\'\', applyUserId=null, auditUserId=null, closeUserId=null, expireDays=null, endTime=null, createTime=null, btnList=[], submitInfo=null}', '119.37.199.25', '2020-08-20 14:25:51', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('137', '123456rp', '提交注资审核', '0', 'cc.mrbird.febs.order.controller.ViewController.submitApply()', ' orderId: \"65\" audityType: \"1\" orderId: \"65\" audityType: \"1\"', '119.37.199.25', '2020-08-20 14:25:55', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('138', '123456rp', '提交审核', '43', 'cc.mrbird.febs.order.controller.OrderController.submitApply()', ' orderVo: OrderVo{acnum=\'CPU222\', nickname=\'null\', auditType=\'1\', applyUserName=\'null\', auditUserName=null, closeUserName=null, orderId=65, orderNumber=\'51424189638381568\', amount=\'1111.00\', orderStatus=\'2\', applyUserId=33, auditUserId=32, closeUserId=null, expireDays=7, endTime=Thu Aug 27 14:25:51 CST 2020, createTime=Thu Aug 20 14:25:51 CST 2020, btnList=[], submitInfo=} auditType: \"1\"', '119.37.199.25', '2020-08-20 14:25:56', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('139', '123456rp', '获取注资分页列表', '11', 'cc.mrbird.febs.order.controller.OrderController.orderList()', ' request: \"QueryRequest(pageSize=10, pageNum=1, field=createTime, order=null)\" order: OrderVo{acnum=\'\', nickname=\'null\', auditType=\'null\', applyUserName=\'null\', auditUserName=null, closeUserName=null, orderId=null, orderNumber=\'\', amount=\'\', orderStatus=\'\', applyUserId=null, auditUserId=null, closeUserId=null, expireDays=null, endTime=null, createTime=null, btnList=[], submitInfo=null}', '119.37.199.25', '2020-08-20 14:25:56', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('140', 'rp123456', '获取审核状态列表', '9', 'cc.mrbird.febs.audit.controller.AuditController.selectStatus()', '', '119.37.199.25', '2020-08-20 14:26:03', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('141', 'rp123456', '审核通过', '33', 'cc.mrbird.febs.audit.controller.AuditController.passAudit()', ' auditId: \"46\"', '119.37.199.25', '2020-08-20 14:26:06', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('142', '123456rp', '获取注资分页列表', '11', 'cc.mrbird.febs.order.controller.OrderController.orderList()', ' request: \"QueryRequest(pageSize=10, pageNum=1, field=null, order=null)\" order: OrderVo{acnum=\'\', nickname=\'null\', auditType=\'null\', applyUserName=\'null\', auditUserName=null, closeUserName=null, orderId=null, orderNumber=\'\', amount=\'\', orderStatus=\'\', applyUserId=null, auditUserId=null, closeUserId=null, expireDays=null, endTime=null, createTime=null, btnList=[], submitInfo=null}', '119.37.199.25', '2020-08-20 14:26:26', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('143', '123456rp', '获取注资分页列表', '13', 'cc.mrbird.febs.order.controller.OrderController.orderList()', ' request: \"QueryRequest(pageSize=10, pageNum=1, field=null, order=null)\" order: OrderVo{acnum=\'\', nickname=\'null\', auditType=\'null\', applyUserName=\'null\', auditUserName=null, closeUserName=null, orderId=null, orderNumber=\'\', amount=\'\', orderStatus=\'\', applyUserId=null, auditUserId=null, closeUserId=null, expireDays=null, endTime=null, createTime=null, btnList=[], submitInfo=null}', '119.37.199.25', '2020-08-20 14:26:42', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('144', '123456rp', '获取注资分页列表', '9', 'cc.mrbird.febs.order.controller.OrderController.orderList()', ' request: \"QueryRequest(pageSize=10, pageNum=1, field=null, order=null)\" order: OrderVo{acnum=\'\', nickname=\'null\', auditType=\'null\', applyUserName=\'null\', auditUserName=null, closeUserName=null, orderId=null, orderNumber=\'\', amount=\'\', orderStatus=\'\', applyUserId=null, auditUserId=null, closeUserId=null, expireDays=null, endTime=null, createTime=null, btnList=[], submitInfo=null}', '119.37.199.25', '2020-08-20 14:43:02', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('145', '123456rp', '获取表头号列表', '16', 'cc.mrbird.febs.order.controller.OrderController.getAcnumList()', '', '119.37.199.25', '2020-08-20 15:17:18', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('146', '123456rp', '获取审核员列表', '29', 'cc.mrbird.febs.order.controller.OrderController.getAuditUserNameList()', ' deviceId: \"29\"', '119.37.199.25', '2020-08-20 15:17:20', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('147', '123456rp', '新增', '23', 'cc.mrbird.febs.order.controller.OrderController.addOrder()', ' order: OrderVo{acnum=\'CPU222\', nickname=\'null\', auditType=\'null\', applyUserName=\'null\', auditUserName=null, closeUserName=null, orderId=66, orderNumber=\'51437164461953024\', amount=\'1111.00\', orderStatus=\'1\', applyUserId=33, auditUserId=32, closeUserId=null, expireDays=7, endTime=Thu Aug 27 15:17:24 CST 2020, createTime=Thu Aug 20 15:17:24 CST 2020, btnList=[], submitInfo=null}', '119.37.199.25', '2020-08-20 15:17:25', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('148', '123456rp', '获取注资分页列表', '11', 'cc.mrbird.febs.order.controller.OrderController.orderList()', ' request: \"QueryRequest(pageSize=10, pageNum=1, field=createTime, order=null)\" order: OrderVo{acnum=\'\', nickname=\'null\', auditType=\'null\', applyUserName=\'null\', auditUserName=null, closeUserName=null, orderId=null, orderNumber=\'\', amount=\'\', orderStatus=\'\', applyUserId=null, auditUserId=null, closeUserId=null, expireDays=null, endTime=null, createTime=null, btnList=[], submitInfo=null}', '119.37.199.25', '2020-08-20 15:17:25', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('149', '123456rp', '提交注资审核', '0', 'cc.mrbird.febs.order.controller.ViewController.submitApply()', ' orderId: \"66\" audityType: \"1\" orderId: \"66\" audityType: \"1\"', '119.37.199.25', '2020-08-20 15:17:27', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('150', '123456rp', '提交审核', '24', 'cc.mrbird.febs.order.controller.OrderController.submitApply()', ' orderVo: OrderVo{acnum=\'CPU222\', nickname=\'null\', auditType=\'1\', applyUserName=\'null\', auditUserName=null, closeUserName=null, orderId=66, orderNumber=\'51437164461953024\', amount=\'1111.00\', orderStatus=\'2\', applyUserId=33, auditUserId=32, closeUserId=null, expireDays=7, endTime=Thu Aug 27 15:17:25 CST 2020, createTime=Thu Aug 20 15:17:25 CST 2020, btnList=[], submitInfo=} auditType: \"1\"', '119.37.199.25', '2020-08-20 15:17:28', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('151', '123456rp', '获取注资分页列表', '12', 'cc.mrbird.febs.order.controller.OrderController.orderList()', ' request: \"QueryRequest(pageSize=10, pageNum=1, field=createTime, order=null)\" order: OrderVo{acnum=\'\', nickname=\'null\', auditType=\'null\', applyUserName=\'null\', auditUserName=null, closeUserName=null, orderId=null, orderNumber=\'\', amount=\'\', orderStatus=\'\', applyUserId=null, auditUserId=null, closeUserId=null, expireDays=null, endTime=null, createTime=null, btnList=[], submitInfo=null}', '119.37.199.25', '2020-08-20 15:17:28', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('152', '123456rp', '获取注资分页列表', '11', 'cc.mrbird.febs.order.controller.OrderController.orderList()', ' request: \"QueryRequest(pageSize=10, pageNum=1, field=null, order=null)\" order: OrderVo{acnum=\'\', nickname=\'null\', auditType=\'null\', applyUserName=\'null\', auditUserName=null, closeUserName=null, orderId=null, orderNumber=\'\', amount=\'\', orderStatus=\'\', applyUserId=null, auditUserId=null, closeUserId=null, expireDays=null, endTime=null, createTime=null, btnList=[], submitInfo=null}', '119.37.199.25', '2020-08-20 15:17:48', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('153', 'rp123456', '审核通过', '41', 'cc.mrbird.febs.audit.controller.AuditController.passAudit()', ' auditId: \"47\"', '119.37.199.25', '2020-08-20 15:17:56', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('154', '123456rp', '获取注资分页列表', '17', 'cc.mrbird.febs.order.controller.OrderController.orderList()', ' request: \"QueryRequest(pageSize=10, pageNum=1, field=null, order=null)\" order: OrderVo{acnum=\'\', nickname=\'null\', auditType=\'null\', applyUserName=\'null\', auditUserName=null, closeUserName=null, orderId=null, orderNumber=\'\', amount=\'\', orderStatus=\'\', applyUserId=null, auditUserId=null, closeUserId=null, expireDays=null, endTime=null, createTime=null, btnList=[], submitInfo=null}', '119.37.199.25', '2020-08-20 15:19:06', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('155', '123456rp', '获取表头号列表', '10', 'cc.mrbird.febs.order.controller.OrderController.getAcnumList()', '', '119.37.199.25', '2020-08-20 15:19:08', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('156', '123456rp', '获取审核员列表', '5', 'cc.mrbird.febs.order.controller.OrderController.getAuditUserNameList()', ' deviceId: \"29\"', '119.37.199.25', '2020-08-20 15:19:21', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('157', '123456rp', '新增', '48', 'cc.mrbird.febs.order.controller.OrderController.addOrder()', ' order: OrderVo{acnum=\'CPU222\', nickname=\'null\', auditType=\'null\', applyUserName=\'null\', auditUserName=null, closeUserName=null, orderId=67, orderNumber=\'51437677333057536\', amount=\'1111.00\', orderStatus=\'1\', applyUserId=33, auditUserId=32, closeUserId=null, expireDays=7, endTime=Thu Aug 27 15:19:26 CST 2020, createTime=Thu Aug 20 15:19:26 CST 2020, btnList=[], submitInfo=null}', '119.37.199.25', '2020-08-20 15:19:27', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('158', '123456rp', '获取注资分页列表', '12', 'cc.mrbird.febs.order.controller.OrderController.orderList()', ' request: \"QueryRequest(pageSize=10, pageNum=1, field=createTime, order=null)\" order: OrderVo{acnum=\'\', nickname=\'null\', auditType=\'null\', applyUserName=\'null\', auditUserName=null, closeUserName=null, orderId=null, orderNumber=\'\', amount=\'\', orderStatus=\'\', applyUserId=null, auditUserId=null, closeUserId=null, expireDays=null, endTime=null, createTime=null, btnList=[], submitInfo=null}', '119.37.199.25', '2020-08-20 15:19:27', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('159', '123456rp', '提交注资审核', '1', 'cc.mrbird.febs.order.controller.ViewController.submitApply()', ' orderId: \"67\" audityType: \"1\" orderId: \"67\" audityType: \"1\"', '119.37.199.25', '2020-08-20 15:19:28', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('160', '123456rp', '提交审核', '32', 'cc.mrbird.febs.order.controller.OrderController.submitApply()', ' orderVo: OrderVo{acnum=\'CPU222\', nickname=\'null\', auditType=\'1\', applyUserName=\'null\', auditUserName=null, closeUserName=null, orderId=67, orderNumber=\'51437677333057536\', amount=\'1111.00\', orderStatus=\'2\', applyUserId=33, auditUserId=32, closeUserId=null, expireDays=7, endTime=Thu Aug 27 15:19:27 CST 2020, createTime=Thu Aug 20 15:19:27 CST 2020, btnList=[], submitInfo=} auditType: \"1\"', '119.37.199.25', '2020-08-20 15:19:29', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('161', '123456rp', '获取注资分页列表', '11', 'cc.mrbird.febs.order.controller.OrderController.orderList()', ' request: \"QueryRequest(pageSize=10, pageNum=1, field=createTime, order=null)\" order: OrderVo{acnum=\'\', nickname=\'null\', auditType=\'null\', applyUserName=\'null\', auditUserName=null, closeUserName=null, orderId=null, orderNumber=\'\', amount=\'\', orderStatus=\'\', applyUserId=null, auditUserId=null, closeUserId=null, expireDays=null, endTime=null, createTime=null, btnList=[], submitInfo=null}', '119.37.199.25', '2020-08-20 15:19:29', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('162', 'rp123456', '审核通过', '26', 'cc.mrbird.febs.audit.controller.AuditController.passAudit()', ' auditId: \"48\"', '119.37.199.25', '2020-08-20 15:19:44', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('163', '123456rp', '获取注资分页列表', '13', 'cc.mrbird.febs.order.controller.OrderController.orderList()', ' request: \"QueryRequest(pageSize=10, pageNum=1, field=null, order=null)\" order: OrderVo{acnum=\'\', nickname=\'null\', auditType=\'null\', applyUserName=\'null\', auditUserName=null, closeUserName=null, orderId=null, orderNumber=\'\', amount=\'\', orderStatus=\'\', applyUserId=null, auditUserId=null, closeUserId=null, expireDays=null, endTime=null, createTime=null, btnList=[], submitInfo=null}', '119.37.199.25', '2020-08-20 15:19:49', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('164', '123456rp', '获取注资分页列表', '9', 'cc.mrbird.febs.order.controller.OrderController.orderList()', ' request: \"QueryRequest(pageSize=10, pageNum=1, field=null, order=null)\" order: OrderVo{acnum=\'\', nickname=\'null\', auditType=\'null\', applyUserName=\'null\', auditUserName=null, closeUserName=null, orderId=null, orderNumber=\'\', amount=\'\', orderStatus=\'\', applyUserId=null, auditUserId=null, closeUserId=null, expireDays=null, endTime=null, createTime=null, btnList=[], submitInfo=null}', '119.37.199.25', '2020-08-20 15:20:10', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('165', '123456rp', '获取表头号列表', '9', 'cc.mrbird.febs.order.controller.OrderController.getAcnumList()', '', '119.37.199.25', '2020-08-20 15:20:11', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('166', '123456rp', '获取审核员列表', '16', 'cc.mrbird.febs.order.controller.OrderController.getAuditUserNameList()', ' deviceId: \"29\"', '119.37.199.25', '2020-08-20 15:20:14', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('167', '123456rp', '新增', '19', 'cc.mrbird.febs.order.controller.OrderController.addOrder()', ' order: OrderVo{acnum=\'CPU222\', nickname=\'null\', auditType=\'null\', applyUserName=\'null\', auditUserName=null, closeUserName=null, orderId=68, orderNumber=\'51437883076251648\', amount=\'1.00\', orderStatus=\'1\', applyUserId=33, auditUserId=32, closeUserId=null, expireDays=7, endTime=Thu Aug 27 15:20:15 CST 2020, createTime=Thu Aug 20 15:20:15 CST 2020, btnList=[], submitInfo=null}', '119.37.199.25', '2020-08-20 15:20:16', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('168', '123456rp', '获取注资分页列表', '13', 'cc.mrbird.febs.order.controller.OrderController.orderList()', ' request: \"QueryRequest(pageSize=10, pageNum=1, field=createTime, order=null)\" order: OrderVo{acnum=\'\', nickname=\'null\', auditType=\'null\', applyUserName=\'null\', auditUserName=null, closeUserName=null, orderId=null, orderNumber=\'\', amount=\'\', orderStatus=\'\', applyUserId=null, auditUserId=null, closeUserId=null, expireDays=null, endTime=null, createTime=null, btnList=[], submitInfo=null}', '119.37.199.25', '2020-08-20 15:20:16', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('169', '123456rp', '提交注资审核', '1', 'cc.mrbird.febs.order.controller.ViewController.submitApply()', ' orderId: \"68\" audityType: \"1\" orderId: \"68\" audityType: \"1\"', '119.37.199.25', '2020-08-20 15:20:18', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('170', '123456rp', '提交审核', '33', 'cc.mrbird.febs.order.controller.OrderController.submitApply()', ' orderVo: OrderVo{acnum=\'CPU222\', nickname=\'null\', auditType=\'1\', applyUserName=\'null\', auditUserName=null, closeUserName=null, orderId=68, orderNumber=\'51437883076251648\', amount=\'1.00\', orderStatus=\'2\', applyUserId=33, auditUserId=32, closeUserId=null, expireDays=7, endTime=Thu Aug 27 15:20:16 CST 2020, createTime=Thu Aug 20 15:20:16 CST 2020, btnList=[], submitInfo=} auditType: \"1\"', '119.37.199.25', '2020-08-20 15:20:19', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('171', '123456rp', '获取注资分页列表', '12', 'cc.mrbird.febs.order.controller.OrderController.orderList()', ' request: \"QueryRequest(pageSize=10, pageNum=1, field=createTime, order=null)\" order: OrderVo{acnum=\'\', nickname=\'null\', auditType=\'null\', applyUserName=\'null\', auditUserName=null, closeUserName=null, orderId=null, orderNumber=\'\', amount=\'\', orderStatus=\'\', applyUserId=null, auditUserId=null, closeUserId=null, expireDays=null, endTime=null, createTime=null, btnList=[], submitInfo=null}', '119.37.199.25', '2020-08-20 15:20:19', '中国|华东|浙江省|杭州市|电信');
INSERT INTO `t_log` VALUES ('172', 'rp123456', '审核通过', '29', 'cc.mrbird.febs.audit.controller.AuditController.passAudit()', ' auditId: \"49\"', '119.37.199.25', '2020-08-20 15:20:30', '中国|华东|浙江省|杭州市|电信');

-- ----------------------------
-- Table structure for t_login_log
-- ----------------------------
DROP TABLE IF EXISTS `t_login_log`;
CREATE TABLE `t_login_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `username` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `login_time` datetime NOT NULL COMMENT '登录时间',
  `location` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '登录地点',
  `ip` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'ip地址',
  `system` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '操作系统',
  `browser` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '浏览器',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `t_login_log_login_time` (`login_time`)
) ENGINE=MyISAM AUTO_INCREMENT=405 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='登录日志表';

-- ----------------------------
-- Records of t_login_log
-- ----------------------------
INSERT INTO `t_login_log` VALUES ('325', 'root', '2020-12-09 13:52:13', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows ', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('326', 'root', '2020-12-09 13:54:35', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows ', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('323', 'root', '2020-12-09 11:21:21', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows ', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('324', 'root', '2020-12-09 11:22:44', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows ', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('321', 'admin', '2020-12-09 11:01:11', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows ', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('322', 'root', '2020-12-09 11:17:48', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows ', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('319', 'admin', '2020-12-08 09:39:57', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows ', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('320', 'sbgly', '2020-12-08 13:26:50', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows ', 'Chrome 78');
INSERT INTO `t_login_log` VALUES ('317', 'lai666', '2020-12-07 15:45:34', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows ', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('318', 'admin', '2020-12-07 15:52:18', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows ', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('315', 'zzshy', '2020-12-07 11:06:33', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows ', 'Chrome 78');
INSERT INTO `t_login_log` VALUES ('316', 'admin', '2020-12-07 14:53:42', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows ', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('313', 'sbgly', '2020-12-04 12:55:04', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows ', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('314', 'sbgly', '2020-12-07 10:14:50', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows ', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('311', '123456rp', '2020-12-03 14:33:59', '中国|华东|浙江省|杭州市|电信', '60.176.203.94', 'Windows 10', 'Chrome 86');
INSERT INTO `t_login_log` VALUES ('312', 'rp123456', '2020-12-03 14:38:33', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows ', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('309', 'admin', '2020-12-02 13:42:23', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows ', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('310', 'rp123456', '2020-12-03 14:33:24', '中国|华东|浙江省|杭州市|电信', '60.176.203.94', 'Windows 10', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('307', 'admin', '2020-11-30 13:09:56', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows ', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('308', 'admin', '2020-12-01 14:24:42', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows ', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('305', 'admin', '2020-11-26 15:37:18', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows ', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('306', 'admin', '2020-11-27 15:47:17', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows ', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('303', 'sbgly', '2020-11-19 09:54:04', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows 10', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('304', 'admin', '2020-11-23 09:53:46', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows 10', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('301', 'admin', '2020-11-11 10:59:08', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows 10', 'Firefox 81');
INSERT INTO `t_login_log` VALUES ('302', 'zzshy', '2020-11-12 09:45:56', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows 10', 'Chrome 86');
INSERT INTO `t_login_log` VALUES ('299', 'zzshy', '2020-11-11 10:52:24', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows 10', 'Chrome 86');
INSERT INTO `t_login_log` VALUES ('300', 'sbgly', '2020-11-11 10:52:49', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows 10', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('297', 'admin', '2020-11-11 10:49:46', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows 10', 'Firefox 81');
INSERT INTO `t_login_log` VALUES ('298', 'lai666', '2020-11-11 10:50:59', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows 10', 'Firefox 81');
INSERT INTO `t_login_log` VALUES ('295', 'rp123456', '2020-11-11 10:46:49', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows 10', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('296', '123456rp', '2020-11-11 10:47:20', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows 10', 'Chrome 86');
INSERT INTO `t_login_log` VALUES ('293', 'admin', '2020-11-03 09:39:07', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows 10', 'Chrome 86');
INSERT INTO `t_login_log` VALUES ('294', 'rp123456', '2020-11-06 10:24:35', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows 10', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('291', 'admin', '2020-11-03 09:35:06', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows 10', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('292', 'lai666', '2020-11-03 09:36:24', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows 10', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('289', 'admin', '2020-11-02 15:09:10', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows 10', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('290', 'rp123456', '2020-11-03 09:34:39', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows 10', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('287', 'rp123456', '2020-09-30 09:42:21', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows 10', 'Chrome 81');
INSERT INTO `t_login_log` VALUES ('288', 'admin', '2020-11-02 15:04:32', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows ', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('285', '123456rp', '2020-09-14 14:47:58', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows 10', 'Chrome 85');
INSERT INTO `t_login_log` VALUES ('286', 'admin', '2020-09-15 15:09:27', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows 10', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('283', 'admin', '2020-09-09 14:00:46', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows 10', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('284', 'admin', '2020-09-14 14:45:18', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows 10', 'Chrome 85');
INSERT INTO `t_login_log` VALUES ('281', '23333a', '2020-09-09 13:13:21', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows 10', 'Firefox 80');
INSERT INTO `t_login_log` VALUES ('282', 'rp123456', '2020-09-09 13:14:16', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows 10', 'Firefox 80');
INSERT INTO `t_login_log` VALUES ('279', '123456rp', '2020-09-09 08:39:43', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows 10', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('280', 'admin', '2020-09-09 13:12:04', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows 10', 'Firefox 80');
INSERT INTO `t_login_log` VALUES ('277', '123456rp', '2020-09-07 09:06:10', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows 10', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('278', '123456rp', '2020-09-08 08:55:38', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows 10', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('275', 'rp123456', '2020-09-05 09:28:44', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows 10', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('276', '123456rp', '2020-09-05 09:30:02', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows 10', 'Firefox 80');
INSERT INTO `t_login_log` VALUES ('273', '123456rp', '2020-08-27 08:46:13', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows 10', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('274', 'rp123456', '2020-08-27 08:53:52', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows 10', 'Firefox 80');
INSERT INTO `t_login_log` VALUES ('271', 'rp123456', '2020-08-26 09:51:26', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows 10', 'Firefox 79');
INSERT INTO `t_login_log` VALUES ('272', 'rp123456', '2020-08-27 08:45:55', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows 10', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('269', 'admin', '2020-08-26 08:47:40', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows 10', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('270', '123456rp', '2020-08-26 09:50:42', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows 10', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('267', '123456rp', '2020-08-25 14:45:10', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows 10', 'Firefox 79');
INSERT INTO `t_login_log` VALUES ('268', 'rp123456', '2020-08-25 14:59:23', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows 10', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('266', 'admin', '2020-08-25 09:25:32', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows 10', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('264', 'admin', '2020-08-24 10:13:13', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows 10', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('265', 'admin', '2020-08-25 08:57:51', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows 10', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('263', 'admin', '2020-08-22 08:48:37', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows 10', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('262', 'admin', '2020-08-21 09:17:35', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows 10', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('261', '123456rp', '2020-08-20 14:19:34', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows 10', 'Firefox 79');
INSERT INTO `t_login_log` VALUES ('260', 'rp123456', '2020-08-20 14:18:45', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows 10', 'Chrome 53');
INSERT INTO `t_login_log` VALUES ('259', 'rp123456', '2020-08-20 14:02:50', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows 10', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('258', '123456rp', '2020-08-20 14:02:28', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows 10', 'Firefox 79');
INSERT INTO `t_login_log` VALUES ('257', 'admin', '2020-08-20 14:01:46', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows 10', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('255', '123456rp', '2020-08-20 10:08:45', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows 10', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('256', 'rp123456', '2020-08-20 10:12:15', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows 10', 'Chrome 74');
INSERT INTO `t_login_log` VALUES ('253', 'admin', '2020-08-20 09:33:11', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows 10', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('254', '23333a', '2020-08-20 10:04:19', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows 10', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('252', 'admin', '2020-08-20 09:08:41', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows 10', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('327', 'root', '2020-12-09 14:11:57', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows ', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('328', 'admin', '2020-12-09 14:24:43', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows ', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('329', 'admin', '2020-12-09 14:24:43', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows ', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('330', 'root', '2020-12-09 14:27:56', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows ', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('331', 'root', '2020-12-09 14:39:51', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows ', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('332', 'admin', '2020-12-09 14:40:57', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows ', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('333', 'root', '2020-12-09 14:43:11', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows ', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('334', 'admin', '2020-12-09 14:59:08', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows ', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('335', 'admin', '2020-12-09 15:18:01', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows ', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('336', 'root', '2020-12-09 15:27:55', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows ', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('337', 'root', '2020-12-09 15:28:55', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows ', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('338', 'admin', '2020-12-09 15:29:48', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows ', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('339', 'root', '2020-12-09 15:38:10', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows ', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('340', 'admin', '2020-12-09 18:30:23', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows ', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('341', 'admin', '2020-12-16 12:32:31', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows ', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('342', 'gszh2020', '2020-12-16 12:38:06', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows ', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('343', 'gszh2020', '2020-12-16 12:38:41', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows ', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('344', 'root', '2020-12-16 12:46:48', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows ', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('345', 'admin', '2020-12-16 12:49:26', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows ', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('346', 'admin', '2020-12-16 16:02:13', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows ', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('347', 'lai666', '2020-12-16 16:52:00', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows ', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('348', 'lai666', '2020-12-16 16:53:04', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows ', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('349', 'admin', '2020-12-16 16:57:25', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows ', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('350', 'sbgly', '2020-12-17 09:50:28', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows ', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('351', 'admin', '2020-12-18 14:07:49', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows ', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('352', 'sbgly', '2020-12-19 13:58:10', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows ', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('353', 'zzshy', '2020-12-19 13:58:54', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows ', 'Chrome 78');
INSERT INTO `t_login_log` VALUES ('354', 'sbgly', '2020-12-23 15:25:53', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows ', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('355', 'zzshy', '2020-12-23 15:26:48', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows ', 'Chrome 78');
INSERT INTO `t_login_log` VALUES ('356', 'sbgly', '2020-12-23 16:49:39', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows ', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('357', 'sbgly', '2020-12-23 17:08:42', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows ', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('358', 'admin', '2020-12-24 10:10:05', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows ', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('359', 'admin', '2020-12-28 11:12:59', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows ', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('360', 'admin', '2020-12-28 13:30:06', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows ', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('361', 'admin', '2020-12-28 13:32:16', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows ', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('362', 'admin', '2020-12-28 13:52:14', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows ', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('363', '23333a', '2020-12-28 14:10:35', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows ', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('364', 'admin', '2020-12-28 14:25:02', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows ', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('365', 'admin', '2020-12-30 09:39:28', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows ', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('366', 'admin', '2020-12-31 12:49:37', '中国|华东|浙江省|杭州市|电信', '115.196.216.198', 'Windows 10', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('367', 'admin', '2021-01-04 15:02:51', '中国|华东|浙江省|杭州市|电信', '125.120.43.222', 'Windows 10', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('368', 'admin', '2021-01-05 08:57:15', '中国|华东|浙江省|杭州市|电信', '183.128.177.159', 'Windows 10', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('369', 'admin', '2021-01-05 10:27:18', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows ', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('370', 'admin', '2021-01-06 08:58:23', '中国|华东|浙江省|杭州市|电信', '183.128.177.159', 'Windows 10', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('371', 'jggly', '2021-01-06 13:42:46', '中国|华东|浙江省|杭州市|电信', '183.128.177.159', 'Windows 10', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('372', 'test1', '2021-01-06 13:45:38', '中国|华东|浙江省|杭州市|电信', '183.128.177.159', 'Windows 10', 'Chrome 86');
INSERT INTO `t_login_log` VALUES ('373', 'test2', '2021-01-06 13:46:10', '中国|华东|浙江省|杭州市|电信', '183.128.177.159', 'Windows 10', 'Firefox 82');
INSERT INTO `t_login_log` VALUES ('374', 'admin', '2021-01-07 08:51:13', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows ', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('375', 'admin', '2021-01-07 08:59:58', '中国|华东|浙江省|杭州市|电信', '183.128.177.159', 'Windows 10', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('376', 'admin', '2021-01-07 10:56:29', '中国|华东|浙江省|杭州市|电信', '183.128.177.159', 'Windows 10', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('377', 'admin', '2021-01-08 08:50:32', '中国|华东|浙江省|杭州市|电信', '183.128.177.159', 'Windows 10', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('378', 'admin', '2021-01-08 10:18:22', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows ', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('379', 'admin', '2021-01-09 08:56:47', '中国|华东|浙江省|杭州市|电信', '125.118.62.204', 'Windows 10', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('380', 'admin', '2021-01-29 14:33:38', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows ', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('381', 'admin', '2021-02-07 10:48:39', '中国|华东|浙江省|杭州市|电信', '115.195.38.121', 'Windows 10', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('382', 'admin', '2021-02-23 09:53:30', '中国|华东|浙江省|杭州市|电信', '115.197.171.114', 'Windows 10', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('383', 'admin', '2021-02-24 09:10:39', '中国|华东|浙江省|杭州市|电信', '115.197.171.114', 'Windows 10', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('384', 'admin', '2021-03-06 14:31:54', '中国|华东|浙江省|杭州市|电信', '122.233.157.221', 'Windows 10', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('385', 'admin', '2021-03-23 13:13:23', '中国|华东|浙江省|杭州市|电信', '183.156.126.145', 'Windows 10', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('386', 'admin', '2021-04-17 13:32:25', '中国|华东|浙江省|嘉兴市|电信', '115.227.107.23', 'Windows 10', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('387', 'admin', '2021-04-22 12:48:40', '中国|华东|浙江省|嘉兴市|电信', '115.227.232.51', 'Windows 10', 'Chrome 90');
INSERT INTO `t_login_log` VALUES ('388', 'admin', '2021-05-12 13:32:46', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows ', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('389', 'adminEls', '2021-05-12 14:21:25', '中国|华东|浙江省|杭州市|电信', '183.129.40.254', 'Windows 10', 'Chrome 90');
INSERT INTO `t_login_log` VALUES ('390', 'adminEls', '2021-05-12 15:29:47', '中国|华东|浙江省|杭州市|电信', '183.129.40.254', 'Windows 10', 'Chrome 90');
INSERT INTO `t_login_log` VALUES ('391', 'admin', '2021-05-13 09:34:51', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows ', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('392', 'admin', '2021-05-14 10:36:36', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows ', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('393', 'admin', '2021-05-21 10:31:41', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows ', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('394', 'admin', '2021-05-22 09:58:02', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows ', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('395', 'admin', '2021-05-24 10:17:50', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows ', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('396', 'admin', '2021-05-25 09:55:06', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows ', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('397', 'admin', '2021-06-01 10:25:20', '中国|华东|浙江省|杭州市|电信', '115.192.74.230', 'Windows 10', 'Chrome 91');
INSERT INTO `t_login_log` VALUES ('398', 'admin', '2021-06-10 09:55:56', '中国|华东|浙江省|杭州市|电信', '125.120.223.98', 'Windows 10', 'Chrome 91');
INSERT INTO `t_login_log` VALUES ('399', 'admin', '2021-06-10 10:01:58', '中国|华东|浙江省|杭州市|电信', '125.120.223.98', 'Windows 10', 'Chrome 91');
INSERT INTO `t_login_log` VALUES ('400', 'admin', '2021-06-10 10:31:25', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows ', 'Chrome 70');
INSERT INTO `t_login_log` VALUES ('401', 'admin', '2021-06-10 14:40:39', '中国|华东|浙江省|杭州市|电信', '119.37.199.25', 'Windows ', 'Chrome 86');
INSERT INTO `t_login_log` VALUES ('402', 'admin', '2021-06-25 11:05:52', '中国|华东|浙江省|杭州市|电信', '183.128.179.68', 'Windows 10', 'Chrome 91');
INSERT INTO `t_login_log` VALUES ('403', 'admin', '2021-07-08 14:17:08', '中国|华东|浙江省|杭州市|电信', '115.230.122.20', 'Windows ', 'Chrome 91');
INSERT INTO `t_login_log` VALUES ('404', 'adminEls', '2021-07-10 13:41:35', '中国|华东|浙江省|杭州市|电信', '115.206.119.218', 'Windows 10', 'Chrome 91');

-- ----------------------------
-- Table structure for t_menu
-- ----------------------------
DROP TABLE IF EXISTS `t_menu`;
CREATE TABLE `t_menu` (
  `menu_id` bigint NOT NULL AUTO_INCREMENT COMMENT '菜单/按钮id',
  `parent_id` bigint NOT NULL COMMENT '上级菜单id',
  `menu_name` varchar(50) NOT NULL COMMENT '菜单/按钮名称',
  `url` varchar(50) DEFAULT NULL COMMENT '菜单url',
  `perms` text COMMENT '权限标识',
  `icon` varchar(50) DEFAULT NULL COMMENT '图标',
  `type` char(2) NOT NULL COMMENT '类型 0菜单 1按钮',
  `order_num` bigint DEFAULT NULL COMMENT '排序',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`menu_id`) USING BTREE,
  KEY `t_menu_parent_id` (`parent_id`),
  KEY `t_menu_menu_id` (`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=205 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='菜单表';

-- ----------------------------
-- Records of t_menu
-- ----------------------------
INSERT INTO `t_menu` VALUES ('1', '0', '系统管理', null, null, 'layui-icon-setting', '0', '1', '2017-12-27 16:39:07', null);
INSERT INTO `t_menu` VALUES ('3', '0', '用户管理', '/system/user', 'user:view', 'layui-icon-team', '0', '2', '2017-12-27 16:47:13', '2020-06-24 15:23:59');
INSERT INTO `t_menu` VALUES ('4', '1', '角色管理', '/system/role', 'role:view', '', '0', '2', '2017-12-27 16:48:09', '2019-06-13 08:57:19');
INSERT INTO `t_menu` VALUES ('5', '1', '菜单管理', '/system/menu', 'menu:view', '', '0', '3', '2017-12-27 16:48:57', '2019-06-13 08:57:34');
INSERT INTO `t_menu` VALUES ('6', '0', '机构管理', '/system/dept', 'dept:view', 'layui-icon-bank-fill', '0', '6', '2017-12-27 16:57:33', '2020-07-13 14:15:06');
INSERT INTO `t_menu` VALUES ('11', '3', '新增用户', null, 'user:add', null, '1', null, '2017-12-27 17:02:58', null);
INSERT INTO `t_menu` VALUES ('12', '3', '修改用户', null, 'user:update', null, '1', null, '2017-12-27 17:04:07', null);
INSERT INTO `t_menu` VALUES ('13', '3', '删除用户', null, 'user:delete', null, '1', null, '2017-12-27 17:04:58', null);
INSERT INTO `t_menu` VALUES ('14', '4', '新增角色', null, 'role:add', null, '1', null, '2017-12-27 17:06:38', null);
INSERT INTO `t_menu` VALUES ('15', '4', '修改角色', null, 'role:update', null, '1', null, '2017-12-27 17:06:38', null);
INSERT INTO `t_menu` VALUES ('16', '4', '删除角色', null, 'role:delete', null, '1', null, '2017-12-27 17:06:38', null);
INSERT INTO `t_menu` VALUES ('17', '5', '新增菜单', null, 'menu:add', null, '1', null, '2017-12-27 17:08:02', null);
INSERT INTO `t_menu` VALUES ('18', '5', '修改菜单', null, 'menu:update', null, '1', null, '2017-12-27 17:08:02', null);
INSERT INTO `t_menu` VALUES ('19', '5', '删除菜单', null, 'menu:delete', null, '1', null, '2017-12-27 17:08:02', null);
INSERT INTO `t_menu` VALUES ('20', '6', '新增', null, 'dept:add', null, '1', null, '2017-12-27 17:09:24', '2020-05-27 14:46:40');
INSERT INTO `t_menu` VALUES ('21', '6', '修改', null, 'dept:update', null, '1', null, '2017-12-27 17:09:24', '2020-05-27 14:46:33');
INSERT INTO `t_menu` VALUES ('22', '6', '删除', null, 'dept:delete', null, '1', null, '2017-12-27 17:09:24', '2020-05-27 14:46:26');
INSERT INTO `t_menu` VALUES ('160', '3', '密码重置', null, 'user:password:reset', null, '1', null, '2019-06-13 08:40:13', null);
INSERT INTO `t_menu` VALUES ('161', '3', '导出excel', null, 'user:export', null, '1', null, '2019-06-13 08:40:34', null);
INSERT INTO `t_menu` VALUES ('162', '4', '导出excel', null, 'role:export', null, '1', null, '2019-06-13 14:29:00', '2019-06-13 14:29:11');
INSERT INTO `t_menu` VALUES ('163', '5', '导出excel', null, 'menu:export', null, '1', null, '2019-06-13 14:29:32', null);
INSERT INTO `t_menu` VALUES ('164', '6', '导出excel', null, 'dept:export', null, '1', null, '2019-06-13 14:29:59', null);
INSERT INTO `t_menu` VALUES ('179', '0', '注资管理', '/order/order', 'order:view', 'layui-icon-alipay-square-fill', '0', '3', '2020-05-27 14:30:24', '2020-06-24 15:22:46');
INSERT INTO `t_menu` VALUES ('180', '0', '审核管理', '/audit/audit', 'audit:view', 'layui-icon-check-circle-fill', '0', '4', '2020-05-27 14:32:20', '2020-06-24 15:22:51');
INSERT INTO `t_menu` VALUES ('181', '0', '设备管理', '/device/device', 'device:view', 'layui-icon-printer', '0', '5', '2020-05-27 14:36:48', '2020-06-24 15:22:54');
INSERT INTO `t_menu` VALUES ('183', '181', '新增', null, 'device:add', null, '1', null, '2020-05-29 12:45:20', null);
INSERT INTO `t_menu` VALUES ('184', '181', '修改', null, 'device:update', null, '1', null, '2020-05-29 12:45:20', null);
INSERT INTO `t_menu` VALUES ('185', '181', '删除', null, 'device:delete', null, '1', null, '2020-05-29 12:45:20', null);
INSERT INTO `t_menu` VALUES ('186', '181', '导出Excel', null, 'device:export', null, '1', null, '2020-05-29 12:45:20', null);
INSERT INTO `t_menu` VALUES ('187', '181', '设备列表', null, 'device:list', null, '1', null, '2020-05-29 12:45:20', null);
INSERT INTO `t_menu` VALUES ('188', '3', '分配设备', null, 'user:device', null, '1', null, '2020-06-01 13:57:12', null);
INSERT INTO `t_menu` VALUES ('189', '179', '订单列表', null, 'order:list', null, '1', null, '2020-06-04 09:13:06', null);
INSERT INTO `t_menu` VALUES ('190', '179', '提交审核按钮', null, 'order:submitAuditApply', null, '1', null, '2020-06-04 09:13:06', null);
INSERT INTO `t_menu` VALUES ('191', '179', '添加注资', null, 'order:add', null, '1', null, '2020-06-04 09:13:06', null);
INSERT INTO `t_menu` VALUES ('192', '179', '导出报表', null, 'order:export', null, '1', null, '2020-06-04 09:13:06', null);
INSERT INTO `t_menu` VALUES ('193', '179', '修改注资', null, 'order:update', null, '1', null, '2020-06-04 09:13:06', null);
INSERT INTO `t_menu` VALUES ('194', '180', '审核列表', null, 'audit:list', null, '1', null, '2020-06-10 10:09:09', null);
INSERT INTO `t_menu` VALUES ('195', '180', '修改', null, 'audit:update', null, '1', null, '2020-06-10 10:10:00', null);
INSERT INTO `t_menu` VALUES ('196', '0', '俄罗斯管理', '', '', 'layui-icon-trademark', '0', '7', '2021-05-21 14:09:02', '2021-05-21 14:12:22');
INSERT INTO `t_menu` VALUES ('197', '196', '打印任务', '/rcs/printJob', 'printJob:view', '', '0', '3', '2021-05-21 14:14:06', '2021-06-10 10:24:49');
INSERT INTO `t_menu` VALUES ('198', '196', '合同管理', '/rcs/contract', 'contract:view', '', '0', '1', '2021-05-21 14:15:13', '2021-06-10 10:34:21');
INSERT INTO `t_menu` VALUES ('199', '196', '邮局管理', '/rcs/postoffice', 'postoffice:view', '', '0', '2', '2021-05-21 14:16:11', '2021-06-10 10:24:24');
INSERT INTO `t_menu` VALUES ('200', '196', '税率管理', '/rcs/tax', 'tax:view', '', '0', '4', '2021-05-21 14:18:28', '2021-06-10 10:24:56');
INSERT INTO `t_menu` VALUES ('201', '198', '新增', null, 'contract:add', null, '1', null, '2021-06-10 14:34:36', '2021-06-10 14:35:08');
INSERT INTO `t_menu` VALUES ('202', '198', '导出Excel', null, 'contract:export', null, '1', null, '2021-06-10 14:35:31', '2021-06-10 14:35:51');
INSERT INTO `t_menu` VALUES ('203', '198', '修改', null, 'contract:update', null, '1', null, '2021-06-10 14:36:22', null);
INSERT INTO `t_menu` VALUES ('204', '198', '列表', null, 'contract:list', null, '1', null, '2021-06-10 14:36:51', null);

-- ----------------------------
-- Table structure for t_notice
-- ----------------------------
DROP TABLE IF EXISTS `t_notice`;
CREATE TABLE `t_notice` (
  `notice_id` bigint NOT NULL AUTO_INCREMENT COMMENT '消息id',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `order_id` bigint NOT NULL COMMENT '订单id',
  `device_id` bigint NOT NULL COMMENT '设备id',
  `order_number` varchar(80) NOT NULL COMMENT '订单号',
  `amount` varchar(50) NOT NULL COMMENT '订单金额（单位为分）',
  `content` varchar(255) DEFAULT NULL COMMENT '描述',
  `is_read` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '0' COMMENT '是否读过 0 未读 1已读',
  `create_time` datetime NOT NULL COMMENT '添加时间',
  PRIMARY KEY (`notice_id`) USING BTREE,
  KEY `p_notice_notice_id` (`notice_id`)
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='消息提示表';

-- ----------------------------
-- Records of t_notice
-- ----------------------------
INSERT INTO `t_notice` VALUES ('42', '33', '64', '29', '51418515399380992', '666.00', '设备注资时间超过了30s', '1', '2020-08-20 14:21:00');
INSERT INTO `t_notice` VALUES ('43', '33', '70', '29', '51460704515002368', '111.00', '设备注资时间超过了30s', '1', '2020-08-20 16:52:54');
INSERT INTO `t_notice` VALUES ('44', '33', '71', '29', '51464029000044544', '111.00', '设备注资时间超过了30s', '1', '2020-08-20 17:06:38');
INSERT INTO `t_notice` VALUES ('45', '33', '74', '29', '53877569812041728', '777.00', '超过了注资截止日期', '0', '2020-09-03 09:00:00');
INSERT INTO `t_notice` VALUES ('46', '37', '77', '31', '81460778405859328', '50000.00', '设备注资时间超过了30s', '1', '2020-11-11 11:41:53');
INSERT INTO `t_notice` VALUES ('47', '37', '78', '31', '81464534069547008', '5000.00', '设备注资时间超过了30s', '1', '2020-11-11 11:57:56');
INSERT INTO `t_notice` VALUES ('48', '37', '79', '31', '81466213263347712', '5000.00', '设备注资时间超过了30s', '1', '2020-11-11 12:06:16');
INSERT INTO `t_notice` VALUES ('49', '37', '80', '31', '81468464501493760', '5000.00', '设备注资时间超过了30s', '1', '2020-11-11 12:12:17');
INSERT INTO `t_notice` VALUES ('50', '37', '81', '31', '81471239155224576', '5000.00', '设备注资时间超过了30s', '1', '2020-11-11 12:23:27');
INSERT INTO `t_notice` VALUES ('51', '37', '83', '31', '81472967472713728', '5000.00', '设备注资时间超过了30s', '1', '2020-11-11 12:50:46');
INSERT INTO `t_notice` VALUES ('52', '37', '91', '31', '90912091044712448', '122.00', '超过了注资截止日期', '1', '2020-12-09 14:00:00');
INSERT INTO `t_notice` VALUES ('53', '33', '88', '27', '89477015564062720', '666.00', '超过了注资截止日期', '0', '2020-12-10 15:00:00');
INSERT INTO `t_notice` VALUES ('54', '45', '102', '39', '101786243234402305', '12345.00', '超过了注资截止日期', '0', '2021-05-14 10:00:00');

-- ----------------------------
-- Table structure for t_order
-- ----------------------------
DROP TABLE IF EXISTS `t_order`;
CREATE TABLE `t_order` (
  `order_id` bigint NOT NULL AUTO_INCREMENT COMMENT '订单id',
  `device_id` bigint NOT NULL COMMENT '设备id',
  `order_number` varchar(80) NOT NULL COMMENT '订单号',
  `amount` varchar(50) NOT NULL COMMENT '订单金额（单位为分）',
  `acnum` varchar(20) DEFAULT NULL COMMENT '表头号',
  `order_status` char(2) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单进度 	1.创建订单													2.审核中\r\n													3.审核通过   4 驳回\r\n													5.机器获取数据包\r\n													6.机器注资成功\r\n													7.撤销\r\n													8.冻结\r\n													9.闭环申请中\r\n													10.闭环申请审核失败',
  `old_status` char(2) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '冻结前的状态',
  `apply_user_id` bigint NOT NULL COMMENT '申请人id',
  `audit_user_id` bigint DEFAULT NULL COMMENT '审核人id',
  `close_user_id` bigint DEFAULT NULL COMMENT '闭环人id',
  `expire_days` int NOT NULL,
  `is_expire` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '0' COMMENT '是否过期 0 未过期 1过期',
  `is_alarm` char(1) DEFAULT '0' COMMENT '是否机器注资超时警报 0 否 1是',
  `end_time` datetime NOT NULL COMMENT '截止日期',
  `create_time` datetime NOT NULL COMMENT '添加时间',
  PRIMARY KEY (`order_id`) USING BTREE,
  KEY `p_order_order_id` (`order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=103 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='订单表';

-- ----------------------------
-- Records of t_order
-- ----------------------------
INSERT INTO `t_order` VALUES ('63', '29', '51359631238369280', '11111.00', 'CPU222', '6', null, '33', '32', null, '7', '0', '0', '2020-08-27 10:09:19', '2020-08-20 10:09:19');
INSERT INTO `t_order` VALUES ('64', '29', '51418515399380992', '666.00', 'CPU222', '6', null, '33', '32', null, '7', '0', '0', '2020-08-27 14:03:18', '2020-08-20 14:03:18');
INSERT INTO `t_order` VALUES ('65', '29', '51424189638381568', '1111.00', 'CPU222', '6', null, '33', '32', null, '7', '0', '0', '2020-08-27 14:25:51', '2020-08-20 14:25:51');
INSERT INTO `t_order` VALUES ('66', '29', '51437164461953024', '1111.00', 'CPU222', '6', null, '33', '32', null, '7', '0', '0', '2020-08-27 15:17:25', '2020-08-20 15:17:25');
INSERT INTO `t_order` VALUES ('67', '29', '51437677333057536', '1111.00', 'CPU222', '6', null, '33', '32', null, '7', '0', '0', '2020-08-27 15:19:27', '2020-08-20 15:19:27');
INSERT INTO `t_order` VALUES ('68', '29', '51437883076251648', '1.00', 'CPU222', '6', null, '33', '32', null, '7', '0', '0', '2020-08-27 15:20:16', '2020-08-20 15:20:16');
INSERT INTO `t_order` VALUES ('69', '29', '51453435421265920', '11.00', 'CPU222', '6', null, '33', '32', null, '7', '0', '0', '2020-08-27 16:22:04', '2020-08-20 16:22:04');
INSERT INTO `t_order` VALUES ('70', '29', '51460704515002368', '111.00', 'CPU222', '6', null, '33', '32', null, '7', '0', '0', '2020-08-27 16:50:57', '2020-08-20 16:50:57');
INSERT INTO `t_order` VALUES ('71', '29', '51464029000044544', '111.00', 'CPU222', '6', null, '33', '32', null, '7', '0', '0', '2020-08-27 17:04:10', '2020-08-20 17:04:10');
INSERT INTO `t_order` VALUES ('72', '29', '53241043377852416', '0.02', 'CPU222', '8', null, '33', '32', null, '7', '0', '0', '2020-09-01 14:45:23', '2020-08-25 14:45:23');
INSERT INTO `t_order` VALUES ('73', '29', '53529362091347968', '888.00', 'CPU222', '6', null, '33', '32', null, '7', '0', '0', '2020-09-02 09:51:03', '2020-08-26 09:51:03');
INSERT INTO `t_order` VALUES ('74', '29', '53877569812041728', '777.00', 'CPU222', '6', null, '33', '32', null, '7', '0', '0', '2020-09-03 08:54:43', '2020-08-27 08:54:43');
INSERT INTO `t_order` VALUES ('75', '29', '57148163009679360', '366.00', 'CPU222', '8', '3', '33', '32', null, '7', '0', '0', '2020-09-12 09:30:53', '2020-09-05 09:30:53');
INSERT INTO `t_order` VALUES ('76', '28', '58654046633988096', '11112.00', 'CPU111', '6', null, '33', '32', null, '7', '0', '0', '2020-09-16 13:14:43', '2020-09-09 13:14:43');
INSERT INTO `t_order` VALUES ('77', '31', '81460778405859328', '50000.00', 'S20008', '6', null, '37', '36', null, '7', '0', '0', '2020-11-18 11:40:32', '2020-11-11 11:40:32');
INSERT INTO `t_order` VALUES ('78', '31', '81464534069547008', '5000.00', 'S20008', '6', null, '37', '36', null, '7', '0', '0', '2020-11-18 11:55:27', '2020-11-11 11:55:27');
INSERT INTO `t_order` VALUES ('79', '31', '81466213263347712', '5000.00', 'S20008', '6', null, '37', '36', null, '7', '0', '0', '2020-11-18 12:02:08', '2020-11-11 12:02:08');
INSERT INTO `t_order` VALUES ('80', '31', '81468464501493760', '5000.00', 'S20008', '6', null, '37', '36', null, '7', '0', '0', '2020-11-18 12:11:04', '2020-11-11 12:11:04');
INSERT INTO `t_order` VALUES ('81', '31', '81471239155224576', '5000.00', 'S20008', '6', null, '37', '36', null, '7', '0', '0', '2020-11-18 12:22:06', '2020-11-11 12:22:06');
INSERT INTO `t_order` VALUES ('82', '31', '81472010403844096', '5000.00', 'S20008', '6', null, '37', '36', null, '7', '0', '0', '2020-11-18 12:25:10', '2020-11-11 12:25:10');
INSERT INTO `t_order` VALUES ('83', '31', '81472967472713728', '5000.00', 'S20008', '6', null, '37', '36', null, '7', '0', '0', '2020-11-18 12:28:58', '2020-11-11 12:28:58');
INSERT INTO `t_order` VALUES ('84', '31', '81479281972219904', '5000.00', 'S20008', '6', null, '37', '36', null, '7', '0', '0', '2020-11-18 12:54:04', '2020-11-11 12:54:04');
INSERT INTO `t_order` VALUES ('85', '31', '81479980944592896', '5000.00', 'S20008', '6', null, '37', '36', null, '7', '0', '0', '2020-11-18 12:56:50', '2020-11-11 12:56:50');
INSERT INTO `t_order` VALUES ('86', '31', '81480714691940352', '5000.00', 'S20008', '6', null, '37', '36', null, '7', '0', '0', '2020-11-18 12:59:45', '2020-11-11 12:59:45');
INSERT INTO `t_order` VALUES ('87', '31', '81481232596209664', '5000.00', 'S20008', '6', null, '37', '36', null, '7', '0', '0', '2020-11-18 13:01:49', '2020-11-11 13:01:49');
INSERT INTO `t_order` VALUES ('88', '27', '89477015564062720', '666.00', 'CPU223', '10', null, '33', '32', null, '7', '1', '0', '2020-12-10 14:34:12', '2020-12-03 14:34:12');
INSERT INTO `t_order` VALUES ('89', '31', '90874369739460608', '1212.00', 'S20008', '8', null, '37', '36', null, '7', '0', '0', '2020-12-14 11:06:47', '2020-12-07 11:06:47');
INSERT INTO `t_order` VALUES ('90', '31', '90898340031631360', '777.00', 'S20008', '8', null, '37', '36', null, '7', '0', '0', '2020-12-14 12:42:02', '2020-12-07 12:42:02');
INSERT INTO `t_order` VALUES ('91', '31', '90912091044712448', '122.00', 'S20008', '8', '1', '37', '36', null, '2', '0', '0', '2020-12-09 13:36:40', '2020-12-07 13:36:40');
INSERT INTO `t_order` VALUES ('92', '31', '94482352780218368', '666.00', 'S20008', '8', null, '37', '36', null, '7', '0', '0', '2020-12-24 10:03:37', '2020-12-17 10:03:37');
INSERT INTO `t_order` VALUES ('93', '31', '94482855152979968', '777.00', 'S20008', '8', '1', '37', '36', null, '7', '0', '0', '2020-12-24 10:05:37', '2020-12-17 10:05:37');
INSERT INTO `t_order` VALUES ('94', '31', '94487114523217920', '888.00', 'S20008', '8', null, '37', '36', null, '7', '0', '0', '2020-12-24 10:22:32', '2020-12-17 10:22:32');
INSERT INTO `t_order` VALUES ('95', '31', '94488463864369152', '6666.00', 'S20008', '8', null, '37', '36', null, '7', '0', '0', '2020-12-24 10:27:54', '2020-12-17 10:27:54');
INSERT INTO `t_order` VALUES ('96', '31', '95266200627580928', '666.00', 'S20008', '6', null, '37', '36', null, '7', '0', '0', '2020-12-26 13:58:21', '2020-12-19 13:58:21');
INSERT INTO `t_order` VALUES ('97', '31', '95272789073858560', '66666.00', 'S20008', '6', null, '37', '36', null, '7', '0', '0', '2020-12-26 14:24:32', '2020-12-19 14:24:32');
INSERT INTO `t_order` VALUES ('98', '31', '96759879297011712', '678.00', 'S20008', '6', null, '37', '36', null, '7', '0', '0', '2020-12-30 16:53:42', '2020-12-23 16:53:42');
INSERT INTO `t_order` VALUES ('99', '31', '96760990569467904', '66.00', 'S20008', '8', null, '37', '36', null, '7', '0', '0', '2020-12-30 16:58:07', '2020-12-23 16:58:07');
INSERT INTO `t_order` VALUES ('100', '31', '96765387852615680', '6666.00', 'S20008', '6', null, '37', '36', null, '7', '0', '0', '2020-12-30 17:15:35', '2020-12-23 17:15:35');
INSERT INTO `t_order` VALUES ('101', '36', '101786243234402304', '12345.00', 'AAA123', '6', null, '45', '44', null, '7', '0', '0', '2021-01-13 13:46:40', '2021-01-06 13:46:40');
INSERT INTO `t_order` VALUES ('102', '39', '101786243234402305', '12345.00', 'MXX001', '3', null, '45', '44', null, '7', '1', '0', '2021-05-14 09:00:40', '2021-05-20 13:46:40');

-- ----------------------------
-- Table structure for t_role
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role` (
  `role_id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色id',
  `role_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色名称',
  `remark` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '角色描述',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='角色表';

-- ----------------------------
-- Records of t_role
-- ----------------------------
INSERT INTO `t_role` VALUES ('1', '系统管理员', '系统管理员，拥有所有操作权限 ^_^', '2020-05-27 16:23:11', '2021-06-10 10:38:55');
INSERT INTO `t_role` VALUES ('2', '机构管理员', '添加设备管理员，注资审核员，查看注资记录', '2020-05-28 10:03:05', '2021-06-10 10:39:08');
INSERT INTO `t_role` VALUES ('3', '注资审核员', '审核注资申请，闭环申请', '2020-05-28 10:01:06', '2020-06-10 10:10:36');
INSERT INTO `t_role` VALUES ('4', '设备管理员', '申请注资，申请闭环', '2020-05-28 10:00:15', '2020-06-04 10:07:37');

-- ----------------------------
-- Table structure for t_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `t_role_menu`;
CREATE TABLE `t_role_menu` (
  `role_id` bigint NOT NULL COMMENT '角色id',
  `menu_id` bigint NOT NULL COMMENT '菜单/按钮id',
  KEY `t_role_menu_menu_id` (`menu_id`),
  KEY `t_role_menu_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='角色菜单关联表';

-- ----------------------------
-- Records of t_role_menu
-- ----------------------------
INSERT INTO `t_role_menu` VALUES ('4', '179');
INSERT INTO `t_role_menu` VALUES ('4', '189');
INSERT INTO `t_role_menu` VALUES ('4', '190');
INSERT INTO `t_role_menu` VALUES ('4', '191');
INSERT INTO `t_role_menu` VALUES ('4', '192');
INSERT INTO `t_role_menu` VALUES ('4', '193');
INSERT INTO `t_role_menu` VALUES ('4', '181');
INSERT INTO `t_role_menu` VALUES ('4', '187');
INSERT INTO `t_role_menu` VALUES ('3', '180');
INSERT INTO `t_role_menu` VALUES ('3', '194');
INSERT INTO `t_role_menu` VALUES ('3', '195');
INSERT INTO `t_role_menu` VALUES ('3', '181');
INSERT INTO `t_role_menu` VALUES ('3', '187');
INSERT INTO `t_role_menu` VALUES ('1', '1');
INSERT INTO `t_role_menu` VALUES ('1', '4');
INSERT INTO `t_role_menu` VALUES ('1', '14');
INSERT INTO `t_role_menu` VALUES ('1', '15');
INSERT INTO `t_role_menu` VALUES ('1', '16');
INSERT INTO `t_role_menu` VALUES ('1', '162');
INSERT INTO `t_role_menu` VALUES ('1', '5');
INSERT INTO `t_role_menu` VALUES ('1', '17');
INSERT INTO `t_role_menu` VALUES ('1', '18');
INSERT INTO `t_role_menu` VALUES ('1', '19');
INSERT INTO `t_role_menu` VALUES ('1', '163');
INSERT INTO `t_role_menu` VALUES ('1', '3');
INSERT INTO `t_role_menu` VALUES ('1', '11');
INSERT INTO `t_role_menu` VALUES ('1', '12');
INSERT INTO `t_role_menu` VALUES ('1', '13');
INSERT INTO `t_role_menu` VALUES ('1', '160');
INSERT INTO `t_role_menu` VALUES ('1', '161');
INSERT INTO `t_role_menu` VALUES ('1', '179');
INSERT INTO `t_role_menu` VALUES ('1', '189');
INSERT INTO `t_role_menu` VALUES ('1', '192');
INSERT INTO `t_role_menu` VALUES ('1', '180');
INSERT INTO `t_role_menu` VALUES ('1', '194');
INSERT INTO `t_role_menu` VALUES ('1', '181');
INSERT INTO `t_role_menu` VALUES ('1', '183');
INSERT INTO `t_role_menu` VALUES ('1', '184');
INSERT INTO `t_role_menu` VALUES ('1', '186');
INSERT INTO `t_role_menu` VALUES ('1', '187');
INSERT INTO `t_role_menu` VALUES ('1', '6');
INSERT INTO `t_role_menu` VALUES ('1', '20');
INSERT INTO `t_role_menu` VALUES ('1', '21');
INSERT INTO `t_role_menu` VALUES ('1', '22');
INSERT INTO `t_role_menu` VALUES ('1', '164');
INSERT INTO `t_role_menu` VALUES ('1', '196');
INSERT INTO `t_role_menu` VALUES ('1', '198');
INSERT INTO `t_role_menu` VALUES ('2', '3');
INSERT INTO `t_role_menu` VALUES ('2', '11');
INSERT INTO `t_role_menu` VALUES ('2', '12');
INSERT INTO `t_role_menu` VALUES ('2', '160');
INSERT INTO `t_role_menu` VALUES ('2', '161');
INSERT INTO `t_role_menu` VALUES ('2', '188');
INSERT INTO `t_role_menu` VALUES ('2', '179');
INSERT INTO `t_role_menu` VALUES ('2', '189');
INSERT INTO `t_role_menu` VALUES ('2', '192');
INSERT INTO `t_role_menu` VALUES ('2', '181');
INSERT INTO `t_role_menu` VALUES ('2', '184');
INSERT INTO `t_role_menu` VALUES ('2', '186');
INSERT INTO `t_role_menu` VALUES ('2', '187');
INSERT INTO `t_role_menu` VALUES ('2', '196');
INSERT INTO `t_role_menu` VALUES ('2', '198');
INSERT INTO `t_role_menu` VALUES ('2', '201');
INSERT INTO `t_role_menu` VALUES ('1', '201');
INSERT INTO `t_role_menu` VALUES ('2', '202');
INSERT INTO `t_role_menu` VALUES ('1', '202');
INSERT INTO `t_role_menu` VALUES ('2', '203');
INSERT INTO `t_role_menu` VALUES ('1', '203');
INSERT INTO `t_role_menu` VALUES ('2', '204');
INSERT INTO `t_role_menu` VALUES ('1', '204');

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `user_id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `username` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
  `realname` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '真实姓名',
  `parent_id` bigint NOT NULL COMMENT '上级用户id',
  `dept_id` bigint DEFAULT NULL COMMENT '机构id',
  `email` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '联系电话',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '状态 0锁定 1有效',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  `last_login_time` datetime DEFAULT NULL COMMENT '最近访问时间',
  `ssex` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '性别 0男 1女 2保密',
  `is_tab` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '是否开启tab，0关闭 1开启',
  `theme` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '主题',
  `avatar` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '头像',
  `description` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`user_id`) USING BTREE,
  KEY `t_user_username` (`username`),
  KEY `t_user_mobile` (`mobile`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='用户表';

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES ('8', 'admin', '37f66f3264e255aede93f3bcab13cf88', '最靓的仔', '0', '11', '', '', '1', '2020-05-26 16:18:07', '2021-02-07 10:52:20', '2021-07-08 14:17:12', '0', '1', 'black', 'default.jpg', '系统管理员');
INSERT INTO `t_user` VALUES ('9', 'adminEls', 'edde340d00161dc2d18e83ab0c38ed25', 'adminEls', '0', '16', '', '', '1', '2021-05-12 14:21:01', null, '2021-07-10 13:41:37', '0', '1', 'black', 'default.jpg', '');
INSERT INTO `t_user` VALUES ('31', '23333a', 'e69be170ce9373441fe61944c0597e0d', 'bbbbbbbb', '0', '11', '', '', '1', '2020-08-20 09:54:43', '2020-12-28 14:25:35', '2020-12-28 14:10:37', '0', '1', 'black', 'default.jpg', '');
INSERT INTO `t_user` VALUES ('32', 'rp123456', 'b60a884d39718b8730c780d74a936e00', 'rp123456', '31', '16', '', '', '1', '2020-08-20 10:04:59', '2021-01-06 09:46:34', '2020-12-03 14:38:35', '0', '1', 'black', 'default.jpg', '');
INSERT INTO `t_user` VALUES ('33', '123456rp', '215651f1bb450748489623b61f49ebf4', '123456rp', '31', '11', '', '', '1', '2020-08-20 10:05:20', '2020-12-09 13:51:53', '2020-12-03 14:34:01', '0', '1', 'black', 'default.jpg', '');
INSERT INTO `t_user` VALUES ('34', 'lai666', 'f76b9075636e1be80d4a89549e895523', 'lai666', '0', '11', '', '', '1', '2020-11-03 09:36:04', '2020-12-16 16:52:54', '2020-12-16 16:53:06', '0', '1', 'black', 'default.jpg', '');
INSERT INTO `t_user` VALUES ('36', 'zzshy', '133e5b3ee09201ac8bdcf175e3b94623', '注资审核员1号1', '34', '11', '', '', '1', '2020-11-03 09:38:29', '2020-12-16 16:57:00', '2020-12-23 16:53:28', '0', '1', 'black', 'default.jpg', '');
INSERT INTO `t_user` VALUES ('37', 'sbgly', '81fcfb6665e446515283fa9ca9ea4c03', '设备管理员一号', '34', '11', '', '', '1', '2020-11-03 09:39:46', null, '2020-12-23 17:15:21', '0', '1', 'black', 'default.jpg', '');
INSERT INTO `t_user` VALUES ('40', 'gszh2020', '5f32ac4eeb6d085509046113b3080f4e', 'test', '0', null, '', '', '1', '2020-12-16 12:37:29', '2020-12-16 12:43:23', '2021-01-06 13:40:18', '2', '1', 'black', 'default.jpg', '');
INSERT INTO `t_user` VALUES ('41', 'test', 'c8ca90a2ea885784ed3517fd502329e8', 'test', '0', '11', '', '', '1', '2020-12-16 16:58:13', '2020-12-28 14:09:39', null, '0', '1', 'black', 'default.jpg', '');
INSERT INTO `t_user` VALUES ('42', 'mytest', 'fd42b1f7b3f281da6fecb4885f24bb15', 'aaa6', '0', '11', '', '', '1', '2020-12-28 14:26:40', '2020-12-28 14:27:17', null, '0', '1', 'black', 'default.jpg', 'asdf ');
INSERT INTO `t_user` VALUES ('43', 'jggly', '4e504f1369ead54cd3641331f8e43455', 'jggly', '0', '11', '', '', '1', '2021-01-06 09:42:29', '2021-01-06 13:38:57', '2021-01-06 13:42:48', '1', '1', 'black', 'default.jpg', '');
INSERT INTO `t_user` VALUES ('44', 'test1', '4185ceee1cc9a58238752bcfc9f48f27', 'test1', '43', '11', '', '', '1', '2021-01-06 13:44:14', null, '2021-01-06 13:45:40', '0', '1', 'black', 'default.jpg', '');
INSERT INTO `t_user` VALUES ('45', 'test2', '3134be371e3e0cec5c4adc5a6cd8531e', 'test2', '43', '11', '', '', '1', '2021-01-06 13:44:41', null, '2021-01-06 13:46:14', '0', '1', 'black', 'default.jpg', '');

-- ----------------------------
-- Table structure for t_user_data_permission
-- ----------------------------
DROP TABLE IF EXISTS `t_user_data_permission`;
CREATE TABLE `t_user_data_permission` (
  `user_id` bigint NOT NULL,
  `dept_id` bigint NOT NULL,
  PRIMARY KEY (`user_id`,`dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户数据权限关联表';

-- ----------------------------
-- Records of t_user_data_permission
-- ----------------------------

-- ----------------------------
-- Table structure for t_user_device
-- ----------------------------
DROP TABLE IF EXISTS `t_user_device`;
CREATE TABLE `t_user_device` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `device_id` bigint NOT NULL COMMENT '设备id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `p_user_device_user_id` (`user_id`),
  KEY `p_user_device_device_id` (`device_id`)
) ENGINE=InnoDB AUTO_INCREMENT=161 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='用户设备关联表';

-- ----------------------------
-- Records of t_user_device
-- ----------------------------
INSERT INTO `t_user_device` VALUES ('121', '31', '27');
INSERT INTO `t_user_device` VALUES ('122', '31', '28');
INSERT INTO `t_user_device` VALUES ('123', '31', '29');
INSERT INTO `t_user_device` VALUES ('124', '31', '30');
INSERT INTO `t_user_device` VALUES ('127', '32', '28');
INSERT INTO `t_user_device` VALUES ('128', '32', '29');
INSERT INTO `t_user_device` VALUES ('129', '32', '30');
INSERT INTO `t_user_device` VALUES ('130', '32', '27');
INSERT INTO `t_user_device` VALUES ('131', '33', '29');
INSERT INTO `t_user_device` VALUES ('132', '33', '27');
INSERT INTO `t_user_device` VALUES ('133', '33', '28');
INSERT INTO `t_user_device` VALUES ('134', '33', '30');
INSERT INTO `t_user_device` VALUES ('135', '34', '31');
INSERT INTO `t_user_device` VALUES ('138', '34', '32');
INSERT INTO `t_user_device` VALUES ('139', '34', '33');
INSERT INTO `t_user_device` VALUES ('140', '34', '34');
INSERT INTO `t_user_device` VALUES ('145', '36', '31');
INSERT INTO `t_user_device` VALUES ('146', '36', '33');
INSERT INTO `t_user_device` VALUES ('147', '36', '34');
INSERT INTO `t_user_device` VALUES ('152', '37', '31');
INSERT INTO `t_user_device` VALUES ('153', '34', '35');
INSERT INTO `t_user_device` VALUES ('154', '43', '36');
INSERT INTO `t_user_device` VALUES ('155', '44', '36');
INSERT INTO `t_user_device` VALUES ('156', '45', '36');
INSERT INTO `t_user_device` VALUES ('157', '34', '37');
INSERT INTO `t_user_device` VALUES ('159', '9', '38');
INSERT INTO `t_user_device` VALUES ('160', '34', '39');

-- ----------------------------
-- Table structure for t_user_role
-- ----------------------------
DROP TABLE IF EXISTS `t_user_role`;
CREATE TABLE `t_user_role` (
  `user_id` bigint NOT NULL COMMENT '用户id',
  `role_id` bigint NOT NULL COMMENT '角色id',
  KEY `t_user_role_user_id` (`user_id`),
  KEY `t_user_role_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='用户角色关联表';

-- ----------------------------
-- Records of t_user_role
-- ----------------------------
INSERT INTO `t_user_role` VALUES ('34', '2');
INSERT INTO `t_user_role` VALUES ('37', '4');
INSERT INTO `t_user_role` VALUES ('33', '4');
INSERT INTO `t_user_role` VALUES ('40', '1');
INSERT INTO `t_user_role` VALUES ('36', '3');
INSERT INTO `t_user_role` VALUES ('31', '2');
INSERT INTO `t_user_role` VALUES ('8', '1');
INSERT INTO `t_user_role` VALUES ('32', '3');
INSERT INTO `t_user_role` VALUES ('42', '2');
INSERT INTO `t_user_role` VALUES ('43', '2');
INSERT INTO `t_user_role` VALUES ('44', '3');
INSERT INTO `t_user_role` VALUES ('45', '4');
INSERT INTO `t_user_role` VALUES ('9', '2');
