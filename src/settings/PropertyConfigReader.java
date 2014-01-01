package settings;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertyConfigReader implements ConfigReader {

    private Properties properties = new Properties();
    
    public PropertyConfigReader(String fileName) throws FileNotFoundException, IOException {
        this(new File(fileName));
    }
    
    public PropertyConfigReader(File file) throws FileNotFoundException, IOException {
        FileInputStream input = new FileInputStream(file);
        properties.load(input);
    }
    
    @Override
    public String readValue(String property) {
        return properties.getProperty(property, null);
    }

}
