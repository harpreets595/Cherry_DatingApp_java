package com.example.project.ui.messages;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.CherryApplication;
import com.example.project.models.Match;
import com.example.project.models.Profile;
import com.example.project.networking.HttpRequest;
import com.example.project.networking.HttpRequestTask;
import com.example.project.ui.firstDate.CategoryActivity;
import com.example.project.R;
import com.example.project.models.Conversation;
import com.example.project.models.Message;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A placeholder fragment containing a simple view.
 */
public class MessageActivityFragment extends Fragment {

    private CherryApplication application;
    private Match match;

    private TextView message_textView;
    private MessageAdapter adapter;
    private RecyclerView messagesView;
    private CardView firstDateCard;

    public MessageActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_message, container, false);

        match = getActivity().getIntent().getParcelableExtra("match");
        application = (CherryApplication) getActivity().getApplication();

        messagesView = root.findViewById(R.id.message_RecyclerView);
        message_textView = root.findViewById(R.id.message_EditText);

        firstDateCard = root.findViewById(R.id.firstDate_CardView);
        firstDateCard.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), CategoryActivity.class);
            startActivity(intent);
        });

        ImageButton sendButton = root.findViewById(R.id.sendMessage_ImageButton);
        sendButton.setOnClickListener(view -> {
            SendMessage(new Message().setMatch(match).setMessage(message_textView.getText().toString()).setTimesent(new Date()).setFromuser(application.currentUser));
        });

        Timer timer = new Timer();
        timer.schedule(new GetNewMessages(), 0, 5000);

        return root;
    }

    private void GetAllMessages () {
        List<Message> messages = new ArrayList<>();
        HttpRequest req = new HttpRequest(application.host + "/match/" + match.id + "/messages", HttpRequest.Method.GET);
        HttpRequestTask task = new HttpRequestTask();
        task.setOnErrorListener(error -> {
            // Server error
            AlertDialog dialog = new AlertDialog.Builder(getActivity())
                    .setMessage("ERROR: " + error.toString())
                    .setNegativeButton("Cancel", (dialog1, which) -> {})
                    .setPositiveButton("Try again", (dialog1, which) -> {
                        GetAllMessages();
                    }).create();
            dialog.show();
        });
        task.setOnResponseListener(response -> {
            messages.addAll(Message.parseArray(response.getResponseBody()));

            adapter = new MessageAdapter(messages, this);
            messagesView.setAdapter(adapter);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            layoutManager.setStackFromEnd(true);
            messagesView.setLayoutManager(layoutManager);
            messagesView.scrollToPosition(adapter.data.size() -1 );
        });
        task.execute(req);
    }

    private void SendMessage(Message message) {
        message_textView.setText("");
        HttpRequest req = new HttpRequest(application.host + "/message", HttpRequest.Method.POST);
        req.setRequestBody("application/json", message.format(application));
        HttpRequestTask task = new HttpRequestTask();
        task.setOnErrorListener(error -> {
            // Server error
            AlertDialog dialog = new AlertDialog.Builder(getActivity())
                    .setMessage("ERROR: " + error.toString())
                    .setNegativeButton("Cancel", (dialog1, which) -> {})
                    .setPositiveButton("Try again", (dialog1, which) -> {
                        SendMessage(message);
                    }).create();
            dialog.show();
        });
        task.setOnResponseListener(response -> {
            adapter.data.add(message);
            adapter.notifyDataSetChanged();

            messagesView.scrollToPosition(adapter.data.size() - 1);
        });
        task.execute(req);


    }

    private class GetNewMessages extends TimerTask {

        @Override
        public void run() {
            GetAllMessages();
        }
    }

    private class MessageAdapter extends RecyclerView.Adapter<MessageViewHolder> {

        private MessageActivityFragment fragment;
        private List<Message> data;

        public MessageAdapter(List<Message> data, MessageActivityFragment fragment) {
            this.data = data;
            this.fragment = fragment;
        }

        @NonNull
        @Override
        public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_list_item, parent, false);
            MessageViewHolder holder = new MessageViewHolder(root,fragment);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
            holder.set(data.get(position));
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }

    private class MessageViewHolder extends RecyclerView.ViewHolder {

        private final TextView message_textView;
        private final TextView date_textView;
        private final LinearLayout linearLayout;

        public MessageViewHolder(@NonNull View itemView, MessageActivityFragment fragment) {
            super(itemView);

            linearLayout = itemView.findViewById(R.id.messages_LinearLayout);
            message_textView = itemView.findViewById(R.id.message_TextView);
            date_textView = itemView.findViewById(R.id.date_TextView);
        }

        public void set(Message message) {
            message_textView.setText(message.message);
            if (message.fromuser.uuid.equals(application.currentUserUuid)) {

                linearLayout.setGravity(Gravity.RIGHT);
            } else {
                linearLayout.setGravity(Gravity.LEFT);
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm");
            date_textView.setText(dateFormat.format(message.timesent));
        }
    }
}