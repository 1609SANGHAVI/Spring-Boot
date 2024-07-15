package com.example.demoForJPA.controller;

import com.example.demoForJPA.entity.Student;
import com.example.demoForJPA.repo.StudentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@RestController
public class StudentController {
    @Autowired
    StudentRepo studentRepo;
@PostMapping({"/api/students"})
public ResponseEntity<Student> saveStudent(@RequestBody Student student) {
    // Debugging: Print the received student object
    System.out.println("Received student: " + student);

    // Validate that studentEmail is not null or empty
    if (student.getStudentEmail() == null || student.getStudentEmail().isEmpty()) {
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    Student savedStudent = studentRepo.save(student);
    return new ResponseEntity<>(savedStudent, HttpStatus.CREATED);
}



    @GetMapping({"/api/students"})
    public ResponseEntity<List<Student>> getStudents() {
        List<Student> students = studentRepo.findAll();
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    @GetMapping("/api/students/{id}")
    public ResponseEntity<Student> getStudent(@PathVariable long id) {
        Optional<Student> student = studentRepo.findById(id);
        return student.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/api/students/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable long id, @RequestBody Student stud) {
        Optional<Student> studentOptional = studentRepo.findById(id);
        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();
            student.setStudentName(stud.getStudentName());
            student.setStudentEmail(stud.getStudentEmail());
            student.setStudentAddress(stud.getStudentAddress());
            Student updatedStudent = studentRepo.save(student);
            return new ResponseEntity<>(updatedStudent, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/api/students/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable long id) {
        Optional<Student> student = studentRepo.findById(id);
        if (student.isPresent()) {
            studentRepo.deleteById(id);
            return new ResponseEntity<>("Student deleted successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Student not found", HttpStatus.NOT_FOUND);
        }
    }


}
