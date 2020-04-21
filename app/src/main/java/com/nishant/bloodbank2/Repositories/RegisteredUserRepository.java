package com.nishant.bloodbank2.Repositories;


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nishant.bloodbank2.common.Constants;
import com.nishant.bloodbank2.dataClass.RegisteredUser;
import com.nishant.bloodbank2.dataClass.RequestCall;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.nishant.bloodbank2.common.Constants.DB;


public class RegisteredUserRepository {
    private final MutableLiveData<RequestCall> downloadMutableLiveData;
    private FirebaseUser user;

    public RegisteredUserRepository() {
        this.downloadMutableLiveData = new MutableLiveData<>();
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    public MutableLiveData<RequestCall> checkForRegistration() {
        final RequestCall r = new RequestCall();

        r.setStatus(Constants.OPERATION_IN_PROGRESS);
        r.setMessage("Please wait..");
        downloadMutableLiveData.setValue(r);

        user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance().getReference().child("Non Registered User").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    int Status = dataSnapshot.getValue(Integer.class);
                    r.setStatus(Constants.OPERATION_COMPLETE_SUCCESS);
                    r.setMessage("Finished");
                    Log.d("Inside Repository", String.valueOf(Status));
                    r.setRegistrationStatus(Status);
                } else {
                    r.setStatus(Constants.OPERATION_COMPLETE_SUCCESS);
                    r.setMessage("No data Found");
                }
                downloadMutableLiveData.postValue(r);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                r.setStatus(Constants.OPERATION_COMPLETE_FAILURE);
                r.setMessage(databaseError.getMessage());
                downloadMutableLiveData.postValue(r);
            }
        });
        return downloadMutableLiveData;
    }

    public MutableLiveData<RequestCall> select() {
        final RequestCall r = new RequestCall();
        final RegisteredUser registeredUser = new RegisteredUser();

        r.setStatus(Constants.OPERATION_IN_PROGRESS);
        r.setMessage("Please wait..");
        r.setRegisteredUser(registeredUser);
        downloadMutableLiveData.setValue(r);

        DB.child("Registered Users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    RegisteredUser registeredUser1 = dataSnapshot.getValue(RegisteredUser.class);
                    r.setStatus(Constants.OPERATION_COMPLETE_SUCCESS);
                    r.setMessage("Finished");
                    r.setRegisteredUser(registeredUser1);
                } else {
                    r.setStatus(Constants.OPERATION_COMPLETE_SUCCESS);
                    r.setMessage("No data Found");
                }
                downloadMutableLiveData.postValue(r);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                r.setStatus(Constants.OPERATION_COMPLETE_FAILURE);
                r.setMessage(databaseError.getMessage());
                downloadMutableLiveData.postValue(r);
            }
        });
        return downloadMutableLiveData;
    }

    public MutableLiveData<RequestCall> searchDonors(final String userBloodType, final String userState, final String userCity) {
        final RequestCall r = new RequestCall();
        final List<RegisteredUser> registeredUsers = new ArrayList<>();

        r.setStatus(Constants.OPERATION_IN_PROGRESS);
        r.setMessage("Please wait..");
        r.setAllRegisteredUser(registeredUsers);
        downloadMutableLiveData.setValue(r);

        DB.child("Registered Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if (!Objects.requireNonNull(ds.getValue(RegisteredUser.class)).getUserId().equals(user.getUid())) {
                            if (Objects.requireNonNull(ds.getValue(RegisteredUser.class)).getBloodGroup().equals(userBloodType) && Objects.requireNonNull(ds.getValue(RegisteredUser.class)).getState().equals(userState) && Objects.requireNonNull(ds.getValue(RegisteredUser.class)).getCity().equals(userCity)) {
                                RegisteredUser registeredUser = ds.getValue(RegisteredUser.class);
                                registeredUsers.add(registeredUser);
                            }
                        }
                        r.setStatus(Constants.OPERATION_COMPLETE_SUCCESS);
                        r.setMessage("Finished");
                        r.setAllRegisteredUser(registeredUsers);
                    }
                } else {
                    r.setStatus(Constants.OPERATION_COMPLETE_SUCCESS);
                    r.setMessage("No data Found");
                }
                downloadMutableLiveData.postValue(r);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                r.setStatus(Constants.OPERATION_COMPLETE_FAILURE);
                r.setMessage(databaseError.getMessage());
                downloadMutableLiveData.postValue(r);
            }
        });
        return downloadMutableLiveData;
    }
}
