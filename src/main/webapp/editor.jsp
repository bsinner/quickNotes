<%@include file="templates/header.jsp"%>
<%--quill css--%>
<link rel="stylesheet" href="css/editor/quill.snow.min.css">
<%--custom css--%>
<link rel="stylesheet" href="css/editor/editor.css">
</head>
<body>
<%@include file="templates/menu.jsp"%>

<div id="editorContainer">
    <div id="editor"></div>
    <button id="saveBtn" class="ui green button">Save</button>
</div>

<script type="text/javascript" src="js/editor/quill.min.js"></script>

<%@include file="js-jsp/editor/editor-js.jsp"%>

<%@include file="templates/footer.jsp"%>