<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="Resolve #${complaint.id}"/>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<jsp:include page="/jsp/common/header.jsp"/>
<jsp:include page="/jsp/common/navbar.jsp"/>

<main class="container">
    <div class="mb-3">
        <a href="${ctx}/officer/complaints" class="text-muted small">
            <i class="bi bi-arrow-left"></i> Back to Work Queue
        </a>
    </div>

    <div class="row g-4">
        <div class="col-lg-8">
            <div class="card-scs p-4 ${complaint.isEmergency ? 'border border-danger' : ''}">
                <div class="d-flex justify-content-between align-items-start mb-2">
                    <div>
                        <div class="text-muted small">Complaint #<c:out value="${complaint.id}"/></div>
                        <h4 class="mb-0"><c:out value="${complaint.title}"/></h4>
                    </div>
                    <div class="text-end">
                        <span class="badge status-${fn:toLowerCase(complaint.status)} fs-6">
                            <c:out value="${complaint.status}"/>
                        </span>
                        <c:if test="${complaint.isEmergency}">
                            <span class="badge emergency-badge">EMERGENCY</span>
                        </c:if>
                    </div>
                </div>

                <div class="row small text-muted mb-3">
                    <div class="col-sm-6">
                        <div>Citizen: <strong><c:out value="${complaint.user.name}"/></strong>
                             &middot; <c:out value="${complaint.user.email}"/></div>
                        <c:if test="${not empty complaint.user.phone}">
                            <div><i class="bi bi-telephone"></i> <c:out value="${complaint.user.phone}"/></div>
                        </c:if>
                        <c:if test="${not empty complaint.location}">
                            <div><i class="bi bi-geo-alt"></i> <c:out value="${complaint.location}"/></div>
                        </c:if>
                    </div>
                    <div class="col-sm-6 text-sm-end">
                        <div>Submitted <fmt:formatDate value="${complaint.createdAt}" pattern="dd MMM yyyy, HH:mm"/></div>
                        <div>Priority: <span class="badge priority-${fn:toLowerCase(complaint.priority)}"><c:out value="${complaint.priority}"/></span></div>
                        <c:if test="${not empty complaint.department}">
                            <div>Dept: <strong><c:out value="${complaint.department.name}"/></strong></div>
                        </c:if>
                        <c:if test="${not empty complaint.etaHours}">
                            <div>ETA: <c:out value="${complaint.etaHours}"/> hours</div>
                        </c:if>
                        <c:if test="${not empty complaint.resolvedAt}">
                            <div class="text-success">Resolved <fmt:formatDate value="${complaint.resolvedAt}" pattern="dd MMM yyyy, HH:mm"/></div>
                        </c:if>
                    </div>
                </div>

                <c:if test="${not empty complaint.summary}">
                    <div class="mb-3">
                        <div class="text-muted text-uppercase small">Auto-Summary</div>
                        <p class="mb-0 fst-italic"><c:out value="${complaint.summary}"/></p>
                    </div>
                </c:if>

                <div class="mb-3">
                    <div class="text-muted text-uppercase small">Description</div>
                    <p class="mb-0" style="white-space: pre-wrap;"><c:out value="${complaint.description}"/></p>
                </div>

                <c:if test="${not empty complaint.tags}">
                    <div class="mb-3">
                        <div class="text-muted text-uppercase small mb-1">Tags</div>
                        <c:forEach items="${fn:split(complaint.tags, ',')}" var="t">
                            <span class="badge bg-light text-dark border">#<c:out value="${fn:trim(t)}"/></span>
                        </c:forEach>
                    </div>
                </c:if>

                <hr>

                <h6 class="mb-3">Conversation</h6>
                <c:choose>
                    <c:when test="${empty replies}">
                        <p class="text-muted small">No replies yet.</p>
                    </c:when>
                    <c:otherwise>
                        <div class="d-flex flex-column gap-2 mb-3">
                            <c:forEach items="${replies}" var="r">
                                <div class="card-scs p-3">
                                    <div class="small text-muted mb-1">
                                        <strong><c:out value="${r.user.name}"/></strong>
                                        <span class="badge badge-role-${fn:toLowerCase(r.user.role)} ms-1"><c:out value="${r.user.role}"/></span>
                                        <span class="ms-2"><fmt:formatDate value="${r.createdAt}" pattern="dd MMM, HH:mm"/></span>
                                    </div>
                                    <div><c:out value="${r.message}"/></div>
                                </div>
                            </c:forEach>
                        </div>
                    </c:otherwise>
                </c:choose>

                <c:if test="${complaint.status != 'CLOSED'}">
                    <form method="post" action="${ctx}/officer/resolve" class="mt-3">
                        <input type="hidden" name="complaintId" value="${complaint.id}"/>
                        <div class="mb-2">
                            <label for="message" class="form-label small">Reply / notes</label>
                            <textarea class="form-control" id="message" name="message" rows="3"
                                      placeholder="Add an update for the citizen (optional)"></textarea>
                        </div>

                        <div class="d-flex flex-wrap gap-2">
                            <c:if test="${complaint.status == 'ASSIGNED' or complaint.status == 'PENDING'}">
                                <button type="submit" name="action" value="start" class="btn btn-warning">
                                    <i class="bi bi-play-circle"></i> Mark In Progress
                                </button>
                            </c:if>
                            <c:if test="${complaint.status == 'IN_PROGRESS' or complaint.status == 'ASSIGNED'}">
                                <button type="submit" name="action" value="resolve" class="btn btn-success">
                                    <i class="bi bi-check-circle"></i> Mark Resolved
                                </button>
                            </c:if>
                            <c:if test="${complaint.status == 'RESOLVED'}">
                                <button type="submit" name="action" value="reopen" class="btn btn-outline-warning">
                                    <i class="bi bi-arrow-counterclockwise"></i> Reopen
                                </button>
                            </c:if>
                            <button type="submit" name="action" value="reply" class="btn btn-outline-primary">
                                <i class="bi bi-reply"></i> Reply only
                            </button>
                        </div>
                    </form>
                </c:if>
            </div>
        </div>

        <div class="col-lg-4">
            <div class="card-scs p-3">
                <h6 class="text-muted text-uppercase small mb-2">Status Flow</h6>
                <ol class="mb-0 ps-3 small">
                    <li class="${complaint.status == 'PENDING'     ? 'fw-bold' : 'text-muted'}">Pending</li>
                    <li class="${complaint.status == 'ASSIGNED'    ? 'fw-bold' : 'text-muted'}">Assigned</li>
                    <li class="${complaint.status == 'IN_PROGRESS' ? 'fw-bold' : 'text-muted'}">In Progress</li>
                    <li class="${complaint.status == 'RESOLVED'    ? 'fw-bold text-success' : 'text-muted'}">Resolved</li>
                </ol>
            </div>
        </div>
    </div>
</main>

<jsp:include page="/jsp/common/footer.jsp"/>
