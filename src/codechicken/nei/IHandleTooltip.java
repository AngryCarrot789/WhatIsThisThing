package codechicken.nei;

import net.minecraft.src.ItemStack;

import java.util.List;

public interface IHandleTooltip {
    List<String> handleTooltip(ItemStack itemstack, List<String> var2);
}
