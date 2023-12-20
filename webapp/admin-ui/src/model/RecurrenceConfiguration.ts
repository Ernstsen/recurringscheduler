export class RecurrenceConfiguration {
    readonly id: String | null;
    name: string;
    timeUnit: string;
    occurrencesPerTimePeriod: number;


    constructor(id: String | null, name: string, timeUnit: string, occurrencesPerTimePeriod: number) {
        this.id = id;
        this.name = name;
        this.timeUnit = timeUnit;
        this.occurrencesPerTimePeriod = occurrencesPerTimePeriod;
    }



    toString() {
        return JSON.stringify(this);
    }
}
