package com.example.eagle_child.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eagle_child.R;
import com.example.eagle_child.listners.CheckBoxListner;
import com.example.eagle_child.models.AppModel;

import java.util.ArrayList;

public class AddAppAdapter extends RecyclerView.Adapter<AddAppAdapter.MyViewHolder> {
    ArrayList<AppModel> list;
    ArrayList<AppModel> selectedList = new ArrayList<>();
    Context context;

    CheckBoxListner checkBoxListner;


    public AddAppAdapter(Context context, ArrayList<AppModel> list, CheckBoxListner checkBoxListner) {
        this.context = context;
        this.list = list;
        this.checkBoxListner = checkBoxListner;

    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_social_app, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        AppModel appModel = list.get(position);

        holder.txtAppName.setText(appModel.getName());
        holder.ivAppImage.setImageResource(appModel.getIcon());

        holder.btnRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!appModel.getSelected()){

                    holder.radioGroup.clearCheck();
                    appModel.setSelected(true);
                    selectedList.add(appModel);
                    holder.btnRadio.setChecked(appModel.getSelected());
                    checkBoxListner.setListApp(selectedList);
                    notifyDataSetChanged();


                }else {
                    holder.radioGroup.clearCheck();
                    selectedList.remove(appModel);
                    appModel.setSelected(false);
                    checkBoxListner.setListApp(selectedList);
                    holder.btnRadio.setChecked(appModel.getSelected());
                    notifyDataSetChanged();

                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView ivAppImage;
        TextView txtAppName;
        RadioButton btnRadio;
        RadioGroup radioGroup;

        public MyViewHolder(View itemView) {
            super(itemView);

            ivAppImage = itemView.findViewById(R.id.ivAppImage);
            txtAppName = itemView.findViewById(R.id.txtAppName);
            btnRadio   = itemView.findViewById(R.id.btnRadio);
            radioGroup = itemView.findViewById(R.id.radioGroup);


        }
    }
}