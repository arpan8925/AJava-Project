# Smart Complaint Management System - Complete Project Plan

## Course: Advanced Java (6th Semester CSE)
## Team Size: 3 Members

---

## 1. Context & Objective

Current complaint management systems are manual, slow, and lack intelligent prioritization. Citizens have no visibility into complaint status, and departments lack analytics to allocate resources.

**Objective:** Build an intelligent web-based complaint management system that automates triage, prioritization, tracking, and resolution using 13 AI-like smart features implemented with pure Java logic (no external AI APIs).

---

## 2. Technology Stack

| Layer | Technology |
|---|---|
| **Frontend/UI** | JSP + JSTL + Bootstrap 5 + Chart.js |
| **Backend** | Java Servlets 3.1 + Hibernate 5.6 (ORM, `javax.persistence`) |
| **Database** | MySQL |
| **Server** | Apache Tomcat 9 |
| **Build Tool** | Maven |
| **Voice Input** | Browser Web Speech API (JavaScript) |
| **Charts** | Chart.js (via CDN) |

**Why this stack:** Aligns with syllabus requirements - JSP for frontend, Servlets for backend, Hibernate for ORM, JDBC/MySQL for database connectivity.

---

## 3. System Roles

| Role | Capabilities |
|---|---|
| **Citizen** | Register, login, submit complaints (with voice/image), track status, view history, receive notifications |
| **Admin** | Manage departments, manage users, assign complaints to departments, view analytics/predictions, geo-analytics |
| **Department Officer** | View assigned complaints, update status (In Progress/Resolved), reply to complaints |

---

## 4. Project Structure

```
SmartComplaintSystem/
├── pom.xml
├── schema.sql
├── seed.sql
├── src/
│   └── main/
│       ├── java/com/scs/
│       │   ├── model/                     # Hibernate Entities
│       │   │   ├── User.java
│       │   │   ├── Department.java
│       │   │   ├── Complaint.java
│       │   │   ├── ComplaintReply.java
│       │   │   ├── Notification.java
│       │   │   └── AnalyticsLog.java
│       │   │
│       │   ├── dao/                       # Data Access Objects
│       │   │   ├── UserDAO.java
│       │   │   ├── DepartmentDAO.java
│       │   │   ├── ComplaintDAO.java
│       │   │   ├── ComplaintReplyDAO.java
│       │   │   ├── NotificationDAO.java
│       │   │   └── AnalyticsLogDAO.java
│       │   │
│       │   ├── servlet/                   # Controllers (22 Servlets)
│       │   │   ├── LoginServlet.java
│       │   │   ├── RegisterServlet.java
│       │   │   ├── LogoutServlet.java
│       │   │   ├── UserDashboardServlet.java
│       │   │   ├── SubmitComplaintServlet.java
│       │   │   ├── TrackComplaintServlet.java
│       │   │   ├── MyComplaintsServlet.java
│       │   │   ├── ComplaintReplyServlet.java
│       │   │   ├── AdminDashboardServlet.java
│       │   │   ├── ManageDepartmentsServlet.java
│       │   │   ├── ManageUsersServlet.java
│       │   │   ├── AssignComplaintServlet.java
│       │   │   ├── AdminComplaintsServlet.java
│       │   │   ├── AnalyticsServlet.java
│       │   │   ├── GeoAnalyticsServlet.java
│       │   │   ├── OfficerDashboardServlet.java
│       │   │   ├── AssignedComplaintsServlet.java
│       │   │   ├── ResolveComplaintServlet.java
│       │   │   ├── NotificationServlet.java
│       │   │   ├── ImageUploadServlet.java
│       │   │   ├── SimilarComplaintsServlet.java
│       │   │   └── SmartSuggestServlet.java
│       │   │
│       │   ├── filter/
│       │   │   └── AuthFilter.java        # Authentication + Role-based access
│       │   │
│       │   ├── listener/
│       │   │   └── AppContextListener.java # Hibernate SessionFactory lifecycle
│       │   │
│       │   └── util/                      # Utility & Smart Feature Classes
│       │       ├── HibernateUtil.java
│       │       ├── PasswordUtil.java
│       │       ├── FileUploadUtil.java
│       │       ├── SmartSummarizer.java
│       │       ├── SentimentAnalyzer.java
│       │       ├── TagGenerator.java
│       │       ├── EmergencyDetector.java
│       │       ├── ETAPredictor.java
│       │       ├── PriorityBooster.java
│       │       ├── SmartRecommender.java
│       │       ├── AutoReplyGenerator.java
│       │       └── PredictiveAnalytics.java
│       │
│       ├── resources/
│       │   └── hibernate.cfg.xml
│       │
│       └── webapp/
│           ├── WEB-INF/web.xml
│           ├── index.jsp                  # Landing page
│           ├── css/
│           │   └── style.css
│           ├── js/
│           │   ├── voice-input.js         # Web Speech API
│           │   ├── charts.js              # Chart.js configurations
│           │   ├── complaint-form.js      # Form validation, AJAX, image preview
│           │   └── notifications.js       # Notification polling
│           ├── images/
│           └── jsp/
│               ├── common/
│               │   ├── header.jsp
│               │   ├── navbar.jsp         # Role-aware navigation
│               │   ├── footer.jsp
│               │   └── sidebar.jsp
│               ├── auth/
│               │   ├── login.jsp
│               │   └── register.jsp
│               ├── user/
│               │   ├── dashboard.jsp
│               │   ├── submit-complaint.jsp  # Most feature-rich page
│               │   ├── my-complaints.jsp
│               │   ├── track-complaint.jsp
│               │   └── profile.jsp
│               ├── admin/
│               │   ├── dashboard.jsp      # Charts + stats
│               │   ├── complaints.jsp
│               │   ├── departments.jsp
│               │   ├── users.jsp
│               │   ├── analytics.jsp
│               │   └── geo-analytics.jsp
│               ├── officer/
│               │   ├── dashboard.jsp
│               │   ├── complaints.jsp
│               │   └── resolve.jsp
│               └── error/
│                   ├── 404.jsp
│                   ├── 500.jsp
│                   └── access-denied.jsp
```

---

## 5. Database Schema Design (6 Tables)

### ER Diagram Relationships
```
users (1) ──────< (many) complaints
departments (1) ──────< (many) complaints
complaints (1) ──────< (many) complaint_replies
users (1) ──────< (many) complaint_replies
users (1) ──────< (many) notifications
```

### Table Definitions

```sql
CREATE DATABASE smart_complaint_system;
USE smart_complaint_system;

-- ============================================================
-- USERS TABLE
-- ============================================================
CREATE TABLE users (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    email       VARCHAR(150) NOT NULL UNIQUE,
    password    VARCHAR(64)  NOT NULL,          -- SHA-256 hex digest
    role        ENUM('CITIZEN', 'ADMIN', 'OFFICER') NOT NULL DEFAULT 'CITIZEN',
    phone       VARCHAR(15),
    area        VARCHAR(100),                   -- locality / area name
    is_active   BOOLEAN DEFAULT TRUE,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- DEPARTMENTS TABLE
-- ============================================================
CREATE TABLE departments (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(500)
);

-- ============================================================
-- COMPLAINTS TABLE (central table)
-- ============================================================
CREATE TABLE complaints (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id      BIGINT NOT NULL,
    dept_id      BIGINT,                         -- NULL until assigned
    title        VARCHAR(200) NOT NULL,
    description  TEXT NOT NULL,
    summary      VARCHAR(500),                   -- auto-generated by SmartSummarizer
    tags         VARCHAR(300),                   -- comma-separated auto-tags
    category     VARCHAR(100),
    sentiment    VARCHAR(20),                    -- ANGRY, NEGATIVE, NEUTRAL, POSITIVE
    priority     ENUM('LOW','MEDIUM','HIGH','CRITICAL') DEFAULT 'MEDIUM',
    status       ENUM('PENDING','ASSIGNED','IN_PROGRESS','RESOLVED','CLOSED') DEFAULT 'PENDING',
    image_path   VARCHAR(300),
    location     VARCHAR(150),
    eta_hours    INT,                            -- predicted resolution hours
    is_emergency BOOLEAN DEFAULT FALSE,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    resolved_at  TIMESTAMP NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (dept_id) REFERENCES departments(id)
);

CREATE INDEX idx_complaints_user     ON complaints(user_id);
CREATE INDEX idx_complaints_dept     ON complaints(dept_id);
CREATE INDEX idx_complaints_status   ON complaints(status);
CREATE INDEX idx_complaints_priority ON complaints(priority);
CREATE INDEX idx_complaints_location ON complaints(location);
CREATE INDEX idx_complaints_created  ON complaints(created_at);

-- ============================================================
-- COMPLAINT REPLIES
-- ============================================================
CREATE TABLE complaint_replies (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    complaint_id  BIGINT NOT NULL,
    user_id       BIGINT NOT NULL,               -- system user (id=1) for auto-replies
    message       TEXT NOT NULL,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (complaint_id) REFERENCES complaints(id),
    FOREIGN KEY (user_id)      REFERENCES users(id)
);

-- ============================================================
-- NOTIFICATIONS
-- ============================================================
CREATE TABLE notifications (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id    BIGINT NOT NULL,
    message    VARCHAR(500) NOT NULL,
    is_read    BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE INDEX idx_notif_user ON notifications(user_id, is_read);

-- ============================================================
-- ANALYTICS LOG (event sourcing for analytics)
-- ============================================================
CREATE TABLE analytics_log (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    event_type    VARCHAR(50) NOT NULL,          -- COMPLAINT_CREATED, RESOLVED, ASSIGNED, PRIORITY_BOOSTED
    complaint_id  BIGINT,
    dept_id       BIGINT,
    location      VARCHAR(150),
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_analytics_date ON analytics_log(created_at);
CREATE INDEX idx_analytics_type ON analytics_log(event_type);
```

### Seed Data Includes
- 1 Admin user (admin@scs.com / admin123)
- 6 Departments: Water Supply, Roads & Infrastructure, Electricity, Sanitation, Public Safety, Parks & Recreation
- 3-4 Officers (one per department)
- 5-10 Citizen users
- 20-30 sample complaints (various categories, statuses, priorities)
- Sample replies and analytics log entries spanning 60 days

---

## 6. All 13 Smart Features - Detailed Implementation

### Feature 1: Smart Complaint Summarization
**File:** `SmartSummarizer.java`

**Algorithm:**
1. Maintain a `Set<String>` of ~200 English stop words (a, an, the, is, was, in, on, at, etc.)
2. Split complaint text into sentences (split on `.`, `!`, `?`)
3. Score each sentence = count of non-stop-words longer than 3 characters
4. Return top 1-2 sentences by score
5. If text < 50 words, return as-is

**Method:** `public static String summarize(String text, int maxSentences)`

**Example:**
- Input: "There is severe water leakage near the hostel building. It is causing slippery roads and students are getting injured. The water has been flowing for three days now and nobody has fixed it."
- Output: "There is severe water leakage near the hostel building. The water has been flowing for three days now and nobody has fixed it."

---

### Feature 2: Sentiment Analysis
**File:** `SentimentAnalyzer.java`

**Algorithm:**
1. Three keyword dictionaries (~80 words total):
   - `negativeWords`: "terrible"=-2, "worst"=-2, "angry"=-1, "frustrated"=-1, "disgusting"=-2, "horrible"=-2, "useless"=-1, "pathetic"=-2 ...
   - `positiveWords`: "good"=1, "thanks"=1, "appreciate"=1, "please"=1 ...
   - `urgentWords`: "urgent"=-2, "emergency"=-3, "immediately"=-2, "asap"=-2 ...
2. Tokenize text (lowercase, split on non-alpha), sum scores
3. Classification:
   - score <= -5 → **ANGRY** → Priority HIGH
   - score <= -2 → **NEGATIVE** → Priority MEDIUM
   - score >= 1 → **POSITIVE** → Priority LOW
   - else → **NEUTRAL** → Priority MEDIUM

**Method:** `public static SentimentResult analyze(String text)`

---

### Feature 3: Smart Recommendation
**File:** `SmartRecommender.java`

**Algorithm:**
- `Map<String, List<String>>` mapping category keywords to solution templates:

| Keywords | Suggested Solutions |
|---|---|
| water, pipe, supply, leak | "Contact water department at ext. 201", "Check area-wide outage status", "Request temporary tanker service" |
| road, pothole, traffic | "Road repair team dispatched within 48hrs", "Use alternate route" |
| electricity, power, outage | "Check scheduled maintenance calendar", "Report to electricity board: 1800-XXX" |
| garbage, waste, sanitation | "Collection schedule: Mon/Thu", "Raise missed pickup complaint" |
| (8-10 categories total) | ... |

**Method:** `public static List<String> recommend(String title, String description, String category)`

**Displayed on:** `track-complaint.jsp` in a "Suggested Solutions" card via AJAX.

---

### Feature 4: Auto Similar Complaint Detection
**Location:** `ComplaintDAO.java` + `SimilarComplaintsServlet.java`

**Algorithm:**
1. Extract top 3-4 non-stop-words from complaint title
2. Build HQL query: `WHERE (title LIKE '%keyword1%' OR description LIKE '%keyword1%' OR title LIKE '%keyword2%') AND id != :excludeId ORDER BY createdAt DESC`
3. Return top 3-5 similar complaints

**Displayed as:** "Similar Past Complaints" sidebar on `track-complaint.jsp` showing title, status, and resolution date.

---

### Feature 5: Predictive Complaint Analytics
**File:** `PredictiveAnalytics.java`

**Algorithm:**
```java
dailyRate = totalComplaints / numberOfDays;
predictedNextMonth = dailyRate * 30;
// Also per-department breakdown
```

**Displayed on:** Admin dashboard as a "Forecast" card with predicted number and trend arrow.

---

### Feature 6: Smart Tag Generation
**File:** `TagGenerator.java`

**Algorithm:**
1. Tokenize title + description (lowercase, remove punctuation)
2. Remove stop words + words < 3 characters
3. Count word frequency with `HashMap<String, Integer>`
4. Sort by frequency descending, take top 5-7 as tags
5. Format: "water,supply,broken,pipe,leak"

**Displayed as:** Bootstrap badges on complaint cards and detail pages.

---

### Feature 7: Voice-to-Complaint (Browser-Side Only)
**File:** `js/voice-input.js`

**Implementation:**
```javascript
const recognition = new (window.SpeechRecognition || window.webkitSpeechRecognition)();
recognition.lang = 'en-US';
recognition.continuous = true;
recognition.interimResults = true;
// On mic button click: recognition.start()
// recognition.onresult: append transcript to description textarea
// Toggle button state (recording/stopped) with visual indicator
```

**On `submit-complaint.jsp`:** Microphone icon button next to the description textarea. No backend component needed. Graceful fallback message for Firefox (not supported).

---

### Feature 8: Image-Based Complaint
**Files:** `SubmitComplaintServlet.java` + `FileUploadUtil.java`

**Upload Flow:**
1. Form with `enctype="multipart/form-data"`, `<input type="file" accept="image/*">`
2. JS `FileReader` for instant image preview
3. Servlet uses `request.getPart("image")` (Servlet 3.0 `@MultipartConfig`)
4. Save to `{UPLOAD_DIR}/{timestamp}_{originalName}`
5. Store relative path in `complaint.setImagePath(...)`

**Smart Category Suggestion:** Extract filename keywords → if match found (e.g., "pothole" in filename), auto-suggest category.

---

### Feature 9: Smart Auto-Reply Bot
**File:** `AutoReplyGenerator.java`

**Templates per category + priority:**
| Category + Priority | Auto-Reply |
|---|---|
| Water + HIGH | "We have received your urgent water-related complaint. A team will be dispatched within 4 hours." |
| Road + MEDIUM | "Your road maintenance complaint has been logged. Expected resolution within 7 days." |
| Generic fallback | "Thank you for your complaint. Your reference ID is {ID}. Current estimated resolution: {ETA}." |

Saved as a `ComplaintReply` (from system user) + `Notification` for the citizen.

---

### Feature 10: Geo-Aware Intelligence
**Files:** `GeoAnalyticsServlet.java` + `geo-analytics.jsp`

**Implementation:**
- User enters area/locality as text during registration (can override per complaint)
- SQL: `SELECT location, COUNT(*) FROM complaints GROUP BY location ORDER BY COUNT(*) DESC`
- Display: Chart.js horizontal bar chart "Top 10 Areas by Complaints"
- Admin dashboard: "Hotspot Areas" card listing top 3 locations

---

### Feature 11: AI-Based ETA Prediction
**File:** `ETAPredictor.java`

**Lookup Table + Adjustments:**
| Priority | Base ETA (hours) |
|---|---|
| CRITICAL | 4 |
| HIGH | 24 |
| MEDIUM | 72 (3 days) |
| LOW | 168 (7 days) |

**Adjustments:**
- Department has > 20 open complaints → multiply by 1.5
- User is repeat complainant (>3 in 30 days) → multiply by 0.8

**Method:** `public static int predictETA(Priority priority, int deptOpenCount, boolean isRepeatUser)`

---

### Feature 12: Behavior-Based Priority Boost
**File:** `PriorityBooster.java`

**Algorithm:**
- Count user's complaints in last 30 days
- count >= 3 and priority is LOW → boost to MEDIUM
- count >= 5 and priority is MEDIUM → boost to HIGH
- Any previous complaints PENDING > 7 days → boost current by one level

**Method:** `public static Priority boost(Priority current, int recentCount, int unresolvedCount)`

---

### Feature 13: Emergency Detection System
**File:** `EmergencyDetector.java`

**Emergency Keywords (~15):** fire, flood, accident, collapse, explosion, danger, death, electrocution, gas leak, emergency, drowning, riot, earthquake, tornado, bomb

**Algorithm:**
1. Scan title + description (lowercase) for keyword matches
2. If match → `EmergencyResult(true, matchedKeywords)`
3. Override priority to CRITICAL, set `is_emergency=true`
4. Create notification for ALL admin users

**Display:** Red badge + pulsing icon on complaint listings for emergency complaints.

---

## 7. Smart Pipeline (SubmitComplaintServlet.doPost)

The complaint submission servlet orchestrates all smart features in this order:

```
Step 1:  Parse form fields + save uploaded image
Step 2:  EmergencyDetector.detect(title, description)     → flag emergency
Step 3:  SentimentAnalyzer.analyze(title + description)    → get sentiment + initial priority
Step 4:  PriorityBooster.boost(priority, recentCount, ...)  → adjust for repeat users
Step 5:  If emergency detected → override priority to CRITICAL
Step 6:  SmartSummarizer.summarize(description)            → generate summary
Step 7:  TagGenerator.generateTags(title, description)     → generate tags
Step 8:  ETAPredictor.predictETA(priority, deptLoad, ...)  → calculate ETA
Step 9:  Save complaint to database
Step 10: AutoReplyGenerator.generateReply(complaint)       → save as first reply + notification
Step 11: Query similar complaints                          → display on success page
Step 12: Log event to analytics_log
```

---

## 8. Team Work Division

### Member 1 (M1): Backend Core & Infrastructure
**Responsibility:** Database, Hibernate entities, DAOs, authentication, core servlet integration
**Estimated Effort:** ~40%

#### Files Owned:

**Configuration:**
- `pom.xml` (Maven dependencies)
- `hibernate.cfg.xml`
- `web.xml`
- `schema.sql`
- `seed.sql`

**Model Layer (All Entities):**
- `User.java` - id, name, email, password, role, phone, area, isActive, createdAt
- `Department.java` - id, name, description
- `Complaint.java` - id, all 17+ fields, @ManyToOne to User and Department
- `ComplaintReply.java` - id, complaint, user, message, createdAt
- `Notification.java` - id, user, message, isRead, createdAt
- `AnalyticsLog.java` - id, eventType, complaintId, deptId, location, createdAt

**DAO Layer (All DAOs):**
- `UserDAO.java` - findByEmail, findById, findByRole, save, update, countByRole
- `DepartmentDAO.java` - findAll, findById, save, update, delete
- `ComplaintDAO.java` - save, update, findById, findByUserId, findByDepartmentId, findByStatus, findSimilar, countByStatus, countByDepartment, countByLocation, countByDateRange, findAll (pagination)
- `ComplaintReplyDAO.java` - save, findByComplaintId
- `NotificationDAO.java` - save, findByUserId, markAsRead, countUnread
- `AnalyticsLogDAO.java` - save, countByDateRange, countByDepartment, countByLocation

**Infrastructure:**
- `HibernateUtil.java` - Singleton SessionFactory builder
- `PasswordUtil.java` - SHA-256 hash + verify
- `FileUploadUtil.java` - Multipart image saving, file type/size validation
- `AuthFilter.java` - Session check + role-based URL access control
- `AppContextListener.java` - Build SessionFactory on startup, close on shutdown

**Servlets:**
- `LoginServlet.java` - GET: show login page, POST: authenticate + set session + redirect by role
- `RegisterServlet.java` - GET: show register page, POST: validate + hash password + save user
- `LogoutServlet.java` - GET: invalidate session
- `SubmitComplaintServlet.java` - THE integration point (calls M3's smart utils)
- `ComplaintReplyServlet.java` - POST: save reply
- `NotificationServlet.java` - GET: fetch as JSON, POST: mark read
- `ImageUploadServlet.java` - POST: handle multipart upload

---

### Member 2 (M2): Frontend & UI
**Responsibility:** All JSP pages, CSS styling, JavaScript features, Chart.js integration
**Estimated Effort:** ~30%

#### Files Owned:

**Common JSP Fragments:**
- `header.jsp` - HTML head, CSS imports, meta tags
- `navbar.jsp` - Role-aware navigation (checks session role to show correct links)
- `footer.jsp` - Script imports, footer HTML
- `sidebar.jsp` - Dashboard sidebar (role-aware)

**Auth Pages:**
- `login.jsp` - Bootstrap login form with email/password, error display
- `register.jsp` - Registration form: name, email, password, phone, area, role selection

**Citizen Pages:**
- `dashboard.jsp` - Stats cards (total/pending/resolved), recent complaints, notifications
- `submit-complaint.jsp` - Full form: title, description (with voice button), category, department, location, image upload, live tag preview, emergency warning
- `my-complaints.jsp` - DataTables list with status/date filters, pagination, status badges
- `track-complaint.jsp` - Full detail view, reply thread, similar complaints sidebar, recommendation panel, ETA display
- `profile.jsp` - View/edit profile

**Admin Pages:**
- `dashboard.jsp` - Stats cards + Chart.js charts (bar, pie, line, prediction)
- `complaints.jsp` - All complaints with filters, assign button per row
- `departments.jsp` - CRUD table with add/edit modal
- `users.jsp` - Users table with role filter and role change dropdown
- `analytics.jsp` - Full analytics: trend chart, department chart, priority distribution, prediction
- `geo-analytics.jsp` - Area-wise complaint bar chart, hotspot areas

**Officer Pages:**
- `dashboard.jsp` - Assigned stats, urgent complaints highlighted in red
- `complaints.jsp` - Assigned complaints with status filter
- `resolve.jsp` - Complaint detail + reply form + status update buttons

**Error Pages:**
- `404.jsp`, `500.jsp`, `access-denied.jsp`

**Static Assets:**
- `css/style.css` - Custom Bootstrap theme overrides
- `js/voice-input.js` - Web Speech API implementation
- `js/charts.js` - Chart.js configuration for all dashboard charts
- `js/complaint-form.js` - Form validation, image preview, AJAX for similar/suggestions
- `js/notifications.js` - Notification polling, mark read

**Landing Page:**
- `index.jsp` - Hero section, features overview, login/register CTA

**Simple Servlets (page-serving):**
- `UserDashboardServlet.java`
- `MyComplaintsServlet.java`
- `TrackComplaintServlet.java`

---

### Member 3 (M3): Smart Features + Admin/Officer Backend
**Responsibility:** All 9 smart utility classes, admin servlets, officer servlets, analytics backend
**Estimated Effort:** ~30%

#### Files Owned:

**Smart Utility Classes (9 files):**
- `SmartSummarizer.java` - Stop-word removal + sentence scoring algorithm
- `SentimentAnalyzer.java` - 80-word keyword dictionary + scoring
- `TagGenerator.java` - Frequency-based tag extraction
- `EmergencyDetector.java` - 15 emergency keywords matching
- `ETAPredictor.java` - Priority lookup table + load multiplier
- `PriorityBooster.java` - Repeat user detection + priority adjustment
- `SmartRecommender.java` - 10 category-to-solution mappings
- `AutoReplyGenerator.java` - Category + priority template system
- `PredictiveAnalytics.java` - Rate calculation + forecasting

**Admin Servlets:**
- `AdminDashboardServlet.java` - Aggregate stats, chart data, prediction data
- `ManageDepartmentsServlet.java` - CRUD operations for departments
- `ManageUsersServlet.java` - List users, update roles, deactivate
- `AssignComplaintServlet.java` - Assign complaint to department + notify officer
- `AdminComplaintsServlet.java` - All complaints with filters + pagination
- `AnalyticsServlet.java` - Analytics data (JSON for charts)
- `GeoAnalyticsServlet.java` - Location-wise complaint data

**Officer Servlets:**
- `OfficerDashboardServlet.java` - Assigned complaint stats
- `AssignedComplaintsServlet.java` - List assigned complaints with filters
- `ResolveComplaintServlet.java` - Update status + add reply + notify citizen

**AJAX API Servlets:**
- `SimilarComplaintsServlet.java` - Returns similar complaints as JSON
- `SmartSuggestServlet.java` - Returns suggested solutions as JSON

---

## 9. Development Plan (Phase by Phase)

The build is organized into 7 sequential phases. Each phase has a clear goal, a concrete deliverable list, the owner for each item, and exit criteria. Phases 1–6 build the system; Phase 7 is reserved entirely for running and testing.

---

### Phase 1: Project Setup & Database Foundation
**Goal:** Establish a buildable Maven project and a populated MySQL database.

| Deliverable | Owner |
|---|---|
| `pom.xml` with Servlet, JSP, JSTL, Hibernate 5.6, MySQL, Gson, JUnit | M1 |
| `hibernate.cfg.xml` configured for local MySQL | M1 |
| `web.xml` with servlet mappings, filter config, `@MultipartConfig` setup | M1 |
| `schema.sql` — all 6 tables + indexes + constraints | M1 |
| `seed.sql` — admin, 6 departments, 3–4 officers, 5–10 citizens, 20–30 sample complaints, replies, 60-day analytics log | M1 |
| 6 Hibernate entities: `User`, `Department`, `Complaint`, `ComplaintReply`, `Notification`, `AnalyticsLog` | M1 |
| 6 DAOs with CRUD + domain query methods (`findByEmail`, `findSimilar`, `countByStatus`, pagination, etc.) | M1 |
| `HibernateUtil` (singleton `SessionFactory`) | M1 |
| `PasswordUtil` (SHA-256 hash + verify) | M1 |
| `FileUploadUtil` (multipart save + type/size validation) | M1 |
| `AppContextListener` (SessionFactory lifecycle) | M1 |

**Exit Criteria:** `mvn clean package` succeeds. MySQL DB is created and seeded. A JUnit test inserts and fetches a `User` through the DAO.

---

### Phase 2: Authentication & Common Layout
**Goal:** Users can register, log in, log out, and land on role-specific pages. Common layout is reusable across all pages.

| Deliverable | Owner |
|---|---|
| `LoginServlet` (GET form, POST authenticate + session + role redirect) | M1 |
| `RegisterServlet` (GET form, POST validate + hash + save) | M1 |
| `LogoutServlet` (invalidate session) | M1 |
| `AuthFilter` (session check + `/admin/*`, `/officer/*`, `/user/*` role gating) | M1 |
| Common JSP fragments: `header.jsp`, `navbar.jsp` (role-aware), `footer.jsp`, `sidebar.jsp` | M2 |
| `auth/login.jsp`, `auth/register.jsp` (Bootstrap forms, error display) | M2 |
| `index.jsp` landing page with hero + features + CTAs | M2 |
| `css/style.css` base theme overrides | M2 |
| `error/404.jsp`, `error/500.jsp`, `error/access-denied.jsp` | M2 |

**Exit Criteria:** A new user can register, log in as CITIZEN / ADMIN / OFFICER, see a placeholder role dashboard, be blocked when accessing another role's URL, and log out cleanly.

---

### Phase 3: Smart Feature Utility Classes
**Goal:** All 9 pure-Java intelligence utilities are implemented and unit-tested in isolation before any servlet depends on them.

| Deliverable | Owner |
|---|---|
| `SmartSummarizer` — stop-word list + sentence scoring (Feature #1) | M3 |
| `SentimentAnalyzer` — 80-word dictionary scoring → ANGRY/NEGATIVE/NEUTRAL/POSITIVE (Feature #2) | M3 |
| `TagGenerator` — frequency-based top-N tag extraction (Feature #6) | M3 |
| `EmergencyDetector` — 15 emergency keywords (Feature #13) | M3 |
| `ETAPredictor` — priority table + load/repeat-user multipliers (Feature #11) | M3 |
| `PriorityBooster` — repeat-complainant escalation (Feature #12) | M3 |
| `SmartRecommender` — 10 category → solutions mapping (Feature #3) | M3 |
| `AutoReplyGenerator` — category + priority templates (Feature #9) | M3 |
| `PredictiveAnalytics` — daily rate + 30-day forecast (Feature #5) | M3 |
| JUnit tests covering the Section 12 validation inputs | M3 |

**Exit Criteria:** All utilities compile and pass unit tests. Sample inputs from Section 12 produce expected outputs (e.g., "fire in building help" → emergency detected, priority CRITICAL).

---

### Phase 4: Citizen Complaint Flow
**Goal:** A logged-in citizen can submit a complaint through the full smart pipeline (Section 7) and track its progress.

| Deliverable | Owner |
|---|---|
| `SubmitComplaintServlet` — orchestrates the 12-step smart pipeline | M1 |
| `ImageUploadServlet` with `@MultipartConfig` | M1 |
| `ComplaintReplyServlet`, `NotificationServlet` | M1 |
| `UserDashboardServlet`, `MyComplaintsServlet`, `TrackComplaintServlet` | M2 |
| `SimilarComplaintsServlet`, `SmartSuggestServlet` (AJAX JSON via Gson) | M3 |
| `user/dashboard.jsp` (stats cards + recent complaints + notifications) | M2 |
| `user/submit-complaint.jsp` (voice button, image preview, live tag preview, emergency warning) | M2 |
| `user/my-complaints.jsp` (DataTables list, filters, pagination, status badges) | M2 |
| `user/track-complaint.jsp` (detail, reply thread, similar sidebar, recommendations, ETA) | M2 |
| `user/profile.jsp` | M2 |
| `js/voice-input.js` (Web Speech API + Firefox fallback) | M2 |
| `js/complaint-form.js` (validation, preview, AJAX for similar + suggestions) | M2 |
| `js/notifications.js` (polling + mark-read) | M2 |

**Exit Criteria:** Citizen submits a complaint using voice and image, sees auto-generated summary, tags, sentiment, priority, ETA, and emergency flag; receives an auto-reply; tracks status; sees similar complaints and recommendations; receives notifications.

---

### Phase 5: Admin Panel
**Goal:** Admin can manage departments and users, assign complaints, and view analytics.

| Deliverable | Owner |
|---|---|
| `AdminDashboardServlet` (aggregate stats + chart payloads) | M3 |
| `ManageDepartmentsServlet`, `ManageUsersServlet` | M3 |
| `AssignComplaintServlet` (assign + notify officer) | M3 |
| `AdminComplaintsServlet` (filters + pagination) | M3 |
| `AnalyticsServlet`, `GeoAnalyticsServlet` (JSON for charts) | M3 |
| `admin/dashboard.jsp` (stats cards + Chart.js bar/pie/line/forecast) | M2 |
| `admin/complaints.jsp` (all complaints with assign button per row) | M2 |
| `admin/departments.jsp` (CRUD table + add/edit modal) | M2 |
| `admin/users.jsp` (role filter + role change dropdown) | M2 |
| `admin/analytics.jsp` (trend, department, priority distribution, prediction) | M2 |
| `admin/geo-analytics.jsp` (area-wise bar chart + hotspot areas card) | M2 |
| `js/charts.js` (all Chart.js configurations) | M2 |

**Exit Criteria:** Admin can CRUD departments, change user roles, assign complaints (officer is notified), and view dashboard charts, analytics, and geo-hotspots — all backed by seeded data.

---

### Phase 6: Officer Panel
**Goal:** Officer can work assigned complaints end-to-end.

| Deliverable | Owner |
|---|---|
| `OfficerDashboardServlet` (assigned-complaint stats) | M3 |
| `AssignedComplaintsServlet` (list with status filters) | M3 |
| `ResolveComplaintServlet` (status update + reply + citizen notification) | M3 |
| `officer/dashboard.jsp` (emergencies highlighted in red) | M2 |
| `officer/complaints.jsp` (assigned complaints + status filter) | M2 |
| `officer/resolve.jsp` (detail + reply form + status update buttons) | M2 |
| Status flow wiring: ASSIGNED → IN_PROGRESS → RESOLVED (sets `resolved_at`) | M3 |

**Exit Criteria:** Officer sees only assigned complaints, emergencies visually highlighted, can reply, progress status, and resolve. Citizen receives a notification on every officer action.

---

### Phase 7: Running & Testing
**Goal:** Deploy the system, verify every flow, fix bugs, and produce final deliverables. No new features in this phase.

**Running Checklist:**
1. Start MySQL; run `schema.sql`, then `seed.sql`
2. Verify `hibernate.cfg.xml` connection URL, username, password match the local MySQL
3. `mvn clean package` → produces `SmartComplaintSystem.war`
4. Copy WAR into Tomcat 9 `webapps/`; start Tomcat
5. Open `http://localhost:8080/SmartComplaintSystem/` — landing page loads
6. Tail `catalina.out` and confirm Hibernate `SessionFactory` built successfully

**End-to-End Test Flows (use Section 12):**
- [ ] Citizen: register → login → submit complaint with voice + image → verify summary, tags, sentiment, priority, ETA, emergency flag, auto-reply all appear
- [ ] Citizen: track complaint → similar complaints and recommendation card populate
- [ ] Admin: login → dashboard charts render with seeded data
- [ ] Admin: assign complaint → officer receives notification → status becomes ASSIGNED
- [ ] Admin: analytics and geo-analytics render with real numbers
- [ ] Admin: CRUD a department, change a user's role
- [ ] Officer: login → sees only assigned complaints → emergencies highlighted red
- [ ] Officer: update IN_PROGRESS → citizen notified → mark RESOLVED → `resolved_at` set
- [ ] Smart feature validation table (Section 12) passes for every listed input
- [ ] `AuthFilter`: direct URL access to another role's pages returns access-denied
- [ ] 404 page on invalid URL; 500 page on thrown exception
- [ ] Browser matrix: Chrome (full), Edge (full), Firefox (voice fallback message shown)
- [ ] Pagination on all list pages (10 per page); page nav works
- [ ] Mobile/tablet responsive check on all major pages
- [ ] Logout invalidates session (back button cannot reach dashboard)

**Polish (only fixes, no new work):**
- Loading spinners on AJAX calls
- Consistent error messages on form validation failures
- Session cleanup on logout
- Image upload size/type rejection shows a friendly message

**Final Deliverables (Section 14):**
- [ ] Source code (Maven project) with README
- [ ] `schema.sql` + `seed.sql`
- [ ] Working deployed WAR on Tomcat 9
- [ ] Project report (design, schema, features, screenshots, testing)
- [ ] 12-slide presentation (Section 13)
- [ ] Demo script + rehearsed walkthrough

**Exit Criteria:** Every item in the test checklist passes. The system runs cleanly on a fresh machine using only the delivered SQL scripts and WAR. Report and slides are finalized.

---

## 10. Maven Dependencies (pom.xml)

```xml
<dependencies>
    <!-- Servlet API -->
    <dependency>
        <groupId>javax.servlet</groupId>
        <artifactId>javax.servlet-api</artifactId>
        <version>3.1.0</version>
        <scope>provided</scope>
    </dependency>

    <!-- JSP API -->
    <dependency>
        <groupId>javax.servlet.jsp</groupId>
        <artifactId>javax.servlet.jsp-api</artifactId>
        <version>2.3.1</version>
        <scope>provided</scope>
    </dependency>

    <!-- JSTL -->
    <dependency>
        <groupId>javax.servlet</groupId>
        <artifactId>jstl</artifactId>
        <version>1.2</version>
    </dependency>

    <!-- Hibernate (5.6.x is last line on javax.persistence; compatible with Java 8–17) -->
    <dependency>
        <groupId>org.hibernate</groupId>
        <artifactId>hibernate-core</artifactId>
        <version>5.6.15.Final</version>
    </dependency>

    <!-- MySQL Connector -->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>8.0.33</version>
    </dependency>

    <!-- Gson (JSON for AJAX) -->
    <dependency>
        <groupId>com.google.code.gson</groupId>
        <artifactId>gson</artifactId>
        <version>2.10.1</version>
    </dependency>

    <!-- JUnit (Testing) -->
    <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>4.13.2</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```

---

## 11. Key Implementation Notes

### Authentication Flow
- Store `User` object in `HttpSession` after login
- `AuthFilter` checks `session.getAttribute("loggedInUser")` on every request
- URL patterns: `/admin/*` → ADMIN only, `/officer/*` → OFFICER only, `/user/*` → CITIZEN only
- Public URLs: `/login`, `/register`, `/css/*`, `/js/*`, `/images/*`

### AJAX Pattern
- Servlets like `SimilarComplaintsServlet` set `response.setContentType("application/json")`
- Write JSON using Gson: `new Gson().toJson(data)`
- JSP pages call via `fetch('/api/similar?keywords=water+leak')`

### Chart.js Integration
- Include Chart.js via CDN in `footer.jsp`
- Dashboard JSPs embed data as JSON in `<script>` blocks (from servlet request attributes)
- `charts.js` reads these and renders bar/pie/line charts

### Pagination
- Servlet receives `page` parameter (default 1), page size = 10
- DAO: `.setFirstResult((page-1)*10).setMaxResults(10)`
- JSP renders page navigation links

---

## 12. Testing & Verification Plan

### End-to-End Test Flows

**Citizen Flow:**
1. Register new account → verify in DB
2. Login → verify redirect to user dashboard
3. Submit complaint with long text, image, use voice input
4. Verify: summary generated, tags created, sentiment detected, priority set, ETA shown
5. Verify: auto-reply appears as first reply
6. Track complaint → verify similar complaints shown
7. View recommendations card

**Admin Flow:**
1. Login as admin → verify admin dashboard with charts
2. Assign complaint to department → verify status changes to ASSIGNED
3. Verify officer gets notification
4. View analytics → verify charts render with data
5. View geo-analytics → verify area-wise breakdown
6. Manage departments: add, edit, delete
7. Manage users: change role

**Officer Flow:**
1. Login as officer → verify assigned complaints shown
2. Emergency complaints highlighted in red
3. Update status to IN_PROGRESS → verify status change
4. Add reply → verify citizen gets notification
5. Mark RESOLVED → verify resolved_at timestamp set

**Smart Features Validation:**
| Input | Expected Output |
|---|---|
| "Water leakage terrible accident near hostel" | Sentiment=ANGRY, Priority=CRITICAL (emergency), Tags=#water,#leakage,#hostel, Emergency=true |
| "Please fix streetlight near park" | Sentiment=POSITIVE, Priority=LOW, Tags=#streetlight,#park |
| User's 4th complaint this month | Priority boosted by one level |
| "fire in building help" | Emergency detected, Priority=CRITICAL, Admin notified |

### Browser Compatibility
| Feature | Chrome | Firefox | Edge |
|---|---|---|---|
| All pages | Yes | Yes | Yes |
| Voice input | Yes | No (fallback msg) | Yes |
| Chart.js | Yes | Yes | Yes |
| Image preview | Yes | Yes | Yes |

---

## 13. Presentation Plan (12 Slides)

| Slide # | Title | Content |
|---|---|---|
| 1 | **Title Slide** | "Smart Complaint Management System", team names, roll numbers, course, semester, guide name |
| 2 | **Problem Statement** | Manual complaints = slow, no prioritization, no analytics, no visibility for citizens |
| 3 | **System Architecture** | Diagram: Browser (JSP/Bootstrap) → Tomcat (Servlets) → Hibernate → MySQL. Three-role overview |
| 4 | **Technology Stack** | JSP, Servlets, Hibernate 4.0, MySQL, Bootstrap 5, Chart.js, Maven, Tomcat 9 |
| 5 | **Database Design** | ER diagram showing 6 tables with relationships and key fields |
| 6 | **Smart Features - Text Intelligence** | Summarization, Sentiment Analysis, Tag Generation, Emergency Detection with examples |
| 7 | **Smart Features - Automation** | ETA Prediction, Priority Boost, Recommendations, Auto-Reply, Predictive Analytics |
| 8 | **Live Demo - Citizen** | Register → Submit complaint → Show all auto-generated intelligence → Track |
| 9 | **Live Demo - Admin** | Dashboard charts → Assign → Analytics → Geo-analytics → Predictions |
| 10 | **Live Demo - Officer** | View assigned → Update status → Resolve → Citizen notified |
| 11 | **Testing & Results** | Test cases summary, smart feature accuracy, analytics screenshots |
| 12 | **Conclusion & Future Scope** | Summary + Future: real NLP/ML, mobile app, SMS/email notifications, GPS geolocation, govt API integration |

---

## 14. Deliverables Checklist

- [ ] Complete source code (Maven project)
- [ ] MySQL schema + seed data scripts
- [ ] Working deployment on Tomcat
- [ ] Project report (system design, DB schema, feature descriptions, screenshots, testing)
- [ ] Presentation (12 slides + live demo)
- [ ] All 13 smart features functional
- [ ] 3 roles fully operational (Citizen, Admin, Officer)
- [ ] Charts and analytics dashboard working
- [ ] Voice input and image upload functional
