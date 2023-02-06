package com.graduation.mah.weather.ui.mapWeather;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.graduation.mah.weather.R;
import com.graduation.mah.weather.adapters.GooglePlaceAdapter;
import com.graduation.mah.weather.databinding.FragmentGalleryBinding;
import com.graduation.mah.weather.model.googleModel.GooglePlaceModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DashboardFragment extends Fragment   implements OnMapReadyCallback {

    private FragmentGalleryBinding binding;
    SharedPreferences locationPref;
    SharedPreferences.Editor locationEditor;
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    private static final String API_KEY = "AIzaSyAMyIZtIMeZjTu-b2P_xyyZaNBeIv-fkzU";
    protected LatLng start = null;
    protected LatLng end = null;
    private List<Polyline> polylines = null;
    Context context;
    View mMapView ;

    String CITY = "London,GB";
    String API = "75497c019df0196f93769e2e9235e706";

    private GoogleMap mMap;
    String pickPlace, distPlace, pickPlaceId, distPlaceId = "", companyId, userId, currentPlace;
    double longitudeStartPoint, latitudeStartPoint, lo, la;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    ArrayList<GooglePlaceModel> googlePlaceModels;
    private int searchTypeAgain = 0;
    Marker marker;











    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
                binding = FragmentGalleryBinding.inflate(inflater, container, false);
          View root = binding.getRoot();
        binding.search.setText("");
        binding.relativeList.setVisibility(View.GONE);
        binding.listView.setVisibility(View.GONE);
        try {
            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            mMapView=mapFragment.getView();

                    mapFragment.getMapAsync(DashboardFragment.this);
        } catch (Exception e) {
            e.printStackTrace();
        }


        locationPref = getContext().getSharedPreferences("location", MODE_PRIVATE);
        preferences = getContext().getSharedPreferences("user_details", MODE_PRIVATE);
        editor = preferences.edit();
        locationEditor = locationPref.edit();

        binding.linSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSearchList(1);
            }
        });

        binding.search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                showSearchList(1);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });





        return root;
    }








//    {
////        GalleryViewModel galleryViewModel =
////                new ViewModelProvider(this).get(GalleryViewModel.class);
//
//        binding = FragmentGalleryBinding.inflate(inflater, container, false);
//        View root = binding.getRoot();
//
////        final TextView textView = binding.textGallery;
////        galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
//        return root;
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;

    }

    @Override
    public void onDetach() {
        super.onDetach();
        context = null;

    }



    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        View locationButton = ((View) mMapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        rlp.setMargins(0, 1250, 180, 0);

        la = Double.parseDouble(locationPref.getString("lat", "0"));
        lo = Double.parseDouble(locationPref.getString("long", "0"));
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        } else {
            mMap.setMyLocationEnabled(true);
        }
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(la, lo);

        CameraPosition INIT = new CameraPosition.Builder().target(sydney).zoom(14.5F).bearing(300F) // orientation
                .tilt(50F) // viewing angle
                .build();

        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(INIT));
        currentPlace = getAddressFromLatLng(context, sydney);

//        mMap.addMarker(new MarkerOptions().position(sydney).title(currentPlace));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


        Log.e("TAG", "onSuccess: long = " + lo + " lat = " + la + " currentPlace = " + currentPlace);


    }


    public static String getAddressFromLatLng(Context context, LatLng latLng) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(context, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            return addresses.get(0).getAddressLine(0);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }




    private void showSearchList(int searchType) {
        searchTypeAgain = searchType;



        googlePlaceModels = new ArrayList<>();


        if (binding.search.getText().toString().isEmpty()) {
//            mic.setVisibility(View.VISIBLE);
            binding.clear.setVisibility(View.GONE);
        } else {
//            mic.setVisibility(View.GONE);
            binding.clear.setVisibility(View.VISIBLE);
        }

        binding.clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.search.setText("");
                binding.relativeList.setVisibility(View.GONE);
                binding.listView.setVisibility(View.GONE);
            }
        });

        binding.search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (binding.search.getText().toString().isEmpty()) {
//                    mic.setVisibility(View.VISIBLE);
                    binding.clear.setVisibility(View.GONE);
                } else {
//                    mic.setVisibility(View.GONE);
                    binding.clear.setVisibility(View.VISIBLE);
                }

                if (s.toString().isEmpty()) {
                    googlePlaceModels.clear();
                    setAdapter();
                } else {
                    new GooglePlaces().execute(String.valueOf(s));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (!googlePlaceModels.get(position).getPlaceName()
                        .equalsIgnoreCase("Not Found")) {

                    if (searchType == 1) {
                        pickPlace = googlePlaceModels.get(position).getPlaceName();
                        pickPlaceId = googlePlaceModels.get(position).getPlaceID();

                        Log.e("TAG", "onItemClick: latitudeStartPoint =" + latitudeStartPoint + "longitudeStartPoint = " + longitudeStartPoint);

                        new GetLatLngDest().execute(pickPlaceId);
                        binding.relativeList.setVisibility(View.GONE);
                        binding.listView.setVisibility(View.GONE);
                    } else if (searchType == 2) {

                        distPlace = googlePlaceModels.get(position).getPlaceName();
                        distPlaceId = googlePlaceModels.get(position).getPlaceID();
                        new GetLatLngDest().execute(distPlaceId);


                        binding.relativeList.setVisibility(View.GONE);
                        binding.listView.setVisibility(View.GONE);
                    }


                }
            }
        });


        binding.relativeList.setVisibility(View.VISIBLE);
        binding.listView.setVisibility(View.VISIBLE);
    }

    public class GooglePlaces extends AsyncTask<String, String, String> {

        @Override
        public void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            String result = null;
            try {
                // Your API key
                String key = "?key=" + API_KEY;
                // components type
                String components = "&components=country:egy";
                // set input type
                String input = "&input=" + URLEncoder.encode(params[0], "utf8");
                // Building the url to the web service
                String strURL = PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON
                        + key + components + input;

                URL url = new URL(strURL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();

                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStreamReader inputStreamReader = new InputStreamReader(conn.getInputStream());
                    BufferedReader reader = new BufferedReader(inputStreamReader);
                    StringBuilder stringBuilder = new StringBuilder();
                    String temp;

                    while ((temp = reader.readLine()) != null) {
                        stringBuilder.append(temp);
                    }
                    result = stringBuilder.toString();
                } else {
                    result = "error";
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        public void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                googlePlaceModels.clear();
            } catch (Exception e) {

            }

            try {
                JSONObject jsonObj = new JSONObject(s);
                JSONArray jsonArray = jsonObj.getJSONArray("predictions");

                if (jsonObj.getString("status").equalsIgnoreCase("OK")) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        GooglePlaceModel googlePlaceModel = new GooglePlaceModel();
                        googlePlaceModel.setPlaceName(jsonArray.getJSONObject(i).getString("description"));
                        googlePlaceModel.setPlaceID(jsonArray.getJSONObject(i).getString("place_id"));
                        googlePlaceModels.add(googlePlaceModel);
                    }
                } else if (jsonObj.getString("status").equalsIgnoreCase("OVER_QUERY_LIMIT")) {
//                    Toast.makeText(getApplicationContext(), "You have exceeded your daily request quota for this API.", Toast.LENGTH_LONG).show();
                    GooglePlaceModel googlePlaceModel = new GooglePlaceModel();
                    googlePlaceModel.setPlaceName("Not Found");
                    googlePlaceModels.add(googlePlaceModel);
                } else {
                    GooglePlaceModel googlePlaceModel = new GooglePlaceModel();
                    googlePlaceModel.setPlaceName("Not Found");
                    googlePlaceModels.add(googlePlaceModel);
                }
                // set adapter
                setAdapter();
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    public void setAdapter() {
        GooglePlaceAdapter adapter = new GooglePlaceAdapter(getContext(), googlePlaceModels);
        binding.listView.setAdapter(adapter);
    }

    public class GetLatLngDest extends AsyncTask<String, String, String> {
        private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
        private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
        private static final String TYPE_DETAIL = "/details";
        private static final String OUT_JSON = "/json";
        private static final String API_KEY = "AIzaSyAMyIZtIMeZjTu-b2P_xyyZaNBeIv-fkzU";

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        String latLng;

        public GetLatLngDest() {
        }

        @Override
        protected String doInBackground(String... params) {
            String resultList = null;

            try {
                StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_DETAIL + OUT_JSON);
                sb.append("?placeid=" + URLEncoder.encode(params[0], "utf8"));
                sb.append("&key=" + API_KEY);
                URL url = new URL(sb.toString());
                Log.d("TAG", "placeDetail: url>>>>>>>>>>>>>>>>>>>>>>" + url);
                conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                InputStreamReader in = new InputStreamReader(conn.getInputStream());

                int read;
                char[] buff = new char[1024];
                while ((read = in.read(buff)) != -1) {
                    jsonResults.append(buff, 0, read);

                }
            } catch (MalformedURLException e) {
//                        //Log.e(LOG_TAG, "Error processing Places API URL", e);
//                        resultList = "resultList";
            } catch (IOException e) {
//                        //Log.e(LOG_TAG, "Error connecting to Places API", e);
//                        resultList = "resultList";
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }

            return resultList;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                // Create a JSON object hierarchy from the results
                //Log.e("creation du fichier Json", "creation du fichier Json");
                JSONObject jsonObj = new JSONObject(jsonResults.toString());
                //JSONArray predsJsonArray = jsonObj.getJSONArray("html_attributions");
                JSONObject result = jsonObj.getJSONObject("result")
                        .getJSONObject("geometry").getJSONObject("location");
                if (searchTypeAgain == 1) {
                    Double longitude = result.getDouble("lng");
                    Double latitude = result.getDouble("lat");
                    latitudeStartPoint = latitude;
                    longitudeStartPoint = longitude;
                    start = new LatLng(latitudeStartPoint, longitudeStartPoint);
                    CameraPosition INIT = new CameraPosition.Builder().target(start).zoom(13F).bearing(300F) // orientation
                            .tilt(50F) // viewing angle
                            .build();
                    mMap.moveCamera(CameraUpdateFactory.newCameraPosition(INIT));

                }


                latLng = String.valueOf(result.getDouble("lat"))
                        + "," + String.valueOf(result.getDouble("lng"));
                Log.d("TAG", latLng + " run: >>>>>>>>>>>>>"
                        + String.valueOf(result.getDouble("lat"))
                        + "," + String.valueOf(result.getDouble("lng")));

            } catch (JSONException e) {
                Log.e("tag", "Cannot process JSON results", e);
            }

        }
    }



}