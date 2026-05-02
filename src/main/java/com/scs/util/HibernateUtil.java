package com.scs.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUtil {

    private static volatile SessionFactory sessionFactory;

    private HibernateUtil() {}

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            synchronized (HibernateUtil.class) {
                if (sessionFactory == null) {
                    sessionFactory = buildSessionFactory();
                }
            }
        }
        return sessionFactory;
    }

    private static SessionFactory buildSessionFactory() {
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().configure();
        applyEnvOverride(builder, "DB_URL", "hibernate.connection.url");
        applyEnvOverride(builder, "DB_USER", "hibernate.connection.username");
        applyEnvOverride(builder, "DB_PASSWORD", "hibernate.connection.password");
        applyEnvOverride(builder, "DB_DRIVER", "hibernate.connection.driver_class");
        applyEnvOverride(builder, "DB_DIALECT", "hibernate.dialect");
        StandardServiceRegistry registry = builder.build();
        try {
            Metadata metadata = new MetadataSources(registry).buildMetadata();
            return metadata.getSessionFactoryBuilder().build();
        } catch (Throwable e) {
            StandardServiceRegistryBuilder.destroy(registry);
            throw e;
        }
    }

    private static void applyEnvOverride(StandardServiceRegistryBuilder builder, String envVar, String setting) {
        String value = System.getenv(envVar);
        if (value != null && !value.isBlank()) {
            builder.applySetting(setting, value.trim());
        }
    }

    public static void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
            sessionFactory = null;
        }
    }
}
