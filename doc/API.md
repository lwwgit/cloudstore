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

##文件分类和查找

###文件分类
```
POST /sort
```
####Request (获取文档类型的文件)
Headers
```
Content-Type:application/x-www-form-urlencoded
```
Body
```
{
    "flag": 1
 }
```

####Response 200
Headers
```
Content-Type: text/plain;
```
Body
```
{
    "code": 0,
    "msg": "成功",
    "data":[
        {
            "path": "/dir1/test1.docx",
            "length": 0,
            "modificationTime": "2018-07-03 10:27",
            "type": "docx",
            "name": "test1.docx"
          },
          {
            "path": "/dir1/test1.pptx",
            "length": 0,
            "modificationTime": "2018-07-03 10:26",
            "type": "pptx",
            "name": "test1.pptx"
          },
          {
            "path": "/dir2/test1.xlsx",
            "length": 6610,
            "modificationTime": "2018-07-03 10:28",
            "type": "xlsx",
            "name": "test1.xlsx"
          },
          {
            "path": "/dir2/test3.pptx",
            "length": 0,
            "modificationTime": "2018-07-03 10:28",
            "type": "pptx",
            "name": "test3.pptx"
          },
          {
            "path": "/dir3/详细报表.txt",
            "length": 10035,
            "modificationTime": "2018-07-04 09:37",
            "type": "txt",
            "name": "详细报表.txt"
          },
          {
            "path": "/dir3/shw/软件开发需求文档模板.pdf",
            "length": 401185,
            "modificationTime": "2018-07-04 09:37",
            "type": "pdf",
            "name": "软件开发需求文档模板.pdf"
          },
          {
            "path": "/dir3/shw/软件详细设计文档模板.doc",
            "length": 275969,
            "modificationTime": "2018-07-04 09:35",
            "type": "doc",
            "name": "软件详细设计文档模板.doc"
          },
          {
            "path": "/dir3/shw/test/test.txt",
            "length": 0,
            "modificationTime": "2018-06-29 11:14",
            "type": "txt",
            "name": "test.txt"
          },
          {
            "path": "/dir3/test2.docx",
            "length": 0,
            "modificationTime": "2018-07-03 10:29",
            "type": "docx",
            "name": "test2.docx"
          },
          {
            "path": "/dir3/test2.pptx",
            "length": 0,
            "modificationTime": "2018-07-03 10:29",
            "type": "pptx",
            "name": "test2.pptx"
          }
    ]
 }
```
####Request (获取图片类型的文件)
Headers
```
Content-Type:application/x-www-form-urlencoded
```
Body
```
{
    "flag": 2
 }
```

####Response 200
Headers
```
Content-Type: text/plain;
```
Body
```
{
    "code": 0,
    "msg": "成功",
    "data":[
      {
        "path": "/dir1/龙母.jpg",
        "length": 25798,
        "modificationTime": "2018-07-04 09:35",
        "type": "jpg",
        "name": "龙母.jpg"
       },
      {
        "path": "/dir3/shw/pjpj.jpg",
        "length": 25424,
        "modificationTime": "2018-06-28 16:15",
        "type": "jpg",
        "name": "pjpj.jpg"
      }
    ]
 }
```
####Request (获取视频类型的文件)
Headers
```
Content-Type:application/x-www-form-urlencoded
```
Body
```
{
    "flag": 3
 }
```

####Response 200
Headers
```
Content-Type: text/plain;
```
Body
```
[]
```
####Request (获取音乐类型的文件 )
Headers
```
Content-Type:application/x-www-form-urlencoded
```
Body
```
{
    "flag": 4
 }
```

####Response 200
Headers
```
Content-Type: text/plain;
```
Body
```
{
    "code": 0,
    "msg": "成功",
    "data":[
      {
        "path": "/dir1/Troye Sivan - WILD.mp3",
        "length": 8830245,
        "modificationTime": "2018-07-04 09:57",
        "type": "mp3",
        "name": "Troye Sivan - WILD.mp3"
       },
     {
        "path": "/dir3/shw/BTS - DNA.flac",
        "length": 29212161,
        "modificationTime": "2018-07-04 09:56",
        "type": "flac",
        "name": "BTS - DNA.flac"
     }
   ]
 }
```
####Request (获取其他类型的文件)
Headers
```
Content-Type:application/x-www-form-urlencoded
```
Body
```
 {
    "flag": 5
 }
```

####Response 200
Headers
```
Content-Type: text/plain;
```
Body
```
 {
    "code": 0,
    "msg": "成功",
    "data":[
      {
        "path": "/dir3/Git-2.18.0-64-bit.exe",
        "length": 41126928,
        "modificationTime": "2018-07-04 09:58",
        "type": "exe",
        "name": "Git-2.18.0-64-bit.exe"
      }
    ]
 }
```
###文件查找
```
POST /search/file
```
####Request
Headers
```
Content-Type:application/x-www-form-urlencoded
```
Body
```
{
    "SearchWord": 软件
 }
```
####Response 200
Headers
```
Content-Type: text/plain
```
Body
```
{
    "code": 0,
    "msg": "成功",
    "data":[
      {
        "path": "/dir3/shw/软件开发需求文档模板.pdf",
        "length": 401185,
        "modificationTime": "2018-07-04 09:37",
        "type": "pdf",
        "name": "软件开发需求文档模板.pdf"
      },
      {
        "path": "/dir3/shw/软件详细设计文档模板.doc",
        "length": 275969,
        "modificationTime": "2018-07-04 09:35",
        "type": "doc",
        "name": "软件详细设计文档模板.doc"
       }
    ]
 }
```
##文件分享
###生成分享链接
```
POST /get/shared/link
```
####Request
Headers
```
Content-Type: application/x-www-form-urlencoded
```
Body
```
{
    "filePath": /dir3, /dir1/test1.docx, /dir3/shw/pjpj.jpg
}
```
####Response
Headers
```
Content-Type: text/plain;charset=UTF-8
```
Body
```
{
  "code": 0,
  "msg": "成功",
  "data":  "http://localhost:8080/home/share?id=cDvo4bDK"
}
```

###查看分享文件
```
POST /home/share
```
####Request
Headers
```
Content-Type: application/x-www-form-urlencoded
```
Body
```
"http://localhost:8080/home/share?id=cDvo4bDK"
```
####Response 200
Headers
```
Content-Type: text/plain;charset=UTF-8
```
Body
```
{
  "code": 0,
  "msg": "成功",
  "data": [
    {
      "id": 32,
      "charId": "cDvo4bDK",
      "filename": "dir3",
      "owner": "victo",
      "size": 71051702,
      "type": "dir",
      "path": "/dir3"
    },
    {
      "id": 33,
      "charId": "cDvo4bDK",
      "filename": "test1.docx",
      "owner": "victo",
      "size": 0,
      "type": "docx",
      "path": "/dir1/test1.docx"
    },
    {
      "id": 34,
      "charId": "cDvo4bDK",
      "filename": "pjpj.jpg",
      "owner": "victo",
      "size": 25424,
      "type": "jpg",
      "path": "/dir3/shw/pjpj.jpg"
    }
  ]
}
```

##回收站管理
###文件单个删除进回收站
```
post /user/recycle/insert
```
####Request
Headers
```
Content-Type: application/x-www-form-urlencoded
```
Body
```
oriPath  /lww/test.txt
```
####Response 200
Headers
```
Content-Type: text/plain;charset=UTF-8
```
Body
```
{
    "code": 0,
    "msg": "成功",
    "data": null
}
```

###多个文件或文件夹删除进回收站
```
post  /user/recycle/insert/all
```
####Request
Headers
```
Content-Type: application/json
```
Body
```
["/lww/test2","/lww/test2.c"]
```
####Response 200
Headers
```
Content-Type: text/plain;charset=UTF-8
```
Body
```
{
    "code": 0,
    "msg": "成功",
    "data": null
}
```
###获取回收站文件
```
get  /user/recycle/get
```
####Response 200
Headers
```
Content-Type: text/plain;charset=UTF-8
```
Body
```
{
    "code": 0,
    "msg": "成功",
    "data": [
        {
            "recoveryId": 8,
            "username": "lww",
            "originalPath": "/lww/test2",
            "presentPath": "/lwwtmp/test2",
            "type": "dir"
        },
        {
            "recoveryId": 7,
            "username": "lww",
            "originalPath": "/lww/test.txt",
            "presentPath": "/lwwtmp/test.txt",
            "type": "txt"
        },
        {
            "recoveryId": 9,
            "username": "lww",
            "originalPath": "/lww/test2.c",
            "presentPath": "/lwwtmp/test2.c",
            "type": "c"
        }
    ]
}
```
###单个还原
```
post /user/recycle/restore
```
####Request
Headers
```
application/x-www-form-urlencoded
```
Body
```
id 7
```
####Response 200
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
###多个还原
```
post   /user/recycle/restore/all
```
####Request
Headers
```
Content-Type: application/json
```
Body
```
["8","9"]
```
####Response 200
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
###单个删除
```
post  /user/recycle/delete
```
####Request
Headers
```
Content-Type: application/x-www-form-urlencoded
```
Body
```
id 10
```
####Response 200
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
###多个删除
```
post  /user/recycle/delete/all
```
####Request
Headers
```
Content-Type: application/json
```
Body
```
["11","12"]
```
####Response 200
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
##目录树
###获取目录树
```
POST /get/tree
```
####Request
Headers
```
Content-Type: application/x-www-form-urlencoded
```
Body
```
{
    "path": /dir3
}
```
####Response 200
Headers
```
Content-Type: application/json
```
Body
```
{
  "code": 0,
  "msg": "成功",
  "data": [
    {
      "path": "/dir3/shw",
      "dirName": "shw",
      "child": "true"
    },
    {
      "path": "/dir3/shw2",
      "dirName": "shw2",
      "child": "true"
    },
    {
      "path": "/dir3/shw3",
      "dirName": "shw3",
      "child": "false"
    }
  ]
}
```