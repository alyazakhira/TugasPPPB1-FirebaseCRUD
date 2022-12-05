package com.example.firebasecrud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity2 extends AppCompatActivity {
    EditText sNameEdt, sAddressEdt, sSubjectEdt, sUpdateNameEdt, sUpdateAddressEdt, sUpdateSubjectEdt;

    DatabaseReference sDatabaseReference;
    Teacher sTeacher;
    String key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        sDatabaseReference = FirebaseDatabase.getInstance().getReference(Teacher.class.getSimpleName());

        sNameEdt = findViewById(R.id.name_edt);
        sAddressEdt = findViewById(R.id.addresses_edt);
        sSubjectEdt = findViewById(R.id.subject_edt);
        sUpdateNameEdt = findViewById(R.id.update_name_edt);
        sUpdateAddressEdt = findViewById(R.id.update_addresses_edt);
        sUpdateSubjectEdt = findViewById(R.id.update_subject_edt);

        findViewById(R.id.insert_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertData();
            }
        });

        findViewById(R.id.read_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readData();
            }
        });

        findViewById(R.id.update_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateData();
            }
        });

        findViewById(R.id.delete_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteData();
            }
        });
    }

    private void insertData() {
        Teacher newTeacher = new Teacher();
        String name = sNameEdt.getText().toString();
        String address = sAddressEdt.getText().toString();
        String subject = sSubjectEdt.getText().toString();
        if (name != "" && address != ""){
            newTeacher.setName(name);
            newTeacher.setAddress(address);
            newTeacher.setSubject(subject);

            sDatabaseReference.push().setValue(newTeacher);
            Toast.makeText(this, "Successfully insert teacher data!", Toast.LENGTH_SHORT).show();
        }
    }

    private void readData() {
        sTeacher = new Teacher();
        sDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()){
                    for (DataSnapshot currentData : snapshot.getChildren()){
                        key = currentData.getKey();
                        sTeacher.setName(currentData.child("name").getValue().toString());
                        sTeacher.setAddress(currentData.child("address").getValue().toString());
                        sTeacher.setSubject(currentData.child("subject").getValue().toString());
                    }
                }

                sUpdateNameEdt.setText(sTeacher.getName());
                sUpdateAddressEdt.setText(sTeacher.getAddress());
                sUpdateSubjectEdt.setText(sTeacher.getSubject());
                Toast.makeText(MainActivity2.this, "Data has been shown!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateData() {
        Teacher updatedData = new Teacher();
        updatedData.setName(sUpdateNameEdt.getText().toString());
        updatedData.setAddress(sUpdateAddressEdt.getText().toString());
        updatedData.setSubject(sUpdateSubjectEdt.getText().toString());

        sDatabaseReference.child(key).setValue(updatedData);
        Toast.makeText(this, "Data has been updated", Toast.LENGTH_SHORT).show();
    }

    private void deleteData() {
        Teacher deleteData = new Teacher();
        deleteData.setName(sUpdateNameEdt.getText().toString());
        deleteData.setAddress(sUpdateAddressEdt.getText().toString());
        deleteData.setSubject(sUpdateSubjectEdt.getText().toString());

        sDatabaseReference.child(key).removeValue();
        Toast.makeText(this, "Data has been deleted", Toast.LENGTH_SHORT).show();
    }
}