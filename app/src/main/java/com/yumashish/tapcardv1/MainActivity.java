package com.yumashish.tapcardv1;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {
    ActionBar mActionBar;
    ExpandableListView mListView;
    Button mTestButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mActionBar = getActionBar();
        mActionBar.setTitle("Yumashish.com");
        mActionBar.setSubtitle("Tapcard List");
        //mActionBar.show();

        ArrayList<TapCard>[] allCards = new ArrayList[3];

        ArrayList<TapCard> my_card = new ArrayList<>();
        my_card.add(new TapCard(0, this.getResources().getDrawable(R.drawable.david), "David Jaques", "Assorted Work Data", "Assorted Location Data"));

        ArrayList<TapCard> search_cards = new ArrayList<>();
        search_cards.add(new TapCard(0, this.getResources().getDrawable(R.drawable.david), "David Jaques", "Assorted Work Data", "Assorted Location Data"));
        search_cards.add(new TapCard(1, this.getResources().getDrawable(R.drawable.lisa), "Lisa Alson", "Assorted Work Data", "Assorted Location Data"));
        search_cards.add(new TapCard(2, this.getResources().getDrawable(R.drawable.micheal), "Micheal Knox", "Assorted Work Data", "Assorted Location Data"));
        search_cards.add(new TapCard(3, this.getResources().getDrawable(R.drawable.mary), "Mary Hagle", "Assorted Work Data", "Assorted Location Data"));
        search_cards.add(new TapCard(4, this.getResources().getDrawable(R.drawable.oldman), "Oldman Jones", "Assorted Work Data", "Assorted Location Data"));

        ArrayList<TapCard> com_cards = new ArrayList<>();
        com_cards.add(new TapCard(0, this.getResources().getDrawable(R.drawable.david), "David Jaques", "Assorted Work Data", "Assorted Location Data"));
        com_cards.add(new TapCard(1, this.getResources().getDrawable(R.drawable.lisa), "Lisa Alson", "Assorted Work Data", "Assorted Location Data"));
        com_cards.add(new TapCard(2, this.getResources().getDrawable(R.drawable.micheal), "Micheal Knox", "Assorted Work Data", "Assorted Location Data"));
        com_cards.add(new TapCard(3, this.getResources().getDrawable(R.drawable.mary), "Mary Hagle", "Assorted Work Data", "Assorted Location Data"));
        com_cards.add(new TapCard(4, this.getResources().getDrawable(R.drawable.oldman), "Oldman Jones", "Assorted Work Data", "Assorted Location Data"));

        allCards[0] = my_card;
        allCards[1] = search_cards;
        allCards[2] = com_cards;

        TapCardListAdapter adapter = new TapCardListAdapter(this, allCards);

        mListView = (ExpandableListView) findViewById(R.id.listView);
        mListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                if(groupPosition == 1)
                {
                    SwitchToSwipper();
                }
                return true;
            }
        });
        mListView.setAdapter(adapter);
    }

    public void SwitchToSwipper()
    {
        Intent intent = new Intent(MainActivity.this, TapcardSwiperActivity.class);
        intent.putExtra("card-1", "Name");
        MainActivity.this.startActivity(intent);
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
}
