package uk.co.txttools.connectors.soap.messaging;

import org.junit.Test;
import uk.co.txttools.connectors.soap.SOAPClient;
import uk.co.txttools.connectors.soap.messaging.definitions.TxttoolsMessagingService;
import uk.co.txttools.connectors.soap.messaging.definitions.TxttoolsMessagingServiceService;
import uk.co.txttools.connectors.soap.messaging.schemas.ReportDate;
import uk.co.txttools.connectors.soap.messaging.schemas.ReportTotalsResponseType;
import uk.co.txttools.connectors.soap.messaging.schemas.ReportsRequest;

import java.math.BigInteger;
import java.util.Calendar;

import static org.junit.Assert.assertNotNull;
import static uk.co.txttools.connectors.soap.ClientConstants.MESSAGING_SOAP_URL;
import static uk.co.txttools.connectors.soap.ClientConstants.SUBUSER_ID;

/**
 * @author pvytykac
 * @since 21/10/2016 11:52
 */
public class ReportsTest {

    private final SOAPClient<TxttoolsMessagingServiceService, TxttoolsMessagingService> sc = new SOAPClient<>(
            new TxttoolsMessagingServiceService(),
            TxttoolsMessagingService.class,
            MESSAGING_SOAP_URL
    );

    @Test
    public void testReports() throws Exception {
        final Calendar calendar = Calendar.getInstance();
        final ReportDate start = sc.getObjectFactory().createReportDate();
        final ReportDate end = sc.getObjectFactory().createReportDate();

        // now
        end.setDd(calendar.get(Calendar.DATE));
        end.setMm(calendar.get(Calendar.MONTH) + 1);
        end.setYYYY(calendar.get(Calendar.YEAR));

        // 6 months ago
        calendar.add(Calendar.MONTH, -6);
        start.setDd(calendar.get(Calendar.DATE));
        start.setMm(calendar.get(Calendar.MONTH) + 1);
        start.setYYYY(calendar.get(Calendar.YEAR));

        ReportsRequest request = sc.getObjectFactory().createReportsRequest();
        request.setStartDate(start);
        request.setEndDate(end);
        request.setSubUser(BigInteger.valueOf(SUBUSER_ID));

        ReportTotalsResponseType response = sc.getPort().reports(request);

        assertNotNull(response);
        assertNotNull(response.getAmber());
        assertNotNull(response.getGreen());
        assertNotNull(response.getRed());
        assertNotNull(response.getTotal());
    }
}
