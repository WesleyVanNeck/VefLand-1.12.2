package com.relatev.minecraft.VefLand.Utils;

import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MultiSecondaryThread {

    private static LinkedBlockingQueue<Runnable> runnables = new LinkedBlockingQueue();
    private static HashMap<Thread, LinkedBlockingQueue<Runnable>> threadmissions = new HashMap();

    public static void init() {
        for (int i = 0; i < Runtime.getRuntime().availableProcessors(); i++) {
            Thread minithread = new Thread() {
                public void run() {
                    try {
                        while (true) {
                            if (threadmissions.get(this).isEmpty() == false) {
                                Runnable run = threadmissions.get(this).take();
                                run.run();
                            } else {
                                if (runnables.isEmpty() == false) {
                                    Runnable run = (Runnable) MultiSecondaryThread.runnables.take();
                                    run.run();
                                } else {
                                    Thread.sleep(0, 1);
                                }
                            }
                        }
                    } catch (InterruptedException ex) {
                        Logger.getLogger(MultiSecondaryThread.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            };
            minithread.setDaemon(true);
            minithread.setPriority(3);
            threadmissions.put(minithread, new LinkedBlockingQueue());
            minithread.start();
        }
    }

    public static void post(Runnable runnable) {
        runnables.add(runnable);
    }
    
    public static void post(Runnable runnable,int tid) {
        while(tid > threadmissions.size()-1){
            tid = tid - threadmissions.size();
        }
        Set<Thread> threads = threadmissions.keySet();
        int thisthreadid = -1;
        for(Thread thread:threads){
            thisthreadid++;
            if(thisthreadid == tid){
                threadmissions.get(thread).add(runnable);
            }
        }
    }
}
