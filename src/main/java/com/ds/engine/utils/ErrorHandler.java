package com.ds.engine.utils;

import org.jetbrains.annotations.NotNull;
import org.lwjgl.Sys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ErrorHandler {
    private static final Logger log = LoggerFactory.getLogger(ErrorHandler.class);

    public static void processError(@NotNull Exception e){
        Sys.alert("An exception has occurred", e.toString());

        log.error(e.toString());
        e.printStackTrace();
    }
}
