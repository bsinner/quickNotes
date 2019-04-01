<script>

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

    alert("<%=request.getContextPath()%>/api/login?email=" + emailInput.value + "&password=" + passwordInput.value);
        $.ajax({
            method: "POST"
            , url: "<%=request.getContextPath()%>/api/login?email=" + emailInput.value + "&password=" + passwordInput.value
        }).done(data => alert(data));
    };

</script>