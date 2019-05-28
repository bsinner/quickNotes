<script>
    const CXT = "<%=request.getContextPath()%>";
    const Q_STRING = "?${params}";
    const JS_COOKIE = "access_token_data";

    // Elements to hold output
    const S_IMG = document.getElementById("statusImg");
    const S_TITLE = document.getElementById("statusTitle");
    const S_DESC = document.getElementById("statusDesc");
    const S_BTN = document.getElementById("statusBtn");

    // Error messages
    const INVALID = ["Invalid Link", "The account activation link is invalid"];
    const EXPIRED = ["Expired Link", "The account activation link has expired"];

    /*
     * Try to activate the user's account when the page loads.
     */
    (function init() {
        if (Q_STRING.length > 1) {
            activateAccount();
        } else {
            showFailed(...INVALID);
        }
    })();

    /*
     * Try to activate the token in the query string, show
     * message with success or failure.
     */
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

    /*
     * Show activation success and a link back to the editor.
     */
    function showSuccess() {
        showStatus("images/success.svg", "Account Activated"
            , "Your account has been activated, you may now save and create notes"
        );

        setButton("Return to Editor", "editor");
    }

    /*
     * Show failed to activate and link to login and resend activation,
     * or link to resend if the user is already logged in.
     */
    function showFailed(title, desc) {
        showStatus("images/fail.svg", title, desc);

        const cookies = document.cookie.split(";")
            .filter(c => c.trim().startsWith(JS_COOKIE + "="));

        if (cookies.length > 0) {
            setButton("Resend Email", "api/register/resend");
        } else {
            setButton("Login and Resend", "...");
        }

    }

    /*
     * Change text and image showing to show activation status.
     */
    function showStatus(img, title, desc) {
        S_IMG.setAttribute("src", img);
        S_TITLE.innerText = title;
        S_DESC.innerText = desc;
    }

    /*
     * Change status button behavior.
     */
    function setButton(text, action) {
        S_BTN.innerText = text;
        S_BTN.setAttribute("href", action);
    }

</script>