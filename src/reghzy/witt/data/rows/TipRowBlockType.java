package reghzy.witt.data.rows;

import net.minecraft.src.ItemStack;
import reghzy.witt.data.rows.TipRowText;
import reghzy.witt.utils.Thickness;

public final class TipRowBlockType extends TipRowText {
    private final String displayName, dnNoColour;

    public TipRowBlockType(ItemStack stack, Thickness padding) {
        String displayName;
        try {
            displayName = stack.getItem().getItemDisplayName(stack);
        }
        catch (Throwable ignored1) {
            try {
                displayName = stack.getItem().getLocalItemName(stack);
            }
            catch (Throwable ignored2) {
                try {
                    displayName = stack.getItem().getItemName();
                }
                catch (Throwable ignored3) {
                    int dmg = stack.getItemDamage();
                    displayName = stack.itemID + (dmg != 0 ? (":" + dmg) : "");
                }
            }
        }

        this.displayName = (char) 167 + "b" + displayName;
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
