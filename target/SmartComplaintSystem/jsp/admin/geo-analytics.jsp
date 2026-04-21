<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Geo Analytics"/>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<jsp:include page="/jsp/common/header.jsp"/>
<jsp:include page="/jsp/common/navbar.jsp"/>

<main class="container-fluid px-4">
    <div class="row">
        <div class="col-lg-2">
            <jsp:include page="/jsp/common/sidebar.jsp"/>
        </div>
        <div class="col-lg-10">
            <h4 class="mb-3">Geo Analytics &mdash; Hotspots</h4>

            <div class="row g-3">
                <div class="col-lg-8">
                    <div class="card-scs p-3">
                        <h6 class="text-muted text-uppercase small mb-2">Top 10 Areas by Complaints</h6>
                        <canvas id="locationChart" height="140"></canvas>
                    </div>
                </div>
                <div class="col-lg-4">
                    <div class="card-scs p-3">
                        <h6 class="text-muted text-uppercase small mb-2">Hotspot Leaderboard</h6>
                        <c:choose>
                            <c:when test="${empty topLocations}">
                                <p class="text-muted mb-0">No location data yet.</p>
                            </c:when>
                            <c:otherwise>
                                <ol class="mb-0 ps-3 small">
                                    <c:forEach items="${topLocations}" var="e">
                                        <li class="py-1">
                                            <strong><c:out value="${e.key}"/></strong>
                                            <span class="text-muted">&mdash; <c:out value="${e.value}"/> complaints</span>
                                        </li>
                                    </c:forEach>
                                </ol>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>

<script>
    window.SCS_CHART_DATA = {
        locations: ${locationsJson}
    };
</script>
<script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.1/dist/chart.umd.min.js"></script>
<script src="${ctx}/js/charts.js"></script>

<jsp:include page="/jsp/common/footer.jsp"/>
