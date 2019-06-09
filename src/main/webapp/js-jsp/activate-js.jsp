<%--<script src="js/loginRequest.js" type="application/javascript"></script>--%>
<script src="js/QNotesRequests.js" type="application/javascript"></script>

<script>
    const Q_STRING = "?${params}";
    const JS_COOKIE = "access_token_data";
    const CXT = "<%=request.getContextPath()%>";

    // The requests object, if the user cant be refreshed, log them out and refresh the page
    // so the servlet's filter will redirect them to the login page
    const REQUESTS = new QNotesRequests(CXT, () => {
        REQUESTS.logout(() => { location.reload(); }, () => { window.location = CXT + "/editor"; })
    });

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

        REQUESTS.activateAcct(Q_STRING, () => { showSuccess() }, err => {

            if (err.status === 410) {
                showFailed(...EXPIRED);
            } else if (err.status === 403) {
                showSuccess(); // show success because 403 means user is already activated
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

        // Refresh the access token, try to logout on fail
        REQUESTS.refresh(() => {}, () => {
           REQUESTS.logout(() => {}, () => {})
        });
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
            setButton("Resend Email", CXT + "/resend");
        } else {
            setButton("Login and Resend", CXT + "/resend");
        }

    }

    /*
     * Change text and image showing to show activation status.
     */
    function showStatus(img, title, desc) {
        document.getElementById("loader").setAttribute("style", "display: none;");
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