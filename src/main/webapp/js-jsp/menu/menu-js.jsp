<script>

    // context path changes between local host and AWS
    const contextPath = "<%=request.getContextPath()%>";

    // Display if the user is logged in or logged out
    initMenu();

    // Modal elements
    const loginModal = $("#loginModal");
    const createModal = $("#createModal");
    const emailInput = document.getElementById("email");
    const passwordInput = document.getElementById("password");

    /*
     * Event handlers
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
    .on("click", "#signIn", () => { loginModal.modal("show"); });

    /*
     * Login modal event handlers
     */
    loginModal.on("click", "#loginBtn", () => {

        if (emailInput.value < 1
                || passwordInput.value < 1) {
            showLoginError("Email and password must not be left blank");
            return;
        }

        // Login
        $.ajax({method : "POST"
                , url : contextPath + "/api/login?email=" + emailInput.value + "&password=" + passwordInput.value
                , statusCode : {
                    401 : () => { showLoginError("Email and/or password are incorrect"); }
                }
        }).done(data => {
            window.sessionStorage.setItem("username", data);
            showLoggedIn(data);
            loginModal.modal("hide")
        })}

    ).on("click", "#loginExit", () => { loginModal.modal("hide"); });

    // TODO: clear the error state on modal close
    function showLoginError(msg) {
        const email = $("#email");
        const pass = $("#password");
        const emailDiv = $("#emailDiv");
        const passDiv = $("#passDiv");
        const errorMsg = document.getElementById("loginError");

        emailDiv.addClass("error");
        passDiv.addClass("error");
        $("#loginError > p").text(msg);
        errorMsg.removeAttribute("style");

        email.on("input", () => {
            errorMsg.setAttribute("style", "display: none;");
            emailDiv.removeClass("error");
        });

        pass.on("input", () => {
            errorMsg.setAttribute("style", "display: none;");
            passDiv.removeClass("error");
        });
    }

    /*
     * Create modal event handlers
     */
    createModal.on("click", "#createSubmit", () => {
        const title = $("#title");

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
                            createNoteInputError("Note " + title.val().toString() + " already exists");
                        }
                        // TODO: handle user not signed in
                    });

        } else {
            createNoteInputError("Note must have title");
        }

    })
    .on("click", "#createCancel", () => { closeCreateModal(); })
    .on("click", "#createExit", () => { closeCreateModal(); });

    /*
     * Create menu that displays if the user is logged in or
     * logged out
     */
    function initMenu() {
        const username = sessionStorage.getItem("username");

        if (username == null) {
            showLoggedOut();
        } else {
            showLoggedIn(username);
        }
    }

    function showLoggedOut() {
        const rMenu = $("#rightMenu");
        rMenu.empty();
        rMenu.append("<a class='item' id='signIn'>Sign In</a>"
                + "<a class='item' id='signUp'>Sign Up</a>");
    }

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
     * Show create note input error
     */
    function createNoteInputError(msg) {
        // const title = $("#title");
        // const div = $("#titleDiv");
        // const errorMsg = document.getElementById("titleError");
        //
        // div.addClass("error");
        // $("#titleError > p").text(msg);
        // errorMsg.removeAttribute("style");
        //
        // title.on("input", () => {
        //     errorMsg.setAttribute("style", "display: none;");
        //     div.removeClass("error");
        // });

        const inputs = [{ div : "titleDiv", input : "title" , msg : { name : "titleError", text : msg } }];
        showFormError(inputs);
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
     * Close the create modal and clear any error states
     */
    function closeCreateModal() {
        createModal.modal("hide");
        $("#titleDiv").removeClass("error");
        document.getElementById("titleError").setAttribute("style", "display: none");
    }

</script>