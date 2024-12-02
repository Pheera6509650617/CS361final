package com.example.cs361final;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    SharedPreferences sharedPreferences;

    public HomeFragment() {

    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        String url = "http://10.0.2.2:8080/fetchPost.php";
        sharedPreferences = getActivity().getSharedPreferences("App", Context.MODE_PRIVATE);
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    List<PostData> posts = new ArrayList<>();
                    try {
                        for (int i = response.length() - 1; i >= 0; i--) {
                            JSONObject postObj = response.getJSONObject(i);

                            String username = postObj.getString("postOwnerUsername");
                            String profileImage = postObj.getString("postOwnerProfileImage");
                            String dateTime = postObj.getString("dateTime");
                            String content = postObj.getString("content");
                            String image = postObj.isNull("Image") ? null : postObj.getString("Image");

                            posts.add(new PostData(username, profileImage, dateTime, content, image));
                        }

                        ListView listView = view.findViewById(R.id.listFeed);
                        PostAdapter adapter = new PostAdapter(getActivity(), posts);
                        listView.setAdapter(adapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Log.d("HomeFragment", "Error");
                });

        queue.add(jsonArrayRequest);

        return view;
    }
}