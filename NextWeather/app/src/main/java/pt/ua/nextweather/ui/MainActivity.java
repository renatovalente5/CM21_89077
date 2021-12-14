package pt.ua.nextweather.ui;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pt.ua.nextweather.R;
import pt.ua.nextweather.datamodel.City;
import pt.ua.nextweather.datamodel.Weather;
import pt.ua.nextweather.datamodel.WeatherType;
import pt.ua.nextweather.network.CityResultsObserver;
import pt.ua.nextweather.network.ForecastForACityResultsObserver;
import pt.ua.nextweather.network.IpmaWeatherClient;
import pt.ua.nextweather.network.WeatherTypesResultsObserver;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private TextView feedback;
    public ArrayList<String> dados;
    public String s = "";
    ListView listView;
    ArrayAdapter adapter;

    IpmaWeatherClient client = new IpmaWeatherClient();
    private HashMap<String, City> cities;
    private HashMap<Integer, WeatherType> weatherDescriptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dados = new ArrayList<>();
        listView = (ListView) findViewById(R.id.listView);
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, dados);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                callWeatherForecastForACityStep1("Aveiro");
//            }
//        });
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.citys_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        feedback = null;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //dados = new ArrayList<>();
        //dados.clear();
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.aveiro) {
            callWeatherForecastForACityStep1("Aveiro");
            return true;
        }
        if (id == R.id.porto) {
            callWeatherForecastForACityStep1("Porto");
            return true;
        }
        if (id == R.id.lisboa) {
            callWeatherForecastForACityStep1("Lisboa");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void callWeatherForecastForACityStep1(String city) {

        //feedback.append("\nGetting forecast for: " + city); feedback.append("\n");

        // call the remote api, passing an (anonymous) listener to get back the results
        client.retrieveWeatherConditionsDescriptions(new WeatherTypesResultsObserver() {
            @Override
            public void receiveWeatherTypesList(HashMap<Integer, WeatherType> descriptorsCollection) {
                MainActivity.this.weatherDescriptions = descriptorsCollection;
                callWeatherForecastForACityStep2( city);
            }
            @Override
            public void onFailure(Throwable cause) {
                feedback.append("Failed to get weather conditions!");
            }
        });

    }

    private void callWeatherForecastForACityStep2(String city) {
        client.retrieveCitiesList(new CityResultsObserver() {

            @Override
            public void receiveCitiesList(HashMap<String, City> citiesCollection) {
                MainActivity.this.cities = citiesCollection;
                City cityFound = cities.get(city);
                if( null != cityFound) {
                    //dados.add(city);
                    int locationId = cityFound.getGlobalIdLocal();
                    callWeatherForecastForACityStep3(locationId);
                } else {
                    feedback.append("unknown city: " + city);
                }
            }

            @Override
            public void onFailure(Throwable cause) {
                feedback.append("Failed to get cities list!");
            }
        });
    }

    private void callWeatherForecastForACityStep3(int localId) {
        //List dados = new List()<String>;
//        ArrayList<String> dados = new ArrayList<>();
        //dados = new ArrayList<>();
        client.retrieveForecastForCity(localId, new ForecastForACityResultsObserver() {
            @Override
            public void receiveForecastList(List<Weather> forecast) {
                dados.clear();
                for (Weather day : forecast) {

                    s += ("\n");
                    s += "Day: " + day.getForecastDate();
                    s += ("\n");
                    s += "Min: " + day.getTMin();
                    s += ("\n");
                    s += "Max: " + day.getTMax();
                    s += ("\n");
                    dados.add(s);
                    s = "";
                }
            }
            @Override
            public void onFailure(Throwable cause) {
                feedback.append( "Failed to get forecast for 5 days");
            }
        });


        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, dados);
        listView.setAdapter(adapter);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i) {
                case 0:
                    // Whatever you want to happen when the first item gets selected
                    callWeatherForecastForACityStep1("Aveiro");
                    break;
                case 1:
                    // Whatever you want to happen when the second item gets selected
                    callWeatherForecastForACityStep1("Porto");
                    break;
                case 2:
                    // Whatever you want to happen when the thrid item gets selected
                    callWeatherForecastForACityStep1("Lisboa");
                    break;
            }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}

