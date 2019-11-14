package com.yc.mmrecover.view.wdiget;

import androidx.appcompat.widget.AppCompatImageView;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnTouchListener;
import android.widget.ImageView.ScaleType;


public class GestureImageView extends AppCompatImageView {
    static final int CLICK = 3;
    static final int DRAG = 1;
    static final int NONE = 0;
    static final int ZOOM = 2;
    Context context;
    PointF last = new PointF();
    float[] m;
    ScaleGestureDetector mScaleDetector;
    Matrix matrix;
    float maxScale = 3.0f;
    float minScale = 1.0f;
    int mode = 0;
    int oldMeasuredHeight;
    int oldMeasuredWidth;
    protected float origHeight;
    protected float origWidth;
    float saveScale = 1.0f;
    PointF start = new PointF();
    int viewHeight;
    int viewWidth;

    private class ScaleListener extends SimpleOnScaleGestureListener {
        private ScaleListener() {
        }

        ScaleListener(GestureImageView gestureImageView, Object anonymousClass1) {
            this();
        }

        public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
            GestureImageView.this.mode = 2;
            return true;
        }

        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            float scaleFactor = scaleGestureDetector.getScaleFactor();
            float f = GestureImageView.this.saveScale;
            GestureImageView gestureImageView = GestureImageView.this;
            gestureImageView.saveScale *= scaleFactor;
            if (GestureImageView.this.saveScale > GestureImageView.this.maxScale) {
                GestureImageView.this.saveScale = GestureImageView.this.maxScale;
                scaleFactor = GestureImageView.this.maxScale / f;
            } else if (GestureImageView.this.saveScale < GestureImageView.this.minScale) {
                GestureImageView.this.saveScale = GestureImageView.this.minScale;
                scaleFactor = GestureImageView.this.minScale / f;
            }
            if (GestureImageView.this.origWidth * GestureImageView.this.saveScale <= ((float) GestureImageView.this.viewWidth) || GestureImageView.this.origHeight * GestureImageView.this.saveScale <= ((float) GestureImageView.this.viewHeight)) {
                GestureImageView.this.matrix.postScale(scaleFactor, scaleFactor, (float) (GestureImageView.this.viewWidth / 2), (float) (GestureImageView.this.viewHeight / 2));
            } else {
                GestureImageView.this.matrix.postScale(scaleFactor, scaleFactor, scaleGestureDetector.getFocusX(), scaleGestureDetector.getFocusY());
            }
            GestureImageView.this.fixTrans();
            return true;
        }
    }

    float getFixDragTrans(float f, float f2, float f3) {
        return f3 <= f2 ? 0.0f : f;
    }

    float getFixTrans(float f, float f2, float f3) {
        if (f3 <= f2) {
            f3 = f2 - f3;
            f2 = 0.0f;
        } else {
            f2 -= f3;
            f3 = 0.0f;
        }
        return f < f2 ? (-f) + f2 : f > f3 ? (-f) + f3 : 0.0f;
    }

    public GestureImageView(Context context) {
        super(context);
        sharedConstructing(context);
    }

    public GestureImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        sharedConstructing(context);
    }

    private void sharedConstructing(Context context) {
        super.setClickable(true);
        this.context = context;
        this.mScaleDetector = new ScaleGestureDetector(context, new ScaleListener(this, null));
        this.matrix = new Matrix();
        this.m = new float[9];
        setImageMatrix(this.matrix);
        setScaleType(ScaleType.MATRIX);
        setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                GestureImageView.this.mScaleDetector.onTouchEvent(motionEvent);
                PointF pointF = new PointF(motionEvent.getX(), motionEvent.getY());
                int action = motionEvent.getAction();
                if (action != 6) {
                    switch (action) {
                        case 0:
                            GestureImageView.this.last.set(pointF);
                            GestureImageView.this.start.set(GestureImageView.this.last);
                            GestureImageView.this.mode = 1;
                            break;
                        case 1:
                            GestureImageView.this.mode = 0;
                            action = (int) Math.abs(pointF.x - GestureImageView.this.start.x);
                            int abs = (int) Math.abs(pointF.y - GestureImageView.this.start.y);
                            if (action < 3 && abs < 3) {
                                GestureImageView.this.performClick();
                                break;
                            }
                        case 2:
                            if (GestureImageView.this.mode == 1) {
                                float f = pointF.y - GestureImageView.this.last.y;
                                GestureImageView.this.matrix.postTranslate(GestureImageView.this.getFixDragTrans(pointF.x - GestureImageView.this.last.x, (float) GestureImageView.this.viewWidth, GestureImageView.this.origWidth * GestureImageView.this.saveScale), GestureImageView.this.getFixDragTrans(f, (float) GestureImageView.this.viewHeight, GestureImageView.this.origHeight * GestureImageView.this.saveScale));
                                GestureImageView.this.fixTrans();
                                GestureImageView.this.last.set(pointF.x, pointF.y);
                                break;
                            }
                            break;
                    }
                }
                GestureImageView.this.mode = 0;
                GestureImageView.this.setImageMatrix(GestureImageView.this.matrix);
                GestureImageView.this.invalidate();
                return true;
            }
        });
    }

    public void setMaxZoom(float f) {
        this.maxScale = f;
    }

    void fixTrans() {
        this.matrix.getValues(this.m);
        float f = this.m[2];
        float f2 = this.m[5];
        f = getFixTrans(f, (float) this.viewWidth, this.origWidth * this.saveScale);
        f2 = getFixTrans(f2, (float) this.viewHeight, this.origHeight * this.saveScale);
        if (f != 0.0f || f2 != 0.0f) {
            this.matrix.postTranslate(f, f2);
        }
    }

    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        this.viewWidth = MeasureSpec.getSize(i);
        this.viewHeight = MeasureSpec.getSize(i2);
        if ((this.oldMeasuredHeight != this.viewWidth || this.oldMeasuredHeight != this.viewHeight) && this.viewWidth != 0 && this.viewHeight != 0) {
            this.oldMeasuredHeight = this.viewHeight;
            this.oldMeasuredWidth = this.viewWidth;
            if (this.saveScale == 1.0f) {
                Drawable drawable = getDrawable();
                if (drawable != null && drawable.getIntrinsicWidth() != 0 && drawable.getIntrinsicHeight() != 0) {
                    i2 = drawable.getIntrinsicWidth();
                    i = drawable.getIntrinsicHeight();
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("bmWidth: ");
                    stringBuilder.append(i2);
                    stringBuilder.append(" bmHeight : ");
                    stringBuilder.append(i);
                    Log.d("bmSize", stringBuilder.toString());
                    float f = (float) i2;
                    float f2 = (float) i;
                    float min = Math.min(((float) this.viewWidth) / f, ((float) this.viewHeight) / f2);
                    this.matrix.setScale(min, min);
                    float f3 = (((float) this.viewHeight) - (f2 * min)) / 2.0f;
                    f2 = (((float) this.viewWidth) - (min * f)) / 2.0f;
                    this.matrix.postTranslate(f2, f3);
                    this.origWidth = ((float) this.viewWidth) - (f2 * 2.0f);
                    this.origHeight = ((float) this.viewHeight) - (f3 * 2.0f);
                    setImageMatrix(this.matrix);
                } else {
                    return;
                }
            }
            fixTrans();
        }
    }

}
