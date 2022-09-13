package com.example.eagle_child.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.eagle_child.R;
import com.example.eagle_child.activities.BaseActivity;
import com.example.eagle_child.constant.AppConstant;
import com.example.eagle_child.fragments.ConnectAccountFragment;
import com.example.eagle_child.models.AppModel;


import java.util.ArrayList;

public class AppsAdapter extends RecyclerView.Adapter<AppsAdapter.MyViewHolder> {
    ArrayList<AppModel> list;
    Context context;


    public AppsAdapter(Context context, ArrayList<AppModel> list) {
        this.context = context;
        this.list = list;

    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_social_app, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        AppModel appModel = list.get(position);

        holder.txtAppName.setText(appModel.getName());
        holder.ivAppImage.setImageResource(appModel.getIcon());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseActivity b = (BaseActivity) context;
                ConnectAccountFragment connectAccountFragment = new ConnectAccountFragment();
                connectAccountFragment.setAppModel(appModel);
                b.addSupportFragment(connectAccountFragment, AppConstant.TRANSITION_TYPES.SLIDE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        ImageView ivAppImage;
        TextView txtAppName;

        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            ivAppImage = itemView.findViewById(R.id.ivAppImage);
            txtAppName = itemView.findViewById(R.id.txtAppName);

        }
    }
}