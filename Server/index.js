/**
----------- Meldi Server -------------------------
Information-request for DigitaL Education of Minors
--------------------------------------------------
@author NprogramDev
@version 0.1
*/
const http = require("http");
const fs = require("fs");
const { userMap, BLOCKED_UUID } = require("./user.js");
const { YEAR, getMIMEType } = require("./util.js");
//
const MAX_ENTRY_LENGTH = 200;
const MAX_ENTRY_COUNT = 70;
//
const RAISED = "true";
const UNRAISED = "false";
const DISPLAY = fs.readFileSync("display.html").toString();
const VERSION = "0.1";

userMap.setUUIDDeath(0.5 * YEAR);

function writeErrorResponse(req, res) {
    res.statusCode = 405; // Method Not Allowed
    res.setHeader("Content-Type", "text/plain");
    res.end("Method Not Allowed");
}
function writeOkResponse(req, res, response, contentType = "text/plain") {
    res.statusCode = 200;
    res.setHeader("Content-Type", contentType);
    res.end(response);
}
function getParam(urlString, param) {
    const uuidArr = urlString.split(param);
    urlString = uuidArr[uuidArr.length - 1];
    const uuid = urlString.split("&")[0];
    return uuid;
}
function getWithoutPath(url) {
    let rt = url.split("?");
    rt.shift();
    if (rt.length < 1) return "";
    rt = rt.join("?");
    return rt;
}
function ErrorPage(code, data, type = "text/plain") {
    return { code: code, data: data, type: type, isErrorPage: true };
}
function writeRedirectResponse(req, res, location) {
    res.statusCode = 302; // Found
    res.setHeader("Location", location);
    res.setHeader("Content-Type", "text/plain");
    res.end("Redirecting to " + location);
}

function handleGETRequest(req, res) {
    // Response with the version of connection check of the client
    if (req.url === "/is-meldi-server") {
        writeOkResponse(req, res, VERSION);
        return;
    }
    // If a student wants to raise the hand
    if (req.url.startsWith("/raise")) {
        const uuid = getParam(req.url, "uuid=");
        const params = getWithoutPath(req.url);
        const group = userMap.get(uuid);
        if (group) {
            group.raise(params);
            writeOkResponse(req, res, RAISED);
            return;
        }
        throw ErrorPage(404, "Group not found or invalid parameters", "text/plain");
    }
    if (req.url.startsWith("/unraise")) {
        const uuid = getParam(req.url, "uuid=");
        const q = getWithoutPath(req.url);
        const group = userMap.get(uuid);
        if (group && group.unraise(q)) {
            writeRedirectResponse(req, res, "/display?uuid=" + uuid);
            return;
        }
        throw ErrorPage(404, "Group not found or invalid parameters", "text/plain");
    }
    if (req.url.startsWith("/get_position")) {
        const uuid = getParam(req.url, "uuid=");
        const stuID = getWithoutPath(req.url);
        const group = userMap.get(uuid);
        if (group) {
            writeOkResponse(req, res, group.indexOfStudent(stuID).toString());
        } else writeOkResponse(req, res, "-2");
        return;
    }
    if (req.url.startsWith("/entriesForID")) {
        const uuid = getParam(req.url, "uuid=");
        const group = userMap.get(uuid);
        if (!group) throw ErrorPage(403, '<h1 style="color: red; font-weight: bold;"> This UUID is blocked! No not use! </h1>', "text/html");
        writeOkResponse(req, res, group.serializedRaises());
        return;
    }
    if (req.url.startsWith("/display")) {
        const uuid = getParam(req.url, "uuid=");
        if (uuid === BLOCKED_UUID) throw ErrorPage();
        const group = userMap.get(uuid);
        if (!group) {
            throw ErrorPage(404, "Not Found", "text/plain");
        }
        writeOkResponse(
            req,
            res,
            DISPLAY.split("{{display}}")
                .join(
                    group.raised
                        .map(entry => {
                            let a = entry.stuId.split("&");
                            let name = "-NoName-",
                                pc = "-!NoName-";
                            for (let b of a) {
                                if (b.startsWith("user=")) {
                                    name = b.split("=")[1];
                                }
                                if (b.startsWith("device=")) {
                                    pc = b.split("=")[1];
                                }
                            }
                            return `<tr><td>${name}</td><td>${pc}</td><td><a href="/unraise?${entry.stuId}">&check;</a></td></tr>`;
                        })
                        .join("")
                )
                .split("{{notify}}")
                .join('[ "' + group.getNotifications().join('","') + '"]'),
            "text/html"
        );
        return;
    }
    if (req.url.startsWith("/expiresOn")) {
        const uuid = getParam(req.url, "uuid=");
        const group = userMap.get(uuid);
        if (!group) throw ErrorPage(404, "Not Found", "text/plain");
        writeOkResponse(req, res, group.expireTime.toString());
        return;
    }
    if (req.url.startsWith("/newExpireTime")) {
        const uuid = getParam(req.url, "uuid=");
        const group = userMap.get(uuid);
        if (!group) throw ErrorPage(404, "Not Found", "text/plain");
        const newExpireTime = parseInt(req.url.substring(req.url.length - 3));
        if (isNaN(newExpireTime)) {
            throw ErrorPage(400, "Invalid expire time", "text/plain");
        }
        group.expireTime = newExpireTime;
        writeOkResponse(req, res, "Expire time updated");
        return;
    }
    if (req.url.startsWith("/getUserId")) {
        const uuid = userMap.createNewUser();
        writeOkResponse(req, res, uuid);
        return;
    }
    if (req.url == "/") {
        writeOkResponse(req, res, fs.readFileSync("index.html"), "text/html");
        return;
    }
    if (req.url == "/favicon.ico") {
        return;
    }
    if (req.url.startsWith("/static")) {
        let filePath = req.url.substring("/static/".length);
        filePath = "./static/" + filePath; // Ensure the path is relative to the static directory
        filePath = filePath.replace(/\.\./g, ""); // Prevent directory traversal
        console.log("Static file requested: " + req.url + "  _ ", filePath);
        if (fs.existsSync(filePath)) {
            const fileContent = fs.readFileSync(filePath);
            const contentType = getMIMEType(filePath);
            writeOkResponse(req, res, fileContent, contentType);
            return;
        }
    }
    throw ErrorPage(404, "This endpoint does not exist!", "text/plain");
}
function handleRequestWithErrorHandling(req, res) {
    try {
        if (req.method !== "GET") {
            writeErrorResponse(req, res);
            return;
        }
        if (req.url.length > MAX_ENTRY_LENGTH) throw ErrorPage(400, "Maximum param length is reached at " + MAX_ENTRY_LENGTH + " chars", "text/plain");
        handleGETRequest(req, res);
    } catch (e) {
        if (e.isErrorPage) {
            res.statusCode = e.code;
            res.setHeader("Content-Type", e.type);
            res.end(e.data);
        } else {
            res.statusCode = 500;
            res.setHeader("Content-Type", "text/plain");
            res.end("Internal Server Error");
        }
        console.log("Error: ", e);
    }
}
const server = http.createServer(handleRequestWithErrorHandling);

const PORT = 5321;
server.listen(PORT, () => {
    console.log(`Server running at http://localhost:${PORT}/`);
});
