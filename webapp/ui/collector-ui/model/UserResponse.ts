import {Event} from "./Event";
export class UserResponse {
    readonly id: String;
    readonly userId: String;
    readonly event: Event;
    chosenDates: Date[];


    constructor(id: String, userId: String, event: Event, chosenDates: Date[]) {
        this.id = id;
        this.userId = userId;
        this.event = event;
        this.chosenDates = chosenDates;
    }

    toString() {
        return JSON.stringify(this);
    }
}
