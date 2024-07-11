package com.example.demo.ope;
import java.text.ParseException;
import java.util.*;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/emp")
public class EmployeeController {
    @Autowired
    SivaOpe so;
    @Autowired
    EmployeeSerive es;
    @Autowired
    ProjectService ps;
    @Autowired
    ProjectEmpDetailsService pes;
    @GetMapping("/search")
    public String search()
    {
    	//System.out.println("hai");
    	return "hello";
    }
    @PostMapping("/employees")
    public String selectUsers(@RequestBody Employee e) 
    {
        try{
        int res=es.selectUsers(e);
        if(res>0)
        return "fine";
        return "not fine";
        }
        catch(EmployeeSerive ee)
        {
         return ee.getMessage();
        }
    }
    @PostMapping("/addProject")
    public String addProject(@RequestBody Project p)
    {
        try{
            int res=ps.addProject(p);
            if(res==0)
            return "fail to add";
            else
            return "fine";
        }catch(ProjectService e)
        {
            return e.getMessage();
        }
    }
    @PostMapping("/addprojectemp")
    public String addProjectEmp(@RequestBody ProjectDetails pd)
    {
        try{
         int res=pes.addProjectEmp(pd);
         if(res==1)
         return "fine added project details";
         else
         return "fail";
        }catch(ProjectEmpDetailsService e)
        {
         return e.getMessage();
        }
    }
    @GetMapping("/displayProjectDetails")
    public List<Map<String,Object>> display()
    {
        List<Map<String,Object>> res=pes.disply();
        System.out.println(res);
        return res;
    }
    @PutMapping("/addtask")
    public String taskList(@RequestBody TaskList tl)
    {
        int res;
        try {
            res = pes.taskList(tl);
        } catch (ProjectEmpDetailsService | ParseException e) {
            return e.getMessage();
        }
        if(res==0)
        return "fail";
        return "fine";
    }
}
