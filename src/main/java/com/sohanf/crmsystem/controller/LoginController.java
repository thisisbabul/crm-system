package com.sohanf.crmsystem.controller;

import com.sohanf.crmsystem.entity.AuthRequest;
import com.sohanf.crmsystem.entity.User;
import com.sohanf.crmsystem.service.UserService;
import com.sohanf.crmsystem.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public Map<String, String> authenticate(@RequestBody AuthRequest authRequest) throws Exception {
        Map<String, String> map = new HashMap<>();

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getSubdomain(), authRequest.getPassword()));
            map.put("status", "success");
            map.put("subdomain", authRequest.getSubdomain());
            map.put("token", jwtUtil.generateToken(authRequest.getSubdomain()));
        }
        catch (Exception ex) {
            map.put("status", "failed");
        }

        return map;
    }

    @PostMapping("/registration")
    public String registration(@RequestBody User user) {
        return userService.registration(user);
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Map admin() {
        Map<String, String> map = new HashMap<>();
        map.put("id", "12345");
        map.put("name", "Babul");
        return map;
    }

    @GetMapping("/teacher")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_TEACHER')")
    public String teacher() {
        return "teacher";
    }

    @GetMapping("/student")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_TEACHER') or hasAuthority('ROLE_STUDENT')")
    public String student() {
        return "student";
    }

    @GetMapping("/checkSubdomain/{subdomain}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_TEACHER') or hasAuthority('ROLE_STUDENT')")
    public boolean checkSubdomain(@PathVariable String subdomain) {
        return userService.checkSubdomain(subdomain);
    }
}
