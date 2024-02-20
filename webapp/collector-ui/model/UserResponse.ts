export class UserResponse {
    readonly id: String;
    readonly userId: String;
    readonly eventId: String;
    chosenTimes: Date[];


    constructor(id: String, userId: String, eventId: String, chosenTimes: Date[]) {
        this.id = id;
        this.userId = userId;
        this.eventId = eventId;
        this.chosenTimes = chosenTimes;
    }

    toString() {
        return JSON.stringify(this);
    }
}
