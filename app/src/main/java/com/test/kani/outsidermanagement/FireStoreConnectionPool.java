package com.test.kani.outsidermanagement;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class FireStoreConnectionPool
{
    private static final FireStoreConnectionPool ourInstance = new FireStoreConnectionPool();
    private FirebaseFirestore db;

    public static FireStoreConnectionPool getInstance()
    {
        return ourInstance;
    }

    private FireStoreConnectionPool()
    {
        this.db = FirebaseFirestore.getInstance();
    }

    public FirebaseFirestore getDB()
    {
        return this.db;
    }

    private void insert()
    {

    }

    public Map<String, Object> select(final FireStoreCallbackListener listener, String... args)
    {
//        String collectionName = args[0];
//        String documentName = args[1];

        this.db.collection(args[0]).document(args[1]).collection(args[2]).document(args[3])
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task)
            {
                if( task.isSuccessful() )
                {
                    if( task.getResult().exists() )
                    {
                        listener.doNext(task.getResult().getData());
                    }
                    else
                    {
                        listener.doNext(null);
                    }
                }
                else
                {
                    Log.d("FireStore", "Task is not successful");
                }
            }
        });

        return null;
    }
}
