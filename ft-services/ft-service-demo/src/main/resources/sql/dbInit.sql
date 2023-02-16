-- ----------------------------
-- 测试使用，上线请删掉或替换掉
-- ----------------------------

DROP TABLE IF EXISTS `test`;
CREATE TABLE `test` (
  `test_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `test_type` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `test_flag` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`test_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;