package com.example.asg02.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.example.asg02.R;
import com.example.asg02.databinding.SettingsActivityBinding;
import com.example.asg02.model.User;

public class SettingsActivity extends BaseActivity {
    private SettingsActivityBinding binding;
    private NavController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SettingsActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        controller = Navigation.findNavController(this, R.id.nav_host_fragment_content_settings);
        controller.addOnDestinationChangedListener((ctr, des, arg) -> {
            getSupportActionBar().setTitle(des.getLabel());
        });

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            int fragmentId = bundle.getInt("fragmentID", -1);
            if (fragmentId != -1) {
                controller.navigate(fragmentId);
            }
        }

        binding.toolbar.setNavigationOnClickListener(v -> {
            if (!controller.navigateUp()) {
                Intent intent = getIntent();
                intent.putExtra("user", (User) intent.getSerializableExtra("user"));
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("SettingsActivity", "onDestroy");
    }
}