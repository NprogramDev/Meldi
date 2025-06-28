const fs = require("fs");
const crypto = require("crypto");
const USER_BASE_FILE = "/data/users.json";
class UserEntry {
    constructor() {
        this._expireTime = 0;
        this.timeOfDeath = 0;
        this.raised = [];
        this.notifBuffer = [];
    }
    get expireTime() {
        return this._expireTime;
    }
    set expireTime(value) {
        if (typeof value !== "number" || value < 0) {
            throw new Error("expireTime must be a positive number");
        }
        this._expireTime = value;
    }
    indexOfStudent(stuId) {
        for (let i = 0; i < this.raised.length; i++) {
            if (this.raised[i].stuId === stuId) {
                return i;
            }
        }
        return -1;
    }
    raise(stuId) {
        if(this.notifBuffer.indexOf(stuId) === -1) this.notifBuffer.push(stuId);
        if(this.raised.find(elm => stuId == elm.stuId)) return;
        this.raised.push({
            stuId: stuId,
            time: Date.now(),
        });
    }
    unraise(student) {
        const index = this.indexOfStudent(student);
        if (index === -1) return false;
        const buffidx = this.notifBuffer.indexOf(student);
        if(buffidx != -1) this.notifBuffer.splice(buffidx,1);
        this.raised.splice(index, 1);
        return true;
    }
    getNotifications(){
        let newObj = this.notifBuffer.slice();
        this.notifBuffer = [];
        return newObj;
    }
    serializedRaises() {
        return this.raised
            .map(entry => {
                return entry.stuId + "\r\n";
            })
            .join("");
    }
    initWithConfig(config) {}
}
class UserMap {
    constructor() {
        this.users = {};
        this.uuidDeath = 0;
    }
    setUUIDDeath(ms) {
        this.uuidDeath = ms;
        this.load();
    }
    checkDeath() {
        const now = Date.now();
        let changed = false;
        for (const uuid in this.users) {
            if (this.users[uuid].timeOfDeath < now) {
                delete this.users[uuid];
                changed = true;
            }
        }
        if (changed) {
            this.save();
        }
    }
    get(uuid) {
        if (!this.users[uuid]) {
            return null;
        }
        const rt = this.users[uuid];
        rt.timeOfDeath = Date.now() + this.uuidDeath;
        this.checkDeath();
        return rt;
    }
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
    save() {
        fs.writeFileSync(USER_BASE_FILE, JSON.stringify(this.users, null, 2), "utf8");
    }
    load() {
        if (fs.existsSync(USER_BASE_FILE)) {
            let fileUsers = JSON.parse(fs.readFileSync(USER_BASE_FILE, "utf8"));
            console.log("Loaded user data from file.", this.users);
            for (let val of Object.keys(fileUsers)) {
                const entry = new UserEntry();
                entry.initWithConfig(fileUsers[val]);
                entry.expireTime = Date.now() + this.uuidDeath;
                this.users[val] = entry;
            }
        } else {
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
};
