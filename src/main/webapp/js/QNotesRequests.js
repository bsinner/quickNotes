function QNotesRequests (cxt, onLoggedOut) {

    this.activateAcct = (query, complete, fail) => {
        ajaxRequest(cxt + "/api/activate" + query, { method : "POST", credentials : "same-origin" }
            , complete, fail
        );
    };

    this.createNote = (complete, fail) => {};
    this.deleteNote = (complete, fail) => {};
    this.getNote = (complete, fail) => {};
    this.resendActivate = (complete, fail) => {};
    this.saveNote = (complete, fail) => {};
    this.login = (complete, fail) => {};

    function ajaxRequest(url, props, onComplete, onFail) {
        fetch(url, props)
            .then(r => {
               if (!r.ok) {
                   // if 401 check error object
                   onFail(r);
               } else {
                   onComplete(r);
               }
            });
    }

}