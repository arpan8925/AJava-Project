<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Page Not Found"/>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<jsp:include page="/jsp/common/header.jsp"/>
<jsp:include page="/jsp/common/navbar.jsp"/>

<main class="container">
    <div class="error-page">
        <div class="code">404</div>
        <h2 class="mb-3">Page Not Found</h2>
        <p class="text-muted">The page you're looking for doesn't exist or has been moved.</p>
        <a href="${ctx}/" class="btn btn-scs mt-3"><i class="bi bi-house"></i> Back Home</a>
    </div>
</main>

<jsp:include page="/jsp/common/footer.jsp"/>
