package com.example.sqlitedemo.service;

import com.example.sqlitedemo.model.Student;

import java.util.List;

public interface StudentService {
     List<Student> getAllStudents();
     void insert(Student student);
     void modifyRealNumber(Student student);
     void delete(String StudentName);
}
