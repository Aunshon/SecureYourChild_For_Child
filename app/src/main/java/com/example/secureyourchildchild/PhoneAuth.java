package com.example.secureyourchildchild;

import android.app.ProgressDialog;
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
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class PhoneAuth extends AppCompatActivity {

    CountryCodePicker ccp;
    String fullPhoneNumber;
    EditText phoneNumber,VarifyCode;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedEditor;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;

    private String user_type="user_type",sharephonekey="phone";
    private String user;
    String SentCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth);


        CheckInternetConnection checkInternetConnection =new CheckInternetConnection();

        if(!checkInternetConnection.isConnected(PhoneAuth.this)){
            checkInternetConnection.buildDialog(PhoneAuth.this).show();
        }
        progressDialog=new ProgressDialog(this);
        ccp =findViewById(R.id.countrycodepicker);
        VarifyCode=findViewById(R.id.varifytext);
        phoneNumber=findViewById(R.id.phonenumber);
        mAuth=FirebaseAuth.getInstance();
        firebaseUser=mAuth.getCurrentUser();

        sharedPreferences=getSharedPreferences(user_type,MODE_PRIVATE);
        sharedEditor=sharedPreferences.edit();

        user=sharedPreferences.getString(user_type,"null");
//        Toast.makeText(this, ""+user, Toast.LENGTH_SHORT).show();
        if (user!="null" && firebaseUser!=null){
            Intent mainint=new Intent(PhoneAuth.this,Child_Register_Input_Activity.class);
            mainint.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(mainint);
        }
    }


    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            //progressDialog.dismiss();
            Toast.makeText(PhoneAuth.this, "Code Recived", Toast.LENGTH_SHORT).show();
            signInWithPhoneAuthCredential(phoneAuthCredential);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            progressDialog.dismiss();
            Toast.makeText(PhoneAuth.this, "Please check your phone number or check your country code", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            SentCode=s;
            progressDialog.dismiss();
            Toast.makeText(PhoneAuth.this, "Code Sent to "+fullPhoneNumber+" 😍", Toast.LENGTH_SHORT).show();
        }
    };
    public void sendCodeBtn(View view) {
        if (phoneNumber.getText().toString().isEmpty()){
            Toast.makeText(this, "Please enter the phone number", Toast.LENGTH_SHORT).show();
        }
        else {
            ccp.registerCarrierNumberEditText(phoneNumber);
            fullPhoneNumber=ccp.getFullNumberWithPlus();
            //Toast.makeText(this, ""+fullPhoneNumber, Toast.LENGTH_SHORT).show();

            progressDialog.setMessage("Sending Varification Code");
            progressDialog.show();

            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    fullPhoneNumber,        // Phone number to verify
                    60,                 // Timeout duration
                    TimeUnit.SECONDS,   // Unit of timeout
                    this,               // Activity (for callback binding)
                    mCallbacks);        // OnVerificationStateChangedCallbacks
        }
    }

    public void varifyCodeBtn(View view) {
        progressDialog.setMessage("Varifing code😘..");
        progressDialog.show();
        String code=VarifyCode.getText().toString();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(SentCode, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //here you can open new activity
                            progressDialog.dismiss();
                            sharedEditor.putString(sharephonekey,fullPhoneNumber);
                            sharedEditor.apply();
                            Toast.makeText(PhoneAuth.this, "Login Successfull as "+sharedPreferences.getString(user_type,null), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(PhoneAuth.this,Child_Register_Input_Activity.class));
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(),
                                        "Incorrect Verification Code ", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }
}
