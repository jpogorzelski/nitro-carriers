import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
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
            },
            {
                path: 'nitro-rating',
                loadChildren: './nitro-rating/nitro-rating.module#NitroCarriersNitroRatingModule'
            },
            {
                path: 'address',
                loadChildren: './address/address.module#NitroCarriersAddressModule'
            },
            {
                path: 'person',
                loadChildren: './person/person.module#NitroCarriersPersonModule'
            },
            {
                path: 'rating',
                loadChildren: './rating/rating.module#NitroCarriersRatingModule'
            },
            {
                path: 'address',
                loadChildren: './address/address.module#NitroCarriersAddressModule'
            },
            {
                path: 'rating',
                loadChildren: './rating/rating.module#NitroCarriersRatingModule'
            },
            {
                path: 'rating',
                loadChildren: './rating/rating.module#NitroCarriersRatingModule'
            },
            {
                path: 'carrier',
                loadChildren: './carrier/carrier.module#NitroCarriersCarrierModule'
            },
            {
                path: 'rating',
                loadChildren: './rating/rating.module#NitroCarriersRatingModule'
            },
            {
                path: 'rating',
                loadChildren: './rating/rating.module#NitroCarriersRatingModule'
            },
            {
                path: 'rating',
                loadChildren: './rating/rating.module#NitroCarriersRatingModule'
            },
            {
                path: 'person',
                loadChildren: './person/person.module#NitroCarriersPersonModule'
            },
            {
                path: 'carrier',
                loadChildren: './carrier/carrier.module#NitroCarriersCarrierModule'
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
