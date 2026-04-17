package com.scs.dao;

import com.scs.model.ComplaintReply;
import com.scs.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class ComplaintReplyDAO {

    public Long save(ComplaintReply r) {
        Transaction tx = null;
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            tx = s.beginTransaction();
            Long id = (Long) s.save(r);
            tx.commit();
            return id;
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw e;
        }
    }

    public List<ComplaintReply> findByComplaintId(Long complaintId) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            Query<ComplaintReply> q = s.createQuery(
                    "FROM ComplaintReply r WHERE r.complaint.id = :cid ORDER BY r.createdAt ASC",
                    ComplaintReply.class);
            q.setParameter("cid", complaintId);
            return q.list();
        }
    }
}
