package fr.insee.pogues.config;

import org.apache.logging.log4j.web.Log4jServletContextListener;
import org.apache.logging.log4j.web.Log4jWebSupport;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class Log4j2ServletContextListener implements ServletContextListener {

    private String log4j2ConfigFile;

    private Log4jServletContextListener listener;

    public Log4j2ServletContextListener() {
        this.listener = new Log4jServletContextListener();
        this.log4j2ConfigFile = String.format("%s/log4j2.xml", System.getProperty("fr.insee.pogues.env"));
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        servletContextEvent.getServletContext().setInitParameter(
                Log4jWebSupport.LOG4J_CONFIG_LOCATION,
                log4j2ConfigFile);
        listener.contextInitialized(servletContextEvent);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) { }
}
