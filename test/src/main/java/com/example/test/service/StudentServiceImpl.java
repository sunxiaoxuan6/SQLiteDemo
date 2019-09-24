package com.example.test.service;

import android.content.Context;

import com.example.test.dao.StudentDAOImpl;
import com.example.test.dao.StudentDao;
import com.example.test.model.Student;

import java.util.List;

public class StudentServiceImpl implements StudentService{
    private StudentDao studentDao;

    public StudentServiceImpl(Context context){
        studentDao=new StudentDAOImpl(context);
    }

    @Override
    public List<Student> getAllStudents() {
        return studentDao.selectAllStudents();
    }

    @Override
    public void insert(Student student) {
        studentDao.insert(student);
    }

    @Override
    public void modifyRealNumber(Student student) {
        studentDao.update(student);
    }

    @Override
    public void delete(String studentName) {
        studentDao.delete(studentName);
    }
}
