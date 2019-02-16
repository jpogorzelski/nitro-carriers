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
                path: 'carrier',
                loadChildren: './carrier/carrier.module#NitroCarriersCarrierModule'
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
                path: 'nitro-rating',
                loadChildren: './nitro-rating/nitro-rating.module#NitroCarriersNitroRatingModule'
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
