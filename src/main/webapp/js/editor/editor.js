window.onload = () => {
    const quill = new Quill("#editor", {
        modules: {
            toolbar: [
                [{"font" : []}]
                , [{"size" : []}]
                , [{"align" : []}]
                , ["bold", "italic", "underline"]
                , ["blockquote"]
                , [{"color" : []}]
                , [{"background" : []}] m
            ]
        },
        theme: "snow"
    });

}
