<script>
    const CXT = "<%=request.getContextPath()%>";
    const Q_STRING = "?${params}";
    const JS_COOKIE = "access_token_data";

    // Elements to hold output
    const S_IMG = document.getElementById("statusImg");
    const S_TITLE = document.getElementById("statusTitle");
    const S_DESC = document.getElementById("statusDesc");
    const S_BTN = document.getElementById("statusBtn");
    const INVALID = ["Invalid Link", "The account activation link is invalid"];
    const EXPIRED = ["Expired Link", "The account activation link has expired"];

    (function init() {
        if (Q_STRING.length > 1) {
            activateAccount();
        } else {
            showFailed(...INVALID);
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
                        showFailed(...EXPIRED);
                    } else if (r.status === 403) {
                        // Show success because 403 indicates user is already activated
                        showSuccess();
                    } else {
                        showFailed(...INVALID);
                    }
                });
    }

    function showSuccess() {
        showStatus("images/success.svg", "Account Activated"
            , "Your account has been activated, you may now save and create notes"
        );

        setButton("Return to Editor", "editor");
    }

    function showFailed(title, desc) {
        showStatus("images/fail.svg", title, desc);

        const cookies = document.cookie.split(";")
            .filter(c => c.trim().startsWith(JS_COOKIE + "="));

        if (cookies.length > 0) {
            setButton("Resend Email", "...")
        } else {
            setButton("Login and Resend", "...");
        }

    }

    function showStatus(img, title, desc) {
        S_IMG.setAttribute("src", img);
        S_TITLE.innerText = title;
        S_DESC.innerText = desc;
    }

    function setButton(text, action) {
        S_BTN.innerText = text;
        S_BTN.setAttribute("href", action);
    }

</script>