import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { ICarrier } from 'app/shared/model/carrier.model';
import { CarrierService } from './carrier.service';

@Component({
    selector: 'jhi-carrier-update',
    templateUrl: './carrier-update.component.html'
})
export class CarrierUpdateComponent implements OnInit {
    carrier: ICarrier;
    isSaving: boolean;

    constructor(protected carrierService: CarrierService, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ carrier }) => {
            this.carrier = carrier;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.carrier.id !== undefined) {
            this.subscribeToSaveResponse(this.carrierService.update(this.carrier));
        } else {
            this.subscribeToSaveResponse(this.carrierService.create(this.carrier));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<ICarrier>>) {
        result.subscribe((res: HttpResponse<ICarrier>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }
}
