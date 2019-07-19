<%@include file="templates/header.jsp"%>
<title>View Notes</title>
<link rel="stylesheet" href="dist/css/viewNotes.min.css">

</head>
<body>

<%@include file="templates/menu.jsp"%>

<!-- Hidden table of found notes -->
<br>
<div class="ui two column centered grid">
    <div class="column" id="resultsColumn">

        <div id="resultsTable" style="display: none;">
            <h2 class="ui center aligned header">All Notes</h2>
            <br>

            <table class="ui celled table" id="noteTable">
                <thead>
                <tr>
                    <th></th>
                    <th>Name</th>
                    <th>Created</th>
                </tr>
                </thead>

                <!-- Output user notes here -->
                <tbody id="results">
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

        <!-- Hidden No Notes Found message -->
        <div id="notFoundContainer" style="display: none;">
            <h2>No Notes Found</h2>
            <br>
            <button id="viewNotesCreate" class="ui small primary button">Create new Note</button>
        </div>

        <!-- Note delete success message is hidden by default -->
        <div class="ui small message" style="display: none;" id="delMessage">
            <div class="header">Notes Deleted</div>
        </div>

    </div>
</div>

<%@include file="templates/footerImports.jsp"%>
<script src="dist/js/viewNotes.min.js"></script>

</body>
</html>