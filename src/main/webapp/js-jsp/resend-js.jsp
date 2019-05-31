<script>
    const JS_COOKIE = "access_token_data";
    const CXT = "<%=request.getContextPath()%>";

    sendEmail();

    // Event handler for resend button
    document.getElementById("resend").onclick = () => {
       sendEmail();
    };

    /*
     * Try to send an activation email, if no access token cookie is
     * present status 400 is returned and the user is redirected to the
     * login page.
     */
    function sendEmail() {
        const url = CXT + "/api/register/resend";
        const props = { method : "PUT",  credentials : "same-origin"};

        fetch(url, props)
            .then(res => {
               if(res.status === 400) {
                   window.location = CXT + "/resend";
               }
            });
    }

</script>
