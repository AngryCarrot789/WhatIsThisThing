package reghzy.witt.data.rows;

import net.minecraft.src.Block;
import net.minecraft.src.Item;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;
import net.minecraft.src.StringTranslate;
import reghzy.witt.data.rows.TipRowText;
import reghzy.witt.utils.Thickness;

public final class TipRowBlockType extends TipRowText {
    private final String displayName, dnNoColour;

    public TipRowBlockType(ItemStack stack, Thickness padding) {
        boolean appendType = true;
        String displayName = null;
        Item item = stack.getItem();
        Block block = stack.itemID < 4096 ? Block.blocksList[stack.itemID] : null;
        try {
            displayName = stack.getItem().getItemDisplayName(stack);
            if (displayName == null || displayName.isEmpty())
                throw null;
        }
        catch (Throwable ignored1) {
            try {
                displayName = stack.getItem().getLocalItemName(stack);
                if (displayName == null || displayName.isEmpty())
                    throw null;
            }
            catch (Throwable ignored2) {
                try {
                    displayName = stack.getItem().getItemName();
                    if (displayName == null || displayName.isEmpty())
                        throw null;
                }
                catch (Throwable ignored3) {
                    try {
                        displayName = new ItemStack(stack.itemID, 1, 0).getItem().getItemName();
                        if (displayName == null || displayName.isEmpty())
                            throw null;
                    }
                    catch (Throwable ignored4) {
                        if (stack.getItem() instanceof ItemBlock && block != null) {
                            displayName = block.getBlockName();
                        }

                        if (displayName == null || displayName.isEmpty()) {
                            int dmg = stack.getItemDamage();
                            displayName = stack.itemID + (dmg != 0 ? (":" + dmg) : "");
                            appendType = false;
                        }
                    }
                }
            }
        }

        try {
            String itemNameIS = item.getItemNameIS(stack);
            String translatedName = ("" + StringTranslate.getInstance().translateNamedKey(itemNameIS)).trim();
        }
        catch (Throwable e) {
        }

        if (appendType) {
            int dmg = stack.getItemDamage();
            displayName += " (" + stack.itemID + (dmg != 0 ? (":" + dmg + ")") : ")");
        }

        this.displayName = (char) 167 + "7" + displayName;
        this.dnNoColour = displayName;
        this.setPadding(padding);
    }

    @Override
    protected String getColouredText() {
        return this.displayName;
    }

    @Override
    protected String getTextNoColour() {
        return this.dnNoColour;
    }
}
