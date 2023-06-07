package com.aliberkaygediko.anbean.Notification;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

import java.util.Objects;

public class MyFirebaseIdService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful ()) {
                    //Could not get FirebaseMessagingToken
                    return;
                }

                if (null != task.getResult () &&firebaseUser != null) {
                    //Got FirebaseMessagingToken
                    String refreshToken  = Objects.requireNonNull ( task.getResult () );
                    //Use firebaseMessagingToken further
                    updateToken(refreshToken);
                    // Log.d("EXAMPLE",refreshToken);
                }
            }
        });


    }

    private void updateToken(String refreshToken) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token = new Token(refreshToken);

        reference.child(firebaseUser.getUid()).setValue(token);
    }
}
