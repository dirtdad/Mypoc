package com.ordiway;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.exception.GenericJDBCException;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;

public class GeoCtxSession {
	private final static Logger logger = Logger.getLogger(GeoCtxSession.class.getName());
	private ClassPathXmlApplicationContext appContext;
	private SessionFactory sessionFactory;
	private Session session;

	public GeoCtxSession(DataSource datasource, String password) throws GenericJDBCException, IOException {
		sessionFactory = null;
		session = null;
		logger.info("configuring session with: " + datasource.getUrl() + " " + datasource.getUser() + " " + password);
		Configuration cfg = new Configuration()
				.setProperty("hibernate.dialect", "org.hibernate.spatial.dialect.postgis.PostgisDialect")
				.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver")
				.setProperty("hibernate.connection.username", datasource.getUser())
				.setProperty("hibernate.connection.password", password)
				.setProperty("hibernate.connection.url", datasource.getUrl());		
		cfg.configure();
		ServiceRegistry serviceRegistry = (ServiceRegistry) new ServiceRegistryBuilder().applySettings(
				cfg.getProperties()).buildServiceRegistry();
		logger.info("serviceRegistry: " + serviceRegistry);
		sessionFactory = cfg.buildSessionFactory(serviceRegistry);

		logger.info("session factory: " + sessionFactory);
		session = sessionFactory.openSession();
		logger.info("session : " + session);
		session.createCriteria(GeoDataset.class).list();
		new SetCurrentDatasource(datasource, password);
	}

	public GeoCtxSession() {
	}

	public void setTransactionManager(PlatformTransactionManager transactionManager) {
	} 

	public ClassPathXmlApplicationContext getAppContext() {
		return appContext;
	}

	public void setAppContext (ClassPathXmlApplicationContext appContext) {
		this.appContext = appContext;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory; 
	}

	public void setSessionFactory (SessionFactory sessionFactory) {		
		this.sessionFactory = sessionFactory;
		logger.info("Set session factory is indeed invoked: " + this.sessionFactory);
	}

	public Session getSession() {
		//We never want to return a closed session
		if (!session.isOpen())session = sessionFactory.openSession();
		return session;
	}

	public void setSession (Session session) {
		this.session = session;
	}

	public void run () {
		if (appContext == null) {
			logger.info("about to read the app context");

			appContext = new ClassPathXmlApplicationContext(
					new String[] { "classpath:app-context.xml" });
			logger.info("read the app context");

			if(appContext == null){
				logger.error("No app context.");		
			}
			else{
				logger.info("Got app context.");            
			}
			Object mgr = appContext.getBean("sessionManager");

			if(mgr != null) {
				sessionFactory = (SessionFactory) appContext.getBean("sessionFactory");
				logger.info("session factory: " + sessionFactory);
				session = sessionFactory.openSession();
				logger.info("session : " + session);
				setSession(session);
			}
			else {
				logger.error("session manager bean null");
			}
		}
	}
}