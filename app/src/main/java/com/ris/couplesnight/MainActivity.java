package com.ris.couplesnight;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ris.couplesnight.classes.ApiCall;
import com.ris.couplesnight.classes.Id;
import com.ris.couplesnight.classes.Movie;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TextView first, second, result, yourCommonMovie, pickAMovie, startText;
    private ProgressBar progressBar;
    private Button nextPlayer, startButton;
    private boolean isSecond;
    private final String apiCall = "https://imdb-api.com/en/API/Top250Movies/k_ydo7ri1i";
    private List<Id> ids;
    private int counter;
    private List<Movie> movies;
    private List<Movie> firstPicks, secondPicks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progressBar);
        first = findViewById(R.id.first);
        second = findViewById(R.id.second);
        result = findViewById(R.id.result);
        nextPlayer = findViewById(R.id.nextPlayer);
        yourCommonMovie = findViewById(R.id.yourCommonMovie);
        pickAMovie = findViewById(R.id.pickAMovie);
        startText = findViewById(R.id.startText);
        startButton = findViewById(R.id.startButton);


        first.setVisibility(View.INVISIBLE);
        second.setVisibility(View.INVISIBLE);
        pickAMovie.setVisibility(View.INVISIBLE);
        result.setVisibility(View.INVISIBLE);
        nextPlayer.setVisibility(View.INVISIBLE);
        yourCommonMovie.setVisibility(View.INVISIBLE);
        startButton.setVisibility(View.INVISIBLE);
        startText.setVisibility(View.INVISIBLE);
        firstPicks = new ArrayList<>();
        secondPicks = new ArrayList<>();
        isSecond = false;
        counter = 0;

        getData();
    }

    public void firstCB(View view) {
        if (!isSecond) {
            firstPicks.add(movies.get(counter - 1));
        } else {
            secondPicks.add(movies.get(counter - 1));
        }
        counter++;
        switchMovies();
    }

    public void secondCB(View view) {
        if (!isSecond) {
            firstPicks.add(movies.get(counter));
        } else {
            secondPicks.add(movies.get(counter));
        }
        counter++;
        switchMovies();
    }

    public void nextCB(View view) {
        counter = 0;
        isSecond = true;
        switchMovies();
        first.setVisibility(View.VISIBLE);
        second.setVisibility(View.VISIBLE);
        pickAMovie.setVisibility(View.VISIBLE);
        nextPlayer.setVisibility(View.INVISIBLE);
    }

    public void startCB(View view) {
        startButton.setVisibility(View.INVISIBLE);
        startText.setVisibility(View.INVISIBLE);

        switchMovies();

        first.setVisibility(View.VISIBLE);
        second.setVisibility(View.VISIBLE);
        pickAMovie.setVisibility(View.VISIBLE);
    }

    private void switchMovies() {
        if (counter >= 20) {
            first.setVisibility(View.INVISIBLE);
            second.setVisibility(View.INVISIBLE);
            pickAMovie.setVisibility(View.INVISIBLE);
            if(isSecond) {
                firstPicks.retainAll(secondPicks);
                yourCommonMovie.setVisibility(View.VISIBLE);
                result.setVisibility(View.VISIBLE);
                if(firstPicks.size() > 0) {
                    result.setText(firstPicks.get(new Random().nextInt((firstPicks.size() + 1))).toString());
                } else {
                    result.setText("You have no common movies!");
                }
            } else {
                nextPlayer.setVisibility(View.VISIBLE);
            }
        } else {
            String firstText = movies.get(counter).toString();
            counter++;
            String secondText = movies.get(counter).toString();
            first.setText(firstText);
            second.setText(secondText);
        }
    }

    private void getData() {
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, apiCall, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response)  {
                        Type type = new TypeToken<List<Id>>() {}.getType();
                        try {
                            ids = new Gson().fromJson(response.getJSONArray("items").toString(), type);
                            if(ids.size() > 20) {
                                ids = ids.subList(0, 20);
                                getMovies();
                            } else if (ids == null || (ids != null && ids.size() == 0)) {
                                ids = ApiCall.getLocalIds();
                                movies = ApiCall.getLocalMovies();

                                progressBar.setVisibility(View.INVISIBLE);
                                startButton.setVisibility(View.VISIBLE);
                                startText.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ids = new ArrayList<>();
                    }
                });

        queue.add(jsonObjectRequest);
    }

    private void getMovies() {
        if (this.ids.size() > 0) {
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = "https://imdb-api.com/en/API/Title/k_ydo7ri1i/";
            movies = new ArrayList<>();
            for (Id id : ids) {
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.GET, url + id.getId(), null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response)  {
                                movies.add(new Gson().fromJson(response.toString(), Movie.class));
                                if (movies.size() >= 20) {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    startButton.setVisibility(View.VISIBLE);
                                    startText.setVisibility(View.VISIBLE);

                                    switchMovies();
                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                movies.add(null);
                            }
                        });

                queue.add(jsonObjectRequest);
            }
        }
    }
}