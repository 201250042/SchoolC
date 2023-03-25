package org.example.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.Data;

@Data
@XStreamAlias("选课")
public class Courses_selection {
  @TableId("cno")
//  @XStreamAsAttribute
  private String cno;//课程编号
//  @XStreamAsAttribute
  private String sno;//学生学号
//  @XStreamAsAttribute
  private Long grd;//成绩
}
