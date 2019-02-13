import { Component, DoCheck, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { Grade, IRating } from 'app/shared/model/rating.model';
import { NitroRatingService } from './nitro-rating.service';
import { IPerson, Person } from 'app/shared/model/person.model';
import { ICargoType } from 'app/shared/model/cargo-type.model';
import { CargoTypeService } from 'app/entities/cargo-type';
import { Carrier, ICarrier } from 'app/shared/model/carrier.model';
import { ICountry } from 'app/shared/model/country.model';
import { CountryService } from 'app/entities/country';

@Component({
    selector: 'jhi-ext-rating-update',
    templateUrl: './nitro-rating-editor.component.html'
})
export class NitroRatingEditorComponent implements OnInit, DoCheck {
    carrierAndPerson: string;
    rating: IRating;
    isSaving: boolean;

    people: IPerson[];
    countries: ICountry[];
    cargotypes: ICargoType[];
    carriers: ICarrier[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected ratingExtService: NitroRatingService,
        protected countryService: CountryService,
        protected cargoTypeService: CargoTypeService,
        protected activatedRoute: ActivatedRoute,
        private router: Router
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ rating }) => {
            this.rating = rating;
        });

        this.countryService
            .query({ filter: 'rating-is-null' })
            .pipe(
                filter((mayBeOk: HttpResponse<ICountry[]>) => mayBeOk.ok),
                map((response: HttpResponse<ICountry[]>) => response.body)
            )
            .subscribe(
                (res: ICountry[]) => {
                    this.countries = res;
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
        this.cargoTypeService
            .query({ filter: 'rating-is-null' })
            .pipe(
                filter((mayBeOk: HttpResponse<ICargoType[]>) => mayBeOk.ok),
                map((response: HttpResponse<ICargoType[]>) => response.body)
            )
            .subscribe(
                (res: ICargoType[]) => {
                    this.cargotypes = res;
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    ngDoCheck(): void {
        if (this.rating.contact && this.rating.price && this.rating.flexibility && this.rating.recommendation) {
            const recommendation = this.getRecommendationAsInt();
            this.rating.average = (this.rating.contact + this.rating.price + this.rating.flexibility + recommendation) / 4;
        }
    }

    extractCarrierAndPerson(rating: IRating, str: string): IRating {
        const result = rating;
        const lines = str.split('\n');
        const personLineIndex = getPersonLineIndex();
        const nameAndTransId = lines[personLineIndex].split(',');
        const fullName = nameAndTransId[0].split(' ');
        const transId = nameAndTransId[1].split('-');
        result.carrier = new Carrier();
        result.carrier.name = lines[0];
        result.carrier.transId = Number(transId[0]);

        result.person = new Person();
        result.person.firstName = fullName[0];
        result.person.lastName = fullName[1];
        result.person.companyId = Number(transId[1]);
        return result;

        function getPersonLineIndex() {
            switch (lines.length) {
                case 2: // only company name line and person with transId line
                    return 1;
                case 3: // missing telephone line or address line
                    if (lines[2].startsWith('tel')) {
                        return 1;
                    } else {
                        return 2;
                    }
                case 4: // full data
                    return 2;
                default:
                    throw new Error('Bad input');
            }
        }
    }

    private getRecommendationAsInt() {
        let recommendation = 0;
        switch (this.rating.recommendation) {
            case Grade.BLACK_LIST:
                recommendation = 1;
                break;
            case Grade.DEF_NO:
                recommendation = 2;
                break;
            case Grade.NO:
                recommendation = 3;
                break;
            case Grade.FINE:
                recommendation = 4;
                break;
            case Grade.YES:
                recommendation = 5;
                break;
            case Grade.DEF_YES:
                recommendation = 6;
                break;
            default:
                break;
        }
        return recommendation;
    }

    previousState() {}

    save() {
        this.rating = this.extractCarrierAndPerson(this.rating, this.carrierAndPerson);
        this.isSaving = true;
        if (this.rating.id !== undefined) {
            this.subscribeToSaveResponse(this.ratingExtService.update(this.rating));
        } else {
            this.subscribeToSaveResponse(this.ratingExtService.create(this.rating));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IRating>>) {
        result.subscribe((res: HttpResponse<IRating>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.router.navigate(['/rating']);
    }

    protected onSaveError() {
        this.isSaving = false;
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackCountryById(index: number, item: ICountry) {
        return item.id;
    }

    trackCargoTypeById(index: number, item: ICargoType) {
        return item.id;
    }
}
