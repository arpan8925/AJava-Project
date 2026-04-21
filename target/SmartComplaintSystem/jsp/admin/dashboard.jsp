<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="Admin Dashboard"/>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="user" value="${sessionScope.loggedInUser}"/>

<jsp:include page="/jsp/common/header.jsp"/>
<jsp:include page="/jsp/common/navbar.jsp"/>

<main class="container-fluid px-4">
    <div class="row">
        <div class="col-lg-2">
            <jsp:include page="/jsp/common/sidebar.jsp"/>
        </div>
        <div class="col-lg-10">
            <h3 class="mb-3">Admin Console</h3>

            <c:if test="${not empty sessionScope.flash}">
                <div class="alert alert-info"><c:out value="${sessionScope.flash}"/></div>
                <c:remove var="flash" scope="session"/>
            </c:if>

            <div class="row g-3 mb-4">
                <div class="col-md-3 col-6"><div class="card-scs p-3 text-center">
                    <div class="text-muted small">Total</div>
                    <div class="h3 mb-0"><c:out value="${totalCount}"/></div>
                </div></div>
                <div class="col-md-3 col-6"><div class="card-scs p-3 text-center">
                    <div class="text-muted small">Pending + Assigned</div>
                    <div class="h3 mb-0 text-secondary"><c:out value="${pendingCount + assignedCount}"/></div>
                </div></div>
                <div class="col-md-3 col-6"><div class="card-scs p-3 text-center">
                    <div class="text-muted small">In Progress</div>
                    <div class="h3 mb-0 text-warning"><c:out value="${inProgressCount}"/></div>
                </div></div>
                <div class="col-md-3 col-6"><div class="card-scs p-3 text-center">
                    <div class="text-muted small">Resolved</div>
                    <div class="h3 mb-0 text-success"><c:out value="${resolvedCount}"/></div>
                </div></div>
                <div class="col-md-3 col-6"><div class="card-scs p-3 text-center">
                    <div class="text-muted small">Emergencies</div>
                    <div class="h3 mb-0 text-danger"><c:out value="${emergencyCount}"/></div>
                </div></div>
                <div class="col-md-3 col-6"><div class="card-scs p-3 text-center">
                    <div class="text-muted small">Citizens</div>
                    <div class="h4 mb-0"><c:out value="${citizenCount}"/></div>
                </div></div>
                <div class="col-md-3 col-6"><div class="card-scs p-3 text-center">
                    <div class="text-muted small">Officers</div>
                    <div class="h4 mb-0"><c:out value="${officerCount}"/></div>
                </div></div>
                <div class="col-md-3 col-6"><div class="card-scs p-3 text-center">
                    <div class="text-muted small">Forecast (next 30d)</div>
                    <div class="h4 mb-0">
                        <c:out value="${forecast.predictedNext30Days}"/>
                        <small class="text-muted">
                            <c:choose>
                                <c:when test="${forecast.trend == 'UP'}">
                                    <i class="bi bi-arrow-up text-danger"></i>
                                </c:when>
                                <c:when test="${forecast.trend == 'DOWN'}">
                                    <i class="bi bi-arrow-down text-success"></i>
                                </c:when>
                                <c:otherwise>
                                    <i class="bi bi-dash text-muted"></i>
                                </c:otherwise>
                            </c:choose>
                        </small>
                    </div>
                </div></div>
            </div>

            <div class="row g-3 mb-4">
                <div class="col-lg-8">
                    <div class="card-scs p-3">
                        <h6 class="text-muted text-uppercase small mb-2">Complaints &mdash; last 30 days</h6>
                        <canvas id="trendChart" height="90"></canvas>
                    </div>
                </div>
                <div class="col-lg-4">
                    <div class="card-scs p-3">
                        <h6 class="text-muted text-uppercase small mb-2">Priority Breakdown</h6>
                        <canvas id="priorityChart" height="140"></canvas>
                    </div>
                </div>
            </div>

            <div class="row g-3 mb-4">
                <div class="col-lg-6">
                    <div class="card-scs p-3">
                        <h6 class="text-muted text-uppercase small mb-2">By Department</h6>
                        <canvas id="departmentChart" height="140"></canvas>
                    </div>
                </div>
                <div class="col-lg-6">
                    <div class="card-scs p-3">
                        <h6 class="text-muted text-uppercase small mb-2">Hotspot Locations</h6>
                        <canvas id="locationChart" height="140"></canvas>
                    </div>
                </div>
            </div>

            <div class="card-scs p-3 mb-4">
                <div class="d-flex justify-content-between mb-2">
                    <h6 class="text-muted text-uppercase small mb-0">Recent Complaints</h6>
                    <a href="${ctx}/admin/complaints" class="small">View all &rarr;</a>
                </div>
                <c:choose>
                    <c:when test="${empty recentComplaints}">
                        <p class="text-muted small mb-0">None yet.</p>
                    </c:when>
                    <c:otherwise>
                        <div class="table-responsive">
                            <table class="table table-sm mb-0 align-middle">
                                <thead class="table-light">
                                    <tr>
                                        <th>#</th><th>Title</th><th>Priority</th><th>Status</th>
                                        <th>Dept</th><th>Location</th><th>Submitted</th><th></th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${recentComplaints}" var="c">
                                        <tr>
                                            <td><c:out value="${c.id}"/></td>
                                            <td>
                                                <c:out value="${c.title}"/>
                                                <c:if test="${c.isEmergency}">
                                                    <span class="badge emergency-badge">EMERGENCY</span>
                                                </c:if>
                                            </td>
                                            <td><span class="badge priority-${fn:toLowerCase(c.priority)}"><c:out value="${c.priority}"/></span></td>
                                            <td><span class="badge status-${fn:toLowerCase(c.status)}"><c:out value="${c.status}"/></span></td>
                                            <td><small><c:out value="${not empty c.department ? c.department.name : '-'}"/></small></td>
                                            <td><small><c:out value="${c.location}"/></small></td>
                                            <td><small><fmt:formatDate value="${c.createdAt}" pattern="dd MMM, HH:mm"/></small></td>
                                            <td>
                                                <a class="btn btn-sm btn-outline-primary"
                                                   href="${ctx}/admin/complaints?page=1">Manage</a>
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

<script>
    window.SCS_CHART_DATA = {
        trendLabels: ${trendLabelsJson},
        trendSeries: ${trendSeriesJson},
        priorities:  ${priorityJson},
        departments: ${departmentsJson},
        locations:   ${locationsJson}
    };
</script>
<script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.1/dist/chart.umd.min.js"></script>
<script src="${ctx}/js/charts.js"></script>

<jsp:include page="/jsp/common/footer.jsp"/>
