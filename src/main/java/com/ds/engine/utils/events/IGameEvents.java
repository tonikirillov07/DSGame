package com.ds.engine.utils.events;

public interface IGameEvents {
    void onStart();
    void onUpdate(float deltaTime, int fps);
    void onDispose();
}
