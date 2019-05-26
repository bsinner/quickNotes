<script>
    const CXT = "<%=request.getContextPath()%>";
    const Q_STRING = "?${params}";

    (function init() {
        if (Q_STRING.length > 1) {
            activateAccount();
        }
    })();

    function activateAccount() {
        const url = CXT + "/api/activate" + Q_STRING;
        const props = { method : "POST" };

        fetch(url, props)
                .then(r => {
                    if (r.ok) {
                        showSuccess();
                    } else if (r.status === 410) {
                        showFailed("Token is expired")
                    } else {
                        showFailed("Invalid token");
                    }
                });
    }

    function showSuccess() {

    }

    function showFailed(msg) {

    }

</script>