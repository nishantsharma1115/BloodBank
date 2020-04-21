package com.nishant.bloodbank2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nishant.bloodbank2.common.Constants;
import com.nishant.bloodbank2.dataClass.RegisteredUser;
import com.nishant.bloodbank2.databinding.ActivityUserDetailBinding;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

public class userDetailActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityUserDetailBinding binding;
    String userBloodType;
    final RegisteredUser registeredUser = new RegisteredUser();
    FirebaseUser user;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_detail);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        binding.layoutBackground.setOnClickListener(this);
        binding.tvGeneral.setOnClickListener(this);

        //Spinner for Blood Type
        binding.spinnerBloodType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                userBloodType = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(userDetailActivity.this, "Fill the Blood type", Toast.LENGTH_SHORT).show();
            }
        });
        ArrayAdapter<CharSequence> bloodTypeAdapter = ArrayAdapter.createFromResource(this, R.array.blood_type, android.R.layout.simple_spinner_item);
        bloodTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerBloodType.setAdapter(bloodTypeAdapter);
    }

    //Choose Photo from Device
    public void choosePhoto(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri selectedImage = Objects.requireNonNull(data).getData();

        if (requestCode == 1 && resultCode == RESULT_OK) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                binding.imgProfilePicture.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //Button for Data save
    public void saveButtonClicked(View view) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        saveDataToDatabase();
        Intent intent = new Intent(userDetailActivity.this, userDashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void saveDataToDatabase() {

        String state = Objects.requireNonNull(binding.edtStateEditText.getText()).toString();
        String city = Objects.requireNonNull(binding.edtCityEditText.getText()).toString();
        String pinCode = Objects.requireNonNull(binding.edtPinCodeEditText.getText()).toString();
        String address = Objects.requireNonNull(binding.edtAddressEditText.getText()).toString() + " " + city + " " + state + " " + pinCode;
        registeredUser.setName(Objects.requireNonNull(binding.edtNameEditText.getText()).toString());
        registeredUser.setUserId(user.getUid());
        registeredUser.setAddress(address);
        registeredUser.setBloodGroup(userBloodType);
        registeredUser.setCity(Objects.requireNonNull(binding.edtCityEditText.getText()).toString());
        registeredUser.setEmail(Objects.requireNonNull(binding.edtEmailEditText.getText()).toString());
        registeredUser.setMobile(Objects.requireNonNull(binding.edtPhoneNoEditText.getText()).toString());
        registeredUser.setPinCode(Objects.requireNonNull(binding.edtPinCodeEditText.getText()).toString());
        registeredUser.setState(Objects.requireNonNull(binding.edtStateEditText.getText()).toString());

        //Photo Upload to Firebase Storage
        binding.imgProfilePicture.setDrawingCacheEnabled(true);
        binding.imgProfilePicture.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) binding.imgProfilePicture.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final StorageReference ref = FirebaseStorage.getInstance().getReference().child("Profile Picture").child(Objects.requireNonNull(user).getUid());
        ref.putBytes(data);
        FirebaseDatabase.getInstance().getReference().child("Registered Users").child(user.getUid()).setValue(registeredUser);
        FirebaseDatabase.getInstance().getReference().child("Non Registered User").child(Objects.requireNonNull(user).getUid()).setValue(Constants.userRegistered);
    }

    //Keyboard Down
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.layout_background || view.getId() == R.id.tv_general) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            Objects.requireNonNull(inputMethodManager).hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(), 0);
        }
    }
}