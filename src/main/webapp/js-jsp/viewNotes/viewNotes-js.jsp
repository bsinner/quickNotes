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
    fetch("<%=request.getContextPath()%>/api/note/all", { credentials: "same-origin" })
            .then((res) => res.json())
            .then((data) => outputData(data));

    /*
     * Display found user notes
     */
    const outputData = data => {
        const lng = Object.keys(data).length;

        if (lng === 0) {
            displayNoneFound();
            return;
        }

        data.forEach((item) => alert(item.toString()));
    };

    /*
     * Display if none are found
     */
    const displayNoneFound = () => {
        alert("no notes found");
    }

</script>