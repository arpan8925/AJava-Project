<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="Citizen Dashboard"/>
<c:set var="user" value="${sessionScope.loggedInUser}"/>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<jsp:include page="/jsp/common/header.jsp"/>
<jsp:include page="/jsp/common/navbar.jsp"/>

<main class="container">
    <div class="row">
        <div class="col-lg-3">
            <jsp:include page="/jsp/common/sidebar.jsp"/>
        </div>
        <div class="col-lg-9">
            <div class="d-flex justify-content-between align-items-center mb-3">
                <div>
                    <h3 class="mb-0">Welcome back, <c:out value="${user.name}"/></h3>
                    <p class="text-muted mb-0"><small>Citizen dashboard</small></p>
                </div>
                <a href="${ctx}/user/submit-complaint" class="btn btn-scs">
                    <i class="bi bi-plus-circle"></i> New Complaint
                </a>
            </div>

            <div class="row g-3 mb-4">
                <div class="col-6 col-md-3">
                    <div class="card-scs p-3 text-center">
                        <div class="text-muted small">Total</div>
                        <div class="h3 mb-0"><c:out value="${totalCount}"/></div>
                    </div>
                </div>
                <div class="col-6 col-md-3">
                    <div class="card-scs p-3 text-center">
                        <div class="text-muted small">Pending</div>
                        <div class="h3 mb-0 text-secondary"><c:out value="${pendingCount}"/></div>
                    </div>
                </div>
                <div class="col-6 col-md-3">
                    <div class="card-scs p-3 text-center">
                        <div class="text-muted small">In Progress</div>
                        <div class="h3 mb-0 text-warning"><c:out value="${inProgressCount}"/></div>
                    </div>
                </div>
                <div class="col-6 col-md-3">
                    <div class="card-scs p-3 text-center">
                        <div class="text-muted small">Resolved</div>
                        <div class="h3 mb-0 text-success"><c:out value="${resolvedCount}"/></div>
                    </div>
                </div>
            </div>

            <c:if test="${emergencyCount > 0}">
                <div class="alert alert-danger">
                    <i class="bi bi-exclamation-triangle"></i>
                    You have <strong><c:out value="${emergencyCount}"/></strong> emergency complaint(s) on file.
                </div>
            </c:if>

            <div class="row g-3">
                <div class="col-lg-7">
                    <div class="card-scs p-3">
                        <h6 class="text-uppercase text-muted mb-3">Recent Complaints</h6>
                        <c:choose>
                            <c:when test="${empty recentComplaints}">
                                <p class="text-muted mb-0">
                                    No complaints yet. <a href="${ctx}/user/submit-complaint">Submit your first one</a>.
                                </p>
                            </c:when>
                            <c:otherwise>
                                <div class="list-group list-group-flush">
                                    <c:forEach items="${recentComplaints}" var="c">
                                        <a href="${ctx}/user/track-complaint?id=${c.id}" class="list-group-item list-group-item-action px-0">
                                            <div class="d-flex justify-content-between align-items-start">
                                                <div class="me-3">
                                                    <div class="fw-semibold"><c:out value="${c.title}"/></div>
                                                    <div class="small text-muted">
                                                        <fmt:formatDate value="${c.createdAt}" pattern="dd MMM yyyy, HH:mm"/>
                                                        <c:if test="${not empty c.location}">&middot; <c:out value="${c.location}"/></c:if>
                                                    </div>
                                                </div>
                                                <div class="text-end">
                                                    <span class="badge status-${fn:toLowerCase(c.status)}">
                                                        <c:out value="${c.status}"/>
                                                    </span>
                                                    <c:if test="${c.isEmergency}">
                                                        <span class="badge emergency-badge">EMERGENCY</span>
                                                    </c:if>
                                                </div>
                                            </div>
                                        </a>
                                    </c:forEach>
                                </div>
                                <div class="text-end mt-2">
                                    <a href="${ctx}/user/my-complaints" class="small">View all &rarr;</a>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
                <div class="col-lg-5">
                    <div class="card-scs p-3">
                        <h6 class="text-uppercase text-muted mb-3">
                            Notifications
                            <c:if test="${unreadCount > 0}">
                                <span class="badge bg-primary"><c:out value="${unreadCount}"/> new</span>
                            </c:if>
                        </h6>
                        <c:choose>
                            <c:when test="${empty notifications}">
                                <p class="text-muted mb-0">No notifications.</p>
                            </c:when>
                            <c:otherwise>
                                <ul class="list-unstyled mb-0 small">
                                    <c:forEach items="${notifications}" var="n">
                                        <li class="py-2 border-bottom">
                                            <div class="<c:if test='${not n.isRead}'>fw-semibold</c:if>">
                                                <c:out value="${n.message}"/>
                                            </div>
                                            <div class="text-muted">
                                                <fmt:formatDate value="${n.createdAt}" pattern="dd MMM, HH:mm"/>
                                            </div>
                                        </li>
                                    </c:forEach>
                                </ul>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>

<jsp:include page="/jsp/common/footer.jsp"/>
