package org.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.pojo.Students;

public interface StudentsMapper extends BaseMapper<Students> {
  @Select("select * from students")
  public List<Students> findAllStudents();

  @Select("SELECT EXISTS(SELECT 1 FROM students WHERE sno=#{sno})")
  boolean checkStudentExists(@Param("sno") String sno);

}
