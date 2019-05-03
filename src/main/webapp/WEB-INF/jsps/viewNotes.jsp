<%@include file="../../templates/header.jsp"%>
</head>
<body>

<%@include file="../../templates/menu.jsp"%>

<!-- Table of found notes is hidden until notes are fetched -->
<br>
<div class="ui two column centered grid" id="resultsTable" style="display: none;">
    <div class="column">

        <h2 class="ui center aligned header">All Notes</h2>
        <br>

        <table class="ui celled table" id="noteTable">
            <thead>
            <tr>
                <th></th>
                <th>Name</th>
                <th>Created on</th>
            </tr>
            </thead>
            <tbody id="results">
                <!-- Results -->
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

        <!-- Note delete success message is hidden by default -->
        <div class="ui small message" style="display: none;" id="delMessage">
            <div class="header">Notes Deleted</div>
        </div>

    </div>
</div>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.0/jquery.min.js"></script>
<%@include file="../../js-jsp/viewNotes/viewNotes-js.jsp"%>
<%@include file="../../templates/footer.jsp"%>