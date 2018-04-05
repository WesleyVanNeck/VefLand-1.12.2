/*    */ package com.relatev.minecraft.VefLand.Utils;
/*    */ 
/*    */ import java.util.concurrent.LinkedBlockingQueue;
/*    */ import java.util.logging.Level;
/*    */ import java.util.logging.Logger;
/*    */ 
/*    */ public class MultiSecondaryThread
/*    */ {
/*  9 */   private static LinkedBlockingQueue<Runnable> runnables = new LinkedBlockingQueue();
/*    */   
/*    */   public static void init() {
/* 12 */     for (int i = 0; i < Runtime.getRuntime().availableProcessors(); i++) {
/* 13 */       Thread minithread = new Thread() {
/*    */         public void run() {
/*    */           try {
/*    */             for (;;) {
/* 17 */               Runnable run = (Runnable)MultiSecondaryThread.runnables.take();
/* 18 */               run.run();
/*    */             }
/* 20 */           } catch (InterruptedException ex) { Logger.getLogger(MultiSecondaryThread.class.getName()).log(Level.SEVERE, null, ex);
/*    */           }
/*    */           
/*    */         }
/* 24 */       };
/* 25 */       minithread.setDaemon(true);
/* 26 */       minithread.setPriority(3);
/* 27 */       minithread.start();
/*    */     }
/*    */   }
/*    */   
/*    */   public static void post(Runnable runnable) {
/* 32 */     runnables.add(runnable);
/*    */   }
/*    */ }


/* Location:              C:\Users\Administrator\Desktop\VefLand-1.12.2.jar!\com\relatev\minecraft\VefLand\Utils\MultiSecondaryThread.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */