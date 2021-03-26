package com.sohanf.crmsystem.controller;

import com.sohanf.crmsystem.entity.AuthRequest;
import com.sohanf.crmsystem.entity.Student;
import com.sohanf.crmsystem.entity.Teacher;
import com.sohanf.crmsystem.entity.User;
import com.sohanf.crmsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class DashboardController {

    @Autowired
    private UserService userService;

    @GetMapping("/user/{subdomain}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_TEACHER') or hasAuthority('ROLE_STUDENT')")
    public User getUser(@PathVariable String subdomain) {
        return userService.getUser(subdomain);
    }

    @PostMapping("/saveUser")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_TEACHER') or hasAuthority('ROLE_STUDENT')")
    public String getUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @PostMapping("/changePassword")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_TEACHER') or hasAuthority('ROLE_STUDENT')")
    public String changePassword(@RequestBody AuthRequest user) {
        return userService.changePassword(user);
    }

    @PostMapping("/addStudent")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_TEACHER')")
    public String addStudent(@RequestBody Student student) {
        return userService.addStudent(student);
    }

    @PostMapping("/deleteStudent")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_TEACHER')")
    public String deleteStudent(@RequestBody Student student) {
        return userService.deleteStudent(student);
    }

    @GetMapping("/getStudents")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_TEACHER')")
    public List<Student> getStudents() {
        return userService.getStudents();
    }

    @PostMapping("/addTeacher")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String addTeacher(@RequestBody Teacher teacher) {
        return userService.addTeacher(teacher);
    }
}
