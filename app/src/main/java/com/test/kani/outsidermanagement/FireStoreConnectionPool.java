package com.test.kani.outsidermanagement;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

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

    public void insert(final FireStoreCallbackListener listener, final Map<String, Object> map, final String... args)
    {
        this.db.collection(args[0]).document(args[1]).collection(args[2]).document(args[3]).set(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid)
            {
                listener.doNext(true, true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                listener.doNext(false,null);
            }
        });


//        this.db.runTransaction(new Transaction.Function<Boolean>() {
//            @Override
//            public Boolean apply(Transaction transaction) throws FirebaseFirestoreException
//            {
//                DocumentSnapshot snapshot = transaction.get(db.collection(args[0]).document(args[1])
//                        .collection(args[2]).document(args[3]));
//
//
//                return false;
//
//
////                Log.d("@@@", "id: " + args[3] + ", " + snapshot.exists());
////
////                if( snapshot.exists() )
////                    return false;
////
////                Log.d("@@@", 222 + "");
////
////                transaction.set(db.collection(args[0]).document(args[1]).collection(args[2]).document(args[3]), map);
////
////                Log.d("@@@", 333 + "");
////
////                return true;
//            }
//        }).addOnSuccessListener(new OnSuccessListener <Boolean> () {
//            @Override
//            public void onSuccess(Boolean flag)
//            {
//                Log.d("@@@", "success : " + flag);
//                listener.doNext(true, flag);
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e)
//            {
//                Log.d("@@@", "fail");
//                listener.doNext(false,null);
//            }
//        });
    }

    public void update(final FireStoreCallbackListener listener, final Map<String, Object> map, final String... args)
    {
        this.db.collection(args[0]).document(args[1]).collection(args[2]).document(args[3]).set(map, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid)
                    {
                        listener.doNext(true, true);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                listener.doNext(false,null);
            }
        });
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
                        listener.doNext(true, task.getResult().getData());
                    else
                        listener.doNext(true,null);
                }
                else
                    listener.doNext(false, null);
            }
        });

        return null;
    }
}
