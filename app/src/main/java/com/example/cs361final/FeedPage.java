package com.example.cs361final;
import com.example.cs361final.R;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.cs361final.databinding.FeedPageBinding;


public class FeedPage extends AppCompatActivity {

    FeedPageBinding binding;

    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        binding = FeedPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragment(new HomeFragment());
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            int itemId = item.getItemId();
            if(itemId == R.id.homeB) {
                replaceFragment(new HomeFragment());
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

    private void replaceFragment(Fragment F) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, F);
        fragmentTransaction.commit();
    }
}
