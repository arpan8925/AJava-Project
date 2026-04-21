<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="Assigned Complaints"/>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<jsp:include page="/jsp/common/header.jsp"/>
<jsp:include page="/jsp/common/navbar.jsp"/>

<main class="container">
    <div class="row">
        <div class="col-lg-3">
            <jsp:include page="/jsp/common/sidebar.jsp"/>
        </div>
        <div class="col-lg-9">
            <h4 class="mb-3">Work Queue <small class="text-muted">(<c:out value="${fn:length(complaints)}"/>)</small></h4>

            <div class="btn-group mb-3" role="group">
                <c:forEach var="s" items="${['ACTIVE','ASSIGNED','IN_PROGRESS','RESOLVED','ALL']}">
                    <a href="${ctx}/officer/complaints?status=${s}"
                       class="btn btn-sm ${statusFilter == s ? 'btn-scs' : 'btn-outline-secondary'}">
                        <c:out value="${s == 'ACTIVE' ? 'Active' : s}"/>
                    </a>
                </c:forEach>
            </div>

            <div class="card-scs">
                <table class="table mb-0 align-middle">
                    <thead class="table-light">
                        <tr>
                            <th>#</th>
                            <th>Title</th>
                            <th>Citizen</th>
                            <th>Dept</th>
                            <th>Priority</th>
                            <th>Status</th>
                            <th>Submitted</th>
                            <th></th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${empty complaints}">
                                <tr><td colspan="8" class="text-center text-muted py-4">No complaints match.</td></tr>
                            </c:when>
                            <c:otherwise>
                                <c:forEach items="${complaints}" var="c">
                                    <tr class="${c.isEmergency ? 'table-danger' : ''}">
                                        <td><c:out value="${c.id}"/></td>
                                        <td>
                                            <c:out value="${c.title}"/>
                                            <c:if test="${c.isEmergency}">
                                                <span class="badge emergency-badge">EMERGENCY</span>
                                            </c:if>
                                        </td>
                                        <td><small><c:out value="${c.user.name}"/></small></td>
                                        <td><small><c:out value="${not empty c.department ? c.department.name : '-'}"/></small></td>
                                        <td><span class="badge priority-${fn:toLowerCase(c.priority)}"><c:out value="${c.priority}"/></span></td>
                                        <td><span class="badge status-${fn:toLowerCase(c.status)}"><c:out value="${c.status}"/></span></td>
                                        <td><small><fmt:formatDate value="${c.createdAt}" pattern="dd MMM, HH:mm"/></small></td>
                                        <td>
                                            <a class="btn btn-sm btn-scs" href="${ctx}/officer/resolve?id=${c.id}">
                                                <c:out value="${c.status == 'RESOLVED' or c.status == 'CLOSED' ? 'View' : 'Work'}"/>
                                            </a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</main>

<jsp:include page="/jsp/common/footer.jsp"/>
