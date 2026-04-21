# Smart Complaint Management System

A web-based civic complaint management system built for the Advanced Java course (6th Semester CSE). Citizens file complaints; admins triage and assign; officers resolve. Thirteen "smart" features — sentiment, emergency detection, ETA prediction, priority boosting, tag generation, summarisation, similar-complaint lookup, category-aware recommendations, auto-reply, predictive analytics, geo-hotspots, voice input, image upload — are implemented in pure Java, no external AI APIs.

See [`PROJECT_PLAN.md`](PROJECT_PLAN.md) for the full design, smart-feature specs, database schema, and phase-by-phase plan.

## Stack

| Layer       | Technology                                         |
|-------------|----------------------------------------------------|
| Frontend    | JSP + JSTL + Bootstrap 5 + Bootstrap Icons + Chart.js |
| Backend     | Java Servlets 3.1 + Hibernate 5.6 (`javax.persistence`) |
| Database    | MySQL 8                                            |
| Server      | Apache Tomcat 9 (production) / Jetty 9.4 (dev)     |
| Build       | Maven 3.9+                                         |
| Java        | JDK 1.8+ (built and tested on JDK 17)              |
| Voice Input | Browser Web Speech API (Chrome / Edge)             |

## Prerequisites

- JDK 1.8+
- Apache Maven 3.6+
- MySQL 8 (or compatible: MariaDB / MySQL 5.7)
- A servlet container: Tomcat 9 for deployment, or none if you use the included Jetty Maven plugin for dev

## Setup

### 1. Database

```bash
# Create DB and tables
mysql -u root -p < schema.sql
# Populate with demo data (admin, 4 officers, 8 citizens, 25 complaints, 60-day analytics log)
mysql -u root -p smart_complaint_system < seed.sql
```

If running schema.sql via phpMyAdmin / cPanel, select the target database first (the `CREATE DATABASE` / `USE` lines are commented out by default for shared hosting compatibility). Uncomment lines 8–9 of `schema.sql` for local dev.

### 2. Configure Hibernate

Edit [`src/main/resources/hibernate.cfg.xml`](src/main/resources/hibernate.cfg.xml) and update the MySQL connection URL, username, and password to match your setup. The defaults assume:

```
jdbc:mysql://localhost:3306/smart_complaint_system
username: root
password: root
```

Alternatively, override at runtime with environment variables (`DB_URL`, `DB_USER`, `DB_PASSWORD`, `DB_DRIVER`, `DB_DIALECT`) — these take precedence over the XML values.

### 3. Build

```bash
mvn clean package
```

Produces `target/SmartComplaintSystem.war` (~17 MB).

## Running

### Option A — Tomcat 9 (production target)

```bash
# copy WAR into Tomcat's webapps directory
cp target/SmartComplaintSystem.war $CATALINA_HOME/webapps/

# start Tomcat
$CATALINA_HOME/bin/startup.sh      # Linux/macOS
$CATALINA_HOME/bin/startup.bat     # Windows
```

Open [http://localhost:8080/SmartComplaintSystem/](http://localhost:8080/SmartComplaintSystem/).

### Option B — Jetty (dev, no install)

```bash
mvn jetty:run
```

Open [http://localhost:8080/](http://localhost:8080/) — the app is served at the root context in dev.

Stop Jetty with Ctrl+C.

## Demo Accounts

All accounts are created by `seed.sql`:

| Role     | Email                          | Password     |
|----------|--------------------------------|--------------|
| Admin    | `admin@scs.com`                | `admin123`   |
| Officer  | `officer-water@scs.com`        | `officer123` |
| Officer  | `officer-roads@scs.com`        | `officer123` |
| Officer  | `officer-electric@scs.com`    | `officer123` |
| Officer  | `officer-sanitation@scs.com`  | `officer123` |
| Citizen  | `citizen1@scs.com` … `citizen8@scs.com` | `citizen123` |

Citizens can also self-register via `/register`. Officer registrations go through the same form; admins are seeded or promoted by another admin.

## Testing

```bash
mvn test
```

Runs the full JUnit 4 suite. Tests split into two groups:

- **Pure-Java unit tests** (no DB needed): `com.scs.util.*Test` — exercises each smart-feature class plus `PasswordUtil`. ~56 tests.
- **Integration tests** (require MySQL running + schema/seed loaded): `com.scs.dao.UserDAOTest` — insert/fetch round-trip plus seed-admin sanity.

Run a specific class with `-Dtest=ClassName`.

## Project Structure

```
SmartComplaintSystem/
├── pom.xml                   Maven build + Jetty dev plugin
├── schema.sql                DDL for all 6 tables + indexes
├── seed.sql                  Admin + 4 officers + 8 citizens + 25 complaints + 60-day analytics
├── README.md
├── PROJECT_PLAN.md           Full design doc, smart-feature specs, phase plan
└── src/
    ├── main/
    │   ├── java/com/scs/
    │   │   ├── model/        Hibernate entities (User, Department, Complaint, ComplaintReply, Notification, AnalyticsLog)
    │   │   ├── dao/          DAOs (CRUD + filter + aggregate methods)
    │   │   ├── servlet/      22+ servlets (auth, citizen, admin, officer, AJAX /api/*)
    │   │   ├── filter/       AuthFilter — session + role gating
    │   │   ├── listener/     AppContextListener — SessionFactory lifecycle
    │   │   └── util/         HibernateUtil, PasswordUtil, FileUploadUtil, StopWords, SmartSummarizer, SentimentAnalyzer, TagGenerator, EmergencyDetector, ETAPredictor, PriorityBooster, SmartRecommender, AutoReplyGenerator, PredictiveAnalytics
    │   ├── resources/
    │   │   └── hibernate.cfg.xml
    │   └── webapp/
    │       ├── WEB-INF/web.xml      Listener + error pages + multipart
    │       ├── index.jsp
    │       ├── css/style.css
    │       ├── js/                  voice-input, complaint-form, notifications, charts
    │       └── jsp/
    │           ├── common/  header, navbar (role-aware), footer, sidebar
    │           ├── auth/    login, register
    │           ├── user/    dashboard, submit-complaint, my-complaints, track-complaint, profile
    │           ├── admin/   dashboard, complaints, departments, users, analytics, geo-analytics
    │           ├── officer/ dashboard, complaints, resolve
    │           └── error/   404, 500, access-denied
    └── test/
        └── java/com/scs/    Unit + integration tests
```

## The Smart Pipeline

When a citizen submits a complaint, `SubmitComplaintServlet.doPost()` orchestrates the 12-step pipeline:

```
1.  Parse form fields + save uploaded image (FileUploadUtil)
2.  EmergencyDetector.detect(title, description)
3.  SentimentAnalyzer.analyze(title + description)  → initial priority
4.  PriorityBooster.boost(priority, recentCount, staleUnresolved)
5.  Emergency override → Priority.CRITICAL
6.  SmartSummarizer.summarize(description)
7.  TagGenerator.generateTagsString(title, description)
8.  ETAPredictor.predictETA(priority, deptLoad, isRepeatUser)
9.  ComplaintDAO.save()
10. AutoReplyGenerator.generateReply() → save as first reply + notify citizen
    (+ notify admins if emergency)
11. Similar complaints shown on the redirect page via /api/similar (AJAX)
12. AnalyticsLog entry written (COMPLAINT_CREATED / EMERGENCY_FLAGGED)
```

Section 12 of [`PROJECT_PLAN.md`](PROJECT_PLAN.md) has the full validation table (exact inputs → expected outputs). All four rows are covered by `SmartPipelineValidationTest`.

## Browser Compatibility

| Feature         | Chrome | Edge | Firefox              |
|-----------------|--------|------|----------------------|
| All pages       | ✅     | ✅   | ✅                   |
| Voice input     | ✅     | ✅   | fallback message     |
| Chart.js        | ✅     | ✅   | ✅                   |
| Image preview   | ✅     | ✅   | ✅                   |

## Troubleshooting

- **`mvn jetty:run` fails with `Address already in use`** — another process is on port 8080. Find and stop it: `netstat -ano | grep 8080`.
- **App returns 500 on login** — MySQL is not reachable. Check the service is running and `hibernate.cfg.xml` credentials are correct.
- **`Schema-validation` error on startup** — schema drift between entities and DB. `hbm2ddl.auto` is set to `none`; if you changed schema.sql, reload it.
- **Voice button greyed out** — browser doesn't support Web Speech API (use Chrome or Edge).
- **Image upload "exceeds maximum size"** — default cap is 5 MB per file, 10 MB per request. Change in `SubmitComplaintServlet` `@MultipartConfig`.

## Credits

- Team: 3-member group (see `PROJECT_PLAN.md` §8 for work division)
- Course: Advanced Java, 6th Semester CSE
