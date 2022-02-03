package com.example.bloodhub.Adapter;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bloodhub.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter {
    public class ViewHolder extends RecyclerView.ViewHolder{

        public CircleImageView userProfileImage;
        public TextView Type,userName,userEmail,phoneNumber,bloodGroup;
        public Button emailNow;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            userProfileImage = itemView.findViewById(R.id.userProfileImage);
            Type = itemView.findViewById(R.id.type);
            userName = itemView.findViewById(R.id.username);
            userEmail = itemView.findViewById(R.id.useremail);
            phoneNumber = itemView.findViewById(R.id.mobilenumber);
            bloodGroup = itemView.findViewById(R.id.bloodGroup);
            emailNow = itemView.findViewById(R.id.email_now);


        }
    }
}
