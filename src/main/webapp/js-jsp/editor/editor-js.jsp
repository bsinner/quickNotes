<script>

const REGEX = /note=([0-9]+)/;

/*
 * Load the editor
 */
const editor = loadEditor();

/*
 * If there is a query string containing a note id, load the note
 */
const id = checkQueryString();
if (id != null) {
    fetchNote(id).then(text => loadContents(editor, text));
}

/*
 * Check the query string for a note id parameter
 */
function checkQueryString() {
    let queryString = window.location.search;

    const results = queryString.match(REGEX);

    if (results != null && results.length > 1) {
        return results[1];
    }

    return null;
}

/*
 * Fetch the contents of a given note id
 */
async function fetchNote(id) {
    return await fetch("<%=request.getContextPath()%>/api/note?id=" + id, { credentials: "same-origin" })
        .then(res => {
            if (!res.ok) {
                console.error("Error loading note: " + res.status);
                return "{}";
            }

            return res.text();
        })
        .then(data => data)
        .catch(err => { console.error(err) });
}

/*
 * Load note contents into the Quill editor
 */
function loadContents(editor, noteString) {
    try {
        const noteJson = JSON.parse(noteString);
            editor.setContents(noteJson);
    } catch (SyntaxError) {
        console.error(SyntaxError + "Could not load note json: " + noteString);
    }
}

/*
 * Load a Quill editor
 */
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