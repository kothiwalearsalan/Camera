package com.procinex.app;

import android.util.Log;

public class NativeBridge {
    private static final String TAG = "NativeBridge";
    private static final boolean NATIVE_AVAILABLE;

    static {
        boolean loaded;
        try {
            System.loadLibrary("procinex_core");
            loaded = true;
        } catch (UnsatisfiedLinkError e) {
            Log.e(TAG, "Failed to load native library 'procinex_core'", e);
            loaded = false;
        }
        NATIVE_AVAILABLE = loaded;
    }

    public static boolean isNativeAvailable() {
        return NATIVE_AVAILABLE;
    }

    private static native String stringFromJNI();

    /**
     * Returns the native greeting, or {@code null} when the native library
     * could not be loaded. Callers must handle the null case rather than
     * letting an {@link UnsatisfiedLinkError} propagate.
     */
    public static String getGreeting() {
        if (!NATIVE_AVAILABLE) {
            Log.w(TAG, "Native library unavailable; returning no greeting");
            return null;
        }
        try {
            return stringFromJNI();
        } catch (UnsatisfiedLinkError e) {
            Log.e(TAG, "Native method stringFromJNI is unavailable", e);
            return null;
        }
    }
}
