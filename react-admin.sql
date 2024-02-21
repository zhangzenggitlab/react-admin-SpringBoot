/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50726
Source Host           : localhost:3306
Source Database       : react-admin

Target Server Type    : MYSQL
Target Server Version : 50726
File Encoding         : 65001

Date: 2024-02-21 12:24:46
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for at_department
-- ----------------------------
DROP TABLE IF EXISTS `at_department`;
CREATE TABLE `at_department` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `status` int(255) DEFAULT '1' COMMENT '1 :启用 2禁止',
  `parent_id` int(11) DEFAULT NULL,
  `phone` varchar(11) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `principal` varchar(255) DEFAULT NULL COMMENT '负责人姓名',
  `is_delete` int(1) DEFAULT '1' COMMENT '1:未删除 2：已删除',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=MyISAM AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of at_department
-- ----------------------------
INSERT INTO `at_department` VALUES ('1', '采购部门', '1', '0', '1808**', '103@qq.com', '张1三', '1', '2024-01-31 17:44:24', '2024-02-20 13:53:00');
INSERT INTO `at_department` VALUES ('2', '采购1', '1', '1', '1808**', '103@qq.com', '张1三', '1', '2024-01-31 17:44:13', '2024-02-20 14:02:42');
INSERT INTO `at_department` VALUES ('3', '销售部', '1', '0', '1808**', '103@qq.com', '张1三', '1', '2024-01-31 17:44:13', '2024-02-20 14:02:42');
INSERT INTO `at_department` VALUES ('4', '销售1', '1', '3', '1808**', '103@qq.com', '张1三', '1', '2024-01-31 17:44:13', '2024-02-20 14:02:42');
INSERT INTO `at_department` VALUES ('5', '销售2', '1', '3', '1808**', '103@qq.com', '张1三', '1', '2024-01-31 17:44:13', '2024-02-20 14:03:49');

-- ----------------------------
-- Table structure for at_menu
-- ----------------------------
DROP TABLE IF EXISTS `at_menu`;
CREATE TABLE `at_menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `path` varchar(255) DEFAULT NULL COMMENT '组件路径',
  `name` varchar(255) DEFAULT NULL,
  `permission` varchar(255) NOT NULL COMMENT '菜单标识(唯一)',
  `icon` varchar(255) DEFAULT NULL,
  `hide_menu` int(1) DEFAULT '1' COMMENT '1：显示 2：隐藏',
  `type` int(1) DEFAULT NULL COMMENT '1:目录 2:菜单 3:按钮',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_delete` int(255) DEFAULT '1' COMMENT '是否删除;1:没有；2：已删除',
  `parent_id` int(11) DEFAULT NULL,
  `status` int(1) DEFAULT NULL COMMENT '1：开启 2：禁止',
  `sort` int(11) DEFAULT '1' COMMENT '排序',
  PRIMARY KEY (`id`,`permission`)
) ENGINE=MyISAM AUTO_INCREMENT=30 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of at_menu
-- ----------------------------
INSERT INTO `at_menu` VALUES ('1', '/system', '系统管理', '/system', 'SettingOutlined', '1', '1', '2024-02-19 14:06:41', '2024-02-20 17:25:13', '1', '0', '1', '2');
INSERT INTO `at_menu` VALUES ('2', '/system/user', '用户列表', '/system/user', '', '1', '2', '2024-02-19 14:13:55', '2024-02-19 15:16:27', '1', '1', '1', '1');
INSERT INTO `at_menu` VALUES ('3', '/system/menu', '菜单列表', '/system/menu', '', '1', '2', '2024-02-19 14:58:37', '2024-02-19 15:16:27', '1', '1', '1', '2');
INSERT INTO `at_menu` VALUES ('4', '/system/role', '角色列表', '/system/role', '', '1', '2', '2024-02-19 14:59:48', '2024-02-19 15:16:27', '1', '1', '1', '3');
INSERT INTO `at_menu` VALUES ('5', '/system/department', '部门列表', '/system/department', '', '1', '2', '2024-02-19 15:00:19', '2024-02-20 09:26:05', '1', '1', '1', '4');
INSERT INTO `at_menu` VALUES ('6', '', '查询', 'user/select', '', '2', '3', '2024-02-19 16:42:02', '2024-02-19 17:00:07', '1', '2', '1', '1');
INSERT INTO `at_menu` VALUES ('7', '', '编辑', 'user/edit', '', '2', '3', '2024-02-19 16:42:21', '2024-02-19 17:00:02', '1', '2', '1', '1');
INSERT INTO `at_menu` VALUES ('8', '', '删除', 'user/delete', '', '2', '3', '2024-02-19 16:42:48', '2024-02-19 16:59:57', '1', '2', '1', '1');
INSERT INTO `at_menu` VALUES ('9', '', '新增', 'user/add', '', '2', '3', '2024-02-19 16:43:07', '2024-02-19 16:59:53', '1', '2', '1', '1');
INSERT INTO `at_menu` VALUES ('10', '', '查询', 'menu/select', '', '2', '3', '2024-02-19 16:46:13', '2024-02-19 16:58:21', '1', '3', '1', '1');
INSERT INTO `at_menu` VALUES ('11', '', '修改', 'menu/edit', '', '2', '3', '2024-02-19 16:52:58', '2024-02-19 16:58:13', '1', '3', '1', '1');
INSERT INTO `at_menu` VALUES ('12', '', '新增', 'menu/add', '', '1', '3', '2024-02-19 16:56:35', '2024-02-19 16:59:08', '1', '3', '1', '1');
INSERT INTO `at_menu` VALUES ('13', '', '删除', 'menu/delete', '', '1', '3', '2024-02-19 16:56:36', '2024-02-19 16:59:20', '1', '3', '1', '1');
INSERT INTO `at_menu` VALUES ('14', '', '新增', 'menu/add', '', '1', '1', '2024-02-19 16:56:38', '2024-02-19 16:58:57', '2', '3', '1', '1');
INSERT INTO `at_menu` VALUES ('15', '', '新增', 'menu/add', '', '1', '1', '2024-02-19 16:56:38', '2024-02-19 16:59:00', '2', '3', '1', '1');
INSERT INTO `at_menu` VALUES ('16', '', '新增', 'menu/add', '', '1', '1', '2024-02-19 16:56:39', '2024-02-19 16:58:54', '2', '3', '1', '1');
INSERT INTO `at_menu` VALUES ('17', '', '新增', 'menu/add', '', '1', '1', '2024-02-19 16:56:39', '2024-02-19 16:58:53', '2', '3', '1', '1');
INSERT INTO `at_menu` VALUES ('18', '', '新增', 'menu/add', '', '1', '1', '2024-02-19 16:56:39', '2024-02-19 16:58:49', '2', '3', '1', '1');
INSERT INTO `at_menu` VALUES ('19', '', '查询', 'role/select', '', '1', '3', '2024-02-19 17:15:36', '2024-02-19 17:42:03', '1', '4', '1', '1');
INSERT INTO `at_menu` VALUES ('20', '', '删除', 'role/delete', '', '1', '3', '2024-02-19 17:41:28', '2024-02-19 17:42:35', '1', '4', '1', '1');
INSERT INTO `at_menu` VALUES ('21', '', '新增', 'role/add', '', '1', '3', '2024-02-19 17:41:52', '2024-02-19 17:42:31', '1', '4', '1', '1');
INSERT INTO `at_menu` VALUES ('22', '', '编辑', 'role/edit', '', '1', '3', '2024-02-19 17:42:25', '2024-02-19 17:42:25', '1', '4', '1', '1');
INSERT INTO `at_menu` VALUES ('23', '', '查询', 'department/select', '', '1', '3', '2024-02-19 17:42:50', '2024-02-19 17:42:50', '1', '5', '1', '1');
INSERT INTO `at_menu` VALUES ('24', '', '编辑', 'department/edit', '', '1', '3', '2024-02-19 17:43:07', '2024-02-19 17:43:07', '1', '5', '1', '1');
INSERT INTO `at_menu` VALUES ('25', '', '删除', 'department/delete', '', '1', '3', '2024-02-19 17:43:20', '2024-02-19 17:43:46', '1', '5', '1', '1');
INSERT INTO `at_menu` VALUES ('26', '', '新增', 'department/add', '', '1', '3', '2024-02-19 17:43:34', '2024-02-19 17:43:34', '1', '5', '1', '1');
INSERT INTO `at_menu` VALUES ('27', '/workplace', '工作台', '', 'HistoryOutlined', '1', '2', '2024-02-20 16:35:00', '2024-02-20 16:35:00', '1', '0', '1', '1');
INSERT INTO `at_menu` VALUES ('28', '/personal', '个人页', '', 'UserOutlined', '1', '1', '2024-02-20 16:35:32', '2024-02-20 17:21:11', '1', '0', '1', '3');
INSERT INTO `at_menu` VALUES ('29', '/personal/setting', '个人设置', '', '', '1', '2', '2024-02-20 16:35:58', '2024-02-20 17:21:10', '1', '28', '1', '1');

-- ----------------------------
-- Table structure for at_menu_role
-- ----------------------------
DROP TABLE IF EXISTS `at_menu_role`;
CREATE TABLE `at_menu_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `menu_id` int(11) DEFAULT NULL,
  `role_id` int(11) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_delete` int(1) DEFAULT '1' COMMENT '1:未删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=MyISAM AUTO_INCREMENT=45 DEFAULT CHARSET=utf8 ROW_FORMAT=FIXED;

-- ----------------------------
-- Records of at_menu_role
-- ----------------------------
INSERT INTO `at_menu_role` VALUES ('1', '3', '3', '2024-02-20 15:34:51', '2024-02-20 15:34:51', '1');
INSERT INTO `at_menu_role` VALUES ('2', '10', '3', '2024-02-20 15:34:51', '2024-02-20 15:34:51', '1');
INSERT INTO `at_menu_role` VALUES ('3', '11', '3', '2024-02-20 15:34:51', '2024-02-20 15:34:51', '1');
INSERT INTO `at_menu_role` VALUES ('4', '12', '3', '2024-02-20 15:34:51', '2024-02-20 15:34:51', '1');
INSERT INTO `at_menu_role` VALUES ('5', '13', '3', '2024-02-20 15:34:51', '2024-02-20 15:34:51', '1');
INSERT INTO `at_menu_role` VALUES ('6', '4', '3', '2024-02-20 15:34:51', '2024-02-20 15:35:15', '2');
INSERT INTO `at_menu_role` VALUES ('7', '19', '3', '2024-02-20 15:34:51', '2024-02-20 15:35:15', '2');
INSERT INTO `at_menu_role` VALUES ('8', '20', '3', '2024-02-20 15:34:51', '2024-02-20 15:35:15', '2');
INSERT INTO `at_menu_role` VALUES ('9', '21', '3', '2024-02-20 15:34:51', '2024-02-20 15:35:15', '2');
INSERT INTO `at_menu_role` VALUES ('10', '22', '3', '2024-02-20 15:34:51', '2024-02-20 15:35:15', '2');
INSERT INTO `at_menu_role` VALUES ('11', '2', '2', '2024-02-20 15:35:05', '2024-02-20 15:35:05', '1');
INSERT INTO `at_menu_role` VALUES ('12', '6', '2', '2024-02-20 15:35:05', '2024-02-20 15:35:05', '1');
INSERT INTO `at_menu_role` VALUES ('13', '7', '2', '2024-02-20 15:35:05', '2024-02-20 15:35:05', '1');
INSERT INTO `at_menu_role` VALUES ('14', '8', '2', '2024-02-20 15:35:05', '2024-02-21 09:50:06', '2');
INSERT INTO `at_menu_role` VALUES ('15', '9', '2', '2024-02-20 15:35:05', '2024-02-20 15:35:05', '1');
INSERT INTO `at_menu_role` VALUES ('16', '5', '4', '2024-02-20 15:35:19', '2024-02-20 15:35:19', '1');
INSERT INTO `at_menu_role` VALUES ('17', '23', '4', '2024-02-20 15:35:19', '2024-02-20 15:35:19', '1');
INSERT INTO `at_menu_role` VALUES ('18', '24', '4', '2024-02-20 15:35:19', '2024-02-20 15:35:19', '1');
INSERT INTO `at_menu_role` VALUES ('19', '25', '4', '2024-02-20 15:35:19', '2024-02-20 15:35:19', '1');
INSERT INTO `at_menu_role` VALUES ('20', '26', '4', '2024-02-20 15:35:19', '2024-02-20 15:35:19', '1');
INSERT INTO `at_menu_role` VALUES ('21', '4', '5', '2024-02-20 15:35:23', '2024-02-20 15:35:23', '1');
INSERT INTO `at_menu_role` VALUES ('22', '19', '5', '2024-02-20 15:35:23', '2024-02-20 15:35:23', '1');
INSERT INTO `at_menu_role` VALUES ('23', '20', '5', '2024-02-20 15:35:23', '2024-02-20 15:35:23', '1');
INSERT INTO `at_menu_role` VALUES ('24', '21', '5', '2024-02-20 15:35:23', '2024-02-20 15:35:23', '1');
INSERT INTO `at_menu_role` VALUES ('25', '22', '5', '2024-02-20 15:35:23', '2024-02-20 15:35:23', '1');
INSERT INTO `at_menu_role` VALUES ('26', '1', '2', '2024-02-20 18:07:04', '2024-02-20 18:07:04', '1');
INSERT INTO `at_menu_role` VALUES ('27', '3', '2', '2024-02-20 18:15:04', '2024-02-20 18:17:31', '2');
INSERT INTO `at_menu_role` VALUES ('28', '4', '2', '2024-02-20 18:15:04', '2024-02-20 18:17:31', '2');
INSERT INTO `at_menu_role` VALUES ('29', '5', '2', '2024-02-20 18:15:04', '2024-02-20 18:17:31', '2');
INSERT INTO `at_menu_role` VALUES ('30', '10', '2', '2024-02-20 18:15:04', '2024-02-20 18:17:31', '2');
INSERT INTO `at_menu_role` VALUES ('31', '11', '2', '2024-02-20 18:15:04', '2024-02-20 18:17:31', '2');
INSERT INTO `at_menu_role` VALUES ('32', '12', '2', '2024-02-20 18:15:04', '2024-02-20 18:17:31', '2');
INSERT INTO `at_menu_role` VALUES ('33', '13', '2', '2024-02-20 18:15:04', '2024-02-20 18:17:31', '2');
INSERT INTO `at_menu_role` VALUES ('34', '19', '2', '2024-02-20 18:15:04', '2024-02-20 18:17:31', '2');
INSERT INTO `at_menu_role` VALUES ('35', '20', '2', '2024-02-20 18:15:04', '2024-02-20 18:17:31', '2');
INSERT INTO `at_menu_role` VALUES ('36', '21', '2', '2024-02-20 18:15:04', '2024-02-20 18:17:31', '2');
INSERT INTO `at_menu_role` VALUES ('37', '22', '2', '2024-02-20 18:15:04', '2024-02-20 18:17:31', '2');
INSERT INTO `at_menu_role` VALUES ('38', '23', '2', '2024-02-20 18:15:04', '2024-02-20 18:17:31', '2');
INSERT INTO `at_menu_role` VALUES ('39', '24', '2', '2024-02-20 18:15:04', '2024-02-20 18:17:31', '2');
INSERT INTO `at_menu_role` VALUES ('40', '25', '2', '2024-02-20 18:15:04', '2024-02-20 18:17:31', '2');
INSERT INTO `at_menu_role` VALUES ('41', '26', '2', '2024-02-20 18:15:04', '2024-02-20 18:17:31', '2');
INSERT INTO `at_menu_role` VALUES ('42', '27', '2', '2024-02-20 18:15:04', '2024-02-20 18:15:04', '1');
INSERT INTO `at_menu_role` VALUES ('43', '28', '2', '2024-02-20 18:15:08', '2024-02-20 18:15:08', '1');
INSERT INTO `at_menu_role` VALUES ('44', '29', '2', '2024-02-20 18:15:08', '2024-02-20 18:15:08', '1');

-- ----------------------------
-- Table structure for at_role
-- ----------------------------
DROP TABLE IF EXISTS `at_role`;
CREATE TABLE `at_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `permission` varchar(255) NOT NULL COMMENT '权限标识(唯一)',
  `description` varchar(255) DEFAULT NULL,
  `status` int(1) DEFAULT '1' COMMENT '是否默认；1 是 2 否',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `parent_id` int(255) DEFAULT '0',
  `is_delete` int(1) DEFAULT '1' COMMENT '1:未删除',
  PRIMARY KEY (`id`,`permission`)
) ENGINE=MyISAM AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of at_role
-- ----------------------------
INSERT INTO `at_role` VALUES ('1', '超级管理员', 'admin', '1', '1', '2024-01-31 17:21:22', '2024-02-20 14:31:52', '0', '1');
INSERT INTO `at_role` VALUES ('2', '用户管理员', 'user', '1', '1', '2024-01-31 17:29:39', '2024-02-20 14:39:45', '0', '1');
INSERT INTO `at_role` VALUES ('3', '菜单管理员', 'menu', '1', '1', '2024-02-01 14:30:56', '2024-02-20 14:39:48', '0', '1');
INSERT INTO `at_role` VALUES ('4', '部门管理员', 'department', '1', '1', '2024-02-01 14:31:24', '2024-02-20 14:39:52', '0', '1');
INSERT INTO `at_role` VALUES ('5', '角色管理员', 'role', '1', '1', '2024-02-18 13:52:49', '2024-02-20 14:39:54', '0', '1');

-- ----------------------------
-- Table structure for at_user
-- ----------------------------
DROP TABLE IF EXISTS `at_user`;
CREATE TABLE `at_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `status` int(1) DEFAULT '1' COMMENT '1:启用 ；2 禁用',
  `last_login_time` datetime DEFAULT NULL,
  `mail` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `is_delete` int(1) DEFAULT '1' COMMENT '是否删除;1:没有；2：已删除',
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `last_login_ip` varchar(255) DEFAULT NULL,
  `avatar` varchar(255) DEFAULT NULL,
  `department_id` int(11) DEFAULT NULL COMMENT '部门id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of at_user
-- ----------------------------
INSERT INTO `at_user` VALUES ('1', '1', '$2a$10$hZpUmOMJWWM7Hi7AhksRU.UoNbCaK9uS25Y8TTohRSUFjXsRyP2P.', '超级管理员', '1', null, null, null, '1', '2024-02-21 11:19:56', null, 'react-admin/1/58910082-6ddb-4c4b-b223-8e9c8d02b13c.png', '1');
INSERT INTO `at_user` VALUES ('2', '2', '$2a$10$hZpUmOMJWWM7Hi7AhksRU.UoNbCaK9uS25Y8TTohRSUFjXsRyP2P.', '张三', '1', null, null, null, '1', '2024-02-21 11:23:11', null, 'react-admin/2/ec4b402f-24d7-4feb-95d8-c71395422831.png', '1');

-- ----------------------------
-- Table structure for at_user_role
-- ----------------------------
DROP TABLE IF EXISTS `at_user_role`;
CREATE TABLE `at_user_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `role_id` int(11) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_delete` int(1) DEFAULT '1',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 ROW_FORMAT=FIXED;

-- ----------------------------
-- Records of at_user_role
-- ----------------------------
INSERT INTO `at_user_role` VALUES ('1', '2', '2', '2024-02-20 15:45:09', '2024-02-20 15:45:09', '1');
INSERT INTO `at_user_role` VALUES ('2', '2', '3', '2024-02-21 10:04:40', '2024-02-21 10:05:51', '2');
