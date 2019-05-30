<script src="js/loginRequest.js" type="application/javascript"></script>

<script>

        // Inputs
        const EMAIL = document.getElementById("email");
        const PASSWORD = document.getElementById("password");
        
        // Root url, js current user cookie name
        const CXT = "<%=request.getContextPath()%>";
        const JS_COOKIE = "access_token_data";
        const LOGIN_REQ = new LoginRequest({
            basePath : CXT
            , onComplete : () => { submit(); }
            , onError : () => { showError(); }
        });

        // Submit event handler
        document.getElementById("submit").onclick = () => {
            LOGIN_REQ.login(EMAIL.value, PASSWORD.value);
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


</script>