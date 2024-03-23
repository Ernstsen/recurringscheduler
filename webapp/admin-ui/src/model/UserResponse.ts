export class UserResponse {
    readonly id: string;
    readonly eventId: string;
    readonly userEntityId: string;
    readonly chosenDates: Date[];

    constructor(id: string, eventId: string, userId: string, chosenDates: Date[]) {
        this.id = id;
        this.eventId = eventId;
        this.userEntityId = userId;
        this.chosenDates = chosenDates;
    }

    toString() {
        return JSON.stringify(this);
    }

}
