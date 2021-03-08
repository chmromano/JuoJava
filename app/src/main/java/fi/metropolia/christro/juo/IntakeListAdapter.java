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
public class IntakeListAdapter extends RecyclerView.Adapter<IntakeListAdapter.IntakeListHolder> {
    private List<IntakeEntity> intakeList = new ArrayList<>();
    @NonNull
    @Override
    public IntakeListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_view_item, parent,false);
        return new IntakeListHolder(itemView);
    }
    
    public void setIntakeList(List<IntakeEntity> intakeList){
        this.intakeList = intakeList;
        notifyDataSetChanged();
    }

    public IntakeEntity getIntakeAt(int position){
        return intakeList.get(position);
    }
    @Override
    public void onBindViewHolder(@NonNull IntakeListHolder holder, int position) {
        IntakeEntity currentIntake = intakeList.get(position);
        holder.textViewTitle.setText(currentIntake.getDate());
        holder.textViewDescription.setText(String.valueOf(currentIntake.getAmount()));
        holder.textViewPriority.setText(String.valueOf(currentIntake.getTime()));
    }

    @Override
    public int getItemCount() {
        return intakeList.size();
    }

    class IntakeListHolder extends RecyclerView.ViewHolder{
        private TextView textViewTitle;
        private TextView textViewDescription;
        private TextView textViewPriority;

        public IntakeListHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            textViewPriority = itemView.findViewById(R.id.text_view_priority);
        }
    }
}
