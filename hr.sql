# ************************************************************
# Sequel Pro SQL dump
# Version 4541
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 127.0.0.1 (MySQL 5.7.26)
# Database: hr
# Generation Time: 2020-05-24 05:47:43 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table sys_attachment
# ------------------------------------------------------------

DROP TABLE IF EXISTS `sys_attachment`;

CREATE TABLE `sys_attachment` (
  `id` varchar(50) CHARACTER SET utf8 NOT NULL,
  `title` text COMMENT '标题',
  `user_id` varchar(50) DEFAULT NULL COMMENT '用户ID',
  `path` varchar(512) DEFAULT NULL COMMENT '路径',
  `mime_type` varchar(128) DEFAULT NULL COMMENT 'mime',
  `suffix` varchar(32) DEFAULT NULL COMMENT '附件的后缀',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`) USING BTREE,
  KEY `mime_type` (`mime_type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='附件';



# Dump of table sys_config
# ------------------------------------------------------------

DROP TABLE IF EXISTS `sys_config`;

CREATE TABLE `sys_config` (
  `id` varchar(50) CHARACTER SET utf8 NOT NULL,
  `config_key` varchar(128) DEFAULT NULL COMMENT 'key',
  `config_value` text COMMENT 'value',
  `status` tinyint(4) DEFAULT '1' COMMENT '状态   0：隐藏   1：显示',
  `remark` varchar(256) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `key` (`config_key`) USING BTREE,
  KEY `status` (`status`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统配置信息表';

LOCK TABLES `sys_config` WRITE;
/*!40000 ALTER TABLE `sys_config` DISABLE KEYS */;

INSERT INTO `sys_config` (`id`, `config_key`, `config_value`, `status`, `remark`)
VALUES
	('1263360574754975746','13','1',1,'3');

/*!40000 ALTER TABLE `sys_config` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table sys_google_auth_key
# ------------------------------------------------------------

DROP TABLE IF EXISTS `sys_google_auth_key`;

CREATE TABLE `sys_google_auth_key` (
  `auth_key_id` varchar(50) NOT NULL,
  `username` varchar(128) NOT NULL COMMENT '用户名',
  `secret` varchar(16) NOT NULL COMMENT '密钥',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime DEFAULT NULL COMMENT '创建时间',
  `reset_flag` tinyint(1) NOT NULL COMMENT '重置标识',
  `status_flag` tinyint(1) NOT NULL COMMENT '用户是否激活令牌',
  `del_flag` tinyint(1) NOT NULL COMMENT '删除标识',
  PRIMARY KEY (`auth_key_id`) USING BTREE,
  UNIQUE KEY `auth_key_id` (`auth_key_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='谷歌令牌';

LOCK TABLES `sys_google_auth_key` WRITE;
/*!40000 ALTER TABLE `sys_google_auth_key` DISABLE KEYS */;

INSERT INTO `sys_google_auth_key` (`auth_key_id`, `username`, `secret`, `create_time`, `modify_time`, `reset_flag`, `status_flag`, `del_flag`)
VALUES
	('cfb8ebf4cbec4f72b5233357ffd156ec','admin','2BOKDLZXYYVD3K6O','2020-05-20 04:10:03','2020-05-20 06:35:37',0,1,0);

/*!40000 ALTER TABLE `sys_google_auth_key` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table sys_log
# ------------------------------------------------------------

DROP TABLE IF EXISTS `sys_log`;

CREATE TABLE `sys_log` (
  `id` varchar(50) CHARACTER SET utf8 NOT NULL,
  `username` varchar(128) DEFAULT NULL COMMENT '用户名',
  `operation` varchar(128) DEFAULT NULL COMMENT '用户操作',
  `method` varchar(256) DEFAULT NULL COMMENT '请求方法',
  `params` text COMMENT '请求参数',
  `ip` varchar(64) DEFAULT NULL COMMENT 'IP地址',
  `time` bigint(20) DEFAULT NULL COMMENT '执行时长(毫秒)',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统日志';

LOCK TABLES `sys_log` WRITE;
/*!40000 ALTER TABLE `sys_log` DISABLE KEYS */;

INSERT INTO `sys_log` (`id`, `username`, `operation`, `method`, `params`, `ip`, `time`, `create_time`)
VALUES
	('1263333330829553666','admin','修改密码','com.rs.hr.modules.sys.controller.SysUserController.updatePassword()','admin','0:0:0:0:0:0:0:1',26,'2020-05-21 04:58:32'),
	('1263359830198870018','admin','保存配置','com.rs.hr.modules.sys.controller.SysConfigController.save()','{\"remark\":\"123\",\"status\":1}','0:0:0:0:0:0:0:1',0,'2020-05-21 06:43:50'),
	('1263359853913464834','admin','保存配置','com.rs.hr.modules.sys.controller.SysConfigController.save()','{\"remark\":\"123\",\"status\":1}','0:0:0:0:0:0:0:1',0,'2020-05-21 06:43:56'),
	('1263360574859833346','admin','保存配置','com.rs.hr.modules.sys.controller.SysConfigController.save()','{\"remark\":\"1\",\"configValue\":\"1\",\"configKey\":\"11\",\"id\":\"1263360574754975746\",\"status\":1}','0:0:0:0:0:0:0:1',23,'2020-05-21 06:46:48'),
	('1263361415948075010','admin','修改配置','com.rs.hr.modules.sys.controller.SysConfigController.update()','{\"remark\":\"2\",\"configValue\":\"1\",\"configKey\":\"11\",\"id\":\"1263360574754975746\",\"status\":1}','0:0:0:0:0:0:0:1',23,'2020-05-21 06:50:08'),
	('1263361740301991937','admin','保存菜单','com.rs.hr.modules.sys.controller.SysMenuController.save()','{\"orderNum\":0,\"type\":2,\"parentId\":\"13\",\"name\":\"查看\",\"perms\":\"sys:config:info\",\"id\":\"1263361740247465986\"}','0:0:0:0:0:0:0:1',21,'2020-05-21 06:51:26'),
	('1263361855309807617','admin','保存菜单','com.rs.hr.modules.sys.controller.SysMenuController.save()','{\"orderNum\":0,\"type\":2,\"parentId\":\"13\",\"name\":\"新增\",\"perms\":\"sys:config:save\",\"id\":\"1263361855267864577\"}','0:0:0:0:0:0:0:1',13,'2020-05-21 06:51:53'),
	('1263361942463250433','admin','保存菜单','com.rs.hr.modules.sys.controller.SysMenuController.save()','{\"orderNum\":0,\"type\":2,\"parentId\":\"13\",\"name\":\"修改\",\"perms\":\"sys:config:update\",\"id\":\"1263361942417113089\"}','0:0:0:0:0:0:0:1',13,'2020-05-21 06:52:14'),
	('1263362002471157762','admin','保存菜单','com.rs.hr.modules.sys.controller.SysMenuController.save()','{\"orderNum\":0,\"type\":2,\"parentId\":\"13\",\"name\":\"删除\",\"perms\":\"sys:config:delete\",\"id\":\"1263362002412437505\"}','0:0:0:0:0:0:0:1',18,'2020-05-21 06:52:28'),
	('1263362076878110722','admin','修改菜单','com.rs.hr.modules.sys.controller.SysMenuController.update()','{\"icon\":\"fa fa-cog\",\"orderNum\":11,\"type\":1,\"parentId\":\"1\",\"url\":\"modules/sys/config.html\",\"name\":\"sys.menu.configmanagement\",\"perms\":\"sys:config:list,sys:config:info\",\"id\":\"13\"}','0:0:0:0:0:0:0:1',18,'2020-05-21 06:52:46'),
	('1263362130779111425','admin','修改配置','com.rs.hr.modules.sys.controller.SysConfigController.update()','{\"remark\":\"3\",\"configValue\":\"1\",\"configKey\":\"11\",\"id\":\"1263360574754975746\",\"status\":1}','0:0:0:0:0:0:0:1',22,'2020-05-21 06:52:59'),
	('1263680198964961282','admin','保存菜单','com.rs.hr.modules.sys.controller.SysMenuController.save()','{\"orderNum\":0,\"type\":2,\"parentId\":\"18\",\"name\":\"新增\",\"perms\":\"sys:role:save\",\"id\":\"1263680198843326465\"}','0:0:0:0:0:0:0:1',19,'2020-05-22 03:56:52'),
	('1263680321333780482','admin','保存菜单','com.rs.hr.modules.sys.controller.SysMenuController.save()','{\"orderNum\":0,\"type\":2,\"parentId\":\"18\",\"name\":\"修改\",\"perms\":\"sys:role:update\",\"id\":\"1263680321283448834\"}','0:0:0:0:0:0:0:1',15,'2020-05-22 03:57:21'),
	('1263680650108493825','admin','保存菜单','com.rs.hr.modules.sys.controller.SysMenuController.save()','{\"orderNum\":0,\"type\":2,\"parentId\":\"18\",\"name\":\"查看\",\"perms\":\"sys:role:info\",\"id\":\"1263680650058162178\"}','0:0:0:0:0:0:0:1',16,'2020-05-22 03:58:40'),
	('1263682069574549506','admin','修改角色','com.rs.hr.modules.sys.controller.SysRoleController.update()','{\"remark\":\"系统管理员角色market\",\"menuIdList\":[],\"createTime\":1520621077000,\"name\":\"系统管理员角色\",\"id\":\"1\"}','0:0:0:0:0:0:0:1',17,'2020-05-22 04:04:18'),
	('1263685296663412737','admin','保存配置','com.rs.hr.modules.sys.controller.SysConfigController.save()','{\"remark\":\"1\",\"configValue\":\"2\",\"configKey\":\"11\",\"status\":1}','0:0:0:0:0:0:0:1',5503,'2020-05-22 04:17:08'),
	('1263685335620108289','admin','保存配置','com.rs.hr.modules.sys.controller.SysConfigController.save()','{\"remark\":\"1\",\"configValue\":\"2\",\"configKey\":\"11\",\"status\":1}','0:0:0:0:0:0:0:1',2000,'2020-05-22 04:17:17'),
	('1263685429765455873','admin','修改配置','com.rs.hr.modules.sys.controller.SysConfigController.update()','{\"remark\":\"3\",\"configValue\":\"1\",\"configKey\":\"11\",\"id\":\"1263360574754975746\",\"status\":1}','0:0:0:0:0:0:0:1',10508,'2020-05-22 04:17:39'),
	('1263686303208275969','admin','修改配置','com.rs.hr.modules.sys.controller.SysConfigController.update()','{\"remark\":\"3\",\"configValue\":\"1\",\"configKey\":\"11\",\"id\":\"1263360574754975746\",\"status\":1}','0:0:0:0:0:0:0:1',2224,'2020-05-22 04:21:07'),
	('1263686341594546178','admin','修改配置','com.rs.hr.modules.sys.controller.SysConfigController.update()','{\"remark\":\"3\",\"configValue\":\"1\",\"configKey\":\"13\",\"id\":\"1263360574754975746\",\"status\":1}','0:0:0:0:0:0:0:1',1810,'2020-05-22 04:21:17'),
	('1264386285075009538','admin','保存菜单','com.rs.hr.modules.sys.controller.SysMenuController.save()','{\"orderNum\":0,\"type\":2,\"parentId\":\"13\",\"name\":\"sys:menu:import\",\"perms\":\"sys:config:import\",\"id\":\"1264386284970151938\"}','0:0:0:0:0:0:0:1',16,'2020-05-24 02:42:36'),
	('1264386727913820161','admin','修改菜单','com.rs.hr.modules.sys.controller.SysMenuController.update()','{\"orderNum\":0,\"type\":2,\"parentId\":\"13\",\"name\":\"upload\",\"perms\":\"sys:config:import\",\"id\":\"1264386284970151938\"}','0:0:0:0:0:0:0:1',18,'2020-05-24 02:44:22'),
	('1264386906373066753','admin','修改菜单','com.rs.hr.modules.sys.controller.SysMenuController.update()','{\"orderNum\":0,\"type\":2,\"parentId\":\"13\",\"name\":\"sys:menu:upload\",\"perms\":\"sys:config:upload\",\"id\":\"1264386284970151938\"}','0:0:0:0:0:0:0:1',13,'2020-05-24 02:45:04'),
	('1264389750861971457','admin','修改菜单','com.rs.hr.modules.sys.controller.SysMenuController.update()','{\"orderNum\":0,\"type\":2,\"parentId\":\"13\",\"name\":\"common:upload\",\"perms\":\"sys:config:upload\",\"id\":\"1264386284970151938\"}','0:0:0:0:0:0:0:1',22,'2020-05-24 02:56:22'),
	('1264390158795677697','admin','修改菜单','com.rs.hr.modules.sys.controller.SysMenuController.update()','{\"orderNum\":0,\"type\":2,\"parentId\":\"13\",\"name\":\"sys.menu.upload\",\"perms\":\"sys:config:upload\",\"id\":\"1264386284970151938\"}','0:0:0:0:0:0:0:1',16,'2020-05-24 02:58:00'),
	('1264424429522644994','admin','保存菜单','com.rs.hr.modules.sys.controller.SysMenuController.save()','{\"orderNum\":0,\"type\":2,\"parentId\":\"13\",\"name\":\"sys.menu.download\",\"perms\":\"sys:config:download\",\"id\":\"1264424429220655106\"}','0:0:0:0:0:0:0:1',32,'2020-05-24 05:14:10');

/*!40000 ALTER TABLE `sys_log` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table sys_menu
# ------------------------------------------------------------

DROP TABLE IF EXISTS `sys_menu`;

CREATE TABLE `sys_menu` (
  `id` varchar(50) CHARACTER SET utf8 NOT NULL,
  `parent_id` varchar(50) DEFAULT NULL COMMENT '父菜单ID，一级菜单为0',
  `name` varchar(128) DEFAULT NULL COMMENT '菜单名称',
  `url` varchar(256) DEFAULT NULL COMMENT '菜单URL',
  `perms` varchar(500) DEFAULT NULL COMMENT '授权(多个用逗号分隔，如：user:list,user:create)',
  `type` tinyint(4) DEFAULT NULL COMMENT '类型   0：目录   1：菜单   2：按钮',
  `icon` varchar(256) DEFAULT NULL COMMENT '菜单图标',
  `order_num` int(11) DEFAULT NULL COMMENT '排序号',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `parent_id` (`parent_id`) USING BTREE,
  KEY `type` (`type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单管理';

LOCK TABLES `sys_menu` WRITE;
/*!40000 ALTER TABLE `sys_menu` DISABLE KEYS */;

INSERT INTO `sys_menu` (`id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
VALUES
	('1','0','sys.menu.systemmanagement',NULL,NULL,0,'fa fa-cogs',10),
	('10','6','sys.menu.save',NULL,'sys:menu:save,sys:menu:select',2,NULL,0),
	('11','6','sys.menu.update',NULL,'sys:menu:update,sys:menu:select',2,NULL,0),
	('12','6','sys.menu.delete',NULL,'sys:menu:delete',2,NULL,0),
	('1263680198843326465','18','sys.menu.save',NULL,'sys:role:save',2,NULL,0),
	('1263680321283448834','18','sys.menu.update',NULL,'sys:role:update',2,NULL,0),
	('1263680650058162178','18','sys.menu.view',NULL,'sys:role:info',2,NULL,0),
	('1264386284970151938','13','sys.menu.upload',NULL,'sys:config:upload',2,NULL,0),
	('1264424429220655106','13','sys.menu.download',NULL,'sys:config:download',2,NULL,0),
	('13','1','sys.menu.configmanagement','modules/sys/config.html','sys:config:list,sys:config:info',1,'fa fa-cog',11),
	('14','13','sys.menu.view',NULL,'sys:config:info',2,NULL,0),
	('15','13','sys.menu.save',NULL,'sys:config:save',2,NULL,0),
	('16','13','sys.menu.delete',NULL,'sys:config:delete',2,NULL,0),
	('17','13','sys.menu.update',NULL,'sys:config:update',2,NULL,0),
	('18','1','sys.menu.rolemanagement','modules/sys/role.html','sys:role:list',1,'fa fa-user-secret',3),
	('2','1','sys.menu.usermanagement','modules/sys/user.html',NULL,1,'fa fa-user',1),
	('3','2','sys.menu.view',NULL,'sys:user:list,sys:user:info',2,NULL,0),
	('4','2','sys.menu.save',NULL,'sys:user:save,sys:role:select,sys:dept:select,sys:dept:list',2,NULL,0),
	('5','2','sys.menu.update',NULL,'sys:user:update,sys:role:select,sys:dept:select,sys:dept:list',2,NULL,0),
	('6','1','sys.menu.menumanagement','modules/sys/menu.html',NULL,1,'fa fa-th-list',4),
	('7','1','sys.menu.systemlog','modules/sys/log.html','sys:log:list',1,'fa fa-pencil',7),
	('8','1','sys.menu.sqlmonitor','druid/sql.html',NULL,1,'fa fa-bug',8),
	('9','6','sys.menu.view',NULL,'sys:menu:list,sys:menu:info',2,NULL,0);

/*!40000 ALTER TABLE `sys_menu` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table sys_role
# ------------------------------------------------------------

DROP TABLE IF EXISTS `sys_role`;

CREATE TABLE `sys_role` (
  `id` varchar(50) CHARACTER SET utf8 NOT NULL,
  `name` varchar(128) DEFAULT NULL COMMENT '角色名称',
  `remark` varchar(256) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `name` (`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色';

LOCK TABLES `sys_role` WRITE;
/*!40000 ALTER TABLE `sys_role` DISABLE KEYS */;

INSERT INTO `sys_role` (`id`, `name`, `remark`, `create_time`)
VALUES
	('1','系统管理员角色','系统管理员角色market','2018-03-09 18:44:37');

/*!40000 ALTER TABLE `sys_role` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table sys_role_menu
# ------------------------------------------------------------

DROP TABLE IF EXISTS `sys_role_menu`;

CREATE TABLE `sys_role_menu` (
  `id` varchar(50) CHARACTER SET utf8 NOT NULL,
  `role_id` varchar(50) NOT NULL COMMENT '角色ID',
  `menu_id` varchar(50) NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `role_id` (`role_id`) USING BTREE,
  KEY `menu_id` (`menu_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色与菜单对应关系';



# Dump of table sys_user
# ------------------------------------------------------------

DROP TABLE IF EXISTS `sys_user`;

CREATE TABLE `sys_user` (
  `id` varchar(50) NOT NULL,
  `username` varchar(128) NOT NULL COMMENT '用户名',
  `nickname` varchar(128) DEFAULT NULL COMMENT '别名',
  `password` varchar(128) DEFAULT NULL COMMENT '密码',
  `salt` varchar(32) DEFAULT NULL COMMENT '盐',
  `email` varchar(64) DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(32) DEFAULT NULL COMMENT '手机号码',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态  0：禁用   1：正常',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `team_permission_ids` varchar(128) DEFAULT NULL COMMENT '组权限 多个逗号隔开 CN中国 JR日本 KR韩国 EN英国',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `username` (`username`) USING BTREE,
  KEY `email` (`email`) USING BTREE,
  KEY `mobile` (`mobile`) USING BTREE,
  KEY `status` (`status`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统用户';

LOCK TABLES `sys_user` WRITE;
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;

INSERT INTO `sys_user` (`id`, `username`, `nickname`, `password`, `salt`, `email`, `mobile`, `status`, `create_time`, `team_permission_ids`)
VALUES
	('1','admin','超级管理员','c1744d470b46a119fba1ed088ada71e9c6229d621de4628d3357193caa1f77f2','RECllptZzR0yxVi2Bgpx',NULL,NULL,1,'2016-11-11 11:11:11','CN,JP,KR,EN');

/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table sys_user_role
# ------------------------------------------------------------

DROP TABLE IF EXISTS `sys_user_role`;

CREATE TABLE `sys_user_role` (
  `id` varchar(50) CHARACTER SET utf8 NOT NULL,
  `user_id` varchar(50) NOT NULL COMMENT '用户ID',
  `role_id` varchar(50) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `user_id` (`user_id`) USING BTREE,
  KEY `role_id` (`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户与角色对应关系';

LOCK TABLES `sys_user_role` WRITE;
/*!40000 ALTER TABLE `sys_user_role` DISABLE KEYS */;

INSERT INTO `sys_user_role` (`id`, `user_id`, `role_id`)
VALUES
	('1167811156996259840','1167811156933345280','1167810647556096000'),
	('1167970399057608704','1167825696983089152','1'),
	('1168348839355088896','1167091081293070336','1'),
	('1169439739531493376','1169439739472773120','1169439503698362368'),
	('1169897277326622720','1169897277280485376','1'),
	('1170154607913992192','1','1'),
	('1172546643526942720','1172546643480805376','1167810647556096000'),
	('1184035813898321920','1170223378322161664','1184035516635414528'),
	('1184036124343926784','1172327298532114432','1184035516635414528'),
	('1184072359292698624','1184072359250755584','1170223103129681920'),
	('1185168394463215616','1185168394417078272','1184035516635414528'),
	('1185168682318299136','1185168682284744704','1184035516635414528'),
	('1187321082030325760','1187321081979994112','1'),
	('1195017721926909952','1160450888750333952','1'),
	('1222130959638331392','1220924719029551104','1'),
	('1223251890033131520','1223251889378820096','1223251617172684800'),
	('1223251958089908224','1220972357863079936','1223251617172684800'),
	('1223252064654589952','1223252064004472832','1223251617172684800'),
	('1223252290832433152','1223252290186510336','1223251617172684800'),
	('1227426877052813312','1227426876406890496','1223251617172684800'),
	('1233319317462843392','1232317990301597696','1'),
	('1233678221027835904','1233678220956532736','1'),
	('1236149302682714112','1236149302611410944','1'),
	('1242128653546946560','1242128653484032000','1223251617172684800'),
	('1243049682519719936','1243049682460999680','1243049576303165440'),
	('2','1220924719029551104','2');

/*!40000 ALTER TABLE `sys_user_role` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table sys_user_token
# ------------------------------------------------------------

DROP TABLE IF EXISTS `sys_user_token`;

CREATE TABLE `sys_user_token` (
  `id` varchar(50) CHARACTER SET utf8 NOT NULL,
  `user_id` varchar(50) NOT NULL COMMENT '用户ID',
  `token` varchar(500) NOT NULL COMMENT 'token',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户Token';

LOCK TABLES `sys_user_token` WRITE;
/*!40000 ALTER TABLE `sys_user_token` DISABLE KEYS */;

INSERT INTO `sys_user_token` (`id`, `user_id`, `token`, `expire_time`, `update_time`)
VALUES
	('1219469704884649984','1','6139326465383235336437376464343634303332306636306337373034336435','2020-04-08 13:15:50','2020-04-07 13:15:50'),
	('1220966706852134912','1220924719029551104','c68b049d42d218175196708f2a29e5f3','2020-05-10 23:54:09','2020-05-09 23:54:09'),
	('1221776460566495232','1220972357863079936','3c9243a64acbed5498a540f99249efc2','2020-02-28 15:11:47','2020-02-27 15:11:47'),
	('1223259099278344192','1223251889378820096','fe2157f42bb48fc8d37f585593201c91','2020-05-10 23:08:29','2020-05-09 23:08:29'),
	('1223264019951910912','1223252064004472832','a29259e2101e88499934541503826b43','2020-02-09 10:15:12','2020-02-08 10:15:12'),
	('1227427129541525504','1227426876406890496','9cce6d4f001ce47590c6a69abff69319','2020-02-16 04:14:39','2020-02-15 04:14:39'),
	('1232329557319614464','1232317990301597696','987fc1bfacf9806af69c04b4c3854aa8','2020-05-10 15:18:39','2020-05-09 15:18:39'),
	('1233679979884052480','1233678220956532736','43c9aa9e23b5e8433b883cdf30cbff9d','2020-03-10 19:06:41','2020-03-09 19:06:41'),
	('1236158363545370624','1236149302611410944','683018c2ff408d95c5b7687694ae2684','2020-03-08 20:42:07','2020-03-07 20:42:07'),
	('1242643834895073280','1242128653484032000','43997a6909c28e2b8043532bae6ea946','2020-05-03 21:48:31','2020-05-02 21:48:31'),
	('1262995652216934401','1','6139326465383235336437376464343634303332306636306337373034336435','2020-05-21 06:36:43','2020-05-20 06:36:43'),
	('1263000055271485441','1','6139326465383235336437376464343634303332306636306337373034336435','2020-05-21 06:54:13','2020-05-20 06:54:13'),
	('1263000123961602050','1','6139326465383235336437376464343634303332306636306337373034336435','2020-05-21 06:54:30','2020-05-20 06:54:30'),
	('1263000242576519170','1','6139326465383235336437376464343634303332306636306337373034336435','2020-05-21 06:54:58','2020-05-20 06:54:58'),
	('1263004073183973378','1','6139326465383235336437376464343634303332306636306337373034336435','2020-05-21 07:10:11','2020-05-20 07:10:11'),
	('1263004717475467266','1','6139326465383235336437376464343634303332306636306337373034336435','2020-05-21 07:12:45','2020-05-20 07:12:45'),
	('1263007508415393793','1','6139326465383235336437376464343634303332306636306337373034336435','2020-05-21 07:23:50','2020-05-20 07:23:50'),
	('1263008380922900481','1','6139326465383235336437376464343634303332306636306337373034336435','2020-05-21 07:27:18','2020-05-20 07:27:18'),
	('1263024959911927810','1','6139326465383235336437376464343634303332306636306337373034336435','2020-05-21 08:33:11','2020-05-20 08:33:11'),
	('1263025135506464770','1','6139326465383235336437376464343634303332306636306337373034336435','2020-05-21 08:33:53','2020-05-20 08:33:53'),
	('1263030637644783617','1','6139326465383235336437376464343634303332306636306337373034336435','2020-05-21 08:55:45','2020-05-20 08:55:45'),
	('1263031797273407489','1','6139326465383235336437376464343634303332306636306337373034336435','2020-05-21 09:00:21','2020-05-20 09:00:21'),
	('1263047625729437697','1','6139326465383235336437376464343634303332306636306337373034336435','2020-05-21 10:03:15','2020-05-20 10:03:15'),
	('1263048180073824257','1','6139326465383235336437376464343634303332306636306337373034336435','2020-05-21 10:05:27','2020-05-20 10:05:27'),
	('1263048972151357442','1','6139326465383235336437376464343634303332306636306337373034336435','2020-05-21 10:08:36','2020-05-20 10:08:36'),
	('1263049696436334594','1','6139326465383235336437376464343634303332306636306337373034336435','2020-05-21 10:11:18','2020-05-20 10:11:18'),
	('1263052730444906497','1','6139326465383235336437376464343634303332306636306337373034336435','2020-05-21 10:23:32','2020-05-20 10:23:32'),
	('1263054542463885314','1','6139326465383235336437376464343634303332306636306337373034336435','2020-05-21 10:30:44','2020-05-20 10:30:44'),
	('1263054840515321857','1','6139326465383235336437376464343634303332306636306337373034336435','2020-05-21 10:31:55','2020-05-20 10:31:55'),
	('1263319986420633602','1','6139326465383235336437376464343634303332306636306337373034336435','2020-05-22 04:05:31','2020-05-21 04:05:31'),
	('1263322202913148930','1','6139326465383235336437376464343634303332306636306337373034336435','2020-05-22 04:14:19','2020-05-21 04:14:19'),
	('1263333399897157634','1','3433333932303835353839666239373162373630636131336636326665386436','2020-05-22 04:58:49','2020-05-21 04:58:49'),
	('1264386112705892354','1','6237643732313836373630663031333832303234313438663934303263646163','2020-05-25 02:41:55','2020-05-24 02:41:55');

/*!40000 ALTER TABLE `sys_user_token` ENABLE KEYS */;
UNLOCK TABLES;



/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
