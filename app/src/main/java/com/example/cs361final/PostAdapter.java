package com.example.cs361final;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class PostAdapter extends ArrayAdapter<PostData> {
    private Context context;
    private List<PostData> posts;
    private String baseUrl = "http://10.0.2.2:8080/";

    public PostAdapter(Context context, List<PostData> posts) {
        super(context, R.layout.list_feed, posts);
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_feed, parent, false);
        }

        PostData post = posts.get(position);

        // Bind data to views
        TextView username = convertView.findViewById(R.id.username);
        TextView dateTime = convertView.findViewById(R.id.dateTime);
        TextView contentText = convertView.findViewById(R.id.contentText);
        ShapeableImageView userProfile = convertView.findViewById(R.id.userProfile);
        ShapeableImageView postImage = convertView.findViewById(R.id.postImage);

        username.setText(post.getPostOwnerUsername());
        dateTime.setText(post.getDateTime());
        contentText.setText(post.getContent());

        // สร้าง URL แบบสมบูรณ์สำหรับรูปโปรไฟล์ของผู้โพสต์
        String profileImageUrl = baseUrl + post.getPostOwnerProfileImage();
        Glide.with(context).load(profileImageUrl).into(userProfile);

        // สร้าง URL แบบสมบูรณ์สำหรับภาพโพสต์
        String postImageUrl = baseUrl + (post.getPostImage() != null ? post.getPostImage() : "");
        if (post.getPostImage() != null) {
            Glide.with(context).load(postImageUrl).into(postImage);
        } else {
            postImage.setVisibility(View.GONE); // ซ่อนภาพถ้าไม่มี
        }

        return convertView;
    }
}
