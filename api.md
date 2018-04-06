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