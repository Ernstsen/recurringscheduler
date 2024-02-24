export class Event {
    readonly id: String | null;
    name: string;
    type: { name: string };
    possibleTimes: Date[];
    chosenTime: Date | null;


    constructor(id: String | null, name: string, eventType: { name: string }, possibleTimes: Date[], chosenTime: Date | null) {
        this.id = id;
        this.name = name;
        this.type = eventType;
        this.possibleTimes = possibleTimes;
        this.chosenTime = chosenTime;
    }

    toString() {
        return JSON.stringify(this);
    }
}
