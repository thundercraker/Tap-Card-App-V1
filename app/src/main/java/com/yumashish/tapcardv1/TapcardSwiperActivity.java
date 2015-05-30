package com.yumashish.tapcardv1;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class TapcardSwiperActivity extends Activity implements View.OnTouchListener {
    TapcardSwiperActivity mContext;
    ActionBar mActionBar;
    LayoutInflater mLayoutInflater;
    private VelocityTracker mVelocityTracker;

    //View members
    RelativeLayout vMain;
    View vDragView;

    //basic members
    static final String DEBUG_TAG = "TapcardSwiperActivity";
    static final int SWIPE_OFF_EDGE_OFFSET = 100;
    static final int MOVE_DISTANCE = 50;

    private int mXD, mYD, mCenterMarginX, mCenterMarginY;
    private float mCenterX, mCenterY;
    private int[] originalPos = new int[2];
    private boolean mFinishedGeneration = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tapcard_swiper);

        mContext = this;

        mLayoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mActionBar = getActionBar();
        mActionBar.setTitle("Yumashish.com");
        mActionBar.setSubtitle("Tapcard Swipe");
        mActionBar.setDisplayHomeAsUpEnabled(true);

        vMain = (RelativeLayout) findViewById(R.id.swipper_main);

        Intent intent = getIntent();
        String intentValue = intent.getStringExtra("card-1");
        Log.d(DEBUG_TAG, intentValue);

        vMain.removeAllViews();


        vMain.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Log.d(DEBUG_TAG, "GTO H/W " + vMain.getHeight() + " " + vMain.getWidth());

                List<TapCard> cards = new ArrayList<>();
                cards.add(new TapCard(0, mContext.getResources().getDrawable(R.drawable.david), "David Jaques", "Assorted Work Data", "Assorted Location Data"));
                cards.add(new TapCard(1, mContext.getResources().getDrawable(R.drawable.lisa), "Lisa Alson", "Assorted Work Data", "Assorted Location Data"));
                cards.add(new TapCard(2, mContext.getResources().getDrawable(R.drawable.micheal), "Micheal Knox", "Assorted Work Data", "Assorted Location Data"));
                cards.add(new TapCard(3, mContext.getResources().getDrawable(R.drawable.mary), "Mary Hagle", "Assorted Work Data", "Assorted Location Data"));
                cards.add(new TapCard(4, mContext.getResources().getDrawable(R.drawable.oldman), "Oldman Jones", "Assorted Work Data", "Assorted Location Data"));

                createChildren(vMain, cards);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    vMain.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    vMain.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });
    }

    public void createChildren(ViewGroup parent, List<TapCard> cardDataList)
    {
        int pHeight = parent.getHeight();
        int pWidth = parent.getWidth();

        //scale the card size to 80% of parent height and width
        int height = (int) (pHeight * 0.8f);
        int width = (int) (pWidth * 0.8f);

        mCenterMarginY = (int)((0.5f * pHeight) - (0.5f * height));
        mCenterMarginX = (int)((0.5f * pWidth) - (0.5f * width));

        TapCard top = cardDataList.get(cardDataList.size() - 1);
        cardDataList.remove(cardDataList.size() - 1);

        for(TapCard cardData : cardDataList) {
            createTapCard(parent, height, width, cardData, true);
        }
        createTapCard(parent, height, width, top, false);
        mFinishedGeneration = true;
        //animations to make the cards appear
        appearR(0);
        //set the top card to be draggable;
        setDraggableCard();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        } else if(id==android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }

        return super.onOptionsItemSelected(item);
    }

    public int pX, pY, oX, oY, oMarginX, oMarginY;
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.d(DEBUG_TAG, "OnTouch");
        int index = event.getActionIndex();
        int action = event.getActionMasked();
        int pointerId = event.getPointerId(index);

        final int x = (int) event.getX();
        final int y = (int) event.getY();

        String tag = (v.getTag()!=null) ? v.getTag().toString() : "";

        if(vDragView == null) return true;

        switch(action) {
            case MotionEvent.ACTION_DOWN:
                if (mVelocityTracker == null)
                    mVelocityTracker = VelocityTracker.obtain();
                else
                    mVelocityTracker.clear();

                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) v.getLayoutParams();
                v.setLayoutParams(layoutParams);
                mXD = x - layoutParams.leftMargin;
                mYD = y - layoutParams.topMargin;
                oMarginX = layoutParams.leftMargin;
                oMarginY = layoutParams.topMargin;
                oX = x;
                oY = y;
                mVelocityTracker.addMovement(event);
                Log.d(DEBUG_TAG, x + "," + y + " Action Down Tag: " + tag);
                break;
            case MotionEvent.ACTION_MOVE:
                //Velocity and Distance are not used at present, it will be in future implementation
                mVelocityTracker.addMovement(event);
                mVelocityTracker.computeCurrentVelocity(1000);
                //Find Velocity
                double velocityX = mVelocityTracker.getXVelocity();
                double velocityY = mVelocityTracker.getYVelocity();

                //if the current X,Y is MOVE_DISTANCE or greater, start a translation animation
                double distance = Math.sqrt(Math.pow(x - oX, 2) + Math.pow(y - oY, 2));
                Log.d(DEBUG_TAG, "Distance: " + distance);
                Log.d(DEBUG_TAG, x + "," + y + " Action Move Tag: " + tag + " Velocity " + velocityX + " " + velocityY);
                break;
            case MotionEvent.ACTION_UP:
                v.animate()
                        .translationXBy(event.getX() - oX)
                        .translationYBy(event.getY() - oY)
                        .setInterpolator(new DecelerateInterpolator())
                        .setDuration(100);
                //Detect if the object is in deletion Zone
                if (event.getRawX() < SWIPE_OFF_EDGE_OFFSET) {
                    vMain.removeView(v);
                    setDraggableCard();
                    Toast.makeText(mContext, "Rejected", Toast.LENGTH_LONG).show();
                }
                else if(event.getRawX() > vMain.getWidth() - SWIPE_OFF_EDGE_OFFSET)
                {
                    vMain.removeView(v);
                    setDraggableCard();
                    Toast.makeText(mContext, "Accepted", Toast.LENGTH_LONG).show();
                }

                Log.d(DEBUG_TAG, event.getRawX() + "," + event.getRawY() + " Action up Tag: " + tag);
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                Log.d(DEBUG_TAG, "Action Pointer Down");
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
        }
        pX = x;
        pY = y;
        vMain.invalidate();

        return true;
    }

    //helper method
    //makes a vew appear
    public void appearAnimation(View v, AnimatorListenerAdapter ala)
    {
        v.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(600)
                .setInterpolator(new AccelerateInterpolator())
                .setListener(ala);
    }

    //Make the invisible tap card views appear one by one recurrsively
    public void appearR(int index)
    {
        if(index >= vMain.getChildCount())
            return;
        final int mIndex = index + 1;
        vMain.getChildAt(index).setAlpha(0f);
        vMain.getChildAt(index).setVisibility(View.VISIBLE);
        appearAnimation(vMain.getChildAt(index),
                new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        appearR(mIndex);
                    }
                });
    }

    int lfirstFrameTopMargin, lfirstFrameLeftMargin;
    public void setDraggableCard()
    {
        if(vMain.getChildCount() > 0 && mFinishedGeneration) {

            vDragView = vMain.getChildAt(vMain.getChildCount() - 1);
            final RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) vDragView.getLayoutParams();
            lfirstFrameLeftMargin = params.leftMargin;
            Log.d(DEBUG_TAG, lfirstFrameLeftMargin + " original animation margin");
            lfirstFrameTopMargin = params.topMargin;
            Animation animation = new Animation() {
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    params.topMargin = (int)((mCenterMarginY - lfirstFrameTopMargin) * interpolatedTime) + lfirstFrameTopMargin;
                    //Log.d(DEBUG_TAG, ((int)(mCenterMarginY + mCenterMarginY - lfirstFrameTopMargin) * interpolatedTime) + " animation margin X");
                    params.leftMargin = (int)((mCenterMarginX - lfirstFrameLeftMargin) * interpolatedTime) + lfirstFrameLeftMargin;
                    vDragView.setRotation(vDragView.getRotation() * interpolatedTime);
                    vDragView.setLayoutParams(params);
                }
            };
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if(vDragView != null)
                        vDragView.setOnTouchListener(mContext);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            animation.setDuration(300);
            vDragView.startAnimation(animation);

        } else {
            vDragView = null;
        }
    }

    //Get a tapcard LinearLayout
    //refer to the tap_card layout
    private LinearLayout createTapCard(ViewGroup parent, int height, int width, TapCard tapCard, boolean randomize)
    {
        final LinearLayout card = (LinearLayout) mLayoutInflater.inflate(R.layout.tap_card, null);

        //get a rando, degree for tilt
        int tilt = (int) ((Math.random() * 15d) - 7.5d);
        Log.d(DEBUG_TAG, "Tilt " + tilt);

        //get random offsets for X and Y
        int rX = (int) ((Math.random() * 20d) - 10d);
        int rY = (int) ((Math.random() * 20d) - 10d);

        card.setTag(R.id.tap_card_tag, tapCard);
        card.setScaleX(1.1f);
        card.setScaleY(1.1f);
        card.setVisibility(View.GONE);
        if(randomize) card.setRotation(tilt);
        parent.addView(card);

        RelativeLayout.LayoutParams lparams = (RelativeLayout.LayoutParams) card.getLayoutParams();
        lparams.width = width;
        lparams.height = height;

        //set to the middle by calculating corrent margins
        //margin top: 50% of parent height - 50% of child height

        lparams.topMargin = mCenterMarginY;
        lparams.leftMargin = mCenterMarginX;

        Log.d(DEBUG_TAG, "Margin T/L" + mCenterMarginX + " " + mCenterMarginY);

        ImageView profile = (ImageView) card.findViewById(R.id.imageView);
        TextView name = (TextView) card.findViewById(R.id.tap_card_name);
        TextView data1 = (TextView) card.findViewById(R.id.tap_card_data1);
        TextView data2 = (TextView) card.findViewById(R.id.tap_card_data2);

        profile.setImageDrawable(tapCard.getProfilePic());
        name.setText(tapCard.getName());
        data1.setText(tapCard.getWork());
        data2.setText(tapCard.getLocation());

        card.setLayoutParams(lparams);

        return card;
    }
}
