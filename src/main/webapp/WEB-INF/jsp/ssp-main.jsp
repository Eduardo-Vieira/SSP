<%--

    Licensed to Apereo under one or more contributor license
    agreements. See the NOTICE file distributed with this work
    for additional information regarding copyright ownership.
    Apereo licenses this file to you under the Apache License,
    Version 2.0 (the "License"); you may not use this file
    except in compliance with the License.  You may obtain a
    copy of the License at the following location:

      http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing,
    software distributed under the License is distributed on an
    "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
    KIND, either express or implied.  See the License for the
    specific language governing permissions and limitations
    under the License.

--%>
<jsp:directive.include file="/WEB-INF/jsp/include.jsp"/>

<c:set var="n"><portlet:namespace/></c:set>

<c:set var="version" scope="request" value="${model.cachebust}"/>

 
<!-- FontAwesome -->
<link rel="stylesheet" type="text/css" href="<c:url value="${version}/resources/css/font-awesome.min.css" />" />

<!-- ExtJS Styles -->
<link rel="stylesheet" type="text/css" href="<c:url value="/js/libs/ext-4.1/resources/css/ext-all.css" />">
<link rel="stylesheet" type="text/css" href="<c:url value="/js/libs/ext-4.1/src/ux/css/CheckHeader.css" />">
<link rel="stylesheet" type="text/css" href="<c:url value="/js/libs/ext-4.1/src/ux/css/ItemSelector.css" />">
<link rel="stylesheet" type="text/css" href="<c:url value="/js/libs/ext-4.1/src/ux/grid/css/GridFilters.css" />">
<link rel="stylesheet" type="text/css" href="<c:url value="/js/libs/ext-4.1/src/ux/grid/css/RangeMenu.css" />">

<!-- SSP Theme -->
<link href="<c:url value="${version}/resources/css/tabs.css" />" rel="stylesheet" type="text/css" />
<link href="<c:url value="${version}/resources/css/ssp-ext-theme.css" />" rel="stylesheet" type="text/css" />

<!-- ExtJS Lib -->
<c:choose>
    <c:when test="${model.useMinified}">
        <script type="text/javascript" src="<c:url value="/js/libs/ext-4.1/ext-all.js" />"></script>
    </c:when>
    <c:otherwise>
        <script type="text/javascript" src="<c:url value="/js/libs/ext-4.1/ext.js" />"></script>
    </c:otherwise>
</c:choose>



<script type="text/javascript" src="<c:url value="/js/libs/ext-4.1/src/ux/CheckColumn.js" />"></script>
<script type="text/javascript" src="<c:url value="/js/libs/ext-4.1/src/ux/form/MultiSelect.js" />"></script>
<script type="text/javascript" src="<c:url value="/js/libs/ext-4.1/src/ux/form/ItemSelector.js" />"></script>   
<script type="text/javascript" src="<c:url value="/js/libs/ext-4.1/src/ux/grid/FiltersFeature.js" />"></script>  
<script type="text/javascript" src="<c:url value="/js/libs/ext-4.1/src/ux/grid/TransformGrid.js" />"></script>   
<script type="text/javascript" src="<c:url value="/js/libs/ext-4.1/src/ux/grid/filter/Filter.js" />"></script>   
<script type="text/javascript" src="<c:url value="/js/libs/ext-4.1/src/ux/grid/filter/StringFilter.js" />"></script>   
<script type="text/javascript" src="<c:url value="/js/libs/ext-4.1/src/ux/grid/filter/DateFilter.js" />"></script>   
<script type="text/javascript" src="<c:url value="/js/libs/ext-4.1/src/ux/grid/filter/ListFilter.js" />"></script>   
<script type="text/javascript" src="<c:url value="/js/libs/ext-4.1/src/ux/grid/filter/NumericFilter.js" />"></script>   
<script type="text/javascript" src="<c:url value="/js/libs/ext-4.1/src/ux/grid/filter/BooleanFilter.js" />"></script>  
<script type="text/javascript" src="<c:url value="/js/libs/ext-4.1/src/ux/grid/menu/RangeMenu.js" />"></script>   
<script type="text/javascript" src="<c:url value="/js/libs/ext-4.1/src/ux/grid/menu/ListMenu.js" />"></script>   

<!-- DEFT Lib -->
<script type="text/javascript" src="<c:url value="/js/libs/deft/deft-0.6.8pre.js" />"></script>

<script type="text/javascript">
    // setting renderSSPFullScreen to true will render the app 
    // in the full Viewport size.
    // This option will exclude any divs and render the SSP App
    // to the body of element of the page.
    // If renderSSPFullScreen is set to false
    // then the sspParentDivId will allow you to set a div
    // in which to render the SSP App.
    var renderSSPFullScreen = true;
    var sspParentDivId = '${n}ssp';
    // When set to true ssp will turn on internal auditing functionality
    var sspInDevelopMode = ${model.developModeOn};
</script>

<!-- SSP Application -->
<c:choose>
    <c:when test="${model.useMinified}">
        <script type="text/javascript" src="<c:url value="${version}/app-all.js" />"></script>
    </c:when>
    <c:otherwise>
        <script type="text/javascript" src="<c:url value="${version}/app.js" />"></script>
    </c:otherwise>
</c:choose>
<div class="sspOuter">
    <div id="${n}ssp"></div>
</div>
