package me.groovie.groovieapp.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by yuweixu on 2015-03-28.
 */
public class FixedImageView extends ImageView {
    /**
     * @param context
     */
    public FixedImageView(Context context)
    {
        super(context);
        // TODO Auto-generated constructor stub
        setBackgroundColor(0xFFFFFF);
    }

    /**
     * @param context
     * @param attrs
     */
    public FixedImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    public FixedImageView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }

    @Override protected void onMeasure(int widthMeasureSpec,
                                       int heightMeasureSpec) {
//   let the default measuring occur, then force the desired aspect ratio
//   on the view (not the drawable).
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
//force a 4:3 aspect ratio
        int height = Math.round(width * .57f);
        setMeasuredDimension(width, height);
    }
}
