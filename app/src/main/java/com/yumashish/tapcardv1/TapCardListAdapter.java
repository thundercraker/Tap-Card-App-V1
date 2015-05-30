package com.yumashish.tapcardv1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by lightning on 5/31/15.
 */
public class TapCardListAdapter extends BaseExpandableListAdapter {
    /* The Tap Card Expandable List will be divided into three super groups as of now
     * arbitrary groups 1) My Card 1.5) Search Results (Mock)  2) TapCard Community
     */
    private String[] GROUP_TITLES = {"My Card", "Search Results", "TapCard Community"};
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private ArrayList<TapCard>[] mTapCards;

    public TapCardListAdapter(Context context, ArrayList<TapCard>[] tapCards)
    {
        mContext = context;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mTapCards = tapCards;
    }

    @Override
    public int getGroupCount() {
        return 3;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if(groupPosition == 1) return 1;
        return mTapCards[groupPosition].size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mTapCards[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mTapCards[groupPosition].get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return (groupPosition * mContext.getResources().getInteger(R.integer.expandable_spacer)) + childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final ArrayList<TapCard> CardGroup = mTapCards[groupPosition];

        convertView = mLayoutInflater.inflate(R.layout.list_group, parent, false);

        //Set Title
        ((TextView) convertView.findViewById(R.id.lg_text)).setText(GROUP_TITLES[groupPosition] + " (" + CardGroup.size() + ")");

        ExpandableListView elv = (ExpandableListView) parent;
        elv.expandGroup(groupPosition);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ArrayList<TapCard> CardGroup = mTapCards[groupPosition];

        if(groupPosition == 0 || groupPosition == 2) {
            final TapCard Card = CardGroup.get(childPosition);
            convertView = createSingleProfileRow(parent, Card);
        } else if (groupPosition == 1) {
            convertView = createMultiProfileRow(parent, CardGroup);
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    //Inflate the list_child which are the
    //child views found under My Card and Community groups
    public LinearLayout createSingleProfileRow(ViewGroup parent, TapCard card)
    {
        LinearLayout singleProfileView = (LinearLayout) mLayoutInflater.inflate(R.layout.list_child, parent, false);
        ImageView profilePic = (ImageView) singleProfileView.findViewById(R.id.lc_profile_pic);
        TextView mainText = (TextView) singleProfileView.findViewById(R.id.lc_name);
        //TextView subText = (TextView) singleProfileView.findViewById(R.id.lc_subtext);
        //TextView subText1 = (TextView) singleProfileView.findViewById(R.id.lc_subtext_1);

        profilePic.setImageDrawable(card.getProfilePic());
        mainText.setText(card.getName());
        //subText.setText(card.getLocation());
        //subText1.setText(card.getWork());

        return singleProfileView;
    }

    //Inflates the child view found in the Search result group
    public LinearLayout createMultiProfileRow(ViewGroup parent, ArrayList<TapCard> cards)
    {
        LinearLayout multiProfileView = (LinearLayout) mLayoutInflater.inflate(R.layout.list_child_many_profiles, parent, false);
        int[] profile_view_ids = { R.id.lcmp_img_0,R.id.lcmp_img_1,R.id.lcmp_img_2,R.id.lcmp_img_3,R.id.lcmp_img_4 };
        for (int i = 0; i < 4; i++)
        {
            ImageView imgv = (ImageView) multiProfileView.findViewById(profile_view_ids[i]);
            imgv.setImageDrawable(cards.get(i).getProfilePic());
        }

        if(cards.size() > 4)
        {
            ImageView imgv = (ImageView) multiProfileView.findViewById(profile_view_ids[4]);
            imgv.setImageDrawable(mContext.getResources().getDrawable(android.R.drawable.ic_input_add));
        }

        return multiProfileView;
    }
}
