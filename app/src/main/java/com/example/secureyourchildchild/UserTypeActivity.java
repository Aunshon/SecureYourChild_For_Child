package com.example.secureyourchildchild;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class UserTypeActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedEditor;
    Spinner userSpinner;
    private String user_type="user_type";
    private String signed_in_user;
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    private String user,UserPhoneNumber=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_type);

        sharedPreferences=getSharedPreferences(user_type,MODE_PRIVATE);
        userSpinner=findViewById(R.id.user_spinner);
        mAuth=FirebaseAuth.getInstance();
        firebaseUser=mAuth.getCurrentUser();

        List<String> allUsers=new ArrayList<>();
        allUsers.add(0,"Choose User");
        allUsers.add(1,"Child");
        //allUsers.add(2,"Parent");

        ArrayAdapter<String> dataAdapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,allUsers);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userSpinner.setAdapter(dataAdapter);

        user=sharedPreferences.getString(user_type,"null");
        //Toast.makeText(this, ""+user, Toast.LENGTH_SHORT).show();
        if (firebaseUser!=null){
            UserPhoneNumber=firebaseUser.getPhoneNumber();
            //Toast.makeText(this, ""+UserPhoneNumber, Toast.LENGTH_SHORT).show();
        }
        if (user!="null"){
            if (UserPhoneNumber!=null){
                Intent mainint = new Intent(UserTypeActivity.this, Child_Register_Input_Activity.class);
                mainint.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mainint);
            }
            else {
                Intent mainint=new Intent(UserTypeActivity.this,PhoneAuth.class);
                mainint.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mainint);
                //Toast.makeText(this, " "+firebaseUser.getPhoneNumber().toString(), Toast.LENGTH_SHORT).show();
            }
            Toast.makeText(this, ""+user, Toast.LENGTH_SHORT).show();
        }

    }
    public void nextBTNClicked(View view) {
        String spinneruer=userSpinner.getSelectedItem().toString();
        if (spinneruer.equals("Choose User")){
            Toast.makeText(this, "Please select User Type", Toast.LENGTH_SHORT).show();
        }
        else{
            saveUser(spinneruer);
            //Toast.makeText(this, ""+sharedPreferences.getString(user_type,"null").toString(), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(UserTypeActivity.this, PhoneAuth.class));
        }

    }

    private void saveUser(String spinnerusertype) {
        //String value=sharedPreferences.getString(user,"null").toString();
        sharedEditor=sharedPreferences.edit();
        sharedEditor.putString(user_type,spinnerusertype);
        sharedEditor.apply();
    }
}
