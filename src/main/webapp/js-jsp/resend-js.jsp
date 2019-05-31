<script>
    const JS_COOKIE = "access_token_data";
    const CXT = "<%=request.getContextPath()%>";

    sendEmail();

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
