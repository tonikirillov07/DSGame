package com.ds.engine;

public interface IGameEvents {
    void onStart();
    void onUpdate(float deltaTime);
    void onDispose();
}
