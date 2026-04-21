package com.scs.listener;

import com.scs.util.HibernateUtil;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            HibernateUtil.getSessionFactory();
            sce.getServletContext().log("Hibernate SessionFactory initialized.");
        } catch (Throwable t) {
            sce.getServletContext().log(
                "Hibernate SessionFactory not initialized (DB unavailable?). "
                + "UI will load; DB-backed actions will fail until connection works. Cause: "
                + t.getMessage());
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        HibernateUtil.shutdown();
        sce.getServletContext().log("Hibernate SessionFactory closed.");
    }
}
