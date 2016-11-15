package uk.co.txttools.connectors.soap;

/**
 * @author pvytykac
 * @since 21/10/2016 10:23
 */
public class ClientConstants {

    // CONFIGURATION
    public static final String PROTO = null;
    public static final String HOST = "uk-nightly.bbconnecttxt.com";
    public static final Integer PORT = null;
    public static final String CONTEXT = null;

    // TEST USER
    public static final String USERNAME = "wsimport";
    public static final String PASSWORD = "wsimport";
    public static final String INBOUND_NUM = "88020";
    public static final String INBOUND_KEY = "WSIMPORT";
    public static final Long SUBUSER_ID = 32L;

    // DEFAULTS
    private static final String DEFAULT_PROTO = "http";
    private static final int DEFAULT_HTTP_PORT = 80;
    private static final int DEFAULT_HTTPS_PORT = 443;
    public static final String MESSAGING_SOAP_URI = "connectors/soap/messaging";
    public static final String ADDRESSBOOK_SOAP_URI = "connectors/soap/addressbook";

    // URLS
    public static final String BASE_URL;
    public static final String ADDRESSBOOK_SOAP_URL;
    public static final String MESSAGING_SOAP_URL;

    static {
        BASE_URL = getBaseURL();
        ADDRESSBOOK_SOAP_URL = getAddressbookSoapURL();
        MESSAGING_SOAP_URL = getMessagingSoapURL();
    }

    private static String getAddressbookSoapURL() {
        return BASE_URL + ADDRESSBOOK_SOAP_URI;
    }

    private static String getMessagingSoapURL() {
        return BASE_URL + MESSAGING_SOAP_URI;
    }

    private static String getBaseURL() {
        String proto = require(PROTO, DEFAULT_PROTO, new String[]{"http", "https"});
        String host = require(HOST);
        int port = require(PORT, proto.equals("http")
                ? DEFAULT_HTTP_PORT
                : DEFAULT_HTTPS_PORT);

        StringBuilder sb = new StringBuilder();
        sb.append(proto).append("://")
                .append(host)
                .append(':').append(port);

        if (CONTEXT != null && !CONTEXT.isEmpty()) {
            sb.append("/").append(CONTEXT);
        }

        if (sb.charAt(sb.length() - 1) != '/')
            sb.append('/');

        return sb.toString();
    }

    private static <T> T require(T value) {
        return require(value, null, null);
    }

    private static <T> T require(T value, T def) {
        return require(value, def, null);
    }

    private static <T> T require(T value, T def, T[] expected) {
        boolean match = true;

        if (value == null)
            if (def == null)
                throw new IllegalArgumentException("value cannot be null");
            else
                value = def;

        if (expected != null) {
            match = false;
            for (T ex : expected) {
                if (value == ex || value.equals(ex)) {
                    match = true;
                    break;
                }
            }
        }

        if (!match) {
            throw new IllegalArgumentException("value does not match any of the expected values");
        }

        return value;
    }

}
