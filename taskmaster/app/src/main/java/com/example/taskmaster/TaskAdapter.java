package com.example.taskmaster;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.datastore.generated.model.TaskTodo;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    List<TaskTodo> tasksList = new ArrayList<TaskTodo>();

    public TaskAdapter(List<TaskTodo> tasksList) {
        this.tasksList = tasksList;
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        public TaskTodo task;
        View itemView;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;

            itemView.setOnClickListener((view -> {
                Intent goToTaskDetail = new Intent(view.getContext(), TaskDetail.class);
                goToTaskDetail.putExtra("taskName", task.getTitle());
                goToTaskDetail.putExtra("taskBody", task.getBody());
                goToTaskDetail.putExtra("taskState", task.getState());
                goToTaskDetail.putExtra("taskImage", task.getImage());
                view.getContext().startActivity(goToTaskDetail);
            }));
        }
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_task, parent, false);
        TaskViewHolder taskViewHolder = new TaskViewHolder(view);
        return taskViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TaskAdapter.TaskViewHolder holder, int position) {
        holder.task = tasksList.get(position);
        TextView title = holder.itemView.findViewById(R.id.titleInFrag);
        TextView body = holder.itemView.findViewById(R.id.bodyInFrag);
        TextView state = holder.itemView.findViewById(R.id.stateInFrag);

        title.setText(holder.task.getTitle());
        body.setText(holder.task.getBody());
        state.setText(holder.task.getState());
    }

    @Override
    public int getItemCount() {
        return tasksList.size();
    }
}
