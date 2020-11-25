/*
 Navicat Premium Data Transfer

 Source Server         : mysql-local
 Source Server Type    : MySQL
 Source Server Version : 50732
 Source Host           : localhost:3306
 Source Schema         : mall-demo

 Target Server Type    : MySQL
 Target Server Version : 50732
 File Encoding         : 65001

 Date: 26/11/2020 00:58:40
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_commodity
-- ----------------------------
DROP TABLE IF EXISTS `t_commodity`;
CREATE TABLE `t_commodity` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '商品ID',
  `code` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '商品编号',
  `user_id` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '所属用户ID',
  `user_name` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '所属用户名',
  `name` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '商品名称',
  `img_url` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '商品图片 ;分割',
  `unit_price` decimal(12,2) unsigned NOT NULL DEFAULT '1.00' COMMENT '商品单价',
  `stock` decimal(12,2) unsigned NOT NULL COMMENT '库存数量',
  `memo` varchar(128) COLLATE utf8_bin DEFAULT NULL COMMENT '商品简介',
  `commodity_stat` tinyint(4) DEFAULT NULL COMMENT '商品状态 1上架 0下架',
  `version` int(11) DEFAULT NULL COMMENT '数据版本号',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(3) unsigned DEFAULT '0' COMMENT '逻辑删除 1是 0否',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`code`) USING BTREE COMMENT '商品编号唯一键',
  KEY `idx_user_id` (`user_id`) USING BTREE COMMENT '所属用户ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='商品信息表\n';

-- ----------------------------
-- Table structure for t_order
-- ----------------------------
DROP TABLE IF EXISTS `t_order`;
CREATE TABLE `t_order` (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `order_code` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '订单编号',
  `commodity_id` bigint(20) NOT NULL COMMENT '商品ID',
  `commodity_name` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '商品名称',
  `purchaser_id` bigint(20) unsigned NOT NULL COMMENT '买方ID',
  `purchaser_name` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '买方名称',
  `seller_id` bigint(20) unsigned NOT NULL COMMENT '卖方ID',
  `seller_name` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '卖方名称',
  `unit_price` decimal(12,2) unsigned NOT NULL COMMENT '单价',
  `count` decimal(12,2) unsigned NOT NULL COMMENT '数量',
  `total_amount` decimal(12,2) NOT NULL COMMENT '订单总价',
  `discount_amout` decimal(12,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '折扣金额',
  `paid_amount` decimal(12,2) unsigned NOT NULL COMMENT '支付金额',
  `order_stat` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '订单状态 0已创建 1已支付 2已发货 3已收货 4etc.',
  `version` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '数据版本号',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '是否删除 1是 0否',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_code` (`order_code`) USING BTREE COMMENT '订单编号',
  KEY `idx_seller_id` (`seller_id`) USING BTREE COMMENT '卖家ID',
  KEY `idx_purchaser_id` (`purchaser_id`) USING BTREE COMMENT '买家ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='订单信息表\n';

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `user_code` varchar(20) COLLATE utf8_bin NOT NULL COMMENT '用户编号',
  `user_name` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '用户名',
  `password` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '用户密码',
  `nick_name` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '昵称',
  `level` tinyint(3) unsigned DEFAULT '1' COMMENT '用户等级 1普通用户 2 xx会员',
  `avatar` varchar(128) COLLATE utf8_bin DEFAULT NULL COMMENT '头像地址',
  `account_stat` tinyint(4) unsigned DEFAULT '0' COMMENT '用户状态 0正常 1冻结 etc.',
  `auth_code` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '外部鉴权编号',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(4) DEFAULT '0' COMMENT '逻辑删除 1是 0否',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_u_code` (`user_code`) USING BTREE COMMENT '用户编号唯一索引',
  UNIQUE KEY `idx_u_name` (`user_name`) USING BTREE COMMENT '用户名唯一索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='用户信息表';

SET FOREIGN_KEY_CHECKS = 1;
