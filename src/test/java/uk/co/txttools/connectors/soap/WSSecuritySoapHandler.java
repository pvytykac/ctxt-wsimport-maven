package uk.co.txttools.connectors.soap;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.util.Collections;
import java.util.Set;

public class WSSecuritySoapHandler implements SOAPHandler<SOAPMessageContext> {

    private static final String SOAP_ELEMENT_PASSWORD = "Password";
    private static final String SOAP_ELEMENT_USERNAME = "Username";
    private static final String SOAP_ELEMENT_USERNAME_TOKEN = "UsernameToken";
    private static final String SOAP_ELEMENT_SECURITY = "Security";
    private static final String NAMESPACE_SECURITY = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";
    private static final String PREFIX_SECURITY = "wsse";

    private final String username;
    private final String password;

    public WSSecuritySoapHandler(String username, String password) {
        if(username == null || password == null || username.length() < 1 || password.length() < 1)
            throw new IllegalArgumentException("credentials must be set");

        this.username = username;
        this.password = password;
    }

    public boolean handleMessage(SOAPMessageContext soapMessageContext) {

        Boolean outboundProperty = (Boolean) soapMessageContext.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

        if (outboundProperty) {

            try {
                SOAPEnvelope soapEnvelope = soapMessageContext.getMessage().getSOAPPart().getEnvelope();

                SOAPHeader header = soapEnvelope.getHeader();
                if (header == null) {
                    header = soapEnvelope.addHeader();
                }

                SOAPElement soapElementSecurityHeader = header.addChildElement(SOAP_ELEMENT_SECURITY, PREFIX_SECURITY,
                        NAMESPACE_SECURITY);

                SOAPElement soapElementUsernameToken = soapElementSecurityHeader.addChildElement(SOAP_ELEMENT_USERNAME_TOKEN, PREFIX_SECURITY);
                SOAPElement soapElementUsername = soapElementUsernameToken.addChildElement(SOAP_ELEMENT_USERNAME, PREFIX_SECURITY);
                soapElementUsername.addTextNode(this.username);

                SOAPElement soapElementPassword = soapElementUsernameToken.addChildElement(SOAP_ELEMENT_PASSWORD, PREFIX_SECURITY);
                soapElementPassword.addTextNode(this.password);

            } catch (Exception e) {
                throw new RuntimeException("Error on wsSecurityHandler: " + e.getMessage());
            }

        }

        return true;
    }

    @Override
    public void close(MessageContext context) {}

    @Override
    public boolean handleFault(SOAPMessageContext context) {
        return true;
    }

    @Override
    public Set<QName> getHeaders() {
        return Collections.emptySet();
    }
}