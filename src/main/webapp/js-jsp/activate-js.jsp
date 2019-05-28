<script>
    const CXT = "<%=request.getContextPath()%>";
    const Q_STRING = "?${params}";
    const JS_COOKIE = "access_token_data";

    // Elements to hold output
    const S_IMG = document.getElementById("statusImg");
    const S_TITLE = document.getElementById("statusTitle");
    const S_DESC = document.getElementById("statusDesc");

    (function init() {
        if (Q_STRING.length > 1) {
            activateAccount();
        }
    })();

    function activateAccount() {
        const url = CXT + "/api/activate" + Q_STRING;
        const props = { method : "POST" };
        const invalid = ["Invalid Link", "The account activation link is invalid"];
        const expired = ["Expired Link", "The account activation link has expired"];

        fetch(url, props)
                .then(r => {
                    if (r.ok) {
                        showSuccess();
                    } else if (r.status === 410) {
                        showFailed(...expired);
                    } else if (r.status === 403) {
                        // Show success because 403 indicates user is already activated
                        showSuccess();
                    } else {
                        showFailed(...invalid);
                    }
                });
    }

    function showSuccess() {
        showStatus("images/success.svg", "Account Activated"
            , "Your account has been activated, you may now save and create notes"
        );
    }

    function showFailed(title, desc) {
        showStatus("images/fail.svg", title, desc);

        const cookies = document.cookie.split(";")
            .filter(c => c.trim().startsWith(JS_COOKIE + "="));


    }

    function showStatus(img, title, desc) {
        S_IMG.setAttribute("src", img);
        S_TITLE.innerText = title;
        S_DESC.innerText = desc;
    }

</script>