package com.scs.dao;

import com.scs.model.Complaint;
import com.scs.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ComplaintDAO {

    public Long save(Complaint c) {
        Transaction tx = null;
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            tx = s.beginTransaction();
            Long id = (Long) s.save(c);
            tx.commit();
            return id;
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw e;
        }
    }

    public void update(Complaint c) {
        Transaction tx = null;
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            tx = s.beginTransaction();
            s.update(c);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw e;
        }
    }

    public Complaint findById(Long id) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            return s.get(Complaint.class, id);
        }
    }

    public List<Complaint> findByUserId(Long userId) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            Query<Complaint> q = s.createQuery(
                    "FROM Complaint c WHERE c.user.id = :uid ORDER BY c.createdAt DESC",
                    Complaint.class);
            q.setParameter("uid", userId);
            return q.list();
        }
    }

    public List<Complaint> findByDepartmentId(Long deptId) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            Query<Complaint> q = s.createQuery(
                    "FROM Complaint c WHERE c.department.id = :did ORDER BY c.createdAt DESC",
                    Complaint.class);
            q.setParameter("did", deptId);
            return q.list();
        }
    }

    public List<Complaint> findByStatus(Complaint.Status status) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            Query<Complaint> q = s.createQuery(
                    "FROM Complaint c WHERE c.status = :st ORDER BY c.createdAt DESC",
                    Complaint.class);
            q.setParameter("st", status);
            return q.list();
        }
    }

    public List<Complaint> findAll(int page, int pageSize) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            Query<Complaint> q = s.createQuery(
                    "FROM Complaint c ORDER BY c.createdAt DESC", Complaint.class);
            q.setFirstResult(Math.max(0, (page - 1) * pageSize));
            q.setMaxResults(pageSize);
            return q.list();
        }
    }

    public long countAll() {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            Long v = s.createQuery("SELECT COUNT(c) FROM Complaint c", Long.class).uniqueResult();
            return v == null ? 0L : v;
        }
    }

    public long countByStatus(Complaint.Status status) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> q = s.createQuery(
                    "SELECT COUNT(c) FROM Complaint c WHERE c.status = :st", Long.class);
            q.setParameter("st", status);
            Long v = q.uniqueResult();
            return v == null ? 0L : v;
        }
    }

    public long countByDepartment(Long deptId) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> q = s.createQuery(
                    "SELECT COUNT(c) FROM Complaint c WHERE c.department.id = :did", Long.class);
            q.setParameter("did", deptId);
            Long v = q.uniqueResult();
            return v == null ? 0L : v;
        }
    }

    public long countOpenByDepartment(Long deptId) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> q = s.createQuery(
                    "SELECT COUNT(c) FROM Complaint c " +
                            "WHERE c.department.id = :did AND c.status NOT IN (:done)",
                    Long.class);
            q.setParameter("did", deptId);
            List<Complaint.Status> done = new ArrayList<>();
            done.add(Complaint.Status.RESOLVED);
            done.add(Complaint.Status.CLOSED);
            q.setParameterList("done", done);
            Long v = q.uniqueResult();
            return v == null ? 0L : v;
        }
    }

    public long countByUserSince(Long userId, Date since) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> q = s.createQuery(
                    "SELECT COUNT(c) FROM Complaint c " +
                            "WHERE c.user.id = :uid AND c.createdAt >= :since",
                    Long.class);
            q.setParameter("uid", userId);
            q.setParameter("since", since);
            Long v = q.uniqueResult();
            return v == null ? 0L : v;
        }
    }

    public long countPendingByUserOlderThan(Long userId, Date cutoff) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> q = s.createQuery(
                    "SELECT COUNT(c) FROM Complaint c " +
                            "WHERE c.user.id = :uid AND c.status = :st AND c.createdAt < :cut",
                    Long.class);
            q.setParameter("uid", userId);
            q.setParameter("st", Complaint.Status.PENDING);
            q.setParameter("cut", cutoff);
            Long v = q.uniqueResult();
            return v == null ? 0L : v;
        }
    }

    public long countByDateRange(Date from, Date to) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> q = s.createQuery(
                    "SELECT COUNT(c) FROM Complaint c " +
                            "WHERE c.createdAt BETWEEN :f AND :t", Long.class);
            q.setParameter("f", from);
            q.setParameter("t", to);
            Long v = q.uniqueResult();
            return v == null ? 0L : v;
        }
    }

    public List<Object[]> countByLocation() {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            return s.createQuery(
                    "SELECT c.location, COUNT(c) FROM Complaint c " +
                            "WHERE c.location IS NOT NULL " +
                            "GROUP BY c.location ORDER BY COUNT(c) DESC",
                    Object[].class).list();
        }
    }

    public List<Object[]> countByDepartmentGrouped() {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            return s.createQuery(
                    "SELECT c.department.name, COUNT(c) FROM Complaint c " +
                            "WHERE c.department IS NOT NULL " +
                            "GROUP BY c.department.name ORDER BY COUNT(c) DESC",
                    Object[].class).list();
        }
    }

    public List<Object[]> countByPriority() {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            return s.createQuery(
                    "SELECT c.priority, COUNT(c) FROM Complaint c " +
                            "GROUP BY c.priority", Object[].class).list();
        }
    }

    public List<Complaint> findSimilar(List<String> keywords, Long excludeId, int limit) {
        if (keywords == null || keywords.isEmpty()) return new ArrayList<>();
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            StringBuilder hql = new StringBuilder("FROM Complaint c WHERE c.id <> :ex AND (");
            for (int i = 0; i < keywords.size(); i++) {
                if (i > 0) hql.append(" OR ");
                hql.append("LOWER(c.title) LIKE :k").append(i)
                   .append(" OR LOWER(c.description) LIKE :k").append(i);
            }
            hql.append(") ORDER BY c.createdAt DESC");

            Query<Complaint> q = s.createQuery(hql.toString(), Complaint.class);
            q.setParameter("ex", excludeId == null ? -1L : excludeId);
            for (int i = 0; i < keywords.size(); i++) {
                q.setParameter("k" + i, "%" + keywords.get(i).toLowerCase() + "%");
            }
            q.setMaxResults(limit);
            return q.list();
        }
    }

    public List<Complaint> findEmergencies() {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            return s.createQuery(
                    "FROM Complaint c WHERE c.isEmergency = TRUE ORDER BY c.createdAt DESC",
                    Complaint.class).list();
        }
    }

    public Date daysAgo(int days) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, -days);
        return c.getTime();
    }
}
