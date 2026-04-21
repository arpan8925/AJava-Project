<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Officer Dashboard"/>
<c:set var="user" value="${sessionScope.loggedInUser}"/>

<jsp:include page="/jsp/common/header.jsp"/>
<jsp:include page="/jsp/common/navbar.jsp"/>

<main class="container">
    <div class="row">
        <div class="col-lg-3">
            <jsp:include page="/jsp/common/sidebar.jsp"/>
        </div>
        <div class="col-lg-9">
            <div class="card-scs p-4">
                <h3>Officer Console</h3>
                <p class="text-muted">
                    Welcome, <c:out value="${user.name}"/>. You'll see your assigned complaints here
                    with status controls and citizen replies.
                </p>
                <p class="text-muted small mb-0">
                    <em>Assigned-complaint list and resolution tooling arrive in Phase 6.</em>
                </p>
            </div>
        </div>
    </div>
</main>

<jsp:include page="/jsp/common/footer.jsp"/>
