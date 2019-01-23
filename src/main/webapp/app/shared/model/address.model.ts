export interface IAddress {
    id?: number;
    postalCode?: string;
    countryId?: number;
}

export class Address implements IAddress {
    constructor(public id?: number, public postalCode?: string, public countryId?: number) {}
}
