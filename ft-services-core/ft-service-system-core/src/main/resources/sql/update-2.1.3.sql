ALTER TABLE sys_tenant DROP COLUMN db_host;
ALTER TABLE sys_tenant DROP COLUMN db_port;
ALTER TABLE sys_tenant DROP COLUMN db_name;
ALTER TABLE sys_tenant DROP COLUMN db_user_name;
ALTER TABLE sys_tenant DROP COLUMN db_type;

CREATE TABLE `sys_tenant_db` (
  `id` int(11) NOT NULL COMMENT '主键',
  `tenant_key` varchar(100) DEFAULT NULL COMMENT '租户标识',
  `service_name` varchar(50) DEFAULT NULL COMMENT '服务名称',
  `db_host` varchar(100) DEFAULT NULL COMMENT '数据库地址',
  `db_port` int(10) DEFAULT NULL COMMENT '数据库端口号',
  `db_name` varchar(50) DEFAULT NULL COMMENT '数据库名称',
  `db_user_name` varchar(50) DEFAULT NULL COMMENT '数据库用户名',
  `db_type` varchar(20) DEFAULT NULL COMMENT '数据库类型',
  `db_password` varchar(50) DEFAULT NULL COMMENT '数据库密码',
  `db_url` varchar(255) DEFAULT NULL COMMENT '数据库url',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE sys_oper_log ADD `tenant_key` varchar(100) DEFAULT 'master' COMMENT '租户标识';
ALTER TABLE sys_menu     ADD `tenant_key` varchar(100) DEFAULT 'master' COMMENT '租户标识';
ALTER TABLE sys_dept     ADD `tenant_key` varchar(100) DEFAULT 'master' COMMENT '租户标识';
ALTER TABLE sys_role     ADD `tenant_key` varchar(100) DEFAULT 'master' COMMENT '租户标识';
ALTER TABLE sys_post     ADD `tenant_key` varchar(100) DEFAULT 'master' COMMENT '租户标识';
ALTER TABLE sys_user     ADD `tenant_key` varchar(100) DEFAULT 'master' COMMENT '租户标识';

ALTER TABLE sys_tenant_resource ADD `resource_new_id` int(20) COMMENT 'copy之后的资源主键';