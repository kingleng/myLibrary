package com.kingleng.baselibrary;

/**
 * Created by leng on 2021/2/8.
 */
class test {

    public static void main(String[] args) {

        test tt = new test();
        tt.test01();
    }

    public void test01(){
        Thread1 t1 = new Thread1();
        Thread2 t2 = new Thread2();

        System.out.print("test01 start");

        t1.start();
        t2.start();

        System.out.print("test01 end");
    }

    class Thread1 extends Thread{
        @Override
        public void run() {
            super.run();

            System.out.print("Thread1 end");
        }
    }

    class Thread2 extends Thread{
        @Override
        public void run() {
            super.run();

            System.out.print("Thread2 end");
        }
    }

}
