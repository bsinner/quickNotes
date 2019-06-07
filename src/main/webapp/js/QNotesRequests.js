function QNotesRequests (cxt, onLoggedOut) {

    const post = { method : "POST", credentials : "same-origin" };
    const root = cxt + "/api/";

    this.activateAcct = (query, complete, fail) => {
        ajaxRequest(root + "activate" + query, post, complete, fail);
    };

    this.createNote = (complete, fail) => {};
    this.deleteNote = (complete, fail) => {};
    this.getNote = (complete, fail) => {};
    this.resendActivate = (complete, fail) => {};
    this.saveNote = (complete, fail) => {};
    this.login = (complete, fail) => {};

    /*
     * Make an ajax request, if the user access token is invalid
     * try to get a new one.
     */
    function ajaxRequest(url, props, onComplete, onFail) {

        fetch(url, props)
            .then(r => {
                if (!r.ok) {

                    const ctType = r.headers.get("content-type");

                    // Only call .json if the content type is json
                    if (ctType && ctType.includes("application/json")) {

                        r.json().then(json => {
                            if ("authFilterError" in json) {

                                // Pass current arguments to refresh so it can re call this
                                // function on success
                                refreshAccess(json["authFilterError"]["code"], arguments);

                            } else {
                                onFail(r);
                            }
                        });

                    } else {
                        onFail(r);
                    }

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
        if (err === "401001" || err === "401003") {
            ajaxRequest(
                root + "refresh"
                , post
                , () => { ajaxRequest(...onCompleteArgs); }
                , () => { onLoggedOut(); }
            );
        }
    }


}