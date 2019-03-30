package com.example.secureyourchildchild;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import static android.content.Context.MODE_PRIVATE;


public class AddParentMenually extends Fragment {

    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase,mdatabaseref;
    private String UserPhoneNumber,UID;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedEditor;
    ProgressBar progressBar;
    TextView id,v;
    View vv;
    private String user_type="user_type",sharename="name",shareage="age";
    //String userPhoneOnFirebase,userNameOnFirebase,userAgeOnfirebase;

    public AddParentMenually() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        if (firebaseUser.getUid()==null){
            Toast.makeText(getContext(), "Try After 30 Second , Data is Loading", Toast.LENGTH_SHORT).show();
        }
        else {
            id.setText(firebaseUser.getUid());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_add_parent_menually, container, false);
        id=view.findViewById(R.id.chidID);
        v=view.findViewById(R.id.v);
        vv=view.findViewById(R.id.vv);
//        progressBar=view.findViewById(R.id.childidprogress);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        sharedPreferences=getContext().getSharedPreferences(user_type,MODE_PRIVATE);
        sharedEditor=sharedPreferences.edit();
        return view;

        //return view;
    }

}
