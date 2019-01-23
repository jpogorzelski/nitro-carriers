export interface IPerson {
    id?: number;
    firstName?: string;
    lastName?: string;
    companyId?: string;
    phoneNumber?: string;
    carrierId?: number;
}

export class Person implements IPerson {
    constructor(
        public id?: number,
        public firstName?: string,
        public lastName?: string,
        public companyId?: string,
        public phoneNumber?: string,
        public carrierId?: number
    ) {}
}
