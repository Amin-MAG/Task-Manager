package com.mag.taskmanager.Controller;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mag.taskmanager.Model.Repository;
import com.mag.taskmanager.Model.Task;
import com.mag.taskmanager.Model.TaskStatus;
import com.mag.taskmanager.R;
import com.mag.taskmanager.RecyclerAdapters.TaskRecyclerAdapter;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class TaskListFragment extends Fragment {

    private TaskRecyclerAdapter taskRecyclerAdapter;
    private RecyclerView recyclerView;

    private List<Task> tasks;
    private String username;
    private TaskStatus status;

    public static TaskListFragment newInstance(TaskStatus status, String username) {

        Bundle args = new Bundle();
        args.putSerializable("arg_status", status);
        args.putString("arg_username", username);

        TaskListFragment fragment = new TaskListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public TaskListFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_task_list, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @SuppressLint("ResourceType")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();
        status = (TaskStatus) bundle.getSerializable("arg_status");
        username = bundle.getString("arg_username");

        recyclerView = view.findViewById(R.id.taskListFragment_recyclerview);

        update();

    }

    public void update(){
        taskRecyclerAdapter = new TaskRecyclerAdapter(Repository.getInstance().getUserByUsername(username).getTaskByStatus(status));
        taskRecyclerAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(taskRecyclerAdapter);
    }

}