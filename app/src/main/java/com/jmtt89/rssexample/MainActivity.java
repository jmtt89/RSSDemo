package com.jmtt89.rssexample;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.prof.rssparser.Article;
import com.prof.rssparser.Parser;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class RssFeedFragment extends Fragment {
        private static final String TAG = "RssFeedFragment";
        private static final String ARG_SECTION_FEED = "section_feed";
        private RssFeed rssFeed;
        private int mColumnCount = 1; //Fijo por ahora (posiblemente luego se haga dinamico)
        private List<Article> items;
        private RssFeedRecyclerViewAdapter adapter;

        public RssFeedFragment() {
        }

        public static RssFeedFragment newInstance(RssFeed feed) {
            RssFeedFragment fragment = new RssFeedFragment();
            Bundle args = new Bundle();
            args.putParcelable(ARG_SECTION_FEED, feed);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            items = new ArrayList<>();
            if (getArguments() != null) {
                rssFeed = getArguments().getParcelable(ARG_SECTION_FEED);
            }


            Parser parser = new Parser();
            parser.execute(rssFeed.getUrl());
            parser.onFinish(new Parser.OnTaskCompleted() {

                @Override
                public void onTaskCompleted(ArrayList<Article> list) {
                    items.clear();
                    items.addAll(list);
                    if(adapter != null) {
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onError() {
                    Log.e(TAG,"Error Loading RSS");
                }
            });
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_main, container, false);

            // Set the adapter
            if (view instanceof RecyclerView) {
                Context context = view.getContext();
                RecyclerView recyclerView = (RecyclerView) view;
                if (mColumnCount <= 1) {
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                } else {
                    recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
                }
                adapter = new RssFeedRecyclerViewAdapter(context,items);
                recyclerView.setAdapter(adapter);
            }

            return view;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private List<RssFeed> feeds;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);

            //Load RSSFeed Subscribed
            feeds = new ArrayList<>();
            feeds.add(new RssFeed("http://www.androidcentral.com/feed","Entertaiment"));
            feeds.add(new RssFeed("http://feeds.reuters.com/news/reutersmedia?format=xml","Media"));
            feeds.add(new RssFeed("http://feeds.reuters.com/reuters/sportsNews?format=xml","Sports"));

        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return RssFeedFragment.newInstance(feeds.get(position));
        }

        @Override
        public int getCount() {
            return feeds.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            RssFeed feed = feeds.get(position);
            return feed.getName();
        }
    }
}