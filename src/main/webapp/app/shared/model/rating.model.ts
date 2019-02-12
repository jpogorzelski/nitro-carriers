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
    contact?: number;
    price?: number;
    flexibility?: number;
    recommendation?: Grade;
    average?: number;
    carrierName?: string;
    carrierId?: number;
    personFirstName?: string;
    personId?: number;
    chargeCountryCountryName?: string;
    chargeCountryId?: number;
    dischargeCountryCountryName?: string;
    dischargeCountryId?: number;
    cargoTypeName?: string;
    cargoTypeId?: number;
}

export class Rating implements IRating {
    constructor(
        public id?: number,
        public chargePostalCode?: string,
        public dischargePostalCode?: string,
        public contact?: number,
        public price?: number,
        public flexibility?: number,
        public recommendation?: Grade,
        public average?: number,
        public carrierName?: string,
        public carrierId?: number,
        public personFirstName?: string,
        public personId?: number,
        public chargeCountryCountryName?: string,
        public chargeCountryId?: number,
        public dischargeCountryCountryName?: string,
        public dischargeCountryId?: number,
        public cargoTypeName?: string,
        public cargoTypeId?: number
    ) {}
}
