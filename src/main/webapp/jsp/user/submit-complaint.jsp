<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Submit Complaint"/>
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
            <div class="card-scs p-4">
                <h4><i class="bi bi-plus-circle"></i> Submit a Complaint</h4>
                <p class="text-muted">
                    Describe the issue and we'll auto-detect priority, category, tags, and ETA.
                </p>

                <c:if test="${not empty error}">
                    <div class="alert alert-danger"><c:out value="${error}"/></div>
                </c:if>

                <form id="complaintForm" method="post" action="${ctx}/user/submit-complaint"
                      enctype="multipart/form-data" novalidate>

                    <div class="mb-3">
                        <label for="title" class="form-label">Title *</label>
                        <input type="text" class="form-control" id="title" name="title"
                               required maxlength="200" value="<c:out value='${param.title}'/>">
                    </div>

                    <div class="mb-3">
                        <label for="description" class="form-label d-flex justify-content-between">
                            <span>Description *</span>
                            <button type="button" id="voiceBtn" class="btn btn-sm btn-outline-primary">
                                <i class="bi bi-mic"></i> <span id="voiceBtnLabel">Use Voice</span>
                            </button>
                        </label>
                        <textarea class="form-control" id="description" name="description"
                                  rows="5" required><c:out value="${param.description}"/></textarea>
                        <div class="form-text" id="voiceStatus"></div>
                    </div>

                    <div class="row g-3">
                        <div class="col-md-6">
                            <label for="category" class="form-label">Category</label>
                            <select class="form-select" id="category" name="category">
                                <option value="">Auto-detect</option>
                                <c:forEach items="${categories}" var="cat">
                                    <option value="${cat}" ${param.category == cat ? 'selected' : ''}>
                                        <c:out value="${cat}"/>
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-md-6">
                            <label for="departmentId" class="form-label">Department (optional)</label>
                            <select class="form-select" id="departmentId" name="departmentId">
                                <option value="">Let admin assign</option>
                                <c:forEach items="${departments}" var="d">
                                    <option value="${d.id}"><c:out value="${d.name}"/></option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="col-md-6">
                            <label for="location" class="form-label">Location</label>
                            <input type="text" class="form-control" id="location" name="location"
                                   value="<c:out value='${not empty param.location ? param.location : user.area}'/>"
                                   maxlength="150">
                        </div>
                        <div class="col-md-6">
                            <label for="image" class="form-label">Attach Image (optional)</label>
                            <input type="file" class="form-control" id="image" name="image" accept="image/*">
                        </div>

                        <div class="col-12">
                            <img id="imagePreview" class="img-thumbnail d-none" style="max-height:200px"/>
                        </div>
                    </div>

                    <div id="smartPreview" class="card-scs p-3 mt-4 d-none">
                        <h6 class="text-muted text-uppercase small">Smart Preview</h6>
                        <div class="row small">
                            <div class="col-md-4 mb-2">
                                <div class="text-muted">Sentiment</div>
                                <div class="fw-semibold" id="previewSentiment">&mdash;</div>
                            </div>
                            <div class="col-md-4 mb-2">
                                <div class="text-muted">Priority</div>
                                <div class="fw-semibold" id="previewPriority">&mdash;</div>
                            </div>
                            <div class="col-md-4 mb-2">
                                <div class="text-muted">Emergency</div>
                                <div class="fw-semibold" id="previewEmergency">&mdash;</div>
                            </div>
                            <div class="col-12 mb-2">
                                <div class="text-muted">Tags</div>
                                <div id="previewTags">&mdash;</div>
                            </div>
                            <div class="col-12">
                                <div class="text-muted">Suggested Solutions</div>
                                <ul id="previewRecommendations" class="mb-0 ps-3"></ul>
                            </div>
                        </div>
                    </div>

                    <div id="emergencyWarning" class="alert alert-danger mt-3 d-none">
                        <i class="bi bi-exclamation-triangle"></i>
                        <strong>Emergency keywords detected.</strong> This complaint will be marked CRITICAL and routed immediately.
                    </div>

                    <div class="mt-4 d-flex gap-2">
                        <button type="submit" class="btn btn-scs">
                            <i class="bi bi-send"></i> Submit Complaint
                        </button>
                        <a href="${ctx}/user/dashboard" class="btn btn-outline-secondary">Cancel</a>
                    </div>
                </form>
            </div>
        </div>
    </div>
</main>

<script src="${ctx}/js/voice-input.js"></script>
<script src="${ctx}/js/complaint-form.js"></script>

<jsp:include page="/jsp/common/footer.jsp"/>
