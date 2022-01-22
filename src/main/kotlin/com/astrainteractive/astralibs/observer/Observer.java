package com.astrainteractive.astralibs.observer;

public interface Observer<T> {
    void onChanged(T t);
}
