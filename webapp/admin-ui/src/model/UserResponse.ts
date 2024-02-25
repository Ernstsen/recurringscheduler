export class UserResponse {
    readonly id: string;
    readonly eventId: string;
    readonly userId: string;
    readonly chosenDates: Date[];

    constructor(id: string, eventId: string, userId: string, chosenDates: Date[]) {
        this.id = id;
        this.eventId = eventId;
        this.userId = userId;
        this.chosenDates = chosenDates;
    }

    toString() {
        return JSON.stringify(this);
    }

}
