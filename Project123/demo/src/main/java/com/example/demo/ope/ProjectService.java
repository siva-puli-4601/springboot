package com.example.demo.ope;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProjectService extends Exception{
    ProjectService(){};
    ProjectService(String s)
    {
        super(s);
    }
    @Autowired
    JdbcTemplate jt;
    @SuppressWarnings("rawtypes")
    public int addProject(Project p) throws ProjectService
    {
        String uname=p.getUname();
        String password=p.getPassword();
        String s="select * from EmployeeDetails where workmail='"+uname+"@miraclesoft' and password='"+password+"' and role in (select id from Role where role='Hr' or role='Admin')";
        List<Map<String,Object>> map=jt.queryForList(s);
        if(map.size()>0)
        {
           int dur=p.getDuration();
           LocalDateTime currentDate = LocalDateTime.now();
           LocalDateTime futureDate = currentDate.plusMonths(dur);
           DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
           String feu= futureDate.format(formatter);
           String cur=currentDate.format(formatter);
           String hrname=p.getHrname();
           
           String ss="select EmpId from EmployeeDetails where workmail="+"'"+hrname+"@miraclesoft'";
           List<Map<String,Object>> res=jt.queryForList(ss);
           if(res.size()==0)
           {
            throw new ProjectService("invalid hr");
           }
           int roleid=0;
           for(Map.Entry m:res.get(0).entrySet())
           {
               roleid=Integer.parseInt(m.getValue().toString());
             
           }
           String pmr=p.getPman();
           String sss="select EmpId from EmployeeDetails where workmail="+"'"+pmr+"@miraclesoft'";
           List<Map<String,Object>> re=jt.queryForList(sss);
           if(re.size()==0)
           {
            throw new ProjectService("invalid project manager");
           }
           int rolenum=0;
           for(Map.Entry m:re.get(0).entrySet())
           {
               rolenum=Integer.parseInt(m.getValue().toString());
             
           }
           String pname=p.getPname();
           String client=p.getClient();
           String sql="insert into project(pname,clientname,createby,startdate,enddate,duration,hr,pmr) values(?,?,?,?,?,?,?,?)";
           int i=jt.update(sql,pname,client,uname,cur,feu,dur,roleid,rolenum);
           return i;
        }
        else
        {
            throw new ProjectService("invalid login");
        }
    }
}
