package com.scs.dao;

import com.scs.model.Department;
import com.scs.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class DepartmentDAO {

    public Long save(Department d) {
        Transaction tx = null;
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            tx = s.beginTransaction();
            Long id = (Long) s.save(d);
            tx.commit();
            return id;
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw e;
        }
    }

    public void update(Department d) {
        Transaction tx = null;
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            tx = s.beginTransaction();
            s.update(d);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw e;
        }
    }

    public Department findById(Long id) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            return s.get(Department.class, id);
        }
    }

    public Department findByName(String name) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            Query<Department> q = s.createQuery(
                    "FROM Department d WHERE d.name = :n", Department.class);
            q.setParameter("n", name);
            return q.uniqueResult();
        }
    }

    public List<Department> findAll() {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            return s.createQuery(
                    "FROM Department d ORDER BY d.name", Department.class).list();
        }
    }

    public void delete(Long id) {
        Transaction tx = null;
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            tx = s.beginTransaction();
            Department d = s.get(Department.class, id);
            if (d != null) s.delete(d);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw e;
        }
    }
}
