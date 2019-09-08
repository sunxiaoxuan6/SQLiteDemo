package com.example.sqlitedemo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.sqlitedemo.R;
import com.example.sqlitedemo.model.Student;

import java.util.List;

public class StudentAdapter extends BaseAdapter {
    private List<Student> students;

    public StudentAdapter(List<Student> students) {
        this.students=students;
    }
    @Override
    public int getCount() {
        return students.size();
    }

    @Override
    public Object getItem(int position) {
        return students.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student, parent, false);
            holder = new ViewHolder();
            holder.name = convertView.findViewById(R.id.name);
            holder.classmate = convertView.findViewById(R.id.classmate);
            holder.age = convertView.findViewById(R.id.age);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Student student = students.get(position);
        holder.name.setText(student.getName());
        holder.classmate.setText(String.valueOf(student.getClassmate()));
        holder.age.setText(String.valueOf(student.getAge()));
        return convertView;
    }
    static class ViewHolder {
        TextView name;
        TextView classmate;
        TextView age;
    }
}
