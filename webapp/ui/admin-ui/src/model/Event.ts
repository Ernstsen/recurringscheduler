import {EventType} from "./EventType.ts";

export class Event {
    readonly id: String | null;
    name: string;
    type: EventType;
    possibleTimes: Date[];
    chosenTime: Date | null;


    constructor(id: String | null, name: string, eventType: EventType, possibleTimes: Date[], chosenTime: Date | null) {
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
