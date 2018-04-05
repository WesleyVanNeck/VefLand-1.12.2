package net.minecraft.server;

import java.io.IOException;
import javax.annotation.Nullable;

public interface IChunkLoader {

    @Nullable
    Chunk a(World world, int i, int j) throws IOException;

    void saveChunk(World world, Chunk chunk, boolean unloaded) throws IOException, ExceptionWorldConflict; // Spigot

    // void b(World world, Chunk chunk) throws IOException; // Spigot

    void b();

    void c();

    boolean chunkExists(int i, int j);
}
