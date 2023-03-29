package org.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.sun.org.apache.xerces.internal.impl.xs.models.XSAllCM;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import io.swagger.annotations.Api;
import org.example.mapper.Courses_selectionMapper;
import org.example.pojo.Courses_selection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "选课表接口")
@CrossOrigin("*")
@SuppressWarnings("all")
@RestController
public class Courses_selectionController {
  XStream xStream = new XStream(new StaxDriver());
//  Gson gson = new Gson();
  @Autowired
  private Courses_selectionMapper courses_selectionMapper;

  @GetMapping("/courses_selection")
  public String getAllCoursesSelectionTable(){
    xStream.processAnnotations(Courses_selection.class);
    return xStream.toXML(courses_selectionMapper.findAllCoursesSelectionTable());
  }

  @PostMapping("/courses_selection/add")
  public void addCoursesSelectionTable(@RequestBody String courses_selectionXml){
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

}
