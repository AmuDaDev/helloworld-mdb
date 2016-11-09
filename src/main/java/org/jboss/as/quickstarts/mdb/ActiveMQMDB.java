package org.jboss.as.quickstarts.mdb;

/**
 * Created by F4888723 on 2015/06/30.
 */
import org.jboss.ejb3.annotation.ResourceAdapter;

import javax.ejb.*;
import javax.jms.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/*@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "activemq/queue/TestQueue") })
@ResourceAdapter("activemq")
public class ActiveMQMDB implements MessageListener {

    private final static Logger LOGGER = Logger.getLogger(ActiveMQMDB.class.getName());

    @Override
    public void onMessage(Message message) {
        LOGGER.info("On ActiveMQMDB - onMessage");
        try {
            if (message instanceof TextMessage) {
                LOGGER.info("Got Message " + ((TextMessage) message).getText());
            }
        } catch (JMSException e) {
            LOGGER.log(Level.ALL, "Error receiving JMS Message : " + e);
            e.printStackTrace();
        }finally {
            LOGGER.info("Done processing message...");
        }
    }
}*/
public class ActiveMQMDB{}