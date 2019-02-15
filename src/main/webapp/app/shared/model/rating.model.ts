import { ICarrier } from 'app/shared/model/carrier.model';
import { IPerson } from 'app/shared/model/person.model';
import { ICountry } from 'app/shared/model/country.model';
import { ICargoType } from 'app/shared/model/cargo-type.model';

export const enum Grade {
    DEF_YES = 'DEF_YES',
    YES = 'YES',
    FINE = 'FINE',
    NO = 'NO',
    DEF_NO = 'DEF_NO',
    BLACK_LIST = 'BLACK_LIST'
}

export interface IRating {
    id?: number;
    chargePostalCode?: string;
    dischargePostalCode?: string;
    distance?: number;
    contact?: number;
    price?: number;
    flexibility?: number;
    recommendation?: Grade;
    average?: number;
    carrier?: ICarrier;
    person?: IPerson;
    chargeCountry?: ICountry;
    dischargeCountry?: ICountry;
    cargoType?: ICargoType;
}

export class Rating implements IRating {
    constructor(
        public id?: number,
        public chargePostalCode?: string,
        public dischargePostalCode?: string,
        public distance?: number,
        public contact?: number,
        public price?: number,
        public flexibility?: number,
        public recommendation?: Grade,
        public average?: number,
        public carrier?: ICarrier,
        public person?: IPerson,
        public chargeCountry?: ICountry,
        public dischargeCountry?: ICountry,
        public cargoType?: ICargoType
    ) {}
}
