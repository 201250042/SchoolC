package org.example.controller;

import com.google.gson.Gson;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import io.swagger.annotations.Api;
import java.io.Serializable;
import org.example.mapper.CoursesMapper;
import org.example.mapper.Courses_selectionMapper;
import org.example.pojo.Courses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "课程接口")
@CrossOrigin("*")
@SuppressWarnings("all")
@RestController
public class CoursesContoller {
  XStream xStream = new XStream(new StaxDriver());
//  Gson gson = new Gson();
  @Autowired
  private CoursesMapper coursesMapper;

  @GetMapping("/courses")
  public String getAllCourses(){
    xStream.processAnnotations(Courses.class);
    return xStream.toXML(coursesMapper.findAllCourses());
  }

  @GetMapping("/courses/add")
  public void addCourse(@RequestParam String courseXml){
    xStream.processAnnotations(Courses.class);
    Courses course = (Courses) xStream.fromXML(courseXml);
    coursesMapper.insert(course);
  }

  @GetMapping("/courses/delete")
  public void deleteCourse(@RequestParam String courseXml){
    xStream.processAnnotations(Courses.class);
    Courses course = (Courses) xStream.fromXML(courseXml);
    coursesMapper.deleteById(course);
  }

  @GetMapping("/courses/update")
  public void updateCourse(@RequestParam String courseXml){
    xStream.processAnnotations(Courses.class);
    Courses course = (Courses) xStream.fromXML(courseXml);
    coursesMapper.updateById(course);
  }

  @GetMapping("courses/searchByCno")
  public String searchByCno(String courseXml){
    xStream.processAnnotations(Courses.class);
    Courses course = (Courses) xStream.fromXML(courseXml);
    return xStream.toXML(coursesMapper.findByCno(course.getCno()));
  }

  public Courses searchByCno2(String cno){
    return coursesMapper.findByCno(cno);
  }

  @GetMapping("/courses/sendSharedCourse")
  public String sendSharedCourse(@RequestParam String courseXml){
    xStream.processAnnotations(Courses.class);
    Courses sharedCourse = (Courses) xStream.fromXML(courseXml);
    sharedCourse.setShare("1");
    coursesMapper.updateById(sharedCourse);
    return xStream.toXML(coursesMapper.findByCno(sharedCourse.getCno()));
  }

  @GetMapping("/courses/receiveSharedCourse")
  public void receiveSharedCourse(@RequestParam String courseXml){
    addCourse(courseXml);
  }

}
