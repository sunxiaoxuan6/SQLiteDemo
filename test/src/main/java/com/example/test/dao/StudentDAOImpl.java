package com.example.test.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.test.model.Student;
import com.example.test.utils.MyDBHelper;

import java.util.ArrayList;
import java.util.List;

public class StudentDAOImpl implements StudentDao{
    private MyDBHelper helper;
    private SQLiteDatabase dbs;

    public StudentDAOImpl(Context context){
        helper=new MyDBHelper(context);
    }
    @Override
    public void insert(Student student) {
        dbs = helper.getWritableDatabase();
        String sql = "insert into student values(null,?,?,?)";
        dbs.execSQL(sql, new Object[]{
                student.getName(),
                student.getClassmate(),
                student.getAge()});
        dbs.close();
    }

    @Override
    public void update(Student student) {
        dbs = helper.getWritableDatabase();
        String sql = "update student set classmate=? where name=?";
        dbs.execSQL(sql, new Object[]{
                student.getAge(),
                student.getName()
        });
    }

    @Override
    public void delete(String studentName) {
        dbs = helper.getWritableDatabase();
        String sql = "delete from student where name=?";
        dbs.execSQL(sql, new Object[]{ studentName });
    }

    @Override
    public List<Student> selectAllStudents() {
        String sql = "select * from student";
        List<Student> students = null;
        dbs = helper.getReadableDatabase();
        Cursor cursor = dbs.rawQuery(sql, null);
        if (cursor != null && cursor.getCount() > 0) {
            students = new ArrayList<>();
            while (cursor.moveToNext()) {
                Student student = new Student();
                student.setId(cursor.getInt(cursor.getColumnIndex("id")));

                student.setName(cursor.getString(cursor.getColumnIndex("name")));
                student.setClassmate(cursor.getString(cursor.getColumnIndex("classmate")));
                student.setAge(cursor.getInt(cursor.getColumnIndex("age")));

                students.add(student);
            }
            cursor.close();
        }
        dbs.close();
        return students;
    }
}
