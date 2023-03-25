package org.example.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.Data;

@Data
@XStreamAlias("课程")
public class Courses {
  @TableId("cno")
//  @XStreamAsAttribute
  private String cno;
  private String cnm;
  private Long ctm;
  private Long cpt;
  private String tec;
  private String pla;
  private String share;//共享课程标记,1 or 0
}
