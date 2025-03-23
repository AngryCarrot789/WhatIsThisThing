package reghzy.witt.gui;

import net.minecraft.src.GuiButton;
import reghzy.WittMod;
import reghzy.witt.WittConfig;

import java.awt.*;

public class GuiWittSettings extends GuiScreenAdvanced {
    protected String screenTitle = "Options";

    public GuiWittSettings() {
    }

    public void initGui() {
        super.initGui();

        this.screenTitle = "WhatIsThisThing Options";

        StackPanel horizontalList = new StackPanel(5, false);
        StackPanel list1 = new StackPanel(5, true);
        list1.addButton(1, 0, 125, 20);
        list1.addButton(2, 0, 125, 20);
        list1.addButton(3, 0, 125, 20);
        list1.addButton(4, 0, 150, 20);
        list1.addEmptySpace(10);
        list1.addButton(5, 0, 155, 20);

        // StackPanel list2 = new StackPanel(5, true);
        // list2.addTextField(ModLoader.getMinecraftInstance().fontRenderer, 0, 250, 20);
        // list2.addTextField(ModLoader.getMinecraftInstance().fontRenderer, 0, 250, 20);

        horizontalList.addPanel(list1);
        // horizontalList.addPanel(list2);

        // First measure the grand total size
        Point size = horizontalList.measure();

        // Next arrange and position in the screen centre
        horizontalList.arrange(this, (this.width / 2) - (size.x / 2), (this.height / 2) - (size.y / 2));

        for (GuiButton button : this.getButtonList())
            updateButtonText(button);
    }

    private void updateButtonText(GuiButton btn) {
        WittConfig cfg = WittMod.getInstance().getConfig();
        switch (btn.id) {
            case 1: btn.displayString = "Show ToolTip: " + (cfg.canShowToolTip ? "YES" : "NO");break;
            case 2: btn.displayString = "Show ID:Meta: " + (cfg.canShowIdMeta ? "YES" : "NO");break;
            case 3: btn.displayString = "Show mod name: " + (cfg.canShowModName ? "YES" : "NO");break;
            case 4: btn.displayString = "Show Sprite: " + (cfg.canShowItemSprite ? "YES" : "NO");break;
            case 5: btn.displayString = "Show mod name in NEI: " + (cfg.canShowModNameInNEI ? "YES" : "NO");break;
        }
    }

    protected void actionPerformed(GuiButton btn) {
        if (btn.enabled) {
            WittConfig cfg = WittMod.getInstance().getConfig();
            switch (btn.id) {
                case 1: cfg.canShowToolTip = !cfg.canShowToolTip; break;
                case 2: cfg.canShowIdMeta = !cfg.canShowIdMeta; break;
                case 3: cfg.canShowModName = !cfg.canShowModName; break;
                case 4: cfg.canShowItemSprite = !cfg.canShowItemSprite; break;
                case 5: cfg.canShowModNameInNEI = !cfg.canShowModNameInNEI; break;
                default:
                    return;
            }

            updateButtonText(btn);
        }
    }

    public void drawScreen(int par1, int par2, float par3) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, this.screenTitle, this.width / 2, 20, 16777215);
        super.drawScreen(par1, par2, par3);
    }
}
