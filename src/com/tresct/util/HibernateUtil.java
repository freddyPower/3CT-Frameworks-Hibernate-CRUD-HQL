package com.tresct.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUtil {

	private static final SessionFactory sessionFactory = buildSessionFactory();

	private static SessionFactory buildSessionFactory() {

		try {
			
			// Para Hibernate 5.x
			// Crear SessionFactory desde hibernate.cfg.xml
			StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
					.configure("hibernate.cfg.xml").build();
			Metadata metadata = new MetadataSources(serviceRegistry).getMetadataBuilder().build();
			return metadata.getSessionFactoryBuilder().build();

		} catch (Throwable ex) {

			System.err.println("*** Creación de SessionFactory falló." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	} //Fin de buildSessionFactory
	
	public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

}
