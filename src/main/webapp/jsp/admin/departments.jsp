<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Manage Departments"/>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<jsp:include page="/jsp/common/header.jsp"/>
<jsp:include page="/jsp/common/navbar.jsp"/>

<main class="container-fluid px-4">
    <div class="row">
        <div class="col-lg-2">
            <jsp:include page="/jsp/common/sidebar.jsp"/>
        </div>
        <div class="col-lg-10">
            <div class="d-flex justify-content-between align-items-center mb-3">
                <h4 class="mb-0">Departments</h4>
                <button class="btn btn-scs" data-bs-toggle="modal" data-bs-target="#addDeptModal">
                    <i class="bi bi-plus-circle"></i> Add Department
                </button>
            </div>

            <c:if test="${not empty sessionScope.flash}">
                <div class="alert alert-info"><c:out value="${sessionScope.flash}"/></div>
                <c:remove var="flash" scope="session"/>
            </c:if>

            <div class="card-scs">
                <table class="table mb-0">
                    <thead class="table-light">
                        <tr>
                            <th>#</th>
                            <th>Name</th>
                            <th>Description</th>
                            <th>Complaints</th>
                            <th class="text-end">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${departments}" var="d">
                            <tr>
                                <td><c:out value="${d.id}"/></td>
                                <td><c:out value="${d.name}"/></td>
                                <td><small class="text-muted"><c:out value="${d.description}"/></small></td>
                                <td><c:out value="${loadByDept[d.id]}"/></td>
                                <td class="text-end">
                                    <button class="btn btn-sm btn-outline-primary"
                                            data-bs-toggle="modal"
                                            data-bs-target="#editDept${d.id}">Edit</button>

                                    <form method="post" action="${ctx}/admin/departments" class="d-inline"
                                          onsubmit="return confirm('Delete this department?');">
                                        <input type="hidden" name="action" value="delete"/>
                                        <input type="hidden" name="id" value="${d.id}"/>
                                        <button type="submit" class="btn btn-sm btn-outline-danger">Delete</button>
                                    </form>
                                </td>
                            </tr>

                            <div class="modal fade" id="editDept${d.id}" tabindex="-1">
                                <div class="modal-dialog">
                                    <form class="modal-content" method="post" action="${ctx}/admin/departments">
                                        <input type="hidden" name="action" value="update"/>
                                        <input type="hidden" name="id" value="${d.id}"/>
                                        <div class="modal-header">
                                            <h5 class="modal-title">Edit &mdash; <c:out value="${d.name}"/></h5>
                                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                        </div>
                                        <div class="modal-body">
                                            <div class="mb-3">
                                                <label class="form-label">Name</label>
                                                <input type="text" class="form-control" name="name"
                                                       value="<c:out value='${d.name}'/>" required>
                                            </div>
                                            <div class="mb-0">
                                                <label class="form-label">Description</label>
                                                <textarea class="form-control" name="description" rows="3"><c:out value="${d.description}"/></textarea>
                                            </div>
                                        </div>
                                        <div class="modal-footer">
                                            <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">Cancel</button>
                                            <button type="submit" class="btn btn-scs">Save</button>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</main>

<div class="modal fade" id="addDeptModal" tabindex="-1">
    <div class="modal-dialog">
        <form class="modal-content" method="post" action="${ctx}/admin/departments">
            <input type="hidden" name="action" value="create"/>
            <div class="modal-header">
                <h5 class="modal-title">Add Department</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body">
                <div class="mb-3">
                    <label class="form-label">Name *</label>
                    <input type="text" class="form-control" name="name" required>
                </div>
                <div class="mb-0">
                    <label class="form-label">Description</label>
                    <textarea class="form-control" name="description" rows="3"></textarea>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">Cancel</button>
                <button type="submit" class="btn btn-scs">Create</button>
            </div>
        </form>
    </div>
</div>

<jsp:include page="/jsp/common/footer.jsp"/>
