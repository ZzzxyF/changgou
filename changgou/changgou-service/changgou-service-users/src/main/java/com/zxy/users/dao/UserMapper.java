package com.zxy.users.dao;
import com.zxy.users.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

/****
 * @Author:admin
 * @Description:User的Dao
 * @Date 2019/6/14 0:12
 *****/
public interface UserMapper extends Mapper<User> {
  @Update("UPDATE TB_USER SET POINTS=POINTS+#{point} WHERE USERNAME=#{username}")
  int addUserPoints(@Param("username") String username,@Param("point") Integer point);
}
