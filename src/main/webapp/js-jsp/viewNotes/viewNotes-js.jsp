<script>


    /*
     * Button and checkbox event handlers
     */
    $("input[type=checkbox]").bind("click", () => {
        const btn = $("#delBtn");

        if ($("input:checkbox:checked").length > 0) {
            btn.attr("class", "ui small button");
        } else {
            btn.attr("class", "ui small disabled button");
        }
    });

    /*
     * Fetch user notes when the page loads
     */
    fetch("<%=request.getContextPath()%>/api/note/list", { credentials: "same-origin" })
            .then((res) => res.json())
            .then((data) => outputData(data));

    /*
     * Display found user notes
     */
    const outputData = data => {

        if (Object.keys(data).length === 0) {
            displayNoneFound();
            return;
        }

        const resultsNode = $("#results");
        document.getElementById("resultsTable").removeAttribute("style");

        Object.keys(data).forEach(key => {

            resultsNode.append("<tr>"
                + "<td class='collasing'>"
                    + "<div class='ui fitted checkbox'>"
                        + "<input type='checkbox'><label></label>"
                    + "</div>"
                + "</td>"
                + "<td><a href='editor?note=" + key + "'>" + data[key].title + "</a></td>"
                + "<td>" + data[key].created + "</td>"
            + "</tr>");

        });

    };

    /*
     * Display if none are found
     */
    const displayNoneFound = () => {
        alert("no notes found");
    }

</script>