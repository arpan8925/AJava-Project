<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Access Denied"/>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<jsp:include page="/jsp/common/header.jsp"/>
<jsp:include page="/jsp/common/navbar.jsp"/>

<main class="container">
    <div class="error-page">
        <div class="code">403</div>
        <h2 class="mb-3">Access Denied</h2>
        <p class="text-muted">
            You don't have permission to view this page. If you think this is a mistake, please
            contact your administrator.
        </p>
        <a href="${ctx}/dashboard" class="btn btn-scs mt-3"><i class="bi bi-speedometer2"></i> Go to My Dashboard</a>
    </div>
</main>

<jsp:include page="/jsp/common/footer.jsp"/>
