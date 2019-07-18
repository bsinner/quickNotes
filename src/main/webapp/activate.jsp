<%@include file="templates/header.jsp"%>
<link rel="stylesheet" href="css/activate.css">
<link rel="stylesheet" href="css/icons/loadingSpinner.css">

<title>Activate Account</title>

</head>
<body>

<input id="params" type="hidden" value="${params}">

<div id="bodyDiv">

    <!-- Menu bar and page content -->
    <div id="contentDiv">
        <%@include file="templates/staticMenu.jsp"%>

        <div class="ui column centered grid">
            <div class="six wide column">

                <!-- Success or fail icon -->
                <br>
                <div id="actImgContainer"><img id="statusImg"></div>

                <!-- Description -->
                <h3 class="ui center aligned header" id="statusTitle"></h3>
                <div class="ui center aligned sub header" id="statusDesc"></div>

                <!-- CSS only loading symbol, empty divs are needed for it to display properly -->
                <div id="loader">
                    <div class="lds-ring">
                        <div></div>
                        <div></div>
                        <div></div>
                    </div>
                    <p>Activating Account</p>
                </div>

                <a id="statusBtn" class="ui primary button">Return to Editor</a>
            </div>
        </div>
    </div>

    <!-- Footer to credit icon source -->
    <footer id="footerDiv">

        <p>
            Icons made by <a href="https://www.flaticon.com/authors/smashicons" title="Smashicons" target="_blank">Smashicons </a>
            from <a href="https://www.flaticon.com/" title="Flaticon" target="_blank">www.flaticon.com </a>
            are licensed by <a href="http://creativecommons.org/licenses/by/3.0/" title="Creative Commons BY 3.0" target="_blank">CC 3.0</a>
        </p>

    </footer>

</div>

<%@include file="js-jsp/activate-js.jsp"%>

</body>
</html>