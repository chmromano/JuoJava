package fi.metropolia.christro.juo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import fi.metropolia.christro.juo.database.MoodEntity;

public class MoodListAdapter extends RecyclerView.Adapter<MoodListAdapter.MoodListHolder> {
    private List<MoodEntity> moodList = new ArrayList<>();
    @NonNull
    @Override
    public MoodListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mood_list_item, parent,false);
        return new MoodListHolder(itemView);
    }

    public void setMoodList(List<MoodEntity> intakeList){
        this.moodList = intakeList;
        notifyDataSetChanged();
    }

    public MoodEntity getMoodAt(int position){
        return moodList.get(position);
    }
    @Override
    public void onBindViewHolder(@NonNull MoodListHolder holder, int position) {
        MoodEntity currentMood = moodList.get(position);

        if (currentMood.getMood() == 1){
            holder.textViewMoodListEmoji.setText(R.string.emoji_bad);
        } else if (currentMood.getMood() == 2) {
            holder.textViewMoodListEmoji.setText(R.string.emoji_normal);
        } else if (currentMood.getMood() == 3) {
            holder.textViewMoodListEmoji.setText(R.string.emoji_good);
        }
        holder.textViewMoodListDate.setText(currentMood.getDate());
    }

    @Override
    public int getItemCount() {
        return moodList.size();
    }

    class MoodListHolder extends RecyclerView.ViewHolder{
        private TextView textViewMoodListEmoji;
        private TextView textViewMoodListDate;

        public MoodListHolder(@NonNull View itemView) {
            super(itemView);
            textViewMoodListEmoji = itemView.findViewById(R.id.textViewMoodListEmoji);
            textViewMoodListDate = itemView.findViewById(R.id.textViewMoodListDate);
        }
    }
}
