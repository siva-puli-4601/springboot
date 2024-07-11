package com.example.demo.ope;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProjectEmpDetailsService extends Exception{
    ProjectEmpDetailsService(){};
    ProjectEmpDetailsService(String s)
    {
        super(s);
    }
    @Autowired
    JdbcTemplate jt;
    @SuppressWarnings("rawtypes")
    public int addProjectEmp(ProjectDetails pd) throws ProjectEmpDetailsService
    {
        String uname=pd.getUname();
        String s="select * from EmployeeDetails where workmail='"+uname+"@miraclesoft'  and role in (select id from Role where role='Hr' or role='ProjectManager')";
        List<Map<String,Object>> map=jt.queryForList(s);
        if(map.size()>0)
        {
           int empid=0;
           String emp=pd.getEmp();
           String esql="select Empid from EmployeeDetails where workmail='"+emp+"@miraclesoft';";
           System.out.println(esql);
           List<Map<String,Object>> map1=jt.queryForList(esql);
           if(map1.size()==0)
           {
            throw new ProjectEmpDetailsService("employee id not found first add employee");
           }
           for(Map.Entry m:map1.get(0).entrySet())
           {
            empid=Integer.parseInt(m.getValue().toString());
           }
           int proid=0;
           String proname=pd.getProject();
           String psql="select pid from project where pname='"+proname+"'";
           List<Map<String,Object>> map12=jt.queryForList(psql);
           if(map12.size()==0)
           {
            throw new ProjectEmpDetailsService("project  not found first add project");
           }
           for(Map.Entry m:map12.get(0).entrySet())
           {
            proid=Integer.parseInt(m.getValue().toString());
           }
           String lead=pd.getLead();
          String lsql="select * from ProjectEmployeeDetails where leadofemp='"+lead+"'and pid!="+proid+"";
          
          List<Map<String,Object>> map123=jt.queryForList(lsql);
          if(map123.size()!=0)
          {
            throw new ProjectEmpDetailsService("lead already in another project");
          }
          String isql="insert into ProjectEmployeeDetails values(?,?,?,?,?)";
          String empass=pd.getAssigndate();
          int i=jt.update(isql,empid,proid,empass,lead,uname);
          return i;

        }
        else
        throw new ProjectEmpDetailsService("efailed to login bcz unathorized uname provide");
        
    }
    public List<Map<String,Object>> disply()
    {
      List<Map<String,Object>> res=new ArrayList<>();
      String pidsql="select pid from project";
      List<Map<String,Object>> pids=jt.queryForList(pidsql);
      for(Map<String,Object> map:pids)
      {
        int id=Integer.parseInt(map.get("pid").toString());
        String pdsql="select * from project where pid="+id;
        List<Map<String,Object>> pd=jt.queryForList(pdsql);
        String empnamessql="select empid from ProjectEmployeeDetails where pid="+id;
        ArrayList<Integer> arr=new ArrayList<>();
        List<Map<String,Object>> empnaes=jt.queryForList(empnamessql);
        for(Map<String,Object> m1:empnaes)
        {
          arr.add(Integer.parseInt(m1.get("empid").toString()));
        }
        pd.get(0).put("empids",arr);
        res.add(pd.get(0));
      }
      return res;
    }

    public int taskList(TaskList tl) throws ProjectEmpDetailsService, ParseException
    {
      String name=tl.getAssignto();
      String sql="select Empid from EmployeeDetails where workmail='"+name+"@miraclesoft'";
      List<Map<String,Object>> lis=jt.queryForList(sql);
      if(lis.size()==0)
      throw new ProjectEmpDetailsService("invaild employee");
      int id=Integer.parseInt(lis.get(0).get("Empid").toString());
      String uname=tl.getUname();
      String sql1="select * from ProjectEmployeeDetails where leadofemp='"+uname+"' and empid="+id;
      List<Map<String,Object>> lis1=jt.queryForList(sql1);
      if(lis1.size()==0)
      throw new ProjectEmpDetailsService("the lead have no chances to add task for emp");
         LocalDateTime myDateObj = LocalDateTime.now();
         DateTimeFormatter dt = DateTimeFormatter.ofPattern("dd-MM-yyyy");
         String date = myDateObj.format(dt);
         SimpleDateFormat sdf= new SimpleDateFormat("dd-MM-yyyy");
         Date date1 = (Date) sdf.parse(date);
         String cc=tl.getStartdate();
         Date date2=(Date) sdf.parse(cc);
         int val=(int) (date2.getTime()-date1.getTime());
         if(val<0)
         throw new ProjectEmpDetailsService("starting date must be after the assign date");
         int dur=tl.getDur();
         int ele=dur/8;
         if(dur%8!=0)
         ele=ele+1;
         long ltime = date2.getTime()+(ele-1)*24*60*60*1000;
         Date enddate = new Date(ltime);
         String status=tl.getStatus();
         String des=tl.getDes();
         String taskname=tl.getTname();
         String dur1=String.valueOf(dur);
         String isql="insert into tasklist(taskname,createby,assignto,assigndate,duration,startdate,enddate,status,des) values(?,?,?,?,?,?,?,?,?)";
         int i=jt.update(isql,taskname,uname,name, date1,dur1,date2,enddate,status,des);
         return i;
    }
}
