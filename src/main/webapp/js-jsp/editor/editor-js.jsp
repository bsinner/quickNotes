<script>

const REGEX = /id=([0-9]+)/;
const PATH = "<%=request.getContextPath()%>";

/*
 * Load the editor, load the current note, if no note is detected
 * id is null
 */
const editor = loadEditor();
loadNote();

function loadNote() {
    const idString = parseQueryString();

    if (idString != null) {

        fetchNote(idString)
                .then(text => {
                    if (text != null) {
                        loadContents(text);
                        addSaveButton(idString);
                    }
                });
    }

}

/*
 * Search the query string for a note parameter
 */
function parseQueryString() {
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
    return await fetch(PATH + "/api/note?id=" + id, { credentials: "same-origin" })
        .then(res => {
            if (!res.ok) {
                console.error("Error loading note: " + res.status);
                return null;
            }

            return res.text();
        })
        .then(data => data)
        .catch(err => { console.error(err); });
}

/*
 * Load note contents into the Quill editor
 */
function loadContents(noteString) {
    try {
        const noteJson = JSON.parse(noteString);
            editor.setContents(noteJson);
    } catch (SyntaxError) {
        console.error(SyntaxError + "Could not load note json: " + noteString);
    }
}

function addSaveButton(id) {
    const saveBtn = document.getElementById("saveBtn");

    saveBtn.onclick = () => {
        const url = PATH + "/api/saveNote?id=" + id;
        const props = {
            credentials : "same-origin"
            , method : "POST"
            , headers : { "note-contents" : JSON.stringify(editor.getContents()) }
        };

        fetch(url, props)
                .then(res => {
                    if (!res.ok) {
                        console.error("Error saving note: " + res.status);
                    }
                    // TODO: gray out save button temporarily
                });
    };

    saveBtn.removeAttribute("style");

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