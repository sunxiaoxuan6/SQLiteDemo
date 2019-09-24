package com.example.test.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.test.R;
import com.example.test.adapter.StudentAdapter;
import com.example.test.model.Student;
import com.example.test.service.StudentService;
import com.example.test.service.StudentServiceImpl;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private static final int ADD_REQUEST = 100;
    private static final int MODIFY_REQUEST = 101;

    private Button add,change,delete,read;
    private ListView list;
    private StudentAdapter studentAdapter;

    private List<Student> students;
    private StudentService studentService;
    private int selectedPos;
    private Student selectedStudent;

    private ArrayList<String> contacts;

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
        read=findViewById(R.id.bt_read);

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
                intent.putExtra("flag","添加");
                startActivityForResult(intent, ADD_REQUEST);
            }
        });

        read.setOnClickListener(new View.OnClickListener() {
            //判断是否有判断权限
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_CONTACTS},1);
                }else {
                    readContacts();
                }
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull
            String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1&&grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){

        }
    }

    private void readContacts() {
        Cursor cursor=getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,null,null,null);

        contacts=new ArrayList<>();
        if(cursor!=null&&cursor.moveToFirst()){
            do{
                String name=cursor.getString(cursor.getColumnIndex(
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phone=cursor.getString(cursor.getColumnIndex(
                        ContactsContract.CommonDataKinds.Phone.NUMBER));
                contacts.add(name+":"+phone);
            }while (cursor.moveToNext());
            cursor.close();
        }
        //设置Adapter
        if(contacts.isEmpty()){
            Toast.makeText(MainActivity.this,"没有联系人",Toast.LENGTH_SHORT).show();
            return;
        }
        ArrayAdapter<String> arrayAdapter= new ArrayAdapter<>(MainActivity.this,
                android.R.layout.simple_list_item_1,contacts);
        list.setAdapter(arrayAdapter);
    }
}

