export class User {
    readonly id: string | null;
    name: string;
    email: string;

    constructor(id: string | null, name: string, email: string) {
        this.id = id;
        this.name = name;
        this.email = email;
    }


    toString() {
        return JSON.stringify(this);
    }
}
