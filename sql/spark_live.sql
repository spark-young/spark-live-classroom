/*
 Navicat Premium Data Transfer

 Source Server         : sparkTencent
 Source Server Type    : MySQL
 Source Server Version : 50733
 Source Host           : 119.29.61.66:3306
 Source Schema         : spark_live

 Target Server Type    : MySQL
 Target Server Version : 50733
 File Encoding         : 65001

 Date: 09/06/2021 15:56:30
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tc_category
-- ----------------------------
DROP TABLE IF EXISTS `tc_category`;
CREATE TABLE `tc_category`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '分类id',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分类名称',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '分类简介',
  `pid` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '父id，0为根节点id',
  `status` tinyint(1) UNSIGNED NOT NULL DEFAULT 1 COMMENT '分类状态：1 启用 2 禁用',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tc_category
-- ----------------------------
INSERT INTO `tc_category` VALUES (1, '未分类', '默认分类，若课程未选择具体分类，则被归属为本分类', 0, 1, '2021-04-12 20:34:00', NULL);
INSERT INTO `tc_category` VALUES (2, '小学', '小学英语', 0, 1, '2021-04-16 15:03:26', NULL);
INSERT INTO `tc_category` VALUES (3, '初中', '初中英语', 0, 1, '2021-04-16 15:03:38', NULL);
INSERT INTO `tc_category` VALUES (4, '高中', '高中英语', 0, 1, '2021-04-16 15:03:50', NULL);
INSERT INTO `tc_category` VALUES (5, '大学', '四级词汇', 0, 1, '2021-04-17 00:35:39', NULL);
INSERT INTO `tc_category` VALUES (6, '职场英语', '职场常用英语', 0, 1, '2021-04-30 14:28:42', NULL);
INSERT INTO `tc_category` VALUES (7, '雅思', '雅思训练', 0, 1, '2021-04-30 14:29:01', NULL);
INSERT INTO `tc_category` VALUES (8, '托福', '托福考试训练', 0, 1, '2021-04-30 14:29:12', NULL);
INSERT INTO `tc_category` VALUES (9, '考研英语', '考研英语教学', 0, 1, '2021-04-30 14:29:25', NULL);

-- ----------------------------
-- Table structure for tc_chapter
-- ----------------------------
DROP TABLE IF EXISTS `tc_chapter`;
CREATE TABLE `tc_chapter`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '课程章节id',
  `cid` int(11) NOT NULL COMMENT '课程id',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '章节标题',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '章节内容简介',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '章节封面图片url',
  `nid` int(11) NOT NULL DEFAULT 0 COMMENT '指向同级的下一个章节id',
  `crid` int(11) NOT NULL DEFAULT 0 COMMENT '指向下级的第一个章节id',
  `section` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '具体章节信息：json格式',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '章节状态：1 启用 2 禁用',
  `sort` int(10) UNSIGNED NOT NULL DEFAULT 1 COMMENT '同一cid下的多个章节的排序树 1 为最先',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 165 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tc_chapter
-- ----------------------------
INSERT INTO `tc_chapter` VALUES (1, 3, 'A单词', 'A单词学习', NULL, 5, 2, '1', 1, 1, '2021-04-18 16:05:19', '2021-04-21 15:42:45');
INSERT INTO `tc_chapter` VALUES (2, 3, 'Ab单词', 'Ab开头单词学习', '', 3, 0, '1,1', 1, 2, '2021-04-18 16:05:47', '2021-04-21 15:43:41');
INSERT INTO `tc_chapter` VALUES (3, 3, 'Ac单词', 'Ac开头单词学习', '', 0, 4, '1,2', 1, 3, '2021-04-18 16:06:05', '2021-04-24 16:22:14');
INSERT INTO `tc_chapter` VALUES (4, 3, 'Aca单词', 'Aca开头单词', '', 0, 0, '1,2,1', 1, 4, '2021-04-18 16:06:22', '2021-04-24 15:41:51');
INSERT INTO `tc_chapter` VALUES (5, 3, 'B单词', 'B单词学习', NULL, 7, 6, '2', 1, 5, '2021-04-18 16:06:51', '2021-04-21 15:44:03');
INSERT INTO `tc_chapter` VALUES (6, 3, 'Ba单词', 'Ba单词学习', '', 0, 0, '2,1', 1, 6, '2021-04-18 16:07:10', '2021-04-25 13:28:55');
INSERT INTO `tc_chapter` VALUES (7, 3, 'C单词', 'C开头单词学习', NULL, 148, 8, '3', 1, 7, '2021-04-18 16:07:34', '2021-04-22 23:58:08');
INSERT INTO `tc_chapter` VALUES (8, 3, 'Ca单词', 'Ca开头单词学习', '', 111, 0, '3,1', 1, 8, '2021-04-18 16:08:36', '2021-04-24 19:03:06');
INSERT INTO `tc_chapter` VALUES (111, 3, 'Cb单词', 'Cb单词学习11', '', 113, 112, '1', 1, 0, '2021-04-24 19:03:05', '2021-04-24 19:03:59');
INSERT INTO `tc_chapter` VALUES (112, 3, 'Cba单词', 'Cba单词学习', '', 0, 0, '1', 1, 0, '2021-04-24 19:03:59', '2021-04-24 21:06:51');
INSERT INTO `tc_chapter` VALUES (113, 3, 'Cc单词', 'Cc单词学习', '', 0, 0, '1', 1, 0, '2021-04-24 20:13:24', NULL);
INSERT INTO `tc_chapter` VALUES (127, 2, '词法', '', '', 142, 128, '1', 1, 0, '2021-04-30 14:32:46', '2021-04-30 14:34:19');
INSERT INTO `tc_chapter` VALUES (128, 2, '名词', '', '', 132, 129, '1', 1, 0, '2021-04-30 14:34:19', '2021-04-30 14:34:54');
INSERT INTO `tc_chapter` VALUES (129, 2, '名词可数性', '', '', 130, 0, '1', 1, 0, '2021-04-30 14:34:54', '2021-04-30 14:35:09');
INSERT INTO `tc_chapter` VALUES (130, 2, '名词复数变化', '', '', 131, 0, '1', 1, 0, '2021-04-30 14:35:09', '2021-04-30 14:35:17');
INSERT INTO `tc_chapter` VALUES (131, 2, '名词所有格', '', '', 0, 0, '1', 1, 0, '2021-04-30 14:35:17', NULL);
INSERT INTO `tc_chapter` VALUES (132, 2, '代词', '', '', 138, 133, '1', 1, 0, '2021-04-30 14:35:28', '2021-04-30 14:35:43');
INSERT INTO `tc_chapter` VALUES (133, 2, '人称代词', '人！！1', '', 134, 0, '1', 1, 0, '2021-04-30 14:35:43', '2021-04-30 14:35:51');
INSERT INTO `tc_chapter` VALUES (134, 2, '物主代词', '', '', 135, 0, '1', 1, 0, '2021-04-30 14:35:51', '2021-04-30 14:36:06');
INSERT INTO `tc_chapter` VALUES (135, 2, '反身代词', '', '', 136, 0, '1', 1, 0, '2021-04-30 14:36:06', '2021-04-30 14:36:13');
INSERT INTO `tc_chapter` VALUES (136, 2, '指示代词', '', '', 137, 0, '1', 1, 0, '2021-04-30 14:36:12', '2021-04-30 14:36:20');
INSERT INTO `tc_chapter` VALUES (137, 2, '不定代词', '', '', 0, 0, '1', 1, 0, '2021-04-30 14:36:20', NULL);
INSERT INTO `tc_chapter` VALUES (138, 2, '冠词', '', '', 0, 139, '1', 1, 0, '2021-04-30 14:40:08', '2021-04-30 14:40:19');
INSERT INTO `tc_chapter` VALUES (139, 2, '不定冠词', '', '', 140, 0, '1', 1, 0, '2021-04-30 14:40:19', '2021-04-30 14:40:27');
INSERT INTO `tc_chapter` VALUES (140, 2, '定冠词基本用法', '', '', 141, 0, '1', 1, 0, '2021-04-30 14:40:27', '2021-04-30 14:40:35');
INSERT INTO `tc_chapter` VALUES (141, 2, '定冠词特殊用法', '', '', 0, 0, '1', 1, 0, '2021-04-30 14:40:35', NULL);
INSERT INTO `tc_chapter` VALUES (142, 2, '时态', '', '', 0, 143, '1', 1, 0, '2021-04-30 14:40:54', '2021-04-30 14:41:06');
INSERT INTO `tc_chapter` VALUES (143, 2, '一般现在时', '', '', 144, 0, '1', 1, 0, '2021-04-30 14:41:06', '2021-04-30 14:41:20');
INSERT INTO `tc_chapter` VALUES (144, 2, '一般过去时', '', '', 145, 0, '1', 1, 0, '2021-04-30 14:41:20', '2021-04-30 14:41:27');
INSERT INTO `tc_chapter` VALUES (145, 2, '现在进行时', '', '', 146, 0, '1', 1, 0, '2021-04-30 14:41:27', '2021-04-30 14:41:35');
INSERT INTO `tc_chapter` VALUES (146, 2, '过去进行时', '', '', 147, 0, '1', 1, 0, '2021-04-30 14:41:35', '2021-04-30 14:41:43');
INSERT INTO `tc_chapter` VALUES (147, 2, '一般将来时', '', '', 0, 0, '1', 1, 0, '2021-04-30 14:41:43', NULL);
INSERT INTO `tc_chapter` VALUES (148, 3, 'D单词', '', '', 158, 149, '1', 1, 0, '2021-04-30 15:41:09', '2021-04-30 15:41:23');
INSERT INTO `tc_chapter` VALUES (149, 3, 'Da单词', '', '', 0, 0, '1', 1, 0, '2021-04-30 15:41:22', '2021-05-15 19:57:06');
INSERT INTO `tc_chapter` VALUES (158, 3, 'E单词', '', '', 0, 0, '1', 1, 0, '2021-05-15 19:57:51', '2021-05-27 22:04:37');
INSERT INTO `tc_chapter` VALUES (163, 10, '元音', '', '', 0, 164, '1', 1, 0, '2021-05-15 20:20:28', '2021-05-15 20:23:27');
INSERT INTO `tc_chapter` VALUES (164, 10, '单元音音标', '单元音音标总介绍和口型教学', '', 0, 0, '1', 1, 0, '2021-05-15 20:23:27', NULL);

-- ----------------------------
-- Table structure for tc_chapter_root_first
-- ----------------------------
DROP TABLE IF EXISTS `tc_chapter_root_first`;
CREATE TABLE `tc_chapter_root_first`  (
  `cid` int(11) NOT NULL COMMENT '课程id',
  `chid` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '同一课程下第一个根章节点',
  PRIMARY KEY (`cid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tc_chapter_root_first
-- ----------------------------
INSERT INTO `tc_chapter_root_first` VALUES (2, 127);
INSERT INTO `tc_chapter_root_first` VALUES (3, 1);
INSERT INTO `tc_chapter_root_first` VALUES (10, 163);

-- ----------------------------
-- Table structure for tc_course
-- ----------------------------
DROP TABLE IF EXISTS `tc_course`;
CREATE TABLE `tc_course`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '课程id',
  `tid` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '开设课程的教师id',
  `type_id` int(10) UNSIGNED NOT NULL DEFAULT 1 COMMENT '课程所属分类id，默认为1 未分类',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '课程名',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '课程描述',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '课程封面图片url',
  `sub_count` int(11) NULL DEFAULT 1 COMMENT '课程可被订阅最大数量',
  `status` tinyint(1) UNSIGNED NULL DEFAULT 1 COMMENT '课程状态：1 启用 2 禁用',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  PRIMARY KEY (`id`, `tid`, `type_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tc_course
-- ----------------------------
INSERT INTO `tc_course` VALUES (1, 9, 2, '老张音标', '张老师步步音标教学', NULL, 1, 1, '2021-04-16 15:05:31', '2021-05-27 20:52:12');
INSERT INTO `tc_course` VALUES (2, 4, 3, '小杨语法', '杨老师带你走进语法的世界！', NULL, 6, 1, '2021-04-16 15:05:54', '2021-05-29 10:26:28');
INSERT INTO `tc_course` VALUES (3, 4, 3, '小杨词汇', '杨老师趣味分析各类词汇', NULL, 3, 1, '2021-04-16 15:06:12', '2021-05-29 10:26:49');
INSERT INTO `tc_course` VALUES (10, 4, 3, '小杨音标', '音标学习从入门到熟练', '', 2, 1, '2021-05-02 11:01:58', '2021-05-29 10:28:20');
INSERT INTO `tc_course` VALUES (11, 4, 5, '小杨4级冲刺', '专项题型高效专攻', '', 3, 1, '2021-05-03 11:43:54', '2021-05-29 10:28:50');
INSERT INTO `tc_course` VALUES (12, 4, 9, '考研词汇', '考研高频1000词精讲', '', 4, 1, '2021-05-03 11:45:39', '2021-05-29 10:36:22');
INSERT INTO `tc_course` VALUES (15, 4, 9, '小杨讲题', '考研英语从零基础到过线入门课程', '', 2, 1, '2021-05-15 19:20:10', '2021-05-29 10:37:01');

-- ----------------------------
-- Table structure for tc_course_selection
-- ----------------------------
DROP TABLE IF EXISTS `tc_course_selection`;
CREATE TABLE `tc_course_selection`  (
  `cid` int(11) NOT NULL COMMENT '课程id',
  `tid` int(11) NULL DEFAULT 0 COMMENT '教师id',
  `sid` int(11) NULL DEFAULT 0 COMMENT '学生id',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tc_course_selection
-- ----------------------------
INSERT INTO `tc_course_selection` VALUES (9, 4, 6, '2021-04-30 14:16:13', NULL);
INSERT INTO `tc_course_selection` VALUES (6, 4, 5, '2021-04-30 21:27:06', '2021-04-30 21:27:06');
INSERT INTO `tc_course_selection` VALUES (2, 4, 5, '2021-05-01 10:57:28', NULL);
INSERT INTO `tc_course_selection` VALUES (2, 4, 3, '2021-05-01 12:51:19', NULL);
INSERT INTO `tc_course_selection` VALUES (10, 4, 7, '2021-05-03 15:03:53', NULL);
INSERT INTO `tc_course_selection` VALUES (3, 4, 6, '2021-06-01 15:52:27', NULL);

-- ----------------------------
-- Table structure for tc_schedule
-- ----------------------------
DROP TABLE IF EXISTS `tc_schedule`;
CREATE TABLE `tc_schedule`  (
  `cid` int(11) NOT NULL COMMENT '课程id',
  `chid` int(11) NOT NULL COMMENT '课程下章节id',
  `sid` int(11) NOT NULL COMMENT '学生id',
  `tid` int(11) NOT NULL COMMENT '教师id',
  `room_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '会议房间id',
  `class_time` timestamp(0) NULL DEFAULT NULL COMMENT '开课时间',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  PRIMARY KEY (`cid`, `chid`, `sid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tc_schedule
-- ----------------------------
INSERT INTO `tc_schedule` VALUES (2, 133, 3, 4, '5443af34efc44dd694c7a64060e925ce', '2021-05-20 00:00:00', '2021-05-03 13:23:21', NULL);
INSERT INTO `tc_schedule` VALUES (2, 133, 5, 4, '7450a077e3f0426a988bb6e0c506b074', '2021-05-21 22:00:00', '2021-05-01 14:22:39', '2021-05-03 13:34:59');
INSERT INTO `tc_schedule` VALUES (2, 134, 3, 4, '8ec2d420f92443efa043eacc1eddbe7f', '2021-05-21 00:00:00', '2021-05-05 21:02:20', NULL);
INSERT INTO `tc_schedule` VALUES (2, 134, 5, 4, '37455c8617b6430682c8bf06d024390d', '2021-05-13 19:07:00', '2021-05-01 14:24:30', NULL);
INSERT INTO `tc_schedule` VALUES (2, 135, 3, 4, 'fb0b456ffcc44ee0b21502b16e33014f', '2021-05-14 09:00:00', '2021-05-05 21:10:37', NULL);
INSERT INTO `tc_schedule` VALUES (2, 135, 5, 4, 'ad5caa0385624f5fb90be4f40b2b1a66', '2021-05-15 00:00:00', '2021-05-05 21:03:50', NULL);
INSERT INTO `tc_schedule` VALUES (2, 136, 3, 4, '5d3a69070d5c49678d5cca938902484e', '2021-05-20 15:00:00', '2021-05-05 21:22:41', NULL);
INSERT INTO `tc_schedule` VALUES (2, 137, 5, 4, 'ac6cb16a119c43e198f7db2183069f62', '2021-05-15 15:57:49', '2021-05-15 15:57:51', NULL);
INSERT INTO `tc_schedule` VALUES (3, 2, 3, 4, 'cf3fba441c56404a9fcb6792b331d955', NULL, '2021-04-25 13:28:46', '2021-05-03 16:07:25');
INSERT INTO `tc_schedule` VALUES (3, 4, 0, 4, '89546f560a7b450a916c148aeaa014f6', '2021-04-12 00:00:00', '2021-04-30 12:07:48', '2021-04-30 12:13:00');
INSERT INTO `tc_schedule` VALUES (3, 6, 3, 4, '480ea0c576aa48d18b316d7db48ef5f1', NULL, '2021-04-25 13:28:55', '2021-05-03 16:07:30');
INSERT INTO `tc_schedule` VALUES (3, 6, 6, 4, 'e51ab41ffa684de4b556b92729fc0074', '2021-06-01 17:00:00', '2021-06-01 16:41:00', NULL);
INSERT INTO `tc_schedule` VALUES (3, 8, 3, 0, '6fe61d05ba1e44a8b15360b11b39ef37', NULL, '2021-04-28 22:29:14', '2021-05-03 16:07:32');
INSERT INTO `tc_schedule` VALUES (3, 8, 6, 4, '8a7eb972de6346a1b2b62b35b47a0038', '2021-06-10 20:00:00', '2021-06-01 15:53:40', NULL);
INSERT INTO `tc_schedule` VALUES (3, 44, 3, 4, 'fdsds', NULL, '2021-04-24 14:31:30', '2021-05-03 16:07:33');
INSERT INTO `tc_schedule` VALUES (3, 52, 3, 4, '4dfcd81c4d1b453ba50b971b3883bdef', NULL, '2021-04-24 21:09:07', '2021-05-03 16:07:34');
INSERT INTO `tc_schedule` VALUES (3, 55, 3, 4, '6c9f90d785854ea48a89b907aaaec525', NULL, '2021-04-25 13:39:00', '2021-05-03 16:07:35');
INSERT INTO `tc_schedule` VALUES (3, 110, 3, 4, 'f0b71f02b9104622a2796ea4345b5983', NULL, '2021-04-24 21:25:27', '2021-05-03 16:07:37');
INSERT INTO `tc_schedule` VALUES (3, 112, 0, 4, '5ebab390-ce5e-43f6-9ea7-b2be0f848818', '2021-04-14 14:22:22', '2021-04-24 21:06:51', '2021-04-30 13:54:23');
INSERT INTO `tc_schedule` VALUES (3, 113, 0, 4, '73d03504ea704e17af7d9739600898b3', '2021-04-28 00:00:00', '2021-04-30 15:00:32', NULL);
INSERT INTO `tc_schedule` VALUES (3, 114, 3, 0, '41e30c1e736744ba81f61689a2be58f9', NULL, '2021-04-28 22:30:23', '2021-05-03 16:07:39');
INSERT INTO `tc_schedule` VALUES (3, 116, 3, 4, '9797977d3d1b4635a403eb527d13996d', NULL, '2021-04-24 23:13:37', '2021-05-03 16:07:40');
INSERT INTO `tc_schedule` VALUES (3, 117, 3, 4, '186ae604049a4281875ed4d81b265283', NULL, '2021-04-25 13:29:16', '2021-05-03 16:07:41');
INSERT INTO `tc_schedule` VALUES (3, 149, 0, 4, 'b2e78d4a3eaa457ea84ce577da423b1f', '2021-04-19 09:07:02', '2021-04-30 15:41:38', NULL);

-- ----------------------------
-- Table structure for tu_note
-- ----------------------------
DROP TABLE IF EXISTS `tu_note`;
CREATE TABLE `tu_note`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `sid` int(11) NOT NULL COMMENT '学生id',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '笔记主体',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tu_note
-- ----------------------------
INSERT INTO `tu_note` VALUES (1, 6, '这是派蒙的第一条笔记这是派蒙的第一条笔记这是派蒙的第一条笔记这是派蒙的第一条笔记这是派蒙的第一条笔记这是派蒙的第一条笔记这是派蒙的第一条笔记这是派蒙的第一条笔记这是派蒙的第一条笔记这是派蒙的第一条笔记这是派蒙的第一条笔记这是派蒙的第一条笔记这是派蒙的第一条笔记这是派蒙的第一条笔记这是派蒙的第一条笔记这是派蒙的第一条笔记这是派蒙的第一条笔记蒙的第一条笔记这是派蒙的第一条笔记这是派蒙的第一条笔记这是派蒙的第一条笔记这是派蒙的第一条笔记这是派蒙的第一条笔记这是派蒙的第一条笔记这是派蒙的第一条笔记这是派蒙的第一条笔记这是派蒙的第一条笔记这是派蒙的第一条笔记这是派蒙的第一条笔记这是派蒙的第一条笔记这是派蒙的第一条笔记这是派蒙的第一条笔记这是派蒙的第一条笔记这是派蒙的第一条笔记', '2021-05-04 20:57:56', '2021-05-04 22:16:23');
INSERT INTO `tu_note` VALUES (4, 6, '<p>真正的</p><p>富文本</p><p><s>编辑器！！</s><strong>﻿</strong></p><p>真正的</p><p>富文本</p><p><strong>编辑器！！</strong></p><p>真正的！！！</p><p>富文本</p><h1>编辑器！！</h1>', '2021-05-04 22:06:46', '2021-05-04 22:17:15');
INSERT INTO `tu_note` VALUES (7, 3, '<h1>登记弟弟</h1>', '2021-05-08 15:00:37', NULL);
INSERT INTO `tu_note` VALUES (8, 3, '<p>df </p><p>fds</p>', '2021-05-09 16:20:45', NULL);
INSERT INTO `tu_note` VALUES (9, 6, '<p>OK good!</p>', '2021-06-01 17:42:34', NULL);
INSERT INTO `tu_note` VALUES (10, 6, '<p>OK good!</p>', '2021-06-01 17:43:56', NULL);
INSERT INTO `tu_note` VALUES (11, 6, '<p>Ok good</p>', '2021-06-01 17:46:12', NULL);
INSERT INTO `tu_note` VALUES (12, 6, '<p>OK good!</p>', '2021-06-01 17:54:20', NULL);
INSERT INTO `tu_note` VALUES (13, 6, '<p>OK good</p>', '2021-06-01 17:59:31', NULL);
INSERT INTO `tu_note` VALUES (14, 6, '<p>OK good</p>', '2021-06-01 18:03:47', NULL);
INSERT INTO `tu_note` VALUES (15, 6, '<p>OK good</p>', '2021-06-01 18:07:33', NULL);

-- ----------------------------
-- Table structure for tu_permissions
-- ----------------------------
DROP TABLE IF EXISTS `tu_permissions`;
CREATE TABLE `tu_permissions`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '权限名称',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '权限描述',
  `http_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '权限关联的uri',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`, `title`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tu_permissions
-- ----------------------------

-- ----------------------------
-- Table structure for tu_request
-- ----------------------------
DROP TABLE IF EXISTS `tu_request`;
CREATE TABLE `tu_request`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '请求id',
  `tid` int(11) NOT NULL COMMENT '教师id',
  `sid` int(11) NOT NULL COMMENT '学生id',
  `rid` int(11) NOT NULL COMMENT '请求类型 1 删除课程 2 调整时间',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '请求正文',
  `param_json` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求参数',
  `direction` tinyint(1) NOT NULL DEFAULT 1 COMMENT '请求方向 1 教师to学生 2 学生to教师 3 管理员toAll',
  `status` tinyint(1) UNSIGNED NOT NULL DEFAULT 1 COMMENT '处理状态 1 未处理 2 已处理',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 81 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tu_request
-- ----------------------------
INSERT INTO `tu_request` VALUES (55, 4, 3, 2, '', '{\"changeTime\":\"2021-05-14T09:00:00\",\"courseName\":\"小杨语法\",\"chapterName\":\"反身代词\",\"chid\":135,\"cid\":2}', 2, 2, '2021-05-05 21:10:55', '2021-05-05 21:13:32');
INSERT INTO `tu_request` VALUES (57, 4, 3, 3, 'f\'d\'s', '{\"result\":\"驳回\",\"id\":55}', 1, 1, '2021-05-05 21:13:32', NULL);
INSERT INTO `tu_request` VALUES (62, 4, 3, 1, 'sd ', '{\"courseName\":\"老张音标\",\"sName\":\"zhen\",\"sid\":3,\"cid\":1}', 2, 2, '2021-05-14 16:26:04', '2021-06-01 15:27:53');
INSERT INTO `tu_request` VALUES (68, 1, 7, 3, '管理员下线了小杨音标', '', 3, 1, '2021-05-16 14:18:21', NULL);
INSERT INTO `tu_request` VALUES (70, 1, 7, 3, '管理员上线了小杨音标', '', 3, 1, '2021-05-16 14:24:41', NULL);
INSERT INTO `tu_request` VALUES (72, 1, 7, 3, '管理员下线了小杨音标', '', 3, 1, '2021-05-16 14:26:08', NULL);
INSERT INTO `tu_request` VALUES (74, 1, 7, 3, '管理员上线了小杨音标', '', 3, 1, '2021-05-16 14:27:35', NULL);
INSERT INTO `tu_request` VALUES (75, 4, 3, 3, '不许', '{\"result\":\"驳回\",\"id\":62}', 1, 1, '2021-06-01 15:27:53', NULL);
INSERT INTO `tu_request` VALUES (79, 4, 6, 3, '管理员上线了小杨词汇', '', 3, 1, '2021-06-01 15:31:12', NULL);
INSERT INTO `tu_request` VALUES (80, 4, 6, 3, '管理员下线了小杨词汇', '', 3, 1, '2021-06-01 15:31:12', NULL);

-- ----------------------------
-- Table structure for tu_role
-- ----------------------------
DROP TABLE IF EXISTS `tu_role`;
CREATE TABLE `tu_role`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色名',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '角色描述',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  PRIMARY KEY (`id`, `title`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tu_role
-- ----------------------------
INSERT INTO `tu_role` VALUES (0, 'admin', '管理员', '2021-04-21 15:37:33', '2021-04-21 15:37:33');
INSERT INTO `tu_role` VALUES (1, 'teacher', '教师', '2021-04-21 15:37:53', '2021-04-21 15:37:53');
INSERT INTO `tu_role` VALUES (2, 'student', '学生', '2021-04-21 15:38:02', NULL);

-- ----------------------------
-- Table structure for tu_role_permission_relation
-- ----------------------------
DROP TABLE IF EXISTS `tu_role_permission_relation`;
CREATE TABLE `tu_role_permission_relation`  (
  `role_id` int(10) UNSIGNED NOT NULL COMMENT '角色id',
  `permission_id` int(10) UNSIGNED NOT NULL COMMENT '权限id',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`role_id`, `permission_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tu_role_permission_relation
-- ----------------------------

-- ----------------------------
-- Table structure for tu_user
-- ----------------------------
DROP TABLE IF EXISTS `tu_user`;
CREATE TABLE `tu_user`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户id',
  `login_pwd` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登录密码',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '邮箱',
  `mobile` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号码',
  `user_status` tinyint(3) UNSIGNED NOT NULL DEFAULT 1 COMMENT '状态 1 启用 2 禁用',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tu_user
-- ----------------------------
INSERT INTO `tu_user` VALUES (1, 'admin', 'admin', 'admin@163.com', '12453369343', 1, '2021-04-12 20:32:43', NULL);
INSERT INTO `tu_user` VALUES (2, 'yang', '123', 'yang@1.com', '15233545343', 1, '2021-04-14 17:34:39', '2021-04-22 16:36:37');
INSERT INTO `tu_user` VALUES (3, 'zhen', '111', '1534343@qq.com', '11114444225', 1, '2021-04-14 17:35:40', '2021-06-01 16:20:56');
INSERT INTO `tu_user` VALUES (4, '杨老师', '123123', '132@163.com', '12234456678', 1, '2021-04-15 21:31:12', '2021-05-15 16:00:56');
INSERT INTO `tu_user` VALUES (5, '张伟', 'qwer1234', 'ze@163.com', '15645435643', 1, '2021-04-20 16:21:07', '2021-06-01 16:21:06');
INSERT INTO `tu_user` VALUES (6, '派蒙', 'yjsp', 'paimeng@mhy.com', '15235436434', 1, '2021-04-20 21:07:08', '2021-04-21 14:41:46');
INSERT INTO `tu_user` VALUES (7, 'stu1', 'stu', 'stu1@spark.com', '15235436432', 1, '2021-05-01 13:07:26', '2021-06-01 16:20:42');
INSERT INTO `tu_user` VALUES (8, 'stu2', 'stu', 'stu2@spark.com', '15235435464', 2, '2021-05-01 13:07:32', '2021-06-01 16:20:47');
INSERT INTO `tu_user` VALUES (9, '张老师', '123123', 'zls@spark.com', '15344534346', 1, '2021-05-27 20:51:53', '2021-06-01 16:20:24');

-- ----------------------------
-- Table structure for tu_user_role_relation
-- ----------------------------
DROP TABLE IF EXISTS `tu_user_role_relation`;
CREATE TABLE `tu_user_role_relation`  (
  `user_id` int(10) UNSIGNED NOT NULL COMMENT '用户主键',
  `role_id` int(10) UNSIGNED NOT NULL COMMENT '角色主键',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tu_user_role_relation
-- ----------------------------
INSERT INTO `tu_user_role_relation` VALUES (1, 0, '2021-04-21 15:39:21', NULL);
INSERT INTO `tu_user_role_relation` VALUES (2, 1, '2021-04-21 15:39:25', NULL);
INSERT INTO `tu_user_role_relation` VALUES (3, 2, '2021-04-21 15:39:28', NULL);
INSERT INTO `tu_user_role_relation` VALUES (4, 1, '2021-04-21 15:39:31', NULL);
INSERT INTO `tu_user_role_relation` VALUES (5, 2, '2021-04-21 15:39:34', NULL);
INSERT INTO `tu_user_role_relation` VALUES (6, 2, '2021-04-21 15:39:41', '2021-04-30 13:54:57');
INSERT INTO `tu_user_role_relation` VALUES (7, 2, '2021-05-01 13:07:26', NULL);
INSERT INTO `tu_user_role_relation` VALUES (8, 2, '2021-05-01 13:07:32', NULL);
INSERT INTO `tu_user_role_relation` VALUES (9, 1, '2021-05-27 20:51:53', NULL);

SET FOREIGN_KEY_CHECKS = 1;
