package com.nishant.bloodbank2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nishant.bloodbank2.dataClass.RequestCall;
import com.nishant.bloodbank2.databinding.ActivityUserDashboardBinding;
import com.nishant.bloodbank2.viewModels.UserViewModel;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class userDashboardActivity extends AppCompatActivity {

    UserViewModel viewModel;
    ActivityUserDashboardBinding binding;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_dashboard);
        viewModel = new ViewModelProvider(this).get(UserViewModel.class);

        viewModel.getRegistrationStatus().observe(this, new Observer<RequestCall>() {
            @Override
            public void onChanged(RequestCall requestCall) {
                Log.d("Inside Activity", String.valueOf(requestCall.getRegistrationStatus()));
                if (requestCall.getStatus() == 1) {
                    if(requestCall.getRegistrationStatus()==1){
                        binding.registration.setVisibility(View.INVISIBLE);
                        binding.cardDetails.setVisibility(View.VISIBLE);
                        initUserCardView();
                    } else if(requestCall.getRegistrationStatus()==0){
                        binding.registration.setVisibility(View.VISIBLE);
                        binding.cardDetails.setVisibility(View.INVISIBLE);
                    }
                } else if (requestCall.getStatus() == -1) {
                    Log.d("Error", requestCall.getMessage());
                }
            }
        });

        //User Registration Button
        binding.registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(userDashboardActivity.this, userDetailActivity.class));
            }
        });

        //Search Donor Button
        binding.searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(userDashboardActivity.this, findDonorActivity.class));
            }
        });
    }

    //Card View initializer for Registered Users
    private void initUserCardView() {
        viewModel.getRegisteredUser().observe(this, new Observer<RequestCall>() {
            @Override
            public void onChanged(RequestCall requestCall) {
                if (requestCall.getStatus() == 1) {
                    binding.setRegisteredUser(requestCall.getRegisteredUser());
                } else if (requestCall.getStatus() == -1) {
                    Log.d("Error", requestCall.getMessage());
                }
            }
        });

        //Load Profile Picture
        user = FirebaseAuth.getInstance().getCurrentUser();
        final StorageReference ref = FirebaseStorage.getInstance().getReference().child("Profile Picture").child(Objects.requireNonNull(user).getUid());
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get()
                        .load(uri)
                        .fit()
                        .into(binding.imgProfilePicture);
            }
        });
    }

    //Inflate Menu
    public void showMenu(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_general, popup.getMenu());
        popup.show();

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.logOut) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(userDashboardActivity.this, splashScreenActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
                return true;
            }
        });
    }
}
