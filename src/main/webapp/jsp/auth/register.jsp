<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Register"/>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<jsp:include page="/jsp/common/header.jsp"/>
<jsp:include page="/jsp/common/navbar.jsp"/>

<main class="container">
    <div class="card-scs form-signin" style="max-width: 520px;">
        <h3 class="text-center mb-4"><i class="bi bi-person-plus"></i> Create Account</h3>

        <c:if test="${not empty error}">
            <div class="alert alert-danger" role="alert">
                <i class="bi bi-exclamation-triangle"></i> <c:out value="${error}"/>
            </div>
        </c:if>

        <form method="post" action="${ctx}/register" novalidate>
            <div class="row g-3">
                <div class="col-12">
                    <label for="name" class="form-label">Full Name</label>
                    <input type="text" class="form-control" id="name" name="name"
                           value="<c:out value='${param.name}'/>" required minlength="2" maxlength="100">
                </div>
                <div class="col-12">
                    <label for="email" class="form-label">Email</label>
                    <input type="email" class="form-control" id="email" name="email"
                           value="<c:out value='${param.email}'/>" required>
                </div>
                <div class="col-md-6">
                    <label for="password" class="form-label">Password</label>
                    <input type="password" class="form-control" id="password" name="password"
                           required minlength="6">
                    <div class="form-text">Minimum 6 characters.</div>
                </div>
                <div class="col-md-6">
                    <label for="phone" class="form-label">Phone</label>
                    <input type="tel" class="form-control" id="phone" name="phone"
                           value="<c:out value='${param.phone}'/>" pattern="[0-9+\-\s]{7,15}">
                </div>
                <div class="col-md-6">
                    <label for="area" class="form-label">Area / Locality</label>
                    <input type="text" class="form-control" id="area" name="area"
                           value="<c:out value='${param.area}'/>" maxlength="100">
                </div>
                <div class="col-md-6">
                    <label for="role" class="form-label">Register As</label>
                    <select class="form-select" id="role" name="role">
                        <option value="CITIZEN" ${param.role == 'OFFICER' ? '' : 'selected'}>Citizen</option>
                        <option value="OFFICER" ${param.role == 'OFFICER' ? 'selected' : ''}>Department Officer</option>
                    </select>
                    <div class="form-text">Admin accounts are provisioned manually.</div>
                </div>
            </div>

            <button type="submit" class="btn btn-scs w-100 mt-4">Create Account</button>
        </form>

        <p class="text-center text-muted mt-3 mb-0">
            Already have an account? <a href="${ctx}/login">Sign in</a>
        </p>
    </div>
</main>

<jsp:include page="/jsp/common/footer.jsp"/>
