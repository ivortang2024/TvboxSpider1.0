package com.github.catvod.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.github.catvod.crawler.SpiderDebug;

import java.lang.ref.WeakReference;

public class App {
    private static WeakReference<Context> contextWeakReference;
    private static final Handler handler = new Handler(Looper.getMainLooper());

    public static void init(Context context) {
        if (context == null) return;
        contextWeakReference = new WeakReference<>(context.getApplicationContext());
    }

    public static Context get() {
        if (contextWeakReference.get() == null) {
            throw new IllegalStateException("Context not initialized");
        }
        return contextWeakReference.get();
    }

    public static void showToast(String message) {
        handler.post(() -> {
            try {
                Toast.makeText(contextWeakReference.get(), message, Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                SpiderDebug.log("Exception in handler: " + e.getMessage());
            }
        });
    }
}
