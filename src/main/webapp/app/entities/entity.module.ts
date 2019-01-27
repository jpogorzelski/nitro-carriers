import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { NitroCarriersCountryModule } from './country/country.module';
import { NitroCarriersAddressModule } from './address/address.module';
import { NitroCarriersCarrierModule } from './carrier/carrier.module';
import { NitroCarriersPersonModule } from './person/person.module';
import { NitroCarriersCargoTypeModule } from './cargo-type/cargo-type.module';
import { NitroCarriersRatingModule } from './rating/rating.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        NitroCarriersCountryModule,
        NitroCarriersAddressModule,
        NitroCarriersCarrierModule,
        NitroCarriersPersonModule,
        NitroCarriersCargoTypeModule,
        NitroCarriersRatingModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class NitroCarriersEntityModule {}
