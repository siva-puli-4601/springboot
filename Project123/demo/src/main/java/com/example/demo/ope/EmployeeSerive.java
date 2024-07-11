package com.example.demo.ope;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
@Service
public class EmployeeSerive extends Exception {
    @Autowired
    JdbcTemplate jt;
    public EmployeeSerive(){};
    public EmployeeSerive(String string) {
        super(string);
    }
    
    @SuppressWarnings("rawtypes")
    public int selectUsers(Employee e) throws EmployeeSerive
    {
        String uname=e.getUname();
        String password=e.getPassword();
        String s="select * from EmployeeDetails where workmail='"+uname+"@miraclesoft' and password='"+password+"' and role in (select id from Role where role='Hr' or role='Admin')";
        List<Map<String,Object>> map=jt.queryForList(s);
        if(map.size()>0)
        {
        String fname=e.getFname();
        String lname=e.getLname();
        String email=e.getGmail();
        String dept=e.getDept();
        String phone=e.getPhone();
        String location=e.getLocation();
        String pincode=e.getPincode();
        double salary=e.getSalary();
        String role=e.getRole();
        String ss="select id from Role where role="+"'"+role+"'";
        List<Map<String,Object>> res=jt.queryForList(ss);
        int roleid=0;
        for(Map.Entry m:res.get(0).entrySet())
        {
             roleid=Integer.parseInt(m.getValue().toString());
              
        }
        LocalDateTime myDateObj = LocalDateTime.now();
         DateTimeFormatter dt = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
         String date = myDateObj.format(dt);
         String workmail=String.valueOf(fname.charAt(0))+lname+"@miraclesoft";
         String password1=lname.substring(lname.length()-2)+"@"+fname.substring(fname.length()-2)+date.substring(12,17);
         String sql1="insert into EmployeeDetails(Fname,Lname,Dept,Phno,JoinDate,Gmail,workmail,password,location,pincode,salary,role,createby) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
         int i=jt.update(sql1,fname,lname,dept,phone,date,email,workmail,password1,location,pincode,salary,roleid,uname);
         return i;
        }
        else{
            
            throw new EmployeeSerive("Un authorized person insert");
            
        }
        
    }


    
}
