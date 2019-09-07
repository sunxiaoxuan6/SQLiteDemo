package com.example.sqlitedemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.sqlitedemo.R;
import com.example.sqlitedemo.adapter.StudentAdapter;
import com.example.sqlitedemo.model.Student;
import com.example.sqlitedemo.service.StudentService;
import com.example.sqlitedemo.service.StudentServiceImpl;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int ADD_REQUEST = 100;
    private static final int MODIFY_REQUEST = 101;

    AdapterView<?> parent = null;
    final int position = 0;

    private Button add,change,delete;
    private ListView list;

    private StudentAdapter studentAdapter;
    private List<Student> students;
    private StudentService studentService;
    private Student selectedStudent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();

        init();
    }

    private void init() {
        add=findViewById(R.id.bt_add);
        change=findViewById(R.id.bt_change);
        delete=findViewById(R.id.bt_delete);
        add.setOnClickListener(this);
        change.setOnClickListener(this);
        delete.setOnClickListener(this);

        list=findViewById(R.id.lv_list);
        studentAdapter=new StudentAdapter(students);
        list.setAdapter(studentAdapter);
    }

    private void initData() {
        studentService = new StudentServiceImpl(this);
        students = studentService.getAllStudents();

        if (students == null) {
            students = new ArrayList<>();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_add:
                Intent intent = new Intent(MainActivity.this, InsertActivity.class);
                startActivityForResult(intent, ADD_REQUEST);
                break;
            case R.id.bt_change:
                intent = new Intent(MainActivity.this, InsertActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("student", selectedStudent);
                intent.putExtras(bundle);
                startActivityForResult(intent, MODIFY_REQUEST);
                break;
            case R.id.bt_delete:


                selectedStudent = (Student) parent.getItemAtPosition(position);

                studentService.delete(selectedStudent.getName());
                studentAdapter.notifyDataSetChanged();
                break;
        }
    }
}
