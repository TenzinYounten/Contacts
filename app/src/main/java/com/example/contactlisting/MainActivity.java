package com.example.contactlisting;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    ArrayList<Contact> storeContacts;
    Cursor cursor;
    public static final int RequestPermissionCode = 1;

    RecyclerView recyclerView;
    CustomAdapter customAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        storeContacts = new ArrayList<Contact>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
    }


    private void showContacts() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, RequestPermissionCode);
        } else {
            LoadContactInBackground loadContact = new LoadContactInBackground();
            loadContact.execute();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        showContacts();
    }


    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {

            case RequestPermissionCode:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(MainActivity.this, "Permission Granted, Now your application can access CONTACTS.", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(MainActivity.this, "Permission Canceled, Now your application cannot access CONTACTS.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private class LoadContactInBackground extends AsyncTask<Void, Void, Void> {
        private static final String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY;

        private final String[] PROJECTION = {
                ContactsContract.Contacts._ID,
                DISPLAY_NAME,
                ContactsContract.Contacts.HAS_PHONE_NUMBER
        };
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            ContentResolver contentResolver = getContentResolver();
            Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, PROJECTION, null, null,  ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");

            while (cursor != null && cursor.moveToNext()) {

                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));

                Cursor emailCursor = contentResolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[]{id}, null);
                String email = "",number = "", imageURI = "";
                if (emailCursor != null && emailCursor.moveToFirst()) {
                    email = emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                    emailCursor.close();
                }
                Cursor numberCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);

                if (numberCursor != null && numberCursor.moveToFirst()) {
                    number = numberCursor.getString(numberCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    imageURI = numberCursor.getString(numberCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
                    numberCursor.close();
                }


                Contact contact = new Contact();
                contact.setName(name);
                contact.setPhoneNumber(number);
                contact.setEmail(email);
                contact.setImage(imageURI == null? "":imageURI);
                storeContacts.add(contact);
            }
            cursor.close();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            int count = storeContacts.size();
            customAdapter = new CustomAdapter(MainActivity.this, storeContacts);
            recyclerView.setAdapter(customAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        }
    }
}
