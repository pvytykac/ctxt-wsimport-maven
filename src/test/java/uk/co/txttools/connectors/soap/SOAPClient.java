package uk.co.txttools.connectors.soap;

import uk.co.txttools.connectors.soap.messaging.schemas.ObjectFactory;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.handler.Handler;
import java.util.Collections;
import java.util.List;

import static uk.co.txttools.connectors.soap.ClientConstants.PASSWORD;
import static uk.co.txttools.connectors.soap.ClientConstants.USERNAME;

/**
 * @author pvytykac
 * @since 21/10/2016 10:24
 */
public class SOAPClient<A extends Service, B> {

    private static final List<Handler> HANDLERS = Collections.<Handler>singletonList(new WSSecuritySoapHandler(USERNAME, PASSWORD));
    private static final ObjectFactory OBJECT_FACTORY = new ObjectFactory();

    private final DatatypeFactory datatypeFactory;
    private final A soapService;
    private final B port;

    static {
        System.setProperty("com.sun.xml.ws.transport.http.client.HttpTransportPipe.dump", "true");
        System.setProperty("com.sun.xml.internal.ws.transport.http.client.HttpTransportPipe.dump", "true");
        System.setProperty("com.sun.xml.ws.transport.http.HttpAdapter.dump", "true");
        System.setProperty("com.sun.xml.internal.ws.transport.http.HttpAdapter.dump", "true");
    }

    public SOAPClient(A soapService, Class<B> portClass, String url) {
        try {
            this.datatypeFactory = DatatypeFactory.newInstance();
            this.soapService = soapService;
            this.port = soapService.getPort(portClass);

            getBindingProvider().getBinding().setHandlerChain(HANDLERS);
            getBindingProvider().getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);
        } catch (Exception ex) {
            throw new RuntimeException("Cannot instantiate the SOAP client", ex);
        }
    }

    public ObjectFactory getObjectFactory() {
        return SOAPClient.OBJECT_FACTORY;
    }

    public A getSoapService() {
        return soapService;
    }

    public DatatypeFactory getDatatypeFactory() {
        return datatypeFactory;
    }

    public BindingProvider getBindingProvider() {
        return (BindingProvider) getPort();
    }

    public B getPort() {
        return port;
    }
}
