package com.nishant.bloodbank2.viewModels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.nishant.bloodbank2.dataClass.RequestCall;
import com.nishant.bloodbank2.Repositories.RegisteredUserRepository;


public class UserViewModel extends ViewModel {

    private final RegisteredUserRepository registeredUserRepository;

    public UserViewModel() {
        registeredUserRepository = new RegisteredUserRepository();
    }

    public MutableLiveData<RequestCall> getRegisteredUser() {
        return registeredUserRepository.select();
    }

    public MutableLiveData<RequestCall> getSearchRegisteredUser(String userBloodType, String state, String city){
        return registeredUserRepository.searchDonors(userBloodType, state, city);
    }

    public MutableLiveData<RequestCall> getRegistrationStatus(){
        return registeredUserRepository.checkForRegistration();
    }

}
