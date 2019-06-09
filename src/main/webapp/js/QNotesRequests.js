/*
 * Function for making requests to the API and refreshing expired access
 * tokens in the background.
 */
function QNotesRequests (cxt, onLoggedOut) {

    const POST = { method : "POST", credentials : "same-origin" };
    const DELETE = { method : "DELETE", credentials : "same-origin" };
    const GET = { method : "GET", credentials : "same-origin" };
    const ROOT = cxt + "/api/";

    this.activateAcct = (query, complete, fail) => {
        ajaxRequest(ROOT + "activate" + query, POST, complete, fail, 0);
    };

    this.createNote = (complete, fail) => {};

    this.deleteNote = (id, complete, fail) => {
        ajaxRequest(ROOT + "delete?id=" + id, DELETE, complete, fail, 0);
    };

    this.getNote = (complete, fail) => {};

    this.getAllNotes = (complete, fail) => {
        ajaxRequest(ROOT + "note/list", GET, complete, fail, 0);
    };

    this.resendActivate = (complete, fail) => {};
    this.saveNote = (complete, fail) => {};
    this.login = (complete, fail) => {};

    this.logout = (complete, fail) => {
        ajaxRequest(ROOT + "logout", POST, complete, fail, 0);
    };

    this.refresh = (complete, fail) => {
        ajaxRequest(ROOT + "refresh", POST, complete, fail, 0);
    };

    /*
     * Make an ajax request, try to refresh access if the access token is invalid,
     * param loop detects an infinite refresh loop, initialize it to 0 when calling
     */
    function ajaxRequest(url, props, onComplete, onFail, loop) {
        fetch(url, props)
            .then(r => {
                if (!r.ok) {
                    inspectError(r, onFail, loop, arguments);
                } else {
                    onComplete(r);
                }
            });
    }

    /*
     * Refresh the access token, on complete call the endpoint that
     * was blocked, on fail call the logged out callback.
     */
    function refreshAccess(err, onCompleteArgs) {
        onCompleteArgs[onCompleteArgs.length - 1] += 1;

        if (err === "401001" || err === "401003") {
            ajaxRequest(
                ROOT + "refresh"
                , POST
                , () => { ajaxRequest(...onCompleteArgs); }
                , () => { onLoggedOut(); }
            );
        } else if (err === "401002") {
            onLoggedOut();
        }
    }

    /*
     * Refresh the access token, call onLoggedOut, or onFail depending on the error
     */
    function inspectError(res, onFail, loop, args) {
        const type = res.headers.get("content-type");

        if (type && type.includes("application/json")) {

            res.json().then(json => {
                const prop = "authFilterError";

                // If the error object emitted is from Authentication filter is found inspect it
                if (prop in json) {

                    // Refresh the token, or call onLoggedOut if it has been refreshed 4 times and
                    // is still invalid
                    if (loop > 3) {
                        showLoggedOut();
                    } else {
                        refreshAccess(json[prop]["code"], args);
                    }

                // If the error message wasn't from auth filter call onFail
                } else {
                    onFail(res);
                }

            });

            // If the error didn't include JSON call onFail
        } else {
            onFail(res);
        }
    }

}