package com.example.project.ui.feed;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.project.CherryApplication;
import com.example.project.R;
import com.example.project.models.Match;
import com.example.project.models.Profile;
import com.example.project.networking.HttpRequest;
import com.example.project.networking.HttpRequestTask;
import com.example.project.ui.feed.adapter.FeedAdapter;
import com.example.project.models.Picture;
import com.example.project.ui.messages.ConversationFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class PictureFeedActivityFragment extends Fragment {

    private CherryApplication application;
    List<Picture> p = new ArrayList<>();
    FeedAdapter adapter;
    RecyclerView recyclerView;
    private SwipeRefreshLayout swipeLayout;
    List<Match> matches;

    public PictureFeedActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_picture_feed, container, false);
        application = (CherryApplication) getActivity().getApplication();
        matches = application.matches;

        swipeLayout = root.findViewById(R.id.feed_SwipeLayout);
        RefreshList(root);
        swipeLayout.setOnRefreshListener(() -> {
            RefreshList(root);
        });


        return root;
    }

    public void RefreshList(View root) {
        swipeLayout.setRefreshing(true);

        getNewPicturesOfMatches( root);
        recyclerView = root.findViewById(R.id.feed_recycler);

        adapter = new FeedAdapter(p, this, application);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView = root.findViewById(R.id.feed_recycler);

        adapter = new FeedAdapter(p, this, application);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


    }

    public void getNewPicturesOfMatches(View root) {
        List<String> matchUuid = new ArrayList<>();
        List<Picture> pics = new ArrayList<>();
        File file = new File("/ui/SamplePicture/sample_img.png");
        String path = file.getAbsolutePath();
        Bitmap pic = BitmapFactory.decodeFile(path);

        for (Match match : matches) {
            if (match.user1.uuid.equals(application.currentUserUuid)) {
                matchUuid.add(match.user2.uuid);
            } else {
                matchUuid.add(match.user1.uuid);
            }
        }
        //List<Picture> p = new ArrayList<>();
        for (String uuid : matchUuid) {
            HttpRequest req1 = new HttpRequest(application.host + "/user/" + uuid + "/pictures", HttpRequest.Method.GET);
            HttpRequestTask task1 = new HttpRequestTask();
            task1.setOnErrorListener(error -> {
                AlertDialog dialog = new AlertDialog.Builder(getActivity())
                        .setMessage("ERROR: " + error.toString())
                        .setNegativeButton("Cancel", (dialog1, which) -> {
                        })
                        .setPositiveButton("Try again", (dialog1, which) -> {
                        }).create();
                dialog.show();
            });
            task1.setOnResponseListener(data -> {

                p.addAll(Picture.parseArray(data.getResponseBody()));

                adapter = new FeedAdapter(p, this, application);

                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                swipeLayout.setRefreshing(false);

            });
            task1.execute(req1);
        }


        final RecyclerView recyclerView = root.findViewById(R.id.feed_recycler);

        final FeedAdapter adapter = new FeedAdapter(p, this, application);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


    }
}





