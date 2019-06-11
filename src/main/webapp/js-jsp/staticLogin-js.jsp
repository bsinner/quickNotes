<script src="js/QNotesRequests.js" type="application/javascript"></script>

<script>

        // Inputs
        const EMAIL = document.getElementById("email");
        const PASSWORD = document.getElementById("password");
        
        // Root url, js current user cookie name
        const CXT = "<%=request.getContextPath()%>";
        const JS_COOKIE = "access_token_data";
        const REQUESTS = new QNotesRequests(CXT, () => {}, () => {});

        // Submit event handler
        document.getElementById("submit").onclick = () => {
            if (EMAIL.value.length < 1 || PASSWORD.value.length < 1) {
                showError("Email and password must not be blank");
            }

            REQUESTS.login(EMAIL.value, PASSWORD.value
                    , () => { submit(); }
                    , () => {
                        showError("Email and/or password are incorrect");
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

        /*
         * Show an error state on the form, remove it if one of the
         * inputs is changed
         */
        function showError(msg) {
            const divs =[$("#emailDiv"), $("#passDiv")];
            const msgDiv = document.getElementById("loginError");

            divs.forEach(div => { div.addClass("error"); });
            msgDiv.innerText = msg;
            msgDiv.removeAttribute("style");

            const removeErr = () => {
                msgDiv.setAttribute("style", "display: none;");
                divs.forEach(div => { div.removeClass("error"); });
            };

            EMAIL.oninput = removeErr;
            PASSWORD.oninput = removeErr;
        }

</script>