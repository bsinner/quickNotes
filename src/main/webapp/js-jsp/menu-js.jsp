<script src="js/loginRequest.js" type="application/javascript"></script>

<script>
// TODO: use objects to store data for all forms

    // context path changes between local host and AWS
    const CXT_PATH = "<%=request.getContextPath()%>";
    const JS_COOKIE = "access_token_data";
    const LOGIN_REQUEST = new LoginRequest({
        basePath : CXT_PATH
        , onComplete : data => {
            showLoggedIn(data);
            loginClose();
        }
        , onError : () => {
            showLoginError("Email and/or password are incorrect");
        }
    });

    // Modal elements
    const loginModal = $("#loginModal");
    const createModal = $("#createModal");
    const signUpModal = $("#signUpModal");
    const confirmModal = $("#regConfirmModal");

    // Display if the user is logged in or logged out
    initMenu();

    /*
     * Logout, create, and sign button event handlers
     */
    $("#rightMenu").on("click", "#logout", () => {
        $.ajax({
            method : "POST"
            , url : CXT_PATH + "/api/logout"
        }).done(() => {
            document.cookie = JS_COOKIE + "= ; "
                    + "Path=" + CXT_PATH + "; "
                    + "Expires=Thu, 01 Jan 1970 00:00:00 GMT";
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
        if (emailInput.value.length < 1
            || passwordInput.value.length < 1) {
            showLoginError("Email and password must not be left blank");
            return;
        }

        LOGIN_REQUEST.login(emailInput.value, passwordInput.value);

    }).on("click", "#loginExit", () => { loginClose(); });

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
            const url = CXT_PATH + "/api/createNote?title=" + title.val();
            const props = { credentials : "same-origin", method : "PUT" };

            fetch(url, props)
                    .then(res => {
                        if (res.ok) {
                            res.text().then(t => {
                                window.location = CXT_PATH + "/editor?id=" + t;
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
     * Sign up functions
     */
    signUpModal.on("click", "#signUpBtn", () => {
        clearSignUp();
        let error = false;

        // Loop through the inputs, if any are left blank show an error state
        for (let i in signUpData) {
            if (signUpData[i].input.value.length < 1) {
                signUpData[i].elems.msg.text = signUpData[i].title + " must not be left blank";
                showFormError([signUpData[i].elems]);
                error = true;
            }
        }

        // If any of the inputs where found to be blank, exit the function
        if (error) return;

        // If passwords don't match show an error, exit the function
        if (signUpData.pass.input.value !== signUpData.pass2.input.value) {
            signUpData.pass.elems.msg.text = "";
            signUpData.pass2.elems.msg.text = "Passwords do not match";
            showFormError([signUpData.pass.elems, signUpData.pass2.elems]);
            return;
        }

        createUser(signUpData.email.input.value
            , signUpData.uname.input.value
            , signUpData.pass.input.value);
    })
    .on("click", "#signUpCancel", () => { signUpClose(); })
    .on("click", "#signUpExit", () => { signUpClose(); });

    /*
     * Send a request to the endpoint to register and email a new user
     */
    function createUser(email, user, pass) {
        const url = CXT_PATH + "/api/register?user=" + user
                + "&pass=" + pass + "&email=" + email;
        const props = { method : "PUT" };
        const btn = "signUpBtn"

        toggleBtn(btn, false);

        fetch(url, props)
            .then(res => {
                toggleBtn(btn, true);
                if (!res.ok) {
                    return res.json();
                } else {
                    LOGIN_REQUEST.login(email, pass);
                    confirmModal.modal("show");
                }
            }).then(json => {

                // if username or email is taken highlight form field
                if ("err" in json) {
                    if (json["err"] === "email") {
                        signUpData.email.elems.msg.text = "Email already signed up";
                        showFormError([signUpData.email.elems]);
                    } else if (json["err"] === "username") {
                        signUpData.uname.elems.msg.text = "Username taken";
                        showFormError([signUpData.uname.elems]);
                    }
                }

            });
    }

    // Confirm modal resend email event handler
    document.getElementById("resendActivation").onclick = () => {
        const url = CXT_PATH + "/api/register/resend";
        const props = { method : "PUT", credentials : "same-origin" };

        fetch(url, props)
            .then(res => {
               if(res.status === 400) {
                   window.location.href = CXT_PATH + "/resend";
               }
            });
    };

    // Confirm modal close button event handler
    document.getElementById("exitRegConfirm").onclick = () => { confirmClose(); };

    /*
     * Hide the confirmation modal
     */
    function confirmClose() {
        confirmModal.modal("hide");
    }

    /*
     * Wrapper function for clearing sign up form error state
     */
    function clearSignUp() {
        clearFormErrState([signUpData.email.elems, signUpData.uname.elems
                , signUpData.pass2.elems, signUpData.pass.elems
        ]);
    }

    /*
     * Close the sign up modal and clear any error states
     */
    function signUpClose() {
        signUpModal.modal("hide");
        clearSignUp();
    }

    /*
     * Show if the user is logged in, initialize modals
     */
    function initMenu() {
        const cookieArray = document.cookie.split(";")
                .filter(c => c.trim().startsWith(JS_COOKIE + "="));

        if (cookieArray.length === 0) {
            showLoggedOut();
        } else {
            showLoggedIn(cookieArray[0].split("=")[1]);
        }

        // Initialize modals to call their form's close methods
        // to clear error states when modals are closed by clicking
        // somewhere else on the screen
        loginModal.modal({ onHidden : () => { loginClose(); } });
        createModal.modal({ onHidden : () => { createClose(); } });
        signUpModal.modal({ onHidden : () => { signUpClose(); } });
        confirmModal.modal({ onHidden : () => { confirmClose(); } });
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
     * Generic show form error state method, If an error message's text
     * property is passed an empty string, no message will be shown.
     *
     * Input must be an array of the following format:
     * [
     *     { div : "div id", input : "input id"
     *         , msg : { name : "err msg id", text : "error msg text to show" }
     *     }
     * ]
     */
    function showFormError(inputs) {
        inputs.forEach(i => {
            const div = $("#" + i.div);
            const input = $("#" + i.input);
            const msg = document.getElementById(i.msg.name);

            div.addClass("error");

            if (i.msg.text !== "") {
                msg.innerText = i.msg.text;
                msg.removeAttribute("style");
            }

            input.on("input", () => {
               msg.setAttribute("style", "display: none;");
               div.removeClass("error");
            });
        });
    }


    /*
     * Toggle a button between its regular state, and being disabled and showing a loading icon.
     */
    function toggleBtn(id, enabled) {
        const btn = document.getElementById(id);
        const classes = btn.getAttribute("class");

        if (enabled) {
            btn.setAttribute("class", classes.replace(/(disabled)|(loading)/gi, ""));
        } else {
            btn.setAttribute("class", classes + "disabled loading");
        }
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

    /*
     * Sign up form data, input properties are used for getting values of inputs,
     * elems properties are sent to error state displaying functions, title is needed
     * for displaying form error messages
     */
    const signUpData = {
        email : {
            input : document.getElementById("signUpEmail")
            , elems : { div : "signUpEmailDiv", input : "signUpEmail"
                    , msg : { name : "signUpEmailErr", text : "" }
            }
            , title : "Email"
        },
        uname : {
            input : document.getElementById("signUpUName")
            , elems : { div : "signUpUNameDiv", input : "signUpUName"
                    , msg : { name : "signUpUNameErr", text : "" }
            }
            , title : "Username"
        },
        pass : {
            input : document.getElementById("signUpPass")
            , elems : { div : "signUpPassDiv", input : "signUpPass"
                    , msg : { name : "signUpPassErr", text : "" }
            }
            , title : "Password"
        },
        pass2 : {
            input : document.getElementById("signUpPass2")
            , elems : { div : "signUpPassDiv2", input : "signUpPass2"
                    , msg : { name : "signUpPass2OrAllErr", text : "" }
            }
            , title : "Password"
        }
    };

</script>