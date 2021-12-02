package com.example.leng.myapplication2;

import android.util.Log;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
//    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void test() throws Exception{
        float total = 0;
        for(int i=1;i<10;i++){
            total = (total+5)*1.1f;
            Log.e("test","year = "+i+">> total = "+total);
        }
    }

    public static void main(String[] args) {
        float total = 0;
        for(int i=1;i<10;i++){
            total = (total+5)*1.1f;
            Log.e("test","year = "+i+">> total = "+total);
        }
    }


}