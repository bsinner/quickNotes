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
        const title = document.getElementById("title").value;

        if (title.length < 1) {
            alert("note created");
        } else {
            // highlight form fields     
        }

    })
    .on("click", "#createCancel", () => { createModal.modal("hide"); })
    .on("click", "#createExit", () => { createModal.modal("hide"); });

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

</script>