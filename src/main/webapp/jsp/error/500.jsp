<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Server Error"/>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<jsp:include page="/jsp/common/header.jsp"/>
<jsp:include page="/jsp/common/navbar.jsp"/>

<main class="container">
    <div class="error-page">
        <div class="code">500</div>
        <h2 class="mb-3">Something Went Wrong</h2>
        <p class="text-muted">
            An unexpected error occurred while processing your request. We've been notified and are
            looking into it.
        </p>
        <a href="${ctx}/" class="btn btn-scs mt-3"><i class="bi bi-house"></i> Back Home</a>
    </div>
</main>

<jsp:include page="/jsp/common/footer.jsp"/>
