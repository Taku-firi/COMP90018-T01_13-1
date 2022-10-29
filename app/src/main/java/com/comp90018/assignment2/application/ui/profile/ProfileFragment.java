package com.comp90018.assignment2.application.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.comp90018.assignment2.R;


public class ProfileFragment extends Fragment {
    private ProfileViewModel profileViewModel;
    private TextView profileUserName;
    private TextView profileUserId;
    private TextView profileGender;
    private TextView profileAge;
    private TextView profileLocation;
    private TextView profileInterests;
    private TextView profileSelfIntroduction;

    final static int REQUEST=10;
    final static int RESULT_OK=1;
    final static int RESULT_CANCELED=-1;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        profileUserName= root.findViewById(R.id.profileUserName);
        profileUserId= root.findViewById(R.id.profileUserId);
        profileGender= root.findViewById(R.id.profileGender);
        profileAge= root.findViewById(R.id.profileAge);
        profileLocation= root.findViewById(R.id.profileLocation);
        profileInterests= root.findViewById(R.id.profileInterests);
        profileSelfIntroduction= root.findViewById(R.id.profileSelfIntroduction);

        //pressure.setText(String.valueOf(event.getPressure()));
        profileUserName.setText("User name: "+"Xiao Ming");
        profileUserId.setText("User Id: "+"001");
        profileGender.setText("Gender: "+"Male");
        profileAge.setText("Age: "+"22");
        profileLocation.setText("Location: "+"Melbourne");
        profileInterests.setText("Interests: "+"Swimming");
        profileSelfIntroduction.setText("Self introduction: "+"Love sports.");

        Button button= root.findViewById(R.id.profileSettigs);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(ProfileFragment.this.getActivity(), ProfileSetting.class);
                ProfileFragment.this.startActivityForResult(myIntent, REQUEST);
            };
            protected void onActivityResult(int requestCode,int resultCode, Intent data) {
                //requestcode
                if(requestCode==REQUEST){
                    //resultcode
                    if(resultCode==RESULT_OK){
                        //success
                        System.out.println("success");
                    }else if(resultCode==RESULT_CANCELED){
                        //fail
                        System.out.println("fail");
                    }
                }
            }
        });




        return root;
    }


}
