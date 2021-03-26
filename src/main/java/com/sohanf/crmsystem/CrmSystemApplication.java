package com.sohanf.crmsystem;

import com.sohanf.crmsystem.common.Constant;
import com.sohanf.crmsystem.entity.User;
import com.sohanf.crmsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.Date;

@SpringBootApplication
public class CrmSystemApplication {

    @Autowired
    private UserService userService;

    @PostConstruct
    public void initGlobalSuperUser() {
        User user = new User(Long.valueOf("1"), "super", "admin", "admin", new Date().toString(), "789456584", "admin@crm.com", "admin", true, Constant.DEFAULT_ROLE);
        userService.registration(user);
    }

    public static void main(String[] args) {
        SpringApplication.run(CrmSystemApplication.class, args);
    }

}
