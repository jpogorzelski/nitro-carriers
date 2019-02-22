import { ICarrier } from 'app/shared/model/carrier.model';
import { IPerson } from 'app/shared/model/person.model';
import { ICountry } from 'app/shared/model/country.model';
import { IUser } from 'app/core/user/user.model';

export const enum CargoType {
    FTL_13_6 = 'FTL_13_6',
    EXTRA_13_6 = 'EXTRA_13_6',
    REEFER = 'REEFER',
    EXTRA_REEFER = 'EXTRA_REEFER',
    SOLO = 'SOLO',
    FULL_BUS_BLASZKA = 'FULL_BUS_BLASZKA',
    FULL_BUS_PLANDEKA = 'FULL_BUS_PLANDEKA',
    EXTRA_BUS = 'EXTRA_BUS',
    OVERSIZED = 'OVERSIZED',
    OTHER = 'OTHER'
}

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
    cargoType?: CargoType;
    distance?: number;
    contact?: number;
    price?: number;
    flexibility?: number;
    recommendation?: Grade;
    average?: number;
    remarks?: string;
    carrier?: ICarrier;
    person?: IPerson;
    chargeCountry?: ICountry;
    dischargeCountry?: ICountry;
    createdBy?: IUser;
}

export class Rating implements IRating {
    constructor(
        public id?: number,
        public chargePostalCode?: string,
        public dischargePostalCode?: string,
        public cargoType?: CargoType,
        public distance?: number,
        public contact?: number,
        public price?: number,
        public flexibility?: number,
        public recommendation?: Grade,
        public average?: number,
        public remarks?: string,
        public carrier?: ICarrier,
        public person?: IPerson,
        public chargeCountry?: ICountry,
        public dischargeCountry?: ICountry,
        public createdBy?: IUser
    ) {}
}
