package com.example.asg02.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.example.asg02.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.appcompat.app.AppCompatActivity;

import com.example.asg02.controller.movie.GetMovieController;
import com.example.asg02.databinding.ActivityMainBinding;
import com.example.asg02.model.Account;
import com.example.asg02.model.User;
import com.example.asg02.utils.FirebaseUtils;
import com.example.asg02.utils.ImageUtils;
import com.example.asg02.utils.ViewUtils;
import com.example.asg02.vm.AccountViewModel;
import com.example.asg02.vm.BaseViewModel;
import com.example.asg02.vm.BookingViewModel;
import com.example.asg02.vm.MapViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPaySDK;

public class MainActivity extends BaseActivity {
    private ActivityMainBinding binding;
    private NavController controller;
    private User user;
    private String userId;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private FusedLocationProviderClient fusedLocationProviderClient;

    private static final int REQUEST_SETTINGS_CODE = 2;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 12;

    private MapViewModel mapViewModel;
    private AccountViewModel accountViewModel;
    private BookingViewModel bookingViewModel;
    private BaseViewModel baseViewModel;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();

        initControl();

    }

    private void init() {
        // zalopay
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // ZaloPay SDK Init
        ZaloPaySDK.init(2553, Environment.SANDBOX);
        // bind components with ids
        sharedPreferences = getSharedPreferences("LoginSharedPrefs", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        //map
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        mapViewModel = new ViewModelProvider(this).get(MapViewModel.class);
        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        bookingViewModel = new ViewModelProvider(this).get(BookingViewModel.class);
        baseViewModel = new ViewModelProvider(this).get(BaseViewModel.class);

        user = (User) getIntent().getSerializableExtra("user");
        userId = getIntent().getStringExtra("userId");

        accountViewModel.setUser(user);
        accountViewModel.setUserId(userId);

        binding.navViewLayout.name.setText(user.getName());
        binding.navViewLayout.point.setText(String.valueOf(user.getPoint()));
        binding.navViewLayout.expense.setText(String.valueOf(user.getExpense()));
        if (user.getAvatar() != null) {
            binding.navViewLayout.imageView.setImageBitmap(ImageUtils.cropToCircleWithBorder(ImageUtils.decodeBitmap(user.getAvatar()), 20, Color.parseColor("#59351A")));
        } else {
            binding.navViewLayout.imageView.setImageResource(R.drawable.account_icon);
        }

        setSupportActionBar(binding.appBarMain.toolbar);

        controller = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);

        ImageView img = binding.navViewLayout.layoutBarcode.barCode;
        img.setImageBitmap(ImageUtils.generateBarcode(userId, 200, 50));
        binding.navViewLayout.layoutBarcode.barCodeText.setText(userId);


        // access permission
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void initControl() {
        binding.appBarMain.navigationToggle.setOnClickListener(v -> {
            openDrawer();
        });

        controller.addOnDestinationChangedListener((ctr, des, arg) -> {
            getSupportActionBar().setTitle(des.getLabel());
            if (ctr.getCurrentDestination().getId() == R.id.nav_home) {
                binding.appBarMain.toolbar.setNavigationIcon(R.drawable.account_icon);
                binding.appBarMain.ticket.setVisibility(View.VISIBLE);
                binding.appBarMain.navigationToggle.setVisibility(View.VISIBLE);
            } else if (ctr.getCurrentDestination().getId() == R.id.nav_select_cinema_map
                    || ctr.getCurrentDestination().getId() == R.id.nav_maps) {
                binding.appBarMain.toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_ios_new_24);
                binding.appBarMain.ticket.setVisibility(View.GONE);
                binding.appBarMain.navigationToggle.setVisibility(View.GONE);
            } else if (ctr.getCurrentDestination().getId() == R.id.nav_success
                    || ctr.getCurrentDestination().getId() == R.id.nav_qr_code_of_booking) {
                binding.appBarMain.toolbar.setNavigationIcon(null);
                binding.appBarMain.ticket.setVisibility(View.GONE);
                binding.appBarMain.navigationToggle.setVisibility(View.GONE);
            } else {
                binding.appBarMain.toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_ios_new_24);
                binding.appBarMain.ticket.setVisibility(View.GONE);
            }
        });



        // open setting
        binding.navViewLayout.settings.setOnClickListener(v -> {
            closeDrawer();
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            intent.putExtra("user", user);
            intent.putExtra("userId", userId);
            startActivityForResult(intent, REQUEST_SETTINGS_CODE);
        });

        // logout
        binding.navViewLayout.logOut.setOnClickListener(v -> {
            String message = "Bạn chắc chắn muốn đăng xuất chứ?";
            Dialog dialog = ViewUtils.createAskingDialog(message, MainActivity.this, v1 -> {
                closeDrawer();
                // logout
                FirebaseUtils.getAuth().signOut();
                editor.clear().apply();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            });
            dialog.show();
        });

        binding.appBarMain.toolbar.setNavigationOnClickListener(v -> {
            if (controller.getCurrentDestination().getId() == R.id.nav_home) {
                moveToAccountInfo();
                closeDrawer();
            } else {
                controller.navigateUp();
            }
        });

        binding.navViewLayout.accountInfo.setOnClickListener(v -> {
            moveToAccountInfo();
            closeDrawer();
        });

        binding.navViewLayout.notification.setOnClickListener(v -> {
            if (controller.getCurrentDestination().getId() == R.id.nav_notification) {
                closeDrawer();
                return;
            }
            controller.navigate(R.id.nav_notification);
            closeDrawer();
        });

        binding.navViewLayout.myTicket.setOnClickListener(v -> {
            if (controller.getCurrentDestination().getId() == R.id.nav_listBooking) {
                closeDrawer();
                return;
            }
            controller.navigate(R.id.nav_listBooking);
            closeDrawer();
        });

        binding.appBarMain.ticket.setOnClickListener(v -> {
            if (controller.getCurrentDestination().getId() == R.id.nav_listBooking) {
                return;
            }
            controller.navigate(R.id.nav_listBooking);
        });

        binding.navViewLayout.cinemas.setOnClickListener(v -> {
            if (controller.getCurrentDestination().getId() == R.id.nav_select_complex_map) {
                closeDrawer();
                return;
            }
            controller.navigate(R.id.nav_select_complex_map);
            closeDrawer();
        });

        binding.navViewLayout.events.setOnClickListener(v -> {
            if (controller.getCurrentDestination().getId() == R.id.nav_list_event) {
                closeDrawer();
                return;
            }
            controller.navigate(R.id.nav_list_event);
            closeDrawer();
        });

        binding.navViewLayout.info.setOnClickListener(v -> {
            String url = "https://github.com/Huypham07/MobDev_Nhom03_Asg02";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);

//            if (intent.resolveActivity(getPackageManager()) != null) {
//            }
            closeDrawer();
        });
    }

    private void closeDrawer() {
        if (binding.drawerLayout != null) {
            binding.drawerLayout.closeDrawer(Gravity.RIGHT);
        }
    }

    private void openDrawer() {
        if (binding.drawerLayout != null) {
            binding.drawerLayout.openDrawer(Gravity.RIGHT);
        }
    }

    private void moveToAccountInfo() {
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("fragmentID", R.id.nav_account);
        intent.putExtras(bundle);
        intent.putExtra("user", user);
        intent.putExtra("userId", userId);
        startActivityForResult(intent, REQUEST_SETTINGS_CODE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.navViewLayout.name.setText(user.getName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_SETTINGS_CODE) {
            user = (User) data.getSerializableExtra("user");
            accountViewModel.setUser(user);
            binding.navViewLayout.name.setText(user.getName());
            if (user.getAvatar() != null) {
                binding.navViewLayout.imageView.setImageBitmap(ImageUtils.cropToCircleWithBorder(ImageUtils.decodeBitmap(user.getAvatar()), 20, Color.parseColor("#59351A")));
                binding.navViewLayout.imageView.setImageTintList(null);
            } else {
                binding.navViewLayout.imageView.setImageResource(R.drawable.account_icon);
                binding.navViewLayout.imageView.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.greyBackground)));
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }

    private void getCurrentLocation() {
        try {
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                        mapViewModel.setCurrentLatLng(currentLatLng);
                    });
        } catch (SecurityException e) {
            Log.e("Location permission denied", "Location permission denied");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Log.e("Location permission denied", "Location permission denied");
            }
        }
    }
}