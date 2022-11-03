package com.comp90018.assignment2.application.ui.profile;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.comp90018.assignment2.R;
import com.comp90018.assignment2.application.CreateEventActivity;
import com.comp90018.assignment2.application.objects.Event;
import com.comp90018.assignment2.application.objects.User;
import com.comp90018.assignment2.application.utils.DaoUser;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

// The fragment of user profile
public class ProfileFragment extends Fragment {
    private ProfileViewModel profileViewModel;
    private MaterialTextView profileUserName,profileLocation,profileInterests,profileSelfIntroduction,profileEmail;
    private ImageView profileGender;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        profileUserName= root.findViewById(R.id.profileUserName);
        profileLocation= root.findViewById(R.id.profileLocation);
        profileInterests= root.findViewById(R.id.profileInterests);
        profileSelfIntroduction= root.findViewById(R.id.profileSelfIntroduction);
        profileEmail = root.findViewById(R.id.profileEmail);
        profileGender = root.findViewById(R.id.user_gender);


        DaoUser daoUser = new DaoUser();

        // Get the current user
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("assignment2",MODE_PRIVATE);
        String username = sharedPreferences.getString("currentUser","");

        // Retrieve the profile data of the user from firebase database
        daoUser.getDatabaseReference().child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(username)){
                    User cUser = snapshot.child(username).getValue(User.class);
                    profileUserName.setText(cUser.getName().toString());
                    profileLocation.setText(cUser.getLocation().toString());
                    profileEmail.setText(cUser.getEmail().toString());
                    profileInterests.setText(cUser.getInterest().toString());
                    profileSelfIntroduction.setText(cUser.getDetail().toString());
                    if (cUser.getGender().equals("Female")){
                        profileGender.setImageResource(R.drawable.female);
                    }else if (cUser.getGender().equals("Unknown")){
                        profileGender.setImageResource(R.drawable.non_sex);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // Change profile
        Button button= root.findViewById(R.id.profileSettigs);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProfileSetting.class);
                startActivity(intent);
            };
        });



        return root;
    }


}
