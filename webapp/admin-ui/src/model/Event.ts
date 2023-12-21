export class Event {
    readonly id: String | null;
    name: string;
    possibleTimes: Date[];
    chosenTime: Date | null;


    constructor(id: String | null, name: string, possibleTimes: Date[], chosenTime: Date | null) {
        this.id = id;
        this.name = name;
        this.possibleTimes = possibleTimes;
        this.chosenTime = chosenTime;
    }

    toString() {
        return JSON.stringify(this);
    }
}
