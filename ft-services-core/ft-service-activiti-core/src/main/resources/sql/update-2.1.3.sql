ALTER TABLE acti_task_agent ADD `tenant_key` varchar(100) DEFAULT 'master' COMMENT '租户标识';
ALTER TABLE acti_task_config ADD `tenant_key` varchar(100) DEFAULT 'master' COMMENT '租户标识';
ALTER TABLE acti_todo_task_history MODIFY COLUMN `state` varchar(1) DEFAULT '0' COMMENT '状态 0：正常  1：已撤回 2：已退回';