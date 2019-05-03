<script>

    /*
     * Add event handlers to note list
     */
     setUpForm();

    /*
     * Fetch user notes when the page loads
     */
    fetch("<%=request.getContextPath()%>/api/note/list", { credentials: "same-origin" })
            .then((res) => res.json())
            .then((data) => outputData(data));

    /*
     * Display found user notes
     */
    function outputData(data) {

        if (Object.keys(data).length === 0) {
            displayNoneFound();
            return;
        }

        const resultsNode = $("#results");
        document.getElementById("resultsTable").removeAttribute("style");

        Object.keys(data).forEach(key => {

            resultsNode.append("<tr class='note'  data-id='" + key + "'>"
                + "<td class='collasing'>"
                    + "<div class='ui fitted checkbox'>"
                        + "<input type='checkbox' class='delCheckbox'><label></label>"
                    + "</div>"
                + "</td>"
                + "<td><a href='editor?note=" + key + "'>" + data[key].title + "</a></td>"
                + "<td>" + data[key].created + "</td>"
            + "</tr>");

        });

    }

    /*
     * Add event handlers to form.
     */
    function setUpForm() {
        const btn = $("#delBtn");

        $("#results").on("click", ".delCheckbox", () => {


            if ($(".delCheckbox:input:checked").length > 0) {
                btn.attr("class", "negative ui small button");
            } else {
                btn.attr("class", "ui small disabled button");
            }

        });

        btn.click(() => {
            let delCount = 0;

            $(".note").each(function(i) {

                const currElement = $(this);

                if (currElement.find(".delCheckbox:input:checked").length > 0) {
                    deleteNote(currElement.attr("data-id")).then(deleted => {

                        if (deleted) {
                            delCount++;
                            currElement.remove();
                        }

                    });
                }
            });

            btn.attr("class", "ui small disabled button");

        });
    }

    async function deleteNote(id) {

        return await fetch("<%=request.getContextPath()%>/api/delete?id=" + id, {
                    method: "DELETE", credentials: "same-origin"
            })
            .then(res => {
                if (!res.ok) {
                    console.error("Error deleting note: " + res.status);
                    return false;
                }
                return true;
            });
    }

    /*
     * Display if none are found
     */
    function displayNoneFound() {
        // TODO: create link to create note
    }

</script>