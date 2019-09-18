package com.mag.taskmanager.Controller.ViewPagerAdapters;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.mag.taskmanager.Controller.TaskListFragment;
import com.mag.taskmanager.Model.TaskStatus;

import java.util.HashMap;

public class TaskViewPagerAdapter extends FragmentStatePagerAdapter {

    private HashMap<TaskStatus, Fragment> taskListFragments;

    public TaskViewPagerAdapter(FragmentManager fm, HashMap<TaskStatus, Fragment> taskListFragments) {
        super(fm);
        this.taskListFragments = taskListFragments;
    }

    @Override
    public Fragment getItem(int position) {
            TaskStatus status = TaskStatus.values()[position];
            return taskListFragments.get(status);
//        return TaskListFragment.newInstance(TaskStatus.DOING);
    }

    @Override
    public int getCount() {
        return taskListFragments.size();
    }

}