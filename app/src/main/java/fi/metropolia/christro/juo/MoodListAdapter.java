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

/**
 * Adapter to adapt list of moods to RecyclerView.
 *
 * @author Christopher Mohan Romano
 * @version 1.0
 */
//https://www.youtube.com/watch?v=reSPN7mgshI&list=PLrnPJCHvNZuDihTpkRs6SpZhqgBqPU118&index=8
public class MoodListAdapter extends RecyclerView.Adapter<MoodListAdapter.MoodListHolder> {

    private List<MoodEntity> moodList = new ArrayList<>();

    /**
     * Holder for list based on custom layout.
     *
     * @param parent   Parent ViewGroup (RecyclerView).
     * @param viewType View type.
     * @return The ViewHolder.
     */
    @NonNull
    @Override
    public MoodListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mood_list_item, parent, false);
        return new MoodListHolder(itemView);
    }

    /**
     * Sets list of moods. Notifies when data in list changes.
     *
     * @param moodList List of moods.
     */
    public void setMoodList(List<MoodEntity> moodList) {
        this.moodList = moodList;
        notifyDataSetChanged();
    }

    /**
     * Sets the information in the custom layout based on mood ID.
     *
     * @param holder   The ViewHolder.
     * @param position Position of the item.
     */
    @Override
    public void onBindViewHolder(@NonNull MoodListHolder holder, int position) {

        MoodEntity currentMood = moodList.get(position);

        if (currentMood.getMood() == MoodActivity.MOOD_BAD_ID) {
            holder.textViewMoodListEmoji.setText(R.string.emoji_bad);
        } else if (currentMood.getMood() == MoodActivity.MOOD_NORMAL_ID) {
            holder.textViewMoodListEmoji.setText(R.string.emoji_normal);
        } else if (currentMood.getMood() == MoodActivity.MOOD_GOOD_ID) {
            holder.textViewMoodListEmoji.setText(R.string.emoji_good);
        }
        holder.textViewMoodListDate.setText(currentMood.getDate());
    }

    /**
     * Method to get item count to set the adapter.
     *
     * @return Number of items in list.
     */
    @Override
    public int getItemCount() {
        return moodList.size();
    }

    /**
     * Holder using custom layout for each item.
     *
     * @author Christopher Mohan Romano
     * @version 1.0
     */
    class MoodListHolder extends RecyclerView.ViewHolder {
        private TextView textViewMoodListEmoji;
        private TextView textViewMoodListDate;

        /**
         * Initialise views inside the custom layout.
         * @param itemView The layout to display.
         */
        public MoodListHolder(@NonNull View itemView) {
            super(itemView);
            textViewMoodListEmoji = itemView.findViewById(R.id.textViewMoodListEmoji);
            textViewMoodListDate = itemView.findViewById(R.id.textViewMoodListDate);
        }
    }
}
