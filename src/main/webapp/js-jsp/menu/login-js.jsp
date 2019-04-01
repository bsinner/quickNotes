<script>

    initMenu();

    const loginModal = $("#loginModal");
    const emailInput = document.getElementById("email");
    const passwordInput = document.getElementById("password");

    document.getElementById("signIn").onclick = () => {
        loginModal.modal("show");
    };

    document.getElementById("loginExit").onclick = () => {
        loginModal.modal("hide");
    };

    document.getElementById("loginBtn").onclick = () => {
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
    };

    function initMenu() {
        const username = sessionStorage.getItem("username");

        if (username == null) {
            showNotLoggedIn();
        } else {
            showLoggedIn(username);
        }
    }

    function showNotLoggedIn() {
        const rMenu = $("#rightMenu")

        rMenu.empty();
        rMenu.append("<a href=\"#\" class=\"item\" id=\"signIn\">Sign In</a>");
        rMenu.append("<a href=\"#\" class=\"item\">Sign Up</a>");
    }

    function showLoggedIn(username) {
        const rMenu = $("#rightMenu")

        rMenu.empty();
        rMenu.append("<a class=\"item\">Signed in As " + username + "</a>");
        rMenu.append("<a href=\"#\" class=\"item\">Sign Out</a>");

    }

</script>