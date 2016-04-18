package com.samsung.rd.dogex.adapters;


import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.base.Preconditions;
import com.samsung.rd.dogex.R;
import com.samsung.rd.dogex.fragments.DogeDescriptionDialogFragment;
import com.samsung.rd.dogex.model.DogeDataProvider;
import com.samsung.rd.dogex.model.domain.Doge;
import com.samsung.rd.dogex.model.domain.DogeGroup;

import java.util.ArrayList;
import java.util.List;

public class ExpandableAdapter extends BaseExpandableListAdapter {
    private final ExpandableListView dogeListView;
    private final LayoutInflater inflater;
    private final DogeDataProvider dataProvider;
    private final Activity activity;


    private static final int LIST_ROTATION_DURATION = 1000;
    private static final String ROTATION_TRANSFORMATION = "rotation";
    private static final float START_DEGREE = 0f;
    private static final float END_DEGREE = 360f;

    ObjectAnimator clockwiseListRotationAnimation;
    ObjectAnimator counterClockwiseListRotationAnimation;
    List<DogeGroup> dogeGroups = new ArrayList<>();

    public ExpandableAdapter(DogeDataProvider dataProvider, LayoutInflater inflater, Activity activity, ExpandableListView dogeListView) {
        this.inflater = inflater;
        this.dataProvider = dataProvider;
        this.activity = activity;
        this.dogeListView = dogeListView;
        DataLoadingAsyncTask task = new DataLoadingAsyncTask();
        task.execute();
        setupAnimations(END_DEGREE);
    }

    private void setupAnimations(float degrees) {
        clockwiseListRotationAnimation = createDogeListRotationAnimation(END_DEGREE);
        counterClockwiseListRotationAnimation = createDogeListRotationAnimation(-END_DEGREE);
    }

    private ObjectAnimator createDogeListRotationAnimation(float endDegree) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(dogeListView, ROTATION_TRANSFORMATION, START_DEGREE, endDegree);
        animator.setDuration(LIST_ROTATION_DURATION);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        return animator;
    }

    @Override
    public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, ViewGroup parent) {


        convertView = inflater.inflate(R.layout.doge_list_group, parent, false);

        TextView nameTextView = (TextView)convertView.findViewById(R.id.group_name_text_view);

        final DogeGroup dogeGroup = (DogeGroup) getDogeGroup(groupPosition);
        nameTextView.setText(dogeGroup.getName());
        nameTextView.setTag(dogeGroup.getId());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isExpanded) {
                    dogeListView.collapseGroup(groupPosition);
                } else {
                    dogeListView.expandGroup(groupPosition);
                }

            }
        });
        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                animateList(dogeGroup);
                return true;
            }
        });

        return convertView;
    }

    private void animateList(DogeGroup group) {
        boolean groupIdIsEven = group.getId() % 2 == 0;
        if (groupIdIsEven) {
            clockwiseListRotationAnimation.start();
        } else {
            counterClockwiseListRotationAnimation.start();
        }
    }




    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        convertView = inflater.inflate(R.layout.doge_list_item, parent, false);

        TextView nameTextView = (TextView)convertView.findViewById(R.id.item_name_text_view);
        ImageView imageView= (ImageView)convertView.findViewById(R.id.item_image);

        final DogeGroup dogeGroup = getDogeGroup(groupPosition);
        Doge doge = Preconditions.checkNotNull(
                dogeGroup.getDoge(childPosition),
                "Doge is null for position " + childPosition);
        nameTextView.setText(doge.getName());
        nameTextView.setTag(doge.getId());
        imageView.setImageDrawable(activity.getResources().getDrawable(doge.getIconId()));
        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Doge doge = (Doge) getChild(groupPosition, childPosition);
                showDogeDescriptionDialog(dogeGroup, doge);
                return false;
            }
        });
        if (doge.isFaved()) {
            ((ImageView) convertView.findViewById(R.id.fave_heart)).setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_favorite));
        }
        return convertView;
    }

    private void showDogeDescriptionDialog(DogeGroup group, Doge doge) {
        DogeDescriptionDialogFragment dogeDescriptionDialog = new DogeDescriptionDialogFragment();
        Bundle arguments = new Bundle();
        arguments.putString(DogeDescriptionDialogFragment.GROUP_NAME, group.getName());
        arguments.putString(DogeDescriptionDialogFragment.DOGE_NAME, doge.getName());
        arguments.putInt(DogeDescriptionDialogFragment.DOGE_ICON, doge.getIconId());
        dogeDescriptionDialog.setArguments(arguments);
        dogeDescriptionDialog.show(activity.getFragmentManager(), "Doge details");
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public int getGroupCount() {
        return dogeGroups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return dogeGroups.get(groupPosition).getDogesCount();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return dogeGroups.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return dogeGroups.get(groupPosition).getDoge(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return dogeGroups.get(groupPosition).getId();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return dogeGroups.get(groupPosition).getDoge(childPosition).getId();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    private DogeGroup getDogeGroup(int groupPosition) {
        return Preconditions.checkNotNull(
                dogeGroups.get(groupPosition),
                "Doge group is null for position " + groupPosition);
    }

    private class DataLoadingAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            dogeGroups = dataProvider.getAllNotEmpty();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            notifyDataSetChanged();
        }
    }
}
