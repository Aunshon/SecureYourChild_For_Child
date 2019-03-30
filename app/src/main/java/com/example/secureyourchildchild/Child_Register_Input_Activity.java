package com.example.secureyourchildchild;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class Child_Register_Input_Activity extends AppCompatActivity {

    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase,mdatabaseref;
    private String UserPhoneNumber,UID;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedEditor;
    private String user_type="user_type",sharename="name",shareage="age";
    //String userPhoneOnFirebase,userNameOnFirebase,userAgeOnfirebase;

    EditText nameText,ageText;
    private String usernameText,userAgeText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child__register__input_);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        nameText=findViewById(R.id.NameEt);
        ageText=findViewById(R.id.AgeEt);
        sharedPreferences=getSharedPreferences(user_type,MODE_PRIVATE);
        sharedEditor=sharedPreferences.edit();
//        usernameText=nameText.getText().toString();
//        userAgeText=ageText.getText().toString();

        if (firebaseUser==null){
            Toast.makeText(this, "You are not Signed In", Toast.LENGTH_SHORT).show();
            Intent mainint=new Intent(Child_Register_Input_Activity.this,PhoneAuth.class);
            mainint.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(mainint);
        }
//        if (sharedPreferences.getString(sharename,"null")!="null"){
//            if (sharedPreferences.getString(user_type,"null")=="Child"){
//                Toast.makeText(Child_Register_Input_Activity.this, "Registration Successful 👏", Toast.LENGTH_SHORT).show();
//                Intent mainint=new Intent(Child_Register_Input_Activity.this,ChildMapActivity.class);
//                mainint.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(mainint);
//            }
//            else {
//                Toast.makeText(Child_Register_Input_Activity.this,sharedPreferences.getString(sharename,"null")+" "+sharedPreferences.getString(user_type,"null") , Toast.LENGTH_SHORT).show();
//                Intent mainint = new Intent(Child_Register_Input_Activity.this, ChildMapActivity.class);
//                mainint.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(mainint);
//            }
//        }
        mdatabaseref= FirebaseDatabase.getInstance().getReference("Users");
//        mdatabaseref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot postSnapshot:dataSnapshot.getChildren()){
//                    UserInfoClass userData=postSnapshot.getValue(UserInfoClass.class);
//                    if (firebaseUser.getPhoneNumber().equals(userData.getPhone())){
//                        userPhoneOnFirebase=userData.getPhone();
//                        userNameOnFirebase=userData.getUsername();
//                        userAgeOnfirebase=userData.getAge();
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(Child_Register_Input_Activity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });


    }

    public void GoBtnClicked(View view) {
        if (nameText.getText().toString().isEmpty()){
            nameText.setError("Please Enter Your Name");
        }
        else if (ageText.getText().toString().isEmpty()){
            ageText.setError("Please Enter Your Age");
        }
        else {
            UserPhoneNumber=firebaseUser.getPhoneNumber();
            UID=firebaseUser.getUid();

            mDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(UID);
            String device_token= FirebaseInstanceId.getInstance().getToken();

            usernameText=nameText.getText().toString();
            userAgeText=ageText.getText().toString();
            Toast.makeText(this, sharedPreferences.getString(user_type,"null"), Toast.LENGTH_SHORT).show();
            UserInfoClass userInfoClass=new UserInfoClass(UserPhoneNumber,usernameText,userAgeText,UID,device_token,sharedPreferences.getString(user_type,"null"));
            //Toast.makeText(this, ""+userInfoClass.getUID(), Toast.LENGTH_SHORT).show();

            mDatabase.setValue(userInfoClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        sharedEditor.putString(sharename,usernameText);
                        sharedEditor.putString(shareage,userAgeText);
                        sharedEditor.apply();
                        if (sharedPreferences.getString(user_type,"null")=="Child"){
                            Toast.makeText(Child_Register_Input_Activity.this, "Registration Successful 👏", Toast.LENGTH_SHORT).show();
                            Intent mainint=new Intent(Child_Register_Input_Activity.this, ChildMapActivity.class);
                            mainint.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(mainint);
                        }
                        else {
                            Toast.makeText(Child_Register_Input_Activity.this, "Parent Is on another app", Toast.LENGTH_SHORT).show();
                            //Intent mainint = new Intent(Child_Register_Input_Activity.this, ChildMapActivity.class);
                            //mainint.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            //startActivity(mainint);
                        }

                    }
                    else {
                        Toast.makeText(Child_Register_Input_Activity.this, ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }
}
