<%@include file="templates/header.jsp"%>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/quill/1.3.6/quill.snow.min.css">
<link rel="stylesheet" href="css/editor.css">
<title>Quick Notes</title>

</head>
<body>
<%@include file="templates/menu.jsp"%>

<div id="editorContainer">
    <div id="editor"></div>
    <br>
    <div>
        <button id="saveBtn" class="ui small green button" style="display: none;">Save</button>
        <button id="translateButton" class="ui small button">Translate</button>
    </div>

</div>

<script src="https://cdnjs.cloudflare.com/ajax/libs/quill/1.3.6/quill.min.js" type="text/javascript"></script>

<%@include file="templates/footerImports.jsp"%>
<script src="js/editor.js"></script>

</body>
</html>