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
    contact?: number;
    price?: number;
    flexibility?: number;
    recommendation?: Grade;
    average?: number;
    carrierName?: string;
    carrierId?: number;
    personFirstName?: string;
    personId?: number;
    chargeAddressPostalCode?: string;
    chargeAddressId?: number;
    dischargeAddressPostalCode?: string;
    dischargeAddressId?: number;
    cargoTypeName?: string;
    cargoTypeId?: number;
}

export class Rating implements IRating {
    constructor(
        public id?: number,
        public contact?: number,
        public price?: number,
        public flexibility?: number,
        public recommendation?: Grade,
        public average?: number,
        public carrierName?: string,
        public carrierId?: number,
        public personFirstName?: string,
        public personId?: number,
        public chargeAddressPostalCode?: string,
        public chargeAddressId?: number,
        public dischargeAddressPostalCode?: string,
        public dischargeAddressId?: number,
        public cargoTypeName?: string,
        public cargoTypeId?: number
    ) {}
}
