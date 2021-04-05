package com.sohanf.crmsystem.service;

import com.sohanf.crmsystem.common.Constant;
import com.sohanf.crmsystem.entity.AuthRequest;
import com.sohanf.crmsystem.entity.Student;
import com.sohanf.crmsystem.entity.Teacher;
import com.sohanf.crmsystem.entity.User;
import com.sohanf.crmsystem.multitenant.TenantContext;
import com.sohanf.crmsystem.repository.StudentRepository;
import com.sohanf.crmsystem.repository.TeacherRepository;
import com.sohanf.crmsystem.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    Logger logger = LoggerFactory.getLogger(UserService.class);

    public String login(User user) {
        try {
            User existingUser = userRepository.findByEmail(user.getEmail());
            if (existingUser.getPassword().equals(user.getPassword())) {
                return "success";
            }
        }
        catch (Throwable ex) {
            System.out.println("User Login Failed "+ex.getMessage());
        }
        return "failed";
    }

    public String registration(User user) {
        try {
            jdbcTemplate.execute("CREATE SCHEMA IF NOT EXISTS "+ user.getSubdomain());
            jdbcTemplate.execute("CREATE SEQUENCE IF NOT EXISTS "+user.getSubdomain()+".hibernate_sequence");
            String encryptedPwd = passwordEncoder.encode(user.getPassword());
            user.setRoles(Constant.DEFAULT_ROLE);
            user.setPassword(encryptedPwd);
            userRepository.save(user);
            return "success";
        }
        /*catch (InvalidDataAccessResourceUsageException ignored) {
            jdbcTemplate.execute("create table public.User(" +
                    "id bigint, " +
                    "firstName varchar(200), " +
                    "lastName varchar(200), " +
                    "subdomain varchar(200), " +
                    "dob varchar(200), " +
                    "phone varchar(200), " +
                    "email varchar(200), " +
                    "password varchar(200), " +
                    "enabled boolean DEFAULT FALSE, " +
                    "roles varchar(200)" +
                    ");");
            registration(user);
            return "success";
        }*/
        catch (Throwable ex) {
            System.out.println("User Login Failed "+ex.getMessage());
        }
        return "failed";
    }

    public List<String> getRolesByLoggedInUser(Principal principal) {
        String roles = getLoggedInUser(principal).getRoles();
        List<String> assignedRoles = Arrays.stream(roles.split(",")).collect(Collectors.toList());

        if (assignedRoles.contains(Constant.ROLE_ADMIN)) {
            return Arrays.stream(Constant.ADMIN_ACCESS).collect(Collectors.toList());
        }
        else if (assignedRoles.contains(Constant.ROLE_TEACHER)) {
            return Arrays.stream(Constant.TEACHER_ACCESS).collect(Collectors.toList());
        }
        else {
            return Arrays.stream(Constant.STUDENT_ACCESS).collect(Collectors.toList());
        }
    }

    public User getLoggedInUser(Principal principal) {
        return userRepository.findBySubdomain(principal.getName()).get();
    }

    public boolean checkSubdomain(String subdomain) {
        try {
            User user = userRepository.findBySubdomain(subdomain).get();
            return user.getSubdomain().equals(subdomain);
        }
        catch (Throwable ex) {
            logger.error(ex.getMessage());
        }
        return false;
    }

    public User getUser(String subdomain) {
        try {
            User user = userRepository.findBySubdomain(subdomain).get();
            if (user.isEnabled()) {
                User userInfo = new User(
                  user.getFirstName(),
                  user.getLastName(),
                  user.getSubdomain(),
                  user.getDob(),
                  user.getPhone(),
                  user.getEmail()
                );
                return userInfo;
            }
        }
        catch (Throwable ex) {
            logger.error(ex.getMessage());
        }
        return null;
    }

    public String saveUser(User user) {
        try {
            User existigUser = userRepository.findBySubdomain(user.getSubdomain()).get();
            if (existigUser.isEnabled() && existigUser.getSubdomain().equals(user.getSubdomain())) {
                existigUser.setFirstName(user.getFirstName());
                existigUser.setLastName(user.getLastName());
                existigUser.setDob(user.getDob());
                existigUser.setPhone(user.getPhone());
                existigUser.setEmail(user.getEmail());
                userRepository.save(existigUser);
                return "success";
            }

        }
        catch (Throwable ex) {
            System.out.println("User registration Failed "+ex.getMessage());
        }
        return "failed";
    }

    public String changePassword(AuthRequest user) {
        try {
            User existigUser = userRepository.findBySubdomain(user.getSubdomain()).get();
            if (existigUser.isEnabled() && existigUser.getSubdomain().equals(user.getSubdomain())) {
                String encryptedPwd = passwordEncoder.encode(user.getPassword());
                existigUser.setPassword(encryptedPwd);
                userRepository.save(existigUser);
                return "success";
            }

        }
        catch (Throwable ex) {
            System.out.println("User registration Failed "+ex.getMessage());
        }
        return "failed";
    }

    public String addStudent(Student student) {
        try {
            if (student.getId() == null) {
                String encryptedPwd = passwordEncoder.encode(student.getPassword());
                student.setRoles(Constant.ROLE_STUDENT);
                student.setPassword(encryptedPwd);
                studentRepository.save(student);
            }
            else {
                Student existStudent = studentRepository.findById(student.getId()).get();
                existStudent.setName(student.getName());
                existStudent.setParentName(student.getParentName());
                existStudent.setParentContact(student.getParentContact());
                studentRepository.save(existStudent);
            }
            return "success";
        }
        catch (InvalidDataAccessResourceUsageException ignored) {
            jdbcTemplate.execute("create table "+TenantContext.getCurrentTenant()+".Student(" +
                    "id BIGSERIAL PRIMARY KEY, " +
                    "name varchar(200), " +
                    "parentName varchar(200), " +
                    "parentContact varchar(200), " +
                    "password varchar(200), " +
                    "roles varchar(200) " +
                    ");");
            addStudent(student);
            return "success";
        }
        catch (Throwable ex) {
            System.out.println("Student Add Failed " + ex.getMessage());
        }
        return "failed";
    }

    public List<Student> getStudents() {
        try {
            return studentRepository.findAll();
        }
        catch (Throwable ex) {
            System.out.println("Student Retrive Failed " + ex.getMessage());
        }
        return null;
    }

    public String deleteStudent(Student student) {
        try {
            studentRepository.delete(student);
            return "success";
        }
        catch (Throwable ex) {
            System.out.println("Student Delete Failed " + ex.getMessage());
        }
        return "failed";
    }

    public String addTeacher(Teacher teacher) {
        try {
            String encryptedPwd = passwordEncoder.encode(teacher.getPassword());
            teacher.setRole(Constant.ROLE_TEACHER);
            teacher.setPassword(encryptedPwd);
            teacherRepository.save(teacher);
            return "success";
        }
        catch (InvalidDataAccessResourceUsageException ignored) {
            jdbcTemplate.execute("create table "+TenantContext.getCurrentTenant()+".Teacher(" +
                    "id BIGSERIAL PRIMARY KEY, " +
                    "name varchar(200), " +
                    "designation varchar(200), " +
                    "email varchar(200), " +
                    "phone varchar(200), " +
                    "password varchar(200), " +
                    "role varchar(200) " +
                    ");");
            addTeacher(teacher);
            return "success";
        }
        catch (Throwable ex) {
            System.out.println("Teacher registration Failed " + ex.getMessage());
        }
        return "failed";
    }
}
