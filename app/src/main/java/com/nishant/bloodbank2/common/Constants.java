package com.nishant.bloodbank2.common;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Constants {
    public static final DatabaseReference DB = FirebaseDatabase.getInstance().getReference();

    public static final int OPERATION_IN_PROGRESS = 0;
    public static final int OPERATION_COMPLETE_SUCCESS = 1;
    public static final int OPERATION_COMPLETE_FAILURE = -1;
    public static final int userRegistered = 1;
    public static final int userNotRegistered = 0;

    public static final int ERROR_STATE = -1;
    public static final int PROGRESS_STATE = 0;
    public static final int SUCCESS_STATE = 1;
}
