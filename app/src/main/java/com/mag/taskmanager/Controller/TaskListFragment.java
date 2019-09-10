package com.mag.taskmanager.Controller;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.mag.taskmanager.Model.Repository;
import com.mag.taskmanager.Model.TaskStatus;
import com.mag.taskmanager.R;
import com.mag.taskmanager.RecyclerAdapters.TaskRecyclerAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class TaskListFragment extends Fragment {

    private TaskRecyclerAdapter taskRecyclerAdapter;
    private RecyclerView recyclerView;

    // Note
    private String username;
    private TaskStatus status;

    public static TaskListFragment newInstance(TaskStatus status, String username) {

        Bundle args = new Bundle();

        TaskListFragment fragment = new TaskListFragment(status, username);
        fragment.setArguments(args);
        return fragment;
    }

    public TaskListFragment(TaskStatus status, String username) {
        this.status = status;
        this.username = username;
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

        recyclerView = view.findViewById(R.id.taskListFragment_recyclerview);
        taskRecyclerAdapter = new TaskRecyclerAdapter(Repository.getInstance().getUserByUsername(username).getTaskByStatus(status));

        recyclerView.setAdapter(taskRecyclerAdapter);

    }

    public void update() {
        taskRecyclerAdapter.setTasks(Repository.getInstance().getUserByUsername(username).getTaskByStatus(status));
        taskRecyclerAdapter.notifyDataSetChanged();
    }

}