注：key->value
无特别注明GET 则为POST
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



### 管理员页

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

#### 修改分类 */user/admin/renameClassificationa*

Args:

- classificationId
- newName
- userId

Returns:

- flag
- msg
- data




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