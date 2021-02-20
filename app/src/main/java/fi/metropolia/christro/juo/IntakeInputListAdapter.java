package fi.metropolia.christro.juo;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

public class IntakeInputListAdapter extends ListAdapter<IntakeInput, IntakeInputViewHolder> {

    public IntakeInputListAdapter(@NonNull DiffUtil.ItemCallback<IntakeInput> diffCallback) {
        super(diffCallback);
    }

    @Override
    public IntakeInputViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return IntakeInputViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(IntakeInputViewHolder holder, int position) {
        IntakeInput current = getItem(position);
        holder.bind(String.valueOf(current.getHour()));
    }

    static class IntakeInputDiff extends DiffUtil.ItemCallback<IntakeInput> {

        @Override
        public boolean areItemsTheSame(@NonNull IntakeInput oldItem, @NonNull IntakeInput newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull IntakeInput oldItem, @NonNull IntakeInput newItem) {
            return oldItem.getHour() == newItem.getHour();
        }
    }
}
