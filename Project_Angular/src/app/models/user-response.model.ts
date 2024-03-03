import { Role } from "./role.model";

export class UserDataResponse {
    id: number;
    firstName: string;
    lastName: string;
    email: string;
    role: Role;
}

