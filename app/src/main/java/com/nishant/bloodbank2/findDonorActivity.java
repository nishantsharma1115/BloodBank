package com.nishant.bloodbank2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.nishant.bloodbank2.databinding.ActivityFindDonorBinding;

public class findDonorActivity extends AppCompatActivity {

    String userBloodType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityFindDonorBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_find_donor);

        //Setting the Spinner and ItemSelectListener
        binding.spinnerBloodType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                userBloodType = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(findDonorActivity.this, "Fill the Blood type", Toast.LENGTH_SHORT).show();
            }
        });
        ArrayAdapter<CharSequence> bloodTypeAdapter = ArrayAdapter.createFromResource(this, R.array.blood_type, android.R.layout.simple_spinner_item);
        bloodTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerBloodType.setAdapter(bloodTypeAdapter);

        //Search button Clicked
        binding.btnFindDonor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userBloodType.equals("Choose Blood Type")) {
                    Toast.makeText(findDonorActivity.this, "Select the blood type", Toast.LENGTH_SHORT).show();
                } else if (binding.edtState.getText().toString().equals("")) {
                    Toast.makeText(findDonorActivity.this, "Select the state", Toast.LENGTH_SHORT).show();
                } else if (binding.edtCity.getText().toString().equals("")) {
                    Toast.makeText(findDonorActivity.this, "Select the City", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(findDonorActivity.this, donorListActivity.class);
                    intent.putExtra("Blood Type", userBloodType);
                    intent.putExtra("State", binding.edtState.getText().toString());
                    intent.putExtra("City", binding.edtCity.getText().toString());
                    startActivity(intent);
                }
            }
        });
    }
}
