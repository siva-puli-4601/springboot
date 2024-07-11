package com.example.demo.ope;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
@Service
public class SivaOpe {
    @Autowired
    JdbcTemplate jt;
    public int create()
    {
    String sql="create table sivareddypuli(id int,name varchar(20))";
    jt.execute(sql);
    return 1;
    
    }
}
