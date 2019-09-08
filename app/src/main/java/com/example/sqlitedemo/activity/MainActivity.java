package com.example.sqlitedemo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

public class MainActivity extends AppCompatActivity{
    private static final int ADD_REQUEST = 100;
    private static final int MODIFY_REQUEST = 101;

    private Button add,change,delete;
    private ListView list;
    private StudentAdapter studentAdapter;

    private List<Student> students;
    private StudentService studentService;
    private int selectedPos;
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

        list=findViewById(R.id.lv_list);
        studentAdapter=new StudentAdapter(students);
        list.setAdapter(studentAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                selectedPos=position;
                selectedStudent=(Student) parent.getItemAtPosition(position);

                //修改
                change.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, InsertActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("student", selectedStudent);
                        intent.putExtras(bundle);
                        startActivityForResult(intent, MODIFY_REQUEST);
                    }
                });

                //删除
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        studentService.delete(selectedStudent.getName());
                        students.remove(position);
                        studentAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
        //增加
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, InsertActivity.class);
                startActivityForResult(intent, ADD_REQUEST);
            }
        });

    }

    private void initData() {
        studentService = new StudentServiceImpl(this);
        students = studentService.getAllStudents();
        if (students == null) {
            students = new ArrayList<>();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode!= Activity.RESULT_OK){
            return;
        }
        if(data!=null){
            Bundle bundle=data.getExtras();
            if(bundle==null){
                return;
            }
            selectedStudent= (Student) bundle.get("student");
            if(requestCode==MODIFY_REQUEST){
                students.set(selectedPos,selectedStudent);
            }else if(requestCode==ADD_REQUEST){
                students.add(selectedStudent);
            }
            studentAdapter.notifyDataSetChanged();
        }
    }
}
