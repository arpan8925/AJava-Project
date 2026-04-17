package com.scs.dao;

import com.scs.model.User;
import com.scs.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.Collections;
import java.util.List;

public class UserDAO {

    public Long save(User user) {
        Transaction tx = null;
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            tx = s.beginTransaction();
            Long id = (Long) s.save(user);
            tx.commit();
            return id;
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw e;
        }
    }

    public void update(User user) {
        Transaction tx = null;
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            tx = s.beginTransaction();
            s.update(user);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw e;
        }
    }

    public User findById(Long id) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            return s.get(User.class, id);
        }
    }

    public User findByEmail(String email) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            Query<User> q = s.createQuery("FROM User u WHERE u.email = :email", User.class);
            q.setParameter("email", email);
            return q.uniqueResult();
        }
    }

    public List<User> findByRole(User.Role role) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            Query<User> q = s.createQuery(
                    "FROM User u WHERE u.role = :role ORDER BY u.name", User.class);
            q.setParameter("role", role);
            return q.list();
        }
    }

    public List<User> findAll() {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            return s.createQuery("FROM User u ORDER BY u.id", User.class).list();
        }
    }

    public long countByRole(User.Role role) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> q = s.createQuery(
                    "SELECT COUNT(u) FROM User u WHERE u.role = :role", Long.class);
            q.setParameter("role", role);
            Long v = q.uniqueResult();
            return v == null ? 0L : v;
        }
    }

    public List<User> findAdmins() {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            Query<User> q = s.createQuery(
                    "FROM User u WHERE u.role = :role", User.class);
            q.setParameter("role", User.Role.ADMIN);
            return q.list();
        }
    }

    public void delete(Long id) {
        Transaction tx = null;
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            tx = s.beginTransaction();
            User u = s.get(User.class, id);
            if (u != null) s.delete(u);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw e;
        }
    }
}
