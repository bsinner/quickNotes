<script>

    // context path changes between local host and AWS
    const contextPath = "<%=request.getContextPath()%>";

    // Modal elements
    const loginModal = $("#loginModal");
    const createModal = $("#createModal");
    const signUpModal = $("#signUpModal");

    // Display if the user is logged in or logged out
    initMenu();

    /*
     * Logout, create, and sign button event handlers
     */
    $("#rightMenu").on("click", "#logout", () => {
        $.ajax({
            method : "POST"
            , url : contextPath + "/api/logout"
        }).done(() => {
            window.sessionStorage.removeItem("username");
            showLoggedOut();
        });

    })
    .on("click", "#create", () => { createModal.modal("show"); })
    .on("click", "#signIn", () => { loginModal.modal("show"); })
    .on("click", "#signUp", () => { signUpModal.modal("show"); });

    /*
     * Login modal event handlers
     */
    loginModal.on("click", "#loginBtn", () => {
        const emailInput = document.getElementById("email");
        const passwordInput = document.getElementById("password");

        // If the login info is missing show an error
        if (emailInput.value < 1
                || passwordInput.value < 1) {
            showLoginError("Email and password must not be left blank");
            return;
        }

        // Login, or show error if invalid credentials
        $.ajax({method : "POST"
                , url : contextPath + "/api/login?email=" + emailInput.value + "&password=" + passwordInput.value
                , statusCode : {
                    401 : () => { showLoginError("Email and/or password are incorrect"); }
                }
        }).done(data => {
            window.sessionStorage.setItem("username", data);
            showLoggedIn(data);
            loginClose();
        })}

    ).on("click", "#loginExit", () => { loginClose(); });

    // Show form error state on login fail
    function showLoginError(msg) {
        const errMsg = { name : "loginError", text : msg };
        showFormError([
            { div : "emailDiv", input : "email", msg : errMsg }
            , { div : "passDiv", input : "password", msg : errMsg }
        ]);
    }

    // Close the login modal and clear it's error state
    function loginClose() {
        loginModal.modal("hide");
        clearFormErrState([
            { div : "emailDiv", msg : { name : "loginError" } }
            , { div : "passDiv", msg : { name : "loginError" } }
        ]);
    }

    /*
     * Create modal event handlers
     */
    createModal.on("click", "#createSubmit", () => {
        const title = $("#title");

        // Create the note and close the modal, or show an error state if no note
        // title is present, or if a note with the same name exists
        if (title.val().length > 1) {
            const url = contextPath + "/api/createNote?title=" + title.val();
            const props = { credentials : "same-origin", method : "PUT" };

            fetch(url, props)
                    .then(res => {
                        if (res.ok) {
                            res.text().then(t => {
                                window.location = contextPath + "/editor?id=" + t;
                            });
                        } else if (res.status === 422) {
                            showCreateInputError("Note " + title.val().toString() + " already exists");
                        }
                        // TODO: handle user not signed in
                    });

        } else {
            showCreateInputError("Note must have title");
        }

    })
    .on("click", "#createCancel", () => { createClose(); })
    .on("click", "#createExit", () => { createClose(); });


    /*
    * Show error state on create note modal
    */
    function showCreateInputError(msg) {
        showFormError([{ div : "titleDiv", input : "title"
            , msg : { name : "titleError", text : msg } }]
        );
    }

    /*
    * Close the create modal and clear any error states
    */
    function createClose() {
        createModal.modal("hide");
        clearFormErrState([{ div : "titleDiv", msg : { name : "titleError" } }]);
    }

    /*
     * Sign up event handlers
     */
    //...

    /*
     * Show if the user is logged in, initialize modals
     */
    function initMenu() {
        const username = sessionStorage.getItem("username");
        loginModal.modal({ onHidden : () => { loginClose(); } });
        createModal.modal({ onHidden : () => { createClose(); } })

        if (username == null) {
            showLoggedOut();
        } else {
            showLoggedIn(username);
        }
    }

    /*
     * Update the menu to show that no user is logged in
     */
    function showLoggedOut() {
        const rMenu = $("#rightMenu");
        rMenu.empty();
        rMenu.append("<a class='item' id='signIn'>Sign In</a>"
                + "<a class='item' id='signUp'>Sign Up</a>");
    }

    /*
     * Update the menu to show that a user is logged in
     */
    function showLoggedIn(username) {
        const rMenu = $("#rightMenu");
        rMenu.empty();
        rMenu.append("<a class='item'>Signed in As " + username + "</a>"
                + "<a class='item' id='logout'>Sign Out</a>"
                + "<a href='viewNotes' class='item'>View Saved Notes</a>"
                + "<a class='item' id='create'>Create</a>"
        );
    }

    /*
     * Generic show form error state method.
     * Input must be an array in the following format:
     * [
     *     { div : "div id", in : "input id"
     *         , msg { name : "err msg id", text : "error msg text to show" }
     *     }
     * ]
     */
    function showFormError(inputs) {
        inputs.forEach(i => {
            const div = $("#" + i.div);
            const input = $("#" + i.input);
            const msg = document.getElementById(i.msg.name);

            div.addClass("error");

            msg.innerText = i.msg.text;
            msg.removeAttribute("style");

            input.on("input", () => {
               msg.setAttribute("style", "display: none;");
               div.removeClass("error");
            });
        });
    }

    /*
     * Generic clear form error state method, input format from showFormError
     * can be used, or the following shortened format can be used:
     * [
     *     { div : "input div id", msg : { name : "err msg id" } }
     * ]
     */
    function clearFormErrState(inputs) {
        // TODO: clear input text as part of clearing the form
        inputs.forEach(i => {
            const div = $("#" + i.div);
            const msg = document.getElementById(i.msg.name);

            div.removeClass("error");
            msg.setAttribute("style","display: none;");
        });
    }



</script>