<script>


    // Display if the user is logged in or logged out
    initMenu();

    // Modal elements
    const loginModal = $("#loginModal");
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

    }).on("click", "#signIn", () => loginModal.modal("show"));

    /*
     * Event handlers for modal buttons
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

    ).on("click", "#loginExit", () => loginModal.modal("hide") );

    // DHTML Menu bar generation
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
                + "<a href='#' class='item' id='logout'>Sign Out</a>"
                + "<a href='viewNotes' class='item'>View Saved Notes</a>"
                + "<a href='new' class='item'>Create</a>"
        );
    }

</script>