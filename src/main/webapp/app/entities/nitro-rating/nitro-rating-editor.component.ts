import { Component, DoCheck, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { Grade, INitroRating } from 'app/shared/model/nitro-rating.model';
import { NitroRatingService } from './nitro-rating.service';
import { IPerson } from 'app/shared/model/person.model';
import { PersonService } from 'app/entities/person';
import { ICargoType } from 'app/shared/model/cargo-type.model';
import { CargoTypeService } from 'app/entities/cargo-type';
import { ICarrier } from 'app/shared/model/carrier.model';
import { CarrierService } from 'app/entities/carrier';
import { ICountry } from 'app/shared/model/country.model';
import { CountryService } from 'app/entities/country';

@Component({
    selector: 'jhi-ext-rating-update',
    templateUrl: './nitro-rating-editor.component.html'
})
export class NitroRatingEditorComponent implements OnInit, DoCheck {
    rating: INitroRating;
    isSaving: boolean;

    people: IPerson[];
    countries: ICountry[];
    cargotypes: ICargoType[];
    carriers: ICarrier[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected ratingExtService: NitroRatingService,
        protected personService: PersonService,
        protected countryService: CountryService,
        protected cargoTypeService: CargoTypeService,
        protected carrierService: CarrierService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ rating }) => {
            this.rating = rating;
        });
        this.personService
            .query({ filter: 'rating-is-null' })
            .pipe(
                filter((mayBeOk: HttpResponse<IPerson[]>) => mayBeOk.ok),
                map((response: HttpResponse<IPerson[]>) => response.body)
            )
            .subscribe(
                (res: IPerson[]) => {
                    if (!this.rating.personTransId) {
                        this.people = res;
                    } else {
                        this.personService
                            .find(this.rating.personTransId)
                            .pipe(
                                filter((subResMayBeOk: HttpResponse<IPerson>) => subResMayBeOk.ok),
                                map((subResponse: HttpResponse<IPerson>) => subResponse.body)
                            )
                            .subscribe(
                                (subRes: IPerson) => (this.people = [subRes].concat(res)),
                                (subRes: HttpErrorResponse) => this.onError(subRes.message)
                            );
                    }
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
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
                    if (!this.rating.cargoTypeId) {
                        this.cargotypes = res;
                    } else {
                        this.cargoTypeService
                            .find(this.rating.cargoTypeId)
                            .pipe(
                                filter((subResMayBeOk: HttpResponse<ICargoType>) => subResMayBeOk.ok),
                                map((subResponse: HttpResponse<ICargoType>) => subResponse.body)
                            )
                            .subscribe(
                                (subRes: ICargoType) => (this.cargotypes = [subRes].concat(res)),
                                (subRes: HttpErrorResponse) => this.onError(subRes.message)
                            );
                    }
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
        this.carrierService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<ICarrier[]>) => mayBeOk.ok),
                map((response: HttpResponse<ICarrier[]>) => response.body)
            )
            .subscribe((res: ICarrier[]) => (this.carriers = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    ngDoCheck(): void {
        if (this.rating.contact && this.rating.price && this.rating.flexibility && this.rating.recommendation) {
            let recommendation: number = 0;
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
            this.rating.average = (this.rating.contact + this.rating.price + this.rating.flexibility + recommendation) / 4;
        }
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.rating.id !== undefined) {
            this.subscribeToSaveResponse(this.ratingExtService.update(this.rating));
        } else {
            this.subscribeToSaveResponse(this.ratingExtService.create(this.rating));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<INitroRating>>) {
        result.subscribe((res: HttpResponse<INitroRating>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackPersonById(index: number, item: IPerson) {
        return item.id;
    }

    trackCountryById(index: number, item: ICountry) {
        return item.id;
    }

    trackCargoTypeById(index: number, item: ICargoType) {
        return item.id;
    }

    trackCarrierById(index: number, item: ICarrier) {
        return item.id;
    }
}
