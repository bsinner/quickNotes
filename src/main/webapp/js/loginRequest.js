/*
 * Try to login the user, input must match
 * following format:
 * {
 *     basePath : the context path
 *     , onComplete : on login callback
 *     , onError : on error callback
 * }
 */
function LoginRequest(obj) {
    this.obj = obj;
    const jsCookie = "access_token_data";

    this.login = (email, pass) => {
        const url = obj.basePath + "/api/login?email=" + email + "&password=" + pass;
        const props = { method : "POST" };

        fetch(url, props)
            .then(res => {
               if (res.status === 401) {
                   obj.onError();
               } else {
                   res.text().then(t => {

                       // add a JS accessible cookie so that the
                       // menu bar can display the current user
                       document.cookie = jsCookie + "=" + t + "; Path=" + obj.basePath;
                       obj.onComplete(t);
                   });
               }
            });
    };
}