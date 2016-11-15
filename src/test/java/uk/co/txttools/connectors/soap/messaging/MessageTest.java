package uk.co.txttools.connectors.soap.messaging;

import org.junit.Test;
import uk.co.txttools.connectors.soap.SOAPClient;
import uk.co.txttools.connectors.soap.messaging.definitions.TxttoolsMessagingService;
import uk.co.txttools.connectors.soap.messaging.definitions.TxttoolsMessagingServiceService;
import uk.co.txttools.connectors.soap.messaging.schemas.MessageRequest;
import uk.co.txttools.connectors.soap.messaging.schemas.MessageType;
import uk.co.txttools.connectors.soap.messaging.schemas.StatusResponse;
import uk.co.txttools.connectors.soap.messaging.schemas.StatusResponseType;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static uk.co.txttools.connectors.soap.ClientConstants.MESSAGING_SOAP_URL;

/**
 * @author pvytykac
 * @since 21/10/2016 11:15
 */
public class MessageTest {

    private final SOAPClient<TxttoolsMessagingServiceService, TxttoolsMessagingService> sc = new SOAPClient<>(
            new TxttoolsMessagingServiceService(),
            TxttoolsMessagingService.class,
            MESSAGING_SOAP_URL
    );

    @Test
    public void testMessage() throws Exception {
        GregorianCalendar cal = new GregorianCalendar();
        cal.set(Calendar.HOUR, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);

        MessageType mt = sc.getObjectFactory().createMessageType();
        mt.setSource("WSIMPORTX");
        mt.setMessageText("SIMPLE TEST");
        mt.getRecipient().add("+420711111111");
        mt.getRecipient().add("+420722222222");
        mt.setHeader("HEADERHEADERHEADER::");
        mt.setSendTime(sc.getDatatypeFactory().newXMLGregorianCalendar(cal));
        mt.setSuppressUnicode(true);
        mt.setTTL(24);

        MessageRequest request = sc.getObjectFactory().createMessageRequest();
        request.getMessage().add(mt);

        StatusResponse response = sc.getPort().message(request);
        List<StatusResponseType> statuses = response.getStatusResponseType();

        assertNotNull(response);
        assertNotNull(statuses);
        assertEquals(2, statuses.size());

        for (int ix = 0; ix < statuses.size(); ix++) {
            StatusResponseType status = statuses.get(ix);

            assertNotNull(status);
            assertNotNull(status.getTicket());
            assertNotNull(status.getStatus());
            assertNotNull(status.getStatusMessage());
            assertEquals(mt.getRecipient().get(ix), status.getRecipient());
        }
    }
}
