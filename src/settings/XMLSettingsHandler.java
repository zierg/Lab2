package settings;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import org.jdom2.Element;
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;
import org.jdom2.output.Format;
import org.jdom2.Attribute;

public class XMLSettingsHandler implements SettingsHandler {
    
    private static final String ROOT_NAME = "settings";
    private static final String ATTR_NAME = "value";
    
    private final String filename;
    
    private final Document document;    // XML document
    private final Element root;         // root of XML document
    
    private final XMLOutputter outputter;   // to save document

    public XMLSettingsHandler(String fileName) throws HandlerCreatingErrorException {
        this.filename = fileName;
        try {
            SAXBuilder builder = new SAXBuilder();
            File file = new File(filename);

            outputter = new XMLOutputter( Format.getPrettyFormat().setIndent("\t") );

            if (file.exists()) {
                document = builder.build(file);
                root = document.getRootElement();
            }
            else {
                root = new Element(ROOT_NAME);
                document = new Document(root);
                saveDocument();
            }
        }
        catch (JDOMException | IOException ex) {
            throw new HandlerCreatingErrorException();
        }  
    }
    
    @Override
    public void setValue(String property, String value) {
        Element propertyElement = root.getChild(property);
        if (propertyElement == null) {
            root.addContent(new Element(property).setAttribute(ATTR_NAME, value));
        } else {
            root.getChild(property).setAttribute(ATTR_NAME, value);
        }
        saveDocument();
    }

    @Override
    public String readValue(String property) {
        return root.getChild(property).getAttributeValue(ATTR_NAME);
    }

    private void saveDocument() {
        try ( Writer writer = new FileWriter(filename) ) {
            outputter.output(document, writer);
        }
        catch (IOException ex) {
            
        }
    }
}
