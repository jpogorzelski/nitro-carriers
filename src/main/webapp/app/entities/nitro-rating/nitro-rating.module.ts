import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { NgSelectModule } from '@ng-select/ng-select';
import { FormsModule } from '@angular/forms';

import { NitroCarriersSharedModule } from 'app/shared';
import {
    NitroRatingComponent,
    NitroRatingDetailComponent,
    NitroRatingEditorComponent,
    NitroRatingDeleteDialogComponent,
    NitroRatingDeletePopupComponent,
    NitroRatingListComponent,
    NitroRatingCustomComponent,
    nitroRatingRoute,
    nitroRatingPopupRoute
} from './';

const ENTITY_STATES = [...nitroRatingRoute, ...nitroRatingPopupRoute];

@NgModule({
    imports: [NitroCarriersSharedModule, RouterModule.forChild(ENTITY_STATES), NgSelectModule, FormsModule],
    declarations: [
        NitroRatingComponent,
        NitroRatingDetailComponent,
        NitroRatingEditorComponent,
        NitroRatingDeleteDialogComponent,
        NitroRatingDeletePopupComponent,
        NitroRatingListComponent,
        NitroRatingCustomComponent
    ],
    entryComponents: [
        NitroRatingComponent,
        NitroRatingDetailComponent,
        NitroRatingEditorComponent,
        NitroRatingDeleteDialogComponent,
        NitroRatingDeletePopupComponent,
        NitroRatingListComponent,
        NitroRatingCustomComponent
    ],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class NitroCarriersNitroRatingModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
