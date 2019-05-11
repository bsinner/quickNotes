<script>


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
            method: "POST"
            , url: "<%=request.getContextPath()%>/api/logout"
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
            // highlight form fields if they are empty
            return;
        }

        // Login
        $.ajax({method: "POST"
                , url: "<%=request.getContextPath()%>/api/login?email=" + emailInput.value + "&password=" + passwordInput.value
        }).done(data => {
            window.sessionStorage.setItem("username", data);
            showLoggedIn(data);
            loginModal.modal("hide")
        })}

    ).on("click", "#loginExit", () => { loginModal.modal("hide"); });

    /*
     * Create modal event handlers
     */
    createModal
    .on("click", "#createSubmit", () => {

        const title = $("#title");

        if (title.val().length > 1) {

        } else {
            showCreateNoteInputError(title);
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
                + "<a class='item'>Sign Up</a>");
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
    function showCreateNoteInputError(title) {
        const titleDiv = $("#titleDiv");
        const titleErrMsg = document.getElementById("titleError");

        titleDiv.addClass("error");
        titleErrMsg.removeAttribute("style");

        title.on("input", () => {
            if (title.val().length > 1) {
                titleErrMsg.setAttribute("style", "display: none;");
                titleDiv.removeClass("error");
            }
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