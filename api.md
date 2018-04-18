注：key->value
无特别注明GET 则为POST

时间传递均为时间戳

### 首页
#### 获取学院信息 GET */index/institute*
Args:
- 无

Returns:
- flag
- msg
- data:jsonArray
    - id -> 学院名


####  获取最多六条新闻 GET */index/sixNoticeTitle*
Args:
- 无

Returns:
- flag
- msg
- data:jsonarray
    - id -> 公告的id
    - title -> 公告的标题
    - time -> 发布时间


#### 公告详情页：公告详情/index/notice
Args:
- id:公告id,首页返回

Returns:
- flag
- msg
- data: json
    - publishTime->发布时间
    - publisherName->发布人名称
    - text->公告正文
    - title->公告标题


#### 获取分类列表 GET */index/classification*
Args: 
- 无

Returns:
- flag
- msg
- data:jsonArray
    - id -> 分类id
    - name -> 分类的中文名
    - refer -> 是谁的子类,0表示大类（就展示用而已）

例：闲置数码是大类，手机是他的子类
[{"refer":0,"name":"闲置数码","id":1},{"refer":1,"name":"手机","id":7}]

#### 搜索分页 */index/getSearchPages*
Args:详解见下文
- search
- institute
- classification
- pageSize

Returns:
- flag
- msg
- data:json
    - pages -> 页数


#### 首页搜索 */index/search*
搜索只返回可购买的物品
返回信息较多 前端选择展示部分即可

Args:
- search:搜索内容
- institute:学院
- classification:分类
- pageStart:页面的开始索引
- pageSize:每一页的大小 

Returns:
- flag
- msg
- data:jsonArray
    - id -> 物品id
    - title -> 出售物品标题
    - price -> 价格
    - classification -> 分类id
    - publisher -> 发布人id
    - description -> 描述
    - images -> base64编码的图片 分号隔开不同图片
    - contact -> 联系方式
    - status -> 0可购买 1已被购买
    - browse -> 浏览量
    - time -> 发布时间




### 商品相关 

#### 发布一个商品 */commodity/publish*

Args:

```
/**
 * 发布一个商品
 * @param title 标题
 * @param contact 联系方式
 * @param description 描述
 * @param images 图片 base64编码 英文分号隔开
 * @param classification 分类对应的id
 * @param price 价格 数字
 * @param publisherId 发布人id 数字
 * @return json
 */
```

Returns:

- flag
- msg
- data



#### 修改一个商品 */commodity/modify*

Args:

```
/**
 * 修改已发布商品信息
 * @param commodityId 商品id
 * @param title 标题
 * @param contact 联系方式
 * @param description 描述
 * @param images 图片base64编码
 * @param classification 分类
 * @param price 价格
 * @param modifier 修改人ID
 * @return json
 */
```

Returns:

- flag
- msg
- data



#### 删除一个商品 */commodity/delete*

Args:

```
/**
 * 删除商品 及相关图片
 * @param commodityId 商品id
 * @param userId 用户id（当前操作用户）
 * @return json
 */
```

Returns:

- flag
- msg
- data



#### 购买商品 */commodity/purchase*

Args:

```
/**
 * 购买商品
 * @param purchaserId 购买人ID
 * @param commodityId 商品ID
 * @return json
 */
```

Returns:

- flag
- msg
- data



####获取帖子的详情 */commodity/detail*
Args:

- commodityId：商品id，进入前肯定有返回
- pageSize: 对回复的分页大小


Returns:

- flag
- msg
- data:json
  - title
  - contact
  - description
  - price
  - status -> 0可购买 1已被购买
  - time
  - publisherName -> 发布人姓名
  - browse -> 浏览数
  - images 
  - replyPages -> 回复分页数




#### 某个大类下的商品列表 1.分页 2.获取具体数据

#### *1./commodity/classPages*

Args:

```
/**
 * 获取某个大类下的商品展示页数
 * @param classificationId 分类id
 * @param pageSize 分页大小
 * @return json pages->value
 */
```

Returns:

- flag
- msg
- data:json  pages -> 页数

#### 2. /commodity/class

Args:

```
/**
 * 分页获取某个分类下的商品数据
 * @param classificationId 分类ID
 * @param start 开始索引
 * @param pageSize 分页大小
 * @return json
 */
```

Returns:

- flag
- msg
- data: JsonArray: (应该不用解释了吧，选择性展示就好了)

```
private Integer id;
private String title;
private Float price;
private String publisherName;
private Integer publisherId;
private String description;
private String images;
private String contact;
private Integer status;
private Integer browse;
private Integer reply;
private Long time;
```



### 和具体到商品详情页下某一条的回复有关的接口

#### 分页获取当前商品的回复*/post/detail*

Args: (页面数在进入商品详情页时发送pageSize返回，/commodity/detail)

```
/**
 * @param commodityId 商品id
 * @param pageSize    分页大小
 * @param pageStart   页面开始
 * @return json
 */
```

Returns:

- flag
- msg
- data:jsonArray (key与名称相同)

```
private Integer id;//回复的id 删除与再回复时用到
private String text;//回复内容
private Integer replyPostId;//当前回复是哪一条回复的回复，那条的id
private String replier;//回复人名字
private Long time;//时间戳
```



#### 回帖*/post/reply*

Args:

```
/**
 * @param replier 回复人ID
 * @param text 回复内容
 * @param replyPostId 被回复的回帖ID 直接回复原贴设为0
 * @param commodityId 商品ID
 * @return json
 */
```

Returns:

- flag
- msg
- data



#### 删除回复*/post/delete*

Args: 普通用户仅能删除自己回复 管理员可以随便删

```
/**
 * @param postId 回复ID
 * @param userId 当前操作用户ID
 * @return json
 */
```

Returns:

- flag
- msg
- data





### 与管理员有关的接口

#### 添加学院 */user/admin/addInstitute*

Args:

- instituteName 要添加的学院名
- userId 操作人ID

Returns:

- flag
- msg
- data:json
  - id -> 学院名 

#### 重命名学院 */user/admin/renameInstitute*

Args: （同上）

- instituteId
- newName
- userId

Returns:

- flag
- msg
- data

#### 删除学院 */user/admin/deleteInstitute*

Args: （同上）

- instituteId
- userId

Returns:

- flag
- msg
- data



#### 新增分类 */user/admin/addClassification*

Args:

- name:分类名称
- refer:上级分类的id 无上级则传0
- userId:

Returns:

- flag
- msg
- data:json
  - id -> 新分类的id
  - name -> 新分类的名字
  - refer -> 新分类的上级分类id

#### 删除分类 */user/admin/deleteClassification*

//前端提示删除也会把子类删除 （删除后建议重新获取一次分类列表）

Args:

- classificationId: 分类id
- userId

Returns:

- flag
- msg
- data

#### 修改分类 */user/admin/renameClassification*

Args:

- classificationId
- newName
- userId

Returns:

- flag
- msg
- data



#### 获取用户列表的分页数 */user/admin/totalUsers*

Args:

```
/**
 * 获取用户的分页数
 * @param pageSize 分页大小
 * @return json
 */
```

Return:

- flag
- msg
- data : json pages -> 页数



#### 分页获取用户列表 */admin/obtainAllUserByPage*

Args:

```
/**
 * 分页获取用户信息
 * @param pageStart 页面开始的索引
 * @param pageSize 页面大小
 * @return json
 */
```

Returns:

- flag
- msg
- data jsonArray

```
private Integer id;
private String username;
private String password;
private String name;
private String phone;
private Integer instituteId;
private String stuId;
private String email;
private String avatar;
private boolean admin;
private Long time;
```



#### 管理员搜索用户 */user/admin/searchUser*

Args:

```
/**
 * 支持id、username、name、stuId搜索用户
 * @param search 搜索内容
 * @return json
 */
```

Returns:返回的是一个列表  和上面的分页获取的信息相同



#### 管理员删除用户 */user/admin/deleteUser*

Args:

```
/**
 * 管理员删除用户
 * @param userId 用户id
 * @return json
 */
```

Returns:



#### 管理员设置其他用户为管理员 */user/admin/addAdmin*

Args:

```
/**
 * 设置其他普通用户为管理员
 * @param adminId 当前管理员ID
 * @param userId 被设置的普通用户的ID
 * @return json
 */
```

Returns:



#### 管理员发布新闻 */user/admin/publishNotice*

Args:

```
/**
 * 发布新闻和公告
 * @param title 标题
 * @param text 文本
 * @param userId 用户id
 * @return json
 */
```

Returns:



#### 管理员删除公告 /user/admin/deleteNotice

Args:

```
/**
 * 删除公告
 * @param noticeId 公告id
 * @param userId 操作人id
 * @return json
 */
```

Returns:









#### 注册 */register*

Args:
- username
- password
- name
- phone
- instituteId
- stuId
- *email(非必传)*

Returns:
- 同下

#### 登录 */login*
Args:
- username
- password

Returns:
- flag:0失败 1成功
- msg:提示信息
- data:好多信息我懒得写诶

登陆后应设置cookie和session 3600s

#### 请求学院 */index/institute*
无参数

Returns:
- flag:0失败 1成功
- msg:提示信息
- data:json字符串，id->学院名





## 与私信有关的接口 

私信做的很简单 一条就是一条 回复性都没有 后台只知道是谁发给谁的、以及是否已读、没有任何上下文联系

#### 发送私信 */user/sendPrivateMessage*

Args:

```
/**
 * @param fromId 发送人ID
 * @param toId 接收人ID
 * @param message 消息体
 */
```

Returns:

- flag
- msg
- data



#### 查看我的私信*/user/myPrivateMessage*

Args:

```
/**
 * @param userId 我的ID
 * @return json
 */
```

Returns:

- flag
- msg
- data jsonArray 返回中未读私信、最后收到的优先

```
private Integer id;//私信的id
private Integer fromId;//谁发的
private Integer fromUserName;//发送人的用户名
private Integer status;// 状态 0未读 1已读
private String message;
private Long time;//时间戳
```



#### 删除私信 */user/deletePrivateMessage*

Args:

```
/**
 * @param messageId 消息id
 * @param userId 操作人id
 * @return json
 */
```

Returns:

- flag
- msg
- data



## 和普通用户有关的接口

#### 获取用户详细信息*/user/detail/*

Args:

```
/**
 * @param userId 用户id
 */
```

Return:

- flag
- msg
- data:json

```
private Integer id;
private String username;//用户名
private String name;//姓名
private String phone;
private String institute;//学院名
private String stuID;
private String email;
private String avatar;//头像的base64编码
private boolean admin;//1是管理员 0普通用户
private Long time;//注册时间
```



#### 已发布的商品分页 */user/commodityPages*

Args:

```
/**
 * 用户中心界面获取"已发布的商品"分页数
 * @param userId 用户id
 * @param pageSize 分页大小
 * @return json
 */
```

Returns:

- flag
- msg
- data:json pages->页数



#### 分页获取已发布的商品 */user/myPublish*

Args:

```
/**
 * 分页获取"已发布的商品"
 * @param userId 用户ID
 * @param pageSize 分页大小
 * @param pageStart 页面开始位置
 * @return json
 */
```

Returns:

- flag
- msg
- data jsonArray 选择性展示 点击跳转难道对应帖子页

```
private Integer id;
private String title;
private Float price;
private String publisherName;
private Integer publisherId;
private String description;
private String images;
private String contact;
private Integer status;
private Integer browse;
private Integer reply;
private Long time;
```



#### 修改密码 */user/modifyPw*

Args:

```
/**
 * 更新账户密码
 * @param userId 用户id
 * @param oldPw 旧密码
 * @param newPw 新密码
 * @return json
 */
```

Returns:



#### 修改除密码以外的个人信息 */user/update*

Args:改没改都传一下 参数要有 传空也可以

```
/**
 * 修改除密码以外的个人信息
 * @param userId 用户id
 * @param userName 用户名
 * @param name 名
 * @param phone 手机号
 * @param instituteId 学院id
 * @param stuId 学号
 * @param email 邮箱
 * @param avatar 头图base64编码
 * @return json
 */
```

Returns:



#### 获取已购买的物品分页数 */user/boughtPages*

Args:

```
/**
 * 获取已购买物品的分页数
 * @param userId 用户ID
 * @param pageSize 分页大小
 * @return json
 */
```

Returns:

- flag
- msg
- data : json pages -> 页数



#### 分页获取购买的物品 */user/bought*

Args: 每个物品建议增加点击跳转到详情页

```
/**
 * 分页获取已购买商品
 * @param userId 用户ID
 * @param pageStart 页面开始
 * @param pageSize 页面大小
 * @return json
 */
```

Returns:

- flag
- msg
- data : jsonArray
  - id -> 物品id
  - title -> 标题
  - description -> 描述
  - price -> 价格



#### 选择时间段查看已购买的物品 */user/timeShopping*

Args: 饼状图展示类别数量即可 

```
/**
 * 按时间获取购物详情
 * @param start 开始时间戳
 * @param end 结束时间戳
 * @param userId 用户id
 * @return json,key为分类id value为购买数
 */
```

Returns:

- flag
- msg
- data : jsonArray
  - key : 分类的id -> value : 购买数量



#### 获取“我的回复” /user/reply2me

Args:

```
/**
 * 获取"我的回复" （回复我的消息）
 * @param userId 我的用户ID
 * @param pageSize 页面大小
 * @param pageStart 页面开始
 * @return json
 */
```

Returns:

- flag
- msg
- data : jsonArray
  - postId -> 回复的id
  - commodityId -> 商品的id
  - commodityTitle -> 商品标题
  - replierId -> 回复人id
  - replierUserName -> 回复人用户名
  - unread -> 是否已读 0未读1已读

