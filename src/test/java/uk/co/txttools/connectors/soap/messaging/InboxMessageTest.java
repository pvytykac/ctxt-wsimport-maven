package uk.co.txttools.connectors.soap.messaging;

import org.junit.Test;
import uk.co.txttools.connectors.soap.SOAPClient;
import uk.co.txttools.connectors.soap.messaging.definitions.TxttoolsMessagingService;
import uk.co.txttools.connectors.soap.messaging.definitions.TxttoolsMessagingServiceService;
import uk.co.txttools.connectors.soap.messaging.schemas.InboxMessageRequest;
import uk.co.txttools.connectors.soap.messaging.schemas.InboxMessageResponse;
import uk.co.txttools.connectors.soap.messaging.schemas.InboxMessageResponseType;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static uk.co.txttools.connectors.soap.ClientConstants.BASE_URL;
import static uk.co.txttools.connectors.soap.ClientConstants.INBOUND_KEY;
import static uk.co.txttools.connectors.soap.ClientConstants.INBOUND_NUM;
import static uk.co.txttools.connectors.soap.ClientConstants.MESSAGING_SOAP_URL;

/**
 * @author pvytykac
 * @since 21/10/2016 11:53
 */
public class InboxMessageTest {

    private final SOAPClient<TxttoolsMessagingServiceService, TxttoolsMessagingService> sc = new SOAPClient<>(
            new TxttoolsMessagingServiceService(),
            TxttoolsMessagingService.class,
            MESSAGING_SOAP_URL
    );

    @Test
    public void testInboxMessage() throws Exception {
        final String body = INBOUND_KEY + " TEST INBOUND MESSAGE";
        final String source = "+420999999999";
        final String recipient = INBOUND_NUM;

        // send an inbound message and sleep for 5 seconds, to allow the message to get processed
        sendMessage(body, source, recipient);
        Thread.sleep(2000L);

        InboxMessageRequest request = sc.getObjectFactory().createInboxMessageRequest();
        request.setMaxReturn("1");

        InboxMessageResponse response = sc.getPort().inboxMessage(request);
        List<InboxMessageResponseType> inboxMessages = response.getInboxMessageResponseType();

        assertNotNull(response);
        assertNotNull(inboxMessages);

        assertEquals(1, inboxMessages.size());

        InboxMessageResponseType inboxMessage = inboxMessages.get(0);
        assertEquals(source, inboxMessage.getSource());
        assertEquals(body, inboxMessage.getContent());
        assertEquals(recipient, inboxMessage.getDestination());
        assertNotNull(inboxMessage.getDeliveryTime());
    }

    private void sendMessage(String body, String source, String recipient) throws Exception {
        String data = "X-E3-ID=0&X-E3-Hex-Message=" + URLEncoder.encode(toHex(body), "utf-8") +
                "&X-E3-Originating-Address=" + URLEncoder.encode(source, "utf-8") + "&X-E3-Recipients=" +
                URLEncoder.encode(recipient, "utf-8") + "&X-E3-Data-Coding-Scheme=01&X-E3-User-Data-Header-Indicator=0";

        HttpURLConnection connection = (HttpURLConnection) new URL(BASE_URL + "/connectors/dialogueMO.jsp").openConnection();
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestMethod("POST");
        connection.connect();

        connection.getOutputStream().write(data.getBytes("utf-8"));
        connection.getOutputStream().close();

        System.err.println(connection.getResponseCode() + ": " + connection.getResponseMessage());
        if(connection.getInputStream() != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                System.err.println(line);
            }
            reader.close();
        }

        if(connection.getErrorStream() != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getErrorStream(), "utf-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                System.err.println(line);
            }
            reader.close();
        }

        assertEquals(200, connection.getResponseCode());
    }

    private String toHex(String original) throws Exception {
        StringBuilder buff = new StringBuilder();
        for (byte b: original.getBytes("utf-8")) {
            buff.append(String.format("%02x", b));
        }

        return buff.toString().toUpperCase();
    }
}
