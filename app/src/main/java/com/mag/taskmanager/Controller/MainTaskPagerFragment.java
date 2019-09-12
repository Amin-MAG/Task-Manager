package com.mag.taskmanager.Controller;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.mag.taskmanager.Model.Repository;
import com.mag.taskmanager.Model.TaskStatus;
import com.mag.taskmanager.R;
import com.mag.taskmanager.Util.UiUtil;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainTaskPagerFragment extends Fragment {

    public static final int REQUEST_CODE_FOR_DIALOG = 100;
    public static final String DIALOG_ERROR = "dialog_error";
    public static final String ARG_USERNAME = "arg_username";
    public static final String ADD_TASK_FRAGMENT = "add_task_fragment";
    public static final String TO_DO = "To Do";
    public static final String DOING = "Doing";
    public static final String DONE = "Done";

    private HashMap<TaskStatus, TaskListFragment> taskListFragments = new HashMap<>();

    private ConstraintLayout mainLayout;
    private TabLayout statusTabLayout;
    private ViewPager taskViewPager;
    private FloatingActionButton fab;


    public static MainTaskPagerFragment newInstance(String username) {

        Bundle args = new Bundle();
        args.putString("arg_username", username);

        MainTaskPagerFragment fragment = new MainTaskPagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public MainTaskPagerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_task_pager, container, false);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE_FOR_DIALOG:

                if (resultCode == Activity.RESULT_OK) {
                    if (data.getIntExtra("has_error", 0) == 1)
                        UiUtil.showSnackbar(mainLayout, data.getStringExtra(DIALOG_ERROR), getResources().getString(R.color.task_app_red));
                    else {
                        taskListFragments.get(TaskStatus.TODO).update();
                        UiUtil.showSnackbar(mainLayout, getResources().getString(R.string.successfully_added), getResources().getString(R.color.task_app_green_dark));
                    }
                }

                break;
            default:
                break;
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();
        final String username = bundle.getString(ARG_USERNAME);

        mainLayout = view.findViewById(R.id.pagerFragment_mainLayout);


        // Floating Action bar

        fab = view.findViewById(R.id.taskActivity_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddTaskFragment addTaskFragment = AddTaskFragment.newInstance(username);
                addTaskFragment.setTargetFragment(MainTaskPagerFragment.this, REQUEST_CODE_FOR_DIALOG);
                addTaskFragment.show(getFragmentManager(), ADD_TASK_FRAGMENT);
            }
        });


        // Tab Layout

        statusTabLayout = view.findViewById(R.id.pagerFragment_statusTabLayout);

        statusTabLayout.addTab(statusTabLayout.newTab().setText(TO_DO));
        statusTabLayout.addTab(statusTabLayout.newTab().setText(DOING));
        statusTabLayout.getTabAt(1).select();
        statusTabLayout.addTab(statusTabLayout.newTab().setText(DONE));

        statusTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                taskViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }

        });
        // 3 Recycler

        for (int i = 0; i < 3; i++)
            taskListFragments.put(TaskStatus.values()[i], TaskListFragment.newInstance(TaskStatus.values()[i], username));

        // View Pager

        taskViewPager = view.findViewById(R.id.pagerFragment_viewPager);
        taskViewPager.setAdapter(new FragmentStatePagerAdapter(getFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                if (Repository.getInstance().getUserByUsername(username).getTaskByStatus(TaskStatus.values()[position]).size() != 0) {
                    TaskStatus status = TaskStatus.values()[position];
                    return taskListFragments.get(status);
                } else
                    return EmptyListFragment.newInstance();
            }

            @Override
            public int getCount() {
                return 3;
            }

        });
        taskViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                statusTabLayout.getTabAt(position).select();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }

        });
        taskViewPager.setCurrentItem(1);
    }


}
