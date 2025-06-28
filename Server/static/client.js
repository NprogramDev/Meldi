const JAR_FILE_INDEX = "/static/dist/index.json";
const NEW_USER_API = "/getUserId";
const IN_JAR_FILE_LOCATION = "de/a_sitko/meldi/Changeables.class";
const BASE_UUID = "c065fa0d-a74c-4723-8fb3-a0eea0d8f17b-f2ef7f77-aa06-4aea-b36b-767dd2886df9";
const JUST_A_FILE_NAME = "conf.meldi.json";
const WAIT_TEXT = "Bitte warten...";
const MIME = {
    JSON: "application/json",
};
const ID_E = {
    ID_FRAME: "id_frame",
    MSG_BOX: "infoMessage",
    NEW_FILE_FRAME: "newFiles",
};
const ID_FRAME = document.getElementById(ID_E.ID_FRAME);

/**
 * Creates a new User with a UUID
 */
async function newUser() {
    if (ID_FRAME.innerText.trim() !== "") return;
    let uuid = await fetch(NEW_USER_API);
    uuid = await uuid.text();
    ID_FRAME.innerText = WAIT_TEXT;
    const b = document.getElementById(ID_E.NEW_FILE_FRAME);
    let indexFile = await fetch(JAR_FILE_INDEX);
    indexFile = await indexFile.json();
    for (let o of indexFile.files) {
        b.appendChild(await jarFileChanger(o, uuid));
    }
    ID_FRAME.innerText = uuid;
    document.getElementById(ID_E.MSG_BOX).style.display = "block";
    b.appendChild(await justAFile(uuid));
}
/**
 * Creates a useless config file with the uuid in it.
 * @returns a file container as html element to be appended
 */
async function justAFile(uuid) {
    let file = new File(['{\r\n "uuid":" ' + uuid + '" \r\n} '], JUST_A_FILE_NAME, {
        type: MIME.JSON,
    });
    let url = URL.createObjectURL(file);
    return fileBox(url, JUST_A_FILE_NAME);
}
/**
 * Creates a file container based on the link and filename
 * @param {string} link
 * @param {string} filename
 * @returns {HTMLDivElement} The file container element
 */
async function fileBox(link, filename) {
    let elm = document.createElement("div");
    let a = document.createElement("a");
    a.href = link;
    a.innerText = filename;
    a.classList.add("fileLink");
    a.download = filename;
    elm.appendChild(a);
    elm.classList.add("filesContainer");
    return elm;
}
/**
 * Changes the uuid in the jar file and returns jar files file container
 * @param {string} path
 * @param {string} uuid
 * @returns {HTMLDivElement}
 */
async function jarFileChanger(path, uuid) {
    let zip = await fetch(path);
    zip = await zip.arrayBuffer();
    zip = await JSZip.loadAsync(zip);
    const changeFile = zip.file(IN_JAR_FILE_LOCATION);
    if (!changeFile) {
        alert("Changeables.class not found in ZIP.");
        return;
    }
    const encoder = new TextEncoder();

    const searchString = encoder.encode(BASE_UUID);
    const newUUIDString = encoder.encode(uuid);
    let binaryData = await changeFile.async("uint8array");
    binaryData = replaceBytes(binaryData, searchString, newUUIDString);
    zip.file(IN_JAR_FILE_LOCATION, binaryData);
    const newZipBlob = await zip.generateAsync({ type: "blob" });
    let filename = path.split("/").pop();
    return fileBox(URL.createObjectURL(newZipBlob), filename);
}
/**
 * Replaces the search string with the replace string in the data string
 * @param {Uint8Array} data
 * @param {Uint8Array} search
 * @param {Uint8Array} replace
 * @returns
 */
function replaceBytes(data, search, replace) {
    for (let i = 0; i <= data.length - search.length; i++) {
        let match = true;
        for (let j = 0; j < search.length; j++) {
            if (data[i + j] !== search[j]) {
                match = false;
                break;
            }
        }
        if (match) {
            data.set(replace, i);
            break; // only replace first match; remove if you want all
        }
    }
    return data;
}
