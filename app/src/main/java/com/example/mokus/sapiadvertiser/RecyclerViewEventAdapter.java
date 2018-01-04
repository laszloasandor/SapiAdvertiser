package com.example.mokus.sapiadvertiser;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Mokus on 1/1/2018.
 */

public class RecyclerViewEventAdapter extends RecyclerView.Adapter<RecyclerViewEventAdapter.MyViewHolder> {

    private static final String TAG = "RecyclerView";

    private List<Post> postList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView postTitle;
        public TextView postShortDesc;

        public MyViewHolder(View view) {
            super(view);
            context = view.getContext();
            postTitle = (TextView) view.findViewById(R.id.post_title);
            postShortDesc = (TextView) view.findViewById(R.id.post_short_description);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            final Intent i;
            i =  new Intent(context, PostOpenActivity.class);
            int pos = getAdapterPosition();
            if(pos != RecyclerView.NO_POSITION){
                Post e = postList.get(pos);
                i.putExtra("Post", e);
                Log.d(TAG, e.getTitle() + " " + e.getShortDesc() + " " + e.getLongDesc());
                context.startActivity(i);
            }
        }
    }


    public RecyclerViewEventAdapter(List<Post> postList) {
        this.postList = postList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Post post = postList.get(position);
        holder.postTitle.setText(post.getTitle());
        holder.postShortDesc.setText(post.getShortDesc());
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }
}

