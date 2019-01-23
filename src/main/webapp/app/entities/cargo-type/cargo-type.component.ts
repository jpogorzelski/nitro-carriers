import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ICargoType } from 'app/shared/model/cargo-type.model';
import { Principal } from 'app/core';
import { CargoTypeService } from './cargo-type.service';

@Component({
    selector: 'jhi-cargo-type',
    templateUrl: './cargo-type.component.html'
})
export class CargoTypeComponent implements OnInit, OnDestroy {
    cargoTypes: ICargoType[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private cargoTypeService: CargoTypeService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {}

    loadAll() {
        this.cargoTypeService.query().subscribe(
            (res: HttpResponse<ICargoType[]>) => {
                this.cargoTypes = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInCargoTypes();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: ICargoType) {
        return item.id;
    }

    registerChangeInCargoTypes() {
        this.eventSubscriber = this.eventManager.subscribe('cargoTypeListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
