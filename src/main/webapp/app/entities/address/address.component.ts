import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IAddress } from 'app/shared/model/address.model';
import { Principal } from 'app/core';
import { AddressService } from './address.service';

@Component({
    selector: 'jhi-address',
    templateUrl: './address.component.html'
})
export class AddressComponent implements OnInit, OnDestroy {
    addresses: IAddress[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private addressService: AddressService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {}

    loadAll() {
        this.addressService.query().subscribe(
            (res: HttpResponse<IAddress[]>) => {
                this.addresses = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInAddresses();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IAddress) {
        return item.id;
    }

    registerChangeInAddresses() {
        this.eventSubscriber = this.eventManager.subscribe('addressListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
