import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICargoType } from 'app/shared/model/cargo-type.model';

@Component({
    selector: 'jhi-cargo-type-detail',
    templateUrl: './cargo-type-detail.component.html'
})
export class CargoTypeDetailComponent implements OnInit {
    cargoType: ICargoType;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ cargoType }) => {
            this.cargoType = cargoType;
        });
    }

    previousState() {
        window.history.back();
    }
}
