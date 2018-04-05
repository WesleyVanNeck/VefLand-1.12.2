package net.minecraft.server;

// CraftBukkit - decompile weirdness
public class RecipeShulkerBox {

    public static class Dye extends ShapelessRecipes implements IRecipe { // CraftBukkit - added extends

        // CraftBukkit start - Delegate to new parent class with bogus info
        public Dye() {
            super("", new ItemStack(Blocks.WHITE_SHULKER_BOX, 0, 0), NonNullList.a(RecipeItemStack.a, RecipeItemStack.a(Items.DYE)));
        }
        // CraftBukkit end

        public boolean a(InventoryCrafting inventorycrafting, World world) {
            int i = 0;
            int j = 0;

            for (int k = 0; k < inventorycrafting.getSize(); ++k) {
                ItemStack itemstack = inventorycrafting.getItem(k);

                if (!itemstack.isEmpty()) {
                    if (Block.asBlock(itemstack.getItem()) instanceof BlockShulkerBox) {
                        ++i;
                    } else {
                        if (itemstack.getItem() != Items.DYE) {
                            return false;
                        }

                        ++j;
                    }

                    if (j > 1 || i > 1) {
                        return false;
                    }
                }
            }

            return i == 1 && j == 1;
        }

        public ItemStack craftItem(InventoryCrafting inventorycrafting) {
            ItemStack itemstack = ItemStack.a;
            ItemStack itemstack1 = ItemStack.a;

            for (int i = 0; i < inventorycrafting.getSize(); ++i) {
                ItemStack itemstack2 = inventorycrafting.getItem(i);

                if (!itemstack2.isEmpty()) {
                    if (Block.asBlock(itemstack2.getItem()) instanceof BlockShulkerBox) {
                        itemstack = itemstack2;
                    } else if (itemstack2.getItem() == Items.DYE) {
                        itemstack1 = itemstack2;
                    }
                }
            }

            ItemStack itemstack3 = BlockShulkerBox.b(EnumColor.fromInvColorIndex(itemstack1.getData()));

            if (itemstack.hasTag()) {
                itemstack3.setTag(itemstack.getTag().g());
            }

            return itemstack3;
        }

        public ItemStack b() {
            return ItemStack.a;
        }

        public NonNullList<ItemStack> b(InventoryCrafting inventorycrafting) {
            NonNullList nonnulllist = NonNullList.a(inventorycrafting.getSize(), ItemStack.a);

            for (int i = 0; i < nonnulllist.size(); ++i) {
                ItemStack itemstack = inventorycrafting.getItem(i);

                if (itemstack.getItem().r()) {
                    nonnulllist.set(i, new ItemStack(itemstack.getItem().q()));
                }
            }

            return nonnulllist;
        }

        public boolean c() {
            return true;
        }
    }
}
