package com.example.cs361final;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.cs361final.databinding.FeedPageBinding;


public class FeedPage extends AppCompatActivity {

    private boolean backPressedOnce = false;
    private Toast backToast;
    private static final int BACK_PRESS_INTERVAL = 2000;
    FeedPageBinding binding;

    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        binding = FeedPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        String fragmentName = intent.getStringExtra("fragmentToLoad");

        if (fragmentName != null) {
            switch (fragmentName) {
                case "PostFragment":
                    replaceFragment(new PostFragment());
                    break;
                case "ProfileFragment":
                    replaceFragment(new ProfileFragment());
                    break;
            }
        } else {
            replaceFragment(new FeedFragment());
        }

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            int itemId = item.getItemId();
            if(itemId == R.id.homeB) {
                replaceFragment(new FeedFragment());
                return true;
            } else if(itemId == R.id.postB) {
                replaceFragment(new PostFragment());
                return true;
            } else if(itemId == R.id.profileB) {
                replaceFragment(new ProfileFragment());
                return true;
            } else {
                return true;
            }

        });
    }

    public void onBackPressed() {
        if (backPressedOnce) {
            if (backToast != null) {
                backToast.cancel();
            }
            super.onBackPressed();
            return;
        }

        this.backPressedOnce = true;
        backToast = Toast.makeText(this, "Press Back again to exit", Toast.LENGTH_SHORT);
        backToast.show();

        new Handler().postDelayed(() -> backPressedOnce = false, BACK_PRESS_INTERVAL);
    }

    private void replaceFragment(Fragment F) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, F);
        fragmentTransaction.commit();
    }
}
