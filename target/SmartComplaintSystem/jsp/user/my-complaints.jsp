<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="My Complaints"/>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<jsp:include page="/jsp/common/header.jsp"/>
<jsp:include page="/jsp/common/navbar.jsp"/>

<main class="container">
    <div class="row">
        <div class="col-lg-3">
            <jsp:include page="/jsp/common/sidebar.jsp"/>
        </div>
        <div class="col-lg-9">
            <div class="card-scs p-4">
                <div class="d-flex justify-content-between align-items-center mb-3">
                    <h4 class="mb-0">
                        My Complaints
                        <small class="text-muted">(<c:out value="${totalCount}"/> total)</small>
                    </h4>
                    <a href="${ctx}/user/submit-complaint" class="btn btn-scs btn-sm">
                        <i class="bi bi-plus-circle"></i> New
                    </a>
                </div>

                <div class="btn-group mb-3" role="group">
                    <c:forEach var="s" items="${['ALL','PENDING','ASSIGNED','IN_PROGRESS','RESOLVED','CLOSED']}">
                        <a href="${ctx}/user/my-complaints?status=${s}"
                           class="btn btn-sm ${currentFilter == s ? 'btn-scs' : 'btn-outline-secondary'}">
                            <c:out value="${s}"/>
                        </a>
                    </c:forEach>
                </div>

                <c:choose>
                    <c:when test="${empty complaints}">
                        <div class="text-center text-muted py-5">
                            No complaints found for this filter.
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="table-responsive">
                            <table class="table table-hover align-middle">
                                <thead class="table-light">
                                    <tr>
                                        <th>#</th>
                                        <th>Title</th>
                                        <th>Category</th>
                                        <th>Priority</th>
                                        <th>Status</th>
                                        <th>Submitted</th>
                                        <th></th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${complaints}" var="c">
                                        <tr>
                                            <td><c:out value="${c.id}"/></td>
                                            <td>
                                                <c:out value="${c.title}"/>
                                                <c:if test="${c.isEmergency}">
                                                    <span class="badge emergency-badge">EMERGENCY</span>
                                                </c:if>
                                            </td>
                                            <td><small class="text-muted"><c:out value="${c.category}"/></small></td>
                                            <td>
                                                <span class="badge priority-${fn:toLowerCase(c.priority)}">
                                                    <c:out value="${c.priority}"/>
                                                </span>
                                            </td>
                                            <td>
                                                <span class="badge status-${fn:toLowerCase(c.status)}">
                                                    <c:out value="${c.status}"/>
                                                </span>
                                            </td>
                                            <td>
                                                <small><fmt:formatDate value="${c.createdAt}" pattern="dd MMM yyyy"/></small>
                                            </td>
                                            <td>
                                                <a href="${ctx}/user/track-complaint?id=${c.id}"
                                                   class="btn btn-sm btn-outline-primary">Track</a>
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
