<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="Manage Users"/>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="currentUser" value="${sessionScope.loggedInUser}"/>

<jsp:include page="/jsp/common/header.jsp"/>
<jsp:include page="/jsp/common/navbar.jsp"/>

<main class="container-fluid px-4">
    <div class="row">
        <div class="col-lg-2">
            <jsp:include page="/jsp/common/sidebar.jsp"/>
        </div>
        <div class="col-lg-10">
            <h4 class="mb-3">Users</h4>

            <c:if test="${not empty sessionScope.flash}">
                <div class="alert alert-info"><c:out value="${sessionScope.flash}"/></div>
                <c:remove var="flash" scope="session"/>
            </c:if>

            <div class="btn-group mb-3" role="group">
                <c:forEach var="r" items="${['ALL','CITIZEN','OFFICER','ADMIN']}">
                    <a href="${ctx}/admin/users?role=${r}"
                       class="btn btn-sm ${roleFilter == r ? 'btn-scs' : 'btn-outline-secondary'}">
                        <c:out value="${r}"/>
                    </a>
                </c:forEach>
            </div>

            <div class="card-scs">
                <table class="table mb-0 align-middle">
                    <thead class="table-light">
                        <tr>
                            <th>#</th>
                            <th>Name</th>
                            <th>Email</th>
                            <th>Role</th>
                            <th>Active</th>
                            <th>Area</th>
                            <th>Registered</th>
                            <th class="text-end">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${users}" var="u">
                            <tr>
                                <td><c:out value="${u.id}"/></td>
                                <td><c:out value="${u.name}"/>
                                    <c:if test="${u.id == currentUser.id}">
                                        <span class="badge bg-secondary">you</span>
                                    </c:if>
                                </td>
                                <td><small><c:out value="${u.email}"/></small></td>
                                <td>
                                    <span class="badge badge-role-${fn:toLowerCase(u.role)}">
                                        <c:out value="${u.role}"/>
                                    </span>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${u.isActive}">
                                            <span class="badge bg-success">YES</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge bg-secondary">NO</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td><small><c:out value="${u.area}"/></small></td>
                                <td><small><fmt:formatDate value="${u.createdAt}" pattern="dd MMM yyyy"/></small></td>
                                <td class="text-end">
                                    <c:if test="${u.id != currentUser.id}">
                                        <form method="post" action="${ctx}/admin/users" class="d-inline">
                                            <input type="hidden" name="action" value="updateRole"/>
                                            <input type="hidden" name="id" value="${u.id}"/>
                                            <select name="role" class="form-select form-select-sm d-inline-block"
                                                    style="width:auto;" onchange="this.form.submit()">
                                                <c:forEach var="r" items="${['CITIZEN','OFFICER','ADMIN']}">
                                                    <option value="${r}" ${u.role == r ? 'selected' : ''}>
                                                        <c:out value="${r}"/>
                                                    </option>
                                                </c:forEach>
                                            </select>
                                        </form>
                                        <form method="post" action="${ctx}/admin/users" class="d-inline">
                                            <input type="hidden" name="action" value="toggleActive"/>
                                            <input type="hidden" name="id" value="${u.id}"/>
                                            <button type="submit"
                                                    class="btn btn-sm ${u.isActive ? 'btn-outline-danger' : 'btn-outline-success'}">
                                                <c:out value="${u.isActive ? 'Deactivate' : 'Activate'}"/>
                                            </button>
                                        </form>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</main>

<jsp:include page="/jsp/common/footer.jsp"/>
