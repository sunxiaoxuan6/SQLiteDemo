package com.example.test.dao;

import com.example.test.model.Student;

import java.util.List;

public interface StudentDao {
    //增删改一个学生
    void insert(Student student);
    void update(Student student);
    void delete(String studentName);

    List<Student> selectAllStudents();
}
