package com.example.matilda.searchlist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SearchComponent searchComponent = (SearchComponent) findViewById(R.id.search_component);
        searchComponent.getDataFromUrl("http://runeberg.org/words/ss100.txt");
        searchComponent.initiate();
    }
}
