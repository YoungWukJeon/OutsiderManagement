package com.test.kani.outsidermanagement;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.List;
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

    public void insertNoID(final FireStoreCallbackListener listener, final Map<String, Object> map, final String... args)
    {
        this.db.collection(args[0]).add(map)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference)
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

    public void insert(final FireStoreCallbackListener listener, final Map<String, Object> map, final String... args)
    {
        this.db.collection(args[0]).document(args[1]).set(map)
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
        this.db.collection(args[0]).document(args[1]).set(map, SetOptions.merge())
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

    public void updateBatch(final FireStoreCallbackListener listener, final String... args)
    {
        this.db.collection(args[0]).whereEqualTo(args[1], args[2]).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {
                        if( task.isSuccessful() )
                        {
                            if( !task.getResult().isEmpty() )
                            {
                                WriteBatch batch = db.batch();

                                for( QueryDocumentSnapshot document : task.getResult() )
                                    batch.update(document.getReference(), args[3], args[4], args[5], args[6]);

                                batch.commit();

//                                        .addOnCompleteListener(new OnCompleteListener<Void>()
//                                {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task)
//                                    {
//
//                                    }
//                                }).addOnFailureListener(new OnFailureListener()
//                                {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e)
//                                    {
//
//                                    }
//                                });

                                listener.doNext(true, true);
                            }
                            else
                                listener.doNext(true,false);
                        }
                        else
                            listener.doNext(false, null);

                    }
                });


//        this.db.collection(args[0]).whereEqualTo(args[1], args[2]).get()
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid)
//                    {
//                        listener.doNext(true, true);
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e)
//            {
//                listener.doNext(false,null);
//            }
//        });
    }

    public Map<String, Object> select(final FireStoreCallbackListener listener, String... args)
    {
        this.db.collection(args[0]).whereEqualTo(args[1], args[2])
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task)
            {
                if( task.isSuccessful() )
                {
                    if( !task.getResult().isEmpty() )
                    {
                        List<Map<String, Object>> list = new ArrayList<> ();

                        for( QueryDocumentSnapshot document : task.getResult() )
                            list.add(document.getData());

                        listener.doNext(true, list);
                    }
                    else
                        listener.doNext(true,null);
                }
                else
                    listener.doNext(false, null);
            }
        });

        return null;
    }

    public Map<String, Object> selectSorting(final FireStoreCallbackListener listener, String... args)
    {
        Query.Direction direction = (args.length > 4)? Query.Direction.DESCENDING: Query.Direction.ASCENDING;

        this.db.collection(args[0]).whereEqualTo(args[1], args[2]).orderBy(args[3], direction)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task)
            {
                if( task.isSuccessful() )
                {
                    if( !task.getResult().isEmpty() )
                    {
                        List<Map<String, Object>> list = new ArrayList<> ();

                        for( QueryDocumentSnapshot document : task.getResult() )
                            list.add(document.getData());

                        listener.doNext(true, list);
                    }
                    else
                        listener.doNext(true,null);
                }
                else
                    listener.doNext(false, null);
            }
        });

        return null;
    }

    public Map<String, Object> selectBetweenDate(final FireStoreCallbackListener listener, String... args)
    {
        this.db.collection(args[0]).whereEqualTo(args[1], args[2]);
        this.db.collection(args[0]).whereGreaterThanOrEqualTo(args[3], args[4]);
        this.db.collection(args[0]).whereLessThanOrEqualTo(args[5], args[6])
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task)
            {
                if( task.isSuccessful() )
                {
                    if( !task.getResult().isEmpty() )
                    {
                        List<Map<String, Object>> list = new ArrayList<> ();

                        for( QueryDocumentSnapshot document : task.getResult() )
                            list.add(document.getData());

                        listener.doNext(true, list);
                    }
                    else
                        listener.doNext(true,null);
                }
                else
                    listener.doNext(false, null);
            }
        });

        return null;
    }

    public Map<String, Object> selectOne(final FireStoreCallbackListener listener, String... args)
    {
        this.db.collection(args[0]).document(args[1])
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

//    public Map<String, Object> selectOnce(final FireStoreCallbackListener listener, String... args)
//    {
////        String collectionName = args[0];
////        String documentName = args[1];
//
//        this.db.collection(args[0]).whereEqualTo("id", args[1]);
//        this.db.collection(args[0]).whereGreaterThan("report.date", args[2])
//                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
//        {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task)
//            {
//                if( task.isSuccessful() )
//                {
//                    if( !task.getResult().isEmpty() )
//                        listener.doNext(true, task.getResult().getDocuments().get(0).getData());
//                    else
//                        listener.doNext(true,null);
//                }
//                else
//                    listener.doNext(false, null);
//            }
//        });
//
//        return null;
//    }
}
