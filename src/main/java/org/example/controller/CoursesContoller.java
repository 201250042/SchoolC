package org.example.controller;

import com.google.gson.Gson;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import io.swagger.annotations.Api;
import java.io.Serializable;
import org.example.mapper.CoursesMapper;
import org.example.pojo.Courses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

  @PostMapping("/courses/add")
  public void addCourse(@RequestBody String courseXml){
    xStream.processAnnotations(Courses.class);
    Courses course = (Courses) xStream.fromXML(courseXml);
    coursesMapper.insert(course);
  }

  @PostMapping("/courses/delete")
  public void deleteCourse(@RequestBody String courseXml){
    xStream.processAnnotations(Courses.class);
    Courses course = (Courses) xStream.fromXML(courseXml);
    coursesMapper.deleteById(course);
  }

  @PostMapping("/courses/update")
  public void updateCourse(@RequestBody String courseXml){
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

  @PostMapping("/courses/sendSharedCourse")
  public String sendSharedCourse(@RequestBody String courseXml){
    xStream.processAnnotations(Courses.class);
    Courses sharedCourse = (Courses) xStream.fromXML(courseXml);
    sharedCourse.setShare("1");
    coursesMapper.updateById(sharedCourse);
    return xStream.toXML(coursesMapper.findByCno(sharedCourse.getCno()));
  }

  @PostMapping("/courses/receiveSharedCourse")
  public void receiveSharedCourse(@RequestBody String courseXml){
    addCourse(courseXml);
  }

}
