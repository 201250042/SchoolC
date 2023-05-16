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
  String intergrationUrl = "192.168.43.195:8081";
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
      String url = "http://"+intergrationUrl+"/integration/httpTest/?studentXml={value}&courses_selectionXml={value}&curr={value}&transTo={value}";
      String response = restTemplate.getForObject(url,String.class,studentXml,courses_selectionXml,curr,transTo);
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
    String studentXml = tempStudentsController.searchStudentBySno(courses_selection.getSno());
    if(!courses_selection.getCno().startsWith("30")){
//      String studentXml = tempStudentsController.searchStudentBySno(courses_selection.getSno());
      String curr = "c";
      String transTo;
      if(courses_selection.getCno().startsWith("10")){
        transTo = "a";
      }else{
        transTo = "b";
      }
      String url = "http://"+intergrationUrl+"/integration/httpTestDelete/?studentXml={value}&courses_selectionXml={value}&curr={value}&transTo={value}";
      String response = restTemplate.getForObject(url,String.class,studentXml,courses_selectionXml,curr,transTo);
    }else {
      courses_selectionMapper.deleteByCnoSno(courses_selection.getCno(),courses_selection.getSno());
      if(!courses_selection.getSno().startsWith("30")){
        if(!courses_selectionMapper.checkCSTableExists(courses_selection.getSno())){
          tempStudentsController.deleteStudent(studentXml);
        }
      }
    }

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

  @GetMapping("/courses_selection/searchBySno")
  public String searchBySno(@RequestParam String courses_selectionXml){
    xStream.processAnnotations(Courses_selection.class);
    Courses_selection  courses_selection = (Courses_selection) xStream.fromXML(courses_selectionXml);
    return xStream.toXML(courses_selectionMapper.findCoursesSelectionBySno(courses_selection.getSno()));

  }
  @GetMapping("/courses_selection/getStudentDistribution")
  public List getStudentDistribution(@RequestParam String cno){
    List<Integer> res = new ArrayList<>();//res[0]为A学院学生数,res[1]为B学院,res[2]为C学院
    res.add(0);
    res.add(0);
    res.add(0);
    xStream.processAnnotations(Courses_selection.class);
    List<Courses_selection> coursesSelectionList = courses_selectionMapper.findCoursesSelectionByCno(cno);
    for (Courses_selection courses_selection:coursesSelectionList) {
      String tempSno = courses_selection.getSno();
      if (tempSno.startsWith("10")){
        res.set(0,res.get(0)+1);
      }else if(tempSno.startsWith("20")){
        res.set(1,res.get(1)+1);
      }else if(tempSno.startsWith("30")){
        res.set(2,res.get(2)+1);
      }
    }
    return res;
  }


  @GetMapping("/courses_selection/getGradeDistribution")
  public List getGradeDistribution(@RequestParam String cno){
    List<Integer> res = new ArrayList<>();//成绩设置3段，【0，60），【60，90），【90，100】，res[0]为【0，60）,res[1]为【60，90）,res[2]为【90，100】
    res.add(0);
    res.add(0);
    res.add(0);
    xStream.processAnnotations(Courses_selection.class);
    List<Courses_selection> coursesSelectionList = courses_selectionMapper.findCoursesSelectionByCno(cno);
    for (Courses_selection courses_selection:coursesSelectionList) {
      int tempGrd = courses_selection.getGrd().intValue();
      if (tempGrd<60){
        res.set(0,res.get(0)+1);
      }else if(tempGrd>=60 && tempGrd<90){
        res.set(1,res.get(1)+1);
      }else if(tempGrd>=90){
        res.set(2,res.get(2)+1);
      }
    }
    return res;
  }

  @GetMapping("/courses_selection/getMostPopularCourse")
  public List getMostPopularCourse(){
    List<Integer> res = new ArrayList<>();//Cno:3001-3010,res[0]-res[9]以此为3001-3010的选课人数
    List<String> res2 = new ArrayList<>();//课程名
    for (int i=0;i<10;i++){
      res.add(0);
    }
    res2.add("操作系统:");
    res2.add("计算机网络:");
    res2.add("软件工程:");
    res2.add("云计算:");
    res2.add("嵌入式:");
    res2.add("web前端:");
    res2.add("自动化分析:");
    res2.add("大数据分析:");
    res2.add("商务智能:");
    res2.add("数据结构:");
    List<Courses_selection> coursesSelectionList = courses_selectionMapper.findAllCoursesSelectionTable();
    for (Courses_selection courses_selection:coursesSelectionList) {
      String tempCno = courses_selection.getCno();
      if(tempCno.equals("3001")){
        res.set(0,res.get(0)+1);
      }else if(tempCno.equals("3002")){
        res.set(1,res.get(1)+1);
      }else if(tempCno.equals("3003")){
        res.set(2,res.get(2)+1);
      }else if(tempCno.equals("3004")){
        res.set(3,res.get(3)+1);
      }else if(tempCno.equals("3005")){
        res.set(4,res.get(4)+1);
      }else if(tempCno.equals("3006")){
        res.set(5,res.get(5)+1);
      }else if(tempCno.equals("3007")){
        res.set(6,res.get(6)+1);
      }else if(tempCno.equals("3008")){
        res.set(7,res.get(7)+1);
      }else if(tempCno.equals("3009")){
        res.set(8,res.get(8)+1);
      }else if(tempCno.equals("3010")){
        res.set(9,res.get(9)+1);
      }
    }
    for(int i=0;i<10;i++){
      res2.set(i,res2.get(i)+res.get(i));
    }
    return res2;
  }
}
