const fs = require("fs");
const crypto = require("crypto");
const USER_BASE_FILE = "/data/users.json";
const BLOCKED_UUID = "c065fa0d-a74c-4723-8fb3-a0eea0d8f17b-f2ef7f77-aa06-4aea-b36b-767dd2886df9";
class UserEntry {
    /**
     * The time until all raised are taken down
     */
    _expireTime = 0;
    /**
     * Time when the user is no longer used
     */
    timeOfDeath = 0;
    /**
     * All the students raising their hand
     */
    raised = [];
    /**
     * Just init with 0
     */
    constructor() {
        this._expireTime = 0;
        this.timeOfDeath = 0;
        this.raised = [];
        this.notifBuffer = [];
    }
    /**
     * @returns The time until every student takes down their hand
     */
    get expireTime() {
        return this._expireTime;
    }
    /**
     * expireTime can only be a positive number
     */
    set expireTime(value) {
        if (typeof value !== "number" || value < 0) {
            throw new Error("expireTime must be a positive number");
        }
        this._expireTime = value;
    }
    /**
     * Find the index of a certain student raising their hand
     * @param {string} stuId The id of a user
     * @returns The index of a student -1 = not existent
     */
    indexOfStudent(stuId) {
        for (let i = 0; i < this.raised.length; i++) {
            if (this.raised[i].stuId === stuId) {
                return i;
            }
        }
        return -1;
    }
    /**
     * Raised the hand of a certain student
     * @param {string} stuId The students ID
     * @returns Whether the student had the state already
     */
    raise(stuId) {
        if (this.notifBuffer.indexOf(stuId) === -1) this.notifBuffer.push(stuId);
        if (this.raised.find(elm => stuId == elm.stuId)) return false;
        this.raised.push({
            stuId: stuId,
            time: Date.now(),
        });
        return true;
    }
    /**
     * Removes the raised hand of a certain student
     * @param {string} student The students ID
     * @returns Whether the student had the state already
     */
    unraise(student) {
        const index = this.indexOfStudent(student);
        if (index === -1) return false;
        const buffidx = this.notifBuffer.indexOf(student);
        if (buffidx != -1) this.notifBuffer.splice(buffidx, 1);
        this.raised.splice(index, 1);
        return true;
    }
    /**
     * @returns All new raises since the last request
     */
    getNotifications() {
        let newObj = this.notifBuffer.slice();
        this.notifBuffer = [];
        return newObj;
    }
    /**
     * @returns All the students as a csv list. Currently on the student ids
     */
    serializedRaises() {
        return this.raised
            .map(entry => {
                return entry.stuId + "\r\n";
            })
            .join("");
    }
    /**
     * Only executed on load from the user file
     * @param {*} config
     */
    initWithConfig(config) {}
}
class UserMap {
    /**
     * All users known to the server!
     */
    users = {};
    /**
     * Time it takes until a unused user uuid is removed
     */
    uuidDeath = 0;
    constructor() {
        this.users = {};
        this.uuidDeath = 0;
    }
    /**
     * Sets the time it takes until a unused user is removed
     * @param {number} ms Time in MS
     */
    setUUIDDeath(ms) {
        this.uuidDeath = ms;
        this.load();
    }
    /**
     * Checks all admin users whether they have to be removed, because of no use
     */
    checkDeath() {
        const now = Date.now();
        let changed = false;
        for (const uuid in this.users) {
            if (this.users[uuid].timeOfDeath < now) {
                // If the time of death is before now
                delete this.users[uuid]; // Delete the uuid user
                changed = true;
                console.log("unused uuid found: ", uuid);
            }
        }
        if (changed) {
            // Save it once if it changed
            this.save();
        }
    }
    /**
     * Returns the admin user profile for a certain uuid
     * @param {uuid} uuid The admin users uuid
     * @returns {UserEntry} The Admin User Entry with a all the raised hands and stuff
     */
    get(uuid) {
        if (uuid === BLOCKED_UUID) return null; // The blocked uuid, because it's the uuid in the repo
        if (!this.users[uuid]) {
            // If the user is not found
            return null;
        }
        const rt = this.users[uuid];
        rt.timeOfDeath = Date.now() + this.uuidDeath; // the death of the user is delayed, because of usage
        this.checkDeath(); // Check whether another uuid is dead.
        return rt;
    }
    /**
     * Creates a mew user entry by finding a unused uuid and saving it to the user stack
     * @returns The uuid of the new user
     */
    createNewUser() {
        let uuid;
        do {
            uuid = crypto.randomUUID() + "-" + crypto.randomUUID();
        } while (this.users[uuid]);
        this.users[uuid] = new UserEntry();
        this.users[uuid].timeOfDeath = Date.now() + this.uuidDeath;
        this.save();
        return uuid;
    }
    /**
     * Saves the user stack to a file
     */
    save() {
        fs.writeFileSync(USER_BASE_FILE, JSON.stringify(this.users, null, 2), "utf8");
    }
    /**
     * Loads the user stack from a file
     */
    load() {
        if (fs.existsSync(USER_BASE_FILE)) {
            let fileUsers = JSON.parse(fs.readFileSync(USER_BASE_FILE, "utf8"));
            console.log("Loaded user data from file.", this.users);
            for (let val of Object.keys(fileUsers)) {
                const entry = new UserEntry();
                entry.initWithConfig(fileUsers[val]);
                entry.timeOfDeath = Date.now() + this.uuidDeath;
                this.users[val] = entry;
            }
        } else {
            //No config file, no users. Just create one
            this.users = {};
            this.save();
        }
    }
}
let userMap = new UserMap();
module.exports = {
    UserEntry,
    UserMap,
    userMap,
    BLOCKED_UUID,
};
