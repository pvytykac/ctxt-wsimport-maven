Runs the SOAP tests against uk-nightly environment.

The tested sources get generated apache CFX maven plugin before test execution

To generate the sources manually execute the following from this directory:
$JAVA_HOME/wsimport -keep https://uk-nightly.bbconnecttxt.com/connectors/soap/messaging/txttoolsMessaging.wsdl
$JAVA_HOME/wsimport -keep https://uk-nightly.bbconnecttxt.com/connectors/soap/addressbook/txttoolsAddressbook.wsdl