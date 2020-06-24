package com.example.project.ui.messages;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.CherryApplication;
import com.example.project.R;
import com.example.project.models.Match;
import com.example.project.networking.HttpRequest;
import com.example.project.networking.HttpRequestTask;

import java.util.ArrayList;
import java.util.List;

public class ConversationFragment extends Fragment {

    private CherryApplication application;
    private RecyclerView convoRecycler;
    private SwipeRefreshLayout swipeLayout;

    public ConversationFragment() {
        // Required empty public constructor
    }

    public static ConversationFragment newInstance(String param1, String param2) {
        ConversationFragment fragment = new ConversationFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_conversation, container, false);

        swipeLayout = root.findViewById(R.id.conversation_SwipeLayout);
        swipeLayout.setOnRefreshListener(() -> {
            RefreshList();
        });

        convoRecycler = root.findViewById(R.id.conversation_RecyclerView);
        application = (CherryApplication) getActivity().getApplication();

        ConversationAdapter convoAdapter = new ConversationAdapter(application.matches);

        convoRecycler.setAdapter(convoAdapter);
        convoRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        swipeLayout.setRefreshing(false);

        return root;
    }

    public void RefreshList() {
            swipeLayout.setRefreshing(true);
        HttpRequest req1 = new HttpRequest(application.host + "/user/" + application.currentUserUuid + "/matches1", HttpRequest.Method.GET);
        HttpRequestTask task1 = new HttpRequestTask();
        task1.setOnErrorListener(error -> {
            AlertDialog dialog = new AlertDialog.Builder(getActivity())
                    .setMessage("ERROR: " + error.toString())
                    .setNegativeButton("Cancel", (dialog1, which) -> {})
                    .setPositiveButton("Try again", (dialog1, which) -> {
                        RefreshList();
                    }).create();
            dialog.show();
        });
        task1.setOnResponseListener(data -> {

            List<Match> matches = Match.parseArray(data.getResponseBody());

            HttpRequest req2 = new HttpRequest(application.host + "/user/" + application.currentUserUuid + "/matches2", HttpRequest.Method.GET);
            HttpRequestTask task2 = new HttpRequestTask();

            task2.setOnErrorListener(error -> {
                // Server error
                AlertDialog dialog = new AlertDialog.Builder(getActivity())
                        .setMessage("ERROR: " + error.toString())
                        .setNegativeButton("Cancel", (dialog1, which) -> {})
                        .setPositiveButton("Try again", (dialog1, which) -> {
                            RefreshList();
                        }).create();
                dialog.show();
            });
            task2.setOnResponseListener(res -> {
                matches.addAll(Match.parseArray(res.getResponseBody()));
                application.matches = matches;

                ConversationAdapter convoAdapter = new ConversationAdapter(matches);

                convoRecycler.setAdapter(convoAdapter);
                convoRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

                swipeLayout.setRefreshing(false);
            });

            task2.execute(req2);


        });
        task1.execute(req1);
    }

    public class ConversationAdapter extends RecyclerView.Adapter<ConversationViewHolder> {

        private List<Match> data;

        public ConversationAdapter(List<Match> data) {
            this.data = data;
        }

        @NonNull
        @Override
        public ConversationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.conversation_list_item, parent, false);
            ConversationViewHolder holder = new ConversationViewHolder(root);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ConversationViewHolder holder, int position) {
            holder.set(data.get(position));
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }

    public class ConversationViewHolder extends RecyclerView.ViewHolder {

        private Match match;
        private final TextView nameTextView;
        private final TextView lastMessageTextView;

        public ConversationViewHolder(@NonNull View root) {
            super(root);

            root.setOnClickListener(view -> {
                Intent intent = new Intent(view.getContext(), MessageActivity.class);
                intent.putExtra("match", match);
                view.getContext().startActivity(intent);
            });

            nameTextView = root.findViewById(R.id.name_TextView);
            lastMessageTextView = root.findViewById(R.id.lastMessage_TextView);
        }

        public void set(Match match) {
            this.match = match;
            if (match.user1.uuid.equals(application.currentUserUuid)) {
                nameTextView.setText(match.user2.firstname);
            } else {
                nameTextView.setText(match.user1.firstname);
            }
            lastMessageTextView.setText("");
        }
    }
}