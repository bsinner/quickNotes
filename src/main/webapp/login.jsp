<%@include file="templates/header.jsp"%>
<title>Login</title>

</head>
<body>

<%@include file="templates/staticMenu.jsp"%>

<div class="ui column centered grid">
    <div class="four wide column">
        <br>

        <h4>You Must be Signed in to Continue</h4>
        <br>

        <form class="ui form" id="form">

            <div class="field" id="emailDiv">
                <label>E-mail</label>
                <input type="text" id="email">
            </div>

            <div class="field" id="passDiv">
                <label>Password</label>
                <input type="password" id="password">
                <div class="ui compact small negative message compactPadding" id="loginError" style="display: none;">asdf</div>
            </div>

        </form>
        <br>

        <button class="small ui primary button" id="submit" style="float:right;">Sign In</button>

    </div>
</div>

<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.3.1/jquery.js"></script>
<%@include file="/js-jsp/staticLogin-js.jsp"%>

</body>
</html>