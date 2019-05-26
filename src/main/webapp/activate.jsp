<%@include file="templates/header.jsp"%>
<link rel="stylesheet" href="css/activate/activate.css">

</head>
<body>

<%@include file="templates/staticMenu.jsp"%>


<div class="ui column centered grid">
    <div class="four wide column">
        <br>
        <h3>${params}</h3>
        <div id="actImgContainer">
            <img id="actImg" src="<c:url value='images/success.svg'/>">
        </div>
    </div>
</div>

<%@include file="js-jsp/activate-js.jsp"%>

<%--<div>Icons made by <a href="https://www.flaticon.com/authors/smashicons" title="Smashicons">Smashicons</a> from <a href="https://www.flaticon.com/" 			    title="Flaticon">www.flaticon.com</a> is licensed by <a href="http://creativecommons.org/licenses/by/3.0/" title="Creative Commons BY 3.0" target="_blank">CC 3.0 BY</a></div>--%>

</body>
</html>