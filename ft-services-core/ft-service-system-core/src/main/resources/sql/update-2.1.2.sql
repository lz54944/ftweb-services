ALTER TABLE sys_role_menu ADD misty_flag varchar(1) DEFAULT '0' COMMENT '半选状态(0：自身被选中  1：自身未被选中，选中的是子集菜单)' ;
ALTER TABLE sys_dept MODIFY `dept_name` varchar(100) DEFAULT '' COMMENT '部门名称';