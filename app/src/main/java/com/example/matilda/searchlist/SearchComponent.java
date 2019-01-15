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

    /**
     * Public method to inflate initiate component, to be called outside of class after it has been created.
     * Array adapter is set to list and watcher is set on search field.
     */
    public void initiate(){
        setOrientation(VERTICAL);
        inflate(getContext(),R.layout.search_component,this);
        EditText searchInput = (EditText) findViewById(R.id.search_input);
        ListView resultList = (ListView) findViewById(R.id.result_list);

        arrayAdapter = new ArrayAdapter<>(getContext(), R.layout.list_text_view, R.id.text_in_list);
        resultList.setAdapter(arrayAdapter);
        setWatcher(searchInput);
    }

    /**
     * Public method to get data from chosen Url, to be called outside of class after it has been created.
     * Volley request queue is created, and then a stringRequest is added to it.
     * @param url String url to fetch word document from.
     */
    public void getDataFromUrl(String url){
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(createWordListRequest(url));
    }

    /**
     * Set watcher for search query input to update array adapter after text has been changed.
     * @param searchInput EditText for input
     */
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

    /**
     * Function to create a Volley Request and split text document with words into list of strings.
     * The text document is split by new line, and each word inserted into the classes wordlist.
     * Toast is shown if request fails.
     * @param url String url to fetch word document from.
     * @return StringRequest (Volley) ready to be added to the queue.
     */
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

    /**
     * Function to check if a word contains the search query.
     * @param word String to be checked for match
     * @param searchQuery String to be matched against
     * @return Boolean True if match, otherwise False.
     */
    private static Boolean checkForMatch(String word, String searchQuery){
        return word.contains(searchQuery);
    }

    /**
     * Function to update the array adapter data source, based on the inserted search query.
     * The word list is iterated and each word is checked.
     * If search query is an empty string, array adapter is just cleared.
     * If no matches found, a toast to inform the user is shown.
     * @param searchQuery String search query to update array adapter against.
     */
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
