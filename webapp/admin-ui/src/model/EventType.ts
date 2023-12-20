import {RecurrenceConfiguration} from "./RecurrenceConfiguration.ts";

export class EventType {
    readonly id: String | null;
    name: string;
    recurrenceConfiguration: RecurrenceConfiguration;


    constructor(id: String | null, name: string, recurrenceConfiguration: RecurrenceConfiguration) {
        this.id = id;
        this.name = name;
        this.recurrenceConfiguration = recurrenceConfiguration;
    }

    toString() {
        return JSON.stringify(this);
    }
}
