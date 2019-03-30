package com.example.secureyourchildchild;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import static android.content.Context.MODE_PRIVATE;


public class AddParentByQr extends Fragment {

    TextView childqr;
    ImageView qrImagwe;
    String ChildFirebaseID;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedEditor;
    MultiFormatWriter multiFormatWriter;
    private String user_type="user_type",sharename="name",shareage="age";

    public AddParentByQr() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        multiFormatWriter=new MultiFormatWriter();
        ChildFirebaseID=firebaseUser.getUid();

        if (ChildFirebaseID==null){
            Toast.makeText(getContext(), "Try After 30 Second , Data is Loading", Toast.LENGTH_SHORT).show();
        }
        else {
            try{
                BitMatrix bitMatrix= multiFormatWriter.encode(ChildFirebaseID, BarcodeFormat.QR_CODE,400,400);
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                Bitmap bitmap=barcodeEncoder.createBitmap(bitMatrix);
                qrImagwe.setImageBitmap(bitmap);

            }catch (WriterException e){
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_add_parent_by_qr, container, false);
        childqr=view.findViewById(R.id.childqr);
        qrImagwe=view.findViewById(R.id.childqrimage);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        sharedPreferences=getContext().getSharedPreferences(user_type,MODE_PRIVATE);
        sharedEditor=sharedPreferences.edit();
        return view;
    }


}
