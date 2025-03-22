package reghzy.witt.data.rows;

import reghzy.witt.utils.Thickness;

/**
 * A text row that only draws text, supporting colour too
 */
public class TipRowTextSimple extends TipRowText {
    private String colouredText, uncolouredText;

    public TipRowTextSimple() {
    }

    public TipRowTextSimple(String colouredText) {
        this(colouredText, new Thickness(0, 1, 3, 1));
    }

    public TipRowTextSimple(String colouredText, Thickness padding) {
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
