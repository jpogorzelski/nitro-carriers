import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
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
        private jhiAlertService: JhiAlertService,
        private ratingService: RatingService,
        private personService: PersonService,
        private addressService: AddressService,
        private cargoTypeService: CargoTypeService,
        private carrierService: CarrierService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ rating }) => {
            this.rating = rating;
        });
        this.personService.query({ filter: 'rating-is-null' }).subscribe(
            (res: HttpResponse<IPerson[]>) => {
                if (!this.rating.personId) {
                    this.people = res.body;
                } else {
                    this.personService.find(this.rating.personId).subscribe(
                        (subRes: HttpResponse<IPerson>) => {
                            this.people = [subRes.body].concat(res.body);
                        },
                        (subRes: HttpErrorResponse) => this.onError(subRes.message)
                    );
                }
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.addressService.query({ filter: 'rating-is-null' }).subscribe(
            (res: HttpResponse<IAddress[]>) => {
                if (!this.rating.chargeAddressId) {
                    this.chargeaddresses = res.body;
                } else {
                    this.addressService.find(this.rating.chargeAddressId).subscribe(
                        (subRes: HttpResponse<IAddress>) => {
                            this.chargeaddresses = [subRes.body].concat(res.body);
                        },
                        (subRes: HttpErrorResponse) => this.onError(subRes.message)
                    );
                }
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.addressService.query({ filter: 'rating-is-null' }).subscribe(
            (res: HttpResponse<IAddress[]>) => {
                if (!this.rating.dischargeAddressId) {
                    this.dischargeaddresses = res.body;
                } else {
                    this.addressService.find(this.rating.dischargeAddressId).subscribe(
                        (subRes: HttpResponse<IAddress>) => {
                            this.dischargeaddresses = [subRes.body].concat(res.body);
                        },
                        (subRes: HttpErrorResponse) => this.onError(subRes.message)
                    );
                }
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.cargoTypeService.query({ filter: 'rating-is-null' }).subscribe(
            (res: HttpResponse<ICargoType[]>) => {
                if (!this.rating.cargoTypeId) {
                    this.cargotypes = res.body;
                } else {
                    this.cargoTypeService.find(this.rating.cargoTypeId).subscribe(
                        (subRes: HttpResponse<ICargoType>) => {
                            this.cargotypes = [subRes.body].concat(res.body);
                        },
                        (subRes: HttpErrorResponse) => this.onError(subRes.message)
                    );
                }
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.carrierService.query().subscribe(
            (res: HttpResponse<ICarrier[]>) => {
                this.carriers = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
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

    private subscribeToSaveResponse(result: Observable<HttpResponse<IRating>>) {
        result.subscribe((res: HttpResponse<IRating>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
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
