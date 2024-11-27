package com.example.cs361final;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {

    TextView username, gmail;
    ImageView Pimage;
    Button logoutBTN, editprofileBTN;
    String uname;
    private SharedPreferences sharedPreferences;

    public ProfileFragment() {

    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        logoutBTN = view.findViewById(R.id.logoutButton);
        sharedPreferences = getActivity().getSharedPreferences("App", Context.MODE_PRIVATE);
        username = view.findViewById(R.id.username);
        gmail = view.findViewById(R.id.gmail);
        editprofileBTN = view.findViewById(R.id.editProfileButton);

        username.setText(sharedPreferences.getString("username", ""));
        gmail.setText(sharedPreferences.getString("gmail", ""));

        editprofileBTN.setOnClickListener(v -> {
            Intent EP = new Intent(getActivity(), EditprofilePage.class);
            startActivity(EP);
            getActivity().finish();
        });

        logoutBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue queue = Volley.newRequestQueue(getActivity());
                String url = "http://10.0.2.2:8080/Logout.php";

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response.equals("success")) {
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("logged", "");
                                    editor.putString("username", "");
                                    editor.putString("gmail", "");
                                    editor.putString("apiKey", "");
                                    editor.apply();

                                    Intent P = new Intent(getActivity(), LoginPage.class);
                                    startActivity(P);
                                    getActivity().finish();
                                } else {
                                    Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> paramV = new HashMap<>();
                        paramV.put("username", sharedPreferences.getString("username", ""));
                        paramV.put("apiKey", sharedPreferences.getString("apiKey", ""));
                        return paramV;
                    }
                };
                queue.add(stringRequest);
            }
        });

        Pimage = view.findViewById(R.id.EditImage);
        String urlForImage = "http://10.0.2.2:8080/readImage.php";

        RequestQueue queue1 = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, urlForImage,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            if(status.equals("success")) {
                                String SimagePath = message;
                                String fullImageUrl = "http://10.0.2.2:8080/" + SimagePath;

                                Glide.with(requireContext())
                                    .load(fullImageUrl)
                                    .into(Pimage);
                            } else {
                                //Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> paramV = new HashMap<>();
                paramV.put("username", sharedPreferences.getString("username", ""));
                return paramV;
            }
        };
        queue1.add(stringRequest1);

        return view;
    }
}