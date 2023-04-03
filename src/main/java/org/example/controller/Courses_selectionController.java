package org.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.sun.org.apache.xerces.internal.impl.xs.models.XSAllCM;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import io.swagger.annotations.Api;
import org.example.mapper.Courses_selectionMapper;
import org.example.pojo.Courses;
import org.example.pojo.Courses_selection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Api(tags = "选课表接口")
@CrossOrigin("*")
@SuppressWarnings("all")
@RestController
public class Courses_selectionController {
  XStream xStream = new XStream(new StaxDriver());
//  Gson gson = new Gson();
  @Autowired
  private Courses_selectionMapper courses_selectionMapper;

  @Autowired
  CoursesContoller tempCourseController;

  @GetMapping("/courses_selection")
  public String getAllCoursesSelectionTable(){
    xStream.processAnnotations(Courses_selection.class);
    return xStream.toXML(courses_selectionMapper.findAllCoursesSelectionTable());
  }

  @GetMapping("/courses_selection/add")
  public void addCoursesSelectionTable(@RequestParam String courses_selectionXml){
    System.out.println("frontend return: " + courses_selectionXml);
    xStream.processAnnotations(Courses_selection.class);
    Courses_selection courses_selection = (Courses_selection) xStream.fromXML(courses_selectionXml);
    courses_selectionMapper.insert(courses_selection);
  }

  @PostMapping("/courses_selection/delete")
  public void deleteCoursesSelectionTable(@RequestBody String courses_selectionXml){
    xStream.processAnnotations(Courses_selection.class);
    Courses_selection courses_selection = (Courses_selection) xStream.fromXML(courses_selectionXml);
    courses_selectionMapper.deleteByCnoSno(courses_selection.getCno(),courses_selection.getSno());
  }

  @PostMapping("/courses_selection/update")
  public void updateCoursesSelectionTable(@RequestBody String courses_selectionXml){
    xStream.processAnnotations(Courses_selection.class);
    Courses_selection courses_selection = (Courses_selection) xStream.fromXML(courses_selectionXml);
    courses_selectionMapper.updateById(courses_selection);
  }

  @GetMapping("/courses_selection/searchBySno")
  public String searchBySno(@RequestParam String courses_selectionXml){
    xStream.processAnnotations(Courses_selection.class);
    Courses_selection  courses_selection = (Courses_selection) xStream.fromXML(courses_selectionXml);

    List<Courses_selection> coursesSelectionList = courses_selectionMapper.findCoursesSelectionBySno(courses_selection.getSno());
//    System.out.println(coursesSelectionList);
    List<Courses> coursesList = new ArrayList<>();
    for (Courses_selection cs : coursesSelectionList) {
      System.out.println(cs);
      String currCno = cs.getCno();
      coursesList.add(tempCourseController.searchByCno2(currCno));
    }
    xStream.processAnnotations(Courses.class);
    return xStream.toXML(coursesList);
//    return xStream.toXML(courses_selectionMapper.findCoursesSelectionBySno(courses_selection.getSno()));
  }

}
