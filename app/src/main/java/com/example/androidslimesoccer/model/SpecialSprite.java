package com.example.androidslimesoccer.model;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.example.androidslimesoccer.R;
import com.example.androidslimesoccer.utils.SlimeType;
import com.example.androidslimesoccer.utils.Utils;

import java.util.Random;

public class SpecialSprite {
    public SlimeType slimeType;
    public Bitmap specialImage1;
    public Bitmap specialImage2;
    public Resources resources;
    public int x, y, x2, y2;
    public boolean isOnDraw = true;

    public SpecialSprite(SlimeType slimeType, Resources resources) {
        this.slimeType = slimeType;
        this.resources = resources;
        Bitmap temp;
        if (slimeType == SlimeType.INDIAN) {
            temp = BitmapFactory.decodeResource(resources, R.drawable.raincloud);
            specialImage1 = getResizedBitmap(temp, (int)(Utils.assetsYScale * temp.getWidth()),
                    (int)(Utils.assetsYScale * temp.getHeight()));
            temp = BitmapFactory.decodeResource(resources, R.drawable.raindrop);
            specialImage2 = getResizedBitmap(temp, (int)(Utils.assetsYScale * temp.getWidth()),
                    (int)(Utils.assetsYScale * temp.getHeight()));
        } else if (slimeType == SlimeType.TRAFFIC) {
            temp = BitmapFactory.decodeResource(resources, R.drawable.stopsign);
            specialImage1 = getResizedBitmap(temp, (int)(Utils.assetsYScale * temp.getWidth()),
                    (int)(Utils.assetsYScale * temp.getHeight()));
        } else if (slimeType == SlimeType.ALIEN) {
            temp = BitmapFactory.decodeResource(resources, R.drawable.alien_fade1);
            specialImage1 = getResizedBitmap(temp, (int)(Utils.assetsYScale * temp.getWidth()),
                    (int)(Utils.assetsYScale * temp.getHeight()));
            temp = BitmapFactory.decodeResource(resources, R.drawable.alien_fade2);
            specialImage2 = getResizedBitmap(temp, (int)(Utils.assetsYScale * temp.getWidth()),
                    (int)(Utils.assetsYScale * temp.getHeight()));
        }
    }

    public void draw(Canvas canvas) {
        if (isOnDraw) {
            if (slimeType == SlimeType.INDIAN) {
                canvas.drawBitmap(specialImage1, x, y, null);
                for (int i = 0 ; i < 5; i++) {
                    Random random = new Random();
                    x2 = random.nextInt(specialImage1.getWidth()) + x;
                    y2 = random.nextInt(specialImage1.getHeight()) + y + specialImage1.getHeight();
                    canvas.drawBitmap(specialImage2, x2, y2, null);
                }
            } else if (slimeType == SlimeType.TRAFFIC) {
                canvas.drawBitmap(specialImage1, x, y, null);
            } else if (slimeType == SlimeType.ALIEN) {
                canvas.drawBitmap(specialImage1, x, y, null);
            }
        }
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        return Bitmap.createScaledBitmap(bm, newWidth, newHeight, true);
    }
}
