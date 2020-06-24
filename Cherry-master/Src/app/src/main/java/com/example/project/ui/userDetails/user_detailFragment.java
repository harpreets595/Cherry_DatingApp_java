package com.example.project.ui.userDetails;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.project.CherryApplication;
import com.example.project.MainActivity;
import com.example.project.R;
import com.example.project.models.Picture;
import com.example.project.models.Profile;
import com.example.project.networking.HttpRequest;
import com.example.project.networking.HttpRequestTask;
import com.example.project.ui.accounts.profile_details;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link profile_details.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link profile_details#newInstance} factory method to
 * create an instance of this fragment.
 */
public class user_detailFragment extends Fragment {

    Button btn_detail;
    ImageView image;

    ScrollView scrollView;

    TextView name;
    TextView age;
    TextView birthday;
    TextView gender;
    TextView astrologicalSign;
    TextView introduction;
    TextView email;

    public boolean show = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root = inflater.inflate(R.layout.fragment_user_detail, container, false);

//        locating the all the txtViews to set their text
        name = root.findViewById(R.id.textView_name);
        age = root.findViewById(R.id.textView_age);
        birthday = root.findViewById(R.id.textView_birthDay);
        gender = root.findViewById(R.id.textView_gender);
        astrologicalSign = root.findViewById(R.id.textView_astrologicalSign);
        introduction = root.findViewById(R.id.textView_introduction);
        email = root.findViewById(R.id.textView_email);


//        gets the uuid from what ever image is clicked from the previous intent
        String uuid = getActivity().getIntent().getStringExtra("uuid");

        CherryApplication application = (CherryApplication) getActivity().getApplication();
        HttpRequest req = new HttpRequest(application.host + "/user/" + uuid, HttpRequest.Method.GET);
        HttpRequestTask task = new HttpRequestTask();

        task.setOnErrorListener(error -> {});
            task.setOnResponseListener(response -> {

                Profile currentUser = Profile.parse(response.getResponseBody());

                name.setText("Name: "+ currentUser.firstname + " " + currentUser.lastname);
                email.setText("Email: " + currentUser.email);
                age.setText("Age: " + currentUser.age);

                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

                birthday.setText("Birthday: " + formatter.format(currentUser.birthday));
                gender.setText("Gender: " + currentUser.gender);
                astrologicalSign.setText("Astrological Sign: " + currentUser.astrologicalSign);
                introduction.setText("Introduction: " + currentUser.introduction);

                HttpRequest pictureReq = new HttpRequest(application.host + "/user/" + currentUser.uuid + "/pictures", HttpRequest.Method.GET);
                HttpRequestTask picTask = new HttpRequestTask();
                picTask.setOnResponseListener(res -> {
                    List<Picture> pic = Picture.parseArray(res.getResponseBody());
                    byte[] decodedString = Base64.decode(pic.get(0).getPicture(), Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    image.setImageBitmap(decodedByte);

                });
                picTask.execute(pictureReq);

            });
        task.execute(req);



        btn_detail = root.findViewById(R.id.Detail_btn);
        image = root.findViewById(R.id.image);
//        txt_view = root.findViewById(R.id.textView_field);

        scrollView = root.findViewById(R.id.scrollView);

//        scrollView.setVisibility(getView().GONE);


        btn_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(image.getVisibility() == view.VISIBLE && show == false){
                    image.setVisibility(view.VISIBLE);
                    scrollView.setVisibility(view.GONE);
                    btn_detail.setText("Click here to see full Details!");
                    show = true;
                }
                else{
                    image.setVisibility(view.VISIBLE);
                    scrollView.setVisibility(view.VISIBLE);
                    btn_detail.setText("Click here to hide Details About me");
                    show = false;
                }

            }
        });

        return root;


    }



}
