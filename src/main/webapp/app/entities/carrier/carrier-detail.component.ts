import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICarrier } from 'app/shared/model/carrier.model';

@Component({
    selector: 'jhi-carrier-detail',
    templateUrl: './carrier-detail.component.html'
})
export class CarrierDetailComponent implements OnInit {
    carrier: ICarrier;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ carrier }) => {
            this.carrier = carrier;
        });
    }

    previousState() {
        window.history.back();
    }
}
