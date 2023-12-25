export class RecurrenceConfiguration {
    readonly id? : string;
    name: string;
    timeUnit: string;
    occurrencesPerTimePeriod: number;


    constructor(name: string, timeUnit: string, occurrencesPerTimePeriod: number, id?: string,) {
        this.id = id;
        this.name = name;
        this.timeUnit = timeUnit;
        this.occurrencesPerTimePeriod = occurrencesPerTimePeriod;
    }



    toString() {
        return JSON.stringify(this);
    }
}
