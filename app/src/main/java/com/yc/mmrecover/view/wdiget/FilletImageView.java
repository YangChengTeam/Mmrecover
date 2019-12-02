package com.yc.mmrecover.view.wdiget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

import com.yc.mmrecover.R;


public class FilletImageView extends AppCompatImageView {
    private Context mContext;
    private float mRoundRadius;

    public FilletImageView(Context context) {
        super(context);
    }

    public FilletImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mContext = context;
//        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.FilletImageView);
//        this.mRoundRadius = obtainStyledAttributes.getDimension(0, 0.0f);
//        obtainStyledAttributes.recycle();
    }

    public FilletImageView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();
        if (drawable != null && getWidth() != 0 && getHeight() != 0) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            if (bitmap != null) {
                bitmap = bitmap.copy(Config.ARGB_8888, true);
                int width = getWidth();
                getHeight();
                if (this.mRoundRadius == 0.0f) {
                    this.mRoundRadius = (float) ((width * 175) / 1000);
                }
                canvas.drawBitmap(getRoundedCornerBitmap(bitmap, this.mRoundRadius, width), 0.0f, 0.0f, null);
            }
        }
    }

    private Bitmap getRoundedCornerBitmap(Bitmap bitmap, float f, int i) {
        bitmap = Bitmap.createScaledBitmap(bitmap, i, i, false);
        Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.parseColor("#BAB399"));
        canvas.drawRoundRect(rectF, f, f, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return createBitmap;
    }
}
