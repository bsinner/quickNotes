window.onload = () => {
    $("input[type=checkbox]").bind("click", () => {
        const btn = $("#delBtn");

        if ($("input:checkbox:checked").length > 0) {
            btn.attr("class", "ui small button");
        } else {
            btn.attr("class", "ui small disabled button");
        }
    });
}