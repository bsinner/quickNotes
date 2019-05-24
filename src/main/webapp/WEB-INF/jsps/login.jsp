<%@include file="../../templates/header.jsp"%>

</head>
<body>

<%@include file="../../templates/staticMenu.jsp"%>

<div class="ui column centered grid">
    <div class="four wide column">
        <br>

        <h4>You Must be Signed in to Continue</h4>
        <br>

        <form class="ui form" id="form">

            <div class="field">
                <label>E-mail</label>
                <input type="text" id="email">
            </div>

            <div class="field">
                <label>Password</label>
                <input type="password" id="password">
            </div>

        </form>
        <br>

        <button class="small ui primary button" id="submit" style="float:right;">Sign In</button>

    </div>
</div>

<%@include file="/js-jsp/staticLogin/staticLogin-js.jsp"%>

</body>
</html>