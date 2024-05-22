package com.example.asg02.view.ui.map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.asg02.R;
import com.example.asg02.databinding.FragmentMapsBinding;
import com.example.asg02.model.Cinema;
import com.example.asg02.vm.BookingViewModel;
import com.example.asg02.vm.MapViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MapsFragment extends Fragment implements OnMapReadyCallback {
    private MapViewModel mapViewModel;
    private FragmentMapsBinding binding;
    private Cinema cinema;
    private GoogleMap googleMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 12;

    private Polyline currentPolyline;
    private SupportMapFragment mapFragment;
    public LatLng currentLatLng;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mapViewModel = new ViewModelProvider(requireActivity()).get(MapViewModel.class);

        binding = FragmentMapsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mapViewModel.getCinema().observe(
                getViewLifecycleOwner(),
                cinema -> {
                    this.cinema = cinema;
                    mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
                    if (mapFragment != null) {
                        mapFragment.getMapAsync(this);
                    }

                    fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
                }
        );

        return root;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            this.googleMap.setMyLocationEnabled(true);
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(getActivity(), location -> {
                        if (location != null) {
                            currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                            this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15)); // Zoom level 15

                            LatLng latLng = new LatLng(cinema.getLatitude(), cinema.getLongitude());
                            this.googleMap.addMarker(new MarkerOptions().position(latLng).title(cinema.getName()));

                            this.googleMap.setOnMarkerClickListener(marker -> {
                                LatLng destinationLatLng = marker.getPosition();
                                String distance = String.format("%.2fKm", calculateDistance(destinationLatLng));
                                marker.setSnippet(distance);
                                marker.showInfoWindow();
                                showRoute(currentLatLng, destinationLatLng);
                                return false;
                            });

                            showRoute(currentLatLng, latLng);
                        } else {
                            Log.e("Location is null", "Location is null");
                        }
                    });

        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void showRoute(LatLng currentLatLng, LatLng destinationLatLng) {
        if (currentPolyline != null) {
            currentPolyline.remove();
        }

        String url = "https://maps.googleapis.com/maps/api/directions/json?origin=" +
                currentLatLng.latitude + "," + currentLatLng.longitude +
                "&destination=" + destinationLatLng.latitude + "," + destinationLatLng.longitude +
                "&key=" + getString(R.string.google_maps_key);

        RequestQueue queue = Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray routes = response.getJSONArray("routes");
                        if (routes.length() > 0) {
                            JSONObject route = routes.getJSONObject(0);
                            JSONObject polyline = route.getJSONObject("overview_polyline");
                            String polylinePoints = polyline.getString("points");

                            List<LatLng> decodedPolyline = PolyUtil.decode(polylinePoints);

                            PolylineOptions polylineOptions = new PolylineOptions()
                                    .addAll(decodedPolyline)
                                    .color(Color.BLUE)
                                    .width(10);
                            currentPolyline = googleMap.addPolyline(polylineOptions);
                        } else {
                            Log.e("No routes found", "No routes found");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                , error -> {
            Log.e("Error fetching route", "Error fetching route");
        });
        queue.add(jsonObjectRequest);
    }

    public float calculateDistance(LatLng cinemaLatLng) {
        float[] results = new float[1];
        if (currentLatLng == null) {
            Log.e("e", "e");
        } else {
            Log.e("a", "a");
        }
        android.location.Location.distanceBetween(currentLatLng.latitude, currentLatLng.longitude, cinemaLatLng.latitude, cinemaLatLng.longitude, results);
        float distanceInMeters = results[0];
        float distanceInKm = distanceInMeters / 1000;
        return distanceInKm;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onMapReady(googleMap);
            } else {
                Log.e("Location permission denied", "Location permission denied");
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}