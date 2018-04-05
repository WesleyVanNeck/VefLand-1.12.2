package com.relatev.minecraft.VefLand.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MultiThreadConcurrent {

    private static LinkedBlockingQueue<Runnable> runnables = new LinkedBlockingQueue();
    private static List<Runnable> remainWork = new ArrayList();

    public static void init() {
        for (int i = 0; i < Runtime.getRuntime().availableProcessors(); i++) {
            Thread AMiniThread = new Thread() {
                public void run() {
                    try {
                        while (true) {
                            Runnable run = (Runnable) MultiThreadConcurrent.runnables.take();
                            run.run();
                            MultiThreadConcurrent.remainWork.remove(run);
                        }
                    } catch (InterruptedException ex) {
                        Logger.getLogger(MultiThreadConcurrent.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            };
            AMiniThread.setDaemon(true);
            AMiniThread.setPriority(7);
            AMiniThread.start();
        }
    }

    public static void post(List<Runnable> runs) {
        remainWork.addAll(runs);
        runnables.addAll(runs);
        while (!remainWork.isEmpty()) {
            try {
                Thread.sleep(0L, 1);
            } catch (InterruptedException ex) {
                Logger.getLogger(MultiThreadConcurrent.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
