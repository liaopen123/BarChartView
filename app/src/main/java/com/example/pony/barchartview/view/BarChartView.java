package com.example.pony.barchartview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;


/**
 * Created by Pony on 2016/12/4.
 */

public class BarChartView extends View {
    private static final String TAG = "BarChartView";
    public int mWidth;
    public int mHeight;
    public static int mPaddingRight = 30;
    public static int mPaddingLeft = 3*mPaddingRight;//坐标的左边距是以右边距为基准成倍数增加的
    public static int mPaddingBottom = 50;
    public Point mLocationPoint = new Point();//原点的坐标
    public Point mLocationYPoint = new Point();//Y端点的坐标
    public Point mLocationXPoint = new Point();//X断点的坐标
    public int mYBlankLength = 30;//Y轴顶端用于留白的长度
    public int mYLength;
    public int mXLength;
    public Paint mPaint = new Paint();
    public Paint mPathPaint = new Paint();
    public int increaseNumber = 1;
    public ArrayList<String> mYNameList =  new ArrayList();
    private ArrayList<String> mXNameList =  new ArrayList();
    private ArrayList<Double> mYValueList =  new ArrayList();
    private ArrayList<Double> mXValueList =  new ArrayList();
    private int currentHeight = -mPaddingBottom;
    public Path path;

    public BarChartView(Context context) {
        super(context);
        initXYName();
    }

    public BarChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initXYName();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mWidth = this.getWidth();
        mHeight = this.getHeight();
        //坐标原点:
        mLocationPoint.x = mPaddingLeft;
        mLocationPoint.y = -mPaddingBottom;
        //Y端点的坐标
        mLocationYPoint.x = mPaddingLeft;
        mLocationYPoint.y = -mHeight;
        //X端点的坐标
        mLocationXPoint.x = mWidth -mPaddingRight;
        mLocationXPoint.y = -mPaddingBottom;

        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(3);
        mPaint.setTextSize((int) (7f * getResources().getDisplayMetrics().scaledDensity + 0.5f));

        //X轴的长度，Y轴有用部分的长度
        mYLength = mHeight -mPaddingBottom - mYBlankLength;
        mXLength = mLocationXPoint.x - mPaddingLeft;

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    Log.d(TAG,"onDraw");
       // canvas.rotate(-90);
        mPaint.setColor(Color.parseColor("#c8c8c8"));
        canvas.translate(0,mHeight);

//画坐标线
        //x轴
        mPaint.setStrokeWidth(3);
        canvas.drawLine(mLocationPoint.x,mLocationPoint.y,mLocationXPoint.x,mLocationXPoint.y,mPaint);
        //y轴
        canvas.drawLine(mLocationPoint.x,mLocationPoint.y,mLocationYPoint.x,mLocationYPoint.y,mPaint);

        //绘制Y轴上的字
        int perYLength = mYLength / mYNameList.size();//Y轴上 每部分的长度  SIZE有可为为0  抛出异常 后期加判断
        for(int i=0;i<mYNameList.size();i++){
            mPaint.setTextAlign(Paint.Align.RIGHT);
            Rect rect = new Rect();
            mPaint.getTextBounds(mYNameList.get(i)+"  ", 0, mXNameList.get(i).length(), rect);
            int h = rect.height();
            //+(h/2)  为了居中 文字与虚线对其
        canvas.drawText(mYNameList.get(i)+"  ",mPaddingLeft,-((i+1)*perYLength+mPaddingBottom)+(h/2),mPaint);


            //画虚线
            path = new Path();
            path.moveTo(mPaddingLeft, -((i+1)*perYLength+mPaddingBottom));
            path.lineTo(mLocationXPoint.x, -((i+1)*perYLength+mPaddingBottom));
            mPathPaint.setStrokeWidth(3);
            mPathPaint.setColor(Color.parseColor("#c8c8c8"));
            mPathPaint.setPathEffect(new DashPathEffect(new float[]{5,5,5,5},1f));
            mPathPaint.setStyle(Paint.Style.STROKE);
            canvas.drawPath(path,mPathPaint);
        }


        //绘制X轴上的字
        int perXLength = mXLength / ((mXNameList.size()*2)+1);//Y轴上 每部分的长度  SIZE有可为为0  抛出异常 后期加判断
        for(int i=0;i<mXNameList.size();i++){
            mPaint.setTextAlign(Paint.Align.CENTER);
            Rect rect = new Rect();
            mPaint.getTextBounds(mXNameList.get(i), 0, mXNameList.get(i).length(), rect);
            int h = rect.height();
            canvas.drawText(mXNameList.get(i),((i+1)*2*perXLength)+mPaddingLeft, (float) (mLocationXPoint.y+(h*1.5)),mPaint);
        }


        //绘制柱状图 这里用drawLine来实现
        mPaint.setStrokeWidth(perXLength);
        mPaint.setColor(Color.parseColor("#ff9496"));
        for(int i=0;i<mXValueList.size();i++){

            if(mXValueList.get(i)>=mYValueList.get(0)){
                //6以上的值为均匀等分
                if(-((mXValueList.get(i)-6)*perYLength+perYLength)-mPaddingBottom<=currentHeight){
                    //还没有到达峰值
                    canvas.drawLine(((i+1)*2*perXLength)+mPaddingLeft,-mPaddingBottom,((i+1)*2*perXLength)+mPaddingLeft, (float) currentHeight,mPaint);
                    refreshData();
                }else{
                    canvas.drawLine(((i+1)*2*perXLength)+mPaddingLeft,-mPaddingBottom,((i+1)*2*perXLength)+mPaddingLeft, (float) -((mXValueList.get(i)-6)*perYLength+perYLength)-mPaddingBottom,mPaint);
                }

            }else{
                //6以下平分
                Log.d(TAG,"6以下");
                double percent = mXValueList.get(i) / mYValueList.get(0);//百分比  比如 值为4，而Y轴最小值为6，则用4/6 = 0.66666
                //竖线的话 X坐标值不用变，
                if(-(percent*perYLength)-getPaddingBottom()<=-currentHeight){
                    canvas.drawLine(((i + 1) * 2 * perXLength) + mPaddingLeft, -mPaddingBottom, ((i + 1) * 2 * perXLength) + mPaddingLeft, (float)currentHeight, mPaint);
                    refreshData();
                }else {
                    canvas.drawLine(((i + 1) * 2 * perXLength) + mPaddingLeft, -mPaddingBottom, ((i + 1) * 2 * perXLength) + mPaddingLeft, (float)-(percent*perYLength)-getPaddingBottom(), mPaint);
                }
            }

        }


//        canvas.drawCircle(0,0,100,mPaint);


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
        Log.d(TAG,"initXYName");
        mYNameList.add("6.0");
        mYNameList.add("7.0");
        mYNameList.add("8.0");
        mYNameList.add("9.0");
        mYNameList.add("10.0");

        mYValueList.add(0,6.0);
        mYValueList.add(1,7.0);
        mYValueList.add(2,8.0);
        mYValueList.add(3,9.0);
        mYValueList.add(4,10.0);


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
        refreshData();
    }



    public void refreshData(){

        postDelayed(new Runnable() {
            @Override
            public void run() {
                currentHeight +=(-increaseNumber);
            }
        },6);
        invalidate();

    }
}
