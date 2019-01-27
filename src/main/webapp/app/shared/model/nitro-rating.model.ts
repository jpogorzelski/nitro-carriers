export const enum Grade {
    DEF_YES = 'DEF_YES',
    YES = 'YES',
    FINE = 'FINE',
    NO = 'NO',
    DEF_NO = 'DEF_NO',
    BLACK_LIST = 'BLACK_LIST'
}

export interface INitroRating {
    id?: number;
    carrierTransId?: number;
    carrierName?: string;
    personTransId?: number;
    personFirstName?: string;
    personLastName?: string;
    chargeAddressCountry?: string;
    chargeAddressPostalCode?: string;
    dischargeAddressCountry?: string;
    dischargeAddressPostalCode?: string;
    cargoTypeName?: string;
    cargoTypeId?: number;
    contact?: number;
    price?: number;
    flexibility?: number;
    recommendation?: Grade;
    average?: number;
}

export class NitroRating implements INitroRating {
    constructor(
        public id?: number,
        public carrierTransId?: number,
        public carrierName?: string,
        public personTransId?: number,
        public personFirstName?: string,
        public personLastName?: string,
        public chargeAddressCountry?: string,
        public chargeAddressPostalCode?: string,
        public dischargeAddressCountry?: string,
        public dischargeAddressPostalCode?: string,
        public cargoTypeName?: string,
        public cargoTypeId?: number,
        public contact?: number,
        public price?: number,
        public flexibility?: number,
        public recommendation?: Grade,
        public average?: number
    ) {}
}
