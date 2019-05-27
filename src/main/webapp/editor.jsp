<%@include file="templates/header.jsp"%>
<%--quill css--%>
<link rel="stylesheet" href="css/quill.snow.min.css">
<%--custom css--%>
<link rel="stylesheet" href="css/editor.css">
</head>
<body>
<%@include file="templates/menu.jsp"%>

<div id="editorContainer">
    <div id="editor"></div>
    <br>
    <div>
        <button id="saveBtn" class="ui green button" style="display: none;">Save</button>
        <button id="translateButton" class="ui button">Translate</button>
    </div>

</div>

<script type="text/javascript" src="js/editor/quill.min.js"></script>

<%@include file="templates/footer.jsp"%>
<%@include file="js-jsp/editor-js.jsp"%>