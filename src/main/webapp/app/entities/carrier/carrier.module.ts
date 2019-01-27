import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { NitroCarriersSharedModule } from 'app/shared';
import {
    CarrierComponent,
    CarrierDetailComponent,
    CarrierUpdateComponent,
    CarrierDeletePopupComponent,
    CarrierDeleteDialogComponent,
    carrierRoute,
    carrierPopupRoute
} from './';

const ENTITY_STATES = [...carrierRoute, ...carrierPopupRoute];

@NgModule({
    imports: [NitroCarriersSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        CarrierComponent,
        CarrierDetailComponent,
        CarrierUpdateComponent,
        CarrierDeleteDialogComponent,
        CarrierDeletePopupComponent
    ],
    entryComponents: [CarrierComponent, CarrierUpdateComponent, CarrierDeleteDialogComponent, CarrierDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class NitroCarriersCarrierModule {}
