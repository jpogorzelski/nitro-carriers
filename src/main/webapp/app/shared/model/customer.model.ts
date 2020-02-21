import { ICity } from 'app/shared/model/city.model';
import { ICountry } from 'app/shared/model/country.model';
import { IUser } from 'app/core/user/user.model';

export const enum CustomerState {
    AVAILABLE = 'AVAILABLE',
    TAKEN = 'TAKEN',
    TEMPORARILY_TAKEN = 'TEMPORARILY_TAKEN'
}

export interface ICustomer {
    id?: number;
    name?: string;
    nip?: string;
    address?: string;
    postalCode?: string;
    state?: CustomerState;
    notes?: string;
    city?: ICity;
    country?: ICountry;
    user?: IUser;
    createdBy?: string;
    createdDate?: Date;
    lastModifiedBy?: string;
    lastModifiedDate?: Date;
}

export class Customer implements ICustomer {
    constructor(
        public id?: number,
        public name?: string,
        public nip?: string,
        public address?: string,
        public postalCode?: string,
        public state?: CustomerState,
        public notes?: string,
        public city?: ICity,
        public country?: ICountry,
        public user?: IUser,
        public createdBy?: string,
        public createdDate?: Date,
        public lastModifiedBy?: string,
        public lastModifiedDate?: Date
    ) {}
}
