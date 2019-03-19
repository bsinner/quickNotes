window.onload = () => {

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
        if (emailInput.innerHTML.length < 1
            || passwordInput.length < 1) {

            // highlight form fields if they are empty

        }
    };

};