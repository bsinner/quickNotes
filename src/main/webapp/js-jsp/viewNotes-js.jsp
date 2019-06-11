<script>

    const TABLE = document.getElementById("resultsTable");
    const VIEW_CXT = "<%=request.getContextPath()%>";

    // Logout request is called in two places with the same parameters
    const LOGGED_OUT = [() => { location.reload() }
            , () => { window.location = VIEW_CXT + "/editor" }];

    const VIEW_REQS = new QNotesRequests(VIEW_CXT, () => { VIEW_REQS.logout(...LOGGED_OUT) });

    /*
     * Add event handlers for the note elements to be loaded,
     * load the list of notes
     */
    setUpForm();
    loadNotes();

    /*
     * Display found user notes
     */
    function outputData(data) {

        if (Object.keys(data).length === 0) {
            displayNoneFound();
            return;
        }

        const resultsNode = $("#results");
        TABLE.removeAttribute("style");

        Object.keys(data).forEach(key => {

            resultsNode.append("<tr class='note'  data-id='" + key + "'>"
                + "<td class='collapsing'>"
                    + "<div class='ui fitted checkbox'>"
                        + "<input type='checkbox' class='delCheckbox'><label></label>"
                    + "</div>"
                + "</td>"
                + "<td><a href='editor?id=" + key + "'>" + data[key].title + "</a></td>"
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

        // Deletes the selected notes, display success message. Promise.all
        // is used so the page will wait to display the number of notes deleted
        // instead of displaying "0 notes deleted"
        btn.click(() => {
            let delCount = 0;
            let total = document.querySelectorAll(".delCheckbox").length;

            const promises = [];

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
                    if (delCount === total) {
                        TABLE.setAttribute("style", "display: none;");
                        displayNoneFound();
                    }
                    btn.attr("class", "ui small disabled button");
                    showMessage(delCount);
                });

        });
    }

    /*
     * Load a list of notes
     */
    function loadNotes() {
        VIEW_REQS.getAllNotes(res => {
                    res.json().then(data => {
                        outputData(data);
                    });
                }
                , () => {
                    VIEW_REQS.logout(...LOGGED_OUT);
                });
    }

    /*
     * Try to delete a note with the passed in id, on success return
     * true, on error return false. Request object isn't used because
     * a promise needs to be returned for the UI to display a message
     * with how many notes where deleted.
     */
    function deleteNote(id) {
        VIEW_REQS.refresh(() => {}, () => {});

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
     * Create note button event handler
     */
    document.getElementById("viewNotesCreate").onclick = () => {
        createModal.modal("show");
    };

    /*
     * Display if no notes where found
     */
    function displayNoneFound() {
        document.getElementById("notFoundContainer").removeAttribute("style");
    }

</script>