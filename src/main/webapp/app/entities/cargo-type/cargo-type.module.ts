import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { NitroCarriersSharedModule } from 'app/shared';
import {
    CargoTypeComponent,
    CargoTypeDetailComponent,
    CargoTypeUpdateComponent,
    CargoTypeDeletePopupComponent,
    CargoTypeDeleteDialogComponent,
    cargoTypeRoute,
    cargoTypePopupRoute
} from './';

const ENTITY_STATES = [...cargoTypeRoute, ...cargoTypePopupRoute];

@NgModule({
    imports: [NitroCarriersSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        CargoTypeComponent,
        CargoTypeDetailComponent,
        CargoTypeUpdateComponent,
        CargoTypeDeleteDialogComponent,
        CargoTypeDeletePopupComponent
    ],
    entryComponents: [CargoTypeComponent, CargoTypeUpdateComponent, CargoTypeDeleteDialogComponent, CargoTypeDeletePopupComponent],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class NitroCarriersCargoTypeModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
