import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IRating } from 'app/shared/model/rating.model';
import { RatingService } from './rating.service';
import { IPerson } from 'app/shared/model/person.model';
import { PersonService } from 'app/entities/person';
import { IAddress } from 'app/shared/model/address.model';
import { AddressService } from 'app/entities/address';
import { ICargoType } from 'app/shared/model/cargo-type.model';
import { CargoTypeService } from 'app/entities/cargo-type';
import { ICarrier } from 'app/shared/model/carrier.model';
import { CarrierService } from 'app/entities/carrier';

@Component({
    selector: 'jhi-rating-update',
    templateUrl: './rating-update.component.html'
})
export class RatingUpdateComponent implements OnInit {
    rating: IRating;
    isSaving: boolean;

    people: IPerson[];

    chargeaddresses: IAddress[];

    dischargeaddresses: IAddress[];

    cargotypes: ICargoType[];

    carriers: ICarrier[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected ratingService: RatingService,
        protected personService: PersonService,
        protected addressService: AddressService,
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
                    if (!this.rating.personId) {
                        this.people = res;
                    } else {
                        this.personService
                            .find(this.rating.personId)
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
        this.addressService
            .query({ filter: 'rating-is-null' })
            .pipe(
                filter((mayBeOk: HttpResponse<IAddress[]>) => mayBeOk.ok),
                map((response: HttpResponse<IAddress[]>) => response.body)
            )
            .subscribe(
                (res: IAddress[]) => {
                    if (!this.rating.chargeAddressId) {
                        this.chargeaddresses = res;
                    } else {
                        this.addressService
                            .find(this.rating.chargeAddressId)
                            .pipe(
                                filter((subResMayBeOk: HttpResponse<IAddress>) => subResMayBeOk.ok),
                                map((subResponse: HttpResponse<IAddress>) => subResponse.body)
                            )
                            .subscribe(
                                (subRes: IAddress) => (this.chargeaddresses = [subRes].concat(res)),
                                (subRes: HttpErrorResponse) => this.onError(subRes.message)
                            );
                    }
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
        this.addressService
            .query({ filter: 'rating-is-null' })
            .pipe(
                filter((mayBeOk: HttpResponse<IAddress[]>) => mayBeOk.ok),
                map((response: HttpResponse<IAddress[]>) => response.body)
            )
            .subscribe(
                (res: IAddress[]) => {
                    if (!this.rating.dischargeAddressId) {
                        this.dischargeaddresses = res;
                    } else {
                        this.addressService
                            .find(this.rating.dischargeAddressId)
                            .pipe(
                                filter((subResMayBeOk: HttpResponse<IAddress>) => subResMayBeOk.ok),
                                map((subResponse: HttpResponse<IAddress>) => subResponse.body)
                            )
                            .subscribe(
                                (subRes: IAddress) => (this.dischargeaddresses = [subRes].concat(res)),
                                (subRes: HttpErrorResponse) => this.onError(subRes.message)
                            );
                    }
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

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.rating.id !== undefined) {
            this.subscribeToSaveResponse(this.ratingService.update(this.rating));
        } else {
            this.subscribeToSaveResponse(this.ratingService.create(this.rating));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IRating>>) {
        result.subscribe((res: HttpResponse<IRating>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackAddressById(index: number, item: IAddress) {
        return item.id;
    }

    trackCargoTypeById(index: number, item: ICargoType) {
        return item.id;
    }

    trackCarrierById(index: number, item: ICarrier) {
        return item.id;
    }
}
