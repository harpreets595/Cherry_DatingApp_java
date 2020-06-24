package com.example.project.ui.feed.adapter;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.CherryApplication;
import com.example.project.models.Match;
import com.example.project.networking.HttpRequest;
import com.example.project.networking.HttpRequestTask;
import com.example.project.ui.feed.PictureFeedActivityFragment;
import com.example.project.R;
import com.example.project.models.Conversation;
import com.example.project.models.Picture;
import com.example.project.ui.messages.MessageActivity;
import com.example.project.ui.userDetails.user_detail;

import java.util.List;


//////////////////////////////////////////////////////////////////////
//  This Class gets very confusing but i'll walk you through it!!   //
//////////////////////////////////////////////////////////////////////
public class FeedViewHolder extends RecyclerView.ViewHolder {

    //public static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm");

    //fields
    private CherryApplication application;
    private Conversation conversation;
    private  TextView username;
    private  CardView card;
    private ImageView messageArrow;
    private ImageView picture;
    private  View root;
    private PictureFeedActivityFragment fragment;
    private FeedAdapter adapter;

    private Picture pic;

    //constructor
    public FeedViewHolder(@NonNull View root, PictureFeedActivityFragment fragment, FeedAdapter adapter, CherryApplication app) {
        super(root);

        this.application = app;
        this.root = root;
        this.fragment = fragment;
        this.adapter = adapter;
        username = root.findViewById(R.id.card_user_textView);
        card = root.findViewById(R.id.cardView);
        messageArrow = root.findViewById(R.id.card_message_imageView);
        picture = root.findViewById(R.id.card_picture_imageView);
        //sets each not in the database to a card view

        messageArrow.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), MessageActivity.class);


            for(Match match : application.matches)
            {

                //get the match!
                if ((match.user1.uuid.equals(application.currentUserUuid) && match.user2.uuid.equals(pic.getUser().uuid)) || (match.user2.uuid.equals(application.currentUserUuid) && match.user1.uuid.equals(pic.getUser().uuid)))
                {
                    intent.putExtra("match", match);
                    break;
                }

            }
            view.getContext().startActivity(intent);

        });


        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), user_detail.class);
                intent.putExtra("uuid", pic.getUser().uuid);
                view.getContext().startActivity(intent);
            }
        });

    }
    public void set(Picture pic){
        this.pic = pic;
        username.setText(pic.getUsername());
        byte[] decodedString = Base64.decode(pic.getPicture(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        picture.setImageBitmap(decodedByte);
    }
}

