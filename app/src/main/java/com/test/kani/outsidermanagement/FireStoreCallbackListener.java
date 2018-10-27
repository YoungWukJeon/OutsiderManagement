package com.test.kani.outsidermanagement;

public interface FireStoreCallbackListener
{
    void doNext(Object obj);
    void occurError(int errorCode);
}
