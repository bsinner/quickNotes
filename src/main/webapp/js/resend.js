
const CXT = document.getElementById("cxt").value;

const REQUESTS = new QNotesRequests(CXT, () => {
    REQUESTS.logout(
        () => { window.location = CXT + "/login" }
        , () => { window.location = CXT + "/editor"; });
}, () => {});

sendEmail();

/*
 * Resend button event handler
 */
document.getElementById("resend").onclick = () => { sendEmail(); };

/*
 * Try to send an activation email, if no user is logged in redirect to login page
 */
function sendEmail() {
    REQUESTS.resendActivate(() => {}, err => {
        console.error("Error " + err.status + ": activation could not be resent");
    });
}
