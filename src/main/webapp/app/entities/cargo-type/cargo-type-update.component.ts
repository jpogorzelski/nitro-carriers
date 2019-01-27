import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { ICargoType } from 'app/shared/model/cargo-type.model';
import { CargoTypeService } from './cargo-type.service';

@Component({
    selector: 'jhi-cargo-type-update',
    templateUrl: './cargo-type-update.component.html'
})
export class CargoTypeUpdateComponent implements OnInit {
    cargoType: ICargoType;
    isSaving: boolean;

    constructor(protected cargoTypeService: CargoTypeService, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ cargoType }) => {
            this.cargoType = cargoType;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.cargoType.id !== undefined) {
            this.subscribeToSaveResponse(this.cargoTypeService.update(this.cargoType));
        } else {
            this.subscribeToSaveResponse(this.cargoTypeService.create(this.cargoType));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<ICargoType>>) {
        result.subscribe((res: HttpResponse<ICargoType>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }
}
