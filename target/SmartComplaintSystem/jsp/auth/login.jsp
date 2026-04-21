<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Sign In"/>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<jsp:include page="/jsp/common/header.jsp"/>
<jsp:include page="/jsp/common/navbar.jsp"/>

<main class="container">
    <div class="card-scs form-signin">
        <h3 class="text-center mb-4"><i class="bi bi-box-arrow-in-right"></i> Sign In</h3>

        <c:if test="${not empty error}">
            <div class="alert alert-danger" role="alert">
                <i class="bi bi-exclamation-triangle"></i> <c:out value="${error}"/>
            </div>
        </c:if>
        <c:if test="${not empty flash}">
            <div class="alert alert-success" role="alert">
                <i class="bi bi-check-circle"></i> <c:out value="${flash}"/>
            </div>
        </c:if>

        <form method="post" action="${ctx}/login" novalidate>
            <div class="mb-3">
                <label for="email" class="form-label">Email</label>
                <input type="email" class="form-control" id="email" name="email"
                       value="<c:out value='${param.email}'/>" required autofocus>
            </div>
            <div class="mb-3">
                <label for="password" class="form-label">Password</label>
                <input type="password" class="form-control" id="password" name="password" required>
            </div>
            <button type="submit" class="btn btn-scs w-100">Sign In</button>
        </form>

        <p class="text-center text-muted mt-3 mb-0">
            New here? <a href="${ctx}/register">Create an account</a>
        </p>
    </div>
</main>

<jsp:include page="/jsp/common/footer.jsp"/>
