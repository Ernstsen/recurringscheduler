import {RecurrenceConfiguration} from "./RecurrenceConfiguration.ts";
import {User} from "./User.ts";

export class EventType {
    readonly id?: string;
    name: string;
    recurrenceConfiguration: RecurrenceConfiguration;
    participatingUsers: User[];


    constructor(name: string, recurrenceConfiguration: RecurrenceConfiguration, participatingUsers: User[], id?: string) {
        this.id = id;
        this.name = name;
        this.recurrenceConfiguration = recurrenceConfiguration;
        this.participatingUsers = participatingUsers;
    }

    toString() {
        return JSON.stringify(this);
    }
}
