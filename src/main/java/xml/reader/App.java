package xml.reader;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws SAXParseException,IOException,SAXException
    {
        App app = new App();
        app.listParsingExceptions("PersonNotMatch.xsd","person.xml");
    }


    private Validator initValidator(String xsdPath) throws SAXException {
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Source schemaFile = new StreamSource(getFile(xsdPath));
        Schema schema = factory.newSchema(schemaFile);
        return schema.newValidator();
    }
    
    private File getFile(String fileName) {
    	URL url = getClass().getResource(fileName);
        return new File(url.getPath());
    }
    
    public boolean isValid(String xsdPath, String xmlPath) throws IOException, SAXException {
        Validator validator = initValidator(xsdPath);
        try {
            validator.validate(new StreamSource(getFile(xmlPath)));
            return true;
        } catch (SAXException e) {
            return false;
        }
    }
    
    
    public List<SAXParseException> listParsingExceptions(String xsdPath, String xmlPath)
    throws SAXParseException,IOException,SAXException
    		{
    	XmlErrorHandler xsdErrorHandler = new XmlErrorHandler();
        Validator validator = initValidator(xsdPath);
        validator.setErrorHandler(xsdErrorHandler);
        validator.validate(new StreamSource(getFile(xmlPath)));
        xsdErrorHandler.getExceptions().forEach(e -> 
        System.out.println(String.format("Line number: %s, Column number: %s. %s",
        		e.getLineNumber(), e.getColumnNumber(), e.getMessage())));
        return xsdErrorHandler.getExceptions();
    }
    
    
}
