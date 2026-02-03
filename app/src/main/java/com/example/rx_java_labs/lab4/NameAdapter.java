package com.example.rx_java_labs.lab4;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rx_java_labs.R;

import java.util.List;

public class NameAdapter extends RecyclerView.Adapter<NameAdapter.NameViewHolder> {

    private List<String> names;

    void setList(List<String> names){
        this.names=names;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.name_item,parent,false);
        return new NameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NameViewHolder holder, int position) {
        String name = names.get(position);
        holder.text.setText(name);
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    public static class NameViewHolder extends RecyclerView.ViewHolder{
        TextView text;
        public NameViewHolder(View view){
            super(view);
            text=view.findViewById(R.id.nameTxt);
        }
    }
}