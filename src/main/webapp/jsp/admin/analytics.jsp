<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Analytics"/>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<jsp:include page="/jsp/common/header.jsp"/>
<jsp:include page="/jsp/common/navbar.jsp"/>

<main class="container-fluid px-4">
    <div class="row">
        <div class="col-lg-2">
            <jsp:include page="/jsp/common/sidebar.jsp"/>
        </div>
        <div class="col-lg-10">
            <h4 class="mb-3">Analytics &mdash; Last <c:out value="${rangeDays}"/> days</h4>

            <div class="row g-3 mb-3">
                <div class="col-md-4">
                    <div class="card-scs p-3 text-center">
                        <div class="text-muted small">Complaints in range</div>
                        <div class="h3 mb-0"><c:out value="${totalRange}"/></div>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="card-scs p-3 text-center">
                        <div class="text-muted small">Daily rate</div>
                        <div class="h3 mb-0">
                            <c:out value="${forecast.dailyRate > 0 ? forecast.dailyRate : 0}"/>
                        </div>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="card-scs p-3 text-center">
                        <div class="text-muted small">Forecast (next 30 days)</div>
                        <div class="h3 mb-0">
                            <c:out value="${forecast.predictedNext30Days}"/>
                            <small class="text-muted ms-1">
                                <c:choose>
                                    <c:when test="${forecast.trend == 'UP'}">
                                        <i class="bi bi-arrow-up text-danger"></i> rising
                                    </c:when>
                                    <c:when test="${forecast.trend == 'DOWN'}">
                                        <i class="bi bi-arrow-down text-success"></i> easing
                                    </c:when>
                                    <c:otherwise>
                                        <i class="bi bi-dash text-muted"></i> steady
                                    </c:otherwise>
                                </c:choose>
                            </small>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row g-3">
                <div class="col-lg-12">
                    <div class="card-scs p-3">
                        <h6 class="text-muted text-uppercase small mb-2">Weekly Trend</h6>
                        <canvas id="trendChart" height="80"></canvas>
                    </div>
                </div>
                <div class="col-lg-4">
                    <div class="card-scs p-3">
                        <h6 class="text-muted text-uppercase small mb-2">Priority</h6>
                        <canvas id="priorityChart" height="180"></canvas>
                    </div>
                </div>
                <div class="col-lg-4">
                    <div class="card-scs p-3">
                        <h6 class="text-muted text-uppercase small mb-2">Status</h6>
                        <canvas id="statusChart" height="180"></canvas>
                    </div>
                </div>
                <div class="col-lg-4">
                    <div class="card-scs p-3">
                        <h6 class="text-muted text-uppercase small mb-2">Departments</h6>
                        <canvas id="departmentChart" height="180"></canvas>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>

<script>
    window.SCS_CHART_DATA = {
        trendLabels: ${trendLabelsJson},
        trendSeries: ${trendSeriesJson},
        priorities:  ${priorityJson},
        statuses:    ${statusJson},
        departments: ${departmentsJson}
    };
</script>
<script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.1/dist/chart.umd.min.js"></script>
<script src="${ctx}/js/charts.js"></script>

<jsp:include page="/jsp/common/footer.jsp"/>
