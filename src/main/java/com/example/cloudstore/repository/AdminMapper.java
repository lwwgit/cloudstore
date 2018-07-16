package com.example.cloudstore.repository;




//public interface AdminMapper {
//    @Select("select * from sys_user")
//    List<SysUser> userSelect();
//
//    @Select("select * from sys_user where username = #{username}")
//    SysUser usernameSelect(@Param(value = "username") String username);
//
//    @Select("Select * from user_info where username = #{username}")
//    UserInfo userInfoSelect(@Param(value = "username") String username);

//    @Select("Select * from user_info where vip = 1")
//    List<UserInfo> userInfoSelectVip();
//
//    @Select("Select * from user_info where vip = 0")
//    List<UserInfo> userInfoSelectNotVip();

//    @Update("Update user_info Set vip=1 where username = #{username}")
//    boolean tobeVip(@Param(value = "username") String username);

//    @Update("Update user_info Set vip=0 where username = #{username}")
//    boolean cancleVip(@Param(value = "username") String username);

//    @Update("Update sys_user Set `state`=1 where username = #{username}")
//    boolean modeEnable(@Param(value = "username") String username);
//
//    @Update("Update sys_user Set `state`=0 where username = #{username}")
//    boolean modeFreeze(@Param(value = "username") String username);
//
//    @Update("Update sys_user Set com = 1 where username = #{username}")
//    boolean comSub(@Param(value = "username") String username);
//
//    @Update("Update sys_user Set com = 0 where username = #{username}")
//    boolean comCan(@Param(value = "username") String username);

//    @Insert("Insert into complaint(username,cominfo) values (#{username},#{cominfo})")
//    boolean comInsert(@Param("username") String username, @Param("cominfo") String cominfo);

//    @Select("Select * from complaint where username = #{username}")
//    Complaint comSelect(@Param("username") String username);

//    @Delete("Delete from complaint where username = #{username}")
//    Boolean deleteCom(@Param("username") String username);

//    //开始年龄分布查询
//    @Select("select count(*) from user_info where age > 0 and age <= 10")
//    Integer Zero2Ten();
//    @Select("select count(*) from user_info where age >10 and age <= 20")
//    Integer Ten2Twenty();
//    @Select("select count(*) from user_info where age >20 and age <= 30")
//    Integer Twenty2Thirty();
//    @Select("select count(*) from user_info where age >30 and age <= 40")
//    Integer Thirty2Forty();
//    @Select("select count(*) from user_info where age >40 and age <= 50")
//    Integer Forty2Fifty();
//    @Select("select count(*) from user_info where age >50 and age <= 60")
//    Integer Fifty2Sixty();
//    @Select("select count(*) from user_info where age > 60")
//    Integer MoerThanSixty();

//    //开始地区分布查询
//    @Select("select count(*) from user_info where city='山东' or city='江苏' or city='安徽' or city='浙江' or city='福建' or city='上海'")
//    Integer HuaDong();
//    @Select("select count(*) from user_info where city='广东' or city='广西' or city='海南'")
//    Integer HuaNan();
//    @Select("select count(*) from user_info where city='湖北' or city='湖南' or city='河南' or city='江西'")
//    Integer HuaZhong();
//    @Select("select count(*) from user_info where city='北京' or city='天津' or city='河北' or city='山西' or city='内蒙古'")
//    Integer HuaBei();
//    @Select("select count(*) from user_info where city='宁夏' or city='新疆' or city='青海' or city='陕西' or city='甘肃'")
//    Integer XiBei();
//    @Select("select count(*) from user_info where city='四川' or city='云南' or city='贵州' or city='西藏' or city='重庆'")
//    Integer XiNan();
//    @Select("select count(*) from user_info where city='辽宁' or city='吉林' or city='黑龙江'")
//    Integer DongBei();
//    @Select("select count(*) from user_info where city='香港' or city='澳门' or city='台湾'")
//    Integer GangAoTai();
//}
