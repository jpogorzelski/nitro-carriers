import { ICarrier } from 'app/shared/model/carrier.model';

export interface IPerson {
    id?: number;
    firstName?: string;
    lastName?: string;
    companyId?: number;
    phoneNumber?: string;
    carrier?: ICarrier;
}

export class Person implements IPerson {
    constructor(
        public id?: number,
        public firstName?: string,
        public lastName?: string,
        public companyId?: number,
        public phoneNumber?: string,
        public carrier?: ICarrier
    ) {}
}
