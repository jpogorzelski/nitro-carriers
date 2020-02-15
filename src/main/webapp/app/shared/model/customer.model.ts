import { ICity } from 'app/shared/model/city.model';
import { ICountry } from 'app/shared/model/country.model';
import { IUser } from 'app/core/user/user.model';

export interface ICustomer {
    id?: number;
    name?: string;
    nip?: string;
    address?: string;
    postalCode?: string;
    city?: ICity;
    country?: ICountry;
    user?: IUser;
}

export class Customer implements ICustomer {
    constructor(
        public id?: number,
        public name?: string,
        public nip?: string,
        public address?: string,
        public postalCode?: string,
        public city?: ICity,
        public country?: ICountry,
        public user?: IUser
    ) {}
}
