import {Event} from "./Event";
export class UserResponse {
    readonly id: String;
    readonly userId: String;
    readonly event: Event;
    chosenTimes: Date[];


    constructor(id: String, userId: String, event: Event, chosenTimes: Date[]) {
        this.id = id;
        this.userId = userId;
        this.event = event;
        this.chosenTimes = chosenTimes;
    }

    toString() {
        return JSON.stringify(this);
    }
}
