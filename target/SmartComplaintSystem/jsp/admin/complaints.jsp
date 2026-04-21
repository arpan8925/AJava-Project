<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="All Complaints"/>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<jsp:include page="/jsp/common/header.jsp"/>
<jsp:include page="/jsp/common/navbar.jsp"/>

<main class="container-fluid px-4">
    <div class="row">
        <div class="col-lg-2">
            <jsp:include page="/jsp/common/sidebar.jsp"/>
        </div>
        <div class="col-lg-10">
            <div class="d-flex justify-content-between align-items-center mb-3">
                <h4 class="mb-0">All Complaints <small class="text-muted">(<c:out value="${total}"/>)</small></h4>
            </div>

            <c:if test="${not empty sessionScope.flash}">
                <div class="alert alert-info"><c:out value="${sessionScope.flash}"/></div>
                <c:remove var="flash" scope="session"/>
            </c:if>

            <form method="get" action="${ctx}/admin/complaints" class="card-scs p-3 mb-3">
                <div class="row g-2 align-items-end">
                    <div class="col-md-2">
                        <label class="form-label small text-muted">Status</label>
                        <select name="status" class="form-select form-select-sm">
                            <option value="">Any</option>
                            <c:forEach var="s" items="${['PENDING','ASSIGNED','IN_PROGRESS','RESOLVED','CLOSED']}">
                                <option value="${s}" ${statusFilter == s ? 'selected' : ''}><c:out value="${s}"/></option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-md-2">
                        <label class="form-label small text-muted">Priority</label>
                        <select name="priority" class="form-select form-select-sm">
                            <option value="">Any</option>
                            <c:forEach var="p" items="${['LOW','MEDIUM','HIGH','CRITICAL']}">
                                <option value="${p}" ${priorityFilter == p ? 'selected' : ''}><c:out value="${p}"/></option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-md-3">
                        <label class="form-label small text-muted">Department</label>
                        <select name="dept" class="form-select form-select-sm">
                            <option value="">Any</option>
                            <c:forEach items="${departments}" var="d">
                                <option value="${d.id}" ${deptFilter == d.id ? 'selected' : ''}>
                                    <c:out value="${d.name}"/>
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-md-2">
                        <div class="form-check mt-4">
                            <input class="form-check-input" type="checkbox" name="emergency" value="true"
                                   id="emergencyOnly" ${emergencyOnly ? 'checked' : ''}>
                            <label class="form-check-label small" for="emergencyOnly">Emergency only</label>
                        </div>
                    </div>
                    <div class="col-md-3 text-end">
                        <button type="submit" class="btn btn-scs btn-sm">Apply</button>
                        <a href="${ctx}/admin/complaints" class="btn btn-outline-secondary btn-sm">Reset</a>
                    </div>
                </div>
            </form>

            <div class="table-responsive card-scs">
                <table class="table mb-0 align-middle">
                    <thead class="table-light">
                        <tr>
                            <th>#</th>
                            <th>Title</th>
                            <th>Citizen</th>
                            <th>Priority</th>
                            <th>Status</th>
                            <th>Dept</th>
                            <th>Location</th>
                            <th>Submitted</th>
                            <th>Assign</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${empty complaints}">
                                <tr><td colspan="9" class="text-center text-muted py-4">No complaints match.</td></tr>
                            </c:when>
                            <c:otherwise>
                                <c:forEach items="${complaints}" var="c">
                                    <tr>
                                        <td><c:out value="${c.id}"/></td>
                                        <td>
                                            <c:out value="${c.title}"/>
                                            <c:if test="${c.isEmergency}">
                                                <span class="badge emergency-badge">EMERGENCY</span>
                                            </c:if>
                                        </td>
                                        <td>
                                            <small>
                                                <c:out value="${c.user.name}"/><br>
                                                <span class="text-muted"><c:out value="${c.user.email}"/></span>
                                            </small>
                                        </td>
                                        <td><span class="badge priority-${fn:toLowerCase(c.priority)}"><c:out value="${c.priority}"/></span></td>
                                        <td><span class="badge status-${fn:toLowerCase(c.status)}"><c:out value="${c.status}"/></span></td>
                                        <td><small><c:out value="${not empty c.department ? c.department.name : '-'}"/></small></td>
                                        <td><small><c:out value="${c.location}"/></small></td>
                                        <td><small><fmt:formatDate value="${c.createdAt}" pattern="dd MMM, HH:mm"/></small></td>
                                        <td>
                                            <form method="post" action="${ctx}/admin/assign" class="d-flex gap-1">
                                                <input type="hidden" name="complaintId" value="${c.id}"/>
                                                <input type="hidden" name="back" value="/admin/complaints?page=${page}"/>
                                                <select name="departmentId" class="form-select form-select-sm">
                                                    <option value="">Choose…</option>
                                                    <c:forEach items="${departments}" var="d">
                                                        <option value="${d.id}"
                                                            ${c.department.id == d.id ? 'selected' : ''}>
                                                            <c:out value="${d.name}"/>
                                                        </option>
                                                    </c:forEach>
                                                </select>
                                                <button type="submit" class="btn btn-sm btn-scs">Assign</button>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                    </tbody>
                </table>
            </div>

            <c:if test="${totalPages > 1}">
                <nav class="mt-3">
                    <ul class="pagination pagination-sm justify-content-center">
                        <c:forEach var="p" begin="1" end="${totalPages}">
                            <c:url var="url" value="/admin/complaints">
                                <c:param name="page" value="${p}"/>
                                <c:if test="${not empty statusFilter}"><c:param name="status"   value="${statusFilter}"/></c:if>
                                <c:if test="${not empty priorityFilter}"><c:param name="priority" value="${priorityFilter}"/></c:if>
                                <c:if test="${not empty deptFilter}"><c:param name="dept"       value="${deptFilter}"/></c:if>
                                <c:if test="${emergencyOnly}"><c:param name="emergency" value="true"/></c:if>
                            </c:url>
                            <li class="page-item ${p == page ? 'active' : ''}">
                                <a class="page-link" href="${url}"><c:out value="${p}"/></a>
                            </li>
                        </c:forEach>
                    </ul>
                </nav>
            </c:if>
        </div>
    </div>
</main>

<jsp:include page="/jsp/common/footer.jsp"/>
