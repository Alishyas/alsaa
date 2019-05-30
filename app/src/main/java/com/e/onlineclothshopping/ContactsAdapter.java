package com.e.onlineclothshopping;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder> {
    Context mContext;
    List<Item> contactsList;

    public ContactsAdapter(Context mContext, List<Item> contactsList) {
        this.mContext = mContext;
        this.contactsList = contactsList;
    }

    @NonNull
    @Override
    public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.contact,viewGroup,false);
        return new ContactsViewHolder(view);
    }

    private void StrictMode() {
        android.os.StrictMode.ThreadPolicy policy =
                new android.os.StrictMode.ThreadPolicy.Builder().permitAll().build();
        android.os.StrictMode.setThreadPolicy(policy);


    }

    @Override
    public void onBindViewHolder(@NonNull ContactsViewHolder contactsViewHolder, int i) {
      final Item contacts =contactsList.get(i);
      String imgPath= Url.BASE_URL + "uploads/" + contacts.getImage();
      StrictMode();
        try {
            URL url = new URL(imgPath);
            contactsViewHolder.imgProfile.setImageBitmap(BitmapFactory.decodeStream((InputStream)url.getContent()));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        contactsViewHolder.tvName.setText(contacts.getItemName());
        contactsViewHolder.tvPrice.setText(contacts.getItemPrice());
        contactsViewHolder.tvDescription.setText(contacts.getItemDescription());
        contactsViewHolder.imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ItemDetailsActivity.class);
                intent.putExtra("image",contacts.getImage());
                intent.putExtra("code",contacts.getItemCode());
                intent.putExtra("name",contacts.getItemName());
                intent.putExtra("price", contacts.getItemPrice());
                intent.putExtra("description", contacts.getItemDescription());
                mContext.startActivity(intent);


            }
        });

    }

    @Override
    public int getItemCount() {
        return contactsList.size();
    }

    public class ContactsViewHolder extends RecyclerView.ViewHolder{
        ImageView imgProfile;
       TextView tvName,tvPrice, tvDescription;

        public ContactsViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProfile=itemView.findViewById(R.id.imgProfile);
            tvName=itemView.findViewById(R.id.tvName);
            tvPrice=itemView.findViewById(R.id.tvPrice);
            tvDescription=itemView.findViewById(R.id.tvDescription);
        }
    }
    }

