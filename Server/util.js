const SEC = 1000;
const MINUTE = 60 * SEC;
const HOUR = 60 * MINUTE;
const DAY = 24 * HOUR;
const YEAR = 365 * DAY;
module.exports = {
    SEC: SEC,
    MINUTE: MINUTE,
    HOUR: HOUR,
    DAY: DAY,
    YEAR: YEAR,
    getMIMEType(filepath) {
        const ext = filepath.split(".").pop().toLowerCase();
        switch (ext) {
            case "html":
                return "text/html";
            case "css":
                return "text/css";
            case "js":
                return "application/javascript";
            case "json":
                return "application/json";
            case "png":
                return "image/png";
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "gif":
                return "image/gif";
            case "svg":
                return "image/svg+xml";
            case "txt":
                return "text/plain";
            default:
                return "application/octet-stream"; // Default for unknown types
        }
    },
};
