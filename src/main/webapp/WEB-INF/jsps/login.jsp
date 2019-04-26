<%@include file="../../templates/header.jsp"%>

</head>
<body>

<div class="ui huge menu">

    <div class="header item">
        <a href="editor" id="homeLink">Quick Notes</a>
    </div>

    <div class="right menu">
        <a href="editor" class="item">Back to Editor</a>
    </div>

</div>

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