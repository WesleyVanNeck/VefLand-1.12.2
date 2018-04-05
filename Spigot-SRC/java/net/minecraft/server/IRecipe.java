package net.minecraft.server;

public interface IRecipe {

    boolean a(InventoryCrafting inventorycrafting, World world);

    ItemStack craftItem(InventoryCrafting inventorycrafting);

    ItemStack b();

    NonNullList<ItemStack> b(InventoryCrafting inventorycrafting);

    default NonNullList<RecipeItemStack> d() {
        return NonNullList.a();
    }

    default boolean c() {
        return false;
    }

    org.bukkit.inventory.Recipe toBukkitRecipe(); // CraftBukkit

    void setKey(MinecraftKey key); // CraftBukkit
}
