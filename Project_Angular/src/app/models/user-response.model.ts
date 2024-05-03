import { Role } from "./role.model";

export interface UserDataResponse {
    id: number;
    firstName: string;
    lastName: string;
    email: string;
    role: Role;
}

