package com.example.connecttoservertest.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.connecttoservertest.R;
import com.example.connecttoservertest.model.Student;
import com.squareup.picasso.Picasso;

import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {
    private List<Student> students;

    public StudentAdapter(List<Student> students) {
        this.students = students;
    }


    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StudentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.student_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        holder.bindStudent(students.get(position));
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    public class StudentViewHolder extends RecyclerView.ViewHolder {
        private ImageView studentImg;
        private TextView fullName;
        private TextView email;
        private TextView id;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            studentImg = itemView.findViewById(R.id.iv_students);
            fullName = itemView.findViewById(R.id.tv_fullname);
            email = itemView.findViewById(R.id.tv_mail);
            id = itemView.findViewById(R.id.tv_id);
        }

        public void bindStudent(Student student) {
            Picasso.get().load(student.getAvatar()).into(studentImg);
            fullName.setText(student.getFirstName()+" "+student.getLastName());
            email.setText(student.getEmail());
            id.setText(String.valueOf(student.getId()));
        }
    }

    public void addStudent(Student student) {
        students.add(0, student);
        notifyItemInserted(0);
    }

}
