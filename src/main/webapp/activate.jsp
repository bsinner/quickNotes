<%@include file="templates/header.jsp"%>
<link rel="stylesheet" href="css/activate.css">

</head>
<body>

<div id="bodyDiv">

    <!-- Menu bar and page content -->
    <div id="contentDiv">
        <%@include file="templates/staticMenu.jsp"%>

        <div class="ui column centered grid">
            <div class="six wide column">
                <br>
                <div id="actImgContainer">
                    <img id="actImg">
                </div>
                <h3 class="ui center aligned header"></h3>
                <div class="sHeader ui center aligned sub header"></div>
            </div>
        </div>
    </div>

    <!-- Footer to credit icon source -->
    <footer id="footerDiv">
        <div class="ui section divider"></div>

        <div class="ui horizontal small divided link list" id="activateFooter">
            <div class="centered item" id="footerMsg">
                Icons made by <a href="https://www.flaticon.com/authors/smashicons" title="Smashicons" target="_blank">Smashicons </a>
                from <a href="https://www.flaticon.com/" title="Flaticon" target="_blank">www.flaticon.com </a>
                are licensed by <a href="http://creativecommons.org/licenses/by/3.0/" title="Creative Commons BY 3.0" target="_blank">CC 3.0</a>
            </div>
        </div>
    </footer>

</div>

<%@include file="js-jsp/activate-js.jsp"%>

</body>
</html>