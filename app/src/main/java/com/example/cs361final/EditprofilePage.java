package com.example.cs361final;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
import java.util.HashMap;
import java.util.Map;

public class EditprofilePage extends AppCompatActivity {

    Button cancelBTN, confirmBTN;
    ImageView editImage;
    EditText editUsername, editGmail;
    TextView warn;
    Bitmap bitmap;
    String username, gmail, oldUsername, oldGmail;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editprofile_page);
        cancelBTN = findViewById(R.id.cancelButton);
        confirmBTN = findViewById(R.id.uploadButton);
        editImage = findViewById(R.id.EditImage);
        editUsername = findViewById(R.id.EditUsername);
        editGmail = findViewById(R.id.EditGmail);
        warn = findViewById(R.id.warning);
        sharedPreferences = getSharedPreferences("App", MODE_PRIVATE);
        oldUsername = sharedPreferences.getString("username", "");
        oldGmail = sharedPreferences.getString("gmail", "");
        Log.d("EditProfilePage", "oldUsername = " + oldUsername);
        Log.d("EditProfilePage", "oldGmail = " + oldGmail);

        cancelBTN.setOnClickListener(view -> {
            Intent C = new Intent(EditprofilePage.this, FeedPage.class);
            C.putExtra("fragmentToLoad", "ProfileFragment");
            startActivity(C);
            finish();
        });

        ActivityResultLauncher<Intent> activityResultLauncher =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            Uri uri = data.getData();
                            try {
                                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
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

        confirmBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = String.valueOf(editUsername.getText());
                if(username.isEmpty()) {
                    username = oldUsername;
                }
                gmail = String.valueOf(editGmail.getText());
                if(gmail.isEmpty()) {
                    gmail = oldGmail;
                }

                Log.d("EditProfilePage", "username = " + username);
                Log.d("EditProfilePage", "gmail = " + gmail);

                final String base64Image;
                ByteArrayOutputStream byteArrayOutputStream;
                byteArrayOutputStream = new ByteArrayOutputStream();
                if(bitmap != null) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    byte[] bytes = byteArrayOutputStream.toByteArray();
                    base64Image = Base64.encodeToString(bytes, Base64.DEFAULT);

                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    String url ="http://10.0.2.2:8080/editProfile.php";

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        String status = jsonObject.getString("status");
                                        String message = jsonObject.getString("message");
                                        if(status.equals("success")) {
                                            username = jsonObject.getString("username");
                                            gmail = jsonObject.getString("gmail");
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putString("username", username);
                                            editor.putString("gmail", gmail);
                                            editor.apply();
                                            Toast.makeText(EditprofilePage.this, "Edit profile success!", Toast.LENGTH_SHORT).show();
                                            Log.d("EditprofilePage", "YES BITMAP");
                                            Intent F = new Intent(getApplicationContext(), FeedPage.class);
                                            F.putExtra("fragmentToLoad", "ProfileFragment");
                                            startActivity(F);
                                            finish();
                                        } else {
                                            warn.setText(message);
                                            Toast.makeText(EditprofilePage.this, "Edit Profile FAIL", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            warn.setText("Error: " + error.toString());
                            Toast.makeText(getApplicationContext(), "Request failed: " + error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }){
                        protected Map<String, String> getParams(){
                            Map<String, String> paramV = new HashMap<>();
                            paramV.put("oldUsername", oldUsername);
                            paramV.put("username", username);
                            paramV.put("gmail", gmail);
                            paramV.put("image", base64Image);
                            return paramV;
                        }
                    };
                    queue.add(stringRequest);
                } else {
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    String url ="http://10.0.2.2:8080/editProfile.php";

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        String status = jsonObject.getString("status");
                                        String message = jsonObject.getString("message");
                                        if(status.equals("success")) {
                                            username = jsonObject.getString("username");
                                            gmail = jsonObject.getString("gmail");
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putString("username", username);
                                            editor.putString("gmail", gmail);
                                            editor.apply();
                                            Toast.makeText(EditprofilePage.this, "Edit profile success!", Toast.LENGTH_SHORT).show();
                                            Log.d("EditprofilePage", "NO BITMAP");
                                            Intent F = new Intent(getApplicationContext(), FeedPage.class);
                                            F.putExtra("fragmentToLoad", "ProfileFragment");
                                            startActivity(F);
                                            finish();
                                        } else {
                                            warn.setText(message);
                                            Toast.makeText(EditprofilePage.this, "Edit Profile FAIL", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            warn.setText("Error: " + error.toString());
                            Toast.makeText(getApplicationContext(), "Request failed: " + error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }){
                        protected Map<String, String> getParams(){
                            Map<String, String> paramV = new HashMap<>();
                            paramV.put("oldUsername", oldUsername);
                            paramV.put("username", username);
                            paramV.put("gmail", gmail);
                            return paramV;
                        }
                    };
                    queue.add(stringRequest);
                }
            }
        });
    }
}

