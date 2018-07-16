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
     @Query("select count(u) from UserInfo  u where u.age > 0 and u.age <= 10")
     Integer Zero2Ten();
     @Query("select count(u) from UserInfo  u where u.age > 10 and u.age <= 20")
     Integer Ten2Twenty();
     @Query("select count(u) from UserInfo  u where u.age > 20 and u.age <= 30")
     Integer Twenty2Thirty();
     @Query("select count(u) from UserInfo  u where u.age > 30 and u.age <= 40")
     Integer Thirty2Forty();
     @Query("select count(u) from UserInfo  u where u.age > 40 and u.age <= 50")
     Integer Forty2Fifty();
     @Query("select count(u) from UserInfo  u where u.age > 50 and u.age <= 60")
     Integer Fifty2Sixty();
     @Query("select count(u) from UserInfo  u where u.age > 60")
     Integer MoerThanSixty();


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

}
