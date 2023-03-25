package org.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.pojo.Courses;

public interface CoursesMapper extends BaseMapper<Courses> {
  @Select("select * from courses")
  public List<Courses> findAllCourses();

  @Select("select * from courses where cno = ${cno}")
  public Courses findByCno(@Param("cno") String cno);
}
