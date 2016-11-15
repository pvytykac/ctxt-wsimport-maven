package uk.co.txttools.connectors.soap.messaging;

import org.junit.Test;
import uk.co.txttools.connectors.soap.SOAPClient;
import uk.co.txttools.connectors.soap.messaging.definitions.TxttoolsMessagingService;
import uk.co.txttools.connectors.soap.messaging.definitions.TxttoolsMessagingServiceService;
import uk.co.txttools.connectors.soap.messaging.schemas.MessageRequest;
import uk.co.txttools.connectors.soap.messaging.schemas.MessageType;
import uk.co.txttools.connectors.soap.messaging.schemas.StatusRequest;
import uk.co.txttools.connectors.soap.messaging.schemas.StatusResponse;
import uk.co.txttools.connectors.soap.messaging.schemas.StatusResponseType;
import uk.co.txttools.connectors.soap.messaging.schemas.TicketType;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static uk.co.txttools.connectors.soap.ClientConstants.MESSAGING_SOAP_URL;

/**
 * @author pvytykac
 * @since 21/10/2016 11:53
 */
public class StatusTest {

    private final SOAPClient<TxttoolsMessagingServiceService, TxttoolsMessagingService> sc = new SOAPClient<>(
            new TxttoolsMessagingServiceService(),
            TxttoolsMessagingService.class,
            MESSAGING_SOAP_URL
    );

    @Test
    public void testStatus() throws Exception {
        // send the message to query status for
        final String recipient = "+4207111222333";
        MessageType msg = sc.getObjectFactory().createMessageType();
        msg.setMessageText("WSIMPORT STATUS TEST MSG");
        msg.getRecipient().add(recipient);

        MessageRequest msgs = sc.getObjectFactory().createMessageRequest();
        msgs.getMessage().add(msg);

        StatusResponse response = sc.getPort().message(msgs);

        assertNotNull(response);
        assertNotNull(response.getStatusResponseType());
        assertEquals(1, response.getStatusResponseType().size());

        StatusResponseType status = response.getStatusResponseType().get(0);
        assertNotNull(status);

        BigInteger ticket = status.getTicket();
        assertNotNull(ticket);

        // query for status
        StatusRequest request = sc.getObjectFactory().createStatusRequest();
        TicketType ticketType = sc.getObjectFactory().createTicketType();
        ticketType.getTicketNumber().add(ticket);
        request.setTicket(ticketType);

        response = sc.getPort().status(request);

        assertNotNull(response);
        assertNotNull(response.getStatusResponseType());
        assertEquals(1, response.getStatusResponseType().size());

        status = response.getStatusResponseType().get(0);

        assertNotNull(status);
        assertEquals(recipient, status.getRecipient());
        assertEquals(ticket, status.getTicket());
        assertNotNull(status.getStatusMessage());
        assertNotNull(status.getStatus());

    }
}
