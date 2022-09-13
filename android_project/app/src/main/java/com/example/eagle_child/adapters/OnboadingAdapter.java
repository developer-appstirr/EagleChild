package com.example.eagle_child.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.eagle_child.R;
import com.example.eagle_child.models.StoryboardingModel;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;


public class OnboadingAdapter extends RecyclerView.Adapter<OnboadingAdapter.OnboadingAdapterViewHolder> {

    private List<StoryboardingModel> data;
    Context context;
    final LayoutInflater inflater;

    public OnboadingAdapter(Context context, List<StoryboardingModel> data) {
        this.data = data;
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public OnboadingAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OnboadingAdapterViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_pager, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull OnboadingAdapterViewHolder holder, int position) {

        holder.setOnboardingData(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class OnboadingAdapterViewHolder extends RecyclerView.ViewHolder {

        private TextView txtHeading;
        private TextView textDescription;
        private ImageView imageView;

        public OnboadingAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            txtHeading = itemView.findViewById(R.id.txtStoryboardingHeading);
            textDescription = itemView.findViewById(R.id.txtStoryboardingDescription);
            imageView = itemView.findViewById(R.id.ivStoryboardingImage);
        }

        void setOnboardingData(StoryboardingModel slider){
            txtHeading.setText(slider.getHeadingText());
            textDescription.setText(slider.getParaText());
            imageView.setImageResource(slider.getBgImage());
            //Glide.with(context).load(slider.getBgImage()).into(imageView);
        }
    }
}
