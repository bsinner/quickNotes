<%@include file="templates/header.jsp"%>
</head>
<body>

<%@include file="templates/menu.jsp"%>

<br>
<div class="ui two column centered grid">
    <div class="column">
        <table class="ui celled table" id="noteTable">
            <thead>
            <tr>
                <th></th>
                <th>Name</th>
                <th>Created on</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="note" items="${notes}">
                <tr>
                    <td class="collapsing">
                        <div class="ui fitted checkbox">
                            <input type="checkbox"><label></label>
                        </div>
                    </td>
                    <td><a href="#">${note.title}</a></td>
                    <td>${note.creationDate}</td>
                </tr>
            </c:forEach>
            </tbody>
            <tfoot class="full-width">
            <tr>
                <th></th>
                <th colspan="2">
                    <button id="delBtn" class="ui small disabled button">Delete</button>
                </th>
            </tr>
            </tfoot>
        </table>
    </div>
</div>

<script type="text/javascript" src="js/jquery-3.3.1.min.js"></script>
<script type="text/javascript" src="js/viewNotes/viewNotes.js"></script>
<%@include file="templates/footer.jsp"%>