package com.example.sqlitedemo.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.sqlitedemo.R;
import com.example.sqlitedemo.model.Student;
import com.example.sqlitedemo.service.StudentService;
import com.example.sqlitedemo.service.StudentServiceImpl;

import java.util.Arrays;
import java.util.List;

public class InsertActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btYes,btNo;
    private EditText etAge,etName;
    private Spinner spClass;
    private List<String> classRoom;

    private Student student;
    private StudentService studentService;
    private String flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        studentService=new StudentServiceImpl(this);
        
        initView();
        initData();
    }
    
    private void initView() {
        btYes=findViewById(R.id.bt_yes);
        btNo=findViewById(R.id.bt_no);
        etAge=findViewById(R.id.et_age);
        etName=findViewById(R.id.et_name);
        spClass=findViewById(R.id.sp_class);
        
        btYes.setOnClickListener(this);
        btNo.setOnClickListener(this);

        classRoom = Arrays.asList(getResources().getStringArray(R.array.classRoom));
        spClass.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                classRoom));
    }
    private void initData() {
        Intent intent = getIntent();
        flag = intent.getStringExtra("flag");

        Bundle bundle = intent.getExtras();
        if(bundle != null) {
            student = (Student) bundle.getSerializable("student");
            if(student != null) {
                etName.setText(student.getName());
                etName.setEnabled(false);
                etAge.setText(String.valueOf(student.getAge()));
                spClass.setSelection(classRoom.indexOf(student.getClassmate()), true);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_yes:
                yes();
                break;
            case R.id.bt_no:
                finish();
        }
    }

    private void yes() {
        if(student == null) {
            student = new Student();
        }
        student.setName(etName.getText().toString());
        student.setAge(Integer.parseInt(etAge.getText().toString()));
        student.setClassmate((String) spClass.getSelectedItem());
        if("修改".equals(flag)) {
            studentService.modifyRealNumber(student);
        } else if("添加".equals(flag)) {
            studentService.insert(student);
        }

        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("student", student);
        intent.putExtras(bundle);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
