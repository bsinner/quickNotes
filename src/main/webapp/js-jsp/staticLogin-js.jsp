<script>

    window.onload = () => {

        // Inputs
        const EMAIL = document.getElementById("email");
        const PASSWORD = document.getElementById("password");
        
        // Root Url
        const CXT = "<%=request.getContextPath()%>";

        // Submit event handler
        document.getElementById("submit").onclick = () => {
            fetch(CXT + "/api/login"
                    + "?email=" + EMAIL.value
                    + "&password=" + PASSWORD.value,
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
        function submit() {
            const form = document.getElementById("form");
            form.setAttribute("action", CXT + "${servlet}");
            form.submit();
        }

        function showError() {
            // TODO: add error handleing
        }
    };

</script>