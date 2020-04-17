package com.example.myfirstapp;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class ImageHandler {

    public static Bitmap joinImages(Bitmap bmp1, Bitmap bmp2)
    {
        if (bmp1 == null || bmp2 == null)
            return bmp1;
        int height = bmp1.getHeight();
        if (height < bmp2.getHeight())
            height = bmp2.getHeight();

        Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth() + bmp2.getWidth(), height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bmp1, 0, 0, null);
        canvas.drawBitmap(bmp2, bmp1.getWidth(), 0, null);
        return bmOverlay;
    }
}
