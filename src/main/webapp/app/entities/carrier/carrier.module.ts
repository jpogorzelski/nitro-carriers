import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { NitroCarriersSharedModule } from 'app/shared';
import {
    CarrierComponent,
    CarrierDetailComponent,
    CarrierUpdateComponent,
    CarrierDeletePopupComponent,
    CarrierDeleteDialogComponent,
    CarrierRatingsComponent,
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
        CarrierRatingsComponent,
        CarrierDeleteDialogComponent,
        CarrierDeletePopupComponent
    ],
    entryComponents: [CarrierComponent, CarrierUpdateComponent, CarrierDeleteDialogComponent, CarrierDeletePopupComponent],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class NitroCarriersCarrierModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
