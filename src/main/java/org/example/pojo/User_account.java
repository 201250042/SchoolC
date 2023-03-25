package org.example.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import java.sql.Timestamp;
import lombok.Data;

@Data
@XStreamAlias("用户账户")
public class User_account {
  @TableId("acc")
//  @XStreamAsAttribute
  private String acc;
//  @XStreamAsAttribute
  private String passwd;
//  @XStreamAsAttribute
  private Timestamp createdate;
}
