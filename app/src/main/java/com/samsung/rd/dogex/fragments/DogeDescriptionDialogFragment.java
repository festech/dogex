package com.samsung.rd.dogex.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.google.common.base.Strings;
import com.samsung.rd.dogex.R;

/**
 * {@link DialogFragment} used to show a single product description
 */
public class DogeDescriptionDialogFragment extends DialogFragment {
    public static final String GROUP_NAME = new String("GROUP_NAME");
    public static final String DOGE_NAME = new String("DOGE_NAME");
    public static final String DOGE_ICON = new String("DOGE_ICON");
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        String groupName = Strings.emptyToNull(arguments.getString(GROUP_NAME));
        String productName = Strings.emptyToNull(arguments.getString(DOGE_NAME));
        int iconKey = arguments.getInt(DOGE_ICON, R.mipmap.ic_launcher);
        Bitmap map = BitmapFactory.decodeResource(getResources(), iconKey);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(map, width / 10, width / 10, true);
        Drawable dogeIcon = new BitmapDrawable(getResources(), scaledBitmap);
        return new AlertDialog.Builder((Context) getActivity())
                .setTitle(groupName)
                .setMessage(productName)
                .setIcon(dogeIcon)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                })
                .create();
    }
}
