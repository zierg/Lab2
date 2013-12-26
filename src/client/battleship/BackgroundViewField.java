package client.battleship;

import java.awt.Color;

final class BackgroundViewField extends JButtonField {
    private static final Color HIT_COLOR = Color.red;
    private static final Color MISS_COLOR = new Color(0x99D9EA);
    private static final Color FILL_COLOR = Color.DARK_GRAY;
    private static final Color EMPTY_COLOR = MISS_COLOR;
    private static final Color DEFAULT_COLOR = Color.white;
    private static final Color AVAILABLE_COLOR = DEFAULT_COLOR;
    private static final Color NOT_AVAILABLE_COLOR = new Color(0xF8A7AB);
    
    private boolean filled;
    private boolean available = true;
    
    private Color currentFillColor;
    private Color currentEnableColor;
        
    public BackgroundViewField(boolean enabled, int index) {
        super();
        setFill(false);
        setEnabled(enabled);
        currentEnableColor = DEFAULT_COLOR;
        setBackground(currentEnableColor);
        setActionCommand(Integer.toString(index));
    }
    
    @Override
    public boolean attack() {
        if ( checkHit() ) {
            setBackground(HIT_COLOR);
        } else {
            setBackground(MISS_COLOR);
        }
        setEnabled(false);
        return filled;
    }
    
    @Override
    public void setFill(boolean filled) {
        this.filled = filled;
        if (filled) {
            currentFillColor = FILL_COLOR;
        } else {
            currentFillColor = EMPTY_COLOR;
        }
    }
    
    private boolean checkHit() {
        return filled;
    }
    
    @Override
    public void setAvailableField(boolean available) {
        if (available) {
            setBackground(currentFillColor);
        } else {
            setBackground(NOT_AVAILABLE_COLOR);
        }
        this.available = available;
        setEnabled(available);
    }
    
    @Override
    public void setVisibleField(boolean visible) {
        if (visible) {
            setBackground(currentFillColor);
        }
        else {
            setBackground(currentEnableColor);
        }
    }

    @Override
    public boolean getAvailableField() {
        return available;
    }

    @Override
    public boolean getFill() {
        return filled;
    }
}