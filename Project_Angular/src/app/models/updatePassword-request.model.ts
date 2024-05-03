export interface UpdatePasswordRequest{
    newPassword: string;
    confirmedPassword: string;
    token: string;
}