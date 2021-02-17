package fi.metropolia.christro.juo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class IntakeInputAdapter extends RecyclerView.Adapter<IntakeInputAdapter.IntakeInputHolder> {
    private List<IntakeInput> intakes = new ArrayList<>();

    @NonNull
    @Override
    public IntakeInputHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.intake_input_item, parent, false);
        return new IntakeInputHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull IntakeInputHolder holder, int position) {
        IntakeInput currentIntakeInput = intakes.get(position);
        holder.textViewAmount.setText(String.valueOf(currentIntakeInput.getAmount()));
        holder.textViewHour.setText(String.valueOf(currentIntakeInput.getHour()));
    }

    @Override
    public int getItemCount() {
        return intakes.size();
    }

    public void setIntakes(List<IntakeInput> intakes) {
        this.intakes = intakes;
        notifyDataSetChanged();
    }

    class IntakeInputHolder extends RecyclerView.ViewHolder {
        private TextView textViewAmount;
        private TextView textViewHour;

        public IntakeInputHolder(@NonNull View itemView) {
            super(itemView);
            textViewAmount = itemView.findViewById(R.id.text_view_amount);
            textViewHour = itemView.findViewById(R.id.text_view_hour);
        }
    }
}
