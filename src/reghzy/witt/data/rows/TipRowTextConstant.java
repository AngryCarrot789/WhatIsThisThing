package reghzy.witt.data.rows;

import reghzy.witt.data.rows.TipRowText;
import reghzy.witt.utils.Thickness;

/**
 * A text row that only draws text, supporting colour too
 */
public class TipRowTextConstant extends TipRowText {
    private String colouredText, uncolouredText;

    public TipRowTextConstant() {
    }

    public TipRowTextConstant(String colouredText) {
        this.colouredText = colouredText;
    }
    public TipRowTextConstant(String colouredText, Thickness padding) {
        this.colouredText = colouredText;
        this.setPadding(padding);
    }

    public void setColouredText(String text) {
        this.colouredText = text;
        this.uncolouredText = null;
        this.onSizeInvalidated();
    }

    @Override
    protected String getColouredText() {
        return this.colouredText;
    }

    @Override
    protected String getTextNoColour() {
        if (this.uncolouredText == null) {
            if (this.colouredText == null)
                return null;

            this.uncolouredText = STRIP_COLOR_PATTERN.matcher(this.colouredText).replaceAll("");
        }

        return this.uncolouredText;
    }
}
