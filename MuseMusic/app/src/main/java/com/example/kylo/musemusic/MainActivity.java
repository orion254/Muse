/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.kylo.musemusic;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toolbar;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>{

    private EditText mSearchBoxEditText;
    private ProgressBar mProgressBar;
    private static final String TAG = "MainActivity";
    private static final int LOADER_ID = 1;
    private static final String SEARCH_QUERY_URL_EXTRA = "searchQuery";
    private static final String SEARCH_QUERY_RESULTS = "searchResults";
    private String musicQueryResults;
    private RecyclerView mRecyclerView;
    private MusicAdapter mAdapter;
    private ArrayList<Track> tracks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSearchBoxEditText = (EditText) findViewById(R.id.et_search_box);
        mProgressBar = (ProgressBar) findViewById(R.id.progress);
        mRecyclerView = (RecyclerView)findViewById(R.id.my_recycler_view);
        mAdapter = new MusicAdapter(this, tracks);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        Typeface tf = Typeface.createFromAsset(getAssets(),
//                "font/dismis.TTF");
//
//        int titleId = getResources().getIdentifier("action_bar_title", "id",
//                "android");
//        TextView yourTextView = (TextView) findViewById(titleId);
//        yourTextView.setTypeface(tf);

        if(savedInstanceState != null && savedInstanceState.containsKey(SEARCH_QUERY_RESULTS)){
            tracks.clear();
            String searchResults = savedInstanceState.getString(SEARCH_QUERY_RESULTS);
            populateRecyclerView(searchResults);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SEARCH_QUERY_RESULTS, musicQueryResults);

    }

    private URL makeSearchUrl() {
        String napsterQuery = mSearchBoxEditText.getText().toString();
        URL napsterSearchUrl = NetworkUtils.buildUrl(napsterQuery);
        return napsterSearchUrl;

    }

    @Override
    protected void onResume() {
        super.onResume();
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.action_search) {
            URL url = makeSearchUrl();
            Bundle bundle = new Bundle();
            bundle.putString(SEARCH_QUERY_URL_EXTRA, url.toString());
            LoaderManager loaderManager = getSupportLoaderManager();
            Loader<String> searchLoader = loaderManager.getLoader(LOADER_ID);
            if(searchLoader == null){
                loaderManager.initLoader(LOADER_ID, bundle, this).forceLoad();
            }else{
                loaderManager.restartLoader(LOADER_ID, bundle, this).forceLoad();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void populateRecyclerView(String searchResults){
        Log.d("mycode", searchResults);
        tracks = JsonUtils.parseNews(searchResults);
        mAdapter.mTrack.clear();
        mAdapter.mTrack.addAll(tracks);
        mAdapter.notifyDataSetChanged();
    }



    @Override
    public android.support.v4.content.Loader<String> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<String>(this) {
            @Override
            protected void onStartLoading() {
                Log.d(TAG, "onStartLoading called");
                super.onStartLoading();
                if(args == null){
                    Log.d(TAG, "bundle null");
                    return;
                }
                mProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public String loadInBackground() {
                Log.d(TAG, "loadInBackground called");

                String napsterSearchQuery = args.getString(SEARCH_QUERY_URL_EXTRA);
                if(napsterSearchQuery == null || napsterSearchQuery.isEmpty()){
                    return null;
                }
                try {
                    Log.d(TAG, "begin network call");
                    musicQueryResults = NetworkUtils.getResponseFromHttpUrl(new URL(napsterSearchQuery));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, musicQueryResults);
                return musicQueryResults;
            }
        };
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<String> loader, String data) {
        Log.d("mycode", data);
        mProgressBar.setVisibility(View.GONE);
        populateRecyclerView(data);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<String> loader) {}
}
