export interface ICountry {
    id?: number;
    countryCode?: string;
    countryNamePL?: string;
    countryNameEN?: string;
}

export class Country implements ICountry {
    constructor(public id?: number, public countryCode?: string, public countryNamePL?: string, public countryNameEN?: string) {}
}
