<script>

    const REGEX = /id=([0-9]+)/;
    const PATH = "<%=request.getContextPath()%>";
    const EDITOR_REQ = new QNotesRequests(PATH, () => { $("#loginModal").modal("show"); });

    // Translate api limits
    const MAX_CHARS = 15000;
    const PER_REQUEST = 5000;
    const REQ_MAX_ELEMENTS = 100;

    /*
     * Load the editor, load the current note, if no note is detected
     * id is null
     */
    const EDITOR = loadEditor();
    loadNote();

    /*
     * Initialize modal drop down
     */
    $("select.dropdown").dropdown();

    /*
     * If there is a note in the
     */
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
                EDITOR.setContents(noteJson);
        } catch (SyntaxError) {
            console.error(SyntaxError + "Could not load note json: " + noteString);
        }
    }

    /*
     * Add a save button and an event handler if the user is logged in
     */
    function addSaveButton(id) {
        const saveBtn = document.getElementById("saveBtn");

        saveBtn.onclick = () => {

            EDITOR_REQ.saveNote(id, JSON.stringify(EDITOR.getContents()), () => {}
                    , res => {
                        console.error("Error " + res.status + ": note could not be saved");
                    });

            // TODO: gray out save button temporarily
        };

        saveBtn.removeAttribute("style");

    }

    /*
     * Translate event handlers
     */
    const translateModal = $("#translateModal");

    // Hide show translate modal events
    document.getElementById("translateCancel").onclick = () => { translateModal.modal("hide"); };
    document.getElementById("translateExit").onclick = () => { translateModal.modal("hide"); };
    document.getElementById("translateButton").onclick = () => { translateModal.modal("show"); };

    /*
     * Translate submit event handler
     */
    document.getElementById("translateSubmit").onclick = () => {

        // Original text and text to send to the api
        const original = EDITOR.getContents().ops;
        const formatted = [[]];

        // Used to split text into multiple requests to stay within the api limits
        let currRequest = 0;
        let requestTotal = 0;
        let requestElems = 0;
        let total = 0;


        // Build the text to send for translation
        loopJson(original.length, index => {
            const currElement = original[index]["insert"];

            if (isTranslatable(currElement)) {
                requestTotal += currElement.length;
                requestElems += 1;
                total += currElement.length;

                if (requestTotal > PER_REQUEST || requestElems > REQ_MAX_ELEMENTS) {
                    requestTotal = 0;
                    requestElems = 0;
                    formatted.push([]);
                    currRequest += 1;
                }

                formatted[currRequest].push({ "Text" : currElement });
            }

        });

        if (total > MAX_CHARS) {
            // TODO: hide modal, show error
        } else {
            translateText(original, formatted);
        }
    };

    /*
     * Make requests and combine the output into one array of objects
     */
    function translateText(original, formatted) {

        // Get values from dropdowns
        const s = document.getElementById("sourceLang");
        const d = document.getElementById("destLang");
        const source = s.options[s.selectedIndex].value;
        const dest = d.options[d.selectedIndex].value;

        // Results of batch of api calls
        let results = [];

        formatted.forEach((item, index) => {

            EDITOR_REQ.translate(JSON.stringify(item), source, dest, res => {

                // On success add json to array, on last api call send array for processing
                res.json().then(data => {
                    results = results.concat(data);
                    if (index === formatted.length - 1) {
                        applyChanges(original, results);
                    }
                });

            }, res => {
                console.error("Error " + res.status + ": could not translate text");
                // TODO: user error message?
            });

        });

    }

    /*
     * Put output from the translate api in the editor, close the modal
     */
    function applyChanges(original, results) {
        let tracker = 0;

        loopJson(original.length, i => {

            if (isTranslatable(original[i]["insert"])) {
                original[i]["insert"] = results[tracker].translations[0]["text"];
                tracker++;
            }

        });

        EDITOR.setContents(original);
        translateModal.modal("hide");
    }

    /*
     * Helper functions for translating text
     */

    // Wrapper for looping an array
    function loopJson(length, callback) {
        for (let i = 0; i < length; i++) {
            callback(i);
        }
    }

    // Determine if an element should be translated,
    // trim filters elements with only newlines and tabs
    function isTranslatable(obj) {
        return obj !== undefined && obj.trim().length >= 1;
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