package com.transaction.controller;
import com.transaction.entity.User;
import com.transaction.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired private UserService userService;

    // Create an employee
    @PostMapping("/employees")
    public User createEmployee(@RequestParam String username,
                               @RequestParam String password) {
        return userService.createEmployee(username,password);
    }

    // List all employees
    @GetMapping("/employees")
    public List<User> listEmployees() {
        return userService.findAllEmployees();
    }
}
