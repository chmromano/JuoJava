package fi.metropolia.christro.juo.utilities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import fi.metropolia.christro.juo.R;
import fi.metropolia.christro.juo.database.IntakeEntity;

/*
https://www.youtube.com/watch?v=reSPN7mgshI&list=PLrnPJCHvNZuDihTpkRs6SpZhqgBqPU118&index=8
https://www.youtube.com/watch?v=QJUCD32dzHE&list=PLrnPJCHvNZuDihTpkRs6SpZhqgBqPU118&index=9
*/

/**
 * IntakeListAdapter class
 * <p>
 * The class extends creates an adapter for RecyclerView,
 * it uses a card display for the intake records
 * it organizes the RecyclerView cards according to their chronology
 *
 * @author Itale Tabaksmane
 * @version 1.0
 */
public class IntakeListAdapter extends RecyclerView.Adapter<IntakeListAdapter.IntakeListHolder> {
    /**
     * List<IntakeEntity> to store the records of type IntakeEntity
     */
    private List<IntakeEntity> intakeList = new ArrayList<>();

    /**
     * This method uses the card layout for a single record
     * it uses this layout for every record
     *
     * @param parent   Parent ViewGroup (RecyclerView).
     * @param viewType View type.
     * @return IntakeListHolder object with the card layout view
     */
    @NonNull
    @Override
    public IntakeListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.intake_list_item, parent, false);
        return new IntakeListHolder(itemView);
    }

    /**
     * Calls notifyDataSetChanged() to present the most recent data in the list
     *
     * @param intakeList List of intakes.
     */
    public void setIntakeList(List<IntakeEntity> intakeList) {
        this.intakeList = intakeList;
        notifyDataSetChanged();
    }

    /**
     * Getter - gets the Intake entity at a certain position in the list
     *
     * @param position Position of the IntakeEntity.
     * @return IntakeEntity
     */
    public IntakeEntity getIntakeAt(int position) {
        return intakeList.get(position);
    }

    /**
     * Overrides onBindViewHolder
     * Binds the view with the data from the database
     *
     * @param holder   The ViewHolder.
     * @param position Position of the item in the list.
     */
    @Override
    public void onBindViewHolder(@NonNull IntakeListHolder holder, int position) {
        IntakeEntity currentIntake = intakeList.get(position);
        holder.textViewTitle.setText(currentIntake.getDate());
        holder.textViewDescription.setText(String.valueOf(currentIntake.getAmount()));
        holder.textViewPriority.setText(String.valueOf(currentIntake.getTime()));
    }

    /**
     * Getter- the length of the list
     *
     * @return Integer size of list.
     */
    @Override
    public int getItemCount() {
        return intakeList.size();
    }

    /**
     * IntakeListHolder class
     * The class extends RecyclerView.ViewHolder
     * The class gets the textview from the layout and uses
     * that data in the constructor of the IntakeListHolder
     *
     * @author Itale Tabaksmane
     * @version 1.0
     */
    static class IntakeListHolder extends RecyclerView.ViewHolder {
        /**
         * TextView for title in the card record
         */
        private final TextView textViewTitle;

        /**
         * TextView for description in the card record
         */
        private final TextView textViewDescription;

        /**
         * TextView for priority in the card record
         */
        private final TextView textViewPriority;

        /**
         * Constructor for the IntakeListHolder
         *
         * @param itemView The layout to display.
         */
        public IntakeListHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            textViewPriority = itemView.findViewById(R.id.text_view_priority);
        }
    }
}
