package com.scs.util;

import com.scs.model.Complaint;
import com.scs.model.Complaint.Priority;
import org.junit.Test;

import static org.junit.Assert.*;

public class AutoReplyGeneratorTest {

    private Complaint make(String category, Priority priority, long id, int eta) {
        Complaint c = new Complaint();
        c.setId(id);
        c.setCategory(category);
        c.setPriority(priority);
        c.setEtaHours(eta);
        return c;
    }

    @Test
    public void waterHighMentionsRefIdAnd4Hours() {
        String reply = AutoReplyGenerator.generateReply(make("water", Priority.HIGH, 42, 24));
        assertTrue(reply.contains("#42"));
        assertTrue(reply.toLowerCase().contains("4 hours"));
    }

    @Test
    public void roadMediumMentionsReferenceAndETA() {
        String reply = AutoReplyGenerator.generateReply(make("road", Priority.MEDIUM, 7, 168));
        assertTrue(reply.contains("#7"));
    }

    @Test
    public void safetyCriticalMentionsEmergencyNotification() {
        String reply = AutoReplyGenerator.generateReply(make("safety", Priority.CRITICAL, 1, 4));
        assertTrue(reply.toLowerCase().contains("emergency") || reply.toLowerCase().contains("admin"));
    }

    @Test
    public void unknownCategoryUsesGenericFallback() {
        String reply = AutoReplyGenerator.generateReply(make("unknown-cat", Priority.MEDIUM, 99, 72));
        assertTrue(reply.contains("99"));
        assertTrue(reply.toLowerCase().contains("reference")
                || reply.toLowerCase().contains("resolution"));
    }

    @Test
    public void nullComplaintDoesNotThrow() {
        String reply = AutoReplyGenerator.generateReply(null);
        assertNotNull(reply);
        assertFalse(reply.isEmpty());
    }
}
