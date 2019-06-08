package com.example.contactlisting;

import android.content.Context;
import android.os.Bundle;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    ArrayList<Contact> contactNames;
    Context context;


    public CustomAdapter(MainActivity context, ArrayList<Contact> contactNames) {
        this.contactNames = contactNames;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowlayout, viewGroup, false);
        ViewHolder myViewHolder = new ViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        viewHolder.name.setText(contactNames.get(i).getName());
        viewHolder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, contactNames.get(i).getName(),Toast.LENGTH_LONG).show();
                CustomContactDialog customContactDialog = new CustomContactDialog();
                FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();

                Bundle bundle = new Bundle();
                bundle.putString("name", contactNames.get(i).getName());
                bundle.putString("number", contactNames.get(i).getPhoneNumber());
                bundle.putString("email", contactNames.get(i).getEmail());
                bundle.putString("image", contactNames.get(i).getImage());

                customContactDialog.setArguments(bundle);
                customContactDialog.show(fragmentManager,"contact");

            }
        });
    }


    @Override
    public int getItemCount() {
        return contactNames.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
        }
    }
}
