<script src="js/loginRequest.js" type="application/javascript"></script>
<script src="js/QNotesRequests.js" type="application/javascript"></script>

<script>
// TODO: use objects to store data for all forms

    // context path changes between local host and AWS
    const CXT_PATH = "<%=request.getContextPath()%>";
    const JS_COOKIE = "access_token_data";

    // Modal elements
    const loginModal = $("#loginModal");
    const createModal = $("#createModal");
    const signUpModal = $("#signUpModal");
    const confirmModal = $("#regConfirmModal");

    // Requests function
    const REQUESTS = new QNotesRequests(CXT_PATH, () => { loginModal.modal("show") });

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

        toggleBtn("loginBtn", false);

        REQUESTS.login(emailInput.value, passwordInput.value, text => {

            // Login success
            toggleBtn("loginBtn", true);
            showLoggedIn(text);
            loginClose();
        }, () => {

            // Login failure
            toggleBtn("loginBtn", true);
            showLoginError("Email and/or password are incorrect");
        });

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
        const titleText = title.val();

        if (titleText.length > 0) {

            REQUESTS.createNote(titleText, res => {

                // Create note success
                res.text().then(t => {
                    window.location = CXT_PATH + "/editor?id=" + t;
                });

            }, err => {

                // Create note failure
                if (err.status === 422) {
                    showCreateInputError("Note " + titleText + " already exists");
                } else {
                    console.error("Error " + err.status + ": could not create note");
                }

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
     * Register a new user
     */
    function createUser(email, user, pass) {
        const btn = "signUpBtn";

        toggleBtn(btn, false);
        
        REQUESTS.createUser(email, user, pass, () => {

            // Create user success
            toggleBtn(btn, true);

            REQUESTS.login(email, pass
                , () => { showLoggedIn(user); }
                , e => { console.error("Error " + e.status + ": could not log user in") });

            updateActivateModal(emailData.newUser.title, emailData.newUser.msg);
            confirmModal.modal("show");

        }, res => {

            // Create user failure
            toggleBtn(btn, true);

            res.json().then(data => {
               if ("error" in data) {

                   if (data["error"]["code"] === "422001") {
                       signUpData.email.elems.msg.text = "Email already signed up";
                       showFormError([signUpData.email.elems]);
                   } else if (data["error"]["code"] === "422002") {
                       signUpData.uname.elems.msg.text = "Username taken";
                       showFormError([signUpData.uname.elems]);
                   }
               }
            });

        });
    }

    // Confirm modal resend email event handler
    document.getElementById("resendActivation").onclick = () => {
        const url = CXT_PATH + "/api/register/resend";
        const props = { method : "PUT", credentials : "same-origin" };

        const id = "resendActivation";
        toggleBtn(id, false);

        fetch(url, props)
            .then(res => {
               toggleBtn(id, true);

               if(res.status === 400) {
                   window.location.href = CXT_PATH + "/resend";
               }
            });
    };

    /*
     * Account confirmation exit button handler
     */
    document.getElementById("exitRegConfirm").onclick = () => { confirmClose(); };

    /*
     * Hide the confirm account modal
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
        const btn = $("#" + id);
        if (enabled) {
            btn.removeClass("disabled");
            btn.removeClass("loading");
        } else {
            btn.addClass("disabled loading");
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
        inputs.forEach(i => {
            const div = $("#" + i.div);
            const msg = document.getElementById(i.msg.name);

            div.removeClass("error");
            msg.setAttribute("style","display: none;");
        });
    }

    function updateActivateModal(title, msg) {
        document.getElementById("actTitle").innerText = title;
        document.getElementById("actMsg").innerText = msg; // TODO: finish showing please activate msgs
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

    const emailData = {
        newUser : {
            title : "Check your Inbox"
            , msg : "Your account has been created and an email containing an activation link has been sent to your inbox, if you did not receive an email click here to resend."
        }
        , error : {
            title : "Please Activate Account"
            , msg : "Your account must be activated to continue, to activate check your email for an activation link, or click here if one was not sent."
        }
    }

</script>