/*
 * Function for making requests to the API and refreshing expired access
 * tokens in the background.
 */
function QNotesRequests (cxt, onLoggedOut) {

    const POST = { method : "POST", credentials : "same-origin" };
    const DELETE = { method : "DELETE", credentials : "same-origin" };
    const GET = { method : "GET", credentials : "same-origin" };
    const ROOT = cxt + "/api/";
    const JS_COOKIE = "access_token_data";

    // Activate account, query is query string for activate endpoint
    this.activateAcct = (query, complete, fail) => {
        ajaxRequest(ROOT + "activate" + query, POST, complete, fail, 0);
    };

    // Create note
    this.createNote = (complete, fail) => {

    };

    // Delete note by id
    this.deleteNote = (id, complete, fail) => {
        ajaxRequest(ROOT + "delete?id=" + id, DELETE, complete, fail, 0);
    };

    // Get note by id
    this.getNote = (id, complete, fail) => {
        ajaxRequest(ROOT + "note?id=" + id, GET, complete, fail, 0);
    };

    // Get all
    this.getAllNotes = (complete, fail) => {
        ajaxRequest(ROOT + "note/list", GET, complete, fail, 0);
    };

    // Resend activation email
    this.resendActivate = (complete, fail) => {

    };

    // Save note
    this.saveNote = (id, json, complete, fail) => {
        const props = { method : "POST", credentials : "same-origin", headers : { "note-contents" : json } };

        ajaxRequest(ROOT + "saveNote?id=" + id, props, complete, fail, 0);
    };

    // Login request, unlike other requests, complete gets passed request.text() instead of request object
    this.login = (email, pass, complete, fail) => {
        const onComplete = res => {
            res.text().then(t => {
                document.cookie = JS_COOKIE + "=" + t + "; Path=" + cxt;
                complete(t);
            });
        };

        ajaxRequest(ROOT + "login?email=" + email + "&password=" + pass, POST, onComplete, fail, 0);
    };

    // Logout, destroy cookies
    this.logout = (complete, fail) => {
        ajaxRequest(ROOT + "logout", POST, complete, fail, 0);
    };

    // Refresh activation
    this.refresh = (complete, fail) => {
        ajaxRequest(ROOT + "refresh", POST, complete, fail, 0);
    };

    // Translate, json is format accepted by Microsoft translate api
    this.translate = (json, source, dest, complete, fail) => {
        const props = { body : json, method : "POST" };

        ajaxRequest(ROOT + "translate?from=" + source + "&to=" + dest, props, complete, fail, 0);
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