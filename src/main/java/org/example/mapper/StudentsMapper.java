package org.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Select;
import org.example.pojo.Students;

public interface StudentsMapper extends BaseMapper<Students> {
  @Select("select * from students")
  public List<Students> findAllStudents();
}
