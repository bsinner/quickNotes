<%@include file="templates/header.jsp"%>
<title>Resend Email</title>
<link rel="stylesheet" href="dist/css/resend.min.css">

</head>
<body>

<%@include file="templates/staticMenu.jsp"%>

<div class="ui column centered grid">
    <div class="four wide column" id="contents">
        <br>

        <h4>An email to activate your account has been sent</h4>
        <br>

        <p>If you did not receive an email click here to resend</p>
        <br>
        <button class="small ui primary button" id="resend">Resend Email</button>

    </div>
</div>

<script src="dist/js/QNotesRequests.min.js"></script>
<script src="dist/js/resend.min.js"></script>

</body>
</html>