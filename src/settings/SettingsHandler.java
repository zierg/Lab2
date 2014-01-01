package settings;

public interface SettingsHandler extends ConfigReader {
    public void setValue(String property, String value);
}
