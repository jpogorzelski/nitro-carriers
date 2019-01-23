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
    flexibiliy?: number;
    contact?: number;
    price?: number;
    recommendation?: Grade;
    average?: number;
    personId?: number;
    chargeAddressId?: number;
    dischargeAddressId?: number;
    cargoTypeId?: number;
    carrierId?: number;
}

export class Rating implements IRating {
    constructor(
        public id?: number,
        public flexibiliy?: number,
        public contact?: number,
        public price?: number,
        public recommendation?: Grade,
        public average?: number,
        public personId?: number,
        public chargeAddressId?: number,
        public dischargeAddressId?: number,
        public cargoTypeId?: number,
        public carrierId?: number
    ) {}
}
