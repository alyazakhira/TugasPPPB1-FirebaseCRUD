package com.example.firebasecrud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    EditText mNameEdt, mAddressEdt, mUpdateNameEdt,mUpdateAddressEdt;

    DatabaseReference mDatabaseReference;
    Student mStudent;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference(Student.class.getSimpleName());

        mNameEdt = findViewById(R.id.name_edt);
        mAddressEdt = findViewById(R.id.addresses_edt);
        mUpdateNameEdt = findViewById(R.id.update_name_edt);
        mUpdateAddressEdt = findViewById(R.id.update_addresses_edt);

        findViewById(R.id.insert_btn).setOnClickListener(view -> insertData());

        findViewById(R.id.read_btn).setOnClickListener(view -> readData());

        findViewById(R.id.update_btn).setOnClickListener(view -> updateData());

        findViewById(R.id.delete_btn).setOnClickListener(view -> deleteData());

        findViewById(R.id.teacher_btn).setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), MainActivity2.class);
            startActivity(intent);
        });
    }

    private void insertData() {
        Student newStudent = new Student();
        String name = mNameEdt.getText().toString();
        String address = mAddressEdt.getText().toString();
        if (name != "" && address != ""){
            newStudent.setName(name);
            newStudent.setAddress(address);

            mDatabaseReference.push().setValue(newStudent);
            Toast.makeText(this, "Successfully insert student data!", Toast.LENGTH_SHORT).show();
        }
    }

    private void readData() {
        mStudent = new Student();
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()){
                    for (DataSnapshot currentData : snapshot.getChildren()){
                        key = currentData.getKey();
                        mStudent.setName(currentData.child("name").getValue().toString());
                        mStudent.setAddress(currentData.child("address").getValue().toString());
                    }
                }

                mUpdateNameEdt.setText(mStudent.getName());
                mUpdateAddressEdt.setText(mStudent.getAddress());
                Toast.makeText(MainActivity.this, "Data has been shown!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateData() {
        Student updatedData = new Student();
        updatedData.setName(mUpdateNameEdt.getText().toString());
        updatedData.setAddress(mUpdateAddressEdt.getText().toString());

        mDatabaseReference.child(key).setValue(updatedData);
        Toast.makeText(this, "Data has been updated", Toast.LENGTH_SHORT).show();
    }

    private void deleteData() {
        Student deleteData = new Student();
        deleteData.setName(mUpdateNameEdt.getText().toString());
        deleteData.setAddress(mUpdateAddressEdt.getText().toString());

        mDatabaseReference.child(key).removeValue();
        Toast.makeText(this, "Data has been deleted", Toast.LENGTH_SHORT).show();
    }
}