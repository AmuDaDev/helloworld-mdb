package org.jboss.as.quickstarts.mdb;

/**
 * Created by F4977165 on 6/20/2016.
 */

import org.jboss.as.quickstarts.service.MessageProcessorService;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.inject.Inject;
import javax.jms.*;

@MessageDriven(activationConfig = {

        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),

        @ActivationConfigProperty(propertyName = "destination", propertyValue = "test.incoming.queue") })

public class TestMQMDB implements MessageListener{

    @Inject
    MessageProcessorService msgProcess;

    @Resource(lookup = "java:/ActiveMQ/ConnectionFactory")
    ConnectionFactory myQueueFactory;

    @Override
    public void onMessage(Message message) {

        try {

            if (message instanceof TextMessage) {

                System.out.println("Got Message " + ((TextMessage) message).getText());
                // Extract the ReplyTo destination
                Destination replyDestination = message.getJMSReplyTo();

                if (replyDestination != null) {
                    System.out.println("Reply to queue: " + replyDestination);

                    //Create ReplyTo
                    Connection conn = myQueueFactory.createConnection();
                    Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
                    MessageProducer producer = session.createProducer(message.getJMSReplyTo());
                    // Create the reply message
                    TextMessage replyMessage = session.createTextMessage("A reply message");
                    // Set the CorrelationID, using message id.
                    replyMessage.setJMSCorrelationID(message.getJMSMessageID());
                    // Send out the reply message
                    producer.send(replyDestination, replyMessage);

                    producer.close();
                    session.close();
                    conn.close();
                }


            }

        } catch (JMSException e) {

            e.printStackTrace();

        }

    }
}
