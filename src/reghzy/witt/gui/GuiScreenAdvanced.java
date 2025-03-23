package reghzy.witt.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Gui;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.GuiTextField;

import java.util.ArrayList;

/**
 * An extension of GuiScreen with automatic processes
 */
public class GuiScreenAdvanced extends GuiScreen {
    private final ArrayList<GuiTextField> textFields;
    private final ArrayList<GuiTextBlock> textBlocks;

    public GuiScreenAdvanced() {
        this.textFields = new ArrayList<GuiTextField>();
        this.textBlocks = new ArrayList<GuiTextBlock>();
    }

    @Override
    public void setWorldAndResolution(Minecraft par1Minecraft, int par2, int par3) {
        this.textFields.clear();
        this.textBlocks.clear();
        super.setWorldAndResolution(par1Minecraft, par2, par3);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        for (GuiTextField field : this.textFields)
            field.updateCursorCounter();
    }

    @Override
    protected void keyTyped(char par1, int par2) {
        super.keyTyped(par1, par2);
        for (GuiTextField field : this.textFields)
            field.textboxKeyTyped(par1, par2);
    }

    @Override
    protected void mouseClicked(int par1, int par2, int par3) {
        super.mouseClicked(par1, par2, par3);
        for (GuiTextField field : this.textFields)
            field.mouseClicked(par1, par2, par3);
    }

    @Override
    public void drawScreen(int par1, int par2, float par3) {
        super.drawScreen(par1, par2, par3);
        for (GuiTextField field : this.textFields)
            field.drawTextBox();

        for (GuiTextBlock field : this.textBlocks)
            field.drawText();
    }

    public Iterable<GuiButton> getButtonList() {
        return this.controlList;
    }

    public Iterable<GuiTextField> getTextFields() {
        return this.textFields;
    }

    public Iterable<GuiTextBlock> getTextBlocks() {
        return this.textBlocks;
    }

    public void addGui(Gui gui) {
        if (gui instanceof GuiButton) {
            this.controlList.add(gui);
        }
        else if (gui instanceof GuiTextField) {
            this.textFields.add((GuiTextField) gui);
        }
        else if (gui instanceof GuiTextBlock) {
            this.textBlocks.add((GuiTextBlock) gui);
        }
    }
}
