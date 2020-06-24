package com.example.project.ui.accounts;

import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.CherryApplication;
import com.example.project.R;
import com.example.project.models.Picture;
import com.example.project.models.Profile;
import com.example.project.networking.HttpRequest;
import com.example.project.networking.HttpRequestTask;

import java.io.File;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;

/**
 * A placeholder fragment containing a simple view.
 */
public class MyProfileActivityFragment extends Fragment {

    static final int REQUEST_IMAGE_CAPTURE = 1;

    public MyProfileActivityFragment() {
    }

    Button btn_detail;
    ImageView image;
    TextView txt_view;
    CherryApplication application;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root = inflater.inflate(R.layout.fragment_my_profile, container, false);

        btn_detail = root.findViewById(R.id.Detail_btn);
        image = root.findViewById(R.id.image);


        CherryApplication app = (CherryApplication) getActivity().getApplication();




            TextView username= root.findViewById(R.id.a_user);
            username.setText(app.currentUser.firstname);
            TextView firstname= root.findViewById(R.id.a_first);
            firstname.setText(app.currentUser.firstname);
            TextView lastname= root.findViewById(R.id.a_last);
            lastname.setText(app.currentUser.lastname);
            TextView age= root.findViewById(R.id.a_age);
            age.setText(app.currentUser.getAge());
            TextView astro= root.findViewById(R.id.a_astrologicalsign);
            astro.setText(app.currentUser.astrologicalSign.toString());
            TextView email= root.findViewById(R.id.a_email);
            email.setText(app.currentUser.email);
            TextView introduction = root.findViewById(R.id.a_introduction);
            introduction.setText(app.currentUser.introduction);





        btn_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                dispatchTakePictureIntent();

            }
        });

        return root;


    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        application = (CherryApplication) getActivity().getApplication();
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Picture pic = new Picture(application.currentUser, (Bitmap) extras.get("data"));
            //imageView.setImageBitmap(imageBitmap);
            HttpRequest req = new HttpRequest(application.host + "/picture", HttpRequest.Method.POST);
            req.setRequestBody("application/json", pic.format(application));
            HttpRequestTask task = new HttpRequestTask();
            task.setOnErrorListener(error -> {
                // Server error
            });
            task.setOnResponseListener(response -> {

            });
            task.execute(req);
        }
    }
}