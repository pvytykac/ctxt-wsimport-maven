package uk.co.txttools.connectors.soap.addressbook;

import org.junit.Test;
import uk.co.txttools.connectors.soap.ClientConstants;
import uk.co.txttools.connectors.soap.SOAPClient;
import uk.co.txttools.connectors.soap.addressbook.definitions.TxttoolsAddressbookService;
import uk.co.txttools.connectors.soap.addressbook.definitions.TxttoolsAddressbookServiceService;

import static uk.co.txttools.connectors.soap.ClientConstants.*;

/**
 * @author pvytykac
 * @since 21/10/2016 11:59
 */
//TODO: implement addressbook tests
public class AddressbookTest {


    private final SOAPClient<TxttoolsAddressbookServiceService, TxttoolsAddressbookService> sc = new SOAPClient<>(
            new TxttoolsAddressbookServiceService(),
            TxttoolsAddressbookService.class,
            ADDRESSBOOK_SOAP_URL
    );

    @Test
    public void testAddressbook() throws Exception {
    }
}
