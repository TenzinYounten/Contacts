package com.example.contactlisting;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomContactDialog extends DialogFragment {
    String name = "", number = "", email = "",image = "";
    Context context;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        name = bundle.getString("name");
        number = bundle.getString("number");
        email = bundle.getString("email");
        image = bundle.getString("image");
        context = getContext();

        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom, null);
        TextView textViewName = (TextView) dialogView.findViewById(R.id.name);
        textViewName.setText(name);
        TextView phoneText = (TextView) dialogView.findViewById(R.id.phoneTxt);
        phoneText.setText(number);
        TextView textViewEmail = (TextView) dialogView.findViewById(R.id.emailTxt);
        textViewEmail.setText(email);
        ImageView imageView = (ImageView) dialogView.findViewById(R.id.image);
        if(!image.isEmpty()) {
            imageView.setImageURI(Uri.parse(image));
        }
        Button dismissButton = (Button) dialogView.findViewById(R.id.dismiss);
        dismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        Button call = (Button) dialogView.findViewById(R.id.call);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!callUser(number)){
                    dismiss();
                }
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(dialogView);
        return builder.create();
    }

    private boolean callUser(String contactNumber) {
        Long number = Long.parseLong("0");
        contactNumber = contactNumber.replaceAll("\\s", "");

        if ((!contactNumber.isEmpty()) & contactNumber != null & !contactNumber.equalsIgnoreCase("null")) {
            try {
                number = Long.parseLong(contactNumber);
            } catch (NumberFormatException e) {
                return false;
            }
            if (number > 0) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL,
                        Uri.parse("tel:" + number));
                context.startActivity(callIntent);
            } else
                return false;
        }
        return true;
    }
}
