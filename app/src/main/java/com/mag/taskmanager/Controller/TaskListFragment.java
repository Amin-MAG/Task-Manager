package com.mag.taskmanager.Controller;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.mag.taskmanager.Controller.Adapters.TaskRecyclerAdapter;
import com.mag.taskmanager.Model.Repository;
import com.mag.taskmanager.Model.Task;
import com.mag.taskmanager.Model.TaskStatus;
import com.mag.taskmanager.R;
import com.mag.taskmanager.Util.*;
import com.mag.taskmanager.Var.Global;

import java.io.Serializable;


/**
 * A simple {@link Fragment} subclass.
 */
public class TaskListFragment extends Fragment implements Serializable {

    private static final int REQUEST_CODE_FOR_EDIT_DIALOG = 1005;
    private static final String EDIT_TASK_FRAGMENT = "edit_task_fragment";
    private static final String ARG_STATUS = "arg_status";
    private static final String DIALOG_ERROR = "dialog_error";
    private static final String HAS_ERROR = "has_error";
    private static final String DELETE_TASK = "delete_task";
    private static final String EDIT_TASK = "edit_task";
    private static final String ACTION_STRING = "action_string";

    private FrameLayout mainFrame;
    private TaskRecyclerAdapter taskRecyclerAdapter;
    private RecyclerView recyclerView;
    private View empty;
    private EditTaskFragment editTaskFragment;

    private static GetViews getViews;

    public static void setGetView(GetViews getter) {
        getViews = getter;
    }

    // Note
    private TaskStatus status;

    public static TaskListFragment newInstance(TaskStatus status) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_STATUS, status);

        TaskListFragment fragment = new TaskListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public TaskListFragment() {
    }

    @SuppressLint("ResourceType")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE_FOR_EDIT_DIALOG:

                if (resultCode == Activity.RESULT_OK) {

                    if (data.getIntExtra(HAS_ERROR, 0) == 1) {
                        UiUtil.showSnackbar(recyclerView, data.getStringExtra(DIALOG_ERROR), getResources().getString(R.color.task_app_red));
                    } else {

                        // Update

                        update();

                        // Show snackbar

                        switch (data.getStringExtra(ACTION_STRING)) {
                            case DELETE_TASK:
                                if (recyclerView != null)
                                    UiUtil.showSnackbar(recyclerView, getResources().getString(R.string.successfully_deleted), getResources().getString(R.color.task_app_green_dark));
                                break;
                            case EDIT_TASK:
                                getViews.updateTaskList();
                                if (recyclerView != null)
                                    UiUtil.showSnackbar(recyclerView, getResources().getString(R.string.successfully_edited), getResources().getString(R.color.task_app_green_dark));
                                break;
                            default:
                                break;
                        }

                    }

                }

                break;
            default:
                break;
        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.status = (TaskStatus) getArguments().getSerializable(ARG_STATUS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_task_list, container, false);
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        update();
//    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("ResourceType")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d("askdjkla", "on created: " + this);


        mainFrame = view.findViewById(R.id.taskListFragment_mainFrame);
        empty = view.findViewById(R.id.taskListFragment_empty);

        recyclerView = view.findViewById(R.id.taskListFragment_recyclerview);
        taskRecyclerAdapter = getNewRecycleAdapter();

        recyclerView.setAdapter(taskRecyclerAdapter);

        update();

    }

    private TaskRecyclerAdapter getNewRecycleAdapter() {
        return new TaskRecyclerAdapter(Repository.getInstance(getContext())
                .getUserByUsername(Global.getOnlineUsername())
                .getTaskByStatus(status)
                , new TaskRecyclerAdapter.OnItemClickListener() {
            @Override
            public void showEditDialog(Task task) {
                editTaskFragment = EditTaskFragment.newInstance(task);
                editTaskFragment.setTargetFragment(TaskListFragment.this, REQUEST_CODE_FOR_EDIT_DIALOG);
                editTaskFragment.show(getFragmentManager(), EDIT_TASK_FRAGMENT);
            }
        });
    }

    public void update() {

        taskRecyclerAdapter.setTasks(Repository
                .getInstance(getContext())
                .getUserByUsername(Global.getOnlineUsername())
                .getTaskByStatus(status));

        taskRecyclerAdapter.notifyDataSetChanged();
        if (Repository.getInstance(getContext()).getUserByUsername(Global.getOnlineUsername()).getTaskByStatus(status).size() == 0)
            empty.setVisibility(View.VISIBLE);
        else if (empty.getVisibility() == View.VISIBLE)
            empty.setVisibility(View.GONE);

    }


    public interface GetViews {
        void updateTaskList();
    }


}