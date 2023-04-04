package org.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import io.swagger.annotations.Api;
import java.util.HashMap;
import java.util.List;
import org.example.mapper.StudentsMapper;
import org.example.pojo.Students;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "学生信息接口")
@CrossOrigin("*")
@SuppressWarnings("all")
@RestController
public class StudentsController {
  XStream xStream = new XStream(new StaxDriver());
  Gson gson = new Gson();
  @Autowired
  private StudentsMapper studentsMapper;

//  // 分页查询学生
//  @PostMapping("/students/bypage")
//  public String getStudent(@RequestBody HashMap<String, Integer> data) {
//    int page = data.get("page");
//    int numberPerPage = data.get("number_per_page");
//    QueryWrapper<Students> studentsQueryWrapper = new QueryWrapper<>();
//    studentsQueryWrapper.last(String.format("limit %s,%s", page * numberPerPage - numberPerPage, numberPerPage));
//    List<Students> students = studentsMapper.selectList(studentsQueryWrapper);
//    return gson.toJson(students);
//  }
  //查询学生
  @GetMapping(value = "/students")
  public String getAllStudents(){
    xStream.processAnnotations(Students.class);
    return xStream.toXML(studentsMapper.findAllStudents());
  }

  // 获取学生总数
  @GetMapping("/students/get_students_count")
  public long getStudentsCount() {
    Long count = studentsMapper.selectCount(null);
    return count;
  }

  // 添加学生
  @GetMapping("/students/add")
  public void addStudent(@RequestParam String studentXml) {
    xStream.processAnnotations(Students.class);
    Students student = (Students) xStream.fromXML(studentXml);
    studentsMapper.insert(student);
  }

  // 删除学生
  @GetMapping("/students/delete")
  public void deleteStudent(@RequestParam String studentXml) {
    xStream.processAnnotations(Students.class);
    Students student = (Students) xStream.fromXML(studentXml);
    studentsMapper.deleteById(student);

  }

  // 修改学生信息
  @GetMapping("/students/update")
  public void updateStudent(@RequestParam String studentXml) {
    xStream.processAnnotations(Students.class);
    Students student = (Students) xStream.fromXML(studentXml);
    studentsMapper.updateById(student);
  }

  // 根据姓名模糊查询学生
  @GetMapping("/students/searchByName")
  public String searchStudent(@RequestParam String studentXml) {
    xStream.processAnnotations(Students.class);
    Students student = (Students) xStream.fromXML(studentXml);
    String snm = student.getSnm();
    QueryWrapper<Students> studentsQueryWrapper = new QueryWrapper<>();
    studentsQueryWrapper.like("snm", snm);
    List<Students> students = studentsMapper.selectList(studentsQueryWrapper);
    return xStream.toXML(students);
  }

//  @GetMapping("/students/searchBySno")
  public String searchStudentBySno(String cno){
    xStream.processAnnotations(Students.class);
    return xStream.toXML(studentsMapper.selectById(cno));
  }
}
