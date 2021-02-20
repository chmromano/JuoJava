package fi.metropolia.christro.juo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

public class IntakeInputViewHolder extends RecyclerView.ViewHolder {
    private final TextView intakeInputItemView;

    private IntakeInputViewHolder(View itemView) {
        super(itemView);
        intakeInputItemView = itemView.findViewById(R.id.text_view_hour);
    }

    public void bind(String text) {
        intakeInputItemView.setText(text);
    }

    static IntakeInputViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.intake_input_item, parent, false);
        return new IntakeInputViewHolder(view);
    }
}