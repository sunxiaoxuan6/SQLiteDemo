package com.example.sqlitedemo.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.sqlitedemo.model.Student;
import com.example.sqlitedemo.utils.MyDBHelper;

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

        // 1. 获取SQLiteDatabase对象
        dbs = helper.getReadableDatabase();

        // 2. 执行SQL查询
        Cursor cursor = dbs.rawQuery(sql, null);

        // 3. 处理结果
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
            // 4. 关闭cursor
            cursor.close();
        }
        dbs.close();
        // 5. 返回结果
        return students;
    }
}
