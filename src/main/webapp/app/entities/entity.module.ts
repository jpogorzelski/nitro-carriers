import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
    imports: [
        RouterModule.forChild([
            {
                path: 'country',
                loadChildren: () => import('./country/country.module').then(m => m.NitroCarriersCountryModule)
            },
            {
                path: 'city',
                loadChildren: () => import('./city/city.module').then(m => m.NitroCarriersCityModule)
            },
            {
                path: 'carrier',
                loadChildren: () => import('./carrier/carrier.module').then(m => m.NitroCarriersCarrierModule)
            },
            {
                path: 'person',
                loadChildren: () => import('./person/person.module').then(m => m.NitroCarriersPersonModule)
            },
            {
                path: 'rating',
                loadChildren: () => import('./rating/rating.module').then(m => m.NitroCarriersRatingModule)
            },
            {
                path: 'nitro-rating',
                loadChildren: () => import('./nitro-rating/nitro-rating.module').then(m => m.NitroCarriersNitroRatingModule)
            },
            {
                path: 'customer',
                loadChildren: () => import('./customer/customer.module').then(m => m.NitroCarriersCustomerModule)
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
