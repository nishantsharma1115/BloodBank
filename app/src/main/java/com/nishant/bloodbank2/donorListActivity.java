
package com.nishant.bloodbank2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nishant.bloodbank2.adapters.RecyclerAdapter;
import com.nishant.bloodbank2.dataClass.RequestCall;
import com.nishant.bloodbank2.databinding.ActivityDonorListBinding;
import com.nishant.bloodbank2.viewModels.UserViewModel;

import java.util.Objects;

public class donorListActivity extends AppCompatActivity {

    ActivityDonorListBinding binding;

    RecyclerAdapter mAdapter;
    UserViewModel viewModel;
    String userBloodType, userState, userCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_donor_list);

        Intent i = getIntent();
        userBloodType = i.getStringExtra("Blood Type");
        userState = i.getStringExtra("State");
        userCity = i.getStringExtra("City");

        viewModel = new ViewModelProvider(this).get(UserViewModel.class);

        viewModel.getSearchRegisteredUser(userBloodType, userState, userCity).observe(this, new Observer<RequestCall>() {
            @Override
            public void onChanged(RequestCall requestCall) {
                if (requestCall.getStatus() == 0) {
                    Log.d("Success", "Success Done");
                } else if (requestCall.getStatus() == 1) {
                    mAdapter.notifyDataSetChanged();
                } else if (requestCall.getStatus() == -1) {
                    Log.d("Error ", "While Fetching List of Users");
                }
            }
        });

        initRecyclerView();

    }

    private void initRecyclerView() {
        mAdapter = new RecyclerAdapter(this, Objects.requireNonNull(viewModel.getSearchRegisteredUser(userBloodType, userState, userCity).getValue()).getAllRegisteredUser());
        RecyclerView.LayoutManager linearLayout = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(linearLayout);
        binding.recyclerView.setAdapter(mAdapter);
    }
}
