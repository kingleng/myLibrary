package com.example.leng.myapplication.view.myView;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by leng on 2017/1/23.
 */

@SuppressWarnings("JavadocReference")
public class MyImageView extends ImageView {

    /**
     * 图片最大放大比例
     */
    public static final float MAX_SCALE = 4f;

    /**
     * 手势状态：自由状态
     */
    public static final int PINCH_MODE_FREE = 0;

    /**
     * 手势状态：拖动状态
     */
    public static final int PINCH_MODE_SCROLL = 1;

    /**
     * 手势状态：缩放状态
     */
    public static final int PINCH_MODE_SCALE = 2;

    /**
     * 当前的手势状态
     */
    private int mPinchMode = PINCH_MODE_FREE;

    /**
     * 外层变换矩阵。如果是单位矩阵，那么图片是fit center状态
     */
    private Matrix mOuterMatrix = new Matrix();

    /**
     * 获取外层变换矩阵
     *
     * 外层变换矩阵记录了图片的手势操作的最终结果，相对于图片fit center的状态的变换
     * @param matrix
     * @return
     */
    private Matrix getmOuterMatrix(Matrix matrix){
        if(matrix == null){
            matrix = new Matrix(mOuterMatrix);
        }else{
            matrix.set(mOuterMatrix);
        }
        return matrix;
    }

    /**
     * 获取内部变换矩阵
     *
     * 内部变换矩阵是原图到fit center的状态变换，当原图尺寸变化和控件大小变化都会发生变化
     * 当尚未布局或原图不存在时，其值无意义，所以在调用前必须确保前置条件的有效，否则影响计算结果。
     * @param matrix
     * @return
     */
    private Matrix getInnerMatrix(Matrix matrix){
        if(matrix==null) {
            matrix = new Matrix();
        }else{
            matrix.reset();
        }
        if(isReady()){
            //原图大小
            RectF tempSrc = MathUtils.rectFTake(0,0,getDrawable().getIntrinsicWidth(),getDrawable().getIntrinsicHeight());
            //控件大小
            RectF tempDst = MathUtils.rectFTake(0,0,getWidth(),getHeight());
            //计算fit center矩阵
            matrix.setRectToRect(tempSrc,tempDst, Matrix.ScaleToFit.CENTER);
            //释放临时对象
            MathUtils.rectFGiven(tempSrc);
            MathUtils.rectFGiven(tempDst);
        }
        return matrix;
    }

    /**
     * 获取图片总变换矩阵
     * 总变化矩阵等于内部变换矩阵x外部变换矩阵，决定了从原图到所见的最终状态变换。
     * 当尚未布局或原图不存在时，其值无意义。所以在调用前必须确保前置条件的有效，以免影响计算结果
     * @param matrix
     * @return
     */
    private Matrix getCurrentImageMatrix(Matrix matrix){
        matrix = getInnerMatrix(matrix);
        matrix.postConcat(mOuterMatrix);
        return matrix;
    }

    /**
     * 获取当前变换后的图片的位置和尺寸
     * 当尚未布局或原图不存在时，其值无意义。所以在调用前必须确保前置条件的有效，以免影响计算结果
     * @param rectF
     * @return
     */
    private RectF getImageBound(RectF rectF){
        if(rectF==null){
            rectF = new RectF();
        }else{
            rectF.setEmpty();
        }

        if(isReady()){
            //获取一个空的matrix
            Matrix matrix = MathUtils.matrixTake();
            //获取当前总的变换矩阵
            getCurrentImageMatrix(matrix);
            //对原图矩形变换得到当前显示矩形
            rectF.set(0,0,getDrawable().getIntrinsicWidth(),getDrawable().getIntrinsicHeight());
            matrix.mapRect(rectF);
            //释放临时对象
            MathUtils.matrixGiven(matrix);
        }
        return rectF;
    }

    /**
     * 获取当前手势状态
     * @see PINCH_MODE_FREE
     * @see PINCH_MODE_SCALE
     * @see PINCH_MODE_SCROLL
     * @return
     */
    private int getmPinchMode(){
        return mPinchMode;
    }

    /**
     * 获取图片最大可放大比例
     * @return
     */
    private float getMaxScale(){
        return MAX_SCALE;
    }

    public MyImageView(Context context) {
        super(context);
        initView();
    }

    public MyImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MyImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView(){
        //强制设置图片scaleType为matrix
        super.setScaleType(ScaleType.MATRIX);
    }

    //不允许设置scaleType，只能用内部设置的matrix
    @Override
    public void setScaleType(ScaleType scaleType) {
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(isReady()){
            Matrix matrix = MathUtils.matrixTake();
            setImageMatrix(getCurrentImageMatrix(matrix));
            MathUtils.matrixGiven(matrix);
        }

        super.onDraw(canvas);

    }

    /**
     * 判断当前能否进行手势计算
     * 包括是否有图片，图片是否有尺寸，控件是否有尺寸
     *
     * @return
     */
    private boolean isReady() {
        return getDrawable() != null && getDrawable().getIntrinsicWidth() > 0 && getDrawable().getIntrinsicHeight() > 0
                && getWidth() > 0 && getHeight() > 0;
    }

    /**
     * 单指模式下
     * 记录上一次手指的位置，用于计算新的位置和上一次位置的差值
     *
     * 双指模式下
     * 记录两个手指的中点，作为和mScaleCenter绑定的点
     */
    private PointF mLastMovePoint = new PointF();

    /**
     * 缩放模式下图片的中点
     */
    private PointF mScaleCenter = new PointF();

    /**
     * 缩放模式下的基础缩放比例
     * <p>
     * 为外层缩放值除以开始缩放时两指距离.
     * 其值乘上最新的两指之间距离为最新的图片缩放比例.
     */
    private float mScaleBase = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        int action = event.getAction() & MotionEvent.ACTION_MASK;
        switch (action) {
            //最后一个点抬起或者取消，结束所有模式
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                //如果是缩放，进行缩放结束修正
                if(mPinchMode == PINCH_MODE_SCALE) {
                    scaleEnd();
                }
                mPinchMode = PINCH_MODE_FREE;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                //多个手指情况下抬起一个手指,此时需要是缩放模式才触发
                if (mPinchMode == PINCH_MODE_SCALE) {
                    //抬起的点如果大于2，那么缩放模式还有效，但是有可能初始点变了，重新测量初始点
                    if(event.getPointerCount()>2){
                        //如果还没结束缩放模式，但是第一个点抬起了，那么让第二个点和第三个点作为缩放控制点
                        if (event.getAction() >> 8 == 0) {
                            saveScaleContext(event.getX(1), event.getY(1), event.getX(2), event.getY(2));
                            //如果还没结束缩放模式，但是第二个点抬起了，那么让第一个点和第三个点作为缩放控制点
                        } else if (event.getAction() >> 8 == 1) {
                            saveScaleContext(event.getX(0), event.getY(0), event.getX(2), event.getY(2));
                        }
                    }
                    //如果抬起的点等于2,那么此时只剩下一个点,也不允许进入单指模式,因为此时可能图片没有在正确的位置上
                }
                break;
            case MotionEvent.ACTION_DOWN:
                //切换到滚动模式
                mPinchMode = PINCH_MODE_SCROLL;
                //保存触发点用于move计算差值
                mLastMovePoint.set(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                //切换到缩放模式
                mPinchMode = PINCH_MODE_SCALE;
                //保存缩放的两个手指
                saveScaleContext(event.getX(0), event.getY(0), event.getX(1), event.getY(1));
                break;
            case MotionEvent.ACTION_MOVE:
                if (mPinchMode == PINCH_MODE_SCROLL) {
                    //每次移动产生一个差值累积到图片位置上
                    scrollBy(event.getX() - mLastMovePoint.x, event.getY() - mLastMovePoint.y);
                    //记录新的移动点
                    mLastMovePoint.set(event.getX(), event.getY());
                } else if (mPinchMode == PINCH_MODE_SCALE  && event.getPointerCount() > 1) {
                    //两个缩放点间的距离
                    float distance = MathUtils.getDistance(event.getX(0), event.getY(0), event.getX(1), event.getY(1));
                    //保存缩放点中点
                    float[] lineCenter = MathUtils.getCenterPoint(event.getX(0), event.getY(0), event.getX(1), event.getY(1));
                    mLastMovePoint.set(lineCenter[0], lineCenter[1]);
                    //处理缩放
                    scale(mScaleCenter, mScaleBase, distance, mLastMovePoint);
                }
                break;
        }

        mGestureDetector.onTouchEvent(event);
        return true;
    }

    private GestureDetector mGestureDetector = new GestureDetector(MyImageView.this.getContext(), new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return super.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if(mPinchMode == PINCH_MODE_SCROLL && !(mScaleAnimator != null && mScaleAnimator.isRunning())){
                doubleTap(e.getX(),e.getY());
            }
            return true;
        }
    });

    /**
     *让图片移动一段距离
     * <p>
     * 不能移动超过可移动范围,超过了就到可移动范围边界为止.
     *
     * @param xDiff
     * @param yDiff
     * @return 是否改变了位置
     */
    private Boolean scrollBy(float xDiff,float yDiff){
        if(!isReady()){
            return false;
        }

        //获取原图方框
        RectF bound = MathUtils.rectFTake();
        getImageBound(bound);
        //控件大小
        float displayWidth = getWidth();
        float displayHeight = getHeight();
        //如果当前图片宽度小于控件宽度，则不能移动
        if (bound.right - bound.left < displayWidth) {
            xDiff = 0;
            //如果图片左边在移动后超出控件左边
        } else if (bound.left + xDiff > 0) {
            //如果在移动之前是没超出的，计算应该移动的距离
            if (bound.left < 0) {
                xDiff = -bound.left;
                //否则无法移动
            } else {
                xDiff = 0;
            }
            //如果图片右边在移动后超出控件右边
        } else if (bound.right + xDiff < displayWidth) {
            //如果在移动之前是没超出的，计算应该移动的距离
            if (bound.right > displayWidth) {
                xDiff = displayWidth - bound.right;
                //否则无法移动
            } else {
                xDiff = 0;
            }
        }
        //以下同理
        if (bound.bottom - bound.top < displayHeight) {
            yDiff = 0;
        } else if (bound.top + yDiff > 0) {
            if (bound.top < 0) {
                yDiff = -bound.top;
            } else {
                yDiff = 0;
            }
        } else if (bound.bottom + yDiff < displayHeight) {
            if (bound.bottom > displayHeight) {
                yDiff = displayHeight - bound.bottom;
            } else {
                yDiff = 0;
            }
        }
        MathUtils.rectFGiven(bound);
        //应用移动变换
        mOuterMatrix.postTranslate(xDiff, yDiff);
        //触发重绘
        invalidate();
        //检查是否有变化
        if (xDiff != 0 || yDiff != 0) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * 对图片按照一些手势信息进行缩放
     *
     * @param scaleCenter mScaleCenter
     * @param scaleBase mScaleBase
     * @param distance 手指两点之间距离
     * @param lineCenter 手指两点之间中点
     */
    private void scale(PointF scaleCenter, float scaleBase, float distance, PointF lineCenter){
        if(!isReady()){
            return;
        }
        //计算图片从fit center状态到目标状态的缩放比例
        float scale = scaleBase * distance;
        Matrix matrix = MathUtils.matrixTake();
        //按照图片缩放中心缩放，并且让缩放中心在缩放点中点上
        matrix.postScale(scale, scale, scaleCenter.x, scaleCenter.y);
        //让图片的缩放中点跟随手指缩放中点
        matrix.postTranslate(lineCenter.x - scaleCenter.x, lineCenter.y - scaleCenter.y);
        //应用变换
        mOuterMatrix.set(matrix);
        MathUtils.matrixGiven(matrix);
        //重绘
        invalidate();
    }

    /**
     * 当缩放操作结束修正
     * <p>
     * 如果图片超过边界,找到最近的位置恢复.
     * 如果图片缩放尺寸超过最大值或者最小值,找到最近的值恢复.
     */
    private void scaleEnd(){
        if(!isReady()){
            return;
        }

        boolean change = false;
        //获取图片整体变换矩阵
        Matrix currentMatrix = MathUtils.matrixTake();
        getCurrentImageMatrix(currentMatrix);

        //获取整体缩放比例
        float currentScale = MathUtils.getMatrixScale(currentMatrix)[0];
        //获取外层缩放比例
        float outerScale = MathUtils.getMatrixScale(mOuterMatrix)[0];
        //控件大小
        float displayWidth = getWidth();
        float displayHeight = getHeight();
        //最大缩放比例
        float maxScale = getMaxScale();
        //修正比例
        float scalePost = 1.0f;
        //修正位置
        float postX = 0;
        float postY = 0;
        //如果整体缩放比例大于最大比例，进行缩放修正
        if(currentScale > maxScale){
            scalePost = maxScale / currentScale;
        }
        float asd = scalePost * currentScale;
        Log.e("scalePost","scalePost!!"+asd);
        //缩放修正后整体导致第二层缩放小于1（就是图片比fit center状态还小），重新修正
        if(outerScale*scalePost<1f){
            scalePost = 1f/outerScale;
        }
        //如果缩放不为1，说明进行了缩放
        if(scalePost != 1f){
            change = true;
        }

        //尝试根据缩放点进行缩放修正
        Matrix testMatrix = MathUtils.matrixTake(currentMatrix);
        testMatrix.postScale(scalePost,scalePost,mLastMovePoint.x,mLastMovePoint.y);
        RectF testBound = MathUtils.rectFTake(0,0,getDrawable().getIntrinsicWidth(),getDrawable().getIntrinsicHeight());
        testMatrix.mapRect(testBound);
        //检测缩放修正后位置有无超出，如果超出进行位置修正
        if (testBound.right - testBound.left < displayWidth) {
            postX = displayWidth / 2f - (testBound.right + testBound.left) / 2f;
        } else if (testBound.left > 0) {
            postX = -testBound.left;
        } else if (testBound.right < displayWidth) {
            postX = displayWidth - testBound.right;
        }
        if (testBound.bottom - testBound.top < displayHeight) {
            postY = displayHeight / 2f - (testBound.bottom + testBound.top) / 2f;
        } else if (testBound.top > 0) {
            postY = -testBound.top;
        } else if (testBound.bottom < displayHeight) {
            postY = displayHeight - testBound.bottom;
        }
        //如果位置修正不为0，说明进行了修正
        if (postX != 0 || postY != 0) {
            change = true;
        }

        if(change){
            Matrix endMatrix = MathUtils.matrixTake(mOuterMatrix);
            endMatrix.postScale(scalePost,scalePost,mLastMovePoint.x,mLastMovePoint.y);
            endMatrix.postTranslate(postX,postY);
            //清理当前可能正在执行的动画
            cancelAllAnimator();
            //启动矩阵动画
            mScaleAnimator = new ScaleAnimator(mOuterMatrix, endMatrix);
            mScaleAnimator.start();

            MathUtils.matrixGiven(endMatrix);
        }

        MathUtils.matrixGiven(currentMatrix);
        MathUtils.matrixGiven(testMatrix);
        MathUtils.rectFGiven(testBound);

    }

    private ScaleAnimator mScaleAnimator;
    /**
     * 停止所有手势动画
     */
    private void cancelAllAnimator() {
        if (mScaleAnimator != null) {
            mScaleAnimator.cancel();
            mScaleAnimator = null;
        }
    }

    /**
     * 缩放动画
     * <p>
     * 在给定时间内从一个矩阵的变化逐渐动画到另一个矩阵的变化
     */
    private class ScaleAnimator extends ValueAnimator implements ValueAnimator.AnimatorUpdateListener {

        /**
         * 开始矩阵
         */
        private float[] mStart = new float[9];

        /**
         * 结束矩阵
         */
        private float[] mEnd = new float[9];

        /**
         * 中间结果矩阵
         */
        private float[] mResult = new float[9];

        /**
         * 构建一个缩放动画
         * <p>
         * 从一个矩阵变换到另外一个矩阵
         *
         * @param start 开始矩阵
         * @param end   结束矩阵
         */
        public ScaleAnimator(Matrix start, Matrix end) {
            this(start, end, 200);
        }

        /**
         * 构建一个缩放动画
         * <p>
         * 从一个矩阵变换到另外一个矩阵
         *
         * @param start    开始矩阵
         * @param end      结束矩阵
         * @param duration 动画时间
         */
        public ScaleAnimator(Matrix start, Matrix end, long duration) {
            super();
            setFloatValues(0, 1f);
            setDuration(duration);
            addUpdateListener(this);
            start.getValues(mStart);
            end.getValues(mEnd);
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            //获取动画进度
            float value = (Float) animation.getAnimatedValue();
            //根据动画进度计算矩阵中间插值
            for (int i = 0; i < 9; i++) {
                mResult[i] = mStart[i] + (mEnd[i] - mStart[i]) * value;
            }
            //设置矩阵并重绘
            mOuterMatrix.setValues(mResult);
            invalidate();
        }
    }

    /**
     * 记录缩放前的一些信息
     * <p>
     * 保存基础缩放值.
     * 保存图片缩放中点.
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     */
    private void saveScaleContext(float x1,float y1,float x2,float y2){
        //记录基础缩放值,其中图片缩放比例按照x方向来计算
        //理论上图片应该是等比的,x和y方向比例相同
        //但是有可能外部设定了不规范的值.
        //但是后续的scale操作会将xy不等的缩放值纠正,改成和x方向相同
        mScaleBase = MathUtils.getMatrixScale(mOuterMatrix)[0] / MathUtils.getDistance(x1,y1,x2,y2);
        //两手指的中点在屏幕上落在了图片的某个点上,图片上的这个点在经过总矩阵变换后和手指中点相同
        //现在我们需要得到图片上这个点在图片是fit center状态下在屏幕上的位置
        //因为后续的计算都是基于图片是fit center状态下进行变换
        //所以需要把两手指中点除以外层变换矩阵得到mScaleCenter
        float[] center = MathUtils.inverseMatrixPoint(MathUtils.getCenterPoint(x1, y1, x2, y2),mOuterMatrix);
        mScaleCenter.set(center[0],center[1]);
    }

    /**
     * 双击缩放
     * @param x
     * @param y
     */
    private void doubleTap(float x,float y){
        if(!isReady()){
            return;
        }

        //获取第一层变换矩阵
        Matrix innerMatrix = MathUtils.matrixTake();
        getInnerMatrix(innerMatrix);
        //获取总的缩放比例
        float innerScale = MathUtils.getMatrixScale(innerMatrix)[0];
        float outerScale = MathUtils.getMatrixScale(mOuterMatrix)[0];
        float currentScale = innerScale * outerScale;

        float maxScale = getMaxScale();
        float nextScale = 1f;
        //获取控件宽高
        float width = getWidth();
        float height = getHeight();

        if(currentScale<maxScale){
            nextScale = maxScale;
        }else{
            nextScale = innerScale;
        }

        Matrix animEnd = MathUtils.matrixTake(mOuterMatrix);
        animEnd.postScale(nextScale/currentScale,nextScale/currentScale,x,y);
        //将放大点移动到控件中心
        animEnd.preTranslate(width/2f-x,height/2f-y);
        //得到放大之后的图片方框
        Matrix testMatrix = MathUtils.matrixTake(innerMatrix);
        testMatrix.postConcat(animEnd);
        RectF testBound = MathUtils.rectFTake(0,0,getDrawable().getIntrinsicWidth(),getDrawable().getIntrinsicHeight());
        testMatrix.mapRect(testBound);
        //修正位置
        float postX = 0;
        float postY = 0;
        if(testBound.right-testBound.left < width){
            postX = width/2f - (testBound.right+testBound.left)/2f;
        }else if(testBound.left>0){
            postX = -testBound.left;
        }else if(testBound.right<width){
            postX = width-testBound.right;
        }
        if(testBound.bottom-testBound.top<height){
            postY = height/2f - (testBound.top+testBound.bottom)/2f;
        }else if(testBound.top>0){
            postY = -testBound.top;
        }else if(testBound.bottom<height){
            postY = height-testBound.bottom;
        }

        animEnd.postTranslate(postX,postY);

        cancelAllAnimator();

        mScaleAnimator = new ScaleAnimator(mOuterMatrix,animEnd);
        mScaleAnimator.start();

        MathUtils.matrixGiven(innerMatrix);
        MathUtils.matrixGiven(animEnd);
        MathUtils.matrixGiven(testMatrix);
        MathUtils.rectFGiven(testBound);
    }

    /**
     * 对象池
     */
    private static abstract class ObjectsPool<T> {
        /**
         * 对象池最大容量
         */
        private int mSize;

        /**
         * 对象池队列
         */
        private Queue<T> mQueue;

        /**
         * 创建一个对象池
         *
         * @param size 对象池的最大容量
         */
        public ObjectsPool(int size) {
            this.mSize = size;
            mQueue = new LinkedList<T>();
        }

        /**
         * 获取一个空闲的对象
         *
         * @return
         */
        public T take() {
            //如果池内为空就创建一个
            if (mQueue.size() == 0) {
                return newInstance();
            } else {
                //对象池里有就从顶端拿出来一个返回
                return resetInstance(mQueue.poll());
            }
        }

        /**
         * 归还对象池内申请的对象
         * <p>
         * 如果归还的对象数量超过对象池容量,那么归还的对象就会被丢弃.
         */
        public void give(T obj) {
            //如果对象池还有空位子就归还对象
            if (obj != null && mQueue.size() < mSize) {
                mQueue.offer(obj);
            }

        }

        /**
         * 实例化对象
         *
         * @return 创建的对象
         */
        abstract protected T newInstance();

        /**
         * 重置对象
         * 把对象的数据清空
         *
         * @param obj 需要重置的对象
         * @return 重置后的对象
         */
        abstract protected T resetInstance(T obj);

    }

    /**
     * 矩阵对象池
     */
    private static class MatrixPool extends ObjectsPool<Matrix> {

        public MatrixPool(int size) {
            super(size);
        }

        @Override
        protected Matrix newInstance() {
            return new Matrix();
        }

        @Override
        protected Matrix resetInstance(Matrix obj) {
            obj.reset();
            return obj;
        }
    }

    /**
     * 矩形对象池
     */
    private static class RectFPool extends ObjectsPool<RectF> {

        public RectFPool(int size) {
            super(size);
        }

        @Override
        protected RectF newInstance() {
            return new RectF();
        }

        @Override
        protected RectF resetInstance(RectF obj) {
            obj.setEmpty();
            return obj;
        }
    }

    /**
     * 数学计算工具类
     */
    public static class MathUtils {

        /**
         * 矩阵对象池
         */
        private static MatrixPool matrixPool = new MatrixPool(16);

        /**
         * 获取矩阵对象
         */
        public static Matrix matrixTake() {
            return matrixPool.take();
        }

        /**
         * 获取某个矩阵的copy
         *
         * @param matrix
         * @return
         */
        public static Matrix matrixTake(Matrix matrix) {
            Matrix result = matrixPool.take();
            if (matrix != null) {
                result.set(matrix);
            }
            return result;
        }

        /**
         * 归还矩阵对象
         *
         * @param matrix
         */
        public static void matrixGiven(Matrix matrix) {
            matrixPool.give(matrix);
        }

        /**
         * 矩形对象池
         */
        private static RectFPool rectFPool = new RectFPool(16);

        /**
         * 获得一个矩形对象
         *
         * @return
         */
        public static RectF rectFTake() {
            return rectFPool.take();
        }

        /**
         * 按指定值获取矩阵对象
         *
         * @return
         */
        public static RectF rectFTake(int left, int top, int right, int bottom) {
            RectF result = rectFPool.take();
            result.set(left, top, right, bottom);
            return result;
        }

        /**
         * 获取某个矩形副本
         *
         * @param rect
         * @return
         */
        public static RectF rectFTake(RectF rect) {
            RectF result = rectFPool.take();
            if (rect != null) {
                result.set(rect);
            }
            return result;
        }

        /**
         * 归还矩形对象
         *
         * @param rectF
         */
        public static void rectFGiven(RectF rectF) {
            rectFPool.give(rectF);
        }

        /**
         * 计算两点之间的距离
         *
         * @param x1
         * @param y1
         * @param x2
         * @param y2
         * @return
         */
        public static float getDistance(float x1, float y1, float x2, float y2) {
            float x = x1 - x2;
            float y = y1 - y2;
            return (float) Math.sqrt(x * x + y * y);
        }

        /**
         * 获取两点的中间点
         *
         * @param x1
         * @param y1
         * @param x2
         * @param y2
         * @return
         */
        public static float[] getCenterPoint(float x1, float y1, float x2, float y2) {
            return new float[]{(x1 + x2) / 2f, (y1 + y2) / 2f};
        }

        /**
         * 获取矩阵的缩放值
         * @param matrix
         * @return
         */
        public static float[] getMatrixScale(Matrix matrix){
            if(matrix !=null){
                float[] values = new float[9];
                matrix.getValues(values);
                return new float[]{values[0],values[4]};
            }else{
                return new float[2];
            }
        }

        /**
         * 计算点除以矩阵的值
         * <p>
         * matrix.mapPoints(unknownPoint) -> point
         * 已知point和matrix,求unknownPoint的值.
         *
         * @param point
         * @param matrix
         * @return unknownPoint
         */
        public static float[] inverseMatrixPoint(float[] point,Matrix matrix){
            if(point!=null && matrix!=null){
                float[] dst = new float[2];
                //计算matrix的逆矩阵
                Matrix inverse = MathUtils.matrixTake();
                matrix.invert(inverse);
                //用逆矩阵变换point到dst,dst就是结果
                inverse.mapPoints(dst,point);
                //清除临时变量
                matrixGiven(inverse);
                return dst;
            }else{
                return new float[2];
            }

        }

    }


}
