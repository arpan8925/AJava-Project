-- ============================================================
-- Smart Complaint Management System - Seed Data
-- Passwords are SHA-256 hex digests (matches PasswordUtil).
--   admin@scs.com       / admin123
--   officer-<dept>@scs.com / officer123
--   citizen-<n>@scs.com  / citizen123
-- ============================================================

USE smart_complaint_system;

SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE analytics_log;
TRUNCATE TABLE notifications;
TRUNCATE TABLE complaint_replies;
TRUNCATE TABLE complaints;
TRUNCATE TABLE departments;
TRUNCATE TABLE users;
SET FOREIGN_KEY_CHECKS = 1;

-- ============================================================
-- USERS
--   id=1 is a reserved "system" admin used as author of auto-replies.
-- ============================================================
INSERT INTO users (id, name, email, password, role, phone, area, is_active) VALUES
(1, 'System Admin', 'admin@scs.com', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'ADMIN', '9000000000', 'HQ', TRUE),

(2, 'Ravi Kumar',      'officer-water@scs.com',    '118b8d35a17bcf2c7d2d790509e12308dc6332c5d234f0098d2d6be6700bebb1', 'OFFICER', '9111111111', 'Sector 1', TRUE),
(3, 'Priya Sharma',    'officer-roads@scs.com',    '118b8d35a17bcf2c7d2d790509e12308dc6332c5d234f0098d2d6be6700bebb1', 'OFFICER', '9111111112', 'Sector 2', TRUE),
(4, 'Amit Patel',      'officer-electric@scs.com', '118b8d35a17bcf2c7d2d790509e12308dc6332c5d234f0098d2d6be6700bebb1', 'OFFICER', '9111111113', 'Sector 3', TRUE),
(5, 'Sneha Iyer',      'officer-sanitation@scs.com','118b8d35a17bcf2c7d2d790509e12308dc6332c5d234f0098d2d6be6700bebb1', 'OFFICER', '9111111114', 'Sector 4', TRUE),

(6,  'Rahul Mehta',    'citizen1@scs.com',  '4b4b4c19fdc4b422ca5a52085c3ba8fd2087c62afb06dae791f8fb9c51c56b4b', 'CITIZEN', '9222222221', 'Koramangala', TRUE),
(7,  'Anjali Desai',   'citizen2@scs.com',  '4b4b4c19fdc4b422ca5a52085c3ba8fd2087c62afb06dae791f8fb9c51c56b4b', 'CITIZEN', '9222222222', 'Indiranagar', TRUE),
(8,  'Vikram Singh',   'citizen3@scs.com',  '4b4b4c19fdc4b422ca5a52085c3ba8fd2087c62afb06dae791f8fb9c51c56b4b', 'CITIZEN', '9222222223', 'Whitefield',   TRUE),
(9,  'Neha Rao',       'citizen4@scs.com',  '4b4b4c19fdc4b422ca5a52085c3ba8fd2087c62afb06dae791f8fb9c51c56b4b', 'CITIZEN', '9222222224', 'Jayanagar',    TRUE),
(10, 'Karan Joshi',    'citizen5@scs.com',  '4b4b4c19fdc4b422ca5a52085c3ba8fd2087c62afb06dae791f8fb9c51c56b4b', 'CITIZEN', '9222222225', 'HSR Layout',   TRUE),
(11, 'Divya Nair',     'citizen6@scs.com',  '4b4b4c19fdc4b422ca5a52085c3ba8fd2087c62afb06dae791f8fb9c51c56b4b', 'CITIZEN', '9222222226', 'Koramangala',  TRUE),
(12, 'Suresh Babu',    'citizen7@scs.com',  '4b4b4c19fdc4b422ca5a52085c3ba8fd2087c62afb06dae791f8fb9c51c56b4b', 'CITIZEN', '9222222227', 'BTM Layout',   TRUE),
(13, 'Meera Pillai',   'citizen8@scs.com',  '4b4b4c19fdc4b422ca5a52085c3ba8fd2087c62afb06dae791f8fb9c51c56b4b', 'CITIZEN', '9222222228', 'Indiranagar',  TRUE);

-- ============================================================
-- DEPARTMENTS
-- ============================================================
INSERT INTO departments (id, name, description) VALUES
(1, 'Water Supply',            'Handles water supply, leakage, pipe, and tanker-service issues.'),
(2, 'Roads & Infrastructure',  'Handles potholes, road repairs, signage, and street conditions.'),
(3, 'Electricity',             'Handles power outages, faulty wiring, streetlights, and transformers.'),
(4, 'Sanitation',              'Handles garbage collection, waste management, and drainage.'),
(5, 'Public Safety',           'Handles emergencies, public safety, and hazardous situations.'),
(6, 'Parks & Recreation',      'Handles parks, gardens, playgrounds, and public spaces.');

-- ============================================================
-- COMPLAINTS (spanning the last 60 days)
-- ============================================================
INSERT INTO complaints
    (id, user_id, dept_id, title, description, summary, tags, category, sentiment, priority, status, location, eta_hours, is_emergency, created_at, resolved_at)
VALUES
(1, 6, 1, 'Severe water leakage near hostel',
   'There is severe water leakage near the hostel building. It is causing slippery roads and students are getting injured. The water has been flowing for three days now and nobody has fixed it.',
   'There is severe water leakage near the hostel building. The water has been flowing for three days now and nobody has fixed it.',
   'water,leakage,hostel,slippery,road', 'water', 'ANGRY', 'HIGH', 'RESOLVED', 'Koramangala', 24, FALSE,
   DATE_SUB(NOW(), INTERVAL 55 DAY), DATE_SUB(NOW(), INTERVAL 53 DAY)),

(2, 7, 2, 'Huge pothole on main road',
   'There is a huge pothole on the main road near the metro station. It is causing accidents and traffic jams every day.',
   'There is a huge pothole on the main road near the metro station.',
   'pothole,road,metro,traffic,accident', 'road', 'NEGATIVE', 'HIGH', 'RESOLVED', 'Indiranagar', 72, FALSE,
   DATE_SUB(NOW(), INTERVAL 48 DAY), DATE_SUB(NOW(), INTERVAL 44 DAY)),

(3, 8, 3, 'Power outage in our area',
   'Frequent power outages in our area. Every evening the electricity goes off for 2-3 hours.',
   'Frequent power outages in our area every evening.',
   'power,outage,electricity,evening', 'electricity', 'NEGATIVE', 'MEDIUM', 'RESOLVED', 'Whitefield', 48, FALSE,
   DATE_SUB(NOW(), INTERVAL 42 DAY), DATE_SUB(NOW(), INTERVAL 40 DAY)),

(4, 9, 4, 'Garbage not collected for a week',
   'Garbage has not been collected in our street for over a week. It is creating a terrible smell and attracting stray dogs.',
   'Garbage has not been collected in our street for over a week.',
   'garbage,waste,sanitation,smell,dogs', 'sanitation', 'NEGATIVE', 'MEDIUM', 'RESOLVED', 'Jayanagar', 48, FALSE,
   DATE_SUB(NOW(), INTERVAL 38 DAY), DATE_SUB(NOW(), INTERVAL 36 DAY)),

(5, 10, 2, 'Streetlight not working',
   'The streetlight in front of house number 42 has been off for two weeks. The area is unsafe at night.',
   'The streetlight has been off for two weeks. The area is unsafe at night.',
   'streetlight,night,unsafe,dark', 'road', 'NEGATIVE', 'MEDIUM', 'RESOLVED', 'HSR Layout', 72, FALSE,
   DATE_SUB(NOW(), INTERVAL 35 DAY), DATE_SUB(NOW(), INTERVAL 33 DAY)),

(6, 11, 1, 'Low water pressure in supply',
   'Very low water pressure in our building. Water barely trickles for the last few days. Please fix soon.',
   'Very low water pressure in our building.',
   'water,pressure,supply,building', 'water', 'NEUTRAL', 'MEDIUM', 'RESOLVED', 'Koramangala', 72, FALSE,
   DATE_SUB(NOW(), INTERVAL 30 DAY), DATE_SUB(NOW(), INTERVAL 28 DAY)),

(7, 12, 4, 'Open drainage issue',
   'The drainage in our street is open and overflowing. Children play nearby and it is a health hazard.',
   'The drainage is open and overflowing.',
   'drainage,overflow,health,hazard,children', 'sanitation', 'NEGATIVE', 'HIGH', 'RESOLVED', 'BTM Layout', 24, FALSE,
   DATE_SUB(NOW(), INTERVAL 27 DAY), DATE_SUB(NOW(), INTERVAL 25 DAY)),

(8, 13, 6, 'Park equipment broken',
   'The swings in the community park are broken and dangerous for children.',
   'The swings in the community park are broken.',
   'park,swing,broken,children,dangerous', 'parks', 'NEGATIVE', 'MEDIUM', 'RESOLVED', 'Indiranagar', 72, FALSE,
   DATE_SUB(NOW(), INTERVAL 25 DAY), DATE_SUB(NOW(), INTERVAL 22 DAY)),

(9, 6, 3, 'Transformer sparking',
   'The transformer near our house is sparking. This looks dangerous and could cause a fire.',
   'The transformer near our house is sparking.',
   'transformer,spark,electricity,fire,dangerous', 'electricity', 'NEGATIVE', 'CRITICAL', 'RESOLVED', 'Koramangala', 4, TRUE,
   DATE_SUB(NOW(), INTERVAL 22 DAY), DATE_SUB(NOW(), INTERVAL 21 DAY)),

(10, 7, 1, 'Pipe burst on main street',
   'A water pipe burst on the main street. Water is being wasted and the road is flooded.',
   'A water pipe burst on the main street.',
   'water,pipe,burst,road,flood', 'water', 'NEGATIVE', 'HIGH', 'IN_PROGRESS', 'Indiranagar', 24, FALSE,
   DATE_SUB(NOW(), INTERVAL 18 DAY), NULL),

(11, 8, 2, 'Broken road divider',
   'The road divider near the traffic signal is broken and creating confusion for drivers.',
   'The road divider is broken and creating confusion for drivers.',
   'road,divider,traffic,signal', 'road', 'NEUTRAL', 'MEDIUM', 'IN_PROGRESS', 'Whitefield', 72, FALSE,
   DATE_SUB(NOW(), INTERVAL 15 DAY), NULL),

(12, 9, 5, 'Fire in nearby building, emergency',
   'There is a fire in the building next to ours. This is an emergency! Please send help immediately.',
   'There is a fire in the building next to ours.',
   'fire,emergency,building,help', 'safety', 'ANGRY', 'CRITICAL', 'RESOLVED', 'Jayanagar', 4, TRUE,
   DATE_SUB(NOW(), INTERVAL 12 DAY), DATE_SUB(NOW(), INTERVAL 12 DAY)),

(13, 10, 4, 'Garbage dump near school',
   'There is an illegal garbage dump near the school. Kids walk past it every day.',
   'There is an illegal garbage dump near the school.',
   'garbage,dump,school,kids', 'sanitation', 'NEGATIVE', 'HIGH', 'ASSIGNED', 'HSR Layout', 48, FALSE,
   DATE_SUB(NOW(), INTERVAL 10 DAY), NULL),

(14, 11, 3, 'Streetlight flickering all night',
   'Streetlight flickers all night long, disturbing our sleep.',
   'Streetlight flickers all night long.',
   'streetlight,flicker,night,sleep', 'electricity', 'NEGATIVE', 'LOW', 'ASSIGNED', 'Koramangala', 168, FALSE,
   DATE_SUB(NOW(), INTERVAL 8 DAY), NULL),

(15, 12, 1, 'Contaminated water supply',
   'The water coming from the tap is brown and smells bad. Please check contamination.',
   'The water coming from the tap is brown and smells bad.',
   'water,contamination,brown,smell,tap', 'water', 'NEGATIVE', 'HIGH', 'ASSIGNED', 'BTM Layout', 24, FALSE,
   DATE_SUB(NOW(), INTERVAL 7 DAY), NULL),

(16, 13, 2, 'Please repair footpath',
   'Could you please repair the footpath near the bus stop. Thanks in advance.',
   'Could you please repair the footpath near the bus stop.',
   'footpath,repair,bus,stop', 'road', 'POSITIVE', 'LOW', 'PENDING', 'Indiranagar', 168, FALSE,
   DATE_SUB(NOW(), INTERVAL 5 DAY), NULL),

(17, 6, 4, 'Missed garbage pickup',
   'The garbage truck did not come this Monday. Please address.',
   'The garbage truck did not come this Monday.',
   'garbage,pickup,monday,missed', 'sanitation', 'NEUTRAL', 'LOW', 'PENDING', 'Koramangala', 168, FALSE,
   DATE_SUB(NOW(), INTERVAL 4 DAY), NULL),

(18, 7, 1, 'No water for two days',
   'We have had no water supply for two whole days. This is unacceptable.',
   'We have had no water supply for two whole days.',
   'water,supply,outage,days', 'water', 'ANGRY', 'HIGH', 'PENDING', 'Indiranagar', 24, FALSE,
   DATE_SUB(NOW(), INTERVAL 3 DAY), NULL),

(19, 8, 5, 'Building collapse risk',
   'The old building at corner of 4th street is leaning dangerously. Risk of collapse. Urgent attention needed.',
   'The old building at corner of 4th street is leaning dangerously.',
   'building,collapse,risk,urgent,dangerous', 'safety', 'ANGRY', 'CRITICAL', 'PENDING', 'Whitefield', 4, TRUE,
   DATE_SUB(NOW(), INTERVAL 2 DAY), NULL),

(20, 9, 6, 'Park lights not on',
   'Lights in the community park are off every evening. Walkers cannot exercise safely.',
   'Lights in the community park are off every evening.',
   'park,lights,evening,walk,exercise', 'parks', 'NEGATIVE', 'MEDIUM', 'PENDING', 'Jayanagar', 72, FALSE,
   DATE_SUB(NOW(), INTERVAL 2 DAY), NULL),

(21, 10, 3, 'Exposed live wire',
   'Live electrical wire hanging from pole. Could cause electrocution. This is an emergency!',
   'Live electrical wire hanging from pole.',
   'wire,electric,electrocution,emergency,pole', 'electricity', 'ANGRY', 'CRITICAL', 'PENDING', 'HSR Layout', 4, TRUE,
   DATE_SUB(NOW(), INTERVAL 1 DAY), NULL),

(22, 11, 2, 'Multiple potholes causing damage',
   'Multiple potholes on the new bypass road are damaging vehicle tires. Please fix.',
   'Multiple potholes on the new bypass road are damaging vehicle tires.',
   'pothole,bypass,road,tire,vehicle', 'road', 'NEGATIVE', 'HIGH', 'PENDING', 'Koramangala', 48, FALSE,
   DATE_SUB(NOW(), INTERVAL 1 DAY), NULL),

(23, 12, 4, 'Blocked sewer line',
   'The sewer line is completely blocked and sewage is overflowing onto the street. Horrible smell.',
   'The sewer line is completely blocked and sewage is overflowing.',
   'sewer,block,sewage,overflow,smell', 'sanitation', 'ANGRY', 'HIGH', 'PENDING', 'BTM Layout', 24, FALSE,
   DATE_SUB(NOW(), INTERVAL 1 DAY), NULL),

(24, 13, 1, 'Please adjust water timings',
   'Could the water supply timings be extended in the morning. Thanks!',
   'Could the water supply timings be extended in the morning.',
   'water,timing,morning,supply', 'water', 'POSITIVE', 'LOW', 'PENDING', 'Indiranagar', 168, FALSE,
   NOW(), NULL),

(25, 6, 3, 'Power cut without notice',
   'Sudden power cut without any prior notice. Disrupting work from home.',
   'Sudden power cut without any prior notice.',
   'power,cut,notice,work,home', 'electricity', 'NEGATIVE', 'MEDIUM', 'PENDING', 'Koramangala', 72, FALSE,
   NOW(), NULL);

-- ============================================================
-- COMPLAINT REPLIES (author id=1 represents system/admin auto-replies)
-- ============================================================
INSERT INTO complaint_replies (complaint_id, user_id, message, created_at) VALUES
(1, 1, 'Thank you for your complaint. Your reference ID is 1. Estimated resolution: 24 hours.', DATE_SUB(NOW(), INTERVAL 55 DAY)),
(1, 2, 'Team dispatched. Leak has been fixed and road cleared.', DATE_SUB(NOW(), INTERVAL 53 DAY)),
(2, 1, 'Your road maintenance complaint has been logged. Expected resolution within 7 days.', DATE_SUB(NOW(), INTERVAL 48 DAY)),
(2, 3, 'Pothole filled. Please report if any further damage is observed.', DATE_SUB(NOW(), INTERVAL 44 DAY)),
(9, 1, 'We have received your urgent electricity-related complaint. A team will be dispatched within 4 hours.', DATE_SUB(NOW(), INTERVAL 22 DAY)),
(9, 4, 'Transformer has been isolated and repaired. Area is safe now.', DATE_SUB(NOW(), INTERVAL 21 DAY)),
(12, 1, 'Emergency detected. Public Safety team has been notified immediately.', DATE_SUB(NOW(), INTERVAL 12 DAY)),
(10, 1, 'We have received your urgent water-related complaint. A team will be dispatched within 4 hours.', DATE_SUB(NOW(), INTERVAL 18 DAY)),
(10, 2, 'Excavation in progress. Expected fix in the next 24 hours.', DATE_SUB(NOW(), INTERVAL 16 DAY)),
(13, 1, 'Thank you for your complaint. A sanitation team will visit within 48 hours.', DATE_SUB(NOW(), INTERVAL 10 DAY));

-- ============================================================
-- NOTIFICATIONS
-- ============================================================
INSERT INTO notifications (user_id, message, is_read, created_at) VALUES
(6, 'Your complaint #1 has been marked RESOLVED.',   TRUE,  DATE_SUB(NOW(), INTERVAL 53 DAY)),
(7, 'Your complaint #2 has been marked RESOLVED.',   TRUE,  DATE_SUB(NOW(), INTERVAL 44 DAY)),
(6, 'Your complaint #9 has been marked RESOLVED.',   TRUE,  DATE_SUB(NOW(), INTERVAL 21 DAY)),
(7, 'Your complaint #10 is now IN_PROGRESS.',        FALSE, DATE_SUB(NOW(), INTERVAL 16 DAY)),
(10,'Your complaint #13 has been ASSIGNED.',         FALSE, DATE_SUB(NOW(), INTERVAL 10 DAY)),
(1, 'Emergency complaint #19 filed. Please review.', FALSE, DATE_SUB(NOW(), INTERVAL 2 DAY)),
(1, 'Emergency complaint #21 filed. Please review.', FALSE, DATE_SUB(NOW(), INTERVAL 1 DAY));

-- ============================================================
-- ANALYTICS LOG (event history spanning 60 days)
-- ============================================================
INSERT INTO analytics_log (event_type, complaint_id, dept_id, location, created_at) VALUES
('COMPLAINT_CREATED', 1, 1, 'Koramangala',  DATE_SUB(NOW(), INTERVAL 55 DAY)),
('COMPLAINT_RESOLVED',1, 1, 'Koramangala',  DATE_SUB(NOW(), INTERVAL 53 DAY)),
('COMPLAINT_CREATED', 2, 2, 'Indiranagar',  DATE_SUB(NOW(), INTERVAL 48 DAY)),
('COMPLAINT_RESOLVED',2, 2, 'Indiranagar',  DATE_SUB(NOW(), INTERVAL 44 DAY)),
('COMPLAINT_CREATED', 3, 3, 'Whitefield',   DATE_SUB(NOW(), INTERVAL 42 DAY)),
('COMPLAINT_RESOLVED',3, 3, 'Whitefield',   DATE_SUB(NOW(), INTERVAL 40 DAY)),
('COMPLAINT_CREATED', 4, 4, 'Jayanagar',    DATE_SUB(NOW(), INTERVAL 38 DAY)),
('COMPLAINT_RESOLVED',4, 4, 'Jayanagar',    DATE_SUB(NOW(), INTERVAL 36 DAY)),
('COMPLAINT_CREATED', 5, 2, 'HSR Layout',   DATE_SUB(NOW(), INTERVAL 35 DAY)),
('COMPLAINT_RESOLVED',5, 2, 'HSR Layout',   DATE_SUB(NOW(), INTERVAL 33 DAY)),
('COMPLAINT_CREATED', 6, 1, 'Koramangala',  DATE_SUB(NOW(), INTERVAL 30 DAY)),
('COMPLAINT_RESOLVED',6, 1, 'Koramangala',  DATE_SUB(NOW(), INTERVAL 28 DAY)),
('COMPLAINT_CREATED', 7, 4, 'BTM Layout',   DATE_SUB(NOW(), INTERVAL 27 DAY)),
('COMPLAINT_RESOLVED',7, 4, 'BTM Layout',   DATE_SUB(NOW(), INTERVAL 25 DAY)),
('COMPLAINT_CREATED', 8, 6, 'Indiranagar',  DATE_SUB(NOW(), INTERVAL 25 DAY)),
('COMPLAINT_RESOLVED',8, 6, 'Indiranagar',  DATE_SUB(NOW(), INTERVAL 22 DAY)),
('COMPLAINT_CREATED', 9, 3, 'Koramangala',  DATE_SUB(NOW(), INTERVAL 22 DAY)),
('PRIORITY_BOOSTED',  9, 3, 'Koramangala',  DATE_SUB(NOW(), INTERVAL 22 DAY)),
('COMPLAINT_RESOLVED',9, 3, 'Koramangala',  DATE_SUB(NOW(), INTERVAL 21 DAY)),
('COMPLAINT_CREATED',10, 1, 'Indiranagar',  DATE_SUB(NOW(), INTERVAL 18 DAY)),
('COMPLAINT_ASSIGNED',10,1, 'Indiranagar',  DATE_SUB(NOW(), INTERVAL 18 DAY)),
('COMPLAINT_CREATED',11, 2, 'Whitefield',   DATE_SUB(NOW(), INTERVAL 15 DAY)),
('COMPLAINT_ASSIGNED',11,2, 'Whitefield',   DATE_SUB(NOW(), INTERVAL 15 DAY)),
('COMPLAINT_CREATED',12, 5, 'Jayanagar',    DATE_SUB(NOW(), INTERVAL 12 DAY)),
('PRIORITY_BOOSTED', 12, 5, 'Jayanagar',    DATE_SUB(NOW(), INTERVAL 12 DAY)),
('COMPLAINT_RESOLVED',12,5, 'Jayanagar',    DATE_SUB(NOW(), INTERVAL 12 DAY)),
('COMPLAINT_CREATED',13, 4, 'HSR Layout',   DATE_SUB(NOW(), INTERVAL 10 DAY)),
('COMPLAINT_ASSIGNED',13,4, 'HSR Layout',   DATE_SUB(NOW(), INTERVAL 10 DAY)),
('COMPLAINT_CREATED',14, 3, 'Koramangala',  DATE_SUB(NOW(), INTERVAL 8 DAY)),
('COMPLAINT_ASSIGNED',14,3, 'Koramangala',  DATE_SUB(NOW(), INTERVAL 8 DAY)),
('COMPLAINT_CREATED',15, 1, 'BTM Layout',   DATE_SUB(NOW(), INTERVAL 7 DAY)),
('COMPLAINT_ASSIGNED',15,1, 'BTM Layout',   DATE_SUB(NOW(), INTERVAL 7 DAY)),
('COMPLAINT_CREATED',16, 2, 'Indiranagar',  DATE_SUB(NOW(), INTERVAL 5 DAY)),
('COMPLAINT_CREATED',17, 4, 'Koramangala',  DATE_SUB(NOW(), INTERVAL 4 DAY)),
('COMPLAINT_CREATED',18, 1, 'Indiranagar',  DATE_SUB(NOW(), INTERVAL 3 DAY)),
('COMPLAINT_CREATED',19, 5, 'Whitefield',   DATE_SUB(NOW(), INTERVAL 2 DAY)),
('PRIORITY_BOOSTED', 19, 5, 'Whitefield',   DATE_SUB(NOW(), INTERVAL 2 DAY)),
('COMPLAINT_CREATED',20, 6, 'Jayanagar',    DATE_SUB(NOW(), INTERVAL 2 DAY)),
('COMPLAINT_CREATED',21, 3, 'HSR Layout',   DATE_SUB(NOW(), INTERVAL 1 DAY)),
('PRIORITY_BOOSTED', 21, 3, 'HSR Layout',   DATE_SUB(NOW(), INTERVAL 1 DAY)),
('COMPLAINT_CREATED',22, 2, 'Koramangala',  DATE_SUB(NOW(), INTERVAL 1 DAY)),
('COMPLAINT_CREATED',23, 4, 'BTM Layout',   DATE_SUB(NOW(), INTERVAL 1 DAY)),
('COMPLAINT_CREATED',24, 1, 'Indiranagar',  NOW()),
('COMPLAINT_CREATED',25, 3, 'Koramangala',  NOW());
