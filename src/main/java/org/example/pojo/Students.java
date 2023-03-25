package org.example.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.Data;

@Data
@XStreamAlias("学生")
public class Students {
  @TableId("sno")
//  @XStreamAsAttribute
  private String sno;
  private String snm;
  private String sex;
  private String sde;
  private String pwd;
}
