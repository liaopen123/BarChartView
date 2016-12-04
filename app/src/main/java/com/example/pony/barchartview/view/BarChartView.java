package com.example.pony.barchartview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;


/**
 * Created by Pony on 2016/12/4.
 */

public class BarChartView extends View {
    public int mWidth;
    public int mHeight;
    public static int mPaddingRight = 30;
    public static int mPaddingLeft = 2*mPaddingRight;//坐标的左边距是以右边距为基准成倍数增加的
    public static int mPaddingBottom = 30;
    public Point mLocationPoint = new Point();//原点的坐标
    public Point mLocationYPoint = new Point();//Y端点的坐标
    public Point mLocationXPoint = new Point();//X断点的坐标
    public int mYBlankLength = 10;//Y轴顶端用于留白的长度
    public int mYLength;
    public int mXLength;
    public Paint mPaint = new Paint();

    public ArrayList<String> mYNameList =  new ArrayList();
    private ArrayList<String> mXNameList =  new ArrayList();
    private ArrayList<Double> mXValueList =  new ArrayList();

    public BarChartView(Context context) {
        super(context);
    }

    public BarChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mWidth = this.getWidth();
        mHeight = this.getHeight();
        //坐标原点:
        mLocationPoint.x = mPaddingLeft;
        mLocationPoint.y = mHeight-mPaddingBottom;
        //Y端点的坐标
        mLocationYPoint.x = mPaddingLeft;
        mLocationYPoint.y = 0;
        //X端点的坐标
        mLocationXPoint.x = mWidth -mPaddingRight;
        mLocationXPoint.y = mHeight - mPaddingBottom;

        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(3);

        //X轴的长度，Y轴有用部分的长度
        mYLength = mLocationPoint.y - mYBlankLength;
        mXLength = mLocationXPoint.x;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//画坐标线
        //x轴
        canvas.drawLine(mLocationPoint.x,mLocationPoint.y,mLocationXPoint.x,mLocationXPoint.y,mPaint);
        //y轴
        canvas.drawLine(mLocationPoint.x,mLocationPoint.y,mLocationYPoint.x,mLocationYPoint.y,mPaint);



    }

    /**
     * 暴露给外界   给柱状图设置数据
     * @param mYNameList  Y轴几个坐标的名称
     * @param mXNameList x轴几个坐标的名称
     * @param mXValueList x轴几个坐标对应的值
     */
    public void setDate(ArrayList<String> mYNameList,ArrayList<String> mXNameList,ArrayList<Double> mXValueList){
        this.mYNameList = mYNameList;
        this.mXNameList = mXNameList;
        this.mXValueList = mXValueList;
    }

    /**
     * 用于在view内设置数据
     */
    public void setDate(){
       initXYName();
        this.mXNameList = mXNameList;
        this.mXValueList = mXValueList;
    }

    private void initXYName() {
        mYNameList.add("6.0");
        mYNameList.add("7.0");
        mYNameList.add("8.0");
        mYNameList.add("9.0");
        mYNameList.add("10.0");

        mXNameList.add("30");
        mXNameList.add("60");
        mXNameList.add("90");
        mXNameList.add("120");
        mXNameList.add("150");
        mXNameList.add("180");
        mXNameList.add("210");
        mXNameList.add("240");
        mXNameList.add("270");
        mXNameList.add("300");
        mXNameList.add("330");
        mXNameList.add("360");

        mXValueList.add(6.0);
        mXValueList.add(6.5);
        mXValueList.add(7.0);
        mXValueList.add(7.5);
        mXValueList.add(8.0);
        mXValueList.add(8.5);
        mXValueList.add(9.0);
        mXValueList.add(10.0);
        mXValueList.add(10.0);
        mXValueList.add(10.0);
        mXValueList.add(10.0);
        mXValueList.add(10.0);
    }
}
