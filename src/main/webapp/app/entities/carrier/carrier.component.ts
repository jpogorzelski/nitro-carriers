import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ICarrier } from 'app/shared/model/carrier.model';
import { AccountService } from 'app/core';
import { CarrierService } from './carrier.service';

@Component({
    selector: 'jhi-carrier',
    templateUrl: './carrier.component.html'
})
export class CarrierComponent implements OnInit, OnDestroy {
    carriers: ICarrier[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        protected carrierService: CarrierService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected accountService: AccountService
    ) {}

    loadAll() {
        this.carrierService
            .query()
            .pipe(
                filter((res: HttpResponse<ICarrier[]>) => res.ok),
                map((res: HttpResponse<ICarrier[]>) => res.body)
            )
            .subscribe(
                (res: ICarrier[]) => {
                    this.carriers = res;
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInCarriers();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: ICarrier) {
        return item.id;
    }

    registerChangeInCarriers() {
        this.eventSubscriber = this.eventManager.subscribe('carrierListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
