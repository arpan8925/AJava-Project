<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="pageTitle" value="My Profile"/>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="user" value="${sessionScope.loggedInUser}"/>

<jsp:include page="/jsp/common/header.jsp"/>
<jsp:include page="/jsp/common/navbar.jsp"/>

<main class="container">
    <div class="row">
        <div class="col-lg-3">
            <jsp:include page="/jsp/common/sidebar.jsp"/>
        </div>
        <div class="col-lg-9">
            <div class="card-scs p-4" style="max-width: 560px;">
                <h4 class="mb-3"><i class="bi bi-person-circle"></i> My Profile</h4>
                <dl class="row mb-0">
                    <dt class="col-sm-4">Name</dt>
                    <dd class="col-sm-8"><c:out value="${user.name}"/></dd>
                    <dt class="col-sm-4">Email</dt>
                    <dd class="col-sm-8"><c:out value="${user.email}"/></dd>
                    <dt class="col-sm-4">Role</dt>
                    <dd class="col-sm-8">
                        <span class="badge badge-role-${fn:toLowerCase(user.role)}">
                            <c:out value="${user.role}"/>
                        </span>
                    </dd>
                    <dt class="col-sm-4">Phone</dt>
                    <dd class="col-sm-8"><c:out value="${not empty user.phone ? user.phone : '-'}"/></dd>
                    <dt class="col-sm-4">Area</dt>
                    <dd class="col-sm-8"><c:out value="${not empty user.area ? user.area : '-'}"/></dd>
                </dl>
                <p class="text-muted small mt-3 mb-0">
                    <em>Editable profile form arrives in a later phase.</em>
                </p>
            </div>
        </div>
    </div>
</main>

<jsp:include page="/jsp/common/footer.jsp"/>
