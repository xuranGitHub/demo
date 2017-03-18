package com.baturu.simpleDemo.concurrency.test;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;

/**
 * Created by xuran on 16/3/6.
 */
public class Piped {

    public static void main(String[] args) throws IOException {
        PipedWriter out = new PipedWriter();
        PipedReader in = new PipedReader();
        out.connect(in);
        Thread printThread = new Thread(new PrintThread(in), "PrintThread");
        printThread.start();
        int receive = 0;
        while ((receive = System.in.read()) != -1) {
            out.write(receive);
        }
        out.close();
    }

    static class PrintThread implements Runnable {

        private PipedReader in;

        public PrintThread(PipedReader in) {
            this.in = in;
        }

        @Override
        public void run() {
            int receive = 0;
            try {
                while ((receive = in.read()) != -1) {
                    System.out.print((char)receive);
                }
            } catch (IOException e) {
                    e.printStackTrace();
            }
        }
    }
}
