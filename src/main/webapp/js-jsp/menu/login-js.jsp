<script>

    // Display the users's username if they are logged in
    // otherwise display a sign in and sign up button
    initMenu();

    // Popup element and its inputs
    const loginModal = $("#loginModal");
    const emailInput = document.getElementById("email");
    const passwordInput = document.getElementById("password");

    // Event handlers
    document.getElementById("signIn").onclick = () => {
        loginModal.modal("show");
    };

    document.getElementById("loginExit").onclick = () => {
        loginModal.modal("hide");
    };

    $("body").on("click", "#logout", () => {
        $.ajax({
            method: "POST"
            , url: "<%=request.getContextPath()%>/api/logout"
        }).done(() => {
            window.sessionStorage.removeItem("username");
            showLoggedOut();
        });

    }).on("click", "#loginBtn", () => {
        if (emailInput.value < 1
                || passwordInput.value < 1) {
            // highlight form fields if they are empty
            return;
        }
        $.ajax({
            method: "POST"
                , url: "<%=request.getContextPath()%>/api/login?email=" + emailInput.value + "&password=" + passwordInput.value
            }).done(data => {
                window.sessionStorage.setItem("username", data);
                showLoggedIn(data);
                loginModal.modal("hide");
        });

    }).on("click", "#signIn", () => {
        loginModal.modal("show");

    });

    // DHTML Menu bar functions
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
        rMenu.append("<a href=\"#\" class=\"item\" id=\"signIn\">Sign In</a>");
        rMenu.append("<a href=\"#\" class=\"item\">Sign Up</a>");
    }

    function showLoggedIn(username) {
        const rMenu = $("#rightMenu");

        rMenu.empty();
        rMenu.append("<a class=\"item\">Signed in As " + username + "</a>");
        rMenu.append("<a href=\"#\" class=\"item\" id=\"logout\">Sign Out</a>");
    }

</script>