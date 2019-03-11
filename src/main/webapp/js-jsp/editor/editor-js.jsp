<script>

const MIN_JSON_LENGTH = 2;

window.onload = () => {

    // Load the editor
    const loadedEditor = loadEditor();

    // Get the note to be loaded, if the user is trying to access an empty
    // editor this variable will be an empty string.
    const noteString = '${contents}';

    // If the note isn't empty, open it in the editor
    if (noteString.length > MIN_JSON_LENGTH) {
        loadContents(loadedEditor, noteString);
    }

    function loadEditor() {
        const quill = new Quill("#editor", {
            modules: {
                toolbar: [
                    [{"font": []}]
                    , [{"size": []}]
                    , [{"align": []}]
                    , ["bold", "italic", "underline", "strike", "blockquote"]
                    , [{"color": []}, {"background": []}]
                    , [{"script": "sub"}, {"script": "super"}]
                    , [{"list": "ordered"}, {"list": "bullet"}, {"header": 1}, {"header": 2}]
                    , ["link", "image", "video"]
                    , ["clean"]
                ]
            }
            , theme: "snow"
        });

        return quill;
    }

    function loadContents(editor, noteString) {
        try {
            const noteJson = JSON.parse(noteString);
            editor.setContents(noteJson);
        } catch (SyntaxError) {
            console.log(
                SyntaxError
                + "Erroneous json: " + noteString
            );
        }
    }

}

</script>