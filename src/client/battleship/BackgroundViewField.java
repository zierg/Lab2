package client.battleship;

import java.awt.Color;

class BackgroundViewField extends JButtonField {
    private static final Color HIT_COLOR = Color.red;
    private static final Color MISS_COLOR = new Color(0x99D9EA);
    private static final Color FILL_COLOR = Color.DARK_GRAY;
    private static final Color EMPTY_COLOR = MISS_COLOR;
    private static final Color ENABLED_COLOR = Color.white;
    private static final Color DISABLED_COLOR = new Color(0xF0F0F0);
    private static final Color AVAILABLE_COLOR = ENABLED_COLOR;
    private static final Color NOT_AVAILABLE_COLOR = new Color(0xF8A7AB);
    
    private boolean filled;
    
    private Color currentFillColor;
    private Color currentEnableColor;
        
    public BackgroundViewField(boolean enabled, int index) {
        super();
        setFill(false);
        setEnabled(enabled);
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
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) {
            currentEnableColor = ENABLED_COLOR;
        } else {
            currentEnableColor = DISABLED_COLOR;
        }
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
            setBackground(AVAILABLE_COLOR);
        } else {
            setBackground(NOT_AVAILABLE_COLOR);
        }
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
}