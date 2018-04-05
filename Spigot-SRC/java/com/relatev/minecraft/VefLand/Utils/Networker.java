/*    */ package com.relatev.minecraft.VefLand.Utils;
/*    */ 
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.File;
/*    */ import java.io.FileOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.PrintStream;
/*    */ import java.net.HttpURLConnection;
/*    */ import java.net.URL;
/*    */ import java.util.Timer;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.event.player.PlayerJoinEvent;
/*    */ 
/*    */ public class Networker
/*    */ {
/*    */   public static String BcMessage;
/*    */   
/*    */   public static void init()
/*    */   {
/*    */     try
/*    */     {
/* 23 */       File URLLogFile = new File("VefLand-URLLog");
/* 24 */       DowloadFile("http://www.relatev.com/files/VefLand/NetWorker-1.12.2.yml", URLLogFile);
/* 25 */       org.bukkit.configuration.file.YamlConfiguration URLLog = org.bukkit.configuration.file.YamlConfiguration.loadConfiguration(URLLogFile);
/*    */       
/* 27 */       System.out.println("正在检查 VefLand 是否拥有新版本...");
/* 28 */       int NewVersion = URLLog.getInt("UpdateVersion");
/* 29 */       int NowVersion = AllUse.version;
/* 30 */       if (NewVersion > NowVersion) {
/* 31 */         Timer updateinfotimer = new Timer();
/* 32 */         updateinfotimer.schedule(new java.util.TimerTask()
/*    */         {
/*    */           public void run() {
/* 35 */             System.err.println("*** VefLand 新版本发布啦!更完善和更快速! ***");
/* 36 */             System.err.println("*** VefLand——新一代多线程高性能服务端 ***");
/* 37 */             System.err.println("*** 加QQ群 664015345 见群文件获取下载地址! ***");
/*    */           }
/* 39 */         }, 0L, 600000L);
/*    */       } else {
/* 41 */         System.out.println("VefLand 已是最新版本,无需更新!");
/*    */       }
/*    */     }
/*    */     catch (IOException localIOException) {}
/*    */   }
/*    */   
/*    */   public static void DowloadFile(String urlStr, File savefile) throws IOException {
/* 48 */     if (savefile.exists()) {
/* 49 */       savefile.delete();
/*    */     }
/* 51 */     URL url = new URL(urlStr);
/* 52 */     HttpURLConnection conn = (HttpURLConnection)url.openConnection();
/*    */     
/* 54 */     conn.setConnectTimeout(3000);
/*    */     
/* 56 */     conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
/*    */     
/*    */ 
/* 59 */     InputStream inputStream = conn.getInputStream();
/*    */     
/* 61 */     byte[] getData = readInputStream(inputStream);
/*    */     
/*    */ 
/* 64 */     File file = savefile;
/* 65 */     FileOutputStream fos = new FileOutputStream(file);
/* 66 */     fos.write(getData);
/* 67 */     if (fos != null) {
/* 68 */       fos.close();
/*    */     }
/* 70 */     if (inputStream != null) {
/* 71 */       inputStream.close();
/*    */     }
/*    */   }
/*    */   
/*    */   public static byte[] readInputStream(InputStream inputStream) throws IOException {
/* 76 */     byte[] buffer = new byte['Ѐ'];
/* 77 */     int len = 0;
/* 78 */     ByteArrayOutputStream bos = new ByteArrayOutputStream();
/* 79 */     while ((len = inputStream.read(buffer)) != -1) {
/* 80 */       bos.write(buffer, 0, len);
/*    */     }
/* 82 */     bos.close();
/* 83 */     return bos.toByteArray();
/*    */   }
/*    */   
/*    */   public static void AATBcMessage(PlayerJoinEvent event) {
/* 87 */     Player player = event.getPlayer();
/* 88 */     if ((player.hasPermission("AntiAttack.admin") & BcMessage != null)) {
/* 89 */       player.sendMessage(BcMessage);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Administrator\Desktop\VefLand-1.12.2.jar!\com\relatev\minecraft\VefLand\Utils\Networker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */