import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
    imports: [
        RouterModule.forChild([
            {
                path: 'country',
                loadChildren: './country/country.module#NitroCarriersCountryModule'
            },
            {
                path: 'address',
                loadChildren: './address/address.module#NitroCarriersAddressModule'
            },
            {
                path: 'carrier',
                loadChildren: './carrier/carrier.module#NitroCarriersCarrierModule'
            },
            {
                path: 'person',
                loadChildren: './person/person.module#NitroCarriersPersonModule'
            },
            {
                path: 'cargo-type',
                loadChildren: './cargo-type/cargo-type.module#NitroCarriersCargoTypeModule'
            },
            {
                path: 'rating',
                loadChildren: './rating/rating.module#NitroCarriersRatingModule'
            }
            /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
        ])
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class NitroCarriersEntityModule {}
