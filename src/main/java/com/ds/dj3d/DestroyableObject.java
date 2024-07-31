package com.ds.dj3d;

public abstract class DestroyableObject {
    private boolean isDestroyed;

    public void destroy(){
        isDestroyed = true;
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }
}
