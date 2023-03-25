package org.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.pojo.Courses_selection;

public interface Courses_selectionMapper extends BaseMapper<Courses_selection> {
  @Select("select * from courses_selection")
  public List<Courses_selection> findAllCoursesSelectionTable();

  @Delete("delete from courses_selection where cno = ${cno} and sno = ${sno}")
  public void deleteByCnoSno(@Param("cno") String cno,@Param("sno") String sno);
}