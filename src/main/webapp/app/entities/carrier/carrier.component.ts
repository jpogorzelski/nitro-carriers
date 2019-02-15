import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
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
    currentSearch: string;

    constructor(
        protected carrierService: CarrierService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected activatedRoute: ActivatedRoute,
        protected accountService: AccountService
    ) {
        this.currentSearch =
            this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search']
                ? this.activatedRoute.snapshot.params['search']
                : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.carrierService
                .search({
                    query: this.currentSearch
                })
                .pipe(
                    filter((res: HttpResponse<ICarrier[]>) => res.ok),
                    map((res: HttpResponse<ICarrier[]>) => res.body)
                )
                .subscribe((res: ICarrier[]) => (this.carriers = res), (res: HttpErrorResponse) => this.onError(res.message));
            return;
        }
        this.carrierService
            .query()
            .pipe(
                filter((res: HttpResponse<ICarrier[]>) => res.ok),
                map((res: HttpResponse<ICarrier[]>) => res.body)
            )
            .subscribe(
                (res: ICarrier[]) => {
                    this.carriers = res;
                    this.currentSearch = '';
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.currentSearch = query;
        this.loadAll();
    }

    clear() {
        this.currentSearch = '';
        this.loadAll();
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
