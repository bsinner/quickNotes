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
     * Add event handlers to the list of notes.
     */
    function setUpForm() {

        // Delete button
        const btn = $("#delBtn");

        // If at least one checkbox is checked activate the
        // delete button
        $("#results").on("click", ".delCheckbox", () => {

            if ($(".delCheckbox:input:checked").length > 0) {
                btn.attr("class", "negative ui small button");
            } else {
                btn.attr("class", "ui small disabled button");
            }

        });

        // Deletes the selected notes, displays success message,
        // deactivates delete button. Promise.all() is used so
        // the page will wait and display the number of notes
        // deleted instead of 0
        btn.click(() => {
            let delCount = 0;

            var promises = [];

            $(".note").each(function(i) {

                const currElement = $(this);

                if (currElement.find(".delCheckbox:input:checked").length > 0) {

                    promises.push(deleteNote(currElement.attr("data-id")).then(wasDeleted => {

                        if (wasDeleted) {
                            delCount += 1;
                            currElement.transition({
                                animation : "scale"
                                , onComplete : () => { currElement.remove(); }
                            })
                        }

                    }));

                }
            });

            Promise.all(promises)
                .then(() => {
                    btn.attr("class", "ui small disabled button");
                    showMessage(delCount);
                });

        });
    }

    /*
     * Try to delete a note with the passed in id, on success return
     * true, on error return false.
     */
    function deleteNote(id) {

        return fetch("<%=request.getContextPath()%>/api/delete?id=" + id, {
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
     * Show a message displaying how many notes where deleted
     */
    function showMessage(count) {

        const msg = $("<div class='ui small message'>"
                        + "<div class='header'>"
                            + count
                            + (count === 1 ? " Note" : " Notes")
                            + " Deleted"
                        + "</div>"
                    + "</div>");

        $("#resultsColumn").append(msg);

        setTimeout(() => {
            msg.transition({
                animation : "scale"
                , onComplete : () => { msg.remove(); }
            });
        }, 4500);

    }

    /*
     * Display if none are found
     */
    function displayNoneFound() {
        // TODO: create link to create note
    }

</script>