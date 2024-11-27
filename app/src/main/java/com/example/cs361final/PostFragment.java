package com.example.cs361final;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class PostFragment extends Fragment {

    ImageView editImage;
    Bitmap bitmap;
    Button postBTN;
    EditText contentInput;
    String content, userId;
    SharedPreferences sharedPreferences;

    public PostFragment() {

    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post, container, false);
        editImage = view.findViewById(R.id.EditImage);
        postBTN = view.findViewById(R.id.postButton);
        contentInput = view.findViewById(R.id.contentInput);
        ContentResolver contentResolver = requireContext().getContentResolver();
        sharedPreferences = getActivity().getSharedPreferences("App", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("userId", "");
        Log.d("PostFragment", "UserId = " + userId);

        ActivityResultLauncher<Intent> activityResultLauncher =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            Uri uri = data.getData();
                            try {
                                bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri);
                                editImage.setImageBitmap(bitmap);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK);
                gallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncher.launch(gallery);
            }
        });

        postBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dateTime = getDateTime();
                Log.d("PostFragment", "DateTime = " + dateTime);
                final String base64Image;
                ByteArrayOutputStream byteArrayOutputStream;
                byteArrayOutputStream = new ByteArrayOutputStream();
                if(bitmap != null) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    byte[] bytes = byteArrayOutputStream.toByteArray();
                    base64Image = Base64.encodeToString(bytes, Base64.DEFAULT);
                    content = String.valueOf(contentInput.getText());
                    String url ="http://10.0.2.2:8080/post.php";

                    RequestQueue queue = Volley.newRequestQueue(getActivity());
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    JSONObject jsonObject = null;
                                    try {
                                        jsonObject = new JSONObject(response);
                                        String status = jsonObject.getString("status");
                                        String message = jsonObject.getString("message");
                                        if(status.equals("success")) {
                                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
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
                            paramV.put("ownerId", userId);
                            paramV.put("content", content);
                            paramV.put("dateTime", dateTime);
                            paramV.put("Image", base64Image);
                            return paramV;
                        }
                    };
                    queue.add(stringRequest);
                    bitmap = null;
                    resetPage();
                } else {
                    content = String.valueOf(contentInput.getText());
                    String url ="http://10.0.2.2:8080/post.php";

                    RequestQueue queue = Volley.newRequestQueue(getActivity());
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    JSONObject jsonObject = null;
                                    try {
                                        jsonObject = new JSONObject(response);
                                        String status = jsonObject.getString("status");
                                        String message = jsonObject.getString("message");
                                        if(status.equals("success")) {
                                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
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
                            paramV.put("ownerId", userId);
                            paramV.put("content", content);
                            paramV.put("dateTime", dateTime);
                            return paramV;
                        }
                    };
                    queue.add(stringRequest);
                    resetPage();
                }
            }
        });

        return view;
    }

    public void resetPage() {
        contentInput.setText("");
        editImage.setImageResource(R.drawable.baseline_image_24);
    }

    public String getDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime now = LocalDateTime.now();

        return now.format(formatter);
    }
}