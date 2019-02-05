export interface IPerson {
    id?: number;
    firstName?: string;
    lastName?: string;
    companyId?: number;
    phoneNumber?: string;
    carrierName?: string;
    carrierId?: number;
}

export class Person implements IPerson {
    constructor(
        public id?: number,
        public firstName?: string,
        public lastName?: string,
        public companyId?: number,
        public phoneNumber?: string,
        public carrierName?: string,
        public carrierId?: number
    ) {}
}
