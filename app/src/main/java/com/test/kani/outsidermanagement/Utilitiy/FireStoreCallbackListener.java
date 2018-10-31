package com.test.kani.outsidermanagement.Utilitiy;

public interface FireStoreCallbackListener
{
    void doNext(boolean isSuccesful, Object obj);
    void occurError(int errorCode);
}
