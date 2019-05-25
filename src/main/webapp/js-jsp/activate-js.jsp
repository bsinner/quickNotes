<script>
    const CXT = "<%=request.getContextPath()%>";
    const Q_STRING = "?${params}";

    if (Q_STRING.length > 1) {
        activateAccount();
    }

    function activateAccount() {
        const url = CXT + "/api/activate" + Q_STRING;
        const props = { method : "POST" };

        fetch(url, props)
                .then(r => {
                    switch(r.status) {
                        case 200:
                            alert("success");
                            break;
                        case 400:
                            alert("missing params");
                            break;
                        case 404:
                            alert("token not found");
                            break;
                        case 410:
                            alert("expired token");
                            break;
                        case 500:
                            alert("internal error");
                            break;
                        default:
                            alert("other");
                    }
                })
    }

</script>