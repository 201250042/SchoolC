package org.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Select;
import org.example.pojo.User_account;

public interface User_accountMapper extends BaseMapper<User_account> {
  @Select("select * from user_account;")
  public List<User_account> findAllUserAccount();
}
