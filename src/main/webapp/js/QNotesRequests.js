/*
 * Function for making requests to the API and refreshing expired access tokens in the background. Params c and f
 * in wrapper functions stand for onComplete and onFail.
 */
function QNotesRequests (cxt, onLoggedOut) {

    const ROOT = cxt + "/api/";
    const JS_COOKIE = "access_token_data";

    const POST = { method : "POST", credentials : "same-origin" };
    const DELETE = { method : "DELETE", credentials : "same-origin" };
    const GET = { method : "GET", credentials : "same-origin" };
    const PUT = { method : "PUT",  credentials : "same-origin" };

    // Activate account, query is query string for activate endpoint
    this.activateAcct = (query, c, f) => { ajaxRequest(ROOT + "activate" + query, POST, c, f, 0); };

    // Create note
    this.createNote = (title, c, f) => { ajaxRequest(ROOT + "createNote?title=" + title, PUT, c, f, 0); };

    // Delete note
    this.deleteNote = (id, c, f) => { ajaxRequest(ROOT + "delete?id=" + id, DELETE, c, f, 0); };

    // Get note
    this.getNote = (id, c, f) => { ajaxRequest(ROOT + "note?id=" + id, GET, c, f, 0); };

    // Get all
    this.getAllNotes = (c, f) => { ajaxRequest(ROOT + "note/list", GET, c, f, 0); };

    // Resend activation email
    this.resendActivate = (c, f) => { ajaxRequest(ROOT + "register/resend", PUT, c, f, 0); };

    // Logout
    this.logout = (c, f) => { ajaxRequest(ROOT + "logout", POST, c, f, 0); };

    // Refresh access token
    this.refresh = (c, f) => { ajaxRequest(ROOT + "refresh", POST, c, f, 0); };

    // Save note
    this.saveNote = (id, json, c, f) => {
        const props = { method : "POST", credentials : "same-origin", headers : { "note-contents" : json } };

        ajaxRequest(ROOT + "saveNote?id=" + id, props, c, f, 0);
    };

    // Create account
    this.createUser = (email, user, pass, c, f) => {
        ajaxRequest("register?user=" + user + "&pass=" + pass + "&email=" + email, PUT, c, f, 0);
    };

    // Login, unlike other functions onComplete param gets passed Response.text() instead of Response
    this.login = (email, pass, c, f) => {
        const onComplete = res => {
            res.text().then(text => {
                document.cookie = JS_COOKIE + "=" + text + "; Path=" + cxt;
                c(text);
            });
        };

        ajaxRequest(ROOT + "login?email=" + email + "&password=" + pass, POST, onComplete, f, 0);
    };

    // Translate, json format is Microsoft translate api format
    this.translate = (json, source, dest, c, f) => {
        const props = { body : json, method : "POST" };

        ajaxRequest(ROOT + "translate?from=" + source + "&to=" + dest, props, c, f, 0);
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