package settings;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyConfigReader implements ConfigReader {

    private Properties properties = new Properties();
    
    public PropertyConfigReader(String fileName) throws HandlerCreatingErrorException {
        this(new File(fileName));
    }
    
    public PropertyConfigReader(File file) throws HandlerCreatingErrorException  {
        try {
            FileInputStream input = new FileInputStream(file);
            properties.load(input);
        } catch (IOException ex) {
            throw new HandlerCreatingErrorException();
        }
    }
    
    @Override
    public String readValue(String property) {
        return properties.getProperty(property, null);
    }

}
