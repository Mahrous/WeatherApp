package com.graduation.mah.weather.ui.fragmentWeather;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.github.pwittchen.prefser.library.rx2.Prefser;
import com.graduation.mah.weather.R;
import com.graduation.mah.weather.databinding.FragmentHomeBinding;
import com.graduation.mah.weather.model.CityInfo;
import com.graduation.mah.weather.model.currentweather.CurrentWeatherResponse;
import com.graduation.mah.weather.model.daysweather.ListItem;
import com.graduation.mah.weather.model.daysweather.MultipleDaysWeatherResponse;
import com.graduation.mah.weather.model.db.CurrentWeather;
import com.graduation.mah.weather.model.db.FiveDayWeather;
import com.graduation.mah.weather.model.db.ItemHourlyDB;
import com.graduation.mah.weather.model.fivedayweather.FiveDayResponse;
import com.graduation.mah.weather.model.fivedayweather.ItemHourly;
import com.graduation.mah.weather.service.ApiService;
import com.graduation.mah.weather.service.HttpRequest;
import com.graduation.mah.weather.ui.about.AboutFragment;
import com.graduation.mah.weather.ui.activity.HourlyActivity;
import com.graduation.mah.weather.utils.ApiClient;
import com.graduation.mah.weather.utils.AppUtil;
import com.graduation.mah.weather.utils.Constants;
import com.graduation.mah.weather.utils.DbUtil;
import com.graduation.mah.weather.utils.MyApplication;
import com.graduation.mah.weather.utils.SnackbarUtil;
import com.graduation.mah.weather.utils.TextViewFactory;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.fastadapter.listeners.OnClickListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.android.AndroidScheduler;
import io.objectbox.query.Query;
import io.objectbox.reactive.DataObserver;
import io.objectbox.reactive.DataSubscriptionList;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

public class HomeFragment extends Fragment {
    String CITY = "London,GB";
    String API = "75497c019df0196f93769e2e9235e706";
    private FastAdapter<FiveDayWeather> mFastAdapter;
    private ItemAdapter<FiveDayWeather> mItemAdapter;
    private CompositeDisposable disposable = new CompositeDisposable();
    private String defaultLang = "en";
    private List<FiveDayWeather> fiveDayWeathers;
    private ApiService apiService;
    private FiveDayWeather todayFiveDayWeather;
    private Prefser prefser;
    private Box<CurrentWeather> currentWeatherBox;
    private Box<FiveDayWeather> fiveDayWeatherBox;
    private Box<ItemHourlyDB> itemHourlyDBBox;
    private DataSubscriptionList subscriptions = new DataSubscriptionList();
    private boolean isLoad = false;
    private CityInfo cityInfo;
    private String apiKey;
    private Typeface typeface;
    private int[] colors;
    private int[] colorsAlpha;
    private FragmentHomeBinding binding;
    Context context = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        com.graduation.mah.weather.ui.fragmentWeather.HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ((AppCompatActivity) requireActivity()).setSupportActionBar(binding.toolbarLayout.toolbar);


        initSearchView();
        initValues();
        setupTextSwitchers();
        initRecyclerView();
        showStoredCurrentWeather();
        showStoredFiveDayWeather();
        checkLastUpdate();
        setHasOptionsMenu(true);

        new weatherTask().execute();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void initSearchView() {

        binding.toolbarLayout.searchView.setVoiceSearch(false);
        binding.toolbarLayout.searchView.setHint(getString(R.string.search_label));
        binding.toolbarLayout.searchView.setCursorDrawable(R.drawable.custom_curosr);
        binding.toolbarLayout.searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                CITY = query;
                requestWeather(query, true);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        binding.toolbarLayout.searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.toolbarLayout.searchView.showSearch();
            }
        });

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.context = null;
    }

    private void initValues() {
        colors = getResources().getIntArray(R.array.mdcolor_500);
        colorsAlpha = getResources().getIntArray(R.array.mdcolor_500_alpha);
        prefser = new Prefser(getActivity());
        apiService = ApiClient.getClient().create(ApiService.class);
        BoxStore boxStore = MyApplication.getBoxStore();
        currentWeatherBox = boxStore.boxFor(CurrentWeather.class);
        fiveDayWeatherBox = boxStore.boxFor(FiveDayWeather.class);
        itemHourlyDBBox = boxStore.boxFor(ItemHourlyDB.class);
        binding.swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        binding.swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                cityInfo = prefser.get(Constants.CITY_INFO, CityInfo.class, null);
                if (cityInfo != null) {
                    long lastStored = prefser.get(Constants.LAST_STORED_CURRENT, Long.class, 0L);
                    if (AppUtil.isTimePass(lastStored)) {
                        requestWeather(cityInfo.getName(), false);
                    } else {
                        binding.swipeContainer.setRefreshing(false);
                    }
                } else {
                    binding.swipeContainer.setRefreshing(false);
                }
            }

        });
        binding.bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAboutFragment();
            }
        });
        if (context!=null)
        typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Vazir.ttf");

        binding.nextDaysButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtil.showFragment(new MultipleDaysFragment(), getActivity().getSupportFragmentManager(), true);
            }
        });
        binding.contentMainLayout.todayMaterialCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (todayFiveDayWeather != null) {
                    if (context!=null){
                    Intent intent = new Intent(context, HourlyActivity.class);
                    intent.putExtra(Constants.FIVE_DAY_WEATHER_ITEM, todayFiveDayWeather);
                    startActivity(intent);}
                }
            }
        });
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        binding.toolbarLayout.searchView.setMenuItem(item);
        super.onCreateOptionsMenu(menu, inflater);

    }

    private void setupTextSwitchers() {
        if (context!=null) {
            binding.contentMainLayout.tempTextView.setFactory(new TextViewFactory(context, R.style.TempTextView, true, typeface));
            binding.contentMainLayout.tempTextView.setInAnimation(context, R.anim.slide_in_right);
            binding.contentMainLayout.tempTextView.setOutAnimation(context, R.anim.slide_out_left);
            binding.contentMainLayout.descriptionTextView.setFactory(new TextViewFactory(context, R.style.DescriptionTextView, true, typeface));
            binding.contentMainLayout.descriptionTextView.setInAnimation(context, R.anim.slide_in_right);
            binding.contentMainLayout.descriptionTextView.setOutAnimation(context, R.anim.slide_out_left);
            binding.contentMainLayout.humidityTextView.setFactory(new TextViewFactory(context, R.style.HumidityTextView, false, typeface));
            binding.contentMainLayout.humidityTextView.setInAnimation(context, R.anim.slide_in_bottom);
            binding.contentMainLayout.humidityTextView.setOutAnimation(context, R.anim.slide_out_top);
            binding.contentMainLayout.windTextView.setFactory(new TextViewFactory(context, R.style.WindSpeedTextView, false, typeface));
            binding.contentMainLayout.windTextView.setInAnimation(context, R.anim.slide_in_bottom);
            binding.contentMainLayout.windTextView.setOutAnimation(context, R.anim.slide_out_top);
        }
    }

    private void initRecyclerView() {
        if (context!=null){
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        binding.contentMainLayout.recyclerView.setLayoutManager(layoutManager);
        mItemAdapter = new ItemAdapter<>();
        mFastAdapter = FastAdapter.with(mItemAdapter);
        binding.contentMainLayout.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.contentMainLayout.recyclerView.setAdapter(mFastAdapter);
        binding.contentMainLayout.recyclerView.setFocusable(false);
        mFastAdapter.withOnClickListener(new OnClickListener<FiveDayWeather>() {
            @Override
            public boolean onClick(@Nullable View v, @NonNull IAdapter<FiveDayWeather> adapter, @NonNull FiveDayWeather item, int position) {
                Intent intent = new Intent(context, HourlyActivity.class);
                intent.putExtra(Constants.FIVE_DAY_WEATHER_ITEM, item);
                startActivity(intent);
                return true;
            }
        });}
    }

    private void showStoredCurrentWeather() {
        Query<CurrentWeather> query = DbUtil.getCurrentWeatherQuery(currentWeatherBox);
        query.subscribe(subscriptions).on(AndroidScheduler.mainThread())
                .observer(new DataObserver<List<CurrentWeather>>() {
                    @Override
                    public void onData(@NonNull List<CurrentWeather> data) {
                        if (data.size() > 0) {
                            hideEmptyLayout();
                            CurrentWeather currentWeather = data.get(0);
                            if (context!=null) {
                                if (isLoad) {
                                    binding.contentMainLayout.tempTextView.setText(String.format(Locale.getDefault(), "%.0f??", currentWeather.getTemp()));
                                    binding.contentMainLayout.descriptionTextView.setText(AppUtil.getWeatherStatus(currentWeather.getWeatherId(), AppUtil.isRTL(context)));
                                    binding.contentMainLayout.humidityTextView.setText(String.format(Locale.getDefault(), "%d%%", currentWeather.getHumidity()));
                                    binding.contentMainLayout.windTextView.setText(String.format(Locale.getDefault(), getResources().getString(R.string.wind_unit_label), currentWeather.getWindSpeed()));
                                } else {
                                    binding.contentMainLayout.tempTextView.setCurrentText(String.format(Locale.getDefault(), "%.0f??", currentWeather.getTemp()));
                                    binding.contentMainLayout.descriptionTextView.setCurrentText(AppUtil.getWeatherStatus(currentWeather.getWeatherId(), AppUtil.isRTL(context)));
                                    binding.contentMainLayout.humidityTextView.setCurrentText(String.format(Locale.getDefault(), "%d%%", currentWeather.getHumidity()));
                                    binding.contentMainLayout.windTextView.setCurrentText(String.format(Locale.getDefault(), getResources().getString(R.string.wind_unit_label), currentWeather.getWindSpeed()));
                                }
                                binding.contentMainLayout.animationView.setAnimation(AppUtil.getWeatherAnimation(currentWeather.getWeatherId()));
                                binding.contentMainLayout.animationView.playAnimation();
                            }}
                    }
                });
    }

    private void showStoredFiveDayWeather() {
        Query<FiveDayWeather> query = DbUtil.getFiveDayWeatherQuery(fiveDayWeatherBox);
        query.subscribe(subscriptions).on(AndroidScheduler.mainThread())
                .observer(new DataObserver<List<FiveDayWeather>>() {
                    @Override
                    public void onData(@NonNull List<FiveDayWeather> data) {
                        if (data.size() > 0) {
                            todayFiveDayWeather = data.remove(0);
                            mItemAdapter.clear();
                            mItemAdapter.add(data);
                        }
                    }
                });
    }

    private void checkLastUpdate() {
        cityInfo = prefser.get(Constants.CITY_INFO, CityInfo.class, null);
        if (cityInfo != null) {
            binding.toolbarLayout.cityNameTextView.setText(String.format("%s, %s", cityInfo.getName(), cityInfo.getCountry()));
            if (prefser.contains(Constants.LAST_STORED_CURRENT)) {
                long lastStored = prefser.get(Constants.LAST_STORED_CURRENT, Long.class, 0L);
                if (AppUtil.isTimePass(lastStored)) {
                    requestWeather(cityInfo.getName(), false);
                }
            } else {
                requestWeather(cityInfo.getName(), false);
            }
        } else {
            showEmptyLayout();
        }

    }


    private void requestWeather(String cityName, boolean isSearch) {
        if (AppUtil.isNetworkConnected()) {
            getCurrentWeather(cityName, isSearch);
            CITY = cityName;
            new weatherTask().execute();
            getFiveDaysWeather(cityName);
        } else {
            SnackbarUtil
                    .with(binding.swipeContainer)
                    .setMessage(getString(R.string.no_internet_message))
                    .setDuration(SnackbarUtil.LENGTH_LONG)
                    .showError();
            binding.swipeContainer.setRefreshing(false);
        }
    }

    private void getCurrentWeather(String cityName, boolean isSearch) {
        apiKey = getResources().getString(R.string.open_weather_map_api);
        disposable.add(
                apiService.getCurrentWeather(
                        cityName, Constants.UNITS, defaultLang, apiKey)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<CurrentWeatherResponse>() {
                            @Override
                            public void onSuccess(CurrentWeatherResponse currentWeatherResponse) {
                                isLoad = true;
                                storeCurrentWeather(currentWeatherResponse);
                                storeCityInfo(currentWeatherResponse);
                                binding.swipeContainer.setRefreshing(false);
                                if (isSearch) {
                                    prefser.remove(Constants.LAST_STORED_MULTIPLE_DAYS);
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                binding.swipeContainer.setRefreshing(false);
                                try {
                                    HttpException error = (HttpException) e;
                                    handleErrorCode(error);
                                } catch (Exception exception) {
                                    e.printStackTrace();
                                }
                            }
                        })

        );
    }

    private void handleErrorCode(HttpException error) {
        if (error.code() == 404) {
            SnackbarUtil
                    .with(binding.swipeContainer)
                    .setMessage(getString(R.string.no_city_found_message))
                    .setDuration(SnackbarUtil.LENGTH_INDEFINITE)
                    .setAction(getResources().getString(R.string.search_label), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            binding.toolbarLayout.searchView.showSearch();
                        }
                    })
                    .showWarning();

        } else if (error.code() == 401) {
            SnackbarUtil
                    .with(binding.swipeContainer)
                    .setMessage(getString(R.string.invalid_api_key_message))
                    .setDuration(SnackbarUtil.LENGTH_INDEFINITE)
                    .setAction(getString(R.string.ok_label), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    })
                    .showError();

        } else {
            SnackbarUtil
                    .with(binding.swipeContainer)
                    .setMessage(getString(R.string.network_exception_message))
                    .setDuration(SnackbarUtil.LENGTH_LONG)
                    .setAction(getResources().getString(R.string.retry_label), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (cityInfo != null) {
                                requestWeather(cityInfo.getName(), false);
                            } else {
                                binding.toolbarLayout.searchView.showSearch();
                            }
                        }
                    })
                    .showWarning();
        }
    }

    private void showEmptyLayout() {
        Glide.with(requireActivity()).load(R.drawable.no_city).into(binding.contentEmptyLayout.noCityImageView);
        binding.contentEmptyLayout.emptyLayout.setVisibility(View.VISIBLE);
        binding.contentMainLayout.nestedScrollView.setVisibility(View.GONE);
    }

    private void hideEmptyLayout() {
        binding.contentEmptyLayout.emptyLayout.setVisibility(View.GONE);
        binding.contentMainLayout.nestedScrollView.setVisibility(View.VISIBLE);
    }


    private void storeCurrentWeather(CurrentWeatherResponse response) {
        CurrentWeather currentWeather = new CurrentWeather();
        currentWeather.setTemp(response.getMain().getTemp());
        currentWeather.setHumidity(response.getMain().getHumidity());
        currentWeather.setDescription(response.getWeather().get(0).getDescription());
        currentWeather.setMain(response.getWeather().get(0).getMain());
        currentWeather.setWeatherId(response.getWeather().get(0).getId());
        currentWeather.setWindDeg(response.getWind().getDeg());
        currentWeather.setWindSpeed(response.getWind().getSpeed());
        currentWeather.setStoreTimestamp(System.currentTimeMillis());
        prefser.put(Constants.LAST_STORED_CURRENT, System.currentTimeMillis());
        if (!currentWeatherBox.isEmpty()) {
            currentWeatherBox.removeAll();
            currentWeatherBox.put(currentWeather);
        } else {
            currentWeatherBox.put(currentWeather);
        }
    }

    private void storeCityInfo(CurrentWeatherResponse response) {
        CityInfo cityInfo = new CityInfo();
        cityInfo.setCountry(response.getSys().getCountry());
        cityInfo.setId(response.getId());
        cityInfo.setName(response.getName());
        prefser.put(Constants.CITY_INFO, cityInfo);
        binding.toolbarLayout.cityNameTextView.setText(String.format("%s, %s", cityInfo.getName(), cityInfo.getCountry()));
    }

    private void getFiveDaysWeather(String cityName) {
        disposable.add(
                apiService.getMultipleDaysWeather(
                        cityName, Constants.UNITS, defaultLang, 5, apiKey)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<MultipleDaysWeatherResponse>() {
                            @Override
                            public void onSuccess(MultipleDaysWeatherResponse response) {
                                handleFiveDayResponse(response, cityName);
                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                            }
                        })
        );
    }

    private void handleFiveDayResponse(MultipleDaysWeatherResponse response, String cityName) {
        fiveDayWeathers = new ArrayList<>();
        List<ListItem> list = response.getList();
        int day = 0;
        for (ListItem item : list) {
            int color = colors[day];
            int colorAlpha = colorsAlpha[day];
            Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
            Calendar newCalendar = AppUtil.addDays(calendar, day);
            FiveDayWeather fiveDayWeather = new FiveDayWeather();
            fiveDayWeather.setWeatherId(item.getWeather().get(0).getId());
            fiveDayWeather.setDt(item.getDt());
            fiveDayWeather.setMaxTemp(item.getTemp().getMax());
            fiveDayWeather.setMinTemp(item.getTemp().getMin());
            fiveDayWeather.setTemp(item.getTemp().getDay());
            fiveDayWeather.setColor(color);
            fiveDayWeather.setColorAlpha(colorAlpha);
            fiveDayWeather.setTimestampStart(AppUtil.getStartOfDayTimestamp(newCalendar));
            fiveDayWeather.setTimestampEnd(AppUtil.getEndOfDayTimestamp(newCalendar));
            fiveDayWeathers.add(fiveDayWeather);
            day++;
        }
        getFiveDaysHourlyWeather(cityName);
    }

    private void getFiveDaysHourlyWeather(String cityName) {
        disposable.add(
                apiService.getFiveDaysWeather(
                        cityName, Constants.UNITS, defaultLang, apiKey)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<FiveDayResponse>() {
                            @Override
                            public void onSuccess(FiveDayResponse response) {
                                handleFiveDayHourlyResponse(response);
                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                            }
                        })

        );
    }

    private void handleFiveDayHourlyResponse(FiveDayResponse response) {
        if (!fiveDayWeatherBox.isEmpty()) {
            fiveDayWeatherBox.removeAll();
        }
        if (!itemHourlyDBBox.isEmpty()) {
            itemHourlyDBBox.removeAll();
        }
        for (FiveDayWeather fiveDayWeather : fiveDayWeathers) {
            long fiveDayWeatherId = fiveDayWeatherBox.put(fiveDayWeather);
            ArrayList<ItemHourly> listItemHourlies = new ArrayList<>(response.getList());
            for (ItemHourly itemHourly : listItemHourlies) {
                Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
                calendar.setTimeInMillis(itemHourly.getDt() * 1000L);
                if (calendar.getTimeInMillis()
                        <= fiveDayWeather.getTimestampEnd()
                        && calendar.getTimeInMillis()
                        > fiveDayWeather.getTimestampStart()) {
                    ItemHourlyDB itemHourlyDB = new ItemHourlyDB();
                    itemHourlyDB.setDt(itemHourly.getDt());
                    itemHourlyDB.setFiveDayWeatherId(fiveDayWeatherId);
                    itemHourlyDB.setTemp(itemHourly.getMain().getTemp());
                    itemHourlyDB.setWeatherCode(itemHourly.getWeather().get(0).getId());
                    itemHourlyDBBox.put(itemHourlyDB);
                }
            }
        }
    }


    public void showAboutFragment() {
        AppUtil.showFragment(new AboutFragment(), getActivity().getSupportFragmentManager(), true);
    }


    class weatherTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

//            /* Showing the ProgressBar, Making the main design GONE */
//            findViewById(R.id.loader).setVisibility(View.VISIBLE);
//            findViewById(R.id.mainContainer).setVisibility(View.GONE);
//            findViewById(R.id.errorText).setVisibility(View.GONE);
        }

        protected String doInBackground(String... args) {
            String response = HttpRequest.excuteGet("https://api.openweathermap.org/data/2.5/weather?q="
                    + CITY + "&units=metric&appid=" + API);
            return response;
        }

        @Override
        protected void onPostExecute(String result) {


            try {
                JSONObject jsonObj = new JSONObject(result);
                JSONObject main = jsonObj.getJSONObject("main");
                JSONObject sys = jsonObj.getJSONObject("sys");
                JSONObject wind = jsonObj.getJSONObject("wind");
                JSONObject weather = jsonObj.getJSONArray("weather").getJSONObject(0);

                Long updatedAt = jsonObj.getLong("dt");
                String updatedAtText = "Updated at: " + new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(new Date(updatedAt * 1000));
                String temp = main.getString("temp") + "??C";
                String tempMin = "Min Temp: " + main.getString("temp_min") + "??C";
                String tempMax = "Max Temp: " + main.getString("temp_max") + "??C";
                String pressure = main.getString("pressure");
                String humidity = main.getString("humidity");

                Long sunrise = sys.getLong("sunrise");
                Long sunset = sys.getLong("sunset");
                String windSpeed = wind.getString("speed");
                String weatherDescription = weather.getString("description");

                String address = jsonObj.getString("name") + ", " + sys.getString("country");


                /* Populating extracted data into our views */
if (context!=null) {
    binding.contentMainLayout.tempMin.setText(tempMin);
    binding.contentMainLayout.tempMax.setText(tempMax);
    binding.contentMainLayout.sunrise.setText(new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(sunrise * 1000)));
    binding.contentMainLayout.sunset.setText(new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(sunset * 1000)));
    binding.contentMainLayout.wind.setText(windSpeed);
    binding.contentMainLayout.pressure.setText(pressure);
    binding.contentMainLayout.humidity.setText(humidity);
}
                /* Views populated, Hiding the loader, Showing the main design */
//                findViewById(R.id.loader).setVisibility(View.GONE);
//                findViewById(R.id.mainContainer).setVisibility(View.VISIBLE);


            } catch (JSONException e) {
//                findViewById(R.id.loader).setVisibility(View.GONE);
//                findViewById(R.id.errorText).setVisibility(View.VISIBLE);
            }

        }
    }


}



