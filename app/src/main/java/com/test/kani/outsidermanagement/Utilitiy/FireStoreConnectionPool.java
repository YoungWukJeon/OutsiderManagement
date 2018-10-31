package com.test.kani.outsidermanagement.Utilitiy;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
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
                        listener.doNext(true, documentReference.getId());
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

    public void updateOne(final FireStoreCallbackListener listener, final String... args)
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
                                for( QueryDocumentSnapshot document : task.getResult() )
                                {
                                    if( document.getString(args[3]).compareTo(args[6]) <= 0 &&
                                            document.getString(args[4]).compareTo(args[6]) >= 0 )
                                        db.collection(args[0]).document(document.getId()).update(args[5], args[6]);
                                }

                                listener.doNext(true, true);
                            }
                            else
                                listener.doNext(true,false);
                        }
                        else
                            listener.doNext(false, null);

                    }
                });
    }

    public void updateBatch(final FireStoreCallbackListener listener, final Map<String, Object> map, final String... args)
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
                                    batch.update(document.getReference(), map);

                                batch.commit();

                                listener.doNext(true, true);
                            }
                            else
                                listener.doNext(true,false);
                        }
                        else
                            listener.doNext(false, null);

                    }
                });
    }

    public void deleteOne(final FireStoreCallbackListener listener, final String... args)
    {
        this.db.collection(args[0]).document(args[1]).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid)
            {
                listener.doNext(true, true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                listener.doNext(false, null);
            }
        });
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
                        {
                            list.add(document.getData());
                            list.get(list.size() - 1).put("documentId", document.getId());
                        }

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

    public Map<String, Object> selectGreaterThanDate(final FireStoreCallbackListener listener, String... args)
    {
        this.db.collection(args[0]).whereEqualTo(args[1], args[2])
                .whereGreaterThanOrEqualTo(args[3], args[4])
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
                        {
                            list.add(document.getData());
                            list.get(list.size() - 1).put("documentId", document.getId());
                        }

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

    public Map<String, Object> selectLessThanDate(final FireStoreCallbackListener listener, String... args)
    {
        this.db.collection(args[0]).whereEqualTo(args[1], args[2])
                .whereLessThanOrEqualTo(args[3], args[4])
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
                        {
                            list.add(document.getData());
                            list.get(list.size() - 1).put("documentId", document.getId());
                        }

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
                    {
                        Map<String, Object> map = task.getResult().getData();
                        map.put("documentId", task.getResult().getId());
                        listener.doNext(true, map);
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
}
