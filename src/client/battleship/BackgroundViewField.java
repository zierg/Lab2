package client.battleship;

import java.awt.Color;

class BackgroundViewField extends JButtonField {
    private static final Color HIT_COLOR = Color.DARK_GRAY;
    private static final Color MISS_COLOR = new Color(0x99D9EA);//Color.cyan;
    private static final Color ENABLED_COLOR = Color.white;
    private static final Color DISABLED_COLOR = new Color(0xF0F0F0);
    
    private boolean filled;
    
    public BackgroundViewField(boolean enabled, int index) {
        super();
        if (enabled) {
            setBackground(ENABLED_COLOR);
        } else {
            setBackground(DISABLED_COLOR);
        }
        setEnabled(enabled);
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
    }
    
    private boolean checkHit() {
        return filled;
    }
}
