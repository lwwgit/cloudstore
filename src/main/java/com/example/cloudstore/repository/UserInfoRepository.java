package com.example.cloudstore.repository;

import com.example.cloudstore.domain.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserInfoRepository extends JpaRepository<UserInfo,Integer> {

     UserInfo findByUsername(String username);

     @Query("Select u from UserInfo u  where u.vip = 1")
     List<UserInfo> userInfoSelectVip();

     @Query("select u from UserInfo u where u.vip = 0")
     List<UserInfo> userInfoSelectNotVip();

     @Modifying
     @Transactional
     @Query("update UserInfo u set u.vip = 1 where u.username = ?1")
     int tobeVip( String username);

     @Modifying
     @Transactional
     @Query("update UserInfo u set u.vip = 0 where u.username = ?1")
     int cancleVip( String username);

     //开始年龄分布查询
     @Query("select count(u) from UserInfo  u where u.age >= 0 and u.age < 10")
     Integer shisuiyixia();
     @Query("select count(u) from UserInfo  u where u.age >= 10 and u.age < 19")
     Integer shi2shijiu();
     @Query("select count(u) from UserInfo  u where u.age >= 20 and u.age < 29")
     Integer ershi2erjiu();
     @Query("select count(u) from UserInfo  u where u.age >= 30 and u.age < 39")
     Integer sanshi2sanjiu();
     @Query("select count(u) from UserInfo  u where u.age >= 40 and u.age < 49")
     Integer sishi2sijiu();
     @Query("select count(u) from UserInfo  u where u.age >= 50 and u.age < 59")
     Integer wushi2wujiu();
     @Query("select count(u) from UserInfo  u where u.age >= 60")
     Integer liushiyishang();


     //开始地区分布查询
     @Query("select count(u) from UserInfo  u where u.city = '山东' or u.city='江苏' or u.city='安徽' or u.city='浙江' or u.city='福建' or u.city='上海'")
     Integer HuaDong();
     @Query("select count(u) from UserInfo  u where u.city='广东' or u.city='广西' or u.city='海南'")
     Integer HuaNan();
     @Query("select count(u) from UserInfo  u where u.city='湖北' or u.city='湖南' or u.city='河南' or u.city='江西'")
     Integer HuaZhong();
     @Query("select count(u) from UserInfo  u where u.city='北京' or u.city='天津' or u.city='河北' or u.city='山西' or u.city='内蒙古'")
     Integer HuaBei();
     @Query("select count(u) from UserInfo  u where u.city='宁夏' or u.city='新疆' or u.city='青海' or u.city='陕西' or u.city='甘肃'")
     Integer XiBei();
     @Query("select count(u) from UserInfo  u where u.city='四川' or u.city='云南' or u.city='贵州' or u.city='西藏' or u.city='重庆'")
     Integer XiNan();
     @Query("select count(u) from UserInfo  u where u.city='辽宁' or u.city='吉林' or u.city='黑龙江'")
     Integer DongBei();
     @Query("select count(u) from UserInfo  u where u.city='香港' or u.city='澳门' or u.city='台湾'")
     Integer GangAoTai();

     //开始各个省市的详细分布查询
     @Query("select count(u) from UserInfo  u where u.city = '山东'")
     Integer shandong();
     @Query("select count(u) from UserInfo  u where u.city = '江苏'")
     Integer jiangsu();
     @Query("select count(u) from UserInfo  u where u.city = '安徽'")
     Integer anhui();
     @Query("select count(u) from UserInfo  u where u.city = '浙江'")
     Integer zhejiang();
     @Query("select count(u) from UserInfo  u where u.city = '福建'")
     Integer fujian();
     @Query("select count(u) from UserInfo  u where u.city = '上海'")
     Integer shanghai();
     @Query("select count(u) from UserInfo  u where u.city = '广东'")
     Integer guangdong();
     @Query("select count(u) from UserInfo  u where u.city = '广西'")
     Integer guangxi();
     @Query("select count(u) from UserInfo  u where u.city = '海南'")
     Integer hainan();
     @Query("select count(u) from UserInfo  u where u.city = '湖北'")
     Integer hubei();
     @Query("select count(u) from UserInfo  u where u.city = '湖南'")
     Integer hunan();
     @Query("select count(u) from UserInfo  u where u.city = '河南'")
     Integer henan();
     @Query("select count(u) from UserInfo  u where u.city = '江西'")
     Integer jiangxi();
     @Query("select count(u) from UserInfo  u where u.city = '北京'")
     Integer beijing();
     @Query("select count(u) from UserInfo  u where u.city = '天津'")
     Integer tianjin();
     @Query("select count(u) from UserInfo  u where u.city = '河北'")
     Integer hebei();
     @Query("select count(u) from UserInfo  u where u.city = '山西'")
     Integer shanxi();
     @Query("select count(u) from UserInfo  u where u.city = '内蒙古'")
     Integer neimenggu();
     @Query("select count(u) from UserInfo  u where u.city = '宁夏'")
     Integer ningxia();
     @Query("select count(u) from UserInfo  u where u.city = '新疆'")
     Integer xinjiang();
     @Query("select count(u) from UserInfo  u where u.city = '青海'")
     Integer qinghai();
     @Query("select count(u) from UserInfo  u where u.city = '陕西'")
     Integer shanxi3();
     @Query("select count(u) from UserInfo  u where u.city = '甘肃'")
     Integer gansu();
     @Query("select count(u) from UserInfo  u where u.city = '四川'")
     Integer sichuan();
     @Query("select count(u) from UserInfo  u where u.city = '云南'")
     Integer yunnan();
     @Query("select count(u) from UserInfo  u where u.city = '贵州'")
     Integer guizhou();
     @Query("select count(u) from UserInfo  u where u.city = '西藏'")
     Integer xizang();
     @Query("select count(u) from UserInfo  u where u.city = '重庆'")
     Integer chongqing();
     @Query("select count(u) from UserInfo  u where u.city = '辽宁'")
     Integer liaoning();
     @Query("select count(u) from UserInfo  u where u.city = '吉林'")
     Integer jilin();
     @Query("select count(u) from UserInfo  u where u.city = '黑龙江'")
     Integer heilongjiang();
     @Query("select count(u) from UserInfo  u where u.city = '香港'")
     Integer xianggang();
     @Query("select count(u) from UserInfo  u where u.city = '澳门'")
     Integer aomen();
     @Query("select count(u) from UserInfo  u where u.city = '台湾'")
     Integer taiwan();

     @Query("select count(u) from UserInfo  u where u.vip = '1'")
     Integer CountVip();
     @Query("select count(u) from UserInfo  u where u.vip = '0'")
     Integer CountNoVip();
}
