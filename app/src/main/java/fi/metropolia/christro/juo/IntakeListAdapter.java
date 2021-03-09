package fi.metropolia.christro.juo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

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
     * @param parent
     * @param viewType
     * @return IntakeListHolder object with the card layout view
     */
    @NonNull
    @Override
    public IntakeListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_view_item, parent,false);
        return new IntakeListHolder(itemView);
    }

    /**
     * Calls notifyDataSetChanged() to present the most recent data in the list
     * @param intakeList
     */
    public void setIntakeList(List<IntakeEntity> intakeList){
        this.intakeList = intakeList;
        notifyDataSetChanged();
    }

    /**
     * Getter - gets the Intake entity at a certain position in the list
     * @param position
     * @return IntakeEntity
     */
    public IntakeEntity getIntakeAt(int position){
        return intakeList.get(position);
    }

    /**
     * Overrides onBindViewHolder
     * Binds the view with the data from the database
     * @param holder
     * @param position
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
     * @return int
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
     */
    class IntakeListHolder extends RecyclerView.ViewHolder{
        /**
         * TextView for title in the card record
         */
        private TextView textViewTitle;

        /**
         * TextView for description in the card record
         */
        private TextView textViewDescription;

        /**
         * TextView for priority in the card record
         */
        private TextView textViewPriority;

        /**
         * Constructor for the IntakeListHolder
         * @param itemView
         */
        public IntakeListHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            textViewPriority = itemView.findViewById(R.id.text_view_priority);
        }
    }
}
