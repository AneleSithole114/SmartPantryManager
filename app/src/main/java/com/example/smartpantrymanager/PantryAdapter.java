package com.example.smartpantrymanager;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.PantryViewHolder> {

    private Context context;
    private Cursor cursor;

    public PantryAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    @NonNull
    @Override
    public PantryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_pantry, parent, false);
        return new PantryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PantryViewHolder holder, int position) {
        if (!cursor.moveToPosition(position)) {
            return;
        }

        long id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
        String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME));
        double quantity = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_QUANTITY));
        String unit = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_UNIT));
        String expiry = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EXPIRY));

        holder.textName.setText(name);
        holder.textDetails.setText("Qty: " + quantity + " " + unit + " | Exp: " + expiry);

        // Tap to Delete item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper dbHelper = new DatabaseHelper(context);
                dbHelper.deletePantryItem(id);
                swapCursor(dbHelper.getAllPantryItems());
                Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show();
            }
        });

        // Long-click to Edit item
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(context, AddPantryActivity.class);
                intent.putExtra("ID", id);
                intent.putExtra("NAME", name);
                intent.putExtra("QTY", quantity);
                intent.putExtra("UNIT", unit);
                intent.putExtra("EXPIRY", expiry);
                context.startActivity(intent);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return (cursor != null) ? cursor.getCount() : 0;
    }

    public void swapCursor(Cursor newCursor) {
        if (cursor != null) {
            cursor.close();
        }
        cursor = newCursor;
        if (newCursor != null) {
            notifyDataSetChanged();
        }
    }

    public static class PantryViewHolder extends RecyclerView.ViewHolder {
        TextView textName, textDetails;

        public PantryViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textItemName);
            textDetails = itemView.findViewById(R.id.textItemDetails);
        }
    }
}