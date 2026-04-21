<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="user" value="${sessionScope.loggedInUser}"/>

<nav class="navbar navbar-expand-lg navbar-scs">
    <div class="container">
        <a class="navbar-brand fw-bold" href="${ctx}/">
            <i class="bi bi-shield-check"></i> Smart Complaint System
        </a>

        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#scsNav"
                aria-controls="scsNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="scsNav">
            <ul class="navbar-nav me-auto">
                <c:choose>
                    <c:when test="${user != null && user.role == 'CITIZEN'}">
                        <li class="nav-item"><a class="nav-link" href="${ctx}/user/dashboard">Dashboard</a></li>
                        <li class="nav-item"><a class="nav-link" href="${ctx}/user/submit-complaint">Submit Complaint</a></li>
                        <li class="nav-item"><a class="nav-link" href="${ctx}/user/my-complaints">My Complaints</a></li>
                    </c:when>
                    <c:when test="${user != null && user.role == 'ADMIN'}">
                        <li class="nav-item"><a class="nav-link" href="${ctx}/admin/dashboard">Dashboard</a></li>
                        <li class="nav-item"><a class="nav-link" href="${ctx}/admin/complaints">Complaints</a></li>
                        <li class="nav-item"><a class="nav-link" href="${ctx}/admin/departments">Departments</a></li>
                        <li class="nav-item"><a class="nav-link" href="${ctx}/admin/users">Users</a></li>
                        <li class="nav-item"><a class="nav-link" href="${ctx}/admin/analytics">Analytics</a></li>
                    </c:when>
                    <c:when test="${user != null && user.role == 'OFFICER'}">
                        <li class="nav-item"><a class="nav-link" href="${ctx}/officer/dashboard">Dashboard</a></li>
                        <li class="nav-item"><a class="nav-link" href="${ctx}/officer/complaints">Assigned</a></li>
                    </c:when>
                </c:choose>
            </ul>

            <ul class="navbar-nav">
                <c:choose>
                    <c:when test="${user != null}">
                        <li class="nav-item">
                            <span class="navbar-text text-white me-3">
                                <i class="bi bi-person-circle"></i>
                                <c:out value="${user.name}"/>
                                <span class="badge badge-role-${fn:toLowerCase(user.role)}"><c:out value="${user.role}"/></span>
                            </span>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${ctx}/logout"><i class="bi bi-box-arrow-right"></i> Logout</a>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li class="nav-item"><a class="nav-link" href="${ctx}/login">Login</a></li>
                        <li class="nav-item"><a class="nav-link" href="${ctx}/register">Register</a></li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </div>
</nav>
