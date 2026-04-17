package com.scs.dao;

import com.scs.model.Notification;
import com.scs.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class NotificationDAO {

    public Long save(Notification n) {
        Transaction tx = null;
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            tx = s.beginTransaction();
            Long id = (Long) s.save(n);
            tx.commit();
            return id;
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw e;
        }
    }

    public List<Notification> findByUserId(Long userId) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            Query<Notification> q = s.createQuery(
                    "FROM Notification n WHERE n.user.id = :uid ORDER BY n.createdAt DESC",
                    Notification.class);
            q.setParameter("uid", userId);
            return q.list();
        }
    }

    public List<Notification> findUnreadByUserId(Long userId) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            Query<Notification> q = s.createQuery(
                    "FROM Notification n " +
                            "WHERE n.user.id = :uid AND n.isRead = FALSE " +
                            "ORDER BY n.createdAt DESC",
                    Notification.class);
            q.setParameter("uid", userId);
            return q.list();
        }
    }

    public void markAsRead(Long notificationId) {
        Transaction tx = null;
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            tx = s.beginTransaction();
            Notification n = s.get(Notification.class, notificationId);
            if (n != null) {
                n.setIsRead(Boolean.TRUE);
                s.update(n);
            }
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw e;
        }
    }

    public long countUnread(Long userId) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> q = s.createQuery(
                    "SELECT COUNT(n) FROM Notification n " +
                            "WHERE n.user.id = :uid AND n.isRead = FALSE",
                    Long.class);
            q.setParameter("uid", userId);
            Long v = q.uniqueResult();
            return v == null ? 0L : v;
        }
    }
}
