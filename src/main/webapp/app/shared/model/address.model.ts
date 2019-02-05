export interface IAddress {
    id?: number;
    postalCode?: string;
    countryCountryName?: string;
    countryId?: number;
}

export class Address implements IAddress {
    constructor(public id?: number, public postalCode?: string, public countryCountryName?: string, public countryId?: number) {}
}
