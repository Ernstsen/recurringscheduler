import {User} from "./User";

export class AuthenticationInformation {
    user?: User
    token?: string;

    constructor(user?: User, token?: string) {
        this.user = user;
        this.token = token;
    }

    toString() {
        return JSON.stringify(this);
    }
}
