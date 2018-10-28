package com.test.kani.outsidermanagement;

public interface FireStoreCallbackListener
{
    void doNext(boolean isSuccesful, Object obj);
    void occurError(int errorCode);
}
