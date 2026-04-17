package com.scs.dao;

import com.scs.model.AnalyticsLog;
import com.scs.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.Date;
import java.util.List;

public class AnalyticsLogDAO {

    public Long save(AnalyticsLog log) {
        Transaction tx = null;
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            tx = s.beginTransaction();
            Long id = (Long) s.save(log);
            tx.commit();
            return id;
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw e;
        }
    }

    public long countByDateRange(Date from, Date to) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> q = s.createQuery(
                    "SELECT COUNT(a) FROM AnalyticsLog a " +
                            "WHERE a.createdAt BETWEEN :f AND :t", Long.class);
            q.setParameter("f", from);
            q.setParameter("t", to);
            Long v = q.uniqueResult();
            return v == null ? 0L : v;
        }
    }

    public long countByEventType(String eventType) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> q = s.createQuery(
                    "SELECT COUNT(a) FROM AnalyticsLog a WHERE a.eventType = :et", Long.class);
            q.setParameter("et", eventType);
            Long v = q.uniqueResult();
            return v == null ? 0L : v;
        }
    }

    public List<Object[]> countByDepartment() {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            return s.createQuery(
                    "SELECT a.deptId, COUNT(a) FROM AnalyticsLog a " +
                            "WHERE a.deptId IS NOT NULL " +
                            "GROUP BY a.deptId ORDER BY COUNT(a) DESC",
                    Object[].class).list();
        }
    }

    public List<Object[]> countByLocation() {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            return s.createQuery(
                    "SELECT a.location, COUNT(a) FROM AnalyticsLog a " +
                            "WHERE a.location IS NOT NULL " +
                            "GROUP BY a.location ORDER BY COUNT(a) DESC",
                    Object[].class).list();
        }
    }

    public List<AnalyticsLog> findRecent(int limit) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            Query<AnalyticsLog> q = s.createQuery(
                    "FROM AnalyticsLog a ORDER BY a.createdAt DESC", AnalyticsLog.class);
            q.setMaxResults(limit);
            return q.list();
        }
    }
}
