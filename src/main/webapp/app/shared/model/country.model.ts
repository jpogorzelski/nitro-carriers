import { ICity } from 'app/shared/model/city.model';

export interface ICountry {
    id?: number;
    countryCode?: string;
    countryNamePL?: string;
    countryNameEN?: string;
    cities?: ICity[];
}

export class Country implements ICountry {
    constructor(
        public id?: number,
        public countryCode?: string,
        public countryNamePL?: string,
        public countryNameEN?: string,
        public cities?: ICity[]
    ) {}
}
