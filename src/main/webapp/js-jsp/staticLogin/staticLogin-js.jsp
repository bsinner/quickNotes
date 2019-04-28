<script>

    window.onload = () => {

        // Inputs
        const email = document.getElementById("email");
        const password = document.getElementById("password");
        
        // Root Url
        const root = "<%=request.getContextPath()%>";

        document.getElementById("submit").onclick = () => {
            fetch(root + "/api/login"
                    + "?email=" + email.value
                    + "&password=" + password.value,
                    { method: "POST" })
            .then((res) => {
                if(res.status === 401) {
                    showError();
                } else {
                    submit()
                }
            });
        };

        /*
         * When a servlet redirects to the static login page
         * the request attribute needs to be set so the submit
         * function can send users to the right page
         */
        const submit = () => {
            const form = document.getElementById("form");
            form.setAttribute("action", root + "${servlet}");
            form.submit();
        };

        const showError = () => {
            // TODO: add error handleing
        };
    };

</script>