package com.nishant.bloodbank2.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nishant.bloodbank2.R;
import com.nishant.bloodbank2.dataClass.RegisteredUser;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private Context context;
    private List<RegisteredUser> registeredUsers;
    private Random rand = new Random();

    public RecyclerAdapter(Context context, List<RegisteredUser> registeredUsers) {
        this.context = context;
        this.registeredUsers = registeredUsers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.single_donor, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        RegisteredUser registeredUser = registeredUsers.get(position);

        holder.tv_bloodGroup.setText(registeredUser.getBloodGroup());
        holder.tv_name.setText(registeredUser.getName());
        holder.tv_address.setText(registeredUser.getAddress());
        holder.tv_mobile.setText(String.valueOf(registeredUser.getMobile()));
        holder.tv_email.setText(registeredUser.getEmail());

        int n = rand.nextInt(4);
        if(n==0){
            holder.background.setBackgroundResource(R.drawable.color_a);
        } else if (n==1){
            holder.background.setBackgroundResource(R.drawable.color_b);
        } else if(n==2){
            holder.background.setBackgroundResource(R.drawable.color_c);
        } else {
            holder.background.setBackgroundResource(R.drawable.color_d);
        }

        final StorageReference ref = FirebaseStorage.getInstance().getReference().child("Profile Picture").child(registeredUser.getUserId());
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get()
                        .load(uri)
                        .fit()
                        .into(holder.profilePicture);
            }
        });
    }

    @Override
    public int getItemCount() {
        return registeredUsers.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_bloodGroup, tv_name, tv_address, tv_mobile, tv_email;
        CircleImageView profilePicture;
        ConstraintLayout background;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_bloodGroup = itemView.findViewById(R.id.tv_bloodGroup);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_address = itemView.findViewById(R.id.tv_address);
            tv_mobile = itemView.findViewById(R.id.tv_mobile);
            tv_email = itemView.findViewById(R.id.tv_email);
            background = itemView.findViewById(R.id.background);
            profilePicture = itemView.findViewById(R.id.profilePicture);
        }
    }
}
