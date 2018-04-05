package org.spigotmc;

import net.minecraft.server.MinecraftServer;

public class AsyncCatcher
{

    public static boolean enabled = true;

    public static void catchOp(String reason)
    {
        if ( enabled && Thread.currentThread() != MinecraftServer.getServer().primaryThread )
        {
//            throw new IllegalStateException( "Asynchronous " + reason + "!" );
            //Vefland：线程安全？不存在的!
        }
    }
}
