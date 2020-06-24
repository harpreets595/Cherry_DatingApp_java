package com.example.project.ui.findMatch;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project.CherryApplication;
import com.example.project.MainActivity;
import com.example.project.R;
import com.example.project.models.Match;
import com.example.project.models.Picture;
import com.example.project.models.Profile;
import com.example.project.networking.HttpRequest;
import com.example.project.networking.HttpRequestTask;
import com.example.project.ui.accounts.profile_details;

import java.util.List;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link profile_details.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link profile_details#newInstance} factory method to
 * create an instance of this fragment.
 */
public class findMatchFragment extends Fragment {

    ImageView like;
    ImageView dislike;
    ImageView image;
    TextView txt_view;
    TextView loadingText;
    View root;

    Profile match;

    private List<Profile> users;
    private CherryApplication application;
    private ConstraintLayout loading;
    private ConstraintLayout display;
    private ConstraintLayout broken;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        root = inflater.inflate(R.layout.fragment_find_match, container, false);
        application = (CherryApplication) getActivity().getApplication();

        broken = root.findViewById(R.id.brokenMatch);
        loading = root.findViewById(R.id.loadingMatch);
        display = root.findViewById(R.id.displayMatch);
        loading.setVisibility(View.VISIBLE);
        display.setVisibility(View.GONE);
        broken.setVisibility(View.GONE);

        loadingText = root.findViewById(R.id.loading_TextView);
        loadingText.setText("Finding your perfect match");

        image = root.findViewById(R.id.image);
        txt_view = root.findViewById(R.id.bioTextField);
        image.setImageBitmap(null);
        txt_view.setText("");

        like = root.findViewById(R.id.like_imageView);
        like.setOnClickListener(view -> {
            loading.setVisibility(View.VISIBLE);
            display.setVisibility(View.GONE);
            broken.setVisibility(View.GONE);
            image.setImageBitmap(null);
            txt_view.setText("");

            loadingText.setText("Creating you love life");

            Match m = new Match();
            m.user1 = application.currentUser;
            m.user2 = match;
            HttpRequest sendMatch = new HttpRequest(application.host + "/match", HttpRequest.Method.POST);
            sendMatch.setRequestBody("application/json", m.format(application));
            HttpRequestTask task = new HttpRequestTask();
            task.setOnResponseListener(response -> {
                HttpRequest match1Req = new HttpRequest(application.host + "/user/" + application.currentUserUuid + "/matches1", HttpRequest.Method.GET);
                HttpRequestTask match1Task = new HttpRequestTask();
                match1Task.setOnResponseListener(matchResponse -> {
                    application.matches.addAll(Match.parseArray(matchResponse.getResponseBody()));
                    HttpRequest match2Req = new HttpRequest(application.host + "/user/" + application.currentUserUuid + "/matches2", HttpRequest.Method.GET);
                    HttpRequestTask match2Task = new HttpRequestTask();
                    match2Task.setOnResponseListener(match2Response -> {
                        application.matches.addAll(Match.parseArray(match2Response.getResponseBody()));
                        GetAllUsers();
                    });
                    match2Task.execute(match2Req);
                });
                match1Task.execute(match1Req);
            });
            task.execute(sendMatch);
        });
        dislike = root.findViewById(R.id.dislike_imageView);

        txt_view.setText("");
        image.setImageBitmap(null);

        GetAllUsers();
        return root;
    }

    public void GetPossibleMatches() {
        for(int i = users.size() - 1; i >= 0; i--) {
            Profile p = users.get(i);
            for (Match m : application.matches) {
                if (m.user1.uuid.equals(p.uuid) || m.user2.uuid.equals(p.uuid))
                    users.remove(p);
            }
        }

        GetRandomUser();
    }

    public void GetAllUsers() {
        HttpRequest usersRequest = new HttpRequest(application.host + "/user", HttpRequest.Method.GET);
        HttpRequestTask userRequestTask = new HttpRequestTask();
        userRequestTask.setOnErrorListener(error -> {

        });
        userRequestTask.setOnResponseListener(response -> {
            users = Profile.parseArray(response.getResponseBody());
            for(int i = users.size() - 1; i >= 0; i--) {
                Profile p = users.get(i);
                if (p.uuid.equals(application.currentUserUuid))
                    users.remove(i);
            }
            GetPossibleMatches();
        });
        userRequestTask.execute(usersRequest);
    }

    public void GetRandomUser() {
        if (users.size() == 0) {
            broken.setVisibility(View.VISIBLE);
            display.setVisibility(View.GONE);
            loading.setVisibility(View.GONE);
            return;
        }

        Random random = new Random();
        match = users.get(random.nextInt(users.size()));
        HttpRequest pictureReq = new HttpRequest(application.host + "/user/" + match.uuid + "/pictures", HttpRequest.Method.GET);
        HttpRequestTask picTask = new HttpRequestTask();
        picTask.setOnResponseListener(res -> {
            List<Picture> pic = Picture.parseArray(res.getResponseBody());
            if (pic.size() > 0)
            {
                byte[] decodedString = Base64.decode(pic.get(0).getPicture(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                image.setImageBitmap(decodedByte);
            }

            loading.setVisibility(View.GONE);
            broken.setVisibility(View.GONE);
            display.setVisibility(View.VISIBLE);
        });
        picTask.execute(pictureReq);

        txt_view.setText("Name: " + match.firstname + " " + match.lastname + "\n" +
                         "Age: " + match.age + "\n" +
                         "Astrological Sign: " + match.astrologicalSign + "\n" +
                         "Introduction: " + match.introduction);
    }
}
