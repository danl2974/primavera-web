package com.dl2974.spring;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		Calendar cal = Calendar.getInstance();
		int second = cal.get(Calendar.SECOND);
		int milli = cal.get(Calendar.MILLISECOND);
		int timeproduct = second * milli;
		
		model.addAttribute("timeproduct", String.format("Second: %d Milli: %d Product %d", second, milli, timeproduct) );
		model.addAttribute("locale", String.format("Country: %s", locale.getCountry()) );
		try{
		SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
        SOAPConnection soapConnection = soapConnectionFactory.createConnection();

        String url = "http://ec2-54-242-85-239.compute-1.amazonaws.com:8080/soapservice/ws/soap";
        SOAPMessage soapResponse = soapConnection.call(createSOAPRequest("tester", String.format("%d", timeproduct) ), url);
        
		
        model.addAttribute("wsresponse", soapResponse.toString() );
		}catch(Exception e){logger.info(e.getMessage());}
        
		return "home";
	}
	
	
	
    private SOAPMessage createSOAPRequest(String action, String param) throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();

        String serverURI = "http://ec2-54-242-85-239.compute-1.amazonaws.com:8080/soapservice/ws/soap";

        // SOAP Envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration("mns1", serverURI);


        // SOAP Body
        SOAPBody soapBody = envelope.getBody();
        SOAPElement soapBodyElem = soapBody.addChildElement(action, "mns1");
        SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("arg0", "mns1");
        soapBodyElem1.addTextNode(param);

        MimeHeaders headers = soapMessage.getMimeHeaders();
        headers.addHeader("SOAPAction", serverURI  + action);

        soapMessage.saveChanges();

        return soapMessage;
    }
  
	
}
