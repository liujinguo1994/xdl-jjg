-- --------------------------------------------------------
-- 主机:                           rm-bp1w97q2026161002wo.mysql.rds.aliyuncs.com
-- 服务器版本:                        5.7.25-log - Source distribution
-- 服务器OS:                        Linux
-- HeidiSQL 版本:                  10.2.0.5599
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Dumping database structure for jjg-test
CREATE DATABASE IF NOT EXISTS `jjg-test` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `jjg-test`;

-- Dumping structure for table jjg-test.auction
CREATE TABLE IF NOT EXISTS `auction` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增长ID',
  `auction_no` varchar(128) NOT NULL DEFAULT '' COMMENT '活动编号',
  `item_no` varchar(64) NOT NULL DEFAULT '' COMMENT '商品编码',
  `item_name` varchar(50) NOT NULL COMMENT '商品名称',
  `item_desc` varchar(512) NOT NULL COMMENT '商品描述',
  `item_pic` varchar(1024) NOT NULL COMMENT '商品主图',
  `supplier_no` varchar(128) NOT NULL DEFAULT '' COMMENT '供应商编号',
  `supplier_name` varchar(50) NOT NULL COMMENT '供应商名称',
  `skus` varchar(255) NOT NULL COMMENT '参加活动的商品sku，用,隔开',
  `sort` int(11) NOT NULL COMMENT '排序字段,数字越大越靠前',
  `cid` int(11) NOT NULL COMMENT '活动类型1.普通活动，2.急速拍 3.团拍',
  `price_lowest` decimal(10,4) NOT NULL COMMENT '起拍价',
  `price_cap_suggest` decimal(10,4) NOT NULL COMMENT '封顶价',
  `price_increase` decimal(10,4) NOT NULL COMMENT '加价幅度',
  `price_now` decimal(10,4) NOT NULL COMMENT '现价',
  `auction_time_no` varchar(64) NOT NULL DEFAULT '' COMMENT '活动场次编码',
  `auction_status` smallint(6) NOT NULL COMMENT '活动状态 1未开始 2进行中 3已结束',
  `reviewer` varchar(128) NOT NULL DEFAULT '' COMMENT '审核人',
  `review_status` smallint(6) NOT NULL COMMENT '审核状态 1审核中 2未通过 3通过',
  `review_feedback` varchar(128) NOT NULL DEFAULT '' COMMENT '活动审核反馈',
  `begin_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime NOT NULL COMMENT '结束时间',
  `expire_hour` int(11) DEFAULT '0' COMMENT '持续时间（小时）',
  `expire_minute` int(11) DEFAULT '0' COMMENT '持续时间（分钟）',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除（0正常，1删除）',
  `specification` varchar(255) DEFAULT '' COMMENT '规格参数',
  `shelf` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否上下架',
  `auction_time_checkId` int(11) NOT NULL DEFAULT '0' COMMENT '时间场次选择id',
  `apply_for_shelf` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否申请上架',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `auction_no` (`auction_no`),
  KEY `sort` (`sort`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3056 DEFAULT CHARSET=utf8mb4 COMMENT='活动表';

-- Data exporting was unselected.

-- Dumping structure for table jjg-test.auction_record
CREATE TABLE IF NOT EXISTS `auction_record` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `auction_record_no` varchar(64) NOT NULL DEFAULT '' COMMENT '活动加价记录',
  `user_no` varchar(64) NOT NULL DEFAULT '' COMMENT '用户编号',
  `auction_no` varchar(128) DEFAULT '' COMMENT '活动编号',
  `record_status` smallint(6) DEFAULT NULL COMMENT '状态 0交易成功 1领先 2出局 3同价待退款 ',
  `amount_auction_status` smallint(6) DEFAULT NULL COMMENT '加价金额退款状态 0未退款 1已退款',
  `pay_status` smallint(6) NOT NULL DEFAULT '0' COMMENT '交易状态 0付款成功 1未付款',
  `amount_auction` decimal(10,4) DEFAULT '0.0000' COMMENT '加价金额',
  `pay_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '付款时间',
  `payment_channel` smallint(6) NOT NULL COMMENT '付款渠道 1.零钱支付。2.微信app支付3.微信H5支付',
  `pay_id` varchar(255) DEFAULT NULL COMMENT '商户号订单id',
  `wechat_order_id` varchar(255) DEFAULT NULL COMMENT '微信订单id',
  `sku_no` varchar(50) DEFAULT NULL COMMENT 'sku',
  `address_no` varchar(64) DEFAULT '' COMMENT '地址编码',
  `postage` decimal(10,4) DEFAULT '0.0000' COMMENT '出价支付邮费',
  `postage_status` smallint(6) DEFAULT '0' COMMENT '支付邮费退款状态 0未退款 1已退款',
  `auction_times` int(11) DEFAULT NULL COMMENT '此活动第几次付款',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `auction_record_no` (`auction_record_no`),
  KEY `auction_no` (`auction_no`) USING BTREE,
  KEY `user_id` (`user_no`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=508 DEFAULT CHARSET=utf8mb4 COMMENT='活动竞价记录表';

-- Data exporting was unselected.

-- Dumping structure for table jjg-test.auction_reward_record
CREATE TABLE IF NOT EXISTS `auction_reward_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `reward_record_no` varchar(64) NOT NULL DEFAULT '' COMMENT '活动奖励记录编码',
  `user_no` varchar(64) NOT NULL DEFAULT '' COMMENT '用户编号',
  `show_user_no` varchar(128) DEFAULT '' COMMENT '展示用户编号，用户编号的直属下级，出价人的上级编号',
  `bid_no` varchar(64) NOT NULL DEFAULT '' COMMENT '出价人编码',
  `auction_no` varchar(128) DEFAULT '' COMMENT '活动编号',
  `auction_record_no` varchar(64) DEFAULT '' COMMENT '活动出价记录编码',
  `show_account` varchar(64) NOT NULL DEFAULT '' COMMENT '展示人员编号',
  `reward_type` smallint(6) DEFAULT NULL COMMENT '收益类型 1出价者奖励 3.VIP1_1奖励,4.VIP1_2奖励5.VIP1_3奖励6.VIP2奖励',
  `reward_proportion` decimal(10,4) DEFAULT NULL COMMENT '奖励比例（存小数）',
  `cash_withdrawal_amount` decimal(10,4) DEFAULT '0.0000' COMMENT '可提现金额',
  `expected_reward` decimal(10,4) DEFAULT '0.0000' COMMENT '预期奖励',
  `type` smallint(6) DEFAULT NULL COMMENT '奖励类型（1.预期奖励-加价，2.预期奖励-发货后15天，3.预期奖励-退款，4.可提奖励-发货后15天）',
  `level` int(11) DEFAULT NULL COMMENT '粉丝等级',
  `income_amount` decimal(10,4) DEFAULT NULL COMMENT '收益金额',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `reward_record_no` (`reward_record_no`),
  KEY `user_id` (`user_no`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=124 DEFAULT CHARSET=utf8mb4 COMMENT='活动奖励记录表\r\n ';

-- Data exporting was unselected.

-- Dumping structure for table jjg-test.auction_template
CREATE TABLE IF NOT EXISTS `auction_template` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增长ID',
  `auction_template_no` varchar(64) NOT NULL DEFAULT '' COMMENT '活动模板编号',
  `item_no` varchar(64) NOT NULL DEFAULT '' COMMENT '商品编号',
  `item_pic` varchar(1024) NOT NULL DEFAULT '' COMMENT '商品主图',
  `skus` varchar(255) NOT NULL COMMENT '参加活动的商品sku，用,隔开',
  `sort` int(11) NOT NULL COMMENT '排序字段',
  `price_lowest` decimal(10,2) NOT NULL COMMENT '起拍价',
  `price_cap_suggest` decimal(10,2) NOT NULL COMMENT '封顶价',
  `price_increase` decimal(10,2) NOT NULL COMMENT '加价幅度',
  `expire_hour` int(11) NOT NULL COMMENT '持续时间：时',
  `expire_minute` int(11) NOT NULL COMMENT '持续时间：分',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COMMENT='活动模板表';

-- Data exporting was unselected.

-- Dumping structure for table jjg-test.auction_time
CREATE TABLE IF NOT EXISTS `auction_time` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `auction_time_no` varchar(64) NOT NULL DEFAULT '' COMMENT '活动场次时间编码',
  `begin_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime NOT NULL COMMENT '结束时间',
  `auction_time_status` smallint(6) NOT NULL COMMENT '活动场次状态1正在抢购 2即将开始 3当前活动',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `auction_time_no` (`auction_time_no`)
) ENGINE=InnoDB AUTO_INCREMENT=179 DEFAULT CHARSET=utf8mb4 COMMENT='活动场次时间表';

-- Data exporting was unselected.

-- Dumping structure for table jjg-test.auction_time_check
CREATE TABLE IF NOT EXISTS `auction_time_check` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增长ID',
  `begin_time` time NOT NULL COMMENT '开始时间',
  `expired_hour` int(11) NOT NULL COMMENT '期限（小时）',
  `expired_minute` int(11) NOT NULL COMMENT '期限（分）',
  `time_status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态1启用0禁用',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除（0正常，1删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=81 DEFAULT CHARSET=utf8mb4 COMMENT='活动场次选择表';

-- Data exporting was unselected.

-- Dumping structure for table jjg-test.auction_time_check_snapshot
CREATE TABLE IF NOT EXISTS `auction_time_check_snapshot` (
  `id` int(11) NOT NULL COMMENT '活动场次选择id',
  `snapshot_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增长ID',
  `is_newest` tinyint(1) NOT NULL COMMENT '是否当前正在使用的快照',
  `begin_time` datetime NOT NULL COMMENT '开始时间',
  `expired_hour` int(10) NOT NULL COMMENT '期限（小时）',
  `expired_minute` int(10) NOT NULL COMMENT '期限（分）',
  `time_status` tinyint(1) NOT NULL COMMENT '状态1启用2禁用',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '快照创建时间',
  PRIMARY KEY (`snapshot_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='活动场次选择表(快照)';

-- Data exporting was unselected.

-- Dumping structure for table jjg-test.auction_video
CREATE TABLE IF NOT EXISTS `auction_video` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `auction_video_no` varchar(128) NOT NULL COMMENT '活动视频编号',
  `user_no` varchar(128) NOT NULL COMMENT '用户编号，视频上传的人',
  `video_desc` varchar(200) NOT NULL COMMENT '视频描述',
  `video_pic` varchar(200) NOT NULL COMMENT '视频主图',
  `video_reduce_pic` varchar(200) NOT NULL COMMENT '视频缩略图',
  `video_link` varchar(200) DEFAULT NULL COMMENT '视频链接',
  `video_link_name` varchar(200) DEFAULT NULL COMMENT '视频链接名称',
  `video_url` varchar(200) DEFAULT NULL COMMENT '视频地址',
  `video_tips` varchar(200) DEFAULT NULL COMMENT '视频提示',
  `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序字段，数字越大越靠前',
  `display` int(1) NOT NULL DEFAULT '1' COMMENT '是否显示1显示，0不显示',
  `type` int(1) NOT NULL DEFAULT '1' COMMENT '类型1.活动2.自定义页面3.无连接',
  `reviewer_no` varchar(128) DEFAULT NULL COMMENT '审核人编号',
  `review_status` int(1) NOT NULL DEFAULT '1' COMMENT '审核状态 1待审核 2未通过 3通过',
  `review_reason` varchar(128) DEFAULT NULL COMMENT '活动审核原因',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `deleted` int(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除（0正常，1删除）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `auction_video_no` (`auction_video_no`),
  KEY `user_no` (`user_no`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='活动视频';

-- Data exporting was unselected.

-- Dumping structure for table jjg-test.auction_video_comment
CREATE TABLE IF NOT EXISTS `auction_video_comment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `video_comment_no` varchar(128) NOT NULL COMMENT '评论编号',
  `video_comment_pno` varchar(128) DEFAULT '0' COMMENT '评论编号的上级',
  `auction_video_no` varchar(128) NOT NULL COMMENT '活动视频编号',
  `user_no` varchar(128) NOT NULL COMMENT '当前评论人的用户编号',
  `reply_user_no` varchar(128) DEFAULT NULL COMMENT '回复人的编号',
  `level` int(11) DEFAULT NULL COMMENT '编号等级',
  `comment` varchar(200) NOT NULL COMMENT '评论内容',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `deleted` int(11) NOT NULL DEFAULT '0' COMMENT '逻辑删除（0正常，1删除）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `video_comment_no` (`video_comment_no`),
  KEY `auction_video_no` (`auction_video_no`),
  KEY `video_comment_pno` (`video_comment_pno`)
) ENGINE=InnoDB AUTO_INCREMENT=147 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='活动视频评论表';

-- Data exporting was unselected.

-- Dumping structure for table jjg-test.item
CREATE TABLE IF NOT EXISTS `item` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `item_no` varchar(64) DEFAULT '' COMMENT '商品编号',
  `item_name` varchar(128) NOT NULL COMMENT '商品名称',
  `cid` int(11) NOT NULL COMMENT '商品分类的子类目编号',
  `item_desc` varchar(64) NOT NULL DEFAULT '' COMMENT '商品描述',
  `pic_urls` varchar(1024) NOT NULL COMMENT '商品图片地址，数组，以,分割 ',
  `brand` varchar(64) NOT NULL DEFAULT '' COMMENT '品牌',
  `price_lowest` decimal(10,4) NOT NULL COMMENT '最低成交价',
  `price_cap_suggest` decimal(10,4) NOT NULL DEFAULT '0.0000' COMMENT '建议封顶价',
  `supplier_no` varchar(50) NOT NULL COMMENT '供应商编号',
  `shelf_life` int(11) NOT NULL COMMENT '保质期',
  `shelf_life_type` smallint(6) NOT NULL DEFAULT '2' COMMENT '保质期单位，默认为月',
  `item_content` text NOT NULL COMMENT '商品详情',
  `shelf` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否上架 1是 0否',
  `stock` int(11) NOT NULL DEFAULT '0' COMMENT '总库存',
  `sales_volume` int(11) NOT NULL DEFAULT '0' COMMENT '销量',
  `deleted` tinyint(1) NOT NULL COMMENT '逻辑删除',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `item_item_no_uindex` (`item_no`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=203 DEFAULT CHARSET=utf8mb4 COMMENT='商品表';

-- Data exporting was unselected.

-- Dumping structure for table jjg-test.item_category
CREATE TABLE IF NOT EXISTS `item_category` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `cname` varchar(64) NOT NULL COMMENT '分类名称',
  `pid` int(11) NOT NULL COMMENT '上级分类id',
  `sort` int(11) NOT NULL COMMENT '排序字段，数字越大越靠前',
  `display` tinyint(1) NOT NULL COMMENT '是否显示1显示，0不显示',
  `level` int(11) NOT NULL DEFAULT '0' COMMENT '等级',
  `desc` varchar(512) NOT NULL DEFAULT '' COMMENT '分类描述',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=62 DEFAULT CHARSET=utf8mb4 COMMENT='商品分类表';

-- Data exporting was unselected.

-- Dumping structure for table jjg-test.item_sku
CREATE TABLE IF NOT EXISTS `item_sku` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `sku_no` varchar(128) NOT NULL DEFAULT '' COMMENT '唯一编码，商品sku编号',
  `item_no` varchar(64) NOT NULL DEFAULT '' COMMENT '商品编号',
  `supplier_no` varchar(64) NOT NULL COMMENT '供应商账号',
  `img_url` varchar(255) NOT NULL DEFAULT '' COMMENT '图片地址',
  `properties` varchar(1024) NOT NULL COMMENT '商品规格 格式：kid[0]-vid[0],kid[1]-vid[1]...kid[n]-vid[n]   例如：20000-3275069,1753146-3485013',
  `shipping_code` varchar(255) NOT NULL COMMENT '发货编码',
  `quantity` int(11) NOT NULL COMMENT '库存',
  `deleted` tinyint(1) NOT NULL COMMENT '逻辑删除',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `sku_no` (`sku_no`)
) ENGINE=InnoDB AUTO_INCREMENT=474 DEFAULT CHARSET=utf8mb4 COMMENT='商品规格表';

-- Data exporting was unselected.

-- Dumping structure for table jjg-test.item_sku_property
CREATE TABLE IF NOT EXISTS `item_sku_property` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '属性编号',
  `name` varchar(32) NOT NULL COMMENT '属性文本',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=149 DEFAULT CHARSET=utf8mb4 COMMENT='规格属性表';

-- Data exporting was unselected.

-- Dumping structure for table jjg-test.item_sku_property_key_reference
CREATE TABLE IF NOT EXISTS `item_sku_property_key_reference` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '属性编号',
  `supplier_no` varchar(64) NOT NULL COMMENT '供应商账户',
  `key_id` int(11) NOT NULL COMMENT '属性键编号',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `account_id` (`supplier_no`) USING BTREE,
  KEY `key_id` (`key_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=58 DEFAULT CHARSET=utf8mb4 COMMENT='规格名引用表';

-- Data exporting was unselected.

-- Dumping structure for table jjg-test.item_sku_property_value_reference
CREATE TABLE IF NOT EXISTS `item_sku_property_value_reference` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '属性编号',
  `supplier_no` varchar(64) NOT NULL COMMENT '供应商编号',
  `reference_id` varchar(32) NOT NULL COMMENT '关联编号',
  `key_id` int(11) NOT NULL COMMENT '属性键编号',
  `value_id` int(11) NOT NULL COMMENT '属性值编号',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=102 DEFAULT CHARSET=utf8mb4 COMMENT='规格值引用表';

-- Data exporting was unselected.

-- Dumping structure for table jjg-test.item_sku_snapshot
CREATE TABLE IF NOT EXISTS `item_sku_snapshot` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `sku_snapshot_no` varchar(64) NOT NULL DEFAULT '' COMMENT '快照编码',
  `item_no` varchar(64) NOT NULL DEFAULT '' COMMENT '商品编号',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '快照创建时间',
  `is_newest` tinyint(1) NOT NULL COMMENT '是否当前正在使用的快照',
  `sku_no` varchar(64) NOT NULL DEFAULT '' COMMENT 'sku编号',
  `supplier_no` varchar(64) NOT NULL DEFAULT '' COMMENT '供应商账号',
  `img_url` varchar(255) NOT NULL DEFAULT '' COMMENT '图片地址',
  `properties` varchar(1024) NOT NULL COMMENT '商品规格 格式：kid[0]-vid[0],kid[1]-vid[1]...kid[n]-vid[n]   例如：20000-3275069,1753146-3485013',
  `shipping_code` varchar(255) NOT NULL COMMENT '发货编码',
  `deleted` tinyint(1) NOT NULL COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `sku_snapshot_no` (`sku_snapshot_no`)
) ENGINE=InnoDB AUTO_INCREMENT=369 DEFAULT CHARSET=utf8mb4 COMMENT='商品sku快照';

-- Data exporting was unselected.

-- Dumping structure for table jjg-test.item_snapshot
CREATE TABLE IF NOT EXISTS `item_snapshot` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '快照编号',
  `item_snapshot_no` varchar(64) NOT NULL DEFAULT '' COMMENT '商品快照编码',
  `is_newest` tinyint(1) NOT NULL COMMENT '是否当前商品正在使用的快照',
  `item_no` varchar(64) NOT NULL DEFAULT '' COMMENT '商品编码',
  `item_name` varchar(128) NOT NULL COMMENT '商品名称',
  `supplier_no` varchar(64) NOT NULL DEFAULT '' COMMENT '供应商编码',
  `cid` int(11) NOT NULL COMMENT '商品分类的子类目编号',
  `pic_urls` varchar(1024) NOT NULL COMMENT '商品图片地址，数组，以,分割 ',
  `price_lowest` decimal(10,2) NOT NULL COMMENT '最低成交价',
  `price_cap_suggest` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '建议封顶价',
  `shelf_life` int(11) NOT NULL COMMENT '保质期',
  `shelf_life_type` smallint(6) NOT NULL DEFAULT '2' COMMENT '保质期单位，默认为月',
  `item_content` text NOT NULL COMMENT '商品详情',
  `shelf` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否上架 1是 0否',
  `item_desc` varchar(64) NOT NULL DEFAULT '' COMMENT '商品描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '快照生成时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `item_snapshot_no` (`item_snapshot_no`)
) ENGINE=InnoDB AUTO_INCREMENT=326 DEFAULT CHARSET=utf8mb4 COMMENT='商品快照';

-- Data exporting was unselected.

-- Dumping structure for table jjg-test.persistent_logins
CREATE TABLE IF NOT EXISTS `persistent_logins` (
  `username` varchar(64) NOT NULL,
  `series` varchar(64) NOT NULL,
  `token` varchar(64) NOT NULL,
  `last_used` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`series`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Data exporting was unselected.

-- Dumping structure for table jjg-test.sys_account_message
CREATE TABLE IF NOT EXISTS `sys_account_message` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `msg_no` varchar(128) NOT NULL COMMENT '消息编号',
  `user_no` varchar(128) NOT NULL DEFAULT '' COMMENT '用户编号',
  `msg_status` smallint(6) NOT NULL COMMENT '消息状态0.已读，1未读',
  `msg_type` smallint(6) NOT NULL COMMENT '消息类型1.交易2.服务商3.物流4.系统',
  `msg_content` varchar(255) NOT NULL COMMENT '消息内容',
  `msg_title` varchar(255) NOT NULL COMMENT '消息标题',
  `auction_no` varchar(128) DEFAULT NULL COMMENT '活动编号',
  `shipment_number` varchar(128) DEFAULT NULL COMMENT '物流单号',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `account_id` (`msg_type`) USING BTREE,
  KEY `msg_no` (`msg_no`) USING BTREE,
  KEY `user_no` (`user_no`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=752 DEFAULT CHARSET=utf8mb4 COMMENT='用户消息推送';

-- Data exporting was unselected.

-- Dumping structure for table jjg-test.sys_areas
CREATE TABLE IF NOT EXISTS `sys_areas` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '地区自增长id',
  `area_id` varchar(6) NOT NULL DEFAULT '0' COMMENT '区域编码',
  `city_id` varchar(6) NOT NULL DEFAULT '0',
  `area` varchar(50) NOT NULL DEFAULT '0' COMMENT '区域名称',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `area_code` (`area_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3725 DEFAULT CHARSET=utf8 COMMENT='省市表';

-- Data exporting was unselected.

-- Dumping structure for table jjg-test.sys_auction_leaderboard
CREATE TABLE IF NOT EXISTS `sys_auction_leaderboard` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_no` varchar(64) NOT NULL DEFAULT '' COMMENT '用户编号',
  `thirty_day_bids` int(11) DEFAULT '0' COMMENT '自己30天出价次数',
  `core_fans_thirty_day_bids` int(11) DEFAULT '0' COMMENT '核心粉丝30天出价次数',
  `thirty_day_bid_count` int(11) DEFAULT '0' COMMENT '三十天出价次数总和',
  `thirty_days_on_the_list` int(11) DEFAULT '0' COMMENT '三十天上榜次数',
  `recording_date` date DEFAULT NULL COMMENT '记录时间（天）',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `rank` int(11) DEFAULT NULL COMMENT '排名',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `user_no` (`user_no`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1465 DEFAULT CHARSET=utf8mb4 COMMENT='加加购排行榜明细';

-- Data exporting was unselected.

-- Dumping structure for table jjg-test.sys_auction_leaderboard_detail
CREATE TABLE IF NOT EXISTS `sys_auction_leaderboard_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_no` varchar(64) NOT NULL DEFAULT '' COMMENT '用户编号',
  `bid_times` int(11) NOT NULL COMMENT '自己出价次数',
  `core_fans_bid_times` int(11) NOT NULL COMMENT '核心粉丝出价次数',
  `total_bid_times` int(11) NOT NULL COMMENT '出价次数总和',
  `statistical_date` date NOT NULL COMMENT '统计日期',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=84 DEFAULT CHARSET=utf8mb4 COMMENT='加加购排行榜明细';

-- Data exporting was unselected.

-- Dumping structure for table jjg-test.sys_banner
CREATE TABLE IF NOT EXISTS `sys_banner` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `banner_img_url` varchar(512) NOT NULL COMMENT 'banner图片URL',
  `banner_url` varchar(512) NOT NULL COMMENT 'banner链接',
  `target_link` varchar(512) NOT NULL DEFAULT '' COMMENT '目标链接',
  `type` smallint(10) NOT NULL COMMENT '类型1.首页2.个人中心',
  `banner_name` varchar(64) NOT NULL COMMENT '名称',
  `banner_sort` int(11) NOT NULL COMMENT '排序值，数值越大的排前面',
  `display` tinyint(1) NOT NULL COMMENT '是否显示0显示，1显示',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COMMENT='banner控制表';

-- Data exporting was unselected.

-- Dumping structure for table jjg-test.sys_cities
CREATE TABLE IF NOT EXISTS `sys_cities` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `province_id` varchar(20) NOT NULL,
  `city_id` varchar(20) NOT NULL,
  `city` varchar(50) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=346 DEFAULT CHARSET=utf8 COMMENT='行政区域地州市信息表';

-- Data exporting was unselected.

-- Dumping structure for table jjg-test.sys_dictionary
CREATE TABLE IF NOT EXISTS `sys_dictionary` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `data_key` varchar(100) NOT NULL DEFAULT '' COMMENT '数据组key',
  `data_name` varchar(100) NOT NULL DEFAULT '' COMMENT '数据字典名称',
  `data_value` varchar(80) NOT NULL DEFAULT '' COMMENT '数据值',
  `data_desc` varchar(400) DEFAULT NULL COMMENT '数据描述',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `data_name` (`data_name`) USING BTREE,
  KEY `data_key` (`data_key`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COMMENT='数据字典';

-- Data exporting was unselected.

-- Dumping structure for table jjg-test.sys_permissions
CREATE TABLE IF NOT EXISTS `sys_permissions` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `permission` int(11) NOT NULL COMMENT '权限编号',
  `description` varchar(64) NOT NULL COMMENT '权限描述',
  `rid` varchar(32) DEFAULT '' COMMENT '此权限关联角色',
  `available` tinyint(1) DEFAULT '0' COMMENT '是否锁定',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限表';

-- Data exporting was unselected.

-- Dumping structure for table jjg-test.sys_permission_role
CREATE TABLE IF NOT EXISTS `sys_permission_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `role_id` int(11) NOT NULL COMMENT '角色编号',
  `permission_id` int(11) NOT NULL COMMENT '权限编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COMMENT='权限-角色关联表';

-- Data exporting was unselected.

-- Dumping structure for table jjg-test.sys_provinces
CREATE TABLE IF NOT EXISTS `sys_provinces` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `province_id` varchar(20) NOT NULL,
  `province` varchar(50) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8 COMMENT='省份信息表';

-- Data exporting was unselected.

-- Dumping structure for table jjg-test.sys_reward_parameter
CREATE TABLE IF NOT EXISTS `sys_reward_parameter` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `reward_type` varchar(50) NOT NULL COMMENT '奖励类型名称',
  `reward_type_level` smallint(6) DEFAULT '1' COMMENT '奖励类型等级1.出价者，3.VIP1_1，4.VIP1_2, 5.VIP1_3,  6.VIP2',
  `reward_proportion` decimal(10,4) NOT NULL COMMENT '奖励比例（存小数）',
  `remarks` varchar(64) DEFAULT NULL COMMENT '描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COMMENT='奖励参数表';

-- Data exporting was unselected.

-- Dumping structure for table jjg-test.sys_roles
CREATE TABLE IF NOT EXISTS `sys_roles` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `role` varchar(32) NOT NULL DEFAULT '' COMMENT '角色名称',
  `description` varchar(64) NOT NULL DEFAULT '' COMMENT '角色描述',
  `pid` int(11) NOT NULL DEFAULT '0' COMMENT '父节点',
  `type` smallint(6) NOT NULL DEFAULT '0' COMMENT '用户角色类型 0运营 1供应商 2前台角色',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- Data exporting was unselected.

-- Dumping structure for table jjg-test.sys_user
CREATE TABLE IF NOT EXISTS `sys_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_no` varchar(128) NOT NULL DEFAULT '' COMMENT '用户编号',
  `phone_number` varchar(64) NOT NULL DEFAULT '' COMMENT '手机号',
  `password` varchar(255) DEFAULT NULL COMMENT '密码',
  `salt` varchar(128) DEFAULT NULL COMMENT '盐值',
  `locked` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否锁定',
  `last_login_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后一次登录时间',
  `last_login_ip` varchar(64) NOT NULL DEFAULT '' COMMENT '最后一次登录ip',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `source` smallint(6) NOT NULL DEFAULT '0' COMMENT '用户来源',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `user_no` (`user_no`) USING BTREE,
  KEY `phone_number` (`phone_number`)
) ENGINE=InnoDB AUTO_INCREMENT=131464 DEFAULT CHARSET=utf8mb4 COMMENT='后台用户信息表';

-- Data exporting was unselected.

-- Dumping structure for table jjg-test.sys_users_roles
CREATE TABLE IF NOT EXISTS `sys_users_roles` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_role_no` varchar(64) NOT NULL DEFAULT '' COMMENT '用户角色关联编号',
  `user_no` varchar(64) NOT NULL DEFAULT '' COMMENT '用户编号',
  `role_id` int(11) NOT NULL COMMENT '角色编号',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `user_no` (`user_no`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=75027 DEFAULT CHARSET=utf8mb4 COMMENT='用户-角色关联表';

-- Data exporting was unselected.

-- Dumping structure for table jjg-test.sys_user_account
CREATE TABLE IF NOT EXISTS `sys_user_account` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增长ID',
  `user_no` varchar(64) NOT NULL DEFAULT '' COMMENT '用户编号',
  `account_nickname` varchar(50) NOT NULL COMMENT '用户昵称',
  `account_gender` smallint(6) NOT NULL DEFAULT '0' COMMENT '用户性别 0未知 1男 2女',
  `account_avatar` varchar(255) DEFAULT NULL COMMENT '用户头像',
  `id_card` varchar(128) DEFAULT '' COMMENT '身份证号',
  `invitation_code` varchar(64) DEFAULT NULL COMMENT '粉丝邀请二维码',
  `cumulative_reward` decimal(10,4) DEFAULT '0.0000' COMMENT '累计奖励',
  `cash_withdrawal_amount` decimal(10,4) DEFAULT '0.0000' COMMENT '可提现金额',
  `expected_reward` decimal(10,4) DEFAULT '0.0000' COMMENT '预期奖励',
  `apply_password` varchar(255) DEFAULT '' COMMENT '支付密码',
  `price_increase` int(11) NOT NULL DEFAULT '0' COMMENT '个人加价次数',
  `refund_times` int(11) NOT NULL DEFAULT '0' COMMENT '个人退款次数',
  `union_id` varchar(128) DEFAULT NULL COMMENT 'unionId',
  `getui_id` varchar(64) DEFAULT NULL COMMENT '个推id',
  `call_number` int(11) NOT NULL DEFAULT '0' COMMENT '拨打电话次数',
  `call_time` datetime DEFAULT NULL COMMENT '拨打电话时间',
  `service_provider_number` int(11) DEFAULT NULL COMMENT 'VIP1数量',
  `service_provider_level` smallint(6) DEFAULT '1' COMMENT '服务商等级（暂无使用）',
  `qualification_expires` datetime DEFAULT NULL COMMENT '服务商资格到期时间（暂无使用）',
  `qualification_time` datetime DEFAULT NULL COMMENT '服务商开始时间（暂无使用）',
  `service_provider_reward` decimal(10,4) NOT NULL DEFAULT '0.0000' COMMENT 'vip1奖励 -- 作废',
  `commission_for_provider` decimal(10,4) NOT NULL DEFAULT '0.0000' COMMENT '从VIP1中得到的奖励 -- 作废',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `user_no` (`user_no`) USING BTREE,
  UNIQUE KEY `invitation_code` (`invitation_code`)
) ENGINE=InnoDB AUTO_INCREMENT=131454 DEFAULT CHARSET=utf8mb4 COMMENT='后台用户信息-用户拓展表';

-- Data exporting was unselected.

-- Dumping structure for table jjg-test.sys_user_address
CREATE TABLE IF NOT EXISTS `sys_user_address` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增长ID',
  `address_no` varchar(64) NOT NULL DEFAULT '' COMMENT '收货地址编号',
  `user_no` varchar(64) NOT NULL DEFAULT '' COMMENT '用户编号',
  `province` varchar(20) NOT NULL COMMENT '省',
  `city` varchar(20) NOT NULL COMMENT '市',
  `area` varchar(20) NOT NULL COMMENT '区',
  `address` varchar(128) NOT NULL COMMENT '详细地址',
  `receiver` varchar(20) NOT NULL COMMENT '收货人',
  `receiver_phone` varchar(20) NOT NULL COMMENT '收货人手机号',
  `default_address` tinyint(1) NOT NULL COMMENT '是否默认地址（1默认，0不是）',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除（0正常，1删除）',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `address_no` (`address_no`),
  KEY `user_no` (`user_no`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=72 DEFAULT CHARSET=utf8mb4 COMMENT='用户收货地址表';

-- Data exporting was unselected.

-- Dumping structure for table jjg-test.sys_user_blacklist
CREATE TABLE IF NOT EXISTS `sys_user_blacklist` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_no` varchar(64) NOT NULL DEFAULT '' COMMENT '用户编号',
  `grey` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否灰名单用户',
  `grey_time` datetime DEFAULT NULL COMMENT '成为灰名单用户时间',
  `grey_reason` varchar(64) DEFAULT NULL COMMENT '成为灰名单用户原因',
  `black` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否黑名单用户',
  `black_time` datetime DEFAULT NULL COMMENT '成为黑名单用户时间',
  `black_reason` varchar(64) DEFAULT NULL COMMENT '成为黑名单用户原因',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COMMENT='用户黑名单表';

-- Data exporting was unselected.

-- Dumping structure for table jjg-test.sys_user_collection
CREATE TABLE IF NOT EXISTS `sys_user_collection` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增长ID',
  `user_no` varchar(64) NOT NULL DEFAULT '' COMMENT '用户编号',
  `auction_no` varchar(64) NOT NULL DEFAULT '' COMMENT '活动编号',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除（0正常，1删除）',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `user_id` (`user_no`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=112 DEFAULT CHARSET=utf8mb4 COMMENT='用户收藏表';

-- Data exporting was unselected.

-- Dumping structure for table jjg-test.sys_user_operator
CREATE TABLE IF NOT EXISTS `sys_user_operator` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_no` varchar(64) NOT NULL DEFAULT '' COMMENT '用户编号',
  `user_name` varchar(64) NOT NULL COMMENT '运营人员姓名',
  `user_desc` varchar(64) DEFAULT NULL COMMENT '运营人员描述',
  `supplier_name` varchar(64) NOT NULL DEFAULT '心动了' COMMENT '供应商名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8mb4 COMMENT='后台用户信息-运营人员账户拓展表';

-- Data exporting was unselected.

-- Dumping structure for table jjg-test.sys_user_relationship
CREATE TABLE IF NOT EXISTS `sys_user_relationship` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增长ID',
  `relationship_no` varchar(128) NOT NULL DEFAULT '' COMMENT '关系编号',
  `user_no` varchar(64) NOT NULL DEFAULT '' COMMENT '用户编号',
  `user_pno` varchar(64) NOT NULL DEFAULT '' COMMENT '用户上级编号',
  `type` int(11) NOT NULL COMMENT '用户和pid间的关系类型（1.上级，2.vip1，3.vip2）',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `relationship_no` (`relationship_no`),
  UNIQUE KEY `user_no` (`user_no`,`type`)
) ENGINE=InnoDB AUTO_INCREMENT=16854 DEFAULT CHARSET=utf8mb4 COMMENT='后台用户关系表';

-- Data exporting was unselected.

-- Dumping structure for table jjg-test.sys_user_supplier
CREATE TABLE IF NOT EXISTS `sys_user_supplier` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_no` varchar(64) NOT NULL DEFAULT '' COMMENT '用户编号',
  `supplier_name` varchar(128) NOT NULL COMMENT '供应商名称',
  `item_category` varchar(64) NOT NULL COMMENT '商品品类',
  `supplier_brand` varchar(64) NOT NULL COMMENT '供应商品牌',
  `contact` varchar(64) NOT NULL COMMENT '联系人',
  `id_card` varchar(128) NOT NULL DEFAULT '' COMMENT '供应商身份证号',
  `bank_card_number` varchar(128) NOT NULL DEFAULT '' COMMENT '供应商卡号',
  `enable` tinyint(1) NOT NULL DEFAULT '1' COMMENT '启用/禁用',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `user_no` (`user_no`)
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=utf8mb4 COMMENT='后台用户信息-供应商账户拓展表';

-- Data exporting was unselected.

-- Dumping structure for table jjg-test.sys_user_withdraw
CREATE TABLE IF NOT EXISTS `sys_user_withdraw` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `withdraw_no` varchar(64) NOT NULL DEFAULT '' COMMENT '提现编号',
  `user_no` varchar(64) NOT NULL DEFAULT '' COMMENT '用户编号',
  `application_withdrawal_date` datetime NOT NULL COMMENT '申请提现日期',
  `withdrawal_amount` decimal(10,4) NOT NULL COMMENT '申请提现金额',
  `withdrawal_amount_before` decimal(10,4) NOT NULL COMMENT '提现前金额',
  `withdraw_status` smallint(6) NOT NULL COMMENT '提现到账状态1审核中 2审核通过 3审核不通过',
  `merchant_order_number` varchar(128) DEFAULT '' COMMENT '商户订单号',
  `wechat_payment_no` varchar(128) DEFAULT '' COMMENT '微信付款单号',
  `pay_success_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '付款成功时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `user_no` (`user_no`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COMMENT='app用户申请提现记录表';

-- Data exporting was unselected.

-- Dumping structure for table jjg-test.sys_version
CREATE TABLE IF NOT EXISTS `sys_version` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '版本编号',
  `product` varchar(32) NOT NULL COMMENT '加加购',
  `platform` smallint(6) NOT NULL COMMENT '平台(1:IOS 2:安卓)',
  `version` varchar(32) NOT NULL COMMENT '版本',
  `version_number` varchar(32) NOT NULL COMMENT '版本号',
  `create_address` varchar(255) NOT NULL COMMENT '创建地址',
  `version_status` smallint(6) NOT NULL COMMENT '状态 1强制更新 2提示更新 3不提示',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_description` varchar(1000) NOT NULL COMMENT '更新描述',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COMMENT='版本迭代';

-- Data exporting was unselected.

-- Dumping structure for table jjg-test.tb_userconnection
CREATE TABLE IF NOT EXISTS `tb_userconnection` (
  `userId` varchar(64) NOT NULL COMMENT '用户编号',
  `providerId` varchar(16) NOT NULL,
  `providerUserId` varchar(128) NOT NULL,
  `rank` int(11) NOT NULL,
  `displayName` varchar(255) DEFAULT NULL,
  `profileUrl` varchar(512) DEFAULT NULL,
  `imageUrl` varchar(512) DEFAULT NULL,
  `accessToken` varchar(512) NOT NULL,
  `secret` varchar(512) DEFAULT NULL,
  `refreshToken` varchar(512) DEFAULT NULL,
  `expireTime` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`providerId`,`providerUserId`,`userId`) USING BTREE,
  UNIQUE KEY `UserConnectionRank` (`userId`,`providerId`,`rank`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Data exporting was unselected.

-- Dumping structure for table jjg-test.trade_order
CREATE TABLE IF NOT EXISTS `trade_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `order_sn` varchar(128) NOT NULL COMMENT '订单编号',
  `user_no` varchar(64) NOT NULL DEFAULT '' COMMENT '用户编号',
  `auction_record_no` varchar(64) NOT NULL DEFAULT '' COMMENT '付款记录id',
  `actual_amount` decimal(10,4) NOT NULL COMMENT '实付金额',
  `total_amount` decimal(10,4) NOT NULL COMMENT '总价',
  `freight` decimal(10,4) NOT NULL COMMENT '运费 ',
  `payment_channel` smallint(6) NOT NULL COMMENT '付款渠道 1.零钱支付。2.微信app支付3.微信H5支付',
  `order_status` smallint(6) NOT NULL COMMENT '订单状态 201待发货202退款中 301待收货302退货中 401待评价 501交易成功，502交易关闭',
  `payment_time` datetime DEFAULT NULL COMMENT '付款时间',
  `delivery_time` datetime DEFAULT NULL COMMENT '发货时间',
  `logistics_company` varchar(32) DEFAULT NULL COMMENT '物流公司',
  `shipment_number` varchar(128) DEFAULT NULL COMMENT '物流单号',
  `confirm_receipt_time` datetime DEFAULT NULL COMMENT '确认收货时间',
  `remarks` varchar(64) DEFAULT NULL COMMENT '备注',
  `buyer_message` varchar(64) DEFAULT NULL COMMENT '买家留言',
  `order_type` smallint(6) NOT NULL COMMENT '订单类型 1加加购订单',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `order_sn` (`order_sn`) USING BTREE,
  KEY `user_no` (`user_no`)
) ENGINE=InnoDB AUTO_INCREMENT=287 DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- Data exporting was unselected.

-- Dumping structure for table jjg-test.trade_order_address
CREATE TABLE IF NOT EXISTS `trade_order_address` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `order_address_no` varchar(64) NOT NULL DEFAULT '' COMMENT '订单地址编码',
  `order_sn` varchar(128) NOT NULL COMMENT '订单编号',
  `province` varchar(32) NOT NULL COMMENT '省',
  `city` varchar(32) NOT NULL COMMENT '市',
  `area` varchar(32) NOT NULL COMMENT '区',
  `address` varchar(128) NOT NULL COMMENT '详细地址',
  `receiver` varchar(32) NOT NULL COMMENT '收货人',
  `receiver_phone` varchar(32) NOT NULL COMMENT '收货人手机号',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `order_address_no` (`order_address_no`),
  KEY `order_sn` (`order_sn`)
) ENGINE=InnoDB AUTO_INCREMENT=245 DEFAULT CHARSET=utf8mb4 COMMENT='订单收货地址表';

-- Data exporting was unselected.

-- Dumping structure for table jjg-test.trade_order_detail
CREATE TABLE IF NOT EXISTS `trade_order_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `order_detail_no` varchar(64) NOT NULL DEFAULT '' COMMENT '订单明细编码',
  `order_sn` varchar(128) DEFAULT NULL COMMENT '订单编号',
  `item_no` varchar(64) DEFAULT '' COMMENT '商品编码',
  `item_snapshot_no` varchar(64) DEFAULT NULL COMMENT '商品快照编码\\\\n',
  `sku_no` varchar(64) NOT NULL DEFAULT '' COMMENT '商品sku编号',
  `sku_snapshot_no` varchar(64) DEFAULT '' COMMENT 'sku快照编码',
  `item_number` int(11) DEFAULT NULL COMMENT '商品数量',
  `unit_price` decimal(10,2) DEFAULT NULL COMMENT '单价',
  `supply_no` varchar(64) NOT NULL DEFAULT '' COMMENT '供应商编码',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `order_detail_no` (`order_detail_no`),
  KEY `order_sn` (`order_sn`)
) ENGINE=InnoDB AUTO_INCREMENT=275 DEFAULT CHARSET=utf8mb4 COMMENT='交易订单明细表';

-- Data exporting was unselected.

-- Dumping structure for table jjg-test.trade_order_refund
CREATE TABLE IF NOT EXISTS `trade_order_refund` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `order_refund_no` varchar(64) NOT NULL DEFAULT '' COMMENT '订单退货信息编码',
  `order_sn` varchar(128) DEFAULT NULL COMMENT '订单编号',
  `logistics_company` varchar(32) DEFAULT '' COMMENT '物流公司',
  `shipment_number` varchar(128) DEFAULT '' COMMENT '物流单号',
  `contact_number` varchar(32) DEFAULT '' COMMENT '联系电话',
  `img_url` varchar(512) DEFAULT '' COMMENT '快递详情单图片URL',
  `refund_type` smallint(6) DEFAULT NULL COMMENT '	退款类型1退款 2退款退货',
  `review_status` smallint(6) DEFAULT '1' COMMENT '审核状态1.待审核，2审核通过，3审核拒绝，4审核通过待收货5,审核通过已收货',
  `review_penalty` smallint(6) DEFAULT NULL COMMENT '审核惩罚 1一周内不能参加加加购活动 2永远不能参加加加购活动 3质量问题，不影响参加活动',
  `review_reason` varchar(255) DEFAULT NULL COMMENT '审核原因',
  `reviewer_no` varchar(64) DEFAULT '' COMMENT '审核人',
  `refund_amount` decimal(10,4) DEFAULT '0.0000' COMMENT '退款金额',
  `refund_reason` varchar(64) DEFAULT '' COMMENT '退款原因',
  `refund_desc` varchar(64) DEFAULT '' COMMENT '退款说明',
  `return_reason` varchar(64) DEFAULT '' COMMENT '退货原因',
  `return_desc` varchar(64) DEFAULT '' COMMENT '退货说明',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `order_refund_no` (`order_refund_no`),
  KEY `order_sn` (`order_sn`)
) ENGINE=InnoDB AUTO_INCREMENT=99 DEFAULT CHARSET=utf8mb4 COMMENT='交易订单退货信息表';

-- Data exporting was unselected.

-- Dumping structure for table jjg-test.user_application_record
CREATE TABLE IF NOT EXISTS `user_application_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `application_record_no` varchar(64) NOT NULL DEFAULT '' COMMENT '供货商资格申请编码',
  `user_no` varchar(64) NOT NULL DEFAULT '' COMMENT '用户编号',
  `latest_application` tinyint(1) DEFAULT '1' COMMENT '是否最新申请',
  `review_status` smallint(6) DEFAULT NULL COMMENT '审核状态1.审核中2.审核不通过3.审核通过',
  `supplier_name` varchar(32) DEFAULT NULL COMMENT '商家名称',
  `item_category` varchar(32) DEFAULT NULL COMMENT '商品品类',
  `contact` varchar(32) DEFAULT NULL COMMENT '联系人',
  `brand` varchar(128) DEFAULT NULL COMMENT '商家品牌',
  `deleted` tinyint(1) DEFAULT NULL COMMENT '逻辑删除',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `application_record_no` (`application_record_no`),
  KEY `user_no` (`user_no`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COMMENT='供货商资格申请记录表';

-- Data exporting was unselected.

-- Dumping structure for table jjg-test.xxl_job_group
CREATE TABLE IF NOT EXISTS `xxl_job_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `app_name` varchar(64) NOT NULL COMMENT '执行器AppName',
  `title` varchar(12) NOT NULL COMMENT '执行器名称',
  `order` int(11) NOT NULL DEFAULT '0' COMMENT '排序',
  `address_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '执行器地址类型：0=自动注册、1=手动录入',
  `address_list` varchar(512) DEFAULT NULL COMMENT '执行器地址列表，多地址逗号分隔',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- Data exporting was unselected.

-- Dumping structure for table jjg-test.xxl_job_info
CREATE TABLE IF NOT EXISTS `xxl_job_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `job_group` int(11) NOT NULL COMMENT '执行器主键ID',
  `job_cron` varchar(128) NOT NULL COMMENT '任务执行CRON',
  `job_desc` varchar(255) NOT NULL,
  `add_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `author` varchar(64) DEFAULT NULL COMMENT '作者',
  `alarm_email` varchar(255) DEFAULT NULL COMMENT '报警邮件',
  `executor_route_strategy` varchar(50) DEFAULT NULL COMMENT '执行器路由策略',
  `executor_handler` varchar(255) DEFAULT NULL COMMENT '执行器任务handler',
  `executor_param` varchar(512) DEFAULT NULL COMMENT '执行器任务参数',
  `executor_block_strategy` varchar(50) DEFAULT NULL COMMENT '阻塞处理策略',
  `executor_timeout` int(11) NOT NULL DEFAULT '0' COMMENT '任务执行超时时间，单位秒',
  `executor_fail_retry_count` int(11) NOT NULL DEFAULT '0' COMMENT '失败重试次数',
  `glue_type` varchar(50) NOT NULL COMMENT 'GLUE类型',
  `glue_source` mediumtext COMMENT 'GLUE源代码',
  `glue_remark` varchar(128) DEFAULT NULL COMMENT 'GLUE备注',
  `glue_updatetime` datetime DEFAULT NULL COMMENT 'GLUE更新时间',
  `child_jobid` varchar(255) DEFAULT NULL COMMENT '子任务ID，多个逗号分隔',
  `trigger_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '调度状态：0-停止，1-运行',
  `trigger_last_time` bigint(13) NOT NULL DEFAULT '0' COMMENT '上次调度时间',
  `trigger_next_time` bigint(13) NOT NULL DEFAULT '0' COMMENT '下次调度时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

-- Data exporting was unselected.

-- Dumping structure for table jjg-test.xxl_job_lock
CREATE TABLE IF NOT EXISTS `xxl_job_lock` (
  `lock_name` varchar(50) NOT NULL COMMENT '锁名称',
  PRIMARY KEY (`lock_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Data exporting was unselected.

-- Dumping structure for table jjg-test.xxl_job_log
CREATE TABLE IF NOT EXISTS `xxl_job_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `job_group` int(11) NOT NULL COMMENT '执行器主键ID',
  `job_id` int(11) NOT NULL COMMENT '任务，主键ID',
  `executor_address` varchar(255) DEFAULT NULL COMMENT '执行器地址，本次执行的地址',
  `executor_handler` varchar(255) DEFAULT NULL COMMENT '执行器任务handler',
  `executor_param` varchar(512) DEFAULT NULL COMMENT '执行器任务参数',
  `executor_sharding_param` varchar(20) DEFAULT NULL COMMENT '执行器任务分片参数，格式如 1/2',
  `executor_fail_retry_count` int(11) NOT NULL DEFAULT '0' COMMENT '失败重试次数',
  `trigger_time` datetime DEFAULT NULL COMMENT '调度-时间',
  `trigger_code` int(11) NOT NULL COMMENT '调度-结果',
  `trigger_msg` text COMMENT '调度-日志',
  `handle_time` datetime DEFAULT NULL COMMENT '执行-时间',
  `handle_code` int(11) NOT NULL COMMENT '执行-状态',
  `handle_msg` text COMMENT '执行-日志',
  `alarm_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '告警状态：0-默认、1-无需告警、2-告警成功、3-告警失败',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `I_trigger_time` (`trigger_time`) USING BTREE,
  KEY `I_handle_code` (`handle_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=67815 DEFAULT CHARSET=utf8;

-- Data exporting was unselected.

-- Dumping structure for table jjg-test.xxl_job_logglue
CREATE TABLE IF NOT EXISTS `xxl_job_logglue` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `job_id` int(11) NOT NULL COMMENT '任务，主键ID',
  `glue_type` varchar(50) DEFAULT NULL COMMENT 'GLUE类型',
  `glue_source` mediumtext COMMENT 'GLUE源代码',
  `glue_remark` varchar(128) NOT NULL COMMENT 'GLUE备注',
  `add_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Data exporting was unselected.

-- Dumping structure for table jjg-test.xxl_job_registry
CREATE TABLE IF NOT EXISTS `xxl_job_registry` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `registry_group` varchar(255) NOT NULL,
  `registry_key` varchar(255) NOT NULL,
  `registry_value` varchar(255) NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `i_g_k_v` (`registry_group`,`registry_key`,`registry_value`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8;

-- Data exporting was unselected.

-- Dumping structure for table jjg-test.xxl_job_user
CREATE TABLE IF NOT EXISTS `xxl_job_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL COMMENT '账号',
  `password` varchar(50) NOT NULL COMMENT '密码',
  `role` tinyint(4) NOT NULL COMMENT '角色：0-普通用户、1-管理员',
  `permission` varchar(255) DEFAULT NULL COMMENT '权限：执行器ID列表，多个逗号分割',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `i_username` (`username`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- Data exporting was unselected.

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
