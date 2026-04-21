<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Smart Complaint Management System"/>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<jsp:include page="/jsp/common/header.jsp"/>
<jsp:include page="/jsp/common/navbar.jsp"/>

<section class="hero">
    <div class="container text-center">
        <h1>Raise a Complaint. Watch Intelligence Do the Rest.</h1>
        <p class="lead mt-3 mx-auto" style="max-width: 720px;">
            Your civic issues, triaged automatically &mdash; prioritized, routed to the right
            department, and tracked end-to-end with 13 smart features powered by pure Java.
        </p>
        <div class="mt-4">
            <c:choose>
                <c:when test="${sessionScope.loggedInUser == null}">
                    <a href="${ctx}/register" class="btn btn-light btn-lg me-2">Get Started</a>
                    <a href="${ctx}/login" class="btn btn-outline-light btn-lg">Sign In</a>
                </c:when>
                <c:otherwise>
                    <a href="${ctx}/dashboard" class="btn btn-light btn-lg">Go to Dashboard</a>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</section>

<main class="container">
    <div class="row g-4">
        <div class="col-md-4">
            <div class="card-scs p-4 h-100">
                <h5><i class="bi bi-mic text-primary"></i> Voice & Image Input</h5>
                <p class="text-muted mb-0">
                    Dictate complaints using your browser's speech recognition, or attach photos of
                    the issue directly. No typing required.
                </p>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card-scs p-4 h-100">
                <h5><i class="bi bi-cpu text-primary"></i> Smart Triage</h5>
                <p class="text-muted mb-0">
                    Sentiment analysis, emergency detection, auto-tagging, summary, ETA prediction,
                    and priority boosting &mdash; all automated.
                </p>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card-scs p-4 h-100">
                <h5><i class="bi bi-bar-chart-line text-primary"></i> Live Analytics</h5>
                <p class="text-muted mb-0">
                    Department dashboards with charts, geo-hotspots, and 30-day complaint
                    forecasts powered by trend analysis.
                </p>
            </div>
        </div>
    </div>

    <div class="row mt-5 g-4">
        <div class="col-md-4">
            <div class="card-scs p-4 h-100 text-center">
                <i class="bi bi-person display-5 text-primary"></i>
                <h5 class="mt-3">Citizen</h5>
                <p class="text-muted small">Submit &middot; Track &middot; Receive updates</p>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card-scs p-4 h-100 text-center">
                <i class="bi bi-shield-lock display-5 text-primary"></i>
                <h5 class="mt-3">Admin</h5>
                <p class="text-muted small">Manage &middot; Assign &middot; Analyze</p>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card-scs p-4 h-100 text-center">
                <i class="bi bi-briefcase display-5 text-primary"></i>
                <h5 class="mt-3">Officer</h5>
                <p class="text-muted small">Resolve &middot; Reply &middot; Close</p>
            </div>
        </div>
    </div>
</main>

<jsp:include page="/jsp/common/footer.jsp"/>
