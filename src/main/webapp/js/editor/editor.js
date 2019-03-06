window.onload = () => {
    const quill = new Quill("#editor", {
        modules: {
            toolbar: [
                [{"font" : []}]
                , [{"size" : []}]
                , [{"align" : []}]
                , ["bold", "italic", "underline", "strike", "blockquote"]
                , [{"color" : []}, {"background" : []}]
                , [{"script" : "sub"}, {"script": "super"}]
                , [{"list" : "ordered"}, {"list" : "bullet"}, {"header" : 1}, {"header" : 2}]
                , ["link", "image", "video"]
                , ["clean"]
            ]
        },
        theme: "snow"
    });

}
