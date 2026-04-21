<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="Officer Dashboard"/>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="user" value="${sessionScope.loggedInUser}"/>

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
                    <h3 class="mb-0">Officer Console</h3>
                    <p class="text-muted mb-0"><small>Welcome, <c:out value="${user.name}"/></small></p>
                </div>
                <a href="${ctx}/officer/complaints" class="btn btn-scs">
                    <i class="bi bi-inbox"></i> Work Queue
                </a>
            </div>

            <c:if test="${emergencyCount > 0}">
                <div class="alert alert-danger d-flex justify-content-between align-items-center">
                    <div>
                        <i class="bi bi-exclamation-triangle"></i>
                        <strong><c:out value="${emergencyCount}"/></strong> active emergency complaint(s) need attention.
                    </div>
                    <a href="${ctx}/officer/complaints?status=ACTIVE" class="btn btn-sm btn-light">View</a>
                </div>
            </c:if>

            <div class="row g-3 mb-4">
                <div class="col-6 col-md-3"><div class="card-scs p-3 text-center">
                    <div class="text-muted small">Assigned</div>
                    <div class="h3 mb-0"><c:out value="${assignedCount}"/></div>
                </div></div>
                <div class="col-6 col-md-3"><div class="card-scs p-3 text-center">
                    <div class="text-muted small">In Progress</div>
                    <div class="h3 mb-0 text-warning"><c:out value="${inProgressCount}"/></div>
                </div></div>
                <div class="col-6 col-md-3"><div class="card-scs p-3 text-center">
                    <div class="text-muted small">Resolved</div>
                    <div class="h3 mb-0 text-success"><c:out value="${resolvedCount}"/></div>
                </div></div>
                <div class="col-6 col-md-3"><div class="card-scs p-3 text-center">
                    <div class="text-muted small">Unread notifs</div>
                    <div class="h3 mb-0 text-primary"><c:out value="${unreadNotifications}"/></div>
                </div></div>
            </div>

            <c:if test="${not empty activeEmergencies}">
                <div class="card-scs p-3 mb-4 border border-danger">
                    <h6 class="text-danger text-uppercase small mb-2">
                        <i class="bi bi-exclamation-triangle-fill"></i> Active Emergencies
                    </h6>
                    <div class="table-responsive">
                        <table class="table table-sm mb-0 align-middle">
                            <thead class="table-light">
                                <tr>
                                    <th>#</th><th>Title</th><th>Priority</th><th>Status</th>
                                    <th>Location</th><th>Submitted</th><th></th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${activeEmergencies}" var="c">
                                    <tr class="table-danger">
                                        <td><c:out value="${c.id}"/></td>
                                        <td>
                                            <strong><c:out value="${c.title}"/></strong>
                                            <span class="badge emergency-badge">EMERGENCY</span>
                                        </td>
                                        <td><span class="badge priority-${fn:toLowerCase(c.priority)}"><c:out value="${c.priority}"/></span></td>
                                        <td><span class="badge status-${fn:toLowerCase(c.status)}"><c:out value="${c.status}"/></span></td>
                                        <td><small><c:out value="${c.location}"/></small></td>
                                        <td><small><fmt:formatDate value="${c.createdAt}" pattern="dd MMM, HH:mm"/></small></td>
                                        <td>
                                            <a class="btn btn-sm btn-danger"
                                               href="${ctx}/officer/resolve?id=${c.id}">Handle</a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </c:if>

            <div class="card-scs p-3">
                <div class="d-flex justify-content-between mb-2">
                    <h6 class="text-muted text-uppercase small mb-0">Active Work Queue</h6>
                    <a href="${ctx}/officer/complaints?status=ACTIVE" class="small">View all &rarr;</a>
                </div>
                <c:choose>
                    <c:when test="${empty recentAssigned}">
                        <p class="text-muted mb-0 small">Nothing on your plate right now.</p>
                    </c:when>
                    <c:otherwise>
                        <div class="table-responsive">
                            <table class="table table-sm mb-0 align-middle">
                                <thead class="table-light">
                                    <tr>
                                        <th>#</th><th>Title</th><th>Dept</th><th>Priority</th>
                                        <th>Status</th><th>Submitted</th><th></th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${recentAssigned}" var="c">
                                        <tr>
                                            <td><c:out value="${c.id}"/></td>
                                            <td>
                                                <c:out value="${c.title}"/>
                                                <c:if test="${c.isEmergency}">
                                                    <span class="badge emergency-badge">EMERGENCY</span>
                                                </c:if>
                                            </td>
                                            <td><small><c:out value="${not empty c.department ? c.department.name : '-'}"/></small></td>
                                            <td><span class="badge priority-${fn:toLowerCase(c.priority)}"><c:out value="${c.priority}"/></span></td>
                                            <td><span class="badge status-${fn:toLowerCase(c.status)}"><c:out value="${c.status}"/></span></td>
                                            <td><small><fmt:formatDate value="${c.createdAt}" pattern="dd MMM, HH:mm"/></small></td>
                                            <td>
                                                <a class="btn btn-sm btn-scs" href="${ctx}/officer/resolve?id=${c.id}">Work</a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</main>

<jsp:include page="/jsp/common/footer.jsp"/>
