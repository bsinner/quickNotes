window.onload = () => {
    const quill = new Quill('#editor', {
        modules: {
            toolbar: [ ['bold', 'italic', 'underline'] ]
        },
        theme: "snow"
    });

}
