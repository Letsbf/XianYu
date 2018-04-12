CREATE DATABASE xyesjy;
USE xyesjy;

CREATE TABLE institute(
  id TINYINT PRIMARY KEY AUTO_INCREMENT NOT NULL ,
  name VARCHAR(48) NOT NULL
)ENGINE=InnoDB DEFAULT CHARSET = utf8;

INSERT INTO institute(name) VALUES ("计算机学院"),("经济管理学院"),
  ("光华管理学院"),("学院1"),("学院2"),("学院3");

CREATE TABLE user(
  id INT PRIMARY KEY AUTO_INCREMENT NOT NULL ,
  username VARCHAR(20) NOT NULL ,
  password VARCHAR(16) NOT NULL ,
  name VARCHAR(16) NOT NULL ,
  phone VARCHAR(11) NOT NULL ,
  institute_id TINYINT NOT NULL ,
  stu_id VARCHAR(20) NOT NULL ,
  email VARCHAR(32) DEFAULT '',
  avatar VARCHAR(128) DEFAULT '',
  admin TINYINT(1) DEFAULT 0 COMMENT '1管理员 0普通用户',
  time BIGINT COMMENT '注册时间',
  INDEX (id),
  FOREIGN KEY(institute_id) REFERENCES institute(id) ON DELETE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET = utf8;

INSERT INTO user(username, password, name, phone, institute_id, stu_id, email, avatar, admin, time) VALUE (
    "admin","admin","admin","12345678911",1,"12345","123@qwq.com","??",1,UNIX_TIMESTAMP()
);
INSERT INTO user(username, password, name, phone, institute_id, stu_id, email, avatar, admin, time) VALUE (
  "user1","user1","user1","12345678911",1,"12345","123@qwq.com","??",0,UNIX_TIMESTAMP()
);
INSERT INTO user(username, password, name, phone, institute_id, stu_id, email, avatar, admin, time) VALUE (
  "user2","user2","user2","12345678911",1,"12345","123@qwq.com","??",0,UNIX_TIMESTAMP()
);

CREATE TABLE notice(
  id INT PRIMARY KEY AUTO_INCREMENT NOT NULL ,
  title VARCHAR(100) NOT NULL COMMENT '标题',
  text VARCHAR(1024) NOT NULL COMMENT '正文',
  publisher INT NOT NULL COMMENT '发布人',
  publish_time BIGINT NOT NULL COMMENT '发布时间',
  INDEX (publisher,id),
  FOREIGN KEY(publisher) REFERENCES user(id) ON DELETE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET = utf8 COMMENT '新闻公告';

INSERT INTO notice (title, text, publisher, publish_time) VALUES (
    "测试新闻1","这是新闻1的正文如果还有梦就追mavcipromacbookpro泰国新加坡印度尼西亚",1,UNIX_TIMESTAMP()
);

CREATE TABLE classification(
  id INT PRIMARY KEY AUTO_INCREMENT NOT NULL ,
  name VARCHAR(20) NOT NULL COMMENT '分类名' ,
  refer INT DEFAULT 0 COMMENT '分类的上级大类',
  INDEX (id)
)ENGINE=InnoDB DEFAULT CHARSET = utf8 COMMENT '分类';

INSERT INTO classification (name, refer) VALUES ("闲置数码", 0),("闲置母婴", 0),("家具日用", 0),("影音家电", 0),
  ("鞋服配饰", 0),("手机", 1),("手机", 1);

CREATE TABLE commodity (
  id             INT PRIMARY KEY  AUTO_INCREMENT NOT NULL,
  title          VARCHAR(20)                     NOT NULL
  COMMENT '帖子标题',
  price          FLOAT(16)                       NOT NULL
  COMMENT '价格',
  classification INT                             NOT NULL
  COMMENT '类别',
  publisher  INT                             NOT NULL
  COMMENT '发布人id',
  description    VARCHAR(1024)                   NOT NULL
  COMMENT '描述',
  images         VARCHAR(500) COMMENT '图片链接,分号隔开',
  contact      VARCHAR(30)                     NOT NULL
  COMMENT '联系方式',
  status         TINYINT DEFAULT 0
  COMMENT '交易状态 0可购买 2已购买 3已关闭',
  browse         INT     DEFAULT 0
  COMMENT '浏览数',
  time           BIGINT COMMENT '发布时间',
  INDEX (id, publisher),
  FOREIGN KEY(publisher) REFERENCES user(id) ON DELETE CASCADE ,
  FOREIGN KEY(classification) REFERENCES classification(id) ON DELETE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET = utf8 COMMENT '商品发布表,算是帖子头';

INSERT INTO commodity (title, price, classification, publisher, description, images, contact, status, time) VALUES (
  "测试商品1", 999, 1, 1, "如果还有梦就追", "", "t@mt.com", 0, UNIX_TIMESTAMP()
);

INSERT INTO commodity (title, price, classification, publisher, description, images, contact, status, time) VALUES (
  "测试商品2", 999, 1, 2, "如果还有梦就追", "", "t@mt.com", 0, UNIX_TIMESTAMP()
);

CREATE TABLE post (
  id INT PRIMARY KEY  AUTO_INCREMENT NOT NULL ,
  text VARCHAR(256) NOT NULL COMMENT '回复信息的主体',
  replier INT NOT NULL COMMENT '回复人id',
  reply_post_id INT DEFAULT 0 COMMENT '被回复的信息id 0为回复帖子',
  commodity_id INT NOT NULL COMMENT '哪个帖子下的回复',
  status INT DEFAULT 0 COMMENT '消息未读状态，0未读1已读',
  time BIGINT NOT NULL DEFAULT 0 COMMENT '回复时间',
  INDEX (id, replier,commodity_id),
  FOREIGN KEY (replier) REFERENCES user(id) ON DELETE CASCADE ,
  FOREIGN KEY (commodity_id) REFERENCES commodity(id) ON DELETE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET = utf8 COMMENT '帖子回复表';

INSERT INTO post (text, replier, reply_post_id, status, time, commodity_id) VALUES (
  "测试帖子1的测试回复1", 1, 0, 0, UNIX_TIMESTAMP(), 1
);

INSERT INTO post (text, replier, reply_post_id, status, time, commodity_id) VALUES (
  "测试帖子2的测试回复1", 1, 0, 0, UNIX_TIMESTAMP(), 2
);


CREATE TABLE deal(
  id INT PRIMARY KEY  AUTO_INCREMENT NOT NULL ,
  purchaser_id INT NOT NULL COMMENT '购买人',
  commodity_id INT NOT NULL COMMENT '商品id',
  time BIGINT NOT NULL DEFAULT 0 COMMENT '购买时间',
  FOREIGN KEY (purchaser_id) REFERENCES user(id) ON DELETE CASCADE,
  FOREIGN KEY (commodity_id) REFERENCES commodity(id) ON DELETE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET = utf8 COMMENT '购买表';