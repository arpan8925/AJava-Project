<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="user" value="${sessionScope.loggedInUser}"/>

<aside class="card-scs p-3 mb-3">
    <h6 class="text-muted text-uppercase small mb-2">Quick Links</h6>
    <ul class="nav flex-column">
        <c:choose>
            <c:when test="${user.role == 'CITIZEN'}">
                <li class="nav-item"><a class="nav-link px-0" href="${ctx}/user/dashboard"><i class="bi bi-speedometer2"></i> Dashboard</a></li>
                <li class="nav-item"><a class="nav-link px-0" href="${ctx}/user/submit-complaint"><i class="bi bi-plus-circle"></i> Submit</a></li>
                <li class="nav-item"><a class="nav-link px-0" href="${ctx}/user/my-complaints"><i class="bi bi-list-ul"></i> My Complaints</a></li>
            </c:when>
            <c:when test="${user.role == 'ADMIN'}">
                <li class="nav-item"><a class="nav-link px-0" href="${ctx}/admin/dashboard"><i class="bi bi-speedometer2"></i> Dashboard</a></li>
                <li class="nav-item"><a class="nav-link px-0" href="${ctx}/admin/complaints"><i class="bi bi-clipboard-check"></i> Complaints</a></li>
                <li class="nav-item"><a class="nav-link px-0" href="${ctx}/admin/departments"><i class="bi bi-building"></i> Departments</a></li>
                <li class="nav-item"><a class="nav-link px-0" href="${ctx}/admin/users"><i class="bi bi-people"></i> Users</a></li>
                <li class="nav-item"><a class="nav-link px-0" href="${ctx}/admin/analytics"><i class="bi bi-bar-chart"></i> Analytics</a></li>
                <li class="nav-item"><a class="nav-link px-0" href="${ctx}/admin/geo-analytics"><i class="bi bi-geo-alt"></i> Geo-Analytics</a></li>
            </c:when>
            <c:when test="${user.role == 'OFFICER'}">
                <li class="nav-item"><a class="nav-link px-0" href="${ctx}/officer/dashboard"><i class="bi bi-speedometer2"></i> Dashboard</a></li>
                <li class="nav-item"><a class="nav-link px-0" href="${ctx}/officer/complaints"><i class="bi bi-inbox"></i> Assigned</a></li>
            </c:when>
        </c:choose>
    </ul>
</aside>
