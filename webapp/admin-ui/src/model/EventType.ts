import {RecurrenceConfiguration} from "./RecurrenceConfiguration.ts";

export class EventType {
    readonly id?: string;
    name: string;
    recurrenceConfiguration: RecurrenceConfiguration;


    constructor(name: string, recurrenceConfiguration: RecurrenceConfiguration, id?: string) {
        this.id = id;
        this.name = name;
        this.recurrenceConfiguration = recurrenceConfiguration;
    }

    toString() {
        return JSON.stringify(this);
    }
}
