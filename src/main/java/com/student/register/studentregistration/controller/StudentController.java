package com.student.register.studentregistration.controller;

import com.student.register.studentregistration.model.Student;
import com.student.register.studentregistration.service.StudentService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class StudentController {

    private static final Logger logger = LogManager.getLogger(StudentController.class);

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping({"/", "/register"})
    public String showRegistrationForm(Model model) {
        logger.info("Showing registration form");
        model.addAttribute("student", new Student());
        model.addAttribute("courses", new String[]{"Computer Science", "Mathematics", "Physics", "Chemistry", "Biology", "English"});
        return "register";
    }

    @PostMapping("/register")
    public String registerStudent(@Valid @ModelAttribute("student") Student student, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            logger.warn("Validation errors while registering student: {} errors", bindingResult.getErrorCount());
            model.addAttribute("courses", new String[]{"Computer Science", "Mathematics", "Physics", "Chemistry", "Biology", "English"});
            return "register";
        }

        Student saved = studentService.save(student);
        logger.info("Student registered successfully: id={}, name={} {}", saved.getId(), saved.getFirstName(), saved.getLastName());
        model.addAttribute("student", saved);
        return "success";
    }

    @GetMapping("/students")
    public String listStudents(Model model) {
        logger.info("Listing all students");
        model.addAttribute("students", studentService.findAll());
        return "students";
    }
}
