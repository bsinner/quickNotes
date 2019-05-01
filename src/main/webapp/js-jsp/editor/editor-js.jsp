<script>

const REGEX = /note=([0-9]+)/;


const editor = loadEditor();

const id = checkQueryString();
if (id != null) {
    fetchNote(id).then(text => loadContents(editor, text));
}

const loadContents = (editor, noteString) => {
    try {
        const noteJson = JSON.parse(noteString);
            editor.setContents(noteJson);
    } catch (SyntaxError) {
        console.error(SyntaxError + "Could not load note json: " + noteString);
    }
};


function checkQueryString() {
    let queryString = window.location.search;

    const results = queryString.match(REGEX);

    if (results != null && results.length > 1) {
       return results[1];
    }

    return null;
}

async function fetchNote(id) {
    return await fetch("<%=request.getContextPath()%>/api/note?id=" + id, { credentials: "same-origin" })
            .then(res => res.text())
            .then(data => data)
            .catch(err => { console.error(err) });
}


function loadEditor() {
    return new Quill("#editor", {
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
}

</script>