package org.jboss.as.quickstarts.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.annotation.Resource;
import javax.jms.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

/**
 * Created by F4977165 on 6/20/2016.
 */
@WebServlet("/HelloJMS")
public class TestJMS extends HttpServlet {
    @Resource(lookup = "java:/ActiveMQ/ConnectionFactory")
    ConnectionFactory cf;

    @Resource(lookup = "java:jboss/test.incoming.queue")
    private Queue queue;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            example();
            PrintWriter out = response.getWriter();
            out.println("Message sent!");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void example() throws Exception     {
        final Map<String, TextMessage> requestMap = new HashMap<>();
        String xmlMessage = "<?xml version=\"1.0\"?><proofOfAddressXmlDto><subject>My subject</subject><ucn>My ucn</ucn><customerName>My customerName</customerName><customerSurname>My customerSurname</customerSurname><totalNumberOfSources>My totalNumberOfSources</totalNumberOfSources><sourcesUsed>My sourcesUsed</sourcesUsed><physicalAddress>My physicalAddress</physicalAddress><physicalAddressLine1>My physicalAddressLine1</physicalAddressLine1><physicalAddressLine2>My physicalAddressLine2</physicalAddressLine2><physicalAddressPostalCode>My physicalAddressPostalCode</physicalAddressPostalCode><physicalAddressCity>My physicalAddressCity</physicalAddressCity><physicalAddressState>My physicalAddressState</physicalAddressState></proofOfAddressXmlDto>";

        Connection connection =  null;
        try {
            connection = cf.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            TextMessage message = session.createTextMessage(xmlMessage);
            MessageProducer publisher = session.createProducer(queue);
            // Creates a callback queue
            Queue myQueue = session.createQueue("response.queue");
            message.setJMSReplyTo(myQueue);

            connection.start();
            publisher.send(message);
            System.out.println("Request message sent.");

            // Put the request message to the map. Later we can use it to
            // check out which request message a reply message is for. Here we use the MessageID as the
            // correlation id (JMSCorrelationID). You don't have to use it though. You can use some arbitrary string for
            // example.
            requestMap.put(message.getJMSMessageID(), message);
            // Receive the reply message.
            MessageConsumer consumer = session.createConsumer(myQueue);
            TextMessage response = (TextMessage) consumer.receive(1000);

            System.out.println("Received reply: " + response.getText());
            System.out.println("CorrelatedId: " + response.getJMSCorrelationID());

            // Check out which request message is this reply message sent for.
            // Here we just have one request message for illustrative purpose. In real world there may be many requests and
            // many replies.
            TextMessage matchedMessage = requestMap.get(response.getJMSCorrelationID());

            System.out.println("We found matched request: " + matchedMessage.getText());

            if (response != null) {
                System.out.println("Response received" + response.getText());
            } else {
                System.out.println("Response not received due to timeout");
            }
            consumer.close();
        }

        finally
        {
            closeConnection(connection);
        }

    }
    private void closeConnection(Connection con)            {
        try  {

            if (con != null) {
                con.close();
            }

        }

        catch(JMSException jex) {

            System.out.println("Could not close connection " + con +" exception was " + jex);

        }

    }
}
