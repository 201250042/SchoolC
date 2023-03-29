package org.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import io.swagger.annotations.Api;
import java.util.HashMap;
import org.example.mapper.User_accountMapper;
import org.example.pojo.User_account;
import org.example.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "用户账户接口")
@CrossOrigin("*")
@SuppressWarnings("all")
@RestController
public class User_accountController {
  XStream xStream = new XStream(new StaxDriver());
  Gson gson = new Gson();
  @Autowired
  private User_accountMapper user_accountMapper;

  //添加用户账户
  @PostMapping("/user_account/add")
  public void addUserAccount(@RequestBody String user_accountXml){
    xStream.processAnnotations(User_account.class);
    User_account user_account = (User_account) xStream.fromXML(user_accountXml);
    user_accountMapper.insert(user_account);
  }

  //删除用户账户
  @PostMapping("/user_account/delete")
  public void deleteUserAccount(@RequestBody String user_accountXml){
    xStream.processAnnotations(User_account.class);
    User_account user_account = (User_account) xStream.fromXML(user_accountXml);
    user_accountMapper.deleteById(user_account);
  }

  //更改用户账户信息
  @PostMapping("user_account/update")
  public void updateUserAccount(@RequestBody String user_accountXml){
    xStream.processAnnotations(User_account.class);
    User_account user_account = (User_account) xStream.fromXML(user_accountXml);
    user_accountMapper.updateById(user_account);
  }

  //查询用户账户
  @GetMapping("/user_account")
  public String getAllUserAccount(){
//    System.out.println(xStream.toXML(user_accountMapper.findAllUserAccount()));
//    return gson.toJson(user_accountMapper.findAllUserAccount());
    xStream.processAnnotations(User_account.class);
    return xStream.toXML(user_accountMapper.findAllUserAccount());
  }


  // 登录
  @GetMapping("/login")
  public String login(@RequestParam String user_accountXml) {
    xStream.processAnnotations(User_account.class);
    User_account user_account = (User_account) xStream.fromXML(user_accountXml);
    QueryWrapper<User_account> userQueryWrapper = new QueryWrapper<>();
    userQueryWrapper.setEntity(user_account);
    if (user_accountMapper.selectOne(userQueryWrapper) == null) {
      return "error:查询不到用户信息，请先注册！";
    }
    String token = TokenUtil.getToken(user_account.getAcc());
    HashMap<String, String> res = new HashMap<>();
    res.put("acc", user_account.getAcc());
    res.put("token", token);
    return xStream.toXML(res);
  }

  // 注册
  @PostMapping("/register")
  public String register(@RequestBody String user_accountXml) {
    xStream.processAnnotations(User_account.class);
    User_account user_account = (User_account) xStream.fromXML(user_accountXml);
    user_accountMapper.insert(user_account);
    String token = TokenUtil.getToken(user_account.getAcc());
    HashMap<String, String> res = new HashMap<>();
    res.put("acc", user_account.getAcc());
    res.put("token", token);
    return xStream.toXML(res);
  }

  // 校验token
  @PostMapping("/checkToken")
  public String checkToken(@RequestBody HashMap<String, String> data) {
    String username = data.get("username");
    String token = data.get("token");
    if (username.equals(TokenUtil.parseToken(token))) {
      return "1";
    }
    return "0";
  }
}
