package org.example.controller;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import io.swagger.annotations.Api;
import org.example.mapper.Courses_selectionMapper;
import org.example.pojo.Courses;
import org.example.pojo.Courses_selection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

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

  @Autowired
  StudentsController tempStudentsController;

  @Autowired
  public RestTemplate restTemplate;

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
    if(!courses_selection.getCno().startsWith("30")){
      //传当前学生和选课信息给集成端
      //选课信息已有，学生信息通过sno查询
      //学生信息通过sno查询(在studentsController添加查询方法)
      String studentXml = tempStudentsController.searchStudentBySno(courses_selection.getSno());
      String curr = "c";
      String transTo;
      if(courses_selection.getCno().startsWith("10")){
        transTo = "a";
      }else{
        transTo = "b";
      }
      String url = "http://localhost:8081/integration/httpTest/?studentXml={value}&courses_selectionXml={value}&curr={value}&transTo={value}";
      String response = restTemplate.getForObject(url,String.class,studentXml,courses_selection,curr,transTo);
//      System.out.println("RESPONSE: "+response);
//      return trans;
    }else{
      courses_selectionMapper.insert(courses_selection);
//      return null;
    }
  }

  @GetMapping("/courses_selection/delete")
  public void deleteCoursesSelectionTable(@RequestParam String courses_selectionXml){
    xStream.processAnnotations(Courses_selection.class);
    Courses_selection courses_selection = (Courses_selection) xStream.fromXML(courses_selectionXml);
    courses_selectionMapper.deleteByCnoSno(courses_selection.getCno(),courses_selection.getSno());
  }

  @GetMapping("/courses_selection/update")
  public void updateCoursesSelectionTable(@RequestParam String courses_selectionXml){
    xStream.processAnnotations(Courses_selection.class);
    Courses_selection courses_selection = (Courses_selection) xStream.fromXML(courses_selectionXml);
    courses_selectionMapper.updateById(courses_selection);
  }

//  @GetMapping("/courses_selection/searchBySno")
//  public String searchBySno(@RequestParam String courses_selectionXml){
//    xStream.processAnnotations(Courses_selection.class);
//    Courses_selection  courses_selection = (Courses_selection) xStream.fromXML(courses_selectionXml);
//
//    List<Courses_selection> coursesSelectionList = courses_selectionMapper.findCoursesSelectionBySno(courses_selection.getSno());
//    List<Courses> coursesList = new ArrayList<>();
//    for (Courses_selection cs : coursesSelectionList) {
//      String currCno = cs.getCno();
//      coursesList.add(tempCourseController.searchByCno2(currCno));
//    }
//    xStream.processAnnotations(Courses.class);
//    return xStream.toXML(coursesList);
////    return xStream.toXML(courses_selectionMapper.findCoursesSelectionBySno(courses_selection.getSno()));
//  }

  @GetMapping("/courses_selectionarchBySno")
  public String searchBySno(@RequestParam String courses_selectionXml){
    xStream.processAnnotations(Courses_selection.class);
    Courses_selection  courses_selection = (Courses_selection) xStream.fromXML(courses_selectionXml);
    return xStream.toXML(courses_selectionMapper.findCoursesSelectionBySno(courses_selection.getSno()));

  }

//  @GetMapping("/courses_selection/searchBySno")
//  public String searchBySno(@RequestParam String courses_selectionXml){
//    xStream.processAnnotations(Courses_selection.class);
//    Courses_selection  courses_selection = (Courses_selection) xStream.fromXML(courses_selectionXml);
//    return xStream.toXML(courses_selectionMapper.findCoursesSelectionBySno(courses_selection.getSno()));
//
//  }


}
