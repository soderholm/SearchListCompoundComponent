package com.example.matilda.searchlist;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchComponent extends LinearLayout {
    List<String> wordList = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;

    public SearchComponent(Context context) {
        super(context);
    }

    public SearchComponent(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SearchComponent(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void initiate(){
        setOrientation(VERTICAL);
        inflate(getContext(),R.layout.search_component,this);
        EditText searchInput = (EditText) findViewById(R.id.search_input);
        ListView resultList = (ListView) findViewById(R.id.result_list);

        arrayAdapter = new ArrayAdapter<>(getContext(), R.layout.list_text_view, R.id.text_in_list);
        resultList.setAdapter(arrayAdapter);
        setWatcher(searchInput);
    }

    public void getDataFromUrl(String url){
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(createWordListRequest(url));
    }

    private void setWatcher(EditText searchInput){
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                updateAdapter(s.toString());
            }
        });
    }

    private StringRequest createWordListRequest(String url){
        return new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        wordList.addAll(Arrays.asList(response.split(("\n"))));
                        System.out.println("REQUEST DONE");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast requestErrorToast = Toast.makeText(getContext(), "Request to database failed", Toast.LENGTH_LONG);
                requestErrorToast.show();
            }
        });
    }

    private static Boolean checkForMatch(String word, String searchQuery){
        return word.contains(searchQuery);
    }

    private void updateAdapter(String searchQuery){
        arrayAdapter.clear();

        if (searchQuery.length() > 0) {
            Boolean matchesFound = false;
            for (String word : wordList) {
                if (checkForMatch(word, searchQuery)) {
                    arrayAdapter.add(word);
                    matchesFound = true;
                }
            }
            if (!matchesFound){
                Toast noResultsToast = Toast.makeText(getContext(), "No results found for that query.", Toast.LENGTH_LONG);
                noResultsToast.show();
            }
        }
    }
}
