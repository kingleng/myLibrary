package com.example.leng.myapplication2.ui.myView.largeimage;

import android.content.Context;
import android.view.MotionEvent;

public class MoveGestureDetector {
        Context context;
        SimpleMoveGestureDetector simpleMoveGestureDetector;

        public MoveGestureDetector(Context context, SimpleMoveGestureDetector simpleMoveGestureDetector) {
            this.context = context;
            this.simpleMoveGestureDetector = simpleMoveGestureDetector;
        }

        float lastX;
        float lastY;
        float lastDistance;

        float currentX0;
        float currentY0;
        float currentX1;
        float currentY1;

        public void onTouchEvent(MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    lastX = event.getX();
                    lastY = event.getY();
                    lastDistance = 0;
                    break;
//                case MotionEvent.ACTION_POINTER_DOWN:
//                    currentX0 = event.getX(0);
//                    currentY0 = event.getY(0);
//                    currentX1 = event.getX(1);
//                    currentY1 = event.getY(1);
//
//                    break;
                case MotionEvent.ACTION_MOVE:

                    if(event.getPointerCount()==1){
                        float currentX = event.getX();
                        float currentY = event.getY();

                        simpleMoveGestureDetector.onMove(currentX - lastX, currentY - lastY);
                        lastX = currentX;
                        lastY = currentY;
                    }else{
                        currentX0 = event.getX(0);
                        currentY0 = event.getY(0);
                        currentX1 = event.getX(1);
                        currentY1 = event.getY(1);

                        float distance = (float) getDistance(currentX0,currentY0,currentX1,currentY1);

                        if(lastDistance == 0){
                            lastDistance = distance;
                        }

                        float scale = (float) Math.sqrt(distance/lastDistance);
                        float centerX = (currentX1+currentX0)/2;
                        float centerY = (currentY1+currentY0)/2;

                        simpleMoveGestureDetector.onScale(scale,centerX,centerY);

                        lastDistance = distance;
                    }

                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    simpleMoveGestureDetector.onFinish();
                    break;
            }
        }

        private double getDistance(float x0,float y0,float x1,float y1){
            float dx = Math.abs(x1-x0);
            float dy = Math.abs(y1-y0);

            return Math.sqrt(Math.pow(dx,2)+Math.pow(dy,2));

        }

        public interface SimpleMoveGestureDetector {
            void onMove(float distanceX, float distanceY);
            void onScale(float scale, float centerX, float centerY);
            void onFinish();
        }
    }