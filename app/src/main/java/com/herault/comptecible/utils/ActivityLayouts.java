package com.herault.comptecible.utils;

import android.view.View;
import android.view.ViewGroup;

import androidx.activity.ComponentActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

public final class ActivityLayouts {

    private ActivityLayouts() {
    }

    public static void applyEdgeToEdge(ComponentActivity activity, int fittedViewId) {
        // Set window to full screen
        WindowCompat.setDecorFitsSystemWindows(activity.getWindow(), false);

        View view = activity.findViewById(fittedViewId);

        // Set Listener
        ViewCompat.setOnApplyWindowInsetsListener(view, (v, windowInsets) -> {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            mlp.leftMargin = insets.left;
            mlp.bottomMargin = insets.bottom;
            mlp.rightMargin = insets.right;
            mlp.topMargin = insets.top;
            v.setLayoutParams(mlp);
            return windowInsets;
        });
    }
}