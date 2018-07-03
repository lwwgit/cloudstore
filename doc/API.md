# API
##用户管理

###用户注册
```
POST /register
```
#####Request 

Headers
```
Content-Type: application/x-www-form-urlencoded
```

Body

```
{
    "username": "lww"
    "tel": "13260907855"
    "password": "123"
    "sms": "1234" //手机短信验证码
}
```
#####Response  200
Headers

```
Content-Type: application/json
```

Body

```
{
  "code": 0,
  "msg": "成功",
  "data": null
}

{
  "code": 1,
  "msg": "验证码错误"||"用户名已存在"||"手机号已被占用"
  "data": null
}
```
###获取图片验证码
```
GET /code/image
```
#####Request 

URI Parameters
```
null
 
```
 
#####Response  200

 
###用户账号密码登录
```
POST /authentication/login
```
#####Request 

Headers
```
Content-Type: application/x-www-form-urlencoded
```

Body

```
{
    "username": "lww"
    "password": "123"
    "imageCode": "1234" //图片验证码
}
```
#####Response  200
Headers

```
Content-Type: application/json
```

Body

```
{
  "code": 0,
  "msg": "登录成功",
  "token":
  "username": lww
  "role": [ROLE_USER]
}

{
  "code": 1,
  "msg": "验证码错误" 
  "data": null
}
```
###用户手机号验证码登录
```
POST /authentication/mobile
```
#####Request 

Headers
```
Content-Type: application/x-www-form-urlencoded
```

Body

```
{
    "mobile"： "13260907875"
    "smsCode": "1234" //短信验证码
}
```
#####Response  200
Headers

```
Content-Type: application/json
```

Body

```
{
  "code": 0,
  "msg": "登录成功",
  "token":
  "username": lww
  "role": [ROLE_USER]
}

{
  "code": 1,
  "msg": "验证码错误" 
  "data": null
}
``` 

###判断用户是否存在
```
GET /user/check
```
#####Request 

URI Parameters
```
username  string 
tel       string
```
 
#####Response  200
Headers

```
Content-Type: application/json
```

Body

```
{
  "code": 0,
  "msg": "成功",
  "data": null
}

{
  "code": 1,
  "msg": "用户名已存在"||"手机号已被占用"
  "data": null
}
```

###获取手机验证码
```
GET /code/sms
```
#####Request 

URI Parameters
```
mobile string
 
```
 
#####Response  200
Headers

```
Content-Type: application/json
```

Body

```
{
  "code": 0,
  "msg": "成功",
}
 
```
###验证手机验证码
```
GET /code/sms/verify
```
#####Request 

URI Parameters
```
    sms  string (required) 
 
```
 
#####Response  200
Headers

```
Content-Type: application/json
```

Body

```
{
  "code": 0,
  "msg": "成功",
  "data": null
}
{
   "code": 1,
   "msg": "短信验证码错误",
   "data": null
}
   
```
###忘记密码
```
POST /forget/password
```
#####Request 

Headers
```
Content-Type: application/x-www-form-urlencoded
```

Body

```
{
    "tel": "13260907855"
    "newPassword": "123"
}
```
#####Response  200
Headers

```
Content-Type: application/json
```

Body

```
{
  "code": 0,
  "msg": "成功",
  "data": null
}

{
  "code": 1,
  "msg": "用户不存在" 
  "data": null
}
```


###修改密码
```
PUT /change/password
```
#####Request 

URI Parameters
```
    newPassword  string (required) 
 
```
 
#####Response  200
Headers

```
Content-Type: application/json
```

Body

```
{
  "code": 0,
  "msg": "成功",
  "data": null
}  
```
###修改手机号
```
PUT /change/tel
```
#####Request 

URI Parameters
```
    newTel  string (required) 
 
```
 
#####Response  200
Headers

```
Content-Type: application/json
```

Body

```
{
  "code": 0,
  "msg": "成功",
  "data": null
}  
{
  "code": 1,
  "msg": "手机号已被注册" 
  "data": null
}
```
###添加（修改）个人资料
```
POST /user/info
```
#####Request 

Headers
```
Content-Type: application/x-www-form-urlencoded
```

Body

```
{
    "introduction": "1234567"
    "age": "10"
    "city": "123"
    "sex": "男"  
}
```
#####Response  200
Headers

```
Content-Type: application/json
```

Body

```
{
  "code": 0,
  "msg": "成功",
  "data": null
}

```
###获取个人信息
```
GET /get/user/info
```
#####Request 

URI Parameters
```
null
```
 
#####Response  200
Headers

```
Content-Type: application/json
```

Body

```
{
  "code": 0,
  "msg": "成功",
  "data": {
          "id" :  //用户信息表的id不是用户id
          "username":
          "icon" :
          "introduction": "1234567"
          "age": "10"
          "city": "123"
          "sex": "男"  
  }
}

```