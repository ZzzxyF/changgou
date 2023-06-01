package com.zxy.users.dao;


import com.zxy.users.pojo.Address;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/****
 * @Author:admin
 * @Description:Address的Dao
 * @Date 2019/6/14 0:12
 *****/

public interface AddressMapper extends Mapper<Address> {
    List<Address> findByUserName(String UserName);
}
